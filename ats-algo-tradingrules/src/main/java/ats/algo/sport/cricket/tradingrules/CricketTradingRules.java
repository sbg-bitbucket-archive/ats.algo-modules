package ats.algo.sport.cricket.tradingrules;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class CricketTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    public CricketTradingRules() {
        super();

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:B:OU", derivedMarketSpec));
        CricketTradingRule tierOneRule = new CricketTradingRule("Default Cricket trading rules", 1, null);
        addRule(tierOneRule);
    }
}
