package ats.algo.sport.rugbyleague.tradingrules.ilt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.rugbyleague.tradingrules.RugbyLeagueSuspendToAwaitParamFindTradingRule;
import ats.algo.sport.rugbyleague.tradingrules.RugbyLeagueTradingRule;

public class IntralotRugbyLeagueTradingRules extends TradingRules {
    // private static final int HALFTIMEINSECONDS = 1800;
    public static final String ALL_MARKET = "All_MARKET";

    private List<String> allTiersAcceptedMarketList =
                    Arrays.asList("FT:A:OU", "FT:2HCP", "FT:AXB", "FT:AXB$OU", "FT:B:OU", "FT:DBLC", "FT:DNB", "FT:HSH",
                                    "FT:HTFT", "FT:ML", "FT:OE", "FT:OU", "FT:TM", "FT:WM", "FT:WWROM", "P:A:OU",
                                    "P:2HCP", "P:AXB", "P:B:OU", "P:DBLC", "P:DNB", "P:OE", "P:OU", "P:TM", "P:WM");

    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> halfTimeSuspendMarketList35Minutes = new ArrayList<String>(Arrays.asList("P:AXB", "P:OE",
                    "P:2HCP", "P:A:OU", "P:B:OU", "P:DNB", "P:DBLC", "P: TM ", "P:OU", "P:WM", "FT:HTFT"));
    private List<String> fullTimeSuspendMarketList70Minutes =
                    new ArrayList<String>(Arrays.asList("FT:DNB", "FT:WWROM", "FT:HSH", "FT:NG", "FT:SPRD"));
    private List<String> fullTimeSuspendMarketList73Minutes =
                    new ArrayList<String>(Arrays.asList("FT:WM", "FT: TM", "FT:DBLC", "OT:NG "));
    private List<String> fullTimeSuspendMarketList75Minutes = new ArrayList<String>(
                    Arrays.asList("FT:OE", "FT:2HCP", "FT:A:OU", "FT:B:OU", "FT:OU", "FT:CS", "OT:NG"));
    private List<String> fullTimeSuspendMarketList78Minutes = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public IntralotRugbyLeagueTradingRules() {
        super();
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);

        RugbyLeagueTradingRule rugbyleagueTradingRule =
                        new RugbyLeagueTradingRule("Intralot Rugby League Trading Rule", null, null);
        rugbyleagueTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        rugbyleagueTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        rugbyleagueTradingRule.addInPlaySuspendAtTimeMarketList(halfTimeSuspendMarketList35Minutes,
                        35 * SECONDS_IN_A_MINUTE);
        rugbyleagueTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList70Minutes,
                        70 * SECONDS_IN_A_MINUTE);
        rugbyleagueTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList73Minutes,
                        73 * SECONDS_IN_A_MINUTE);
        rugbyleagueTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList75Minutes,
                        75 * SECONDS_IN_A_MINUTE);
        rugbyleagueTradingRule.addInPlaySuspendAtTimeMarketList(fullTimeSuspendMarketList78Minutes,
                        78 * SECONDS_IN_A_MINUTE);
        rugbyleagueTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
        addRule(rugbyleagueTradingRule);
        addRule(new DerivedMarketTradingRule(null, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(null, "FT:B:OU", derivedMarketSpec));

        addRule(new RugbyLeagueSuspendToAwaitParamFindTradingRule());

    }


}
