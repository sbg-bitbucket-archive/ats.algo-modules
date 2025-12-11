package ats.algo.tradingruletests;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.MarketsMetaData;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.sport.tennis.TennisMatchFormat;


public class DerivedMarketTradingRuleTest extends AlgoManagerSimpleTestBase {

    @Test
    public void test() {
        /*
         * start with empty set of trading rules. All mkts should be OPEN and no derived markets should be created
         */
        algoManager.setTradingRules(SupportedSportType.TENNIS, emptyTradingRules());
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, new TennisMatchFormat());
        MarketsMetaData marketsMetaData = publishedMarkets.getMarketsMetaData();
        Set<String> keys = marketsMetaData.getAllKeysForMarketType("FT:OU");
        assertEquals(1, keys.size());
        Market market = publishedMarkets.getMarketForFullKey((String) keys.toArray()[0]);
        assertEquals(SuspensionStatus.OPEN, market.getMarketStatus().getSuspensionStatus());
        /*
         * change rules to generate a derived market and set different suspension statuses for base and derived markets
         */
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules1());
        /*
         * force a recalc
         */
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        System.out.println(publishedMarkets);
        marketsMetaData = publishedMarkets.getMarketsMetaData();
        keys = marketsMetaData.getAllKeysForMarketType("FT:OU");
        market = publishedMarkets.getMarketForFullKey((String) keys.toArray()[0]);
        Market market2 = publishedMarkets.get("FT:OU:X");
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, market.getMarketStatus().getSuspensionStatus());
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, market2.getMarketStatus().getSuspensionStatus());

    }

    private TradingRules emptyTradingRules() {
        return new TradingRules();
    }

    private TradingRules tradingRules1() {
        TradingRules tradingRules = new TradingRules();
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal("FT:OU:X",
                        "TEST derived market", DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 4, 2, 12);
        AbstractTradingRule rule = new DerivedMarketTradingRule(null, "FT:OU", spec);
        tradingRules.addRule(rule);
        tradingRules.addRule(new TestSuspendTradingRule());
        return tradingRules;
    }

    private class TestSuspendTradingRule extends SetSuspensionStatusTradingRule {

        TestSuspendTradingRule() {
            super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, "testRule", null, null);
        }

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                        TriggerParamFindData triggerParamFindData) {
            MarketStatus status = market.getMarketStatus();
            if (market.getType().equals("FT:OU")) {
                status.setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
            }
            if (market.getType().equals("FT:OU:X")) {
                status.setSuspensionStatus(SuspensionStatus.SUSPENDED_DISPLAY);
            }
            status.setSuspensionStatusReason("test rule reason");
            status.setSuspensionStatusRuleName("testRuleName");



        }

    }



}
