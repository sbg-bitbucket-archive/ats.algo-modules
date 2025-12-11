package ats.algo.algomanager;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchIncident;
import ats.algo.sport.badminton.BadmintonMatchState;
import ats.algo.sport.badminton.BadmintonMatchIncident.BadmintonMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchState;
import ats.core.util.json.JsonUtil;

public class AbandonEventTest extends AlgoManagerSimpleTestBase {

    public AbandonEventTest() {
        PublishMarketsManager.publishAllMarkets = true;
    }

    long eventId = 123L;

    @Test
    public void testAbandonViaIncident() {
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat(), 1);

        TennisMatchIncident startingIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.SERVING_ORDER, TeamId.A, 1);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(startingIncident, true);

        startingIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R2");
        algoManager.handleMatchIncident(startingIncident, true);
        System.out.println((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState());
        tennisMoveGameForwardByPoints(startingIncident, 20, TeamId.A);
        tennisMoveGameForwardByPoints(startingIncident, 25, TeamId.B);
        System.out.println((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState());
        tennisMoveGameForwardByPoints(startingIncident, 20, TeamId.B);
        tennisMoveGameForwardByPoints(startingIncident, 30, TeamId.A);
        tennisMoveGameForwardByPoints(startingIncident, 20, TeamId.B);
        System.out.println(algoManager.getEventDetails(eventId).getEventState().getMatchState());
        String incidentstring =
                        "{\"AbandonMatchIncident\":{\"subClass\":\"AbandonMatchIncident\",\"undo\":false,\"elapsedTimeSecs\":19,\"eventId\":123,\"requestId\":\"414_8\",\"teamId\":\"B\",\"sourceSystem\":\"IMG\",\"externalEventId\":\"414\",\"timeStamp\":1501078081398,\"incidentSubType\":\"WALKOVER\"}}\"";
        AbandonMatchIncident abandonIncident = JsonUtil.unmarshalJson(incidentstring, AbandonMatchIncident.class);

        abandonIncident.setEventId(eventId);
        algoManager.handleMatchIncident(abandonIncident, true);
        ResultedMarket resultedMarket1 = publishedResultedMarkets.getResultedMarkets().get("FT:OU#19.5_M");
        System.out.println(publishedResultedMarkets);
        assertTrue(resultedMarket1.isMarketVoided());
        assertTrue(resultedMarket1.isFullyResulted());
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:OU#23.5_M");
        assertTrue(resultedMarket2.isMarketVoided());
        assertTrue(resultedMarket2.isFullyResulted());

        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertTrue(resultedMarket3.isMarketVoided());
        assertTrue(resultedMarket3.isFullyResulted());


    }



    @SuppressWarnings("unused") // uncomment tests in testFootball()
    private void getNonVoidMarkets(ResultedMarkets publishedResultedMarkets,
                    TreeMap<String, ResultedMarket> compareMarketsMap) {
        for (Map.Entry<String, ResultedMarket> entry : publishedResultedMarkets.getResultedMarkets().entrySet()) {
            if (!entry.getValue().isMarketVoided())
                compareMarketsMap.put(entry.getKey(), entry.getValue());
            else
                System.out.println(entry.getValue());
        }
    }

    @Test
    public void testAbandonViaMethod() {
        algoManager.handleNewEventCreation(SupportedSportType.BADMINTON, eventId, new BadmintonMatchFormat());
        BadmintonMatchIncident startingIncident = new BadmintonMatchIncident();
        startingIncident.set(BadmintonMatchIncidentType.SERVEFIRST, TeamId.A);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(startingIncident, true);
        startingIncident.setIncidentId("R2");
        startingIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.A);
        algoManager.handleMatchIncident(startingIncident, true);
        System.out.println((BadmintonMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState());
        badmintonMoveGameForwardByPoints(startingIncident, 25);
        System.out.println((BadmintonMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState());

        /*
         * got to end of first game so have a few resulted markets. Now abandon.
         */
        algoManager.handleAbandonEvent(eventId, false);
        System.out.println(this.publishedResultedMarkets);
        ResultedMarket resultedMarket1 = publishedResultedMarkets.getResultedMarkets().get("P:PW_S1.10");
        assertEquals("A", resultedMarket1.getWinningSelections().get(0));
        assertFalse(resultedMarket1.isMarketVoided());

        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("P:A:OU#20.5_S1");
        assertEquals("Over", resultedMarket2.getWinningSelections().get(0));
        assertFalse(resultedMarket2.isMarketVoided());

        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertTrue(resultedMarket3.isMarketVoided());
    }

    @Test
    public void testDoNotVoidPropertyFalse() {
        System.setProperty("doNotVoidMarketsOnAbandonEvent", "false");
        super.beforeEachTest();// create new instance of AlgoManager after property has been set
        algoManager.handleNewEventCreation(SupportedSportType.BADMINTON, eventId, new BadmintonMatchFormat());
        algoManager.handleAbandonEvent(eventId, false);

        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertTrue(resultedMarket3.isMarketVoided());
    }

    @Test
    public void testDoNotVoidPropertyTrue() {
        System.setProperty("doNotVoidMarketsOnAbandonEvent", "true");
        super.beforeEachTest();// create new instance of AlgoManager after property has been set
        algoManager.handleNewEventCreation(SupportedSportType.BADMINTON, eventId, new BadmintonMatchFormat());
        algoManager.handleAbandonEvent(eventId, false);
        assertEquals(null, publishedResultedMarkets);
        System.setProperty("doNotVoidMarketsOnAbandonEvent", "false"); // restore to default value

    }



    private void badmintonMoveGameForwardByPoints(BadmintonMatchIncident startingIncident, int points) {
        int ii = Integer.parseInt(
                        startingIncident.getIncidentId().substring(1, startingIncident.getIncidentId().length()));
        for (int i = 0; i < points; i++) {
            String string = "R" + Integer.toString((ii + i));
            startingIncident.setIncidentId(string);
            startingIncident.set(BadmintonMatchIncidentType.POINTWON, TeamId.A);
            algoManager.handleMatchIncident(startingIncident, true);
        }
    }

    private void tennisMoveGameForwardByPoints(TennisMatchIncident startingIncident, int points, TeamId team) {
        int ii = Integer.parseInt(
                        startingIncident.getIncidentId().substring(1, startingIncident.getIncidentId().length()));
        for (int i = 0; i < points; i++) {
            String string = "R" + Integer.toString((ii + i));
            startingIncident.setIncidentId(string);
            startingIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
            startingIncident.setPointWinner(team);
            algoManager.handleMatchIncident(startingIncident, true);
        }
    }

    @Test
    public void test() {
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(startingIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident("R2", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R3", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R4", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R5", TeamId.A), true);
        /*
         * got to end of first game so have a few resulted markets. Now abandon.
         */
        algoManager.handleAbandonEvent(eventId, false);
        System.out.println(this.publishedResultedMarkets);
        ResultedMarket resultedMarket1 = publishedResultedMarkets.getResultedMarkets().get("G:ML_S1.1");
        assertEquals("A", resultedMarket1.getWinningSelections().get(0));
        assertFalse(resultedMarket1.isMarketVoided());
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertTrue(resultedMarket2.isMarketVoided());
    }

    @Test
    public void testAbandonWithoutResulting() {
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        TennisMatchIncident startingIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingIncident.setEventId(eventId);
        startingIncident.setIncidentId("R1");
        algoManager.handleMatchIncident(startingIncident, true);
        algoManager.handleMatchIncident(getPointWonIncident("R2", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R3", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R4", TeamId.A), true);
        algoManager.handleMatchIncident(getPointWonIncident("R5", TeamId.A), true);
        /*
         * got to end of first game so have a few resulted markets. Now abandon and check that markets are not resulted
         * and that trader alert gets published
         */
        traderAlert = null;
        algoManager.handleAbandonEvent(eventId, true);
        assertTrue(traderAlert != null);
        System.out.println(traderAlert);
        System.out.println(this.publishedResultedMarkets);
        ResultedMarket resultedMarket1 = publishedResultedMarkets.getResultedMarkets().get("G:ML_S1.1");
        assertEquals(null, resultedMarket1);
    }

    MatchIncident getPointWonIncident(String reqId, TeamId teamId) {
        MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, teamId);
        incident.setEventId(eventId);
        incident.setIncidentId(reqId);
        return incident;

    }

}
