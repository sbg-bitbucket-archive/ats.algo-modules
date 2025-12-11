package ats.algo.derivedmarkets;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.AlgoManagerTest;
import ats.algo.algomanager.CollectMarkets;
import ats.algo.algomanager.PublishMarketsManager;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.MarketsMetaData;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.lc.tradingrules.LcAflTradingRules;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchParams;

public class DerivedMarketsTest extends AlgoManagerSimpleTestBase {

    public DerivedMarketsTest() {
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void testAflDerivedMarkets() {
        MethodName.log();
        AflMatchFormat matchformat = new AflMatchFormat();
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        long eventId = 2345L;

        // algoManager.setTradingRules(SupportedSportType.AUSSIE_RULES,
        // derivedMarketTradingRules());
        algoManager.handleNewEventCreation(SupportedSportType.AUSSIE_RULES, eventId, matchformat, 2);
        Gaussian g = publishedMatchParams.getParamMap().get("totalScoreRate").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.setTradingRules(SupportedSportType.AUSSIE_RULES, new LcAflTradingRules());
        for (Entry<String, Market> e : publishedMarkets.getMarkets().entrySet()) {
            String key = e.getKey();
            if (key.contains("FT:SPRD")) {
                // System.out.println(key + ": " + e.getValue());
            }
        }

    }

    @Test
    public void testAmericanFootballDerivedMarkets() {
        MethodName.log();
        MatchFormat matchFormat = new AmericanfootballMatchFormat();
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        long eventId = 2345L;
        algoManager.handleNewEventCreation(SupportedSportType.AMERICAN_FOOTBALL, eventId, matchFormat, 1);
        Gaussian g = publishedMatchParams.getParamMap().get("scoreTotal").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        int mktCount = 0;
        for (Entry<String, Market> e : publishedMarkets.getMarkets().entrySet()) {
            @SuppressWarnings("unused")
            String key = e.getKey();
            Market market = e.getValue();
            if (market.getType().equals("FT:OU") || market.getType().equals("FT:SPRD")) {
                // System.out.println(key + ": " + e.getValue());
                mktCount++;
            }
        }
        assertEquals(14, mktCount);

    }

    @Test
    public void testMarketMetaData() {
        MethodName.log();
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.setTradingRules(SupportedSportType.TENNIS, derivedMarketTradingRules());
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        /*
         * change one of the params so not exactly 50-50
         */
        ((TennisMatchParams) publishedMatchParams).getOnServePctA1().setMean(0.63);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());

        MarketsMetaData marketsMetaData = publishedMarkets.getMarketsMetaData();
        // System.out.println(marketsMetaData);
        Set<String> marketTypes = marketsMetaData.getAllLineMarketTypes();
        assertTrue(marketTypes.contains("FT:OU"));
        assertFalse(marketTypes.contains("FT:ML"));
        Set<String> ouKeys = marketsMetaData.getAllKeysForMarketType("FT:OU");
        // System.out.println(ouKeys);
        assertTrue(ouKeys.contains("FT:OU#22.0_M"));
        String b = marketsMetaData.getBalancedLineForType("FT:SPRD");
        // System.out.println(" BalancedLine for FT:SPRD: " + b);
        assertEquals("1.50", b);
    }

