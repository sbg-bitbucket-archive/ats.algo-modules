package ats.algo.core.recordplayback;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.request.AbstractAlgoRequest;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.GCMath;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordedItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private RecordedItemType recordedItemType;
    private String uniqueRequestId;
    private long timeRequestIssued;
    private long timeResponseReceived;

    private boolean fatalErrorExecutingRequest;
    private PriceCalcRequest priceCalcRequest;
    private PriceCalcResponse priceCalcResponse;
    private ParamFindRequest paramFindRequest;
    private ParamFindResponse paramFindResponse;

    private String revertToEarlierStateForRequestId;
    private long eventId;
    private ConfiguredProperties configuredProperties;
    private int sequenceId;

    /**
     * constructor for use by Json
     */
    public RecordedItem() {}

    public static RecordedItem priceCalc(long requestTime, PriceCalcRequest request, PriceCalcResponse response) {
        RecordedItem item = new RecordedItem();
        item.setRecordedItemType(RecordedItemType.PRICE_CALC);
        item.setEventId(request.getEventId());
        item.setUniqueRequestId(request.getUniqueRequestId());
        item.setPriceCalcRequest(request);
        item.setPriceCalcResponse(response);
        item.setTimeRequestIssued(requestTime);
        item.setTimeResponseReceived(System.currentTimeMillis());
        item.setFatalErrorExecutingRequest(false);
        return item;
    }

    public void addConfiguredProperties(Map<String, String> generalProperties,
                    Map<String, String> sportSpecificProperties) {
        configuredProperties = new ConfiguredProperties();
        configuredProperties.setGeneralProperties(generalProperties);
        configuredProperties.setSportSpecificProperties(sportSpecificProperties);
    }

    public static RecordedItem priceCalcError(long requestTime, PriceCalcRequest request) {
        RecordedItem item = new RecordedItem();
        item.setRecordedItemType(RecordedItemType.PRICE_CALC);
        item.setEventId(request.getEventId());
        item.setUniqueRequestId(request.getUniqueRequestId());
        item.setPriceCalcRequest(request);
        item.setTimeRequestIssued(requestTime);
        item.setTimeResponseReceived(System.currentTimeMillis());
        item.setFatalErrorExecutingRequest(true);
        return item;
    }

    public static RecordedItem paramFind(long requestTime, ParamFindRequest request, ParamFindResponse response) {
        RecordedItem item = new RecordedItem();
        item.setRecordedItemType(RecordedItemType.PARAM_FIND);
        item.setEventId(request.getEventId());
        item.setUniqueRequestId(request.getUniqueRequestId());
        item.setParamFindRequest(request);
        item.setParamFindResponse(response);
        item.setTimeRequestIssued(requestTime);
        item.setTimeResponseReceived(System.currentTimeMillis());
        item.setFatalErrorExecutingRequest(false);
        return item;
    }

    public static RecordedItem paramFindError(long requestTime, ParamFindRequest request) {
        RecordedItem item = new RecordedItem();
        item.setRecordedItemType(RecordedItemType.PARAM_FIND);
        item.setEventId(request.getEventId());
        item.setUniqueRequestId(request.getUniqueRequestId());
        item.setParamFindRequest(request);
        item.setTimeRequestIssued(requestTime);
        item.setTimeResponseReceived(System.currentTimeMillis());
        item.setFatalErrorExecutingRequest(true);
        return item;
    }

    public static RecordedItem revertToEarlierState(long eventId, String requestId) {
        RecordedItem item = new RecordedItem();
        item.setRecordedItemType(RecordedItemType.REVERT_TO_EARLIER_STATE);
        item.setEventId(eventId);
        item.setUniqueRequestId(AbstractAlgoRequest.createUniqueRequestId());
        item.setRevertToEarlierStateForRequestId(requestId);
        long now = System.currentTimeMillis();
        item.setTimeRequestIssued(now);
        item.setTimeResponseReceived(now);
        item.setFatalErrorExecutingRequest(false);
        return item;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

    public RecordedItemType getRecordedItemType() {
        return recordedItemType;
    }

    public void setRecordedItemType(RecordedItemType recordedItemType) {
        this.recordedItemType = recordedItemType;
    }

    public String getUniqueRequestId() {
        return uniqueRequestId;
    }

    public void setUniqueRequestId(String uniqueRequestId) {
        this.uniqueRequestId = uniqueRequestId;
    }

    public long getTimeRequestIssued() {
        return timeRequestIssued;
    }

    public void setTimeRequestIssued(long timeRequestIssued) {
        this.timeRequestIssued = timeRequestIssued;
    }

    public long getTimeResponseReceived() {
        return timeResponseReceived;
    }

    public void setTimeResponseReceived(long timeResponseReceived) {
        this.timeResponseReceived = timeResponseReceived;
    }

    public Boolean getFatalErrorExecutingRequest() {
        return fatalErrorExecutingRequest;
    }

    public void setFatalErrorExecutingRequest(Boolean fatalErrorExecutingRequest) {
        this.fatalErrorExecutingRequest = fatalErrorExecutingRequest;
    }

    public PriceCalcRequest getPriceCalcRequest() {
        return priceCalcRequest;
    }

    public void setPriceCalcRequest(PriceCalcRequest priceCalcRequest) {
        this.priceCalcRequest = priceCalcRequest;
    }

    public PriceCalcResponse getPriceCalcResponse() {
        return priceCalcResponse;
    }

    public void setPriceCalcResponse(PriceCalcResponse priceCalcResponse) {
        this.priceCalcResponse = priceCalcResponse;
    }

    public ParamFindRequest getParamFindRequest() {
        return paramFindRequest;
    }

    public void setParamFindRequest(ParamFindRequest paramFindRequest) {
        this.paramFindRequest = paramFindRequest;
    }

    public ParamFindResponse getParamFindResponse() {
        return paramFindResponse;
    }

    public void setParamFindResponse(ParamFindResponse paramFindResponse) {
        this.paramFindResponse = paramFindResponse;
    }

    public String getRevertToEarlierStateForRequestId() {
        return revertToEarlierStateForRequestId;
    }

    public void setRevertToEarlierStateForRequestId(String revertToEarlierStateForRequestId) {
        this.revertToEarlierStateForRequestId = revertToEarlierStateForRequestId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String shortDescription() {
        StringBuilder b = new StringBuilder();
        b.append(this.recordedItemType.toString()).append(". ");
        if (this.fatalErrorExecutingRequest)
            b.append("fatal error executing request");
        else {
            switch (recordedItemType) {
                case PARAM_FIND:
                    b.append("resultsStatus: ");
                    b.append(paramFindResponse.getParamFindResults().getParamFindResultsStatus().toString());
                    break;
                case PRICE_CALC:
                    CalcRequestCause cause = priceCalcRequest.getCalcRequestCause();
                    if (cause.equals(CalcRequestCause.MATCH_INCIDENT))
                        b.append(priceCalcRequest.getMatchIncident().shortDescription());
                    else {
                        String s = null;
                        switch (priceCalcRequest.getCalcRequestCause()) {
                            case EVENT_TIER_CHANGE:
                                s = "Event tier change";
                                break;

                            case NEW_MATCH:
                                s = "New match";
                                break;
                            case PARAMS_CHANGED_BY_TRADER:
                                s = "Trader param change";
                                break;
                            case PARAMS_CHANGED_FOLLOWING_PARAM_FIND:
                                s = "Param change following param find";
                                break;
                            case PARAM_FIND:
                                s = "Param find";
                                break;
                            case TIMER:
                                s = "Timer initiated";
                                break;
                            default:
                                s = priceCalcRequest.getCalcRequestCause().toString();
                                break;
                        }
                        b.append(s);
                    }
                    break;
                case REVERT_TO_EARLIER_STATE:
                    b.append("revertToState: ");
                    b.append(revertToEarlierStateForRequestId);
                    break;
                default:
                    break;
            }
        }
        double elapsedTimeSecs = ((double) (timeResponseReceived - timeRequestIssued)) / 1000.0;
        b.append(" (").append(Double.toString(GCMath.round(elapsedTimeSecs, 1))).append("secs");
        if (sequenceId != 0)
            b.append(", sequenceId: ").append(this.sequenceId);
        b.append(")");
        return b.toString();
    }

    public void writeItemToFile(String archiveDirectory) throws JsonProcessingException, IOException {
        Recording.writeItemToFile(archiveDirectory, eventId, uniqueRequestId + ".json", this);
    }

    public enum RecordedItemType {
        PRICE_CALC,
        PARAM_FIND,
        REVERT_TO_EARLIER_STATE,
        CONFIGURABLE_PROPERTIES
    }

}
