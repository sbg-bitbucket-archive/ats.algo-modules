package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingsList {
    private long eventID;
    private List<StandingsListEntry> standingsList;

    public StandingsList() {
        standingsList = new ArrayList<>();
    }

    public StandingsList(long eventId) {
        this();
        this.eventID = eventId;
    }


    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public List<StandingsListEntry> getStandingsList() {
        return standingsList;
    }

    public void setStandingsList(List<StandingsListEntry> standingsList) {
        this.standingsList = standingsList;
    }

    public void add(StandingsListEntry e) {
        standingsList.add(e);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((standingsList == null) ? 0 : standingsList.hashCode());
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
        StandingsList other = (StandingsList) obj;
        if (eventID != other.eventID)
            return false;
        if (standingsList == null) {
            if (other.standingsList != null)
                return false;
        } else if (!standingsList.equals(other.standingsList))
            return false;
        return true;
    }



}
