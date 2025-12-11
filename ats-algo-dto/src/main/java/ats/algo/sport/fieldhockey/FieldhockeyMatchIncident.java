package ats.algo.sport.fieldhockey;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class FieldhockeyMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private FieldhockeyMatchIncidentType incidentSubType;

    public enum FieldhockeyMatchIncidentType {
        // SET_ELAPSED_TIME, // replaced by SET_MATCH_CLOCK
        GOAL,
        TWO_MINS_PENALTY_START, // 2 MINS
        FIVE_MINS_PENALTY_START, // 5 MINS
        TWO_MINS_PENALTY_END,
        FIVE_MINS_PENALTY_END,
    }

    public FieldhockeyMatchIncident() {
        super();
    }

    public FieldhockeyMatchIncident(FieldhockeyMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public FieldhockeyMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(FieldhockeyMatchIncidentType incidentType) {
        this.incidentSubType = incidentType;
    }



    public void set(FieldhockeyMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
