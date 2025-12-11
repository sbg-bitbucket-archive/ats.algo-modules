package ats.algo.sport.dart.tradingrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ats.algo.core.tradingrules.TradingRules;

public class DartTradingRules extends TradingRules {
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> tierTwoprematchDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB",
                    "P:SPRD", "FT:M180", "FT:CS", "P:CS", "G:CS", "FT:OU180", "G:SPRD", "FT:SPRD"));

    private List<String> tierTwoinplayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB", "P:GW",
                    "G:ML", "P:ML", "P:SPRD", "FT:M180", "P:CS", "G:CS", "FT:OU180", "G:SPRD", "FT:SPRD", "FT:CS"));

    private List<String> tierThreeprematchDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB", "P:SPRD", "FT:CS", "P:CS", "G:CS", "G:SPRD",
                                    "FT:SPRD", "FT:9DFN", "FT:170CHK", "FT:OUS", "FT:A:OUS", "FT:B:OUS"));

    private List<String> tierThreeinplayDisplayMarketList = new ArrayList<String>(
                    Arrays.asList("FT:ML", "FT:AXB", "P:GW", "G:ML", "P:ML", "P:SPRD", "FT:CS", "P:CS", "G:CS",
                                    "G:SPRD", "FT:SPRD", "FT:9DFN", "FT:170CHK", "FT:OUS", "FT:A:OUS", "FT:B:OUS"));

    private List<String> tierFourprematchDisplayMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB", "FT:CS", "P:CS", "G:CS"));

    private List<String> tierFourinplayDisplayMarketList = new ArrayList<String>(
                    Arrays.asList("FT:ML", "FT:AXB", "P:GW", "G:ML", "P:ML", "FT:CS", "P:CS", "G:CS", "G:SPRD"));

    private List<String> tierOneprematchDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB",
                    "FT:9DFN", "FT:170CHK", "FT:OUCHK", "FT:A:OUCHK", "FT:B:OUCHK", "P:SPRD", "FT:A:HTRK", "FT:B:HTRK",
                    "FT:M180", "FT:CS", "P:CS", "G:CS", "FT:OU180", "FT:A:OU180", "FT:B:OU180", "FT:OUS", "FT:A:OUS",
                    "FT:B:OUS", "G:SPRD", "FT:OE", "FT:SPRD"));

    private List<String> tierOneinplayDisplayMarketList = new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB", "P:GW",
                    "G:1180", "G:CHKC", "G:OUCHK", "G:OU180", "G:ML", "FT:OUCHK", "FT:A:OUCHK", "FT:B:OUCHK", "P:SPRD",
                    "FT:M180", "FT:N180", "P:CS", "G:CS", "FT:OU180", "FT:A:OU180", "FT:B:OU180", "FT:OUS", "FT:A:OUS",
                    "FT:B:OUS", "G:SPRD", " FT:OE", "P:OE", "P:OE180", "P:OU", "P:ML", "FT:SPRD", "FT:CS"));

    private List<String> less250LegSuspendMarketList = new ArrayList<String>(Arrays.asList("G:ML"));
    private List<String> matchSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("FT:ML", "FT:AXB", "FT:SPRD", "FT:OE"));
    private List<String> matchEndSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("FT:CS", "FT:OUS", "FT:A:OUS", "FT:B:OUS", "FT:OE"));
    private List<String> firstDartSuspendMarketList = new ArrayList<String>(
                    Arrays.asList("G:CHKC", "G:OUCHK40", "G:CHKC", "G:1180", "G:OUCHK", "G:OU180"));
    private List<String> less250SetSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("P:CS", "P:SPRD", "P:OU", "P:OE", "P:ML"));
    private List<String> all180MarketSuspendMarketList =
                    new ArrayList<String>(Arrays.asList("FT:M180", "FT:OU180", "FT:A:OU180", "FT:B:OU180"));
    private List<String> twoLeg180MarketSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:M180", "FT:OU180"));
    private List<String> oneLeg180AMarketSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:A:OU180"));
    private List<String> oneLeg180BMarketSuspendMarketList = new ArrayList<String>(Arrays.asList("FT:B:OU180"));

    public DartTradingRules() {
        super();

        DartTradingRule tierOneRule = new DartTradingRule("Tier One Dart trading rule ", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOneprematchDisplayMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneinplayDisplayMarketList);
        tierOneRule.setLess250LegSuspendMarketList(less250LegSuspendMarketList);
        tierOneRule.setMatchSuspendMarketList(matchSuspendMarketList);
        tierOneRule.setFirstDartSuspendMarketList(firstDartSuspendMarketList);
        tierOneRule.setLess250SetSuspendMarketList(less250SetSuspendMarketList);
        tierOneRule.setMatchEndSuspendMarketList(matchEndSuspendMarketList);
        tierOneRule.setAll180MarketSuspendMarketList(all180MarketSuspendMarketList);
        tierOneRule.setOneLeg180AMarketSuspendMarketList(oneLeg180AMarketSuspendMarketList);
        tierOneRule.setOneLeg180BMarketSuspendMarketList(oneLeg180BMarketSuspendMarketList);
        tierOneRule.setTwoLeg180MarketSuspendMarketList(twoLeg180MarketSuspendMarketList);
        addRule(tierOneRule);
        DartTradingRule tierTwoRule = new DartTradingRule("Tier Two Dart trading rule ", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoprematchDisplayMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoinplayDisplayMarketList);
        tierTwoRule.setLess250LegSuspendMarketList(less250LegSuspendMarketList);
        tierTwoRule.setMatchSuspendMarketList(matchSuspendMarketList);
        tierTwoRule.setFirstDartSuspendMarketList(firstDartSuspendMarketList);
        tierTwoRule.setLess250SetSuspendMarketList(less250SetSuspendMarketList);
        tierTwoRule.setMatchEndSuspendMarketList(matchEndSuspendMarketList);
        tierTwoRule.setAll180MarketSuspendMarketList(all180MarketSuspendMarketList);
        tierTwoRule.setOneLeg180AMarketSuspendMarketList(oneLeg180AMarketSuspendMarketList);
        tierTwoRule.setOneLeg180BMarketSuspendMarketList(oneLeg180BMarketSuspendMarketList);
        tierTwoRule.setTwoLeg180MarketSuspendMarketList(twoLeg180MarketSuspendMarketList);
        addRule(tierTwoRule);
        DartTradingRule tierThreeRule = new DartTradingRule("Tier Three Dart trading rule ", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreeprematchDisplayMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeinplayDisplayMarketList);
        tierThreeRule.setLess250LegSuspendMarketList(less250LegSuspendMarketList);
        tierThreeRule.setMatchSuspendMarketList(matchSuspendMarketList);
        tierThreeRule.setFirstDartSuspendMarketList(firstDartSuspendMarketList);
        tierThreeRule.setLess250SetSuspendMarketList(less250SetSuspendMarketList);
        tierThreeRule.setMatchEndSuspendMarketList(matchEndSuspendMarketList);
        tierThreeRule.setAll180MarketSuspendMarketList(all180MarketSuspendMarketList);
        tierThreeRule.setOneLeg180AMarketSuspendMarketList(oneLeg180AMarketSuspendMarketList);
        tierThreeRule.setOneLeg180BMarketSuspendMarketList(oneLeg180BMarketSuspendMarketList);
        tierThreeRule.setTwoLeg180MarketSuspendMarketList(twoLeg180MarketSuspendMarketList);
        addRule(tierThreeRule);
        DartTradingRule tierFourRule = new DartTradingRule("Tier Four Dart trading rule ", 4, null);
        tierFourRule.setPrematchDisplayMarketList(tierFourprematchDisplayMarketList);
        tierFourRule.setInplayDisplayMarketList(tierFourinplayDisplayMarketList);
        tierFourRule.setLess250LegSuspendMarketList(less250LegSuspendMarketList);
        tierFourRule.setMatchSuspendMarketList(matchSuspendMarketList);
        tierFourRule.setFirstDartSuspendMarketList(firstDartSuspendMarketList);
        tierFourRule.setLess250SetSuspendMarketList(less250SetSuspendMarketList);
        tierFourRule.setMatchEndSuspendMarketList(matchEndSuspendMarketList);
        tierFourRule.setAll180MarketSuspendMarketList(all180MarketSuspendMarketList);
        tierFourRule.setOneLeg180AMarketSuspendMarketList(oneLeg180AMarketSuspendMarketList);
        tierFourRule.setOneLeg180BMarketSuspendMarketList(oneLeg180BMarketSuspendMarketList);
        tierFourRule.setTwoLeg180MarketSuspendMarketList(twoLeg180MarketSuspendMarketList);
        addRule(tierFourRule);
    }
}
