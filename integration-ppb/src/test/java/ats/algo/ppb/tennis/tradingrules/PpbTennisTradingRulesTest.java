package ats.algo.ppb.tennis.tradingrules;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerConfiguration;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.SimpleAlgoManagerConfiguration;
import ats.algo.algomanager.SupportedSportsInitialisation;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;

public class PpbTennisTradingRulesTest implements AlgoManagerPublishable {

    protected AlgoManager algoManager;
    protected MatchParams publishedMatchParams;
    protected SimpleMatchState publishedMatchState;
    protected Markets publishedMarkets;
    protected ResultedMarkets publishedResultedMarkets;
    protected ParamFindResults publishedParamFinderResults;
    protected MatchResultMap matchResultProforma;
    protected Boolean publishedNotifyEventCompleted;
    protected Set<String> keysForDiscontinuedMarkets;
    protected Recording recording;
    protected TraderAlert traderAlert;



    @Before
    public void beforeEachTest() {
        // System.setProperty("algo.tennis.priceCalculator.class", "ats.algo.gateway.ppb.PpbPriceCalcGateway");
        System.setProperty("algo.tennis.paramFinder.class", "ats.algo.gateway.ppb.PpbParamFinder");
        System.setProperty("algo.tennis.tradingrules.class",
                        "ats.algo.sport.tennis.tradingrules.ppb.PpbTennisTradingRules");
        System.setProperty("updateParamMapWithPropertiesChange", "false");
        System.setProperty("clientMarkets", "ppb");
        AlgoManagerConfiguration algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        SupportedSportsInitialisation.init();
        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        recording = new Recording();
    }

    @After
    public void afterEachTest() {
        System.clearProperty("algo.tennis.priceCalculator.class");
        System.clearProperty("algo.tennis.paramFinder.class");
        System.clearProperty("algo.tennis.tradingrules.class");
        System.clearProperty("updateParamMapWithPropertiesChange");
        algoManager = null;
        recording = null;
        System.clearProperty("clientMarkets");
        SupportedSportsInitialisation.init();

    }

