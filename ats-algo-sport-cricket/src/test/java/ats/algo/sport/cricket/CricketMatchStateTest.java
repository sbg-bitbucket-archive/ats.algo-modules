package ats.algo.sport.cricket;


import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.cricket.CricketMatchIncident.CricketMatchIncidentType;

public class CricketMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        CricketMatchState matchState = new CricketMatchState(matchFormat);
        CricketMatchIncident cricketMatchIncident = new CricketMatchIncident();
        cricketMatchIncident.set(CricketMatchIncidentType.BATFIRST, TeamId.A);

        CricketMatchIncidentResult outcome =
                        (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        // System.out.println(matchState);
        cricketMatchIncident.set(CricketMatchIncidentType.RUN1, TeamId.A);
        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        assertFalse(matchState.isMatchCompleted());
        assertEquals(matchState.getBat(), TeamId.A);

        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        assertFalse(matchState.isMatchCompleted());

        for (int i = 0; i < 10; i++) {
            cricketMatchIncident.set(CricketMatchIncidentType.WICKET_CAUGHT, TeamId.A);
            outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);

        }
        assertEquals(matchState.getBat(), TeamId.B);
        cricketMatchIncident.set(CricketMatchIncidentType.RUN1, TeamId.B);
        outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        for (int i = 0; i < 10; i++) {
            cricketMatchIncident.set(CricketMatchIncidentType.WICKET_CAUGHT);
            outcome = (CricketMatchIncidentResult) matchState.updateStateForIncident(cricketMatchIncident, false);
        }

        assertEquals(outcome.getTeamId(), TeamId.A);
        assertTrue(matchState.isMatchCompleted());
        // System.out.println(matchState);


    }
}
