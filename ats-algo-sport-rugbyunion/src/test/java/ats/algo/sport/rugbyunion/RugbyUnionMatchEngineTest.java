package ats.algo.sport.rugbyunion;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchParams;
import ats.algo.sport.rugbyunion.RugbyUnionMatchEngine;

public class RugbyUnionMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        RugbyUnionMatchEngine matchEngine = new RugbyUnionMatchEngine(matchFormat);
        RugbyUnionMatchParams matchParams = (RugbyUnionMatchParams) matchEngine.getMatchParams();
        matchParams.setScoreTotal(58.0, 5);
        matchParams.setScoreSupremacy(5.5, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.get("FT:AXB"));
        assertEquals(0.627, markets.get("FT:AXB").get("A"), 0.01);
        assertEquals(0.027, markets.get("FT:AXB").get("Draw"), 0.01);
        assertEquals(0.345, markets.get("FT:AXB").get("B"), 0.01);

    }
}
