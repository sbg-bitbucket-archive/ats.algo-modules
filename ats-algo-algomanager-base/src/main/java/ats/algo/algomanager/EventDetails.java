package ats.algo.algomanager;

import java.util.HashSet;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.matchengineframework.MatchEngine;

/**
 * Holds the details associated with each event managed by AlgoManager
 * 
 * @author Geoff
 *
 */
public class EventDetails {
    private long eventId;
    private SupportedSportType supportedSport;
    private MatchFormat matchFormat;
    private MatchEngine matchEngine;
    private long delayBeforePublishingResultedMarkets;
    private int eventStateHistorySize;
    private EventState eventState;
    private EventStateHistory eventStateHistory;
    private EventStatisticsCollector eventStats;
    private EventSettings eventSettings;
    private boolean useClientParamFinder;
    private boolean useClientResultingOnly;
    private boolean useClientTradingRules;
    private boolean useExternalModel;
    private AlgoQueue queue;
    private TriggerParamFindData triggerParamFindData;
    private long timeOfLastMatchIncident;
    private long timeOfLastPriceUpdate;
    private boolean publishedPricesDelayWarning;
    private boolean publishedPricesDelayDanger;
    private boolean publishedIncidentDelayWarning;
    private boolean publishedIncidentDelayDanger;
    private boolean eventSuspendedDuetoTradingRules;
    private boolean eventSuspendedAwaitingSuccessfulParamFind;
    private boolean clearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc;
    private int recordedItemSequenceId;

    private boolean atLeastOnePriceCalcCompleted;

    private volatile boolean priceCalcInProgress;
    private CalcRequestCause calcRequestCause;
    private volatile long priceCalcInProgressStartedTime;
    private PriceCalcRequest priceCalcRequest;
    private TimeStamp timeStamp;

    private boolean preMatchWhenParamFindStarted;

    private boolean handlingUndoableIncidentInProgress;
    private String incidentIdInProgress;

    private volatile boolean paramFindInProgress;
    private ParamFindRequest paramFindRequest;
    private long paramFindInProgressStartedTime;

    private boolean eventSuspendDueToDisaster = false;

    public enum EventSuspendedReason {
        ACTIVEMQ_FAILURE;
    }

    public boolean isEventSuspendDueToDisaster() {
        return eventSuspendDueToDisaster;
    }

    public void setEventSuspendDueToDisaster(boolean eventSuspendDueToDisaster) {
        this.eventSuspendDueToDisaster = eventSuspendDueToDisaster;
    }

    /**
     * 
     * @param supportedSport
     * @param matchFormat
     * @param matchParams
     * @param matchState
     */
    EventDetails(long eventId, SupportedSportType supportedSport, MatchFormat matchFormat,
                    GenericMatchParams matchParams, MatchState matchState) {
        this.eventState = new EventState(matchState, matchParams);
        initialiseEventDetails(eventId, supportedSport, matchFormat);
    }

    public EventDetails(long eventId, SupportedSportType supportedSport, MatchFormat matchFormat,
                    EventStateBlob eventStateBlob) {
        eventState = (EventState) eventStateBlob;
        eventState.setPublishedMarketKeys(new HashSet<String>());
        initialiseEventDetails(eventId, supportedSport, matchFormat);
    }

