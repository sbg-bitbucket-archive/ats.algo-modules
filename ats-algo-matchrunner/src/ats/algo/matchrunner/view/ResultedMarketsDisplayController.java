package ats.algo.matchrunner.view;

import ats.algo.matchrunner.model.ObservableMarketsAwaitingResult;
import ats.algo.matchrunner.model.ObservableMarketsAwaitingResult.ObservableMarketAwaitingResult;
import ats.algo.matchrunner.model.ObservableResultedMarkets;
import ats.algo.matchrunner.model.ObservableResultedMarkets.ObservableResultedMarket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResultedMarketsDisplayController {

    @FXML
    private TableView<ObservableResultedMarket> resultedMarketsTable;
    @FXML
    private TableColumn<ObservableResultedMarket, String> typeColumn;
    @FXML
    private TableColumn<ObservableResultedMarket, String> subTypeColumn;
    @FXML
    private TableColumn<ObservableResultedMarket, String> sequenceIdColumn;
    @FXML
    private TableColumn<ObservableResultedMarket, String> descriptionColumn;
    @FXML
    private TableColumn<ObservableResultedMarket, String> actualLineColumn;
    @FXML
    private TableColumn<ObservableResultedMarket, String> winningSelectionColumn;

    @FXML
    private TableView<ObservableMarketAwaitingResult> marketsAwaitingResultTable;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktTypeColumn;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktSubTypeColumn;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktSequenceIdColumn;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktDescriptionColumn;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktActualLineColumn;
    @FXML
    private TableColumn<ObservableMarketAwaitingResult, String> mktWinningSelectionColumn;

    private Stage dialogStage;

    @FXML
    private TextField resultMarketFilter;

    private ObservableList<ObservableResultedMarket> observableResultedMarkets;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleOk() {
        dialogStage.close();
    }

    @FXML
    public void initialize() {
        /*
         * Set up associations
         */
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketTypeProperty());
        subTypeColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketSubTypeProperty());
        sequenceIdColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketSequenceIdProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketDescriptionProperty());
        actualLineColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketActualLine());
        winningSelectionColumn.setCellValueFactory(cellData -> cellData.getValue().resultedMarketWinningSelection());
        resultedMarketsTable.setEditable(false);

        mktTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        mktSubTypeColumn.setCellValueFactory(cellData -> cellData.getValue().subTypeProperty());
        mktSequenceIdColumn.setCellValueFactory(cellData -> cellData.getValue().sequenceIdProperty());
        mktDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        mktActualLineColumn.setCellValueFactory(cellData -> cellData.getValue().actualLine());
        mktWinningSelectionColumn.setCellValueFactory(cellData -> cellData.getValue().winningSelection());
        marketsAwaitingResultTable.setEditable(false);
    }

    public void setResultedMarkets(ObservableResultedMarkets resultedMarkets,
                    ObservableMarketsAwaitingResult marketsAwaitingResult) {
        observableResultedMarkets = resultedMarkets.getData();
        resultedMarketsTable.setItems(resultedMarkets.getData());
        marketsAwaitingResultTable.setItems(marketsAwaitingResult.getData());
    }

    @FXML
    public void onEventEntry() {
        String filter = resultMarketFilter.getText();
        ObservableList<ObservableResultedMarket> observableResultedMarketsfilter = FXCollections.observableArrayList();

        observableResultedMarkets.stream()
                        .filter(name -> name.resultedMarketTypeProperty().getValue().toUpperCase()
                                        .contains(filter.toUpperCase()))
                        .forEach((e) -> observableResultedMarketsfilter.add(e));
        observableResultedMarkets.stream()
                        .filter(name -> name.resultedMarketWinningSelection().getValue().toUpperCase()
                                        .contains(filter.toUpperCase()))
                        .forEach((e) -> observableResultedMarketsfilter.add(e));
        resultedMarketsTable.setItems(observableResultedMarketsfilter);

    }
}
