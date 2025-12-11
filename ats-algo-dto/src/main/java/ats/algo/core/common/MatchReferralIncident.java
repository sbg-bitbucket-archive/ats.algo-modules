package ats.algo.core.common;

import ats.algo.core.baseclasses.MatchIncident;

/**
 * defines incident types related to the referrals
 * 
 * @author Jin
 *
 */
public class MatchReferralIncident extends MatchIncident {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * referrals match incident,
     * 
     * @author Jin
     *
     */
    public enum MatchReferralIncidentType {
        VAR_POSSIBLE_REFERRAL,
        VAR_REFERRAL_CONFIRMED,
        VAR_REFERRAL_COMPLETED
    }

    private MatchReferralIncidentType incidentSubType;

    /**
     * json constructor - not for normal use
     */
    public MatchReferralIncident() {
        super();
    }

    /**
     * Standard Constructor
     * 
     * @param incidentType
     * @param elapsedTime
     */
    public MatchReferralIncident(MatchReferralIncidentType incidentSubType) {
        this(incidentSubType, 0);

    }

    /**
     * Standard Constructor
     * 
     * @param incidentType
     * @param elapsedTime
     */
    public MatchReferralIncident(MatchReferralIncidentType incidentSubType, int elapsedTime) {
        super();
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = elapsedTime;
        this.teamId = null;
    }

    public MatchReferralIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(MatchReferralIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
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
        MatchReferralIncident other = (MatchReferralIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        return true;
    }

}
