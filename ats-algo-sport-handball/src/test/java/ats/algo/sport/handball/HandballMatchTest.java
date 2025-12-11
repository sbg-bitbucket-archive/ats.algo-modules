package ats.algo.sport.handball;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.handball.HandballMatch;
import ats.algo.sport.handball.HandballMatchFormat;
import ats.algo.sport.handball.HandballMatchParams;
import ats.algo.sport.handball.HandballMatchState;

public class HandballMatchTest {

    @Test
    public void test() {
        MethodName.log();
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        HandballMatchState matchState = new HandballMatchState(matchFormat);
        HandballMatchParams matchParams = new HandballMatchParams();
        matchParams.setTotalScoreRate(2, 0);
        matchParams.setSupremacyScoreRate(2, 0);
        // matchParams.setHomeLoseBoost(0.0, 0);
        // matchParams.setAwayLoseBoost(0.0, 0);
        HandballMatch match = new HandballMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
