package ats.algo.tradingruletests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.tradingrules.TradingRulesManager;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.genericsupportfunctions.MethodName;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.generic.tradingrules.ElapsedTimeTradingRule;

public class TradingRulesManagerTest {

    @Test
    public void test() {
        MethodName.log();
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchWinnerMarket());
        markets.addMarketWithShortKey(generateTotalGamesMarket());
        /*
         * Market FT:ML has MarketGroup: NOT_SPECIFIED, marketTier = 2
         * 
         * Market FT:OU has MarketGroup: GOALS, marketTier = 1;
         */
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[2];
        TriggerParamFindData triggerParamFindData = new TriggerParamFindData();
        tradingRules[0] = getRule0();
        tradingRules[1] = getRule1();
        TradingRulesManager tradingRulesManager = new TradingRulesManager();
        FootballMatchState matchState = new FootballMatchState(new FootballMatchFormat());
        tradingRulesManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);
        /*
         * check state before any rules applied
         */
        MarketStatus status = markets.get("FT:ML").getMarketStatus();
        // // System.out.print(status.toString());
        assertEquals(SuspensionStatus.OPEN, status.getSuspensionStatus());
        assertTrue(status.getSuspensionStatusRuleName().equals("Default"));
        assertTrue(status.getSuspensionStatusReason().equals("Default"));
        /*
         * apply the rules. Rule 0 should apply
         */
        tradingRulesManager.applyUpdateMarketsPostPriceCalcRules(1, SupportedSportType.SOCCER, matchState, markets,
                        CalcRequestCause.EVENT_TIER_CHANGE, triggerParamFindData);
        status = markets.get("FT:ML").getMarketStatus();
        // System.out.print(status.toString());
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, status.getSuspensionStatus());
        assertTrue(status.getSuspensionStatusRuleName().equals("Test rule 0"));
        assertTrue(status.getSuspensionStatusReason().equals("Default state pre-match"));
        /*
         * change rule 1 so that it SHOULD also apply to FT:ML
         */
        tradingRules[1].setEventTier(1);
        tradingRulesManager.applyUpdateMarketsPostPriceCalcRules(1, SupportedSportType.SOCCER, matchState, markets,
                        CalcRequestCause.EVENT_TIER_CHANGE, triggerParamFindData);
        status = markets.get("FT:ML").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, status.getSuspensionStatus());
        assertTrue(status.getSuspensionStatusRuleName().equals("Test rule 1"));
        assertTrue(status.getSuspensionStatusReason().equals("Default state pre-match"));

    }

    private ElapsedTimeTradingRule getRule0() {
        ElapsedTimeTradingRule rule = new ElapsedTimeTradingRule();
        rule.setRuleType(TradingRuleType.SET_MARKET_SUSPENSION_STATUS);
        rule.setRuleName("Test rule 0");
        rule.setDefaultStatusPreMatch(SuspensionStatus.SUSPENDED_DISPLAY);
        rule.setDefaultStatusInPlay(SuspensionStatus.OPEN);
        rule.setStatusInAmberZone(SuspensionStatus.SUSPENDED_DISPLAY);
        rule.setStatusInRedZone(SuspensionStatus.SUSPENDED_UNDISPLAY);
        rule.setAmberZoneSecsBeforePeriodEnd(300);
        rule.setRedZoneSecsBeforePeriodEnd(60);
        return rule;
    }

    private ElapsedTimeTradingRule getRule1() {
        ElapsedTimeTradingRule rule = new ElapsedTimeTradingRule();
        rule.setRuleType(TradingRuleType.SET_MARKET_SUSPENSION_STATUS);
        rule.setRuleName("Test rule 1");
        rule.setEventTier(2);
        rule.setDefaultStatusPreMatch(SuspensionStatus.SUSPENDED_UNDISPLAY);
        rule.setDefaultStatusInPlay(SuspensionStatus.OPEN);
        rule.setStatusInAmberZone(SuspensionStatus.SUSPENDED_DISPLAY);
        rule.setStatusInRedZone(SuspensionStatus.SUSPENDED_UNDISPLAY);
        rule.setAmberZoneSecsBeforePeriodEnd(120);
        rule.setRedZoneSecsBeforePeriodEnd(30);
        return rule;
    }

    private Market generateMatchWinnerMarket() {

        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match Winner");
        market.setLineId(null);
        market.setIsValid(true);
        market.setMarketGroup(MarketGroup.NOT_SPECIFIED);
        market.put("A", 0.5);
        market.put("B", 0.3);
        market.put("Draw", 0.2);
        return market;
    }

    /**
     * Generates an example total games market. All properties are set correctly except for the probabilities which are
     * set to fixed values in this example code. The probabilities are set up for a balanced line of 22.5
     */
    private Market generateTotalGamesMarket() {

        Market market = new Market(MarketCategory.OVUN, "FT:OU", "M", "Match total goals");
        market.setIsValid(true);
        market.setLineBase(13);
        /*
         * probs [n] holds the probability of total games being <= lineBase+ n; Using fixed probs here instead of
         * calculating from the model
         */

        double[] probs = {0.002, 0.011, 0.032, 0.068, 0.129, 0.199, 0.278, 0.354, 0.406, 0.484, 0.543, 0.558, 0.594,
                0.641, 0.674, 0.713, 0.756, 0.799, 0.846, 0.891, 0.919, 0.941, 0.968, 0.986, 0.988, 0.994, 1.000};
        market.setLineProbs(probs);
        market.setLineId("22.5"); // lineId is the balanced line - the one
                                  // that has prob closest to 0.5
        double probUnder = .484; // i.e. probs[9]
                                 // (=21-13)

        market.put("Under", probUnder);
        market.put("Over", 1 - probUnder);
        market.setSelectionNameOverOrA("Over 22.5");
        market.setSelectionNameUnderOrB("Under 22.5");
        market.setMarketGroup(MarketGroup.GOALS);
        return market;
    }

}
