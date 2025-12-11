package ats.algo.outrights.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import java.util.Map;
import javafx.scene.Node;
import com.google.common.primitives.Doubles;

import ats.algo.genericsupportfunctions.OddsLadder;
import ats.algo.matchrunner.view.AlertBox;
import ats.algo.outrights.OutrightGateway;
import ats.algo.outrights.OutrightGuiService;
import ats.algo.outrights.model.ObservableAlerts;
import ats.algo.outrights.model.ObservableAlerts.ObservableAlert;
import ats.algo.outrights.model.ObservableCompetitions;
import ats.algo.outrights.model.ObservableCompetitions.ObservableCompetition;
import ats.algo.outrights.model.ObservableFcastStandings;
import ats.algo.outrights.model.ObservableFcastStandings.ObservableFcastStanding;
import ats.algo.outrights.model.ObservableFixtures;
import ats.algo.outrights.model.ObservableOutrightMarkets;
import ats.algo.outrights.model.ObservableOutrightMarkets.ObservableOutrightMarket;
import ats.algo.outrights.model.ObservableOutrightResultedMarkets;
import ats.algo.outrights.model.ObservableOutrightResultedMarkets.ObservableOutrightResultedMarket;
import ats.algo.outrights.model.ObservableStandings;
import ats.algo.outrights.model.ObservableTeams;
import ats.algo.outrights.model.ObservableFixtures.ObservableFixture;
import ats.algo.outrights.model.ObservableStandings.ObservableStanding;
import ats.algo.outrights.model.ObservableTeams.ObservableTeam;
import ats.algo.sport.outrights.calcengine.core.FcastStanding;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.server.api.Alert;
import ats.algo.sport.outrights.server.api.AlertType;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.CompetitionsListEntry;
import ats.algo.sport.outrights.server.api.EventID;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.FixturesListEntry;
import ats.algo.sport.outrights.server.api.PostReply;
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.algo.sport.outrights.server.api.StandingsListEntry;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;
import ats.algo.sport.outrights.server.api.TargetPointsList;
import ats.algo.sport.outrights.server.api.TeamObject;
import ats.core.util.json.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.text.Font;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;

public class OutrightMatchOverviewController {

    private OutrightGuiService outrightGuiService;

    @FXML
    private TableView<ObservableTeam> teamsTableRatings;
    @FXML
    private TableColumn<ObservableTeam, String> teamID;
    @FXML
    private TableColumn<ObservableTeam, String> displayName;
    @FXML
    private TableColumn<ObservableTeam, String> ratingAttack;
    @FXML
    private TableColumn<ObservableTeam, String> ratingDefense;
    @FXML
    private TableColumn<ObservableTeam, String> ratingsBiasAttack;
    @FXML
    private TableColumn<ObservableTeam, String> ratingsBiasDefense;
    @FXML
    private TableColumn<ObservableTeam, String> upTeam;
    @FXML
    private TableView<ObservableTeam> teamsNameTable;
    @FXML
    private TableColumn<ObservableTeam, String> displayName1;
    @FXML
    private TableColumn<ObservableTeam, String> fiveThirtyEightName;
    @FXML
    private TableColumn<ObservableTeam, String> lsportsName;
    @FXML
    private TableColumn<ObservableTeam, String> sportingIndexName;
    @FXML
    private TableColumn<ObservableTeam, String> upTeamName;

    @FXML
    private TableView<ObservableStanding> standingsTable;
    @FXML
    private TableColumn<ObservableStanding, String> teamId;
    @FXML
    private TableColumn<ObservableStanding, String> played;
    @FXML
    private TableColumn<ObservableStanding, String> won;
    @FXML
    private TableColumn<ObservableStanding, String> drawn;
    @FXML
    private TableColumn<ObservableStanding, String> lost;
    @FXML
    private TableColumn<ObservableStanding, String> goalsFor;
    @FXML
    private TableColumn<ObservableStanding, String> goalsAgainst;
    @FXML
    private TableColumn<ObservableStanding, String> goalDiff;
    @FXML
    private TableColumn<ObservableStanding, String> points;
    @FXML
    private TableColumn<ObservableStanding, String> hpp;
    @FXML
    private TableColumn<ObservableStanding, String> lpp;
    @FXML
    private TableColumn<ObservableStanding, String> mpa;
    @FXML
    private TableColumn<ObservableStanding, String> mtba;
    @FXML
    private TableColumn<ObservableStanding, String> upStanding;

    @FXML
    private TableView<ObservableFcastStanding> fstandingsTable;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fteamId;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fplayed;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fwon;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fdrawn;
    @FXML
    private TableColumn<ObservableFcastStanding, String> flost;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fgoalsFor;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fgoalsAgainst;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fgoalDiff;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fpoints;
    @FXML
    private TableColumn<ObservableFcastStanding, String> ftargetpoints;
    @FXML
    private TableColumn<ObservableFcastStanding, String> fupStanding;

    @FXML
    private TableView<ObservableFixture> fixturesTable;
    @FXML
    private TableColumn<ObservableFixture, String> date;
    @FXML
    private TableColumn<ObservableFixture, String> homeTeamID;
    @FXML
    private TableColumn<ObservableFixture, String> awayTeamID;
    @FXML
    private TableColumn<ObservableFixture, String> eventID;
    @FXML
    private TableColumn<ObservableFixture, String> suspend;
    @FXML
    private TableColumn<ObservableFixture, String> tag;
    @FXML
    private TableColumn<ObservableFixture, String> playedAtNeutralGround;
    @FXML
    private TableColumn<ObservableFixture, String> status;
    @FXML
    private TableColumn<ObservableFixture, String> goalsHome;
    @FXML
    private TableColumn<ObservableFixture, String> goalsAway;
    @FXML
    private TableColumn<ObservableFixture, String> score;
    @FXML
    private TableColumn<ObservableFixture, String> probsSourcedfromATS;
    @FXML
    private TableColumn<ObservableFixture, String> fixtureType;
    @FXML
    private TableColumn<ObservableFixture, String> fixtureID;
    @FXML
    private TableColumn<ObservableFixture, String> pHome;
    @FXML
    private TableColumn<ObservableFixture, String> pAway;
    @FXML
    private TableColumn<ObservableFixture, String> pDraw;
    @FXML
    private TableColumn<ObservableFixture, String> upFixture;

    @FXML
    private TableView<ObservableOutrightMarket> marketsTable;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketsId;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketsName;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketSelections;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketSuspensionStatus;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketsProbs;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketsPrice;
    @FXML
    private TableColumn<ObservableOutrightMarket, String> marketsMargin;

    @FXML
    private TableView<ObservableOutrightResultedMarket> resultedMarketsTable;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketType;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketSubType;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketSequenceId;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketDescription;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketActualLine;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketWinningSelections;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketLosingSelections;
    @FXML
    private TableColumn<ObservableOutrightResultedMarket, String> resultedMarketFullyResulted;

    @FXML
    private TableView<ObservableAlert> alertsTable;
    @FXML
    private TableColumn<ObservableAlert, String> alertType;
    @FXML
    private TableColumn<ObservableAlert, String> alertEventId;
    @FXML
    private TableColumn<ObservableAlert, String> dateTime;
    @FXML
    private TableColumn<ObservableAlert, String> description;

    @FXML
    private TableView<ObservableCompetition> competitionTable;
    @FXML
    private TableColumn<ObservableCompetition, String> competitionID;
    @FXML
    private TableColumn<ObservableCompetition, String> competitionName;
    @FXML
    private TableColumn<ObservableCompetition, String> competitionEventID;
    @FXML
    private TableColumn<ObservableCompetition, String> competitionATSID;
    @FXML
    private TableColumn<ObservableCompetition, String> competitionUpdate;

