package ats.algo.sport.bandy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.bandy.BandyMatchEngine;
import ats.algo.sport.bandy.BandyMatchFormat;
import ats.algo.sport.bandy.BandyMatchParams;

public class BandyMatchEngineTest {

    @Test
    public void test() {
        BandyMatchFormat matchFormat = new BandyMatchFormat();
        BandyMatchEngine matchEngine = new BandyMatchEngine(matchFormat);
        BandyMatchParams matchParams = (BandyMatchParams) matchEngine.getMatchParams();
        matchParams.setTotalScoreRate(4, 0);
        matchParams.setSupremacyScoreRate(0, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(0.479, markets.get("FT:AXB").get("A"), 0.01);
        // assertEquals(0.05, markets.get("AB").get("Draw"), 0.01);
        // assertEquals(0.48, markets.get("AB").get("Away"), 0.01);

        matchParams.setTotalScoreRate(8.5, 0.85);
        matchParams.setSupremacyScoreRate(0.5, 0.85);
        matchParams.setHomeLoseBoost(0.05, 0);
        matchParams.setAwayLoseBoost(0.05, 0);
        matchParams.setPowerBoostRate(0.150, 0.005);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(0.54, markets.get("FT:AXB").get("A"), 0.01);
        assertEquals(0.40, markets.get("FT:AXB").get("B"), 0.01);
        assertEquals(0.06, markets.get("FT:AXB").get("D"), 0.01);

    }
}
