package ats.algo.betstars.tennis;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.betstars.algo.ats.integration.BsTennisMatchParams;

import ats.algo.algomanager.EventStateBlob;

import ats.algo.algomanager.TriggerParamFindTradingRulesResult;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.springbridge.SpringContextBridge;

public class BetStarsTennisTradingRulesTestForMerge extends BsSimpleAlgoManagerTennisTestBase {



    /**
     * Test for event Tier 4
     * 
     */
    @Test
    public void testEventTierFourTradingRule() {
        MethodName.log();
        algoManager.onlyPublishMarketsFollowingParamChange(true);
        initSpring();
        long eventId = 1234567L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.autoSyncWithMatchFeed(false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 4);

        /*
         * should not publish any markets until param change or param find
         */
        assertTrue(publishedMarkets == null);
        /*
         * note that if the test fails here then it has not been run with the correct properties set
         */
        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;

        /*
         * Set back to matchParams used to setup all trading rule checks.
         */

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        assertFalse(publishedMarkets == null);

        /*
         * Trading rule that will exclude overs for event tier 4 to check exclusion.
         * 
         * Setup as event tier 4, run param find and overs should be rejected from the param find.
         */

        algoManager.handleSupplyMarketPrices(eventId, getPricesPrematch(false));
        int countForTotalsMarket = 0;

        /*
         * Add a count if the param find results contain FT:OU
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:OU")) {
            countForTotalsMarket++;
        }
        assertFalse(countForTotalsMarket > 0);

        /*
         * Setting up same test but with event Tier 3. Should allow FT:OU to be used.
         */

        revertBackToStartParams(matchParams);
        sleep(5);
        publishedParamFinderResults = null;
        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "3");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        algoManager.handleSupplyMarketPrices(eventId, getPricesPrematch(false));
        countForTotalsMarket = 0;

