package example.algo.tennis;

import javafx.application.Application;
import javafx.stage.Stage;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.sport.tennis.TennisMatchFormat;

/**
 * Example tennis model
 * 
 * @author Geoff
 *
 */
public class ExampleTennisMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static MatchFormat matchFormat;
    private static MatchEngine matchEngine;



    public static void main(String[] args) {
        matchFormat = new TennisMatchFormat();
        matchRunner = new MatchRunner(" Third party tennis model", (MatchFormat) matchFormat, () -> getMatchEngine());
        launch(args);
    }


    /**
     * called by matchRunner to instantiate the match engine for this sport
     * 
     * @return the matchEngine object
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = new ExampleTennisMatchEngine((TennisMatchFormat) matchFormat);
        return matchEngine;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }

}
