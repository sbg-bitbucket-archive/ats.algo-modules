package ats.algo.sport.snooker.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class SnookerTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> matchSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("FT:CS", "FT:OE", "FT:OU", "FT:SPRD"));
    private List<String> marketListForOverrideProbability = Arrays.asList("FT:ML");
    Double[] rangeOfProbs = {0.02, 0.98};

    public SnookerTradingRules() {
        super();
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        for (int tier = 1; tier <= 5; tier++) {
            addRule(new DerivedMarketTradingRule(tier, "FT:SPRD", derivedMarketSpec));
            addRule(new DerivedMarketTradingRule(tier, "FT:OU", derivedMarketSpec));
            addRule(new DerivedMarketTradingRule(tier, "FT:A:OU", derivedMarketSpec));
            addRule(new DerivedMarketTradingRule(tier, "FT:B:OU", derivedMarketSpec));

            SnookerTradingRule tierRule = new SnookerTradingRule("Snooker trading rule", tier, null);
            tierRule.setMatchSuspendMarketList(matchSuspendMarketList);
            tierRule.setStandardMinProbabilityForSuspension(0.035);
            tierRule.setStandardMaxProbabilityForSuspension(0.965);
            addRule(tierRule);
            for (String string : marketListForOverrideProbability) {
                tierRule.addOverrideProbabilitySuspensionList(string, rangeOfProbs);
            }
        }

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Snooker TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("P:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("P:ML", 1);
        addRule(triggerParamFindTradingRule);

        SnookerSuspendToAwaitParamFindTradingRule snookerSuspendToAwaitParamFindTradingRule =
                        new SnookerSuspendToAwaitParamFindTradingRule();
        addRule(snookerSuspendToAwaitParamFindTradingRule);
    }
}
