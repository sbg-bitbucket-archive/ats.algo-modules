package ats.algo.outrights.view;

import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Optional;
import javafx.scene.Node;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.Help.Column;
import org.boon.core.Sys;
import org.w3c.dom.css.ViewCSS;

import com.google.common.primitives.Doubles;

import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.matchrunner.view.AlertBox;
import ats.algo.outrights.OutrightGateway;
import ats.algo.outrights.OutrightGuiService;
import ats.algo.outrights.model.ObservableCompetitions;
import ats.algo.outrights.model.ObservableFixtures;
import ats.algo.outrights.model.ObservableMarkets;
import ats.algo.outrights.model.ObservableStandings;
import ats.algo.outrights.model.ObservableTeams;
import ats.algo.outrights.model.ObservableCompetitions.ObservableCompetition;
import ats.algo.outrights.model.ObservableFixtures.ObservableFixture;
import ats.algo.outrights.model.ObservableMarkets.ObservableMarket;
import ats.algo.outrights.model.ObservableStandings.ObservableStanding;
import ats.algo.outrights.model.ObservableTeams.ObservableTeam;
import ats.algo.outrights.model.ObservableWarnings;
import ats.algo.outrights.model.ObservableWarnings.ObservableWarning;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureStatus;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Team;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.FixturesListEntry;
import ats.algo.sport.outrights.server.api.TeamObject;
import ats.core.util.json.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ContentDisplay;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;

@SuppressWarnings({"restriction", "unused"})
public class OutrightMatchOverviewController {

    private OutrightGuiService outrightGuiService;

    @FXML
    private TableView<ObservableTeam> teamsTable;
    @FXML
    private TableColumn<ObservableTeam, String> teamID;
    @FXML
    private TableColumn<ObservableTeam, String> displayName;
    @FXML
    private TableColumn<ObservableTeam, String> fiveThirtyEightName;
    @FXML
    private TableColumn<ObservableTeam, String> ratingAttack;
    @FXML
    private TableColumn<ObservableTeam, String> ratingDefense;
    @FXML
    private TableColumn<ObservableTeam, String> upTeam;

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
    private TableColumn<ObservableStanding, String> goalsFor;
    @FXML
    private TableColumn<ObservableStanding, String> goalsAgainst;
    @FXML
    private TableColumn<ObservableStanding, String> points;
    @FXML
    private TableColumn<ObservableStanding, String> upStanding;


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
    private TableView<ObservableWarning> warningsTable;
    @FXML
    private TableColumn<ObservableWarning, String> eventIDWarning;
    @FXML
    private TableColumn<ObservableWarning, String> competetionNameWarning;
    @FXML
    private TableColumn<ObservableWarning, String> stateOk;
    @FXML
    private TableColumn<ObservableWarning, String> warningMessages;

    @FXML
    private TextArea updateText;

    @FXML
    private Tab tabWarnings;

    @SuppressWarnings("rawtypes")
    @FXML
    private ComboBox competitionCombo = new ComboBox<>();

    @FXML
    private ObservableList<String> options;

    private Map<String, Team> updateTeam = new HashMap<>();
    private ObservableList<Boolean> upTeamList = FXCollections.observableArrayList();
    private Map<String, Boolean> updatedTeam = new HashMap<>();

    private Map<String, FixturesListEntry> fixturesListEntry = new HashMap<>();

    private Map<String, Standing> standing = new HashMap<>();

    private Map<String, String> params = new HashMap<>();

    private Map<String, String> eventCompetition = new HashMap<>();

    private Map<String, String> teamsIDName = new HashMap<>();

    private String updateCompetitionName = "";

    final List<String> colors = Arrays.asList("white", "blue", "green", "red", "violet", "yellow", "black");

    private final String paleYellowTextColor = "rgb(255, 255, 244, 0.99)";
    private final String lightBlueTextColor = "rgb(136, 206, 250, 0.99)";
    private final String lightGreenTextColor = "rgb(144, 238, 144, 0.99)";
    private final String appleGreenTextColor = "rgb(204, 255, 204, 0.99)";
    private final String skyBlueTextColor = "rgb(220, 246, 241, 0.99)";
    private final String greyTextColor = "rgb(235, 235, 228, 0.99)";
    private final String yellowTextColor = "rgb(250, 249, 222, 0.99)";
    private final String darkGreenTextColor = "rgb(0, 100, 0, 0.99)";
    private final String balckTextColor = "black";

