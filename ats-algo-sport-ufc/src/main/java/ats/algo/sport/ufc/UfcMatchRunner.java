package ats.algo.sport.ufc;

import javafx.application.Application;
import javafx.stage.Stage;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;

/**
 * ufc model
 * 
 * @author Rob
 *
 */
public class UfcMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static MatchFormat matchFormat;
    private static MatchEngine matchEngine;



    public static void main(String[] args) {
        matchFormat = new UfcMatchFormat();
        matchRunner = new MatchRunner(" UFC model", (MatchFormat) matchFormat, () -> getMatchEngine());
        launch(args);
    }


    /**
     * called by matchRunner to instantiate the match engine for this sport
     * 
     * @return the matchEngine object
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = new UfcMatchEngine((UfcMatchFormat) matchFormat);
        return matchEngine;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }

}
