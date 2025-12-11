package ats.algo.sport.squash.tradingrules;


import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class SquashTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    public SquashTradingRules() {
        super();

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));
        SquashTradingRule tierOneRule = new SquashTradingRule("Default Squash trading rules", 3, null);
        addRule(tierOneRule);
    }
}
