package ats.algo.sport.icehockey.tradingrules;


import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;

public class IceHockeyTradingRules extends TradingRules {

    public IceHockeyTradingRules() {
        super();

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Ice Hockey TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:OU, FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:ML, FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FAKEMARKET", 1);
        addRule(triggerParamFindTradingRule);


        double[] fixedOUlines = {-0.25, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5, -6.5, -7.5, -8.5, 0.25, 0.5, 1.5, 2.5, 3.5,
                4.5, 5.5, 6.5, 7.5, 8.5};
        double minProbBoundaries = 0.05;
        double maxProbBoundaries = 0.95;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);
        for (int i = 1; i < 6; i++)
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", derivedMarketFixedLineSpec1));

    }
}
