package ats.algo.sport.rollerhockey;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.sport.rollerhockey.RollerhockeyMatchEngine;
import ats.algo.sport.rollerhockey.RollerhockeyMatchFormat;
import ats.algo.sport.rollerhockey.RollerhockeyMatchParams;

public class RollerhockeyMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        RollerhockeyMatchFormat matchFormat = new RollerhockeyMatchFormat();
        RollerhockeyMatchEngine matchEngine = new RollerhockeyMatchEngine(matchFormat);
        RollerhockeyMatchParams matchParams = (RollerhockeyMatchParams) matchEngine.getMatchParams();
        matchParams.setGoalTotal(2, 0);
        matchParams.setGoalSupremacy(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        // Markets markets = matchEngine.getCalculatedMarkets();
        // // System.out.print(markets.toString());
        // assertEquals(0.4, markets.get("AXB").get("A"), 0.01);
        // assertEquals(0.2, markets.get("AXB").get("D"), 0.01);
        // assertEquals(0.4, markets.get("AXB").get("B"), 0.01);
        //
        // matchParams.setGoalTotal(1.7847, 0);
        // matchParams.setGoalSupremacy(0.80939, 0);
        // matchParams.setHomeLoseBoost(0.2393, 0);
        // matchParams.setAwayLoseBoost(0.11043, 0);
        // matchEngine.setMatchParams(matchParams);
        // markets = matchEngine.calculate();
        // // System.out.print(markets.toString());
        // assertEquals(0.605, markets.get("AXB").get("A"), 0.005);
        // assertEquals(0.249, markets.get("AXB").get("D"), 0.005);
        // assertEquals(0.146, markets.get("AXB").get("B"), 0.005);

    }
}
