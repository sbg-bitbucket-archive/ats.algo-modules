package ats.algo.core.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceCalcResponse extends AbstractAlgoResponse {
    private static final long serialVersionUID = 1L;

    private Markets markets;
    private GenericMatchParams matchParams;
    private MatchEngineSavedState matchEngineSavedState;
    private ResultedMarkets resultedMarkets;
    private long timePriceCalcRequestReceived;
    private long timePriceCalcResponseIssued;
    private boolean triggerParamFindAsap;
    private EventSettings eventSettings;

    /**
     * for Json use only
     */
    public PriceCalcResponse() {}

    public PriceCalcResponse(AbstractAlgoResponse abstractAlgoResponse) {
        super(abstractAlgoResponse.getUniqueRequestId());
        this.setFatalError(abstractAlgoResponse.fatalError);
        this.setFatalErrorCause(abstractAlgoResponse.getFatalErrorCause());
    }

    /**
     * 
     * @param uniqueRequestId
     * @param markets
     * @param matchParams
     * @param matchEngineSavedState
     * @param resultedMarkets
     */
    public PriceCalcResponse(String uniqueRequestId, Markets markets, GenericMatchParams matchParams,
                    MatchEngineSavedState matchEngineSavedState, ResultedMarkets resultedMarkets) {
        super(uniqueRequestId);
        this.markets = markets;
        this.matchParams = matchParams;
        this.matchEngineSavedState = matchEngineSavedState;
        this.resultedMarkets = resultedMarkets;
    }

    public void setMarkets(Markets markets) {
        this.markets = markets;
    }

    public Markets getMarkets() {
        return markets;
    }

    public void setMatchParams(GenericMatchParams matchParams) {
        this.matchParams = matchParams;
    }

    public GenericMatchParams getMatchParams() {
        return matchParams;
    }

    public MatchEngineSavedState getMatchEngineSavedState() {
        return matchEngineSavedState;
    }

    public void setResultedMarkets(ResultedMarkets resultedMarkets) {
        this.resultedMarkets = resultedMarkets;
    }

    public ResultedMarkets getResultedMarkets() {
        return resultedMarkets;
    }

    /**
     * optional property to set the time this request was received as measured on the local server. Time is in millisecs
     * since 1/1/70, so std Java time. A zero value indicates that this property was not set.
     * 
     * @param timePriceCalcRequestReceived
     */
    public void setTimePriceCalcRequestReceived(long timePriceCalcRequestReceived) {
        this.timePriceCalcRequestReceived = timePriceCalcRequestReceived;
    }

    /**
     * gets the time this request was received as measured on the local server. Time is in millisecs since 1/1/70, so
     * std Java time. A zero value indicates that this property was not set.
     * 
     * @return
     */
    public long getTimePriceCalcRequestReceived() {
        return timePriceCalcRequestReceived;
    }

    /**
     * Sets any event Settings that have been updated
     * 
     * @param eventSettings
     */
    public void setEventSettings(EventSettings eventSettings) {
        this.eventSettings = eventSettings;
    }

    /**
     * Returns any eventSettings that have been updated
     * 
     * @return
     */
    public EventSettings getEventSettings() {
        return eventSettings;
    }

    /**
     * optional property to set the time the response to this request was sent as measured on the local server. Time is
     * in millisecs since 1/1/70, so std Java time. A zero value indicates that this property was not set.
     * 
     * @param timePriceCalcResponseIssued
     */
    public void setTimePriceCalcResponseIssued(long timePriceCalcResponseIssued) {
        this.timePriceCalcResponseIssued = timePriceCalcResponseIssued;
    }

    /**
     * optional property to set the time the response to this request was sent as measured on the local server. Time is
     * in millisecs since 1/1/70, so std Java time. A zero value indicates that this property was not set.
     * 
     * @return
     */
    public long getTimePriceCalcResponseIssued() {
        return timePriceCalcResponseIssued;
    }

    public void setTriggerParamFindAsap(boolean triggerParamFindAsap) {
        this.triggerParamFindAsap = triggerParamFindAsap;
    }

    public boolean isTriggerParamFindAsap() {
        return triggerParamFindAsap;
    }

    public static PriceCalcResponse generateFatalErrorResponse(String uniqueRequestId, String cause) {
        return new PriceCalcResponse(AbstractAlgoResponse.generateFatalErrorResponse(uniqueRequestId, cause));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((markets == null) ? 0 : markets.hashCode());
        result = prime * result + ((matchEngineSavedState == null) ? 0 : matchEngineSavedState.hashCode());
        result = prime * result + ((matchParams == null) ? 0 : matchParams.hashCode());
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
        PriceCalcResponse other = (PriceCalcResponse) obj;
        if (markets == null) {
            if (other.markets != null)
                return false;
        } else if (!markets.equals(other.markets))
            return false;
        if (matchEngineSavedState == null) {
            if (other.matchEngineSavedState != null)
                return false;
        } else if (!matchEngineSavedState.equals(other.matchEngineSavedState))
            return false;
        if (matchParams == null) {
            if (other.matchParams != null)
                return false;
        } else if (!matchParams.equals(other.matchParams))
            return false;

        if (resultedMarkets == null) {
            if (other.resultedMarkets != null)
                return false;
        } else if (!resultedMarkets.equals(other.resultedMarkets))
            return false;
        return true;
    }

    /**
     * setter for saved State
     * 
     * @param savedState
     */
    public void setMatchEngineSavedState(MatchEngineSavedState savedState) {
        this.matchEngineSavedState = savedState;

    }

}
