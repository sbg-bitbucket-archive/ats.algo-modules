package ats.algo.sport.badminton;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchIncident.BadmintonMatchIncidentType;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult.BadmintonMatchIncidentResultType;
import ats.algo.sport.badminton.BadmintonMatchState;

public class BadmintonlMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        BadmintonMatchFormat matchFormat = new BadmintonMatchFormat();
        matchFormat.setnGamesInMatch(3);
        BadmintonMatchState matchState = new BadmintonMatchState(matchFormat);
        BadmintonMatchIncident badmintonMatchIncident = new BadmintonMatchIncident();
        badmintonMatchIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.A);
        BadmintonMatchIncidentResult outcome =
                        (BadmintonMatchIncidentResult) matchState.updateStateForIncident(badmintonMatchIncident, false);
        for (int i = 0; i < 21; i++) {
            badmintonMatchIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.A);
            outcome = (BadmintonMatchIncidentResult) matchState.updateStateForIncident(badmintonMatchIncident, false);
        }
        for (int i = 0; i < 21; i++) {
            badmintonMatchIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.B);
            outcome = (BadmintonMatchIncidentResult) matchState.updateStateForIncident(badmintonMatchIncident, false);
        }

        assertFalse(matchState.isMatchCompleted());
        for (int i = 0; i < 21; i++) {
            badmintonMatchIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.A);
            outcome = (BadmintonMatchIncidentResult) matchState.updateStateForIncident(badmintonMatchIncident, false);
        }
        assertEquals(BadmintonMatchIncidentResultType.MATCHWON, outcome.getBadmintonMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());

    }
}
