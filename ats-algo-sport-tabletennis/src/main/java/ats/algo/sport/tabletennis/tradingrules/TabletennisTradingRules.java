package ats.algo.sport.tabletennis.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class TabletennisTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> currentPointSuspendMarketList = new ArrayList<String>(Arrays.asList("P:RACE"));
    private List<String> finalGamePoint8OpenMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "FT:OE"));

    public TabletennisTradingRules() {
        super();

        /*
         * display one line around the balanced line for each of these markets
         */
        DerivedMarketSpec derivedMarketSpec = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 1, 1);
        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:A:OU", derivedMarketSpec));
        addRule(new DerivedMarketTradingRule(3, "FT:B:OU", derivedMarketSpec));


        TabletennisTradingRule allTiersRule =
                        new TabletennisTradingRule("Tabletennis trading rules - all tiers", null, null);
        allTiersRule.setGamePointsSuspendMarketList(currentPointSuspendMarketList);
        allTiersRule.setFinalGamePoint8OpenMarketList(finalGamePoint8OpenMarketList);
        addRule(allTiersRule);


        TabletennisTradingRule tierOneRule = new TabletennisTradingRule("Default Table tennis trading rules", 1, null);
        tierOneRule.setFinalGamePoint8OpenMarketList(finalGamePoint8OpenMarketList);
        addRule(tierOneRule);

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Table Tennis TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:ML,FT:SPRD", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:ML,FT:SPRD", 1);
        addRule(triggerParamFindTradingRule);
    }
}
