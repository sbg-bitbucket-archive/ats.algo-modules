package ats.algo.sport.tennis.tradingrules.ppb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.abandonIncident.tradingrules.AbandonIncidentTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.tradingrules.TradingRules;

@SuppressWarnings("unused")
public class PpbTennisTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    // Market list to SU poker generated market
    // private List<String> currentPointSuspendMarketList = new
    // ArrayList<String>(Arrays.asList("G:PW"));
    private List<String> suspendListWhenAbandonMatch = Arrays.asList("FT:ML");

    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:ML");

    private List<String> tierTwoinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "P:ML", "G:ML",
                    "G:PW", "P:TBML", "SG:AA", "SG:BB", "P:TBCS", "FT:CTBCS", "G:TBPW"));

    private List<String> tierThreePreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:CS", "FT:S:SPRD", "FT:W1S:A",
                    "FT:W1S:B", "P:CS", "P:ML", "P:SPRD", "P:CSG", "S:OU3", "FT:TB:OU", "FT:BPTW", "FT:BTW1S",
                    "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6", "P:RTSG");

    private List<String> tierFourinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "G:ML", "G:PW",
                    "P:ML", "S:OU", "G:CS", "FT:CS", "FT:S:SPRD", "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "G:DEUCE",
                    "P:TBML", "SG:AA", "SG:BB", "G:DEUCE", "P:SPRD", "P:CSG", "S:OU3", "FT:TB:OU", "FT:BPTW",
                    "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6", "FT:BTW1S", "FT:TBIM",
                    "P:TBCS", "FT:CTBCS", "G:TBPW", "P:RTSG"));

    private List<String> tierFivePreMatchOpenMarketList = Arrays.asList("FT:ML", "S:OU", "FT:SPRD", "FT:OU", "FT:CS",
                    "FT:L1SML", "FT:W1SML", "FT:S:OU", "FT:CS", "FT:L1SML", "FT:W1SML", "FT:OU:A", "FT:SPRD",
                    "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "P:SPRD", "P:CSG", "P:CS2", "P:CS4", "P:CS6", "S:OU3",
                    "FT:TB:OU", "FT:BPTW", "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6",
                    "FT:BTW1S", "FT:TBIM", "FT:ALT:S:OU", "FT:S:ASPRD", "FT:S:SPRD", "FT:OU:B", "FT:FSB:A", "FT:FSB:B",
                    "FT:AML", "FT:AOU", "P:RTSG", "FT:WM", "FT:WM:A", "FT:WM:B");

    private List<String> tierSixinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("G:CS", "FT:ML", "G:ML",
                    "G:PW", "P:ML", "S:OU", "FT:SPRD", "FT:OU", "FT:CS", "FT:L1SML", "FT:W1SML", "FT:S:OU", "FT:CS",
                    "FT:L1SML", "FT:W1SML", "FT:OU:A", "FT:SPRD", "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "G:DEUCE",
                    "P:TBML", "SG:AA", "SG:BB", "G:DEUCE", "P:SPRD", "P:CSG", "P:CS2", "P:CS4", "P:CS6", "S:OU3",
                    "FT:TB:OU", "FT:BPTW", "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6",
                    "FT:BTW1S", "FT:TBIM", "P:TBCS", "FT:CTBCS", "FT:ALT:S:OU", "FT:S:ASPRD", "FT:S:SPRD", "FT:OU:B",
                    "FT:FSB:A", "FT:FSB:B", "FT:AOU", "FT:AML", "G:TBPW", "P:RTSG", "FT:WM", "FT:WM:A", "FT:WM:B"));

    private List<String> tierElevenPreMatchOpenMarketList = new ArrayList<String>(Arrays.asList("FT:OU", "FT:BTW1S",
                    "FT:ML", "FT:CS", "FT:S:OU", "P:CS", "P:ML", "FT:SPRD", "FT:W1SML", "FT:L1SML", "FT:W1S:A",
                    "FT:W1S:B", "FT:TBIM", "FT:BPTW", "FT:TB:OU", "FT:OU:A", "FT:OU:B"));

    private List<String> tierTwelveInPlayDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("G:ML", "G:PW", "FT:OU", "FT:BTW1S", "FT:ML", "FT:CS", "G:ML",
                                    "P:ML", "P:CS", "FT:S:OU", "P:TBML", "FT:SPRD", "FT:W1SML", "FT:L1SML", "FT:W1S:A",
                                    "FT:W1S:B", "FT:TBIM", "FT:BPTW", "FT:TB:OU", "FT:OU:A", "FT:OU:B"));

    // private List<String> tierSevenPreMatchOpenMarketList = Arrays.asList("");
    // private List<String> tierSeveninPlayDisplayMarketList = new ArrayList<String>(Arrays.asList(""));

    private List<String> tierOneGamePointSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("G:ML", "G:CS", "G:DEUCE", "G:PW", "P:ML"));
    private List<String> setCSSuspendMarketList = new ArrayList<String>(Arrays.asList("P:CS"));
    private List<String> setRunningSuspendMarketList = new ArrayList<String>(Arrays.asList("P:CS"));

    private List<String> setBettingSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:CS"));

    private List<String> dangerPointSuspendMarketList = new ArrayList<String>(Arrays.asList("G:ML", "G:CS", "G:DEUCE",
                    "FT:SPRD", "FT:S:SPRD", "FT:S:OU", "FT:OU:A", "FT:OU:B", "FT:OU", "FT:ASPRD1", "FT:ASPRD2",
                    "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6", "SG:AA", "SG:BB", "FT:FSB:A", "FT:FSB:B"));

    private List<String> zeorProbaliltySuspendMarketList = new ArrayList<String>(Arrays.asList("FT:FSB:A", "FT:FSB:B"));
    // private List<String> tierOneTieBreakSuspendMarketList = new
    // ArrayList<String>(Arrays.asList("P:ML", "P:CS"));

    public PpbTennisTradingRules() {
        super();

        PpbTennisTradingRule tierEightRule = new PpbTennisTradingRule("Tennis Tier " + 8 + " trading rules", 8, null);
        tierEightRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
        Map<String, List<String>> inplaySequenceIdFiltersMapTierEight = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapTierEight.put("G:PW", new ArrayList<String>(Arrays.asList("S0.0.1")));
        tierEightRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapTierEight);
        addRule(tierEightRule);
        for (int i = 1; i <= 12; i++) {


            PpbTennisTradingRule tierOneRule = new PpbTennisTradingRule("Tennis Tier " + i + " trading rules", i, null);
            tierOneRule.setRuleForDoublesMatch(true);
            tierOneRule.setSuspendOnlyOneSelectionMarkets(false); // affect sg:aa/bb markets
            tierOneRule.setZeorProbaliltySuspendMarketList(zeorProbaliltySuspendMarketList);
            Map<String, List<String>> inplaySequenceIdFiltersMapTierOne = new HashMap<String, List<String>>();
            inplaySequenceIdFiltersMapTierOne.put("G:PW",
                            new ArrayList<String>(Arrays.asList("S0.0.1", "S0.0.2", "S0.0.3", "S0.0.4", "S0.0.5")));
            tierOneRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapTierOne);

            Map<String, List<String>> preMatchSequenceIdFiltersMapTierOne = new HashMap<String, List<String>>();
            preMatchSequenceIdFiltersMapTierOne.put("P:CS", new ArrayList<String>(Arrays.asList("S0")));
            preMatchSequenceIdFiltersMapTierOne.put("P:ML", new ArrayList<String>(Arrays.asList("S0")));
            tierOneRule.setPreMatchSequenceIdFilterList(preMatchSequenceIdFiltersMapTierOne);

            if (i == 12) {
                tierOneRule.setInplayDisplayMarketList(tierTwelveInPlayDisplayMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 11) {
                tierOneRule.setPrematchDisplayMarketList(tierElevenPreMatchOpenMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 6) {
                tierOneRule.setInplayDisplayMarketList(tierSixinPlayDisplayMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 5) {
                tierOneRule.setPrematchDisplayMarketList(tierFivePreMatchOpenMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 4) {
                tierOneRule.setInplayDisplayMarketList(tierFourinPlayDisplayMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 3) {
                tierOneRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else if (i == 2) {
                tierOneRule.setInplayDisplayMarketList(tierTwoinPlayDisplayMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
            } else {
                tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
                // tierOneRule.setNoPFSuspendMarketList(tierOneNoPFSuspendMarketList);
                tierOneRule.setGamePointSuspendMarketList(tierOneGamePointSuspendMarketList);
                tierOneRule.setSetRunningSuspendMarketList(setRunningSuspendMarketList);
                tierOneRule.setSetBettingSuspendMarketList(setBettingSuspendMarketList);
                tierOneRule.setSetCSSuspendMarketList(setCSSuspendMarketList);
                tierOneRule.setDangerPointSuspendMarketList(dangerPointSuspendMarketList);
                tierOneRule.setRuleForDoublesMatch(false);
                // tierOneRule.setCurrentPointSuspendMarketList(currentPointSuspendMarketList);
                tierOneRule.setInPlayRequiresSuccessfulParamFind(false);
            }
            addRule(tierOneRule);

            // double[] lines = {1.5, 2.5, 3.5};
            // DerivedMarketSpec derivedMarketFixedLineSpec = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
            // DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, lines);
            // addRule(new DerivedMarketTradingRule(3, "FT:TB:OU", derivedMarketFixedLineSpec));
        }


        PpbTennisTraderAlertTradingRule traderAlertTradingRule =
                        new PpbTennisTraderAlertTradingRule("Ppb TennisTraderAlertTradingRule");
        addRule(traderAlertTradingRule);

        /*
         * Delay the suspension
         */
        PpbTennisMonitorFeedDelaySuspensionMethodHelper suspensionMethod =
                        new PpbTennisMonitorFeedDelaySuspensionMethodHelper.Builder()
                                        .medicalTimeout(0, 10000 * 6 * 7, true).challenge(0, 10000 * 3, false).build(); // 10
                                                                                                                        // second
                                                                                                                        // increments
        /*
         * could change 4, 0 parameters if needed
         */
        addRule(new MonitorFeedTradingRule(0, 0,
                        (now, matchState) -> suspensionMethod.monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        // AbandonIncidentTradingRule ppbTennisAbandonMarketsRule =
        // new AbandonIncidentTradingRule(suspendListWhenAbandonMatch);
        // addRule(ppbTennisAbandonMarketsRule);
        PpbTennisPropertyChangesTradingRule ppbTennisPropertyChangesTradingRule =
                        new PpbTennisPropertyChangesTradingRule();
        addRule(ppbTennisPropertyChangesTradingRule);

        AbandonIncidentTradingRule abandonIncidentTradingRule = new AbandonIncidentTradingRule();
        abandonIncidentTradingRule
                        .setSuspendList(Arrays.asList("FT:W1S:A", "FT:W1S:B", "FT:ML", "P:CS", "P:ML", "FT:CS"));
        addRule(abandonIncidentTradingRule);

    }


}
