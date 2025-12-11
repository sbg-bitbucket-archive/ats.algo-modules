package ats.algo.sport.outrights.calcengine.core;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.Selection;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.sport.outrights.OutrightsFixtureData;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsWatchList;
import ats.algo.sport.outrights.competitionsdata.TestCompetition;
import ats.algo.sport.outrights.server.api.Alerts;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class OutrightsTest {

    private static final Logger log = LoggerFactory.getLogger(OutrightsTest.class);

    @Test
    public void handlePriceCalcRequestTest() {
        MethodName.log();
        Outrights outrights = new Outrights();
        Competition competition = TestCompetition.generate();
        Competitions competitions = outrights.getCompetitions();
        competitions.add(competition);
        Fixtures fixtures = competitions.get(123456L).getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getFixtureID().equals("F371")) {
                /*
                 * Crystal Palace home match - set eventID to 55555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }

        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, new GenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId("REQ_ID_001");

        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        // System.out.println(response);
        assertEquals(0.40, response.getMarkets().get("C:BH").getSelections().get("Crystal Palace").getProb(), 0.01);
        OutrightsWatchList watchList = (OutrightsWatchList) response.getMatchEngineSavedState();
        assertEquals(3, watchList.getWatchListEntries().size());
        assertEquals("FT:CS", watchList.getWatchListEntries().get(55555L));
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market.put("0-1", 1.0); // Crystal palace sure to lose so prob in bottom half shld go up
        Map<Long, OutrightsFixtureData> outrightsFixturesData = new HashMap<>();
        OutrightsFixtureData data = new OutrightsFixtureData();
        data.setMarket(market);
        outrightsFixturesData.put(55555L, data);
        incident.setOutrightsFixturesData(outrightsFixturesData);
        request.updateUniqueRequestId("REQ_ID_002");
        response = outrights.handlePriceCalcRequest(request);
        // System.out.println(response);
        assertEquals(0.91, response.getMarkets().get("C:BH").getSelections().get("Crystal Palace").getProb(), 0.01);
    }

    @Test
    public void handlePriceCalcRequestErrorTest() {
        MethodName.log();
        Outrights outrights = new Outrights();
        Competition competition = TestCompetition.generate();
        Competitions competitions = outrights.getCompetitions();
        competitions.add(competition);
        Fixtures fixtures = competitions.get(123456L).getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getHomeTeamID().equals("T6")) {
                /*
                 * Crystal Palace home match - set eventID to 5555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, new GenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId("REQ_ID_001");
        // System.out.println("EXCEPTION EXPECTED TO OCCUR IN THIS UNIT TEST - IT IS CHECKING ERROR HANDLING");
        @SuppressWarnings("unused")
        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        // System.out.println(response);
        /*
         * add a market where the probs are invalid. should cause an exception
         */
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market.put("0-1", 0.0);
        Map<Long, OutrightsFixtureData> outrightsFixturesData = new HashMap<>();
        log.error("** ERROR ABOUT TO BE THROWN.  THIS IS EXPECTED AS PART OF THE TEST **");
        OutrightsFixtureData data = new OutrightsFixtureData();
        data.setMarket(market);
        outrightsFixturesData.put(55555L, data);
        incident.setOutrightsFixturesData(outrightsFixturesData);
        request.updateUniqueRequestId("REQ_ID_002");
        response = outrights.handlePriceCalcRequest(request);
        log.error("** END OF EXPECTED THROWN ERROR **");
        // System.out.println(response);
    }

    // @Test
    public void outrightsFixturesIntegrationTest() {
        MethodName.log();
        Outrights outrights = new Outrights();
        Competition competition = TestCompetition.generate();
        Competitions competitions = outrights.getCompetitions();
        competitions.add(competition);
        Fixtures fixtures = competition.getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getHomeTeamID().equals("T6")) {
                /*
                 * Crystal Palace home match - set eventID to 55555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, new GenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId("REQ_ID_001");
        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        // System.out.println(response);
        /*
         * going to update 55555L while leaving 999112 unchanged
         */
        Fixture fixture = fixtures.getByEventID(55555L);
        Fixture fixture2 = fixtures.getByEventID(999112L);
        // System.out.println(fixture);
        // System.out.println(fixture2);
        OutrightsWatchList watchList = (OutrightsWatchList) response.getMatchEngineSavedState();
        assertEquals(3, watchList.getWatchListEntries().size());
        assertEquals("FT:CS", watchList.getWatchListEntries().get(55555L));
        /*
         * send market and status consistent with score of 0-1 for 55555L match
         */
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market.put("0-1", 0.9);
        market.put("0-2", 0.1);
        Map<Long, OutrightsFixtureData> outrightsFixturesData = new HashMap<>();
        OutrightsFixtureData data = new OutrightsFixtureData();
        data.setMarket(market);
        data.setFixtureStatus(OutrightsFixtureStatus.IN_PLAY);
        outrightsFixturesData.put(55555L, data);
        incident.setOutrightsFixturesData(outrightsFixturesData);
        request.updateUniqueRequestId("REQ_ID_002");
        response = outrights.handlePriceCalcRequest(request);
        // System.out.println(fixture);
        // System.out.println(fixture2);
        assertTrue(fixture.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.IN_PLAY, fixture.getStatus());
        assertEquals(0, fixture.getGoalsHome());
        assertEquals(1, fixture.getGoalsAway());
        assertFalse(fixture2.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.PRE_MATCH, fixture2.getStatus());
        assertEquals(0, fixture2.getGoalsHome());
        assertEquals(0, fixture2.getGoalsAway());
        /*
         * send market and status consistent with score of 1-1 for 55555L match
         */
        Market market2 = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market2.put("1-1", 0.9);
        market2.put("1-2", 0.1);
        data.setMarket(market2);
        request.updateUniqueRequestId("REQ_ID_003");
        response = outrights.handlePriceCalcRequest(request);
        // System.out.println(fixture);
        // System.out.println(fixture2);
        assertTrue(fixture.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.IN_PLAY, fixture.getStatus());
        assertEquals(1, fixture.getGoalsHome());
        assertEquals(1, fixture.getGoalsAway());
        assertFalse(fixture2.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.PRE_MATCH, fixture2.getStatus());
        assertEquals(0, fixture2.getGoalsHome());
        assertEquals(0, fixture2.getGoalsAway());
        /*
         * result the 55555L match
         */
        ResultedMarket resultedMarket =
                        new ResultedMarket("FT:CS", "M", MarketCategory.GENERAL, "M", false, "Correct score", "2-3", 0);
        data.setResultedMarket(resultedMarket);
        request.updateUniqueRequestId("REQ_ID_003");
        response = outrights.handlePriceCalcRequest(request);
        // System.out.println(fixture);
        // System.out.println(fixture2);
        assertFalse(fixture.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.COMPLETED, fixture.getStatus());
        assertEquals(2, fixture.getGoalsHome());
        assertEquals(3, fixture.getGoalsAway());
        assertFalse(fixture2.isProbsSourcedfromATS());
        assertEquals(OutrightsFixtureStatus.PRE_MATCH, fixture2.getStatus());
        assertEquals(0, fixture2.getGoalsHome());
        assertEquals(0, fixture2.getGoalsAway());
        /*
         * check that data for 55555L is no longer being requested
         */
        OutrightsWatchList watchList2 = (OutrightsWatchList) response.getMatchEngineSavedState();
        // System.out.println(watchList2);
        assertEquals(2, watchList2.getWatchListEntries().size());
        assertEquals(null, watchList2.getWatchListEntries().get(55555L));
    }

    @Test
    public void suspensionRulesTest() {
        MethodName.log();
        Outrights outrights = new Outrights();
        Competition competition = TestCompetition.generate();
        Competitions competitions = outrights.getCompetitions();
        competitions.add(competition);
        Fixtures fixtures = competition.getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getFixtureID().equals("F371")) {
                /*
                 * Crystal Palace vs West Brom - set eventID to 55555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }
        /*
         * generate the outrights match incident and add a market for 55555L fixture
         */

        Market ftcsMarket = new Market(MarketCategory.GENERAL, MarketGroup.NOT_SPECIFIED, "FT:CS", "M",
                        "Correct score market");
        ftcsMarket.getSelections().put("0-0", new Selection(0.6));
        ftcsMarket.getSelections().put("0-1", new Selection(0.2));
        ftcsMarket.getSelections().put("1-0", new Selection(0.2));
        OutrightsFixtureData outrightsFixtureData = new OutrightsFixtureData();
        outrightsFixtureData.setMarket(ftcsMarket);
        runTest(outrights, outrightsFixtureData, "TEST_1", false);

        /*
         * suspend the 55555L event. Should result in both the C:BH and C:OU:T6 markets being suspended. C:T4 should not
         * be suspended since neither team is a selection in that market
         */
        outrightsFixtureData.setSuspended(true);
        runTest(outrights, outrightsFixtureData, "TEST_2", true);
        /*
         * unsuspend the 55555L event and verify that we have returned back to everything unsuspended
         */
        outrightsFixtureData.setSuspended(false);
        runTest(outrights, outrightsFixtureData, "TEST_2", false);
        /*
         * suspend the individual market FT:CS for the 55555L instead suspending the entire event and confirm behaviour
         * is the same
         */
        ftcsMarket.getMarketStatus().setSuspensionStatus(SuspensionStatus.SUSPENDED_DISPLAY);
        runTest(outrights, outrightsFixtureData, "TEST_4", true);
        /*
         * Modify probs so that prob of bottom half finish for Crystal palace exceeds 0.75. Market should remain open
         */
        ftcsMarket.getSelections().put("0-0", new Selection(0.0));
        ftcsMarket.getSelections().put("0-1", new Selection(0.9));
        ftcsMarket.getSelections().put("1-0", new Selection(0.1));
        runTest(outrights, outrightsFixtureData, "TEST_5", true);
    }

    private void runTest(Outrights outrights, OutrightsFixtureData outrightsFixtureData, String testId,
                    boolean expectSuspended) {
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        incident.getOutrightsFixturesData().put(55555L, outrightsFixtureData);
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, new GenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId(testId);
        PriceCalcResponse response = outrights.handlePriceCalcRequest(request);
        // System.out.println(testId);
        // System.out.println(response.getMarkets());
        if (expectSuspended)
            checkSuspendedAsExpected(response.getMarkets());
        else
            checkUnsuspendedAsExpected(response.getMarkets());
    }

    private void checkUnsuspendedAsExpected(Markets markets) {
        Market bhMarket = markets.get("C:BH");
        assertEquals(SuspensionStatus.OPEN, bhMarket.getMarketStatus().getSuspensionStatus());
        assertEquals(0.59, bhMarket.getSelections().get("Crystal Palace").getProb(), 0.01);
        Market ouMarket = markets.get("C:OU:T6");
        assertEquals(SuspensionStatus.OPEN, ouMarket.getMarketStatus().getSuspensionStatus());
        Market t4Market = markets.get("C:T4");
        assertEquals(SuspensionStatus.OPEN, t4Market.getMarketStatus().getSuspensionStatus());
        assertEquals(0.18, t4Market.getSelections().get("Chelsea").getProb(), 0.01);
    }

    private void checkSuspendedAsExpected(Markets markets) {
        Market bhMarket = markets.get("C:BH");
        double cpProb = bhMarket.getSelections().get("Crystal Palace").getProb();
        if (cpProb < OutrightTradingRules.PROB_UPPER_SUSPENSION_LIMIT) {
            assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, bhMarket.getMarketStatus().getSuspensionStatus());
            assertEquals(0.59, cpProb, 0.01);
        } else {
            assertEquals(SuspensionStatus.OPEN, bhMarket.getMarketStatus().getSuspensionStatus());
            assertEquals(0.82, cpProb, 0.01);
        }
        Market ouMarket = markets.get("C:OU:T6");
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, ouMarket.getMarketStatus().getSuspensionStatus());
        Market t4Market = markets.get("C:T4");
        assertEquals(SuspensionStatus.OPEN, t4Market.getMarketStatus().getSuspensionStatus());
        assertEquals(0.18, t4Market.getSelections().get("Chelsea").getProb(), 0.01);
    }

    @Test
    public void getAlertsTest() {
        MethodName.log();
        try {
            Outrights outrights = new Outrights();
            for (@SuppressWarnings("unused")
            Competition competition : outrights.getCompetitions().values()) {
                // System.out.println("Checking: " + competition.getName());
                @SuppressWarnings("unused")
                Alerts alerts = outrights.getAlerts();
                // System.out.println(alerts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * Outrights price calc request soak test to investigate possible memory leaks. Not a regular unit test since takes
     * far too long
     */
    public static void main(String[] args) {
        Outrights outrights = new Outrights();
        Competition competition = TestCompetition.generate();
        Competitions competitions = outrights.getCompetitions();
        competitions.add(competition);
        Fixtures fixtures = competitions.get(123456L).getFixtures();
        for (Fixture fixture : fixtures) {
            if (fixture.getHomeTeamID().equals("T6")) {
                /*
                 * Crystal Palace home match - set eventID to 5555L
                 */
                fixture.setEventID(55555L);
                break;
            }
        }

        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        PriceCalcRequest request = new PriceCalcRequest(123456L, null, null, CalcRequestCause.MATCH_INCIDENT, null,
                        null, new GenericMatchParams(), incident, null, null, 0);
        request.updateUniqueRequestId("REQ_ID_001");

        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        market.put("0-1", 1.0); // Crystal palace sure to lose so prob in bottom half shld go up
        Map<Long, OutrightsFixtureData> outrightsFixturesData = new HashMap<>();
        OutrightsFixtureData data = new OutrightsFixtureData();
        data.setMarket(market);
        outrightsFixturesData.put(55555L, data);
        incident.setOutrightsFixturesData(outrightsFixturesData);
        for (int i = 0; i < 10000; i++) {
            String reqId = "REQ_ID_" + i;
            request.updateUniqueRequestId(reqId);
            outrights.handlePriceCalcRequest(request);
            // long heapSize = Runtime.getRuntime().totalMemory();
            // long heapFreeSize = Runtime.getRuntime().freeMemory();
            // int nThreads = Thread.activeCount();

            // System.out.printf("ReqId no: %d, heapSize: %d, freeSize: %d, threads: %d\n", i, heapSize, heapFreeSize,
            // nThreads);
        }
    }

}