        /*
         * Add a count if the param find results contain FT:OU
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:OU")) {
            countForTotalsMarket++;
        }

        assertTrue(countForTotalsMarket > 0);

        /*
         * go in play with event Tier 4 and check totals trading rule We should reject OU regardless of event tier as we
         * are inplay.
         */
        MatchIncident matchIncident = new TennisMatchIncident(10, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        publishedMarkets = null;
        algoManager.handleMatchIncident(matchIncident, true);
        publishedMarkets = null;

        revertBackToStartParams(matchParams);
        sleep(5);
        properties = null;
        properties = new HashMap<String, String>(1);
        properties.put("eventTier", "4");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        // Allowing S1.1 G:ML x2 and 2x FT:ML
        algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, false, false, false, false));

        countForTotalsMarket = 0;

        /*
         * Add a count if the param find results contain FT:OU
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:OU")) {
            countForTotalsMarket++;
        }
        assertFalse(countForTotalsMarket > 0);

        /*
         * Setting up same test but with event Tier 3. Should allow FT:OU to be used.
         */

        revertBackToStartParams(matchParams);
        sleep(5);
        properties = null;
        properties = new HashMap<String, String>(1);
        properties.put("eventTier", "3");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        // Allowing S1.1 G:ML x2 and 2x FT:ML
        algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, false, false, false, false));

        countForTotalsMarket = 0;

        /*
         * Add a count if the param find results contain FT:OU
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:OU")) {
            countForTotalsMarket++;
        }
        /*
         * Should be 0 totals as we reject totals inplay.
         */

        assertFalse(countForTotalsMarket > 0);

        // System.out.println("Event Tier 4 trading rule inplay checked and complete");
        // System.out.println("All event tier OU trading rules checked");
        // System.out.println();

    }

    @Test
    public void testInPlayRejectionOfSpreadMarket() {
        MethodName.log();
        algoManager.onlyPublishMarketsFollowingParamChange(true);
        initSpring();
        long eventId = 1234567L;
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        /*
         * should not publish any markets until param change or param find
         */
        assertTrue(publishedMarkets == null);
        /*
         * note that if the test fails here then it has not been run with the correct properties set
         */

        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        assertFalse(publishedMarkets == null);

        algoManager.handleSupplyMarketPrices(eventId, getPricesPrematch(false));
        int countForSpreadMarket = 0;

        sleep(5);

        /*
         * Add a count if the param find results contain FT:SPRD
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:SPRD")) {
            countForSpreadMarket++;
        }
        assertTrue(countForSpreadMarket > 0);

        /*
         * Go in play and check FT:SPRD trading rule We should reject SPRD regardless of event tier as we are inplay.
         */
        MatchIncident matchIncident = new TennisMatchIncident(10, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident, true);

        // MatchIncident matchIncident1 = new TennisMatchIncident(10,
        // TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        // matchIncident1.setEventId(eventId);
        // algoManager.handleMatchIncident(matchIncident1, true);
        publishedMarkets = null;

        // Allowing S1.1 G:ML x2 and 2x FT:ML
        algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, false, false, false, false));

        countForSpreadMarket = 0;

        /*
         * Add a count if the param find results contain FT:SPRD
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("FT:SPRD")) {
            countForSpreadMarket++;
        }

        assertFalse(countForSpreadMarket > 0);

        // System.out.println("Spread trading rule checked and complete");
        // System.out.println();

    }

    /*
     * This test will encapsulate various cases we have seen within game winner issues raised.
     * 
     * 1. Test Game winner rejected prematch 2. Test Game winner of S1.2 is rejected at firstPoint 3. Test Game winner
     * of S1.1 is accepted at firstPoint 4. Test rejection of current game prices 5. Test rejection of prices that are
     * Game +2 ahead 6. Test acceptance of only next game when passed in current, next and game +2.
     */

    @Test
    public void testGameWinnerTradingRules() {
        MethodName.log();
        algoManager.onlyPublishMarketsFollowingParamChange(true);

        initSpring();
        long eventId = 1234567L;

        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        traderAlert = null;

        /*
         * should not publish any markets until param change or param find
         */
        assertTrue(publishedMarkets == null);
        /*
         * note that if the test fails here then it has not been run with the correct properties set
         */

        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        assertFalse(publishedMarkets == null);

        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getPricesPrematch(true));

        /*
         * Test 1 - rejection of game winner prices prematch
         */

        int countForGameMarket = 0;

        /*
         * Add a count if the param find results contain G:ML
         */
        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("G:ML")) {
            countForGameMarket++;
        }
        assertFalse(countForGameMarket > 0);

        // System.out.println("Test 1 Passed");

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        countForGameMarket = 0;

        // Going in play

        MatchIncident matchIncident = new TennisMatchIncident(10, TennisMatchIncidentType.MATCH_STARTING, TeamId.B, 1);
        matchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident, true);

        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY,
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatus());
        assertEquals("Suspend without INPF",
                        publishedMarkets.getMarkets().get("G:ML_S1.2").getMarketStatus().getSuspensionStatusReason());

        publishedMarkets = null;
        // assertTrue(traderAlert == null); //TODO check why this breaks every
        // now and then
        sleep(5);

        /*
         * Test 2 - Rejection of G:ML S1.2 prices for param find at S1.1.1 Will check for BLUE param find result as it
         * will reject the param find. Will also check effect of when both 2x Current sand 2x next prices are fed in. To
         * begin - some combinations of G:ML S1.2 and FT:ML.
         */

        // Allowing S1.2 G:ML x1 and 1x FT:ML
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(false, false, false, true, false, false, false));

        assertFalse(result.isParamFindScheduled());
        result = null;
        // System.out.println();

        // Allowing S1.2 G:ML x2 and 1x FT:ML
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(false, false, false, true, true, false, false));
        assertFalse(result.isParamFindScheduled());
        result = null;
        // System.out.println();

        // Allowing S1.2 G:ML x1 and 2x FT:ML
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, false, false, true, false, false, false));

        assertFalse(result.isParamFindScheduled());
        result = null;
        // System.out.println();

        // Allowing S1.2 G:ML x2 and 2x FT:ML
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, false, false, true, true, false, false));

        assertFalse(result.isParamFindScheduled());
        result = null;
        // System.out.println();

        // Allowing S1.2 G:ML x2, S1.1 G:ML x2 and 1x FT:ML
        // Trader alert should get generated
        // result =
        // algoManager.handleSupplyMarketPrices(getGameOnePriceAtStartOfMatch(eventId,
        // false, true, true, true, true, false, false));
        // assertFalse (result.isParamFindScheduled());

        // System.out.println();
        // System.out.println("Test 2 passed");

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        countForGameMarket = 0;
        traderAlert = null;
        publishedMatchParams = null;
        publishedParamFinderResults = null;
        sleep(2);

        /*
         * Test 3 - acceptance of S1.1 G:ML prices
         * 
         * Allowing S1.2 G:ML x2, S1.1 G:ML x2 and 2x FT:ML This should pass the param and reject S1.2 prices whilst
         * only allowing S1.1 prices Should generate first inplay PF alert
         */

        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, true, true, false, false));
        assertTrue(result.isParamFindScheduled());

        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("S1.2")) {
            countForGameMarket++;
        }
        assertTrue(countForGameMarket == 0);

        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("S1.1")) {
            countForGameMarket++;
        }
        assertFalse(countForGameMarket == 0);
        assertTrue(publishedParamFinderResults.getParamFindResultsStatus() == ParamFindResultsStatus.GREEN);

        assertTrue(traderAlert.getTraderAlertType() == TraderAlertType.FIRST_INPLAY_PF);

        // System.out.println("Test 3 passed");

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        countForGameMarket = 0;
        traderAlert = null;
        publishedMatchParams = null;
        publishedParamFinderResults = null;
        result = null;
        sleep(5);

        /*
         * Test 4 - Rejection of current game prices. Insert incident to make match state = 0-15. Pass in S1.1 G:ML x2
         * and FT:ML x2 prices.
         */

        MatchIncident matchIncident2 = new TennisMatchIncident(12, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        matchIncident2.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident2, true);

        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, false, false, false, false));
        assertFalse(result.isParamFindScheduled());
        result = null;

        // System.out.println("Test 4 passed");

        sleep(5);
        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        /*
         * Test 5 - rejection of Game + 2 winner prices at 0-30
         */

        MatchIncident matchIncident3 = new TennisMatchIncident(12, TennisMatchIncidentType.POINT_WON, TeamId.B, 1);
        matchIncident3.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident3, true);

        // Allow 2x FT:ML, 2x G:ML S1.3
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, false, false, false, false, true, true));
        assertFalse(result.isParamFindScheduled());
        result = null;

        // System.out.println("Test 5 passed");

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        countForGameMarket = 0;
        sleep(5);

        /*
         * Test 6 - Acceptance of only 2x G:ML S1.2 prices when sent in S1.1, S1.2, S1.3 at same time.
         */

        MatchIncident matchIncident4 = new TennisMatchIncident(12, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchIncident4.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident4, true);

        // Allow 2x FT:ML, 2x G:ML S1.1, 2x G:ML S1.2, 2x G:ML S1.3
        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, true, true, true, true));
        assertTrue(result.isParamFindScheduled());

        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("S1.1")) {
            countForGameMarket++;
        }
        assertTrue(countForGameMarket == 0);

        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("S1.2")) {
            countForGameMarket++;
        }
        assertFalse(countForGameMarket == 2);
        countForGameMarket = 0;

        if (publishedParamFinderResults.getParamFindResultsDescription().getResultDetail().toString()
                        .contains("S1.3")) {
            countForGameMarket++;
        }
        assertTrue(countForGameMarket == 0);

        assertTrue(publishedParamFinderResults.getParamFindResultsStatus() == ParamFindResultsStatus.GREEN);

        // System.out.println("Test 6 passed");

        revertBackToStartParams(matchParams);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        countForGameMarket = 0;
        result = null;
        sleep(5);

        /*
         * Test 7 - Rejection when it has only 1x G:ML S1.2 prices when sent in S1.1, S1.2, S1.3 at same time.
         */

        MatchIncident matchIncident5 = new TennisMatchIncident(12, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        matchIncident5.setEventId(eventId);
        algoManager.handleMatchIncident(matchIncident5, true);

        result = algoManager.handleSupplyMarketPrices(eventId,
                        getGameOnePriceAtStartOfMatch(true, true, true, true, false, true, true));
        assertFalse(result.isParamFindScheduled());

        // System.out.println("Test 7 passed");
        // System.out.println("All tests passed");

    }

    private static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
    }

    private void revertBackToStartParams(BsTennisMatchParams matchParams) {
        matchParams.getOnServePctA1().setMean(0.6);
        matchParams.getOnServePctB1().setMean(0.65);

    }

    /*
     * Prematch prices for testing
     */

    private MarketPricesList getPricesPrematch(boolean includeGame) {

        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice matchWinner = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);

        matchWinner.put("A", 1.86);
        matchWinner.put("B", 1.88);
        m.addMarketPrice(matchWinner);

        MarketPrice totalGames = new MarketPrice("FT:OU", "Total Games", MarketCategory.OVUN, "23.5");

        totalGames.put("Over", 1.86);
        totalGames.put("Under", 1.88);
        m.addMarketPrice(totalGames);

        MarketPrice handicap = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "-0.5");

        handicap.put("AH", 1.86);
        handicap.put("BH", 1.88);
        m.addMarketPrice(handicap);

        if (includeGame) {
            MarketPrice gameOneWinner = new MarketPrice("G:ML", "Game Winner", MarketCategory.GENERAL, "", "S1.1");

            gameOneWinner.put("A", 1.86);
            gameOneWinner.put("B", 1.88);
            m.addMarketPrice(gameOneWinner);
        }

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("MarathonBet", m);

        return marketPricesList;
    }

    /*
     * Inplay prices used for testing.
     */

    private MarketPricesList getGameOnePriceAtStartOfMatch(boolean twoMatchWinners, boolean includeGameOne,
                    boolean twoGameOneWinners, boolean includeGameTwo, boolean twoGameTwoWinners,
                    boolean includeGameThree, boolean twoGameThreeWinners) {
        MarketPrices m = new MarketPrices();
        MarketPrices m2 = new MarketPrices();
        m.setSourceWeight(1);
        m2.setSourceWeight(1);

        MarketPrice matchWinner1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);

        matchWinner1.put("A", 1.86);
        matchWinner1.put("B", 1.88);
        m.addMarketPrice(matchWinner1);

        if (twoMatchWinners) {

            MarketPrice matchWinner2 = new MarketPrice("FT:ML", "Match winner 2", MarketCategory.GENERAL, null);

            matchWinner2.put("A", 1.87);
            matchWinner2.put("B", 1.87);
            m2.addMarketPrice(matchWinner2);
        }

        MarketPrice totalGames = new MarketPrice("FT:OU", "Total Games", MarketCategory.OVUN, "23.5");

        totalGames.put("Over", 1.87);
        totalGames.put("Under", 1.87);
        m.addMarketPrice(totalGames);

        MarketPrice handicap = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "-0.5");

        handicap.put("AH", 1.87);
        handicap.put("BH", 1.87);
        m.addMarketPrice(handicap);

        if (includeGameOne) {
            MarketPrice gameOneWinner1 = new MarketPrice("G:ML", "GameOneWinner", MarketCategory.GENERAL, "", "S1.1");
            gameOneWinner1.put("A", 3.0);
            gameOneWinner1.put("B", 1.3);
            m.addMarketPrice(gameOneWinner1);
        }

        if (twoGameOneWinners) {
            MarketPrice gameOneWinner2 =
                            new MarketPrice("G:ML", "GameOneWinnerNumber2", MarketCategory.GENERAL, "", "S1.1");
            gameOneWinner2.put("A", 3.01);
            gameOneWinner2.put("B", 1.31);
            m2.addMarketPrice(gameOneWinner2);
        }

        if (includeGameTwo) {
            MarketPrice gameTwoWinner1 = new MarketPrice("G:ML", "GameTwoWinner", MarketCategory.GENERAL, "", "S1.2");
            gameTwoWinner1.put("A", 1.3);
            gameTwoWinner1.put("B", 3.0);
            m.addMarketPrice(gameTwoWinner1);
        }

        if (twoGameTwoWinners) {
            MarketPrice gameTwoWinner2 =
                            new MarketPrice("G:ML", "GameOneWinnerNumber2", MarketCategory.GENERAL, "", "S1.2");
            gameTwoWinner2.put("A", 1.31);
            gameTwoWinner2.put("B", 3.01);
            m2.addMarketPrice(gameTwoWinner2);
        }

        if (includeGameThree) {
            MarketPrice gameThreeWinner1 =
                            new MarketPrice("G:ML", "GameThreeWinner", MarketCategory.GENERAL, "", "S1.3");
            gameThreeWinner1.put("A", 3.0);
            gameThreeWinner1.put("B", 1.3);
            m.addMarketPrice(gameThreeWinner1);
        }

        if (twoGameThreeWinners) {
            MarketPrice gameThreeWinner2 =
                            new MarketPrice("G:ML", "GameThreeWinnerNumber2", MarketCategory.GENERAL, "", "S1.3");
            gameThreeWinner2.put("A", 3.01);
            gameThreeWinner2.put("B", 1.31);
            m2.addMarketPrice(gameThreeWinner2);
        }

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("MarathonBet", m);

        marketPricesList.put("Unibet", m2); // Might need this for trading
                                            // rules.
        // System.out.println(m);
        // System.out.println(m2);

        return marketPricesList;
    }

    private static void initSpring() {
        String componentScanPackage = System.getProperty("algomgr.springBridgeComponentScanPackage");
        // System.out.println("algomgr.springBridgeComponentScanPackage = " + componentScanPackage);
        if (componentScanPackage != null) {
            // System.out.println("initialising spring context...");
            String[] packages = componentScanPackage.split(",");
            AnnotationConfigApplicationContext springApplicationContext =
                            new AnnotationConfigApplicationContext(packages);
            SpringContextBridge.newApplicationContext(springApplicationContext);
        }
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", matchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState simpleMatchState) {
        this.publishedMatchState = simpleMatchState;
        printResults("SimpleMatchState", simpleMatchState);
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;
        printResults("markets", markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFindResults;
        // System.out.println(paramFindResults);
        // System.out.println(this.publishedParamFinderResults);
        // System.out.println();
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * // System.out.printf("Published %s for event: %s \n", resultsDescription); //
         * System.out.print(results.toString()); // System.out.printf("--- Published %s ends ---\n\n",
         * resultsDescription);
         */
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {
        this.traderAlert = traderAlert;
        printResults("traderAlertResults", traderAlert);
    }

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {}

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }

}
