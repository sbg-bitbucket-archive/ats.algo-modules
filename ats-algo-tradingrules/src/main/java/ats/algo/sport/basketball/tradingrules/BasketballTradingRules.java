package ats.algo.sport.basketball.tradingrules;

import static ats.algo.sport.basketball.tradingrules.BasketballTradingRule.ALL_MARKET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class BasketballTradingRules extends TradingRules {


    private List<String> tierOnePrematchDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));
    private List<String> tierOneInplayDisplayMarketList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    private List<String> tierOneFinalFiveMinuteMatchSuspendList =
                    new ArrayList<String>(Arrays.asList("P:OU", "P:A:OU", "P:B:OU", "FT:OU", "FT:A:OU", "FT:B:OU"));
    private List<String> tierOneFinalTwoMinuteMatchSuspendList =
                    new ArrayList<String>(Arrays.asList("P:SPRD", "P:AXB", "FT:SPRD"));


    private List<String> tierTwoPrematchDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> tierTwoInplayDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> tierTwoFinalFiveMinuteMatchSuspendList =
                    new ArrayList<String>(Arrays.asList("P:OU", "P:A:OU", "P:B:OU", "FT:OU", "FT:A:OU", "FT:B:OU"));
    private List<String> tierTwoFinalTwoMinuteMatchSuspendList =
                    new ArrayList<String>(Arrays.asList("P:SPRD", "P:AXB", "FT:SPRD"));


    private List<String> tierThreeprematchDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> tierThreeinplayDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:OU", "FT:SPRD"));
    private List<String> tierThreeFinalFiveMinuteMatchSuspendList = new ArrayList<String>(Arrays.asList("FT:OU"));
    private List<String> tierThreeFinalTwoMinuteMatchSuspendList = new ArrayList<String>(Arrays.asList("FT:SPRD"));

    private int halfTimeEnd = 24;
    private int fullTimeEnd = 48;
    private int minuteSusspend = 2;
    private int[] min = {5, 10, 15};

    public BasketballTradingRules() {
        super();

        /*
         * add derived market rules to generate 3 lines each side of the balanced line at intervals of 0.5 for event
         * tiers 1,2 for FT:OU and FT:SPRD
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);
        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(1, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:SPRD", derivedMarketSpec));

        /*
         * add a line rule to display 2 markets each side of balanced line when in play
         */
        BasketballTradingRule basketballTradingRuleTierOne =
                        new BasketballTradingRule("Basketball trading rule", 1, null);
        basketballTradingRuleTierOne.setPrematchDisplayMarketList(tierOnePrematchDisplayMarketList);
        basketballTradingRuleTierOne.setInplayDisplayMarketList(tierOneInplayDisplayMarketList);
        basketballTradingRuleTierOne.setFinalFiveMinuteMatchSuspendList(tierOneFinalFiveMinuteMatchSuspendList);
        basketballTradingRuleTierOne.setFinalTwoMinuteMatchSuspendList(tierOneFinalTwoMinuteMatchSuspendList);
        basketballTradingRuleTierOne.setMinuteSusspend(minuteSusspend);
        basketballTradingRuleTierOne.setHalfTimeEnd(halfTimeEnd);
        basketballTradingRuleTierOne.setFullTimeEnd(fullTimeEnd);
        basketballTradingRuleTierOne.setMin(min);
        addRule(basketballTradingRuleTierOne);

        BasketballTradingRule basketballTradingRuleTierTwo =
                        new BasketballTradingRule("Basketball trading rule", 2, null);
        basketballTradingRuleTierTwo.setPrematchDisplayMarketList(tierTwoPrematchDisplayMarketList);
        basketballTradingRuleTierTwo.setInplayDisplayMarketList(tierTwoInplayDisplayMarketList);
        basketballTradingRuleTierTwo.setFinalFiveMinuteMatchSuspendList(tierTwoFinalFiveMinuteMatchSuspendList);
        basketballTradingRuleTierTwo.setFinalTwoMinuteMatchSuspendList(tierTwoFinalTwoMinuteMatchSuspendList);
        basketballTradingRuleTierTwo.setMinuteSusspend(minuteSusspend);
        basketballTradingRuleTierTwo.setHalfTimeEnd(halfTimeEnd);
        basketballTradingRuleTierTwo.setFullTimeEnd(fullTimeEnd);
        basketballTradingRuleTierTwo.setMin(min);
        addRule(basketballTradingRuleTierTwo);

        BasketballTradingRule basketballTradingRuleTierThree =
                        new BasketballTradingRule("Basketball trading rule", 3, null);
        basketballTradingRuleTierThree.setPrematchDisplayMarketList(tierThreeprematchDisplayMarketList);
        basketballTradingRuleTierThree.setInplayDisplayMarketList(tierThreeinplayDisplayMarketList);
        basketballTradingRuleTierThree.setFinalFiveMinuteMatchSuspendList(tierThreeFinalFiveMinuteMatchSuspendList);
        basketballTradingRuleTierThree.setFinalTwoMinuteMatchSuspendList(tierThreeFinalTwoMinuteMatchSuspendList);
        basketballTradingRuleTierThree.setMinuteSusspend(minuteSusspend);
        basketballTradingRuleTierThree.setHalfTimeEnd(halfTimeEnd);
        basketballTradingRuleTierThree.setFullTimeEnd(fullTimeEnd);
        basketballTradingRuleTierThree.setMin(min);
        addRule(basketballTradingRuleTierThree);

        // include spread later
        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Basketball TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:OU, FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:ML,FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FTOT:OU, FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FTOT:ML,FT:ML", 1);
        addRule(triggerParamFindTradingRule);

    }
}
