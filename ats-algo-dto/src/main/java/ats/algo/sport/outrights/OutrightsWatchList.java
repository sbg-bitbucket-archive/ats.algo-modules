package ats.algo.sport.outrights;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.core.util.json.JsonUtil;

/**
 * contains the set of markets and eventID's that the Outrights server is interested in for a a given competition
 * 
 * @author gicha
 *
 */
public class OutrightsWatchList extends MatchEngineSavedState {

    static final long serialVersionUID = 1L;

    private Map<Long, String> watchListEntries;

    public OutrightsWatchList() {
        watchListEntries = new HashMap<Long, String>();
    }

    public Map<Long, String> getWatchListEntries() {
        return watchListEntries;
    }

    public void setWatchListEntries(Map<Long, String> watchListEntries) {
        this.watchListEntries = watchListEntries;
    }

    public void addEntry(long eventId, String marketType) {
        watchListEntries.put(eventId, marketType);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((watchListEntries == null) ? 0 : watchListEntries.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        OutrightsWatchList other = (OutrightsWatchList) obj;
        if (watchListEntries == null) {
            if (other.watchListEntries != null)
                return false;
        } else if (!watchListEntries.equals(other.watchListEntries))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public String get(long key) {
        return watchListEntries.get(key);
    }

    public void put(long matchEventId, String marketType) {
        watchListEntries.put(matchEventId, marketType);
    }

}
