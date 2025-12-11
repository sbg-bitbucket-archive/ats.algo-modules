package ats.algo.core.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.MatchIncident;

/**
 * defines incident types related to the Match clock
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElapsedTimeMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    /**
     * specifies the possible types of Elapsed time match incident
     * 
     * @author Geoff
     *
     */
    public enum ElapsedTimeMatchIncidentType {
        SET_MATCH_CLOCK, // used to explicitly set the elapsed time in the match
        SET_PERIOD_START, // start the next period - automatically starts the
                          // match timer
        SET_PERIOD_END, // end the current period - automatically stops the
                        // match timer
        SET_STOP_MATCH_CLOCK, // stop the independent match clock timer - for
                              // those sports where this is possible
        SET_START_MATCH_CLOCK // start the match clock timer
    }

    private ElapsedTimeMatchIncidentType incidentSubType;

    /**
     * json constructor - not for normal use
     */
    public ElapsedTimeMatchIncident() {
        super();
    }

    /**
     * Standard Constructor
     * 
     * @param incidentType
     * @param elapsedTime
     * @param injuryTimeSecs
     */
    public ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType incidentSubType, int elapsedTime) {
        super();
        this.incidentSubType = incidentSubType;
        this.elapsedTimeSecs = elapsedTime;
    }

    @Override
    public ElapsedTimeMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(ElapsedTimeMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    @Override
    public String shortDescription() {
        return super.shortDescription() + ": " + elapsedTimeSecs;
    }


}
