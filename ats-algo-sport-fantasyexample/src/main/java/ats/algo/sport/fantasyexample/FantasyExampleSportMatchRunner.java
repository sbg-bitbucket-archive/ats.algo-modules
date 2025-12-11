package ats.algo.sport.fantasyexample;

import javafx.application.Application;
import javafx.stage.Stage;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchEngine;
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchFormat;
import ats.algo.springbridge.GetInstancesFromProperties;
import ats.algo.springbridge.InitSpring;

public class FantasyExampleSportMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static FantasyExampleSportMatchFormat matchFormat;
    private static MatchEngine matchEngine;

    protected static void init(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        InitSpring.init();
        matchFormat = new FantasyExampleSportMatchFormat();
        matchRunner = new MatchRunner("Fantasy sport example v1.0", (MatchFormat) matchFormat, () -> getMatchEngine());
        launch(args);
    }

    public static void main(String[] args) {
        init(args);
    }



    /**
     * instantiates the required objects and returns the references to each of them within the map
     * 
     * @return map containing references to each of the objects that MAtchRunner needs
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = GetInstancesFromProperties.getMatchEngine(FantasyExampleSportMatchEngine.class,
                        SupportedSportType.TEST_SPORT, matchFormat);
        return matchEngine;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }
}
