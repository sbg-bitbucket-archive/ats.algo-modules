package ats.algo.revertEventState;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSharedMemoryTestBase;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

public class DelayPublishingResultedMarketsTest extends AlgoManagerSharedMemoryTestBase {


    @Test
    public void testDelayPublishingResultedMarkets() {
        long eventId = 11L;
        TennisMatchFormat format = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, format);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(matchIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R2"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R3"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R4"), true);
        algoManager.handleMatchIncident(getPointWonIncident(eventId, "R5"), true);
        /*
         * score now 0-0 1-0 0-0
         */
        assertEquals(null, publishedResultedMarkets);
        int waitTime = 250;
        try {
            for (int i = 0; i < waitTime; i += 10) {
                System.out.printf("wait %d secs (of %d)\n", i, waitTime);
                Thread.sleep(10000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("R5", publishedResultedMarkets.getIncidentId());
    }

    TennisMatchIncident getPointWonIncident(long eventId, String incidentId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        matchIncident.setEventId(eventId);
        matchIncident.setIncidentId(incidentId);
        return matchIncident;
    }

}
