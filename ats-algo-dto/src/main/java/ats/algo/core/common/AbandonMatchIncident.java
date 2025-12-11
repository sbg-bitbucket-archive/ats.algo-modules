package ats.algo.core.common;

import ats.algo.core.baseclasses.MatchIncident;

/**
 * defines incident types related to the abandon match
 * 
 * @author Jin
 *
 */
public class AbandonMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    /**
     * Abandon match incident,
     * 
     * @author Jin
     *
     */
    public enum AbandonMatchIncidentType {
        // ABANDON_MATCH // currently this is the only incident type for this class

        // NORMALLY, //NORMALLY MEANS MATCH IS COMPLETED NORMALLY
        RETIREMENT,
        WALKOVER,
        DISQUALIFICATION,
        INJURY,
        CANCELLATION
    }

    private AbandonMatchIncidentType incidentSubType;

    /**
     * json constructor - not for normal use
     */
    public AbandonMatchIncident() {
        super();
    }


    /**
     * 
     * @param incidentSubType
     * @param teamId
     */
    public AbandonMatchIncident(AbandonMatchIncidentType incidentSubType, TeamId teamId) {
        super();
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
    }

    /**
     * @param incidentSubType
     */
    public AbandonMatchIncident(AbandonMatchIncidentType incidentSubType) {
        this(incidentSubType, null);
    }

    @Override
    public AbandonMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(AbandonMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }



}
