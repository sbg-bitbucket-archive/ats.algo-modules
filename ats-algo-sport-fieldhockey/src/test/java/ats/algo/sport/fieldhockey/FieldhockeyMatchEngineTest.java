package ats.algo.sport.fieldhockey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.fieldhockey.FieldhockeyMatchEngine;
import ats.algo.sport.fieldhockey.FieldhockeyMatchFormat;
import ats.algo.sport.fieldhockey.FieldhockeyMatchParams;

public class FieldhockeyMatchEngineTest {

    @Test
    public void test() {
        FieldhockeyMatchFormat matchFormat = new FieldhockeyMatchFormat();
        FieldhockeyMatchEngine matchEngine = new FieldhockeyMatchEngine(matchFormat);
        FieldhockeyMatchParams matchParams = (FieldhockeyMatchParams) matchEngine.getMatchParams();
        matchParams.setTotalScoreRate(4, 0);
        matchParams.setSupremacyScoreRate(0, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(0.47, markets.get("FT:AXB").get("A"), 0.01);
        assertEquals(0.07, markets.get("FT:AXB").get("Draw"), 0.01);
        assertEquals(0.46, markets.get("FT:AXB").get("B"), 0.01);

        matchParams.setTotalScoreRate(1.7847 + 0.80939, 0);
        matchParams.setSupremacyScoreRate(1.7847 - 0.80939, 0);
        matchParams.setHomeLoseBoost(0.2393, 0);
        matchParams.setAwayLoseBoost(0.11043, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.get("FT:AXB").get("B"));
        assertEquals(0.738656, markets.get("FT:AXB").get("A"), 0.01);
        assertEquals(0.061104, markets.get("FT:AXB").get("Draw"), 0.01);
        assertEquals(0.204224, markets.get("FT:AXB").get("B"), 0.01);
    }
}