    @Test
    public void testResultedDerivedMarketsForCompleteMatch() {
        MethodName.log();
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 987654321L;
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.publishResultedMarketsImmediately(true);
        CollectMarkets collectMarkets = new CollectMarkets();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        // // System.out.println(publishedResultedMarkets);
        collectMarkets.addToSet(publishedMarkets);
        algoManager.setTradingRules(SupportedSportType.TENNIS, derivedMarketTradingRules());
        TennisMatchIncident tennisMatchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        tennisMatchIncident.setEventId(eventId);
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        // // System.out.println("\nAFTER FIRST POINT IN MATCH:\n");
        // // System.out.println(publishedMarkets);
        Markets oldMarkets = publishedMarkets;
        tennisMatchIncident.setTennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        TennisMatchIncident tennisMatchIncidentA =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A);
        TennisMatchIncident tennisMatchIncidentB =
                        new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.B);
        tennisMatchIncidentA.setEventId(eventId);
        tennisMatchIncidentB.setEventId(eventId);
        // // System.out.println("Calculating...");
        /*
         * let B win first 5 games
         */
        for (int i = 0; i < 20; i++) {
            // System.out.println("ITERATION1:" + i);
            algoManager.handleMatchIncident(tennisMatchIncidentB, true);
            collectMarkets.addToSet(publishedMarkets);
            AlgoManagerTest.checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
            oldMarkets = publishedMarkets;
        }
        /*
         * then A win rest of the match
         */
        for (int i = 0; i < 51; i++) {
            // System.out.println("ITERATION2:" + i);
            algoManager.handleMatchIncident(tennisMatchIncidentA, true);
            collectMarkets.addToSet(publishedMarkets);
            AlgoManagerTest.checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
            oldMarkets = publishedMarkets;
        }

        // System.out.println("Final point in match...");
        assertEquals(null, publishedNotifyEventCompleted);
        MatchState penultimateMatchState = publishedMatchState.copy();
        int nResultedMarketsBeforeCompletion = publishedResultedMarkets.size();
        // System.out.println("\n ** BEFORE FINAL POINT IN MATCH: **");
        // System.out.println(publishedMarkets);
        // System.out.println(publishedResultedMarkets);

        assertTrue(publishedResultedMarkets.getResultedMarkets().get("FT:SPRD#-13.5_M") == null);
        assertTrue(publishedResultedMarkets.getResultedMarkets().get("FT:ML_M") == null);
        /*
         * next incident will be final point of the match
         */
        publishedMarkets = null;
        algoManager.handleMatchIncident(tennisMatchIncident, true);
        AlgoManagerTest.checkConsistent(oldMarkets, publishedMarkets, keysForDiscontinuedMarkets);
        collectMarkets.addToSet(publishedMarkets);
        assertTrue(publishedNotifyEventCompleted);
        assertTrue(publishedResultedMarkets.size() > nResultedMarketsBeforeCompletion);
        // System.out.println("\nAFTER FINAL POINT IN MATCH:\n");
        // System.out.println(publishedMarkets);
        // System.out.println(publishedResultedMarkets);
        /*
         * verify there is a resulted market for every market that was ever published
         */
        for (String fullKey : collectMarkets.getAllMarketKeys()) {
            ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get(fullKey);
            //
            // String winningSelections;
            // if (resultedMarket == null)
            // winningSelections = "null";
            // else
            // winningSelections = resultedMarket.getWinningSelections().toString();
            // System.out.println(fullKey + ": " + winningSelections);

            assertTrue(resultedMarket != null);
        }
        Map<String, ResultedMarket> resultedMarkets = publishedResultedMarkets.getResultedMarkets();
        assertTrue(resultedMarkets.get("FT:ML_M") != null);

        assertEquals("AH", resultedMarkets.get("FT:SPRD#7.5_M").getWinningSelections().get(0));
        // assertEquals("BH",
        // resultedMarkets.get("FT:SPRD#8.5_M").getWinningSelections().get(0));
        assertEquals("VOID", resultedMarkets.get("FT:SPRD#8.0_M").getWinningSelections().get(0));
        assertFalse(resultedMarkets.get("FT:SPRD#8.5_M").isMarketVoided());
        assertTrue(resultedMarkets.get("FT:SPRD#8.0_M").isMarketVoided());

        assertEquals("Over", resultedMarkets.get("FT:OU#17.5_M").getWinningSelections().get(0));
        assertEquals("Under", resultedMarkets.get("FT:OU#18.5_M").getWinningSelections().get(0));
        assertEquals("VOID", resultedMarkets.get("FT:OU#18.0_M").getWinningSelections().get(0));
        assertFalse(resultedMarkets.get("FT:OU#17.5_M").isMarketVoided());
        assertTrue(resultedMarkets.get("FT:OU#18.0_M").isMarketVoided());

        /*
         * undo the match completion incident
         */
        algoManager.handleUndoLastMatchIncident(eventId);
        assertFalse(publishedNotifyEventCompleted);
        assertEquals(nResultedMarketsBeforeCompletion, publishedResultedMarkets.size());
        assertEquals(penultimateMatchState, publishedMatchState);
        assertTrue(publishedResultedMarkets.getResultedMarkets().get("FT:SPRD#-13.5_M") == null);
        assertTrue(publishedResultedMarkets.getResultedMarkets().get("FT:ML_M") == null);
    }

    private AbstractTradingRule[] derivedMarketTradingRules() {
        AbstractTradingRule[] tradingRules = new AbstractTradingRule[6];
        /*
         * add derived market dynamicRange hcap
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR1",
                        "Example winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 2, false);
        DerivedMarketTradingRule derivedMarketTradingRule =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec);
        tradingRules[0] = derivedMarketTradingRule;

        /*
         * add derived market dynamicRange total
         */
        DerivedMarketSpec derivedMarketSpec2 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal("FT:OU_DR2",
                        "Example total points dynamic range", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4,
                        3, 15);
        DerivedMarketTradingRule derivedMarketTradingRule2 =
                        new DerivedMarketTradingRule(null, "FT:OU", derivedMarketSpec2);
        tradingRules[1] = derivedMarketTradingRule2;
        /*
         * add extra lines each side of balanced line for FT:SPRD
         */
        DerivedMarketSpec derivedMarketSpec3 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);
        DerivedMarketTradingRule derivedMarketTradingRule3 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec3);
        tradingRules[2] = derivedMarketTradingRule3;
        /*
         * add a static line example
         */
        DerivedMarketSpec derivedMarketSpec4 = DerivedMarketSpec.getDerivedMarketSpecForStaticLines("FT:SPRD_DR3",
                        "Static line example market", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 2);
        DerivedMarketTradingRule derivedMarketTradingRule4 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec4);
        tradingRules[3] = derivedMarketTradingRule4;
        /*
         * add an example of lines near 50-50
         */
        DerivedMarketSpec derivedMarketSpec5 = DerivedMarketSpec.getDerivedMarketSpecForLinesNearestEvens("FT:SPRD_DR4",
                        "Nearest evens example market", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4);
        DerivedMarketTradingRule derivedMarketTradingRule5 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec5);
        tradingRules[4] = derivedMarketTradingRule5;
        /*
         * add extra lines each side of balanced line for FT:OU
         */
        DerivedMarketSpec derivedMarketSpec6 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);
        DerivedMarketTradingRule derivedMarketTradingRule6 =
                        new DerivedMarketTradingRule(null, "FT:OU", derivedMarketSpec6);
        tradingRules[5] = derivedMarketTradingRule6;
        return tradingRules;
    }

}
