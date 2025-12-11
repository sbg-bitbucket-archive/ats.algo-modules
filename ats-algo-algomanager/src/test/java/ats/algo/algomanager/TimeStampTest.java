package ats.algo.algomanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class TimeStampTest extends AlgoManagerSimpleTestBase {

    long eventId = 123L;

    @Test
    public void testTennis() {
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        assertTrue(this.publishedTimeStamp == null);
        TennisMatchIncident startingIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(startingIncident, true);
        assertFalse(this.publishedTimeStamp == null);
        System.out.println(publishedTimeStamp);

    }

    // private MatchIncident getPointWonIncident (String reqId, TeamId teamId) {
    // MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
    // incident.setEventId(eventId);
    // incident.setRequestId(reqId);
    // return incident;
    // }

}
