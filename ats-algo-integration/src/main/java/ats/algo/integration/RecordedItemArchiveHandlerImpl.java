package ats.algo.integration;

import static java.nio.file.Files.readAttributes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.RecordingHeader;
import ats.core.AtsBean;
import generated.ats.betsync.dto.MatchDetails;
import generated.ats.betsync.dto.Participant;

public class RecordedItemArchiveHandlerImpl extends AtsBean implements RecordedItemArchiveHandler {

    private Set<Long> eventRecordings = Sets.newConcurrentHashSet();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String archiveLogDirectory = ".";
    private JmsTemplate archivingTemplate;
    private AtomicLong pendingTasks = new AtomicLong();
    private ObjectMapper mapper = new ObjectMapper();
    private volatile long lastCleanupTime = System.currentTimeMillis();
    private boolean archiveToJmsEnabled;
    private boolean archiveToFileEnabled;
    private int deleteArchiveAfterHrs = 24;
    private int deletedCount = 0;

    public void init() {
        info("archiveToJmsEnabled=%s, archiveToFileEnabled=%s, ", archiveToJmsEnabled, archiveToFileEnabled);
    }

    public void setDeleteArchiveAfterHrs(int deleteArchiveAfterHrs) {
        this.deleteArchiveAfterHrs = deleteArchiveAfterHrs;
    }

    public void setArchiveToJmsEnabled(boolean archiveToJmsEnabled) {
        this.archiveToJmsEnabled = archiveToJmsEnabled;
    }

    public void setArchiveToFileEnabled(boolean archiveToFileEnabled) {
        this.archiveToFileEnabled = archiveToFileEnabled;
    }

    public void setArchiveLogDirectory(String archiveLogDirectory) {
        this.archiveLogDirectory = archiveLogDirectory;
    }

    public void setArchivingTemplate(JmsTemplate archivingTemplate) {
        this.archivingTemplate = archivingTemplate;
    }

    public boolean isEnabled() {
        return archiveToJmsEnabled || archiveToFileEnabled;
    }

    @Override
    public void startArchive(TradedEvent event) {
        if (!isEnabled()) {
            return;
        }
        MatchDetails matchDetails = event.getMatchDetails();
        Long matchId = matchDetails.getMatchId();
        if (!eventRecordings.add(matchId)) {
            warn("Already archiving match %s ", matchId);
            return;
        }
        RecordingHeader recordingHeader = createRecordingHeader(event);
        if (archiveToFileEnabled) {
            save(recordingHeader);
        }
        publishToJms(recordingHeader);
    }

    @Override
    public void handleArchive(long eventId, RecordedItem recordedItem) {
        if (!isEnabled()) {
            return;
        }
        if (archiveToFileEnabled) {
            save(recordedItem);
        }
        publishToJms(recordedItem);
    }

    protected void checkCleanupTaskSchedule() {
        final long taskStarted = System.currentTimeMillis();
        if (taskStarted >= (lastCleanupTime + (60 * 60 * 1000))) {
            lastCleanupTime = taskStarted;
            pendingTasks.incrementAndGet();
            executor.submit(() -> {
                try {
                    cleanOldArchives();
                } finally {
                    info("Old archive cleanup done. %s tasks are pending", pendingTasks.decrementAndGet());
                }
            });
        }
    }

    @Override
    public void finishArchive(Long eventId) {
        boolean enabled = isEnabled();
        if (enabled && eventRecordings.remove(eventId)) {
            info("Finished archiving for event %s", eventId);
            checkCleanupTaskSchedule();
        }
    }

    private RecordingHeader createRecordingHeader(TradedEvent event) {
        RecordingHeader header = new RecordingHeader();
        MatchDetails details = event.getMatchDetails();
        header.setEventId(details.getMatchId());
        Long tier = details.getTier();
        if (tier != null) {
            header.setEventTier(tier.intValue());
        }

        header.setSportType(event.getSport());
        header.setCompetitionName(getCompetitionName(details));
        header.setRecordingStartTimeMillis(System.currentTimeMillis());

        Participant participantA = event.getParticipantA();
        if (participantA != null) {
            header.setTeamAName(participantA.getName());
            Participant participantB = event.getParticipantB();
            if (participantB != null) {
                header.setTeamBName(participantB.getName());
            }
        }

        header.setMatchFormat(event.getMatchFormat());
        return header;
    }

