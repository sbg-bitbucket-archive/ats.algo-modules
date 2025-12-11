package ats.algo.sport.football.tradingrules;

import java.util.Arrays;
import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedSpecs;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethod;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;

public class FootballTradingRules extends TradingRules {

    public static final String ALL_MARKET = "All_MARKET";

    private List<String> finalMinuteOpenMarketList = Arrays.asList("FT:AXB");


    private List<String> tierOneIntroLotPreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DBLC", "P:AXB", "P:DBLC",
                    "P:AXB", "P:DBLC", "FT:HTFT", "FT:HSH", "FT:3HCP", "FT:DBLC$3HCP", "FT:OU", "P:OU", "FT:A:OU",
                    "FT:B:OU", "FT:£OUR_4", "P:£OUR_2", "FT:A:£OUR_1", "FT:B:£OUR_1", "P:A:£OUR_1", "P:B:£OUR_1",
                    "FT:£OUR_3", "FT:BTS", "P:BTS", "FT:OE", "P:OE", "FT:FTSC", "FT:£CS_2", "P:£CS_2");
    private List<String> tierOneIntroLotInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DBLC", "P:AXB", "P:DBLC",
                    "P:AXB", "P:DBLC", "FT:HTFT", "FT:HSH", "FT:3HCP", "FT:DBLC$3HCP", "FT:OU", "P:OU", "FT:A:OU",
                    "FT:B:OU", "FT:£OUR_4", "P:£OUR_2", "FT:A:£OUR_1", "FT:B:£OUR_1", "P:A:£OUR_1", "P:B:£OUR_1",
                    "FT:£OUR_3", "FT:BTS", "P:BTS", "FT:OE", "P:OE", "FT:FTSC", "FT:£CS_2", "P:£CS_2");

    private List<String> tierOnePreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:FTSC", "FT:BTS", "FT:ATG", "FT:CS",
                    "FT:OE", "FT:A:OE", "FT:B:OE", "FT:DBLC", "FT:DNB", "FT:A:NB", "FT:B:NB", "FT:MBTSC", "FT:OU",
                    "FT:A:OU", "FT:B:OU", "FT:WM", "FT:DBLC$BTS", "FT:AXB$OU", "FT:A:CLS", "FT:B:CLS", "FT:3HCP",
                    "FT:SPRD", "FT:AWN", "FT:BWN", "FT:HTFT", "FT:A:BH", "FT:B:BH", "FT:A:EH", "FT:B:EH", "FT:A:TTS2H",
                    "FT:B:TTS2H", "FT:HTTS", "FT:ATTS", "FT:RTG3", "FT:RTG2", "FT:DBLC$OU", "FT:NTS", "FT:LTS",
                    "FT:LAT:H", "FT:LAT:A", "FT:20MG", "FT:30MG", "FT:60MG", "FT:75MG", "FT:15MR", "FT:30MR", "FT:60MR",
                    "FT:75MR", "FT:HSH", "FT:AHCP", "FT:H1G", "FT:A:H1G", "FT:B:H1G", "FT:A:TW23", "FT:B:TW23",
                    "FT:WTTS", "FT:A:MLTG", "FT:B:MLTG", "FT:£OUR_3", "FT:Â£OUR_1", "FT:HTTS2", "FT:HTTS3", "FT:ATTS2",
                    "FT:ATTS3", "FT:1HCOMBO", "FT:CM15", "FT:CM25", "FT:CM35", "FT:WNTN:A", "FT:WNTN:B", "FT:BTTS1HH",
                    "FT:BTNTS1HH", "FT:BTTS2HH", "FT:BTNTS2HH", "FT:BTTS1HX", "FT:BTNTS1HX", "FT:BTTS2HX",
                    "FT:BTNTS2HX", "FT:BTTS1HA", "FT:BTNTS1HA", "FT:BTTS2HA", "FT:BTNTS2HA", "P:AXB", "P:AXB", "P:BTS",
                    "P:BTS", "P:OE", "P:OE", "P:A:OE", "P:A:OE", "P:B:OE", "P:B:OE", "P:DBLC", "P:DBLC", "P:DNB",
                    "P:DNB", "P:OU", "P:OU", "P:A:OU", "P:A:OU", "P:B:OU", "P:B:OU", "P:3HCP", "P:3HCP", "P:DBLC$BTS",
                    "P:DBLC$BTS", "P:AXB$OU", "P:AXB$OU", "P:A:WTN", "P:A:WTN", "P:B:WTN", "P:B:WTN", "P:A£OUR_1",
                    "P:A£OUR_1", "P:A:£OUR_1", "P:A:£OUR_1", "P:B:£OUR_1", "P:B:£OUR_1", "P:HNB", "P:HNB", "P:ANB",
                    "P:ANB", "P:SPRD", "P:SPRD", "FT:COU", "FT:A:COU", "FT:B:COU", "FT:COE", "FT:A:COE", "FT:B:COE",
                    "FT:CHCP", "FT:CEHCP", "FT:CAXB", "P:COU", "P:COU", "P:A:COU", "P:A:COU", "P:B:COU", "P:B:COU",
                    "FT:5MC", "FT:10MC", "FT:15MC", "P:CAXB", "P:CAXB", "FT:ACORNER", "FT:BCORNER", "FT:HWMCR",
                    "FT:CORNEROVER2", "FT:CORNEROVER15", "FT:FC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:TQ",
                    "ET:AXB", "ET:OU", "ET:BTS", "P:DBLCBTS", "P:CEHCP", "FT:£OUR_4", "FT:TOFG", "FT:TOFG:H",
                    "FT:TOFG:A", "FT:B:Â£OUR_2", "FT:A:Â£OUR_2");
    private List<String> tierOneInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:FTSC", "FT:BTS", "FT:ATG",
                    "FT:CS", "FT:OE", "FT:A:OE", "FT:B:OE", "FT:DBLC", "FT:DNB", "FT:A:NB", "FT:B:NB", "FT:MBTSC",
                    "FT:OU", "FT:A:OU", "FT:B:OU", "FT:WM", "FT:DBLC$BTS", "FT:AXB$OU", "FT:A:CLS", "FT:B:CLS",
                    "FT:3HCP", "FT:SPRD", "FT:AWN", "FT:BWN", "FT:HTFT", "FT:A:BH", "FT:B:BH", "FT:A:EH", "FT:B:EH",
                    "FT:A:TTS2H", "FT:B:TTS2H", "FT:HTTS", "FT:ATTS", "FT:RTG3", "FT:RTG2", "FT:DBLC$OU", "FT:NTS",
                    "FT:LTS", "FT:LAT:H", "FT:LAT:A", "FT:20MG", "FT:30MG", "FT:60MG", "FT:75MG", "FT:15MR", "FT:30MR",
                    "FT:60MR", "FT:75MR", "FT:HSH", "FT:AHCP", "FT:H1G", "FT:A:H1G", "FT:B:H1G", "FT:A:TW23",
                    "FT:B:TW23", "FT:WTTS", "FT:A:MLTG", "FT:B:MLTG", "FT:£OUR_3", "FT:Â£OUR_1", "FT:HTTS2", "FT:HTTS3",
                    "FT:ATTS2", "FT:ATTS3", "FT:1HCOMBO", "FT:CM15", "FT:CM25", "FT:CM35", "FT:WNTN:A", "FT:WNTN:B",
                    "FT:BTTS1HH", "FT:BTNTS1HH", "FT:BTTS2HH", "FT:BTNTS2HH", "FT:BTTS1HX", "FT:BTNTS1HX", "FT:BTTS2HX",
                    "FT:BTNTS2HX", "FT:BTTS1HA", "FT:BTNTS1HA", "FT:BTTS2HA", "FT:BTNTS2HA", "P:AXB", "P:AXB", "P:BTS",
                    "P:BTS", "P:OE", "P:OE", "P:A:OE", "P:A:OE", "P:B:OE", "P:B:OE", "P:DBLC", "P:DBLC", "P:DNB",
                    "P:DNB", "P:OU", "P:OU", "P:A:OU", "P:A:OU", "P:B:OU", "P:B:OU", "P:3HCP", "P:3HCP", "P:DBLC$BTS",
                    "P:DBLC$BTS", "P:AXB$OU", "P:AXB$OU", "P:A:WTN", "P:A:WTN", "P:B:WTN", "P:B:WTN", "P:A£OUR_1",
                    "P:A£OUR_1", "P:A:£OUR_1", "P:A:£OUR_1", "P:B:£OUR_1", "P:B:£OUR_1", "P:HNB", "P:HNB", "P:ANB",
                    "P:ANB", "P:SPRD", "P:SPRD", "FT:COU", "FT:A:COU", "FT:B:COU", "FT:COE", "FT:A:COE", "FT:B:COE",
                    "FT:CHCP", "FT:CEHCP", "FT:CAXB", "P:COU", "P:COU", "P:A:COU", "P:A:COU", "P:B:COU", "P:B:COU",
                    "FT:5MC", "FT:10MC", "FT:15MC", "P:CAXB", "P:CAXB", "FT:ACORNER", "FT:BCORNER", "FT:HWMCR",
                    "FT:CORNEROVER2", "FT:CORNEROVER15", "FT:FC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:RTCW", "FT:TQ",
                    "ET:AXB", "ET:OU", "ET:BTS", "P:DBLCBTS", "P:CEHCP", "FT:£OUR_4", "FT:TOFG", "FT:TOFG:H",
                    "FT:TOFG:A", "FT:B:Â£OUR_2", "FT:A:Â£OUR_2");

    private List<String> tierTwoPreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:AHCP", "FT:ATG", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:ACOU", "FT:HNB", "FT:WTN:H",
                    "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR",
                    "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:NTS", "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW",
                    "FT:CFBWD", "FT:OUR", "FT:WFB:A", "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU",
                    "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3",
                    "FT:OUR:A", "FT:ATTS2", "FT:ATTS3", "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H",
                    "FT:HTTS", "FT:HTTS2H", "FT:HTTS1H", "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A",
                    "FT:ATTS", "FT:ATTS2H", "FT:ATTS1H", "FT:B:BH", "FT:B:EH", "FT:TOFG:A", "FT:COU", "P:CSPRD",
                    "P:COU", "P:CEHCP", "P:CAXB", "P:A:COU", "P:B:COU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:CSPRD", "FT:FC", "FT:HWMCR", "FT:CEHCP", "FT:CAXB",
                    "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC", "FT:TOFBR", "FT:BAXB", "FT:RED", "FT:CAOU",
                    "FT:HWMCD", "FT:A:YCAOU", "FT:B:YCAOU", "FT:BOU", "FT:BSPRD");
    private List<String> tierTwoInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:AHCP", "FT:ATG", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:ACOU", "FT:HNB", "FT:WTN:H",
                    "FT:10MG", "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR",
                    "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:NTS", "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW",
                    "FT:CFBWD", "FT:OUR", "FT:WFB:A", "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU",
                    "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3",
                    "FT:OUR:A", "FT:ATTS2", "FT:ATTS3", "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H",
                    "FT:HTTS", "FT:HTTS2H", "FT:HTTS1H", "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A",
                    "FT:ATTS", "FT:ATTS2H", "FT:ATTS1H", "FT:B:BH", "FT:B:EH", "FT:TOFG:A", "FT:COU", "P:CSPRD",
                    "P:COU", "P:CEHCP", "P:CAXB", "P:A:COU", "P:B:COU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:CSPRD", "FT:FC", "FT:HWMCR", "FT:CEHCP", "FT:CAXB",
                    "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC", "FT:TOFBR", "FT:BAXB", "FT:RED", "FT:CAOU",
                    "FT:HWMCD", "FT:A:YCAOU", "FT:B:YCAOU", "FT:BOU", "FT:BSPRD");

    private List<String> tierThreePreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:ATG", "FT:AHCP", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:HNB", "FT:WTN:H", "FT:10MG",
                    "FT:10MR", "FT:15MG", "FT:15MR", "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG", "FT:NTS",
                    "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:WFB:A",
                    "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU",
                    "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3", "FT:OUR:A", "FT:ATTS2", "FT:ATTS3",
                    "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H", "FT:HTTS", "FT:HTTS2H", "FT:HTTS1H",
                    "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A", "FT:ATTS", "FT:ATTS2H", "FT:ATTS1H",
                    "FT:B:BH", "FT:B:EH", "FT:TOFG:A", "FT:COU", "FT:ACOU", "P:CSPRD", "P:COU", "P:CEHCP", "P:CAXB",
                    "P:A:COU", "P:B:COU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:TOFCR", "FT:CSPRD", "FT:FC", "FT:HWMCR", "FT:CEHCP", "FT:CAXB", "FT:A:COU", "FT:B:COU",
                    "FT:5MC", "FT:10MC", "FT:15MC");
    private List<String> tierThreeInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:AHCP", "FT:ATG", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:HNB", "FT:WTN:H", "FT:10MG",
                    "FT:10MR", "FT:10MB", "FT:15MB", "FT:15MG", "FT:15MR", "FT:5MB", "FT:5MG", "FT:5MR", "FT:BTSOU",
                    "FT:DBLCBTS", "FT:NG", "FT:NTS", "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD",
                    "FT:OUR", "FT:WFB:A", "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU",
                    "FT:DBLCOU", "FT:DBLCOU", "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3", "FT:OUR:A",
                    "FT:ATTS2", "FT:ATTS3", "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H", "FT:HTTS",
                    "FT:HTTS2H", "FT:HTTS1H", "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A", "FT:ATTS",
                    "FT:ATTS2H", "FT:ATTS1H", "FT:B:BH", "FT:B:EH", "FT:TOFG:A", "FT:COU", "FT:ACOU", "P:CSPRD",
                    "P:COU", "P:CEHCP", "P:CAXB", "P:A:COU", "P:B:COU", "FT:RTC", "FT:RTC", "FT:RTC", "FT:RTC",
                    "FT:RTC", "FT:RTC", "FT:RTC", "FT:TOFCR", "FT:CSPRD", "FT:FC", "FT:HWMCR", "FT:CEHCP", "FT:CAXB",
                    "FT:A:COU", "FT:B:COU", "FT:5MC", "FT:10MC", "FT:15MC");

    private List<String> tierFourPreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:AHCP", "FT:ATG", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:ACOU", "FT:HNB", "FT:WTN:H",
                    "FT:10MG", "FT:10MR", "FT:15MG", "FT:15MR", "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG",
                    "FT:NTS", "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:WFB:A",
                    "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU",
                    "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3", "FT:OUR:A", "FT:ATTS2", "FT:ATTS3",
                    "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H", "FT:HTTS", "FT:HTTS2H", "FT:HTTS1H",
                    "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A", "FT:ATTS", "FT:ATTS2H", "FT:ATTS1H",
                    "FT:B:BH", "FT:B:EH", "FT:TOFG:A");
    private List<String> tierFourInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A", "FT:DBLC", "P:AXB",
                    "FT:CS", "FT:RTG2", "FT:RTG3", "FT:OE", "FT:TQ", "FT:WM", "FT:AGS", "P:OUR", "FT:FHNTCS", "FT:HSH",
                    "FT:LHTL", "P:A:OE", "P:B:OE", "P:OUR:A", "P:A:OU", "P:OUR:B", "P:B:OU", "P:OE", "FT:FHSHR",
                    "FT:A:HSH", "P:HNB", "FT:B:HSH", "P:ANB", "P:BTS", "P:BTSOU", "P:CS", "FT:FHSHCS", "P:DBLC",
                    "P:DBLCBTS", "P:DNB", "P:EHCP", "P:SPRD", "P:AXBBTS", "P:AXBOU", "P:AXBOU", "P:AXBOU", "P:AXBOU",
                    "FT:FHSHR", "FT:FHNTCS", "FT:AHCP", "FT:ATG", "P:AHCP", "P:ATG", "FT:30MR", "FT:60MR", "FT:75MR",
                    "FT:AXBBTS", "FT:GBH", "FT:20MG", "FT:30MG", "FT:LTS", "FT:A:OE", "FT:ACOU", "FT:HNB", "FT:WTN:H",
                    "FT:10MG", "FT:10MR", "FT:15MG", "FT:15MR", "FT:5MG", "FT:5MR", "FT:BTSOU", "FT:DBLCBTS", "FT:NG",
                    "FT:NTS", "FT:FGL", "FT:TOFG", "FT:TOFG:H", "FT:CFBD", "FT:CFBW", "FT:CFBWD", "FT:OUR", "FT:WFB:A",
                    "FT:WNTN:A", "FT:DBLCOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:AXBOU", "FT:DBLCOU", "FT:DBLCOU",
                    "FT:DBLCOU", "FT:A:OU", "FT:B:OU", "FT:HTTS2", "FT:HTTS3", "FT:OUR:A", "FT:ATTS2", "FT:ATTS3",
                    "FT:OUR:B", "FT:B:OE", "FT:ANB", "FT:A:CLS", "FT:LAT:H", "FT:HTTS", "FT:HTTS2H", "FT:HTTS1H",
                    "FT:A:BH", "FT:A:EH", "FT:WFB:H", "FT:B:CLS", "FT:LAT:A", "FT:ATTS", "FT:ATTS2H", "FT:ATTS1H",
                    "FT:B:BH", "FT:B:EH", "FT:TOFG:A");

    private List<String> tierFivePreMatchOpenMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A");
    private List<String> tierFiveInPlayDisplayMarketList = Arrays.asList("FT:AXB", "FT:DNB", "FT:OU", "FT:HF", "FT:EH",
                    "FT:SPRD", "FT:FTS", "FT:BTS", "P:OUR", "P:OU", "FT:WNTN:H", "FT:WTN:A");



    private int[] min = {5, 10, 15};

    public FootballTradingRules() {
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


        FootballTradingRule footballTradingRule = new FootballTradingRule("Football trading rule", 3, null);
        DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec.getDerivedMarketSpecForAsianLines("", "",
                        DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 3, 1, DerivedMarketGapBetweenLines.QUARTER_LINE);

        addRule(new DerivedMarketTradingRule(3, "FT:SPRD", preMatchLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(3, "FT:OU:A", preMatchLinesToDisplaySpec));
        DerivedMarketSpec preMatchLinesToDisplaySpec2 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 7, 1);

        addRule(new DerivedMarketTradingRule(6, "FT:OU", preMatchLinesToDisplaySpec2));
        DerivedMarketSpec preMatchLinesToDisplaySpec3 = DerivedMarketSpec.getDerivedMarketSpecForRegularLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 3, 1,
                        DerivedMarketGapBetweenLines.HALF_LINE);
        addRule(new DerivedMarketTradingRule(3, "FT:ACOU", preMatchLinesToDisplaySpec3));



        addRule(new DerivedMarketTradingRule(3, "P:AHCP", preMatchLinesToDisplaySpec1));
        footballTradingRule.setMin(min);
        addRule(footballTradingRule);

        FootballTradingRule tierOneRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 1", 1, null);
        tierOneRule.setPrematchDisplayMarketList(tierOnePreMatchOpenMarketList);
        tierOneRule.setInplayDisplayMarketList(tierOneInPlayDisplayMarketList);
        tierOneRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierOneRule);

        FootballTradingRule tierTwoRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 2", 2, null);
        tierTwoRule.setPrematchDisplayMarketList(tierTwoPreMatchOpenMarketList);
        tierTwoRule.setInplayDisplayMarketList(tierTwoInPlayDisplayMarketList);
        tierTwoRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierTwoRule);

        FootballTradingRule tierThreeRule =
                        new FootballTradingRule("Football trading rule for suspension - tier 3", 3, null);
        tierThreeRule.setPrematchDisplayMarketList(tierThreePreMatchOpenMarketList);
        tierThreeRule.setInplayDisplayMarketList(tierThreeInPlayDisplayMarketList);
        tierThreeRule.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        tierThreeRule.addParamFindSuspensionList("FT:COU,FT:CSPRD", MarketGroup.CORNERS);
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
         * Add Intro Lot trading Rule in as tier 6 Rule
         */
        FootballTradingRule tierOneRuleIntroLot =
                        new FootballTradingRule("Football trading rule for suspension - Intralot 1", 6, null);
        tierOneRuleIntroLot.setPrematchDisplayMarketList(tierOneIntroLotPreMatchOpenMarketList);
        tierOneRuleIntroLot.setInplayDisplayMarketList(tierOneIntroLotInPlayDisplayMarketList);
        tierOneRuleIntroLot.setFullTimeSuspendMarketList(finalMinuteOpenMarketList);
        addRule(tierOneRuleIntroLot);


        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Football TriggerParamFindTradingRule", null);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRule.addMarketTypeGroupsRequiredPreMatch("FT:COU,FT:CSPRD");
        triggerParamFindTradingRule.addMarketTypeGroupsRequiredPreMatch("FT:BCOU,FT:BCSPRD");
        // triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:CSPRDRPD", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        triggerParamFindTradingRule.addMarketTypeGroupsRequiredInPlay("FT:COU,FT:CSPRD");
        triggerParamFindTradingRule.addMarketWeight("FT:AXB", 2.0);
        triggerParamFindTradingRule.addMarketWeight("FT:AHCP", 2.0);
        addRule(triggerParamFindTradingRule);

        MonitorFeedTradingRuleSuspensionMethod footballSuspensionMethod = (now, matchState) -> {
            if (((FootballMatchState) matchState).getMatchPeriod().equals(FootballMatchPeriod.AT_HALF_TIME))
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.DO_NOT_SUSPEND, "at half time");
            else
                return new MonitorFeedTradingRuleSuspensionMethodResult(
                                MonitorFeedTradingRuleSuspensionMethodResultType.APPLY_STANDARD_RULES,
                                "not at half time");
        };

        MonitorFeedSpecs[] pricingFeedsUpdateTimeOut = new MonitorFeedSpecs[2]; // 3mins 5 mins
        MonitorFeedSpecs[] incidentFeedsUpdateTimeOut = new MonitorFeedSpecs[1];
        pricingFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(3 * 60, true, TraderAlertType.INPUT_PRICES_MISSING_WARNING);
        pricingFeedsUpdateTimeOut[1] = new MonitorFeedSpecs(5 * 60, false, TraderAlertType.INPUT_PRICES_MISSING_DANGER);
        incidentFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(60, false, TraderAlertType.INPUT_INCIDENT_MISSING_WARNING);
        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(incidentFeedsUpdateTimeOut,
                        pricingFeedsUpdateTimeOut, footballSuspensionMethod);
        addRule(monitorFeedTradingRule);
        addRule(new FootballSuspendToAwaitParamFindTradingRule());
    }
}
