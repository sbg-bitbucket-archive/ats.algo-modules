package ats.algo.outrights.loggetter.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.genericsupportfunctions.JsonFormatter;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMap.ObservableMapRow;
import ats.algo.outrights.loggetter.LogGetter;
import ats.org.json.JSONException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogGetterDisplayController {

    ObservableMap observableLoggedLines;
    LogGetter logGetter;
    @FXML
    private TableView<ObservableMapRow> loggedLinesTable;
    @FXML
    private TableColumn<ObservableMapRow, String> loggedLineColumn;
    @FXML
    private TextArea rowDetail;
    @FXML
    private TextArea jsonObjectDetail;
    @FXML
    private TextField eventIdInput;
    @FXML
    private TextField freeTextInput;

    @FXML
    private void initialize() {

    }

    public void initialize(LogGetter logGetter) {

        this.logGetter = logGetter;
        observableLoggedLines = new ObservableMap(null);
        loggedLinesTable.setItems(observableLoggedLines.getData());
        loggedLinesTable.setEditable(false);
        loggedLineColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        loggedLinesTable.getSelectionModel().selectedItemProperty().addListener(
                        (observable, oldValue, newValue) -> handleLoggedLinesRowSelected(newValue.getValue()));

        rowDetail.setEditable(false);
        jsonObjectDetail.setEditable(false);
        rowDetail.setWrapText(true);
        jsonObjectDetail.setWrapText(false);
    }

    @FXML
    void onEventIdEntered() {
        logGetter.handleEventIdEntered(eventIdInput.getText());
    }

    @FXML
    void onFreeTextEntered() {
        logGetter.handleFreeTextEntered(this.freeTextInput.getText());
    }


    void handleLoggedLinesRowSelected(String row) {
        logGetter.handleLogRowSelected(row);
    }

    public void updateLogRows(List<String> logRows) {
        int i = 1;
        int maxLength = 0;
        Map<String, String> map = new HashMap<>();
        for (String row : logRows) {
            map.put("R" + Integer.toString(i), row);
            if (row.length() > maxLength)
                maxLength = row.length();
            i++;
        }
        // loggedLinesTable.setMinWidth(maxLength);
        // loggedLineColumn.setMinWidth(maxLength);
        // loggedLinesTable.setMaxWidth(maxLength);
        // loggedLineColumn.setMaxWidth(maxLength);
        // loggedLinesTable.setPrefWidth(maxLength);
        // loggedLineColumn.setPrefWidth(maxLength);

        observableLoggedLines.updateDisplayedData(map);

    }

    public void updateLogRowDetail(String row) {
        String rowDetail = extractRowDetail(row);
        String objectDetail = extractJsonObject(row);
        this.rowDetail.setText(rowDetail);
        this.jsonObjectDetail.setText(objectDetail);
    }

    private String extractRowDetail(String row) {
        int n = row.indexOf("{");
        if (n > 0)
            return row.substring(0, n - 1);
        else
            return row;
    }

    private String extractJsonObject(String row) {
        int n = row.indexOf("{");
        if (n >= 0) {
            String json = row.substring(n);
            String formattedJson;
            try {
                formattedJson = JsonFormatter.format(json);
            } catch (JSONException e) {
                formattedJson = json;
            }
            return formattedJson;
        } else
            return "";
    }



    public void setDialogStage(Stage dialogStage) {
        /*
         * do nothing
         */
    }
}
