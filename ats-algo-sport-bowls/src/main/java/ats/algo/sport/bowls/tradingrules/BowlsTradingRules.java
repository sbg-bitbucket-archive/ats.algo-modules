package ats.algo.sport.bowls.tradingrules;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class BowlsTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    public BowlsTradingRules() {
        super();

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:B:OU", derivedMarketSpec));
        BowlsTradingRule tierOneRule = new BowlsTradingRule("Default Bowls trading rules", 1, null);
        addRule(tierOneRule);
    }
}
