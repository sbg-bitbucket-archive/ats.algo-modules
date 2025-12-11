package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartMatchFormat.DartMatchLevel;

/**
 * verify effect of new incidents on DartMatchState
 * 
 * @author gicha
 *
 */
public class DartMatchStateTest2 {

    @Test
    public void scoreMatchIncidentTest() {
        MethodName.log();
        executeTest(false);
        executeTest(true);
    }

    /**
     * checks that score type matchIncidents correctly update matchState. to deal with different feeds there are two
     * different ways in which end of leg can be signified - either receiving a score of 0 for one of the players or
     * receiving a score of 501. The boolean param lets both options be tested
     * 
     * @param endLegScore501
     */
    private void executeTest(boolean endLegScore501) {
        DartMatchState matchState = new DartMatchState();
        matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(381, 501), false);
        assertEquals(381, matchState.getCurrentLeg().getPlayerA().getPoints());
        assertEquals(501, matchState.getCurrentLeg().getPlayerB().getPoints());
        assertEquals(TeamId.A, matchState.getPlayerAtOcheAtStartOfCurrentLeg());
        matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(381, 321), false);
        assertEquals(381, matchState.getCurrentLeg().getPlayerA().getPoints());
        assertEquals(321, matchState.getCurrentLeg().getPlayerB().getPoints());
        assertEquals(0, matchState.getCurrentLeg().getPlayerA().getN180sThrown());
        assertEquals(1, matchState.getCurrentLeg().getPlayerB().getN180sThrown());
        matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(211, 321), false);
        matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(211, 141), false);
        assertEquals(0, matchState.getCurrentLeg().getPlayerA().getN180sThrown());
        assertEquals(2, matchState.getCurrentLeg().getPlayerB().getN180sThrown());
        matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(200, 141), false);
        if (endLegScore501)
            matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(501, 501), false);
        else
            matchState.updateStateForIncident(DartMatchIncident.generateUpdatedScoreIncident(200, 0), false);
        assertEquals(0, matchState.getPlayerAScore().getLegs());
        assertEquals(0, matchState.getPlayerAScore().getSets());
        assertEquals(0, matchState.getPlayerAScore().getHighestCheckout());
        assertEquals(0, matchState.getPlayerAScore().getN180s());
        assertEquals(1, matchState.getPlayerBScore().getLegs());
        assertEquals(0, matchState.getPlayerBScore().getSets());
        assertEquals(141, matchState.getPlayerBScore().getHighestCheckout());
        assertEquals(2, matchState.getPlayerBScore().getN180s());
        assertEquals(501, matchState.getCurrentLeg().getPlayerA().getPoints());
        assertEquals(501, matchState.getCurrentLeg().getPlayerB().getPoints());
        assertEquals(TeamId.B, matchState.getPlayerAtOcheAtStartOfCurrentLeg());
        assertEquals(0, matchState.getCurrentLeg().getPlayerA().getN180sThrown());
        assertEquals(0, matchState.getCurrentLeg().getPlayerB().getN180sThrown());
    }

    @Test
    public void testCheckoutPossible() {
        MethodName.log();
        DartMatchState matchState = new DartMatchState();
        matchState.getCurrentLeg().getPlayerA().setPoints(180);
        assertFalse(matchState.checkoutPossible());
        matchState.getCurrentLeg().getPlayerB().setPoints(170);
        assertTrue(matchState.checkoutPossible());
        matchState.getCurrentLeg().getPlayerB().setPoints(165); // can't check out fm 165
        assertFalse(matchState.checkoutPossible());
        matchState.getCurrentLeg().getPlayerA().setPoints(164);
        assertTrue(matchState.checkoutPossible());
    }

    @Test
    public void testMaybeFinalLegForLegBasedMatch() {
        MethodName.log();
        DartMatchFormat matchFormat = new DartMatchFormat(DartMatchLevel.WORLDCLASS, 1, 17, true);
        DartMatchState matchState = new DartMatchState(matchFormat);
        matchState.getPlayerAScore().setSets(7);
        assertFalse(matchState.maybeFinalLeg());
        matchState.getPlayerBScore().setSets(8);
        assertTrue(matchState.maybeFinalLeg());
    }

    @Test
    public void testMaybeFinalLegForSetBasedMatch() {
        MethodName.log();
        DartMatchFormat matchFormat = new DartMatchFormat(DartMatchLevel.WORLDCLASS, 5, 7, true);
        DartMatchState matchState = new DartMatchState(matchFormat);
        matchState.getPlayerAScore().setLegs(2);
        matchState.getPlayerAScore().setSets(2);
        assertFalse(matchState.maybeFinalLeg());
        matchState.getPlayerAScore().setLegs(2);
        matchState.getPlayerAScore().setSets(3);
        assertTrue(matchState.maybeFinalLeg());
    }
}
