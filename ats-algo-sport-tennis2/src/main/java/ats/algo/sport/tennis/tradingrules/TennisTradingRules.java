package ats.algo.sport.tennis.tradingrules;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.resetbias.tradingrules.ResetBiasTradingRule;
import ats.algo.core.tradingrules.TradingRules;

public class TennisTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    // Market list to SU poker generated market

    private List<String> currentPointSuspendMarketList = new ArrayList<String>(Arrays.asList("G:PW"));

    private List<String> noPFSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("G:PW", "G:ML", "G:CS", "G:DEUCE"));

    private List<String> breakPointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("FT:OU", "G:PW", "FT:PW5G", "FT:NUMSET", "FT:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B"));

    private List<String> tieBreakSuspendMarketList = new ArrayList<String>(Arrays.asList("P:ML", "FT:OU", "G:DEUCE",
                    "FT:NUMSET", "FT:CS", "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:TBIM", "FT:W1S:A", "FT:W1S:B"));
    /*
     * Tier 1 markets
     */
    private List<String> tierOnePreMatchOpenMarketList =
                    Arrays.asList("FT:ML", "FT:W1S:A", "FT:W1S:B", "FT:OU", "FT:SPRD", "FT:OU:A", "FT:OU:B", "FT:PW5G",
                                    "FT:CS", "FT:NUMSET", "FT:TBIM", "S:OU", "P:CS", "P:ML");
    private List<String> tierOneinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "P:ML",
                    "FT:W1S:A", "FT:W1S:B", "G:ML", "FT:OU", "FT:SPRD", "FT:OU:A", "FT:OU:B", "G:PW", "FT:PW5G",
                    "FT:CS", "P:CS", "FT:NUMSET", "FT:TBIM", "G:DEUCE", "G:CS", "P:TBML", "P:TBCS", "S:OU"));

    /*
     * Tier 2 markets
     */
    private List<String> tierTwoPreMatchOpenMarketList = Arrays.asList("FT:ML", "P:ML");
    private List<String> tierTwoinPlayDisplayMarketList = new ArrayList<String>(
                    Arrays.asList("FT:ML", "G:PW", "G:ML", "G:DEUCE", "FT:SPRD", "FT:PW5G", "FT:OU", "P:ML"));

    /*
     * Tier 3 markets
     */
    private List<String> tierThreePreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:OU", "FT:CS", "P:ML");
    private List<String> tierThreeinPlayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "G:PW", "G:ML",
                    "G:DEUCE", "FT:SPRD", "FT:PW5G", "P:CS", "FT:CS", "FT:OU", "P:ML"));



    public TennisTradingRules() {
        super();

        TennisTradingRule allTiersRule = new TennisTradingRule("Tennis trading rules - all tiers", null, null);
        allTiersRule.setInPlayRequiresSuccessfulParamFind(true);
        allTiersRule.setBreakPointSuspendMarketList(breakPointSuspendMarketList);
        allTiersRule.setTieBreakSuspendMarketList(tieBreakSuspendMarketList);
        allTiersRule.setNoPFSuspendMarketList(noPFSuspendMarketList);
        allTiersRule.setCurrentPointSuspendMarketList(currentPointSuspendMarketList);
        addRule(allTiersRule);

        TennisTradingRule tierOneRule = new TennisTradingRule("Tennis ATP/WTA trading rules", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneinPlayDisplayMarketList);
        addRule(tierOneRule);

        TennisTradingRule tierTwoRule = new TennisTradingRule("Tennis Challenger trading rules", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoinPlayDisplayMarketList);
        addRule(tierTwoRule);

        TennisTradingRule tierThreeRule = new TennisTradingRule("Tennis Tier 3 trading rules", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeinPlayDisplayMarketList);
        addRule(tierThreeRule);

        ResetBiasTradingRule resetBiasTradingRule = new ResetBiasTradingRule(true);
        addRule(resetBiasTradingRule);


        TennisTriggerParamFindTradingRule tennisTriggerParamFindTradingRule = new TennisTriggerParamFindTradingRule();
        tennisTriggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML", 1);
        tennisTriggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML", 1);
        tennisTriggerParamFindTradingRule.addMarketTypeRequiredInPlay("G:ML", 1);
        tennisTriggerParamFindTradingRule.addMarketTypeToFilterOutPreMatch("G:ML");
        tennisTriggerParamFindTradingRule.addMarketTypeToFilterOutInPlay("FT:SPRD");
        tennisTriggerParamFindTradingRule.addMarketTypeToFilterOutInPlay("FT:OU");
        addRule(tennisTriggerParamFindTradingRule);

        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(240, 0);
        addRule(monitorFeedTradingRule);

        /*
         * Delay the suspension
         */
        TennisMonitorFeedDelaySuspensionMethodHelper suspensionMethod =
                        new TennisMonitorFeedDelaySuspensionMethodHelper.Builder()
                                        .medicalTimeout(0, 10000 * 6 * 60 * 24, true).challenge(0, 30000, false)
                                        .build();

        /*
         * could change 4, 0 parameters if needed
         */
        addRule(new MonitorFeedTradingRule(0, 0,
                        (now, matchState) -> suspensionMethod.monitorFeedTradingRuleSuspensionMethod(now, matchState)));


    }



}
