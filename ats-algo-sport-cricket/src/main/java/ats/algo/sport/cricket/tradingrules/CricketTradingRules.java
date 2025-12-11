package ats.algo.sport.cricket.tradingrules;

import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class CricketTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> finalOverDisplayMarketList =
                    Arrays.asList("FT:ML", "FT:AXB", "FT:A:OU", "FT:B:OU", "P:WA", "P：CA", "P:WB", "P:CB");
    private List<String> inplayDisplayMarketList = Arrays.asList(ALL_MARKET);
    private List<String> prematchDisplayMarketList = Arrays.asList(ALL_MARKET);
    private List<String> marketListForOverrideProbability = Arrays.asList("FT:ML");
    Double[] rangeOfProbs = {0.02, 0.98};

    public CricketTradingRules() {
        super();
        double[] fixedOUlines = {0.5, 1.5, 2.5, 3.5};
        double minProbBoundaries = 0.0;
        double maxProbBoundaries = 1;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);
        addRule(new DerivedMarketTradingRule(null, "P:A:OUB", derivedMarketFixedLineSpec1));
        addRule(new DerivedMarketTradingRule(null, "P:B:OUB", derivedMarketFixedLineSpec1));
        for (int eventTier = 1; eventTier <= 7; eventTier++) {

            CricketTradingRule tierOneRule = new CricketTradingRule("Default Cricket trading rules", eventTier, null);
            tierOneRule.setFinalOverDisplayMarketList(finalOverDisplayMarketList);
            tierOneRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            tierOneRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            for (String string : marketListForOverrideProbability) {
                tierOneRule.addOverrideProbabilitySuspensionList(string, rangeOfProbs);
            }
            addRule(tierOneRule);
        }
        addRule(new CricketSuspendToAwaitParamFindTradingRule());
    }
}
