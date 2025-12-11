package ats.algo.sport.ufc;


import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;

public class UfcMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        UfcMatchFormat matchFormat = new UfcMatchFormat();
        UfcMatchEngine matchEngine = new UfcMatchEngine(matchFormat);
        UfcMatchParams matchParams = (UfcMatchParams) matchEngine.getMatchParams();
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        assertEquals(0.75, markets.get("FT:ML").get("A"), 0.01);
        assertEquals(0.25, markets.get("FT:ML").get("B"), 0.01);
        assertEquals(0.44, markets.get("FT:GD").get("No"), 0.01);
        assertEquals(0.56, markets.get("FT:GD").get("Yes"), 0.01);
    }
}
