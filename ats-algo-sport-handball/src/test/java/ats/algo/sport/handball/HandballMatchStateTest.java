package ats.algo.sport.handball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.handball.HandballMatchIncident.HandballMatchIncidentType;

public class HandballMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        matchFormat.setExtraTimeMinutes(15);
        HandballMatchState matchState = new HandballMatchState(matchFormat);
        HandballMatchIncident handballMatchIncident = new HandballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(HandballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        HandballMatchPeriod outcome = (HandballMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        handballMatchIncident.set(HandballMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident, false);
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        assertEquals(2, matchState.getGoalNo());
        assertEquals(1, matchState.getPeriodNo());
        assertEquals("G2", matchState.getSequenceIdForGoal(0));
        assertEquals("P1", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        /*
         * score away goal
         */
        handballMatchIncident.set(HandballMatchIncidentType.GOAL, 80, TeamId.B);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident, false);
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(80, matchState.getElapsedTimeSecs());
        assertEquals(80, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 1700);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(1700, matchState.getElapsedTimeSecs());
        assertEquals(1700, matchState.getElapsedTimeThisPeriodSecs());

        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(1700, matchState.getElapsedTimeSecs());

        /*
         * roll over to second half
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1800);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(1, matchState.getPreviousPeriodGoalsA());
        assertEquals(1, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.AT_HALF_TIME, outcome);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1800);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(HandballMatchPeriod.IN_SECOND_HALF, outcome);
        /*
         * score two goals for B)
         */
        handballMatchIncident.set(HandballMatchIncidentType.GOAL, 1900, TeamId.B);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident, false);
        handballMatchIncident.set(HandballMatchIncidentType.GOAL, 1900, TeamId.B);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident, false);
        assertEquals(1, matchState.getPreviousPeriodGoalsA());
        assertEquals(1, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(2, matchState.getCurrentPeriodGoalsB());
        /*
         * check to see that all incidents update elapsed time
         */
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident, false);
        assertEquals(1900, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());
        /*
         * check injury time gets extended at the end of the second half
         */


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3600);
        outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(3, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());
        assertEquals(HandballMatchPeriod.MATCH_COMPLETED, outcome);
        // elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 3600);
        // outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
        // false);
        // assertEquals(HandballMatchPeriod.IN_EXTRATIME_PERIOD, outcome);
        /*
         * score a goal for B
         */
        // handballMatchIncident.set(HandballMatchIncidentType.GOAL, 5600, TeamId.B);
        // outcome = (HandballMatchPeriod) matchState.updateStateForIncident((MatchIncident) handballMatchIncident,
        // false);
        // assertEquals(0, matchState.getPreviousPeriodGoalsA());
        // assertEquals(3, matchState.getPreviousPeriodGoalsB());
        // assertEquals(1, matchState.getGoalsA());
        // assertEquals(5, matchState.getGoalsB());
        // assertEquals(0, matchState.getCurrentPeriodGoalsA());
        // assertEquals(1, matchState.getCurrentPeriodGoalsB());
        // assertEquals(5600, matchState.getElapsedTimeSecs());
        // assertEquals(200, matchState.getElapsedTimeThisPeriodSecs());
        // /*
        // * quite a bit of state info by now - test the copy
        // method */
        // HandballMatchState cc = (HandballMatchState)matchState.copy();
        // assertEquals (cc,matchState);

    }

    @Test
    public void testIncrementSimulationElapsedTime() {
        MethodName.log();
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        HandballMatchState matchState = new HandballMatchState(matchFormat);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        matchState.incrementSimulationElapsedTime(1600); // 10 secs before half time
        assertEquals(HandballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(1600, matchState.getElapsedTimeSecs());
        assertEquals(1600, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(300);
        /**
         * roll over to second half
         */
        assertEquals(HandballMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        // HIGH LIGHT, 1800 will be recored when switching period.
        // this is different from football matchstate test
        assertEquals(1800, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(30);
        assertEquals(1830, matchState.getElapsedTimeSecs());
        assertEquals(30, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(1800); // 10 seconds before match end
        assertEquals(HandballMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
    }

    @Ignore
    @Test
    public void testElapsedTimeMethods() {
        MethodName.log();
        HandballMatchFormat matchFormat = new HandballMatchFormat();
        HandballMatchState matchState = new HandballMatchState(matchFormat);
        HandballMatchIncident matchIncident = new HandballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        matchIncident.set(HandballMatchIncidentType.GOAL, 30, TeamId.A);
        matchState.updateStateForIncident((MatchIncident) matchIncident, false);
        matchState.setPriceCalcTime();
        assertEquals(0, matchState.getSecsSinceLastElapsedTimeFromIncident());
        sleep(2);
        assertEquals(2, matchState.getSecsSinceLastElapsedTimeFromIncident());
        boolean recalc = matchState.updateElapsedTime();
        assertFalse(recalc);
        assertEquals(32, matchState.getElapsedTimeSecs());
        sleep(5);
        assertEquals(7, matchState.getSecsSinceLastElapsedTimeFromIncident());
        recalc = matchState.updateElapsedTime();
        assertFalse(recalc);
        assertEquals(37, matchState.getElapsedTimeSecs());
        sleep(3);
        assertEquals(10, matchState.getSecsSinceLastElapsedTimeFromIncident());
        recalc = matchState.updateElapsedTime();
        assertTrue(recalc);
        assertEquals(40, matchState.getElapsedTimeSecs());
        matchIncident.set(HandballMatchIncidentType.GOAL, 38, TeamId.A);
        matchState.updateStateForIncident((MatchIncident) matchIncident, false);
        assertEquals(0, matchState.getSecsSinceLastElapsedTimeFromIncident());

    }

    private void sleep(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
