package ats.algo.sport.baseball.tradingrules;


import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class BaseballTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    public BaseballTradingRules() {
        super();

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));

        BaseballTradingRule tierOneRule = new BaseballTradingRule("Default Base ball trading rules", 3, null);
        addRule(tierOneRule);

    }
}
