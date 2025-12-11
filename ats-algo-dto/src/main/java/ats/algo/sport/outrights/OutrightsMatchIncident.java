package ats.algo.sport.outrights;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.ResultedMarket;
import ats.core.util.json.JsonUtil;

public class OutrightsMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;
    private OutrightsMatchIncidentType incidentSubType;
    private Map<Long, Market> inputMarkets;
    private Map<Long, ResultedMarket> inputResultedMarkets;

    public enum OutrightsMatchIncidentType {
        ALL
    }

    public OutrightsMatchIncident() {
        super();
        incidentSubType = OutrightsMatchIncidentType.ALL;
        inputMarkets = new HashMap<>();
    }

    @Override
    public OutrightsMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(OutrightsMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    public Map<Long, Market> getInputMarkets() {
        return inputMarkets;
    }

    public void setInputMarkets(Map<Long, Market> inputMarkets) {
        this.inputMarkets = inputMarkets;
    }

    public Map<Long, ResultedMarket> getInputResultedMarkets() {
        return inputResultedMarkets;
    }

    public void setInputResultedMarkets(Map<Long, ResultedMarket> inputResultedMarkets) {
        this.inputResultedMarkets = inputResultedMarkets;
    }

    public void addMarket(long eventID, Market market) {
        inputMarkets.put(eventID, market);
    }

    public void addResultedMarket(long eventID, ResultedMarket resultedMarket) {
        inputResultedMarkets.put(eventID, resultedMarket);
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + ((inputMarkets == null) ? 0 : inputMarkets.hashCode());
        result = prime * result + ((inputResultedMarkets == null) ? 0 : inputResultedMarkets.hashCode());
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
        OutrightsMatchIncident other = (OutrightsMatchIncident) obj;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (inputMarkets == null) {
            if (other.inputMarkets != null)
                return false;
        } else if (!inputMarkets.equals(other.inputMarkets))
            return false;
        if (inputResultedMarkets == null) {
            if (other.inputResultedMarkets != null)
                return false;
        } else if (!inputResultedMarkets.equals(other.inputResultedMarkets))
            return false;
        return true;
    }

}
