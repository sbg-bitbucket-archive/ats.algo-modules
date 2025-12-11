package ats.algo.betstars.football;

import ats.algo.betstars.BetstarsSportInitialisation;
import ats.algo.sport.football.FootballMatchRunner;

/**
 * Matchrunner which uses the Betstars tennis model
 */
public class BetstarsFootballMatchRunner extends FootballMatchRunner {

    public static void main(String[] args) {
        /*
         * set properties for accessing BS external model via MQ server
         */
        BetstarsSportInitialisation.initSoccer();
        init(args);
    }
}
