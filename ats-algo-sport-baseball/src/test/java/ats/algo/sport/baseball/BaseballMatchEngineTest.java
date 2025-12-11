package ats.algo.sport.baseball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.baseball.BaseballMatchEngine;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.baseball.BaseballMatchParams;


public class BaseballMatchEngineTest {


    @Test
    public void test() {
        MethodName.log();
        BaseballMatchFormat matchFormat = new BaseballMatchFormat();
        BaseballMatchEngine matchEngine = new BaseballMatchEngine(matchFormat);
        BaseballMatchParams matchParams = (BaseballMatchParams) matchEngine.getMatchParams();
        matchParams.setTotalRuns(new Gaussian(13.0, 1));
        matchParams.setSupremacy(new Gaussian(2.5, 0.5));
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.get("FT:ML"));
        assertEquals(0.710, markets.get("FT:ML").get("A"), 0.01);
        assertEquals(0.290, markets.get("FT:ML").get("B"), 0.01);
    }
}
