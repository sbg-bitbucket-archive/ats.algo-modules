package ats.algo.sport.icehockey;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class IcehockeyMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private IcehockeyMatchIncidentType incidentSubType;

    public enum IcehockeyMatchIncidentType {
        GOAL,
        TWO_MINUTE_PENALTY_START,
        TWO_MINUTE_PENALTY_END,
        // DOUBLE_TWO_MINUTE_PENALTY_START,
        // DOUBLE_TWO_MINUTE_PENALTY_END,
        FIVE_MINUTE_PENALTY_START,
        FIVE_MINUTE_PENALTY_END,

    }

    public IcehockeyMatchIncident() {
        super();
    }

    public IcehockeyMatchIncident(IcehockeyMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public IcehockeyMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(IcehockeyMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void set(IcehockeyMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }



}
