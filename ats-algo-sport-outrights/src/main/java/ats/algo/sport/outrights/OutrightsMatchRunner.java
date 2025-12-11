package ats.algo.sport.outrights;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MatchRunner which uses the external outrights model
 */
public class OutrightsMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static MatchFormat matchFormat;
    private static MatchEngine matchEngine;

    protected static void init(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        matchFormat = new OutrightsMatchFormat();
        matchRunner = new MatchRunner("Outrights", matchFormat, () -> getMatchEngine(),
                        new OutrightsMatchIncidentGenerator());
        launch(args);
    }

    // private static final String eventID = "123456"; //demo league
    private static final String eventID = "5537098"; // 18/19 premier leaugue

    public static void main(String[] args) {
        String url = System.getProperty("urlForExternalModelsMqBroker");
        /*
         * if url not already set then set to default value
         */
        if (url == null)
            System.setProperty("urlForExternalModelsMqBroker",
                            "tcp://localhost:61616?connectionTimeout=0&wireFormat.maxInactivityDuration=0");
        System.setProperty("algo.outrights.externalModel", "true");
        System.setProperty("algo.outrights.clientTradingRules", "true");
        System.setProperty("algo.outrights.clientResultingOnly", "true");
        System.setProperty("algo.outrights.clientParamFinding", "true");
        System.setProperty("eventID", eventID);
        init(args);
    }

    /**
     * instantiates the required objects and returns the references to each of them within the map
     * 
     * @return map containing references to each of the objects that MAtchRunner needs
     */
    private static MatchEngine getMatchEngine() {
        matchEngine = new OutrightsMatchEngine();
        return matchEngine;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }
}
