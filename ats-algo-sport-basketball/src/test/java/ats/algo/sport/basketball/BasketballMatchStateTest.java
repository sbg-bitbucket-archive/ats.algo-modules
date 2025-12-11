package ats.algo.sport.basketball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.basketball.BasketballMatchIncident;
import ats.algo.sport.basketball.BasketballMatchPeriod;
import ats.algo.sport.basketball.BasketballMatchState;
import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchIncidentType;

public class BasketballMatchStateTest {

    @Test
    public void testFourQuarters() {
        MethodName.log();
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setNormalTimeMinutes(40);
        matchFormat.setTwoHalvesFormat(false);

        BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        BasketballMatchIncident basketballMatchIncident = new BasketballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(BasketballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        BasketballMatchPeriod outcome = (BasketballMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.TWO_POINTS_SCORED, 30, TeamId.A);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(2, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(1, matchState.getPeriodNo());
        assertEquals("Q1", matchState.getSequenceIdForPeriod(0));
        /*
         * score away goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.TWO_POINTS_SCORED, 180, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(180, matchState.getElapsedTimeSecs());
        assertEquals(180, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 590);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(590, matchState.getElapsedTimeSecs());
        assertEquals(590, matchState.getElapsedTimeThisPeriodSecs());

        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(590, matchState.getElapsedTimeSecs());

        /*
         * roll over to second half
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 600);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2, matchState.getPreviousPeriodGoalsA());
        assertEquals(2, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.AT_FIRST_QUARTER_END, outcome);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 600);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_SECOND_QUARTER, outcome);
        /*
         * score two goals for B)
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 700, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 700, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(2, matchState.getPreviousPeriodGoalsA());
        assertEquals(2, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(6, matchState.getCurrentPeriodGoalsB());
        /*
         * check to see that all incidents update elapsed time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 800);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(800, matchState.getElapsedTimeSecs());
        assertEquals(200, matchState.getElapsedTimeThisPeriodSecs());
        /*
         * check injury time gets extended at the end of the second half
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1200);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(6, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.AT_SECOND_QUARTER_END, outcome);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1200);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(6, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.IN_THIRD_QUARTER, outcome);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1800);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(0, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.AT_THIRD_QUARTER_END, outcome);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1800);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(0, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.IN_FOURTH_QUARTER, outcome);


        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 2000, TeamId.A);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FOURTH_QUARTER, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FOURTH_QUARTER, matchState.getMatchPeriod());
        assertEquals(2000, matchState.getElapsedTimeSecs());
        assertEquals(3, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("Q4", matchState.getSequenceIdForPeriod(0));
        /*
         * score away goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 2000, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(5, matchState.getPointsA());
        assertEquals(11, matchState.getPointsB());
        assertEquals(3, matchState.getCurrentPeriodGoalsA());
        assertEquals(3, matchState.getCurrentPeriodGoalsB());
        assertEquals(2000, matchState.getElapsedTimeSecs());
        assertEquals(200, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2400);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(5, matchState.getPointsA());
        assertEquals(11, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.MATCH_COMPLETED, outcome);

    }

    @Ignore
    @Test
    public void testIncrementSimulationElapsedTime() {
        MethodName.log();
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        matchState.incrementSimulationElapsedTime(1600); // 10 secs before half time
        assertEquals(BasketballMatchPeriod.IN_FIRST_QUARTER, matchState.getMatchPeriod());
        assertEquals(1600, matchState.getElapsedTimeSecs());
        assertEquals(1600, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(300);
        /**
         * roll over to second half
         */
        assertEquals(BasketballMatchPeriod.IN_SECOND_QUARTER, matchState.getMatchPeriod());
        // HIGH LIGHT, 1800 will be recored when switching period.
        // this is different from football matchstate test
        assertEquals(1800, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(30);
        assertEquals(1830, matchState.getElapsedTimeSecs());
        assertEquals(30, matchState.getElapsedTimeThisPeriodSecs());
        matchState.incrementSimulationElapsedTime(1800); // 10 seconds before match end
        assertEquals(BasketballMatchPeriod.MATCH_COMPLETED, matchState.getMatchPeriod());



    }

    @Test
    public void testTwoHalves() {
        MethodName.log();
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setNormalTimeMinutes(40);
        matchFormat.setTwoHalvesFormat(true);

        BasketballMatchState matchState = new BasketballMatchState(matchFormat);
        BasketballMatchIncident basketballMatchIncident = new BasketballMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(BasketballMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * start the match
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        BasketballMatchPeriod outcome = (BasketballMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.TWO_POINTS_SCORED, 30, TeamId.A);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(2, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(1, matchState.getPeriodNo());
        assertEquals("H1", matchState.getSequenceIdForPeriod(0));
        /*
         * score away goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.TWO_POINTS_SCORED, 180, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(180, matchState.getElapsedTimeSecs());
        assertEquals(180, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 590);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(590, matchState.getElapsedTimeSecs());
        assertEquals(590, matchState.getElapsedTimeThisPeriodSecs());

        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(2, matchState.getPointsA());
        assertEquals(2, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(590, matchState.getElapsedTimeSecs());

        /*
         * roll over to second half
         */

        /*
         * score two goals for B)
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 700, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 700, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(0, matchState.getPreviousPeriodGoalsA());
        assertEquals(0, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(2, matchState.getCurrentPeriodGoalsA());
        assertEquals(8, matchState.getCurrentPeriodGoalsB());
        /*
         * check to see that all incidents update elapsed time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 800);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(800, matchState.getElapsedTimeSecs());
        assertEquals(800, matchState.getElapsedTimeThisPeriodSecs());
        /*
         * check injury time gets extended at the end of the second half
         */

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1200);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2, matchState.getPreviousPeriodGoalsA());
        assertEquals(8, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.AT_HALF_TIME, outcome);

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1200);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2, matchState.getPreviousPeriodGoalsA());
        assertEquals(8, matchState.getPreviousPeriodGoalsB());
        assertEquals(2, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.IN_SECOND_HALF, outcome);


        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 2000, TeamId.A);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(BasketballMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(BasketballMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2000, matchState.getElapsedTimeSecs());
        assertEquals(3, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(2, matchState.getPeriodNo());
        assertEquals("H2", matchState.getSequenceIdForPeriod(0));
        /*
         * score away goal
         */
        basketballMatchIncident.set(BasketballMatchIncidentType.THREE_POINTS_SCORED, 2000, TeamId.B);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) basketballMatchIncident,
                        false);
        assertEquals(5, matchState.getPointsA());
        assertEquals(11, matchState.getPointsB());
        assertEquals(3, matchState.getCurrentPeriodGoalsA());
        assertEquals(3, matchState.getCurrentPeriodGoalsB());
        assertEquals(2000, matchState.getElapsedTimeSecs());
        assertEquals(800, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2400);
        outcome = (BasketballMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(5, matchState.getPointsA());
        assertEquals(11, matchState.getPointsB());
        assertEquals(0, matchState.getCurrentPeriodGoalsA());
        assertEquals(0, matchState.getCurrentPeriodGoalsB());
        assertEquals(BasketballMatchPeriod.MATCH_COMPLETED, outcome);

    }



}
