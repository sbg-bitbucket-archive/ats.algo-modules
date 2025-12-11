package ats.algo.matchrunner.view;



import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.tennis.TennisMatchState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MatchStateDialogController {
    private MatchRunner matchRunner;

    @FXML
    private TextArea marketStateTextArea;

    private String supportSport;

    @FXML
    private Button loadOk;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOk() {
        dialogStage.close();
    }

    @FXML
    private void handleLoadOk() {
        String json = marketStateTextArea.getText();
        try {
            if (supportSport.equals("TENNIS")) {
                TennisMatchState matchState = JsonSerializer.deserialize(json, TennisMatchState.class);
                matchRunner.handleResetMatchFormat(matchState.getMatchFormat());
                matchRunner.publishMatchStateMR(matchRunner.getEventId(), matchState);
                matchRunner.publishMatchState(matchRunner.getEventId(),
                                (SimpleMatchState) matchState.generateSimpleMatchState());
            }
            if (supportSport.equals("SOCCER")) {
                FootballMatchState matchState = JsonSerializer.deserialize(json, FootballMatchState.class);
                matchRunner.handleResetMatchFormat(matchState.getMatchFormat());
                matchRunner.publishMatchStateMR(matchRunner.getEventId(), matchState);
                matchRunner.publishMatchState(matchRunner.getEventId(),
                                (SimpleMatchState) matchState.generateSimpleMatchState());
            }

            dialogStage.close();

        } catch (Exception e) {
            System.err.println("Problem handling message " + json + " : " + e.getMessage());
        }

    }

    public void setMatchRunner(MatchRunner matchRunner1) {
        this.matchRunner = matchRunner1;
    }



    public void setMarketDetails(SupportedSportType sport) {
        supportSport = sport.toString();
        String s = sport + " Match State JSon needed";
        loadOk.setVisible(false);
        if (sport.equals(SupportedSportType.TENNIS) || sport.equals(SupportedSportType.SOCCER)) {
            marketStateTextArea.setText(s);
            loadOk.setVisible(true);
        } else {
            marketStateTextArea.setText("Detail view not supported for this sport ： " + sport);
        }

    }
}
