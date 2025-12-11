package ats.algo.sport.handball;

import javafx.application.Application;
import javafx.stage.Stage;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;

/**
 * Provides the main entry point to run the tennis model within the MatchRunner model testing GUI the main entry point
 * just instantiates the matchFormat object plus MatchRunner JavaFx then executes the "start" method which passes
 * control to MatchRunner. MatchRunner then displays the dialog box which allows the user to review/change the
 * MatchFormat settings When the user Ok's the settings the "initialisetennisObjects" method is executed via the
 * callback provided to the MatchRunner constructor
 * 
 * Above all required because we can't instantiate the various required objects until the match format is set, and we
 * can't set the match format without instantiating the gui
 * 
 * @author Geoff
 * 
 */

public class HandballMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static MatchFormat matchFormat;
    private static MonteCarloMatchEngine matchEngine;

    public static void main(String[] args) {
        matchFormat = new HandballMatchFormat();
        matchRunner = new MatchRunner(" Handball v1.0", (MatchFormat) matchFormat, () -> getMatchEngine());
        launch(args);
    }

    /**
     * instantiates the required objects and returns the references to each of them within the map
     * 
     * @return map containing references to each of the objects that MAtchRunner needs
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = new HandballMatchEngine((HandballMatchFormat) matchFormat);
        return matchEngine;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }

}
