package ats.algo.sport.squash.tradingrules;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.squash.tradingrules.SquashTradingRule;

public class SquashTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> gamePointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("P:OU", "P:OE", "P:A:OU", "P:B:OU", "P:ET", "P:SPRD", "P:ML", "P:SPSPRD", "P:WM"));
    private List<String> finalPointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("FT:ML", "FT:OU", "FT:A:OU", "FT:B:OU", "FT:SPRD", "FT:PSPRD", "FT:OE", "FT:CS",
                                    "P:OU", "P:OE", "P:A:OU", "P:B:OU", "P:ET", "P:SPRD", "P:ML", "P:SPSPRD", "P:WM"));
    private List<String> raceSuspendMarketList = new ArrayList<String>(Arrays.asList("P:RACE"));
    private List<String> marketListForOverrideProbability = Arrays.asList("FT:ML");
    Double[] rangeOfProbs = {0.02, 0.98};

    public SquashTradingRules() {
        super();

        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));
        for (int tier = 1; tier <= 5; tier++) {
            SquashTradingRule rule = new SquashTradingRule("Default Squash trading rules", tier, null);
            rule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            rule.setFinalPointSuspendMarketList(finalPointSuspendMarketList);
            rule.setRaceSuspendMarketList(raceSuspendMarketList);
            rule.setStandardMinProbabilityForSuspension(0.035);
            rule.setStandardMaxProbabilityForSuspension(0.965);
            for (String string : marketListForOverrideProbability) {
                rule.addOverrideProbabilitySuspensionList(string, rangeOfProbs);
            }
            addRule(rule);
        }
        SquashSuspendToAwaitParamFindTradingRule squashSuspendToAwaitParamFindTradingRule =
                        new SquashSuspendToAwaitParamFindTradingRule();
        addRule(squashSuspendToAwaitParamFindTradingRule);
    }
}
