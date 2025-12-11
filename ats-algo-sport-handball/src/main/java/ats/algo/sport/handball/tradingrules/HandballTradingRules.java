package ats.algo.sport.handball.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ats.algo.sport.handball.HandballMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.AbstractTradingRule;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.core.tradingrules.DerivedMarketsStopingCriterias;

public class HandballTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> halfTimeSuspendMarketList28Minutes =
                    new ArrayList<String>(Arrays.asList("P:OU_P1", "P:DNB_P1", "P:SPRD_P1", "P:AXB_P1", "P:DBLC_P1"));
    private List<String> halfTimeSuspendMarketList50Minutes = new ArrayList<String>(Arrays.asList("FT:DBLC"));
    private List<String> halfTimeSuspendMarketList55Minutes = new ArrayList<String>(Arrays.asList("FT:DNB"));
    private List<String> halfTimeSuspendMarketList57Minutes =
                    new ArrayList<String>(Arrays.asList("P:OU", "FT:A:OU", "FT:OU", "P:DNB", "P:SPRD", "OT:NG",
                                    "FT:A:OU", "P:AXB_P1", "P:OU", "FT:SPRD", "P:DBLC", "FT:B:OU", "P:AXB"));
    private List<String> halfTimeSuspendMarketList58Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public HandballTradingRules() {
        super();
        for (int i = 0; i < 6; i++) {
            HandballTradingRule handballTradingRule = new HandballTradingRule("Handball trading rule", i, null);
            handballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            handballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList28Minutes,
                            28 * SECONDS_IN_A_MINUTE);
            handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList50Minutes,
                            50 * SECONDS_IN_A_MINUTE);
            handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList55Minutes,
                            55 * SECONDS_IN_A_MINUTE);
            handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList57Minutes,
                            57 * SECONDS_IN_A_MINUTE);
            handballTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList58Minutes,
                            58 * SECONDS_IN_A_MINUTE);
            addRule(handballTradingRule);


            DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", preMatchLinesToDisplaySpec));
            addRule(new DerivedMarketTradingRule(i, "FT:OU", preMatchLinesToDisplaySpec));

            DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 2, 1);

            DerivedMarketsStopingCriterias<AlgoMatchState> r = (AlgoMatchState ms) -> ((HandballMatchState) ms)
                            .getElapsedTimeSecs() < (55 * SECONDS_IN_A_MINUTE);

            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", inPlayLinesToDisplaySpec, r));
            addRule(new DerivedMarketTradingRule(i, "FT:OU", inPlayLinesToDisplaySpec, r));

        }



        AbstractTradingRule suspensionRule = new TestSuspensionRule();
        addRule(suspensionRule);
    }


    class TestSuspensionRule extends SetSuspensionStatusTradingRule {

        private static final long serialVersionUID = 1L;

        TestSuspensionRule() {
            super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, "test suspension rule", null, null);
        }

        @Override
        public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                        TriggerParamFindData triggerParamFindData) {
            if (market.getType().equals("FT:SPRD_DR2")) {
                MarketStatus status = market.getMarketStatus();
                status.setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
                status.setSuspensionStatusReason("test reason");
                status.setSuspensionStatusRuleName(this.getClass().toString());
            }
        }
    }


}
