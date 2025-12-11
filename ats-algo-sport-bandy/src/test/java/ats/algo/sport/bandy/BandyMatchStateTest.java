package ats.algo.sport.bandy;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.bandy.BandyMatchFormat;
import ats.algo.sport.bandy.BandyMatchIncident;
import ats.algo.sport.bandy.BandyMatchPeriod;
import ats.algo.sport.bandy.BandyMatchState;
import ats.algo.sport.bandy.BandyMatchIncident.BandyMatchIncidentType;

public class BandyMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        BandyMatchFormat matchFormat = new BandyMatchFormat();
        matchFormat.setExtraTimeMinutes(15);
        matchFormat.setPenaltiesPossible(true);

        BandyMatchState matchState = new BandyMatchState(matchFormat);

        BandyMatchIncident bandyMatchIncident = new BandyMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(BandyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        BandyMatchPeriod outcome = (BandyMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);

        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
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

        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 1200, TeamId.B);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(1200, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 3000);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(3000, matchState.getElapsedTimeSecs());
        assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 5400);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 5400);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(5400, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 5600);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(5600, matchState.getElapsedTimeSecs());
        assertEquals(200, matchState.getElapsedTimeThisPeriodSecs());

        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 5600, TeamId.A);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);

        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(5600, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        assertEquals(4, matchState.getGoalNo());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("G4", matchState.getSequenceIdForGoal(0));
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        /*
         * score away goal for going into extra time
         */


        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 5700, TeamId.B);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);

        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(5700, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(1, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());
        assertEquals(5, matchState.getGoalNo());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("G5", matchState.getSequenceIdForGoal(0));
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 6300);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, matchState.getMatchPeriod());
        assertEquals(6300, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 6300);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(6300, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        /// team b score in extra time period
        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 6400, TeamId.B);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(6400, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());

        // // if ending the extra time period
        // elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3900);
        // outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
        // false);
        // assertEquals(BandyMatchPeriod.MATCH_COMPLETED, outcome);
        // assertEquals(2, matchState.getGoalsA());
        // assertEquals(3, matchState.getGoalsB());
        // assertEquals(BandyMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
        // assertEquals(3900, matchState.getElapsedTimeSecs());
        // assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());

        // if A score again
        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 6450, TeamId.A);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(6450, matchState.getElapsedTimeSecs());
        assertEquals(150, matchState.getElapsedTimeThisPeriodSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 7200);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.AT_EXTRA_TIME_END, matchState.getMatchPeriod());
        assertEquals(7200, matchState.getElapsedTimeSecs());
        assertEquals(15 * 60, matchState.getElapsedTimeThisPeriodSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 7200);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(7200, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        // if A IN SHOOT OUT
        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 7300, TeamId.A);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(BandyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(7300, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(0, matchState.getShootOutGoalsB());

        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 7300, TeamId.B);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);
        assertEquals(BandyMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 7300);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        // B SCORED
        bandyMatchIncident.set(BandyMatchIncidentType.GOAL, 7300, TeamId.B);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) bandyMatchIncident, false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 7300);
        outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(7300, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(2, matchState.getShootOutGoalsB());
        assertEquals(BandyMatchPeriod.MATCH_COMPLETED, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());

    }

    @Test
    public void testExtraTime() {
        MethodName.log();
        // BandyMatchFormat matchFormat = new BandyMatchFormat();
        // matchFormat.setExtraTimeMinutes(30);
        // BandyMatchState matchState = new BandyMatchState(matchFormat);
        // BandyMatchIncident matchEvent = new BandyMatchIncident();
        // assertEquals(BandyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        // /*
        // * move forward one unit of time
        // */
        // matchEvent.set(BandyMatchIncidentType.TIME_UNIT_ELAPSED_NO_GOAL);
        // BandyMatchPeriod outcome = (BandyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // matchEvent);
        // assertEquals(BandyMatchPeriod.IN_FIRST_PERIOD, outcome);
        // assertEquals(0, matchState.getGoalsA());
        // assertEquals(0, matchState.getGoalsB());
        // assertEquals(BandyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        // assertEquals(10, matchState.getElapsedTimeSecs());

    }
}
