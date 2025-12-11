package ats.algo.algomanager;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;

public class EventStateHistoryTest {

    EventStateHistory history;
    EventState eventState;
    ResultedMarkets resultedMarkets;



    @Test
    public void testAddToHistory() {
        history = new EventStateHistory(3);
        createTestHistory();
        assertEquals(null, eventState);
        resultedMarkets = history.addToHistory(newEventState("R4", 400L));
        assertEquals("R1", resultedMarkets.getIncidentId());
        resultedMarkets = history.addToHistory(newEventState("R5", 500L));
        assertEquals("R2", resultedMarkets.getIncidentId());
        assertEquals(3, history.size());
        resultedMarkets = history.addToHistory(newEventState("R5", 500L));
        assertEquals(null, resultedMarkets);
        assertEquals(3, history.size());
        System.out.println(history);

    }

    @Test
    public void testGetMostRecentEventInHistory() {
        history = new EventStateHistory(3);
        eventState = history.getMostRecentEventStateFromHistory();
        assertEquals(null, eventState);
        createTestHistory();
        eventState = history.getMostRecentEventStateFromHistory();
        assertEquals("R3", eventState.getIncidentId());
        assertEquals(3, history.size());
    }

    @Test
    public void testGetPreviousIncidentRequestId() {
        history = new EventStateHistory(3);
        String reqId = history.getPreviousIncidentId();
        assertEquals(null, reqId);
        createTestHistory();
        reqId = history.getPreviousIncidentId();
        assertEquals("R3", reqId);
        assertEquals(3, history.size());
    }

    @Test
    public void testGetMostRecentTimeExpiredEventState() {
        history = new EventStateHistory(3);
        resultedMarkets = history.getMostRecentTimeExpiredResultedMarkets(250L);
        assertEquals(null, resultedMarkets);
        createTestHistory();
        resultedMarkets = history.getMostRecentTimeExpiredResultedMarkets(250L);
        assertEquals("R2", resultedMarkets.getIncidentId());
        eventState = history.getMostRecentEventStateFromHistory();
        assertEquals("R3", eventState.getIncidentId());
        assertEquals(3, history.size());
        resultedMarkets = history.getMostRecentTimeExpiredResultedMarkets(250L);
        assertEquals(null, resultedMarkets);
    }

    @Test
    public void testRollBackTo() {
        history = new EventStateHistory(3);
        RevertEventState eventState = history.rollBackToBefore("R2", false);
        assertEquals(null, eventState);
        createTestHistory();
        eventState = history.rollBackToBefore("R2", false);
        assertEquals("R1", eventState.getEventState().getIncidentId());
        assertEquals(1, history.size());
        history.addToHistory(newEventState("R3", 500L));
        String reqId = history.getPreviousIncidentId();
        assertEquals("R3", reqId);
        eventState = history.rollBackToBefore("R3", false);
        assertEquals("R1", eventState.getEventState().getIncidentId());
    }

    @Test
    public void testMostRecentTimeExpiredResultedMarket() {
        history = new EventStateHistory(3);
        createTestHistory();
        resultedMarkets = history.getMostRecentTimeExpiredResultedMarkets(250L);
        assertEquals("R2", resultedMarkets.getIncidentId());
        assertEquals(3, history.size());
        RevertEventState eventState = history.rollBackToBefore("R2", false);
        assertEquals(null, eventState);
        assertEquals(3, history.size());
        eventState = history.rollBackToBefore("R2", true);
        assertEquals("R1", eventState.getEventState().getIncidentId());
    }


    private void createTestHistory() {
        history.addToHistory(newEventState("R1", 100L));
        history.addToHistory(newEventState("R2", 200L));
        history.addToHistory(newEventState("R3", 300L));
    }

    private EventState newEventState(String reqId, long timeStampMillis) {
        EventState eventState =
                        new EventState(new TennisMatchState(), new TennisMatchParams().generateGenericMatchParams());
        eventState.setIncidentId(reqId);
        eventState.setTimeStampMillis(timeStampMillis);
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        resultedMarkets.setIncidentId(reqId);
        eventState.setResultedMarkets(resultedMarkets);
        return eventState;
    }
}
