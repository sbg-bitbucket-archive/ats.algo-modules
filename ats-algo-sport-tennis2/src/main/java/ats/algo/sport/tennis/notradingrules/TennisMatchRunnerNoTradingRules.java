package ats.algo.sport.tennis.notradingrules;

import ats.algo.sport.tennis.TennisMatchRunner;

public class TennisMatchRunnerNoTradingRules extends TennisMatchRunner {

    public static void main(String[] args) {
        /*
         * set properties for accessing BS external model via MQ server
         */
        System.setProperty("algo.tennis.tradingrules.class",
                        "ats.algo.sport.tennis.notradingrules.TennisEmptyTradingRules");
        init(args);
    }

}
