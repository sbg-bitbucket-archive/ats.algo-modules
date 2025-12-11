package ats.algo.sport.badminton;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BadmintonMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;
    private BadmintonMatchIncidentType incidentSubType;

    public enum BadmintonMatchIncidentType {
        SERVEFIRST,
        POINTWON,
        GAMEWON,
    }

    public BadmintonMatchIncident() {
        super();
        set(null, TeamId.UNKNOWN);
    }

    public void set(BadmintonMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }



    @Override
    public BadmintonMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BadmintonMatchIncidentType eventType) {
        this.incidentSubType = eventType;
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
        BadmintonMatchIncident other = (BadmintonMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }


}
