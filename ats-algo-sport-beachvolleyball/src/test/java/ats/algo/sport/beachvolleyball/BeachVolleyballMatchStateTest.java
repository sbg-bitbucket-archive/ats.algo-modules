package ats.algo.sport.beachvolleyball;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncident;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncidentResult;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchState;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncident.BeachVolleyballMatchIncidentType;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchIncidentResult.BeachVolleyballMatchIncidentResultType;

public class BeachVolleyballMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        BeachVolleyballMatchFormat matchFormat = new BeachVolleyballMatchFormat();
        matchFormat.setnSetsInMatch(3);
        BeachVolleyballMatchState matchState = new BeachVolleyballMatchState(matchFormat);
        BeachVolleyballMatchIncident volleyballMatchIncident = new BeachVolleyballMatchIncident();
        volleyballMatchIncident.set(BeachVolleyballMatchIncidentType.POINTWON, TeamId.A);
        BeachVolleyballMatchIncidentResult outcome = (BeachVolleyballMatchIncidentResult) matchState
                        .updateStateForIncident(volleyballMatchIncident, false);
        for (int i = 0; i < 21; i++) {
            volleyballMatchIncident.set(BeachVolleyballMatchIncidentType.POINTWON, TeamId.B);
            outcome = (BeachVolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident,
                            false);
        }
        assertFalse(matchState.isMatchCompleted());
        for (int i = 0; i < 21; i++) {
            volleyballMatchIncident.set(BeachVolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (BeachVolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident,
                            false);
        }
        for (int i = 0; i < 15; i++) {
            outcome = (BeachVolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident,
                            false);
        }
        assertEquals(BeachVolleyballMatchIncidentResultType.MATCHWON, outcome.getVolleyballMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());

    }
}
