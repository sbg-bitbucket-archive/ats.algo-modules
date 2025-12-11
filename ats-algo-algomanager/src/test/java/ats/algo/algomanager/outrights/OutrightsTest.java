package ats.algo.algomanager.outrights;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SupportedSportsInitialisation;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.tradingrules.FootballTradingRules;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.outrights.OutrightsFixtureData;
import ats.algo.sport.outrights.OutrightsMatchFormat;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsMatchParams;

/**
 * These tests verify comms between AlgoManager and the Outrights calculator. However does not use the real outrights
 * server, instead uses the internal outrightsMatchEngine which just returns some dummy markets.
 * 
 * The OutrightsIntegrationTest can be used to verify comms with the external model
 * 
 * @author gicha
 *
 */
public class OutrightsTest implements AlgoManagerPublishable {

    AlgoManager algoManager;
    AlgoManagerConfigurationForOutrightsTest algoManagerConfiguration;

    public OutrightsTest() {
        // LogUtil.initConsoleLogging(Level.TRACE);
    }

    static final long football11L = 11L;
    static final long football12L = 12L;
    static final long outrightsEventID = 22L;

    private boolean outrightsMarketsPublished;

    @Before
    public void outrightsTestSetup() {
        /*
         * Creating two football matches and change default params so mkts get published
         */
        algoManagerConfiguration = new AlgoManagerConfigurationForOutrightsTest();
        SupportedSportsInitialisation.init();
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.setTradingRules(SupportedSportType.SOCCER, new FootballTradingRules());
        algoManager.publishResultedMarketsImmediately(true);

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, football11L, new FootballMatchFormat());
        FootballMatchParams params = new FootballMatchParams();
        params.setEventId(football11L);
        params.setGoalSupremacy(3.8, 0.17);
        algoManager.handleSetMatchParams(params.generateGenericMatchParams());
        outrightsMarketsPublished = false;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, football12L, new FootballMatchFormat());
        params.setEventId(football12L);
        params.setGoalSupremacy(3.8, 0.17);
        algoManager.handleSetMatchParams(params.generateGenericMatchParams());
        /*
         * create the outrights event
         */
        algoManager.handleNewEventCreation(SupportedSportType.OUTRIGHTS, outrightsEventID, new OutrightsMatchFormat());
    }

    @After
    public void outrightsTestTeardown() {
        algoManager.close();
        algoManager = null;
        algoManagerConfiguration = null;
    }

    @Test
    /**
     * verify basic functionality and that goal incident caused immediate outrights price calc request
     */
    public void outrightsTest() {
        MethodName.log();
        /*
         * change outrights param so markets get published
         */
        OutrightsMatchParams outrightsParams = new OutrightsMatchParams();
        outrightsParams.setEventId(outrightsEventID);
        outrightsParams.updateDummyParam(0.8);
        algoManager.handleSetMatchParams(outrightsParams.generateGenericMatchParams());
        assertTrue(outrightsMarketsPublished);
        assertEquals(null, algoManagerConfiguration.outrightsMatchIncident);
        outrightsMarketsPublished = false;
        /*
         * issue goal for 12L - should not cause outrights calc
         */
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 0, TeamId.A);
        incident.setEventId(football12L);
        algoManager.handleMatchIncident(incident, true);
        /*
         * wait for the timer process to pick up the goal;
         */
        Sleep.sleep(3);
        assertFalse(outrightsMarketsPublished);
        incident.setEventId(football11L);
        algoManager.handleMatchIncident(incident, true);
        /*
         * Issuing goal for 11L SHOULD cause an outrights calc. Wait for the timer to pick up the goal;
         */
        Sleep.sleep(2);
        assertTrue(outrightsMarketsPublished);
        /*
         * add 12L to the watchlist. Next incident should cause a priceCalc.
         */
        algoManagerConfiguration.addToWatchList(football12L);
        /*
         * get the watchlist returned to AlgoManager in the priceCalcResponse for a params change
         */
        algoManager.handleSetMatchParams(outrightsParams.generateGenericMatchParams());
        outrightsMarketsPublished = false;
        incident.setEventId(football12L);
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertTrue(outrightsMarketsPublished);
        /*
         * verify the incident that was sent contains what was expected - i.e. one market for each football event
         */
        OutrightsMatchIncident outrightsIncident = algoManagerConfiguration.outrightsMatchIncident;
        // System.out.println(outrightsIncident);
        Map<Long, OutrightsFixtureData> outrightsFixtureData = outrightsIncident.getOutrightsFixturesData();
        assertEquals(2, outrightsFixtureData.size());
        Market market1 = outrightsFixtureData.get(football11L).getMarket();
        assertEquals("FT:CS", market1.getType());
        Market market2 = outrightsFixtureData.get(football12L).getMarket();
        assertEquals("FT:MW", market2.getType());
        algoManager.close();
        ResultedMarket resultedMarket = outrightsFixtureData.get(football11L).getResultedMarket();
        assertEquals(null, resultedMarket);
    }

    @Test
    /**
     * verify that if a market gets resulted it gets sent through to the outrgiths engine
     */
    public void outrightsTestResultedMarkets() {
        MethodName.log();
        OutrightsMatchParams outrightsParams = new OutrightsMatchParams();
        outrightsParams.setEventId(outrightsEventID);
        outrightsParams.updateDummyParam(0.8);
        algoManager.handleSetMatchParams(outrightsParams.generateGenericMatchParams());
        assertTrue(outrightsMarketsPublished);
        /*
         * result match 11L and verify that next incident that gets sent contains resultedMarket for 11L
         */
        algoManagerConfiguration.resultEvent(football11L);
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 0, TeamId.A);
        incident.setEventId(football11L);
        outrightsMarketsPublished = false;
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertTrue(outrightsMarketsPublished);
        /*
         * verify the incident that was sent contains what was expected - i.e. one resulted market only
         */
        OutrightsMatchIncident outrightsIncident = algoManagerConfiguration.outrightsMatchIncident;
        Map<Long, OutrightsFixtureData> outrightsFixturesData = outrightsIncident.getOutrightsFixturesData();
        // System.out.println(outrightsFixturesData);
        assertEquals(1, outrightsFixturesData.size());

        ResultedMarket rm = outrightsFixturesData.get(football11L).getResultedMarket();
        assertEquals("FT:CS", rm.getType());
    }

    @Test
    /**
     * verify that a goal incident for a non-monitored eventID does NOT cause an outrights price calc
     */
    public void testNonMonitoredGoal() {
        MethodName.log();
        OutrightsMatchParams outrightsParams = new OutrightsMatchParams();
        outrightsParams.setEventId(outrightsEventID);
        outrightsParams.updateDummyParam(0.8);
        algoManager.handleSetMatchParams(outrightsParams.generateGenericMatchParams());
        assertTrue(outrightsMarketsPublished);
        /*
         * result match 11L and verify that next incident that gets sent contains resultedMarket for 11L
         */
        FootballMatchIncident incident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 0, TeamId.A);
        incident.setEventId(football12L);
        outrightsMarketsPublished = false;
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(3);
        assertFalse(outrightsMarketsPublished);
    }

    @Test
    /**
     * verify that if nothing is happening an outrights price calc still gets scheduled at regular intervals
     */
    public void testRoutineRecalc() {
        MethodName.log();
        OutrightsMatchParams outrightsParams = new OutrightsMatchParams();
        outrightsParams.setEventId(outrightsEventID);
        outrightsParams.updateDummyParam(0.8);
        algoManager.handleSetMatchParams(outrightsParams.generateGenericMatchParams());
        assertTrue(outrightsMarketsPublished);
        outrightsMarketsPublished = false;
        long savedCalcTime = OutrightsMonitor.TIME_BETWEEN_CALCS_MS;
        OutrightsMonitor.TIME_BETWEEN_CALCS_MS = 2000;
        Sleep.sleep(5);
        assertTrue(outrightsMarketsPublished);
        OutrightsMonitor.TIME_BETWEEN_CALCS_MS = savedCalcTime;
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {}

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {}

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        // System.out.println("Publish markets: " + markets);
        if (eventId == outrightsEventID)
            outrightsMarketsPublished = true;
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedTimeMs) {

    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {

    }

    @Override
    public void notifyFatalError(long eventId, String uniqueRequestId, String errorCause) {

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {

    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {

    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
