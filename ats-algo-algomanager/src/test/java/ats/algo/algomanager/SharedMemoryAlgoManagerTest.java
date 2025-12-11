package ats.algo.algomanager;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Set;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;

/*
 * test of the multithreaded implementation using
 */
public class SharedMemoryAlgoManagerTest extends AlgoManagerSharedMemoryTestBase {



    public SharedMemoryAlgoManagerTest() {
        super();
    }

    @Test
    public void test() {
        /*
         * create two tennis and one darts event. Since multi threaded, the publish methods will be called
         * asynchronously
         */
        TennisMatchFormat tennisMatchFormat1 =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        setPublishedDataToNull();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat1);
        assertEquals(null, publishedMarkets); // since asynchronously
                                              // calculated should not
                                              // immediately return
        waitOnPublishedMatchParams();
        assertEquals(11L, publishedMatchParams.getEventId());
        TennisMatchParams tennisMatchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, tennisMatchParams.getOnServePctA1().getMean(), 0.01);
        TennisSimpleMatchState tennisMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, tennisMatchState.getOnServeNow());

        // assertEquals(0.29, publishedMarkets.get("FT:W1S:A").get("A No"), 0.01);

        /*
         * create second tennis event
         */
        TennisMatchFormat tennisMatchFormat2 = new TennisMatchFormat(5, FinalSetType.ADVANTAGE_SET, false, false);
        setPublishedDataToNull();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 12L, tennisMatchFormat2);
        waitOnPublishedMatchParams();
        assertEquals(12L, publishedMatchParams.getEventId());
        // assertEquals(0.19, publishedMarkets.get("FT:W1S:A").get("A No"), 0.01);
        assertEquals(TeamId.UNKNOWN, tennisMatchState.getOnServeNow());
        assertEquals(null, publishedMarkets.get("G:ML"));
        /*
         * create a darts event
         */
        DartMatchFormat dartMatchFormat = new DartMatchFormat();
        setPublishedDataToNull();
        algoManager.handleNewEventCreation(SupportedSportType.DARTS, 21L, dartMatchFormat);
        waitOnPublishedMatchParams();
        Gaussian g = publishedMatchParams.getParamMap().get("skillA").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        waitOnPublishedMarkets();

        assertEquals(0.64, publishedMarkets.get("G:1180", "L1.1").get("No"), 0.01);
        assertEquals(42681241600L, 42681241600L);
        /*
         * fire in an incident for the second tennis match
         */
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(12L);
        tennisMatchIncident.setIncidentId("X123");
        setPublishedDataToNull();
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        waitOnPublishedMatchParams();
        tennisMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.A, tennisMatchState.getOnServeNow());
        assertEquals(0.82, publishedMarkets.get("G:ML", "S1.1").get("A"), 0.01);
        /*
         * undo the last incident
         */

        setPublishedDataToNull();
        algoManager.handleUndoLastMatchIncident(12L);
        waitOnPublishedMatchParams();
        assertEquals(12L, publishedMatchParams.getEventId());
        tennisMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, tennisMatchState.getOnServeNow());

        assertEquals(null, publishedMarkets.get("GMW1"));
        /*
         * set the matchParams explicitly for the first tennis match
         */
        tennisMatchParams = new TennisMatchParams();
        tennisMatchParams.setOnServePctA1(0.65, 0.08);
        tennisMatchParams.setOnServePctB1(0.58, 0.05);
        tennisMatchParams.setEventId(11L);
        setPublishedDataToNull();
        algoManager.handleSetMatchParams(tennisMatchParams.generateGenericMatchParams());
        waitOnPublishedMatchParams();
        TennisMatchParams publishedTennisMatchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, publishedTennisMatchParams.getOnServePctA1().getMean(), 0.01);
        assertEquals(0.08, publishedTennisMatchParams.getOnServePctA1().getStdDevn(), 0.01);
        assertEquals(0.71, publishedMarkets.get("FT:ML").get("A"), 0.01);

    }

    @Test
    public void testParamFind() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 1234567890123456789L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        waitOnPublishedMatchParams();


        /*
         * param find should be triggered
         */
        System.out.println("TEST FAR");
        setPublishedDataToNull();
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFar());
        assertTrue(result.isParamFindScheduled());
        System.out.println("Test: Waiting for param find results...");
        waitOnPublishedParamFindResults();
        // System.out.println("Test: Waiting for price calc results...");

        System.out.print(publishedParamFinderResults.toString());
        assertTrue(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.getnIterations() > 0);
        assertEquals(0.0, publishedParamFinderResults.getFunctionValueAtMinimum(), 0.015);
        TennisMatchParams publishedTennisMatchParams = (TennisMatchParams) publishedMatchParams;
        double onServePctDiff = publishedTennisMatchParams.getOnServePctA1().getMean()
                        - publishedTennisMatchParams.getOnServePctB1().getMean();
        assertEquals(0.063, onServePctDiff, 0.01);

    }


    /**
     * should trigger a param find
     * 
     * @return
     */
    private MarketPricesList getTestMarketPricesFar() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.36);
        ab.put("B", 3.40);
        m.addMarketPrice(ab);
        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "20.5");
        tmtg.put("Over", 1.84);
        tmtg.put("Under", 2.0);
        m.addMarketPrice(tmtg);

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    @Test
    public void testResultedMarkets() throws InterruptedException {

        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        setPublishedDataToNull();
        algoManager.publishResultedMarketsImmediately(true);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        setPublishedDataToNull();
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        Thread.sleep(4000);
        waitOnPublishedMatchParams();
        setPublishedDataToNull();
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        Thread.sleep(4000);
        setPublishedDataToNull();
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        assertEquals(null, publishedResultedMarkets);
        setPublishedDataToNull();
        Thread.sleep(4000);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        waitOnPublishedResultedMarkets();
        System.out.println(publishedResultedMarkets);
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("G:PW_S1.1.2");
        assertEquals("S1.1.2", resultedMarket.getSequenceId());
        assertEquals("A", resultedMarket.getWinningSelections().get(0));
    }



    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", matchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
        printResults("matchState", matchState);
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        printResults("markets", markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        if (resultedMarkets == null)
            System.out.printf("Resulted markets null\n");
        System.out.printf("Resulted markets size: %d\n", resultedMarkets.size());
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFinderResults,
                    GenericMatchParams matchParams, long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFinderResults;
        printResults("paramFinderResults", paramFinderResults);
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;

    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {
        this.publishedNotifyFatalError = eventId;

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {

    }

    private void printResults(String resultsDescription, Object results) {
        /*
         * System.out.printf("Published %s for event: %s \n", resultsDescription); System.out.print(results.toString());
         * System.out.printf("--- Published %s ends ---\n\n", resultsDescription);
         */
    }

    @Test
    public void testQueue() {
        LinkedList<String> testQueue = new LinkedList<String>();
        assertTrue(testQueue.isEmpty());
        testQueue.add("Item1");
        testQueue.add("Item2");
        testQueue.add("Item3");
        String s = null;
        for (int i = 0; i < testQueue.size(); i++) {
            s = testQueue.get(i);
            if (s == "Item2")
                break;
        }
        assertEquals("Item2", s);
        assertEquals(3, testQueue.size());
        s = testQueue.get(2);
        assertEquals("Item3", s);
        s = testQueue.removeFirst();
        assertEquals("Item1", s);
        assertEquals(2, testQueue.size());
        s = testQueue.remove(1);
        assertEquals("Item3", s);
        assertEquals(1, testQueue.size());
        assertFalse(testQueue.isEmpty());

    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }



}
