package ats.algo.sport.tennis.ppb.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.abandonIncident.tradingrules.AbandonIncidentTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class PpbTennisTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";

    private List<String> suspendListWhenAbandonMatch = Arrays.asList("FT:ML");
    private List<String> tiebreakDangerPointSuspensionList = Arrays.asList("P:TBCS");
    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:ML");

    private List<String> tierTwoinPlayDisplayMarketList = new ArrayList<String>(
                    Arrays.asList("FT:ML", "P:ML", "G:ML", "G:PW", "P:TBML", "SG:AA", "SG:BB", "P:TBCS"));

    private List<String> tierThreePreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:CS", "FT:S:SPRD", "FT:W1S:A",
                    "FT:W1S:B", "P:CS", "P:ML", "P:SPRD", "P:CSG", "S:OU3", "FT:TB:OU", "FT:BPTW", "FT:BTW1S",
                    "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6");

    private List<String> tierFourinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "G:ML", "G:PW",
                    "P:ML", "S:OU", "G:CS", "FT:CS", "FT:S:SPRD", "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "G:DEUCE",
                    "P:TBML", "SG:AA", "SG:BB", "G:DEUCE", "P:SPRD", "P:CSG", "S:OU3", "FT:TB:OU", "FT:BPTW",
                    "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6", "FT:BTW1S", "FT:TBIM",
                    "P:TBCS"));

    private List<String> tierFivePreMatchOpenMarketList = Arrays.asList("FT:ML", "S:OU", "FT:SPRD", "FT:OU", "FT:CS",
                    "FT:L1SML", "FT:W1SML", "FT:S:OU", "FT:CS", "FT:L1SML", "FT:W1SML", "FT:OU:A", "FT:SPRD",
                    "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "P:SPRD", "P:CSG", "P:CS2", "P:CS4", "P:CS6", "S:OU3",
                    "FT:TB:OU", "FT:BPTW", "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6",
                    "FT:BTW1S", "FT:TBIM", "FT:ALT:S:OU", "FT:S:ASPRD", "FT:S:SPRD", "FT:OU:B", "FT:FSB:A", "FT:FSB:B");

    private List<String> tierSixinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("G:CS", "FT:ML", "G:ML",
                    "G:PW", "P:ML", "S:OU", "FT:SPRD", "FT:OU", "FT:CS", "FT:L1SML", "FT:W1SML", "FT:S:OU", "FT:CS",
                    "FT:L1SML", "FT:W1SML", "FT:OU:A", "FT:SPRD", "FT:W1S:A", "FT:W1S:B", "P:CS", "P:ML", "G:DEUCE",
                    "P:TBML", "SG:AA", "SG:BB", "G:DEUCE", "P:SPRD", "P:CSG", "P:CS2", "P:CS4", "P:CS6", "S:OU3",
                    "FT:TB:OU", "FT:BPTW", "FT:ASPRD1", "FT:ASPRD2", "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6",
                    "FT:BTW1S", "FT:TBIM", "P:TBCS", "FT:ALT:S:OU", "FT:S:ASPRD", "FT:S:SPRD", "FT:OU:B", "FT:FSB:A",
                    "FT:FSB:B"));

    // private List<String> tierSevenPreMatchOpenMarketList = Arrays.asList("");
    // private List<String> tierSeveninPlayDisplayMarketList = new ArrayList<String>(Arrays.asList(""));

    private List<String> tierOneGamePointSuspendMarketList = new ArrayList<String>(Arrays.asList("G:ML", "G:CS",
                    "G:DEUCE", "G:PW", "P:ML", "FT:SPRD", "FT:S:SPRD", "FT:S:OU", "FT:OU:A", "FT:OU:B", "FT:OU"));
    private List<String> setCSSuspendMarketList = new ArrayList<String>(Arrays.asList("P:CS"));
    private List<String> setRunningSuspendMarketList = new ArrayList<String>(Arrays.asList("P:CS"));

    private List<String> setBettingSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:CS"));

    private List<String> gamePointSuspendMarketList = new ArrayList<String>(Arrays.asList("G:ML", "G:CS", "G:DEUCE",
                    "FT:SPRD", "FT:S:SPRD", "FT:S:OU", "FT:OU:A", "FT:OU:B", "FT:OU", "FT:ASPRD1", "FT:ASPRD2",
                    "FT:ASPRD3", "FT:ASPRD4", "FT:ASPRD5", "FT:ASPRD6", "SG:AA", "SG:BB"));
    // private List<String> tierOneTieBreakSuspendMarketList = new
    // ArrayList<String>(Arrays.asList("P:ML", "P:CS"));

    public PpbTennisTradingRules() {
        super();

        PpbTennisTradingRule tierEightRule = new PpbTennisTradingRule("Tennis Tier " + 8 + " trading rules", 8, null);
        tierEightRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
        Map<String, List<String>> inplaySequenceIdFiltersMapTierEight = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapTierEight.put("G:PW", new ArrayList<String>(Arrays.asList("S0.0.1")));
        tierEightRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapTierEight);
        addRule(tierEightRule);
        for (int i = 1; i <= 7; i++) {


            PpbTennisTradingRule tierOneRule = new PpbTennisTradingRule("Tennis Tier " + i + " trading rules", i, null);
            tierOneRule.setRuleForDoublesMatch(true);
            tierOneRule.setSuspendOnlyOneSelectionMarkets(false); // affect sg:aa/bb markets

            Map<String, List<String>> inplaySequenceIdFiltersMapTierOne = new HashMap<String, List<String>>();
            inplaySequenceIdFiltersMapTierOne.put("G:PW",
                            new ArrayList<String>(Arrays.asList("S0.0.1", "S0.0.2", "S0.0.3", "S0.0.4", "S0.0.5")));
            tierOneRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapTierOne);

            Map<String, List<String>> preMatchSequenceIdFiltersMapTierOne = new HashMap<String, List<String>>();
            preMatchSequenceIdFiltersMapTierOne.put("P:CS", new ArrayList<String>(Arrays.asList("S0")));
            preMatchSequenceIdFiltersMapTierOne.put("P:ML", new ArrayList<String>(Arrays.asList("S0")));
            tierOneRule.setPreMatchSequenceIdFilterList(preMatchSequenceIdFiltersMapTierOne);
            tierOneRule.setTiebreakDangerPointSuspensionList(tiebreakDangerPointSuspensionList);
            if (i == 6) {
                tierOneRule.setInplayDisplayMarketList(tierSixinPlayDisplayMarketList);
                tierOneRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            } else if (i == 5) {
                tierOneRule.setPrematchDisplayMarketList(tierFivePreMatchOpenMarketList);
                tierOneRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            } else if (i == 4) {
                tierOneRule.setInplayDisplayMarketList(tierFourinPlayDisplayMarketList);
                tierOneRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            } else if (i == 3) {
                tierOneRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
                tierOneRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            } else if (i == 2) {
                tierOneRule.setInplayDisplayMarketList(tierTwoinPlayDisplayMarketList);
                tierOneRule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            } else {
                tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
                tierOneRule.setGamePointSuspendMarketList(tierOneGamePointSuspendMarketList);
                tierOneRule.setSetRunningSuspendMarketList(setRunningSuspendMarketList);
                tierOneRule.setSetBettingSuspendMarketList(setBettingSuspendMarketList);
                tierOneRule.setSetCSSuspendMarketList(setCSSuspendMarketList);
                tierOneRule.setRuleForDoublesMatch(false);
                tierOneRule.setInPlayRequiresSuccessfulParamFind(false);
            }
            addRule(tierOneRule);
        }


        PpbTennisTraderAlertTradingRule traderAlertTradingRule =
                        new PpbTennisTraderAlertTradingRule("Ppb TennisTraderAlertTradingRule");
        addRule(traderAlertTradingRule);

        /*
         * Delay the suspension
         */
        PpbTennisMonitorFeedDelaySuspensionMethodHelper suspensionMethod =
                        new PpbTennisMonitorFeedDelaySuspensionMethodHelper.Builder()
                                        .medicalTimeout(0, 10000 * 6 * 7, true).challenge(0, 30000, false).build();
        /*
         * could change 4, 0 parameters if needed
         */
        addRule(new MonitorFeedTradingRule(0, 0,
                        (now, matchState) -> suspensionMethod.monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        AbandonIncidentTradingRule ppbTennisAbandonMarketsRule =
                        new AbandonIncidentTradingRule(suspendListWhenAbandonMatch);
        addRule(ppbTennisAbandonMarketsRule);
        PpbTennisPropertyChangesTradingRule ppbTennisPropertyChangesTradingRule =
                        new PpbTennisPropertyChangesTradingRule();
        addRule(ppbTennisPropertyChangesTradingRule);

    }


}