    @Test
    public void testPricingFlip() {
        MethodName.log();
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setServerSideAtStartOfCurrentGame(TeamId.A);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        double[] probs = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        int i = 0;
        if (publishedMarkets != null) {
            for (double prob : publishedMarkets.get("G:ML", "S1.1").getSelectionsProbs().values()) {
                probs[i] = prob;
                i++;
            }

            // System.out.println(Arrays.toString(probs));
            // System.out.println(i);

            startingTennisMatchIncident = null;

            startingTennisMatchIncident = new TennisMatchIncident();
            startingTennisMatchIncident.setElapsedTime(0);
            startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.SERVING_ORDER);
            startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
            startingTennisMatchIncident.setServerSideAtStartOfCurrentGame(TeamId.B);
            startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
            startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
            startingTennisMatchIncident.setEventId(eventId);

            algoManager.handleMatchIncident(startingTennisMatchIncident, true);
            for (double prob : publishedMarkets.get("G:ML", "S1.1").getSelectionsProbs().values()) {
                probs[i] = prob;
                i++;
            }

            startingTennisMatchIncident = null;

            startingTennisMatchIncident = new TennisMatchIncident();
            startingTennisMatchIncident.setElapsedTime(0);
            startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
            startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
            startingTennisMatchIncident.setServerSideAtStartOfCurrentGame(TeamId.B);
            startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
            startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
            startingTennisMatchIncident.setEventId(eventId);

            algoManager.handleMatchIncident(startingTennisMatchIncident, true);

            for (double prob : publishedMarkets.get("G:ML", "S1.1").getSelectionsProbs().values()) {
                probs[i] = prob;
                i++;
            }
            // System.out.println(Arrays.toString(probs) + "");
            assertEquals(probs[3], probs[0], 0.005);
            // System.out.println();
        }

    }

    @Test
    public void testTennisRetirement() {
        MethodName.log();
        System.setProperty("algo.tennis.tradingrules.class",
                        "ats.algo.sport.tennis.tradingrules.ppb.PpbTennisTradingRules");
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        startingTennisMatchIncident.setIncidentId("1296055513");
        startingTennisMatchIncident.setSourceSystem("BETRADAR");
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(matchIncident, true);

        AbandonMatchIncident abandonMatchIncident = new AbandonMatchIncident();
        abandonMatchIncident.setElapsedTime(19);
        abandonMatchIncident.setIncidentId("123");
        abandonMatchIncident.setSourceSystem("BETRADAR");
        abandonMatchIncident.setIncidentSubType(AbandonMatchIncidentType.RETIREMENT);
        abandonMatchIncident.setTeamId(TeamId.B);
        abandonMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(abandonMatchIncident, true);

        // System.out.println(this.publishedResultedMarkets);

        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertEquals(resultedMarket3.getWinningSelections().get(0), "B");

    }

    @Test
    public void testNoMatchStartingIncident() {
        MethodName.log();

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(31);
        startingTennisMatchIncident.setIncidentId("1214002928");
        startingTennisMatchIncident.setSourceSystem("BETRADAR");
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.POINT_WON);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(0);
        startingTennisMatchIncident.setPointWinner(TeamId.A);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);

        TennisMatchStateFromFeed tennisMatchStateFromFeed =
                        new TennisMatchStateFromFeed(0, 0, 0, 0, 1, 0, false, TeamId.A);
        startingTennisMatchIncident.setTennisMatchStateFromFeed(tennisMatchStateFromFeed);
        startingTennisMatchIncident.setEventId(eventId);
        // System.out.println(startingTennisMatchIncident);
        algoManager.handleMatchIncident(startingTennisMatchIncident, true);
        assertTrue(algoManager.getEventDetails(eventId).getEventSettings().getEventTier() == 2);

    }

    @Test
    public void testServerSwap() {
        MethodName.log();

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        startingTennisMatchIncident.setIncidentId("1296055513");
        startingTennisMatchIncident.setSourceSystem("BETRADAR");
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.B);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);
        if (publishedMarkets != null) {
            Market market = this.publishedMarkets.get("G:ML", "S1.1");
            // System.out.println(market);

            startingTennisMatchIncident = null;

            startingTennisMatchIncident = new TennisMatchIncident();
            startingTennisMatchIncident.setElapsedTime(0);
            startingTennisMatchIncident.setIncidentId("1296055513");
            startingTennisMatchIncident.setSourceSystem("BETRADAR");
            startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.SERVING_ORDER);
            startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
            startingTennisMatchIncident.setServerSideAtStartOfCurrentGame(TeamId.A);
            startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
            startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
            startingTennisMatchIncident.setEventId(eventId);
            algoManager.handleMatchIncident(startingTennisMatchIncident, true);

            // System.out.println(this.publishedMarkets);
            Market market1 = this.publishedMarkets.get("G:ML", "S1.1");
            // System.out.println(market1);
            double playerAServe = market.getSelectionsProbs().get("A").doubleValue();
            double playerBServe = market1.getSelectionsProbs().get("B").doubleValue();

            assertNotSame(playerAServe, playerBServe);
        }
    }

    @Test
    public void testEventTierChangePrematchToInPlay() {
        MethodName.log();

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "3");
        algoManager.handleSetEventProperties(12345L, properties);

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        assertTrue(algoManager.getEventDetails(eventId).getEventSettings().getEventTier() == 4);

    }

    @Test
    public void testAllIncidentsGetAlerts() {
        MethodName.log();

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchParams matchParams = new TennisMatchParams();

        assertNull(traderAlert);

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TennisMatchIncident startingTennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        TennisMatchIncident tennisMatchIncident1 = new TennisMatchIncident(0, TennisMatchIncidentType.RAIN, TeamId.A);
        tennisMatchIncident1.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident1, true);

        // System.out.println(traderAlert);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.RAIN);

        TennisMatchIncident tennisMatchIncident2 =
                        new TennisMatchIncident(0, TennisMatchIncidentType.HEAT_DELAY, TeamId.A);
        tennisMatchIncident2.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident2, true);

        // System.out.println(traderAlert);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.HEAT_DELAY);

        TennisMatchIncident tennisMatchIncident3 =
                        new TennisMatchIncident(0, TennisMatchIncidentType.HEAT_DELAY, TeamId.A);
        tennisMatchIncident3.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident3, true);

        // System.out.println(traderAlert);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.HEAT_DELAY);

        TennisMatchIncident tennisMatchIncident4 =
                        new TennisMatchIncident(0, TennisMatchIncidentType.ON_COURT_COACHING, TeamId.A);
        tennisMatchIncident4.setEventId(eventId);

        algoManager.handleMatchIncident(tennisMatchIncident4, true);

        // System.out.println(traderAlert);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.ON_COURT_COACHING);

        // System.out.println("Test passed");

    }

    /**
     * verify that every market that was published has been resulted
     * 
     * @param marketKeys
     * @param resultedMarkets
     * @param exceptionsSet
     */
    static void verifyAllMarketsResulted(Set<String> marketKeys, ResultedMarkets resultedMarkets,
                    Set<String> exceptionsSet) {
        boolean failTest = false;
        for (String key : marketKeys) {
            ResultedMarket resultedMarket = resultedMarkets.getResultedMarkets().get(key);
            if (resultedMarket == null) {
                if (!exceptionsSet.contains(key)) {
                    // System.out.println("Missing resulted market: " + key);
                    failTest = true;
                }
            }
        }
        if (failTest)
            fail();
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {

    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {

    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;

    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets ResultedMarkets) {
        this.publishedResultedMarkets = ResultedMarkets;

    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams, long elapsedtimeMs) {

    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {

    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {

    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {

    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
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
