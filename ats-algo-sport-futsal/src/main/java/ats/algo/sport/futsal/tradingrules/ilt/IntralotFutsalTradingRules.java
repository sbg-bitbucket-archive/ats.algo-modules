package ats.algo.sport.futsal.tradingrules.ilt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ats.algo.sport.futsal.FutsalMatchState;
import ats.algo.sport.futsal.tradingrules.FutsalTradingRule;
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

public class IntralotFutsalTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> halfTimeSuspendMarketList17Minutes = new ArrayList<String>(Arrays.asList("P:AXB_P1", "P:NG_P1",
                    "P:AHCP_P1", "P:OU_P1", "P:CS_P1", "P:£CSFLEX_1", "P:SPRD_P1"));
    private List<String> halfTimeSuspendMarketList35Minutes =
                    new ArrayList<String>(Arrays.asList("FT:WWROM", "FT:DNB", "FT:A:OU", "FT:B:OU"));
    private List<String> halfTimeSuspendMarketList37Minutes = new ArrayList<String>(
                    Arrays.asList("FT:DBLC", "FT:SPRD", "FT:NG", "FT:3HCP", "FT:OE", "FT:OU", "FT:EH"));
    private List<String> halfTimeSuspendMarketList38Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public IntralotFutsalTradingRules() {
        super();
        for (int i = 0; i < 6; i++) {
            FutsalTradingRule futsalTradingRule = new FutsalTradingRule("Futsal trading rule", i, null);
            futsalTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            futsalTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            futsalTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList17Minutes,
                            17 * SECONDS_IN_A_MINUTE);
            futsalTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList35Minutes,
                            35 * SECONDS_IN_A_MINUTE);
            futsalTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList37Minutes,
                            37 * SECONDS_IN_A_MINUTE);
            futsalTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList38Minutes,
                            38 * SECONDS_IN_A_MINUTE);
            addRule(futsalTradingRule);


            DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", preMatchLinesToDisplaySpec));
            addRule(new DerivedMarketTradingRule(i, "FT:OU", preMatchLinesToDisplaySpec));

            DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
                            .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 2, 1);

            DerivedMarketsStopingCriterias<AlgoMatchState> r = (AlgoMatchState ms) -> ((FutsalMatchState) ms)
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
