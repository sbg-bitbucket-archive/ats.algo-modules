package ats.algo.core.recordplayback;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.Time;

public class RecordingHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    private SupportedSportType sportType;
    private long eventId;
    private long recordingStartTimeMillis;
    private String competitionName;
    private String teamAName;
    private String teamBName;
    private Integer eventTier;
    private MatchFormat matchFormat;

    public RecordingHeader() {}

    public RecordingHeader(SupportedSportType sportType, long eventId, long recordingStartTimeMillis,
                    String competitionName, String teamAName, String teamBName, Integer eventTier,
                    MatchFormat matchFormat) {
        super();
        this.sportType = sportType;
        this.eventId = eventId;
        this.recordingStartTimeMillis = recordingStartTimeMillis;
        this.competitionName = competitionName;
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.eventTier = eventTier;
        this.matchFormat = matchFormat;
    }

    public SupportedSportType getSportType() {
        return sportType;
    }

    public void setSportType(SupportedSportType sportType) {
        this.sportType = sportType;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getRecordingStartTimeMillis() {
        return recordingStartTimeMillis;
    }

    public void setRecordingStartTimeMillis(long recordingStartTimeMillis) {
        this.recordingStartTimeMillis = recordingStartTimeMillis;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public Integer getEventTier() {
        return eventTier;
    }

    public void setEventTier(Integer eventTier) {
        this.eventTier = eventTier;
    }

    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(MatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    /**
     * Adds a file containing the header to a recording file with name of the format "match-<eventId>"
     * 
     * @param archiveDirectory
     * @throws JsonProcessingException
     * @throws IOException
     */
    public void writeHeaderToFile(String archiveDirectory) throws JsonProcessingException, IOException {
        Recording.writeItemToFile(archiveDirectory, eventId, "recording-header.json", this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitionName == null) ? 0 : competitionName.hashCode());
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((eventTier == null) ? 0 : eventTier.hashCode());
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + (int) (recordingStartTimeMillis ^ (recordingStartTimeMillis >>> 32));
        result = prime * result + ((sportType == null) ? 0 : sportType.hashCode());
        result = prime * result + ((teamAName == null) ? 0 : teamAName.hashCode());
        result = prime * result + ((teamBName == null) ? 0 : teamBName.hashCode());
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
        RecordingHeader other = (RecordingHeader) obj;
        if (competitionName == null) {
            if (other.competitionName != null)
                return false;
        } else if (!competitionName.equals(other.competitionName))
            return false;
        if (eventId != other.eventId)
            return false;
        if (eventTier == null) {
            if (other.eventTier != null)
                return false;
        } else if (!eventTier.equals(other.eventTier))
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
        if (recordingStartTimeMillis != other.recordingStartTimeMillis)
            return false;
        if (sportType != other.sportType)
            return false;
        if (teamAName == null) {
            if (other.teamAName != null)
                return false;
        } else if (!teamAName.equals(other.teamAName))
            return false;
        if (teamBName == null) {
            if (other.teamBName != null)
                return false;
        } else if (!teamBName.equals(other.teamBName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RecordingHeader [sportType=" + sportType + ", eventId=" + eventId + ", recordingStartTimeMillis="
                        + recordingStartTimeMillis + ", competitionName=" + competitionName + ", teamAName=" + teamAName
                        + ", teamBName=" + teamBName + ", eventTier=" + eventTier + ", matchFormat=" + matchFormat
                        + "]";
    }

    public String toMultiLineString() {
        String startTime = Time.getDateAsString(recordingStartTimeMillis);
        return "eventId=" + eventId + "\nsportType=" + sportType + "\nrecordingStartTime=" + startTime
                        + "\ncompetitionName=" + competitionName + "\nteamAName=" + teamAName + "\nteamBName="
                        + teamBName + "\nmatchFormat=[" + matchFormat + "]";
    }



}
