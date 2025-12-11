package ats.algo.sport.outrights.server.api;

public class CompetitionsListEntry {

    private long eventID;
    private String name;

    public CompetitionsListEntry() {}

    public CompetitionsListEntry(long eventId, String name) {
        super();
        this.eventID = eventId;
        this.name = name;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        if (eventID != other.eventID)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }



}
