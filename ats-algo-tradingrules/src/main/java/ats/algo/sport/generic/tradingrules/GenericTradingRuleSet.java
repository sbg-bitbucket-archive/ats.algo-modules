package ats.algo.sport.generic.tradingrules;

import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class GenericTradingRuleSet extends TradingRules {


    /**
     * defines the set of rules that apply across all sports
     */
    public GenericTradingRuleSet() {
        super();
        SetSuspensionStatusTradingRule datafeedStateTradingRule = new DatafeedStateTradingRule();
        super.addRule(datafeedStateTradingRule);
    }
}
