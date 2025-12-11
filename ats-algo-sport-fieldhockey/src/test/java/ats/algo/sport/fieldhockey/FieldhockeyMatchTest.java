package ats.algo.sport.fieldhockey;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.fieldhockey.FieldhockeyMatch;
import ats.algo.sport.fieldhockey.FieldhockeyMatchFormat;
import ats.algo.sport.fieldhockey.FieldhockeyMatchParams;
import ats.algo.sport.fieldhockey.FieldhockeyMatchState;

public class FieldhockeyMatchTest {

    @Test
    public void test() {
        MethodName.log();
        FieldhockeyMatchFormat matchFormat = new FieldhockeyMatchFormat();
        FieldhockeyMatchState matchState = new FieldhockeyMatchState(matchFormat);
        FieldhockeyMatchParams matchParams = new FieldhockeyMatchParams();
        matchParams.setTotalScoreRate(2, 0);
        matchParams.setSupremacyScoreRate(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        FieldhockeyMatch match = new FieldhockeyMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
