package ats.algo.sport.icehockey;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.icehockey.IcehockeyMatchFormat;
import ats.algo.sport.icehockey.IcehockeyMatchIncident;
import ats.algo.sport.icehockey.IcehockeyMatchPeriod;
import ats.algo.sport.icehockey.IcehockeyMatchState;
import ats.algo.sport.icehockey.IcehockeyMatchIncident.IcehockeyMatchIncidentType;

public class IcehockeyMatchStateTest {

    @Test
    public void test() {
        IcehockeyMatchFormat matchFormat = new IcehockeyMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setPenaltiesPossible(true);

        IcehockeyMatchState matchState = new IcehockeyMatchState(matchFormat);

        IcehockeyMatchIncident icehockeyMatchIncident = new IcehockeyMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(IcehockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        IcehockeyMatchPeriod outcome = (IcehockeyMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);

        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
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

        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 1200, TeamId.B);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(1200, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1200);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1200);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_SECOND_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_SECOND_PERIOD, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2200);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_SECOND_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_SECOND_PERIOD, matchState.getMatchPeriod());
        assertEquals(2200, matchState.getElapsedTimeSecs());
        assertEquals(1000, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2400);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2400);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, matchState.getMatchPeriod());
        assertEquals(2400, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 3000);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, matchState.getMatchPeriod());
        assertEquals(3000, matchState.getElapsedTimeSecs());
        assertEquals(600, matchState.getElapsedTimeThisPeriodSecs());

        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 3300, TeamId.A);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);

        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, matchState.getMatchPeriod());
        assertEquals(3300, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        assertEquals(4, matchState.getGoalNo());
        assertEquals(3, matchState.getPeriodNo());
        assertEquals("G4", matchState.getSequenceIdForGoal(0));
        assertEquals("P3", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        /*
         * score away goal for going into extra time
         */


        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 3400, TeamId.B);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);

        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_THIRD_PERIOD, matchState.getMatchPeriod());
        assertEquals(3400, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(1, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());
        assertEquals(5, matchState.getGoalNo());
        assertEquals(3, matchState.getPeriodNo());
        assertEquals("G5", matchState.getSequenceIdForGoal(0));
        assertEquals("P3", matchState.getSequenceIdForPeriod(0));
        assertEquals(TeamId.B, matchState.getTeamScoringLastGoal());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3600);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.AT_FULL_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.AT_FULL_TIME, matchState.getMatchPeriod());
        assertEquals(3600, matchState.getElapsedTimeSecs());
        assertEquals(1200, matchState.getElapsedTimeThisPeriodSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 3600);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(2, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, matchState.getMatchPeriod());
        assertEquals(3600, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        /// team b score in extra time period
        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 3700, TeamId.B);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, outcome);
        assertEquals(2, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, matchState.getMatchPeriod());
        assertEquals(3700, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());

        // // if ending the extra time period
        // elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3900);
        // outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
        // false);
        // assertEquals(IcehockeyMatchPeriod.MATCH_COMPLETED, outcome);
        // assertEquals(2, matchState.getGoalsA());
        // assertEquals(3, matchState.getGoalsB());
        // assertEquals(IcehockeyMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
        // assertEquals(3900, matchState.getElapsedTimeSecs());
        // assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());

        // if A score again
        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 3750, TeamId.A);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_EXTRA_TIME, matchState.getMatchPeriod());
        assertEquals(3750, matchState.getElapsedTimeSecs());
        assertEquals(150, matchState.getElapsedTimeThisPeriodSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 3900);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.AT_EXTRA_PERIOD_END, matchState.getMatchPeriod());
        assertEquals(3900, matchState.getElapsedTimeSecs());
        assertEquals(300, matchState.getElapsedTimeThisPeriodSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 3900);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(3900, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        // if A IN SHOOT OUT
        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 4000, TeamId.A);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(IcehockeyMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(4000, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(0, matchState.getShootOutGoalsB());

        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 4000, TeamId.B);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);
        assertEquals(IcehockeyMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 4000);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        // B SCORED
        icehockeyMatchIncident.set(IcehockeyMatchIncidentType.GOAL, 4000, TeamId.B);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) icehockeyMatchIncident,
                        false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 4000);
        outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(4000, matchState.getElapsedTimeSecs());
        assertEquals(100, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(1, matchState.getShootOutGoalsA());
        assertEquals(2, matchState.getShootOutGoalsB());
        assertEquals(IcehockeyMatchPeriod.MATCH_COMPLETED, outcome);
        assertEquals(3, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());

    }

    @Test
    public void testExtraTime() {
        // IcehockeyMatchFormat matchFormat = new IcehockeyMatchFormat();
        // matchFormat.setExtraTimeMinutes(30);
        // IcehockeyMatchState matchState = new IcehockeyMatchState(matchFormat);
        // IcehockeyMatchIncident matchEvent = new IcehockeyMatchIncident();
        // assertEquals(IcehockeyMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        // /*
        // * move forward one unit of time
        // */
        // matchEvent.set(IcehockeyMatchIncidentType.TIME_UNIT_ELAPSED_NO_GOAL);
        // IcehockeyMatchPeriod outcome = (IcehockeyMatchPeriod) matchState.updateStateForIncident((MatchIncident)
        // matchEvent);
        // assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, outcome);
        // assertEquals(0, matchState.getGoalsA());
        // assertEquals(0, matchState.getGoalsB());
        // assertEquals(IcehockeyMatchPeriod.IN_FIRST_PERIOD, matchState.getMatchPeriod());
        // assertEquals(10, matchState.getElapsedTimeSecs());

    }
}
