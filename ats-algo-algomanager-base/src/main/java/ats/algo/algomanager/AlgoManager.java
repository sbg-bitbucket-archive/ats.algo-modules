package ats.algo.algomanager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ats.algo.core.eventsettings.GeneralEventSetting;
import com.google.common.collect.Lists;

import ats.algo.algomanager.AlgoQueue.PendingAlgoWork;
import ats.algo.algomanager.EventDetails.EventSuspendedReason;
import ats.algo.algomanager.outrights.OutrightsMonitor;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.core.common.TeamSheetMatchIncident.TeamSheetMatchIncidentType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.DataToGenerateStaticMarkets;
import ats.algo.core.markets.DerivedMarketDetails;
import ats.algo.core.markets.DerivedMarketDetails.DerivedMarketResulter;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsAwaitingResult;
import ats.algo.core.markets.MarketsMetaData;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleResult;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.request.CalcModelType;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.tradingrules.TradingRulesManager;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.genericsupportfunctions.SetOps;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.sport.generic.tradingrules.GenericTradingRuleSet;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;

public class AlgoManager extends AtsBean implements AlgoManagerTimerInterface {

    private AlgoManagerConfiguration algoManagerConfiguration;
    private AlgoManagerPublishable publisher;
    private volatile Map<Long, EventDetails> eventList;
    AlgoManagerTimer algoManagerTimer;
    private Thread timerThread;
    private AlgoManagerStatisticsCollector algoManagerStatistics;
    private TradingRulesManager tradingRulesManager;
    private int timerFrequencyMs;
    private long statisticsLogTimer;
    private OutrightsMonitor outrightsMonitor;

    private static final long TIME_BETWEEN_STATISTICS_LOG = 3600000;
    private static final long delayBeforePublishingResultedMarketsVar = 5400 * 1000;
    private GeneralProperties algoManagerProperties;
    private boolean useMarginChart;

    /**
     * 
     * @param algoManagerConfiguration - the glue that bindsAlgoMgr to calculators and paramfinders
     * @param publisher - the interface to push results out to a listener.
     */
    public AlgoManager(AlgoManagerConfiguration algoManagerConfiguration, AlgoManagerPublishable publisher) {
        this.info("AlgoManager starting up");
        algoManagerConfiguration.setHandlePriceCalcResponse(
                        (long eventId, PriceCalcResponse response) -> handlePriceCalcResponse(eventId, response));
        algoManagerConfiguration.setHandleParamFindResponse(
                        (long eventId, ParamFindResponse response) -> handleParamFindResponse(eventId, response));
        algoManagerConfiguration.setHandlePriceCalcError((long eventId, String uniqueRequestId,
                        String errorCause) -> handlePriceCalcError(eventId, uniqueRequestId, errorCause));
        algoManagerConfiguration.setHandleParamFindError((long eventId, String uniqueRequestId,
                        String errorCause) -> handleParamFindError(eventId, uniqueRequestId, errorCause));

        this.algoManagerConfiguration = algoManagerConfiguration;
        this.publisher = publisher;
        eventList = new ConcurrentHashMap<>();
        algoManagerStatistics = new AlgoManagerStatisticsCollector(eventList, algoManagerConfiguration);
        tradingRulesManager = new TradingRulesManager();
        setDefaultTradingRules();
        algoManagerProperties = GeneralProperties.initialiseFromSystemProperties();
        info("GeneralProperties at startup:");
        algoManagerProperties.logProperties();
        useMarginChart = algoManagerProperties.isUseMarginChart();
        outrightsMonitor = new OutrightsMonitor();
        /*
         * start the timer
         */
        this.timerFrequencyMs = 2000;
        algoManagerTimer = new AlgoManagerTimer(eventList, algoManagerStatistics, this);
        timerThread = new Thread(algoManagerTimer, "AlgoMgrTimerQueueProcessor");
        timerThread.start();
        statisticsLogTimer = 0;
    }

    /**
     * Returns the number of pending tasks on the internal algoQueues
     * 
     * @return
     */
    public int getAlgoMgrQueueSize() {
        int queueSize = 0;
        for (Entry<Long, EventDetails> e : eventList.entrySet()) {
            EventDetails eventDetails = e.getValue();
            int qSizeForEvent = eventDetails.getQueue().size();
            queueSize += qSizeForEvent;
            if (qSizeForEvent > 2) {
                /*
                 * if more than one event queued for event then log
                 */
                warn("eventID %d queue of pending actions is unusually large.  Queue size: %d", e.getKey(),
                                qSizeForEvent);
            }
        }
        return queueSize;
    }

    /**
     * Diagnostic add to log contents of the queue.
     */
    public void logCurrentQueueTasks() {
        for (Entry<Long, EventDetails> e : eventList.entrySet()) {
            EventDetails eventDetails = e.getValue();
            eventDetails.getQueue().logCurrentQueueTasks();
        }
    }

    /**
     * if set to true then no markets are published until a param find or manual param change has occurred. Default
     * value is true
     * 
     * @param onlyPublishMarketsFollowingParamChange
     */
    public void onlyPublishMarketsFollowingParamChange(boolean onlyPublishMarketsFollowingParamChange) {
        info("onlyPublishMarketsFollowingParamChange set to: " + onlyPublishMarketsFollowingParamChange);
        algoManagerProperties.setOnlyPublishMarketsFollowingParamChange(onlyPublishMarketsFollowingParamChange);
    }

    /**
     * if set to true then only corners and cards for football will be published. Default value is false.
     * 
     * @param onlyPublishCornersAndCardsMarket
     */
    public void onlyPublishCornersAndCardsMarket(boolean onlyPublishCornersAndCardsMarket) {
        info("onlyPublishCornersAndCardsMarket set to: " + onlyPublishCornersAndCardsMarket);
        algoManagerProperties.setOnlyPublishCornersAndCardsMarket(onlyPublishCornersAndCardsMarket);
    }

    /**
     * if set to true then alert will be published when match goes in play published. Default value is false.
     * 
     * @param transitionToInplayAlert
     */
    public void transitionToInplayAlert(boolean transitionToInplayAlert) {
        info("transitionToInplayAlert set to: " + transitionToInplayAlert);
        algoManagerProperties.setTransitionToInplayAlert(transitionToInplayAlert);
    }

    /**
     * Useful for running unit tests without setting the associated system property
     * 
     * @param autosyncMatchStateToFeedOnMismatch
     */
    public void autoSyncWithMatchFeed(boolean autosyncMatchStateToFeedOnMismatch) {
        algoManagerProperties.setAutosyncMatchStateToFeedOnMismatch(autosyncMatchStateToFeedOnMismatch);
    }

    /**
     * Useful for running unit tests without setting the associated system property
     * 
     * @param usingParamFindTradingRules
     */
    public void usingParamFindTradingRules(boolean usingParamFindTradingRules) {
        algoManagerProperties.setUsingParamFindTradingRules(usingParamFindTradingRules);
    }

    /**
     * sets the elapsed time between param finds before alert
     * 
     * @param elapsedTimeBeforeConsecutiveAlertTriggered
     */

    public void setElapsedTimeBeforeConsecutiveAlertTriggered(int elapsedTimeBeforeConsecutiveAlertTriggered) {
        info("elapsedTimeBeforeConsecutiveAlertTriggered set to: " + elapsedTimeBeforeConsecutiveAlertTriggered);
        algoManagerProperties.setElapsedTimeBeforeConsecutiveAlertTriggered(elapsedTimeBeforeConsecutiveAlertTriggered);
    }

    public int getElapsedTimeBeforeConsecutiveAlertTriggered() {
        return algoManagerProperties.getElapsedTimeBeforeConsecutiveAlertTriggered();
    }

    /**
     * sets the timer length for internal calcs as client specific. Default is 10 secs.
     * 
     * @param timerLengthForInternalCalcs
     */

    public void setTimerFrequencyMs(int timerLengthForInternalCalcs) {
        info("timerLengthForInternalCalcs set to: " + timerLengthForInternalCalcs);
        this.timerFrequencyMs = timerLengthForInternalCalcs;
    }

    public int getTimerFrequencyMs() {
        return timerFrequencyMs;
    }

    public void setUseClientResulting(SupportedSportType supportedSport, boolean b) {
        SupportedSports.setUseClientResulting(supportedSport, b);
    }

    public void setUseClientParamFinder(SupportedSportType supportedSport, boolean b) {
        SupportedSports.setUseClientParamFinder(supportedSport, b);
    }

    public void setUseClientTradingRules(SupportedSportType supportedSport, boolean b) {
        SupportedSports.setUseExternalModel(supportedSport, b);
    }

    public void setUseExternalModel(SupportedSportType supportedSport, boolean b) {
        SupportedSports.setUseExternalModel(supportedSport, b);
    }

    public void setDelayBeforePublishingResultedMarkets(SupportedSportType supportedSport, int secs) {
        SupportedSports.setDelayBeforePublishingResultedMarkets(supportedSport, secs);
    }

    /**
     * if set to true then resulted markets are published as soon as they are created. If false then the delay logic
     * controlled by the delayBeforePublishingResultedMarkets and eventStateHistorySize params is executed. Normally set
     * to false in ATS but useful to set to true for test and MatchRunner purposes
     * 
     * @param publishResultedMarketsImmediately
     */
    public void publishResultedMarketsImmediately(boolean publishResultedMarketsImmediately) {
        info("publishResultedMarketsImmediately set to: " + publishResultedMarketsImmediately);
        algoManagerProperties.setPublishResultedMarketsImmediately(publishResultedMarketsImmediately);
    }

    /**
     * sets the URL for the broker
     * 
     * 
     * @param activeMqBroker
     * @return true if connects ok
     */
    public boolean setActiveMqBrokerUrl(String activeMqBroker) {
        return algoManagerConfiguration.establishExternalModelConnection(activeMqBroker);
    }

    /**
     * stops the timer - useful for unit testing
     */
    public void killTimer() {
        algoManagerTimer.killtimer();
    }

