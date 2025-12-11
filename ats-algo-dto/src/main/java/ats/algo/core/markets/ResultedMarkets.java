package ats.algo.core.markets;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import ats.core.util.json.JsonUtil;

import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * holds the set of markets that have already been resulted for a particular event
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultedMarkets implements Iterable<ResultedMarket>, Serializable {


    private static final long serialVersionUID = 1L;

    private String incidentId;
    private long timeStamp;


    /**
     * gets a tag which can be used by ATS to match up requests/rseponses
     * 
     * @return
     */
    public String getIncidentId() {
        return incidentId;
    }

    /**
     * sets a tag which can be used by ATS to match up requests/rseponses
     * 
     * @param id
     */
    public void setIncidentId(String id) {
        this.incidentId = id;
    }

    /**
     * gets the time that this set of markets was resulted
     * 
     * @return
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * sets the time that this set of markets was resulted
     * 
     * @return
     */
    public void setTimeStamp(long timeMillis) {
        this.timeStamp = timeMillis;
    }

    private Map<String, ResultedMarket> resultedMarkets;

    /**
     * class constructor
     */
    public ResultedMarkets() {
        resultedMarkets = new TreeMap<String, ResultedMarket>();
        this.timeStamp = System.currentTimeMillis();
    }

    /**
     * get the resulted markets as a map. The keys are the combination of market type and sequenceId
     * 
     * @return
     */
    public Map<String, ResultedMarket> getResultedMarkets() {
        return resultedMarkets;
    }

    @JsonIgnore
    public Map<String, ResultedMarket> getFullyResultedMarkets() {
        Map<String, ResultedMarket> fullyResulted = new TreeMap<String, ResultedMarket>();
        for (Map.Entry<String, ResultedMarket> marketEntry : resultedMarkets.entrySet()) {
            if (marketEntry.getValue().isFullyResulted())
                fullyResulted.put(marketEntry.getKey(), marketEntry.getValue());
        }
        return fullyResulted;
    }

    /**
     * set the resulted markets as a map. The keys are the combination of market type and sequenceId
     * 
     * @param resultedMarkets
     */
    public void setResultedMarkets(Map<String, ResultedMarket> resultedMarkets) {
        this.resultedMarkets = resultedMarkets;
    }

    /**
     * adds a market to the set of markets, generating the unique key
     * 
     * @param market
     */
    public void addMarket(ResultedMarket market) {
        resultedMarkets.put(market.getFullKey(), market);
    }

    @JsonIgnore
    public ResultedMarket get(String marketType) {
        return get(marketType, "M");
    }

    /**
     * gets market for specified marketType and sequenceId (may be for any lineId if OVUN or HANDICAP type market)
     * 
     * @param marketType
     * @param sequenceId
     * @return
     */
    @JsonIgnore
    public ResultedMarket get(String marketType, String sequenceId) {
        for (ResultedMarket resultedMarket : resultedMarkets.values()) {
            if (resultedMarket.getType().equals(marketType) && resultedMarket.getSequenceId().equals(sequenceId))
                return resultedMarket;
        }
        return null;
    }

    @JsonIgnore
    public ResultedMarket get(String marketType, String lineId, String sequenceId) {
        String key = marketType + "#" + lineId + "_" + sequenceId;
        return resultedMarkets.get(key);
    }



    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // String s = "ResultedMarkets [eventId=" + eventId + ", id=" + requestId + ", timeStamp=" + timeStamp
        // + ", resultedMarkets=";
        // for (Entry<String, ResultedMarket> e : resultedMarkets.entrySet())
        // s += "\n" + e.getKey() + ": " + e.getValue().toString();
        // s += "]";
        // return s;
    }

    /**
     * makes a copy of itself
     * 
     * @return
     */
    public ResultedMarkets copy() {
        ResultedMarkets cc = new ResultedMarkets();
        cc.setIncidentId(this.getIncidentId());
        cc.setTimeStamp(timeStamp);
        for (ResultedMarket market : this) {
            cc.addMarket(market);
        }
        return cc;
    }

    @Override
    public Iterator<ResultedMarket> iterator() {
        return resultedMarkets.values().iterator();
    }

    public int size() {
        return resultedMarkets.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incidentId == null) ? 0 : incidentId.hashCode());
        result = prime * result + ((resultedMarkets == null) ? 0 : resultedMarkets.hashCode());
        result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResultedMarkets other = (ResultedMarkets) obj;
        if (incidentId == null) {
            if (other.incidentId != null)
                return false;
        } else if (!incidentId.equals(other.incidentId))
            return false;
        if (resultedMarkets == null) {
            if (other.resultedMarkets != null)
                return false;
        } else if (!resultedMarkets.equals(other.resultedMarkets))
            return false;
        if (timeStamp != other.timeStamp)
            return false;
        return true;
    }

}
