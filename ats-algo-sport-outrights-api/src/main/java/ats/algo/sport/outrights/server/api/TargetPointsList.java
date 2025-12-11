package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetPointsList {
    long eventID;
    private List<TargetPointsEntry> entries;

    public TargetPointsList() {
        entries = new ArrayList<>();
    }

    public TargetPointsList(int size) {
        entries = new ArrayList<>(size);
    }

    public TargetPointsList(long eventID, FcastStandings fcastStandings) {
        this(fcastStandings.size());
        this.eventID = eventID;
        fcastStandings.getStandings().forEach((teamID, fcastStanding) -> {
            entries.add(new TargetPointsEntry(teamID, fcastStanding.getTargetPoints()));
        });

    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }


    public List<TargetPointsEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TargetPointsEntry> entries) {
        this.entries = entries;
    }

    public void add(TargetPointsEntry entry) {
        entries.add(entry);
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
        result = prime * result + (int) (eventID ^ (eventID >>> 32));
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
        TargetPointsList other = (TargetPointsList) obj;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        if (eventID != other.eventID)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }
}
