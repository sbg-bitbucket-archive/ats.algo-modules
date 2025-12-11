package ats.algo.core.matchresult;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Holds the data necessary to result all the markets which get created pre-match. The interpretation of what is in the
 * map is sport specific
 * 
 * @author Geoff
 *
 */
public class MatchResultMap implements Serializable {

    private static final long serialVersionUID = 1L;
    private long eventId;
    private String requestId;
    private String userId;
    private Map<String, MatchResultElement> map;


    public MatchResultMap() {
        map = new LinkedHashMap<String, MatchResultElement>();

    }

    public Map<String, MatchResultElement> getMap() {
        return map;
    }

    public void setMap(Map<String, MatchResultElement> map) {
        this.map = map;
    }

    @JsonIgnore
    public MatchResult extractMatchResult() {
        MatchResult matchResult = new MatchResult();
        Map<String, String> ssmap = matchResult.getMap();
        for (Entry<String, MatchResultElement> e : map.entrySet()) {
            ssmap.put(e.getKey(), e.getValue().getValue().toString());
        }
        return matchResult;
    }

    @JsonIgnore
    public String setFromMap(Map<String, String> resultMap) {
        Map<String, MatchResultElement> map1 = new LinkedHashMap<String, MatchResultElement>();
        for (Entry<String, String> e : resultMap.entrySet()) {
            MatchResultElement mre = new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 0, "0");
            mre.setValue(e.getValue());
            map1.put(e.getKey(), mre);
        }
        this.map = map1;
        return null;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * user id of the person doing the manual resulting
     * 
     * @return
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.map == null) ? 0 : this.map.hashCode());
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
        MatchResultMap other = (MatchResultMap) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        return true;
    }


    @Override
    public String toString() {
        String s = "MatchResult: [\n";
        for (Entry<String, MatchResultElement> e : map.entrySet()) {
            s += "  " + e.getKey() + ": " + e.getValue().toString() + "\n";
        }
        s += "]";
        return s;
    }


    public String toShortString() {
        String s = "MatchResult: [";
        for (Entry<String, MatchResultElement> e : map.entrySet()) {
            s += e.getKey() + ": " + e.getValue().getValue() + " ";
        }
        s += "]";
        return s;
    }

    public void put(String property, MatchResultElement matchResultEntry) {
        map.put(property, matchResultEntry);

    }

}
