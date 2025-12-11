package ats.algo.sport.snooker;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.snooker.SnookerMatchIncident;
import ats.algo.sport.snooker.SnookerMatchIncidentResult;
import ats.algo.sport.snooker.SnookerMatchState;
import ats.algo.sport.snooker.SnookerMatchIncident.SnookerMatchIncidentType;
import ats.algo.sport.snooker.SnookerMatchIncidentResult.SnookerMatchIncidentResultType;

public class SnookerMatchStateTest {

    @Test
    public void test() {
        SnookerMatchFormat matchFormat = new SnookerMatchFormat();
        matchFormat.setnFramesInMatch(5);
        SnookerMatchState matchState = new SnookerMatchState(matchFormat);
        SnookerMatchIncident snookerMatchIncident = new SnookerMatchIncident();
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.A);
        SnookerMatchIncidentResult outcome =
                        (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.A);
        outcome = (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        assertFalse(matchState.isMatchCompleted());
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.A);
        outcome = (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        assertEquals(SnookerMatchIncidentResultType.MATCHWON, outcome.getSnookerMatchIncidentResultType());
        assertTrue(matchState.isMatchCompleted());
        matchFormat.setnFramesInMatch(3);
        matchState = new SnookerMatchState(matchFormat);
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.A);
        outcome = (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.B);
        outcome = (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        assertFalse(matchState.isMatchCompleted());
        snookerMatchIncident.set(SnookerMatchIncidentType.FRAMEWON);
        snookerMatchIncident.setTeamId(TeamId.A);
        outcome = (SnookerMatchIncidentResult) matchState.updateStateForIncident(snookerMatchIncident, false);
        assertEquals(SnookerMatchIncidentResultType.MATCHWON, outcome.getSnookerMatchIncidentResultType());

    }
}
