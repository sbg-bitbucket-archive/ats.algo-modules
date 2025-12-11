package ats.algo.sport.bandy;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.bandy.BandyMatch;
import ats.algo.sport.bandy.BandyMatchFormat;
import ats.algo.sport.bandy.BandyMatchParams;
import ats.algo.sport.bandy.BandyMatchState;

public class BandyMatchTest {

    @Test
    public void test() {
        MethodName.log();
        BandyMatchFormat matchFormat = new BandyMatchFormat();
        BandyMatchState matchState = new BandyMatchState(matchFormat);
        BandyMatchParams matchParams = new BandyMatchParams();
        matchParams.setTotalScoreRate(2, 0);
        matchParams.setSupremacyScoreRate(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        BandyMatch match = new BandyMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
