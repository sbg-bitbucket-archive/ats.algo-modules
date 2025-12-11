package ats.algo.outrights;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.outrights.model.ObservableAlerts;
import ats.algo.outrights.model.ObservableCompetitions;
import ats.algo.outrights.model.ObservableFcastStandings;
import ats.algo.outrights.model.ObservableFixtures;
import ats.algo.outrights.model.ObservableOutrightMarkets;
import ats.algo.outrights.model.ObservableOutrightResultedMarkets;
import ats.algo.outrights.model.ObservableStandings;
import ats.algo.outrights.model.ObservableTeams;
import ats.algo.outrights.view.OutrightMatchOverviewController;
import ats.algo.sport.outrights.server.api.Alerts;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.MarketsList;
import ats.algo.sport.outrights.server.api.ResultedMarketsList;
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OutrightGuiService extends AtsBean implements OutrightGuiHandler {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private OutrightMatchOverviewController outrightMatchOverviewController;
    private String matchTitle;
    private boolean logLevelIsTrace;
    private ObservableTeams observableTeams;

    private ObservableCompetitions observableCompetitions;
    private ObservableStandings observableStandings;
    private ObservableOutrightResultedMarkets observableOutrightResultedMarkets;
    private ObservableAlerts observableAlerts;
    private ObservableFixtures observableFixtures;
    private ObservableOutrightMarkets observableMarkets;
    private ObservableFcastStandings observableFcastStandings;
    // private final long eventId = 123456L;
    private OutrightGateway outrightGateway;
    private boolean brokenURL = false;
    private boolean autoConnection = false;
    private Long autoConnectionTime = 5L;
    private Timer timer;
    private final String DEFAULT_PATH_ISSUSPEND = "issuspended";
    private boolean isMarketsSuspending;

    /**
     * executed following the confirmation of the match format the returned map must contain three objects with keys
     * "MatchEngine", "MatchState", "MatchParams". Object in each case must be of the correct class
     *
     * @author Rob
     *
     */

    /**
     *
     * @param matchTitle
     * @param matchFormat
     * @param getMatchEngineFunction
     * @throws IOException
     */

    public OutrightGuiService(String matchTitle, String versionNo) {
        setLogLevel(Level.DEBUG);
        logLevelIsTrace = true;
        info("Changing log level to 'trace'");
        info("Current GUI version ：  %s", versionNo);
        this.initialiseMatchObjects();
        this.matchTitle = matchTitle + " : " + getURL();
        timer = new Timer();
    }

    private void setLogLevel(Level level) {
        LogUtil.initConsoleLogging(level);
    }

    @SuppressWarnings("unused")
    private void switchLogLevel() throws Exception {
        if (logLevelIsTrace) {
            info("Switching log level to WARN");
            setLogLevel(Level.WARN);
            logLevelIsTrace = false;
        } else {
            setLogLevel(Level.TRACE);
            logLevelIsTrace = true;
            info("Switching log level to TRACE");
            showMatchOverview();
        }
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OutrightGuiService.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.

            StackPane stack = new StackPane(rootLayout);



            Scene scene = new Scene(stack);
            // rootLayout.prefHeightProperty().bind(scene.heightProperty());
            // rootLayout.prefWidthProperty().bind(scene.widthProperty());
            primaryStage.setScene(scene);
            primaryStage.show();
            stack.prefWidthProperty().bind(primaryStage.widthProperty());
            stack.prefHeightProperty().bind(primaryStage.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * called following instantiation of the class
     * 
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(matchTitle);
        if (brokenURL) {
            primaryStage.close();
            error("Outrights Server in the URL %s does not start, please report to server side team. ",
                            outrightGateway.getUrl());
        } else {
            initRootLayout();
            showMatchOverview();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Calendar timerNow = Calendar.getInstance();
                    Date timerNowDate = timerNow.getTime();
                    System.out.println("1 min Timer reached: " + timerNowDate);
                    refreshOutrightGui(null);
                }

            };
            if (autoConnection) {
                timer = new Timer();
                timer.schedule(task, autoConnectionTime * 1000);
            }
        }

    }

    private void showMatchOverview() throws Exception {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OutrightGuiService.class.getResource("view/OutrightMatchOverviewV2.fxml"));
            AnchorPane matchOverview = (AnchorPane) loader.load();

            // Set overview into the centre of root layout.
            rootLayout.setCenter(matchOverview);
            matchOverview.prefHeightProperty().bind(rootLayout.heightProperty());
            matchOverview.prefWidthProperty().bind(rootLayout.widthProperty());
            outrightMatchOverviewController = loader.getController();
            outrightMatchOverviewController.initializeOutrightGuiService(this);
            outrightMatchOverviewController.initializeMatchOverviewController(observableCompetitions, observableTeams,
                            observableStandings, observableFixtures, observableMarkets, observableFcastStandings,
                            observableOutrightResultedMarkets, observableAlerts);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialiseMatchObjects() {
        /*
         * call back to the top level class to get the matchEngine following confirmation of match format by the user
         */
        outrightGateway = new OutrightGateway();
        if (testURL(outrightGateway.getUrl())) {
            info("good connection to url " + outrightGateway.getUrl());
            brokenURL = false;
        } else {
            brokenURL = true;
            return;
        }
        Map<String, String> params = new HashMap<>();
        String httpResponse = outrightGateway.getHttpResponse("outrights", null);
        observableCompetitions = new ObservableCompetitions();
        String errorTitle = "Error";
        if (!httpResponse.contains(errorTitle)) {
            CompetitionsList competitionsList = JsonSerializer.deserialize(httpResponse, CompetitionsList.class);
            observableCompetitions.update(competitionsList);
            params.put("eventID", String.valueOf(competitionsList.getCompetitionsList().get(0).getEventID()));
        }
        httpResponse = outrightGateway.getHttpResponse("teams", params);
        if (!httpResponse.contains(errorTitle)) {
            Teams teams = JsonSerializer.deserialize(httpResponse, Teams.class);

            observableTeams = new ObservableTeams();
            observableTeams.update(teams);
        }
        httpResponse = outrightGateway.getHttpResponse("standings", params);
        observableStandings = new ObservableStandings();
        if (!httpResponse.contains(errorTitle)) {
            StandingsList standings = JsonSerializer.deserialize(httpResponse, StandingsList.class);
            observableStandings.update(standings);
        }
        httpResponse = outrightGateway.getHttpResponse("fixtures", params);
        observableFixtures = new ObservableFixtures();
        if (!httpResponse.contains(errorTitle)) {
            FixturesList fixturesList = JsonSerializer.deserialize(httpResponse, FixturesList.class);
            observableFixtures.update(fixturesList);
        }
        httpResponse = outrightGateway.getHttpResponse("markets", params);
        observableMarkets = new ObservableOutrightMarkets();
        MarketsList marketsList = JsonSerializer.deserialize(httpResponse, MarketsList.class);
        observableMarkets.update(marketsList);

        httpResponse = outrightGateway.getHttpResponse("resultedmarkets", params);
        observableOutrightResultedMarkets = new ObservableOutrightResultedMarkets();
        if (!httpResponse.contains(errorTitle)) {
            ResultedMarketsList resultedMarketsList =
                            JsonSerializer.deserialize(httpResponse, ResultedMarketsList.class);
            observableOutrightResultedMarkets.update(resultedMarketsList);
        }

        httpResponse = outrightGateway.getHttpResponse("alerts", null);
        observableAlerts = new ObservableAlerts();
        Alerts alerts = JsonUtil.unmarshalJson(httpResponse, Alerts.class);
        observableAlerts.update(alerts);

        httpResponse = outrightGateway.getHttpResponse("fcaststandings", params);
        observableFcastStandings = new ObservableFcastStandings();
        FcastStandings fcaststandings = JsonUtil.unmarshalJson(httpResponse, FcastStandings.class);
        observableFcastStandings.update(fcaststandings);

        httpResponse = outrightGateway.getHttpResponse(DEFAULT_PATH_ISSUSPEND, params);
        Boolean suspend = JsonUtil.unmarshalJson(httpResponse, Boolean.class);
        isMarketsSuspending = suspend;

    }

    @Override
    public void refreshOutrightGui(Map<String, String> params) {
        Map<String, String> teamsIDName = new HashMap<>();
        String httpResponse = outrightGateway.getHttpResponse("teams", params);
        Teams teams = JsonSerializer.deserialize(httpResponse, Teams.class);
        ObservableTeams observableTeams = new ObservableTeams();
        observableTeams.update(teams);
        for (int i = 0; i < observableTeams.getData().size(); i++) {
            teamsIDName.put(observableTeams.getData().get(i).getTeamID().getValue(),
                            observableTeams.getData().get(i).getDisplayName().getValue());
        }

        httpResponse = outrightGateway.getHttpResponse("standings", params);
        ObservableStandings observableStandings = new ObservableStandings();
        StandingsList standings = JsonSerializer.deserialize(httpResponse, StandingsList.class);
        observableStandings.update(standings);

        httpResponse = outrightGateway.getHttpResponse("fixtures", params);
        ObservableFixtures observableFixtures = new ObservableFixtures();
        FixturesList fixturesList = JsonSerializer.deserialize(httpResponse, FixturesList.class);
        observableFixtures.update(fixturesList);

        httpResponse = outrightGateway.getHttpResponse("markets", params);
        ObservableOutrightMarkets observableMarkets = new ObservableOutrightMarkets();
        MarketsList marketsList = JsonSerializer.deserialize(httpResponse, MarketsList.class);
        observableMarkets.update(marketsList);

        httpResponse = outrightGateway.getHttpResponse("resultedmarkets", params);
        ObservableOutrightResultedMarkets observableOutrightResultedMarkets = new ObservableOutrightResultedMarkets();
        ResultedMarketsList resultedMarketsList = JsonSerializer.deserialize(httpResponse, ResultedMarketsList.class);
        observableOutrightResultedMarkets.update(resultedMarketsList);

        httpResponse = outrightGateway.getHttpResponse("outrights", null);
        ObservableCompetitions observableCompetitions = new ObservableCompetitions();
        CompetitionsList competitionsList = JsonSerializer.deserialize(httpResponse, CompetitionsList.class);
        observableCompetitions.update(competitionsList);
        outrightMatchOverviewController.setTeamIDName(teamsIDName);

        httpResponse = outrightGateway.getHttpResponse("fcaststandings", params);
        observableFcastStandings = new ObservableFcastStandings();
        FcastStandings fcaststandings = JsonUtil.unmarshalJson(httpResponse, FcastStandings.class);
        observableFcastStandings.update(fcaststandings);

        httpResponse = outrightGateway.getHttpResponse("alerts", null);
        observableAlerts = new ObservableAlerts();
        Alerts alerts = JsonUtil.unmarshalJson(httpResponse, Alerts.class);
        observableAlerts.update(alerts);

        httpResponse = outrightGateway.getHttpResponse(DEFAULT_PATH_ISSUSPEND, params);
        Boolean suspend = JsonUtil.unmarshalJson(httpResponse, Boolean.class);
        isMarketsSuspending = suspend;

        try {
            outrightMatchOverviewController.updateMatchOverviewController(observableCompetitions, observableTeams,
                            observableStandings, observableFixtures, observableMarkets, observableFcastStandings,
                            observableOutrightResultedMarkets, observableAlerts);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void refreshOutrightMarket(Map<String, String> params, String selectionFilter) {
        String httpResponse = outrightGateway.getHttpResponse("markets", params);
        ObservableOutrightMarkets observableMarkets = new ObservableOutrightMarkets();
        MarketsList marketsList = JsonSerializer.deserialize(httpResponse, MarketsList.class);
        observableMarkets.update(marketsList, selectionFilter);


        try {
            outrightMatchOverviewController.updateMarketController(observableMarkets);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean testURL(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
                return true;

            } else
                return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void publishUpdate(String body) {
        info("Update is %s ", body);
    }

    public void publishInfo(String body) {
        info("info %s ", body);
    }

    public boolean isMarketsSuspending() {
        return isMarketsSuspending;
    }

    public void setMarketsSuspending(boolean isMarketsSuspending) {
        this.isMarketsSuspending = isMarketsSuspending;
    }

    public String getURL() {
        return outrightGateway.getUrl();
    }

    public static void main(String[] args) {
        OutrightGuiService outrightMatchRunner = new OutrightGuiService("Outright model", "v1.0");
        outrightMatchRunner.initialiseMatchObjects();
    }

}
