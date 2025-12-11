package ats.algo.core.matchresult;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import ats.core.util.json.JsonUtil;

public class MatchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> map;

    public MatchResult() {
        map = new LinkedHashMap<>();
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((map == null) ? 0 : map.hashCode());
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
        MatchResult other = (MatchResult) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        return true;
    }



}
