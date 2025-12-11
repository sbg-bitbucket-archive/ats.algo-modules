package ats.algo.matchrunner.view;

import ats.algo.matchrunner.MatchRunner;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMap.ObservableMapRow;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class MatchFormatDialogController {

    @FXML
    private TableView<ObservableMapRow> matchFormatTable;
    @FXML
    private TableColumn<ObservableMapRow, String> nameColumn;
    @FXML
    private TableColumn<ObservableMapRow, String> valueColumn;
    @FXML
    private RadioButton externalModel;

    private Stage dialogStage;
    private MatchRunner matchRunner;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        matchRunner.setReplayMatchFromFile(false);
        boolean useExternalModel = externalModel.isSelected();
        matchRunner.setUseExternalModel(useExternalModel);
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
        matchRunner.exitApp();
    }

    @FXML
    private void handleReplay() {
        matchRunner.setReplayMatchFromFile(true);
        boolean useExternalModel = externalModel.isSelected();
        matchRunner.setUseExternalModel(useExternalModel);
        dialogStage.close();
    }

    // @FXML
    // private void handleExternalModel() {
    // matchRunner.setUseExternalModel(true);
    // dialogStage.close();
    // dialogStage.close();
    // matchRunner.exitApp();
    // }

    public void initialise(MatchRunner matchRunner, ObservableMap observableMatchFormat) {
        this.matchRunner = matchRunner;

        /*
         * Set up table associations
         */
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        valueColumn.setStyle("-fx-alignment: CENTER;");
        matchFormatTable.setItems(observableMatchFormat.getData());
        matchFormatTable.setEditable(true);
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
