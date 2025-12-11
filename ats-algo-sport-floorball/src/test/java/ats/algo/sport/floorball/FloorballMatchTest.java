package ats.algo.sport.floorball;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.floorball.FloorballMatch;
import ats.algo.sport.floorball.FloorballMatchFormat;
import ats.algo.sport.floorball.FloorballMatchParams;
import ats.algo.sport.floorball.FloorballMatchState;

public class FloorballMatchTest {

    @Test
    public void test() {
        MethodName.log();
        FloorballMatchFormat matchFormat = new FloorballMatchFormat();
        FloorballMatchState matchState = new FloorballMatchState(matchFormat);
        FloorballMatchParams matchParams = new FloorballMatchParams();
        matchParams.setGoalToal(9, 0);
        matchParams.setGoalSupremacy(0.5, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        FloorballMatch match = new FloorballMatch(matchFormat, matchState, matchParams);
        match.playMatch();
    }
}
