package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.baseclasses.MatchIncidentResult;

public class MatchIncidentResultCacheEntry {
    long incidentTime;
    MatchIncidentResult matchIncidentResult;

    public MatchIncidentResultCacheEntry(long incidentTime, MatchIncidentResult matchIncidentResult) {
        super();
        this.incidentTime = incidentTime;
        this.matchIncidentResult = matchIncidentResult;
    }

    public long getIncidentTime() {
        return incidentTime;
    }

    public MatchIncidentResult getMatchIncidentResult() {
        return matchIncidentResult;
    }



}
