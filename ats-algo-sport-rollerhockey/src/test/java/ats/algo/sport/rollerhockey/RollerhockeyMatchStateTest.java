package ats.algo.sport.rollerhockey;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.rollerhockey.RollerhockeyMatchFormat;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident;
import ats.algo.sport.rollerhockey.RollerhockeyMatchPeriod;
import ats.algo.sport.rollerhockey.RollerhockeyMatchState;
import ats.algo.sport.rollerhockey.RollerhockeyMatchIncident.RollerhockeyMatchIncidentType;

public class RollerhockeyMatchStateTest {

    @Test
    public void test() {
        RollerhockeyMatchFormat matchFormat = new RollerhockeyMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setPenaltiesPossible(true);

        RollerhockeyMatchState matchState = new RollerhockeyMatchState(matchFormat);

        RollerhockeyMatchIncident rollerhockeyMatchIncident = new RollerhockeyMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(RollerhockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        RollerhockeyMatchPeriod outcome = (RollerhockeyMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);

        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
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

        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 600, TeamId.B);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(600, matchState.getElapsedTimeSecs());
        assertEquals(600, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1200);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1200);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 1300);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1300, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2400);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2400);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2400, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2500);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());

        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2500, TeamId.A);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);

        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());
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


        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2540, TeamId.B);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);

        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2540, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(1, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());
        assertEquals(5, matchState.getGoalNo());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("G5", matchState.getSequenceIdForGoal(0));
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2550);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME, matchState.getMatchPeriod());
        assertEquals(2550, matchState.getElapsedTimeSecs());
        assertEquals(150, matchState.getElapsedTimeThisPeriodSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2550);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2550, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        /// team b score in extra time period
        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2560, TeamId.B);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());
        assertEquals(10, matchState.getElapsedTimeThisPeriodSecs());

        // // if ending the extra time period
        // elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3900);
        // outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // elapsedTimeMatchIncident, false);
        // assertEquals(RollerhockeyMatchPeriod.MATCH_COMPLETED, outcome);
        // assertEquals(2, matchState.getGoalsA());
        // assertEquals(3, matchState.getGoalsB());
        // assertEquals(RollerhockeyMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
        // assertEquals(3900, matchState.getElapsedTimeSecs());
        // assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());

        // if A score again
        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2560, TeamId.A);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());
        assertEquals(10, matchState.getElapsedTimeThisPeriodSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.AT_EXTRA_TIME_END, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(150, matchState.getElapsedTimeThisPeriodSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        // if A IN SHOOT OUT
        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2700, TeamId.A);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(RollerhockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(0, matchState.getShootOutGoalsB());

        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 2700, TeamId.B);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);
        assertEquals(RollerhockeyMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        // B SCORED
        rollerhockeyMatchIncident.set(RollerhockeyMatchIncidentType.GOAL, 5100, TeamId.B);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) rollerhockeyMatchIncident,
                        false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(2, matchState.getShootOutGoalsB());
        assertEquals(RollerhockeyMatchPeriod.MATCH_COMPLETED, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());

    }

    @Test
    public void testExtraTime() {
        // RollerhockeyMatchFormat matchFormat = new RollerhockeyMatchFormat();
        // matchFormat.setExtraTimeMinutes(30);
        // RollerhockeyMatchState matchState = new RollerhockeyMatchState(matchFormat);
        // RollerhockeyMatchIncident matchEvent = new RollerhockeyMatchIncident();
        // assertEquals(RollerhockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        // /*
        // * move forward one unit of time
        // */
        // matchEvent.set(RollerhockeyMatchIncidentType.TIME_UNIT_ELAPSED_NO_GOAL);
        // RollerhockeyMatchPeriod outcome = (RollerhockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // matchEvent);
        // assertEquals(RollerhockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        // assertEquals(0, matchState.getGoalsA());
        // assertEquals(0, matchState.getGoalsB());
        // assertEquals(RollerhockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        // assertEquals(10, matchState.getElapsedTimeSecs());

    }
}
