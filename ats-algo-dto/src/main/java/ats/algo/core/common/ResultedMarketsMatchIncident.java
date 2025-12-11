package ats.algo.core.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.markets.ResultedMarkets;

/**
 * specifies non sport-specific incidents
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultedMarketsMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    /**
     * defines the possible types of ResultedMarkets incident. At the moment there is only one
     * 
     * @author Geoff
     *
     */
    public enum ResultedMarketsIncidentType {
        RESULTED_MARKETS
    }

    private ResultedMarketsIncidentType incidentSubType;
    private ResultedMarkets resultedMarkets;

    /**
     * Constructor required by json - not for normal use
     */
    public ResultedMarketsMatchIncident() {
        super();
        incidentSubType = ResultedMarketsIncidentType.RESULTED_MARKETS;
    }

    /**
     * Constructor for all datafeed match incident types
     * 
     * @param incidentType
     * @param elapsedTime
     * @param injuryTimeSecs
     */
    public ResultedMarketsMatchIncident(ResultedMarkets resultedMarkets) {
        this();
        this.resultedMarkets = resultedMarkets;
    }

    /**
     * gets the type of this incident
     */
    @Override
    public ResultedMarketsIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    /**
     * sets the type of this incident
     * 
     * @param incidentType
     */

    public void setIncidentSubType(ResultedMarketsIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public ResultedMarkets getResultedMarkets() {
        return resultedMarkets;
    }

    public void setResultedMarkets(ResultedMarkets resultedMarkets) {
        this.resultedMarkets = resultedMarkets;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + ((resultedMarkets == null) ? 0 : resultedMarkets.hashCode());
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
        ResultedMarketsMatchIncident other = (ResultedMarketsMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (resultedMarkets == null) {
            if (other.resultedMarkets != null)
                return false;
        } else if (!resultedMarkets.equals(other.resultedMarkets))
            return false;
        return true;
    }



}