    @FXML
    private volatile TextField logText;
    @FXML
    private TextField logTextURL;
    @FXML
    private TextField marketFilter;
    @FXML
    private TextArea updateText;
    @FXML
    CheckBox standingEditable;
    @FXML
    private Tab tabAlerts;
    @FXML
    Button debug;
    @FXML
    Button marketStatusOn;
    @FXML
    Button manage;
    @FXML
    Tab competitionsTab;
    @FXML
    Tab teamNameTab;

    @SuppressWarnings("rawtypes")
    @FXML
    private ComboBox competitionCombo = new ComboBox<>();
    @FXML
    private ObservableList<String> options;

    private Map<String, Team> updateTeam = new HashMap<>();
    private Map<String, CompetitionsListEntry> updateCompetition = new HashMap<>();
    private Map<String, Boolean> updatedTeam = new HashMap<>();
    private Map<String, FixturesListEntry> fixturesListEntry = new HashMap<>();
    private Map<String, StandingsListEntry> updatedStanding = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> eventCompetition = new HashMap<>();
    private Map<String, String> teamsIDName = new HashMap<>();
    private Map<String, Double> teamsIDPlayed = new HashMap<>();

    private final String DEFAULT_PATH_PARAM_FIND = "paramfind";
    private final String DEFAULT_PATH_UPDATE_RATINGS = "updateratings";
    private final String DEFAULT_PATH_UPDATE_ATS = "updateatsdata";
    private final String DEFAULT_PATH_STANDINGS = "standings";
    private final String DEFAULT_PATH_UPDATE_TARGET_POINTS = "updatetargetpoints";
    private final String DEFAULT_PATH_TARGET_POINTS = "targetpoints";
    public final String DEFAULT_PATH_TEAMS = "team";
    public final String DEFAULT_PATH_FIXTURES = "fixtures";
    private final String DEFAULT_PATH_SUSPEND_MARKETS = "suspendmarkets";
    private final String DEFAULT_PATH_UNSUSPEND_MARKETS = "unsuspendmarkets";
    private final String DEFAULT_EVENT_ID_KEY = "eventID";
    private final String DEFAULT_PATH_COMPETITIONS = "competitions";
    private final String DEFAULT_PATH_ALERT = "alert";

    private final String DEFAULT_ERROR_VALID_INT = "%s is not a valid int";
    private final String DEFAULT_ERROR_VALID_STATUS = "%s is not a valid status";
    private final String DEFAULT_ERROR_VALID_NUM = "%s is not a valid number";
    private final String DEFAULT_ERROR_VALID_EVENTID = "%s is not a valid EventId";
    private final String DEFAULT_ERROR_SAME_VALUE = "is same Value, do nothing";
    private final String DEFAULT_ERROR_SAME_RATE = "%s is not a valid Rate that should be between (0,5)";
    private final String DEFAULT_ERROR_SAME_SCORE = "%s is not a valid score format like 3-2";

    private final String lightBlueTextColor = "rgb(136, 206, 250, 0.99)";
    private final String greenTextColor = "rgb(0, 204, 0, 0.99)";
    private final String balckTextColor = "black";
    private final String defaultFillinTextColor = balckTextColor;

    private boolean useColor = true;
    private TargetPointsList tPointsList;
    private boolean editStanding = true;
    private boolean suspendMarkets;
    private final DecimalFormat dfThreeDecimal = new DecimalFormat("#0.000");
    private final DecimalFormat dfTwoDecimal = new DecimalFormat("#0.00");
    private final BooleanProperty numbersChanged = new SimpleBooleanProperty(false);
    private OutrightGateway outrightGateway;
    private String updateCompetitionName = "";


    @FXML
    private void initialize() {
        /*
         * for reasons not understood when this is called none of the FXML objects have been instantiated so can't
         * initialise anything in this method. Instead get initialised in the "initialiseMatchOverviewController" method
         * which is explicitly called by MatchRunner
         */
    }

    public void initializeOutrightGuiService(OutrightGuiService outrightGuiService) {
        this.outrightGuiService = outrightGuiService;
        outrightGateway = new OutrightGateway();
        tPointsList = new TargetPointsList();
        updateText.setEditable(false);
        updateText.setWrapText(true);
        logText.setEditable(false);
        standingEditable.setVisible(false);
        suspendMarkets = outrightGuiService.isMarketsSuspending();
        if (suspendMarkets)
            marketStatusOn.setText("Unsuspend markets");
        else
            marketStatusOn.setText("Suspend markets");
        teamsNameTable.setVisible(false);
        competitionTable.setVisible(false);
        competitionsTab.setDisable(true);
        teamNameTab.setDisable(true);
        logTextURL.setEditable(false);
        logTextURL.setText("Server is : " + outrightGuiService.getURL());
        logTextURL.setVisible(false);
        marketFilter.setText("");
        marketFilter.setVisible(true);
        marketFilter.setEditable(true);
    }

    @SuppressWarnings("unchecked")
    public void initializeMatchOverviewController(ObservableCompetitions observableCompetitions,
                    ObservableTeams observableTeams, ObservableStandings observableStandings,
                    ObservableFixtures observableFixtures, ObservableOutrightMarkets observableMarkets,
                    ObservableFcastStandings observableFcastStandings,
                    ObservableOutrightResultedMarkets observableResultedMarkets, ObservableAlerts observableAlerts)
                    throws Exception {

        /*
         * set up competition
         */

        options = FXCollections.observableArrayList();
        for (int i = 0; i < observableCompetitions.getData().size(); i++) {
            String eventID = observableCompetitions.getData().get(i).eventIDProperty().getValue();
            eventCompetition.put(eventID, observableCompetitions.getData().get(i).competitionNameProperty().getValue());
            String compeitionEvent = observableCompetitions.getData().get(i).competitionNameProperty().getValue()
                            + " : (" + eventID + ")";
            options.add(compeitionEvent);
        }
        competitionCombo.setItems(options);
        competitionCombo.getSelectionModel().select(0);
        competitionCombo.setVisibleRowCount(10);

        if (observableTeams != null) {
            for (int i = 0; i < observableTeams.getData().size(); i++)
                teamsIDName.put(observableTeams.getData().get(i).getTeamID().getValue(),
                                observableTeams.getData().get(i).getDisplayName().getValue());
            params.clear();
        }
        if (competitionCombo.getSelectionModel().getSelectedItem() != null) {
            String selectedEventID = competitionCombo.getSelectionModel().getSelectedItem().toString();
            String selectedEventID2 =
                            selectedEventID.substring(selectedEventID.indexOf("(") + 1, selectedEventID.indexOf(")"));
            params.put(DEFAULT_EVENT_ID_KEY, selectedEventID2);
            updateCompetitionName = selectedEventID.substring(0, selectedEventID.indexOf(":"));
        }

        updateMatchOverviewController(observableCompetitions, observableTeams, observableStandings, observableFixtures,
                        observableMarkets, observableFcastStandings, observableResultedMarkets, observableAlerts);
        onRefreshButtonPressed();
        manageDefaultCssStyle();

    }

