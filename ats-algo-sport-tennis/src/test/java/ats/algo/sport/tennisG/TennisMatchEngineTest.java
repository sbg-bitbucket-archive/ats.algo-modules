package ats.algo.sport.tennisG;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchIncident;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchState;
import ats.algo.sport.tennisG.TennisGMatchEngine;
import ats.algo.sport.tennisG.TennisGMatchFormat.Sex;
import ats.algo.sport.tennisG.TennisGMatchFormat.Surface;
import ats.algo.sport.tennisG.TennisGMatchFormat.TournamentLevel;
import ats.algo.sport.tennisG.TennisGMatchIncident.TennisMatchIncidentType;

public class TennisMatchEngineTest {
    @Test
    public void testSingleThread() {
        testCommon(1);
    }

    @Test
    public void testMultipleThreads() {
        testCommon(4);
    }

    void testCommon(int nThreads) {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 3, true);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.55, 0.0);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(.74, markets.get("FT:ML").get("A"), 0.012);
        assertEquals(.45, markets.get("FT:CS").get("2-0"), 0.012);
        assertEquals("22.5", markets.get("FT:OU").getLineId());

        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.55, .113137);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(.64, markets.get("FT:ML").get("A"), 0.012);
        assertEquals(.46, markets.get("FT:CS").get("2-0"), 0.012);
        assertEquals("20.5", markets.get("FT:OU").getLineId());
        /*
         * test unknown server
         */
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.6, 0.0);
        matchState.setScore(0, 0, 0, 0, TeamId.UNKNOWN);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        assertEquals(.5, markets.get("FT:ML").get("A"), 0.012);

    }

    @Test
    public void testAdvantageMatch() {
        /*
         * test 5 set advantage match
         */
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 5, false);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.55, 0.0);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // markets.print();
        assertEquals(.796, markets.get("FT:ML").get("A"), 0.008);
        assertEquals(.199, markets.get("FT:CS").get("3-2"), 0.008);
        assertEquals("38.5", markets.get("FT:OU").getLineId());
    }

    @Test
    public void testMarketsPreMatch() {

        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 3, true);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.55, 0.0);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        assertEquals(.75, markets.get("FT:ML").get("A"), 0.015);
        assertEquals(.15, markets.get("FT:CS").get("1-2"), 0.015);
        assertEquals("22.5", markets.get("FT:OU").getLineId());
        assertEquals(.51, markets.get("FT:OU").get("Over"), 0.015);
        assertEquals(.11, markets.get("FT:W1S:A").get("No"), 0.015);
        assertEquals(null, markets.get("G:ML"));

    }

    @Test
    public void testMarketsInPlay1() {
        /*
         * test when score is beginning of first set
         */
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(Sex.MEN, Surface.HARD, TournamentLevel.ATP, 3, true);
        TennisGMatchEngine matchEngine = new TennisGMatchEngine(matchFormat);
        TennisGMatchState matchState = (TennisGMatchState) matchEngine.getMatchState();
        TennisGMatchParams matchParams = (TennisGMatchParams) matchEngine.getMatchParams();
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        matchParams.setOnServePctA(.6, 0.0);
        matchParams.setOnServePctB(.55, 0.0);
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);

        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        assertEquals(.75, markets.get("FT:ML").get("A"), 0.015);
        assertEquals(.11, markets.get("FT:W1S:A").get("No"), 0.015);
        assertEquals(.74, markets.get("G:ML", "S1.1").get("A"), 0.015);
        assertEquals(.15, markets.get("FT:CS").get("1-2"), 0.015);
        assertEquals(.21, markets.get("G:CS", "S1.1").get("4-1"), 0.015);
        assertEquals("22.5", markets.get("FT:OU").getLineId());
        assertEquals(.51, markets.get("FT:OU").get("Over"), 0.015);
        assertEquals(null, markets.get("P:TBCS", "S1"));
        Market market = markets.get("G:PW", "S1.1.3");
        // System.out.print(market.toString());
        assertEquals("S1.1.3", market.getSequenceId());
        TennisGMatchIncident incident = new TennisGMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        matchState.updateStateForIncident(incident, false);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        market = markets.get("G:PW", "S1.1.4");
        assertEquals("S1.1.4", market.getSequenceId());
        /*
         * check tie break markets
         */
        matchState.setScore(0, 1, 5, 6, TeamId.A);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(null, markets.get("FT:W1S:B")); // B has already won a set
        assertEquals(.42, markets.get("FT:W1S:A").get("Yes"), 0.015);
        assertEquals(null, markets.get("P:TBCS"));
        assertEquals(.74, markets.get("FT:TBIM").get("Yes"), 0.015);
        matchState.updateStateForIncident(incident, false);
        matchState.updateStateForIncident(incident, false);
        matchState.updateStateForIncident(incident, false);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        System.out.print(markets.toString());
        assertEquals(null, markets.get("TBSCS"));
        assertEquals(.74, markets.get("FT:TBIM").get("Yes"), 0.015);

    }



}
