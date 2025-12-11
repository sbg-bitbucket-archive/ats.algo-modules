package ats.algo.algomanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethod;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.core.tradingrules.SuspendToAwaitParamFindTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.testsport.TestSportMatchEngine;
import ats.algo.sport.testsport.TestSportMatchFormat;
import ats.algo.sport.testsport.TestSportMatchIncident;
import ats.algo.sport.testsport.TestSportMatchState;
import ats.algo.sport.testsport.TestSportSimpleMatchState;

public class TestEventLevelSuspension extends AlgoManagerSimpleTestBase {

    private static final long eventId = 789L;
    private static final int TIME_SECS_BETWEEN_PARAM_FINDS = 7;

    @Test
    public void testSuspendAwaitingParamFindRule() {
        /*
         * earlier unit tests may have set the static flags in TestSportMatchEngine to non default values, so reset to
         * known values before starting test
         */
        TestSportMatchEngine.resetTestFlags();
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(new TestSuspendToAwaitParamFindTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.handleMatchIncident(TestSportMatchIncident.generateInvalidIncident(eventId), true);
        assertTrue(this.publishedEventLevelShouldSuspend);
        /*
         * schedule a standard param find and verify it clears the flag
         */
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(5.22, 1.16));
        assertTrue(result.isParamFindScheduled());
        assertTrue(publishedParamFinderResults.isSuccess());
        assertEquals(0.35, publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().getMean(), 0.07);
        assertFalse(publishedEventLevelShouldSuspend);
        /*
         * issue another invalid incident and request a param find. should get scheduled immediately even though prices
         * haven't changed
         */
        algoManager.handleMatchIncident(TestSportMatchIncident.generateInvalidIncident(eventId), true);
        assertTrue(this.publishedEventLevelShouldSuspend);
        Sleep.sleepMs((int) TriggerParamFindTradingRule.MIN_TIME_BETWEEN_PARAM_FINDS + 2000);
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(5.22, 1.15));
        assertTrue(result.isParamFindScheduled());
        assertTrue(publishedParamFinderResults.isSuccess());
        assertEquals(0.35, publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().getMean(), 0.07);
        assertFalse(publishedEventLevelShouldSuspend);
    }

    @Test
    public void testSuspendAwaitingParamFindRule2() {
        /*
         * earlier unit tests may have set the static flags in TestSportMatchEngine to non default values, so reset to
         * known values before starting test
         */
        TestSportMatchEngine.resetTestFlags();
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(new TestSuspendToAwaitParamFindTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.handleMatchIncident(TestSportMatchIncident.generateInvalidIncident(eventId), true);
        assertTrue(this.publishedEventLevelShouldSuspend);
        /*
         * throw in a standard incident. Verify stays suspended
         */
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        assertTrue(this.publishedEventLevelShouldSuspend);
        /*
         * change a param verify that it unsuspends
         */
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertFalse(this.publishedEventLevelShouldSuspend);
    }


    @Test
    public void testFailedParamFindSuspension() {
        /*
         * earlier unit tests may have set the static flags in TestSportMatchEngine to non default values, so reset to
         * known values before starting test
         */
        TestSportMatchEngine.resetTestFlags();
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(generateTestTriggerParamFindTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        /*
         * schedule a standard param find
         */
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(5.22, 1.16));
        assertTrue(result.isParamFindScheduled());
        assertTrue(publishedParamFinderResults.isSuccess());
        assertFalse(publishedParamFinderResults.isShouldSuspendMarkets());
        assertEquals(0.35, publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().getMean(), 0.07);
        assertTrue(publishedEventLevelShouldSuspend == null);
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        /*
         * set the flag to make the pf fail then schedule another pf. check that event level suspension happens ok
         */

        TestSportMatchEngine.disableMatchWinnerCalc = true;
        publishedParamFinderResults = null;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(1.060, 11.072));
        int timeToWait = (int) (TIME_SECS_BETWEEN_PARAM_FINDS + 2);
        Sleep.sleep(timeToWait);
        assertTrue(result.isParamFindScheduled());
        assertFalse(publishedParamFinderResults.isSuccess());
        assertTrue(publishedParamFinderResults.isShouldSuspendMarkets());
        assertTrue(publishedEventLevelShouldSuspend);
        /*
         * handle a match incident. Verify that event level suspension remains in force
         */
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        assertTrue(publishedEventLevelShouldSuspend);
        /*
         * do a successful pf. Verify that eventlevelsuspension is cleared
         */
        Sleep.sleep(TIME_SECS_BETWEEN_PARAM_FINDS + 2);
        TestSportMatchEngine.disableMatchWinnerCalc = false;
        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(5.22, 1.16));
        assertTrue(result.isParamFindScheduled());
        assertFalse(publishedEventLevelShouldSuspend);
        /*
         * do another failed pf
         */

        TestSportMatchEngine.disableMatchWinnerCalc = true;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(1.16, 5.22));
        Sleep.sleep(TIME_SECS_BETWEEN_PARAM_FINDS + 2);
        assertTrue(publishedEventLevelShouldSuspend);
        /*
         * change match params and verify flag cleared
         */
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.55);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        assertFalse(publishedEventLevelShouldSuspend);
    }

    private MarketPricesList getTestMarketPrices(double p1, double p2) {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices bet365 = new MarketPrices();
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", p1);
        b1.put("B", p2);
        bet365.addMarketPrice(b1);
        // MarketPrice b2 = new MarketPrice("FT:OU", "Total",
        // MarketCategory.OVUN, "3.5");
        // b2.put("Over", 1.50);
        // b2.put("Under", 2.47);
        // bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    @Test
    public void testMonitorFeedSuspensionRules() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(generateTestMonitorFeedTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        TestSportMatchEngine.resetTestFlags();
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.6);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        Sleep.sleep(6);
        assertEquals(null, this.publishedEventLevelShouldSuspend);
        /*
         * put the match in play. Should suspend after 4 secs with no matchIncident
         */

        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        Sleep.sleep(6);
        assertEquals(true, this.publishedEventLevelShouldSuspend);
        /*
         * next incident should cancel the suspension
         */
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);

        Sleep.sleep(2);
        assertEquals(false, this.publishedEventLevelShouldSuspend);
        assertEquals(4, ((TestSportSimpleMatchState) publishedMatchState).getScoreA());
        /*
         * Since score is 4-0 suspensionMethod should ensure that event does not get suspended
         */
        Sleep.sleep(6);
        assertEquals(false, this.publishedEventLevelShouldSuspend);

        publishedEventLevelShouldSuspend = null;
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);

        /*
         * score now 5-0 so suspendMethod should suspend next time the trading rule is run (on the clock timer)
         */
        Sleep.sleep(2);
        assertEquals(true, this.publishedEventLevelShouldSuspend);
    }

    private MonitorFeedTradingRule generateTestMonitorFeedTradingRule() {
        MonitorFeedTradingRuleSuspensionMethod suspensionMethod = (now, matchState) -> {
            int scoreA = ((TestSportMatchState) matchState).getScoreA();
            if (scoreA == 4)
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.DO_NOT_SUSPEND,
                                "test rule. do not suspend");
            else if (scoreA == 5)
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY,
                                "test rule. suspend immediately");
            else
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.APPLY_STANDARD_RULES,
                                "test rule.  apply normal rules");
        };
        return new MonitorFeedTradingRule(4, 0, suspensionMethod);
    }

    private TriggerParamFindTradingRule generateTestTriggerParamFindTradingRule() {
        return TriggerParamFindTradingRule.generateTimerVariant("test rule", TIME_SECS_BETWEEN_PARAM_FINDS, null);
    }

    private class TestSuspendToAwaitParamFindTradingRule extends SuspendToAwaitParamFindTradingRule {

        private static final long serialVersionUID = 1L;

        public TestSuspendToAwaitParamFindTradingRule() {
            super("TestRule", null);
        }

        @Override
        public boolean shouldSuspendToAwaitParamFind(MatchIncident matchIncident, MatchState matchState) {
            TestSportMatchIncident testMatchIncident = (TestSportMatchIncident) matchIncident;
            if (testMatchIncident.getIncidentSubType().equals("INVALID"))
                return true;
            return false;
        }

    }



    @Test
    /**
     * test failed pf followed by failed incident
     */
    public void testCombinedSuspensionLogic1() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(generateTestMonitorFeedTradingRule());
        tradingRules.addRule(generateTestTriggerParamFindTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        TestSportMatchEngine.resetTestFlags();
        /*
         * create match and put in play
         */
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.5);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        assertEquals(null, publishedEventLevelShouldSuspend);
        /*
         * do a failed param find
         */
        TestSportMatchEngine.disableMatchWinnerCalc = true;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(1.15, 5.37));
        assertTrue(publishedParamFinderResults.isShouldSuspendMarkets());
        assertTrue(publishedEventLevelShouldSuspend);
        /*
         * wait 10 secs. Param find should get scheduled
         */
        TestSportMatchEngine.disableMatchWinnerCalc = false;
        publishedEventLevelShouldSuspend = null;
        publishedParamFinderResults = null;
        Sleep.sleep(TIME_SECS_BETWEEN_PARAM_FINDS + 2);
        assertFalse(publishedParamFinderResults.isShouldSuspendMarkets());
        /*
         * event level suspension should still be in force because more than 4 secs since last MatchIncident
         */
        assertEquals(null, publishedEventLevelShouldSuspend);
        /*
         * fire in an incident. Should clear eventLevel suspension
         */
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        Sleep.sleep(2);
        assertEquals(false, publishedEventLevelShouldSuspend);
    }

    @Test
    /**
     * test failed incident followed by pf
     */
    public void testCombinedSuspensionLogic2() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(generateTestMonitorFeedTradingRule());
        tradingRules.addRule(generateTestTriggerParamFindTradingRule());
        algoManager.setTradingRules(SupportedSportType.TEST_SPORT, tradingRules);
        TestSportMatchEngine.resetTestFlags();
        /*
         * create match and put in play
         */
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, eventId, new TestSportMatchFormat());
        publishedMatchParams.getParamMap().get("pctAWinsGame").getGaussian().setMean(0.5);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        assertEquals(null, publishedEventLevelShouldSuspend);
        Sleep.sleep(6);
        assertEquals(true, this.publishedEventLevelShouldSuspend);
        /*
         * do a failed param find
         */
        TestSportMatchEngine.disableMatchWinnerCalc = true;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices(1.15, 5.37));
        assertTrue(publishedParamFinderResults.isShouldSuspendMarkets());
        assertTrue(publishedEventLevelShouldSuspend);

        // /*
        // * fire in an incident to clear the incident related suspension
        // */
        // algoManager.handleMatchIncident(TestSportMatchIncident.generateIncident(eventId, TeamId.A), true);
        // Sleep.sleep(2);
        // assertEquals(true, publishedEventLevelShouldSuspend);
        /*
         * wait 10 secs. Param find should get scheduled
         */
        TestSportMatchEngine.disableMatchWinnerCalc = false;
        publishedEventLevelShouldSuspend = null;
        publishedParamFinderResults = null;
        Sleep.sleep(TIME_SECS_BETWEEN_PARAM_FINDS + 2);
        assertFalse(publishedParamFinderResults.isShouldSuspendMarkets());


    }


}
