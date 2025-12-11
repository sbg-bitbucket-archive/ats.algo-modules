package ats.algo.sport.examplesport;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.examplesport.ExampleMatchEngine;
import ats.algo.sport.examplesport.ExampleMatchFormat;
import ats.algo.sport.examplesport.ExampleMatchParams;

public class ExampleMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        ExampleMatchFormat matchFormat = new ExampleMatchFormat();
        matchFormat.setNoLegsInMatch(3);
        ExampleMatchEngine matchEngine = new ExampleMatchEngine(matchFormat);
        ExampleMatchParams matchParams = (ExampleMatchParams) matchEngine.getMatchParams();
        double p = 0.7;
        matchParams.setProbAWinsLeg(p, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.println(markets.toString());
        double probAWins = p * p * (3 - 2 * p);
        assertEquals(probAWins, markets.get("AB").get("A"), 0.012);
        assertEquals(p * p, markets.get("LB").get("2-0"), 0.012);
        assertEquals("2.5", markets.get("LT").getLineId());
    }


}
