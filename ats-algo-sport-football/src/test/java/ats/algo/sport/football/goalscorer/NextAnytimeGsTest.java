package ats.algo.sport.football.goalscorer;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.PlayerStatus;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class NextAnytimeGsTest {

    private TeamSheet initialTeamSheet() {
        TeamSheet teamSheet = new TeamSheet();
        teamSheet.addPlayer(TeamId.A, "George Best", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.A, "Gary Lineker", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Ronaldo", PlayerStatus.PLAYING);

        teamSheet.addPlayer(TeamId.B, "Wayne Rooney", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Bobby Charlton", PlayerStatus.PLAYING);
        teamSheet.addPlayer(TeamId.B, "Didier Drogba", PlayerStatus.ON_THE_BENCH);
        return teamSheet;
    }

    private TeamSheet updateTeamSheet() {
        TeamSheet teamSheet = new TeamSheet();
        teamSheet.addPlayer(TeamId.A, "George Best", PlayerStatus.ON_THE_BENCH);
        teamSheet.addPlayer(TeamId.A, "Gary Lineker", PlayerStatus.PLAYING);
        return teamSheet;
    }



    private TeamSheetMatchIncident generateMatchIncidentTeamSheet() {
        return TeamSheetMatchIncident.generateMatchIncidentForInitialTeamSheet(initialTeamSheet());
    }


    private FootballMatchParams testMatchParams() {
        FootballMatchParams matchParams = new FootballMatchParams(new FootballMatchFormat());
        GenericMatchParams genericParams = matchParams.generateGenericMatchParams();
        genericParams.updatePlayerMatchParams(initialTeamSheet());
        FootballMatchParams matchParams2 = (FootballMatchParams) genericParams.generateXxxMatchParams();
        Map<String, MatchParam> individualParams = matchParams2.getIndividualParams();
        individualParams.get("A.George Best").getGaussian().setMean(1.282);
        individualParams.get("A.Gary Lineker").getGaussian().setMean(3.333);
        individualParams.get("A.Ronaldo").getGaussian().setMean(5.385);
        individualParams.get("B.Wayne Rooney").getGaussian().setMean(5.294);
        individualParams.get("B.Bobby Charlton").getGaussian().setMean(1.765);
        individualParams.get("B.Didier Drogba").getGaussian().setMean(2.941);
        return matchParams2;
    }



    private FootballMatchState preMatchTestMatchState() {
        FootballMatchState matchState = new FootballMatchState();
        TeamSheetMatchIncident incident = generateMatchIncidentTeamSheet();
        matchState.updateStateForIncident(incident, false);
        return matchState;
    }

    private FootballMatchState inPlayTestMatchState() {
        FootballMatchState matchState = preMatchTestMatchState();
        matchState.getTeamSheet().getTeamSheetMap().get("A.Gary Lineker").setPlayerStatus(PlayerStatus.PLAYING);
        MatchIncident matchStartIncident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident(matchStartIncident, false);
        MatchIncident goalScoredIncident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 100, TeamId.B);
        matchState.updateStateForIncident(goalScoredIncident, false);
        MatchIncident goalScorer =
                        PlayerMatchIncident.generateMatchIncidentForGoalScorer(1, TeamId.B, "Bobby Charlton");
        matchState.updateStateForIncident(goalScorer, false);
        System.out.println(matchState);
        return matchState;
    }

    private Market preMatchCsMarket() {
        Market market = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "FT:CS", "M", "Correct score");
        market.setIsValid(true);
        market.put("0-0", .1);
        market.put("0-1", .15);
        market.put("0-2", .3);
        market.put("1-2", .25);
        market.put("2-3", .2);
        return market;
    }

    private Market inPlayCsMarket() {
        Market market = new Market(MarketCategory.GENERAL, MarketGroup.INDIVIDUAL, "FT:CS", "M", "Correct score");
        market.setIsValid(true);
        market.put("0-1", .15);
        market.put("0-2", .35);
        market.put("1-2", .30);
        market.put("2-3", .20);
        return market;
    }

    @Test
    public void testParamsToGeneric() {
        FootballMatchParams matchParams = testMatchParams();
        GenericMatchParams genericMatchParams = matchParams.generateGenericMatchParams();
        FootballMatchParams matchParams2 = (FootballMatchParams) genericMatchParams.generateXxxMatchParams();
        printMatchParams("matchParams", matchParams);
        printMatchParams("matchParams2", matchParams2);
        assertEquals(matchParams, matchParams2);
    }

    @Test
    public void testParamsSetEqualTo() {
        FootballMatchParams matchParams = testMatchParams();
        FootballMatchParams matchParams2 = new FootballMatchParams(new FootballMatchFormat());
        matchParams2.setEqualTo(matchParams);
        System.out.println(matchParams);
        System.out.println(matchParams2);
        assertEquals(matchParams, matchParams2);
    }

    @Test
    public void testMatchStateSetEqualTo() {
        FootballMatchState matchState = preMatchTestMatchState();
        FootballMatchState matchState2 = new FootballMatchState();
        matchState2.setEqualTo(matchState);
        assertEquals(matchState, matchState2);
    }

    private void printMatchParams(String name, MatchParams matchParams) {
        System.out.println(name + ": ");
        for (Entry<String, MatchParam> e : matchParams.getParamMap().entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    @Test
    public void generateJsonExamples() {
        MatchIncident incident = generateMatchIncidentTeamSheet();
        String json = JsonSerializer.serialize(incident, true);
        System.out.println(json);
        incident = PlayerMatchIncident.generateMatchIncidentForGoalScorer(1, TeamId.A, "George Best");
        json = JsonSerializer.serialize(incident, true);
        System.out.println(json);
        incident = TeamSheetMatchIncident.generateMatchIncidentForUpdateTeamSheet(updateTeamSheet());
        json = JsonSerializer.serialize(incident, true);
        System.out.println(json);

    }

    @Test
    public void test() {
        LogUtil.initConsoleLogging(Level.TRACE);

        Market csMarket = preMatchCsMarket();
        FootballMatchState matchState = preMatchTestMatchState();
        FootballMatchParams matchParams = testMatchParams();
        Map<String, Market> markets = NextAnytimeGsCalculator.calculate(csMarket, matchState, matchParams);
        Market nextGsMkt = markets.get("G:NS_G1");
        Market anytimeGsMkt = markets.get("FT:AGS_M");

        System.out.println("Calculated next to score prices:\n");
        for (Entry<String, Double> e : nextGsMkt.getSelectionsProbs().entrySet())
            System.out.printf("%s: %.3f\n", e.getKey(), e.getValue());
        System.out.println("Calculated anytime score prices:\n");
        for (Entry<String, Double> e : anytimeGsMkt.getSelectionsProbs().entrySet())
            System.out.printf("%s: %.3f\n", e.getKey(), e.getValue());
        assertEquals(.031, nextGsMkt.get("A.George Best"), 0.0005);
        assertEquals(.394, anytimeGsMkt.get("B.Bobby Charlton"), 0.0005);
        assertTrue(nextGsMkt.get("A.Gary Lineker") == null);
    }

    @Test
    public void test2() {
        LogUtil.initConsoleLogging(Level.TRACE);
        Market csMarket = inPlayCsMarket();
        FootballMatchState matchState = inPlayTestMatchState();
        FootballMatchParams matchParams = testMatchParams();
        Map<String, Market> markets = NextAnytimeGsCalculator.calculate(csMarket, matchState, matchParams);
        Market nextGsMkt = markets.get("G:NS_G2");
        Market anytimeGsMkt = markets.get("FT:AGS_M");

        System.out.print("Calculated next to score prices:\n");
        for (Entry<String, Double> e : nextGsMkt.getSelectionsProbs().entrySet())
            System.out.printf("%s: %.3f\n", e.getKey(), e.getValue());
        System.out.print("\nCalculated anytime score prices:\n");
        for (Entry<String, Double> e : anytimeGsMkt.getSelectionsProbs().entrySet())
            System.out.printf("%s: %.3f\n", e.getKey(), e.getValue());
        assertEquals(.032, nextGsMkt.get("A.George Best"), 0.0005);
        assertEquals(.319, anytimeGsMkt.get("A.Ronaldo"), 0.0005);
        assertTrue(nextGsMkt.get("A.Gary Lineker") != null);
        assertTrue(anytimeGsMkt.get("B.Bobby Charlton") == null); // already scored
    }



}
