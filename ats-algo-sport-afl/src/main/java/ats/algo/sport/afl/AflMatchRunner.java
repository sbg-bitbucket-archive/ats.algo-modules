package ats.algo.sport.afl;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.springbridge.GetInstancesFromProperties;
import javafx.application.Application;
import javafx.stage.Stage;

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
 * @author Jin
 */
public class AflMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static MatchFormat matchFormat;
    private static MatchEngine matchEngine;

    public static void main(String[] args) {
        init(args);
    }

    protected static void init(String[] args) {
        matchFormat = new AflMatchFormat();
        matchRunner = new MatchRunner(" Afl sport v1.0", (MatchFormat) matchFormat, () -> getMatchEngine());
        launch(args);
    }

    /**
     * instantiates the required objects and returns the references to each of them within the map
     * 
     * @return map containing references to each of the objects that MAtchRunner needs
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = GetInstancesFromProperties.getMatchEngine(AflMatchEngine.class, SupportedSportType.AUSSIE_RULES,
                        matchFormat);
        System.out.println("Using matchEngine: " + matchEngine.getClass().toString());
        return matchEngine;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }

}
