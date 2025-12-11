package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

public class FixturesList {
    private long eventID;

    private List<FixturesListEntry> fixturesList;

    public FixturesList() {}

    public FixturesList(long eventId) {
        this.eventID = eventId;
        fixturesList = new ArrayList<>();
    }

    public void add(FixturesListEntry entry) {
        fixturesList.add(entry);
    }

    public long getEventID() {
        return eventID;
    }

    public List<FixturesListEntry> getFixturesList() {
        return fixturesList;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public void setFixturesList(List<FixturesListEntry> fixturesList) {
        this.fixturesList = fixturesList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
        result = prime * result + ((fixturesList == null) ? 0 : fixturesList.hashCode());
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
        FixturesList other = (FixturesList) obj;
        if (eventID != other.eventID)
            return false;
        if (fixturesList == null) {
            if (other.fixturesList != null)
                return false;
        } else if (!fixturesList.equals(other.fixturesList))
            return false;
        return true;
    }


}
