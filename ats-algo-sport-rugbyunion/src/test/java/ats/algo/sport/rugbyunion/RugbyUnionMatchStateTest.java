package ats.algo.sport.rugbyunion;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident;
import ats.algo.sport.rugbyunion.RugbyUnionMatchPeriod;
import ats.algo.sport.rugbyunion.RugbyUnionMatchState;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident.RugbyUnionMatchIncidentType;

public class RugbyUnionMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyUnionMatchFormat matchFormat = new RugbyUnionMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setPenaltiesPossible(true);

        RugbyUnionMatchState matchState = new RugbyUnionMatchState(matchFormat);

        RugbyUnionMatchIncident RugbyUnionMatchIncident = new RugbyUnionMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(RugbyUnionMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        RugbyUnionMatchPeriod outcome = (RugbyUnionMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home Point
         */
        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 30, TeamId.A);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);

        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(5, matchState.getCurrentPeriodPointsA());
        assertEquals(0, matchState.getCurrentPeriodPointsB());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * score away Point
         */

        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 600, TeamId.B);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(600, matchState.getElapsedTimeSecs());
        assertEquals(600, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1500);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1500);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1500, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 1300);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1300, matchState.getElapsedTimeSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2500);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2500);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2500);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(5, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());

        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2500, TeamId.A);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);

        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(10, matchState.getPointsA());
        assertEquals(5, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());
        assertEquals(5, matchState.getCurrentPeriodPointsA());
        assertEquals(0, matchState.getCurrentPeriodPointsB());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        /*
         * score away Point for going into extra time
         */


        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2550, TeamId.B);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);

        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(10, matchState.getPointsA());
        assertEquals(10, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2550, matchState.getElapsedTimeSecs());
        assertEquals(5, matchState.getCurrentPeriodPointsA());
        assertEquals(5, matchState.getCurrentPeriodPointsB());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2550);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.AT_EXTRA_TIME_HALF_TIME, outcome);
        assertEquals(10, matchState.getPointsA());
        assertEquals(10, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.AT_EXTRA_TIME_HALF_TIME, matchState.getMatchPeriod());
        assertEquals(5100, matchState.getElapsedTimeSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2550);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(10, matchState.getPointsA());
        assertEquals(10, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2550, matchState.getElapsedTimeSecs());

        /// team b score in extra time period
        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2560, TeamId.B);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(10, matchState.getPointsA());
        assertEquals(15, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());

        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2560, TeamId.A);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(15, matchState.getPointsA());
        assertEquals(15, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(15, matchState.getPointsA());
        assertEquals(15, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.AT_EXTRA_TIME_END, matchState.getMatchPeriod());
        assertEquals(5400, matchState.getElapsedTimeSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(15, matchState.getPointsA());
        assertEquals(15, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());

        // if A IN SHOOT OUT
        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2700, TeamId.A);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(20, matchState.getPointsA());
        assertEquals(15, matchState.getPointsB());
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());

        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 2700, TeamId.B);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        // B SCORED
        RugbyUnionMatchIncident.set(RugbyUnionMatchIncidentType.TRY, 5100, TeamId.B);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyUnionMatchIncident,
                        false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RugbyUnionMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(RugbyUnionMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(20, matchState.getPointsA());
        assertEquals(25, matchState.getPointsB());

    }

}