    private void manageDefaultCssStyle() {
        /*
         * css color style code
         */
        String chosedTextColor = greenTextColor;
        if (useColor) {
            chosedTextColor = greenTextColor;
        } else {
            chosedTextColor = lightBlueTextColor;

        }
        outrightGuiService.publishInfo("defaule color not editable is Black");
        if (!chosedTextColor.equals(greenTextColor))
            outrightGuiService.publishInfo("defaule color editable is BlueTextColor");
        else
            outrightGuiService.publishInfo("defaule color editable is Green");

        eventID.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        goalsHome.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        goalsAway.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        score.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        status.setStyle("-fx-alignment: CENTER;");
        teamID.setStyle("-fx-alignment: CENTER;");
        displayName.setStyle("-fx-alignment: CENTER;");
        date.setStyle("-fx-alignment: CENTER;");
        homeTeamID.setStyle("-fx-alignment: CENTER;");
        awayTeamID.setStyle("-fx-alignment: CENTER;");
        suspend.setStyle("-fx-alignment: CENTER;");
        tag.setStyle("-fx-alignment: CENTER;");
        playedAtNeutralGround.setStyle("-fx-alignment: CENTER;");
        fixtureType.setStyle("-fx-alignment: CENTER;");
        fixtureID.setStyle("-fx-alignment: CENTER;");
        pHome.setStyle("-fx-alignment: CENTER;");
        pAway.setStyle("-fx-alignment: CENTER;");
        pDraw.setStyle("-fx-alignment: CENTER;");
        marketsId.setStyle("-fx-alignment: CENTER;");
        marketSuspensionStatus.setStyle("-fx-alignment: CENTER;");
        marketSelections.setStyle("-fx-alignment: CENTER;");
        marketsName.setStyle("-fx-alignment: CENTER;");
        marketsPrice.setStyle("-fx-alignment: CENTER;");
        marketsProbs.setStyle("-fx-alignment: CENTER;");
        marketsMargin.setStyle("-fx-alignment: CENTER;");
        resultedMarketActualLine.setStyle("-fx-alignment: CENTER;");
        resultedMarketDescription.setStyle("-fx-alignment: CENTER;");
        resultedMarketSequenceId.setStyle("-fx-alignment: CENTER;");
        resultedMarketSubType.setStyle("-fx-alignment: CENTER;");
        resultedMarketType.setStyle("-fx-alignment: CENTER;");
        resultedMarketWinningSelections.setStyle("-fx-alignment: CENTER;");
        resultedMarketLosingSelections.setStyle("-fx-alignment: CENTER;");
        resultedMarketFullyResulted.setStyle("-fx-alignment: CENTER;");
        alertType.setStyle("-fx-alignment: CENTER;");
        alertEventId.setStyle("-fx-alignment: CENTER;");
        description.setStyle("-fx-alignment: CENTER;");
        dateTime.setStyle("-fx-alignment: CENTER;");
        ratingDefense.setStyle("-fx-alignment: CENTER;");
        ratingAttack.setStyle("-fx-alignment: CENTER;");
        ratingsBiasAttack.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        ratingsBiasDefense.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        displayName1.setStyle("-fx-alignment: CENTER;");
        fiveThirtyEightName.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        lsportsName.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        sportingIndexName.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");


        competitionID.setStyle("-fx-alignment: CENTER;");
        competitionName.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        competitionEventID.setStyle("-fx-alignment: CENTER;");
        competitionATSID.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        competitionUpdate.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");

        upTeam.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        upTeamName.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        upFixture.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        upStanding.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        teamId.setStyle("-fx-alignment: CENTER;");
        fteamId.setStyle("-fx-alignment: CENTER;");
        fwon.setStyle("-fx-alignment: CENTER;");
        fplayed.setStyle("-fx-alignment: CENTER;");
        fdrawn.setStyle("-fx-alignment: CENTER;");
        flost.setStyle("-fx-alignment: CENTER;");
        fgoalsFor.setStyle("-fx-alignment: CENTER;");
        fgoalsAgainst.setStyle("-fx-alignment: CENTER;");
        fpoints.setStyle("-fx-alignment: CENTER;");
        fgoalDiff.setStyle("-fx-alignment: CENTER;");
        ftargetpoints.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        fupStanding.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        if (editStanding) {
            won.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            played.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            drawn.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            lost.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsFor.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsAgainst.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalDiff.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            points.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");


        } else {
            won.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            played.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            drawn.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            lost.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsFor.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsAgainst.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalDiff.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            points.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
        }
        hpp.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
        lpp.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
        mpa.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        mtba.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");

        updateText.setFont(Font.font("Verdana", 20));
        logText.setFont(Font.font("Verdana", 20));
        logText.setBorder(Border.EMPTY);
        logText.setBackground(Background.EMPTY);
        logText.setStyle(" -fx-text-fill: red;");

        logTextURL.setFont(Font.font("Verdana", 12));
        logTextURL.setBorder(Border.EMPTY);
        logTextURL.setBackground(Background.EMPTY);

    }

