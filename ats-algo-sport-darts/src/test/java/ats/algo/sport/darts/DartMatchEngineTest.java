package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MatchEngineThreadPool;
import ats.algo.sport.darts.DartMatchEngine;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchIncident;
import ats.algo.sport.darts.DartMatchIncidentResult;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.DartMatchIncidentResult.DartMatchIncidentOutcome;

public class DartMatchEngineTest {

    static final int nIterations = 30000;



    @Test
    public void dartMatchEngineTest() {
        MethodName.log();
        runDartMatchEngineTest(4);
    }

    void runDartMatchEngineTest(int nThreads) {
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(5);
        matchFormat.setnLegsOrSetsInMatch(9);
        DartMatchEngine matchEngine = new DartMatchEngine(matchFormat);
        matchEngine.initialiseMatchEngine(nThreads, new MatchEngineThreadPool(nThreads - 1));
        DartMatchState matchState = (DartMatchState) matchEngine.getMatchState();
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        matchEngine.setMatchState(matchState);
        DartMatchParams matchParams = (DartMatchParams) matchEngine.getMatchParams();
        matchParams.setSkillA(1.2574, 0);
        matchParams.setSkillB(1.2574, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        assertEquals(markets.get("FT:ML").get("A"), 0.52, 0.015);
        assertEquals(markets.get("P:ML", "S1").get("A"), 0.58, 0.015);
        assertEquals(markets.get("P:ML", "S2").get("A"), 0.48, 0.015);
        assertEquals(markets.get("G:ML", "L1.2").get("A"), 0.31, 0.015);
        assertEquals(markets.get("FT:CS").get("2-5"), .11, 0.015);
        assertEquals(markets.get("P:CS", "S2").get("2-3"), .23, 0.015);
        assertEquals(markets.get("FT:B:OUS").get("Over"), .48, 0.015);
        assertEquals(markets.get("FT:M180").get("Neither"), .1, 0.015);
        assertEquals(markets.get("FT:A:HTRK").get("Yes"), .14, 0.03);
    }

    /**
     * tests to see that no 180s dealt with correctly at end of leg
     */
    @Test
    public void test180sRollover() {
        MethodName.log();
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(3);
        matchFormat.setnLegsOrSetsInMatch(5);
        DartMatchEngine matchEngine = new DartMatchEngine(matchFormat);
        DartMatchState matchState = (DartMatchState) matchEngine.getMatchState();
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        LegState leg = matchState.getCurrentLeg();
        leg.playerA.setPoints(200);
        leg.playerB.setPoints(200);
        matchState.setCurrentLeg(leg);
        matchEngine.setMatchState(matchState);
        DartMatchParams matchParams = (DartMatchParams) matchEngine.getMatchParams();
        matchParams.setSkillA(1.1, 0);
        matchParams.setSkillB(1.1, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        assertEquals("2.5", (markets.get("FT:A:OU180").getLineId()));
        assertEquals("2.5", (markets.get("FT:B:OU180").getLineId()));
        MatchIncident matchIncident = new DartMatchIncident(0, 3, 20);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        matchIncident = new DartMatchIncident(0, 1, 17);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        matchIncident = new DartMatchIncident(0, 2, 10);
        DartMatchIncidentResult eventResult =
                        (DartMatchIncidentResult) matchState.updateStateForIncident(matchIncident, false);
        assertEquals(DartMatchIncidentOutcome.LEGWONA, eventResult.getDartMatchEventOutcome());
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        markets = matchEngine.getCalculatedMarkets();
        // System.out.print(markets.toString());
        assertEquals("3.5", (markets.get("FT:A:OU180")).getLineId());
        assertEquals("2.5", (markets.get("FT:B:OU180")).getLineId());
    }

}
