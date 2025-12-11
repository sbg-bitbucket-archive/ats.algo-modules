package ats.algo.sport.rollerhockey;

import org.junit.Test;

import ats.algo.sport.rollerhockey.RollerhockeyMatch;
import ats.algo.sport.rollerhockey.RollerhockeyMatchFormat;
import ats.algo.sport.rollerhockey.RollerhockeyMatchParams;
import ats.algo.sport.rollerhockey.RollerhockeyMatchState;

public class RollerhockeyMatchTest {

    @Test
    public void test() {
        RollerhockeyMatchFormat matchFormat = new RollerhockeyMatchFormat();
        RollerhockeyMatchState matchState = new RollerhockeyMatchState(matchFormat);
        RollerhockeyMatchParams matchParams = new RollerhockeyMatchParams();
        matchParams.setGoalTotal(2, 0);
        matchParams.setGoalSupremacy(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        RollerhockeyMatch match = new RollerhockeyMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
