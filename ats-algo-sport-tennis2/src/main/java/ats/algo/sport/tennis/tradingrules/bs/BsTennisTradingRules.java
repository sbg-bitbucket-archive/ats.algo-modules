package ats.algo.sport.tennis.tradingrules.bs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.resetbias.tradingrules.ResetBiasTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class BsTennisTradingRules extends TradingRules {

    public static final String ALL_MARKET = "All_MARKET";

    private List<String> currentPointSuspendMarketList = new ArrayList<String>(Arrays.asList("G:PW"));

    private List<String> noPFSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("G:PW", "G:ML", "G:CS", "G:DEUCE"));

    private List<String> breakPointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("FT:OU", "G:PW", "FT:PW5G", "FT:NUMSET", "FT:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B"));

    private List<String> tieBreakSuspendMarketList = new ArrayList<String>(Arrays.asList("P:ML", "FT:OU", "G:DEUCE",
                    "FT:NUMSET", "FT:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B", "G:PW"));

    private List<String> finalSetTieBreakSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("P:ML", "FT:OU", "G:DEUCE", "FT:NUMSET", "FT:CS", "FT:SPRD",
                                    "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B", "G:PW"));


    /*
     * Tier 1 suspension
     */

    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:ML", "P:ML", "FT:OU", "FT:NUMSET", "FT:CS",
                    "P:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");
    private List<String> tierOneInPlayDisplayMarketList = Arrays.asList("FT:ML", "P:ML", "FT:OU", "G:PW", "G:ML",
                    "G:CS", "G:DEUCE", "FT:PW5G", "FT:NUMSET", "FT:CS", "P:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B",
                    "FT:TBIM", "FT:W1S:A", "FT:W1S:B");

    /*
     * Tier 2 suspension
     */
    private List<String> tierTwoPreMatchOpenMarketList = Arrays.asList("FT:ML", "P:ML", "FT:OU", "FT:CS", "P:CS",
                    "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");
    private List<String> tierTwoInPlayDisplayMarketList =
                    Arrays.asList("FT:ML", "P:ML", "FT:OU", "G:PW", "G:ML", "G:CS", "G:DEUCE", "FT:PW5G", "FT:CS",
                                    "P:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");

    /*
     * Tier 3 suspension
     */
    private List<String> tierThreePreMatchOpenMarketList =
                    Arrays.asList("FT:ML", "P:ML", "FT:OU", "FT:CS", "P:CS", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");
    private List<String> tierThreeInPlayDisplayMarketList =
                    Arrays.asList("FT:ML", "P:ML", "FT:OU", "G:PW", "G:ML", "G:CS", "G:DEUCE", "FT:PW5G", "FT:CS",
                                    "P:CS", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");

    /*
     * Tier 4 suspension
     */
    private List<String> tierFourPreMatchOpenMarketList =
                    Arrays.asList("FT:ML", "P:ML", "FT:CS", "P:CS", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");
    private List<String> tierFourInPlayDisplayMarketList = Arrays.asList("FT:ML", "P:ML", "G:PW", "G:ML", "G:CS",
                    "G:DEUCE", "FT:PW5G", "FT:CS", "P:CS", "FT:TBIM", "FT:W1S:A", "FT:W1S:B");

    /*
     * Tier 5 suspension
     */
    private List<String> tierFivePreMatchOpenMarketList = Arrays.asList("FT:ML", "P:ML");
    private List<String> tierFiveInPlayDisplayMarketList = Arrays.asList("FT:ML", "P:ML", "G:PW", "G:ML", "G:CS",
                    "G:DEUCE", "FT:PW5G", "FT:CS", "P:CS", "FT:TBIM");

    /*
     * Tier 6 suspension
     */
    private List<String> tierSixPreMatchOpenMarketList = Arrays.asList("FT:ML");
    private List<String> tierSixInPlayDisplayMarketList =
                    Arrays.asList("FT:ML", "P:ML", "G:DEUCE", "G:PW", "G:ML", "G:CS");

    /*
     * Tier 7 suspension
     */
    private List<String> tierSevenPreMatchOpenMarketList = Arrays.asList("FT:ML");
    private List<String> tierSevenInPlayDisplayMarketList = Arrays.asList("FT:ML", "P:ML");

    /*
     * Jin group markets as game markets, set markets, game winner, set winner, set score, set total games
     * 
     * For doubles trading rule use
     */


    public BsTennisTradingRules() {
        super();

        BsTennisTradingRule allTierRule = new BsTennisTradingRule("BS Tennis All Tier trading rules", null, null);
        allTierRule.setNoPFSuspendMarketList(noPFSuspendMarketList);
        allTierRule.setBreakPointSuspendMarketList(breakPointSuspendMarketList);
        allTierRule.setTieBreakSuspendMarketList(tieBreakSuspendMarketList);
        allTierRule.setCurrentPointSuspendMarketList(currentPointSuspendMarketList);
        allTierRule.setFinalSetTiebreakSuspendList(finalSetTieBreakSuspendMarketList);

        Map<String, List<String>> inplaySequenceIdFiltersMapAllTiers = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapAllTiers.put("G:PW", new ArrayList<String>(Arrays.asList("S0.0.1")));
        allTierRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapAllTiers);

        Map<String, List<String>> inplaySequenceIdFiltersMapForBreakPointAllTiers = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapForBreakPointAllTiers.put("G:ML", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForBreakPointAllTiers.put("G:CS", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForBreakPointAllTiers.put("P:CS", new ArrayList<String>(Arrays.asList("S1")));
        inplaySequenceIdFiltersMapForBreakPointAllTiers.put("P:ML", new ArrayList<String>(Arrays.asList("S1")));
        allTierRule.setInplaySequenceIdFilterListForBreakPoint(inplaySequenceIdFiltersMapForBreakPointAllTiers);

        Map<String, List<String>> inplaySequenceIdFiltersMapForTiebreakAllTiers = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapForTiebreakAllTiers.put("G:ML", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForTiebreakAllTiers.put("G:CS", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForTiebreakAllTiers.put("P:ML", new ArrayList<String>(Arrays.asList("S1")));
        inplaySequenceIdFiltersMapForTiebreakAllTiers.put("P:CS", new ArrayList<String>(Arrays.asList("S1")));
        allTierRule.setInplaySequenceIdFilterListForTiebreak(inplaySequenceIdFiltersMapForTiebreakAllTiers);

        Map<String, List<String>> inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers =
                        new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers.put("G:ML", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers.put("G:CS", new ArrayList<String>(Arrays.asList("S0.1")));
        inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers.put("P:ML", new ArrayList<String>(Arrays.asList("S1")));
        inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers.put("P:CS", new ArrayList<String>(Arrays.asList("S1")));
        allTierRule.setInplaySequenceIdFilterListForFinalSetTiebreak(
                        inplaySequenceIdFiltersMapForFinalSetTiebreakAllTiers);

        Map<String, List<String>> inplaySequenceIdFiltersMapAllTiersUncertainSet = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapAllTiersUncertainSet.put("P:ML", new ArrayList<String>(Arrays.asList("S0")));
        inplaySequenceIdFiltersMapAllTiersUncertainSet.put("P:CS", new ArrayList<String>(Arrays.asList("S0")));
        allTierRule.setInplaySequenceIdFilterListUncertainSet(inplaySequenceIdFiltersMapAllTiersUncertainSet);

        allTierRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(allTierRule);

        BsTennisTradingRule tierOneRule = new BsTennisTradingRule("BS Tennis Tier 1 trading rules", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneInPlayDisplayMarketList);
        tierOneRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(tierOneRule);

        BsTennisTradingRule tierTwoRule = new BsTennisTradingRule("BS Tennis Tier 2 trading rules", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoInPlayDisplayMarketList);
        tierTwoRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(tierTwoRule);

        BsTennisTradingRule tierThreeRule = new BsTennisTradingRule("BS Tennis Tier 3 trading rules", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeInPlayDisplayMarketList);
        tierThreeRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(tierThreeRule);

        BsTennisTradingRule tierFourRule = new BsTennisTradingRule("BS Tennis Tier 4 trading rules", 4, null);
        tierFourRule.setPrematchDisplayMarketList(tierFourPreMatchOpenMarketList);
        tierFourRule.setInplayDisplayMarketList(tierFourInPlayDisplayMarketList);
        tierFourRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(tierFourRule);

        BsTennisTradingRule tierFiveRule = new BsTennisTradingRule("BS Tennis Tier 5 trading rules", 5, null);
        tierFiveRule.setPrematchDisplayMarketList(tierFivePreMatchOpenMarketList);
        tierFiveRule.setInplayDisplayMarketList(tierFiveInPlayDisplayMarketList);
        tierFiveRule.setInPlayRequiresSuccessfulParamFind(true);

        Map<String, List<String>> preMatchSequenceIdFiltersMapTierFive = new HashMap<String, List<String>>();
        preMatchSequenceIdFiltersMapTierFive.put("P:ML", new ArrayList<String>(Arrays.asList("S0")));
        tierFiveRule.setPreMatchSequenceIdFilterList(preMatchSequenceIdFiltersMapTierFive);

        addRule(tierFiveRule);

        BsTennisTradingRule tierSixRule = new BsTennisTradingRule("BS Tennis Tier 6 trading rules", 6, null);
        tierSixRule.setPrematchDisplayMarketList(tierSixPreMatchOpenMarketList);
        tierSixRule.setInplayDisplayMarketList(tierSixInPlayDisplayMarketList);
        tierSixRule.setInPlayRequiresSuccessfulParamFind(true);

        addRule(tierSixRule);

        BsTennisTradingRule tierSevenRule = new BsTennisTradingRule("BS Tennis Tier 7 trading rules", 7, null);
        tierSevenRule.setPrematchDisplayMarketList(tierSevenPreMatchOpenMarketList);
        tierSevenRule.setInplayDisplayMarketList(tierSevenInPlayDisplayMarketList);
        tierSevenRule.setInPlayRequiresSuccessfulParamFind(true);

        Map<String, List<String>> inplaySequenceIdFiltersMapTierSeven = new HashMap<String, List<String>>();
        inplaySequenceIdFiltersMapTierSeven.put("P:ML", new ArrayList<String>(Arrays.asList("S0")));
        tierSevenRule.setInplaySequenceIdFilterList(inplaySequenceIdFiltersMapTierSeven);

        addRule(tierSevenRule);

        ResetBiasTradingRule resetBiasTradingRule = new ResetBiasTradingRule(true);
        addRule(resetBiasTradingRule);

        BsTennisTriggerParamFindTradingRule betStarsTriggerParamFindTradingRule =
                        new BsTennisTriggerParamFindTradingRule();
        betStarsTriggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML", 1);
        betStarsTriggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML", 2);
        betStarsTriggerParamFindTradingRule.addMarketTypeRequiredInPlay("G:ML", 2);
        betStarsTriggerParamFindTradingRule.addMarketTypeToFilterOutPreMatch("G:ML");
        betStarsTriggerParamFindTradingRule.addMarketTypeToFilterOutInPlay("FT:SPRD");
        betStarsTriggerParamFindTradingRule.addMarketTypeToFilterOutInPlay("FT:OU");
        betStarsTriggerParamFindTradingRule.addMarketWeight("FT:ML", 4.0);
        betStarsTriggerParamFindTradingRule.addMarketWeight("G:ML", 4.0);
        betStarsTriggerParamFindTradingRule.addMarketWeight("FT:OU", 4.0);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Pinnacle", 1);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Unibet", 2);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("BetVictor", 3);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("PaddyPower", 4);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("WilliamHill", 5);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("MarathonBet", 6);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Bwin", 7);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("1XBet", 8);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Ladbrokes", 9);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Unibet", 1);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("BetVictor", 2);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("PaddyPower", 3);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("WilliamHill", 4);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("MarathonBet", 5);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Bwin", 6);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("1XBet", 7);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Ladbrokes", 8);
        betStarsTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Pinnacle", 9);
        betStarsTriggerParamFindTradingRule.addMarketTypeGroupsRequiredPreMatch("FT:ML");
        betStarsTriggerParamFindTradingRule.addMarketTypeGroupsRequiredInPlay("FT:ML,G:ML");
        betStarsTriggerParamFindTradingRule.setNumberOfBookiesRequiredPrematch(1);
        betStarsTriggerParamFindTradingRule.setNumberOfBookiesRequiredInplay(2);
        betStarsTriggerParamFindTradingRule.setMaxNumberOfBookiesWantedPrematch(2);
        betStarsTriggerParamFindTradingRule.setMaxNumberOfBookiesWantedInplay(3);

        addRule(betStarsTriggerParamFindTradingRule);

        BsTennisPropertyChangesTradingRule betStarsTennisPropertyChangesTradingRule =
                        new BsTennisPropertyChangesTradingRule();
        addRule(betStarsTennisPropertyChangesTradingRule);

    }
}
