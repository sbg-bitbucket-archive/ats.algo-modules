package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class HandballMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;
    private HandballMatchIncidentType incidentSubType;

    public enum HandballMatchIncidentType {
        GOAL,
        BALL_POSESSION_SETTING,
        TWO_MINS_SUSPENSION_START,
        TWO_MINS_SUSPENSION_END,
    }

    public HandballMatchIncident() {
        super();
    }

    public HandballMatchIncident(HandballMatchIncidentType incidentSubType, int elapsedTime, TeamId teamId) {
        super();
        setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public HandballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(HandballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



    public void set(HandballMatchIncidentType incidentType, int elapsedTime, TeamId teamId) {
        this.incidentSubType = incidentType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = teamId;
    }


}
