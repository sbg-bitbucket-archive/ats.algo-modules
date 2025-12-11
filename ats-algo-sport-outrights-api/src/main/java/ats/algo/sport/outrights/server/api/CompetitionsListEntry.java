package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionsListEntry {

    private long eventID;
    private String name;
    private String atsCompetitionID;

    public CompetitionsListEntry() {}

    public CompetitionsListEntry(long eventId, String name, String atsCompetitionId) {
        super();
        this.eventID = eventId;
        this.name = name;
        this.atsCompetitionID = atsCompetitionId;
    }

    public long getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getAtsCompetitionID() {
        return atsCompetitionID;
    }

    public void setAtsCompetitionID(String atsCompetitionId) {
        this.atsCompetitionID = atsCompetitionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((atsCompetitionID == null) ? 0 : atsCompetitionID.hashCode());
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        CompetitionsListEntry other = (CompetitionsListEntry) obj;
        if (atsCompetitionID == null) {
            if (other.atsCompetitionID != null)
                return false;
        } else if (!atsCompetitionID.equals(other.atsCompetitionID))
            return false;
        if (eventID != other.eventID)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
