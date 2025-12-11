package ats.algo.sport.football.ml.tradingrules;

import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.football.tradingrules.FootballTradingRule;

public class MachLearnFootballTradingRules extends TradingRules {

    // private List<String> tierAllPrematchDisplayList = Arrays.asList("FT:AXB",
    // "FT:DBLC", "P:AXB");
    //
    // private List<String> tierAllInplayDisplayList = Arrays.asList("FT:AXB",
    // "FT:DBLC");

    public static final String ALL_MARKET = "All_MARKET";

    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList(ALL_MARKET);

    private List<String> finalMinuteOpenMarketList = Arrays.asList("FT:AXB");

    // private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:AXB",
    // "FT:FTSC", "FT:BTS", "FT:ATG",
    // "FT:CS",
    // "FT:OE", "FT:A:OE", "FT:B:OE", "FT:DBLC", "FT:DNB", "FT:A:NB", "FT:B:NB",
    // "FT:MBTSC", "FT:OU",
    // "FT:A:OU", "FT:B:OU", "FT:WM", "FT:DBLC$BTS", "FT:AXB$OU", "FT:A:CLS",
    // "FT:B:CLS", "FT:3HCP",
    // "FT:SPRD", "FT:AWN", "FT:BWN", "FT:HTFT", "FT:A:BH", "FT:B:BH", "FT:A:EH",
    // "FT:B:EH", "FT:A:TTS2H",
    // "FT:B:TTS2H", "FT:HTTS", "FT:ATTS", "FT:RTG3", "FT:RTG2", "FT:DBLC$OU",
    // "FT:NTS", "FT:LTS",
    // "FT:LAT:H", "FT:LAT:A", "FT:20MG", "FT:30MG", "FT:60MG", "FT:75MG",
    // "FT:15MR", "FT:30MR", "FT:60MR",
    // "FT:75MR", "FT:HSH", "FT:AHCP", "FT:H1G", "FT:A:H1G", "FT:B:H1G",
    // "FT:A:TW23", "FT:B:TW23",
    // "FT:WTTS", "FT:A:MLTG", "FT:B:MLTG", "FT:£OUR_3", "FT:Â£OUR_1", "FT:HTTS2",
    // "FT:HTTS3", "FT:ATTS2",
    // "FT:ATTS3", "FT:1HCOMBO", "FT:CM15", "FT:CM25", "FT:CM35", "FT:WNTN:A",
    // "FT:WNTN:B", "FT:BTTS1HH",
    // "FT:BTNTS1HH", "FT:BTTS2HH", "FT:BTNTS2HH", "FT:BTTS1HX", "FT:BTNTS1HX",
    // "FT:BTTS2HX",
    // "FT:BTNTS2HX", "FT:BTTS1HA", "FT:BTNTS1HA", "FT:BTTS2HA", "FT:BTNTS2HA",
    // "P:AXB", "P:AXB", "P:BTS",
    // "P:BTS", "P:OE", "P:OE", "P:A:OE", "P:A:OE", "P:B:OE", "P:B:OE", "P:DBLC",
    // "P:DBLC", "P:DNB",
    // "P:DNB", "P:OU", "P:OU", "P:A:OU", "P:A:OU", "P:B:OU", "P:B:OU", "P:3HCP",
    // "P:3HCP", "P:DBLC$BTS",
    // "P:DBLC$BTS", "P:AXB$OU", "P:AXB$OU", "P:A:WTN", "P:A:WTN", "P:B:WTN",
    // "P:B:WTN", "P:A£OUR_1",
    // "P:A£OUR_1", "P:A:£OUR_1", "P:A:£OUR_1", "P:B:£OUR_1", "P:B:£OUR_1", "P:HNB",
    // "P:HNB", "P:ANB",
    // "P:ANB", "P:SPRD", "P:SPRD", "FT:COU", "FT:A:COU", "FT:B:COU", "FT:COE",
    // "FT:A:COE", "FT:B:COE",
    // "FT:CHCP", "FT:CEHCP", "FT:CAXB", "P:COU", "P:COU", "P:A:COU", "P:A:COU",
    // "P:B:COU", "P:B:COU",
    // "FT:5MC", "FT:10MC", "FT:15MC", "P:CAXB", "P:CAXB", "FT:ACORNER",
    // "FT:BCORNER", "FT:HWMCR",
    // "FT:CORNEROVER2", "FT:CORNEROVER15", "FT:FC", "FT:RTC", "FT:RTC", "FT:RTC",
    // "FT:RTC", "FT:RTC",
    // "FT:RTC", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW",
    // "FT:RTCW", "FT:TQ",
    // "ET:AXB", "ET:OU", "ET:BTS", "P:DBLCBTS", "P:CEHCP", "FT:£OUR_4", "FT:TOFG",
    // "FT:TOFG:H",
    // "FT:TOFG:A", "FT:B:Â£OUR_2", "FT:A:Â£OUR_2", "FT:CSDS");

    public MachLearnFootballTradingRules() {
        // Tier Rules
        FootballTradingRule tierAllRule =
                        new FootballTradingRule("Football trading rule for suspension - No Tiers", null, null);
        tierAllRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierAllRule.setInplayDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierAllRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierAllRule);

        // Derived Line Rules
        double[] fixedOUlines = {0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5};
        double minProbBoundaries = 0.05;
        double maxProbBoundaries = 0.95;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);
        for (int i = 1; i < 6; i++)
            addRule(new DerivedMarketTradingRule(i, "FT:OU", derivedMarketFixedLineSpec1));

        DerivedMarketSpec preMatchLinesToDisplaySpec1 = DerivedMarketSpec.getDerivedMarketSpecForAsianLines("", "",
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 6, 1,
                        DerivedMarketGapBetweenLines.QUARTER_LINE, minProbBoundaries, maxProbBoundaries);
        for (int i = 1; i < 5; i++)
            addRule(new DerivedMarketTradingRule(i, "FT:AHCP", preMatchLinesToDisplaySpec1));

        for (int i = 1; i < 5; i++)
            addRule(new DerivedMarketTradingRule(i, "FT:ATG", preMatchLinesToDisplaySpec1));

        // ParamFind Logic
        TriggerParamFindTradingRule paramFindTradingRule =
                        new TriggerParamFindTradingRule("Football Mach Learn TriggerParamFindTradingRule", null);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:AXB", 1);
        paramFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        paramFindTradingRule.addMarketTypeRequiredInPlay("FT:FAKEMARKET", 1);
        paramFindTradingRule.setSmartParamFindKickoffThreshold(0.01);
        addRule(paramFindTradingRule);

    }

}
