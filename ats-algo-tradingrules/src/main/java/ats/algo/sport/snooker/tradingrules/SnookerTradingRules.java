package ats.algo.sport.snooker.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class SnookerTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> matchSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:SPRD", "FT:OU", "FT:CS"));

    public SnookerTradingRules() {
        super();
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));

        SnookerTradingRule tierOneRule = new SnookerTradingRule("Snooker trading rule", 3, null);
        tierOneRule.setMatchSuspendMarketList(matchSuspendMarketList);
        addRule(tierOneRule);

    }
}
