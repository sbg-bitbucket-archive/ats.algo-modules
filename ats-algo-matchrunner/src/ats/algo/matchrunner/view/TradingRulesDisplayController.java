package ats.algo.matchrunner.view;

import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.JsonSerializer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TradingRulesDisplayController {

    @FXML
    private TextArea rulesDisplay;

    @FXML
    public void handleFinished() {
        dialogStage.close();
    }

    @FXML
    private void handleUpdateRules() {
        AlertBox.displayComingSoon();
    }

    @FXML
    private void handleLoadRules() {
        AlertBox.displayComingSoon();
    }

    @FXML
    private void handleSaveRules() {
        AlertBox.displayComingSoon();
    }

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize() {
        /*
         * nothing to do
         */
    }

    public void setTradingRules(TradingRules tradingRules) {
        JsonSerializer.serialize(tradingRules, true);
        StringBuilder sb = new StringBuilder();
        sb.append("Trading rules class: ").append(tradingRules.getClass().getName()).append("\n");
        sb.append(JsonSerializer.serialize(tradingRules, true));
        rulesDisplay.setText(sb.toString());
    }

}
