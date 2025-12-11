package ats.algo.matchrunner.view;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.matchrunner.MatchRunnable;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMap.ObservableMapRow;
import ats.algo.matchrunner.model.ObservableMarkets;
import ats.algo.matchrunner.model.ObservableMarkets.ObservableMarket;
import ats.algo.matchrunner.model.ObservableMatchParams;
import ats.algo.matchrunner.model.ObservableMatchParams.ObservableMatchParam;
import ats.algo.matchrunner.model.ObservablePrices;
import ats.algo.matchrunner.model.ObservablePrices.ObservablePrice;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class MatchOverviewController {

    private MatchRunnable matchRunner;

    private MatchRunner matchRunnerParent;

    @FXML
    private TitledPane marketsTitlePane;

    @FXML
    private TitledPane generatedMarketsTitlePane;

    /*
     * data for the Params table
     */
    @FXML
    private TableView<ObservableMatchParam> matchParamsTable;
    @FXML
    private TableColumn<ObservableMatchParam, String> nameColumn;
    @FXML
    private TableColumn<ObservableMatchParam, String> typeColumn;
    @FXML
    private TableColumn<ObservableMatchParam, String> groupColumn;
    @FXML
    private TableColumn<ObservableMatchParam, String> valueColumn;
    @FXML
    private TableColumn<ObservableMatchParam, String> stdDevnColumn;
    @FXML
    private TableColumn<ObservableMatchParam, String> biasColumn;

    /*
     * data for the Params table
     */
    @FXML
    private TableView<ObservableMatchParam> matchParamsTable1;
    @FXML
    private TableColumn<ObservableMatchParam, String> nameColumn1;
    @FXML
    private TableColumn<ObservableMatchParam, String> typeColumn1;
    @FXML
    private TableColumn<ObservableMatchParam, String> groupColumn1;
    @FXML
    private TableColumn<ObservableMatchParam, String> valueColumn1;

    /*
     * data for the Params table
     */
    @FXML
    private TableView<ObservableMatchParam> matchParamsTable11;
    @FXML
    private TableColumn<ObservableMatchParam, String> nameColumn11;
    @FXML
    private TableColumn<ObservableMatchParam, String> typeColumn11;
    @FXML
    private TableColumn<ObservableMatchParam, String> groupColumn11;
    @FXML
    private TableColumn<ObservableMatchParam, String> valueColumn11;
    // @FXML
    // private TableColumn<ObservableMatchParam, String> stdDevnColumn1;

    /*
     * data for the Market prices table
     */
    @FXML
    private TableView<ObservableMarket> marketsTable;
    @FXML
    private TableColumn<ObservableMarket, String> marketKeyColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketNameColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketTypeColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketLineIdColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketSelectionColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketProbColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketPriceColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketMarginColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketStateColumn;
    @FXML
    private TableColumn<ObservableMarket, String> selectionStateColumn;
    @FXML
    private TableColumn<ObservableMarket, String> marketSequenceIdColumn;

    /*
     * data for the price source table
     */
    @FXML
    private TableView<ObservableMapRow> sourceTable;
    @FXML
    private TableColumn<ObservableMapRow, String> sourceNameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> sourceWeightColumn;

    /*
     * data for the prices table
     */

    private int currentObservablePricesIndex;
    private ObservablePrices[] observablePrices;
    @FXML
    private TableView<ObservablePrice> priceTable;
    @FXML
    private TableColumn<ObservablePrice, String> priceKeyColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceNameColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceLineIdColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceSelectionColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceValueColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceProbColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceMarginColumn;
    @FXML
    private TableColumn<ObservablePrice, String> priceSequenceIdColumn;

    /*
     * match state related data
     */
    ObservableMap observableMatchState;

    @FXML
    private TableView<ObservableMapRow> generalMatchStateTable;
    @FXML
    private TableColumn<ObservableMapRow, String> generalMatchStateNameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> generalMatchStateValueColumn;
    /*
     * data for the individual scores tab
     */
    @FXML
    private TableView<ObservableMapRow> playerMatchStateTable;
    @FXML
    private TableColumn<ObservableMapRow, String> playerMatchStateNameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> playerMatchStateValueColumn;
    /*
     * data for the match Format table
     */
    @FXML
    private TableView<ObservableMapRow> formatTable;
    @FXML
    private TableColumn<ObservableMapRow, String> formatNameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> formatValueColumn;

    @FXML
    private Label eventTierLabel;


    @FXML
    private TextField eventTierField;

    @FXML
    private TextField marketFilter;

    @FXML
    private void initialize() {
        /*
         * for reasons not understood when this is called none of the FXML objects have been instantiated so can't
         * initialise anything in this method. Instead get initialised in the "initialiseMatchOverviewController" method
         * which is explicitly called by MatchRunner
         */
    }

    /**
     * 
     * @param matchRunner - calling class instance
     * @param observableMatchParams
     * @param observableMarkets
     * @param observableSources
     * @param observablePrices
     * @param observableMatchState
     * @param observableMatchFormat
     */
    public void initialiseMatchOverviewController(int eventTier, MatchRunnable matchRunner,
                    ObservableMatchParams observableMatchParams, ObservableMarkets observableMarkets,
                    ObservableMap observableSources, ObservablePrices[] observablePrices,
                    ObservableMap observableMatchFormat, SimpleMatchState simpleMatchState) {
        this.matchRunner = matchRunner;
        this.observablePrices = observablePrices;
        final Tooltip toolTip = new Tooltip("Filter markets by Type, Name, Category, Line, Prob");
        toolTip.setFont(new Font(12));
        toolTip.setStyle("-fx-background-color: purple;");

        // marketFilter.setTooltip(toolTip);
        /*
         * Set up matchParams associations
         */
        eventTierField.setText(Integer.toString(eventTier));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        stdDevnColumn.setCellValueFactory(cellData -> cellData.getValue().stdDevnProperty());
        biasColumn.setCellValueFactory(cellData -> cellData.getValue().biasProperty());
        matchParamsTable.setItems(observableMatchParams.getData());
        typeColumn.setStyle("-fx-alignment: CENTER;");
        groupColumn.setStyle("-fx-alignment: CENTER;");
        valueColumn.setStyle("-fx-alignment: CENTER;");
        stdDevnColumn.setStyle("-fx-alignment: CENTER;");
        biasColumn.setStyle("-fx-alignment: CENTER;");
        matchParamsTable.setEditable(true);

        valueColumn.setCellFactory(TextFieldTableCell.<ObservableMatchParam>forTableColumn());
        valueColumn.setOnEditCommit((CellEditEvent<ObservableMatchParam, String> t) -> {
            ObservableMatchParam o =
                            ((ObservableMatchParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            String errMsg = o.updateMean(t.getOldValue(), t.getNewValue());
            if (errMsg == null) {
                matchRunner.handleMatchParamsChanged(observableMatchParams.getMatchParams());
            } else
                displayErrorMsg(errMsg);
        });
        stdDevnColumn.setCellFactory(TextFieldTableCell.<ObservableMatchParam>forTableColumn());
        stdDevnColumn.setOnEditCommit((CellEditEvent<ObservableMatchParam, String> t) -> {
            ObservableMatchParam o =
                            ((ObservableMatchParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            String errMsg = o.updateStdDevn(t.getOldValue(), t.getNewValue());
            if (errMsg == null) {
                matchRunner.handleMatchParamsChanged(observableMatchParams.getMatchParams());
            } else
                displayErrorMsg(errMsg);
        });
        biasColumn.setCellFactory(TextFieldTableCell.<ObservableMatchParam>forTableColumn());
        biasColumn.setOnEditCommit((CellEditEvent<ObservableMatchParam, String> t) -> {
            ObservableMatchParam o =
                            ((ObservableMatchParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            String errMsg = o.updateBias(t.getOldValue(), t.getNewValue());
            if (errMsg == null) {
                matchRunner.handleMatchParamsChanged(observableMatchParams.getMatchParams());
            } else
                displayErrorMsg(errMsg);
        });

        nameColumn1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        typeColumn1.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        groupColumn1.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
        valueColumn1.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        // stdDevnColumn1.setCellValueFactory(cellData ->
        // cellData.getValue().stdDevnProperty());

        matchParamsTable1.setItems(observableMatchParams.getData1());
        typeColumn1.setStyle("-fx-alignment: CENTER;");
        groupColumn1.setStyle("-fx-alignment: CENTER;");
        valueColumn1.setStyle("-fx-alignment: CENTER;");
        // stdDevnColumn1.setStyle("-fx-alignment: CENTER;");
        matchParamsTable1.setEditable(true);

        nameColumn11.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        typeColumn11.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        groupColumn11.setCellValueFactory(cellData -> cellData.getValue().groupProperty());
        valueColumn11.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        matchParamsTable11.setItems(observableMatchParams.getData2());
        typeColumn11.setStyle("-fx-alignment: CENTER;");
        groupColumn11.setStyle("-fx-alignment: CENTER;");
        valueColumn11.setStyle("-fx-alignment: CENTER;");
        // stdDevnColumn1.setStyle("-fx-alignment: CENTER;");
        matchParamsTable11.setEditable(true);

        valueColumn11.setCellFactory(TextFieldTableCell.<ObservableMatchParam>forTableColumn());
        valueColumn11.setOnEditCommit((CellEditEvent<ObservableMatchParam, String> t) -> {
            ObservableMatchParam o =
                            ((ObservableMatchParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            String errMsg = o.updateMean(t.getOldValue(), t.getNewValue());
            if (errMsg == null) {
                matchRunner.handleMatchParamsChanged(observableMatchParams.getMatchParams());
            } else
                displayErrorMsg(errMsg);
        });

        valueColumn1.setCellFactory(TextFieldTableCell.<ObservableMatchParam>forTableColumn());
        valueColumn1.setOnEditCommit((CellEditEvent<ObservableMatchParam, String> t) -> {
            ObservableMatchParam o =
                            ((ObservableMatchParam) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            String errMsg = o.updateMean(t.getOldValue(), t.getNewValue());
            if (errMsg == null) {
                matchRunner.handleMatchParamsChanged(observableMatchParams.getMatchParams());
            } else
                displayErrorMsg(errMsg);
        });

        /*
         * Set up markets associations
         */
        marketKeyColumn.setCellValueFactory(cellData -> cellData.getValue().marketKeyProperty());
        marketTypeColumn.setCellValueFactory(cellData -> cellData.getValue().marketTypeProperty());
        marketNameColumn.setCellValueFactory(cellData -> cellData.getValue().marketNameProperty());
        marketLineIdColumn.setCellValueFactory(cellData -> cellData.getValue().marketSubTypeProperty());
        marketSelectionColumn.setCellValueFactory(cellData -> cellData.getValue().marketSelectionProperty());
        marketProbColumn.setCellValueFactory(cellData -> cellData.getValue().marketProbProperty());
        marketPriceColumn.setCellValueFactory(cellData -> cellData.getValue().marketPriceProperty());
        marketMarginColumn.setCellValueFactory(cellData -> cellData.getValue().marketMarginProperty());
        marketStateColumn.setCellValueFactory(cellData -> cellData.getValue().marketStateProperty());
        selectionStateColumn.setCellValueFactory(cellData -> cellData.getValue().selectionStateProperty());
        marketSequenceIdColumn.setCellValueFactory(cellData -> cellData.getValue().marketSequenceIdProperty());

        marketStateColumn.setCellFactory(column -> {
            return new TableCell<ObservableMarket, String>() {
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
                                setStyle("-fx-background-color: #C5E1A5"); // green
                                break;
                            case "SD":
                            case " SD":
                                setStyle("-fx-background-color: #FFF59D");// yellow
                                break;
                            case "SU":
                            case " SU":
                                setStyle("-fx-background-color: #FF8F00");// amber
                                break;
                            case "CL":
                            case " CL":
                                setStyle("-fx-background-color: #F44336");// red
                                break;
                            case "AU":
                            case " AU":
                                setStyle("-fx-background-color: #00FFFF");// blue
                                break;
                            default:
                                setStyle("");
                                break;
                        }
                    }
                }
            };
        });

        selectionStateColumn.setCellFactory(column -> {
            return new TableCell<ObservableMarket, String>() {
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
                                setStyle("-fx-background-color: #C5E1A5"); // green
                                break;
                            case "SD":
                            case " SD":
                                setStyle("-fx-background-color: #FFF59D");// yellow
                                break;
                            case "SU":
                            case " SU":
                                setStyle("-fx-background-color: #FF8F00");// amber
                                break;
                            case "CL":
                            case " CL":
                                setStyle("-fx-background-color: #F44336");// red
                                break;
                            case "AU":
                            case " AU":
                                setStyle("-fx-background-color: #00FFFF");// blue
                                break;
                            default:
                                setStyle("");
                                break;
                        }
                    }
                }
            };
        });

        marketProbColumn.setStyle("-fx-alignment: CENTER;");
        marketPriceColumn.setStyle("-fx-alignment: CENTER;");
        marketMarginColumn.setStyle("-fx-alignment: CENTER;");
        marketTypeColumn.setStyle("-fx-alignment: CENTER;");
        marketLineIdColumn.setStyle("-fx-alignment: CENTER;");
        marketStateColumn.setStyle("-fx-alignment: CENTER;");
        marketKeyColumn.setSortable(false);
        marketTypeColumn.setSortable(false);
        marketNameColumn.setSortable(false);
        marketLineIdColumn.setSortable(false);
        marketSelectionColumn.setSortable(false);
        marketProbColumn.setSortable(false);
        marketPriceColumn.setSortable(false);
        marketMarginColumn.setSortable(false);
        marketStateColumn.setSortable(false);
        selectionStateColumn.setSortable(false);
        marketSequenceIdColumn.setSortable(false);

        marketsTable.setItems(observableMarkets.getData());
        // Listen for selection changes
        // marketsTable.getSelectionModel().selectedItemProperty()
        // .addListener((observable, oldValue, newValue) ->
        // handleMarketRowSelected(newValue));

        marketsTable.setRowFactory(tv -> {
            TableRow<ObservableMarket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                ObservableMarket rowData = row.getItem();
                if (event.isAltDown())
                    handleSelectionRowSelect(rowData);
                else if (event.isControlDown())
                    matchRunner.handleExportCollectedMarkets();
                else if (!row.isEmpty())
                    if (event.getButton() == MouseButton.PRIMARY)
                        handleMarketRowSelect(rowData);
                    else
                        handleMarketRowRightClick(rowData);
            });
            return row;
        });

        /*
         * set up price source associations and the methods to handle table edits
         */
        sourceTable.setEditable(true);
        sourceTable.setItems(observableSources.getData());
        sourceNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        sourceNameColumn.setCellFactory(TextFieldTableCell.<ObservableMapRow>forTableColumn());
        sourceNameColumn.setOnEditCommit((CellEditEvent<ObservableMapRow, String> t) -> {
            ObservableMapRow o = ((ObservableMapRow) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            o.setDisplayKey(t.getNewValue());
            marketsTitlePane.setText(t.getNewValue());
        });
        sourceWeightColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        sourceWeightColumn.setStyle("-fx-alignment: CENTER;");
        sourceWeightColumn.setCellFactory(TextFieldTableCell.<ObservableMapRow>forTableColumn());
        sourceWeightColumn.setOnEditCommit((CellEditEvent<ObservableMapRow, String> t) -> {
            ObservableMapRow o = ((ObservableMapRow) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            try {
                double no = Double.parseDouble(t.getNewValue());
                o.setValue(String.format("%.1f", no));
            } catch (NumberFormatException e) {
                o.setValue(t.getOldValue());
            }

        });
        // Listen for selection changes
        sourceTable.getSelectionModel().selectedItemProperty()
                        .addListener((observable, oldValue, newValue) -> showSelectedMarketPrices(newValue));
        /*
         * set up price Table associates and methods to hand table edit and row select
         */
        priceKeyColumn.setCellValueFactory(cellData -> cellData.getValue().priceKeyProperty());
        priceSequenceIdColumn.setCellValueFactory(cellData -> cellData.getValue().priceSequenceIdProperty());
        priceNameColumn.setCellValueFactory(cellData -> cellData.getValue().priceNameProperty());
        priceLineIdColumn.setCellValueFactory(cellData -> cellData.getValue().priceSubTypeProperty());
        priceSelectionColumn.setCellValueFactory(cellData -> cellData.getValue().priceSelectionProperty());
        priceValueColumn.setCellValueFactory(cellData -> cellData.getValue().priceValueProperty());
        priceValueColumn.setStyle("-fx-alignment: CENTER;");
        priceProbColumn.setCellValueFactory(cellData -> cellData.getValue().priceProbProperty());
        priceProbColumn.setStyle("-fx-alignment: CENTER;");
        priceProbColumn.setEditable(false);
        priceMarginColumn.setCellValueFactory(cellData -> cellData.getValue().priceMarginProperty());
        priceMarginColumn.setStyle("-fx-alignment: CENTER;");
        priceMarginColumn.setEditable(false);
        priceTable.setEditable(true);
        priceTable.setItems(observablePrices[0].getData());
        currentObservablePricesIndex = 0;
        marketsTitlePane.setText("Source 1");
        priceKeyColumn.setEditable(false);
        priceNameColumn.setEditable(false);
        priceSelectionColumn.setEditable(false);
        priceValueColumn.setCellFactory(TextFieldTableCell.<ObservablePrice>forTableColumn());
        priceValueColumn.setOnEditCommit((CellEditEvent<ObservablePrice, String> t) -> {
            ObservablePrice o = ((ObservablePrice) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            o.updateValue(t.getNewValue());
        });
        priceLineIdColumn.setCellFactory(TextFieldTableCell.<ObservablePrice>forTableColumn());
        priceLineIdColumn.setOnEditCommit((CellEditEvent<ObservablePrice, String> t) -> {
            ObservablePrice o = ((ObservablePrice) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            o.updateSubType(t.getNewValue());
        });

        // Listen for selection changes

        priceTable.setRowFactory(tv -> {
            TableRow<ObservablePrice> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                ObservablePrice rowData = row.getItem();
                if (!row.isEmpty())
                    if (event.getButton() == MouseButton.PRIMARY)
                        handlePriceRowSelect(rowData);
                    else
                        handlePriceRowRightClick(rowData);

            });
            return row;
        });



        /*
         * set up format
         */
        formatNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        formatValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        formatValueColumn.setStyle("-fx-alignment: CENTER;");
        formatTable.setItems(observableMatchFormat.getData());

        /*
         * set up currentScore
         */
        observableMatchState = new ObservableMap(e -> null);
        updateObservableMatchState(simpleMatchState);

        generalMatchStateNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        generalMatchStateValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        generalMatchStateValueColumn.setStyle("-fx-alignment: CENTER;");
        generalMatchStateTable.setItems(observableMatchState.getData());
        generalMatchStateTable.setOnMouseClicked(event -> {
            handleMatchStateRowRightClick();
        });
        playerMatchStateNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        playerMatchStateValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        playerMatchStateValueColumn.setStyle("-fx-alignment: CENTER;");
        playerMatchStateTable.setItems(observableMatchState.getPlayerData());
        /*
         * set up format
         */
        formatNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        formatValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        formatValueColumn.setStyle("-fx-alignment: CENTER;");
        formatTable.setItems(observableMatchFormat.getData());
        formatTable.setEditable(true);
        formatValueColumn.setCellFactory(TextFieldTableCell.<ObservableMapRow>forTableColumn());
        formatValueColumn.setOnEditCommit((CellEditEvent<ObservableMapRow, String> t) -> {
            ObservableMapRow o = ((ObservableMapRow) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            o.setValue(t.getNewValue());
            String s = o.getErrorMsg();
            if (s != null) {
                AlertBox.displayMsg(s);
            } else {
                MatchFormat matchFormat = matchRunnerParent.getMatchFormat();
                matchRunnerParent.handleResetMatchFormat(matchFormat);
            } ;

        });
        /*
         * 
         */
        this.updateSuspensionStatus(false);
    }

    /**
     * 
     * @param o
     * @return
     */
    private Object handleMarketRowSelect(ObservableMarket o) {

        String selKey = o.getSelection();
        matchRunner.handleMarketRowSelected(o.getFullKey(), selKey);
        return null;
    }

    private Object handleSelectionRowSelect(ObservableMarket o) {

        String selKey = o.getSelection();
        matchRunner.setButtonClickAble(true);
        if (o.firstRow)
            matchRunner.handleSelectionRowSelected(o.getFullKey(), null, selKey);
        else
            matchRunner.handleSelectionRowSelected(o.getFullKey(), o.getShortKey(), selKey);
        return null;
    }

    private Object handleMarketRowRightClick(ObservableMarket o) {
        matchRunner.handleNewPricesRowRequest(o.getFullKey());
        return null;
    }

    private Object handlePriceRowSelect(ObservablePrice o) {
        /*
         * do nothing with left click for now
         */
        return null;
    }

    private Object handlePriceRowRightClick(ObservablePrice o) {
        matchRunner.handleRemovePricesRowRequest(o.getFullKey());
        return null;
    }

    private void showSelectedMarketPrices(ObservableMapRow o) {
        int index = o.getIndex();
        String key = o.getDisplayKey();
        priceTable.setItems(observablePrices[index].getData());
        currentObservablePricesIndex = index;
        marketsTitlePane.setText(key);
    }

    public int getObservablePricesIndex() {
        return currentObservablePricesIndex;
    }

    public void refreshObservablePrices() {
        priceTable.setItems(observablePrices[currentObservablePricesIndex].getData());
    }

    private void displayErrorMsg(String errMsg) {

        AlertBox.displayMsg(errMsg);
    }

    public void updateEventTierLabel(int eventTier) {
        this.eventTierLabel.setText(String.format("EventTier %d", eventTier));
    }

    public void updateObservableMatchState(SimpleMatchState sms) {
        observableMatchState.updateDisplayedData(sms.generalStateAsMap(), sms.playerStateAsMap());
    }

    public void updateObservableEventTier(String eventTier) {
        this.eventTierField.setText(eventTier);
    }

    public void updateSuspensionStatus(boolean suspend) {
        if (suspend)
            generatedMarketsTitlePane.setText("Generated markets - EVENT LEVEL SUSPENSION IN FORCE");
        else
            generatedMarketsTitlePane.setText("Generated markets");
    }

    private Object handleMatchStateRowRightClick() {
        matchRunner.handleMatchStateRequest();
        return null;
    }

    @FXML
    public void onEventEntry() {
        String filter = marketFilter.getText();
        ObservableMarkets ovM = matchRunnerParent.getObservableMarkets();
        if (matchRunnerParent.getMarkets() != null)
            ovM.update(matchRunnerParent.getMarkets(), filter);
        matchRunnerParent.setObservableMarkets(ovM);
    }

    public void setParent(MatchRunner matchRunner) {
        this.matchRunnerParent = matchRunner;
    }

    @FXML
    private void onEventTierEntry() {
        String eventTierStr = eventTierField.getText();
        try {
            int eventTier = Integer.parseInt(eventTierStr);
            matchRunner.setEventTier(eventTier);
        } catch (NumberFormatException e) {
            eventTierField.setText(Integer.toString(matchRunner.getEventTier()));

        }
    }



}
