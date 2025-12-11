package ats.algo.matchrunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.sport.buildabet.BuildABet;

import java.util.Set;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.algomanager.SupportedSports;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiIncidentResponse;
import ats.algo.core.incidentgenerator.GuiTimerResponse;
import ats.algo.core.incidentgenerator.MatchIncidentGenerator;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.RecordedItem.RecordedItemType;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.recordplayback.RecordingHeader;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.JsonFormatter;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.marketscollector.MarketsCollector;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMarkets;
import ats.algo.matchrunner.model.ObservableMarketsAwaitingResult;
import ats.algo.matchrunner.model.ObservableMatchParams;
import ats.algo.matchrunner.model.ObservablePrices;
import ats.algo.matchrunner.model.ObservableResultedMarkets;
import ats.algo.matchrunner.view.AlertBox;
import ats.algo.matchrunner.view.ControlButtonsPaneController;
import ats.algo.matchrunner.view.IncidentsPaneOldController;
import ats.algo.matchrunner.view.IncidentsPaneV2Controller;
import ats.algo.matchrunner.view.MarketDetailDialogController;
import ats.algo.matchrunner.view.MatchStateDialogController;
import ats.algo.matchrunner.view.MatchFormatDialogController;
import ats.algo.matchrunner.view.MatchOverviewController;
import ats.algo.matchrunner.view.ParamFindDialogController;
import ats.algo.matchrunner.view.PrematchResultDialogController;
import ats.algo.matchrunner.view.ReplayMatchDisplayController;
import ats.algo.matchrunner.view.ResultedMarketsDisplayController;
import ats.algo.matchrunner.view.TimerControlsPaneController;
import ats.algo.matchrunner.view.TradingRulesDisplayController;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import ats.org.json.JSONException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MatchRunner extends AtsBean implements MatchRunnable, AlgoManagerPublishable {

    private static final String ACTIVE_MQ_BROKER_URL = "tcp://localhost:61616";

    @FunctionalInterface
    public interface GetMatchEngineFunction {
        public MatchEngine get();
    }

    private Recording publishedRecording;

    private Stage primaryStage;
    private BorderPane rootLayout;
    private BorderPane controlsLayout;
    private MatchOverviewController matchOverviewController;
    private ParamFindDialogController paramFindDialogController;
    private TimerControlsPaneController timerControlsPaneController;
    private IncidentsPaneOldController incidentsPaneOldController;
    private IncidentsPaneV2Controller incidentsPaneV2Controller;
    private ControlButtonsPaneController controlButtonsPaneController;
    private PrematchResultDialogController prematchResultController;

    private MatchStateDialogController matchStateDialogController;

    private MatchFormatDialogController matchFormatDialogController;
    private ResultedMarketsDisplayController resultedMarketsDisplayController;
    private TradingRulesDisplayController tradingRulesDisplayController;
    private ReplayMatchDisplayController replayMatchDisplayController;
    private AlgoManager algoManager;
    private MatchFormat matchFormat;
    private MatchState matchState;
    private GenericMatchParams matchParams;
    private Markets markets;
    private String matchTitle;
    private boolean logLevelIsTrace;
    private ParamFindResults paramFindResults;
    private MatchIncidentGenerator matchIncidentGenerator;

    private ObservableMap observableMatchFormat;
    private ObservableMap observablePrematchResult;
    private ObservableMatchParams observableMatchParams;
    private ObservableMarkets observableMarkets;
    private ObservableResultedMarkets observableResultedMarkets;
    private ObservableMarketsAwaitingResult observableMarketsAwaitingResult;
    private ObservableMap observableSources;
    private ObservablePrices[] observablePricesArray;

    private Map<String, String> sourcesMap;
    private Markets marketsAwaitingResult;
    private MatchIncidentPrompt prompt;
    private int eventTier;
    private long eventId = 149L;
    private Recording savedRecording;
    private boolean replayMatchFromFile;
    private String replayFileName;
    private boolean useExternalModel;
    private String defaultFeedProvider;
    private MarketsCollector marketsCollector;
    private boolean isUndoIncident;
    SupportedSportType supportedSportType;
    /**
     * executed following the confirmation of the match format the returned map must contain three objects with keys
     * "MatchEngine", "MatchState", "MatchParams". Object in each case must be of the correct class
     *
     * @author Geoff
     *
     */

    private GetMatchEngineFunction getMatchEngineFn;
    // private MatchIncident matchIncident;
    // private MatchIncidentResult matchIncidentResult;

    /**
     *
     * @param matchTitle
     * @param matchFormat
     * @param getMatchEngineFunction
     */
    public MatchRunner(String matchTitle, MatchFormat matchFormat, GetMatchEngineFunction getMatchEngineFunction,
                    MatchIncidentGenerator matchIncidentGenerator) {
        setLogLevel(Level.DEBUG);
        logLevelIsTrace = true;
        info("Changing log level to 'trace'");
        this.matchTitle = matchTitle;
        this.matchFormat = matchFormat;
        this.getMatchEngineFn = getMatchEngineFunction;
        this.matchIncidentGenerator = matchIncidentGenerator;
        observableMatchFormat = new ObservableMap((Map<String, String> map) -> matchFormat.setFromMap(map));
        observableMatchFormat.updateDisplayedData(matchFormat.getAsMap());
    }

    /**
     *
     * @param matchTitle
     * @param matchFormat
     * @param getMatchEngineFunction
     */
    public MatchRunner(String matchTitle, MatchFormat matchFormat, GetMatchEngineFunction getMatchEngineFunction) {
        this(matchTitle, matchFormat, getMatchEngineFunction, null);
    }

    private void setLogLevel(Level level) {
        LogUtil.initConsoleLogging(level);
    }

    private void switchLogLevel() {
        if (logLevelIsTrace) {
            info("Switching log level to WARN");
            setLogLevel(Level.WARN);
            logLevelIsTrace = false;
        } else {
            setLogLevel(Level.TRACE);
            logLevelIsTrace = true;
            info("Switching log level to TRACE");

        }
    }

    private static final int nPriceSources = 5;

    public void initialiseMatchObjects() {
        /*
         * call back to the top level class to get the matchEngine following confirmation of match format by the user
         */
        if (replayMatchFromFile)
            if (getMatchRecordingOk()) {
                observableMatchFormat.updateDisplayedData(matchFormat.getAsMap());
                this.showReplayDialog();
            } else
                replayMatchFromFile = false;
        MatchEngine matchEngine = getMatchEngineFn.get();
        /*
         * add this sport to SupportedSports for later use by AlgoManager
         */
        TradingRules tradingRules = matchEngine.getTradingRuleSet();
        Class<? extends TradingRules> tradingRulesClass = null;
        if (tradingRules != null)
            tradingRulesClass = tradingRules.getClass();
        supportedSportType = matchEngine.getSupportedSportType();
        SupportedSports.addSport(supportedSportType, supportedSportType.toString(), matchEngine.getClass(),
                        tradingRulesClass);
        publishedRecording = new Recording();
        RecordingHeader hdr = publishedRecording.getRecordingHeader();
        hdr.setEventId(eventId);
        hdr.setEventTier(eventTier);
        hdr.setMatchFormat(matchFormat);
        hdr.setCompetitionName("Recorded in MatchRunner");
        hdr.setTeamAName("teamA");
        hdr.setTeamBName("teamB");
        hdr.setSportType(supportedSportType);
        hdr.setRecordingStartTimeMillis(System.currentTimeMillis());
        marketsAwaitingResult = new Markets();
        /*
         * create the observable objects
         */
        observableMatchParams = new ObservableMatchParams();

        observableMarkets = new ObservableMarkets();
        observableMarkets.update(marketsNotYetGeneratedMessage());
        observableResultedMarkets = new ObservableResultedMarkets();
        observableMarketsAwaitingResult = new ObservableMarketsAwaitingResult();
        sourcesMap = new LinkedHashMap<String, String>();
        sourcesMap.put("Source1", "1.0");
        sourcesMap.put("Source2", "1.0");
        sourcesMap.put("Source3", "1.0");
        sourcesMap.put("Source4", "1.0");
        sourcesMap.put("Source5", "1.0");
        observableSources = new ObservableMap((Map<String, String> map) -> handleSourcesMapUpdatedByUser(map));
        observableSources.updateDisplayedData(sourcesMap);
        observablePricesArray = new ObservablePrices[nPriceSources];
        for (int i = 0; i < nPriceSources; i++)
            observablePricesArray[i] = new ObservablePrices();
        if (this.replayMatchFromFile)
            replayMatchDisplayController.setRecordingNow(publishedRecording);
        /*
         * instantiate AlgoManager and create the new event
         */
        eventTier = 3;
        defaultFeedProvider = "TRADER";
        AlgoManagerConfiguration algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        algoManager = new AlgoManager(algoManagerConfiguration, this);

        algoManager.killTimer();
        algoManager.publishResultedMarketsImmediately(true);
        if (useExternalModel) {
            String url = System.getProperty("urlForExternalModelsMqBroker");
            if (url == null)
                url = ACTIVE_MQ_BROKER_URL;;
            if (!algoManager.setActiveMqBrokerUrl(url)) {
                AlertBox.displayError(String.format("Can't connect to MQ broker with url: %s", url));
                System.exit(0);
            }
            AlgoManager.setSystemProperty(supportedSportType, AlgoManager.USE_EXTERNAL_MODEL, "true");
        }
        String eventIdStr = System.getProperty("eventID");
        if (eventIdStr != null) {
            try {
                this.eventId = Long.parseLong(eventIdStr);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        marketsCollector = new MarketsCollector();
        algoManager.handleNewEventCreation(supportedSportType, eventId, matchFormat);
    }

    private Markets marketsNotYetGeneratedMessage() {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "-", "-",
                        "No markets yet published.  Maybe awaiting manual param change or param find");
        markets.addMarketWithShortKey(market);
        return markets;
    }

    private String handleSourcesMapUpdatedByUser(Map<String, String> map) {
        sourcesMap = map;
        return null;
    }

    /**
     * called following instantiation of the class
     * 
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Amelco Labs Match Runner v2.0: " + matchTitle);
        initRootLayout();
        showMatchFormatDialog();
        showControlsSidePane();
        initialiseMatchObjects();
        showMatchOverview();

        if (this.replayMatchFromFile)
            this.replayMatchDisplayController.displayInitialData();
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.isControlDown()) {
                if (key.getCode() == KeyCode.G)
                    incidentsPaneV2Controller.onSendIncidentButtonPressed();
                if (key.getCode() == KeyCode.U)
                    incidentsPaneV2Controller.onUndoLastButtonPressed();
                if (key.getCode() == KeyCode.S)
                    controlButtonsPaneController.onSaveMatchButtonPressed();
                if (key.getCode() == KeyCode.T)
                    controlButtonsPaneController.onTradingRulesButtonPressed();
                if (key.getCode() == KeyCode.R)
                    controlButtonsPaneController.onShowResultedMarketsButtonPressed();
                if (key.getCode() == KeyCode.P)
                    controlButtonsPaneController.onParamFindButtonPressed();
                if (key.getCode() == KeyCode.M)
                    controlButtonsPaneController.onManualResultButtonPressed();
            }
        });
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MatchRunner.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage showMatchFormatDialog() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/MatchFormatDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Match Format");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            MatchFormatDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            matchFormatDialogController = controller;
            matchFormatDialogController.initialise(this, observableMatchFormat);
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    private void showMatchOverview() {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MatchRunner.class.getResource("view/MatchOverview.fxml"));
            AnchorPane matchOverview = (AnchorPane) loader.load();

            // Set overview into the centre of root layout.
            rootLayout.setCenter(matchOverview);
            matchOverviewController = loader.getController();
            eventTier = 3; // set default value - will get updated when
                           // matchParams get published
            matchOverviewController.setParent(this);
            matchOverviewController.initialiseMatchOverviewController(eventTier, this, observableMatchParams,
                            observableMarkets, observableSources, observablePricesArray, observableMatchFormat,
                            (SimpleMatchState) matchState.generateSimpleMatchState(eventTier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showControlsSidePane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MatchRunner.class.getResource("view/ControlsLayout.fxml"));
            controlsLayout = (BorderPane) loader.load();
            if (matchIncidentGenerator == null) {
                /*
                 * Not yet available for this sport so using the old form of gui - text box entry
                 */
                FXMLLoader loader1 = new FXMLLoader(MatchRunner.class.getResource("view/EmptyControlsPane.fxml"));
                AnchorPane emptyControlsPane = (AnchorPane) loader1.load();
                controlsLayout.setTop(emptyControlsPane);
                FXMLLoader loader2 = new FXMLLoader(MatchRunner.class.getResource("view/IncidentsPaneOld.fxml"));
                AnchorPane incidentsPane = (AnchorPane) loader2.load();
                controlsLayout.setCenter(incidentsPane);
                incidentsPaneOldController = loader2.getController();
                incidentsPaneOldController.setParent(this);
                controlsLayout.setCenter(incidentsPane);

            } else {
                /*
                 * using the new form of gui
                 */
                if (matchIncidentGenerator.usesTimer()) {
                    FXMLLoader loader1 = new FXMLLoader(MatchRunner.class.getResource("view/TimerControlsPane.fxml"));
                    AnchorPane timerControlsPane = (AnchorPane) loader1.load();
                    controlsLayout.setTop(timerControlsPane);
                    timerControlsPaneController = loader1.getController();
                    timerControlsPaneController.setParent(this);
                } else {
                    FXMLLoader loader1 = new FXMLLoader(MatchRunner.class.getResource("view/EmptyControlsPane.fxml"));
                    AnchorPane emptyControlsPane = (AnchorPane) loader1.load();
                    controlsLayout.setTop(emptyControlsPane);
                }
                FXMLLoader loader2 = new FXMLLoader(MatchRunner.class.getResource("view/IncidentsPaneV2.fxml"));
                AnchorPane incidentsPane = (AnchorPane) loader2.load();
                controlsLayout.setCenter(incidentsPane);
                incidentsPaneV2Controller = loader2.getController();
                incidentsPaneV2Controller.setParent(this);
                controlsLayout.setCenter(incidentsPane);

            }
            FXMLLoader loader3 = new FXMLLoader(MatchRunner.class.getResource("view/ControlButtonsPane.fxml"));
            AnchorPane controlButtonsPane = (AnchorPane) loader3.load();
            controlsLayout.setBottom(controlButtonsPane);
            controlButtonsPaneController = loader3.getController();
            controlButtonsPaneController.setParent(this);
            rootLayout.setLeft(controlsLayout);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * called when user enters some text into the event box via IncidentsPaneOld
     *
     * @param string
     */

    public void processOldIncidentEntry(String string) {
        MatchIncident matchIncident = null;

        if (string.toUpperCase().equals("X")) {
            /*
             * toggle between feed ok and not ok
             */
            matchIncident = new DatafeedMatchIncident();
            DatafeedMatchIncidentType status = matchState.getDataFeedStatus();
            if (status == DatafeedMatchIncidentType.OK)
                ((DatafeedMatchIncident) matchIncident).setIncidentSubType(DatafeedMatchIncidentType.BET_STOP);
            else
                ((DatafeedMatchIncident) matchIncident).setIncidentSubType(DatafeedMatchIncidentType.OK);
        } else if (string.toUpperCase().equals("DEBUG")) {
            switchLogLevel();
            incidentsPaneOldController.setNextPrompt(prompt);
            return;

        } else if (string.toUpperCase().equals("IMG")) {
            defaultFeedProvider = "IMG";
            matchIncident = new DatafeedMatchIncident();
            ((DatafeedMatchIncident) matchIncident).setIncidentSubType(DatafeedMatchIncidentType.OK);
        } else if (string.toUpperCase().equals("BET")) {
            defaultFeedProvider = "BETRADAR";
            matchIncident = new DatafeedMatchIncident();
            ((DatafeedMatchIncident) matchIncident).setIncidentSubType(DatafeedMatchIncidentType.OK);
        } else if (string.toUpperCase().equals("LSP")) {
            defaultFeedProvider = "LSP";
            matchIncident = new DatafeedMatchIncident();
            ((DatafeedMatchIncident) matchIncident).setIncidentSubType(DatafeedMatchIncidentType.OK);
        } else {
            matchIncident = matchState.getMatchIncident(string);
        }

        if (matchIncident == null) {
            incidentsPaneOldController.notifyInputError();
        } else {
            matchIncident.setSourceSystem(defaultFeedProvider);
            matchIncident.setIncidentId("R" + System.currentTimeMillis());
            handleMatchIncident(matchIncident);
            prompt = matchState.getNextPrompt();
            if (!defaultFeedProvider.equals(""))
                prompt.setPromt("<" + defaultFeedProvider + ">: " + prompt.getPrompt());
            incidentsPaneOldController.setNextPrompt(prompt);
        }
        matchOverviewController.onEventEntry();
    }

    public void processV2MatchIncidentEntry(GuiIncidentResponse guiResponse) {
        MatchIncident matchIncident = matchIncidentGenerator.generateMatchIncident(guiResponse);
        matchIncident.setEventId(eventId);
        handleMatchIncident(matchIncident);
    }

    public void processV2TimerIncidentEntry(GuiTimerResponse guiResponse) {
        MatchIncident matchIncident = matchIncidentGenerator.generateElapsedTimeIncident(guiResponse);
        matchIncident.setEventId(eventId);
        handleMatchIncident(matchIncident);
    }

    @Override
    public void handleMatchIncident(MatchIncident matchIncident) {
        BetsyncProxy.checkSerializationOk(matchIncident);
        matchIncident.setEventId(eventId);
        /**
         * Jin added from if(matchIncident instanceof AbandonMatchIncident){... }, until else To make match runner
         * handles abandon match use the same logic as ats
         **/
        // if(matchIncident instanceof AbandonMatchIncident){
        // algoManager.handleMatchIncident(matchIncident, true); // waste of
        // calculation, but allows matchstate to be
        // updated
        // algoManager.handleAbandonEvent(eventId,
        // matchIncident.getRequestId());
        // }
        // else
        // if (!(matchIncident instanceof DatafeedMatchIncident))
        algoManager.handleMatchIncident(matchIncident, true);
        matchOverviewController.onEventEntry();
    }

    public void handleResetMatchFormat(MatchFormat matchFormat) {
        info(matchFormat);
        this.matchFormat = (MatchFormat) matchFormat;
        observableMatchFormat.updateDisplayedData(matchFormat.getAsMap());
        algoManager.handleResetEvent(eventId, matchFormat, this.getEventTier());
    }

    /**
     * undoes the most recent action - can only undo one level
     */
    public boolean undoLastEvent() {
        Boolean undo = algoManager.handleUndoLastMatchIncident(eventId);
        matchOverviewController.onEventEntry();
        isUndoIncident = undo;
        return undo;
    }

    @Override
    public void handleMatchParamsChanged(MatchParams matchParams) {
        GenericMatchParams genericMatchParams = (GenericMatchParams) matchParams;
        genericMatchParams.setEventId(eventId);
        setEventTierFromMatchParams(genericMatchParams);
        algoManager.handleClearParamFindData(eventId);
        algoManager.handleSetMatchParams(genericMatchParams);
        matchOverviewController.onEventEntry();
    }

    /**
     * emulate what happens in ATS to allow any json related bugs to be detected. Converts to json then back again
     * 
     * @param matchState
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private MatchState getMatchStateViaJson(MatchState matchState) {
        String json = JsonUtil.marshalJson(matchState, true);
        MatchState matchState2 = JsonUtil.unmarshalJson(json, matchState.getClass());
        boolean jsonOk = matchState.equals(matchState2);
        if (!jsonOk) {
            error("MatchState object not serializing correctly to/from json:");
            error("MatchState object pre serialize/deserialize:\n" + matchState);
            error("MatchState object post serialize/deserialize:\n" + matchState2);
            AlertBox.displayMsg(String.format(
                            "%s does not serialize correctly to/from json - c.f. console for details.  Can't use in MatchRunner until corrected",
                            matchState.getClass()));
            throw new IllegalArgumentException();
        }
        return matchState2;
    }

    /**
     * gets the eventTier from the matchParam
     * 
     * @param matchParams
     */
    private void setEventTierFromMatchParams(GenericMatchParams matchParams) {
        MatchParam matchParam = matchParams.getParamMap().get("eventTier");
        if (matchParam != null) {
            this.eventTier = (int) matchParam.getGaussian().getMean();
            this.matchOverviewController.updateEventTierLabel(this.eventTier);
        }
    }

    /**
     * called when user right clicks on a
     *
     * @param key
     * @param sequenceId
     */
    @Override
    public void handleNewPricesRowRequest(String fullKey) {
        int index = matchOverviewController.getObservablePricesIndex();
        observablePricesArray[index].addPriceRows(fullKey);
        matchOverviewController.refreshObservablePrices();
    }

    /**
     * called when user right clicks on a
     *
     * @param key
     * @param sequenceId
     */
    @Override
    public void handleSelectionRowSelected(String fullKey, String sequenceId, String selKey) {
        int index = matchOverviewController.getObservablePricesIndex();
        if (sequenceId == null)
            observablePricesArray[index].addPriceRows(fullKey, selKey);
        else
            observablePricesArray[index].addPriceRows(fullKey, sequenceId, selKey);

        matchOverviewController.refreshObservablePrices();
    }

    /**
     * called when user requests removal of a price
     *
     * @param key
     * @param sequenceId
     */
    @Override
    public void handleRemovePricesRowRequest(String fullKey) {
        /*
         * get the index for the source for which prices are currently being displayed
         */
        int index = matchOverviewController.getObservablePricesIndex();
        observablePricesArray[index].removePricesForKey(fullKey);
        matchOverviewController.refreshObservablePrices();

    }

    /*
     * data shared between the start and execute paramFind methods
     */
    private MarketPricesList marketPricesList;

    /**
     * starts the param find process and displays the dialog box
     */
    public void handleFindParamsButtonPressed() {
        marketPricesList = new MarketPricesList();
        /*
         * this code relies on sourcesMap being a LinkedHashMap where the order in which elements are iterated is
         * guaranteed
         */
        int i = 0;
        StringBuilder b = new StringBuilder();
        b.append("Market prices to add to marketPricesCache: \n");
        Map<String, String> sourceWeights = new HashMap<String, String>();
        for (Entry<String, String> entry : sourcesMap.entrySet()) {
            String sourceName = entry.getKey();
            String sourceWeight = entry.getValue();
            sourceWeights.put("SOURCEWEIGHT_" + sourceName, sourceWeight);
            MarketPrices marketPrices = observablePricesArray[i].getMarketPrices();
            Set<String> keys = new HashSet<String>();
            for (Entry<String, MarketPrice> e : marketPrices.getMarketPrices().entrySet()) {
                if (!e.getValue().sanityCheckPrice()) {
                    b.append("Market price: ").append(e.getKey()).append(" from source: ").append(sourceName)
                                    .append(" fails priceSanityCheck\n");
                    b.append("Failure reason: ").append(e.getValue().getSanityCheckFailureReason()).append("\n");
                    keys.add(e.getKey());
                }
            }
            /*
             * remove any prices that fail the sanity check
             */
            for (String key : keys)
                marketPrices.getMarketPrices().remove(key);

            if (marketPrices.size() > 0) {
                marketPricesList.put(sourceName, marketPrices);
                b.append(sourceName).append(" (sourceWeight: ").append(sourceWeight).append("):\n");
                for (MarketPrice marketPrice : marketPrices)
                    b.append("  " + marketPrice.toString() + "\n");
            }
            i++;
        }
        if (marketPricesList.getMarketPricesList().size() == 0)
            b.append("No valid prices for any source\n");
        algoManager.handleSetEventProperties(eventId, sourceWeights);
        b.append("\nClick 'Start' to initiate param finding trading rules.  If param find triggered this may take some time...\n");
        showParamFindDialog(b.toString());
    }

    public void handlePrematchResultButtonPressed() {
        showPrematchResultDialog("");
    }

    public void handleBuildABetButtonPressed() {

        BuildABet buildABet = new BuildABet();
        List<Market> buildMarkets = new ArrayList<Market>();
        List<String> buildSelections = new ArrayList<String>();
        MarketPrices marketPrices = observablePricesArray[0].getMarketPrices();
        double[] prob = new double[2];
        int i = 0;
        for (Entry<String, MarketPrice> e2 : marketPrices.getMarketPrices().entrySet()) {
            MarketPrice marketPrice = e2.getValue();
            String marketType = marketPrice.getType();
            String sequenceId = marketPrice.getSequenceId();
            String lineId = marketPrice.getLineId();
            if (!"".equals(lineId))
                buildSelections.add(
                                marketType + "-" + marketPrice.getSelections().keySet().toArray()[0] + "-" + lineId);
            else
                buildSelections.add(marketType + "-" + marketPrice.getSelections().keySet().toArray()[0]);
            prob[i] = observablePricesArray[0].getData().get(i).getProbValue();
            i++;
            if (lineId != null && !lineId.equals("")) {
                Market marketToAdd = markets.get(marketType, lineId, sequenceId);
                if (marketToAdd == null)
                    marketToAdd = markets.get(marketType, sequenceId);
                buildMarkets.add(marketToAdd);
            }

            else
                buildMarkets.add(markets.get(marketType, sequenceId));
        }


        if (buildMarkets.size() == 2) {
            info("Two selection in Markets: " + buildSelections);
        } else {
            AlertBox.displayMsg("Only Support Two Markets");
            return;
        }
        if (!buildMarkets.get(0).getMarketGroup().equals(buildMarkets.get(1).getMarketGroup())) {
            Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BAB", "M", "Build A Bet");
            market.setIsValid(true);
            double probNew = prob[0] * prob[1];
            market.put(buildSelections.get(0) + " && " + buildSelections.get(1), probNew);
            market.put("Build a bet B", 1 - probNew);
            markets.addMarketWithShortKey(market);
        } else {
            Market csSourceMarket = markets.get("FT:CS");
            if (csSourceMarket != null) {
                Map<String, Double> csGrid = csSourceMarket.getSelectionsProbs();

                buildABet.setScoreGridOne(csGrid);
                Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:BAB", "M", "Build A Bet");
                market.setIsValid(true);
                String[] requirements = buildSelections.get(0).split("-");
                String key = requirements[0] + "-" + requirements[1];
                if (!buildABet.isMarketValidForBuildABet(key)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Only Support the Markets");
                    info(buildABet.getBuildBetMap().keySet().toString());
                    alert.setContentText(buildABet.getBuildBetMap().keySet().toString());
                    alert.show();
                    return;
                }
                requirements = buildSelections.get(1).split("-");
                key = requirements[0] + "-" + requirements[1];
                if (!buildABet.isMarketValidForBuildABet(key)) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Only Support the Markets");
                    alert.setContentText(buildABet.getBuildBetMap().keySet().toString());
                    alert.show();
                    return;
                }
                double probBuildABet = buildABet.generateBuildABetProbs(buildSelections);
                market.put(buildSelections.get(0) + " && " + buildSelections.get(1), probBuildABet);
                market.put("Build a bet B", 1 - probBuildABet);
                if (probBuildABet == 0.0) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("the probs of this bet equals 0.0");
                    alert.setContentText(buildABet.getBuildBetMap().keySet().toString());
                    alert.show();
                    return;
                }
                markets.addMarketWithShortKey(market);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Bet has been created successfully");
                alert.setContentText(market.toString());
                alert.show();
            }

        }

    }



    private StopWatch sw;

    /**
     * handles the user pressing the startParamFind button
     *
     * @param detailedLogReqd
     */
    public void handleStartParamFind(boolean detailedLogReqd) {
        marketPricesList.setGenerateDetailedParamFindLog(detailedLogReqd);
        sw = new StopWatch();
        sw.start();
        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        if (result.isParamFindScheduled()) {
            paramFindDialogController.addTextToDialogBox(
                            "\nTrigger param find trading rules determined param find should be scheduled.\nReason: "
                                            + result.getActionReason() + "\n");
            paramFindDialogController.addTextToDialogBox("\nParam find completed.  Results:\n");
            String s;
            try {
                s = JsonFormatter.format(paramFindResults.toString()) + "\n";
            } catch (JSONException e) {
                s = paramFindResults.toString();
            }
            s += String.format("Elapsed time: %.2f seconds\n", sw.getElapsedTimeSecs()) + "\n";
            paramFindDialogController.addTextToDialogBox(s);
        } else
            paramFindDialogController.addTextToDialogBox(
                            "\nTrigger param find trading rules determined param find should NOT be scheduled.\nReason: "
                                            + result.getActionReason());

    }

    private Stage showParamFindDialog(String s) {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/ParamFindDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Param Find");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            ParamFindDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            paramFindDialogController = controller;
            paramFindDialogController.setParent((MatchRunnable) this);
            paramFindDialogController.setTextInDialogBox(s);
            dialogStage.show();
            dialogStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if ((key.isControlDown() && (key.getCode() == KeyCode.F || key.getCode() == KeyCode.C))
                                || key.getCode() == KeyCode.ESCAPE)
                    paramFindDialogController.handleCancel();

            });

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    private Stage showPrematchResultDialog(String s) {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/PrematchResultDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Prematch result");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            PrematchResultDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            prematchResultController = controller;
            MatchResultMap matchResultMap = matchFormat.generateMatchResultProForma();
            observablePrematchResult = new ObservableMap((Map<String, String> map) -> matchResultMap.setFromMap(map));
            observablePrematchResult.updateDisplayedData(matchResultMap.extractMatchResult().getMap());
            prematchResultController.initialise(this, observablePrematchResult, matchResultMap);
            dialogStage.show();
            dialogStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if ((key.isControlDown() && (key.getCode() == KeyCode.F || key.getCode() == KeyCode.C))
                                || key.getCode() == KeyCode.ESCAPE)
                    prematchResultController.handleCancel();
            });

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    /**
     * called if user exists out of format edit dialog controller
     */
    public void exitApp() {}

    Map<String, String> displayMap;

    /**
     * called when user selects a row in the markets table. This generates the pop-up dialog to show the history
     *
     * @param mktKey
     * @param selKey
     */
    @Override
    public void handleMarketRowSelected(String fullKey, String selection) {
        /*
         * show the dialog box
         */
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/HistoryDisplayDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle(String.format("Market properties for %s", fullKey));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            MarketDetailDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            Market market = markets.getMarkets().get(fullKey);
            controller.setMarketDetails(fullKey, market, supportedSportType);
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public void showResultedMarkets() {
        showResultedMarketsDialog();
    }

    private Stage showResultedMarketsDialog() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/ResultedMarketsDisplay.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Markets awaiting Result and Resulted Markets");
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            ResultedMarketsDisplayController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            resultedMarketsDisplayController = controller;
            resultedMarketsDisplayController.initialize();
            resultedMarketsDisplayController.setResultedMarkets(observableResultedMarkets,
                            observableMarketsAwaitingResult);
            dialogStage.show();
            dialogStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if ((key.isControlDown() && (key.getCode() == KeyCode.F || key.getCode() == KeyCode.C))
                                || key.getCode() == KeyCode.ESCAPE)
                    resultedMarketsDisplayController.handleOk();
            });

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    private Stage showTradingRulesDialog() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/TradingRulesDisplay.fxml"));
            Object x = loader.load();
            AnchorPane page = (AnchorPane) x;
            dialogStage = new Stage();
            dialogStage.setTitle("Trading Rules");
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            TradingRulesDisplayController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            tradingRulesDisplayController = controller;
            tradingRulesDisplayController.initialize();
            TradingRules tradingRules = SupportedSports.getTradingRulesSet(supportedSportType);
            tradingRulesDisplayController.setTradingRules(tradingRules);
            dialogStage.show();
            dialogStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if ((key.isControlDown() && (key.getCode() == KeyCode.F || key.getCode() == KeyCode.C))
                                || key.getCode() == KeyCode.ESCAPE)
                    tradingRulesDisplayController.handleFinished();
            });

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    public void showTradingRules() {
        showTradingRulesDialog();
    }

    public void saveMatch() throws IOException {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Save Match");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Match");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("zip files", "*.zip"));
            Stage stage = new Stage();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                publishedRecording.writeToFile(file);
            }
            alert.setHeaderText("Match successfully saved");

        } catch (Exception e) {
            alert.setHeaderText("Error while saving match");
        }
        alert.showAndWait();
    }

    private boolean getMatchRecordingOk() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Replay match");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("zip files", "*.zip"));
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                savedRecording = Recording.readFromFile(file);
            } catch (Exception e) {
                String errMsg = "Error reading recording file " + file.getName() + ". See console for details";
                AlertBox.displayError(errMsg);
                System.exit(1);
            }
        } else {
            AlertBox.displayError("No file selected");
            System.exit(1);
        }

        /*
         * set the matchformat from the fist item of type PriceCalc rather than from the header.
         */

        for (RecordedItem item : savedRecording.getRecordedItemList()) {
            if (item.getRecordedItemType() == RecordedItemType.PRICE_CALC) {
                matchFormat = item.getPriceCalcRequest().getMatchFormat();
                break;
            }
        }
        if (matchFormat == null) {
            AlertBox.displayError("Empty recording - no PRICE_CALC items.  MatchRunner will exit");
            System.exit(1);
        }
        this.replayFileName = file.getName();
        return true;
    }

    private Stage showReplayDialog() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/ReplayMatchDisplay.fxml"));
            Object x = loader.load();
            BorderPane page = (BorderPane) x;
            dialogStage = new Stage();
            dialogStage.setTitle("Replay Match from file: " + replayFileName);
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            ReplayMatchDisplayController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            replayMatchDisplayController = controller;
            replayMatchDisplayController.initialize(this, savedRecording);
            dialogStage.show();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
        return dialogStage;
    }

    public void setReplayMatchFromFile(boolean replayMatchFromFile) {
        this.replayMatchFromFile = replayMatchFromFile;
    }

    public void setUseExternalModel(boolean useExternalModel) {
        this.useExternalModel = useExternalModel;
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams genericMatchParams) {
        BetsyncProxy.checkSerializationOk(genericMatchParams);
        this.matchParams = genericMatchParams;
        observableMatchParams.updateDisplayedMatchParams(matchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState sms) {
        if (matchOverviewController != null) {
            /*
             * matchState will get published the first time before matchOverviewController has been initialised
             */
            BetsyncProxy.checkSerializationOk(sms);
            matchOverviewController.updateObservableMatchState(sms);
        }
        this.matchState = algoManager.getMatchState(eventId);
        if (matchIncidentGenerator == null) {
            /*
             * using old form of incidents generator
             */
            prompt = matchState.getNextPrompt();
            incidentsPaneOldController.setNextPrompt(prompt);
        } else {
            /*
             * use new form of incidents generator
             */
            if (timerControlsPaneController != null) {
                int elapsedTime = timerControlsPaneController.getElapsedTime();
                GuiData guiData = matchIncidentGenerator.generateGuiData(sms);

                if (timerControlsPaneController.getClockChange().getText().contains("start")) {
                    guiData.setStopClock(true);
                } else {
                    guiData.setStopClock(false);

                }
                if (isUndoIncident) {
                    incidentsPaneV2Controller.updateGui(guiData, sms.elapsedTime());
                    timerControlsPaneController.updateGui(guiData, sms.elapsedTime());
                } else if (guiData.getGuiDataComponents().get(0).isOnlyPeriodChangeSelectable() == true) {
                    incidentsPaneV2Controller.updateGui(guiData, sms.elapsedTime());
                    timerControlsPaneController.updateGui(guiData, sms.elapsedTime());
                } else {
                    incidentsPaneV2Controller.updateGui(guiData, elapsedTime);
                    timerControlsPaneController.updateGui(guiData, elapsedTime);
                }
            } else {
                GuiData guiData = matchIncidentGenerator.generateGuiData(sms);
                incidentsPaneV2Controller.updateGui(guiData, sms.elapsedTime());
            }

        }
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.markets = markets;
        observableMarkets.update(markets);
        this.marketsAwaitingResult.getMarkets().putAll(markets.getMarkets());
        this.observableMarketsAwaitingResult.update(marketsAwaitingResult);
        for (int i = 0; i < nPriceSources; i++)
            observablePricesArray[i].setMarkets(markets);
        marketsCollector.addMarkets(markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.observableResultedMarkets.update(resultedMarkets);
        this.marketsAwaitingResult.getMarkets().keySet().removeAll(resultedMarkets.getResultedMarkets().keySet());
        this.observableMarketsAwaitingResult.update(marketsAwaitingResult);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults results, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.paramFindResults = results;
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        AlertBox.displayMsg("Event is completed");
    }

    public void notifyMarketCreated(long eventId, boolean isMarketCreated, String msg) {
        if (isMarketCreated)
            AlertBox.displayMsg("Market is createted successfully ");
        else
            AlertBox.displayMsg("Market is Not  createted due to : " + msg);
    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        publishedRecording.add(recordedItem);
        // System.out.println(recordedItem.getItemType());
        // System.out.println (recordedItem.getMarkets());
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        AlertBox.displayMsg("Fatal error notification received for AlgoMgr calc or pf.  Cause: " + errorCause);
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        AlertBox.displayMsg("TraderAlert: " + traderAlert.getTraderAlertType().toString());
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    /**
     * @param observableMarkets the observableMarkets to set
     */
    public void setObservableMarkets(ObservableMarkets observableMarkets) {
        this.observableMarkets = observableMarkets;
    }

    /**
     * @return the observableMarkets
     */
    public ObservableMarkets getObservableMarkets() {
        return observableMarkets;
    }

    /**
     * @return the markets
     */
    public Markets getMarkets() {
        return markets;
    }

    @Override
    public int getEventTier() {
        return eventTier;
    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {
        matchOverviewController.updateSuspensionStatus(suspend);

    }

    @Override
    public void setEventTier(int eventTier) {
        this.eventTier = eventTier;
        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", Integer.toString(eventTier));
        algoManager.handleSetEventProperties(eventId, properties);

    }

    @Override
    public void handleExportCollectedMarkets() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export markets to csv file");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export all markets to csv file");
            ExtensionFilter filter = new ExtensionFilter("csv files", "*.csv");
            fileChooser.getExtensionFilters().add(filter);
            Stage stage = new Stage();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                marketsCollector.exportMarkets(file);
            }
            alert.setHeaderText("Markets successfully exported");

        } catch (Exception e) {
            alert.setHeaderText("Error while exporting markets");
        }
        alert.showAndWait();
    }

    @Override
    public void handleRevertToEarlierState(String requestId) {
        algoManager.handleRevertToStatePreceedingRequestId(eventId, requestId);
    }

    @Override
    public void handleNewMatch(MatchFormat matchFormat) {
        this.matchFormat = matchFormat;
        algoManager.handleNewEventCreation(matchFormat.getSportType(), eventId, matchFormat);

    }



    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

        String eventTierStr = properties.get("eventTier");

        if (eventTierStr != null) {
            try {
                eventTier = Integer.parseInt(eventTierStr);
            } catch (NumberFormatException e) {
                error("Can't parse eventTier property for event: " + eventId + " property: " + eventTierStr);
            }
        }

        this.matchOverviewController.updateObservableEventTier(String.valueOf(eventTier));
        this.matchOverviewController.onEventEntry();
    }

    public void stopPanelClickAble() {
        this.incidentsPaneV2Controller.stopButtonClick();
    }

    public void startPanelClickAble() {
        this.incidentsPaneV2Controller.startButtonClick();
    }

    public void stopPanel2ClickAble() {
        this.controlButtonsPaneController.stopButtonClick();
    }

    public void startPanel2ClickAble() {
        this.controlButtonsPaneController.startButtonClick();
    }

    public void stopManualResult() {
        this.controlButtonsPaneController.stopManualResult();
    }

    public void stopPeridoStart() {
        if (this.timerControlsPaneController != null)
            this.timerControlsPaneController.stopPeriodStart();
    }

    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    public long getEventId() {
        return eventId;
    }

    public AlgoManager getAlgoManager() {
        return algoManager;
    }

    @Override
    public void handleMatchStateRequest() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MatchRunner.class.getResource("view/MatchStateDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle(String.format("Market State : "));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            MatchStateDialogController controller = loader.getController();
            matchStateDialogController = controller;
            matchStateDialogController.setMatchRunner(this);
            matchStateDialogController.setDialogStage(dialogStage);
            matchStateDialogController.setMarketDetails(supportedSportType);
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public void publishMatchStateMR(long eventId, MatchState matchState) {
        algoManager.setMatchState(eventId, matchState);
    }

    @Override
    public void setButtonClickAble(boolean buttonClickAble) {
        controlButtonsPaneController.buildABetOpen();
    }
}
