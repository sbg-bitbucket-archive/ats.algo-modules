package ats.algo.sport.americanfootball.tradingrules;

import static ats.algo.sport.americanfootball.tradingrules.AmericanFootballTradingRule.ALL_MARKET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class AmericanFootballTradingRules extends TradingRules {
    private List<String> prematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> inplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    private List<String> prematchDisplayMarketListTier2 =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> inplayDisplayMarketListTier2 =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));

    private List<String> prematchDisplayMarketListTier3 =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> inplayDisplayMarketListTier3 =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));


    private List<String> fullTimeSuspendMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> fiveMinuteSuspendMarketList = new ArrayList<String>(Arrays.asList(""));
    private List<String> tenMinuteSuspendMarketList = new ArrayList<String>(Arrays.asList(""));
    private List<String> fifteenMinuteSuspendMarketList = new ArrayList<String>(Arrays.asList(""));

    private int halfTimeEnd = 30;
    private int fullTimeEnd = 60;
    private int minuteSusspend = 2;
    private int[] min = {5, 10, 15};

    public AmericanFootballTradingRules() {
        super();

        /*
         * add derived market rules to generate 3 lines each side of the balanced line at intervals of 0.5 for
         * eventtiers 1,2 for FT:OU and FT:SPRD
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);
        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec));



        AmericanFootballTradingRule americanfootballTradingRule =
                        new AmericanFootballTradingRule("American Football trading rule 1", 1, null, null);
        americanfootballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        americanfootballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketList);
        americanfootballTradingRule.setMinuteSusspend(minuteSusspend);
        americanfootballTradingRule.setHalfTimeEnd(halfTimeEnd);
        americanfootballTradingRule.setFullTimeEnd(fullTimeEnd);
        americanfootballTradingRule.setFiveMinuteSuspendMarketList(fiveMinuteSuspendMarketList);
        americanfootballTradingRule.setTenMinuteSuspendMarketList(tenMinuteSuspendMarketList);
        americanfootballTradingRule.setFifteenMinuteSuspendMarketList(fifteenMinuteSuspendMarketList);
        americanfootballTradingRule.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        americanfootballTradingRule.setMin(min);
        addRule(americanfootballTradingRule);

        AmericanFootballTradingRule americanfootballTradingRule2 =
                        new AmericanFootballTradingRule("American Football trading rule Tier 2", 2, null, null);
        americanfootballTradingRule2.setPrematchDisplayMarketList(prematchDisplayMarketListTier2);
        americanfootballTradingRule2.setInplayDisplayMarketList(inplayDisplayMarketListTier2);
        americanfootballTradingRule2.setMinuteSusspend(minuteSusspend);
        americanfootballTradingRule2.setHalfTimeEnd(halfTimeEnd);
        americanfootballTradingRule2.setFullTimeEnd(fullTimeEnd);
        americanfootballTradingRule2.setFiveMinuteSuspendMarketList(fiveMinuteSuspendMarketList);
        americanfootballTradingRule2.setTenMinuteSuspendMarketList(tenMinuteSuspendMarketList);
        americanfootballTradingRule2.setFifteenMinuteSuspendMarketList(fifteenMinuteSuspendMarketList);
        americanfootballTradingRule2.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        americanfootballTradingRule2.setMin(min);
        addRule(americanfootballTradingRule2);

        AmericanFootballTradingRule americanfootballTradingRule3 =
                        new AmericanFootballTradingRule("American Football trading rule Tier 3", 3, null, null);
        americanfootballTradingRule3.setPrematchDisplayMarketList(prematchDisplayMarketListTier3);
        americanfootballTradingRule.setInplayDisplayMarketList(inplayDisplayMarketListTier3);
        americanfootballTradingRule3.setMinuteSusspend(minuteSusspend);
        americanfootballTradingRule3.setHalfTimeEnd(halfTimeEnd);
        americanfootballTradingRule3.setFullTimeEnd(fullTimeEnd);
        americanfootballTradingRule3.setFiveMinuteSuspendMarketList(fiveMinuteSuspendMarketList);
        americanfootballTradingRule3.setTenMinuteSuspendMarketList(tenMinuteSuspendMarketList);
        americanfootballTradingRule3.setFifteenMinuteSuspendMarketList(fifteenMinuteSuspendMarketList);
        americanfootballTradingRule3.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        americanfootballTradingRule3.setMin(min);
        addRule(americanfootballTradingRule3);

        AmericanFootballTradingRule americanfootballTradingRule4 =
                        new AmericanFootballTradingRule("American Football trading rule Tier 4", 4, null, null);
        americanfootballTradingRule4.setPrematchDisplayMarketList(prematchDisplayMarketListTier3);
        americanfootballTradingRule4.setInplayDisplayMarketList(inplayDisplayMarketListTier3);
        americanfootballTradingRule4.setMinuteSusspend(minuteSusspend);
        americanfootballTradingRule4.setHalfTimeEnd(halfTimeEnd);
        americanfootballTradingRule4.setFullTimeEnd(fullTimeEnd);
        americanfootballTradingRule4.setFiveMinuteSuspendMarketList(fiveMinuteSuspendMarketList);
        americanfootballTradingRule4.setTenMinuteSuspendMarketList(tenMinuteSuspendMarketList);
        americanfootballTradingRule4.setFifteenMinuteSuspendMarketList(fifteenMinuteSuspendMarketList);
        americanfootballTradingRule4.setFullTimeSuspendMarketList(fullTimeSuspendMarketList);
        americanfootballTradingRule4.setMin(min);
        addRule(americanfootballTradingRule4);

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("American Football TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML,FT:SPRD", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML,FT:SPRD", 1);


    }
}