    /**
     * 
     * @throws Exception
     */
    public void updateMarketController(ObservableOutrightMarkets observableMarkets) throws Exception {
        /*
         * Set up markets associations
         */
        if (observableMarkets != null) {
            marketsTable.setItems(observableMarkets.getData());
            marketsId.setCellValueFactory(cellData -> cellData.getValue().marketKeyProperty());
            marketsName.setCellValueFactory(cellData -> cellData.getValue().marketNameProperty());
            marketSelections.setCellValueFactory(cellData -> cellData.getValue().marketSelectionProperty());
            marketSuspensionStatus.setCellValueFactory(cellData -> cellData.getValue().marketStateProperty());
            marketsProbs.setCellValueFactory(cellData -> cellData.getValue().marketProbProperty());

            marketsPrice.setCellValueFactory(cellData -> new SimpleStringProperty(OddsLadder
                            .applyOddsString(Double.valueOf(cellData.getValue().marketPriceProperty().getValue()))));
            marketsMargin.setCellValueFactory(cellData -> cellData.getValue().marketMarginProperty());

            marketSuspensionStatus.setCellFactory(column -> {
                return new TableCell<ObservableOutrightMarket, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                        } else {
                            setText(item);
                            switch (item) {
                                /*
                                 * c.f. https://material.google.com/style/color.html#color- color-palette for colours
                                 */
                                case "OP":
                                case " OP":
                                    setStyle("-fx-background-color: #C5E1A5;-fx-alignment: CENTER;"); // green
                                    break;
                                case "SD":
                                case " SD":
                                    setStyle("-fx-background-color: #FFF59D;-fx-alignment: CENTER;");// yellow
                                    break;
                                case "SU":
                                case " SU":
                                    setStyle("-fx-background-color: #FF8F00;-fx-alignment: CENTER;");// amber
                                    break;
                                case "CL":
                                case " CL":
                                    setStyle("-fx-background-color: #F44336;-fx-alignment: CENTER;");// red
                                    break;
                                case "AU":
                                case " AU":
                                    setStyle("-fx-background-color: #00FFFF;-fx-alignment: CENTER;");// blue
                                    break;
                                default:
                                    setStyle("");
                                    break;
                            }
                        }
                    }
                };
            });
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void updateMatchOverviewController(ObservableCompetitions observableCompetitions,
                    ObservableTeams observableTeams, ObservableStandings observableStandings,
                    ObservableFixtures observableFixtures, ObservableOutrightMarkets observableMarkets,
                    ObservableFcastStandings observableFStandings,
                    ObservableOutrightResultedMarkets observableOutrightResultedMarkets,
                    ObservableAlerts observableAlerts) throws Exception {
        /*
         * Set up competitions associations
         */
        if (observableCompetitions != null) {
            competitionTable.setItems(observableCompetitions.getData());
            competitionID.setCellValueFactory(cellData -> cellData.getValue().outrightsCompetitionIDProperty());
            competitionName.setCellValueFactory(cellData -> cellData.getValue().competitionNameProperty());
            competitionEventID.setCellValueFactory(cellData -> cellData.getValue().eventIDProperty());
            competitionATSID.setCellValueFactory(cellData -> cellData.getValue().atsCompetitionIDProperty());
            competitionUpdate.setCellValueFactory(cellData -> cellData.getValue().competitionUpdatedProperty());

            competitionName.setEditable(true);
            competitionName.setCellFactory(TextFieldTableCell.<ObservableCompetition>forTableColumn());
            competitionATSID.setEditable(true);
            competitionATSID.setCellFactory(TextFieldTableCell.<ObservableCompetition>forTableColumn());

            competitionName.setOnEditCommit(new EventHandler<CellEditEvent<ObservableCompetition, String>>() {

                public void handle(CellEditEvent<ObservableCompetition, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableCompetition o = (ObservableCompetition) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setCompetitionUpdated("Yes");
                        o.setCompetitionName(t.getNewValue());
                        handleCompetitionRowSelect(o);
                    }
                    competitionTable.refresh();
                }
            });

            competitionATSID.setOnEditCommit(new EventHandler<CellEditEvent<ObservableCompetition, String>>() {

                public void handle(CellEditEvent<ObservableCompetition, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableCompetition o = (ObservableCompetition) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setCompetitionUpdated("Yes");
                        o.setAtsCompetitionID(t.getNewValue());
                        handleCompetitionRowSelect(o);
                    }
                    competitionTable.refresh();
                }
            });
        }

        /*
         * Set up Alerts
         */
        if (observableAlerts != null) {
            tabAlerts.setStyle("-fx-background-color: #C5E1A5;");
            boolean updateAmber = false;
            boolean updateRed = false;
            for (ObservableAlert alert : observableAlerts.getData()) {

                if (alert.getAlertType().getValue().equals(AlertType.ERROR_ACK.toString())
                                || alert.getAlertType().getValue().equals(AlertType.WARNING.toString()))
                    updateAmber = true;
                if (alert.getAlertType().getValue().equals(AlertType.ERROR.toString()))
                    updateRed = true;
            }
            if (updateAmber)
                tabAlerts.setStyle("-fx-background-color:  #FF8F00;");
            if (updateRed)
                tabAlerts.setStyle("-fx-background-color: #F44336;");

            alertsTable.setItems(observableAlerts.getData());
            alertsTable.setRowFactory(tv -> {
                TableRow<ObservableAlert> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        ObservableAlert rowData = row.getItem();
                        Alert alert = rowData.getAlert();
                        alert.setAcknowledged(true);
                        PostRequest(alert.toString(), DEFAULT_PATH_ALERT);
                    }
                });
                return row;
            });
            alertType.setCellValueFactory(cellData -> cellData.getValue().alertTypeProperty());
            alertEventId.setCellValueFactory(cellData -> cellData.getValue().eventIdProperty());
            dateTime.setCellValueFactory(cellData -> cellData.getValue().dateTimeProperty());
            description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
            alertType.setCellFactory(column -> {
                return new TableCell<ObservableAlert, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                        } else {
                            setText(item);
                            switch (item) {
                                /*
                                 * c.f. https://material.google.com/style/color.html#color- color-palette for colours
                                 */
                                case "INFO":
                                    setStyle("-fx-background-color: #C5E1A5;-fx-alignment: CENTER;"); // green
                                    break;
                                case "WARNING":
                                case "ERROR_ACK":
                                    setStyle("-fx-background-color: #FF8F00;-fx-alignment: CENTER;");// amber
                                    break;
                                case "ERROR":
                                    setStyle("-fx-background-color: #F44336;-fx-alignment: CENTER;");// red
                                    break;
                                default:
                                    setStyle("");
                                    break;
                            }
                        }
                    }
                };
            });

        }


        /*
         * Set up markets associations
         */
        if (observableMarkets != null) {
            marketsTable.setItems(observableMarkets.getData());
            marketsId.setCellValueFactory(cellData -> cellData.getValue().marketKeyProperty());
            marketsName.setCellValueFactory(cellData -> cellData.getValue().marketNameProperty());
            marketSelections.setCellValueFactory(cellData -> cellData.getValue().marketSelectionProperty());
            marketSuspensionStatus.setCellValueFactory(cellData -> cellData.getValue().marketStateProperty());
            marketsProbs.setCellValueFactory(cellData -> cellData.getValue().marketProbProperty());

            marketsPrice.setCellValueFactory(cellData -> new SimpleStringProperty(OddsLadder
                            .applyOddsString(Double.valueOf(cellData.getValue().marketPriceProperty().getValue()))));
            marketsMargin.setCellValueFactory(cellData -> cellData.getValue().marketMarginProperty());

            marketSuspensionStatus.setCellFactory(column -> {
                return new TableCell<ObservableOutrightMarket, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                        } else {
                            setText(item);
                            switch (item) {
                                /*
                                 * c.f. https://material.google.com/style/color.html#color- color-palette for colours
                                 */
                                case "OP":
                                case " OP":
                                    setStyle("-fx-background-color: #C5E1A5;-fx-alignment: CENTER;"); // green
                                    break;
                                case "SD":
                                case " SD":
                                    setStyle("-fx-background-color: #FFF59D;-fx-alignment: CENTER;");// yellow
                                    break;
                                case "SU":
                                case " SU":
                                    setStyle("-fx-background-color: #FF8F00;-fx-alignment: CENTER;");// amber
                                    break;
                                case "CL":
                                case " CL":
                                    setStyle("-fx-background-color: #F44336;-fx-alignment: CENTER;");// red
                                    break;
                                case "AU":
                                case " AU":
                                    setStyle("-fx-background-color: #00FFFF;-fx-alignment: CENTER;");// blue
                                    break;
                                default:
                                    setStyle("");
                                    break;
                            }
                        }
                    }
                };
            });
            marketsId.setSortable(false);
            marketsName.setSortable(false);
            marketSelections.setSortable(false);
            marketSuspensionStatus.setSortable(false);
            marketsProbs.setSortable(false);
            marketsPrice.setSortable(false);
            marketSuspensionStatus.setSortable(false);
        }

        /*
         * Set up resulted markets associations
         */
        if (observableOutrightResultedMarkets != null) {
            resultedMarketsTable.setItems(observableOutrightResultedMarkets.getData());
            resultedMarketType.setCellValueFactory(cellData -> cellData.getValue().resultedMarketTypeProperty());
            resultedMarketSubType.setCellValueFactory(cellData -> cellData.getValue().resultedMarketSubTypeProperty());
            resultedMarketSequenceId
                            .setCellValueFactory(cellData -> cellData.getValue().resultedMarketSequenceIdProperty());
            resultedMarketDescription
                            .setCellValueFactory(cellData -> cellData.getValue().resultedMarketDescriptionProperty());
            resultedMarketActualLine.setCellValueFactory(cellData -> cellData.getValue().resultedMarketActualLine());
            resultedMarketWinningSelections
                            .setCellValueFactory(cellData -> cellData.getValue().resultedMarketWinningSelection());
            resultedMarketFullyResulted
                            .setCellValueFactory(cellData -> cellData.getValue().resultedMarketFullyResulted());
            resultedMarketLosingSelections
                            .setCellValueFactory(cellData -> cellData.getValue().resultedMarketLosingSelection());
            resultedMarketWinningSelections.setResizable(true);
            resultedMarketLosingSelections.setResizable(true);
            resultedMarketFullyResulted.setResizable(true);
        }


        /*
         * set up fixture
         */
        if (observableFixtures != null) {
            fixturesTable.setItems(observableFixtures.getData());
            date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            homeTeamID.setCellValueFactory(cellData -> new SimpleStringProperty(
                            teamsIDName.get(cellData.getValue().homeTeamIDProperty().getValue())));
            awayTeamID.setCellValueFactory(cellData -> new SimpleStringProperty(
                            teamsIDName.get(cellData.getValue().awayTeamIDProperty().getValue())));
            eventID.setCellValueFactory(cellData -> cellData.getValue().eventIDProperty());
            suspend.setCellValueFactory(cellData -> cellData.getValue().suspendProperty());
            tag.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
            playedAtNeutralGround.setCellValueFactory(cellData -> cellData.getValue().playedAtNeutralGroundProperty());
            status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
            fixtureType.setCellValueFactory(cellData -> cellData.getValue().fixtureTypeProperty());
            fixtureID.setCellValueFactory(cellData -> cellData.getValue().fixtureIDProperty());
            goalsAway.setCellValueFactory(cellData -> cellData.getValue().goalsAwayProperty());
            goalsHome.setCellValueFactory(cellData -> cellData.getValue().goalsHomeProperty());
            score.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());
            pHome.setCellValueFactory(cellData -> cellData.getValue().pHomeProperty());
            pAway.setCellValueFactory(cellData -> cellData.getValue().pAwayProperty());
            pDraw.setCellValueFactory(cellData -> cellData.getValue().pDrawProperty());
            probsSourcedfromATS.setCellValueFactory(cellData -> new SimpleStringProperty(
                            cellData.getValue().probsSourcedfromATSProperty().getValue().equals("true") ? "ATS"
                                            : "RATING"));
            upFixture.setCellValueFactory(cellData -> cellData.getValue().fixtureUpdateProperty());

            probsSourcedfromATS.setStyle("-fx-alignment: CENTER;");
            fixturesTable.setRowFactory(tv -> {
                TableRow<ObservableFixture> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    ObservableFixture rowData = row.getItem();
                    if (!row.isEmpty())
                        if (event.getButton() == MouseButton.PRIMARY)
                            updateText.setText(rowData.toString());
                });
                return row;
            });
            fixturesTable.setEditable(true);
            eventID.setEditable(true);
            eventID.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            eventID.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isLong(t.getNewValue())) {
                    o.setEventID(Long.valueOf(t.getNewValue()));
                    o.setFixtureUpdate("Yes");
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_EVENTID, t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                fixturesTable.refresh();
            });

            suspend.setCellFactory(column -> {
                return new TableCell<ObservableFixture, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                        } else {
                            setText(item);
                            switch (item) {
                                /*
                                 * c.f. https://material.google.com/style/color.html#color- color-palette for colours
                                 */
                                case "true":
                                    setStyle("-fx-background-color: #FFF59D;-fx-alignment: CENTER;");// yellow
                                    break;
                                default:
                                    setStyle("-fx-alignment: CENTER;");
                                    break;
                            }
                        }
                    }
                };
            });

            status.setEditable(false);
            status.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            status.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isValidOutrightsFixtureStatus(t.getNewValue())) {
                    o.setStatus(t.getNewValue());
                    o.setFixtureUpdate("Yes");
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_STATUS, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

                fixturesTable.refresh();
            });

            goalsHome.setEditable(true);
            goalsHome.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            goalsHome.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setGoalsHome(t.getNewValue());
                    o.setFixtureUpdate("Yes");
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

                fixturesTable.refresh();
            });

            score.setEditable(true);
            score.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            score.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (t.getNewValue().equals("-")) {
                    o.setGoalsHome(0);
                    o.setGoalsAway(0);
                    o.setScore("-");
                    o.setFixtureUpdate("Yes");
                    o.setStatus(OutrightsFixtureStatus.PRE_MATCH.toString());
                    handleFixtureRowSelect(o);
                } else if (isValidScore(t.getNewValue())) {
                    String[] scores = t.getNewValue().split("-");
                    o.setGoalsHome(scores[0]);
                    o.setGoalsAway(scores[1]);
                    o.setScore(t.getNewValue());
                    o.setFixtureUpdate("Yes");
                    o.setStatus(OutrightsFixtureStatus.COMPLETED.toString());
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_SAME_SCORE, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

                fixturesTable.refresh();
            });

            goalsAway.setEditable(true);
            goalsAway.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            goalsAway.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setGoalsAway(t.getNewValue());
                    o.setFixtureUpdate("Yes");
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

                fixturesTable.refresh();
            });

        }
        /*
         * set up standing
         */
        if (observableStandings != null)

        {
            standingsTable.setItems(observableStandings.getData());
            observableStandings.getData().forEach(o -> teamsIDPlayed.put(o.getTeamID(), (double) o.getPlayed()));
            teamId.setCellValueFactory(cellData -> new SimpleStringProperty(
                            teamsIDName.get(cellData.getValue().teamIDProperty().getValue())));
            played.setCellValueFactory(cellData -> cellData.getValue().playedProperty());
            won.setCellValueFactory(cellData -> cellData.getValue().wonProperty());
            drawn.setCellValueFactory(cellData -> cellData.getValue().drawnProperty());
            lost.setCellValueFactory(cellData -> cellData.getValue().lostProperty());
            goalsFor.setCellValueFactory(cellData -> cellData.getValue().goalsForProperty());
            goalsAgainst.setCellValueFactory(cellData -> cellData.getValue().goalsAgainstProperty());
            goalDiff.setCellValueFactory(cellData -> cellData.getValue().goalDiffProperty());
            points.setCellValueFactory(cellData -> cellData.getValue().pointsProperty());
            hpp.setCellValueFactory(cellData -> cellData.getValue().highestPossibleFinishPosnProperty());
            lpp.setCellValueFactory(cellData -> cellData.getValue().lowestPossibleFinishPosnProperty());
            mpa.setCellValueFactory(cellData -> cellData.getValue().manualPointsAdjProperty());
            mtba.setCellValueFactory(cellData -> cellData.getValue().manualtieBreakAdjProperty());
            upStanding.setCellValueFactory(celData -> celData.getValue().standingUpdateProperty());

            standingsTable.setRowFactory(tv -> {
                TableRow<ObservableStanding> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    ObservableStanding rowData = row.getItem();
                    if (!row.isEmpty())
                        if (event.getButton() == MouseButton.PRIMARY)
                            updateText.setText(rowData.toString());
                });
                return row;
            });
            // outrightGuiService.publishInfo("edit standing tale? " + editStanding);
            standingsTable.setEditable(true);
            mpa.setEditable(true);
            mpa.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            mpa.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setManualPointsAdj(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });
            mtba.setEditable(true);
            mtba.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            mtba.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setManualtieBreakAdj(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });
            // won.setEditable(editStanding);
            // played.setEditable(editStanding);
            // played.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // won.setEditable(editStanding);
            // won.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // drawn.setEditable(editStanding);
            // drawn.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // goalsFor.setEditable(editStanding);
            // goalsFor.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // goalsAgainst.setEditable(editStanding);
            // goalsAgainst.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // points.setEditable(editStanding);
            // points.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            // played.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setPlayed(t.getNewValue());
            // o.setStandingUpdate("Yes");
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            // });
            //
            // drawn.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setDrawn(t.getNewValue());
            // o.setStandingUpdate("Yes");
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            // });
            //
            // goalsFor.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setGoalsFor(t.getNewValue());
            // o.setStandingUpdate("Yes");
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            // });
            //
            // goalsAgainst.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setGoalsAgainst(t.getNewValue());
            // o.setStandingUpdate("Yes");
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            // });
            //
            // points.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setPoints(t.getNewValue());
            // o.setStandingUpdate("Yes");
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            //
            // });
            //
            // won.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
            // ObservableStanding o =
            // ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            // if (isInteger(t.getNewValue())) {
            // o.setWon(t.getNewValue());
            // handleStandingRowSelect(o);
            // } else {
            // String errMsg = String.format(DEFAULT_ERROR_VALID_INT, t.getNewValue());
            // displayErrorMsg(errMsg);
            // }
            // standingsTable.refresh();
            // });
        }

        /*
         * set up standing
         */
        if (observableFStandings != null)

        {
            fstandingsTable.setItems(observableFStandings.getData());
            // List<TargetPointsEntry> entries = new ArrayList<>();
            // observableFStandings.getData()
            // .forEach(o -> entries.add(new TargetPointsEntry(o.getTeamID(), o.getTargetpoints())));
            // tPointsList.setEntries(entries);
            tPointsList.setEventID(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
            fteamId.setCellValueFactory(cellData -> new SimpleStringProperty(
                            teamsIDName.get(cellData.getValue().teamIDProperty().getValue())));
            fplayed.setCellValueFactory(cellData -> cellData.getValue().playedProperty());
            fwon.setCellValueFactory(cellData -> cellData.getValue().wonProperty());
            fdrawn.setCellValueFactory(cellData -> cellData.getValue().drawnProperty());
            flost.setCellValueFactory(cellData -> cellData.getValue().lostProperty());
            fgoalsFor.setCellValueFactory(cellData -> cellData.getValue().goalsForProperty());
            fgoalsAgainst.setCellValueFactory(cellData -> cellData.getValue().goalsAgainstProperty());
            fgoalDiff.setCellValueFactory(cellData -> cellData.getValue().goalDiffProperty());
            fpoints.setCellValueFactory(cellData -> cellData.getValue().pointsProperty());
            fupStanding.setCellValueFactory(celData -> celData.getValue().standingUpdateProperty());
            ftargetpoints.setCellValueFactory(cellData -> cellData.getValue().targetPointsProperty());
            fstandingsTable.setRowFactory(tv -> {
                TableRow<ObservableFcastStanding> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    ObservableFcastStanding rowData = row.getItem();
                    if (!row.isEmpty())
                        if (event.getButton() == MouseButton.PRIMARY)
                            updateText.setText(rowData.toString());
                });
                return row;
            });
            fstandingsTable.setEditable(true);
            ftargetpoints.setEditable(true);

            ftargetpoints.setCellFactory(TextFieldTableCell.<ObservableFcastStanding>forTableColumn());
            ftargetpoints.setOnEditCommit((CellEditEvent<ObservableFcastStanding, String> t) -> {
                ObservableFcastStanding o = ((ObservableFcastStanding) t.getTableView().getItems()
                                .get(t.getTablePosition().getRow()));


                double point = o.getpoints();
                String teamIDFCast = o.getTeamID();
                double n = o.getPlayed() - teamsIDPlayed.get(teamIDFCast);
                if (isDouble(t.getNewValue())) {
                    double newValue = Double.valueOf(t.getNewValue());
                    String newString = dfTwoDecimal.format(newValue);
                    String error = FcastStanding.targetPointsOk(n, point, newValue);
                    if (null == error) {
                        List<TargetPointsEntry> currentEntries = tPointsList.getEntries();
                        currentEntries.add(new TargetPointsEntry(teamIDFCast, newValue));
                        o.setTargetPoints(newString);
                        o.setStandingUpdate("Yes");
                        handleFcastStandingRowSelect(o);
                        fstandingsTable.refresh();
                    } else {
                        displayErrorMsg(error);
                        o.setTargetPoints(t.getOldValue());
                    }
                } else {
                    String error = String.format(DEFAULT_ERROR_VALID_NUM, t.getNewValue());
                    displayErrorMsg(error);
                    o.setTargetPoints(t.getOldValue());
                }

            });
        }

        /*
         * set up teams
         */

        if (observableTeams != null) {
            teamsTableRatings.setItems(observableTeams.getData());
            teamsNameTable.setItems(observableTeams.getData());
            teamsTableRatings.setRowFactory(tv -> {
                TableRow<ObservableTeam> row = new TableRow<>();

                row.setOnMouseClicked(event -> {
                    ObservableTeam rowData = row.getItem();
                    if (!row.isEmpty())
                        if (event.getButton() == MouseButton.PRIMARY)
                            updateText.setText(rowData.toString());
                });
                return row;
            });

            teamID.setCellValueFactory(cellData -> cellData.getValue().teamIDProperty());
            displayName.setCellValueFactory(cellData -> cellData.getValue().displayNameProperty());
            ratingAttack.setCellValueFactory(cellData -> cellData.getValue().ratingAttackProperty());
            ratingDefense.setCellValueFactory(cellData -> cellData.getValue().ratingDefenseProperty());
            ratingsBiasAttack.setCellValueFactory(cellData -> cellData.getValue().ratingsBiasAttackProperty());
            ratingsBiasDefense.setCellValueFactory(cellData -> cellData.getValue().ratingsBiasDefenseProperty());
            upTeam.setCellValueFactory(cellData -> cellData.getValue().teamUpdatedProperty());
            upTeamName.setCellValueFactory(cellData -> cellData.getValue().teamUpdatedProperty());
            displayName1.setCellValueFactory(cellData -> cellData.getValue().displayNameProperty());
            fiveThirtyEightName.setCellValueFactory(cellData -> cellData.getValue().fiveThirtyEightNameProperty());
            sportingIndexName.setCellValueFactory(cellData -> cellData.getValue().sportingIndexNameProperty());
            lsportsName.setCellValueFactory(cellData -> cellData.getValue().lsportsNameProperty());

            teamsNameTable.setEditable(true);
            teamsTableRatings.setEditable(true);
            fiveThirtyEightName.setEditable(true);
            fiveThirtyEightName.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            sportingIndexName.setEditable(true);
            sportingIndexName.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            lsportsName.setEditable(true);
            lsportsName.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());

            ratingAttack.setEditable(false);
            ratingAttack.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            ratingDefense.setEditable(false);
            ratingDefense.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            ratingsBiasAttack.setEditable(true);
            ratingsBiasAttack.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            ratingsBiasDefense.setEditable(true);
            ratingsBiasDefense.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            fiveThirtyEightName.setOnEditCommit(new EventHandler<CellEditEvent<ObservableTeam, String>>() {

                public void handle(CellEditEvent<ObservableTeam, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableTeam o = (ObservableTeam) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setTeamUpdated("Yes");
                        o.setFiveThirtyEightName(t.getNewValue());
                        handleTeamRowSelect(o);
                    }
                    teamsNameTable.refresh();
                }
            });

            sportingIndexName.setOnEditCommit(new EventHandler<CellEditEvent<ObservableTeam, String>>() {

                public void handle(CellEditEvent<ObservableTeam, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableTeam o = (ObservableTeam) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setTeamUpdated("Yes");
                        o.setSportingIndexName(t.getNewValue());
                        handleTeamRowSelect(o);
                    }
                    teamsNameTable.refresh();
                }
            });

            lsportsName.setOnEditCommit(new EventHandler<CellEditEvent<ObservableTeam, String>>() {

                public void handle(CellEditEvent<ObservableTeam, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableTeam o = (ObservableTeam) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setTeamUpdated("Yes");
                        o.setLsportsName(t.getNewValue());
                        handleTeamRowSelect(o);
                    }
                    teamsNameTable.refresh();
                }
            });

            ratingAttack.setOnEditCommit((

                            CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        if (Double.parseDouble(t.getNewValue()) > 0 && Double.parseDouble(t.getNewValue()) < 5.0) {
                            o.setRatingAttack(dfThreeDecimal.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTableRatings.refresh();
                        } else {
                            String errMsg = String.format(DEFAULT_ERROR_SAME_RATE, t.getNewValue());
                            displayErrorMsg(errMsg);
                        }

                    } else {
                        outrightGuiService.publishInfo(DEFAULT_ERROR_SAME_VALUE);
                    }

                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_NUM, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

            ratingsBiasAttack.setOnEditCommit((CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        String error = Team.biasAttackOk(Double.valueOf(o.getRatingAttack().getValue()),
                                        Double.valueOf(t.getNewValue()));

                        if (null == error) {
                            o.setRatingsBiasAttack(dfTwoDecimal.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTableRatings.refresh();
                        } else {
                            displayErrorMsg(error);
                        }

                    } else {
                        outrightGuiService.publishInfo(DEFAULT_ERROR_SAME_VALUE);
                    }

                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_NUM, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

            ratingsBiasDefense.setOnEditCommit((CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        String error = Team.biasDefenseOk(Double.valueOf(o.getRatingDefense().getValue()),
                                        Double.valueOf(t.getNewValue()));

                        if (null == error) {
                            o.setRatingsBiasDefense(dfTwoDecimal.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTableRatings.refresh();
                        } else {
                            displayErrorMsg(error);
                        }

                    } else {
                        outrightGuiService.publishInfo(DEFAULT_ERROR_SAME_VALUE);
                    }

                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_NUM, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

            ratingDefense.setOnEditCommit((CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        if (Double.parseDouble(t.getNewValue()) > 0 && Double.parseDouble(t.getNewValue()) < 5.0) {
                            o.setRatingDefense(dfThreeDecimal.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTableRatings.refresh();
                        } else {
                            String errMsg = String.format(DEFAULT_ERROR_SAME_RATE, t.getNewValue());
                            displayErrorMsg(errMsg);
                        }

                    } else {
                        outrightGuiService.publishInfo(DEFAULT_ERROR_SAME_VALUE);
                    }

                } else {
                    String errMsg = String.format(DEFAULT_ERROR_VALID_NUM, t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

        }



    }

    /*
     * Not used methods
     */
    public void highLightUpdateRow() {
        int i = 0;
        System.out.println("innnn");
        for (Node n : teamsTableRatings.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                @SuppressWarnings("rawtypes")
                TableRow row = (TableRow) n;
                System.out.println("innnn1111" + row.getText() + "-----"
                                + teamsTableRatings.getItems().get(i).getTeamID().getValue());
                if (updatedTeam.containsKey(teamsTableRatings.getItems().get(i).getTeamID().getValue())) {
                    row.setStyle("-fx-alignment: CENTER;-fx-text-fill: red");
                    n.setStyle("-fx-alignment: CENTER;-fx-text-fill: red");
                    BooleanBinding contains = Bindings.createBooleanBinding(() -> {
                        if (row.getItem() != null) {
                            return true;
                        }

                        return false;
                    }, row.itemProperty(), numbersChanged);
                    row.styleProperty().bind(Bindings.when(contains).then("-fx-background-color: blue;")
                                    .otherwise("-fx-background-color: red;"));
                    n.styleProperty().bind(Bindings.when(contains).then("-fx-background-color: blue;")
                                    .otherwise("-fx-background-color: red;"));
                    System.out.println("innnn2222222222222" + row.getStyle() + "---" + n.getStyle());
                } else {
                    row.setStyle("-fx-alignment: CENTER;-fx-text-fill: purple");
                    n.setStyle("-fx-alignment: CENTER;-fx-text-fill: red");
                }
                i++;
                if (i == teamsTableRatings.getItems().size())
                    break;
            }
        }

    }

    /*
     * Not used methods
     */
    public TableCell<?, ?> getPoint(TableView<?> tableView, int columnIndex, int rowIndex) {
        Set<Node> tableRowCell = tableView.lookupAll(".table-row-cell");
        TableRow<?> row = null;
        for (Node tableRow : tableRowCell) {
            TableRow<?> r = (TableRow<?>) tableRow;
            if (r.getIndex() == rowIndex) {
                row = r;
                break;
            }
        }
        Set<Node> cells = row.lookupAll(".table-cell");
        for (Node node : cells) {
            TableCell<?, ?> cell = (TableCell<?, ?>) node;
            if (tableView.getColumns().indexOf(cell.getTableColumn()) == columnIndex) {
                return cell;
            }
        }
        return null;
    }

    private void displayErrorMsg(String errMsg) {
        AlertBox.displayMsg(errMsg);
    }

    private static boolean isDouble(String s) {
        Double double1 = Doubles.tryParse(s);
        if (double1 == null)
            return false;
        else
            return true;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isValidScore(String s) {
        try {
            String[] scores = s.split("-");
            if (scores.length != 2)
                return false;
            else {
                Integer.parseInt(scores[0]);
                Integer.parseInt(scores[1]);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    private static boolean isValidOutrightsFixtureStatus(String s) {
        if (s.toUpperCase().equals(OutrightsFixtureStatus.PRE_MATCH.toString())) {
            return true;
        }
        if (s.toUpperCase().equals(OutrightsFixtureStatus.IN_PLAY.toString())) {
            return true;
        }
        if (s.toUpperCase().equals(OutrightsFixtureStatus.COMPLETED.toString())) {
            return true;
        }
        return false;
    }

    private void handleTeamRowSelect(ObservableTeam rowData) {

        updateText.setText(rowData.toString());
        String teamKey = params.get(DEFAULT_EVENT_ID_KEY) + rowData.getTeam().getTeamID();
        if (updateTeam.containsKey(teamKey)) {
            updateTeam.replace(teamKey, updateTeam.get(teamKey), rowData.getTeam());
        } else {
            updateTeam.put(teamKey, rowData.getTeam());
        }
        updatedTeam.put(rowData.getTeam().getTeamID(), true);
        outrightGuiService.publishInfo("update team ID are : " + updatedTeam.keySet().toString());
    }

    private void handleCompetitionRowSelect(ObservableCompetition rowData) {

        updateText.setText(rowData.toString());
        updateCompetition.put(params.get(DEFAULT_EVENT_ID_KEY), rowData.getCompetitionsListEntry());
        outrightGuiService.publishInfo("update competition ID are : " + updateCompetition.keySet().toString());
    }

    private void handleFixtureRowSelect(ObservableFixture rowData) {

        updateText.setText(rowData.toString());
        String fixtureKey = params.get(DEFAULT_EVENT_ID_KEY) + rowData.getFixtureID();
        if (fixturesListEntry.containsKey(fixtureKey)) {
            fixturesListEntry.replace(fixtureKey, fixturesListEntry.get(fixtureKey), rowData.getFixturesListEntry());
        } else {
            if (rowData.getFixtureUpdate().equals("Yes"))
                fixturesListEntry.put(fixtureKey, rowData.getFixturesListEntry());
        }
        outrightGuiService.publishInfo("update Fixture ID are : " + fixturesListEntry.keySet().toString());

    }

    private void handleStandingRowSelect(ObservableStanding rowData) {

        updateText.setText(rowData.toString());
        String standingKey = params.get(DEFAULT_EVENT_ID_KEY) + rowData.getStanding().getTeamId();
        if (updatedStanding.containsKey(standingKey)) {
            updatedStanding.replace(standingKey, updatedStanding.get(standingKey), rowData.getStandingListEntry());
        } else {
            updatedStanding.put(standingKey, rowData.getStandingListEntry());
        }
    }

    private void handleFcastStandingRowSelect(ObservableFcastStanding rowData) {
        updateText.setText(rowData.toString());
    }

    @FXML
    private void onUpdateButtonPressed() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        if (updateTeam.size() == 0 && fixturesListEntry.size() == 0 && tPointsList.getEntries().size() == 0
                        && updateCompetition.size() == 0 && updatedStanding.size() == 0) {
            updateText.setText("Nothing to update ...");
            logText.setText("Nothing to update ...");
            outrightGuiService.publishInfo("Nothing to update ...");
            return;
        }

        if (updateTeam.size() > 0) {
            TeamObject teamObject = new TeamObject();
            teamObject.setEventId(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
            teamObject.setCompetitionName(updateCompetitionName);
            for (Map.Entry<String, Team> entry : updateTeam.entrySet()) {
                teamObject.setTeam(entry.getValue());
                String requestBody = JsonUtil.marshalJson(teamObject);
                logText.setText("Updating ratings bias ...");
                executor.submit(() -> {
                    PostRequest(requestBody, DEFAULT_PATH_TEAMS);
                });
                outrightGuiService.publishUpdate(requestBody);
            }
        }

        if (fixturesListEntry.size() > 0) {
            FixturesList list = new FixturesList();
            list.setEventID(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
            List<FixturesListEntry> fixturesList = new ArrayList<>();
            list.setFixturesList(fixturesList);
            logText.setText("Updating fixtures ...");
            for (FixturesListEntry fixturesListEntry : fixturesListEntry.values()) {
                list.getFixturesList().add(fixturesListEntry);
                String requestBody = JsonUtil.marshalJson(list);
                executor.submit(() -> {
                    PostRequest(requestBody, DEFAULT_PATH_FIXTURES);
                });
                outrightGuiService.publishUpdate(requestBody);
            }
        }

        if (updateCompetition.size() > 0) {

            CompetitionsList competitionsList = new CompetitionsList();
            CompetitionsListEntry competitionsListEntry = updateCompetition.get(params.get(DEFAULT_EVENT_ID_KEY));
            competitionsList.getCompetitionsList().add(competitionsListEntry);
            String requestBody = JsonUtil.marshalJson(competitionsList);
            logText.setText("Updating competitions IDs ...");
            executor.submit(() -> {
                PostRequest(requestBody, DEFAULT_PATH_COMPETITIONS);
            });
            outrightGuiService.publishUpdate(requestBody);
        }

        if (updatedStanding.size() > 0) {

            StandingsList list = new StandingsList();
            list.setEventID(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
            List<StandingsListEntry> standingsList = new ArrayList<>();
            list.setStandingsList(standingsList);
            logText.setText("Updating Standings ...");
            for (StandingsListEntry standingsListEntry : updatedStanding.values()) {
                list.getStandingsList().add(standingsListEntry);
                String requestBody = JsonUtil.marshalJson(list);
                executor.submit(() -> {
                    PostRequest(requestBody, DEFAULT_PATH_STANDINGS);
                });
                outrightGuiService.publishUpdate(requestBody);
            }
        }

        if (tPointsList.getEntries().size() > 0) {
            String requestBody = JsonUtil.marshalJson(tPointsList);
            logText.setText("Updating target points ...");
            executor.submit(() -> {
                PostRequest(requestBody, DEFAULT_PATH_TARGET_POINTS);
            });
            outrightGuiService.publishUpdate(requestBody);
        }
        executor.shutdown();
        updateTeam.clear();
        updatedTeam.clear();
        tPointsList.getEntries().clear();
        fixturesListEntry.clear();
        updateCompetition.clear();
        return;

    }

    @FXML
    private void onRefreshButtonPressed() throws Exception {
        outrightGuiService.refreshOutrightGui(params);
        suspendMarkets = outrightGuiService.isMarketsSuspending();
        updateText.setText("");
        logText.setText("");
    }

    @FXML
    private void onManageButtonPressed() throws Exception {
        teamsNameTable.setVisible(!teamsNameTable.isVisible());
        competitionTable.setVisible(!competitionTable.isVisible());
        competitionsTab.setDisable(!competitionsTab.isDisable());
        teamNameTab.setDisable(!teamNameTab.isDisable());
    }

    @FXML
    private void onScrapeButtonPressed() throws Exception {
        updateText.setText("Updating ratings ...");
        logText.setText("Updating ratings ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostRequest(null, DEFAULT_PATH_UPDATE_RATINGS);
            }
        }).start();
    }

    @FXML
    private void onUpdateATSButtonPressed() {
        updateText.setText("Updating Fixture event Ids ...");
        logText.setText("Updating Fixture event Ids ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostRequest(null, DEFAULT_PATH_UPDATE_ATS);
            }
        }).start();
    }

    @FXML
    private void onDisplayButtonPressed() throws Exception {

        updateText.setVisible(!updateText.isVisible());
        if (updateText.isVisible())
            debug.setText("Debug on");
        else
            debug.setText("Debug off");
    }

    @FXML
    public void onComboboxUpdated() throws Exception {
        params.clear();
        String selectedEventID = competitionCombo.getSelectionModel().getSelectedItem().toString();
        String selectedEventID2 =
                        selectedEventID.substring(selectedEventID.indexOf("(") + 1, selectedEventID.indexOf(")"));
        params.put(DEFAULT_EVENT_ID_KEY, selectedEventID2);
        updateCompetitionName = selectedEventID.substring(0, selectedEventID.indexOf(":"));
        updatedTeam.clear();
        onRefreshButtonPressed();
    }

    public void setTeamIDName(Map<String, String> teamsIDName2) {
        teamsIDName.clear();
        this.teamsIDName = new HashMap<String, String>(teamsIDName2);
    }

    public boolean isUseColor() {
        return useColor;
    }

    public void setUseColor(boolean useColor) {
        this.useColor = useColor;
    }

    public boolean isEditStanding() {
        return editStanding;
    }

    public void setEditStanding(boolean editStanding) {
        this.editStanding = editStanding;
    }

    public boolean isSuspendMarkets() {
        return suspendMarkets;
    }

    public void setSuspendMarkets(boolean suspendMarkets) {
        this.suspendMarkets = suspendMarkets;
    }

    @FXML
    private void onUpdateColorButtonPressed() throws Exception {
        setUseColor(!this.isUseColor());
        manageDefaultCssStyle();
        teamsTableRatings.refresh();
        teamsNameTable.refresh();
        standingsTable.refresh();
        fstandingsTable.refresh();
        fixturesTable.refresh();
        alertsTable.refresh();
        marketsTable.refresh();
        resultedMarketsTable.refresh();
    }

    @FXML
    private void onUpdateStandingButtonPressed() throws Exception {
        this.setEditStanding(!this.isEditStanding());
        System.out.println(isEditStanding());
        standingsTable.setEditable(editStanding);
        played.setEditable(editStanding);
        won.setEditable(editStanding);
        drawn.setEditable(editStanding);
        lost.setEditable(editStanding);
        goalsFor.setEditable(editStanding);
        goalsAgainst.setEditable(editStanding);
        goalDiff.setEditable(editStanding);
        points.setEditable(editStanding);

        manageDefaultCssStyle();
        teamsTableRatings.refresh();
        teamsNameTable.refresh();
        standingsTable.refresh();
        fstandingsTable.refresh();
        fixturesTable.refresh();
    }

    @FXML
    private void onScrapeTPButtonPressed() throws Exception {
        logText.setText("Updating target points ...");
        updateText.setText("Updating target points ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostRequest(null, DEFAULT_PATH_UPDATE_TARGET_POINTS);
            }
        }).start();
    }

    @FXML
    private void onClearTPButtonPressed() throws Exception {
        logText.setText("Clearing target points ...");
        updateText.setText("Clearing target points ...");
        TargetPointsList clearTPointsList = new TargetPointsList();
        List<TargetPointsEntry> currentEntries = clearTPointsList.getEntries();
        clearTPointsList.setEventID(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
        for (String teamIDFCast : teamsIDName.keySet()) {
            currentEntries.add(new TargetPointsEntry(teamIDFCast, 0.0));
        }
        String requestBody = JsonUtil.marshalJson(clearTPointsList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostRequest(requestBody, DEFAULT_PATH_TARGET_POINTS);
            }
        }).start();
    }

    @FXML
    private void onParamFindButtonPressed() throws Exception {
        logText.setText("Param finding ...");
        updateText.setText("Param finding ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostRequest(null, DEFAULT_PATH_PARAM_FIND);
            }
        }).start();

    }

    @FXML
    public void onEventEntry() {
        String filter = marketFilter.getText();
        outrightGuiService.refreshOutrightMarket(params, filter);
    }

    @FXML
    private void onSuspendMarketButtonPressed() throws Exception {
        suspendMarkets = !suspendMarkets;
        String suspendMarketInfo = "Suspending markets ...";
        String unsuspendMarketInfo = "Unsuspending markets ...";
        if (suspendMarkets) {
            logText.setText(suspendMarketInfo);
            updateText.setText(suspendMarketInfo);
            marketStatusOn.setText("Unsuspend markets");
        } else {
            logText.setText(unsuspendMarketInfo);
            updateText.setText(unsuspendMarketInfo);
            marketStatusOn.setText("Suspend markets");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (suspendMarkets)
                    PostRequest(null, DEFAULT_PATH_SUSPEND_MARKETS);
                else
                    PostRequest(null, DEFAULT_PATH_UNSUSPEND_MARKETS);
            }
        }).start();

    }

    private synchronized void PostRequest(String requestBody, String path) {
        if (null == requestBody) {
            EventID eventID = new EventID(Long.valueOf(params.get(DEFAULT_EVENT_ID_KEY)));
            requestBody = JsonUtil.marshalJson(eventID);
        }
        String httpResponse = outrightGateway.postHttpResponse(requestBody, path, null);
        if (!httpResponse.toUpperCase().contains("ERROR")) {
            PostReply postReply = JsonUtil.unmarshalJson(httpResponse, PostReply.class);

            updateText.setText("Response is " + httpResponse);
            logText.setText(postReply.getFurtherInformation());
        } else {
            httpResponse = httpResponse.replaceAll("\"", "");
            // System.out.println(httpResponse);
            Map<String, String> result = Arrays.stream(httpResponse.split(",")).map(s -> s.split(":"))
                            .collect(Collectors.toMap(a -> a[0], // key
                                            a -> a[1] // value
                            ));
            logText.setText("Update error " + result.get("error"));
            updateText.setText("Response is " + httpResponse);
        }
        outrightGuiService.refreshOutrightGui(params);
        suspendMarkets = outrightGuiService.isMarketsSuspending();
    }

}
