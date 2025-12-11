package ats.algo.sport.football;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.core.util.json.JsonUtil;

public class FootballAsianMarketTest {

    @Test
    public void testAsianMarket() {
        MethodName.log();
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
        Market asianMarket = markets.getMarketForFullKey("FT:AHCP_S0-0");
        Market dnbMarket = markets.getMarketForFullKey("FT:DNB_M");

        // Market marketA1 = asianMarket.getMarketForLineId("-1");
        // Market marketA2 = asianMarket.getMarketForLineId("-0.75");
        Market marketA3 = asianMarket.getMarketForLineId("-0.25");
        Market marketA4 = asianMarket.getMarketForLineId("-0.5");
        Market marketA5 = asianMarket.getMarketForLineId("-0.0");
        // System.out.println(dnbMarket.getSelectionsProbs());
        // System.out.println(marketA5.getSelectionsProbs());
        // asian handicap market 0.0 equals dnb market
        assertEquals(dnbMarket.getSelectionsProbs().get("A"), marketA5.getSelectionsProbs().get("AH"), 0.01);
        // System.out.println(marketA1.getSelectionsProbs());
        // System.out.println(marketA2.getSelectionsProbs());
        // System.out.println(marketA3.getSelectionsProbs());
        // System.out.println(marketA4.getSelectionsProbs());
        // quaterl line equals half of sum of full line and half line
        assertEquals((marketA5.getSelectionsProbs().get("AH") + marketA4.getSelectionsProbs().get("AH")) / 2,
                        marketA3.getSelectionsProbs().get("AH"), 0.01);
    }

    @Test
    public void testResultingBABMarket() {
        MethodName.log();
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

        // Market m1 = markets.getMarketForFullKey("P:CS_H1");

        String marketstring =
                        "{\"Market\":{\"valid\":true,\"type\":\"FT:BAB\",\"lineId\":\"\",\"marketDescription\":\"FA,H1-0,C8-0\",\"sequenceId\":\"M\",\"selections\":{\"No\":{\"suspensionStatus\":\"OPEN\",\"prob\":0.29642},\"Yes\":{\"suspensionStatus\":\"OPEN\",\"prob\":0.70358}},\"marketGroup\":\"GOALS\",\"marketStatus\":{\"suspensionStatus\":\"OPEN\",\"suspensionStatusRuleName\":\"Default\",\"suspensionStatusReason\":\"Default\"},\"doNotResultThisMarket\":false,\"filterOutThisMarketForClient\":false,\"lineBase\":0,\"category\":\"GENERAL\"}}";
        Market marketBab = JsonUtil.unmarshalJson(marketstring, Market.class);

        markets.addMarketWithFullKey(marketBab);
        FootballMatchState previousMatchState = new FootballMatchState();
        FootballMatchState currentMatchState = new FootballMatchState();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        currentMatchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        FootballMatchIncident footballMatchIncident = new FootballMatchIncident();
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 30, TeamId.A);
        currentMatchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        matchEngine.resultMarkets(markets, previousMatchState, currentMatchState);
    }


}
