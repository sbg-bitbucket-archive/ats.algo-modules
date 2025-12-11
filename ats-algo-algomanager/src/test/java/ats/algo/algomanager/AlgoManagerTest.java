package ats.algo.algomanager;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.StopWatch;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchStateFromFeed;
import ats.algo.sport.tennis.TennisMatchEngine;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisSimpleMatchState;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchIncident;
import ats.algo.sport.volleyball.VolleyballMatchIncident.VolleyballMatchIncidentType;
import ats.algo.sport.volleyball.VolleyballMatchParams;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

/*
 * tests functionality using the single threaded SimpleAlgoManagerConfiguration
 */
public class AlgoManagerTest implements AlgoManagerPublishable {

    AlgoManager algoManager;
    MatchParams publishedMatchParams;
    SimpleMatchState publishedMatchState;
    Markets publishedMarkets;
    ResultedMarkets publishedResultedMarkets;
    ParamFindResults publishedParamFinderResults;
    MatchResultMap matchResultProforma;
    Boolean publishedNotifyEventCompleted;
    Set<String> keysForDiscontinuedMarkets;
    long publishedEventId;

    public AlgoManagerTest() {
        /*
         * define the various functions that AlgoManager is going to need
         */

        LogUtil.initConsoleLogging(Level.TRACE);

        AlgoManagerConfiguration algoManagerConfiguration = new SimpleAlgoManagerConfiguration();
        SupportedSportsInitialisation.init();
        /*
         * instantiate AlgoManager and tell it what functions use to calc prices and report the results
         */

        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[0];
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.publishResultedMarketsImmediately(true);
        recording = new Recording();
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void testStatCollection() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 1L, tennisMatchFormat);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 2L, tennisMatchFormat);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 3L, tennisMatchFormat);

        FootballMatchFormat footballMatchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 4L, footballMatchFormat);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 5L, footballMatchFormat);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 6L, footballMatchFormat);

        algoManager.handleSupplyMarketPrices(1L, getTestMarketPricesChaff());
        algoManager.handleSupplyMarketPrices(2L, getTestMarketPricesChaff());
        algoManager.handleSupplyMarketPrices(3L, getTestMarketPricesChaff());

        algoManager.handleSupplyMarketPrices(4L, getTestMarketPricesforFootballStatsTest());
        algoManager.handleSupplyMarketPrices(5L, getTestMarketPricesforFootballStatsTest());
        algoManager.handleSupplyMarketPrices(5L, getTestMarketPricesforFootballStatsTest());

        TennisMatchParams tennisMatchParams = new TennisMatchParams();
        GenericMatchParams tennisGenericMatchParams = tennisMatchParams.generateGenericMatchParams();

        FootballMatchParams footballMatchParams = new FootballMatchParams();
        GenericMatchParams footballGenericMatchParams = footballMatchParams.generateGenericMatchParams();

        tennisGenericMatchParams.setEventId(1L);
        algoManager.handleSetMatchParams(tennisGenericMatchParams);

        tennisGenericMatchParams.setEventId(2L);
        algoManager.handleSetMatchParams(tennisGenericMatchParams);

        tennisGenericMatchParams.setEventId(3L);
        algoManager.handleSetMatchParams(tennisGenericMatchParams);

        footballGenericMatchParams.setEventId(4L);
        algoManager.handleSetMatchParams(footballGenericMatchParams);

        footballGenericMatchParams.setEventId(5L);
        algoManager.handleSetMatchParams(footballGenericMatchParams);

        footballGenericMatchParams.setEventId(6L);
        algoManager.handleSetMatchParams(footballGenericMatchParams);



    }

    @Test
    public void test() {
        /*
         * create two tennis and one darts event. Since single threaded, the publish methods will be called before the
         * handleNewEventCreation method completes, so we can test results immediately
         *
         * FinalSetType finalSetType, boolean noAdvantageGameFormat, boolean isDoublesMatch
         */
        TennisMatchFormat tennisMatchFormat1 =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat1);
        Gaussian g = publishedMatchParams.getParamMap().get("onServePctA").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertEquals(11L, publishedMatchParams.getEventId());
        TennisMatchParams tennisMatchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, tennisMatchParams.getOnServePctA1().getMean(), 0.01);
        TennisSimpleMatchState TennisSimpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, TennisSimpleMatchState.getOnServeNow());
        // System.out.println (publishedMarkets);
        // assertEquals(0.29, publishedMarkets.get("FT:W1S:A").get("A No"), 0.01);


        /*
         * try to create a new event with the same id. Should fail
         */
        try {
            algoManager.handleNewEventCreation(SupportedSportType.DARTS, 11L, tennisMatchFormat1);
            fail();
        } catch (Exception e) {
            /*
             * do nothing - test passed
             */
        }
        /*
         * create second tennis event
         */
        TennisMatchFormat tennisMatchFormat2 =
                        new TennisMatchFormat(5, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 12L, tennisMatchFormat2);
        g = publishedMatchParams.getParamMap().get("onServePctA").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertEquals(12L, publishedMatchParams.getEventId());
        // assertEquals(0.19, publishedMarkets.get("FT:W1S:A").get("A No"), 0.01);
        assertEquals(TeamId.UNKNOWN, TennisSimpleMatchState.getOnServeNow());
        assertEquals(null, publishedMarkets.get("G:ML"));
        /*
         * create a darts event
         */
        DartMatchFormat dartMatchFormat = new DartMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.DARTS, 21L, dartMatchFormat);
        g = publishedMatchParams.getParamMap().get("skillA").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertEquals(0.64, publishedMarkets.get("G:1180", "L1.1").get("No"), 0.01);
        assertEquals(42681241600L, 42681241600L);
        /*
         * fire in an incident for the second tennis match
         */
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(12L);
        tennisMatchIncident.setIncidentId("REQUEST_ID_235");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        TennisSimpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.A, TennisSimpleMatchState.getOnServeNow());
        System.out.println(publishedMarkets.toString());
        assertEquals(0.82, publishedMarkets.get("G:ML", "S1.1").get("A"), 0.01);

        /*
         * undo the last incident
         */
        publishedMatchState = null;
        assertTrue(algoManager.handleUndoLastMatchIncident(12L));
        assertEquals(12L, publishedMatchParams.getEventId());
        TennisSimpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, TennisSimpleMatchState.getOnServeNow());
        assertEquals(null, publishedMarkets.get("G:ML"));
        /*
         * set the matchParams explicitly for the first tennis match
         */
        tennisMatchParams = new TennisMatchParams();
        tennisMatchParams.setOnServePctA1(0.650001, 0.08);
        tennisMatchParams.setOnServePctB1(0.58, 0.05);
        tennisMatchParams.setEventId(11L);
        this.publishedMarkets = null;
        algoManager.handleSetMatchParams(tennisMatchParams.generateGenericMatchParams());
        TennisMatchParams publishedTennisMatchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, publishedTennisMatchParams.getOnServePctA1().getMean(), 0.01);
        assertEquals(0.08, publishedTennisMatchParams.getOnServePctA1().getStdDevn(), 0.01);
        assertEquals(0.71, publishedMarkets.get("FT:ML").get("A"), 0.01);
    }

    @Test
    public void testDifferentMatchFormat() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.WOMEN, Surface.CLAY, TournamentLevel.WTA,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.55, matchParams.getOnServePctA1().getMean(), 0.01);
    }

    @Test
    public void testMatchParamBiasMaintained() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        matchParams.getOnServePctA1().setBias(0.05);
        assertEquals(0.65, matchParams.getOnServePctA1().getMean(), 0.005);
        assertEquals(0.05, matchParams.getOnServePctA1().getBias(), 0.005);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        TennisMatchParams matchParams2 = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, matchParams2.getOnServePctA1().getMean(), 0.005);
        assertEquals(0.05, matchParams2.getOnServePctA1().getBias(), 0.005);

        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        matchIncident.setEventId(11L);
        algoManager.handleMatchIncident(matchIncident, true);
        matchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, matchParams.getOnServePctA1().getMean(), 0.005);
        assertEquals(0.05, matchParams.getOnServePctA1().getBias(), 0.005);
        matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchParams = (TennisMatchParams) publishedMatchParams;
        assertEquals(0.65, matchParams.getOnServePctA1().getMean(), 0.005);
        assertEquals(0.05, matchParams.getOnServePctA1().getBias(), 0.005);
    }

    @Test
    public void testResetEvent() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 13579L, tennisMatchFormat);
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(13579L);
        tennisMatchIncident.setIncidentId("REQUEST_ID_235");
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        TennisSimpleMatchState TennisSimpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.A, TennisSimpleMatchState.getOnServeNow());
        algoManager.handleResetEvent(13579L, tennisMatchFormat, 3);
        TennisSimpleMatchState = (TennisSimpleMatchState) publishedMatchState;
        assertEquals(TeamId.UNKNOWN, TennisSimpleMatchState.getOnServeNow());

    }

    @Test
    public void testTieBreak() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(11L);
        algoManager.handleMatchIncident(matchIncident, true);
        TennisMatchIncident matchIncidentA = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchIncidentA.setEventId(11L);
        TennisMatchIncident matchIncidentB = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        matchIncidentB.setEventId(11L);
        /*
         * play through to 6-6
         */
        boolean gameWinnerA = true;
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 4; j++) {
                if (gameWinnerA)
                    algoManager.handleMatchIncident(matchIncidentA, true);
                else
                    algoManager.handleMatchIncident(matchIncidentB, true);
            }
            gameWinnerA = !gameWinnerA;
        }

        TennisMatchState matchState = (TennisMatchState) algoManager.getMatchState(11L);
        assertEquals("S1.13.1", matchState.getSequenceIdForPoint(0));
        assertEquals(6, matchState.getGamesA());
        assertEquals(6, matchState.getGamesB());
        double probAWinsMatch = publishedMarkets.get("FT:ML").get("A");
        for (int j = 1; j <= 4; j++) {
            algoManager.handleMatchIncident(matchIncidentA, true);
            System.out.println("Handled incident for: " + matchState.getSequenceIdForPoint(0) + "\n");
        }
        double probAWinsMatch2 = publishedMarkets.get("FT:ML").get("A");
        assertTrue(probAWinsMatch != probAWinsMatch2);

    }

    /*
     * compare calculating via algoManager with calculating directly via the matchEngine
     * 
     * Note this test generally passes when run on its own, but not when run as part of a group of tests, so generally
     * leave it commented out
     * 
     */
    // @Test
    public void performanceTest() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 10L, tennisMatchFormat);
        TennisMatchEngine matchEngine = new TennisMatchEngine(tennisMatchFormat);
        TennisSimpleMatchState matchState = (TennisSimpleMatchState) matchEngine.getMatchState();
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(10L);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        matchState.updateStateForIncident(tennisMatchIncident, true);
        matchEngine.setMatchState(matchState);
        matchEngine.calculate();
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);

        StopWatch sw = new StopWatch();
        sw.start();
        for (int i = 0; i < 20; i++) {
            algoManager.handleMatchIncident(tennisMatchIncident, true);
        }
        sw.stop();
        double elapsedTimeAlgoManager = sw.getElapsedTimeSecs();
        System.out.printf("AlgoManager elapsed time %.1f seconds\n", elapsedTimeAlgoManager);
        System.out.print("\nRepeat calculations directly through matchEngine to give comparison...\n");
        sw = new StopWatch();
        sw.start();
        for (int i = 0; i < 20; i++) {
            matchState.updateStateForIncident(tennisMatchIncident, true);
            matchEngine.setMatchState(matchState);
            matchEngine.calculate();
        }
        sw.stop();
        double elapsedTimeMatchEngine = sw.getElapsedTimeSecs();
        System.out.printf("MatchEngine elapsed time %.1f seconds\n", elapsedTimeMatchEngine);
        double pctDiff = (elapsedTimeAlgoManager - elapsedTimeMatchEngine) / elapsedTimeMatchEngine;
        System.out.printf("Percentage difference in elapsed time (target <20pct): %.2f\n", pctDiff);
        /*
         * check to see that overhead of running via algo mgr is not greater than 20%
         */
        assertEquals(0, pctDiff, 0.2);
    }

    @Test
    public void testParamFindForClosePrices() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 1234567890123456789L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        setPublishedDataToNull();
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesNear(false));
        assertTrue(publishedParamFinderResults.isSuccess());
        assertEquals(0.0, publishedParamFinderResults.getFunctionValueAtMinimum(), 0.02);
        assertEquals(0.50, publishedMarkets.get("FT:ML").get("A"), 0.02);


    }

    @Test
    public void testParamFind() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 1234567890123456789L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        /*
         * compare with prices - param find should be triggered
         */
        System.out.println("TEST FAR");
        setPublishedDataToNull();
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFar());
        assertTrue(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.getnIterations() > 0);
        assertEquals(0.0, publishedParamFinderResults.getFunctionValueAtMinimum(), 0.02);
        TennisMatchParams publishedTennisMatchParams = (TennisMatchParams) publishedMatchParams;
        double onServePctDiff = publishedTennisMatchParams.getOnServePctA1().getMean()
                        - publishedTennisMatchParams.getOnServePctB1().getMean();
        assertEquals(0.063, onServePctDiff, 0.02);
        assertEquals(0.056, publishedTennisMatchParams.getOnServePctA1().getStdDevn(), 0.02);
        assertEquals(0.72, publishedMarkets.get("FT:ML").get("A"), 0.02);
        /*
         * compare with prices that are clearly wrong - param find will return false
         */

        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesError());
        assertEquals(eventId, publishedEventId);
        assertTrue(publishedParamFinderResults.getnIterations() > 0);
        assertEquals(ParamFindResultsStatus.GREEN, publishedParamFinderResults.getParamFindResultsStatus());
    }

    @Test
    public void testSequenceIdConversion() {
        LogUtil.initConsoleLogging(Level.TRACE);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);

        long eventId = 1234567890123456789L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident, true);
        /*
         * compare with prices close to the current prices - should still trigger a full param find
         */
        System.out.println("TEST SEQUENCE ID CONVERSION");
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesWithExternalSequenceId());
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        double onServePctA = matchParams.getOnServePctA1().getMean();
        double onServePctB = matchParams.getOnServePctB1().getMean();
        assertEquals(0.65, onServePctA, 0.08);
        assertEquals(0.55, onServePctB, 0.08);
    }

    @Test
    public void testMarketsNotPublishedBeforeParamChange() {
        this.publishedMarkets = null;
        algoManager.onlyPublishMarketsFollowingParamChange(true);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 37L, new TennisMatchFormat());
        assertTrue(this.publishedMarkets == null);
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        matchIncident.setEventId(37L);
        algoManager.handleMatchIncident(matchIncident, true);
        assertTrue(this.publishedMarkets == null);
        ((TennisMatchParams) publishedMatchParams).getOnServePctA1().setMean(.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertFalse(this.publishedMarkets == null);
        this.publishedMarkets = null;
        matchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(matchIncident, true);
        assertFalse(this.publishedMarkets == null);
    }

    /**
     * should be close to the default settings, so should not trigger a paramFind
     *
     * @return
     */
    private MarketPricesList getTestMarketPricesNear(boolean useTotals) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.87);
        ab.put("B", 1.87);
        m.addMarketPrice(ab);

        if (useTotals) {
            MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
            tmtg.put("Over", 1.82);
            tmtg.put("Under", 1.92);
            m.addMarketPrice(tmtg);
        }

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
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

    /**
     * should trigger a param find
     *
     * @return
     */
    private MarketPricesList getTestMarketPricesError() {
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
        MarketPrice atws = new MarketPrice("FT:W1S:A", "A to win a set", MarketCategory.GENERAL, null);
        atws.put("No", 1.12);
        atws.put("Yes", 5.83);
        m.addMarketPrice(atws);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesWithExternalSequenceId() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);
        MarketPrice mw = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        mw.setSequenceId("M");
        mw.put("A", 1.17);
        mw.put("B", 4.94);
        m.addMarketPrice(mw);
        MarketPrice cg = new MarketPrice("G:ML", "Current game winner", MarketCategory.GENERAL, null);
        cg.setSequenceId("G1");
        cg.put("A", 1.18);
        cg.put("B", 4.71);
        m.addMarketPrice(cg);
        MarketPrice ng = new MarketPrice("G:ML", "Next game winner", MarketCategory.GENERAL, null);
        ng.setSequenceId("G2");
        ng.put("A", 2.41);
        ng.put("B", 1.53);
        m.addMarketPrice(ng);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    @Test
    public void testResultedMarkets() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        System.out.println(publishedResultedMarkets);
        // assertEquals(3, publishedResultedMarkets.size());
        System.out.println(publishedMarkets);
        System.out.println(publishedMarkets.size());
        System.out.println(publishedResultedMarkets);
        System.out.println(publishedResultedMarkets.size());
        // assertEquals(2, publishedResultedMarkets.getFullyResultedMarkets().size());
        System.out.print(publishedResultedMarkets.toString());
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("G:PW_S1.1.2");
        assertEquals("S1.1.2", resultedMarket.getSequenceId());
        assertEquals("A", resultedMarket.getWinningSelections().get(0));
    }

    @Test
    public void testFootballResultedMarkets1() {
        // algoManager.autoSyncWithMatchFeed(false);
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incidentStart =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incidentStart.setEventId(eventId);
        incidentStart.setIncidentId("R1");

        algoManager.handleMatchIncident(incidentStart, true);

        sleep(2);

        FootballMatchStateFromFeed matchStateFromFeed =
                        new FootballMatchStateFromFeed(0, 1, 2, 5, 0, 0, 0, 0, FootballMatchPeriod.IN_FIRST_HALF);
        FootballMatchIncident footballMatchIncident =
                        new FootballMatchIncident(FootballMatchIncidentType.GOAL, 2160, TeamId.B);
        footballMatchIncident.setFootballMatchStateFromFeed(matchStateFromFeed);
        footballMatchIncident.setEventId(eventId);
        footballMatchIncident.setIncidentId("R2");

        algoManager.handleMatchIncident(footballMatchIncident, true);

        sleep(2);


        FootballMatchStateFromFeed matchStateFromFeed1 =
                        new FootballMatchStateFromFeed(0, 1, 2, 5, 0, 0, 0, 0, FootballMatchPeriod.AT_HALF_TIME);
        ElapsedTimeMatchIncident elapsedTimeMatchIncident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        elapsedTimeMatchIncident.setMatchStateFromFeed(matchStateFromFeed1);
        elapsedTimeMatchIncident.setEventId(eventId);
        elapsedTimeMatchIncident.setIncidentId("R3");

        algoManager.handleMatchIncident(elapsedTimeMatchIncident, true);

        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("P:CS_H1");
        assertTrue(resultedMarket != null);

    }

    @Test
    public void testFootballResultedMarkets() {


        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incidentStart =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incidentStart.setEventId(eventId);
        incidentStart.setIncidentId("R1");
        algoManager.handleMatchIncident(incidentStart, true);
        ElapsedTimeMatchIncident incidentEnd =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        incidentEnd.setIncidentId("R2");
        incidentEnd.setEventId(eventId);
        algoManager.handleMatchIncident(incidentEnd, true);
        sleep(6);
        algoManager.handleMatchIncident(incidentStart, true);
        incidentStart.setIncidentId("R3");
        sleep(6);
        algoManager.handleMatchIncident(incidentEnd, true);
        sleep(6);
        incidentEnd.setIncidentId("R4");
        System.out.println(publishedResultedMarkets);
        System.out.print(publishedResultedMarkets.toString());
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:20MG_M");
        assertTrue(resultedMarket != null);
        // assertEquals("S1.1.2", resultedMarket.getSequenceId());
        // assertEquals("A", resultedMarket.getWinningSelections().get(0));
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:60MR_F0");
        assertTrue(resultedMarket2 != null);
        ResultedMarket resultedMarket3 = publishedResultedMarkets.getResultedMarkets().get("FT:60MR_F1");
        assertTrue(resultedMarket3 == null);
        ResultedMarket resultedMarket4 = publishedResultedMarkets.getResultedMarkets().get("FT:75MR_F0");
        assertTrue(resultedMarket4 != null);
    }


    @Test
    public void testResultedMarketsFor3SetTennisMatch() {
        testResultedMarketsForCompleteMatch(3);
    }

    @Test
    public void testResultedMarketsFor5SetTennisMatch() {
        testResultedMarketsForCompleteMatch(5);
    }

    private void testResultedMarketsForCompleteMatch(int nSets) {
        algoManager.publishResultedMarketsImmediately(true);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(nSets, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        CollectMarkets collectMarkets = new CollectMarkets();
        int incidentId = 1;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        Gaussian g = publishedMatchParams.getParamMap().get("onServePctA").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        collectMarkets.addToSet(publishedMarkets);
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        tennisMatchIncident.setIncidentId("R" + Integer.toString(incidentId++));
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        collectMarkets.addToSet(publishedMarkets);
        Markets oldMarkets = publishedMarkets;
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        int nPoints = (nSets / 2 + 1) * 24 - 1;
        System.out.println("Calculating...");
        for (int i = 0; i < nPoints; i++) {
            tennisMatchIncident.setIncidentId("R" + Integer.toString(incidentId++));
            algoManager.handleMatchIncident(tennisMatchIncident, true);
            collectMarkets.addToSet(publishedMarkets);
            /*
             * also check the logic for publishing discontinued market keys
             */
            checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
            oldMarkets = publishedMarkets;
            // if (i==10) {
            // String json = JsonSerializer.serialize(SerializationType.STANDARD, this.publishedResultedMarkets, true);
            // System.out.println(json);
            // }
        }
        System.out.println("Final point in match...");
        assertEquals(null, publishedNotifyEventCompleted);
        MatchState penultimateMatchState = publishedMatchState.copy();
        int nResultedMarketsBeforeCompletion = publishedResultedMarkets.size();
        /*
         * next incident will be final point of the match
         */
        publishedMarkets = null;
        tennisMatchIncident.setIncidentId("R" + Integer.toString(incidentId++));
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        collectMarkets.addToSet(publishedMarkets);
        // System.out.println(collectMarkets.toString());
        assertTrue(publishedNotifyEventCompleted);
        assertTrue(publishedResultedMarkets.size() > nResultedMarketsBeforeCompletion);
        System.out.println(publishedResultedMarkets);
        for (String fullKey : collectMarkets.getAllMarketKeys()) {
            ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get(fullKey);
            String winningSelections;
            if (resultedMarket == null)
                winningSelections = "null";
            else
                winningSelections = resultedMarket.getWinningSelections().toString();
            System.out.println(fullKey + ": " + winningSelections);
            assertTrue(resultedMarket != null);
        }
        /*
         * undo the match completion incident
         */
        algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(publishedNotifyEventCompleted);
        assertEquals(penultimateMatchState, publishedMatchState);
    }

    /*
     * Might fail due to football internal timed calc. Check FootballMatchState.updateElapsedTime() for price calc time.
     * Has changed to appease Paras and reduce strain on Bob whilst integrating football.
     */

    @Test
    public void testTimer() {
        BasketballMatchFormat matchFormat = new BasketballMatchFormat();
        long eventId = 789L;

        algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat);
        Gaussian g = publishedMatchParams.getParamMap().get("pace").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        publishedMarkets = null;
        /*
         * wait 12 secs - should not be any recalc of prices since pre match
         */
        sleep(15);
        assertNull(publishedMarkets);
        /*
         * start the match clock. Should trigger a timer driven update after 10 secs
         */
        System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incident.setEventId(eventId);
        incident.setIncidentId("R1");
        algoManager.handleMatchIncident(incident, true);
        publishedMarkets = null;
        sleep(15);
        assertFalse(publishedMarkets == null);
    }

    private static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
    }


    @Test
    public void testSetProperties() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 789L, matchFormat);
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("SOURCEWEIGHT_Bet365", "2.3");
        algoManager.handleSetEventProperties(789L, properties);

    }

    @Test
    public void testFootball() {
        /*
         * basic smoke test for each sport to confirm can calculate a price
         */
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 789L, matchFormat);
        Gaussian g = publishedMatchParams.getParamMap().get("goalTotal").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        FootballMatchParams matchParams = (FootballMatchParams) publishedMatchParams;
        // assertEquals(1.5, matchParams.getHomeScoreRate().getMean(), 0.01);
        System.out.println(publishedMarkets);
        assertEquals(0.29, publishedMarkets.get("FT:AXB").get("A"), 0.44);
        algoManager.onlyPublishCornersAndCardsMarket(true);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        System.out.println("after corners and cards set");
        System.out.println(publishedMarkets);
    }

    @Test
    public void testTimerAfl() {
        AflMatchFormat matchFormat = new AflMatchFormat();
        long eventId = 789L;
        algoManager.handleNewEventCreation(SupportedSportType.AUSSIE_RULES, eventId, matchFormat);
        MatchParams defaultMatchParams = this.publishedMatchParams;
        Gaussian g = defaultMatchParams.getParamMap().get("totalScoreRate").getGaussian();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        publishedMarkets = null;
        /*
         * wait 12 secs - should not be any recalc of prices since pre match
         */
        sleep(12);
        assertNull(publishedMarkets);
        /*
         * start the match clock. Should trigger a timer driven update after 10 secs
         */
        System.out.printf("End of part 1\n");
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);

        assertTrue(algoManager.getEventDetails(eventId).getEventState().getMatchState().isClockRunning());
        // System.out.println(publishedMarkets.getRequestTime());
        incident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK, 0);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        assertFalse(algoManager.getEventDetails(eventId).getEventState().getMatchState().isClockRunning());
    }

    @Test
    public void testFootballCornersAndCards() {

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 789L, matchFormat);
        assertEquals(789L, publishedEventId);
        FootballMatchParams matchParams = (FootballMatchParams) publishedMatchParams;
        Gaussian g = publishedMatchParams.getParamMap().get("goalTotal").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        // System.out.println(publishedMarkets);
        assertEquals(0.29, publishedMarkets.get("FT:AXB").get("A"), 0.44);
        algoManager.onlyPublishCornersAndCardsMarket(true);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        // System.out.println("after corners and cards set");
        // System.out.println(publishedMarkets);
        MatchIncident matchIncident;
        matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchIncident.setEventId(789L);
        algoManager.handleMatchIncident(matchIncident, true);
        matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 45 * 60);
        matchIncident.setEventId(789L);
        algoManager.handleMatchIncident(matchIncident, true);
        matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 45 * 60);
        matchIncident.setEventId(789L);
        algoManager.handleMatchIncident(matchIncident, true);
        matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 91 * 60);
        matchIncident.setEventId(789L);
        algoManager.handleMatchIncident(matchIncident, true);
        assertFalse(publishedNotifyEventCompleted == null);
        assertTrue(publishedNotifyEventCompleted);
        matchIncident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 92 * 60);
        matchIncident.setEventId(789L);
        algoManager.handleMatchIncident(matchIncident, true);

    }



    @Test
    public void testVolleyball() {
        /*
         * basic smoke test + check momentum logic ok
         */
        VolleyballMatchFormat matchFormat = new VolleyballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.VOLLEYBALL, 789L, matchFormat);
        ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().setMean(0.35);
        ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().setMean(0.35);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertEquals(0.35, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        VolleyballMatchIncident incident = new VolleyballMatchIncident();
        incident.setEventId(789L);
        incident.setIncidentSubType(VolleyballMatchIncidentType.SERVEFIRST);
        incident.setTeamId(TeamId.A);
        algoManager.handleMatchIncident(incident, true);
        incident.setIncidentSubType(VolleyballMatchIncidentType.POINTWON);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(0.355, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        assertEquals(0.350, ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().getMean(), 0.001);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(0.359, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        assertEquals(0.350, ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().getMean(), 0.001);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(0.363, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        assertEquals(0.350, ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().getMean(), 0.001);
        incident.setTeamId(TeamId.B);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(0.361, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        assertEquals(0.350, ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().getMean(), 0.001);
        algoManager.handleMatchIncident(incident, true);
        assertEquals(0.361, ((VolleyballMatchParams) publishedMatchParams).getOnServePctA().getMean(), 0.001);
        assertEquals(0.355, ((VolleyballMatchParams) publishedMatchParams).getOnServePctB().getMean(), 0.001);

    }

    // @Test
    /*
     * note to execute this test, need to modify the test rig to load the correct set of trading rules
     */
    public void testParamFindTradingRulesPreMatch() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        long eventId = 23L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        /*
         * pre match - no FT:ML
         */
        algoManager.handleSupplyMarketPrices(eventId, getPfTestMarketPrices1());
        // System.out.print(publishedParamFinderResults);
        assertFalse(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary().contains("AMBER"));
        /*
         * include Moneyline
         */
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesNear(true));
        System.out.print(publishedParamFinderResults);
        assertTrue(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary()
                        .contains("No param find necessary"));
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary().contains("GREEN"));
        /*
         * trigger a real param find
         */
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFar());
        System.out.print(publishedParamFinderResults);
        assertTrue(publishedParamFinderResults.isSuccess());
        assertFalse(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary()
                        .contains("No param find necessary"));
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary().contains("GREEN"));
        /*
         * try some prices that don't fit each other
         */
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesError());
        System.out.print(publishedParamFinderResults);
        assertFalse(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary()
                        .contains("Some prob differences exceed threshold"));
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary()
                        .contains("Param find failed to find good fit - check prices"));
        assertTrue(publishedParamFinderResults.getParamFindResultsDescription().getResultSummary().contains("RED"));
    }

    @Test
    public void testSetEventTier() {
        TradingRules tradingRules = new TennisTradingRules();
        ArrayList<AbstractTradingRule> arrayList = tradingRules.getTradingRules();
        AbstractTradingRule[] ruleArray = new AbstractTradingRule[arrayList.size()];
        ruleArray = arrayList.toArray(ruleArray);
        algoManager.setTradingRules(SupportedSportType.TENNIS, ruleArray);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, new TennisMatchFormat(), 3);
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());
        /*
         * set eventTier to 3. FT:OU should have status SUSPENDED_UNDISPLAY
         */
        System.out.println(publishedMarkets);
        MarketStatus marketStatus = publishedMarkets.get("FT:OU", "23.5", "M").getMarketStatus();
        SuspensionStatus status = marketStatus.getSuspensionStatus();
        assertEquals(SuspensionStatus.OPEN, status);
        MatchParams matchParams = publishedMatchParams.copy();
        /*
         * set tier to 1
         */
        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(11L, properties);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        status = publishedMarkets.get("FT:OU", "23.5", "M").getMarketStatus().getSuspensionStatus();
        assertEquals(SuspensionStatus.OPEN, status);
        /*
         * and back to 3
         */
        properties.put("eventTier", "3");
        algoManager.handleSetEventProperties(11L, properties);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        marketStatus = publishedMarkets.get("FT:OU", "23.5", "M").getMarketStatus();
        status = marketStatus.getSuspensionStatus();
        assertEquals(SuspensionStatus.OPEN, status);
    }



    @Test
    public void testKeysForDiscontinuedMarkets() {
        LogUtil.initConsoleLogging(Level.WARN);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, new TennisMatchFormat());
        System.out.println("AFTER EVENT CREATION");
        TennisMatchParams defaultMatchParams = (TennisMatchParams) this.publishedMatchParams;
        defaultMatchParams.setEventId(11L);
        Gaussian g = defaultMatchParams.getOnServePctA1();
        g.setMean(g.getMean() + 0.000001); // make a small change the default params
        algoManager.handleSetMatchParams(defaultMatchParams.generateGenericMatchParams());

        Markets oldMarkets = publishedMarkets;
        TennisMatchParams matchParams = (TennisMatchParams) this.publishedMatchParams;
        matchParams.setOnServePctA1(.6, .05);
        matchParams.setOnServePctB1(.65, .05);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        System.out.println("AFTER PARAM CHANGE");
        checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        oldMarkets = publishedMarkets;
        matchParams.setOnServePctA1(.5, .05);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        System.out.println("AFTER PARAM CHANGE2");
        checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        oldMarkets = publishedMarkets;
        algoManager.setTradingRules(SupportedSportType.TENNIS, testTradingRules());
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams()); // kick algoMgr to
                                                                                    // do recalc
        System.out.println("AFTER NEW TRADING RULES");
        checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        oldMarkets = publishedMarkets;
        // System.out.println(publishedMarkets);
        // System.out.println(keysForDiscontinuedMarkets);
        // assertTrue(this.publishedMarkets.getMarkets().keySet().contains("FT:OU#15.5_M"));
        // assertTrue(this.publishedMarkets.getMarkets().keySet().contains("FT:OU#19.5_M"));
        // assertEquals(0, this.keysForDiscontinuedMarkets.size());
        algoManager.setTradingRules(SupportedSportType.TENNIS, emptyTradingRules());
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams()); // kick algoMgr to
                                                                                    // do recalc
        checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        // assertFalse(this.publishedMarkets.getMarkets().keySet().contains("FT:OU#15.5_M"));
        // assertFalse(this.publishedMarkets.getMarkets().keySet().contains("FT:OU#19.5_M"));
        // assertTrue(this.keysForDiscontinuedMarkets.contains("FT:OU#15.5_M"));
        // assertTrue(this.keysForDiscontinuedMarkets.contains("FT:OU#19.5_M"));
        // algoManager.handleSetMatchParams(matchParams); // kick algoMgr to do recalc
    }

    public static void checkConsistent(Markets oldMarkets, Markets newMarkets, Set<String> keysForDiscontinuedMarkets) {
        Set<String> oldKeys = extractKeys(oldMarkets);
        Set<String> newKeys = extractKeys(newMarkets);
        System.out.println("oldKeys: " + oldKeys);
        System.out.println("newKeys: " + newKeys);
        System.out.println("deltaKeys: " + keysForDiscontinuedMarkets);
        for (String key : oldKeys)
            if (!newKeys.contains(key))
                assertTrue(keysForDiscontinuedMarkets.contains(key));
            else
                assertFalse(keysForDiscontinuedMarkets.contains(key));
    }

    static Set<String> extractKeys(Markets markets) {
        Set<String> set = new HashSet<String>();
        for (String key : markets.getMarkets().keySet())
            set.add(key);
        return set;
    }


    AbstractTradingRule[] testTradingRules() {
        AbstractTradingRule[] rules = new AbstractTradingRule[1];
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 1);
        DerivedMarketTradingRule derivedMarketTradingRule =
                        new DerivedMarketTradingRule(null, "FT:OU", derivedMarketSpec);
        rules[0] = derivedMarketTradingRule;
        return rules;
    }

    SetSuspensionStatusTradingRule[] emptyTradingRules() {
        return new SetSuspensionStatusTradingRule[0];

    }


    /**
     * should be close to the default settings, so should not trigger a paramFind
     *
     * @return
     */
    private MarketPricesList getPfTestMarketPrices1() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
        tmtg.put("Over", 1.82);
        tmtg.put("Under", 1.92);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforFootballStatsTest() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.33);
        b1.put("B", 7.00);
        b1.put("X", 4.50);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.35);
        b2.put("Under", 3.10);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    /**
     * should be close to the default settings, so should not trigger a paramFind
     *
     * @return
     */
    private MarketPricesList getTestMarketPricesChaff() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.33);
        ab.put("B", 3.25);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "21.5");
        tmtg.put("Over", 1.72);
        tmtg.put("Under", 2.00);
        m.addMarketPrice(tmtg);

        MarketPrice ghcp = new MarketPrice("FT:SPRD", "Game Handicap", MarketCategory.HCAP, "-3.5");
        ghcp.put("AH", 1.80);
        ghcp.put("BH", 1.90);
        m.addMarketPrice(ghcp);

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    /*
     * obsolete test I think.
     */
    // @Test
    // public void testPfGameWinnerMarketErrorAtEndOfSet() {
    // TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
    // long eventId = 987654321L;
    // algoManager.setOnlyPublishMarketsFollowingParamChange(false);
    // TradingRules tradingRules = new BsTennisTradingRules();
    // algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
    // algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
    // TennisMatchIncident tennisMatchIncident =
    // new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
    // tennisMatchIncident.setEventId(eventId);
    // algoManager.handleMatchIncident(tennisMatchIncident, true);
    // /*
    // * uncomment this if you need to check pf working ok
    // */
    // // MarketPricesList marketPricesList = this.getTestMarketPricesForPfError1(eventId);
    // // algoManager.handleCompareToMarket(marketPricesList);
    // // assertEquals(ParamFindResultsStatus.GREEN,
    // // this.publishedParamFinderResults.getSummaryStatus());
    // tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
    // /*
    // * get the score up to 5-6
    // */
    // for (int i = 1; i <= 11; i++) {
    // if (2 * (i / 2) == i)
    // tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
    // else
    // tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
    // for (int j = 1; j <= 4; j++) {
    // algoManager.handleMatchIncident(tennisMatchIncident, true);
    // }
    // }
    // algoManager.handleMatchIncident(tennisMatchIncident, true);
    // TennisSimpleMatchState matchState = (TennisSimpleMatchState) this.publishedMatchState;
    // /*
    // * check the score is 5-6 15-15
    // */
    // assertEquals(5, matchState.getGamesA());
    // assertEquals(6, matchState.getGamesB());
    // assertEquals(0, matchState.getPointsA());
    // assertEquals(1, matchState.getPointsB());
    //
    // MarketPricesList marketPricesList2 = this.getTestMarketPricesForPfError2(eventId);
    // algoManager.handleParamFindRequest(marketPricesList2);
    //
    // assertEquals(ParamFindResultsStatus.RED, this.publishedParamFinderResults.getParamFindResultsStatus());
    // assertTrue(this.publishedParamFinderResults.getParamFindResultsDescription().getResultSummary()
    // .contains("Required in-play market(s) missing."));
    // }

    @SuppressWarnings("unused")
    private MarketPricesList getTestMarketPricesForPfError1() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.36);
        ab.put("B", 3.40);
        m.addMarketPrice(ab);
        MarketPrice ngw = new MarketPrice("G:ML", "Next game winner", MarketCategory.GENERAL, null, "G1");
        ngw.put("A", 1.84);
        ngw.put("B", 2.0);
        m.addMarketPrice(ngw);

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

    // private MarketPricesList getTestMarketPricesForPfError2(long eventId) {
    // MarketPrices m = new MarketPrices();
    // m.setSourceWeight(1);
    //
    // MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
    // ab.put("A", 1.36);
    // ab.put("B", 3.40);
    // m.addMarketPrice(ab);
    // MarketPrice ngw = new MarketPrice("G:ML", "Next game winner", MarketCategory.GENERAL, null, "G13");
    // ngw.put("A", 1.84);
    // ngw.put("B", 2.0);
    // m.addMarketPrice(ngw);
    //
    // MarketPricesList marketPricesList = new MarketPricesList();
    // marketPricesList.put("TestSource1", m);
    // marketPricesList.setEventId(eventId);
    // marketPricesList.setRequestId("PfErrorTest");
    // return marketPricesList;
    // }



    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", publishedMatchParams);
        this.publishedEventId = eventId;
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
        printResults("matchState", matchState);
        this.publishedEventId = eventId;
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
        printResults("markets", markets);
        this.publishedEventId = eventId;
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
        this.publishedEventId = eventId;
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
        printResults("paramFinderResults", paramFindResults);
        this.publishedEventId = eventId;
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
        this.publishedEventId = eventId;
    }

    Recording recording;

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * System.out.printf("Published %s for event: %s \n", resultsDescription); System.out.print(results.toString());
         * System.out.printf("--- Published %s ends ---\n\n", resultsDescription);
         */
    }

    private void setPublishedDataToNull() {
        publishedMatchParams = null;
        publishedMatchState = null;
        publishedMarkets = null;
        publishedResultedMarkets = null;
        publishedParamFinderResults = null;
        publishedNotifyEventCompleted = null;
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }



}
