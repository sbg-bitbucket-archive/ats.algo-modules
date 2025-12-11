package ats.algo.integration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.AlgoManagerStatistics;
import ats.algo.algomanager.EventDetails;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.cache.AlgoManagerCacheService;
import ats.algo.core.MarketGroup;
import ats.algo.core.SportMetaData;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.SimpleMatchState.Source;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsDescription;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.betting.betsync.dto.FeedBetsyncDto;
import ats.betting.betsync.out.AbstractBetsyncOutListener;
import ats.betting.betsync.out.BetsyncOutWebsocketClient;
import ats.betting.betsync.out.LoginInterceptor;
import ats.betting.betsync.service.BetsyncService;
import ats.core.util.StringUtil;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.sports.betsync.in.feed.BetsyncInRestApiClient;
import generated.ats.betsync.dto.Attribute;
import generated.ats.betsync.dto.Attributes;
import generated.ats.betsync.dto.BookmakersPrices;
import generated.ats.betsync.dto.Event;
import generated.ats.betsync.dto.EventState;
import generated.ats.betsync.dto.EventTradingState;
import generated.ats.betsync.dto.EventTradingStateUpdate;
import generated.ats.betsync.dto.IncidentFeedAvailabilityNotification;
import generated.ats.betsync.dto.LoginRequest;
import generated.ats.betsync.dto.MarketGroupState;
import generated.ats.betsync.dto.MarketGroupsStates;
import generated.ats.betsync.dto.MarketState;
import generated.ats.betsync.dto.Match;
import generated.ats.betsync.dto.MatchDetails;
import generated.ats.betsync.dto.MatchResultNotification;
import generated.ats.betsync.dto.Notification;
import generated.ats.betsync.dto.NotificationMessage;
import generated.ats.betsync.dto.RepublishMatchStatesNotification;
import generated.ats.betsync.dto.RevertMatchStateNotification;
import generated.ats.betsync.dto.Setting;
import generated.ats.betsync.dto.SubscribeResponse;

/**
 * Key class to handle communication between ATS and the Algo Manager. All interaction is through the Betsync-out and
 * Betsync-in APIs.
 */