    private final String defaultFillinTextColor = balckTextColor;

    private String paleYellowBackGround = "rgb(255, 255, 244, 0.99)";
    private String lightBlue1BackGround = "rgb(136, 206, 250, 0.99)";
    private String lightGreenBackGround = "rgb(144, 238, 144, 0.99)";


    private boolean useColor = true;

    private boolean editStanding = false;
    private final DecimalFormat df = new DecimalFormat("#0.000");
    private final BooleanProperty numbersChanged = new SimpleBooleanProperty(false);

    @FXML
    Button update;

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
        updateText.setEditable(false);
        updateText.setWrapText(true);
        updateText.setFont(Font.font("Verdana", 20));

    }

    @SuppressWarnings("unchecked")
    public void initializeMatchOverviewController(ObservableCompetitions observableCompetitions,
                    ObservableTeams observableTeams, ObservableStandings observableStandings,
                    ObservableFixtures observableFixtures, ObservableWarnings observableWarnings) throws Exception {

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
        competitionCombo.getSelectionModel().selectFirst();
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
            params.put("eventID", selectedEventID2);
            updateCompetitionName = selectedEventID.substring(0, selectedEventID.indexOf(":"));
        }

        updateMatchOverviewController(observableCompetitions, observableTeams, observableStandings, observableFixtures,
                        observableWarnings);
        manageDefaultCssStyle();

    }

    private void manageDefaultCssStyle() {
        /*
         * css color style code
         */
        String chosedTextColor = darkGreenTextColor;
        if (useColor) {
            chosedTextColor = darkGreenTextColor;
        } else {
            chosedTextColor = lightBlueTextColor;

        }
        outrightGuiService.publishInfo("defaule color not editable is Black");
        if (!chosedTextColor.equals(darkGreenTextColor))
            outrightGuiService.publishInfo("defaule color editable is BlueTextColor");
        else
            outrightGuiService.publishInfo("defaule color editable is DarkGreen");

        eventID.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        goalsHome.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        goalsAway.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        status.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        teamID.setStyle("-fx-alignment: CENTER;");
        displayName.setStyle("-fx-alignment: CENTER;");
        stateOk.setStyle("-fx-alignment: CENTER;");
        date.setStyle("-fx-alignment: CENTER;");
        homeTeamID.setStyle("-fx-alignment: CENTER;");
        awayTeamID.setStyle("-fx-alignment: CENTER;");
        tag.setStyle("-fx-alignment: CENTER;");
        playedAtNeutralGround.setStyle("-fx-alignment: CENTER;");
        fixtureType.setStyle("-fx-alignment: CENTER;");
        fixtureID.setStyle("-fx-alignment: CENTER;");
        pHome.setStyle("-fx-alignment: CENTER;");
        pAway.setStyle("-fx-alignment: CENTER;");
        pDraw.setStyle("-fx-alignment: CENTER;");

        ratingDefense.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        ratingAttack.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        fiveThirtyEightName.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
        upTeam.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        upFixture.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        upStanding.setStyle("-fx-alignment: CENTER; -fx-text-fill: red;");
        teamId.setStyle("-fx-alignment: CENTER;");

        if (editStanding) {
            won.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            played.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            drawn.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            goalsFor.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            goalsAgainst.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            points.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + chosedTextColor + ";");
            System.out.println("iiiiiiiii");
        } else {
            won.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            played.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            drawn.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsFor.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            goalsAgainst.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
            points.setStyle("-fx-alignment: CENTER;-fx-text-fill: " + defaultFillinTextColor + ";");
        }

    }

    /**
     * 
     * @throws Exception
     */
    public void updateMatchOverviewController(ObservableCompetitions observableCompetitions,
                    ObservableTeams observableTeams, ObservableStandings observableStandings,
                    ObservableFixtures observableFixtures, ObservableWarnings observableWarnings) throws Exception {
        /*
         * set up warnings
         */
        if (observableWarnings != null) {
            warningsTable.setItems(observableWarnings.getData());
            eventIDWarning.setCellValueFactory(cellData -> cellData.getValue().getEventIDWarnings());
            competetionNameWarning.setCellValueFactory(cellData -> new SimpleStringProperty(
                            eventCompetition.get(cellData.getValue().getEventIDWarnings().getValue())));
            boolean isStateOk = true;
            int i = 0;
            for (ObservableWarning warning : observableWarnings.getData()) {
                if (warning.stateOkProperty().getValue().equals("false"))
                    isStateOk = false;
            }
            if (!isStateOk)
                tabWarnings.setStyle("-fx-background-color: red;");
            else
                tabWarnings.setStyle("-fx-background-color: green;");
            stateOk.setCellValueFactory(cellData -> new SimpleStringProperty(
                            cellData.getValue().stateOkProperty().getValue().equals("true") ? "YES" : "NO"));
            warningMessages.setCellValueFactory(cellData -> cellData.getValue().warningMessagesProperty());

            stateOk.setCellFactory(column -> {
                return new TableCell<ObservableWarning, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                        } else {
                            setText(item);
                            switch (item) {
                                case "YES":
                                    setStyle("-fx-alignment: CENTER;-fx-background-color: #C5E1A5"); // green
                                    break;
                                case "NO":
                                    setStyle("-fx-alignment: CENTER;-fx-background-color: #F44336");// red
                                    break;
                                default:
                                    setStyle("");
                                    break;
                            }
                        }
                    }
                };
            });

            warningsTable.setRowFactory(tv -> {
                TableRow<ObservableWarning> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    ObservableWarning rowData = row.getItem();
                    if (!row.isEmpty())
                        if (event.getButton() == MouseButton.PRIMARY)
                            updateText.setText(rowData.toString());
                });
                return row;
            });
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
            tag.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
            playedAtNeutralGround.setCellValueFactory(cellData -> cellData.getValue().playedAtNeutralGroundProperty());
            status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
            fixtureType.setCellValueFactory(cellData -> cellData.getValue().fixtureTypeProperty());
            fixtureID.setCellValueFactory(cellData -> cellData.getValue().fixtureIDProperty());
            goalsAway.setCellValueFactory(cellData -> cellData.getValue().goalsAwayProperty());
            goalsHome.setCellValueFactory(cellData -> cellData.getValue().goalsHomeProperty());
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
                    String errMsg = String.format("%s is not a valid eventID", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                fixturesTable.refresh();
            });

            status.setEditable(true);
            status.setCellFactory(TextFieldTableCell.<ObservableFixture>forTableColumn());
            status.setOnEditCommit((CellEditEvent<ObservableFixture, String> t) -> {
                ObservableFixture o =
                                ((ObservableFixture) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isStatus(t.getNewValue())) {
                    o.setStatus(t.getNewValue());
                    o.setFixtureUpdate("Yes");
                    handleFixtureRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid status", t.getNewValue());
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
                    String errMsg = String.format("%s is not a valid int", t.getNewValue());
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
                    String errMsg = String.format("%s is not a valid int", t.getNewValue());
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
            teamId.setCellValueFactory(cellData -> new SimpleStringProperty(
                            teamsIDName.get(cellData.getValue().teamIDProperty().getValue())));
            played.setCellValueFactory(cellData -> cellData.getValue().playedProperty());
            won.setCellValueFactory(cellData -> cellData.getValue().wonProperty());
            drawn.setCellValueFactory(cellData -> cellData.getValue().drawnProperty());
            goalsFor.setCellValueFactory(cellData -> cellData.getValue().goalsForProperty());
            goalsAgainst.setCellValueFactory(cellData -> cellData.getValue().goalsAgainstProperty());
            points.setCellValueFactory(cellData -> cellData.getValue().pointsProperty());
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
            outrightGuiService.publishInfo("edit standing tale? " + editStanding);
            standingsTable.setEditable(editStanding);
            played.setEditable(editStanding);
            played.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            won.setEditable(editStanding);
            won.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            drawn.setEditable(editStanding);
            drawn.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            goalsFor.setEditable(editStanding);
            goalsFor.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            goalsAgainst.setEditable(editStanding);
            goalsAgainst.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            points.setEditable(editStanding);
            points.setCellFactory(TextFieldTableCell.<ObservableStanding>forTableColumn());
            played.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setPlayed(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });

            drawn.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setDrawn(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });

            goalsFor.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setGoalsFor(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });

            goalsAgainst.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setGoalsAgainst(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });

            points.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setPoints(t.getNewValue());
                    o.setStandingUpdate("Yes");
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();

            });

            won.setOnEditCommit((CellEditEvent<ObservableStanding, String> t) -> {
                ObservableStanding o =
                                ((ObservableStanding) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isInteger(t.getNewValue())) {
                    o.setWon(t.getNewValue());
                    handleStandingRowSelect(o);
                } else {
                    String errMsg = String.format("%s is not a valid Int", t.getNewValue());
                    displayErrorMsg(errMsg);
                }
                standingsTable.refresh();
            });
        }
        // /*
        // * set up markets
        // */
        //
        // marketsTable.setItems(observableMarkets.getData());
        // eventIdMarket.setCellValueFactory(cellData ->
        // cellData.getValue().eventIDProperty());
        // idMarket.setCellValueFactory(cellData -> cellData.getValue().iDProperty());
        // nameMarket.setCellValueFactory(cellData ->
        // cellData.getValue().nameProperty());
        // marketsTable.setVisible(false);

        /*
         * set up teams
         */

        if (observableTeams != null) {
            teamsTable.setItems(observableTeams.getData());
            teamsTable.setRowFactory(tv -> {
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
            fiveThirtyEightName.setCellValueFactory(cellData -> cellData.getValue().fiveThirtyEightNameProperty());
            ratingAttack.setCellValueFactory(cellData -> cellData.getValue().ratingAttackProperty());
            ratingDefense.setCellValueFactory(cellData -> cellData.getValue().ratingDefenseProperty());
            upTeam.setCellValueFactory(cellData -> cellData.getValue().teamUpdatedProperty());


            teamsTable.setEditable(true);
            fiveThirtyEightName.setEditable(true);
            fiveThirtyEightName.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            ratingAttack.setEditable(true);
            ratingAttack.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            ratingDefense.setEditable(true);
            ratingDefense.setCellFactory(TextFieldTableCell.<ObservableTeam>forTableColumn());
            fiveThirtyEightName.setOnEditCommit(new EventHandler<CellEditEvent<ObservableTeam, String>>() {
                public void handle(CellEditEvent<ObservableTeam, String> t) {
                    int row = t.getTablePosition().getRow();
                    ObservableTeam o = (ObservableTeam) t.getTableView().getItems().get(row);
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        o.setTeamUpdated("Yes");
                        o.setFiveThirtyEightName(t.getNewValue());
                        handleTeamRowSelect(o);
                    }
                    teamsTable.refresh();
                }
            });

            ratingAttack.setOnEditCommit((CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        if (Double.parseDouble(t.getNewValue()) > 0 && Double.parseDouble(t.getNewValue()) < 5.0) {
                            o.setRatingAttack(df.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTable.refresh();
                        } else {
                            String errMsg = String.format("%s is not a valid Rate that should be between (0,5)",
                                            t.getNewValue());
                            displayErrorMsg(errMsg);
                        }

                    } else {
                        outrightGuiService.publishInfo("---sameValue, do nothing");
                    }

                } else {
                    String errMsg = String.format("%s is not a valid number", t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

            ratingDefense.setOnEditCommit((CellEditEvent<ObservableTeam, String> t) -> {
                ObservableTeam o = ((ObservableTeam) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                if (isDouble(t.getNewValue())) {
                    if (Double.parseDouble(t.getOldValue()) != Double.parseDouble(t.getNewValue())) {
                        if (Double.parseDouble(t.getNewValue()) > 0 && Double.parseDouble(t.getNewValue()) < 5.0) {
                            o.setRatingDefense(df.format(Double.parseDouble(t.getNewValue())));
                            o.setTeamUpdated("Yes");
                            handleTeamRowSelect(o);
                            teamsTable.refresh();
                        } else {
                            String errMsg = String.format("%s is not a valid Rate that should be between (0,5)",
                                            t.getNewValue());
                            displayErrorMsg(errMsg);
                        }

                    } else {
                        outrightGuiService.publishInfo("---sameValue, do nothing");
                    }

                } else {
                    String errMsg = String.format("%s is not a valid number", t.getNewValue());
                    displayErrorMsg(errMsg);
                }

            });

        }



    }

    public void highLightUpdateRow() {
        int i = 0;
        System.out.println("innnn");
        for (Node n : teamsTable.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                @SuppressWarnings("rawtypes")
                TableRow row = (TableRow) n;
                System.out.println("innnn1111" + row.getText() + "-----"
                                + teamsTable.getItems().get(i).getTeamID().getValue());
                if (updatedTeam.containsKey(teamsTable.getItems().get(i).getTeamID().getValue())) {
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
                if (i == teamsTable.getItems().size())
                    break;
            }
        }

    }

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

    private Set<Node> getTableCells(TableView<ObservableTeam> tableView) {
        Set<Node> l = tableView.lookupAll("*");
        Set<Node> r = new HashSet<>();
        for (Node node : l) {
            if (node instanceof TableCell<?, ?>) {
                r.add(node);
            }
        }
        return r;
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

    private static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isStatus(String s) {
        if (s.toUpperCase().equals(FixtureStatus.PRE_MATCH.toString())) {
            return true;
        }
        if (s.toUpperCase().equals(FixtureStatus.IN_PLAY.toString())) {
            return true;
        }
        if (s.toUpperCase().equals(FixtureStatus.COMPLETED.toString())) {
            return true;
        }
        return false;
    }

    private static boolean isDate(String s) {
        return true;
    }

    private void handleTeamRowSelect(ObservableTeam rowData) {

        updateText.setText(rowData.toString());
        String teamKey = params.get("eventID") + rowData.getTeam().getTeamID();
        if (updateTeam.containsKey(teamKey)) {
            updateTeam.replace(teamKey, updateTeam.get(teamKey), rowData.getTeam());
        } else {
            updateTeam.put(teamKey, rowData.getTeam());
        }
        updatedTeam.put(rowData.getTeam().getTeamID(), true);
        outrightGuiService.publishInfo("update team ID are : " + updatedTeam.keySet().toString());
    }

    private void handleFixtureRowSelect(ObservableFixture rowData) {

        updateText.setText(rowData.toString());
        String fixtureKey = params.get("eventID") + rowData.getFixtureID();
        if (fixturesListEntry.containsKey(fixtureKey)) {
            fixturesListEntry.replace(fixtureKey, fixturesListEntry.get(fixtureKey), rowData.getFixturesListEntry());
        } else {
            fixturesListEntry.put(fixtureKey, rowData.getFixturesListEntry());
        }

    }

    private void handleStandingRowSelect(ObservableStanding rowData) {

        updateText.setText(rowData.toString());
        String standingKey = params.get("eventID") + rowData.getStanding().getTeamId();
        if (standing.containsKey(standingKey)) {
            standing.replace(standingKey, standing.get(standingKey), rowData.getStanding());
        } else {
            standing.put(standingKey, rowData.getStanding());
        }
    }

    @FXML
    private void onUpdateButtonPressed() {
        OutrightGateway outrightGateway = new OutrightGateway();
        if (updateText.getText().equals("")) {
            AlertBox.displayMsg("nothing to update");
            return;
        }
        String response = updateText.getText();
        if (updateText.getText().contains("ObservableTeam")) {
            TeamObject teamObject = new TeamObject();
            teamObject.setEventId(Long.valueOf(params.get("eventID")));
            teamObject.setCompetitionName(updateCompetitionName);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Update team to server");
            try {
                alert.setHeaderText("Would you like to update teams ?");

            } catch (Exception e) {
                alert.setHeaderText("Error while updating");
            }
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {
            } else if (option.get() == ButtonType.OK) {
                for (Map.Entry<String, Team> entry : updateTeam.entrySet()) {
                    teamObject.setTeam(entry.getValue());
                    String requestBody = JsonUtil.marshalJson(teamObject);
                    response = outrightGateway.postHttpResponse(requestBody, "team", null);
                }
                outrightGuiService.publishUpdate(updateTeam.values().toString());
            } else if (option.get() == ButtonType.CANCEL) {
            }
            updateText.setText(response);
            outrightGuiService.refreshOutrightGui(params);
            updateTeam.clear();
            updatedTeam.clear();
            return;
        }

        if (updateText.getText().contains("ObservableFixture")) {
            FixturesList list = new FixturesList();
            list.setEventID(Long.valueOf(params.get("eventID")));
            List<FixturesListEntry> fixturesList = new ArrayList<>();
            list.setFixturesList(fixturesList);
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Update fxiture to server");
            try {
                alert.setHeaderText("Would you like to update Fixtures ?");

            } catch (Exception e) {
                alert.setHeaderText("Error while updating");
            }
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {
            } else if (option.get() == ButtonType.OK) {
                for (FixturesListEntry fixturesListEntry : fixturesListEntry.values()) {
                    list.getFixturesList().add(fixturesListEntry);
                    String requestBody = JsonUtil.marshalJson(list);
                    response = outrightGateway.postHttpResponse(requestBody, "fixtures", null);
                    outrightGuiService.publishUpdate(requestBody);
                }


            } else if (option.get() == ButtonType.CANCEL) {
            }
            updateText.setText(response);
            outrightGuiService.refreshOutrightGui(params);
            fixturesListEntry.clear();
            return;
        }

        if (updateText.getText().contains("ObservableStanding")) {
            String requestBody = JsonUtil.marshalJson(standing);
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Update standing to server");
            try {
                alert.setHeaderText("Would you like to update Standings ?");

            } catch (Exception e) {
                alert.setHeaderText("Error while updating ");
            }
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {
            } else if (option.get() == ButtonType.OK) {
                response = outrightGateway.postHttpResponse(requestBody, "standings", null);
                outrightGuiService.publishUpdate(requestBody);
            } else if (option.get() == ButtonType.CANCEL) {
            }
            updateText.setText(response);
            outrightGuiService.refreshOutrightGui(params);
            standing.clear();
            return;
        }

        if (updateText.getText().contains("ObservableWarning")) {
            response = "Warnings could not be updated";
            updateText.setText(response);
            return;
        }
        response = "Unexpected errors";
        updateText.setText(response);
        return;

    }

    @FXML
    private void onRefreshButtonPressed() throws Exception {

        outrightGuiService.refreshOutrightGui(params);
        updateText.setText("");
    }

    @FXML
    private void onDisplayButtonPressed() throws Exception {

        updateText.setVisible(!updateText.isVisible());
    }

    @FXML
    public void onComboboxUpdated() throws Exception {
        params.clear();
        String selectedEventID = competitionCombo.getSelectionModel().getSelectedItem().toString();
        String selectedEventID2 =
                        selectedEventID.substring(selectedEventID.indexOf("(") + 1, selectedEventID.indexOf(")"));
        params.put("eventID", selectedEventID2);
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

    @FXML
    private void onUpdateColorButtonPressed() throws Exception {
        setUseColor(!this.isUseColor());
        manageDefaultCssStyle();
        teamsTable.refresh();
        standingsTable.refresh();
        warningsTable.refresh();
        fixturesTable.refresh();
    }

    @FXML
    private void onUpdateStandingButtonPressed() throws Exception {
        this.setEditStanding(!this.isEditStanding());
        System.out.println(isEditStanding());
        standingsTable.setEditable(editStanding);
        played.setEditable(editStanding);
        won.setEditable(editStanding);
        drawn.setEditable(editStanding);
        goalsFor.setEditable(editStanding);
        goalsAgainst.setEditable(editStanding);
        points.setEditable(editStanding);

        manageDefaultCssStyle();
        teamsTable.refresh();
        standingsTable.refresh();
        warningsTable.refresh();
        fixturesTable.refresh();
    }

}
