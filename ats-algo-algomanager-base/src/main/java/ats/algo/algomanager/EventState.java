package ats.algo.algomanager;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsAwaitingResult;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.genericsupportfunctions.CopySerializableObject;


/*
 * this class should not be used outsideAlgoManager. Use EventStateBlob instead
 */
public class EventState extends EventStateBlob implements Serializable {

    /**
     * 
     */

    private static final long serialVersionUID = 1L;
    private GenericMatchParams matchParams;
    private MatchState matchState;
    private MatchState previousMatchState;
    private MatchIncident matchIncident;
    private MatchIncidentResult matchIncidentResult;
    private MatchEngineSavedState matchEngineSavedState;
    private Markets markets; // base markets only
    private Set<String> publishedMarketKeys; // complete list of base+derived market keys
    private MarketsAwaitingResult marketsAwaitingResult;
    private ResultedMarkets resultedMarkets;
    private boolean isCompleted;
    private Date timeCompleted;
    private long timeStampMillis;
    private boolean resultedMarketsPublished;
    private Set<String> deltaKeySet;
    private TimeStamp lastTimeStamp;
    private PublishMarketsManager publishMarketsManager;

    private EventState() {
        this.isCompleted = false;
        this.timeCompleted = null;
    }

    public EventState(MatchState matchState, GenericMatchParams matchParams) {
        this();
        this.matchState = matchState;
        this.matchParams = matchParams;
        this.previousMatchState = matchState.copy();
        this.markets = new Markets();
        this.marketsAwaitingResult = new MarketsAwaitingResult();
        this.resultedMarkets = new ResultedMarkets();
        this.publishedMarketKeys = new HashSet<String>();
        this.timeStampMillis = System.currentTimeMillis();
        this.publishMarketsManager = new PublishMarketsManager();
    }

    public GenericMatchParams getMatchParams() {
        return matchParams;
    }

    @JsonIgnore
    public boolean undoable() {
        if (matchIncident == null || matchIncident.getSourceSystem() == null)
            return true;
        else if (matchIncident.getSourceSystem().toLowerCase().equals("abelson"))
            return false;
        else
            return true;
    }

    public void setMatchParams(GenericMatchParams matchParams) {
        this.matchParams = matchParams;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }


    public MatchState getPreviousMatchState() {
        return previousMatchState;
    }

    public void setPreviousMatchState(MatchState previousMatchState) {
        this.previousMatchState = previousMatchState;
    }

    public MatchIncident getMatchIncident() {
        return matchIncident;
    }

    public void setMatchIncident(MatchIncident matchIncident) {
        this.matchIncident = matchIncident;
    }

    public MatchIncidentResult getMatchIncidentResult() {
        return matchIncidentResult;
    }

    public void setMatchIncidentResult(MatchIncidentResult matchIncidentResult) {
        this.matchIncidentResult = matchIncidentResult;
    }

    public MatchEngineSavedState getMatchEngineSavedState() {
        return matchEngineSavedState;
    }

    public void setMatchEngineSavedState(MatchEngineSavedState matchEngineSavedState) {
        this.matchEngineSavedState = matchEngineSavedState;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }



    public boolean isResultedMarketsPublished() {
        return resultedMarketsPublished;
    }

    public void setResultedMarketsPublished(boolean resultedMarketsPublished) {
        this.resultedMarketsPublished = resultedMarketsPublished;
    }


    /**
     * gets the list of base Markets. Does not include derived markets. The map of markets uses short keys
     * 
     * @return
     */
    public Markets getMarkets() {
        return markets;
    }

    /**
     * sets the list of base Markets. Does not include derived markets. The map of markets uses short keys
     * 
     * @param markets
     */
    public void setMarkets(Markets markets) {
        this.markets = markets;
    }

    /**
     * gets the list of the full keys associated with the most recently published set of markets (including all derived
     * markets)
     * 
     * @return
     */
    public Set<String> getPublishedMarketKeys() {
        return publishedMarketKeys;
    }

