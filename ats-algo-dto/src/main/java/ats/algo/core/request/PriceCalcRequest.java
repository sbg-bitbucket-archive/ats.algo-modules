package ats.algo.core.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.matchresult.MatchResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceCalcRequest extends AbstractAlgoRequest {
    private static final long serialVersionUID = 1L;
    private long eventId;
    private EventSettings eventSettings;
    private String matchEngineClassName;
    private CalcRequestCause calcRequestCause;
    private MatchFormat matchFormat;
    private MatchState matchState;
    private GenericMatchParams matchParams;
    private MatchIncident matchIncident;
    private MatchIncidentResult matchIncidentResult;
    private MatchEngineSavedState matchEngineSavedState;
    private MatchResult matchResult;

    public PriceCalcRequest() {

    }

    /**
     * 
     * @param eventId
     * @param eventSettings
     * @param matchEngineClass
     * @param calcRequestCause
     * @param matchFormat
     * @param matchState
     * @param matchParams
     * @param matchIncident
     * @param matchIncidentResult
     * @param matchEngineSavedState
     * @param requestId
     * @param requestTime
     */
    public PriceCalcRequest(long eventId, EventSettings eventSettings, String matchEngineClassName,
                    CalcRequestCause calcRequestCause, MatchFormat matchFormat, MatchState matchState,
                    GenericMatchParams matchParams, MatchIncident matchIncident,
                    MatchIncidentResult matchIncidentResult, MatchEngineSavedState matchEngineSavedState,
                    long requestTime) {
        this.eventId = eventId;
        this.eventSettings = eventSettings;
        this.matchEngineClassName = matchEngineClassName;
        this.calcRequestCause = calcRequestCause;
        this.matchFormat = matchFormat;
        this.matchState = matchState;
        this.matchParams = matchParams;
        this.matchIncident = matchIncident;
        this.matchIncidentResult = matchIncidentResult;
        this.matchEngineSavedState = matchEngineSavedState;
        this.requestTime = requestTime;
        this.uniqueRequestId = createUniqueRequestId();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
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

    public String getMatchEngineClassName() {
        return matchEngineClassName;
    }

    public void setMatchEngineClassName(String matchEngineClassName) {
        this.matchEngineClassName = matchEngineClassName;
    }

    public CalcRequestCause getCalcRequestCause() {
        return calcRequestCause;
    }

    public void setCalcRequestCause(CalcRequestCause calcRequestCause) {
        this.calcRequestCause = calcRequestCause;
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

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((calcRequestCause == null) ? 0 : calcRequestCause.hashCode());
        result = prime * result + (int) (eventId ^ (eventId >>> 32));
        result = prime * result + ((eventSettings == null) ? 0 : eventSettings.hashCode());
        result = prime * result + ((matchEngineClassName == null) ? 0 : matchEngineClassName.hashCode());
        result = prime * result + ((matchEngineSavedState == null) ? 0 : matchEngineSavedState.hashCode());
        result = prime * result + ((matchFormat == null) ? 0 : matchFormat.hashCode());
        result = prime * result + ((matchIncident == null) ? 0 : matchIncident.hashCode());
        result = prime * result + ((matchIncidentResult == null) ? 0 : matchIncidentResult.hashCode());
        result = prime * result + ((matchParams == null) ? 0 : matchParams.hashCode());
        result = prime * result + ((matchResult == null) ? 0 : matchResult.hashCode());
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
        PriceCalcRequest other = (PriceCalcRequest) obj;
        if (calcRequestCause != other.calcRequestCause)
            return false;
        if (eventId != other.eventId)
            return false;
        if (eventSettings == null) {
            if (other.eventSettings != null)
                return false;
        } else if (!eventSettings.equals(other.eventSettings))
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
        if (matchIncident == null) {
            if (other.matchIncident != null)
                return false;
        } else if (!matchIncident.equals(other.matchIncident))
            return false;
        if (matchIncidentResult == null) {
            if (other.matchIncidentResult != null)
                return false;
        } else if (!matchIncidentResult.equals(other.matchIncidentResult))
            return false;
        if (matchParams == null) {
            if (other.matchParams != null)
                return false;
        } else if (!matchParams.equals(other.matchParams))
            return false;
        if (matchResult == null) {
            if (other.matchResult != null)
                return false;
        } else if (!matchResult.equals(other.matchResult))
            return false;
        if (matchState == null) {
            if (other.matchState != null)
                return false;
        } else if (!matchState.equals(other.matchState))
            return false;
        return true;
    }

    public static PriceCalcRequest generateRequestForMatchResult(long eventId, EventSettings eventSettings,
                    MatchFormat matchFormat, MatchState matchState, GenericMatchParams matchParams,
                    MatchEngineSavedState matchEngineSavedState, MatchResult matchResult, long requestTime) {
        PriceCalcRequest request = new PriceCalcRequest(eventId, eventSettings, null, CalcRequestCause.MATCH_RESULT,
                        matchFormat, matchState, matchParams, null, null, matchEngineSavedState, requestTime);
        request.setMatchResult(matchResult);
        return request;
    }

    public static PriceCalcRequest generateRequestForAbandonMatch(long eventId, EventSettings eventSettings,
                    MatchFormat matchFormat, MatchState matchState, GenericMatchParams matchParams,
                    MatchEngineSavedState matchEngineSavedState, long requestTime) {
        PriceCalcRequest request = new PriceCalcRequest(eventId, eventSettings, null, CalcRequestCause.MATCH_ABANDONED,
                        matchFormat, matchState, matchParams, null, null, matchEngineSavedState, requestTime);
        return request;
    }

}
