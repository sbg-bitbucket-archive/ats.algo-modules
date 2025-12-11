package ats.algo.sport.rugbyunion;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchParams;
import ats.algo.sport.rugbyunion.RugbyUnionMatchState;
import ats.algo.sport.rugbyunion.RugbyUnionMatch;

public class RugbyUnionMatchTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        RugbyUnionMatchState matchState = new RugbyUnionMatchState(matchFormat);
        RugbyUnionMatchParams matchParams = new RugbyUnionMatchParams();
        matchParams.setScoreTotal(58.0, 5);
        matchParams.setScoreSupremacy(5.5, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        RugbyUnionMatch match = new RugbyUnionMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
