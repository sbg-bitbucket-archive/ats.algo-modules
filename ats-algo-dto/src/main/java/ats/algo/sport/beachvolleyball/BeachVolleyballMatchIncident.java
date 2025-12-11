package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BeachVolleyballMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;
    private BeachVolleyballMatchIncidentType incidentSubType;

    public enum BeachVolleyballMatchIncidentType {
        SERVEFIRST,
        POINTWON,
        SETWON
    }

    public BeachVolleyballMatchIncident() {
        super();
        this.incidentSubType = null;
        this.teamId = TeamId.UNKNOWN;
    }

    public void set(BeachVolleyballMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    public void setBeachVolleyballMatchEventType(BeachVolleyballMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public BeachVolleyballMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BeachVolleyballMatchIncidentType incidentSubType) {
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
        BeachVolleyballMatchIncident other = (BeachVolleyballMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
