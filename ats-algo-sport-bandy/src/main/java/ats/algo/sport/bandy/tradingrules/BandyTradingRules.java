package ats.algo.sport.bandy.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.tradingrules.TradingRules;

public class BandyTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    private List<String> marketListForOverrideProbability = Arrays.asList("FT:ML");
    private List<String> halfTimeSuspendMarketList40Minutes = new ArrayList<String>(
                    Arrays.asList("P:AXB", "P:OE", "P:DBLC", "P:DNB", "P:SPRD", "P:LTS", "P:AHCP", "P:A:OU", "P:B:OU"));
    private List<String> fullTimeSuspendMarketList80Minutes = new ArrayList<String>(
                    Arrays.asList("P:AXB", "P:OE", "P:DBLC", "P:DNB", "P:SPRD", "P:LTS", "P:AHCP", "P:A:OU", "P:B:OU"));
    private List<String> fullTimeSuspendMarketList85Minutes = new ArrayList<String>(
                    Arrays.asList("FT:CS", "FT:NG", "FT:DBLC", "FT:DNB", "FT:SPRD", "FT:LTS", "FT:AHCP"));
    private List<String> fullTimeSuspendMarketList89Minutes =
                    new ArrayList<String>(Arrays.asList("FT:AXB", "FT:ML", "FT:OU", "FT:OE"));

    Double[] rangeOfProbs = {0.02, 0.98};


    public BandyTradingRules() {
        super();
        for (int tier = 1; tier <= 6; tier++) {
            BandyTradingRule rule = new BandyTradingRule("Default Bandy trading rules", tier, null);
            rule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList40Minutes, 40 * SECONDS_IN_A_MINUTE);
            rule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList80Minutes, 80 * SECONDS_IN_A_MINUTE);
            rule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList85Minutes, 85 * SECONDS_IN_A_MINUTE);
            rule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList89Minutes, 89 * SECONDS_IN_A_MINUTE);
            rule.setStandardMinProbabilityForSuspension(0.035);
            rule.setStandardMaxProbabilityForSuspension(0.965);
            for (String string : marketListForOverrideProbability) {
                rule.addOverrideProbabilitySuspensionList(string, rangeOfProbs);
            }
            addRule(rule);
        }

        addRule(new BandySuspendToAwaitParamFindTradingRule());

    }
}
