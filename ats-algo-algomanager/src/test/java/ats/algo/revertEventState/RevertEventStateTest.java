package ats.algo.revertEventState;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.MatchReferralIncident;
import ats.algo.core.common.MatchReferralIncident.MatchReferralIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.AflMatchIncident;
import ats.algo.sport.afl.AflMatchIncident.AflMatchIncidentType;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class RevertEventStateTest extends AlgoManagerSimpleTestBase {

    @Test
    public void testUndoLastUndoableIncident() {
        long eventId = 11L;
        FootballMatchFormat format = new FootballMatchFormat();
        algoManager.publishResultedMarketsImmediately(true);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, format);
        publishedMatchState = null;
        boolean undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);

        assertEquals(null, publishedMatchState);
        ElapsedTimeMatchIncident matchIncident =
                        getElapsedTimeIncident(eventId, "R1", ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getFootballIncident(eventId, "R2", FootballMatchIncidentType.CORNER, 100,
                        TeamId.A, "runningBall"), true);
        algoManager.handleMatchIncident(getFootballIncident(eventId, "R3", FootballMatchIncidentType.CORNER, 200,
                        TeamId.A, "runningBall"), true);
        assertEquals(2, ((FootballSimpleMatchState) publishedMatchState).getCornersA());
        algoManager.handleMatchIncident(
                        getFootballIncident(eventId, "R4", FootballMatchIncidentType.GOAL, 300, TeamId.A, "Abelson"),
                        true);
        algoManager.handleMatchIncident(
                        getFootballIncident(eventId, "R5", FootballMatchIncidentType.GOAL, 350, TeamId.A, "TX"), true);

        assertEquals(2, ((FootballSimpleMatchState) publishedMatchState).getGoalsA());

        algoManager.handleMatchIncident(getFootballIncident(eventId, "R6", FootballMatchIncidentType.CORNER, 400,
                        TeamId.A, "runningBall"), true);
        assertEquals(3, ((FootballSimpleMatchState) publishedMatchState).getCornersA());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        /*
         * R4 is Goal Abelson, can not revert here
         */
        assertFalse(algoManager.handleRevertToStatePreceedingRequestId(eventId, "R4"));
        /*
         * Could revert to R3 which is not from Abelson, will process R4 to the queue after the reversion.
         */
        assertTrue(algoManager.handleRevertToStatePreceedingRequestId(eventId, "R3"));
        /*
         * Only revert TX Goal
         */
        assertEquals(1, ((FootballSimpleMatchState) publishedMatchState).getGoalsA());

    }

    /**
     * issue two incidents, goal then VAR referral, then undo goal. Make sure correct incident gets undone
     */
    @Test
    public void testUndoGoalIncident() {
        long eventId = 174L;
        FootballMatchFormat format = new FootballMatchFormat();
        algoManager.publishResultedMarketsImmediately(true);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, format);
        ElapsedTimeMatchIncident matchIncident =
                        getElapsedTimeIncident(eventId, "R1", ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getFootballIncident(eventId, "R2", FootballMatchIncidentType.GOAL, 100,
                        TeamId.A, "runningBall"), true);
        algoManager.handleMatchIncident(getMatchReferralIncident(eventId, "R3",
                        MatchReferralIncidentType.VAR_REFERRAL_CONFIRMED, 200, "runningBall"), true);
        assertEquals(1, ((FootballSimpleMatchState) publishedMatchState).getGoalsA());
        MatchIncident incident = getFootballIncident(eventId, "R4", FootballMatchIncidentType.GOAL, 100, TeamId.A,
                        "runningBall");
        incident.setUndo(true);
        algoManager.handleUndoMatchIncident(eventId, incident);
        assertEquals(0, ((FootballSimpleMatchState) publishedMatchState).getGoalsA());

    }

    @Test
    public void testRevertEventState() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();

        algoManager.publishResultedMarketsImmediately(false); // ensure flag set
                                                              // to false
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        publishedMatchState = null;
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R3"), true);
        /*
         * first markets should be resulted, but not yet published
         */
        assertTrue(publishedResultedMarkets == null);
        boolean success = algoManager.handleRevertToStatePreceedingRequestId(eventId, "R2");
        assertTrue(success);
        System.out.println(publishedMatchState);
        assertEquals("R1", publishedMatchState.getIncidentId());
        success = algoManager.handleRevertToStatePreceedingRequestId(eventId, "R2");
        assertFalse(success);
    }

    @Test
    public void testUndoLastIncident() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.publishResultedMarketsImmediately(true);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        publishedMatchState = null;
        boolean undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);
        assertEquals(null, publishedMatchState);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("S0-0");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S15-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S40-0"), true);
        assertEquals(3, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(3,this.publishedResultedMarkets.getFullyResultedMarkets().size());
        assertEquals("S40-0", publishedMatchState.getIncidentId());
        assertTrue(this.keysForDiscontinuedMarkets.contains("G:PW_S1.1.4"));

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(2, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(2,this.publishedResultedMarkets.getFullyResultedMarkets().size());
        assertEquals("S30-0", publishedMatchState.getIncidentId());
        assertTrue(this.keysForDiscontinuedMarkets.contains("G:PW_S1.1.6"));

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(1, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(1,this.publishedResultedMarkets.getFullyResultedMarkets().size());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(0, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        assertFalse(((TennisSimpleMatchState) publishedMatchState).preMatch());
        // assertEquals(0,this.publishedResultedMarkets.size());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(0, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        assertTrue(((TennisSimpleMatchState) publishedMatchState).preMatch());
        // assertEquals(0,this.publishedResultedMarkets.size());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);
    }

    @Test
    public void testUndoLastIncidentWithoutPublishMarkets() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.publishResultedMarketsImmediately(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        publishedMatchState = null;
        boolean undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);
        assertEquals(null, publishedMatchState);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("S0-0");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S15-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S40-0"), true);
        assertEquals(3, ((TennisSimpleMatchState) publishedMatchState).getPointsA());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(2, ((TennisSimpleMatchState) publishedMatchState).getPointsA());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(1, ((TennisSimpleMatchState) publishedMatchState).getPointsA());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(0, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        assertFalse(((TennisSimpleMatchState) publishedMatchState).preMatch());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(0, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        assertTrue(((TennisSimpleMatchState) publishedMatchState).preMatch());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);
    }

    @Test
    public void testUndoThenRedoLastIncident() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.publishResultedMarketsImmediately(true);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        publishedMatchState = null;
        boolean undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoSucceeded);
        assertEquals(null, publishedMatchState);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("S0-0");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S15-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S40-0"), true);
        assertEquals(3, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        System.out.println(publishedMarkets);
        System.out.println(publishedMarkets.size());
        // assertEquals(3,
        // this.publishedResultedMarkets.getFullyResultedMarkets().size());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(2, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(2,
        // this.publishedResultedMarkets.getFullyResultedMarkets().size());

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(1, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(1,
        // this.publishedResultedMarkets.getFullyResultedMarkets().size());

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0v2"), true);

        undoSucceeded = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoSucceeded);
        assertEquals(1, ((TennisSimpleMatchState) publishedMatchState).getPointsA());
        // assertEquals(1,
        // this.publishedResultedMarkets.getFullyResultedMarkets().size());
    }

    @Test
    public void testDelayedPublishResultedMarkets() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.publishResultedMarketsImmediately(false); // ensure flag set
                                                              // to false
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("S0-0");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S15-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0"), true);
        /*
         * first markets should be resulted, but not yet published
         */
        assertTrue(publishedResultedMarkets == null);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S40-0"), true);
        assertTrue(publishedResultedMarkets == null);
        /*
         * next incident should cause resultedMarkets related to R1 to be published, which will be non-null but empty
         */
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S1-0,0-0"), true);
        assertTrue(publishedResultedMarkets != null);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S1-0,15-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S1-0,30-0"), true);
        /*
         * next incident will cause the first real resultedMarket to be published
         */
        publishedResultedMarkets = null;
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S1-0,40-0"), true);
        System.out.println(publishedResultedMarkets);
        assertTrue(publishedResultedMarkets != null);
        // assertEquals(2,
        // publishedResultedMarkets.getFullyResultedMarkets().size());
        /*
         * try to roll back to 15-0 - should return false since is not in cache
         */
        assertFalse(algoManager.handleRevertToStatePreceedingRequestId(eventId, "S15-0"));
        /*
         * add another incident then roll back to "S1-0,40-0"
         */

        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S2-0,0-0"), true);
        // assertEquals(3,
        // publishedResultedMarkets.getFullyResultedMarkets().size());
        publishedResultedMarkets = null;
        algoManager.handleUndoLastMatchIncident(eventId);
        System.out.println(publishedResultedMarkets);
        assertTrue(publishedResultedMarkets == null);
    }

    @Test
    public void testResultedMarketsPublishedAfterDelay() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.publishResultedMarketsImmediately(false); // ensure flag set
                                                              // to false
        algoManager.setDelayBeforePublishingResultedMarkets(SupportedSportType.TENNIS, 20); // set
                                                                                            // delay
                                                                                            // to
                                                                                            // 20
                                                                                            // secs
                                                                                            // for
                                                                                            // testing
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        sleep(3);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("S0-0");
        algoManager.handleMatchIncident(matchIncident, true);
        sleep(3);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S15-0"), true);
        sleep(5);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S30-0"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "S40-0"), true);
        assertTrue(publishedResultedMarkets == null);
        int n = waitForResultedMarketsToPublish();
        System.out.println(n);
        assertTrue(n >= 7 && n <= 11); // shld be 9 secs (20-the 9 we've already
                                       // waited)
        assertEquals("CreateEvent_11", publishedResultedMarkets.getIncidentId());
        n = waitForResultedMarketsToPublish();
        assertTrue(n >= 2 && n <= 4); // shld be 3 secs
        assertTrue(publishedResultedMarkets.getIncidentId().equals("S0-0"));
        n = waitForResultedMarketsToPublish();
        assertTrue(n >= 2 && n <= 4); // shld be 3 secs
        assertTrue(publishedResultedMarkets.getIncidentId().equals("S15-0"));
        /*
         * try undoing. Should only be able to undo two incidents and we should not then get any more resulted markets
         */
        publishedResultedMarkets = null;
        System.out.println("UNDO S40-0");
        boolean undoOk = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoOk);
        System.out.println("UNDO S30-0");
        undoOk = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoOk);
        System.out.println("UNDO S15-0");
        undoOk = algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(undoOk);
        sleep(6);
        assertEquals(null, publishedResultedMarkets);
    }

    @Test
    public void testResultedMarketsPublishedAfterDelayForTimerBasedSport() {
        long eventId = 21L;
        AflMatchFormat format = new AflMatchFormat();
        algoManager.publishResultedMarketsImmediately(false); // ensure flag set
                                                              // to false
        algoManager.setDelayBeforePublishingResultedMarkets(SupportedSportType.AUSSIE_RULES, 20);
        algoManager.handleNewEventCreation(SupportedSportType.AUSSIE_RULES, eventId, format);
        sleep(3);
        MatchIncident matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("Incident1 - START_MATCH");
        algoManager.handleMatchIncident(matchIncident, true);
        sleep(22);
        matchIncident = new AflMatchIncident(AflMatchIncidentType.SIX_POINTS_SCORED, 5, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("Incident2 - SCORE_A");
        algoManager.handleMatchIncident(matchIncident, true);
        sleep(3);
        boolean undoOk = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoOk);
        sleep(3);
        matchIncident = new AflMatchIncident(AflMatchIncidentType.SIX_POINTS_SCORED, 8, TeamId.B);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("Incident3 - SCORE_B");
        algoManager.handleMatchIncident(matchIncident, true);
        sleep(3);
        undoOk = algoManager.handleUndoLastMatchIncident(eventId);
        assertTrue(undoOk);
    }

    private int waitForResultedMarketsToPublish() {
        publishedResultedMarkets = null;
        int n = 0;
        while (publishedResultedMarkets == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            n++;
            System.out.printf("Waiting %d secs\n", n);
        }
        return n;
    }

    private void sleep(int n) {
        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Waiting %d of %d\n", i + 1, n);
        }

    }

    TennisMatchIncident getPointWonIncident(long eventId, String incidentId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        return matchIncident;
    }

    FootballMatchIncident getFootballIncident(long eventId, String incidentId, FootballMatchIncidentType type,
                    int elapsedTime, TeamId team, String source) {
        FootballMatchIncident matchIncident = new FootballMatchIncident(type, elapsedTime, team);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        matchIncident.setSourceSystem(source);
        return matchIncident;
    }

    ElapsedTimeMatchIncident getElapsedTimeIncident(long eventId, String incidentId, ElapsedTimeMatchIncidentType type,
                    int elapsedTime) {
        ElapsedTimeMatchIncident matchIncident = new ElapsedTimeMatchIncident(type, 0);

        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        matchIncident.setSourceSystem("runningBall");
        return matchIncident;
    }

    /**
     * 
     * @param eventId
     * @param incidentId
     * @param type
     * @param elapsedTime
     * @return
     */
    MatchReferralIncident getMatchReferralIncident(long eventId, String incidentId, MatchReferralIncidentType type,
                    int elapsedTime, String source) {
        MatchReferralIncident matchIncident = new MatchReferralIncident(type, elapsedTime);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        matchIncident.setSourceSystem(source);
        return matchIncident;
    }



}
