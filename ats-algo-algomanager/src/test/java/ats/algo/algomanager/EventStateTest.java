package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.genericsupportfunctions.CopySerializableObject;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;

public class EventStateTest extends AlgoManagerSimpleTestBase {
    @Test
    public void test() {
        MethodName.log();
        algoManager.publishResultedMarketsImmediately(true);
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        assertEquals("R1", this.publishedEventStateBlob.getIncidentId());
        tennisMatchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        tennisMatchIncident.setEventId(12345L);
        tennisMatchIncident.setIncidentId("R2");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        /*
         * update the params and verify no blob published
         */
        TennisMatchParams matchParams = (TennisMatchParams) this.publishedMatchParams;
        matchParams.getOnServePctA1().setProperties(.55, .03, .02);
        this.publishedEventStateBlob = null;
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        assertEquals(null, this.publishedEventStateBlob);
        tennisMatchIncident.setIncidentId("R4");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        assertEquals("R4", this.publishedEventStateBlob.getIncidentId());
        EventStateBlob blob = this.publishedEventStateBlob;
        /*
         * keep track of just published objects
         */
        Set<String> mktKeys1 = this.publishedMarkets.getMarketKeys();
        double probAWinsMatch1 = this.publishedMarkets.getMarkets().get("FT:ML_M").get("A");
        MatchParams matchParams1 = this.publishedMatchParams.copy();
        MatchState matchState1 = this.publishedMatchState.copy();
        /*
         * move the score on a point
         */
        tennisMatchIncident.setIncidentId("R5");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        assertEquals("R5", this.publishedEventStateBlob.getIncidentId());
        /*
         * keep track of the second set of published objects
         */
        ResultedMarkets resultedMarkets2 = this.publishedResultedMarkets.copy();
        Set<String> mktKeys2 = this.publishedMarkets.getMarketKeys();
        double probAWinsMatch2 = this.publishedMarkets.getMarkets().get("FT:ML_M").get("A");
        MatchParams matchParams2 = this.publishedMatchParams.copy();
        MatchState matchState2 = this.publishedMatchState.copy();
        Set<String> keys2 = this.keysForDiscontinuedMarkets;
        /*
         * remove then recreate this event, initialising to the same state as the saved blob, and verify that everything
         * published is the same as expected
         */
        publishedMatchParams = null;
        publishedMatchState = null;
        publishedMarkets = null;
        publishedResultedMarkets = null;
        publishedEventStateBlob = null;
        algoManager.handleRemoveEvent(eventId);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat(), blob, null);
        assertEquals(matchState1, publishedMatchState);
        assertEquals(matchParams1, publishedMatchParams);
        assertEquals(mktKeys1, publishedMarkets.getMarketKeys());
        double probAWinsMatch3 = publishedMarkets.getMarkets().get("FT:ML_M").get("A");
        assertEquals(probAWinsMatch1, probAWinsMatch3, 0.02);
        assertEquals(0, this.keysForDiscontinuedMarkets.size());
        /*
         * replay the R5 incident
         */
        tennisMatchIncident.setIncidentId("R5");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        /*
         * should now be in the same place as we were before the restart
         */
        assertEquals("R5", this.publishedEventStateBlob.getIncidentId());
        assertEquals(matchState2, publishedMatchState);
        assertEquals(matchParams2, publishedMatchParams);
        assertEquals(mktKeys2, publishedMarkets.getMarketKeys());
        double probAWinsMatch4 = publishedMarkets.getMarkets().get("FT:ML_M").get("A");
        assertEquals(probAWinsMatch2, probAWinsMatch4, 0.02);
        assertEquals(keys2, this.keysForDiscontinuedMarkets);
        resultedMarkets2.setTimeStamp(publishedResultedMarkets.getTimeStamp());
        assertEquals(resultedMarkets2, publishedResultedMarkets);

    }

    @Test
    public void testCopy() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        EventState eventState = (EventState) this.publishedEventStateBlob;
        // EventState eventState2 = eventState.copy();
        // EventState eventState3 = (EventState) CopySerializableObject.copy(publishedEventStateBlob);
        EventStateBlob blobx = publishedEventStateBlob.copy();
        // assertEquals (eventState, eventState2);
        // boolean x = eventState.equals(eventState3);
        // assertTrue(x);
        // assertEquals (eventState, eventState3);
        assertEquals(eventState, (EventState) blobx);
    }

    @Test
    public void testCopyTime() {
        MethodName.log();
        long eventId = 12345L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        EventState eventState = (EventState) this.publishedEventStateBlob;
        EventState eventState2 = null;
        EventState eventState3 = null;
        EventState eventState4 = null;
        long n = 2000;
        // long t1 = System.currentTimeMillis();
        for (int i = 0; i < n; i++)
            eventState2 = eventState.copy();
        // long t2 = System.currentTimeMillis();
        // long t3 = System.currentTimeMillis();
        for (int i = 0; i < n; i++)
            eventState3 = CopySerializableObject.copy(eventState);
        // long t4 = System.currentTimeMillis();
        // long t5 = System.currentTimeMillis();
        for (int i = 0; i < n; i++)
            eventState4 = (EventState) publishedEventStateBlob.copy();
        // long t6 = System.currentTimeMillis();
        // System.out.printf("via copy: %d, via serialize: %d, via blob: %d \n", t2 - t1, t4 - t3, t6 - t5);
        assertEquals(eventState, eventState2);
        assertEquals(eventState, eventState3);
        assertEquals(eventState, eventState4);

    }
}
