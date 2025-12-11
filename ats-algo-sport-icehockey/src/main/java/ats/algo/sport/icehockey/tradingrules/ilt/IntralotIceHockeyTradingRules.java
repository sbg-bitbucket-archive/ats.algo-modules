package ats.algo.sport.icehockey.tradingrules.ilt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.icehockey.tradingrules.IceHockeyTradingRule;
import ats.algo.sport.icehockey.tradingrules.IcehockeySuspendToAwaitParamFindTradingRule;

public class IntralotIceHockeyTradingRules extends TradingRules {

    private List<String> allTiersAcceptedMarketList = Arrays.asList("FT:A:CLS", "FT:A:OU", "FT:A:Â£OUR_1", "FT:A:SIAP",
                    "FT:A:WAP", "FT:A:WEP", "FT:AHCP", "FT:ANB", "FT:ATG", "FT:AXB", "FT:AXB$BTS", "FT:AXB$FTSC",
                    "FT:AXB$OU", "FT:B:CLS", "FT:B:OU", "FT:B:Â£OUR_1", "FT:B:SIAP", "FT:B:WAP", "FT:B:WEP", "FT:BTS",
                    "FT:Â£CS_1", "FT:DBLC", "FT:DNB", "FT:3HCP", "FT:EXNG", "FT:FTSC", "FT:HNB", "FT:HSP", "FT:LTS",
                    "FT:ML", "FT:NG", "FT:OE", "FT:OTYN", "FT:OU", "FT:P1$AXB", "FT:WM", "FT:WTTS", "FT:WTWPS",
                    "FT:WWROM", "FTOT:A:CLS", "FTOT:A:OU", "FTOT:A:Â£OUR_1", "FTOT:AHCP", "FTOT:ATG", "FTOT:AXB$BTS",
                    "FTOT:AXB$FTSC", "FTOT:AXB$OU", "FTOT:B:CLS", "FTOT:B:OU", "FTOT:B:Â£OUR_1", "FTOT:EXNG", "FTOT:ML",
                    "FTOT:NG", "FTOT:OE", "FTOT:OU", "FTOT:P1$AXB", "FTOT:SPRD", "FTOT:WM", "FTOT:WTTS", "OT:NG",
                    "OT:PSPRD", "OT:WWROM", "P:AXB", "P:BTS", "P:DBLC", "P:DNB", "P:FTS", "P:LTS", "P:OE", "P:OU",
                    "P:SPRD", "P:WWROP", "PEN:NG");

    public static final String ALL_MARKET = "All_MARKET";
    private List<String> periodOneEiteenMinuteSuspendList =
                    new ArrayList<String>(Arrays.asList("P:BTTS_P1", "P:AXB_P1", "P:OU_P1", "P:CS_P1"));

    private List<String> periodTwoEiteenMinuteSuspendList =
                    new ArrayList<String>(Arrays.asList("P:BTTS_P2", "P:AXB_P2", "P:OU_P2", "P:CS_P2"));

    private List<String> periodThreeSixteenMinuteSuspendList = new ArrayList<String>(Arrays.asList("FT:A:OU",
                    "FTOU:A:OU", "FT:B:OU", "FTOU:B:OU", "FT:SPRD", "FT:BTTS", "FT:DBLC", "FT:DNB", "FT:CS"));

    private List<String> periodThreeEiteenMinuteSuspendList = new ArrayList<String>(Arrays.asList(ALL_MARKET));

    public IntralotIceHockeyTradingRules() {
        super();

        double[] fixedOUlines = {-0.25, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5, -6.5, -7.5, -8.5, 0.25, 0.5, 1.5, 2.5, 3.5,
                4.5, 5.5, 6.5, 7.5, 8.5};
        double minProbBoundaries = 0.05;
        double maxProbBoundaries = 0.95;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);
        addRule(new DerivedMarketTradingRule(null, "FT:SPRD", derivedMarketFixedLineSpec1));

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Intralot Ice Hockey TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:OU,FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FTOT:ML,FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FTOT:ML,FT:ML", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FTOT:OU,FT:OU", 1);
        addRule(triggerParamFindTradingRule);

        IceHockeyTradingRule iceHockeyTradingRule =
                        new IceHockeyTradingRule("Intralot Ice Hockey All Tiers Trading Rule", null, null);

        iceHockeyTradingRule.setPrematchDisplayMarketList(allTiersAcceptedMarketList);
        iceHockeyTradingRule.setInplayDisplayMarketList(allTiersAcceptedMarketList);
        iceHockeyTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);

        iceHockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodOneEiteenMinuteSuspendList,
                        18 * SECONDS_IN_A_MINUTE);
        iceHockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodTwoEiteenMinuteSuspendList,
                        38 * SECONDS_IN_A_MINUTE);
        iceHockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodThreeSixteenMinuteSuspendList,
                        56 * SECONDS_IN_A_MINUTE);
        iceHockeyTradingRule.addInPlaySuspendAtTimeMarketList(periodThreeEiteenMinuteSuspendList,
                        58 * SECONDS_IN_A_MINUTE);

        addRule(iceHockeyTradingRule);
        addRule(new IcehockeySuspendToAwaitParamFindTradingRule());// SUSPEND EVENT WHEN GOAL HAPPENS

    }
}
