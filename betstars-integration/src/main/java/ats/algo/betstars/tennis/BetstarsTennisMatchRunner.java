package ats.algo.betstars.tennis;

import ats.algo.betstars.BetstarsSportInitialisation;
import ats.algo.sport.tennis.TennisMatchRunner;

/**
 * Matchrunner which uses the Betstars tennis model
 */
public class BetstarsTennisMatchRunner extends TennisMatchRunner {

    public static void main(String[] args) {
        BetstarsSportInitialisation.initTennis();
        init(args);
    }
}
