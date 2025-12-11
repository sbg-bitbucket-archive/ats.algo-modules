package ats.algo.sport.football.baba.tradingrules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethod;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.tradingrules.FootballSuspendToAwaitParamFindTradingRule;
import ats.algo.sport.football.tradingrules.FootballTradingRule;

public class BabaFootballTradingRules extends TradingRules {

    private List<String> inplaySuspendMarketList = Arrays.asList("FT:FHSHCS");

    public static final String ALL_MARKET = "All_MARKET";

    // public static final String ALL_MARKET = "All_MARKET";
    private List<String> lineCapMarkets = Arrays.asList("FT:OU");

    private List<String> finalMinuteOpenMarketList = Arrays.asList("FT:AXB");

    private Map<String, Double> priceCapList;
    private Map<String, Double> extraMarginList;


    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:DBLCHTFT", "FT:TWOBTSOU", "FT:BTSOU:3",
                    "FT:BTSOU:4", "FT:CM15", "FT:CM25", "FT:CM35", "FT:CM2", "FT:CM", "FT:AXB", "FT:DBLC", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM",
                    "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B",
                    "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS",
                    "FT:FHSHCS", "P:DBLC", "P:DBLCBTS", "P:DNB", "P:AXBBTS", "P:AXBOU", "FT:FHSHR", "FT:30MR",
                    "FT:60MR", "FT:75MR", "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:HNB",
                    "FT:WNTN:H", "FT:WTN:H", "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB",
                    "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:FGL", "FT:TOFBR", "FT:TOFG", "FT:TOFG:H",
                    "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:BAXB", "FT:BRED", "FT:BCOU", "FT:HWMCD", "FT:WFB:A",
                    "FT:WNTN:A", "FT:A:BYOU", "FT:B:BYOU", "FT:COU", "P:COU", "P:CAXB", "P:A:COU", "P:B:COU", "FT:RTC",
                    "FT:RTC", "FT:DBLCOU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:FC", "FT:HWMCR", "FT:CAXB",
                    "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC", "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU",
                    "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:OUR:A", "FT:ATTS2", "FT:OUR:B",
                    "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS", "FT:HTTS2H", "FT:A:BH", "FT:A:EH", "FT:WFB:H",
                    "FT:B:CLS", "FT:ATTS", "FT:ATTS2H", "FT:B:BH", "FT:B:EH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU",
                    "FT:B:H1G", "FT:A:H1G", "P:DBLCOU", "FT:BTTSOOU", "P:A:TW23", "P:B:TW23", "FT:DBLCOU", "FT:A:TW23",
                    "FT:B:TW23", "FT:DBLCRNG", "FT:ETWBH", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU",
                    "FT:HOF", "FT:AXBBTS", "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS", "FTBTTS2HH",
                    "FTBTTS2HA", "FTBTTS2HX", "FTBTNTS2HH", "FTBTNTS2HA", "FTBTNTS2HX", "FTBTTS1HH", "FTBTTS1HA",
                    "FTBTTS1HX", "FTBTNTS1HH", "FTBTNTS1HA", "FTBTNTS1HX", "FTBTTS1HH", "FTBTTS1HA", "FTBTTS1HX",
                    "FTBTNTS1HH", "FTBTNTS1HA", "FTBTNTS1HX", "FT:2HCOMBO", "FT:1HCOMBO", "FT:CORNEROVER2",
                    "FT:CORNEROVER15", "FT:ACORNER", "FT:BCORNER", "FT:CORNERODDEVEN", "FT:CORNERODDEVENA",
                    "FT:CORNERODDEVENB", "FT:CARDSYELLOWOVER1", "FT:CARDSYELLOWOVER8", "FT:CARDSYESNO",
                    "FT:CARDSODDEVEN", "FT:YCARDSODDEVEN", "P:DBCS", "FT:RTCTW", "FT:HHTHFT2", "FT:AHTAFT2",
                    "FT:HHTHFT3", "FT:AHTAFT3", "FT:HHTHFT4", "FT:AHTAFT4", "FT:HHTHFT2H", "FT:AHTAFT2H", "FT:A:BCOU",
                    "FT:B:BCOU");

    private List<String> tierOneInPlayDisplayMarketList = Arrays.asList("FT:DBLCHTFT", "FT:TWOBTSOU", "FT:BTSOU:3",
                    "FT:BTSOU:4", "FT:CM15", "FT:CM25", "FT:CM35", "FT:CM2", "FT:CM", "FT:AXB", "FT:DBLC", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM",
                    "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B",
                    "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS",
                    "P:DBLC", "P:DBLCBTS", "P:DNB", "P:AXBBTS", "P:AXBOU", "FT:FHSHR", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:HNB", "FT:WNTN:H", "FT:WTN:H",
                    "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR",
                    "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:FGL", "FT:TOFBR", "FT:TOFG", "FT:TOFG:H", "FT:CFBD",
                    "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:BAXB", "FT:BRED", "FT:BCOU", "FT:HWMCD", "FT:WFB:A",
                    "FT:WNTN:A", "FT:A:BYOU ", "FT:B:BYOU", "FT:COU", "P:COU", "P:CAXB", "P:A:COU", "P:B:COU", "FT:RTC",
                    "FT:RTC", "FT:DBLCOU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:FC", "FT:HWMCR", "FT:CAXB",
                    "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC", "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU",
                    "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:OUR:A", "FT:ATTS2", "FT:OUR:B",
                    "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS", "FT:HTTS2H", "FT:A:BH", "FT:A:EH", "FT:WFB:H",
                    "FT:B:CLS", "FT:ATTS", "FT:ATTS2H", "FT:B:BH", "FT:B:EH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU",
                    "FT:B:H1G", "FT:A:H1G", "P:DBLCOU", "FT:BTTSOOU", "P:A:TW23", "P:B:TW23", "FT:DBLCOU", "FT:A:TW23",
                    "FT:B:TW23", "FT:DBLCRNG", "FT:ETWBH", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU",
                    "FT:HOF", "FT:AXBBTS", "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS", "FT:RTCTW", "FT:A:BCOU",
                    "FT:B:BCOU");
    private List<String> tierTwoPreMatchOpenMarketList = Arrays.asList("FT:A:BYOU", "FT:B:BYOU", "P:A:COU", "P:B:COU",
                    "FT:DBLCHTFT", "FT:TWOBTSOU", "FT:BTSOU:3", "FT:BTSOU:4", "FT:CM15", "FT:CM25", "FT:CM35", "FT:CM2",
                    "FT:CM", "FT:AXB", "FT:DBLC", "P:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS",
                    "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE",
                    "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB",
                    "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC", "P:DBLCBTS", "P:DNB",
                    "P:AXBBTS", "FT:FHSHR", "FT:30MR", "FT:60MR", "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS",
                    "FT:A:OE", "FT:HNB", "FT:WNTN:H", "FT:WTN:H", "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG",
                    "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:FGL", "FT:TOFBR",
                    "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:BAXB", "FT:BRED",
                    "FT:HWMCD", "FT:WFB:A", "FT:WNTN:A", "FT:COU", "P:CAXB", "FT:RTC", "FT:DBLCOU", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:TOFCR", "FT:FC", "FT:HWMCR", "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC",
                    "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:OUR:A",
                    "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS2H", "FT:A:BH", "FT:A:EH", "FT:WFB:H",
                    "FT:B:CLS", "FT:ATTS2H", "FT:B:BH", "FT:B:EH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU", "FT:B:H1G",
                    "FT:A:H1G", "P:DBLCOU", "FT:BTTSOOU", "P:A:TW23", "P:B:TW23", "FT:DBLCOU", "FT:A:TW23", "FT:B:TW23",
                    "FT:DBLCRNG", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU", "FT:HOF", "FT:AXBBTS",
                    "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS", "FT:RTCTW", "FT:HHTHFT2", "FT:AHTAFT2",
                    "FT:HHTHFT3", "FT:AHTAFT3", "FT:HHTHFT4", "FT:AHTAFT4", "FT:HHTHFT2H", "FT:AHTAFT2H", "FT:A:BCOU",
                    "FT:B:BCOU");
    private List<String> tierTwoInPlayDisplayMarketList = Arrays.asList("FT:B:BYOU", "FT:A:BYOU", "P:A:COU", "P:B:COU",
                    "FT:DBLCHTFT", "FT:TWOBTSOU", "FT:BTSOU:3", "FT:BTSOU:4", "FT:CM15", "FT:CM25", "FT:CM35", "FT:CM2",
                    "FT:CM", "FT:AXB", "FT:DBLC", "P:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS",
                    "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE",
                    "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB",
                    "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "P:DBLC", "P:DBLCBTS", "P:DNB", "P:AXBBTS",
                    "FT:FHSHR", "FT:30MR", "FT:60MR", "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE",
                    "FT:HNB", "FT:WNTN:H", "FT:WTN:H", "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR",
                    "FT:5MB", "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:FGL", "FT:TOFBR", "FT:TOFG",
                    "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:BAXB", "FT:BRED", "FT:HWMCD",
                    "FT:WFB:A", "FT:WNTN:A", "FT:COU", "P:CAXB", "FT:RTC", "FT:DBLCOU", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:TOFCR", "FT:FC", "FT:HWMCR", "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC", "FT:AXBOU",
                    "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:OUR:A", "FT:OUR:B",
                    "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS2H", "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS",
                    "FT:ATTS2H", "FT:B:BH", "FT:B:EH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU", "FT:B:H1G", "FT:A:H1G",
                    "P:DBLCOU", "FT:BTTSOOU", "P:A:TW23", "P:B:TW23", "FT:DBLCOU", "FT:A:TW23", "FT:B:TW23",
                    "FT:DBLCRNG", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU", "FT:HOF", "FT:AXBBTS",
                    "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS", "FT:AXBTTSOU", "FT:B:H1G", "FT:A:H1G",
                    "P:DBLCOU", "FT:BTTSOOU", "P:A:TW23", "P:B:TW23", "FT:DBLCOU", "FT:A:TW23", "FT:B:TW23",
                    "FT:DBLCRNG", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU", "FT:HOF", "FT:AXBBTS",
                    "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS", "FT:RTCTW", "FT:A:BCOU", "FT:B:BCOU");
    private List<String> tierThreePreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:WNTN:H", "FT:DBLC", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM",
                    "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B",
                    "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS",
                    "FT:FHSHCS", "P:DNB", "FT:FHSHR", "FT:30MR", "FT:AXBBTS", "FT:A:OE", "FT:HNB", "FT:10MG", "FT:10MR",
                    "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR", "FT:FGL", "FT:TOFBR",
                    "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:HWMCD", "FT:WFB:A",
                    "FT:WNTN:A", "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:FC", "FT:5MC", "FT:10MC", "FT:AXBOU",
                    "FT:AXBOU", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS2H", "FT:A:BH", "FT:WFB:H", "FT:B:CLS",
                    "FT:ATTS2H", "FT:B:BH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU", "FT:B:H1G", "FT:A:H1G",
                    "FT:BTTSOOU", "FT:H1G", "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU", "FT:HOF", "FT:AXBBTS",
                    "P:BTSOU", "FT:AXBTTSOU", "FT:DBLCOU", "FT:DBLCBTS");
    private List<String> tierThreeInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:WNTN:H", "FT:DBLC", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:FTS", "FT:BTS", "FT:OE", "FT:TQ", "FT:WM",
                    "FT:AGS", "P:OUR", "FT:HSH", "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B",
                    "P:B:OU", "P:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS",
                    "P:DNB", "FT:FHSHR", "FT:30MR", "FT:AXBBTS", "FT:A:OE", "FT:HNB", "FT:10MG", "FT:10MR", "FT:10MB",
                    "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR", "FT:FGL", "FT:TOFBR", "FT:TOFG",
                    "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:HWMCD", "FT:WFB:A", "FT:WNTN:A",
                    "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:FC", "FT:5MC", "FT:10MC", "FT:AXBOU", "FT:AXBOU",
                    "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:HTTS2H", "FT:A:BH", "FT:WFB:H", "FT:B:CLS", "FT:ATTS2H",
                    "FT:B:BH", "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU", "FT:B:H1G", "FT:A:H1G", "FT:BTTSOOU", "FT:H1G",
                    "FT:AXBTTSOU", "FT:HFOU", "FT:AXBOU", "FT:BTSOOU", "FT:HOF", "FT:AXBBTS", "P:BTSOU", "FT:AXBTTSOU",
                    "FT:DBLCOU", "FT:DBLCBTS");

    private List<String> tierFourPreMatchOpenMarketList = Arrays.asList("FT:WNTN:A", "FT:WNTN:H", "FT:AXB", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS",
                    "P:BTSOU", "P:CS", "FT:FHSHCS", "FT:A:OE", "FT:HNB", "FT:10MG", "FT:10MR", "FT:10MB", "FT:5MB",
                    "FT:5MG", "FT:5MR", "FT:FGL", "FT:TOFBR", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD",
                    "FT:OUR", "FT:HWMCD", "FT:WFB:A", "FT:WNTN:A", "FT:5MC", "FT:10MC", "FT:AXBOU", "FT:AXBOU",
                    "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:A:BH", "FT:WFB:H", "FT:B:CLS", "FT:ATTS2H", "FT:B:BH",
                    "FT:WTN:A", "FT:TOFG:A", "FT:AXBTTSOU");
    private List<String> tierFourInPlayDisplayMarketList = Arrays.asList("FT:WNTN:A", "FT:WNTN:H", "FT:AXB", "P:AXB",
                    "FT:DNB", "FT:OU", "FT:HF", "FT:CS", "FT:EH", "G:NS", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "FT:FHSHR", "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS",
                    "P:BTSOU", "P:CS", "FT:A:OE", "FT:HNB", "FT:10MG", "FT:10MR", "FT:10MB", "FT:5MB", "FT:5MG",
                    "FT:5MR", "FT:FGL", "FT:TOFBR", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR",
                    "FT:HWMCD", "FT:WFB:A", "FT:WNTN:A", "FT:5MC", "FT:10MC", "FT:AXBOU", "FT:AXBOU", "FT:B:OE",
                    "FT:ANB", "FT:A:CLS", "FT:A:BH", "FT:WFB:H", "FT:B:CLS", "FT:ATTS2H", "FT:B:BH", "FT:WTN:A",
                    "FT:TOFG:A", "FT:AXBTTSOU");

    private List<String> tierFivePreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "G:NS", "FT:WM",
                    "FT:AGS", "FT:A:HSH", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "FT:A:OE",
                    "FT:HNB", "FT:10MG", "FT:10MR", "FT:10MB", "FT:5MB", "FT:5MG", "FT:5MR", "FT:FGL", "FT:TOFBR",
                    "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:HWMCD", "FT:WFB:A",
                    "FT:5MC", "FT:10MC", "FT:AXBOU", "FT:AXBOU", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:WFB:H",
                    "FT:B:CLS", "FT:WTN:A", "FT:TOFG:A");
    private List<String> tierFiveInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "G:NS", "FT:WM",
                    "FT:AGS", "FT:A:HSH", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:A:OE", "FT:HNB",
                    "FT:10MG", "FT:10MR", "FT:10MB", "FT:5MB", "FT:5MG", "FT:5MR", "FT:FGL", "FT:TOFBR", "FT:TOFG",
                    "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:HWMCD", "FT:WFB:A", "FT:5MC",
                    "FT:10MC", "FT:AXBOU", "FT:AXBOU", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:WFB:H", "FT:B:CLS",
                    "FT:WTN:A", "FT:TOFG:A");

    private int[] min = {5, 10, 15};
    private List<String> allTiersAcceptedMarketList =
                    Arrays.asList("FT:CM", "FT:CM2", "P:DBCS", "FT:CM15", "FT:CM25", "FT:CM35", "FT:15MR", "FT:30MR",
                                    "FT:60MR", "FT:5MG", "FT:10MG", "FT:15MG", "FT:20MG", "FT:30MG", "FT:COU");

    public BabaFootballTradingRules(Boolean testing) {

        if (testing)
            allTiersAcceptedMarketList = tierOnePreMatchOpenMarketList;
        // DerivedMarketSpec preMatchLinesToDisplaySpec2 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
        // DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 2, 1);

        double[] preMatch = {4.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerPreMatch = DerivedMarketSpec
                        .getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, preMatch);

        double[] fixedOUlinesCorner = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.5, 7.5, 8.5, 9.0, 9.5, 10.0,
                10.5, 11.0, 11.5, 12.0, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18, 18.5, 19, 19.5, 20,
                20.5};

        double[] fixedOUlinesCards = {1.5, 2.5, 3.5};

        DerivedMarketSpec derivedMarketFixedLineOUCornerSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCorner);

        DerivedMarketSpec derivedMarketFixedLineOUCards = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCards);

        double[] fixedOUlinesCornerTeam = {2.5, 3.5, 4.5, 5.5, 6.5, 7.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerSpecTeam = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCornerTeam);
        double[] fixedOUlinesCornerTeamT2 = {2.5, 3.5, 4.5, 5.5, 6.5, 7.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerSpecTeamT2 =
                        DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCornerTeamT2);

        double[] fixedOUlinesT1 = {0.5, 1.5, 2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT1);

        double[] fixedOUlinesT2 = {1.5, 2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec2 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT2);

        double[] fixedOUlinesT3 = {2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec3 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT3);

        double[] fixedOUlinesT4 = {3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec4 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT4);

        double[] fixedOUlinesT5 = {4.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec5 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT5);

        double[] fixedOUlinesCardTeam = {3.5, 4.5};
        DerivedMarketSpec derivedMarketFixedLineOUTeam = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_ONLY, fixedOUlinesCardTeam);
        addRule(new DerivedMarketTradingRule(1, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BCOU", derivedMarketFixedLineOUTeam));

        double[] fixedOUlinesCard = {5.5, 6.5, 7.5, 8.5, 9.5, 10.5};
        DerivedMarketSpec derivedMarketFixedLineOUCard = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCard);
        addRule(new DerivedMarketTradingRule(1, "FT:BCOU", derivedMarketFixedLineOUCard));
        addRule(new DerivedMarketTradingRule(2, "FT:BCOU", derivedMarketFixedLineOUCard));


        addRule(new DerivedMarketTradingRule(1, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BCOU", derivedMarketFixedLineOUTeam));


        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketFixedLineOUSpec1));
        addRule(new DerivedMarketTradingRule(1, "FT:COU", derivedMarketFixedLineOUCornerSpec1));
        addRule(new DerivedMarketTradingRule(1, "FT:A:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(1, "P:B:COU", derivedMarketFixedLineOUCornerPreMatch));
        addRule(new DerivedMarketTradingRule(1, "P:A:COU", derivedMarketFixedLineOUCornerPreMatch));

        addRule(new DerivedMarketTradingRule(2, "FT:COU", derivedMarketFixedLineOUCornerSpec1));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BYOU", derivedMarketFixedLineOUCards));


        addRule(new DerivedMarketTradingRule(1, "FT:A:COU", derivedMarketFixedLineOUCornerSpecTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:COU", derivedMarketFixedLineOUCornerSpecTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:COU", derivedMarketFixedLineOUCornerSpecTeamT2));
        addRule(new DerivedMarketTradingRule(2, "FT:B:COU", derivedMarketFixedLineOUCornerSpecTeamT2));

        addRule(new DerivedMarketTradingRule(2, "P:B:COU", derivedMarketFixedLineOUCornerPreMatch));
        addRule(new DerivedMarketTradingRule(2, "P:A:COU", derivedMarketFixedLineOUCornerPreMatch));

        addRule(new DerivedMarketTradingRule(2, "FT:OU", derivedMarketFixedLineOUSpec2));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketFixedLineOUSpec3));
        addRule(new DerivedMarketTradingRule(4, "FT:OU", derivedMarketFixedLineOUSpec4));
        addRule(new DerivedMarketTradingRule(5, "FT:OU", derivedMarketFixedLineOUSpec5));

        double[] fixedOUlinesFTABOU = {1.5};
        DerivedMarketSpec derivedMarketFixedLineFTABOU = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesFTABOU);

        addRule(new DerivedMarketTradingRule(2, "FT:A:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(2, "FT:B:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(1, "FT:A:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(1, "FT:B:OU", derivedMarketFixedLineFTABOU));

        int[][] range = {{0, 4}, {1, 4}, {2, 4}, {3, 4}, {3, 4}};


        // String[] priceCapMarkets = {"FT:TWOBTSOU"};
        // double[] priceCaps = {1.35};
        // addPriceCapMarkets(priceCapMarkets, priceCaps);
        //
        String[] extraMarginMarkets = {"FT:15MC"};
        double[] extraMargins = {0.05}; // extra margin per selections
        addPriceExtraMarginMarkets(extraMarginMarkets, extraMargins);

        String[] capMarkets = {"FT:BREDYes", "FT:BREDNo"};
        double[] capMargins = {1.0 / 4.2, 1.0 / 1.2}; // extra cap per selections
        addPriceCapMarkets(capMarkets, capMargins);

        for (int i = 1; i <= 5; i++) {
            FootballTradingRule footballTradingRule = new FootballTradingRule("Football trading rule", i, null);
            footballTradingRule.setInplaySuspendMarketList(inplaySuspendMarketList);
            footballTradingRule.setLineCapdMarketList(lineCapMarkets);
            footballTradingRule.setPriceCapList(priceCapList);
            footballTradingRule.setExtraMarginList(extraMarginList);
            footballTradingRule.setLineRages(range[i - 1]);
            footballTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
            addRule(footballTradingRule);
            footballTradingRule.setMin(min);
        }

        FootballTradingRule tierOneRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 1", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneInPlayDisplayMarketList);
        tierOneRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        tierOneRule.addParamFindSuspensionList("FT:COU,FT:CHCP", MarketGroup.CORNERS);

        double[] lines = {-1.0, -2.0};
        DerivedMarketSpec derivedMarketFixedLineSpec = DerivedMarketSpec
                        .getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, lines);
        addRule(new DerivedMarketTradingRule(1, "FT:EH", derivedMarketFixedLineSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:EH", derivedMarketFixedLineSpec));

        double[] lines2 = {2.5};
        DerivedMarketSpec derivedMarketFixedLineSpec2 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, lines2);
        addRule(new DerivedMarketTradingRule(1, "FT:15MC", derivedMarketFixedLineSpec2));
        addRule(new DerivedMarketTradingRule(2, "FT:15MC", derivedMarketFixedLineSpec2));

        addRule(tierOneRule);

        FootballTradingRule tierTwoRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 2", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoInPlayDisplayMarketList);
        tierTwoRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        tierTwoRule.addParamFindSuspensionList("FT:COU,FT:CHCP", MarketGroup.CORNERS);
        addRule(tierTwoRule);

        FootballTradingRule tierThreeRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 3", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeInPlayDisplayMarketList);
        tierThreeRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierThreeRule);

        FootballTradingRule tierFourRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 4", 4, null);
        tierFourRule.setPrematchDisplayMarketList(tierFourPreMatchOpenMarketList);
        tierFourRule.setInplayDisplayMarketList(tierFourInPlayDisplayMarketList);
        tierFourRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierFourRule);

        FootballTradingRule tierFiveRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 5", 5, null);
        tierFiveRule.setPrematchDisplayMarketList(tierFivePreMatchOpenMarketList);
        tierFiveRule.setInplayDisplayMarketList(tierFiveInPlayDisplayMarketList);
        tierFiveRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierFiveRule);


        /**
         * 
         * **/
        DerivedMarketSpec preMatchLinesToDisplaySpecCorner = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);

        // addRule(new DerivedMarketTradingRule(1, "FT:COU", preMatchLinesToDisplaySpecCorner));
        addRule(new DerivedMarketTradingRule(1, "FT:CHCP", preMatchLinesToDisplaySpecCorner));
        /**
         * 
         * */

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierOne = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 1", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeGroupsRequiredPreMatch("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeGroupsRequiredInPlay("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierOne.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierOne.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierOne);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierTwo = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 2", 2);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredPreMatch("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredPreMatch("FT:BCOU,FT:BCSPRD");
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredInPlay("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierTwo.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierTwo.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierTwo);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierThree = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 3", 3);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierThree.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierThree);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierFour = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 4", 4);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierFour.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierFour.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierFour);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierFive = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 5", 5);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierFive.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierFive.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierFive);


        MonitorFeedTradingRuleSuspensionMethod footballSuspensionMethod = (now, matchState) -> {
            if (((FootballMatchState) matchState).getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME))
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.DO_NOT_SUSPEND, "at half time");
            else
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.APPLY_STANDARD_RULES,
                                "not at half time");
        };

        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(240, 240, footballSuspensionMethod);
        addRule(monitorFeedTradingRule);
        addRule(new FootballSuspendToAwaitParamFindTradingRule());
    }

    public BabaFootballTradingRules() {



        // DerivedMarketSpec preMatchLinesToDisplaySpec2 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
        // DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 2, 1);

        double[] preMatch = {4.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerPreMatch = DerivedMarketSpec
                        .getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability.PRE_MATCH_ONLY, preMatch);

        double[] fixedOUlinesCorner = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.5, 7.5, 8.5, 9.0, 9.5, 10.0,
                10.5, 11.0, 11.5, 12.0, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18, 18.5, 19, 19.5, 20,
                20.5};

        double[] fixedOUlinesCards = {1.5, 2.5, 3.5};

        DerivedMarketSpec derivedMarketFixedLineOUCornerSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCorner);

        DerivedMarketSpec derivedMarketFixedLineOUCards = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCards);

        double[] fixedOUlinesCornerTeam = {2.5, 3.5, 4.5, 5.5, 6.5, 7.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerSpecTeam = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCornerTeam);
        double[] fixedOUlinesCornerTeamT2 = {2.5, 3.5, 4.5, 5.5, 6.5, 7.5};
        DerivedMarketSpec derivedMarketFixedLineOUCornerSpecTeamT2 =
                        DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCornerTeamT2);

        double[] fixedOUlinesT1 = {0.5, 1.5, 2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT1);

        double[] fixedOUlinesT2 = {1.5, 2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec2 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT2);

        double[] fixedOUlinesT3 = {2.5, 3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec3 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT3);

        double[] fixedOUlinesT4 = {3.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec4 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT4);

        double[] fixedOUlinesT5 = {4.5};
        DerivedMarketSpec derivedMarketFixedLineOUSpec5 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesT5);

        double[] fixedOUlinesCardTeam = {3.5, 4.5};
        DerivedMarketSpec derivedMarketFixedLineOUTeam = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_ONLY, fixedOUlinesCardTeam);
        addRule(new DerivedMarketTradingRule(1, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BCOU", derivedMarketFixedLineOUTeam));

        double[] fixedOUlinesCard = {5.5, 6.5, 7.5, 8.5, 9.5, 10.5};
        DerivedMarketSpec derivedMarketFixedLineOUCard = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesCard);
        addRule(new DerivedMarketTradingRule(1, "FT:BCOU", derivedMarketFixedLineOUCard));
        addRule(new DerivedMarketTradingRule(2, "FT:BCOU", derivedMarketFixedLineOUCard));


        addRule(new DerivedMarketTradingRule(1, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BCOU", derivedMarketFixedLineOUTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BCOU", derivedMarketFixedLineOUTeam));


        addRule(new DerivedMarketTradingRule(1, "FT:OU", derivedMarketFixedLineOUSpec1));
        addRule(new DerivedMarketTradingRule(1, "FT:COU", derivedMarketFixedLineOUCornerSpec1));
        addRule(new DerivedMarketTradingRule(1, "FT:A:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(1, "FT:B:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(1, "P:B:COU", derivedMarketFixedLineOUCornerPreMatch));
        addRule(new DerivedMarketTradingRule(1, "P:A:COU", derivedMarketFixedLineOUCornerPreMatch));

        addRule(new DerivedMarketTradingRule(2, "FT:COU", derivedMarketFixedLineOUCornerSpec1));
        addRule(new DerivedMarketTradingRule(2, "FT:B:BYOU", derivedMarketFixedLineOUCards));
        addRule(new DerivedMarketTradingRule(2, "FT:A:BYOU", derivedMarketFixedLineOUCards));


        addRule(new DerivedMarketTradingRule(1, "FT:A:COU", derivedMarketFixedLineOUCornerSpecTeam));
        addRule(new DerivedMarketTradingRule(1, "FT:B:COU", derivedMarketFixedLineOUCornerSpecTeam));
        addRule(new DerivedMarketTradingRule(2, "FT:A:COU", derivedMarketFixedLineOUCornerSpecTeamT2));
        addRule(new DerivedMarketTradingRule(2, "FT:B:COU", derivedMarketFixedLineOUCornerSpecTeamT2));

        addRule(new DerivedMarketTradingRule(2, "P:B:COU", derivedMarketFixedLineOUCornerPreMatch));
        addRule(new DerivedMarketTradingRule(2, "P:A:COU", derivedMarketFixedLineOUCornerPreMatch));

        addRule(new DerivedMarketTradingRule(2, "FT:OU", derivedMarketFixedLineOUSpec2));
        addRule(new DerivedMarketTradingRule(3, "FT:OU", derivedMarketFixedLineOUSpec3));
        addRule(new DerivedMarketTradingRule(4, "FT:OU", derivedMarketFixedLineOUSpec4));
        addRule(new DerivedMarketTradingRule(5, "FT:OU", derivedMarketFixedLineOUSpec5));

        double[] fixedOUlinesFTABOU = {1.5};
        DerivedMarketSpec derivedMarketFixedLineFTABOU = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlinesFTABOU);

        addRule(new DerivedMarketTradingRule(2, "FT:A:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(2, "FT:B:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(1, "FT:A:OU", derivedMarketFixedLineFTABOU));
        addRule(new DerivedMarketTradingRule(1, "FT:B:OU", derivedMarketFixedLineFTABOU));

        int[][] range = {{0, 4}, {1, 4}, {2, 4}, {3, 4}, {3, 4}};


        // String[] priceCapMarkets = {"FT:TWOBTSOU"};
        // double[] priceCaps = {1.35};
        // addPriceCapMarkets(priceCapMarkets, priceCaps);
        //
        String[] extraMarginMarkets = {"FT:15MC"};
        double[] extraMargins = {0.05}; // extra margin per selections
        addPriceExtraMarginMarkets(extraMarginMarkets, extraMargins);

        String[] capMarkets = {"FT:BREDYes", "FT:BREDNo"};
        double[] capMargins = {1.0 / 4.2, 1.0 / 1.2}; // extra cap per selections
        addPriceCapMarkets(capMarkets, capMargins);

        for (int i = 1; i <= 5; i++) {
            FootballTradingRule footballTradingRule = new FootballTradingRule("Football trading rule", i, null);
            footballTradingRule.setInplaySuspendMarketList(inplaySuspendMarketList);
            footballTradingRule.setLineCapdMarketList(lineCapMarkets);
            footballTradingRule.setPriceCapList(priceCapList);
            footballTradingRule.setExtraMarginList(extraMarginList);
            footballTradingRule.setLineRages(range[i - 1]);
            footballTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
            addRule(footballTradingRule);
            footballTradingRule.setMin(min);
        }

        FootballTradingRule tierOneRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 1", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneInPlayDisplayMarketList);
        tierOneRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        tierOneRule.addParamFindSuspensionList("FT:COU,FT:CHCP", MarketGroup.CORNERS);

        double[] lines = {-1.0, -2.0};
        DerivedMarketSpec derivedMarketFixedLineSpec = DerivedMarketSpec
                        .getDerivedMarketSpecForFixedLines(DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, lines);
        addRule(new DerivedMarketTradingRule(1, "FT:EH", derivedMarketFixedLineSpec));
        addRule(new DerivedMarketTradingRule(2, "FT:EH", derivedMarketFixedLineSpec));

        double[] lines2 = {2.5};
        DerivedMarketSpec derivedMarketFixedLineSpec2 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, lines2);
        addRule(new DerivedMarketTradingRule(1, "FT:15MC", derivedMarketFixedLineSpec2));
        addRule(new DerivedMarketTradingRule(2, "FT:15MC", derivedMarketFixedLineSpec2));

        addRule(tierOneRule);

        FootballTradingRule tierTwoRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 2", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoInPlayDisplayMarketList);
        tierTwoRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        tierTwoRule.addParamFindSuspensionList("FT:COU,FT:CHCP", MarketGroup.CORNERS);
        addRule(tierTwoRule);

        FootballTradingRule tierThreeRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 3", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeInPlayDisplayMarketList);
        tierThreeRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierThreeRule);

        FootballTradingRule tierFourRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 4", 4, null);
        tierFourRule.setPrematchDisplayMarketList(tierFourPreMatchOpenMarketList);
        tierFourRule.setInplayDisplayMarketList(tierFourInPlayDisplayMarketList);
        tierFourRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierFourRule);

        FootballTradingRule tierFiveRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 5", 5, null);
        tierFiveRule.setPrematchDisplayMarketList(tierFivePreMatchOpenMarketList);
        tierFiveRule.setInplayDisplayMarketList(tierFiveInPlayDisplayMarketList);
        tierFiveRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierFiveRule);


        /**
         * 
         * **/
        DerivedMarketSpec preMatchLinesToDisplaySpecCorner = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 5, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);

        // addRule(new DerivedMarketTradingRule(1, "FT:COU", preMatchLinesToDisplaySpecCorner));
        addRule(new DerivedMarketTradingRule(1, "FT:CHCP", preMatchLinesToDisplaySpecCorner));
        /**
         * 
         * */

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierOne = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 1", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeGroupsRequiredPreMatch("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierOne.addMarketTypeGroupsRequiredInPlay("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierOne.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierOne.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierOne);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierTwo = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 2", 2);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredPreMatch("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredPreMatch("FT:BCOU,FT:BCSPRD");
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierTwo.addMarketTypeGroupsRequiredInPlay("FT:COU,FT:CHCP");
        triggerParamFindTradingRuleEventTierTwo.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierTwo.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierTwo);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierThree = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 3", 3);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierThree.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierThree);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierFour = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 4", 4);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFour.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierFour.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierFour.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierFour);

        TriggerParamFindTradingRule triggerParamFindTradingRuleEventTierFive = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule - Tier 5", 5);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRuleEventTierFive.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutPreMatch("FT:CHCP");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:COU");
        triggerParamFindTradingRuleEventTierThree.addMarketTypeToFilterOutInPlay("FT:CHCP");
        triggerParamFindTradingRuleEventTierFive.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRuleEventTierFive.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRuleEventTierFive);


        MonitorFeedTradingRuleSuspensionMethod footballSuspensionMethod = (now, matchState) -> {
            if (((FootballMatchState) matchState).getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME))
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.DO_NOT_SUSPEND, "at half time");
            else
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.APPLY_STANDARD_RULES,
                                "not at half time");
        };

        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(240, 240, footballSuspensionMethod);
        addRule(monitorFeedTradingRule);
        addRule(new FootballSuspendToAwaitParamFindTradingRule());
    }

    private void addPriceExtraMarginMarkets(String[] priceCapMarkets, double[] priceCaps) {
        if (priceCapMarkets.length != priceCaps.length) {
            throw new IllegalArgumentException("Markets list size should be the same to extra margin list");
        }
        extraMarginList = new HashMap<String, Double>();
        int i = 0;
        for (String market : priceCapMarkets) {
            extraMarginList.put(market, priceCaps[i]);
            i++;
        }
    }

    private void addPriceCapMarkets(String[] priceCapMarkets, double[] priceCaps) {
        if (priceCapMarkets.length != priceCaps.length) {
            throw new IllegalArgumentException("Price cap markets list size should be the same to PriceCap list");
        }
        priceCapList = new HashMap<String, Double>();
        int i = 0;
        for (String market : priceCapMarkets) {
            priceCapList.put(market, priceCaps[i]);
            i++;
        }
    }
}
