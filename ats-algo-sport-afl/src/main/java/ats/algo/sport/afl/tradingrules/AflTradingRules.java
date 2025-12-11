package ats.algo.sport.afl.tradingrules;

import static ats.algo.sport.afl.tradingrules.AflTradingList.EXTRA_TIME_OPEN_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.FIRST_HALF_SUSPEND_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.FULL_TIME_SUSPEND_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.INPLAY_OPEN_MARKET;
import static ats.algo.sport.afl.tradingrules.AflTradingList.PREMATCH_OPEN_MARKET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class AflTradingRules extends TradingRules {
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(PREMATCH_OPEN_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(INPLAY_OPEN_MARKET));
    private List<String> halfTimeSuspendMarketList = new ArrayList<String>(Arrays.asList(FIRST_HALF_SUSPEND_MARKET));
    private List<String> fullTimeSuspendMarketList = new ArrayList<String>(Arrays.asList(FULL_TIME_SUSPEND_MARKET));
    private List<String> extraTimeOpenMarketList = new ArrayList<String>(Arrays.asList(EXTRA_TIME_OPEN_MARKET));
    private int halfTimeSuspend = 2;
    private int fullTimeSuspend = 4;

    public AflTradingRules() {
        super();

        AflTradingRule aflTradingRule = new AflTradingRule("Afl trading rule", 1, null);
        aflTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        aflTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        aflTradingRule.setHalfTimeSuspend(halfTimeSuspend);
        aflTradingRule.setFullTimeSuspend(fullTimeSuspend);
        aflTradingRule.setHalfTimeSuspendMarketList(halfTimeSuspendMarketList);
        aflTradingRule.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        aflTradingRule.setExtraTimeOpenMarketList(extraTimeOpenMarketList);
        addRule(aflTradingRule);

        DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec
                        .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 1, 1);
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", preMatchLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU", preMatchLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU:G", preMatchLinesToDisplaySpec));
        DerivedMarketSpec inPlayLinesToDisplaySpec = DerivedMarketSpec
                        .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 2, 1);
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", inPlayLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU", inPlayLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU:G", inPlayLinesToDisplaySpec));

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR1",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 10, 10, true);
        DerivedMarketTradingRule derivedMarketTradingRule =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec);
        addRule(derivedMarketTradingRule);

        DerivedMarketSpec derivedMarketSpec2 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR2",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4, 10, true);
        DerivedMarketTradingRule derivedMarketTradingRule2 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec2);
        addRule(derivedMarketTradingRule2);

        DerivedMarketSpec derivedMarketSpec5 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("FT:SPRD_DR5",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 20, true);
        DerivedMarketTradingRule derivedMarketTradingRule5 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec5);
        addRule(derivedMarketTradingRule5);

        DerivedMarketSpec derivedMarketSpec3 = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeTotal("FT:OU_DR3",
                        "Total points", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 4, 20, 150);
        DerivedMarketTradingRule derivedMarketTradingRule3 =
                        new DerivedMarketTradingRule(null, "FT:OU", derivedMarketSpec3);
        addRule(derivedMarketTradingRule3);

        DerivedMarketSpec derivedMarketSpec6 = DerivedMarketSpec.getDerivedMarketSpecForStaticLines("FT:SPRD_DR6",
                        "Winning margin", DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 20);
        DerivedMarketTradingRule derivedMarketTradingRule6 =
                        new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketSpec6);
        addRule(derivedMarketTradingRule6);


        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Aussie Rules TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML,FT:SPRD", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML,FT:SPRD", 1);
        addRule(triggerParamFindTradingRule);

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
