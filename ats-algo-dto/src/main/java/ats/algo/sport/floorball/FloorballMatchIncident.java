package ats.algo.sport.floorball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class FloorballMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private FloorballMatchIncidentType incidentSubType;

    public enum FloorballMatchIncidentType {
        // SET_ELAPSED_TIME, // replaced by SET_MATCH_CLOCK
        GOAL,
        MINOR_PENALTY_START, // 2 MINS
        MAJOR_PENALTY_START, // 5 MINS
        TEN_MINS_PENALTY_START, // 10 MINS
        MINOR_PENALTY_END,
        MAJOR_PENALTY_END,
        TEN_MINS_PENALTY_END,
    }

    public FloorballMatchIncident() {
        super();
    }

    public FloorballMatchIncident(FloorballMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public FloorballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }


    public void setIncidentSubType(FloorballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



    public void set(FloorballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
