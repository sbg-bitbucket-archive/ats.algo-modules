package ats.algo.core.recordplayback;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ats.algo.genericsupportfunctions.ZipUtil;

public class Recording implements Serializable, Iterable<RecordedItem> {

    private static final long serialVersionUID = 2L;

    private RecordingHeader recordingHeader;
    private List<RecordedItem> recordedItemList;

    public Recording() {
        recordingHeader = new RecordingHeader();
        recordedItemList = new ArrayList<RecordedItem>();
    }

    public List<RecordedItem> getRecordedItemList() {
        return recordedItemList;
    }

    public void setRecordedItemList(List<RecordedItem> recordedItemList) {
        this.recordedItemList = recordedItemList;
    }

    public RecordingHeader getRecordingHeader() {
        return recordingHeader;
    }

    public void setRecordingHeader(RecordingHeader recordingHeader) {
        this.recordingHeader = recordingHeader;
    }

    public void add(RecordedItem item) {
        recordedItemList.add(item);
    }

    @JsonIgnore
    public RecordedItem get(int i) {
        return recordedItemList.get(i);
    }

    /**
     * writes the contents of this list to file
     * 
     * @param fileName
     */
    public void writeToFile(String fileName) {
        File file = new File(fileName);
        writeToFile(file);
    }

    public void writeToFile(File file) {
        Map<String, Object> data = new HashMap<>();
        data.put(HEADER_FILE_NAME, this.recordingHeader);
        for (RecordedItem item : recordedItemList)
            data.put(item.getUniqueRequestId(), item);
        try {
            ZipUtil.save(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a file containing the supplied object in json format to a zip file with name "match-<eventId>.zip" in the
     * specified archiveDirectory. If the zip file does not exist then it gets created
     *
     * @param archiveDirectory
     * @param eventId
     * @param fileName Name of the file to create in the zip file
     * @param dto
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static void writeItemToFile(String archiveDirectory, long eventId, String fileName, Object dto)
                    throws JsonProcessingException, IOException {
        String matchId = "match-" + eventId;
        try (FileSystem zipfs = getMatchZipFile(archiveDirectory, matchId)) {
            Path path = zipfs.getPath(fileName);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
            byte[] data = json.getBytes();
            Files.write(path, data, StandardOpenOption.CREATE);
        }

    }

    private static FileSystem getMatchZipFile(String archiveDirectory, String matchId) {
        Map<String, String> env = new HashMap<>();
        env.put("create", "false");
        String zipFilePathName = archiveDirectory + "/" + matchId + ".zip";
        FileSystem zipfs = null;
        URI uri = URI.create("jar:file:///" + zipFilePathName);
        boolean found = false;
        try {
            zipfs = FileSystems.newFileSystem(uri, env);
            found = true;

        } catch (FileSystemNotFoundException | IOException e) {
            /*
             * zip file does not exist yet so create it
             */
        }
        if (!found) {
            env.put("create", "true");
            try {
                zipfs = FileSystems.newFileSystem(uri, env, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zipfs;

    }

    public String writeToString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Recording readFromFile(String fileName) throws JsonParseException, JsonMappingException, IOException {
        File file = new File(fileName);
        return readFromFile(file);
    }

    private static final String HEADER_FILE_NAME = "recording-header";

    public static Recording readFromFile(File file) throws JsonParseException, JsonMappingException, IOException {
        Map<String, Object> data = ZipUtil.load(file.getAbsolutePath(), HEADER_FILE_NAME, RecordingHeader.class,
                        RecordedItem.class);
        Recording recording = new Recording();
        recording.setRecordingHeader((RecordingHeader) data.get(HEADER_FILE_NAME));
        List<RecordedItem> list = data.entrySet().stream().filter(e -> !e.getKey().equals(HEADER_FILE_NAME))
                        .map(Entry::getValue).map(o -> (RecordedItem) o)
                        .sorted(Comparator.comparing(RecordedItem::getTimeRequestIssued)).collect(Collectors.toList());
        recording.setRecordedItemList(list);
        return recording;

    }

    public static Recording readFromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        Recording rpList = null;
        try {
            rpList = mapper.readValue(json, Recording.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rpList;
    }

    @Override
    public Iterator<RecordedItem> iterator() {
        return recordedItemList.iterator();
    }

    public int size() {
        return recordedItemList.size();
    }

    public void removeLastItem() {
        recordedItemList.remove(recordedItemList.size() - 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((recordedItemList == null) ? 0 : recordedItemList.hashCode());
        result = prime * result + ((recordingHeader == null) ? 0 : recordingHeader.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recording other = (Recording) obj;
        if (recordedItemList == null) {
            if (other.recordedItemList != null)
                return false;
        } else if (!recordedItemList.equals(other.recordedItemList))
            return false;
        if (recordingHeader == null) {
            if (other.recordingHeader != null)
                return false;
        } else if (!recordingHeader.equals(other.recordingHeader))
            return false;
        return true;
    }

}
