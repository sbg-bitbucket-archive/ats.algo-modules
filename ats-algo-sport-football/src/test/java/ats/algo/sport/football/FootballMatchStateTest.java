package ats.algo.sport.football;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;

public class FootballMatchStateTest {

    @Test
    public void testShootout() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        matchFormat.setExtraTimeMinutes(30);
        matchFormat.setPenaltiesPossible(true);

        FootballMatchState matchState = new FootballMatchState(matchFormat);
        FootballMatchIncident footballMatchIncident = new FootballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(FootballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        FootballMatchPeriod outcome = (FootballMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(120, matchState.getInjuryTimeSecs());
        assertEquals(0, matchState.getElapsedTimeSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        /*
         * Penalty
         */
        assertEquals(FootballMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());


        footballMatchIncident.set(FootballMatchIncidentType.SHOOTOUT_START, 120 * 60, TeamId.A);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);

        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 120 * 60, TeamId.A);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);

        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 120 * 60, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);

        // System.out.println(matchState.getPenaltyInfo().getShootoutListStatusA().toString());
        // System.out.println(matchState.getPenaltyInfo().getShootoutListStatusB().toString());

    }


    @Test
    public void test() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        matchFormat.setExtraTimeMinutes(15);
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        FootballMatchIncident footballMatchIncident = new FootballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(FootballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        FootballMatchPeriod outcome = (FootballMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(120, matchState.getInjuryTimeSecs());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 30, TeamId.A);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(0, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(1, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        assertEquals(2, matchState.getGoalNo());
        assertEquals(1, matchState.getPeriodNo());
        assertEquals("G2", matchState.getGoalSequenceId());
        assertEquals("H1", matchState.getPeriodSequenceId());
        assertEquals(TeamId.A, matchState.getTeamScoringLastGoal());
        /*
         * score away goal
         */
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 80, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(120, matchState.getInjuryTimeSecs());
        assertEquals(80, matchState.getElapsedTimeSecs());
        assertEquals(80, matchState.getElapsedTimeThisPeriodSecs());
        /*
         * test injury time + rollovers half time
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2680);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(120, matchState.getInjuryTimeSecs());
        assertEquals(2680, matchState.getElapsedTimeSecs());
        assertEquals(2680, matchState.getElapsedTimeThisPeriodSecs());

        FootballMatchIncident injuryTimeIncident =
                        new FootballMatchIncident(FootballMatchIncidentType.SET_INJURY_TIME, 2680, 15);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) injuryTimeIncident, false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(15, matchState.getInjuryTimeSecs());
        assertEquals(2680, matchState.getElapsedTimeSecs());
        /*
         * now go past the expected elapsed time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2720);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(80, matchState.getInjuryTimeSecs()); // should have added a minute of injury time
        /*
         * roll over to second half
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2740);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(1, matchState.getPreviousPeriodGoalsA());
        assertEquals(1, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(1, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.AT_HALF_TIME, outcome);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FootballMatchPeriod.IN_SECOND_HALF, outcome);
        /*
         * score two goals for B)
         */
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 2800, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 2900, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(1, matchState.getPreviousPeriodGoalsA());
        assertEquals(1, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(3, matchState.getGoalsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(2, matchState.getCurrentPeriodGoalsB());
        /*
         * check to see that all incidents update elapsed time
         */
        footballMatchIncident.set(FootballMatchIncidentType.DANGEROUS_ATTACK, 4600, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(4600, matchState.getElapsedTimeSecs());
        assertEquals(1900, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(180, matchState.getInjuryTimeSecs());
        /*
         * check injury time gets extended at the end of the second half
         */
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 6000, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(6000, matchState.getElapsedTimeSecs());
        assertEquals(3300, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(660, matchState.getInjuryTimeSecs());
        /*
         * roll over the the first period of extra time
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2740);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(3, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(4, matchState.getGoalsB());
        assertEquals(FootballMatchPeriod.MATCH_COMPLETED, outcome);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 5400);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(FootballMatchPeriod.MATCH_COMPLETED, outcome);
        /*
         * score a goal for B
         */
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 5600, TeamId.B);
        outcome = (FootballMatchPeriod) matchState.updateStateForIncident((MatchIncident) footballMatchIncident, false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(3, matchState.getPreviousPeriodGoalsB());
        assertEquals(1, matchState.getGoalsA());
        assertEquals(5, matchState.getGoalsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(1, matchState.getCurrentPeriodGoalsB());
        assertEquals(5600, matchState.getElapsedTimeSecs());
        assertEquals(40, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(660, matchState.getInjuryTimeSecs());
        /*
         * quite a bit of state info by now - test the copy method
         */
        FootballMatchState cc = (FootballMatchState) matchState.copy();
        // System.out.println(matchState);
        // System.out.println(cc);
        assertEquals(cc, matchState);

    }

    @Test
    public void testIncrementSimulationElapsedTime() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        matchState.incrementSimulationElapsedTime(2810); // 10 secs before half time
        assertEquals(FootballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2810, matchState.getElapsedTimeSecs());
        assertEquals(2810, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(20);
        /**
         * roll over to second half
         */
        assertEquals(FootballMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        assertEquals(180, matchState.getInjuryTimeSecs());
        matchState.incrementSimulationElapsedTime(30);
        assertEquals(2730, matchState.getElapsedTimeSecs());
        assertEquals(30, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(2840); // 10 seconds before match end
        assertEquals(FootballMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        matchState.incrementSimulationElapsedTime(10);
        assertEquals(FootballMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());
    }

    @Test
    public void testElapsedTimeMethods() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);
        FootballMatchIncident matchIncident = new FootballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        matchIncident.set(FootballMatchIncidentType.GOAL, 30, TeamId.A);
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
        matchState.updateElapsedTime();
        assertEquals(40, matchState.getElapsedTimeSecs());
        matchIncident.set(FootballMatchIncidentType.GOAL, 38, TeamId.A);
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

    @Test
    public void setIncidentTeamIdTest() {
        MethodName.log();
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballMatchState matchState = new FootballMatchState(matchFormat);

        // Null test
        FootballMatchIncident footballMatchIncident = null;
        matchState.setIncidentTeamId(footballMatchIncident);
        assertEquals(matchState.getIncidentTeamId(), TeamId.UNKNOWN.toString());

        footballMatchIncident = new FootballMatchIncident();
        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 30, TeamId.A);
        matchState.setIncidentTeamId(footballMatchIncident);
        assertEquals(matchState.getIncidentTeamId(), TeamId.A.toString());

        footballMatchIncident.set(FootballMatchIncidentType.GOAL, 30, null);
        matchState.setIncidentTeamId(footballMatchIncident);
        assertEquals(matchState.getIncidentTeamId(), TeamId.UNKNOWN.toString());

    }

}
