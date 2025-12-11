package ats.algo.sport.outrights.hyperparameters;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import ats.algo.sport.outrights.server.api.TeamDataUpdate;

public class HistoryMap implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Map<LocalDateTime, HashMap<String, TeamDataUpdate>> historyMap;

    public HistoryMap() {

    }

    public HistoryMap(Map<LocalDateTime, HashMap<String, TeamDataUpdate>> historyMap2) {
        this.historyMap = historyMap2;
    }

    public Map<LocalDateTime, HashMap<String, TeamDataUpdate>> getHistoryMap() {
        return historyMap;
    }

    public void setHistoryMap(Map<LocalDateTime, HashMap<String, TeamDataUpdate>> historyMap) {
        this.historyMap = historyMap;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((historyMap == null) ? 0 : historyMap.hashCode());
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
        HistoryMap other = (HistoryMap) obj;
        if (historyMap == null) {
            if (other.historyMap != null)
                return false;
        } else if (!historyMap.equals(other.historyMap))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<LocalDateTime, HashMap<String, TeamDataUpdate>> date : historyMap.entrySet()) {
            out.append(date.getKey().toString());
            out.append('\n');
        }
        return out.toString();
    }



}