    /**
     * sets the list of the full keys associated with the most recently published set of markets (including all derived
     * markets)
     * 
     * @param publishedMarketKeys
     */
    public void setPublishedMarketKeys(Set<String> publishedMarketKeys) {
        this.publishedMarketKeys = publishedMarketKeys;
    }

    /**
     * gets the list of all markets (including derived markets) awaiting result. The map of markets uses full keys
     * 
     * @return
     */
    public MarketsAwaitingResult getMarketsAwaitingResult() {
        return marketsAwaitingResult;
    }



    public ResultedMarkets getResultedMarkets() {
        return resultedMarkets;
    }

    public void setResultedMarkets(ResultedMarkets resultedMarkets) {
        this.resultedMarkets = resultedMarkets;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(Date timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public boolean isTimeExpired(long expiryTimeMillis) {
        return timeStampMillis < expiryTimeMillis;
    }

    public Set<String> getDeltaKeySet() {
        return deltaKeySet;
    }

    public void setDeltaKeySet(Set<String> deltaKeySet) {
        this.deltaKeySet = deltaKeySet;
    }

    public TimeStamp getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(TimeStamp lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public EventState copy() {
        EventState cc = new EventState();
        cc.setIncidentId(this.getIncidentId());
        cc.matchParams = (GenericMatchParams) ((GenericMatchParams) this.matchParams).copy();
        cc.matchState = this.matchState.copy();
        cc.previousMatchState = this.previousMatchState.copy();
        cc.matchIncident = CopySerializableObject.copy(this.matchIncident);
        cc.matchIncidentResult = CopySerializableObject.copy(this.matchIncidentResult);
        cc.matchEngineSavedState = CopySerializableObject.copy(this.matchEngineSavedState);
        if (this.markets == null)
            cc.markets = null;
        else
            cc.markets = this.markets.copy();
        cc.publishedMarketKeys = copySet(this.publishedMarketKeys);
        cc.marketsAwaitingResult = this.marketsAwaitingResult.copy();
        if (this.resultedMarkets != null)
            cc.resultedMarkets = this.resultedMarkets.copy();
        else
            cc.resultedMarkets = null;
        cc.isCompleted = this.isCompleted;
        cc.timeCompleted = this.timeCompleted;
        cc.timeStampMillis = this.timeStampMillis;
        cc.resultedMarketsPublished = this.resultedMarketsPublished;
        cc.deltaKeySet = this.deltaKeySet;
        cc.lastTimeStamp = this.lastTimeStamp;
        cc.publishMarketsManager = this.publishMarketsManager.copy();
        return cc;

    }

    private Set<String> copySet(Set<String> keys) {
        Set<String> cc = new HashSet<String>(keys.size());
        cc.addAll(keys);
        return cc;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deltaKeySet == null) ? 0 : deltaKeySet.hashCode());
        result = prime * result + (isCompleted ? 1231 : 1237);
        result = prime * result + ((lastTimeStamp == null) ? 0 : lastTimeStamp.hashCode());
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
        result = prime * result + ((marketsAwaitingResult == null) ? 0 : marketsAwaitingResult.hashCode());
        result = prime * result + ((matchEngineSavedState == null) ? 0 : matchEngineSavedState.hashCode());
        result = prime * result + ((matchIncident == null) ? 0 : matchIncident.hashCode());
        // result = prime * result + ((matchIncidentResult == null) ? 0 : matchIncidentResult.hashCode());
        result = prime * result + ((matchParams == null) ? 0 : matchParams.hashCode());
        result = prime * result + ((matchState == null) ? 0 : matchState.hashCode());
        result = prime * result + ((previousMatchState == null) ? 0 : previousMatchState.hashCode());
        result = prime * result + ((publishedMarketKeys == null) ? 0 : publishedMarketKeys.hashCode());
        result = prime * result + ((resultedMarkets == null) ? 0 : resultedMarkets.hashCode());
        result = prime * result + (resultedMarketsPublished ? 1231 : 1237);
        result = prime * result + ((timeCompleted == null) ? 0 : timeCompleted.hashCode());
        result = prime * result + (int) (timeStampMillis ^ (timeStampMillis >>> 32));
        result = prime * result + ((publishMarketsManager == null) ? 0 : publishMarketsManager.hashCode());

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
        EventState other = (EventState) obj;
        if (deltaKeySet == null) {
            if (other.deltaKeySet != null)
                return false;
        } else if (!deltaKeySet.equals(other.deltaKeySet))
            return false;
        if (isCompleted != other.isCompleted)
            return false;
        if (lastTimeStamp == null) {
            if (other.lastTimeStamp != null)
                return false;
        } else if (!lastTimeStamp.equals(other.lastTimeStamp))
            return false;
        if (markets == null) {
            if (other.markets != null)
                return false;
        } else if (!markets.equals(other.markets))
            return false;
        if (marketsAwaitingResult == null) {
            if (other.marketsAwaitingResult != null)
                return false;
        } else if (!marketsAwaitingResult.equals(other.marketsAwaitingResult))
            return false;
        if (matchEngineSavedState == null) {
            if (other.matchEngineSavedState != null)
                return false;
        } else if (!matchEngineSavedState.equals(other.matchEngineSavedState))
            return false;
        if (matchIncident == null) {
            if (other.matchIncident != null)
                return false;
        } else if (!matchIncident.equals(other.matchIncident))
            return false;
        /*
         * matchIncidentresult does not declare an equals method
         */
        // if (matchIncidentResult == null) {
        // if (other.matchIncidentResult != null)
        // return false;
        // } else if (!matchIncidentResult.equals(other.matchIncidentResult))
        // return false;
        if (matchParams == null) {
            if (other.matchParams != null)
                return false;
        } else if (!matchParams.equals(other.matchParams))
            return false;
        if (matchState == null) {
            if (other.matchState != null)
                return false;
        } else if (!matchState.equals(other.matchState))
            return false;
        if (previousMatchState == null) {
            if (other.previousMatchState != null)
                return false;
        } else if (!previousMatchState.equals(other.previousMatchState))
            return false;
        if (publishedMarketKeys == null) {
            if (other.publishedMarketKeys != null)
                return false;
        } else if (!publishedMarketKeys.equals(other.publishedMarketKeys))
            return false;
        if (resultedMarkets == null) {
            if (other.resultedMarkets != null)
                return false;
        } else if (!resultedMarkets.equals(other.resultedMarkets))
            return false;
        if (resultedMarketsPublished != other.resultedMarketsPublished)
            return false;
        if (timeCompleted == null) {
            if (other.timeCompleted != null)
                return false;
        } else if (!timeCompleted.equals(other.timeCompleted))
            return false;
        if (timeStampMillis != other.timeStampMillis)
            return false;
        if (publishMarketsManager == null) {
            if (other.publishMarketsManager != null)
                return false;
        } else if (!publishMarketsManager.equals(other.publishMarketsManager))
            return false;
        return true;
    }

    @Override
    public String toString() {
        long ageMillis = System.currentTimeMillis() - timeStampMillis;
        return "EventState [hashcode: " + hashCode() + ", ageMillis=" + ageMillis + ", resultedMarketsPublished="
                        + resultedMarketsPublished + "]";
    }


    public void paramsChanged(GenericMatchParams oldMatchParams, GenericMatchParams newMatchParams,
                    boolean fromParamFind) {
        publishMarketsManager.paramsChanged(oldMatchParams, newMatchParams, fromParamFind);
    }

    public boolean shouldPublishMarket(Market market) {
        return publishMarketsManager.shouldPublishMarket(market);
    }

    public boolean shouldPublishAnyMarkets() {
        return publishMarketsManager.shouldPublishAnyMarkets();
    }

}