    private void initialiseEventDetails(long eventId, SupportedSportType supportedSport, MatchFormat matchFormat) {
        this.eventId = eventId;
        this.supportedSport = supportedSport;
        this.matchFormat = matchFormat;
        this.matchEngine = SupportedSports.getActiveMatchEngine(supportedSport, matchFormat);
        this.priceCalcInProgress = false;
        this.eventStats = new EventStatisticsCollector();
        this.eventStateHistorySize = SupportedSports.getEventStateHistorySize(supportedSport);
        this.delayBeforePublishingResultedMarkets =
                        SupportedSports.getDelayBeforePublishingResultedMarkets(supportedSport);
        this.eventStateHistory = new EventStateHistory(eventStateHistorySize);
        this.handlingUndoableIncidentInProgress = false;
        this.queue = new AlgoQueue();
        this.triggerParamFindData = new TriggerParamFindData();

        useClientParamFinder = SupportedSports.isUseClientParamFinder(supportedSport);
        useClientResultingOnly = SupportedSports.isUseClientResulting(supportedSport);
        useClientTradingRules = SupportedSports.isUseClientTradingRules(supportedSport);
        useExternalModel = SupportedSports.isUseExternalModel(supportedSport);
        recordedItemSequenceId = 0;
        eventSettings = new EventSettings();
        this.publishedPricesDelayDanger = false;
        this.publishedPricesDelayWarning = false;
        this.publishedIncidentDelayWarning = false;
        this.publishedIncidentDelayDanger = false;

    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public SupportedSportType getSupportedSport() {
        return supportedSport;
    }

    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    public MatchEngine getMatchEngine() {
        return matchEngine;
    }

    public EventSettings getEventSettings() {
        return eventSettings;
    }

    public void setEventSettings(EventSettings eventSettings) {
        this.eventSettings = eventSettings;
    }


    public boolean isPriceCalcInProgress() {
        return priceCalcInProgress;
    }

    public long getPriceCalcInProgressStartedTime() {
        return priceCalcInProgressStartedTime;
    }

    public CalcRequestCause getcalcRequestCause() {
        return calcRequestCause;
    }

    public void setPriceCalcInProgress(PriceCalcRequest request) {
        priceCalcInProgress = true;
        priceCalcInProgressStartedTime = System.currentTimeMillis();
        priceCalcRequest = request;
        calcRequestCause = request.getCalcRequestCause();
    }

    public void clearPriceCalcInProgress() {
        priceCalcInProgress = false;
        priceCalcInProgressStartedTime = 0l;
        calcRequestCause = null;
        priceCalcRequest = null;
    }

    public void setParamFindInProgress(ParamFindRequest request) {
        paramFindInProgress = true;
        paramFindInProgressStartedTime = System.currentTimeMillis();
        paramFindRequest = request;
    }

    public void clearParamFindInProgress() {
        paramFindInProgress = false;
        paramFindInProgressStartedTime = 0l;
        paramFindRequest = null;
    }

    public boolean isCompleted() {
        return eventState.isCompleted();
    }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public boolean isParamFindInProgress() {
        return paramFindInProgress;
    }

    boolean isPreMatchWhenParamFindStarted() {
        return preMatchWhenParamFindStarted;
    }

    void setPreMatchWhenParamFindStarted(boolean preMatchWhenParamFindStarted) {
        this.preMatchWhenParamFindStarted = preMatchWhenParamFindStarted;
    }

    public EventStatisticsCollector getStatistics() {
        return eventStats;
    }

    public boolean isHandlingUndoableIncidentInProgress() {
        return handlingUndoableIncidentInProgress;
    }

    public void setHandlingUndoableIncidentInProgress(String incidentId) {
        this.handlingUndoableIncidentInProgress = true;
        this.incidentIdInProgress = incidentId;
    }

    public void clearHandlingUndoableIncidentInProgress() {
        handlingUndoableIncidentInProgress = false;
        incidentIdInProgress = null;
    }

    public String getIncidentIdInProgress() {
        return incidentIdInProgress;
    }

    public long getDelayBeforePublishingResultedMarkets() {
        return delayBeforePublishingResultedMarkets;
    }

    public int getEventStateHistorySize() {
        return eventStateHistorySize;
    }

    public EventStateHistory getEventStateHistory() {
        return eventStateHistory;
    }

    public boolean isUseClientResultingOnly() {
        return useClientResultingOnly;
    }

    public void setUseClientResultingOnly(boolean useClientResulting) {
        this.useClientResultingOnly = useClientResulting;
    }

    public boolean isUseClientTradingRules() {
        return useClientTradingRules;
    }

    public void setUseClientTradingRules(boolean useClientTradingRules) {
        this.useClientTradingRules = useClientTradingRules;
    }

    public boolean isUseClientParamFinder() {
        return useClientParamFinder;
    }

    public void setUseClientParamFinder(boolean useClientParamFinder) {
        this.useClientParamFinder = useClientParamFinder;
    }

    public boolean isUseExternalModel() {
        return useExternalModel;
    }

    public void setUseExternalModel(boolean useExternalModel) {
        this.useExternalModel = useExternalModel;
    }

    public AlgoQueue getQueue() {
        return queue;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TriggerParamFindData getTriggerParamFindData() {
        return triggerParamFindData;
    }

    public long getTimeOfLastMatchIncident() {
        return timeOfLastMatchIncident;
    }

    public void setTimeOfLastMatchIncident(long timeOfLastMatchIncident) {
        this.timeOfLastMatchIncident = timeOfLastMatchIncident;
    }

    public long getTimeOfLastPriceUpdate() {
        return timeOfLastPriceUpdate;
    }

    public void setTimeOfLastPriceUpdate(long timeOfLastPriceUpdate) {
        this.timeOfLastPriceUpdate = timeOfLastPriceUpdate;
    }

    public void setTriggerParamFindData(TriggerParamFindData triggerParamFindData) {
        this.triggerParamFindData = triggerParamFindData;
    }

    public boolean isEventSuspendedDuetoTradingRules() {
        return eventSuspendedDuetoTradingRules;
    }

    public void setEventSuspendedDuetoTradingRules(boolean eventLevelSuspensionInForce) {
        this.eventSuspendedDuetoTradingRules = eventLevelSuspensionInForce;
    }

    public boolean isEventSuspendedAwaitingSuccessfulParamFind() {
        return eventSuspendedAwaitingSuccessfulParamFind;
    }

    public void setEventSuspendedAwaitingSuccessfulParamFind(boolean eventSuspendedDueToFailedParamFind) {
        if (this.eventSuspendedAwaitingSuccessfulParamFind != eventSuspendedDueToFailedParamFind) {
            this.eventSuspendedAwaitingSuccessfulParamFind = eventSuspendedDueToFailedParamFind;
            setClearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc(false);
        }
    }

    public boolean isClearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc() {
        return clearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc;
    }

    public void setClearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc(
                    boolean clearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc) {
        this.clearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc =
                        clearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc;
    }


    public boolean isEventSuspended() {
        return eventSuspendedDuetoTradingRules || eventSuspendedAwaitingSuccessfulParamFind;
    }

    public boolean isAtLeastOnePriceCalcCompleted() {
        return atLeastOnePriceCalcCompleted;
    }

    public void setAtLeastOnePriceCalcCompleted(boolean atLeastOnePriceCalcCompleted) {
        this.atLeastOnePriceCalcCompleted = atLeastOnePriceCalcCompleted;
    }

    public void clearTriggerParamFindData() {
        triggerParamFindData = new TriggerParamFindData();
    }

    public PriceCalcRequest getPriceCalcRequest() {
        return priceCalcRequest;
    }

    public ParamFindRequest getParamFindRequest() {
        return paramFindRequest;
    }

    public long getParamFindInProgressStartedTime() {
        return paramFindInProgressStartedTime;
    }

    public int nextRecordedItemSequenceId() {
        recordedItemSequenceId++;
        return recordedItemSequenceId;
    }

    public boolean isPublishedPricesDelayWarning() {
        return publishedPricesDelayWarning;
    }

    public void setPublishedPricesDelayWarning(boolean publishedPricesDelayWarning) {
        this.publishedPricesDelayWarning = publishedPricesDelayWarning;
    }

    public boolean isPublishedPricesDelayDanger() {
        return publishedPricesDelayDanger;
    }

    public void setPublishedPricesDelayDanger(boolean publishedPricesDelayDanger) {
        this.publishedPricesDelayDanger = publishedPricesDelayDanger;
    }

    public boolean isPublishedIncidentDelayWarning() {
        return publishedIncidentDelayWarning;
    }

    public void setPublishedIncidentDelayWarning(boolean publishedIncidentDelayWarning) {
        this.publishedIncidentDelayWarning = publishedIncidentDelayWarning;
    }

    public boolean isPublishedIncidentDelayDanger() {
        return publishedIncidentDelayDanger;
    }

    public void setPublishedIncidentDelayDanger(boolean publishedIncidentDelayDanger) {
        this.publishedIncidentDelayDanger = publishedIncidentDelayDanger;
    }

    public void setDelayBeforePublishingResultedMarkets(long delayBeforePublishingResultedMarkets) {
        this.delayBeforePublishingResultedMarkets = delayBeforePublishingResultedMarkets;
    }

}
