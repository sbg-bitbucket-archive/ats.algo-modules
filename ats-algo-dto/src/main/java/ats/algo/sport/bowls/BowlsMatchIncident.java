package ats.algo.sport.bowls;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class BowlsMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;
    private BowlsMatchIncidentType incidentSubType;

    public enum BowlsMatchIncidentType {
        SERVEFIRST,
        SERVENOW,
        ENDWON1,
        ENDWON2,
        ENDWON3,
        ENDWON4,
        SETWON,
        EXTRAEND
    }

    public BowlsMatchIncident() {
        super();
        this.incidentSubType = null;
        this.teamId = TeamId.UNKNOWN;
    }



    public void set(BowlsMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    public void setBowlsMatchEventType(BowlsMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    @Override
    public BowlsMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(BowlsMatchIncidentType incidentSubType) {
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
        BowlsMatchIncident other = (BowlsMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
