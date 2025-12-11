package ats.algo.sport.snooker;


import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.snooker.SnookerMatchEngine;
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.snooker.SnookerMatchParams;

public class SnookerMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        matchFormat.setnFramesInMatch(3);
        SnookerMatchEngine matchEngine = new SnookerMatchEngine(matchFormat);
        SnookerMatchParams matchParams = (SnookerMatchParams) matchEngine.getMatchParams();
        matchParams.setOnFramePctA(new Gaussian(0.58, 0.05));
        matchParams.setOnCurrentFramePctA(new Gaussian(0.4, 0.05));
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        assertEquals(0.531, markets.get("FT:ML").get("A"), 0.01);
        assertEquals(0.468, markets.get("FT:ML").get("B"), 0.01);
    }
}
