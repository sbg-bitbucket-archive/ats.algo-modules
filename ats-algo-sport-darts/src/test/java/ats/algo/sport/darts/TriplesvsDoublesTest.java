package ats.algo.sport.darts;

import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.darts.DartMatchEngine;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchParams;

public class TriplesvsDoublesTest {

    @Test
    public void testTriplesVsDoubles() {
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(5);
        matchFormat.setnLegsOrSetsInMatch(7);
        matchFormat.setDoubleReqdToStart(false);
        DartMatchEngine matchEngine = new DartMatchEngine(matchFormat);
        DartMatchParams matchParams = (DartMatchParams) matchEngine.getMatchParams();
        matchParams.setSkillA(1, 0);
        matchParams.setSkillB(1, 0);
        matchParams.setTriplesVsDoublesA(1, 0);
        matchParams.setTriplesVsDoublesB(1, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());

        /*
         * start with triplesVsDoubles = 1.0 (i.e neutral)
         */
        double pMw = markets.get("FT:ML").get("A");
        double pLw = markets.get("G:ML", "L1.1").get("A");
        Market m180A = markets.get("FT:A:OU180");
        Market m180B = markets.get("FT:B:OU180");
        assertEquals("4.5", m180A.getLineId());
        assertEquals("4.5", m180B.getLineId());
        double p180A = m180A.get("Over");
        double p180B = m180B.get("Over");
        assertEquals(0.5, pMw, 0.025);
        assertEquals(0.5, pLw, 0.025);
        assertEquals(0.557, p180A, 0.025);
        assertEquals(0.557, p180B, 0.025);
        /*
         * now set triples vs doubles for player A
         */
        matchParams.setSkillA(1, 0);
        matchParams.setSkillB(1, 0);
        matchParams.setTriplesVsDoublesA(1.2, 0);
        matchParams.setTriplesVsDoublesB(0.8, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());

        pMw = markets.get("FT:ML").get("A");
        pLw = markets.get("G:ML", "L1.1").get("A");
        m180A = markets.get("FT:A:OU180");
        m180B = markets.get("FT:B:OU180");
        assertEquals("6.5", m180A.getLineId());
        assertEquals("3.5", m180B.getLineId());
        p180A = m180A.get("Over");
        p180B = m180B.get("Over");
        assertEquals(0.75, pMw, 0.025);
        assertEquals(0.56, pLw, 0.025);
        assertEquals(0.47, p180A, 0.025);
        assertEquals(0.48, p180B, 0.025);

    }

}
