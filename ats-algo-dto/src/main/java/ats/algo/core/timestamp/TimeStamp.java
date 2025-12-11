package ats.algo.core.timestamp;

import java.io.Serializable;
import java.util.Date;

import ats.core.util.json.JsonUtil;

/**
 * container to hold time stamps for a given MatchIncident. All time stamps are in millisecs since 1/1/1970 (i.e. Java
 * std time)
 * 
 * @author gicha
 *
 */
public class TimeStamp implements Serializable {

    private static final long serialVersionUID = 1L;

    long eventId;
    String requestId;
    long timeMatchIncidentCreated;
    long timeMatchIncidentReceivedByAlgoManager;
    long timePriceCalcRequestIssued;
    long timePriceCalcRequestReceivedByCalculationServer;
    long timePriceCalcResponseIssedByCalculationServer;
    long timePriceCalcResponseReceived;
    long timeNewMarketsSentToBetsyc;
    long timeUpdatedMarketsPublishedByAlgoManager;
    long timeMarketServerReceivedUpdatedMarkets;


    public TimeStamp() {

    }

    /**
     * 
     * @param eventId
     * @param requestId
     * @param timeMatchIncidentCreated
     * @param timeMatchIncidentReceivedByAlgoManager
     * @param timePriceCalcRequestIssued
     */
    public TimeStamp(long eventId, String requestId, long timeMatchIncidentCreated,
                    long timeMatchIncidentReceivedByAlgoManager, long timePriceCalcRequestIssued) {
        super();
        this.eventId = eventId;
        this.requestId = requestId;
        this.timeMatchIncidentCreated = timeMatchIncidentCreated;
        this.timeMatchIncidentReceivedByAlgoManager = timeMatchIncidentReceivedByAlgoManager;
        this.timePriceCalcRequestIssued = timePriceCalcRequestIssued;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getTimeMatchIncidentCreated() {
        return timeMatchIncidentCreated;
    }

    public void setTimeMatchIncidentCreated(long timeMatchIncidentCreated) {
        this.timeMatchIncidentCreated = timeMatchIncidentCreated;
    }

    public long getTimeMatchIncidentReceivedByAlgoManager() {
        return timeMatchIncidentReceivedByAlgoManager;
    }

    public void setTimeMatchIncidentReceivedByAlgoManager(long timeMatchIncidentReceivedByAlgoManager) {
        this.timeMatchIncidentReceivedByAlgoManager = timeMatchIncidentReceivedByAlgoManager;
    }

    public long getTimePriceCalcRequestIssued() {
        return timePriceCalcRequestIssued;
    }

    public void setTimePriceCalcRequestIssued(long timePriceCalcRequestIssued) {
        this.timePriceCalcRequestIssued = timePriceCalcRequestIssued;
    }

    public long getTimePriceCalcResponseReceived() {
        return timePriceCalcResponseReceived;
    }

    public void setTimePriceCalcResponseReceived(long timePriceCalcResponseReceived) {
        this.timePriceCalcResponseReceived = timePriceCalcResponseReceived;
    }

    public long getTimeUpdatedMarketsPublishedByAlgoManager() {
        return timeUpdatedMarketsPublishedByAlgoManager;
    }

    public void setTimeUpdatedMarketsPublishedByAlgoManager(long timeUpdatedMarketsPublishedByAlgoManager) {
        this.timeUpdatedMarketsPublishedByAlgoManager = timeUpdatedMarketsPublishedByAlgoManager;
    }

    public long getTimeMarketServerReceivedUpdatedMarkets() {
        return timeMarketServerReceivedUpdatedMarkets;
    }

    public void setTimeMarketServerReceivedUpdatedMarkets(long timeMarketServerReceivedUpdatedMarkets) {
        this.timeMarketServerReceivedUpdatedMarkets = timeMarketServerReceivedUpdatedMarkets;
    }

    public long getTimePriceCalcRequestReceivedByCalculationServer() {
        return timePriceCalcRequestReceivedByCalculationServer;
    }

    public void setTimePriceCalcRequestReceivedByCalculationServer(
                    long timePriceCalcRequestReceivedByCalculationServer) {
        this.timePriceCalcRequestReceivedByCalculationServer = timePriceCalcRequestReceivedByCalculationServer;
    }

    public long getTimePriceCalcResponseIssedByCalculationServer() {
        return timePriceCalcResponseIssedByCalculationServer;
    }

    public void setTimePriceCalcResponseIssedByCalculationServer(long timePriceCalcResponseIssedByCalculationServer) {
        this.timePriceCalcResponseIssedByCalculationServer = timePriceCalcResponseIssedByCalculationServer;
    }

    public long getTimeNewMarketsSentToBetsyc() {
        return timeNewMarketsSentToBetsyc;
    }

    public void setTimeNewMarketsSentToBetsyc(long timeNewMarketsSentToBetsyc) {
        this.timeNewMarketsSentToBetsyc = timeNewMarketsSentToBetsyc;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    protected Date asDate(long epoch) {
        return new Date(epoch);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
        result = prime * result + (int) (timeMarketServerReceivedUpdatedMarkets
                        ^ (timeMarketServerReceivedUpdatedMarkets >>> 32));
        result = prime * result + (int) (timeMatchIncidentCreated ^ (timeMatchIncidentCreated >>> 32));
        result = prime * result + (int) (timeMatchIncidentReceivedByAlgoManager
                        ^ (timeMatchIncidentReceivedByAlgoManager >>> 32));
        result = prime * result + (int) (timePriceCalcRequestIssued ^ (timePriceCalcRequestIssued >>> 32));
        result = prime * result + (int) (timePriceCalcRequestReceivedByCalculationServer
                        ^ (timePriceCalcRequestReceivedByCalculationServer >>> 32));
        result = prime * result + (int) (timePriceCalcResponseIssedByCalculationServer
                        ^ (timePriceCalcResponseIssedByCalculationServer >>> 32));
        result = prime * result + (int) (timePriceCalcResponseReceived ^ (timePriceCalcResponseReceived >>> 32));
        result = prime * result + (int) (timeUpdatedMarketsPublishedByAlgoManager
                        ^ (timeUpdatedMarketsPublishedByAlgoManager >>> 32));
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
        TimeStamp other = (TimeStamp) obj;
        if (eventId != other.eventId)
            return false;
        if (requestId == null) {
            if (other.requestId != null)
                return false;
        } else if (!requestId.equals(other.requestId))
            return false;
        if (timeMarketServerReceivedUpdatedMarkets != other.timeMarketServerReceivedUpdatedMarkets)
            return false;
        if (timeMatchIncidentCreated != other.timeMatchIncidentCreated)
            return false;
        if (timeMatchIncidentReceivedByAlgoManager != other.timeMatchIncidentReceivedByAlgoManager)
            return false;
        if (timePriceCalcRequestIssued != other.timePriceCalcRequestIssued)
            return false;
        if (timePriceCalcRequestReceivedByCalculationServer != other.timePriceCalcRequestReceivedByCalculationServer)
            return false;
        if (timePriceCalcResponseIssedByCalculationServer != other.timePriceCalcResponseIssedByCalculationServer)
            return false;
        if (timePriceCalcResponseReceived != other.timePriceCalcResponseReceived)
            return false;
        if (timeUpdatedMarketsPublishedByAlgoManager != other.timeUpdatedMarketsPublishedByAlgoManager)
            return false;
        return true;
    }



}
