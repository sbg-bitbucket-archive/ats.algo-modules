package ats.algo.sport.volleyball;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchIncident;
import ats.algo.sport.volleyball.VolleyballMatchIncident.VolleyballMatchIncidentType;
import ats.algo.sport.volleyball.VolleyballMatchIncidentResult;
import ats.algo.sport.volleyball.VolleyballMatchIncidentResult.VolleyballMatchIncidentResultType;
import ats.algo.sport.volleyball.VolleyballMatchState;

public class VolleyballMatchStateTest {

    @Test
    public void test() {
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        matchFormat.setnSetsInMatch(5);
        VolleyballMatchState matchState = new VolleyballMatchState(matchFormat);
        VolleyballMatchIncident volleyballMatchIncident = new VolleyballMatchIncident();
        VolleyballMatchIncidentResult outcome =
                        new VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType.POINTWON);
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.B);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);
        }
        assertFalse(matchState.isMatchCompleted());
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        assertEquals(VolleyballMatchIncidentResultType.MATCHWON, outcome.getVolleyballMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());
        matchFormat.setnSetsInMatch(3);
        matchState = new VolleyballMatchState(matchFormat);
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        for (int i = 0; i < 25; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.B);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        assertFalse(matchState.isMatchCompleted());
        for (int i = 0; i < 15; i++) {
            volleyballMatchIncident.set(VolleyballMatchIncidentType.POINTWON, TeamId.A);
            outcome = (VolleyballMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        }
        assertEquals(VolleyballMatchIncidentResultType.MATCHWON, outcome.getVolleyballMatchIncidentResultType());

    }
}
