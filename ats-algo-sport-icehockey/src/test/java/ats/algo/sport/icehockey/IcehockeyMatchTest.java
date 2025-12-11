package ats.algo.sport.icehockey;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.icehockey.IcehockeyMatch;
import ats.algo.sport.icehockey.IcehockeyMatchFormat;
import ats.algo.sport.icehockey.IcehockeyMatchParams;
import ats.algo.sport.icehockey.IcehockeyMatchState;

public class IcehockeyMatchTest {

    @Test
    public void test() {
        MethodName.log();
        IcehockeyMatchFormat matchFormat = new IcehockeyMatchFormat();
        IcehockeyMatchState matchState = new IcehockeyMatchState(matchFormat);
        IcehockeyMatchParams matchParams = new IcehockeyMatchParams();
        matchParams.setGoalTotalRate(2, 0);
        matchParams.setGoalSupremacy(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        IcehockeyMatch match = new IcehockeyMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
