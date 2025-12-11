package ats.algo.sport.snooker;


import org.junit.Test;

import ats.algo.sport.snooker.SnookerMatchEngine;
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.snooker.SnookerMatchParams;

public class SnookerMatchEngineTest {

    @Test
    public void test() {
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        matchFormat.setnFramesInMatch(3);
        SnookerMatchEngine matchEngine = new SnookerMatchEngine(matchFormat);
        SnookerMatchParams matchParams = (SnookerMatchParams) matchEngine.getMatchParams();
        matchEngine.setMatchParams(matchParams);
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
