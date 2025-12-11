package ats.algo.sport.rugbyleague;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchEngine;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchFormat;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchParams;

public class RugbyLeagueMatchEngineTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        RugbyLeagueMatchEngine matchEngine = new RugbyLeagueMatchEngine(matchFormat);
        RugbyLeagueMatchParams matchParams = (RugbyLeagueMatchParams) matchEngine.getMatchParams();
        matchParams.setScoreTotal(58.0, 5);
        matchParams.setScoreSupremacy(5.5, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.get("FT:AXB"));
        assertEquals(0.675, markets.get("FT:AXB").get("A"), 0.01);
        assertEquals(0.032, markets.get("FT:AXB").get("Draw"), 0.01);
        assertEquals(0.293, markets.get("FT:AXB").get("B"), 0.01);

    }
}
