package ats.algo.sport.tennis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;
import ats.core.util.json.JsonUtil;

public class TennisMatchStateTest {

    TennisMatchState matchState;

    /**
     * 
     * DOUBLE TENNIS TESTS
     * 
     **/


    @Test //
    public void testDoubleTBGame() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.CHAMPIONSHIP_TIE_BREAK, true, true);
        matchState = new TennisMatchState(matchFormat);
        assertTrue(matchState.isPreMatch());
        matchState.setScore(0, 1, 6, 6, TeamId.A, 1, 1);
        System.out.println("Start of the match serve team/player - " + matchState.getStartOfMatchServer());
        @SuppressWarnings("unused")
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResultTB(TeamId.A);
        System.out.println("Points A : B - " + matchState.getPointsA() + " : " + matchState.getPointsB());
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        System.out.println("Points A : B - " + matchState.getPointsA() + " : " + matchState.getPointsB());

        System.out.println(
                        "Tiebreak - Supertiebreak :" + matchState.inTieBreak + " - " + matchState.isInSuperTieBreak());
        outcome = getTennisMatchIncidentResult(TeamId.A);

        System.out.println("Points A : B - " + matchState.getPointsA() + " : " + matchState.getPointsB());

        System.out.println(
                        "Tiebreak - Supertiebreak :" + matchState.inTieBreak + " - " + matchState.isInSuperTieBreak());
        // points goes to 7-4
        outcome = getTennisMatchIncidentResult(TeamId.A);
        System.out.println("Sets A : B - " + matchState.getSetsA() + " : " + matchState.getSetsB() + "Games A : B - "
                        + matchState.getGamesA() + " : " + matchState.getGamesB() + " Points A : B - "
                        + matchState.getPointsA() + " : " + matchState.getPointsB());

        System.out.println(
                        "Tiebreak - Supertiebreak :" + matchState.inTieBreak + " - " + matchState.isInSuperTieBreak());

        ///
        TennisMatchIncident tempMatchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println("Sets A : B - " + matchState.getSetsA() + " : " + matchState.getSetsB() + "Games A : B - "
                        + matchState.getGamesA() + " : " + matchState.getGamesB() + " Points A : B - "
                        + matchState.getPointsA() + " : " + matchState.getPointsB());

        System.out.println(
                        "Tiebreak - Supertiebreak :" + matchState.inTieBreak + " - " + matchState.isInSuperTieBreak());
    }


    @Test // Single, final set advantage ,3 sets, for Ticket RT-4722
    public void testSingle4AdvantageMatchGenerateMatchCharacteristics() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, false);
        matchState = new TennisMatchState(matchFormat);

        TennisMatchIncident tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.FAULT, TeamId.A, 1);

        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println(tempMatchIncident.getPointWinner());
        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(0);
        matchState.updateStateForIncident(tempMatchIncident, false);

    }


    /**
     * 
     * SINGLE TENNIS TESTS
     * 
     **/
    @Test // Single, final set advantage ,3 sets, for Ticket RT-4722
    public void testSingle4AdvantageMatch() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, false);
        matchState = new TennisMatchState(matchFormat);

        TennisMatchIncident tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.FAULT, TeamId.A, 1);

        matchState.updateStateForIncident(tempMatchIncident, false);

        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(0);

        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println(matchState);

        assertEquals(1, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());


        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(0);

        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println(matchState);

        assertEquals(1, matchState.getPointsA());
        assertEquals(1, matchState.getPointsB());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());


        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.FAULT, TeamId.A, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(0);

        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println(matchState);

        assertEquals(1, matchState.getPointsA());
        assertEquals(1, matchState.getPointsB());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());

        // penalty point won test
        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.PENALTY_POINT_WON, TeamId.A, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        assertEquals(2, matchState.getPointsA());
        assertEquals(1, matchState.getPointsB());
        assertEquals(TeamId.B, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());


        // penalty GAME won test
        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.PENALTY_GAME_WON, TeamId.B, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        System.out.println(matchState);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());

        // point won test
        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        assertEquals(1, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());

        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.PENALTY_POINT_WON, TeamId.A, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        assertEquals(2, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(TeamId.A, matchState.getOnServeNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerInFirstGameOfSet());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerInSecondGameOfSet());


        System.out.println(JsonUtil.marshalJson(matchState, true));

        tempMatchIncident = new TennisMatchIncident(1, TennisMatchIncidentType.PENALTY_MATCH_WON, TeamId.A, 1);
        matchState.updateStateForIncident(tempMatchIncident, false);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(0, matchState.getSetsB());
        assertEquals(true, matchState.isMatchCompleted());

    }


    @Test // Single, final set advantage ,3 sets
    public void testSingle3AdvantageMatch() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, false);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(0, 1, 5, 6, TeamId.A, 1, 1);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertTrue(matchState.isInTieBreak()); //////// not final set, still play tie breaks
        assertFalse(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(13));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // System.out.println(matchState.isASetJustWon());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // System.out.println(matchState.isASetJustWon());
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());
        // System.out.println(matchState.isASetJustWon());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertFalse(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // System.out.println(matchState.isASetJustWon());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        PairOfIntegers score = matchState.getGameScoreInSetN(1);
        assertEquals(7, score.A);
        assertEquals(6, score.B);
        /*
         * check AD ok in final sets
         */
        matchState.setScore(1, 1, 5, 5, TeamId.A, 1, 1);
        outcome = getTennisMatchIncidentResult(TeamId.A);
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
        assertEquals(3, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertFalse(matchState.isPreMatch());
        assertTrue(matchState.isTieBreakAlreadyPlayedInMatch());
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(11));
        assertTrue(matchState.isGameMayBePlayed(12));
        assertTrue(matchState.isGameMayBePlayed(14)); // confirmed no tie breaks
        assertTrue(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.MATCHWONB, outcome.getTennisMatchIncidentResultType());
        assertFalse(matchState.isGameMayBePlayed(20));
    }

    @Test // single 3 Regular tie break final set
    public void testSingle3Game() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false); // singles
        matchState = new TennisMatchState(matchFormat);
        assertTrue(matchState.isPreMatch());
        matchState.setScore(0, 0, 0, 0, TeamId.A, 1, 1);

        System.out.println(matchState.getOnServePlayerInFirstGameOfSet());

        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(1, matchState.getOnServePlayerTeamANow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // FIXEME: add get on serve player now
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertFalse(matchState.isAlreadyWonASetB());
        assertFalse(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(4));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());

        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
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
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(7, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * should be back to deuce
         */
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(3, matchState.getPointsA());
        assertEquals(3, matchState.getPointsB());
        assertEquals(9, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.GAMEWONB, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(3, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // A won a game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());

        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        // set serving order for b
        matchState.setOnServePlayerTeamBNow(1);
        // A won one game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);

        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // New set, unknown serving orders

        matchState.setScore(1, 1, 5, 6, TeamId.B, 1, 1); // current serving order.
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        System.out.println(matchState.getSetsA() + " " + matchState.getSetsB() + " " + matchState.getGamesA() + "  "
                        + matchState.getGamesB());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        System.out.println(matchState.getSetsA() + " " + matchState.getSetsB());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // entering tie break
        assertTrue(matchState.isInTieBreak());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
    }


    @Test // Single, final set advantage ,5 sets
    public void testSingleAdvantageMatch() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(5, FinalSetType.ADVANTAGE_SET, false, false);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(1, 1, 5, 6, TeamId.A, 1, 1);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
        assertTrue(matchState.isInTieBreak()); //////// not final set, still play tie breaks
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(13));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertFalse(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        PairOfIntegers score = matchState.getGameScoreInSetN(2);
        assertEquals(7, score.A);
        assertEquals(6, score.B);
        /*
         * check AD ok in final sets
         */
        matchState.setScore(2, 2, 5, 5, TeamId.A, 1, 1);
        outcome = getTennisMatchIncidentResult(TeamId.A);
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
        assertEquals(5, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertFalse(matchState.isPreMatch());
        assertTrue(matchState.isTieBreakAlreadyPlayedInMatch());
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(11));
        assertTrue(matchState.isGameMayBePlayed(12));
        assertTrue(matchState.isGameMayBePlayed(14)); // confirmed no tie breaks
        assertTrue(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.MATCHWONB, outcome.getTennisMatchIncidentResultType());
        assertFalse(matchState.isGameMayBePlayed(20));
    }

    @Test // single 5 Regular tie break final set
    public void testSingleGame() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(5, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false); // singles
        matchState = new TennisMatchState(matchFormat);
        assertTrue(matchState.isPreMatch());
        matchState.setScore(0, 0, 0, 0, TeamId.A, 1, 1);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(1, matchState.getOnServePlayerTeamANow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // FIXEME: add get on serve player now
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertFalse(matchState.isAlreadyWonASetB());
        assertFalse(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(4));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());

        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
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
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(7, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * should be back to deuce
         */
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(3, matchState.getPointsA());
        assertEquals(3, matchState.getPointsB());
        assertEquals(9, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.GAMEWONB, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(3, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // A won a game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());

        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        // set serving order for b
        matchState.setOnServePlayerTeamBNow(1);
        // A won one game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);

        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // New set, unknown serving orders

        matchState.setScore(1, 2, 5, 6, TeamId.B, 1, 1); // current serving order.
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // entering tie break
        assertTrue(matchState.isInTieBreak());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);

    }



    /**
     * 
     * DOUBLE TENNIS TESTS
     * 
     **/


    @Test // double 5 Regular tie break final set
    public void testGame() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(5, FinalSetType.NORMAL_WITH_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        assertTrue(matchState.isPreMatch());
        matchState.setScore(0, 0, 0, 0, TeamId.A, 2, 2);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(2, matchState.getOnServePlayerTeamANow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // FIXEME: add get on serve player now
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertFalse(matchState.isAlreadyWonASetB());
        assertFalse(matchState.isAlreadyOver4GamesInCurrentSet());
        assertTrue(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(4));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());

        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
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
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(7, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        /*
         * should be back to deuce
         */
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(3, matchState.getPointsA());
        assertEquals(3, matchState.getPointsB());
        assertEquals(9, matchState.getPointNo());
        assertEquals(2, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.GAMEWONB, outcome.getTennisMatchIncidentResultType());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        assertFalse(outcome.isTeamAServedPoint());
        assertFalse(outcome.isTeamAWonPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(1, matchState.getGamesA());
        assertEquals(1, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(3, matchState.getGameNo());
        assertEquals(1, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        // A won a game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        System.out.println(matchState);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A2, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // A won a game
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow()); // A1 serve point now
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());

        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.UNKNOWN, matchState.getOnServePlayerNow()); // New set, unknown serving orders
        // set serving order for b
        matchState.setOnServePlayerTeamBNow(1);
        // A won one game
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);

        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.UNKNOWN, matchState.getOnServePlayerNow()); // New set, unknown serving orders

        matchState.setScore(1, 2, 5, 6, TeamId.B, 1, 1); // current serving order.
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B1, matchState.getOnServePlayerNow());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        // entering tie break
        assertTrue(matchState.isInTieBreak());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A2, matchState.getOnServePlayerNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());

        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.B2, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertEquals(PlayerId.A1, matchState.getOnServePlayerNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);

    }


    @Test // double 3 SETS AD
    public void testSet() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(1, 1, 5, 5, TeamId.A, 1, 2);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
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
        assertEquals(3, matchState.getSetNo());
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertFalse(matchState.isPreMatch());
        assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        assertTrue(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(11));
        assertTrue(matchState.isGameMayBePlayed(12));
        assertTrue(matchState.isGameMayBePlayed(14)); // confirmed no tie breaks
        assertTrue(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        assertEquals(TennisMatchIncidentResultType.MATCHWONB, outcome.getTennisMatchIncidentResultType());
        // assertEquals(0, matchState.getPointsA());
        // assertEquals(0, matchState.getPointsB());
        // assertEquals(0, matchState.getGamesA());
        // assertEquals(0, matchState.getGamesB());
        // assertEquals(0, matchState.getSetsA());
        // assertEquals(1, matchState.getSetsB());
        // assertFalse(matchState.isTieBreakAlreadyPlayedInMatch());
        // assertFalse(matchState.isAlreadyWonASetA());
        // assertTrue(matchState.isAlreadyWonASetB());
        // PairOfIntegers setScore = matchState.getGameScoreInSetN(0);
        // assertEquals(5, setScore.A);
        // assertEquals(7, setScore.B);
        // assertEquals(1, matchState.getPointNo());
        // assertEquals(1, matchState.getGameNo());
        // assertEquals(2, matchState.getSetNo());
    }

    @Ignore
    @Test
    public void testMatch() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(1, 1, 6, 5, TeamId.A, 1, 1);
        matchState.setGameScoreInSetN(0, 7, 5);
        matchState.setGameScoreInSetN(1, 6, 7);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
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

    @Test // Regular tie break for double 3 sets
    public void testTieBreak() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(0, 1, 5, 6, TeamId.A, 2, 1);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertTrue(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(5, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(4, matchState.getPointNo());
        assertEquals(12, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
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
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertTrue(matchState.isInTieBreak());
        assertTrue(matchState.isTieBreakAlreadyPlayedInMatch());
        assertFalse(matchState.isAlreadyWonASetA());
        assertTrue(matchState.isAlreadyWonASetB());
        assertTrue(matchState.isAlreadyOver4GamesInCurrentSet());
        assertFalse(matchState.isGameMayBePlayed(1));
        assertTrue(matchState.isPointMayBePlayed(1));
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertFalse(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertTrue(outcome.isTeamAServedPoint());
        assertTrue(outcome.isTeamAWonPoint());
        /*
         * now 3-0 in TB
         */
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        assertEquals(3, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.B, matchState.getOnServeTeamNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.POINTWONONLY, outcome.getTennisMatchIncidentResultType());
        assertEquals(6, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(7, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.SETWON, outcome.getTennisMatchIncidentResultType());
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
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertFalse(matchState.isInTieBreak());
        /*
         * play a few more points...
         */
        // new set set serving orders
        matchState.setOnServePlayerTeamANow(1);
        matchState.setOnServePlayerTeamBNow(1);
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
        assertEquals(TeamId.A, matchState.getOnServeTeamNow());
        assertFalse(matchState.isInTieBreak());
        // a win game 1
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // b win game 2
        matchState.setOnServePlayerTeamBNow(1);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        // a win game 3
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // a win game 4
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // b win game 5
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        outcome = getTennisMatchIncidentResult(TeamId.B);
        // a win game 6
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // a win game 7
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        // a win game 8 and won the match
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);

        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(0, matchState.getGamesA());
        assertEquals(0, matchState.getGamesB());
        assertEquals(2, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());
    }

    @Ignore
    @Test
    public void testSuperTieBreakMatch() { // tb 10
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.CHAMPIONSHIP_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(1, 1, 0, 0, TeamId.A, 1, 2);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.MATCHWONA, outcome.getTennisMatchIncidentResultType());

    }



    @Test // double, final set advantage ,game advantage
    public void testAdvantageMatch() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.ADVANTAGE_SET, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(1, 1, 5, 6, TeamId.A, 1, 2);
        TennisMatchIncidentResult outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
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
        assertEquals(1, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertFalse(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(14, matchState.getGameNo());
        assertEquals(3, matchState.getSetNo());
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.MATCHWONA, outcome.getTennisMatchIncidentResultType());
        PairOfIntegers score = matchState.getGameScoreInSetN(2);
        assertEquals(8, score.A);
        assertEquals(6, score.B);
        /*
         * check tie break ok in earlier sets
         */
        matchState.setScore(0, 1, 5, 6, TeamId.A, 1, 1);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        outcome = getTennisMatchIncidentResult(TeamId.A);
        assertEquals(TennisMatchIncidentResultType.GAMEWONA, outcome.getTennisMatchIncidentResultType());
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        assertEquals(0, matchState.getSetsA());
        assertEquals(1, matchState.getSetsB());
        assertTrue(matchState.isInTieBreak());
        assertEquals(1, matchState.getPointNo());
        assertEquals(13, matchState.getGameNo());
        assertEquals(2, matchState.getSetNo());
    }

    @Test // test scores first, and Championship tie breaks
    public void testCopy() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.CHAMPIONSHIP_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(0, 0, 6, 5, TeamId.A, 2, 2);

        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        // new Set serving order settings, delayed serving order test
        TennisMatchIncident tempMatchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(1);
        // tempMatchIncident.setType(TennisMatchIncidentType.POINT_WON);
        matchState.updateStateForIncident(tempMatchIncident, false);
        getTennisMatchIncidentResult(TeamId.A);
        /*
         * score should now be 1-0 0-0 30-0
         */
        TennisMatchState sCopy = (TennisMatchState) matchState.copy();
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
        TennisMatchState s2 = new TennisMatchState(matchFormat);
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

        matchState.setScore(1, 1, 0, 0, TeamId.A, 1, 1);
        // team A won 10 continues points
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);

        TennisMatchState s3 = (TennisMatchState) matchState.copy();
        assertEquals(0, s3.getPointsA());
        assertEquals(0, s3.getPointsB());
        assertEquals(0, s3.getGamesA());
        assertEquals(0, s3.getGamesB());
        assertEquals(2, s3.getSetsA());
        assertEquals(1, s3.getSetsB());
        assertEquals(1, matchState.getPointNo());
        assertEquals(1, matchState.getGameNo());
        assertEquals(4, matchState.getSetNo());
        PairOfIntegers p3 = sCopy.getGameScoreInSetN(2);
        assertEquals(0, p3.A); // Match Finished points reseted.
        assertEquals(0, p3.B);

    }

    private TennisMatchIncidentResult getTennisMatchIncidentResult(TeamId playerId) {
        // FIXEME:
        TennisMatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, playerId, 0);
        return (TennisMatchIncidentResult) matchState.updateStateForIncident(incident, false);
    }


    private TennisMatchIncidentResult getTennisMatchIncidentResultTB(TeamId playerId) {
        // FIXEME:
        TennisMatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, playerId, 0);
        incident.setServerSideAtStartOfMatch(TeamId.A);
        return (TennisMatchIncidentResult) matchState.updateStateForIncident(incident, false);
    }

    @Test
    public void testOutcomes() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, true);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(0, 0, 0, 0, TeamId.A, 1, 2);
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

    @Test // single test incidences contains serving at the beggning of the match
    public void testOutCome() {
        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.CHAMPIONSHIP_TIE_BREAK, false, false);
        matchState = new TennisMatchState(matchFormat);
        matchState.setScore(0, 0, 6, 5, TeamId.A, 1, 1);

        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.A);
        // new Set serving order settings, delayed serving order test
        TennisMatchIncident tempMatchIncident =
                        new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        tempMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        tempMatchIncident.setServerPlayerAtStartOfMatch(1);
        // tempMatchIncident.setType(TennisMatchIncidentType.POINT_WON);
        matchState.updateStateForIncident(tempMatchIncident, false);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.B);


        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        getTennisMatchIncidentResult(TeamId.A);
        getTennisMatchIncidentResult(TeamId.B);
        matchState.setOnServeSideNow(TeamId.UNKNOWN);
        TennisMatchIncident tempMatchIncident2 =
                        new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        tempMatchIncident2.setServerSideAtStartOfMatch(TeamId.A);
        tempMatchIncident2.setServerPlayerAtStartOfMatch(1);
        matchState.updateStateForIncident(tempMatchIncident2, false);
        System.out.println(matchState.getOnServeNow());

    }



}
