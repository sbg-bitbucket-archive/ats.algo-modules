package ats.algo.sport.icehockey.tradingrules;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.icehockey.tradingrules.IceHockeyTradingRule;

public class IceHockeyTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> periodOneEiteenMinuteSuspendList =
                    new ArrayList<String>(Arrays.asList("P:BTTS_P1", "P:AXB_P1", "P:OU_P1", "P:CS_P1"));

    private List<String> periodTwoEiteenMinuteSuspendList =
                    new ArrayList<String>(Arrays.asList("P:BTTS_P2", "P:AXB_P2", "P:OU_P2", "P:CS_P2"));

    private List<String> periodThreeSixteenMinuteSuspendList = new ArrayList<String>(Arrays.asList("FT:A:OU",
                    "FTOU:A:OU", "FT:B:OU", "FTOU:B:OU", "FT:SPRD", "FT:BTTS", "FT:DBLC", "FT:DNB", "FT:CS"));

    private List<String> periodThreeEiteenMinuteSuspendList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public IceHockeyTradingRules() {
        super();

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Ice Hockey TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:OU,FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:ML,FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FAKEMARKET", 1);
        addRule(triggerParamFindTradingRule);



        double[] fixedOUlines = {-0.25, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5, -6.5, -7.5, -8.5, 0.25, 0.5, 1.5, 2.5, 3.5,
                4.5, 5.5, 6.5, 7.5, 8.5};
        double minProbBoundaries = 0.05;
        double maxProbBoundaries = 0.95;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);
        for (int i = 1; i < 6; i++) {
            addRule(new DerivedMarketTradingRule(i, "FT:SPRD", derivedMarketFixedLineSpec1));

            IceHockeyTradingRule icehockeyTradingRule = new IceHockeyTradingRule("Icehockey trading rule", i, null);
            icehockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodOneEiteenMinuteSuspendList,
                            18 * SECONDS_IN_A_MINUTE);
            icehockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodTwoEiteenMinuteSuspendList,
                            38 * SECONDS_IN_A_MINUTE);
            icehockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodThreeSixteenMinuteSuspendList,
                            56 * SECONDS_IN_A_MINUTE);
            icehockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodThreeEiteenMinuteSuspendList,
                            58 * SECONDS_IN_A_MINUTE);
            addRule(icehockeyTradingRule);

        }
        addRule(new IcehockeySuspendToAwaitParamFindTradingRule());// SUSPEND EVENT WHEN GOAL HAPPENS

    }
}
