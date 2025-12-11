package ats.algo.sport.beachvolleyball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchEngine;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchParams;

public class BeachVolleyballMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        matchFormat.setnSetsInMatch(3);
        BeachVolleyballMatchEngine matchEngine = new BeachVolleyballMatchEngine(matchFormat);
        BeachVolleyballMatchParams matchParams = (BeachVolleyballMatchParams) matchEngine.getMatchParams();
        double p = 0.7;
        double p1 = 0.7;
        matchParams.setOnServePctA(p, 0);
        matchParams.setOnServePctB(p1, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // // System.out.println(markets.get("FT:ML").get("A"));
        // // System.out.println(markets.get("FT:ML").get("B"));
        //
        // // System.out.println(markets.get("FT:PSPRD").get("A"));
        // // System.out.println(markets.get("FT:PSPRD").get("B"));

        assertEquals(0.487856, markets.get("FT:ML").get("A"), 0.02);
        assertEquals(0.51214, markets.get("FT:ML").get("B"), 0.02);
        assertEquals(0.492464, markets.get("FT:PSPRD").get("AH"), 0.02);
        assertEquals(0.507536, markets.get("FT:PSPRD").get("BH"), 0.02);

        // assertEquals(p*p, markets.get("LB").get("2-0"), 0.012);
        // assertEquals("2.5", markets.get("LT").getSubType());
    }
}
