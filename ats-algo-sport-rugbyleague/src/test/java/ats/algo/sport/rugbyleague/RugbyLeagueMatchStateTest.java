package ats.algo.sport.rugbyleague;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchFormat;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchPeriod;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchState;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchIncident.RugbyLeagueMatchIncidentType;

public class RugbyLeagueMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        RugbyLeagueMatchFormat matchFormat = new RugbyLeagueMatchFormat();
        matchFormat.setExtraTimeMinutes(5);
        matchFormat.setPenaltiesPossible(true);

        RugbyLeagueMatchState matchState = new RugbyLeagueMatchState(matchFormat);

        RugbyLeagueMatchIncident RugbyLeagueMatchIncident = new RugbyLeagueMatchIncident();
        ElapsedTimeMatchIncident elapsedTimeMatchIncident;
        assertEquals(RugbyLeagueMatchPeriod.PREMATCH, matchState.getMatchPeriod());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * move forward one unit of time
         */
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        RugbyLeagueMatchPeriod outcome = (RugbyLeagueMatchPeriod) matchState
                        .updateStateForIncident((MatchIncident) elapsedTimeMatchIncident, false);

        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(0, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(0, matchState.getElapsedTimeSecs());
        /*
         * score home Point
         */
        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 30, TeamId.A);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);

        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(0, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(30, matchState.getElapsedTimeSecs());
        assertEquals(4, matchState.getCurrentPeriodPointsA());
        assertEquals(0, matchState.getCurrentPeriodPointsB());
        assertEquals(1, matchState.getPeriodNo());
        /*
         * score away Point
         */

        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 600, TeamId.B);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(600, matchState.getElapsedTimeSecs());
        assertEquals(600, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 1200);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 1200);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1200, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 1300);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_SECOND_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(1300, matchState.getElapsedTimeSecs());

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2400);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2400);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2400, matchState.getElapsedTimeSecs());
        assertEquals(0, matchState.getElapsedTimeThisPeriodSecs());


        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2500);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(4, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());

        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2500, TeamId.A);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);

        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(8, matchState.getPointsA());
        assertEquals(4, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2500, matchState.getElapsedTimeSecs());
        assertEquals(4, matchState.getCurrentPeriodPointsA());
        assertEquals(0, matchState.getCurrentPeriodPointsB());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));
        /*
         * score away Point for going into extra time
         */


        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2540, TeamId.B);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);

        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, outcome);
        assertEquals(8, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF, matchState.getMatchPeriod());
        assertEquals(2540, matchState.getElapsedTimeSecs());
        assertEquals(4, matchState.getCurrentPeriodPointsA());
        assertEquals(4, matchState.getCurrentPeriodPointsB());
        assertEquals(4, matchState.getPeriodNo());
        assertEquals("P4", matchState.getSequenceIdForPeriod(0));

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2550);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME, outcome);
        assertEquals(8, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.AT_EXTRA_TIME_HALF_TIME, matchState.getMatchPeriod());
        assertEquals(5100, matchState.getElapsedTimeSecs());


        ///////////////// extra time starts/////////////////////

        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2550);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(8, matchState.getPointsA());
        assertEquals(8, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2550, matchState.getElapsedTimeSecs());

        /// team b score in extra time period
        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2560, TeamId.B);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(8, matchState.getPointsA());
        assertEquals(12, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());

        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2560, TeamId.A);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, outcome);
        assertEquals(12, matchState.getPointsA());
        assertEquals(12, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF, matchState.getMatchPeriod());
        assertEquals(2560, matchState.getElapsedTimeSecs());

        // going to penalty
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.AT_EXTRA_TIME_END, outcome);
        assertEquals(12, matchState.getPointsA());
        assertEquals(12, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.AT_EXTRA_TIME_END, matchState.getMatchPeriod());
        assertEquals(5400, matchState.getElapsedTimeSecs());

        // TESTING SHOOT OUT PERIOD
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2700);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(12, matchState.getPointsA());
        assertEquals(12, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());

        // if A IN SHOOT OUT
        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2700, TeamId.A);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(16, matchState.getPointsA());
        assertEquals(12, matchState.getPointsB());
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, matchState.getMatchPeriod());
        assertEquals(2700, matchState.getElapsedTimeSecs());

        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 2700, TeamId.B);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, outcome);


        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        // B SCORED
        RugbyLeagueMatchIncident.set(RugbyLeagueMatchIncidentType.TRY, 5100, TeamId.B);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) RugbyLeagueMatchIncident,
                        false);

        // NOTHING HAPPEND, ASSUME A MISSED A SHOT
        elapsedTimeMatchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 2700);
        outcome = (RugbyLeagueMatchPeriod) matchState.updateStateForIncident((MatchIncident) elapsedTimeMatchIncident,
                        false);
        assertEquals(2700, matchState.getElapsedTimeSecs());
        assertEquals(RugbyLeagueMatchPeriod.IN_SHOOTOUT, outcome);
        assertEquals(16, matchState.getPointsA());
        assertEquals(20, matchState.getPointsB());

    }

}
