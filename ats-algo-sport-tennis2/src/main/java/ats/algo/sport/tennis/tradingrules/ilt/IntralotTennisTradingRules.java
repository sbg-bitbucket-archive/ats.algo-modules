package ats.algo.sport.tennis.tradingrules.ilt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.resetbias.tradingrules.ResetBiasTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.tennis.tradingrules.TennisMonitorFeedDelaySuspensionMethodHelper;
import ats.algo.sport.tennis.tradingrules.TennisTradingRule;

public class IntralotTennisTradingRules extends TradingRules {
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
    private List<String> allTiersAcceptedMarketList = Arrays.asList("FT:A:OU", "FT:AHCP", "FT:B:OU", "FT:CS",
                    "FT:CSFLEX", "FT:DR", "FT:ML", "FT:OE", "FT:SHCP", "FT:STNIL", "FT:OU", "FT:STOTR", "FT:TBIM",
                    "FT:TBIS", "P:A:W1S", "P:B:W1S", "P:GCS", "P:GCSOB", "P:GW", "P:ML", "P:OU", "P:OVEROU", "P:SOE",
                    "P:SPRD", "P:TBP");



    public IntralotTennisTradingRules() {
        super();

        TennisTradingRule allTiersRule = new TennisTradingRule("Intralot Tennis trading rules - all tiers", null, null);
        allTiersRule.setInPlayRequiresSuccessfulParamFind(true);
        allTiersRule.setBreakPointSuspendMarketList(breakPointSuspendMarketList);
        allTiersRule.setTieBreakSuspendMarketList(tieBreakSuspendMarketList);
        allTiersRule.setNoPFSuspendMarketList(noPFSuspendMarketList);
        allTiersRule.setCurrentPointSuspendMarketList(currentPointSuspendMarketList);
        allTiersRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
        allTiersRule.setPrematchDisplayMarketList(allTiersAcceptedMarketList);
        allTiersRule.setInplayDisplayMarketList(allTiersAcceptedMarketList);
        addRule(allTiersRule);


        ResetBiasTradingRule resetBiasTradingRule = new ResetBiasTradingRule(true);
        addRule(resetBiasTradingRule);


        IntralotTennisTriggerParamFindTradingRule tennisTriggerParamFindTradingRule =
                        new IntralotTennisTriggerParamFindTradingRule();
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
