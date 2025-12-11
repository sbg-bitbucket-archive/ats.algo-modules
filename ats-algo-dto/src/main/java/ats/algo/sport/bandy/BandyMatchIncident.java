package ats.algo.sport.bandy;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BandyMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private BandyMatchIncidentType incidentSubType;

    public enum BandyMatchIncidentType {
        GOAL,
        MAJOR_PENALTY_START, // 5 MINS
        TEN_MINS_PENALTY_START, // 10 MINS
        MINOR_PENALTY_END,
        MAJOR_PENALTY_END,
        TEN_MINS_PENALTY_END,
    }

    public BandyMatchIncident() {
        super();
    }

    public BandyMatchIncident(BandyMatchIncidentType eventType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = eventType;
        this.teamId = teamId;
    }

    @Override
    public BandyMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BandyMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



    public void set(BandyMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
