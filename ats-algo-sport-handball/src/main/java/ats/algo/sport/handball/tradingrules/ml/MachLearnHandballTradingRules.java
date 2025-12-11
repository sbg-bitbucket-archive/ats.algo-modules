package ats.algo.sport.handball.tradingrules.ml;

import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class MachLearnHandballTradingRules extends TradingRules {
    public MachLearnHandballTradingRules() {
        TriggerParamFindTradingRule paramFindTradingRule =
                        new TriggerParamFindTradingRule("Handball Mach Learn TriggerParamFindTradingRule", null);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:AXB", 1);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        paramFindTradingRule.addMarketTypeRequiredInPlay("FAKEMARKET", 1);
        addRule(paramFindTradingRule);

    }

}
