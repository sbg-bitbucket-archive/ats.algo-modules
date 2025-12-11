package ats.algo.matchrunner.view;

import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMap.ObservableMapRow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PrematchResultDialogController {

    @FXML
    private TableView<ObservableMapRow> prematchResultTable;
    @FXML
    private TableColumn<ObservableMapRow, String> nameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> valueColumn;
    @FXML
    Button handleOk;
    @FXML
    Label labelDec;

    private Stage dialogStage;
    private MatchRunner matchRunner;
    private MatchResultMap matchResultMap;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        matchRunner.setReplayMatchFromFile(false);
        matchResultMap.setEventId(matchRunner.getEventId());
        matchRunner.getAlgoManager().handleManualResultMarkets(matchResultMap);
        dialogStage.close();
        matchRunner.stopPeridoStart();
        matchRunner.stopManualResult();
        matchRunner.stopPanel2ClickAble();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
        matchRunner.exitApp();
    }

    public void initialise(MatchRunner matchRunner, ObservableMap observablePrematchResult,
                    MatchResultMap matchResultMap) {
        this.matchRunner = matchRunner;
        if (matchResultMap.extractMatchResult().getMap().containsKey("notSupportedForThisSport")) {
            handleOk.setMouseTransparent(true);
            handleOk.setTextFill(Color.GRAY);
            labelDec.setText("Manual resulting not supported for this sport");
        }
        /*
         * Set up table associations
         */
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        valueColumn.setStyle("-fx-alignment: CENTER;");
        prematchResultTable.setItems(observablePrematchResult.getData());
        this.matchResultMap = matchResultMap;
        prematchResultTable.setEditable(true);
        nameColumn.setEditable(false);
        valueColumn.setCellFactory(TextFieldTableCell.<ObservableMapRow>forTableColumn());
        valueColumn.setOnEditCommit((CellEditEvent<ObservableMapRow, String> t) -> {
            ObservableMapRow o = ((ObservableMapRow) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            o.setValue(t.getNewValue());
            String s = o.getErrorMsg();
            if (s != null) {
                AlertBox.displayMsg(s);
            }
        });
    }



}
