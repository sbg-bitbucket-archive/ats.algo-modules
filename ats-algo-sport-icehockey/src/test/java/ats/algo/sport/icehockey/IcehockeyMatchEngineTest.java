package ats.algo.sport.icehockey;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.core.markets.Markets;

public class IcehockeyMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        IcehockeyMatchFormat matchFormat = new IcehockeyMatchFormat();
        IcehockeyMatchEngine matchEngine = new IcehockeyMatchEngine(matchFormat);
        IcehockeyMatchParams matchParams = (IcehockeyMatchParams) matchEngine.getMatchParams();
        matchParams.setGoalTotalRate(2, 0);
        matchParams.setGoalSupremacy(2, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        @SuppressWarnings("unused")
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        // assertEquals(0.4, markets.get("AB").get("Home"), 0.01);
        // assertEquals(0.2, markets.get("AB").get("Draw"), 0.01);
        // assertEquals(0.4, markets.get("AB").get("Away"), 0.01);

        matchParams.setGoalTotalRate(1.7847, 0);
        matchParams.setGoalSupremacy(0.80939, 0);
        matchParams.setHomeLoseBoost(0.2393, 0);
        matchParams.setAwayLoseBoost(0.11043, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        // assertEquals(0.605, markets.get("AB").get("Home"), 0.005);
        // assertEquals(0.249, markets.get("AB").get("Draw"), 0.005);
        // assertEquals(0.146, markets.get("AB").get("Away"), 0.005);

    }
}
