package ats.algo.sport.tabletennis.tradingrules;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class TabletennisTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    public TabletennisTradingRules() {
        super();

        /*
         * display one line around the balanced line for each of these markets
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));

        TabletennisTradingRule tierOneRule = new TabletennisTradingRule("Default Table tennis trading rules", 3, null);
        addRule(tierOneRule);
    }
}
