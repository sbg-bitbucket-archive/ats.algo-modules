package ats.algo.ppb.tennis;

import javafx.application.Application;
import javafx.stage.Stage;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.matchrunner.MatchRunner;
import ats.algo.sport.tennis.TennisMatchEngine;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.gui.TennisMatchIncidentGenerator;
import ats.algo.springbridge.GetInstancesFromProperties;
import ats.algo.springbridge.InitSpring;

public class PpbTennisMatchRunner extends Application {
    private static MatchRunner matchRunner;
    private static TennisMatchFormat matchFormat;
    private static MatchEngine matchEngine;

    protected static void init(String[] args) {
        // LogUtil.initConsoleLogging(Level.TRACE);
        InitSpring.init();
        matchFormat = new TennisMatchFormat();
        matchRunner = new MatchRunner("Tennis model v1.0.6", (MatchFormat) matchFormat, () -> getMatchEngine(),
                        new TennisMatchIncidentGenerator());
        launch(args);
    }

    public static void main(String[] args) {
        System.setProperty("algo.tennis.priceCalculator.class", "ats.algo.gateway.ppb.PpbPriceCalcGateway");
        System.setProperty("algo.tennis.paramFinder.class", "ats.algo.gateway.ppb.PpbParamFinder");
        System.setProperty("algo.tennis.tradingrules.class",
                        "ats.algo.sport.tennis.tradingrules.ppb.PpbTennisTradingRules");
        System.setProperty("updateParamMapWithPropertiesChange", "false");
        System.setProperty("clientMarkets", "ppb");
        init(args);
    }

    /**
     * instantiates the required objects and returns the references to each of them within the map
     * 
     * @return map containing references to each of the objects that MAtchRunner needs
     */
    private static MatchEngine getMatchEngine() {

        matchEngine = GetInstancesFromProperties.getMatchEngine(TennisMatchEngine.class, SupportedSportType.TENNIS,
                        matchFormat);
        return matchEngine;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        matchRunner.start(primaryStage);
    }
}
