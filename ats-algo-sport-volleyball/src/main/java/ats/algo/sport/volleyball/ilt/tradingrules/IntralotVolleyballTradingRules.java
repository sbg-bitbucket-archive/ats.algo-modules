package ats.algo.sport.volleyball.ilt.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.volleyball.tradingrules.VolleyballTradingRule;

public class IntralotVolleyballTradingRules extends TradingRules {

    private List<String> allTiersAcceptedMarketList = Arrays.asList("FT:4SP", "FT:5SP", "FT:AHCP", "FT:AXB",
                    "FT:CSFLEX", "FT:EXSCL", "FT:ML", "FT:OU", "FT:STOT", "FT:STOTR", "P:AHCP", "P:ML", "P:OE", "P:OU",
                    "P:OVEROE", "P:OVEROU", "P:PS", "P:RX", "P:SPRD");

    private List<String> setPointSuspendMarketList = new ArrayList<String>(Arrays.asList("P:PW"));
    private List<String> finalSetSuspendMarketList = new ArrayList<String>(Arrays.asList("P:ML", "FT:SPRD", "FT:CS",
                    "FT:OU:S", "FT:A:OU", "FT:B:OU", "FT:OE", "FT:OU", "FT:PSPRD", "P:CS"));
    private List<String> finalPointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("FT:SPRD", "FT:CS", "P:CS2", "P:CS3", "P:CS4", "FT:OU:S", "FT:ML"));
    private List<String> totalPointSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("P:OU", "FT:A:OU", "FT:B:OU", "FT:OU", "FT:OE", "P:OE", "P:EP", "P:WM", "FT:PSPRD"));
    private List<String> handicapPointSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("P:SPRD", "FT:SPRD", "P:ML", "P:OU", "P:CS"));
    private List<String> raceSuspendMarketList = new ArrayList<String>(Arrays.asList("P:RACE"));
    private List<String> leadSuspendMarketList = new ArrayList<String>(Arrays.asList("P:LEAD"));
    private List<String> prematchDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:W1H", "FT:W1A", "FT:WH", "FT:WA"));
    private List<String> ExtraPointSuspendMarketList = new ArrayList<String>(Arrays.asList("P:EP", "P:OE"));
    private List<String> secondSetSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("FT:OU", "FT:A:OU", "FT:B:OU", "FT:PSPRD", "FT:OE"));


    public IntralotVolleyballTradingRules() {
        super();
        VolleyballTradingRule volleyballTradingRule =
                        new VolleyballTradingRule("Intralot Volleyball trading rules", null, null);
        volleyballTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
        volleyballTradingRule.setSetPointSuspendMarketList(setPointSuspendMarketList);
        volleyballTradingRule.setFinalPointSuspendMarketList(finalPointSuspendMarketList);
        volleyballTradingRule.setFinalSetSuspendMarketList(finalSetSuspendMarketList);
        volleyballTradingRule.setHandicapPointSuspendMarketList(handicapPointSuspendMarketList);
        volleyballTradingRule.setTotalPointSuspendMarketList(totalPointSuspendMarketList);
        volleyballTradingRule.setRaceSuspendMarketList(raceSuspendMarketList);
        volleyballTradingRule.setLeadSuspendMarketList(leadSuspendMarketList);
        volleyballTradingRule.setPrematchDisplayMarketList(prematchDisplayMarketList);
        volleyballTradingRule.setThreeSetSuspendMarketList(ExtraPointSuspendMarketList);
        volleyballTradingRule.setSecondSetSuspendMarketList(secondSetSuspendMarketList);
        addRule(volleyballTradingRule);

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Intralot Volleyball TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML,FT:SPRD", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML,FT:SPRD", 1);
        addRule(triggerParamFindTradingRule);

    }
}