    protected String getCompetitionName(MatchDetails details) {
        String namePath = details.getNamePath();
        if (namePath != null && namePath.contains("|")) {
            String[] pathNameArray = namePath.split("\\|");
            return pathNameArray[pathNameArray.length - 2];
        }
        return null;
    }

    protected void cleanOldArchives() {
        try {
            info("Cleaning old archives");
            deletedCount = 0;
            Path parent = Paths.get(archiveLogDirectory);
            Files.walk(parent, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
                            .forEach(this::checkDelete);

            if (deletedCount > 0) {
                info("Deleted %s old files", deletedCount);
            } else {
                info("Nothing to clean");
            }
        } catch (IOException e) {
            error(e);
        }
    }

    protected void checkDelete(File file) {
        try {
            String name = file.getName();
            if (file.isDirectory()) {
                if (file.exists() && name.startsWith("match-")) {
                    if (file.listFiles().length == 0) {
                        info("Deleting empty dir %s", file.getAbsolutePath());
                        Files.delete(file.toPath());
                    }
                }
                return;
            }

            BasicFileAttributes attrs = readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime lastModifiedTime = attrs.lastModifiedTime();
            long hours = (System.currentTimeMillis() - lastModifiedTime.toMillis()) / (60 * 60 * 1000);
            if (hours >= deleteArchiveAfterHrs) {
                info("Deleting %s last modified %s hrs ago at %s", file.getPath(), hours, lastModifiedTime);
                Files.delete(file.toPath());
                ++deletedCount;
            }
        } catch (IOException e) {
            error(e);
        }
    }

    private void writeItemToFile(RecordedItem item) throws Exception {
        Path parentDir = ensureEventDir(item.getEventId());
        writeFile(parentDir, item.getUniqueRequestId() + ".json", item);
    }

    private void writeFile(Path parentDir, String fileName, Object dto) throws Exception {
        if (null == parentDir) {
            return;
        }
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        Path archiveFile = parentDir.resolve(fileName);
        Files.write(archiveFile, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Path ensureEventDir(long eventId) {
        Path parent = Paths.get(archiveLogDirectory, "match-" + eventId);
        if (!parent.toFile().exists()) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                error(e);
                return null;
            }
        }
        return parent;
    }

    private void writeHeadertoFile(RecordingHeader header) throws Exception {
        Path parentDir = ensureEventDir(header.getEventId());
        writeFile(parentDir, "recording-header.json", header);
    }

    protected void save(RecordingHeader header) {
        pendingTasks.incrementAndGet();
        executor.submit(() -> {
            try {
                writeHeadertoFile(header);
            } catch (Exception e) {
                error(e);
            }
            info("Recording header local file save done. %s tasks are pending", pendingTasks.decrementAndGet());
        });
    }

    protected void save(RecordedItem item) {
        pendingTasks.incrementAndGet();
        executor.submit(() -> {
            try {
                writeItemToFile(item);
            } catch (Exception e) {
                error(e);
            }
            info("Recordings local file save done. %s tasks are pending", pendingTasks.decrementAndGet());
        });
    }

    protected void publishToJms(final Serializable archiveItem) {
        if (!archiveToJmsEnabled || archivingTemplate == null) {
            return;
        }
        pendingTasks.incrementAndGet();
        executor.submit(() -> {
            try {
                while (true) {
                    try {
                        RecordedItem item = null;
                        if (archiveItem instanceof RecordedItem) {
                            item = ((RecordedItem) archiveItem);
                        }
                        if (item != null) {
                            debug("publishRecordedItemToJms %s for match %s", item.getUniqueRequestId(),
                                            item.getEventId());
                        }
                        archivingTemplate.convertAndSend(archiveItem);
                        break;
                    } catch (JmsException jme) {
                        Throwable cause = jme.getCause();
                        if (cause != null && cause instanceof ConcurrentModificationException) {
                            warn("Error in JMS sending", cause);
                            defaultPause();
                        } else {
                            error(jme);
                            return;
                        }
                    }
                }
            } finally {
                info("JMS recordedItem publish done. %s tasks are pending", pendingTasks.decrementAndGet());
            }
        });
    }

    protected void defaultPause() {
        pause(500);
    }

    protected void pause(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            error(e);
        }
    }
}
