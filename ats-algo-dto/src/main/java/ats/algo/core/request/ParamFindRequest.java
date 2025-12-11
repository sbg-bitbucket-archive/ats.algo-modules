package ats.algo.core.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.core.util.json.JsonUtil;

/**
 * Container for holding all data required to perform a param find.
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamFindRequest extends AbstractAlgoRequest {
    private static final long serialVersionUID = 1L;
    protected long eventId;
    private EventSettings eventSettings;
    protected CalcModelType calcModelType;
    protected String matchEngineClassName;
    protected MatchFormat matchFormat;
    protected MatchState matchState;
    protected GenericMatchParams matchParams;
    protected MarketPricesList marketPricesList;
    protected MatchEngineSavedState matchEngineSavedState;

    /**
     * Json constructor
     */
    public ParamFindRequest() {

    }

    /**
     * 
     * @param eventId
     * @param eventSettings
     * @param calcModelType
     * @param matchEngineClassName
     * @param matchFormat
     * @param matchState
     * @param matchParams
     * @param marketPricesList
     * @param matchEngineSavedState
     * @param requestTime
     */
    public ParamFindRequest(long eventId, EventSettings eventSettings, CalcModelType calcModelType,
                    String matchEngineClassName, MatchFormat matchFormat, MatchState matchState,
                    GenericMatchParams matchParams, MarketPricesList marketPricesList,
                    MatchEngineSavedState matchEngineSavedState, long requestTime) {
        this.eventId = eventId;
        this.eventSettings = eventSettings;
        this.calcModelType = calcModelType;
        this.matchEngineClassName = matchEngineClassName;
        this.matchFormat = matchFormat;
        this.matchState = matchState;
        this.matchParams = matchParams;
        this.marketPricesList = marketPricesList;
        this.matchEngineSavedState = matchEngineSavedState;
        this.requestTime = requestTime;
        this.uniqueRequestId = createUniqueRequestId();
    }

    @JsonIgnore
    public long getEventTier() {
        return eventSettings.getEventTier();
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public EventSettings getEventSettings() {
        return eventSettings;
    }

    public void setEventSettings(EventSettings eventSettings) {
        this.eventSettings = eventSettings;
    }

    public CalcModelType getCalcModelType() {
        return calcModelType;
    }

    public void setCalcModelType(CalcModelType calcModelType) {
        this.calcModelType = calcModelType;
    }

    public String getMatchEngineClassName() {
        return matchEngineClassName;
    }

    public void setMatchEngineClassName(String matchEngineClassName) {
        this.matchEngineClassName = matchEngineClassName;
    }

    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(MatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public GenericMatchParams getMatchParams() {
        return matchParams;
    }

    public void setMatchParams(GenericMatchParams matchParams) {
        this.matchParams = matchParams;
    }

    public MarketPricesList getMarketPricesList() {
        return marketPricesList;
    }

    public void setMarketPricesList(MarketPricesList marketPricesList) {
        this.marketPricesList = marketPricesList;
    }

    public MatchEngineSavedState getMatchEngineSavedState() {
        return matchEngineSavedState;
    }

    public void setMatchEngineSavedState(MatchEngineSavedState matchEngineSavedState) {
        this.matchEngineSavedState = matchEngineSavedState;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((calcModelType == null) ? 0 : calcModelType.hashCode());
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((eventSettings == null) ? 0 : eventSettings.hashCode());
        result = prime * result + ((marketPricesList == null) ? 0 : marketPricesList.hashCode());
        result = prime * result + ((matchEngineClassName == null) ? 0 : matchEngineClassName.hashCode());
        result = prime * result + ((matchEngineSavedState == null) ? 0 : matchEngineSavedState.hashCode());
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchParams == null) ? 0 : matchParams.hashCode());
        result = prime * result + ((matchState == null) ? 0 : matchState.hashCode());
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
        ParamFindRequest other = (ParamFindRequest) obj;
        if (calcModelType != other.calcModelType)
            return false;
        if (eventId != other.eventId)
            return false;
        if (eventSettings == null) {
            if (other.eventSettings != null)
                return false;
        } else if (!eventSettings.equals(other.eventSettings))
            return false;
        if (marketPricesList == null) {
            if (other.marketPricesList != null)
                return false;
        } else if (!marketPricesList.equals(other.marketPricesList))
            return false;
        if (matchEngineClassName == null) {
            if (other.matchEngineClassName != null)
                return false;
        } else if (!matchEngineClassName.equals(other.matchEngineClassName))
            return false;
        if (matchEngineSavedState == null) {
            if (other.matchEngineSavedState != null)
                return false;
        } else if (!matchEngineSavedState.equals(other.matchEngineSavedState))
            return false;
        if (matchFormat == null) {
            if (other.matchFormat != null)
                return false;
        } else if (!matchFormat.equals(other.matchFormat))
            return false;
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
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
