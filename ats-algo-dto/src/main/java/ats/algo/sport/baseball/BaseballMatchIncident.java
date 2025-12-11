package ats.algo.sport.baseball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BaseballMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private BaseballMatchIncidentType incidentSubType;

    public enum BaseballMatchIncidentType {
        BATFIRST,
        NORUNS,
        RUN1,
        RUN2,
        RUN3,
        RUN4,
        BASE0,
        BASE1,
        BASE2,
        BASE3,
        STRIKE,
        S12,
        S13,
        S23,
        S34,
        HITBYPITCH,
        SWITCH_PITCHER,
        OUT0,
        OUT,
        OUT2,
        OUT3,
        BALL,
        EXTRAS,
        OVEREND,
        MATCH_COMPLETED
    }

    public BaseballMatchIncident() {
        super();
        this.incidentSubType = null;
        this.teamId = TeamId.UNKNOWN;
    }

    public void set(BaseballMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }



    public void setBaseballMatchEventType(BaseballMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public BaseballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BaseballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseballMatchIncident other = (BaseballMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