    /**
     * set properties for the specified event
     * 
     * @param properties
     */
    public void handleSetEventProperties(long eventId, Map<String, String> properties) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            if (eventDetails == null)
                error("EventId: %d.  handleSetEventProperties: no eventDetails available for this event", eventId);
            return;
        }
        String propertiesStr = JsonUtil.marshalJson(properties);
        info("EventId: %d.  handleSetEventProperties: %s", eventId, propertiesStr);
        processSetEventProperties(eventDetails, properties, false);
    }

    private void processSetEventProperties(EventDetails eventDetails, Map<String, String> properties,
                    boolean changedInternally) {
        long eventId = eventDetails.getEventId();

        synchronized (eventDetails) {
            /*
             * update eventSettings and check for eventTier change
             */

            EventSettings eventSettings = eventDetails.getEventSettings();
            long oldEventTier = eventSettings.getEventTier();
            boolean oldUseBookiePrices = eventSettings.isIgnoreBookiePrices();
            Map<String, Double> oldSourceWeights = eventSettings.getSourceWeights();
            eventSettings.updateEventSettings(properties);

            long newEventTier = eventSettings.getEventTier();
            if (newEventTier == oldEventTier) {
                info("EventId: %d. processSetEventProperties.  EventTier is unchanged at %d.  No action taken", eventId,
                                newEventTier);
            } else {
                info("EventId: %d. Setting eventTier to: %d", eventId, newEventTier);
                eventDetails.getTriggerParamFindData().setLastUsedMarketPricesList(null);
                GenericMatchParams matchParams = eventDetails.getEventState().getMatchParams();

                if (algoManagerProperties.isUpdateParamMapWithPropertiesChange())
                    matchParams.updateParamMapForEventTier(newEventTier);
                long now = System.currentTimeMillis();
                synchronized (eventDetails) {
                    if (!(changedInternally)) {
                        if (eventDetails.isPriceCalcInProgress()) {
                            eventDetails.getQueue().addSetMatchParams(matchParams, now,
                                            CalcRequestCause.EVENT_TIER_CHANGE);
                        } else {
                            processSetMatchParams(matchParams, now, CalcRequestCause.EVENT_TIER_CHANGE);
                        }
                    }
                }
            }
            /*
             * update any sourceWeights that have changed
             */
            Map<String, Double> newSourceWeights = eventSettings.getSourceWeights();
            for (Entry<String, Double> sourceWeight : newSourceWeights.entrySet()) {
                String sourceName = sourceWeight.getKey();
                Double newWeight = sourceWeight.getValue();
                Double oldWeight = oldSourceWeights.get(sourceName);
                if (!newWeight.equals(oldWeight)) {
                    eventDetails.getTriggerParamFindData().getPriceSourceWeights().setPriceSourceWeight(sourceName,
                                    newWeight);
                    eventDetails.getTriggerParamFindData().setLastUsedMarketPricesList(null);
                    info("EventId: %d. Setting source weight for %s to: %.2f", eventId, sourceName, newWeight);
                }
            }

            boolean newUseBookiePrices = eventSettings.isIgnoreBookiePrices();
            if (newUseBookiePrices == oldUseBookiePrices) {
                info("EventId: %d. processSetEventProperties. ignoreBookiePrices is unchanged at %b.  No action taken",
                                eventId, newUseBookiePrices);
            } else {
                info("EventId: " + eventId + ". Setting ignoreBookiePrices to: " + newUseBookiePrices);
                eventDetails.getTriggerParamFindData().setLastUsedMarketPricesList(null);
            }
        }

        if (changedInternally) {
            publishEventPropertiesSafely(eventId, properties);
        }
    }

    /**
     * called whenever a new event for the specified supportedSport needs to be created
     *
     * @param supportedSport
     * @param eventId
     * @param matchFormat
     * @return
     */
    public void handleNewEventCreation(SupportedSportType supportedSport, long eventId, MatchFormat matchFormat) {
        handleNewEventCreation(supportedSport, eventId, matchFormat, EventSettings.DEFAULT_EVENT_TIER);
    }

    /**
     * called whenever a new event for the specified supportedSport needs to be created
     * 
     * @param supportedSport
     * @param eventId
     * @param matchFormat
     * @param eventTier
     */
    public void handleNewEventCreation(SupportedSportType supportedSport, long eventId, MatchFormat matchFormat,
                    long eventTier) {
        Map<String, String> eventProperties = new HashMap<>(1);
        eventProperties.put("eventTier", Long.toString(eventTier));
        eventProperties.put("ignoreBookiePrices", Boolean.toString(false));
        handleNewEventCreation(supportedSport, eventId, matchFormat, eventProperties);
    }

    /**
     * 
     * @param supportedSport
     * @param eventId
     * @param matchFormat
     * @param eventProperties
     */
    public void handleNewEventCreation(SupportedSportType supportedSport, long eventId, MatchFormat matchFormat,
                    Map<String, String> eventProperties) {
        this.info("EventId: %d. handleNewEventCreation", eventId);
        String propertiesStr;
        if (eventProperties != null)
            propertiesStr = JsonUtil.marshalJson(eventProperties);
        else
            propertiesStr = "null";
        this.debug("EventId: %d. handleNewEventCreation.  matchFormat: %s, eventProperties: %s", eventId, matchFormat,
                        propertiesStr);
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails != null)
            error("EventId: %d already exists.  Overwriting", eventId);

        if (!matchFormat.isMatchFormatOk()) {
            handleMatchFormatUpdate(eventId, matchFormat);
            matchFormat = matchFormat.generateDefaultMatchFormat();
            warn("EventId: %d, Match Format Incorrect. Creating event with default format: %s", eventId,
                            matchFormat.toString());
        }

        MatchEngine matchEngine = SupportedSports.getActiveMatchEngine(supportedSport, matchFormat);
        TradingRules tradingRules = SupportedSports.getTradingRulesSet(supportedSport);
        info("EventId: %d. Match engine class being used:%s", eventId, matchEngine.getClass().getName());
        if (tradingRules != null) {
            info("EventId: %d. Trading class being used:%s", eventId, tradingRules.getClass().getName());
        }
        GenericMatchParams genericMatchParams = matchEngine.getMatchParams().generateGenericMatchParams();
        MatchState initialMatchState = matchEngine.getMatchState();
        /*
         * easiest way to make the system override the requirement for in play PF before publishing markets is to
         * increment the successful pf counter
         * 
         * Note this has two effects: - disables the pf required to release markets rule - disables the resetBias on
         * first successful pf rule
         */
        if (algoManagerProperties.isOverrideRequirementForInPlayPfToReleaseMarkets())
            initialMatchState.incrementNoSuccessfulInPlayParamFindsExecuted();
        eventDetails = new EventDetails(eventId, supportedSport, matchFormat, genericMatchParams, initialMatchState);
        EventSettings eventSettings = eventDetails.getEventSettings();
        genericMatchParams.updateParamMapForEventTier(eventSettings.getEventTier());
        genericMatchParams.setEventId(eventId);
        debug("EventId: %d. Default match params:%s", eventId, genericMatchParams.toString());
        String incidentId = "CreateEvent_" + eventId;
        createEvent(supportedSport, eventDetails, incidentId, eventProperties);

    }

    /**
     * called to create a new event for the specified sport and initialise the eventState to that provided by
     * EventStateBlob
     * 
     * @param supportedSport
     * @param eventId
     * @param matchFormat
     * @param eventStateBlob
     * @param eventProperties
     */
    public void handleNewEventCreation(SupportedSportType supportedSport, long eventId, MatchFormat matchFormat,
                    EventStateBlob eventStateBlob, Map<String, String> eventProperties) {

        this.info("EventId: %d. handleNewEventCreation with supplied EventStateBlob. Setting to eventState for incidentId: %s",
                        eventId, eventStateBlob.getIncidentId());
        this.debug("EventId: %d. handleNewEventCreation from blob.  matchFormat: %s", eventId, matchFormat);
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails != null)
            error("EventId: %d already exists.  Overwriting", eventId);
        eventDetails = new EventDetails(eventId, supportedSport, matchFormat, eventStateBlob);
        eventDetails.getEventSettings().updateEventSettings(eventProperties);
        this.info("EventId: %d. handleNewEventCreation from blob.  Supplied params: %s", eventId,
                        eventDetails.getEventState().getMatchParams().toString());
        if (!matchFormat.isMatchFormatOk()) {
            handleMatchFormatUpdate(eventId, matchFormat);
            warn("EventId: %d, Match Format Incorrect. Remove event due to attempted creation.", eventId,
                            matchFormat.toString());
            handleRemoveEvent(eventId);
        } else {
            EventState eventState = eventDetails.getEventState();
            if (null != eventState) {
                GenericMatchParams matchParams = eventState.getMatchParams();
                debug("EventId: %d recovered blob params %s", eventId, matchParams);
            }
            String incidentId = eventStateBlob.getIncidentId();
            createEvent(supportedSport, eventDetails, incidentId, eventProperties);
        }
    }

    private void createEvent(SupportedSportType supportedSport, EventDetails eventDetails, String incidentId,
                    Map<String, String> eventProperties) {
        long eventId = eventDetails.getEventId();
        eventDetails.setUseExternalModel(getSystemProperty(supportedSport, USE_EXTERNAL_MODEL));
        eventDetails.setUseClientResultingOnly(getSystemProperty(supportedSport, USE_CLIENT_RESULTING));
        eventDetails.setUseClientTradingRules(getSystemProperty(supportedSport, USE_CLIENT_TRADING_RULES));
        eventDetails.setUseClientParamFinder(getSystemProperty(supportedSport, USE_CLIENT_PARAM_FINDING));
        /*
         * set undoableIncidentInProgress to true to ensure gets added to history
         */
        eventDetails.setHandlingUndoableIncidentInProgress(incidentId);
        eventList.put(eventId, eventDetails);
        if (supportedSport.equals(SupportedSportType.OUTRIGHTS)) {
            /*
             * add any outrights type event to the monitor
             */
            outrightsMonitor.addOutrightEvent(eventId);
        }

        synchronized (eventDetails) {
            /*
             * need to create the eventStatistics object before executing publishAndCalcPrices()
             */
            algoManagerStatistics.recordEventCreated();
            if (eventDetails.isUseExternalModel())
                algoManagerConfiguration.establishExternalModelConnection(
                                algoManagerProperties.getUrlForExternalModelsMqBroker());
            publishAndCalcPrices(eventId, CalcRequestCause.NEW_MATCH, eventDetails, System.currentTimeMillis());

            if (eventProperties != null) {
                if (!eventProperties.isEmpty()) {
                    handleSetEventProperties(eventId, eventProperties);
                }
            }
        }
    }

    /*
     * this section deals with getting and setting required system properties. The setter is provided as a static public
     * method for use by MatchRunner and unit tests
     */
    public static final String USE_EXTERNAL_MODEL = "externalModel";
    public static final String USE_POLYMORPHIC_SERIALISATION = "polymorphicJsonSerialisation";
    public static final String USE_CLIENT_RESULTING = "clientResultingOnly";
    public static final String USE_CLIENT_TRADING_RULES = "clientTradingRules";
    public static final String USE_CLIENT_PARAM_FINDING = "clientParamFinding";

    /**
     * sets a property that can subsequently be read by AlgoManager
     * 
     * @param sportId
     * @param propertyName
     * @param value
     */
    public static void setSystemProperty(SupportedSportType sportId, String propertyName, String value) {
        String fullPropertyName = getPropertyIdentifier(sportId, propertyName);
        System.setProperty(fullPropertyName, value);
    }

    private boolean getSystemProperty(SupportedSportType sportId, String propertyName) {
        String fullPropertyName = getPropertyIdentifier(sportId, propertyName);
        String property = System.getProperty(fullPropertyName);
        if (property != null)
            if (property.toLowerCase().equals("true")) {
                info("System property: " + fullPropertyName + " is set to true");
                return true;
            }
        return false;
    }

    private static String getPropertyIdentifier(SupportedSportType sportId, String propertyName) {
        String fullPropertyName = "algo." + sportId.toString().toLowerCase() + "." + propertyName;
        return fullPropertyName;
    }
    /*
     * end of system properties section
     */

    /**
     * Used to show when the incident feed becomes available or unavailable
     * 
     * @param eventId
     * @param feedId identifier for the source feed in question
     * @param feedAvailable true if available, false if not
     */
    public void handleIncidentFeedStatus(long eventId, String feedId, boolean feedAvailable) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleIncidentFeedStatus.  No eventDetails available for this event", eventId);
            return;
        }
        synchronized (eventDetails) {
            if (feedAvailable) {

                if (eventDetails.getEventState().getMatchState().preMatch()) {
                    info("EventId: %d.  Publishing eventLevelShouldSuspend to %b.  Reason: handleIncidentFeedStatus method invoked for feed %s",
                                    eventId, false, feedId);
                    eventDetails.setEventSuspendedDuetoTradingRules(false);
                    this.publishEventSuspensionStatusSafely(eventId, false, null);
                }

            } else {
                info("EventId: %d.  Publishing eventLevelShouldSuspend to %b.  Reason: handleIncidentFeedStatus method invoked for feed %s",
                                eventId, true, feedId);
                eventDetails.setEventSuspendedDuetoTradingRules(true);

                debug("Created trader alert for eventID %d as the feed has been disconnected", eventId);
                TraderAlert traderAlert = new TraderAlert(TraderAlertType.FEED_DISCONNECTED,
                                String.valueOf(System.currentTimeMillis()), null);
                publishTraderAlertSafely(eventId, traderAlert);

                /*
                 * set time of last incident and price update to zero so that suspension status stays in force until a
                 * new incident and price update have been received
                 */
                eventDetails.setTimeOfLastMatchIncident(0);
                eventDetails.setTimeOfLastPriceUpdate(0);
                this.publishEventSuspensionStatusSafely(eventId, true, null);
            }
        }
    }

    public void handleRepublishData(long eventId) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleRepublishData.  No eventDetails available for this event", eventId);
            return;
        }
        synchronized (eventDetails) {
            if (eventDetails.isAtLeastOnePriceCalcCompleted()) {
                this.info("EventId: %d. handleRepublishData requested.  About to publish data", eventId);
                EventState eventState = eventDetails.getEventState();
                MatchState matchState = eventState.getMatchState();
                publishMatchStateSafely(eventId, matchState.generateSimpleMatchState(),
                                eventDetails.getEventSettings().getEventTier());
                if (!eventDetails.isCompleted()) {
                    publishMatchParamsSafely(eventId, eventState.getMatchParams());
                    deriveAndPublishMarkets(eventState.getMarkets(), eventId, eventDetails, null);

                }
            } else {
                this.warn("EventId: %d. handleRepublishData requested before first priceCalcCompleted.  No action taken",
                                eventId);
            }
        }
    }

    public void handleMatchFormatUpdate(long eventId, MatchFormat matchFormat) {
        warn("EventId: %d. Incorrect Format. Match format being used is : %s", eventId, matchFormat.toString());
        publishTraderAlertSafely(eventId,
                        new TraderAlert(TraderAlertType.MATCHFORMAT_WARNING, "Match Format is not possible.", null));
    }

    /**
     * reset Event - i.e. discard all state info and start again from a clean sheet.
     * 
     * @param eventId
     */
    public void handleResetEvent(long eventId, MatchFormat matchFormat, long eventTier) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleResetEvent. No eventDetails available for this event", eventId);
            return;
        }
        synchronized (eventDetails) {
            this.info("EventId: %d. handleResetEvent", eventId);
            
            if (!algoManagerProperties.isOnlyPublishMarketsFollowingParamChange()
                            || (eventDetails.getEventState().shouldPublishAnyMarkets())) {
                this.info("EventId: %d. Publishing all markets to be suspended due to reset", eventId);
    
                MarketsAwaitingResult marketsAwaitingResult = eventDetails.getEventState().getMarketsAwaitingResult();
                Markets marketsToBeSuspendedOnReset = marketsAwaitingResult.getAllMarketsAwaitingResult();
                for (Market market : marketsToBeSuspendedOnReset) {
                    market.getMarketStatus().setSuspensionStatusRuleName("Reset event rule - all market suspension");
                    market.getMarketStatus()
                                    .setSuspensionStatusReason("Event has been reset - need to suspend all markets");
                    market.getMarketStatus().setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
                    debug("Market being set = " + market);
                }
                String currentSequenceId = chooseSportSpecificSequenceId(eventDetails);
                Set<String> deltaKeySet = new HashSet<String>();
                TimeStamp timeStamp = eventDetails.getTimeStamp();
                MarketsMetaData marketsMetaData = new MarketsMetaData();
                for (Market market : marketsToBeSuspendedOnReset)
                    marketsMetaData.addMarket(market);
                marketsToBeSuspendedOnReset.setMarketsMetaData(marketsMetaData);
                debug("Markets to be suspended due to reset = " + marketsToBeSuspendedOnReset);
    
    
                publishMarketsSafely(eventId, marketsToBeSuspendedOnReset, deltaKeySet, timeStamp, currentSequenceId);
            }

            if (eventDetails.isPriceCalcInProgress()) {
                String uniqueRequestId = eventDetails.getPriceCalcRequest().getUniqueRequestId();
                info("EventId: %d, handleResetEvent.  Abandoning priceCalc in progress for reqId: %s", eventId,
                                uniqueRequestId);
                algoManagerConfiguration.abandonPriceCalc(eventId, uniqueRequestId);
            }
            if (eventDetails.isParamFindInProgress()) {
                String uniqueRequestId = eventDetails.getParamFindRequest().getUniqueRequestId();
                info("EventId: %d, handleResetEvent.  Abandoning paramFind in progress for reqId: %s", eventId,
                                uniqueRequestId);
                algoManagerConfiguration.abandonParamFind(eventId, uniqueRequestId);
            }
            SupportedSportType supportedSport = eventDetails.getSupportedSport();
            if (!matchFormat.isMatchFormatOk()) {
                warn("EventId: %d, Match Format Incorrect. Not resetting event.", eventId, matchFormat.toString());
            } else {
                Map<String, String> eventProperties = populateBookiePriceWeightsFromOldProp(eventDetails.getEventSettings(),eventTier);
                handleRemoveEvent(eventId);
                handleNewEventCreation(supportedSport, eventId, matchFormat, eventProperties);
            }
        }
    }

    private Map<String, String> populateBookiePriceWeightsFromOldProp(EventSettings eventSettings, long eventTier) {
        Map<String, String> eventProperties = new HashMap<>(1);
        eventProperties.put("eventTier", Long.toString(eventTier));
        eventProperties.put("ignoreBookiePrices", Boolean.toString(false));
        if(eventSettings == null)
            return eventProperties;
        if(eventSettings.getGeneralEventSettings() != null && !eventSettings.getGeneralEventSettings().isEmpty()){
            for(Entry<String, GeneralEventSetting> eventSettingEntry :eventSettings.getGeneralEventSettings().entrySet()){
                if(eventSettingEntry.getKey().startsWith("SOURCEWEIGHT_")){
                    GeneralEventSetting generalSetting = eventSettingEntry.getValue();
                    eventProperties.put(eventSettingEntry.getKey(),generalSetting.getValue());
                }
            }
        }
        return eventProperties;
    }

    /**
     * called whenever a new matchIncident arrives for the event specified by eventId
     *
     * @param eventId The unique id as supplied when event was created
     * @param matchIncident The incident details
     * @param shouldUpdatePrices - ignored in this implementation Normally set to true. Set to false if replaying a
     *        series of incidents when recovering from a systems failure and no need to recalculate market prices after
     *        each event
     */
    public void handleMatchIncident(MatchIncident matchIncident, Boolean shouldUpdatePrices) {
        long eventId = matchIncident.getEventId();
        EventDetails eventDetails = getEventDetails(eventId);
        boolean processIncident = true;
        Object incidentSubType = matchIncident.getIncidentSubType();
        if (incidentSubType != null && incidentSubType instanceof TennisMatchIncidentType) {
            if (incidentSubType.equals(TennisMatchIncidentType.SERVING_ORDER)) {
                debug("EventId: %d. Serving order is probably wrong here - ignoring it", eventId);
                processIncident = false;
            }
        }
        if (eventDetails == null) {
            error("EventId: %d.  handleMatchIncident. no eventDetails available for this event", eventId);
            return;
        }
        synchronized (eventDetails) {
            long now = System.currentTimeMillis();
            eventDetails.setTimeOfLastMatchIncident(now);
            if (eventDetails.isPriceCalcInProgress() && processIncident) {
                /*
                 * need to wait until the current priceCalc is finished
                 */
                this.info("EventId: %d. Queuing match incident: %s, shouldUpdatePrices:%b", eventId, matchIncident,
                                shouldUpdatePrices);
                eventDetails.getQueue().addMatchIncident(matchIncident, now);
            } else if (processIncident) {
                this.info("EventId: %d. Process match incident: %s, shouldUpdatePrices:%b", eventId, matchIncident,
                                shouldUpdatePrices);
                processMatchIncident(matchIncident, now);
            }
        }
    }

    /**
     * processes the match Incident either as soon as it arrives or after having been dequeued. Must be executed within
     * a synchronized (eventList) block
     *
     * @param matchIncident
     * @param eventDetails
     * @param shouldUpdatePrices
     */
    private void processMatchIncident(MatchIncident matchIncident, long requestTime) {
        /*
         * The details from matchIncident are used to update the matchState object for this event
         *
         * Any markets that can be resulted following this incident are resulted added to the resultedMarkets list and
         * published
         *
         * If this incident signifies that the match is completed and all markets can be resulted then the
         * notifyEventCompleted function is used to notify downstream systems that this match is over.
         *
         * A single match incident can therefore cause any or all of these items to be published: a) updated markets, b)
         * updated params, c) updated resulted markets, d) match over
         */

        long eventId = matchIncident.getEventId();
        try {
            EventDetails eventDetails = getEventDetails(eventId);
            if (eventDetails == null) {
                error("EventId: %d.  processMatchIncident.  No eventDetails available for this event", eventId);
                return;
            }
            /*
             * if AbandonMatchIncident then call the method designed to handle it
             */

            if (matchIncident instanceof AbandonMatchIncident) {
                processAbandonEvent(eventId, false, System.currentTimeMillis());
                return;
            }

            EventState eventState = eventDetails.getEventState();
            MatchState matchState = eventState.getMatchState();
            MatchState previousMatchState = eventState.getPreviousMatchState();
            previousMatchState.setEqualTo(matchState);
            boolean stateWasDatafeedStateMismatch = matchState.isDatafeedStateMismatch();
            MatchIncidentResult matchIncidentResult = matchState.updateStateForIncident(matchIncident,
                            algoManagerProperties.isAutosyncMatchStateToFeedOnMismatch());

            if (matchState.getDataFeedStatus() != null) {
                Boolean matchStateFeedStatusActive = DatafeedMatchIncident.isFeedStateActive(matchState.getDataFeedStatus());
                if(matchStateFeedStatusActive != null && !matchStateFeedStatusActive){
                    if (matchIncident instanceof DatafeedMatchIncident) {
                        DatafeedMatchIncident dfi = (DatafeedMatchIncident) matchIncident;
                        Boolean incidentFeedStatusActive = DatafeedMatchIncident.isFeedStateActive(dfi.getIncidentSubType());
                        if (incidentFeedStatusActive != null && incidentFeedStatusActive) {
                            matchState.setDataFeedStatus(DatafeedMatchIncident.DatafeedMatchIncidentType.OK);
                        }
                    } else {
                        matchState.setDataFeedStatus(DatafeedMatchIncident.DatafeedMatchIncidentType.OK);
                    }
                }
            }

            /*
             * extend delay result market for Soccer
             */
            if ((eventDetails.getSupportedSport() == SupportedSportType.SOCCER)) {
                if (matchState.isVarReferralInProgress()) {
                    info("EventId: %d. set delayBeforePublishingResultedMarkets as 90 mins ", eventId);
                    eventDetails.setDelayBeforePublishingResultedMarkets(delayBeforePublishingResultedMarketsVar);
                } else {
                    long delay = SupportedSports
                                    .getDelayBeforePublishingResultedMarkets(eventDetails.getSupportedSport());
                    info("EventId: %d. set delayBeforePublishingResultedMarkets as %d mins ", eventId, delay);
                    eventDetails.setDelayBeforePublishingResultedMarkets(SupportedSports
                                    .getDelayBeforePublishingResultedMarkets(eventDetails.getSupportedSport()));
                }
            }
            TraderAlert result = tradingRulesManager.applyTraderAlertRules(eventDetails.getSupportedSport(), matchState,
                            matchIncident);
            if (result != null) {
                publishTraderAlertSafely(eventId, result);
            }

            result = null;

            result = tradingRulesManager.applyTraderAlertRules(eventDetails.getSupportedSport(), matchState,
                            previousMatchState, matchIncident, eventDetails.getMatchFormat());
            if (result != null) {
                publishTraderAlertSafely(eventId, result);
            }

            Map<String, String> properties = new HashMap<String, String>();
            EventSettings eventSettings = eventDetails.getEventSettings();

            properties = tradingRulesManager.applySetPropertyChangeTradingRules(eventDetails.getSupportedSport(),
                            matchState, previousMatchState, matchIncident, eventSettings);

            if (properties != null) {
                info("Handling property change due to trading rules");
                processSetEventProperties(eventDetails, properties, true);
            }

            boolean suspendAwaitingParamFind = tradingRulesManager.applySuspendToAwaitParamFindRules(
                            eventDetails.getSupportedSport(), matchState, matchIncident);
            if (suspendAwaitingParamFind)
                updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, true, null, null);
            // add suspend prematch logic when prematch to inplay
            if (previousMatchState.preMatch() && !matchState.preMatch()) {
                if (algoManagerProperties.isTransitionToInplayAlert()) {
                    debug("Created trader alert for eventID %d going inplay", eventId);
                    TraderAlert traderAlert = new TraderAlert(TraderAlertType.EVENT_INPLAY,
                                    String.valueOf(System.currentTimeMillis()), null);
                    publishTraderAlertSafely(eventId, traderAlert);
                }
            }
            eventState.setMatchIncident(matchIncident);
            eventState.setMatchIncidentResult(matchIncidentResult);
            long currentTimeMillis = System.currentTimeMillis();
            eventState.setTimeStampMillis(currentTimeMillis);

            if (matchState.isDatafeedStateMismatch()) {

                /*
                 * We're out of sync with the feed Set the correct flags, create and publish alert, put logging.
                 */

                matchState.setDataFeedStateMismatchCleared(false);
                eventDetails.getEventState().getMatchState().setDataFeedStateMismatchCleared(false);

                eventDetails.getEventState().getMatchState().setClearingAlertHasBeenActivated(false);
                matchState.setClearingAlertHasBeenActivated(false);

                TraderAlert traderAlert = new TraderAlert(TraderAlertType.MATCH_STATE_MISMATCH,
                                "Score per AlgoMgr out of sync with feed. Score per feed: "
                                                + matchIncident.toStringScorePerFeed(),
                                null);
                publishTraderAlertSafely(eventId, traderAlert);

                int eventTier = (int) eventDetails.getEventSettings().getEventTier();
                MatchFormat matchFormat = eventDetails.getMatchFormat();

                SimpleMatchState previousSMS =
                                (SimpleMatchState) previousMatchState.generateSimpleMatchState(eventTier);
                SimpleMatchState currentSMS = (SimpleMatchState) matchState.generateSimpleMatchState(eventTier);

                warn(String.format("EventId: %d. Our pre incident state was %s.", eventId, previousSMS));
                warn(String.format("EventId: %d. Our current incident state is %s.", eventId, currentSMS));
                warn(String.format("EventId: %d. Out of sync generated. Maybe a match format issue? %s", eventId,
                                matchFormat));

            } else if (stateWasDatafeedStateMismatch && !matchState.isClearingAlertHasBeenActivated()) {
                /*
                 * we were out of sync, but now recovering
                 */

                debug("EventId: %d. Clearing the out of sync.", eventId);

                eventDetails.getEventState().getMatchState().setClearingAlertHasBeenActivated(true);
                matchState.setClearingAlertHasBeenActivated(true);

                eventDetails.getEventState().getMatchState().setDataFeedStateMismatchCleared(false);
                matchState.setDataFeedStateMismatchCleared(false);

                TraderAlert traderAlert = new TraderAlert(TraderAlertType.MATCH_STATE_MISMATCH_CLEARING,
                                "Score per AlgoMgr being put back in sync by trader.", null);
                publishTraderAlertSafely(eventId, traderAlert);
                info(String.format("EventId: %d.  %s", eventId, traderAlert));

            } else if (matchState.isClearingAlertHasBeenActivated()) {

                debug("EventId: %d. The clearing alert has been triggered - checking to see if we are to send a cleared alert.",
                                eventId);

                if (matchIncident.toStringScorePerFeed() != "" && !matchIncident.getSourceSystem().equals("TRADER")) {

                    /*
                     * we were out of sync, but now back in sync
                     */

                    debug("EventId: %d. We are now back in sync. Send cleared alert.", eventId);

                    eventDetails.getEventState().getMatchState().setDataFeedStateMismatchCleared(true);
                    matchState.setDataFeedStateMismatchCleared(true);

                    eventDetails.getEventState().getMatchState().setClearingAlertHasBeenActivated(false);
                    matchState.setClearingAlertHasBeenActivated(false);

                    TraderAlert traderAlert = new TraderAlert(TraderAlertType.MATCH_STATE_MISMATCH_CLEARED,
                                    "Score per AlgoMgr back in line with feed.", null);
                    publishTraderAlertSafely(eventId, traderAlert);
                    info(String.format("EventId: %d.  %s", eventId, traderAlert));

                } else {
                    debug("EventId: %d. Not back in sync yet.", eventId);

                    eventDetails.getEventState().getMatchState().setClearingAlertHasBeenActivated(true);
                    matchState.setClearingAlertHasBeenActivated(true);

                    eventDetails.getEventState().getMatchState().setDataFeedStateMismatchCleared(false);
                    matchState.setDataFeedStateMismatchCleared(false);

                }

            }
            if (matchIncident instanceof TeamSheetMatchIncident) {
                /*
                 * team sheet incident of type INITIAL_TEAM_SHEET may update match params
                 */
                TeamSheetMatchIncident tsMatchIncident = (TeamSheetMatchIncident) matchIncident;
                if (tsMatchIncident.getIncidentSubType() == TeamSheetMatchIncidentType.INITIAL_TEAM_SHEET) {
                    /*
                     * need to make sure the version of updatePlayerMatchParams specific to this sport gets applied
                     */
                    if (!eventDetails.isUseExternalModel()) {
                        eventState.getMatchParams().updatePlayerMatchParams(matchState.getTeamSheet());
                    }
                }
            }

            GenericMatchParams matchParams = eventState.getMatchParams();
            debug("EventId: %d. Match params for calc: %s", eventId, matchParams.toString());
            boolean undoableMatchIncident = !(matchIncident instanceof DatafeedMatchIncident);
            String incidentId = matchIncident.getIncidentId();
            if (incidentId == null)
                incidentId = "I-" + System.currentTimeMillis();
            if (undoableMatchIncident) {
                eventDetails.setHandlingUndoableIncidentInProgress(incidentId);
                matchState.setIncidentId(incidentId);
            } else
                eventDetails.clearHandlingUndoableIncidentInProgress();

            TimeStamp timeStamp = new TimeStamp(eventId, incidentId, eventState.getMatchIncident().getTimeStamp(),
                            requestTime, currentTimeMillis);
            eventDetails.setTimeStamp(timeStamp);
            eventDetails.getTriggerParamFindData().getMatchIncidentResultCache().addMatchIncidentResult(incidentId,
                            currentTimeMillis, matchIncidentResult);
            publishAndCalcPrices(eventId, CalcRequestCause.MATCH_INCIDENT, eventDetails, requestTime);

            /*
             * check for end of match don't complete if there is a sync issue
             */

            boolean outOfSyncIssues =
                            matchState.isDatafeedStateMismatch() || !matchState.isDataFeedStateMismatchCleared();

            if (matchState.isMatchCompleted()) {
                if (outOfSyncIssues) {
                    debug("EventId %d. Game is not in sync so don't want to complete it.", eventId);
                } else
                    processMatchCompleted(eventDetails);
            }
        } catch (Exception ex) {
            error("EventId: %d.  Problem handling matchIncident: %s", eventId, matchIncident);
            error(ex);
        }
    }

    private void processMatchCompleted(EventDetails eventDetails) {

        long eventId = eventDetails.getEventId();
        EventState eventState = eventDetails.getEventState();
        MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();
        if (marketsAwaitingResult.size() > 0) {
            Markets markets = marketsAwaitingResult.getAllMarketsAwaitingResult();
            warn("EventId: %d. processMatchCompleted request is being actioned but there are still markets that have not been resulted.  Unresulted markets: %s",
                            eventId, marketsAwaitingResult.toString());
            markets.forEach(x -> {
                if (x.getMarketGroup() == MarketGroup.INDIVIDUAL) {
                    LocalDateTime now = LocalDateTime.now();
                    warn("EventId: %d will not complete in after %s due to  %s not resulted ", eventId, x.toString(),
                                    now.toString());
                    return;
                    // try {
                    // TimeUnit.HOURS.sleep(24);
                    // } catch (InterruptedException e) {
                    // error("EventId: %d. Problem in handleManualResultMarkets
                    // 24 hours", eventDetails.getEventId());
                    // error(e);
                    // }
                }
            });

        } else {
            info("EventId: %d. for sport %s is completed.  All markets resulted. ", eventId,
                            eventDetails.getSupportedSport().toString());
        }
        eventState.setCompleted(true);
        eventState.setTimeCompleted(new Date());
        publishNotifyEventCompletedSafely(eventId, true);
        if (eventDetails.getSupportedSport().equals(SupportedSportType.OUTRIGHTS))
            outrightsMonitor.removeOutrightsEvent(eventId);
    }

    /**
     * ` returns the set of resulted markets that can be resulted
     * 
     * @param eventId
     * @param marketsAwaitingResult updated on exit to remove those markets that have been resulted
     * @param matchEngine
     * @param previousMatchState
     * @param matchState
     * @return the set of resulted markets
     */
    private ResultedMarkets resultMarkets(MarketsAwaitingResult marketsAwaitingResult, MatchEngine matchEngine,
                    MatchState previousMatchState, MatchState matchState) {
        return resultMarkets(marketsAwaitingResult, matchEngine, previousMatchState, matchState, false);
    }

    private ResultedMarkets resultMarketsForAbandonedEvent(MarketsAwaitingResult marketsAwaitingResult,
                    MatchEngine matchEngine, MatchState matchState) {
        return resultMarkets(marketsAwaitingResult, matchEngine, matchState, matchState, true);

    }

    private ResultedMarkets resultMarkets(MarketsAwaitingResult marketsAwaitingResult, MatchEngine matchEngine,
                    MatchState previousMatchState, MatchState matchState, boolean matchAbandoned) {
        ResultedMarkets allNewResultedMarkets = new ResultedMarkets();
        /*
         * result the base markets first
         */
        ResultedMarkets baseResultedMarkets;
        if (matchAbandoned)
            baseResultedMarkets = matchEngine.resultMarketsForAbandonedEvent(
                            marketsAwaitingResult.getBaseMarketsAwaitingResult(), matchState);
        else
            baseResultedMarkets = matchEngine.resultMarkets(marketsAwaitingResult.getBaseMarketsAwaitingResult(),
                            previousMatchState, matchState);

        ResultedMarkets allResultedMarkets = null;
        /*
         * Temporarily only attempt to result all markets for a abandoned match.
         */
        if (matchAbandoned)
            allResultedMarkets = matchEngine.resultMarkets(marketsAwaitingResult.getAllMarketsAwaitingResult(),
                            previousMatchState, matchState);

        if (baseResultedMarkets != null) {
            for (ResultedMarket resultedMarket : baseResultedMarkets.getResultedMarkets().values())
                allNewResultedMarkets.addMarket(resultedMarket);

            if (allResultedMarkets != null)
                for (ResultedMarket resultedMarket : allResultedMarkets.getResultedMarkets().values())
                    allNewResultedMarkets.addMarket(resultedMarket);

            /*
             * result all the non derivedMarkets associated with each of these base markets. e.g. if the base resulted
             * market FT:OU is for line 16.5 then this list will include FT:OU markets for all other lines that have
             * been generated (not including those generated via a derived trading rule
             */
            Markets nonDerivedMarketsReadyToResult =
                            marketsAwaitingResult.getNonDerivedMarketsReadyToResult(baseResultedMarkets);
            ResultedMarkets nonDerivedResultedMarkets;
            if (matchAbandoned)
                nonDerivedResultedMarkets =
                                matchEngine.resultMarketsForAbandonedEvent(nonDerivedMarketsReadyToResult, matchState);
            else
                nonDerivedResultedMarkets = matchEngine.resultMarkets(nonDerivedMarketsReadyToResult,
                                previousMatchState, matchState);
            for (ResultedMarket resultedMarket : nonDerivedResultedMarkets)
                allNewResultedMarkets.addMarket(resultedMarket);
            for (ResultedMarket resultedBaseMarket : baseResultedMarkets.getResultedMarkets().values()) {
                /*
                 * Continue the resulting logic for the derived market ONLY if we are not resulting partially ,Jin
                 * 
                 */
                if (resultedBaseMarket.isFullyResulted()) {

                    String baseMarketShortKey = resultedBaseMarket.getShortKey();
                    /*
                     * get the markets derived from this base and result each of them
                     */
                    Map<String, Market> derivedMarketsAwaitingResultForKey =
                                    marketsAwaitingResult.getDerivedMarketsAwaitingResult(baseMarketShortKey);
                    if (derivedMarketsAwaitingResultForKey != null) {
                        for (Market derivedMarket : derivedMarketsAwaitingResultForKey.values()) {
                            DerivedMarketDetails derivedMarketDetails = derivedMarket.getDerivedMarketDetails();
                            if (derivedMarketDetails == null)
                                derivedMarketDetails = null;
                            DerivedMarketResulter derivedMarketResulter =
                                            derivedMarketDetails.getDerivedMarketResulter();
                            ResultedMarket resultedDerivedMarket =
                                            derivedMarketResulter.result(derivedMarket, resultedBaseMarket);
                            allNewResultedMarkets.addMarket(resultedDerivedMarket);
                        }
                    }
                    /*
                     * remove the entry for this baseMarket
                     */

                    marketsAwaitingResult.removeEntry(baseMarketShortKey);
                } else {
                    /*
                     * Result losing, instead removeEntry from the marketsAwaitingResult, remove selections from it
                     * 
                     */
                    // System.out.println("Removing Selection From the Market" +
                    // resultedBaseMarket.getFullKey());
                    matchEngine.cleanUpMarketAwaitingResultSelections(marketsAwaitingResult, resultedBaseMarket);
                    // Market market =
                    // marketsAwaitingResult.getAllMarketsAwaitingResult()
                    // .getMarketForFullKey(resultedBaseMarket.getFullKey());
                    // System.out.println(market.getSelections());
                }
            }
        }
        return allNewResultedMarkets;
    }

    /**
     * must be called from within a block synchronized on eventDetails and assumes priceCalcInProgress = false
     */
    public void handleTimerInitiatedCalc(EventDetails eventDetails, long now) {
        long eventId = eventDetails.getEventId();
        EventState eventState = eventDetails.getEventState();
        if (eventDetails == null || !eventState.getMatchState().isClockRunning())
            return;
        try {
            eventState.setMatchIncident(null);
            eventState.setMatchIncidentResult(null);
            publishAndCalcPrices(eventId, CalcRequestCause.TIMER, eventList.get(eventId), now);
        } catch (Exception ex) {
            error("EventId: %d.. Problem handling timerCalcRequest", eventId);
            error(ex);
        }
    }

    /**
     * processes the publishing of any delayed resultedMarkets. Must be executed from within a
     * synchronized(eventDetails) block
     * 
     * @param eventId
     * @param now
     */
    public void handleDelayedPublishResultedMarkets(EventDetails eventDetails, long now) {
        /*
         * publish resulted markets if any have time expired
         */
        long delayBeforePublishingResultedMarkets = eventDetails.getDelayBeforePublishingResultedMarkets();

        if (eventDetails.getEventState().getMatchState().isVarReferralInProgress())
            delayBeforePublishingResultedMarkets = 5400 * 1000;

        ResultedMarkets resultedMarkets = eventDetails.getEventStateHistory()
                        .getMostRecentTimeExpiredResultedMarkets(now - delayBeforePublishingResultedMarkets);
        if (resultedMarkets != null) {
            debug("EventId: %d, incidentId: %s. delayedPublishResultedMarkets triggered following time delay of %d ms",
                            eventDetails.getEventId(), resultedMarkets.getIncidentId(),
                            delayBeforePublishingResultedMarkets);
            this.publishResultedMarketsSafely(eventDetails.getEventId(), resultedMarkets);
        }

    }

    /**
     * called from the timer does two things:
     * 
     * - determines whether a param find should be triggered.
     * 
     * - determines whether eventLevelSuspension is required due to feed inactivity
     * 
     * Must be executed from within a synchronized(eventDetails) block
     */

    public void runTimerRelatedTradingRules(EventDetails eventDetails, long now) {
        EventState eventState = eventDetails.getEventState();
        /*
         * first execute the triggerParamfind logic
         */
        if (algoManagerProperties.isUsingParamFindTradingRules()) {
            runTriggerParamFindTradingRules(eventDetails, now);
        }
        /*
         * now do the eventLevelSuspension logic. Only applies in play
         */
        if (eventState.getMatchState().preMatch()) {
            /*
             * pre-match keep timeOfLastPriceUpdate set to current time, otherwise when first goes in play markets will
             * immediately suspend the first time the rule is run
             */
            eventDetails.setTimeOfLastPriceUpdate(now);
        } else {
            EventSettings eventSettings = eventDetails.getEventSettings();
            MonitorFeedTradingRuleResult result = tradingRulesManager.applyEventSuspensionRules(
                            eventDetails.getSupportedSport(), now, eventDetails.isEventSuspendedDuetoTradingRules(),
                            eventDetails.getTimeOfLastMatchIncident(), eventDetails.getTimeOfLastPriceUpdate(),
                            eventState.getMatchState(), eventSettings);
            updateEventLevelSuspensionStatusForTradingRules(eventDetails, result);
        }
    }

    private void updateEventLevelSuspensionStatusForTradingRules(EventDetails eventDetails,
                    MonitorFeedTradingRuleResult result) {

        publishAlertsForMonitorResult(result, eventDetails);

        boolean suspendDueToTradingRule = false;
        if (result != null)
            suspendDueToTradingRule = result.isShouldSuspend();
        if (eventDetails.isEventSuspended()) {
            if (!suspendDueToTradingRule && !eventDetails.isEventSuspendedAwaitingSuccessfulParamFind()) {
                /*
                 * was suspended but shouldn't be, so publish update
                 */
                String reason = String.format(
                                "suspendDueToTradingRule was %b is now %b, suspendDueToFailedParamfind is %b",
                                eventDetails.isEventSuspendedDuetoTradingRules(), suspendDueToTradingRule,
                                eventDetails.isEventSuspendedAwaitingSuccessfulParamFind());
                long eventId = eventDetails.getEventId();
                info("EventId: %d.  Publishing eventLevelSuspensionStatus: false due to trading rules.  Reason: %s. Result from rule: %s",
                                eventId, reason, result);
                this.publishEventSuspensionStatusSafely(eventId, false, null);
            }
        } else {
            if (suspendDueToTradingRule) {
                /*
                 * not currently suspended but should be, so publish update
                 */
                String reason = String.format(
                                "suspendDueToTradingRule was %b is now %b, suspendDueToFailedParamfind is %b",
                                eventDetails.isEventSuspendedDuetoTradingRules(), suspendDueToTradingRule,
                                eventDetails.isEventSuspendedAwaitingSuccessfulParamFind());
                long eventId = eventDetails.getEventId();
                info("EventId: %d.  Publishing eventLevelSuspensionStatus: true due to trading rules.  Reason: %s. Result from rule: %s",
                                eventId, reason, result);
                this.publishEventSuspensionStatusSafely(eventId, true, null);
            }
        }
        eventDetails.setEventSuspendedDuetoTradingRules(suspendDueToTradingRule);
    }

    private void publishAlertsForMonitorResult(MonitorFeedTradingRuleResult result, EventDetails eventDetails) {

        if (result != null) {

            if (result.getAlertActionForPrices() != null) {
                TraderAlert traderAlert = null;
                switch (result.getAlertActionForPrices().getTraderAlertType()) {
                    case INPUT_PRICES_MISSING_WARNING:
                        if (!eventDetails.isPublishedPricesDelayWarning()) {
                            traderAlert = new TraderAlert(result.getAlertActionForPrices().getTraderAlertType(),
                                            String.valueOf(System.currentTimeMillis()), null);
                            eventDetails.setPublishedPricesDelayWarning(true);
                        }
                        break;
                    case INPUT_PRICES_MISSING_DANGER:
                        if (!eventDetails.isPublishedPricesDelayDanger()) {
                            traderAlert = new TraderAlert(result.getAlertActionForPrices().getTraderAlertType(),
                                            String.valueOf(System.currentTimeMillis()), null);
                            eventDetails.setPublishedPricesDelayDanger(true);
                        }
                    default:
                        break;
                }
                if (traderAlert != null) {
                    publishTraderAlertSafely(eventDetails.getEventId(), traderAlert);
                }
            }

            if (result.getAlertActionForIncidents() != null) {
                TraderAlert traderAlert = null;
                switch (result.getAlertActionForIncidents().getTraderAlertType()) {
                    case INPUT_INCIDENT_MISSING_WARNING:
                        if (!eventDetails.isPublishedIncidentDelayWarning()) {
                            traderAlert = new TraderAlert(result.getAlertActionForIncidents().getTraderAlertType(),
                                            String.valueOf(System.currentTimeMillis()), null);
                            eventDetails.setPublishedIncidentDelayWarning(true);
                        }
                        break;
                    case INPUT_INCIDENT_MISSING_DANGER:
                        if (!eventDetails.isPublishedIncidentDelayDanger()) {
                            traderAlert = new TraderAlert(result.getAlertActionForIncidents().getTraderAlertType(),
                                            String.valueOf(System.currentTimeMillis()), null);
                            eventDetails.setPublishedIncidentDelayDanger(true);
                        }

                    default:
                        break;
                }
                if (traderAlert != null) {
                    publishTraderAlertSafely(eventDetails.getEventId(), traderAlert);
                }
            }

            if (eventDetails.isPublishedPricesDelayDanger() && eventDetails.isPublishedPricesDelayWarning()) {
                eventDetails.setPublishedPricesDelayDanger(false);
                eventDetails.setPublishedPricesDelayWarning(false);

                debug("EventID: %s. Resetting alert flags for pricing due to no Alert Action within result",
                                eventDetails.getEventId());
            } else if (eventDetails.isPublishedIncidentDelayDanger()
                            && eventDetails.isPublishedIncidentDelayWarning()) {
                eventDetails.setPublishedIncidentDelayDanger(false);
                eventDetails.setPublishedIncidentDelayWarning(false);

                debug("EventID: %s. Resetting alert flags for incidents due to no Alert Action within result",
                                eventDetails.getEventId());
            }
        } else if (eventDetails.isPublishedPricesDelayDanger() && eventDetails.isPublishedPricesDelayWarning()) {
            eventDetails.setPublishedPricesDelayDanger(false);
            eventDetails.setPublishedPricesDelayWarning(false);
            debug("EventID: %s. Resetting alert flags for pricing due to Monitor Trading Rule having no result",
                            eventDetails.getEventId());
        } else if (eventDetails.isPublishedIncidentDelayDanger() && eventDetails.isPublishedIncidentDelayWarning()) {
            eventDetails.setPublishedIncidentDelayDanger(false);
            eventDetails.setPublishedIncidentDelayWarning(false);
            debug("EventID: %s. Resetting alert flags for incidents due to Monitor Trading Rule having no result",
                            eventDetails.getEventId());
        }

    }

    /*
     * suspensionCause is non-null ONLY IF an ActiveMQ error caused the suspension
     */
    private void updateEventLevelSuspensionStatusForPendingParamFind(EventDetails eventDetails,
                    boolean shouldSuspendPendingSuccessfulParamFind, Set<MarketGroup> marketGroups,
                    EventSuspendedReason suspensionCause) {
        if (eventDetails.isEventSuspended()) {
            if (!shouldSuspendPendingSuccessfulParamFind && !eventDetails.isEventSuspendedDuetoTradingRules()) {
                /*
                 * was suspended but shouldn't be, so publish update
                 */
                String reason = String.format("suspendDueToParamFind was %b is now %b, suspendDueToTradingRule is %b",
                                eventDetails.isEventSuspendedAwaitingSuccessfulParamFind(),
                                shouldSuspendPendingSuccessfulParamFind,
                                eventDetails.isEventSuspendedDuetoTradingRules());

                long eventId = eventDetails.getEventId();
                info("EventId: %d.  Publishing eventLevelSuspensionStatus: false due to params change.  Reason: %s",
                                eventId, reason);
                this.publishEventSuspensionStatusSafely(eventId, false, null);
            }
        } else {
            if (shouldSuspendPendingSuccessfulParamFind) {
                /*
                 * not currently suspended but should be, so publish update
                 */
                String reason = String.format("suspendDueToParamFind was %b is now %b, suspendDueToTradingRule is %b",
                                eventDetails.isEventSuspendedAwaitingSuccessfulParamFind(),
                                shouldSuspendPendingSuccessfulParamFind,
                                eventDetails.isEventSuspendedDuetoTradingRules());
                long eventId = eventDetails.getEventId();
                info("EventId: %d.  Publishing eventLevelSuspensionStatus: true.  Reason: %s", eventId, reason);
                this.publishEventSuspensionStatusSafely(eventId, true, marketGroups);
            }
        }
        eventDetails.setEventSuspendedAwaitingSuccessfulParamFind(shouldSuspendPendingSuccessfulParamFind);
        if (suspensionCause == EventSuspendedReason.ACTIVEMQ_FAILURE)
            eventDetails.setEventSuspendDueToDisaster(true);
    }

    private TriggerParamFindTradingRulesResult runTriggerParamFindTradingRules(EventDetails eventDetails,
                    long timeStamp) {
        EventState eventState = eventDetails.getEventState();
        long eventId = eventDetails.getEventId();
        eventState.getMatchState().setEventId(eventId);
        if (eventDetails.isParamFindInProgress()) {
            info("EventId: %d. Param find in progress so no need to param find", eventId);
            return new TriggerParamFindTradingRulesResult(false, "Param find in progress.  No action taken");
        }
        if (!eventDetails.isAtLeastOnePriceCalcCompleted()) {
            info("EventId: %d. First price calc not yet completed.  No action taken", eventId);
            return new TriggerParamFindTradingRulesResult(false,
                            "First price calc not yet completed.  No action taken");
        }
        boolean paramFindRequested = false;
        String actionReason = null;
        EventSettings eventSettings = eventDetails.getEventSettings();
        /*
         * trigger pf asap if either suspended awaiting param find or suspended following event creation
         */
        boolean triggerParamFindAsap = eventDetails.isEventSuspendedAwaitingSuccessfulParamFind()
                        || !eventState.shouldPublishAnyMarkets();

        eventState.getMatchState().setEventId(eventId);

        MarketPricesList activePrices = tradingRulesManager.applyTriggerParamFindRequiredRules(
                        eventDetails.getSupportedSport(), eventState.getMatchState(), eventState.getMarkets(),
                        eventState.getMatchParams().isBiased(), eventDetails.getTriggerParamFindData(), eventSettings,
                        triggerParamFindAsap, eventDetails.isEventSuspendDueToDisaster());

        TriggerParamFindData triggerParamFindData = eventDetails.getTriggerParamFindData();
        if (activePrices != null) {
            if (!activePrices.getMarketPricesList().isEmpty()) {
                paramFindRequested = true;
                actionReason = triggerParamFindData.getLogEntry();
                info("EventId: %d.  Pf triggered. Reason: %s", eventId, actionReason);
                executeParamFindRequest(eventId, eventDetails, activePrices, timeStamp);
            }
        } else {
            /*
             * periodically log the status of the pf related data
             */
            actionReason = triggerParamFindData.getLogEntry();
            if (triggerParamFindData.timeToMakeLogEntry()) {
                info("EventId: %d.  Pf not triggered for a while.  Most recent reason: %s. Time since last PF: %s",
                                eventId, actionReason, triggerParamFindData.getParamFindsCache().getLastPfTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date resultdate = new Date(triggerParamFindData.getParamFindsCache().getLastPfTime());

                ParamFindResults paramInfoResults = new ParamFindResults();
                paramInfoResults.addResultSummaryInfo(true, ParamFindResultsStatus.BLUE,
                                actionReason + ". Time since last completed PF: " + sdf.format(resultdate));

                paramInfoResults.convertTraderPfResultsDescriptionToLegacyDescription();

                MatchParams matchParams = eventDetails.getEventState().getMatchParams();

                debug("EventId: " + eventId + ". One minute param find update for trader = " + paramInfoResults);

                debug("EventId: " + eventId + ". Last market Prices used for param find = "
                                + triggerParamFindData.getLastUsedMarketPricesList());

                publishParamFindResultsSafely(eventId, paramInfoResults, matchParams.generateGenericMatchParams(), 0);

            }
        }

        // if
        // (eventDetails.getTriggerParamFindData().timeElapsedSinceLastParamFind()
        // >
        // elapsedTimeBeforeConsecutiveAlertTriggered) {
        //
        // debug("EventId: %d. 10 minutes sinces last param find. Trigger alert
        // for trader", eventDetails.getEventId());
        // TraderAlert traderAlert = new
        // TraderAlert(TraderAlertType.CONSECUTIVE_FAILED_PARAMS,
        // String.valueOf(System.currentTimeMillis()));
        // publishTraderAlertSafely(eventDetails.getEventId(), traderAlert);
        // }

        return new TriggerParamFindTradingRulesResult(paramFindRequested, actionReason);
    }
    
    public void handleRemoveEvent(long eventId) {
    	handleRemoveEvent(eventId, false);
    }

    /**
     * removes the stated event from the list being managed by AlgoManager
     *
     * @param eventId
     */
    public void handleRemoveEvent(long eventId, boolean purge) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d. handleRemoveEvent called for non-existent event.", eventId);
            return;
        }
        synchronized (eventDetails) {
            EventState eventState = eventDetails.getEventState();
            if (eventState != null) {
                MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();
                if (marketsAwaitingResult.size() > 0) {
                    error("EventId: %d. for sport %s is completed but there are still markets that have not been resulted.  Unresulted markets: %s",
                                    eventId, eventDetails.getSupportedSport().toString(),
                                    marketsAwaitingResult.toString());
                } else {
                    /*
                     * publish the most recent set of ResultedMarkets that have not yet been pushed out. Since
                     * ResultedMarkets is cumulative only need to push out the final set.
                     */
                    if (!algoManagerProperties.isPublishResultedMarketsImmediately()
                                    && eventDetails.getEventStateHistory() != null
                                    && eventDetails.getEventStateHistory().getMostRecentEventStateFromHistory() != null
                                    && eventDetails.getEventStateHistory().getMostRecentEventStateFromHistory()
                                                    .getResultedMarkets() != null) {
                        ResultedMarkets resultedMarkets = eventDetails.getEventStateHistory()
                                        .getMostRecentEventStateFromHistory().getResultedMarkets();
                        this.publishResultedMarketsSafely(eventId, resultedMarkets);
                    }
                    info("EventId: %d. handleRemoveEvent.  All markets for this event correctly resulted", eventId);
                }
            }
            if (!purge) {
            	eventDetails.getQueue().removePendingWorkForEvent();
            }
            eventList.remove(eventId);
            
            if (eventDetails.getSupportedSport().equals(SupportedSportType.OUTRIGHTS))
                outrightsMonitor.removeOutrightsEvent(eventId);
        }
        eventDetails = null;
    }

    /**
     * Any resultedMarkets that have not already been published get published, and any markets awaiting result are
     * settled as void. The event is then removed from the list being managed by AlgoManager.
     * 
     * @param eventId
     * @param doNotVoidMarkets - set to true to avoid voiding unresulted markets
     */
    public void handleAbandonEvent(long eventId, boolean doNotVoidMarkets) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d. handleAbandonEvent called for non-existent event.", eventId);
            return;
        }
        synchronized (eventDetails) {
            long now = System.currentTimeMillis();
            if (eventDetails.isPriceCalcInProgress()) {
                /*
                 * need to wait until the current priceCalc is finished
                 */
                this.info("EventId: %d. Queuing handleAbandonEvent: ", eventId);
                if (doNotVoidMarkets)
                    eventDetails.getQueue().addAbandonEventWithNoResulting(eventId, now);
                else
                    eventDetails.getQueue().addAbandonEventWithVoidResulting(eventId, now);
            } else {
                processAbandonEvent(eventId, doNotVoidMarkets, now);
            }
        }
    }

    /**
     * must be called from within a synchronized (eventDetails) block
     * 
     * @param eventId
     */
    private void processAbandonEvent(long eventId, boolean doNotVoidMarkets, long requestTime) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d. processAbandonEvent called for non-existent event.", eventId);
            return;
        }
        try {
            this.info("EventId %d. processAbandonEvent", eventId);
            EventState eventState = eventDetails.getEventState();

            if (eventDetails.isUseExternalModel()) {
                PriceCalcRequest request = PriceCalcRequest.generateRequestForAbandonMatch(eventId,
                                eventDetails.getEventSettings(), eventDetails.getMatchFormat(),
                                eventState.getMatchState().generateSimpleMatchState(), eventState.getMatchParams(),
                                eventState.getMatchEngineSavedState(), requestTime);
                info("EventId: %d. Scheduling external price calc for MATCH_RESULT. UniqueRequestId: %s. API version: %s.",
                                eventId, request.getUniqueRequestId(), request.getVersionId());
                if (algoManagerProperties.isPublishOutputOfRequestsAndResponses()
                                && eventDetails.isUseExternalModel()) {
                    info("EventId: %d. External price calc (manual result) for uniqueRequestId %s. Request: %s",
                                    eventId, request.getUniqueRequestId(), request);
                }
                eventDetails.setPriceCalcInProgress(request);
                algoManagerConfiguration.scheduleExternalPriceCalc(request);
            } else
                completeProcessAbandonEvent(eventDetails, doNotVoidMarkets, null);
        } catch (Exception e) {
            error("EventId: %d. Error in processAbandonEvent: %s", eventId, e);
        }
    }

    private void completeProcessAbandonEvent(EventDetails eventDetails, boolean doNotVoidMarkets,
                    ResultedMarkets clientResultedMarkets) {
        long eventId = eventDetails.getEventId();
        try {
            EventState eventState = eventDetails.getEventState();
            /*
             * only result markets if the algo manager property is set to true - c.f Jira ticket BFAM-420
             */
            if (algoManagerProperties.isDoNotVoidMarketsOnAbandonEvent()) {
                info("EventId: %d was abandoned.  No markets were resulted because system property doNotVoidMarketsOnAbandonEvent is set to true",
                                eventId);
            } else if (doNotVoidMarkets) {
                info("EventId: %d was abandoned.  No markets were resulted because boolean param doNotVoidMarkets is set to true",
                                eventId);
            } else {
                processResultedMarkets(eventDetails, clientResultedMarkets, true);
                publishResultedMarketsSafely(eventId, eventState.getResultedMarkets());
                MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();
                if (marketsAwaitingResult.size() > 0) {
                    Markets markets = marketsAwaitingResult.getAllMarketsAwaitingResult();
                    warn("EventId: %d was abandoned but there are still markets that have not been resulted.  Unresulted markets: %s",
                                    eventId, eventDetails.getSupportedSport().toString(),
                                    marketsAwaitingResult.toString());
                    markets.forEach(x -> {
                        if (x.getMarketGroup() == MarketGroup.INDIVIDUAL) {
                            LocalDateTime now = LocalDateTime.now();
                            warn("EventId: %d will not complete in after %s due to  %s not resulted ", eventId,
                                            x.toString(), now.toString());
                            return;
                        }
                    });
                } else {
                    info("EventId: %d was abandoned.  All markets resulted. ", eventId);
                }
            }
            /*
             * generate an empty set of markets and publish the set of discontinued mkts
             */
            Markets opMarkets = new Markets();
            deriveAndPublishMarkets(opMarkets, eventId, eventDetails, null);
            /*
             * check whether any unresulted markets
             */

            /*
             * notify event complete
             */
            publishNotifyEventCompletedSafely(eventId, true);

        } catch (Exception ex) {
            error("EventId: %d.  Problem in completeProcessAbandonEvent", eventDetails.getEventId());
            error(ex);
        } finally {
            eventDetails.getQueue().removePendingWorkForEvent();
            eventList.remove(eventId);
            debug("Created trader alert for eventID %d due to abandon event", eventId);
            String abandonMsg;
            if (eventDetails.getEventState().getMarketsAwaitingResult().size() > 0)
                abandonMsg = "Event abandoned. There are unresulted markets requiring manual settlement";
            else
                abandonMsg = "Event abandoned. ";
            TraderAlert traderAlert = new TraderAlert(TraderAlertType.ABANDONED_EVENT, abandonMsg, null);
            publishTraderAlertSafely(eventId, traderAlert);
            if (eventDetails.getSupportedSport().equals(SupportedSportType.OUTRIGHTS))
                outrightsMonitor.removeOutrightsEvent(eventId);
        }
    }

    /**
     * sets the matchParams for this event. If this method is called while a paramFind is in progress, then the params
     * supplied in this call may be overwritten by the results of the paramFind
     *
     * @param eventId
     * @param genericMatchParams
     */
    public void handleSetMatchParams(GenericMatchParams genericMatchParams) {
        long eventId = genericMatchParams.getEventId();
        long now = System.currentTimeMillis();
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null)
            return;
        synchronized (eventDetails) {
            if (eventDetails.isPriceCalcInProgress()) {
                eventDetails.getQueue().addSetMatchParams(genericMatchParams, now,
                                CalcRequestCause.PARAMS_CHANGED_BY_TRADER);
            } else {
                processSetMatchParams(genericMatchParams, now, CalcRequestCause.PARAMS_CHANGED_BY_TRADER);
            }
        }
    }

    private void processSetMatchParams(GenericMatchParams genericMatchParams, long requestTime,
                    CalcRequestCause calcRequestCause) {
        long eventId = genericMatchParams.getEventId();

        this.info("EventId: %d. processSetMatchParams. Cause: %s. Params: %s", eventId, calcRequestCause.toString(),
                        genericMatchParams);
        try {
            EventDetails eventDetails = getEventDetails(eventId);
            if (eventDetails == null)
                return;
            EventState eventState = eventDetails.getEventState();
            eventState.paramsChanged(eventState.getMatchParams(), genericMatchParams, false);
            eventDetails.getEventStateHistory().getMostRecentEventStateFromHistory()
                            .paramsChanged(eventState.getMatchParams(), genericMatchParams, false);
            eventState.setMatchParams(genericMatchParams);
            /*
             * reset the suspend flag following a param change
             */
            eventState.setMatchIncident(null);
            eventState.setMatchIncidentResult(null);

            /*
             * Trader saves params - set marketPricesList to null so as to allow for more param finds if needed.
             */
            if (calcRequestCause.equals(CalcRequestCause.PARAMS_CHANGED_BY_TRADER))
                eventDetails.getTriggerParamFindData().setLastUsedMarketPricesList(null);

            eventDetails.getEventStateHistory().getMostRecentEventStateFromHistory().setMatchParams(genericMatchParams);
            publishAndCalcPrices(eventId, calcRequestCause, eventDetails, requestTime);
        } catch (Exception ex) {
            error("EventId: %d.. Problem in processSetMatchParams: %s", eventId, genericMatchParams);
            error(ex);
        }
    }

    /**
     * reverts to the specified state.
     * 
     * @param eventId
     * @param incidentId
     * @return true if successful revert, false if failed. null if can't execute revert immediately so unable to tell
     *         whether succeeded or failed
     */
    public Boolean handleRevertToStatePreceedingRequestId(long eventId, String incidentId) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleRevertToStatePreceedingRequestId.  No eventDetails available for this event",
                            eventId);
            return false;
        }
        synchronized (eventDetails) {
            if (eventDetails.isPriceCalcInProgress()) {
                eventDetails.getQueue().addRevertToEarlierEventState(eventId, incidentId, System.currentTimeMillis());
                return null;
            } else {
                RevertEventState revertEventStateAttempt = eventDetails.getEventStateHistory().rollBackToBefore(
                                incidentId, algoManagerProperties.isPublishResultedMarketsImmediately());
                if (revertEventStateAttempt == null) {
                    this.info("EventId: %d. handleRevertToEarlierState. IncidentId: %s.  Can't revert - no such incidentId or resultedMarkets already published",
                                    eventId, incidentId);
                    scheduleAnyPendingWork(eventDetails);
                    return false;
                }
                EventState revertEventState = revertEventStateAttempt.getEventState();
                if (revertEventStateAttempt.getNonUndoableIncidents() != null) {
                    this.info("EventId: %d. handleRevertToEarlierState. IncidentId: %s.  Contains non undo able incidents that need to be re-played.",
                                    eventId, incidentId);
                    for (MatchIncident nonUndoableIncidentEntry : revertEventStateAttempt.getNonUndoableIncidents())
                        eventDetails.getQueue().addMatchIncident(nonUndoableIncidentEntry,
                                        (long) System.currentTimeMillis());
                }

                this.info("EventId: %d. handleRevertToEarlierState. IncidentId: %s.  Reverting to event state associated with this incidentId",
                                eventId, incidentId);
                debug(revertEventState.getMatchState());
                EventState currentEventState = eventDetails.getEventState();
                MatchState currentMatchState = currentEventState.getMatchState();
                boolean eventWasNotifiedCompleted = currentMatchState.isMatchCompleted();
                boolean stateWasDatafeedStateMismatch = currentMatchState.isDatafeedStateMismatch();
                eventDetails.setEventState(revertEventState.copy());
                Set<String> currentlyPublishedMarketKeys = currentEventState.getPublishedMarketKeys();
                eventDetails.getEventState().setPublishedMarketKeys(currentlyPublishedMarketKeys);
                /*
                 * reset the mismatch flag since we are undoing the incident
                 */
                revertEventState.getMatchState().setDatafeedStateMismatch(false);
                if (eventWasNotifiedCompleted) {
                    publishNotifyEventCompletedSafely(eventId, false);
                }
                if (stateWasDatafeedStateMismatch) {
                    /*
                     * we were out of sync, but now either recovering or back in sync
                     */
                    TraderAlert traderAlert = new TraderAlert(TraderAlertType.MATCH_STATE_MISMATCH_CLEARING, "", null);
                    publishTraderAlertSafely(eventId, traderAlert);
                    eventDetails.getEventState().getMatchState().setClearingAlertHasBeenActivated(true);
                    debug("This is an undo - we are reverting back to the previous state and starting the clearing of mistamch for event = "
                                    + eventId);
                }
                GenericMatchParams matchParams = revertEventState.getMatchParams();
                publishMatchParamsSafely(eventId, matchParams);
                MatchState matchState = revertEventState.getMatchState();
                debug(matchState);
                publishMatchStateSafely(eventId, matchState, eventDetails.getEventSettings().getEventTier());
                Markets markets = revertEventState.getMarkets();
                ResultedMarkets resultedMarkets = revertEventState.getResultedMarkets();
                if (algoManagerProperties.isPublishResultedMarketsImmediately()) {
                    this.publishResultedMarketsSafely(eventId, resultedMarkets);
                }
                deriveAndPublishMarkets(markets, eventId, eventDetails, null);
                this.publishEventStateSafely(eventId, revertEventState);
                eventDetails.getTriggerParamFindData().getMatchIncidentResultCache().revertToEarlierState(incidentId);
                publishRecordedItemSafely(eventId, RecordedItem.revertToEarlierState(eventId, incidentId),
                                eventDetails.nextRecordedItemSequenceId());
            }
            scheduleAnyPendingWork(eventDetails);
        }

        return true;
    }

    /**
     * older version of method retained for backwards compatability
     * 
     * @param eventId
     * @return
     */
    public Boolean handleUndoLastMatchIncident(long eventId) {
        return handleUndoMatchIncident(eventId, null);
    }

    /**
     * 
     * 
     * @param eventId
     */
    /**
     * undoes just the most recent matchIncident
     * 
     * @param eventId
     * @return true if successful revert, false if failed. null if can't execute revert immediately so unable to tell
     *         whether succeeded or failed
     */
    public Boolean handleUndoMatchIncident(long eventId, MatchIncident incident) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleUndoLastMatchIncident.  No eventDetails available for this event", eventId);
            return false;
        }
        Boolean undo = false;
        synchronized (eventDetails) {
            if (eventDetails.isPriceCalcInProgress()) {
                eventDetails.getQueue().addUndoMatchIncident(eventId, System.currentTimeMillis(), incident);
                return null;
            } else {
                String lastIncidentRequestId;
                if (incident == null)
                    lastIncidentRequestId = eventDetails.getEventStateHistory().getPreviousIncidentId();
                else {
                    Object incidentSubType = getSubTypeToUndo(incident);
                    lastIncidentRequestId = eventDetails.getEventStateHistory().getPreviousIncidentId(incidentSubType);
                }
                if (lastIncidentRequestId == null) {
                    this.info("EventId: %d. handleUndoLastMatchIncident requested but can't undo - eventStateHistory is empty",
                                    eventId);
                } else {
                    this.info("EventId: %d. handleUndoLastMatchIncident.  Reverting to incidentId: %s", eventId,
                                    lastIncidentRequestId);
                    undo = handleRevertToStatePreceedingRequestId(eventId, lastIncidentRequestId);
                }
            }
        }
        return undo;
    }

    /**
     * If FootballMatchIncident then returns the subtype of the incident to undo. Otherwise returns null. This is to
     * deal with a sequence of incidents such as {goal, var referral, undo}. At present only known sport where incident
     * being undone may not be the most recent match incident is football. May need to be extended in future for other
     * sports.
     * 
     * 
     * @param incident
     * @return
     */
    private Object getSubTypeToUndo(MatchIncident incident) {
        if (incident.getIncidentSubType() != null) {
            return incident.getIncidentSubType();
        }
        return null;
    }

    private static final int eventTierToTriggerStaticDataCapture = 3;

    /**
     * called when the results of a price calculation arrive back
     * 
     * @param matchEngineSavedState
     * @param matchParams
     */
    private void handlePriceCalcResponse(long eventId, PriceCalcResponse response) {

        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handlePriceCalcResult.  No eventDetails available for this event", eventId);
            return;
        }
        if (!eventDetails.isPriceCalcInProgress()) {
            error("EventId: %d.  handlePriceCalcResponse received for reqId: %s. Unexpected response - no price calc is in progress. Response discarded.",
                            eventId, response.getUniqueRequestId());
            return;
        }
        PriceCalcRequest request = eventDetails.getPriceCalcRequest();
        if (!response.getUniqueRequestId().equals(request.getUniqueRequestId())) {
            /*
             * received response does not match the one expected, so throw away. This may happen following a call to
             * handleResetEvent
             */
            error("EventId: %d.  response received for reqId: %s. Unexpected response - uniqueRequestId's don't match. Response discarded.",
                            eventId, response.getUniqueRequestId());
            return;
        }
        long timeResponseReceived = System.currentTimeMillis();
        synchronized (eventDetails) {
            try {
                /*
                 * check whether this is a response to a MATCH_RESULT or MATCH_ABANDONED request
                 */
                if (request.getCalcRequestCause().equals(CalcRequestCause.MATCH_RESULT)) {
                    if (algoManagerProperties.isPublishOutputOfRequestsAndResponses()
                                    && eventDetails.isUseExternalModel()) {
                        info("EventId: %d. External priceCalcResponse (manual result) for uniqueRequestId %s, %s",
                                        eventId, response.getUniqueRequestId(), response);
                    }
                    completeProcessMatchResult(eventDetails, response.getResultedMarkets());
                    return;
                }

                if (request.getCalcRequestCause().equals(CalcRequestCause.MATCH_ABANDONED)) {
                    if (algoManagerProperties.isPublishOutputOfRequestsAndResponses()
                                    && eventDetails.isUseExternalModel()) {
                        info("EventId: %d. External priceCalcResponse (manual result) for uniqueRequestId %s, %s",
                                        eventId, response.getUniqueRequestId(), response);
                    }
                    completeProcessMatchResult(eventDetails, response.getResultedMarkets());
                    return;
                }

                eventDetails.setAtLeastOnePriceCalcCompleted(true);
                if (response.isTriggerParamFindAsap())
                    eventDetails.getTriggerParamFindData().triggerParamFindAsap();
                Markets markets = response.getMarkets();
                info("EventId: %d.  PriceCalcResponse received for uniqueRequestId: %s from server: %s", eventId,
                                response.getUniqueRequestId(), response.getServerId());

                if (algoManagerProperties.isPublishOutputOfRequestsAndResponses()
                                && eventDetails.isUseExternalModel()) {
                    info("EventId: %d. External priceCalcResponse for uniqueRequestId %s. Response: %s", eventId,
                                    response.getUniqueRequestId(), response);
                }
                RecordedItem recordedItem = RecordedItem.priceCalc(eventDetails.getPriceCalcInProgressStartedTime(),
                                request, response);
                if (request.getCalcRequestCause().equals(CalcRequestCause.NEW_MATCH))
                    recordedItem.addConfiguredProperties(algoManagerProperties.propertiesAsMap(),
                                    SupportedSports.getPropertiesAsMap(eventDetails.getSupportedSport()));
                publishRecordedItemSafely(eventId, recordedItem, eventDetails.nextRecordedItemSequenceId());
                EventState eventState = eventDetails.getEventState();
                /*
                 * Comment out the suspend logic and FIXME when the connection has been tested
                 */
                // if(response.isError()){
                // info("EventId: %d. Publishing eventLevelShouldSuspend to
                // %b.
                // Reason: failed to get the successful
                // response",
                // eventId, true);
                // eventDetails.setEventSuspendedDuetoTradingRules(true);
                //
                // } else {
                // info("EventId: %d. Publishing eventLevelShouldSuspend to
                // %b.
                // Reason: successful response",
                // eventId, false);
                // eventDetails.setEventSuspendedDuetoTradingRules(false);
                // }

                if (eventDetails.isHandlingUndoableIncidentInProgress()) {

                    processResultedMarkets(eventDetails, response.getResultedMarkets(), false);
                }
                if (markets != null) {
                    eventState.setMarkets(markets);
                }
                eventState.getMatchState().setPriceCalcTime();
                int eventTier = (int) eventDetails.getEventSettings().getEventTier();
                if (!response.isFatalError()) {
                    if (!eventDetails.isUseClientTradingRules() && markets != null) {
                        applyDerivedMarketRulesSafely(markets, eventDetails);
                    }
                    /*
                     * decide whether to initialise static data from these markets
                     */
                    DataToGenerateStaticMarkets dataToGenerateStaticMarkets =
                                    eventState.getMatchState().getDataToGenerateStaticMarkets();
                    if ((eventTier <= eventTierToTriggerStaticDataCapture)
                                    && !dataToGenerateStaticMarkets.isInitialisationCompleted()) {
                        if (markets != null) {
                            dataToGenerateStaticMarkets.initialize(markets);
                        }
                    }
                }

                TimeStamp timeStamp = eventDetails.getTimeStamp();
                /*
                 * timeStamp will only be non-null for calcRequests of type MATCH_INCIDENT
                 */
                if (timeStamp != null) {
                    timeStamp.setTimePriceCalcResponseReceived(timeResponseReceived);
                    timeStamp.setTimeUpdatedMarketsPublishedByAlgoManager(System.currentTimeMillis());
                    timeStamp.setTimePriceCalcRequestReceivedByCalculationServer(
                                    response.getTimePriceCalcRequestReceived());
                    timeStamp.setTimePriceCalcResponseIssedByCalculationServer(
                                    response.getTimePriceCalcResponseIssued());
                }
                /*
                 * reset the event level suspend flag if the result of a paramFind
                 */
                CalcRequestCause calcRequestCause = eventDetails.getcalcRequestCause();
                if (calcRequestCause == CalcRequestCause.PARAMS_CHANGED_BY_TRADER)
                    updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, false, null, null);
                else if (calcRequestCause == CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND
                                && eventDetails.isClearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc())
                    updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, false, null, null);
                if (!response.isFatalError() && markets != null)
                    deriveAndPublishMarkets(markets, eventId, eventDetails, timeStamp);

                /*
                 * if matchEngineSavedState is null then has not been updated
                 */
                MatchEngineSavedState matchEngineSavedState = response.getMatchEngineSavedState();
                if (matchEngineSavedState != null) {
                    eventState.setMatchEngineSavedState(matchEngineSavedState);
                }
                /*
                 * if matchParams is null then has not been updated
                 */
                GenericMatchParams outputMatchParams = response.getMatchParams();
                if (outputMatchParams == null) {
                    outputMatchParams = eventState.getMatchParams();
                    warn("EventId: %d, Match params Is empty. Use the default params. and will NOT publish the default one",
                                    eventId);
                } else {
                    eventState.setMatchParams(outputMatchParams);
                    publishMatchParamsSafely(eventId, outputMatchParams);
                }
                if (!response.isFatalError())
                    eventDetails.getStatistics().priceCalcCompleted(eventId, response.getUniqueRequestId(),
                                    request.getRequestTime());
                if (eventDetails.isHandlingUndoableIncidentInProgress()) {
                    eventState.setIncidentId(eventDetails.getIncidentIdInProgress());
                    EventState eventStateCopy = eventState.copy();
                    ResultedMarkets resultedMarketsToPublishNow =
                                    eventDetails.getEventStateHistory().addToHistory(eventStateCopy);
                    if (algoManagerProperties.isPublishResultedMarketsImmediately()) {
                        publishResultedMarketsSafely(eventId, eventState.getResultedMarkets());
                        eventStateCopy.setResultedMarketsPublished(true);
                    } else if (resultedMarketsToPublishNow != null) {
                        /*
                         * if not null then have removed these resultedMarkets the history so need to publish now
                         */
                        publishResultedMarketsSafely(eventId, resultedMarketsToPublishNow);
                    }
                    publishEventStateSafely(eventId, eventStateCopy);
                } else {
                    eventState.setIncidentId(response.getUniqueRequestId());
                    EventState eventStateCopy = eventState.copy();
                    publishEventStateSafely(eventId, eventStateCopy);

                }
                /*
                 * handle any changes to eventSettings
                 */
                EventSettings eventSettings = response.getEventSettings();
                if (eventSettings != null) {
                    /*
                     * at the moment only eventTier is updateable
                     */
                    String eventTierStr = eventSettings.getEventTierStr();
                    if (eventTierStr != null) {
                        Map<String, String> properties = new HashMap<>(1);
                        properties.put("eventTier", eventTierStr);
                        this.processSetEventProperties(eventDetails, properties, true);
                    }
                }

                if (algoManagerProperties.shouldLogAlgoStatistics()) {
                    long now = System.currentTimeMillis();
                    if (now - statisticsLogTimer > TIME_BETWEEN_STATISTICS_LOG) {
                        statisticsLogTimer = now;

                        info("Algomanager statistics");
                        info(getStatistics().toString());

                    }
                }
                /*
                 * if of type outrights then update the watchlist, else notify that a matchIncident was processed
                 */
                if (eventDetails.getSupportedSport().equals(SupportedSportType.OUTRIGHTS)) {
                    MatchEngineSavedState savedState = response.getMatchEngineSavedState();
                    if (savedState instanceof OutrightsWatchList)
                        outrightsMonitor.updateWatchList(eventId, (OutrightsWatchList) savedState);
                } else {
                    outrightsMonitor.notifyMatchIncident(eventId, request.getMatchIncident());
                }

            } catch (Exception ex) {
                error("EventId: %d. Problem in handlePriceCalcResponse", eventId);
                error(ex);
            } finally {
                eventDetails.clearPriceCalcInProgress();
                eventDetails.clearHandlingUndoableIncidentInProgress();

                scheduleAnyPendingWork(eventDetails);
            }
        }
    }

    /**
     * 
     * @param eventDetails
     * @param clientResultedMarkets
     * @param matchAbandoned
     */
    private void processResultedMarkets(EventDetails eventDetails, ResultedMarkets clientResultedMarkets,
                    boolean matchAbandoned) {

        long eventId = eventDetails.getEventId();
        EventState eventState = eventDetails.getEventState();
        ResultedMarkets resultedMarkets = eventState.getResultedMarkets();
        MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();

        ResultedMarkets newResultedMarkets = null;
        if (eventDetails.isUseClientResultingOnly()) {
            /*
             * don't result any markets ourselves if clientResultingOnly is true
             */
            newResultedMarkets = new ResultedMarkets();
        } else {
            if (matchAbandoned)
                newResultedMarkets = resultMarketsForAbandonedEvent(eventState.getMarketsAwaitingResult(),
                                eventDetails.getMatchEngine(), eventState.getMatchState());
            else
                newResultedMarkets = resultMarkets(eventState.getMarketsAwaitingResult(), eventDetails.getMatchEngine(),
                                eventState.getPreviousMatchState(), eventState.getMatchState());
        }

        newResultedMarkets.setIncidentId(eventDetails.getIncidentIdInProgress());

        /*
         * merge any client resulted markets into ours (which will be empty if clientResultingOnly is true. If both
         * result the same market then the client ones take precedence.
         */
        if (clientResultedMarkets != null)
            for (ResultedMarket clientResultedMarket : clientResultedMarkets) {
                if (clientResultedMarket.getLineId() == null) {
                    /*
                     * set the resultedMarket lineId property to the expected default value, which is "";
                     */
                    clientResultedMarket.setLineId("");
                }
                String marketShortKey = clientResultedMarket.getShortKey();
                marketsAwaitingResult.removeEntry(marketShortKey);
                newResultedMarkets.addMarket(clientResultedMarket);
            }

        if (newResultedMarkets.size() > 0) {
            /*
             * add newResultedMarkets to the existing list
             */
            debug("eventId: " + eventId + ". NewResultedMarkets: " + newResultedMarkets);
            ResultedMarket partiallyResulted;
            for (ResultedMarket resultedMarket : newResultedMarkets.getResultedMarkets().values()) {
                /*
                 * Update resulted markets history, treat selections differently
                 */
                if (!resultedMarket.isFullyResulted()) {
                    partiallyResulted = resultedMarkets.getResultedMarkets().get(resultedMarket.getFullKey());
                    if (partiallyResulted != null) {// first time
                                                    // result this
                                                    // market
                                                    // selections
                        partiallyResulted.getLosingSelections().addAll(resultedMarket.getLosingSelections());
                        partiallyResulted.getVoidedSelections().addAll(resultedMarket.getVoidedSelections());

                        resultedMarkets.addMarket(partiallyResulted);
                    } else
                        resultedMarkets.addMarket(resultedMarket);
                } else {
                    /*
                     * If a market is fully resulted, resultedMarket.addMarket will update the market resulting status
                     */
                    /*
                     * Check if we partially resulted this market before
                     */
                    ResultedMarket previouslyResulted =
                                    resultedMarkets.getResultedMarkets().get(resultedMarket.getFullKey());
                    // if(resultedMarket.getFullKey().contains("FT:PRT")){
                    // System.out.println();
                    // }

                    if (previouslyResulted != null && !previouslyResulted.isFullyResulted()) {
                        resultedMarket.getVoidedSelections().addAll(previouslyResulted.getVoidedSelections());
                    }
                    resultedMarkets.addMarket(resultedMarket);
                }
            }
        }
        resultedMarkets.setIncidentId(eventDetails.getIncidentIdInProgress());
    }

    /**
     * to support unit testing and matchRunner. Not threadsafe so should not be used for any production purpose
     * 
     * @param eventId
     */
    public void handleClearParamFindData(long eventId) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleClearParamFindData.  No eventDetails available for this event", eventId);
            return;
        }
        eventDetails.clearTriggerParamFindData();

    }

    private void applyUpdateMarketsPostPriceCalcRulesSafely(Markets markets, EventDetails eventDetails,
                    CalcRequestCause priceCalcCause, TriggerParamFindData triggerParamFindData) {
        try {
            tradingRulesManager.applyUpdateMarketsPostPriceCalcRules(eventDetails.getEventSettings().getEventTier(),
                            eventDetails.getSupportedSport(), eventDetails.getEventState().getMatchState(), markets,
                            priceCalcCause, triggerParamFindData);
        } catch (Exception ex) {
            error("EventId: %d. Error applying trading rules", eventDetails.getEventId());
            error(ex);
        }
    }

    private void applyDerivedMarketRulesSafely(Markets markets, EventDetails eventDetails) {
        try {
            tradingRulesManager.applyDerivedMarketRules(eventDetails.getEventSettings().getEventTier(),
                            eventDetails.getSupportedSport(), eventDetails.getEventState().getMatchState(), markets);
        } catch (Exception ex) {
            error("EventId: %d. Error applying trading rules", eventDetails.getEventId());
            error(ex);
        }
    }

    /**
     * generates the derived markets, publishes everything and updates
     * 
     * @param markets - the base set of markets generated by algCalculator
     * @param eventId
     * @param eventDetails
     * @param timeStamp the timeStamp object if the result of a price calc, else null
     */
    private void deriveAndPublishMarkets(Markets markets, long eventId, EventDetails eventDetails,
                    TimeStamp timeStamp) {
        /*
         * create the new opMarkets object to hold all of the output markets including derived markets
         */
        Markets opMarkets = new Markets();
        EventState eventState = eventDetails.getEventState();
        MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();
        for (Market market : markets) {
            marketsAwaitingResult.addNonDerivedMarket(market);
            if (!algoManagerProperties.isOnlyPublishMarketsFollowingParamChange()
                            || eventState.shouldPublishMarket(market)) {
                /*
                 * add the base market to the set of opMarkets
                 */
                if (!market.getFullKey().contains("AHCP") || eventDetails.isUseExternalModel())
                    opMarkets.addMarketWithFullKey(market);

                if (eventDetails.isUseClientTradingRules()) {
                    /*
                     * do nothing - do not generate derived markets if using client custom rules
                     */
                } else {
                    /*
                     * generate all the derived markets and add to the opMarkets object and to the map of derived
                     * markets awaiting result
                     */
                    DataToGenerateStaticMarkets dataToGenerateStaticMarkets =
                                    eventState.getMatchState().getDataToGenerateStaticMarkets();
                    // debug("Log009: dataToGenerateStaticMarkets is : ",
                    // dataToGenerateStaticMarkets.getMap());
                    List<Market> derivedMarkets = market
                                    .generateAllDerivedMarketsSpecifiedByTradingRules(dataToGenerateStaticMarkets);

                    if (derivedMarkets.size() > 0) {
                        String marketShortKey = market.getShortKey();
                        for (Market derivedMarket : derivedMarkets) {
                            opMarkets.addMarketWithFullKey(derivedMarket);
                            marketsAwaitingResult.addDerivedMarket(marketShortKey, derivedMarket);
                        }
                    }
                }
            }
        }
        /*
         * only publish markets after first successful param find or after params have manually been changed
         */

        if (!algoManagerProperties.isOnlyPublishMarketsFollowingParamChange()
                        || (eventState.shouldPublishAnyMarkets())) {

            boolean shouldPublishCornersAndCards = algoManagerProperties.isOnlyPublishCornersAndCardsMarket()
                            && (eventDetails.getSupportedSport() == SupportedSportType.SOCCER);

            if (shouldPublishCornersAndCards) {
                Set<String> notCcMktsKeySet = new HashSet<String>();
                for (Market m : opMarkets) {
                    // trace("%s %s", m.getFullKey(),
                    // m.getMarketStatus().getSuspensionStatus());
                    boolean ccMarket = (m.getMarketGroup() == MarketGroup.CORNERS)
                                    || (m.getMarketGroup() == MarketGroup.BOOKINGS);
                    if (!ccMarket) {
                        notCcMktsKeySet.add(m.getFullKey());
                    }
                }
                opMarkets.getMarkets().keySet().removeAll(notCcMktsKeySet);
            }
            /*
             * generate the set of markets that were published last time but not this
             */
            Set<String> oldKeySet = eventState.getPublishedMarketKeys();
            Set<String> newKeySet = opMarkets.getMarketKeys();
            // System.out.println("oldKeySet:"+ oldKeySet);
            // System.out.println("newKeySet:"+ newKeySet);
            Set<String> deltaKeySet = SetOps.inSet1NotSet2(oldKeySet, newKeySet);
            eventState.setPublishedMarketKeys(newKeySet);
            /*
             * add required meta data to the opMarkets object
             */
            MarketsMetaData marketsMetaData = new MarketsMetaData();
            for (Market market : opMarkets)
                marketsMetaData.addMarket(market);
            opMarkets.setMarketsMetaData(marketsMetaData);
            CalcRequestCause priceCalcCause = eventDetails.getcalcRequestCause();
            TriggerParamFindData triggerParamFindData = eventDetails.getTriggerParamFindData();
            // FIXME: betfair abandon match bfam420, should apply suspend all
            // here
            if ((eventDetails.getEventState().getMatchIncident() instanceof AbandonMatchIncident)
                            && algoManagerProperties.isDoNotVoidMarketsOnAbandonEvent()) {
                info("EventId: " + eventId + ", apply ResultingAbandonRules");
                tradingRulesManager.applyResultingAbandonRules(eventDetails.getEventSettings().getEventTier(),
                                eventDetails.getSupportedSport(), eventDetails.getEventState().getMatchState(),
                                opMarkets);
                info("EventId: " + eventId + ", markets to be published when abandon match:" + opMarkets.toString());

            }

            applyUpdateMarketsPostPriceCalcRulesSafely(opMarkets, eventDetails, priceCalcCause, triggerParamFindData);
            /*
             * add th filter logic
             */
            Set<String> notCcMktsKeySet = new HashSet<String>();
            for (Market market : opMarkets)
                if (!market.isFilterMarketByClient()) {
                    notCcMktsKeySet.add(market.getFullKey());
                }
            opMarkets.getMarkets().keySet().removeAll(notCcMktsKeySet);
            for (Market market : opMarkets)
                marketsMetaData.addMarket(market);
            opMarkets.setMarketsMetaData(marketsMetaData);
            eventState.setDeltaKeySet(deltaKeySet);
            if (timeStamp != null)
                eventState.setLastTimeStamp(timeStamp);
            if (!paramsAreDefault(eventDetails) || eventDetails.isUseExternalModel()) {
                String currentSequenceId = chooseSportSpecificSequenceId(eventDetails);
                publishMarketsSafely(eventId, opMarkets, deltaKeySet, timeStamp, currentSequenceId);
            } else
                info("EventId: " + eventId
                                + ", Markets not published because all params are set to their default values");
        } else {
            debug("EventId: " + eventId + ". Should not yet publish markets");
        }
    }

    /*
     * returns true if all params used in the most recent price calc are set to their default values.
     */
    private boolean paramsAreDefault(EventDetails eventDetails) {
        GenericMatchParams currentMatchParams = eventDetails.getEventState().getMatchParams();
        MatchParams matchParams = eventDetails.getMatchEngine().getMatchParams();
        if (matchParams instanceof TennisMatchParams) {
            /*
             * logic in this method doesn't work because params may be renamed e.g. onServePctA1 and onServePctA
             */
            return false;
        }
        GenericMatchParams defaultMatchParams = matchParams.generateGenericMatchParams();
        defaultMatchParams.updateParamMapForEventTier(eventDetails.getEventSettings().getEventTier());
        for (Entry<String, MatchParam> e : currentMatchParams.getParamMap().entrySet()) {
            MatchParam currentMatchParam = e.getValue();
            MatchParam defaultMatchParam = defaultMatchParams.getParamMap().get(e.getKey());
            if (defaultMatchParam != null) {
                if (currentMatchParam.getGaussian().getMean() != defaultMatchParam.getGaussian().getMean())
                    return false;
            }
        }
        return true;
    }

    private void handlePriceCalcError(long eventId, String uniqueRequestId, String errorCause) {

        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handlePriceCalcError.  No eventDetails available for this event", eventId);
            return;
        }
        if (!eventDetails.isPriceCalcInProgress()) {
            error("EventId: %d.  handlePriceCalcError received for reqId: %s. Unexpected error - no price calc is in progress. Error discarded.",
                            eventId, uniqueRequestId);
            return;
        }
        if (!uniqueRequestId.equals(eventDetails.getPriceCalcRequest().getUniqueRequestId())) {
            /*
             * received response does not match the one expected, so throw away. This may happen following a call to
             * handleResetEvent
             */
            error("EventId: %d.  handlePriceCalcError received for reqId: %s. Unexpected error - uniqueRequestId's don't match. Error discarded.",
                            eventId, uniqueRequestId);
            return;
        }
        error("EventId: %d. Fatal price calc error for uniqueRequestid: %s.  Cause: %s", eventId, uniqueRequestId,
                        errorCause);
        info("Jin, before entering synchronized");
        synchronized (eventDetails) {
            eventDetails.getStatistics().priceCalcAbandoned(eventId, uniqueRequestId);
            info("EventId: %d.  Ready to suspend event for a PendingParamFind.", eventId);
            /*
             * Suspend event if fatal error happens, un-suspend only when we get a successful param find
             */

            TraderAlert traderAlert = new TraderAlert(TraderAlertType.CONNECTION_BROKEN,
                            String.valueOf(System.currentTimeMillis()), null);
            publishTraderAlertSafely(eventId, traderAlert);

            this.updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, true, null,
                            EventSuspendedReason.ACTIVEMQ_FAILURE);
            publishNotifyFatalErrorSafely(eventId, uniqueRequestId, null);
            publishRecordedItemSafely(eventId,
                            RecordedItem.priceCalcError(eventDetails.getPriceCalcInProgressStartedTime(),
                                            eventDetails.getPriceCalcRequest()),
                            eventDetails.nextRecordedItemSequenceId());
            eventDetails.clearPriceCalcInProgress();
            scheduleAnyPendingWork(eventDetails);
        }
    }

    /**
     * result this event based on the contents of the matchResultMap.
     * 
     * Can be called anytime but only likely to be able successfully to result all markets if called pre-match.
     * 
     * The publishMarkets methods is invoked with an empty set of Markets and the list of keys for all discontinued
     * markets
     * 
     * All resultedMarkets are published. An error is logged if any markets awaiting result are not resulted.
     * 
     * The publishNotifyMatchCompleted method gets invoked with value "true"
     * 
     * @param matchResultMap - the list of properties
     */
    public void handleManualResultMarkets(MatchResultMap matchResultMap) {
        long eventId = matchResultMap.getEventId();
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleManualResultMarkets.  no eventDetails available for this event", eventId);
            return;
        }
        synchronized (eventDetails) {
            long now = System.currentTimeMillis();
            if (eventDetails.isPriceCalcInProgress()) {
                /*
                 * wait until the current priceCalc is finished
                 */
                this.info("EventId %d. Queuing resultMarketsManually: %s", eventId, matchResultMap.toShortString());
                eventDetails.getQueue().addResultMarketsManually(eventId, matchResultMap, now);
            } else {
                processManualResultMarkets(eventId, matchResultMap, now);
            }
        }
    }

    private void processManualResultMarkets(long eventId, MatchResultMap matchResultMap, long requestTime) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  processManualResultMarkets.  No eventDetails available for this event", eventId);
            return;
        }
        try {
            this.info("EventId %d. processManualResultMarkets from user %s.  ManualResult: %s", eventId,
                            matchResultMap.getUserId(), matchResultMap.toShortString());
            EventState eventState = eventDetails.getEventState();
            MatchState startMatchState = eventState.getMatchState();
            MatchResulter matchResulter = eventDetails.getMatchFormat().generateMatchResulter();
            MatchState finalMatchState =
                            matchResulter.generateMatchStateForMatchResult(startMatchState, matchResultMap, false);
            eventState.setPreviousMatchState(startMatchState);
            eventState.setMatchState(finalMatchState);
            if (eventDetails.isUseExternalModel()) {
                PriceCalcRequest request = PriceCalcRequest.generateRequestForMatchResult(eventId,
                                eventDetails.getEventSettings(), eventDetails.getMatchFormat(),
                                finalMatchState.generateSimpleMatchState(), eventState.getMatchParams(),
                                eventState.getMatchEngineSavedState(), matchResultMap.extractMatchResult(),
                                requestTime);
                info("EventId: %d. Scheduling external price calc for MATCH_RESULT. UniqueRequestId: %s. API version: %s.",
                                eventId, request.getUniqueRequestId(), request.getVersionId());
                if (algoManagerProperties.isPublishOutputOfRequestsAndResponses()
                                && eventDetails.isUseExternalModel()) {
                    info("EventId: %d. External price calc (manual result) for uniqueRequestId %s. Request: %s",
                                    eventId, request.getUniqueRequestId(), request);
                }
                eventDetails.setPriceCalcInProgress(request);
                algoManagerConfiguration.scheduleExternalPriceCalc(request);
            } else
                completeProcessMatchResult(eventDetails, null);
        } catch (Exception e) {
            error("EventId: " + eventId + ". Error in handleManualResultMarkets", e);
        }
    }

    private void completeProcessMatchResult(EventDetails eventDetails, ResultedMarkets clientResultedMarkets) {
        try {
            long eventId = eventDetails.getEventId();
            EventState eventState = eventDetails.getEventState();
            processResultedMarkets(eventDetails, clientResultedMarkets, false);
            publishResultedMarketsSafely(eventId, eventState.getResultedMarkets());
            MarketsAwaitingResult marketsAwaitingResult = eventState.getMarketsAwaitingResult();
            if (marketsAwaitingResult.size() > 0) {
                Markets markets = marketsAwaitingResult.getAllMarketsAwaitingResult();
                warn("EventId: %d. for sport %s was manually resulted but there are still markets that have not been resulted.  Unresulted markets: %s",
                                eventId, eventDetails.getSupportedSport().toString(), marketsAwaitingResult.toString());
                markets.forEach(x -> {
                    if (x.getMarketGroup() == MarketGroup.INDIVIDUAL) {
                        LocalDateTime now = LocalDateTime.now();
                        warn("EventId: %d will not complete in after %s due to  %s not resulted ", eventId,
                                        x.toString(), now.toString());
                        return;
                    }
                });
            } else {
                info("EventId: %d. for sport %s was manually resulted.  All markets resulted. ", eventId,
                                eventDetails.getSupportedSport().toString());
            }

            eventState.setCompleted(true);
            eventState.setTimeCompleted(new Date());

            /*
             * generate an empty set of markets and publish the set of discontinued mkts
             */
            Markets opMarkets = new Markets();
            deriveAndPublishMarkets(opMarkets, eventId, eventDetails, null);

            /*
             * notify event complete
             */
            publishNotifyEventCompletedSafely(eventId, true);

        } catch (Exception ex) {
            error("EventId: %d.  Problem in handleManualResultMarkets", eventDetails.getEventId());
            error(ex);
        } finally {
            if (!eventDetails.isPriceCalcInProgress())
                scheduleAnyPendingWork(eventDetails);
        }
    }

    /**
     * checks the queue and if anything held up pending completion of a price calc releases it. Should only be called
     * from within a synchronised (eventDetails) block
     *
     * @param eventId
     */
    private void scheduleAnyPendingWork(EventDetails eventDetails) {
        PendingAlgoWork work = eventDetails.getQueue().removeFirstForEvent();
        if (work != null) {
            long eventId = eventDetails.getEventId();
            try {
                info("EventId: %d. Dequeuing pending work %s", eventId, work);
                switch (work.getAlgoWorkType()) {
                    case MATCH_INCIDENT:
                        processMatchIncident(work.getMatchIncident(), work.getRequestTime());
                        break;
                    case SET_MATCH_PARAMS_MANUAL:
                        processSetMatchParams(work.getMatchParams(), work.getRequestTime(),
                                        CalcRequestCause.PARAMS_CHANGED_BY_TRADER);
                        break;
                    case SET_MATCH_PARAMS_AUTO:
                        processSetMatchParams(work.getMatchParams(), work.getRequestTime(),
                                        CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND);
                        break;
                    case UNDO_INCIDENT:
                        handleUndoLastMatchIncident(work.getEventId());
                        break;
                    case TIMER_CALC_REQUEST:
                        handleDelayedPublishResultedMarkets(eventDetails, work.getRequestTime());
                        break;
                    case RESULT_MARKETS_MANUALLY:
                        this.processManualResultMarkets(work.getEventId(), work.getMatchManualResult(),
                                        work.getRequestTime());
                        break;
                    case REVERT_TO_EARLIER_EVENT_STATE:
                        this.handleRevertToStatePreceedingRequestId(work.getEventId(), work.getRequestId());
                        break;
                    case ABANDON_EVENT_WITH_VOID_RESULTING:
                        this.processAbandonEvent(work.getEventId(), false, work.getRequestTime());
                    case ABANDON_EVENT_WITH_NO_RESULTING:
                        this.processAbandonEvent(work.getEventId(), true, work.getRequestTime());
                    default:
                        break;
                }
            } catch (Exception ex) {
                error("EventId: %d. Problem handling work: %s ", eventId, work);
                error(ex);
            }
        }
    }

    /**
     * called when a set of competitor market prices has been received
     * 
     * @param marketPricesList
     * @return descriptive string explaining what action if any was taken by the trading rules
     */
    public TriggerParamFindTradingRulesResult handleSupplyMarketPrices(long eventId,
                    MarketPricesList marketPricesList) {
        long now = System.currentTimeMillis();
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleSupplyMarketPrices.  No eventDetails available for this event", eventId);
            return new TriggerParamFindTradingRulesResult(false, "No event details available for this event");
        }

        this.info("EventId: %d. handleSupplyMarketPrices: %s", eventId, marketPricesList);
        if (marketPricesList.getMarketPricesList().isEmpty()) {
            warn("EventId: %d.  Supplied marketPricesList is empty.", eventId);
            return new TriggerParamFindTradingRulesResult(false, "Supplied marketPricesList is empty");
        }
        synchronized (eventDetails) {

            eventDetails.setTimeOfLastPriceUpdate(now);
            EventState eventState = eventDetails.getEventState();
            marketPricesList.convertSequenceIdsToAlgoMgrStdAndFilter(eventState.getMatchState());
            checkSuppliedPrices(eventId, marketPricesList, eventDetails.getEventState().getMarkets());
            eventDetails.getTriggerParamFindData().getMarketPricesCache().addPricesToCache(marketPricesList, now);
            debug("EventId: " + eventId + ", suppliedMarketPrices = " + marketPricesList);
            TriggerParamFindTradingRulesResult result =
                            runTriggerParamFindTradingRules(eventDetails, System.currentTimeMillis());
            return result;
        }
    }

    /**
     * checks that the supplied marketPrices match markets that we generate plus updates the timestamp on each
     * marketPrice
     * 
     * @param eventId
     * @param marketPricesList
     * @param markets
     */
    private void checkSuppliedPrices(long eventId, MarketPricesList marketPricesList, Markets markets) {
        long now = System.currentTimeMillis();
        EventDetails eventDetails = getEventDetails(eventId);
        for (Entry<String, MarketPrices> e : marketPricesList.getMarketPricesList().entrySet()) {
            String source = e.getKey();
            for (Entry<String, MarketPrice> e2 : e.getValue().getMarketPrices().entrySet()) {
                MarketPrice marketPrice = e2.getValue();
                marketPrice.setTimeStamp(now);
                String marketType = marketPrice.getType();
                String sequenceId = marketPrice.getSequenceId();
                if (markets.get(marketType, sequenceId) == null && !eventDetails.isUseClientParamFinder()) {
                    marketPrice.setValid(false);
                    warn("EventId: %d. MarketPrice (source: %s, type: %s, seqId: %s) is not matched by any market we currently generate",
                                    eventId, source, marketType, sequenceId);
                }
                if (!marketPrice.sanityCheckPrice() && !eventDetails.isUseClientParamFinder()) {
                    marketPrice.setValid(false);
                    warn("EventId: %d.  %s %s", eventId, marketPrice.getSanityCheckFailureReason(),
                                    marketPrice.toString());
                }
            }
        }

    }

    private void executeParamFindRequest(long eventId, EventDetails eventDetails, MarketPricesList marketPricesList,
                    long requestTime) {
        EventState eventState = eventDetails.getEventState();
        GenericMatchParams matchParams = eventState.getMatchParams();
        eventDetails.setPreMatchWhenParamFindStarted(eventState.getMatchState().preMatch());

        CalcModelType calcModelType;
        if (eventDetails.isUseExternalModel())
            calcModelType = CalcModelType.EXTERNAL_MODEL;
        else
            calcModelType = CalcModelType.INTEGRATED_MODEL;

        boolean useClientParamFinder = eventDetails.isUseClientParamFinder();

        EventSettings eventSettings = eventDetails.getEventSettings();
        if (useClientParamFinder && eventDetails.isUseExternalModel()) {
            ParamFindRequest request = new ParamFindRequest(eventId, eventSettings, calcModelType, null,
                            eventDetails.getMatchFormat(), eventState.getMatchState().generateSimpleMatchState(),
                            matchParams, marketPricesList, eventState.getMatchEngineSavedState(), requestTime);

            info("EventId: %d.  Scheduling external param find for uniqueRequestId %s. API version: %s.", eventId,
                            request.getUniqueRequestId(), request.getVersionId());

            if (algoManagerProperties.isPublishOutputOfRequestsAndResponses() && eventDetails.isUseExternalModel()) {
                info("EventId: %d. External param find for uniqueRequestId %s. Request: %s", eventId,
                                request.getUniqueRequestId(), request);
            }
            addCacheEntry(eventDetails.getTriggerParamFindData(), request.getUniqueRequestId());
            eventDetails.setParamFindInProgress(request);
            algoManagerConfiguration.scheduleExternalParamFind(request);
        } else {
            ParamFindRequest request = new ParamFindRequest(eventId, eventSettings, calcModelType,
                            eventDetails.getMatchEngine().getClass().getName(), eventDetails.getMatchFormat(),
                            eventState.getMatchState(), matchParams, marketPricesList,
                            eventState.getMatchEngineSavedState(), requestTime);
            info("EventId: %d.  Scheduling param find for uniqueRequestId %s.", eventId, request.getUniqueRequestId());
            addCacheEntry(eventDetails.getTriggerParamFindData(), request.getUniqueRequestId());
            eventDetails.setParamFindInProgress(request);
            Class<? extends AbstractParamFinder> clazz =
                            SupportedSports.getParamFinderClass(eventDetails.getSupportedSport());
            if (clazz == null)
                algoManagerConfiguration.scheduleParamFind(request);
            else
                algoManagerConfiguration.scheduleParamFind(request, clazz);
        }
    }

    private void addCacheEntry(TriggerParamFindData triggerParamFindData, String uniqueRequestId) {
        triggerParamFindData.getParamFindsCache().setPfRequested(uniqueRequestId, System.currentTimeMillis());
    }

    /**
     * called when the results of a param find arrive back
     */
    void handleParamFindResponse(long eventId, ParamFindResponse response) {
        /*
         * if the param find succeeded then the local copy of matchParams is updated and a price calculation scheduled
         * via priceCalcScheduler. Downstream listeners are notified of the updated parameter set via the
         * publishMatchParams function
         *
         */
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  handleParamFindResult.  No eventDetails available for this event", eventId);
            return;
        }
        if (!eventDetails.isParamFindInProgress()) {
            error("EventId: %d.  handleParamFindResponse received for reqId: %s. Unexpected response - no param find is in progress. Response discarded.",
                            eventId, response.getUniqueRequestId());
            return;
        }
        ParamFindRequest request = eventDetails.getParamFindRequest();
        String uniqueRequestId = response.getUniqueRequestId();
        if (!uniqueRequestId.equals(request.getUniqueRequestId())) {
            /*
             * received response does not match the one expected, so throw away. This may happen following a call to
             * handleResetEvent
             */
            error("EventId: %d.  handleParamFindResponse received for reqId: %s. Unexpected response - uniqueRequestId's don't match. Response discarded.",
                            eventId, uniqueRequestId);
            return;
        }
        synchronized (eventDetails) {
            ParamFindResults results = response.getParamFindResults();
            long elapsedTimeMs = System.currentTimeMillis() - request.getRequestTime();
            double duration = ((double) elapsedTimeMs) / 1000.0;
            info("EventId: %d. ParamFindResponse received for uniqueRequestId: %s after %.1f seconds from server: %s. Status: %s.",
                            eventId, uniqueRequestId, duration, response.getServerId(),
                            results.getParamFindResultsStatus().toString());
            if (algoManagerProperties.isPublishOutputOfRequestsAndResponses() && eventDetails.isUseExternalModel()) {
                info("EventId: %d. External paramFindresponse for uniqueRequestId %s. Response: %s", eventId,
                                uniqueRequestId, response);
            }

            long paramFindStartTime = eventDetails.getParamFindInProgressStartedTime();
            publishRecordedItemSafely(eventId,
                            RecordedItem.paramFind(paramFindStartTime, eventDetails.getParamFindRequest(), response),
                            eventDetails.nextRecordedItemSequenceId());
            eventDetails.clearParamFindInProgress();
            EventState eventState = eventDetails.getEventState();
            eventDetails.getTriggerParamFindData().getParamFindsCache().setPfCompleted(response.getUniqueRequestId(),
                            results);
            EventStatisticsCollector eventStats = eventDetails.getStatistics();
            eventStats.paramFindCompleted(eventId, uniqueRequestId, request.getRequestTime());

            boolean paramError = false;

            GenericMatchParams matchParams = response.getGenericMatchParams();
            matchParams.setEventId(eventId);

            for (MatchParam matchParam : matchParams.getParamMap().values()) {
                double pValue = matchParam.getGaussian().getMean();
                if (pValue < matchParam.getMinAllowedParamValue() || pValue > matchParam.getMaxAllowedParamValue()) {
                    paramError = true;
                    error("Event id: %d.  Following param find MatchParams are outside allowed min max values. Match Param causing issue = : %s",
                                    eventId, matchParam);
                    break;
                }
            }
            MatchState matchState = eventState.getMatchState();
            if (results.isSuccess() && !paramError) {

                publishMatchParamsSafely(eventId, matchParams);

                // if (eventDetails.isEventSuspendDueToDisaster()) {
                info("EventID " + eventDetails.getEventId() + ". Event Suspend Status Reset.");
                eventDetails.setEventSuspendDueToDisaster(false);
                // }
                eventDetails.getTriggerParamFindData().setLastSuccessfulTraderParamFindResultsDescription(
                                response.getParamFindResults().getTraderParamFindResultsDescription());

                eventState.paramsChanged(eventState.getMatchParams(), matchParams, true);
                eventState.setMatchParams(matchParams);
                /*
                 * apply ParamFinding trading rules
                 */
                if (!eventDetails.isPreMatchWhenParamFindStarted()) {
                    if (matchState.getNoSuccessfulInPlayParamFindsExecuted() == 0) {
                        debug("Created trader alert for eventID %d due to 1st inplay PF", eventId);
                        TraderAlert traderAlert = new TraderAlert(TraderAlertType.FIRST_INPLAY_PF,
                                        String.valueOf(System.currentTimeMillis()), null);
                        publishTraderAlertSafely(eventId, traderAlert);
                    }

                    matchState.incrementNoSuccessfulInPlayParamFindsExecuted();
                }
                tradingRulesManager.applyResetBiasFollowingParamFindRules(eventDetails.getSupportedSport(), matchState,
                                matchParams);
                /*
                 * if awaiting a successful pf to unsuspend the only set the clear suspension flag if the param find was
                 * initiated after the most recent match incident
                 */
                if (eventDetails.isEventSuspendedAwaitingSuccessfulParamFind())
                    if (paramFindStartTime > eventDetails.getTimeOfLastMatchIncident())
                        eventDetails.setClearEventSuspendedAwaitingSuccessfulParamFindAfterPriceCalc(true);
                long now = System.currentTimeMillis();
                if (response.isDoNotSchedulePriceCalc()) {
                    /*
                     * do nothing
                     */
                } else {
                    if (eventDetails.isPriceCalcInProgress()) {
                        eventDetails.getQueue().addSetMatchParams(matchParams.generateGenericMatchParams(), now,
                                        CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND);
                    } else {
                        processSetMatchParams(matchParams, now, CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND);
                    }
                }
                /*
                 * temporary logging for stats collection
                 */
                if (eventDetails.getSupportedSport().equals(SupportedSportType.SOCCER)) {
                    double gt = getParamValue(matchParams, "goalTotal");
                    double gs = getParamValue(matchParams, "goalSupremacy");
                    double gb = getParamValue(matchParams, "targetGoalBoost");
                    info("MaxS stats eventId: %d, goalTotal: %.3f, goalSupremacy: %.3f, targetGoalBoost: %.3f", eventId,
                                    gt, gs, gb);
                }
            }
            /*
             * handle the ParamFindResult legacy issue here
             */
            results.convertTraderPfResultsDescriptionToLegacyDescription();
            publishParamFindResultsSafely(eventId, results, matchParams.generateGenericMatchParams(), elapsedTimeMs);

            /*
             * if should suspend then suspend existing markets. Otherwise the suspension status will be checked when the
             * following priceCalcResult is handled
             */
            if (results.isShouldSuspendMarkets())
                this.updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, true,
                                results.getMarketGroupsToSuspend(), null);

        }
    }

    private double getParamValue(GenericMatchParams matchParams, String key) {
        MatchParam p = matchParams.getParamMap().get(key);
        double x = -999.9;
        if (p != null) {
            x = p.getGaussian().getMean();
        }
        return x;
    }

    private void handleParamFindError(long eventId, String uniqueRequestId, String errorCause) {

        synchronized (eventList) {
            EventDetails eventDetails = getEventDetails(eventId);
            if (eventDetails == null) {
                error("EventId: %d.  handleParamFindError.  No eventDetails available for this event", eventId);
                return;
            }

            if (!eventDetails.isParamFindInProgress()) {
                error("EventId: %d.  handleParamFindError received for reqId: %s. Unexpected error - no param find is in progress. Error discarded.",
                                eventId, uniqueRequestId);
                return;
            }
            if (!uniqueRequestId.equals(eventDetails.getParamFindRequest().getUniqueRequestId())) {
                /*
                 * received response does not match the one expected, so throw away. This may happen following a call to
                 * handleResetEvent
                 */
                error("EventId: %d.  handleParamFindError received for reqId: %s. Unexpected error - uniqueRequestId's don't match. Error discarded.",
                                eventId, uniqueRequestId);
                return;
            }
            error("EventId: %d.  Fatal param find error for : %s", eventId, uniqueRequestId);
            /*
             * Suspend event if fatal error happens, un-suspend only when we get a successful param find
             */
            TraderAlert traderAlert = new TraderAlert(TraderAlertType.CONNECTION_BROKEN,
                            String.valueOf(System.currentTimeMillis()), null);
            publishTraderAlertSafely(eventId, traderAlert);

            this.updateEventLevelSuspensionStatusForPendingParamFind(eventDetails, true, null,
                            EventSuspendedReason.ACTIVEMQ_FAILURE);
            eventDetails.getStatistics().paramFindAbandoned(eventId, uniqueRequestId);
            eventDetails.getTriggerParamFindData().getParamFindsCache().setPfFatalError(uniqueRequestId);
            publishNotifyFatalErrorSafely(eventId, uniqueRequestId, errorCause);
            publishRecordedItemSafely(eventId,
                            RecordedItem.paramFindError(eventDetails.getParamFindInProgressStartedTime(),
                                            eventDetails.getParamFindRequest()),
                            eventDetails.nextRecordedItemSequenceId());
            eventDetails.clearParamFindInProgress();

        }
    }

    /**
     * Return the event details to obtain useful read only properties
     * 
     * @param eventId
     * @return
     */
    public EventDetails getEventDetails(long eventId) {
        EventDetails eventDetails = eventList.get(eventId);
        return eventDetails;
    }

    public List<EventDetails> getEvents() {
        return Lists.newArrayList(eventList.values());
    }

    /**
     * publishes matchParams and matchState and schedules a price calc
     *
     * @param eventDetails
     * @param matchIncidentResult
     * @param
     */
    private void publishAndCalcPrices(long eventId, CalcRequestCause calcRequestCause, EventDetails eventDetails,
                    long requestTime) {
        EventState eventState = eventDetails.getEventState();
        MatchState matchState = eventState.getMatchState();
        publishMatchStateSafely(eventId, matchState, eventDetails.getEventSettings().getEventTier());

        eventState.setResultedMarketsPublished(false);// ensure this flag has
                                                      // been reset
        EventSettings eventSettings = eventDetails.getEventSettings();
        if (eventDetails.isUseExternalModel()) {
            PriceCalcRequest request = new PriceCalcRequest(eventId, eventSettings, null, calcRequestCause,
                            eventDetails.getMatchFormat(), matchState.generateSimpleMatchState(),
                            eventState.getMatchParams(), eventState.getMatchIncident(),
                            eventState.getMatchIncidentResult(), eventState.getMatchEngineSavedState(), requestTime);
            info("EventId: %d. Scheduling external price calc for uniqueRequestId: %s. CalcRequestCause: %s. API version: %s.",
                            eventId, request.getUniqueRequestId(), calcRequestCause.toString(), request.getVersionId());
            if (algoManagerProperties.isPublishOutputOfRequestsAndResponses() && eventDetails.isUseExternalModel()) {
                info("EventId: %d. External priceCalc for uniqueRequestId %s. Request: %s", eventId,
                                request.getUniqueRequestId(), request);
            }
            eventDetails.setPriceCalcInProgress(request);
            algoManagerConfiguration.scheduleExternalPriceCalc(request);
        } else {
            PriceCalcRequest request = new PriceCalcRequest(eventId, eventSettings,
                            eventDetails.getMatchEngine().getClass().getName(), calcRequestCause,
                            eventDetails.getMatchFormat(), matchState, eventState.getMatchParams(),
                            eventState.getMatchIncident(), eventState.getMatchIncidentResult(),
                            eventState.getMatchEngineSavedState(), requestTime);
            info("EventId: %d.  Scheduling price calc for uniqueRequestId: %s. CalcRequestCause: %s.", eventId,
                            request.getUniqueRequestId(), calcRequestCause.toString());
            eventDetails.setPriceCalcInProgress(request);
            Class<? extends AbstractPriceCalculator> clazz =
                            SupportedSports.getPriceCalculatorClass(eventDetails.getSupportedSport());

            if (clazz == null)
                algoManagerConfiguration.schedulePriceCalc(request);
            else
                algoManagerConfiguration.schedulePriceCalc(request, clazz);
        }
    }

    @SuppressWarnings("unused")
    /**
     * utility method can be added into code when needed
     * 
     * @param obj
     */
    private void printForDocumentation(Object obj) {
        System.out.println(JsonUtil.marshalJson(obj, true));
    }

    /**
     * should be called when AlgoManager is finished with - to remove threadpools etc
     */
    public void close() {
        algoManagerConfiguration.close();
        killTimer();
    }

    /**
     * NOT for general use.
     * 
     * For use by Matchrunner only until can get rid of the dependencies on matchState.nextPrompt() etc.
     * 
     * @param eventId
     * @return
     */
    public MatchState getMatchState(long eventId) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  getMatchState.  No eventDetails available for this event", eventId);
            return null;
        } else {
            return eventDetails.getEventState().getMatchState();
        }

    }

    /**
     * returns a snapshot of the current stats
     *
     * @return
     */
    public AlgoManagerStatistics getStatistics() {
        AlgoManagerStatistics stats;
        synchronized (eventList) {
            stats = algoManagerStatistics.getSnapshot();
        }
        return stats;
    }

    /**
     * helper method for supportin unit tests
     * 
     * @param eventId
     * @return
     */
    public boolean paramFindInProgress(long eventId) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null)
            return false;
        else

            return eventDetails.isParamFindInProgress();
    }

    private String chooseSportSpecificSequenceId(EventDetails eventDetails) {
        String sequenceId = null;
        SupportedSportType sport = eventDetails.getSupportedSport();

        switch (sport) {
            case TENNIS:
                sequenceId = eventDetails.getEventState().getMatchState().getSequenceId("S1.1.1", 0);
                break;
            default:
                break;
        }

        return sequenceId;
    }

    /**
     * May optionally be used to override the default set of trading rules for the given sport, or the non-sport
     * specific set of rules of supportedSportType is null
     *
     * @param supportedSportType the sport to which these rules apply. If null then the rules are assumed to apply to
     *        all sports
     * @param tradingRulesArray the set of tradingRules to use
     */
    public void setTradingRules(SupportedSportType supportedSportType, AbstractTradingRule[] tradingRuleArray) {
        synchronized (tradingRulesManager) {
            info("Trading rules overriden for sport : %s", supportedSportType.toString());
            tradingRulesManager.setTradingRules(supportedSportType, tradingRuleArray);
        }
    }

    /**
     * May optionally be used to override the default set of trading rules for the given sport, or the non-sport
     * specific set of rules of supportedSportType is null
     * 
     * @param supportedSportType the sport to which these rules apply. If null then the rules are
     * @param tradingRules the set of tradingRules to use
     */
    public void setTradingRules(SupportedSportType supportedSportType, TradingRules tradingRules) {
        ArrayList<AbstractTradingRule> tradingRulesList = tradingRules.getTradingRules();
        int n = tradingRulesList.size();
        AbstractTradingRule[] tradingRuleArray = tradingRulesList.toArray(new AbstractTradingRule[n]);
        setTradingRules(supportedSportType, tradingRuleArray);
    }

    /**
     * sets the default set of trading rules by collecting them from each sport and consolidating into a single list
     */
    private void setDefaultTradingRules() {
        /*
         * add the rules that are not sport specific
         */
        tradingRulesManager.setGenericTradingRules(new GenericTradingRuleSet());
        for (SupportedSportType supportedSportType : SupportedSports.sportsList.keySet()) {
            TradingRules tradingRuleSet = SupportedSports.getTradingRulesSet(supportedSportType);
            if (tradingRuleSet != null)
                tradingRulesManager.setTradingRules(supportedSportType, tradingRuleSet);
        }
    }

    private void publishResultedMarketsSafely(long eventId, ResultedMarkets resultedMarkets) {
        try {
            info("EventId: %d, incidentId: %s.  Publishing resulted markets: %s", eventId,
                            resultedMarkets.getIncidentId(), resultedMarkets);
            publisher.publishResultedMarkets(eventId, resultedMarkets);
            outrightsMonitor.addResultedMarketsOnWatchLists(eventId, resultedMarkets);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing resulted markets for event", eventId);
            error(ex);
        }
    }

    private void publishNotifyEventCompletedSafely(long eventId, boolean isCompleted) {
        try {
            info("EventId: %d.  Publishing eventComnpleted: %B", eventId, isCompleted);
            publisher.notifyEventCompleted(eventId, isCompleted);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing notifyEventCompleted", eventId);
            error(ex);
        }
    }

    private void publishMatchParamsSafely(long eventId, GenericMatchParams matchParams) {
        try {
            info("EventId: %d.  Publishing matchParams: %s", matchParams.getEventId(), matchParams);
            publisher.publishMatchParams(eventId, (GenericMatchParams) matchParams.copy());
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing matchParams", matchParams.getEventId());
            error(ex);
        }
    }

    private void publishMatchStateSafely(long eventId, MatchState matchState, long eventTier) {
        try {
            info("EventId: %d, Publishing complete MatchState: %s", eventId, matchState);
            SimpleMatchState sms = (SimpleMatchState) matchState.generateSimpleMatchState(eventTier);
            sms.setIncidentId(matchState.getIncidentId());
            sms.setEventId(eventId);
            info("EventId: %d, Publishing simpleMatchState: %s", eventId, sms);
            publisher.publishMatchState(eventId, sms);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing simpleMatchState", eventId);
            error(ex);
        }

    }

    private void publishRecordedItemSafely(long eventId, RecordedItem recordedItem, int sequenceId) {
        try {
            recordedItem.setEventId(eventId);
            recordedItem.setSequenceId(sequenceId);
            publisher.publishRecordedItem(eventId, recordedItem);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing recorded item", eventId);
            error(ex);
        }
    }

    private void publishMarketsSafely(long eventId, Markets markets, Set<String> deltaKeySet, TimeStamp timeStamp,
                    String currentSequenceId) {
        try {

            info("EventId: %d.  SequenceId: %s. Publishing markets: %s", eventId, currentSequenceId, markets);
            info("EventId: %d.  Publishing deltaKeySet: %s", eventId, deltaKeySet);
            info("EventId: %d.  Publishing timeStamp: %s", eventId, timeStamp);

            publisher.publishMarkets(eventId, markets, deltaKeySet, timeStamp, currentSequenceId);
            outrightsMonitor.addMarketsOnWatchLists(eventId, markets);

        } catch (Exception ex) {
            error("EventId: %d. Problem publishing markets", eventId);
            error(ex);
        }
    }

    private void publishParamFindResultsSafely(long eventId, ParamFindResults results,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {
        try {
            info("EventId: %d.  Publishing paramFindResults: %s", eventId, results);
            info("EventId: %d.   Publishing genericMatchParams: %s", eventId, genericMatchParams);
            publisher.publishParamFindResults(eventId, results, genericMatchParams, elapsedTimeMs);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing paramFindResults", eventId);
            error(ex);
        }
    }

    private void publishNotifyFatalErrorSafely(long eventId, String uniqueRequestId, String errorCause) {
        try {
            info("EventId: %d, uniqueRequestId: %s.  Publishing notifyFatalError %s", eventId, uniqueRequestId,
                            errorCause);
            publisher.notifyFatalError(eventId, uniqueRequestId, errorCause);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing notifyFatalError", eventId);
            error(ex);

        }
    }

    private void publishTraderAlertSafely(long eventId, TraderAlert traderAlert) {
        try {
            info("EventId: %d.  Publishing traderAlert %s", eventId, traderAlert);
            publisher.publishTraderAlert(eventId, traderAlert);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing notifyTraderAlert", eventId);
            error(ex);
        }
    }

    private void publishEventStateSafely(long eventId, EventStateBlob eventStateBlob) {
        try {
            info("EventId: %d.  Publishing eventStateBlob for incidentId %s", eventId, eventStateBlob.getIncidentId());
            publisher.publishEventState(eventId, eventStateBlob);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing eventStateBlob for incidentId %s", eventId,
                            eventStateBlob.getIncidentId());
            error(ex);
        }
    }

    private void publishEventSuspensionStatusSafely(long eventId, boolean suspensionStatus,
                    Set<MarketGroup> marketGroups) {
        try {
            info("EventId: %d.  Publishing EventSuspensionStatus %s for marketGroups", eventId, suspensionStatus,
                            marketGroups);
            publisher.publishEventSuspensionStatus(eventId, suspensionStatus, marketGroups);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing eventSuspensionStatus ", eventId);
            error(ex);
        }
    }

    private void publishEventPropertiesSafely(long eventId, Map<String, String> eventProperties) {
        try {
            info("EventId: %d.  Publishing updated eventProperties: %s", eventId,
                            JsonUtil.marshalJson(eventProperties));
            publisher.publishEventProperties(eventId, eventProperties);
        } catch (Exception ex) {
            error("EventId: %d. Problem publishing eventProperties ", eventId);
            error(ex);
        }
    }

    public void setMatchState(long eventId, MatchState matchState) {
        EventDetails eventDetails = getEventDetails(eventId);
        if (eventDetails == null) {
            error("EventId: %d.  getMatchState.  No eventDetails available for this event", eventId);
        } else {
            eventDetails.getEventState().setMatchState(matchState);
            /*
             * also do the rest of resulting post match state updated
             */
            eventDetails.setHandlingUndoableIncidentInProgress("000");
            PriceCalcRequest request = new PriceCalcRequest(eventId, eventDetails.getEventSettings(),
                            eventDetails.getMatchEngine().getClass().getName(), CalcRequestCause.TIMER,
                            eventDetails.getMatchFormat(), matchState, eventDetails.getEventState().getMatchParams(),
                            eventDetails.getEventState().getMatchIncident(),
                            eventDetails.getEventState().getMatchIncidentResult(),
                            eventDetails.getEventState().getMatchEngineSavedState(), System.currentTimeMillis());
            info("EventId: %d.  Scheduling price calc for uniqueRequestId: %s. CalcRequestCause: %s.", eventId,
                            request.getUniqueRequestId(), "Match State Updated");
            eventDetails.setPriceCalcInProgress(request);
            algoManagerConfiguration.schedulePriceCalc(request);
        }
    }

    public boolean isUseMarginChart() {
        return useMarginChart;
    }

    public void setUseMarginChart(boolean useMarginChart) {
        this.useMarginChart = useMarginChart;
    }



}
