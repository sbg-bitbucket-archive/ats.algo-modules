package ats.algo.sport.fieldhockey;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.fieldhockey.FieldhockeyMatchFormat;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident;
import ats.algo.sport.fieldhockey.FieldhockeyMatchPeriod;
import ats.algo.sport.fieldhockey.FieldhockeyMatchState;
import ats.algo.sport.fieldhockey.FieldhockeyMatchIncident.FieldhockeyMatchIncidentType;

public class FieldhockeyMatchStateTest {

    @Test
    public void test() {
        FieldhockeyMatchFormat matchFormat = new FieldhockeyMatchFormat();
        matchFormat.setExtraTimeMinutes(15);
        matchFormat.setPenaltiesPossible(true);

        FieldhockeyMatchState matchState = new FieldhockeyMatchState(matchFormat);

        FieldhockeyMatchIncident fieldhockeyMatchIncident = new FieldhockeyMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(FieldhockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        FieldhockeyMatchPeriod outcome = (FieldhockeyMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);

        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
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

        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 1200, TeamId.B);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(1200, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 3000);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(3000, matchState.getElapsedTimeSecs());
        assertEquals(900, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 4200);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 4200);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(4200, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 4300);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(4300, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());

        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 4300, TeamId.A);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);

        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(4300, matchState.getElapsedTimeSecs());
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


        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 4400, TeamId.B);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);

        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(4400, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(1, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());
        assertEquals(5, matchState.getGoalNo());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("G5", matchState.getSequenceIdForGoal(0));
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 4650);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, matchState.getMatchPeriod());
        assertEquals(4650, matchState.getElapsedTimeSecs());
        assertEquals(450, matchState.getElapsedTimeThisPeriodSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 4650);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(4650, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        /// team b score in extra time period
        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 4750, TeamId.B);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(4750, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());

        // // if ending the extra time period
        // elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3900);
        // outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // elapsedTimeMatchIncident, false);
        // assertEquals(FieldhockeyMatchPeriod.MATCH_COMPLETED, outcome);
        // assertEquals(2, matchState.getGoalsA());
        // assertEquals(3, matchState.getGoalsB());
        // assertEquals(FieldhockeyMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
        // assertEquals(3900, matchState.getElapsedTimeSecs());
        // assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());

        // if A score again
        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 4850, TeamId.A);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(4850, matchState.getElapsedTimeSecs());
        assertEquals(200, matchState.getElapsedTimeThisPeriodSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 5100);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.AT_EXTRA_TIME_END, matchState.getMatchPeriod());
        assertEquals(5100, matchState.getElapsedTimeSecs());
        assertEquals(450, matchState.getElapsedTimeThisPeriodSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 5100);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(5100, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        // if A IN SHOOT OUT
        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 5100, TeamId.A);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(FieldhockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(5100, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(0, matchState.getShootOutGoalsB());

        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 5100, TeamId.B);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);
        assertEquals(FieldhockeyMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 5100);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        // B SCORED
        fieldhockeyMatchIncident.set(FieldhockeyMatchIncidentType.GOAL, 5100, TeamId.B);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) fieldhockeyMatchIncident,
                        false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 5100);
        outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(5100, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(2, matchState.getShootOutGoalsB());
        assertEquals(FieldhockeyMatchPeriod.MATCH_COMPLETED, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());

    }

    @Test
    public void testExtraTime() {
        // FieldhockeyMatchFormat matchFormat = new FieldhockeyMatchFormat();
        // matchFormat.setExtraTimeMinutes(30);
        // FieldhockeyMatchState matchState = new FieldhockeyMatchState(matchFormat);
        // FieldhockeyMatchIncident matchEvent = new FieldhockeyMatchIncident();
        // assertEquals(FieldhockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        // /*
        // * move forward one unit of time
        // */
        // matchEvent.set(FieldhockeyMatchIncidentType.TIME_UNIT_ELAPSED_NO_GOAL);
        // FieldhockeyMatchPeriod outcome = (FieldhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // matchEvent);
        // assertEquals(FieldhockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        // assertEquals(0, matchState.getGoalsA());
        // assertEquals(0, matchState.getGoalsB());
        // assertEquals(FieldhockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        // assertEquals(10, matchState.getElapsedTimeSecs());

    }
}
