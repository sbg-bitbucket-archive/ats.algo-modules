package ats.algo.sport.football;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.football.FootballMatchEngine;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;

public class FootballMatchEngineTest {

    @Test
    public void test() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchEngine matchEngine = new FootballMatchEngine(matchFormat);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        matchParams.setGoalTotal(4, 0);
        matchParams.setGoalSupremacy(0, 0);
        matchParams.setHomeLoseBoost(0.0, 0);
        matchParams.setAwayLoseBoost(0.0, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(0.415, markets.get("FT:AXB").get("A"), 0.015);
        System.out.println(markets.get("FT:AXB").get("X"));
        assertEquals(0.1840, markets.get("FT:AXB").get("X"), 0.015);
        // System.out.println(markets.get("FT:AXB").get("B")+markets.get("FT:AXB").get("Draw")+markets.get("FT:AXB").get("A"));
        assertEquals(0.400, markets.get("FT:AXB").get("B"), 0.015);

        matchParams.setGoalTotal(1.7847 + 0.80939, 0);
        matchParams.setGoalSupremacy(1.7847 - 0.80939, 0);
        matchParams.setHomeLoseBoost(0.2393, 0);
        matchParams.setAwayLoseBoost(0.11043, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.get("FT:AXB").get("A"));
        assertEquals(0.5886, markets.get("FT:AXB").get("A"), 0.005);
        assertEquals(0.221, markets.get("FT:AXB").get("X"), 0.005);
        assertEquals(0.1889, markets.get("FT:AXB").get("B"), 0.005);
    }

}
