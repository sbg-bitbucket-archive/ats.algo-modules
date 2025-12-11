package ats.algo.sport.bowls;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.sport.bowls.BowlsMatchFormat;
import ats.algo.sport.bowls.BowlsMatchIncident;
import ats.algo.sport.bowls.BowlsMatchIncident.BowlsMatchIncidentType;
import ats.algo.sport.bowls.BowlsMatchIncidentResult;
import ats.algo.sport.bowls.BowlsMatchState;
import ats.algo.sport.bowls.BowlsMatchIncidentResult.BowlsMatchIncidentResultType;
import ats.algo.core.common.TeamId;

public class BowlsMatchStateTest {

    @Test
    public void test() {
        MethodName.log();
        BowlsMatchFormat matchFormat = new BowlsMatchFormat();
        matchFormat.setnSetsInMatch(3);
        BowlsMatchState matchState = new BowlsMatchState(matchFormat);
        BowlsMatchIncident volleyballMatchIncident = new BowlsMatchIncident();
        volleyballMatchIncident.set(BowlsMatchIncidentType.ENDWON1, TeamId.A);
        BowlsMatchIncidentResult outcome =
                        (BowlsMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);
        volleyballMatchIncident.set(BowlsMatchIncidentType.ENDWON3, TeamId.A);
        outcome = (BowlsMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);
        assertFalse(matchState.isMatchCompleted());
        volleyballMatchIncident.set(BowlsMatchIncidentType.ENDWON2, TeamId.A);
        for (int i = 0; i < 12; i++)
            outcome = (BowlsMatchIncidentResult) matchState.updateStateForIncident(volleyballMatchIncident, false);

        assertEquals(BowlsMatchIncidentResultType.MATCHWON, outcome.getBowlsMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());

    }
}
