package ats.algo.sport.outrights.server.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultedMarketsList {

    private long eventId;
    private List<ResultedMarketListEntry> entries;

    public ResultedMarketsList() {

    }

    public ResultedMarketsList(long eventId) {
        super();
        this.eventId = eventId;
        entries = new ArrayList<>();
    }

    public long getEventId() {
        return eventId;
    }

    public List<ResultedMarketListEntry> getEntries() {
        return entries;
    }

    public void add(ResultedMarketListEntry entry) {
        entries.add(entry);
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setEntries(List<ResultedMarketListEntry> entries) {
        this.entries = entries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entries == null) ? 0 : entries.hashCode());
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
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
        ResultedMarketsList other = (ResultedMarketsList) obj;
        if (entries == null) {
            if (other.entries != null)
                return false;
        } else if (!entries.equals(other.entries))
            return false;
        if (eventId != other.eventId)
            return false;
        return true;
    }


}
