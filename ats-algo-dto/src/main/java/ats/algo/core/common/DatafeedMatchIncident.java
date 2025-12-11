package ats.algo.core.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.MatchIncident;

/**
 * specifies non sport-specific incidents
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatafeedMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    /**
     * Check status of feed variable
     *
     * @param incidentType
     */
    public static Boolean isFeedStateActive(DatafeedMatchIncidentType datafeedMatchIncidentType){
        switch (datafeedMatchIncidentType){
            case OK:
            case BET_START:
                return true;
            case BET_STOP:
            case CANCELLED_COVERAGE:
            case SCOUT_DISCONNECT:
                return false;
        }
        return null;
    }

    /**
     * defines the possible types of datafeed incident
     * 
     * @author Geoff
     *
     */
    public enum DatafeedMatchIncidentType {
        SCOUT_DISCONNECT,
        CANCELLED_COVERAGE,
        BET_STOP,
        BET_START,
        OK
    }

    private DatafeedMatchIncidentType incidentSubType;

    /**
     * Constructor required by json - not for normal use
     */
    public DatafeedMatchIncident() {}

    /**
     * Constructor for all datafeed match incident types
     * 
     * @param incidentType
     * @param elapsedTime
     * @param injuryTimeSecs
     */
    public DatafeedMatchIncident(DatafeedMatchIncidentType incidentSubType, int elapsedTime) {
        super();
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = elapsedTime;
    }

    /**
     * gets the type of this incident
     */
    @Override
    public DatafeedMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    /**
     * sets the type of this incident
     * 
     * @param incidentType
     */

    public void setIncidentSubType(DatafeedMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

}
