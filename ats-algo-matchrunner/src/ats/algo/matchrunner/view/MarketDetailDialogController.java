package ats.algo.matchrunner.view;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.core.util.json.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MarketDetailDialogController {


    @FXML
    private TextArea marketStatusTextArea;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        dialogStage.close();
    }

    @FXML
    private void initialize() {
        /*
         * Set up matchParams associations
         */

    }



    public void setMarketDetails(String key, Market market, SupportedSportType sport) {
        String s = null;
        if (sport == null)
            s = "Sport: null\n";
        else
            s = "Sport" + sport.toString() + "\n";
        s += "MarketKey: " + key + "\n";
        String mktStr = JsonUtil.marshalJson(market, true);
        s += mktStr;
        marketStatusTextArea.setText(s);

    }
}
