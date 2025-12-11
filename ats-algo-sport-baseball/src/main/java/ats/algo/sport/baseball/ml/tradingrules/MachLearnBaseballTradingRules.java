package ats.algo.sport.baseball.ml.tradingrules;

import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class MachLearnBaseballTradingRules extends TradingRules {
    public MachLearnBaseballTradingRules() {

        TriggerParamFindTradingRule paramFindTradingRule =
                        new TriggerParamFindTradingRule("Baseball Mach Learn TriggerParamFindTradingRule", null);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML,FTOT:ML", 1);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU,FTOT:OU", 1);
        paramFindTradingRule.addMarketTypeRequiredInPlay("FAKEMARKET", 1);
        addRule(paramFindTradingRule);

    }
}
