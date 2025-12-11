package ats.algo.sport.handball;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;

import ats.algo.core.markets.Markets;

public class HandballMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        HandballMatchEngine matchEngine = new HandballMatchEngine(matchFormat);
        HandballMatchParams matchParams = (HandballMatchParams) matchEngine.getMatchParams();
        matchParams.setTotalScoreRate(2, 0);
        matchParams.setSupremacyScoreRate(2, 0);
        // matchParams.setHomeLoseBoost(0.0, 0);
        // matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        @SuppressWarnings("unused")
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        // // System.out.println(markets.get("FT:AXB"));
        // assertEquals(0.34, markets.get("FT:AXB").get("A"), 0.01);
        // assertEquals(0.33, markets.get("FT:AXB").get("Draw"), 0.01);
        // assertEquals(0.33, markets.get("FT:AXB").get("B"), 0.01);

        matchParams.setTotalScoreRate(1.7847, 0);
        matchParams.setSupremacyScoreRate(0.80939, 0);
        // matchParams.setHomeLoseBoost(0.2393, 0);
        // matchParams.setAwayLoseBoost(0.11043, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        // // System.out.print(markets.toString());
        // assertEquals(0.58, markets.get("FT:AXB").get("A"), 0.005);
        // assertEquals(0.33, markets.get("FT:AXB").get("Draw"), 0.005);
        // assertEquals(0.09, markets.get("FT:AXB").get("B"), 0.005);

    }
}
