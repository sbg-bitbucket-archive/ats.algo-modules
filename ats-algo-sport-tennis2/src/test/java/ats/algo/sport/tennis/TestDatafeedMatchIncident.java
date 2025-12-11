package ats.algo.sport.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class TestDatafeedMatchIncident {

    @Test
    public void test() {
        MethodName.log();
        TennisMatchState tennisMatchState = new TennisMatchState(new TennisMatchFormat());
        TennisMatchIncident tennisMatchIncident = new TennisMatchIncident();
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        TennisMatchIncidentResult incidentResult =
                        (TennisMatchIncidentResult) tennisMatchState.updateStateForIncident(tennisMatchIncident, true);
        assertTrue(incidentResult != null);
        DatafeedMatchIncident datafeedMatchIncident =
                        new DatafeedMatchIncident(DatafeedMatchIncidentType.SCOUT_DISCONNECT, 10);
        incidentResult = (TennisMatchIncidentResult) tennisMatchState.updateStateForIncident(datafeedMatchIncident,
                        true);
        assertTrue(incidentResult == null);
        assertEquals(DatafeedMatchIncidentType.SCOUT_DISCONNECT, tennisMatchState.getDataFeedStatus());
    }

}
