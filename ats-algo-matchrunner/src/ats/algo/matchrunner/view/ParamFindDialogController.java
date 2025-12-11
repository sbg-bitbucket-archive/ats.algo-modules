package ats.algo.matchrunner.view;

import ats.algo.matchrunner.MatchRunnable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ParamFindDialogController {

    private Stage dialogStage;
    @FXML
    private TextArea textArea;

    @FXML
    private Label detailedLogReqdMsg;
    private boolean detailedLogReqd;

    private boolean okClicked = false;
    private String text;
    private MatchRunnable matchRunner;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleStart() {
        matchRunner.handleStartParamFind(detailedLogReqd);
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleDetailedLogReqd() {
        if (detailedLogReqd) {
            detailedLogReqd = false;
            detailedLogReqdMsg.setText("Standard Logs");
        } else {
            detailedLogReqd = true;
            detailedLogReqdMsg.setText("Detailed Logs");

        }
    }

    public void setParent(MatchRunnable matchRunner) {
        this.matchRunner = matchRunner;
    }

    public void addTextToDialogBox(String text) {
        this.text += text;
        textArea.setText(this.text);
    }

    public void setTextInDialogBox(String text) {
        this.text = text;
        textArea.setText(this.text);
    }

}
