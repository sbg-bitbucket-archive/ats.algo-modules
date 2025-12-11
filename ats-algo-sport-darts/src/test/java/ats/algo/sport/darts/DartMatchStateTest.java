package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchIncident;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.LegWinResult;
import ats.algo.sport.darts.LegWinResult.LegWinType;

public class DartMatchStateTest {

    @Test
    public void test1() {
        MethodName.log();
        // start with leg based match composes of 5 legs
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(5);
        DartMatchState matchState = new DartMatchState(matchFormat);
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        LegWinResult status = matchState.legWon(TeamId.A);
        assertTrue(status.legWinType == LegWinType.IsSetWinner);
        assertTrue(matchState.getPlayerAScore().legs == 0 && matchState.getPlayerBScore().legs == 0);
        assertTrue(matchState.getPlayerAScore().sets == 1 && matchState.getPlayerBScore().sets == 0);
        assertTrue(matchState.getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.B);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        assertTrue(status.legWinType == LegWinType.IsMatchWinner);
        assertTrue(status.matchScoreSetsOrLegsA == 3 && status.matchScoreSetsOrLegsB == 0);
        matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(6);
        matchState = new DartMatchState(matchFormat);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.B);
        status = matchState.legWon(TeamId.B);
        status = matchState.legWon(TeamId.B);
        assertTrue(status.legWinType == LegWinType.IsMatchDraw);
        assertTrue(status.matchScoreSetsOrLegsA == 3 && status.matchScoreSetsOrLegsB == 3);
        assertTrue(status.matchWinner == TeamId.UNKNOWN);
        DartMatchState msCopy = matchState.copy();
        assertEquals(matchState.getPlayerAScore().legs, msCopy.getPlayerAScore().legs);
        assertEquals(matchState.getPlayerBScore().legs, msCopy.getPlayerBScore().legs);
        assertEquals(matchState.getPlayerAtOcheAtStartOfCurrentLeg(), msCopy.getPlayerAtOcheAtStartOfCurrentLeg());
    }

    @Test
    public void test2() {
        MethodName.log();
        // set based match composes of 5 sets of 3 legs each
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(3);
        matchFormat.setnLegsOrSetsInMatch(5);
        DartMatchState matchState = new DartMatchState(matchFormat);
        LegWinResult status;
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        status = matchState.legWon(TeamId.B);
        status = matchState.legWon(TeamId.A);
        assertTrue(status.legWinType == LegWinType.IsNotMatchOrSetWinner);
        assertTrue(matchState.getPlayerAScore().legs == 1 && matchState.getPlayerBScore().legs == 1);
        assertTrue(matchState.getPlayerAScore().sets == 0 && matchState.getPlayerBScore().sets == 0);
        assertTrue(matchState.getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.A);
        status = matchState.legWon(TeamId.B);
        assertTrue(status.legWinType == LegWinType.IsSetWinner);
        assertTrue(status.lastSetLegsA == 1 && status.lastSetLegsB == 2);
        assertTrue(matchState.getPlayerAScore().legs == 0 && matchState.getPlayerBScore().legs == 0);
        assertTrue(matchState.getPlayerAScore().sets == 0 && matchState.getPlayerBScore().sets == 1);
        assertTrue(matchState.getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.B);
        assertTrue(status.lastSetLegsA == 1 && status.lastSetLegsB == 2);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        status = matchState.legWon(TeamId.A);
        assertTrue(status.legWinType == LegWinType.IsNotMatchOrSetWinner);
        assertTrue(matchState.getPlayerAScore().legs == 1 && matchState.getPlayerBScore().legs == 0);
        assertTrue(matchState.getPlayerAScore().sets == 2 && matchState.getPlayerBScore().sets == 1);
        assertTrue(matchState.getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.A);
        assertFalse(matchState.isMatchCompleted());
        assertNull(matchState.getMatchOutcome());
        // System.out.println(matchState);
        status = matchState.legWon(TeamId.A);
        assertTrue(status.legWinType == LegWinType.IsMatchWinner);
        assertTrue(status.lastSetLegsA == 2 && status.lastSetLegsB == 0);
        assertTrue(status.matchScoreSetsOrLegsA == 3 && status.matchScoreSetsOrLegsB == 1);
        assertTrue(status.matchWinner == TeamId.A);
        assertTrue(matchState.isMatchCompleted());
        assertEquals(TeamId.A, matchState.getMatchOutcome());
    }

    @Test
    public void test3() {
        MethodName.log();
        // leg based match composed of 5 legs
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(11);
        DartMatchState matchState = new DartMatchState(matchFormat);
        LegWinResult status = null;
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        for (int i = 0; i < 5; i++) {
            status = matchState.legWon(TeamId.B);
            status = matchState.legWon(TeamId.A);
        }
        assertTrue(status.legWinType == LegWinType.IsSetWinner);
        status = matchState.legWon(TeamId.B);
        assertTrue(status.legWinType == LegWinType.IsMatchWinner);
    }

    @Test
    public void testLegStateUpdates() {
        MethodName.log();
        DartMatchFormat matchFormat = new DartMatchFormat();
        matchFormat.setnLegsPerSet(1);
        matchFormat.setnLegsOrSetsInMatch(1);
        DartMatchState matchState = new DartMatchState(matchFormat);
        matchState.setPlayerAtOcheAtStartOfCurrentLeg(TeamId.A);
        LegState leg = matchState.getCurrentLeg();
        leg.playerA.setPoints(80);
        leg.playerB.setPoints(40);
        matchState.setCurrentLeg(leg);

        MatchIncident matchIncident = new DartMatchIncident(0, 1, 10);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        matchState.updateStateForIncident(matchIncident, false);
        leg = matchState.getCurrentLeg();
        assertEquals(50, leg.playerA.getPoints());
        assertEquals(40, leg.playerB.getPoints());
        assertEquals(TeamId.B, leg.playerAtOche);
        //
        // test going bust
        //
        matchIncident = new DartMatchIncident(0, 2, 16);
        matchState.updateStateForIncident(matchIncident, false);
        leg = matchState.getCurrentLeg();
        assertEquals(50, leg.playerA.getPoints());
        assertEquals(8, leg.playerB.getPoints());
        assertEquals(TeamId.B, leg.playerAtOche);
        assertEquals(1, leg.getThreeDartSet().noDartsThrown);
        matchIncident = new DartMatchIncident(0, 1, 10);
        matchState.updateStateForIncident(matchIncident, false);
        assertEquals(50, leg.playerA.getPoints());
        assertEquals(40, leg.playerB.getPoints());
        assertEquals(TeamId.A, leg.playerAtOche);
        assertEquals(0, leg.getThreeDartSet().noDartsThrown);
    }
}