public class AlgoMgrBetsyncCoordinator extends AbstractBetsyncOutListener
                implements AlgoManagerPublishable, LoginInterceptor {
    private BetsyncOutWebsocketClient websocketClient;
    private Map<Long, TradedEvent> events = Maps.newConcurrentMap();
    private AlgoManager algoManager;
    private BetsyncService betsyncService;
    private AlgoManagerConfiguration algoManagerConfiguration;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    private AtsMarketTypeMapper atsMarketTypeMapper;
    private AlgoStatsPublisher algoStatsPublisher;
    private RecordedItemArchiveHandler recordedItemArchiveHandler;

    private boolean loggedBreach;
    private int algoWorkQueueDumpSize = 15;
    private int newMarketsToBetsyncBatchSize = 60;
    private SettingChangedHandler settingChangedHandler;
    private BookieParamfindHelper bookieParamfindHelper = new BookieParamfindHelper();
    private TraderAlertBuilder traderAlertBuilder = new TraderAlertBuilder();
    private ResultedMarketsHelper resultedMarketsHelper;
    private MarketCreationHelper marketCreationHelper;
    private MarketPricesHelper marketPricesHelper;
    private AlgoManagerCacheService algoManagerCacheService;

    public void init() throws Exception {
        algoManager = new AlgoManager(algoManagerConfiguration, this);

        settingChangedHandler = new SettingChangedHandler(algoManager);

        resultedMarketsHelper = new ResultedMarketsHelper(atsMarketTypeMapper);

        marketCreationHelper = new MarketCreationHelper(atsMarketTypeMapper);

        marketPricesHelper = new MarketPricesHelper(atsMarketTypeMapper);

        traderAlertBuilder.setAlgoManager(algoManager);

        executorService.scheduleWithFixedDelay(this::cleanUpCompletedEvents, 30, 15, TimeUnit.MINUTES);
        executorService.scheduleWithFixedDelay(this::publishAlgoStats, 80, 15, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(this::publishAlgoTimings, 60, 15, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(this::monitorAlgoQueue, 60, 5, TimeUnit.SECONDS);

        websocketClient.addListener(this);
        websocketClient.setLoginInterceptor(this);
        websocketClient.init();
    }

    public void setNewMarketsToBetsyncBatchSize(int newMarketsToBetsyncBatchSize) {
        this.newMarketsToBetsyncBatchSize = newMarketsToBetsyncBatchSize;
    }

    public void setAlgoManagerCacheService(AlgoManagerCacheService algoManagerCacheService) {
        this.algoManagerCacheService = algoManagerCacheService;
    }

    public void setAlgoWorkQueueDumpSize(int algoWorkQueueDumpSize) {
        this.algoWorkQueueDumpSize = algoWorkQueueDumpSize;
    }

    public void setRecordedItemArchiveHandler(RecordedItemArchiveHandler recordedItemArchiveHandler) {
        this.recordedItemArchiveHandler = recordedItemArchiveHandler;
    }

    public void setAlgoStatsPublisher(AlgoStatsPublisher algoStatsPublisher) {
        this.algoStatsPublisher = algoStatsPublisher;
    }

    public void setAtsMarketTypeMapper(AtsMarketTypeMapper atsMarketTypeMapper) {
        this.atsMarketTypeMapper = atsMarketTypeMapper;
    }

    public void setAlgoManagerConfiguration(AlgoManagerConfiguration algoManagerConfiguration) {
        this.algoManagerConfiguration = algoManagerConfiguration;
    }

    public AlgoManagerStatistics getAlgoStatistics() {
        return algoManager.getStatistics();
    }

    @Override
    public void beforeLogin(LoginRequest request, boolean isReconnection) {
        boolean operational = algoManagerCacheService.isOperational();
        if (events.isEmpty() && operational) {
            return;
        }

        Attributes attributes = request.getAttributes();
        if (attributes == null) {
            attributes = new Attributes();
            request.setAttributes(attributes);
        }

        List<Attribute> attributesList = attributes.getAttributes();

        if (!events.isEmpty()) {
            String csvBookedEventIds = StringUtil.toCsv(events.keySet());
            Attribute bookingsAttribute = new Attribute();
            warn("Looks like we are recovering from a disconnect will provide bookedEventIds %s", csvBookedEventIds);
            bookingsAttribute.setName("bookedEventIds");
            bookingsAttribute.setValue(csvBookedEventIds);
            attributesList.add(bookingsAttribute);
        }

        Attribute eventBlobRecoveryOperationalAttribute = new Attribute();
        eventBlobRecoveryOperationalAttribute.setName("eventBlobRecoveryOperational");
        eventBlobRecoveryOperationalAttribute.setValue(String.valueOf(operational));
        attributesList.add(eventBlobRecoveryOperationalAttribute);
    }

    @Override
    public void onSubscribeResponse(SubscribeResponse snapshot) {
        super.onSubscribeResponse(snapshot);

        List<Match> match = snapshot.getMatch();
        match.forEach(this::processMatch);
    }



    @Override
    public void onNotification(Notification notification) {
        super.onNotification(notification);
        List<Setting> settingsChanged = notification.getSettingsChanged();
        if (settingsChanged != null && !settingsChanged.isEmpty()) {
            handleChangedSettings(settingsChanged);
        }
        List<MatchResultNotification> matchResults = notification.getMatchResults();
        if (matchResults != null && !matchResults.isEmpty()) {
            handleMatchResultsFromBetsync(matchResults);
        }
        RevertMatchStateNotification revertMatchState = notification.getRevertMatchState();
        if (revertMatchState != null) {
            handleRevertMatchStateNotification(revertMatchState);
        }

        RepublishMatchStatesNotification republishMatchStates = notification.getRepublishMatchStates();
        if (republishMatchStates != null) {
            handleRepublishMatchStates(republishMatchStates);
        }

        IncidentFeedAvailabilityNotification incidentFeedAvailability = notification.getIncidentFeedAvailability();
        if (incidentFeedAvailability != null) {
            TradedEvent tradedEvent = events.get(incidentFeedAvailability.getMatchId());
            if (tradedEvent == null) {
                error("handling %s incidentFeedAvailability for unknown match %s ???",
                                incidentFeedAvailability.getFeed(), incidentFeedAvailability.getMatchId());
                return;
            }

            if (tradedEvent.isPrematch()) {
                info("%s incidentFeedAvailability irellevant for %s since its prematch",
                                incidentFeedAvailability.getFeed(), tradedEvent);
                return;
            }

            if (tradedEvent.isCompleted()) {
                info("%s incidentFeedAvailability irellevant for %s since its completed",
                                incidentFeedAvailability.getFeed(), tradedEvent);
                return;
            }

            if (!incidentFeedAvailability.getFeed().equalsIgnoreCase(tradedEvent.getIncidentProvider())) {
                info("%s incidentFeedAvailability irellevant for %s since %s is the incident source",
                                incidentFeedAvailability.getFeed(), tradedEvent, tradedEvent.getIncidentProvider());
                return;
            }

            algoManager.handleIncidentFeedStatus(incidentFeedAvailability.getMatchId(),
                            incidentFeedAvailability.getFeed(), incidentFeedAvailability.isAvailable());
        }
    }

    private void handleRepublishMatchStates(RepublishMatchStatesNotification republishMatchStates) {
        for (String matchIdStr : republishMatchStates.getMatchIds().split(",")) {
            try {
                algoManager.handleRepublishData(Long.parseLong(matchIdStr));
            } catch (Exception ex) {
                error("Problem republishing match %s data", ex, matchIdStr);
            }
        }
    }

    private void handleRevertMatchStateNotification(RevertMatchStateNotification revertMatchState) {
        TradedEvent tradedEvent = events.get(revertMatchState.getMatchId());
        if (tradedEvent == null) {
            warn("Match %s not loaded so cant revert state back to request %s", revertMatchState.getMatchId(),
                            revertMatchState.getRevertToRequestId());
            return;
        }
        Boolean revertedOk = algoManager.handleRevertToStatePreceedingRequestId(revertMatchState.getMatchId(),
                        revertMatchState.getRevertToRequestId());
        String text;
        String status;
        if (revertedOk == null) {
            text = "Revert Match requested" + revertMatchState.getMatchId() + " State Back to Incident Id "
                            + revertMatchState.getRevertToRequestId();
            status = "UNKNOWN"; // TODO Ivan - is this status setting allowed?
        } else if (revertedOk) {
            text = "Successfully Reverted Match " + revertMatchState.getMatchId() + " State Back to Incident Id "
                            + revertMatchState.getRevertToRequestId();
            status = "SUCCESS";
        } else {
            text = "Unable to Revert Match " + revertMatchState.getMatchId() + " State Back to Incident Id "
                            + revertMatchState.getRevertToRequestId();
            status = "FAILED";
        }

        NotificationMessage alertNotification = tradedEvent.createDisplayVariable("algoManagerTraderAlert", text);
        alertNotification.getAttributes().add(attrib("alertType", "revertMatchState"));
        alertNotification.getAttributes().add(attrib("revertToIncidentId", revertMatchState.getRevertToRequestId()));
        alertNotification.getAttributes().add(attrib("status", status));
        sendNotificationToBetsync(tradedEvent.getEventId(), alertNotification);
    }

    private void handleMatchResultsFromBetsync(List<MatchResultNotification> matchResults) {
        for (MatchResultNotification mrn : matchResults) {
            MatchResultMap matchResult = JsonUtil.unmarshalJson(mrn.getResultJson(), MatchResultMap.class);

            TradedEvent tradedEvent = events.get(mrn.getMatchId());
            if (tradedEvent == null) {
                tradedEvent = new TradedEvent(mrn.getMatchId(), mrn.getSportcode());
                tradedEvent.setForResultingOnly(true);
                events.put(mrn.getMatchId(), tradedEvent);
            }

            tradedEvent.setPricingMethod(null);

            EventDetails eventDetails = algoManager.getEventDetails(mrn.getMatchId());
            if (eventDetails == null) {
                try {
                    prepareNewEventForManualResulting(mrn);
                } catch (Exception ex) {
                    events.remove(mrn.getMatchId());
                    error("Problem generating match format for puposes of resulting an unloaded %s match %s", ex,
                                    mrn.getSportcode(), mrn.getMatchId());
                    return;
                }
            }
            algoManager.handleManualResultMarkets(matchResult);
        }
    }

    private void prepareNewEventForManualResulting(MatchResultNotification mrn)
                    throws InstantiationException, IllegalAccessException {
        info("%s match %s not loaded will prepare AlgoMgr for purposes of manual resulting", mrn.getSportcode(),
                        mrn.getMatchId());
        String matchFormatJson = mrn.getMatchFormatJson();
        SupportedSportType sst = SupportedSportType.valueOf(mrn.getSportcode());
        Class<? extends MatchFormat> matchFormatClass = SportMetaData.forSportType(sst).getMatchFormat();
        MatchFormat matchFormat;
        if (matchFormatJson != null) {
            matchFormat = JsonUtil.unmarshalJson(matchFormatJson, matchFormatClass);
        } else {
            matchFormat = matchFormatClass.newInstance();
        }
        algoManager.handleNewEventCreation(sst, mrn.getMatchId(), matchFormat);
    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {
        TradedEvent tradedEvent = events.get(eventId);
        EventTradingStateUpdate update = tradedEvent.createEventTradingStateUpdate();
        EventTradingState eventTradingState = update.getEventTradingState();
        List<Setting> settings = settingChangedHandler.createBetsyncSettings(eventId, properties);
        eventTradingState.setSettings(settings);
        for (Setting s : settings) {
            tradedEvent.settingPublished(s.getSettingName());
        }
        debug("Sending changed %s settings to betsync - %s", tradedEvent, JsonUtil.marshalJson(settings));
        sendToBetsync(update);
    }

    private void handleChangedSettings(List<Setting> settingsChanged) {
        Collection<TradedEvent> matchEvents = events.values();
        for (Setting setting : settingsChanged) {
            settingChangedHandler.handleChangedSetting(setting, matchEvents);
        }
    }

    @Override
    public void onEventTradingState(EventTradingState state) {
        super.onEventTradingState(state);
        if(state.isPurgeFromAlgo()) {
        	Long eventId = state.getId();
            TradedEvent tradedEvent = events.get(eventId);
            EventDetails eventDetails = algoManager.getEventDetails(eventId);
            
            debug("EventID: %s. Event has been purged from MS. Remove it from integration layer and algomanager.", eventId);
            
            tradedEvent.completed(true);
            
            events.remove(eventId);
            
            tradedEvent = null;
            
            if (eventDetails != null) {
                algoManager.handleRemoveEvent(eventId, true);
            }
            
        } else if (state.getState() == EventState.COMPLETED || state.getState() == EventState.VOID) {
            Long eventId = state.getId();
            TradedEvent tradedEvent = events.get(eventId);
            EventDetails eventDetails = algoManager.getEventDetails(eventId);
            boolean outOfSyncWithState = false;
            boolean outOfSyncWithEngine = false;
            boolean isEventStillOutOfSync = false;

            if (eventDetails != null) {
                outOfSyncWithState = eventDetails.getEventState().getMatchState().isDatafeedStateMismatch();
                outOfSyncWithEngine = eventDetails.getMatchEngine().getMatchState().isDatafeedStateMismatch();
                isEventStillOutOfSync = !eventDetails.getMatchEngine().getMatchState().isDataFeedStateMismatchCleared();
            }


            if (null == tradedEvent || outOfSyncWithState || outOfSyncWithEngine || isEventStillOutOfSync) {
                debug("Game out of sync or not a real event for Event " + eventId);
                return;
            }

            info("Event = " + eventId + ", tradedEvent.isCompleted() = " + tradedEvent.isCompleted());
            info("Event = " + eventId + ", tradedEvent.getMatchDetails().getState() = "
                            + tradedEvent.getMatchDetails().getState());
            info("Event = " + eventId + ", eventDetails.isCompleted() = " + eventDetails.isCompleted());

            if (!tradedEvent.isCompleted()) {

                debug("Event = " + eventId
                                + ", tradedEvent.isCompleted() currently false. Is this correct? What do the eventDetails say? Starting 3 min delay for completeEvent(tradedEvent)");

                executorService.schedule(() -> completeEvent(tradedEvent), 3, TimeUnit.MINUTES);

            } else if (tradedEvent.isPrematch()) {
                debug("Event = " + eventId
                                + ", tradedEvent.isPrematch() currently true. Is this correct? Starting 3 min delay for completeEvent(tradedEvent)");
                executorService.schedule(() -> completeEvent(tradedEvent), 3, TimeUnit.MINUTES);
                executorService.schedule(() -> algoManager.handleAbandonEvent(eventId, false), 3, TimeUnit.MINUTES);
                info("Event = " + eventId
                                + ", Marking prematch  %s as completed with 3 minute delay for setting tradedEvent.completed(true)",
                                tradedEvent);
                executorService.schedule(() -> tradedEvent.completed(true), 3, TimeUnit.MINUTES);
            } else {
                debug("Event = " + eventId + ", Start the 3 min delay before we complete event");
                executorService.schedule(() -> completeEvent(tradedEvent), 3, TimeUnit.MINUTES);
            }
        }
    }

    protected void completeEvent(TradedEvent tradedEvent) {
        EventDetails eventDetails = algoManager.getEventDetails(tradedEvent.getEventId());
        if (eventDetails != null && !eventDetails.isCompleted()) {
            algoManager.handleAbandonEvent(tradedEvent.getEventId(), true);
        }
        info("Marking as completed %s", tradedEvent);
        tradedEvent.completed(true);
    }

    @Override
    public void onMatchIncident(MatchIncident incident) {
        super.onMatchIncident(incident);
        TradedEvent tradedEvent = events.get(incident.getEventId());
        if (tradedEvent == null) {
            error("handling incident for unknown match %s ???", incident.getEventId());
            return;
        }

        if (incident.getIncidentSubType() == null && !incident.isUndo()) {
            warn("Dropping %s for %s- no incident type info", incident.getClass().getSimpleName(), tradedEvent);
            return;
        }

        if (incident instanceof DatafeedMatchIncident) {
            DatafeedMatchIncident dmi = (DatafeedMatchIncident) incident;
            DatafeedMatchIncidentType matchIncidentType = dmi.getIncidentSubType();
            if (dmi.getElapsedTimeSecs() == 0 && matchIncidentType == DatafeedMatchIncidentType.OK) {
                warn("Dropping %s DatafeedMatchIncident - 0 elapsed time for OK status", tradedEvent);
                return;
            }
        }
        if (incident.isUndo()) {
            algoManager.handleUndoMatchIncident(incident.getEventId(), incident);
        } else {
            if (tradedEvent.isNewIncident(incident)) {
                if (incident.getTimeStamp() <= 0) {
                    info("Missing timeStamp in match %s incident %s", tradedEvent, incident.getIncidentId());
                    incident.setTimeStamp(System.currentTimeMillis());
                }
                debug("Event for incident = " + incident.getEventId() + ". Incident = " + incident + ". Traded event = "
                                + tradedEvent);
                if (incident instanceof AbandonMatchIncident
                                || incident.getIncidentSubType() instanceof AbandonMatchIncidentType) {
                    algoManager.handleAbandonEvent(incident.getEventId(), false);
                } else {
                    algoManager.handleMatchIncident(incident, true);
                }
            } else {
                warn("Ignoring duplicate incident %s in match %s - was betsync restarted", incident.getIncidentId(),
                                incident.getEventId());
            }
        }
    }

    @Override
    public void onMatchParams(MatchParams params) {
        super.onMatchParams(params);
        TradedEvent tradedEvent = events.get(params.getEventId());
        if (tradedEvent == null) {
            error("handling MatchParams for unknown match %s ???", params.getEventId());
            return;
        }

        long blobRecoveredTime = tradedEvent.getBlobRecoveredTime();
        if (blobRecoveredTime > 0 && ((System.currentTimeMillis() - blobRecoveredTime) < 9000)) {
            warn("Blob recovered params : Ignoring pushed match params %s", params);
            return;
        }

        tradedEvent.setCurrentParams(params);
        info("Binding pushed match params %s", params);
        algoManager.handleSetMatchParams(params.generateGenericMatchParams());
    }

    @Override
    public void onBookmakersPrices(BookmakersPrices bookiePrices) {
        if (bookiePrices.getBookies().isEmpty()) {
            return;
        }
        super.onBookmakersPrices(bookiePrices);
        TradedEvent tradedEvent = events.get(bookiePrices.getEventId());
        if (tradedEvent == null) {
            error("handling bookiePrices for unknown match %s ???", bookiePrices.getEventId());
            return;
        }
        tradedEvent.setParamFindingSource(bookiePrices.getSource());
        MarketPricesList marketPrices = bookieParamfindHelper.generateMarketPricesList(bookiePrices);
        Map<String, MarketPrices> marketPricesList = marketPrices.getMarketPricesList();
        if (null == marketPricesList || marketPricesList.isEmpty()) {
            return;
        }
        algoManager.handleSupplyMarketPrices(bookiePrices.getEventId(), marketPrices);
    }

    private void sendNotificationToBetsync(long matchId, NotificationMessage notification) {
        if (notification != null) {
            FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
            feedBetsyncDto.setNotification(notification);
            betsyncService.asyncSend(feedBetsyncDto, matchId);
        }
    }

    protected void registerNewEventWithAlgoMgr(TradedEvent event, EventStateBlob eventState,
                    Map<String, String> eventProperties) {
        MatchDetails matchDetails = event.getMatchDetails();

        Long eventId = matchDetails.getMatchId();

        Stopwatch stopwatch = Stopwatch.createStarted();

        MatchFormat format = event.getMatchFormat();
        debug("use margin chart %b in new event %s (%s)", algoManager.isUseMarginChart(), matchDetails.getName(),
                        eventId);
        format.setUseMarginChart(algoManager.isUseMarginChart());
        if (null == eventState) {
            debug("creating new event %s (%s)", matchDetails.getName(), eventId);
            algoManager.handleNewEventCreation(event.getSport(), eventId, format, eventProperties);
        } else {
            debug("recreating event %s (%s) from state saved in prior request %s", matchDetails.getName(), eventId,
                            eventState.getIncidentId());
            algoManager.handleNewEventCreation(event.getSport(), eventId, format, eventState, eventProperties);
        }
        debug("created event %s (%s) in %s there are now %s events in this AlgoManager", matchDetails.getName(),
                        eventId, stopwatch, events.size());
    }

    private void processMatch(Match match) {
        MatchDetails matchDetails = match.getMatchDetails();
        if (events.containsKey(matchDetails.getMatchId())) {
            debug("already pricing event %s (%s)", matchDetails.getName(), matchDetails.getMatchId());
            return;
        }

        EventState state = matchDetails.getState();
        if (state == EventState.COMPLETED) {
            return;
        }

        if (state == EventState.VOID) {
            return;
        }

        if (state == EventState.CLOSED) {
            return;
        }

        TradedEvent event = new TradedEvent(matchDetails);
        events.put(event.getEventId(), event);

        recordedItemArchiveHandler.startArchive(event);

        Map<String, String> eventProperties =
                        settingChangedHandler.extractInitialEventCreationSettings(matchDetails.getSettings());

        String pricingMethod = eventProperties.remove(SettingChangedHandler.PRICING_METHOD_SETTING_NAME);
        if (pricingMethod != null) {
            event.setPricingMethod(pricingMethod);
        }

        Long tier = matchDetails.getTier();
        if (tier != null && tier > 0) {
            eventProperties.put("eventTier", Long.toString(tier));
        }

        try {
            EventStateBlob eventState = (null != algoManagerCacheService)
                            ? algoManagerCacheService.getEventState(event.getEventId())
                            : null;

            registerNewEventWithAlgoMgr(event, eventState, eventProperties);

            if (null != eventState) {
                event.blobRecovered();
            }
        } catch (Exception ex) {
            error("Problem handling %s subscription", ex, event);
        }
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        warn("Should notify trader of Fatal PF/PC error in event %s", eventId);
    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recordedItemArchiveHandler.handleArchive(eventId, recordedItem);
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent == null) {
            error("in notify for unkown : event %s, completed = %s", eventId, isCompleted);
            return;
        }
        debug("in notify : event %s, completed = %s. Delay by 3 minute to allow any final updates", eventId,
                        isCompleted);
        executorService.schedule(() -> tradedEvent.completed(isCompleted), 3, TimeUnit.MINUTES);
        if (isCompleted) {
            EventTradingStateUpdate update = tradedEvent.createEventTradingStateUpdate();
            EventTradingState eventTradingState = update.getEventTradingState();
            eventTradingState.setDisplayed(Boolean.FALSE);
            eventTradingState.setState(EventState.SUSPENDED);
            sendToBetsync(update);
        }
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {
        if (genericMatchParams != null) {
            publishMatchParams(eventId, genericMatchParams);
        }

        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent != null) {
            tradedEvent.setParamsOk(paramFindResults.isSuccess());
            tradedEvent.setLastParamFindResults(paramFindResults);
            tradedEvent.setUpdatedMatchParams(genericMatchParams);
            publishParamFindResults(tradedEvent);
            algoStatsPublisher.publishParamFindStatus(tradedEvent.getSport(), tradedEvent.getEventId(),
                            paramFindResults, elapsedTimeMs);
        } else {
            warn("ParamFindResult for unrecognised event %s", eventId);
        }
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent == null) {
            warn("TraderAlert for unrecognised event %s", eventId);
            return;
        }

        TraderAlertType traderAlertType = traderAlert.getTraderAlertType();
        if (traderAlertType == null) {
            return;
        }

        NotificationMessage alertNotification =
                        traderAlertBuilder.createTraderAlertNotification(tradedEvent, traderAlert);
        sendNotificationToBetsync(tradedEvent.getEventId(), alertNotification);
    }

    protected void publishParamFindResults(TradedEvent tradedEvent) {
        ParamFindResults paramFindResults = tradedEvent.getLastParamFindResults();
        if (paramFindResults != null) {
            GenericMatchParams foundParams = tradedEvent.getUpdatedMatchParams();
            ParamFindResultsDescription resultsDescription = paramFindResults.getParamFindResultsDescription();
            NotificationMessage notification = tradedEvent.createDisplayVariable("lastParamFindTime",
                            String.valueOf(System.currentTimeMillis()));
            List<Attribute> attributes = notification.getAttributes();
            String paramFindingSource = tradedEvent.getParamFindingSource();
            if (paramFindingSource == null) {
                paramFindingSource = "na";
            }
            attributes.add(attrib("feed", paramFindingSource));
            attributes.add(attrib("found", foundParams != null ? "true" : "false"));
            attributes.add(attrib("good", foundParams != null && paramFindResults.isSuccess() ? "true" : "false"));
            attributes.add(attrib("Alert Text", "lastParamFindTime"));
            attributes.add(attrib("Alert Colour", paramFindResults.getParamFindResultsStatus().name()));
            attributes.add(attrib("Feed Provider", paramFindingSource));

            String resultSummary = resultsDescription.getResultSummary();
            if (resultSummary == null || resultSummary.isEmpty()) {
                resultSummary = "N/A";
            }

            attributes.add(attrib("summary", resultSummary));
            ArrayList<String> resultDetail = resultsDescription.getResultDetail();
            if (resultDetail != null && !resultDetail.isEmpty()) {
                int detailLine = 1;
                for (String detail : resultDetail) {
                    attributes.add(attrib("detail_" + detailLine, detail));
                    detailLine++;
                }
            }
            sendNotificationToBetsync(tradedEvent.getEventId(), notification);
        }
    }

    private Attribute attrib(String name, String value) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setValue(value);
        return attribute;
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent == null) {
            warn("publishEventState for unrecognised event %s", eventId);
            return;
        }
        if (null == algoManagerCacheService) {
            warn("algoManagerCacheService not injected can checkpoint %s for match %s", eventStateBlob.getIncidentId(),
                            eventId);
            return;
        }

        algoManagerCacheService.putAsyncEventState(eventId, eventStateBlob,
                        (EventStateBlob b) -> sendNotificationToBetsync(eventId,
                                        tradedEvent.createEventBlobCheckpointNotification(b)));
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        TradedEvent tradedEvent = events.get(eventId);
        matchParams.setEventId(eventId);
        if (tradedEvent != null) {
            if (tradedEvent.isChangedParams(matchParams)) {
                tradedEvent.setCurrentParams(matchParams);

                if (isTrace()) {
                    trace("in publish changed %s params %s", tradedEvent, matchParams);
                } else {
                    info("in publish changed %s params", tradedEvent);
                }
                FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
                feedBetsyncDto.setMatchParams(matchParams);
                betsyncService.asyncSend(feedBetsyncDto, eventId);
            } else {
                info("Event %s params %s unchanged wont publish back to betsync", eventId, matchParams);
            }
        } else {
            debug("Nothing to publish in MatchParams : matchParams = %s", matchParams);
        }
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent == null) {
            warn("handling results for unknown match %s ??? (match may have been purged)", eventId);
            return;
        }
        if (resultedMarkets.size() == 0) {
            return;
        }

        if (tradedEvent.isMirroredPricingInAts()) {
            debug("Wont publish results as %s was created in Ats for mirrored pricing only", tradedEvent);
            return;
        }

        Set<String> keySet = resultedMarkets.getResultedMarkets().keySet();
        Thread currentThread = Thread.currentThread();
        String originalName = currentThread.getName();
        if (isTrace()) {
            trace("in publish %s ResultedMarkets %s", tradedEvent, resultedMarkets);
        } else {
            if (isDebug()) {
                debug("in publish %s ResultedMarkets %s", tradedEvent, keySet);
            } else {
                info("in publish %s ResultedMarkets %s of them", tradedEvent, keySet.size());
            }
        }

        try {
            currentThread.setName(originalName + "->publishResults(" + tradedEvent + ")");
            FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
            if (resultedMarketsHelper.addMarketResults(feedBetsyncDto, tradedEvent, resultedMarkets)) {
                betsyncService.asyncSend(feedBetsyncDto, eventId);
            }
        } finally {
            currentThread.setName(originalName);
        }
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String currentSequenceId) {
        algoStatsPublisher.pendPublishing(timeStamp);
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent == null) {
            error("handling update for unknown match %s ???", eventId);
            return;
        }

        if (tradedEvent.isForResultingOnly()) {
            debug("Wont publish markets as %s was created for resulting only", tradedEvent);
            return;
        }

        if (tradedEvent.isMirroredPricingInAts()) {
            debug("Wont publish markets as %s was created in Ats for mirrored pricing only", tradedEvent);
            return;
        }
        boolean resetEvent = false;
        for (Market market : markets) {
            if (market.getMarketStatus().getSuspensionStatusReason().contains("Event has been reset")) {
                resetEvent = true;
            }
        }

        String tradedEventName = tradedEvent.toString();
        Thread currentThread = Thread.currentThread();
        String originalName = currentThread.getName();

        try {
            currentThread.setName(originalName + "->publishMarkets(" + tradedEventName + ")");
            if (isTrace()) {
                trace("in publish %s Markets %s", tradedEvent, markets);
            } else if (isDebug()) {
                debug("in publish %s Markets %s", tradedEvent, markets.getMarketKeys());
            } else {
                info("in publish %s Markets for %s", markets.getMarkets().size(), tradedEvent);
            }

            debug("Starting the whole mapping process");

            marketCreationHelper.generateNewBetsyncMarkets(tradedEvent, markets, timeStamp,
                            newMarketsToBetsyncBatchSize, this::sendNewMarketsToBetsync, resetEvent);

            FeedBetsyncDto feedBetsyncDto = marketPricesHelper.addPrices(new FeedBetsyncDto(), tradedEvent, markets,
                            keysForDiscontinuedMarkets, timeStamp, resetEvent);
            feedBetsyncDto.getEventTradingStateUpdate().getEventTradingState().setPeriodSequence(currentSequenceId);
            debug("Sending prices to betsync: " + JsonUtil.marshalJson(feedBetsyncDto));
            betsyncService.asyncSend(feedBetsyncDto, tradedEvent.getEventId());
        } finally {
            currentThread.setName(originalName);
        }
    }

    protected void sendNewMarketsToBetsync(FeedBetsyncDto eventCreationWrapper) {
        Event event = eventCreationWrapper.getEventTreeCreation().getEvent().get(0);
        debug("Sending new markets to betsync: %s", JsonUtil.marshalJson(eventCreationWrapper));
        betsyncService.asyncSend(eventCreationWrapper, event.getId());
    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {
        TradedEvent tradedEvent = events.get(eventId);
        if (tradedEvent.isMirroredPricingInAts()) {
            debug("Wont publish event suspension as %s was created in Ats for mirrored pricing only", tradedEvent);
            return;
        }
        EventTradingStateUpdate eventTradingStateUpdate = tradedEvent.createEventTradingStateUpdate();
        EventTradingState eventTradingState = eventTradingStateUpdate.getEventTradingState();
        if (marketGroups == null || marketGroups.isEmpty()) {
            debug("in publishEventSuspensionStatus(%s, %s)", eventId, suspend);
            eventTradingState.setState(suspend ? EventState.SUSPENDED : EventState.ACTIVE);
        } else {
            debug("in publishEventMarketGroupsSuspensionStatus(%s, %s, %s)", eventId, suspend, marketGroups);
            MarketGroupsStates marketGroupsStates = new MarketGroupsStates();
            MarketState marketState = suspend ? MarketState.SUSPENDED : MarketState.OPEN;
            marketGroups.forEach(marketGroup -> marketGroupsStates.getStates()
                            .add(createMarketGroupState(marketState, marketGroup)));
            eventTradingState.setMarketGroupsStates(marketGroupsStates);
        }
        sendToBetsync(eventTradingStateUpdate);
    }

    private MarketGroupState createMarketGroupState(MarketState marketState, MarketGroup marketGroup) {
        MarketGroupState marketGroupState = new MarketGroupState();
        marketGroupState.setGroup(marketGroup.name());
        marketGroupState.setState(marketState);
        return marketGroupState;
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState sms) {
        sms.setSource(Source.ALGO_MANAGER);
        FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
        feedBetsyncDto.setSimpleMatchState(sms);
        debug("in publish (simple) MatchState %s", sms);
        betsyncService.asyncSend(feedBetsyncDto, eventId);
    }

    public void setBetsyncService(BetsyncService betsyncService) {
        this.betsyncService = betsyncService;
    }

    public void setWebsocketClient(BetsyncOutWebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
    }

    private void monitorAlgoQueue() {
        try {
            int algoMgrQueueSize = algoManager.getAlgoMgrQueueSize();
            if (algoMgrQueueSize >= algoWorkQueueDumpSize) {
                if (!loggedBreach) {
                    loggedBreach = true;
                    warn("Hightide %s algo mgr work queue tasks...", algoMgrQueueSize);
                    algoManager.logCurrentQueueTasks();
                }
            } else if (loggedBreach) {
                loggedBreach = false;
                info("Lowtide %s algo mgr work queue tasks...", algoMgrQueueSize);
            }
        } catch (Exception ex) {
            error("Problem monitroing algo work queue", ex);
        }
    }

    private void publishAlgoStats() {
        try {
            if (events.isEmpty()) {
                return;
            }
            AlgoManagerStatistics statistics = algoManager.getStatistics();
            algoStatsPublisher.publishStats(algoManager, statistics);
        } catch (Exception ex) {
            error("Problem logging algo stats", ex);
        }
    }

    private void publishAlgoTimings() {
        try {
            if (events.isEmpty()) {
                return;
            }
            algoStatsPublisher.publishTimings();
        } catch (Exception ex) {
            error("Problem publishing algo timings", ex);
        }
    }

    private void cleanUpCompletedEvents() {
        try {
            if (events.isEmpty()) {
                return;
            }
            Instant now = Instant.now();
            for (TradedEvent tradedEvent : new ArrayList<>(events.values())) {
                if (tradedEvent.isCompleted()) {
                    checkRemoveEvent(now, tradedEvent);
                }
            }
            info("%s events remain after cleanup checks", events.size());
        } catch (Exception ex) {
            error("Problem cleaning up completed events", ex);
        }
    }

    private void checkRemoveEvent(Instant now, TradedEvent tradedEvent) {
        long minutesSinceCompleted = ChronoUnit.MINUTES.between(tradedEvent.getCompletedAt(), now);
        if (minutesSinceCompleted >= 10) {
            Long eventId = tradedEvent.getEventId();
            debug("%s completed %s minutes ago at %s, will remove it", tradedEvent, minutesSinceCompleted,
                            tradedEvent.getCompletedAt());
            events.remove(eventId);
            algoManager.handleRemoveEvent(eventId);
            recordedItemArchiveHandler.finishArchive(eventId);
            if (null != algoManagerCacheService) {
                algoManagerCacheService.removeEventState(eventId);
            }
        }
    }

    private void sendToBetsync(EventTradingStateUpdate update) {
        FeedBetsyncDto feedBetsyncDto = new FeedBetsyncDto();
        feedBetsyncDto.setEventTradingStateUpdate(update);
        betsyncService.asyncSend(feedBetsyncDto, update.getEventTradingState().getId());
    }

    public static void main(String[] args) {
        try {
            LogUtil.initConsoleLogging(Level.TRACE);

            BetsyncOutWebsocketClient client = new BetsyncOutWebsocketClient();
            client.setBetsyncUrl("ws://iomsampss01:9998/websocket");
            String user = "algomgr";
            String password = "test1";
            client.setUsername(user);
            client.setPassword("test1");
            client.setApplicationName("algoMgr");

            BetsyncInRestApiClient restApiClient = new BetsyncInRestApiClient();
            restApiClient.setBetsyncInUrlPrefix("http://iomsampss01:8083/betsync/rest/");
            restApiClient.setBetsyncInUser(user);
            restApiClient.setBetsyncInPass(password);
            restApiClient.setGenerateApiExceptions(true);

            BetsyncService betsyncService = new BetsyncService();
            betsyncService.setBetsyncInProxy(restApiClient);

            AlgoMgrBetsyncCoordinator coordinator = new AlgoMgrBetsyncCoordinator();
            coordinator.setAlgoManagerConfiguration(new SimpleAlgoManagerConfiguration());
            coordinator.setWebsocketClient(client);
            coordinator.setBetsyncService(betsyncService);

            // coordinator.setAlgoSports("ICE_HOCKEY");
            // client.setAlgoSports("SOCCER");
            coordinator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
