package ats.algo.sport.outrights.server.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.sport.outrights.calcengine.core.CompetitionWarnings;
import ats.core.util.json.JsonUtil;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown = true)
public class Warnings {

    private Map<Long, CompetitionWarnings> warnings;

    public Warnings() {
        warnings = new HashMap<>();
    }

    public void put(long eventID, CompetitionWarnings competitionWarnings) {
        warnings.put(eventID, competitionWarnings);
    }

    public Map<Long, CompetitionWarnings> getWarnings() {
        return warnings;
    }

    public void setWarnings(Map<Long, CompetitionWarnings> warnings) {
        this.warnings = warnings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((warnings == null) ? 0 : warnings.hashCode());
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
        Warnings other = (Warnings) obj;
        if (warnings == null) {
            if (other.warnings != null)
                return false;
        } else if (!warnings.equals(other.warnings))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
