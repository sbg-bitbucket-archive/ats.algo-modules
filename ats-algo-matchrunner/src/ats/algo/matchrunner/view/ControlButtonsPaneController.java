package ats.algo.matchrunner.view;

import java.io.IOException;

import ats.algo.matchrunner.MatchRunner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;


public class ControlButtonsPaneController {

    private MatchRunner matchRunner;

    @FXML
    Button saveMatch;
    @FXML
    Button paramFind;
    @FXML
    Button manualResult;
    @FXML
    Button buildABet;

    @FXML
    private void initialize() {
        unGreyOutButton(saveMatch);
        unGreyOutButton(paramFind);
        buildABet.setVisible(false);
        buildABet.setDisable(true);

    }

    public void setParent(MatchRunner matchRunner) {
        this.matchRunner = matchRunner;
        if (this.matchRunner == null)
            greyOutButton(manualResult);
        else if (this.matchRunner.getMatchFormat().generateMatchResulter() == null)
            greyOutButton(manualResult);
        else
            unGreyOutButton(manualResult);
    }



    @FXML
    public void onParamFindButtonPressed() {
        matchRunner.handleFindParamsButtonPressed();
    }



    @FXML
    public void onShowResultedMarketsButtonPressed() {
        matchRunner.showResultedMarkets();
    }

    @FXML
    public void onTradingRulesButtonPressed() {
        matchRunner.showTradingRules();
    }

    @FXML
    public void onManualResultButtonPressed() {
        matchRunner.handlePrematchResultButtonPressed();
    }

    @FXML
    public void onBuildABetButtonPressed() {
        matchRunner.handleBuildABetButtonPressed();
    }

    @FXML
    public void onSaveMatchButtonPressed() {

        try {
            matchRunner.saveMatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopButtonClick() {
        greyOutButton(saveMatch);
        greyOutButton(paramFind);
        greyOutButton(manualResult);
        saveMatch.setMouseTransparent(true);
        paramFind.setMouseTransparent(true);
        manualResult.setMouseTransparent(true);
    }

    public void stopManualResult() {
        // greyOutButton(manualResult);
        // manualResult.setMouseTransparent(true);
    }

    public void startButtonClick() {
        unGreyOutButton(saveMatch);
        unGreyOutButton(paramFind);
        unGreyOutButton(manualResult);
        saveMatch.setMouseTransparent(false);
        paramFind.setMouseTransparent(false);
        manualResult.setMouseTransparent(false);
    }

    private void greyOutButton(Button button) {
        button.setTextFill(Color.GRAY);
    }

    private void unGreyOutButton(Button button) {
        button.setTextFill(Color.BLACK);
    }

    public void buildABetOpen() {
        buildABet.setDisable(false);
        buildABet.setVisible(true);
    }

}
