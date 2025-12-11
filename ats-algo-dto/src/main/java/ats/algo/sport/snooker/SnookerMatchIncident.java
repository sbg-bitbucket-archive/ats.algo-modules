package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;

public class SnookerMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    public enum SnookerMatchIncidentType {
        SERVEFIRST,
        WHOISATTABLE,
        REDPOT,
        YELLOWPOT,
        GREENPOT,
        BROWNPOT,
        BLUEPOT,
        PINKPOT,
        BLACKPOT,
        FRAMEWON
    }

    private SnookerMatchIncidentType incidentSubType;
    private int injuryTimeSecs;

    public SnookerMatchIncident() {
        super();
    }

    void set(SnookerMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
        this.injuryTimeSecs = 0;
        this.teamId = TeamId.UNKNOWN;
    }

    void set(SnookerMatchIncidentType incidentSubType, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.injuryTimeSecs = 0;
        this.teamId = teamId;
    }

    void setSnookerMatchEventType(SnookerMatchIncidentType incidentSubType, int injuryTimeSecs, TeamId teamId) {
        this.incidentSubType = incidentSubType;
        this.injuryTimeSecs = injuryTimeSecs;
        this.teamId = teamId;
    }

    @Override
    public SnookerMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }


    public void setIncidentSubType(SnookerMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public void setInjuryTimeSecs(int injuryTimeSecs) {
        this.injuryTimeSecs = injuryTimeSecs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + injuryTimeSecs;
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
        SnookerMatchIncident other = (SnookerMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (injuryTimeSecs != other.injuryTimeSecs)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
