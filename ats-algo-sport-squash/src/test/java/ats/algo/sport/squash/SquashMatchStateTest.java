package ats.algo.sport.squash;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.squash.SquashMatchFormat;
import ats.algo.sport.squash.SquashMatchIncident;
import ats.algo.sport.squash.SquashMatchIncident.SquashMatchIncidentType;
import ats.algo.sport.squash.SquashMatchIncidentResult;
import ats.algo.sport.squash.SquashMatchIncidentResult.SquashMatchIncidentResultType;
import ats.algo.sport.squash.SquashMatchState;

public class SquashMatchStateTest {

    @Test
    public void test() {
        SquashMatchFormat matchFormat = new SquashMatchFormat();
        matchFormat.setnGamesInMatch(5);
        SquashMatchState matchState = new SquashMatchState(matchFormat);
        SquashMatchIncident squashMatchIncident = new SquashMatchIncident();
        squashMatchIncident.set(SquashMatchIncidentType.GAMEWON, TeamId.A);
        SquashMatchIncidentResult outcome =
                        (SquashMatchIncidentResult) matchState.updateStateForIncident(squashMatchIncident, false);
        for (int i = 0; i < 11; i++) {
            squashMatchIncident.set(SquashMatchIncidentType.POINTWON, TeamId.A);
            outcome = (SquashMatchIncidentResult) matchState.updateStateForIncident(squashMatchIncident, false);
        }
        for (int i = 0; i < 11; i++) {
            squashMatchIncident.set(SquashMatchIncidentType.POINTWON, TeamId.A);
            outcome = (SquashMatchIncidentResult) matchState.updateStateForIncident(squashMatchIncident, false);
        }
        for (int i = 0; i < 11; i++) {
            squashMatchIncident.set(SquashMatchIncidentType.POINTWON, TeamId.B);
            outcome = (SquashMatchIncidentResult) matchState.updateStateForIncident(squashMatchIncident, false);
        }
        for (int i = 0; i < 11; i++) {
            squashMatchIncident.set(SquashMatchIncidentType.POINTWON, TeamId.A);
            outcome = (SquashMatchIncidentResult) matchState.updateStateForIncident(squashMatchIncident, false);
        }

        assertEquals(SquashMatchIncidentResultType.MATCHWON, outcome.getSquashMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());

    }
}
