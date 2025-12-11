package ats.algo.sport.bowls;

import org.junit.Test;

import ats.algo.sport.bowls.BowlsMatchEngine;
import ats.algo.sport.bowls.BowlsMatchFormat;
import ats.algo.sport.bowls.BowlsMatchParams;

public class BowlsMatchEngineTest {

    @Test
    public void test() {
        BowlsMatchFormat matchFormat = new BowlsMatchFormat();
        matchFormat.setnSetsInMatch(3);
        BowlsMatchEngine matchEngine = new BowlsMatchEngine(matchFormat);
        BowlsMatchParams matchParams = (BowlsMatchParams) matchEngine.getMatchParams();
        double p = 0.7;
        double p1 = 0.7;
        matchParams.setOnEndPctA(p, 0);
        matchParams.setOnEndPctB(p1, 0);
        matchEngine.setMatchParams(matchParams);
        // Markets markets = matchEngine.calculate();
        // System.out.println(markets.get("FT:ML").get("A"));
        // System.out.println(markets.get("FT:ML").get("B"));
        //
        // System.out.println(markets.get("FT:PSPRD").get("A"));
        // System.out.println(markets.get("FT:PSPRD").get("B"));

        // assertEquals(0.487856, markets.get("FT:ML").get("A"), 0.02);
        // assertEquals(0.51214, markets.get("FT:ML").get("B"), 0.02);
        // assertEquals(0.492464, markets.get("FT:PSPRD").get("A"), 0.02);
        // assertEquals(0.507536, markets.get("FT:PSPRD").get("B"), 0.02);

        // assertEquals(p*p, markets.get("LB").get("2-0"), 0.012);
        // assertEquals("2.5", markets.get("LT").getSubType());
    }
}
