package ats.algo.sport.futsal;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class FutsalMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private FutsalMatchIncidentType incidentSubType;

    public enum FutsalMatchIncidentType {
        // SET_ELAPSED_TIME, // replaced by SET_MATCH_CLOCK
        GOAL,
        TWO_MINS_PENALTY_START, // 2 MINS
        FIVE_MINS_PENALTY_START, // 5 MINS
        TWO_MINS_PENALTY_END,
        FIVE_MINS_PENALTY_END,
    }

    public FutsalMatchIncident() {
        super();
    }

    public FutsalMatchIncident(FutsalMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public FutsalMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(FutsalMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void set(FutsalMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
