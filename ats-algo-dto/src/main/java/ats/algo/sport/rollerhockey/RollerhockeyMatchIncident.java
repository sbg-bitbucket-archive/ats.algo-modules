package ats.algo.sport.rollerhockey;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class RollerhockeyMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private RollerhockeyMatchIncidentType incidentSubType;

    public enum RollerhockeyMatchIncidentType {
        // SET_ELAPSED_TIME, // replaced by SET_MATCH_CLOCK
        GOAL,
        TWO_MINS_PENALTY_START, // 2 MINS
        FIVE_MINS_PENALTY_START, // 5 MINS
        TWO_MINS_PENALTY_END,
        FIVE_MINS_PENALTY_END,
    }

    public RollerhockeyMatchIncident() {
        super();
    }

    public RollerhockeyMatchIncident(RollerhockeyMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }


    @Override
    public RollerhockeyMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(RollerhockeyMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void set(RollerhockeyMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
