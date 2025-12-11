package ats.algo.requestresponse.ppb;


import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.request.AbstractAlgoRequest;

public class PpbTennisPriceCalcRequest extends AbstractAlgoRequest {

    private static final String API_VERSION_ID = "PPB_1.0.0";

    private static final long serialVersionUID = 1L;
    private long eventId;
    private String requestId;
    private PpbTennisMatchFormat ppbMatchFormat;
    private PpbTennisMatchState ppbMatchState;
    private PpbTennisMatchParams ppbMatchParams;
    private PpbMarkets ppbMarkets;
    private String ppbSavedState;

    /**
     * 
     * @param eventId
     * @param matchFormat
     * @param matchState
     * @param matchParams
     * @param markets
     */
    public PpbTennisPriceCalcRequest(@JsonProperty("eventId") long eventId, @JsonProperty("requestId") String requestId,
                    @JsonProperty("ppbMatchFormat") PpbTennisMatchFormat ppbMatchFormat,
                    @JsonProperty("ppbMatchState") PpbTennisMatchState ppbMatchState,
                    @JsonProperty("ppbMatchParams") PpbTennisMatchParams ppbMatchParams,
                    @JsonProperty("ppbMarkets") PpbMarkets ppbMarkets,
                    @JsonProperty("ppbSavedState") String ppbSavedState) {
        super();
        this.eventId = eventId;
        this.requestId = requestId;
        this.ppbMatchFormat = ppbMatchFormat;
        this.ppbMatchState = ppbMatchState;
        this.ppbMatchParams = ppbMatchParams;
        this.ppbMarkets = ppbMarkets;
        this.ppbSavedState = ppbSavedState;
        super.versionId = API_VERSION_ID;
    }


    public long getEventId() {
        return eventId;
    }

    public PpbTennisMatchFormat getPpbMatchFormat() {
        return ppbMatchFormat;
    }

    public PpbTennisMatchState getPpbMatchState() {
        return ppbMatchState;
    }

    public PpbTennisMatchParams getPpbMatchParams() {
        return ppbMatchParams;
    }

    public PpbMarkets getPpbMarkets() {
        return ppbMarkets;
    }

    public String getPpbSavedState() {
        return ppbSavedState;
    }

    public String getRequestId() {
        return requestId;
    }


}
