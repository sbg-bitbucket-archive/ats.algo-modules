package ats.algo.sport.rugbyunion.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class RugbyUnionTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> halfTimeSuspendMarketList35Minutes = new ArrayList<String>(
                    Arrays.asList("P:AXB", "P:OE", "P:2HCP", "P:A:OU", "P:B:OU", "P:DNB", "P:DBLC", "P: TM ", "P:OU",
                                    "P:WM", "FT:HTFT", "P:TRYOU", "P:TRYCS", "P:BTTS", "P:TRYOU"));
    private List<String> fullTimeSuspendMarketList70Minutes =
                    new ArrayList<String>(Arrays.asList("FT:DNB", "FT:WWROM", "FT:HSH", "FT:NG", "FT:SPRD"));
    private List<String> fullTimeSuspendMarketList73Minutes =
                    new ArrayList<String>(Arrays.asList("FT:WM", "FT: TM", "FT:DBLC", "OT:NG "));
    private List<String> fullTimeSuspendMarketList75Minutes = new ArrayList<String>(
                    Arrays.asList("FT:OE", "FT:2HCP", "FT:A:OU", "FT:B:OU", "FT:OU", "FT:CS", "OT:NG"));
    private List<String> fullTimeSuspendMarketList78Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public RugbyUnionTradingRules() {
        super();
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);

        for (int i = 0; i < 6; i++) {
            RugbyUnionTradingRule rugbyunionTradingRule = new RugbyUnionTradingRule("Rugbyunion trading rule", i, null);
            rugbyunionTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
            rugbyunionTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
            rugbyunionTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList35Minutes,
                            35 * SECONDS_IN_A_MINUTE);
            rugbyunionTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList70Minutes,
                            70 * SECONDS_IN_A_MINUTE);
            rugbyunionTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList73Minutes,
                            73 * SECONDS_IN_A_MINUTE);
            rugbyunionTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList75Minutes,
                            75 * SECONDS_IN_A_MINUTE);
            rugbyunionTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList78Minutes,
                            78 * SECONDS_IN_A_MINUTE);
            addRule(rugbyunionTradingRule);
            addRule(new DerivedMarketTradingRule(i, "FT:A:OU", derivedMarketSpec));
            addRule(new DerivedMarketTradingRule(i, "FT:B:OU", derivedMarketSpec));

        }
        addRule(new RugbyUnionSuspendToAwaitParamFindTradingRule());

    }


}
