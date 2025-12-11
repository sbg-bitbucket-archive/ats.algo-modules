package ats.algo.sport.tennisG;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchIncident;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult;
import ats.algo.sport.tennisG.TennisGMatchState;
import ats.algo.sport.tennisG.TennisGMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult.TennisMatchIncidentResultType;

public class TennisMatchStateTest {

    TennisGMatchState matchState;

    @Test
    public void testGame() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(matchFormat);
        assertTrue(matchState.isPreMatch());
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        TennisGMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.playerAServedPoint());
        assertTrue(outcome.playerAwonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertFalse(matchState.isAlreadyWonASetB());
        assertFalse(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(4));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * score now deuce
         */
        assertEquals(3, matchState.getPointsA());
        assertEquals(3, matchState.getPointsB());
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertFalse(outcome.playerAServedPoint());
        assertFalse(outcome.playerAwonPoint());
        assertEquals(7, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * should be back to deuce
         */
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(3, matchState.getPointsA());
        assertEquals(3, matchState.getPointsB());
        assertEquals(9, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertFalse(outcome.playerAServedPoint());
        assertFalse(outcome.playerAwonPoint());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.GAMEWONB, outcome.getTennisMatchIncidentResultType());
        assertFalse(outcome.playerAServedPoint());
        assertFalse(outcome.playerAwonPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(3, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeNow());
    }

    @Test
    public void testSet() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(0, 0, 5, 5, TeamId.A);
        TennisGMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * now 5-6
         */
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(5, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(12, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertFalse(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(11));
        assertTrue(matchState.isGameMayBePlayed(12));
        assertFalse(matchState.isGameMayBePlayed(13));
        assertTrue(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.SETWONB, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        PairOfIntegers setScore = matchState.getGameScoreInSetN(0);
        assertEquals(5, setScore.A);
        assertEquals(7, setScore.B);
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
    }

    @Test
    public void testMatch() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(1, 1, 6, 5, TeamId.A);
        matchState.setGameScoreInSetN(0, 7, 5);
        matchState.setGameScoreInSetN(1, 6, 7);
        TennisGMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.MATCHWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());
        assertTrue(matchState.isTieBreakAlreadyPlayedInMatch());
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertFalse(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(11));
        assertTrue(matchState.isGameMayBePlayed(12));
        assertFalse(matchState.isGameMayBePlayed(13));
        assertTrue(matchState.isPointMayBePlayed(1));
        PairOfIntegers setScore = matchState.getGameScoreInSetN(2);
        assertEquals(7, setScore.A);
        assertEquals(5, setScore.B);
    }

    @Test
    public void testTieBreak() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(0, 1, 5, 6, TeamId.A);
        TennisGMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.playerAServedPoint());
        assertTrue(outcome.playerAwonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(5, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(12, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertFalse(matchState.isInTieBreak());
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(12));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        /*
         * score now 6-6, so in tie break
         */
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertTrue(matchState.isInTieBreak());
        assertTrue(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(1));
        assertFalse(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertFalse(outcome.playerAServedPoint());
        assertTrue(outcome.playerAwonPoint());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertTrue(outcome.playerAServedPoint());
        assertTrue(outcome.playerAwonPoint());
        /*
         * now 3-0 in TB
         */
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(6, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(7, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
        PairOfIntegers score = matchState.getGameScoreInSetN(1);
        assertEquals(7, score.A);
        assertEquals(6, score.B);
        /*
         * player to serve in first game after TB is the one who served at 5-6 (or 6-5)
         */
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertFalse(matchState.isInTieBreak());
        /*
         * play a few more points...
         */
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(0, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(3, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertFalse(matchState.isInTieBreak());
    }

    @Test
    public void testAdvantageMatch() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(5, false);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(2, 2, 5, 6, TeamId.A);
        TennisGMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(2, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(5, matchState.getSetNo());
        assertFalse(matchState.isInTieBreak());
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(20));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(7, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(2, matchState.getSetsB());
        assertFalse(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(14, matchState.getGameNo());
        assertEquals(5, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.MATCHWONA, outcome.getTennisMatchIncidentResultType());
        PairOfIntegers score = matchState.getGameScoreInSetN(4);
        assertEquals(8, score.A);
        assertEquals(6, score.B);
        /*
         * check tie break ok in earlier sets
         */
        matchState.setScore(1, 2, 5, 6, TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(2, matchState.getSetsB());
        assertTrue(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());
    }

    @Test
    public void testCopy() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(5, false);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(0, 0, 6, 5, TeamId.A);

        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        /*
         * score should now be 1-0 0-0 30-0
         */
        TennisGMatchState sCopy = (TennisGMatchState) matchState.copy();
        assertEquals(2, sCopy.getPointsA());
        assertEquals(0, sCopy.getPointsB());
        assertEquals(0, sCopy.getGamesA());
        assertEquals(0, sCopy.getGamesB());
        assertEquals(1, sCopy.getSetsA());
        assertEquals(0, sCopy.getSetsB());
        assertEquals(3, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        PairOfIntegers p = sCopy.getGameScoreInSetN(0);
        assertEquals(7, p.A);
        assertEquals(5, p.B);
        TennisGMatchState s2 = new TennisGMatchState(matchFormat);
        s2.setEqualTo(matchState);
        assertEquals(2, s2.getPointsA());
        assertEquals(0, s2.getPointsB());
        assertEquals(0, s2.getGamesA());
        assertEquals(0, s2.getGamesB());
        assertEquals(1, s2.getSetsA());
        assertEquals(0, s2.getSetsB());
        assertEquals(3, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        PairOfIntegers p2 = s2.getGameScoreInSetN(0);
        assertEquals(7, p2.A);
        assertEquals(5, p.B);

    }

    private TennisGMatchIncidentResult getTennisMatchIncidentResult(TeamId playerId) {
        TennisGMatchIncident incident = new TennisGMatchIncident(0, TennisMatchIncidentType.POINT_WON, playerId);
        return (TennisGMatchIncidentResult) matchState.updateStateForIncident(incident, false);
    }

    @Test
    public void testOutcomes() {
        TennisGMatchFormat matchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(matchFormat);
        matchState.setScore(0, 0, 0, 0, TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getLastPointPlayedOutcome());
        getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TeamId.B, matchState.getLastPointPlayedOutcome());
        assertEquals(null, matchState.getLastGamePlayedOutcome());
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TeamId.B, matchState.getLastPointPlayedOutcome());
        assertEquals(TeamId.B, matchState.getLastGamePlayedOutcome());
    }

    @Test
    public void testCompleteMatch() {
        TennisGMatchFormat tennisMatchFormat = new TennisGMatchFormat(3, true);
        matchState = new TennisGMatchState(tennisMatchFormat);

        TennisGMatchIncident tennisMatchIncident =
                        new TennisGMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchState.updateStateForIncident(tennisMatchIncident, false);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        for (int i = 0; i < 47; i++)
            matchState.updateStateForIncident(tennisMatchIncident, false);
        System.out.print(matchState.toString());
        assertTrue(!matchState.isMatchCompleted());
        matchState.updateStateForIncident(tennisMatchIncident, false);
        assertTrue(matchState.isMatchCompleted());
    }
}
