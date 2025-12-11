package ats.algo.ladbrokescoral.football;

import ats.algo.ladbrokescoral.LadbrokesCoralSportInitialisation;
import ats.algo.sport.football.FootballMatchRunner;

/**
 * Matchrunner which uses the Betstars tennis model
 */
public class LadbrokesCoralFootballMatchRunner extends FootballMatchRunner {

    public static void main(String[] args) {
        /*
         * set properties for accessing BS external model via MQ server
         */
        LadbrokesCoralSportInitialisation.initSoccer();
        init(args);
    }
}
