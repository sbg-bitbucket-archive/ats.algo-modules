package ats.algo.sport.football.notradingrules;

import ats.algo.sport.football.FootballMatchRunner;

public class FootballMatchRunnerNoTradingRules extends FootballMatchRunner {

    public static void main(String[] args) {
        /*
         * set properties for accessing BS external model via MQ server
         */
        System.setProperty("algo.soccer.tradingrules.class",
                        "ats.algo.sport.football.notradingrules.EmptyTradingRules");
        init(args);
    }

}
