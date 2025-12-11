package ats.algo.sport.badminton.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class BadmintonTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> gamePointSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("P:OU", "P:OE", "P:PW", "P:A:OU", "P:B:OU", "P:ET", "P:SPRD"));
    private List<String> finalPointSuspendMarketList = new ArrayList<String>(Arrays.asList("P:OU", "P:OE", "P:WM",
                    "P:A:OU", "P:B:OU", "P:CS", "P:ML", "P:PW", "P:SPRD", "P:ET"));
    private List<String> raceSuspendMarketList = new ArrayList<String>(Arrays.asList("P:RACE"));
    private List<String> leadSuspendMarketList = new ArrayList<String>(Arrays.asList("P:LEAD"));
    private List<String> nextGameSuspendMarketList = new ArrayList<String>(Arrays.asList("P:ML", "P:OU", "P:OE", "P:PW",
                    "P:A:OU", "P:B:OU", "P:ET", "P:SPRD", "P:RACE", "P:LEAD", "P:WM", "P:CS"));

    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:OE");

    private List<String> tierOneInplayOpenMarketList = Arrays.asList("FT:ML", "FT:OE");

    private List<String> tierTwoPreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:CS", "FT:OU", "P:SPRD");

    private List<String> tierTwoInplayOpenMarketList = Arrays.asList("FT:ML", "FT:CS", "FT:OU", "P:SPRD");

    private List<String> marketListForOverrideProbability = Arrays.asList("FT:ML");

    private List<String> pointTotalDisplayMarketList = Arrays.asList("FT:ML");

    private List<String> secondGameSuspendMarketList = Arrays.asList("FT:OU", "FT:A:OU", "FT:B:OU");

    Double[] rangeOfProbs = {0.02, 0.98};


    public BadmintonTradingRules() {
        super();
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));
        for (int tier = 1; tier <= 5; tier++) {
            BadmintonTradingRule rule = new BadmintonTradingRule("Default Badminton trading rules", tier, null);
            rule.setGamePointSuspendMarketList(gamePointSuspendMarketList);
            rule.setFinalPointSuspendMarketList(finalPointSuspendMarketList);
            rule.setRaceSuspendMarketList(raceSuspendMarketList);
            rule.setLeadSuspendMarketList(leadSuspendMarketList);
            rule.setNextGameSuspendMarketList(nextGameSuspendMarketList);
            rule.setPointTotalDisplayMarketList(pointTotalDisplayMarketList);
            rule.setPointTotalForSuspensionOfMarket(18);
            rule.setStandardMinProbabilityForSuspension(0.035);
            rule.setStandardMaxProbabilityForSuspension(0.965);
            rule.setSecondGameSuspendMarketList(secondGameSuspendMarketList);
            for (String string : marketListForOverrideProbability) {
                rule.addOverrideProbabilitySuspensionList(string, rangeOfProbs);
            }
            addRule(rule);
        }

        BadmintonTradingRule tierOneRule =
                        new BadmintonTradingRule("Badminton trading rule for suspension - tier 1", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneInplayOpenMarketList);
        addRule(tierOneRule);

        BadmintonTradingRule tierTwoRule =
                        new BadmintonTradingRule("Badminton trading rule for suspension - tier 2", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoInplayOpenMarketList);
        addRule(tierTwoRule);

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Badminton TriggerParamFindTradingRule 2", null);
        // triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("G:PW,FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML", 1);
        // triggerParamFindTradingRule.addMarketTypeRequiredInPlay("G:PW,FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML", 1);
        addRule(triggerParamFindTradingRule);

        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(600, 600);
        addRule(monitorFeedTradingRule);

    }
}
