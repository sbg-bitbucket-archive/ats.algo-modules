package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class VolleyballMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;
    private VolleyballMatchIncidentType incidentSubType;

    public enum VolleyballMatchIncidentType {
        SERVEFIRST,
        SERVEFINALSET,
        POINTWON,
        SETWON

    }

    public VolleyballMatchIncident() {
        super();
        this.incidentSubType = null;
        this.teamId = TeamId.UNKNOWN;
    }

    public void set(VolleyballMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void set(VolleyballMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public VolleyballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(VolleyballMatchIncidentType incidentSubType) {
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
        VolleyballMatchIncident other = (VolleyballMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
