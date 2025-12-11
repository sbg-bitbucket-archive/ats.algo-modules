package ats.algo.sport.football.ilt.tradingrules;

import java.util.Arrays;
import java.util.List;

import ats.algo.core.markets.DerivedMarketSpec;
import ats.algo.core.markets.DerivedMarketSpec.DerivedMarketGapBetweenLines;
import ats.algo.core.markets.DerivedMarketSpecApplicability;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedSpecs;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethod;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.DerivedMarketTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.tradingrules.FootballSuspendToAwaitParamFindTradingRule;
import ats.algo.sport.football.tradingrules.FootballTradingRule;

public class IntralotFootballTradingRules extends TradingRules {

    public static final String ALL_MARKET = "All_MARKET";

    private List<String> finalMinuteOpenMarketList = Arrays.asList("FT:AXB");

    private List<String> allTiersAcceptedMarketList = Arrays.asList("FT:BHU", "FT:10M3", "FT:A:BH", "FT:A:BOU",
                    "FT:A:BTOT", "FT:A:CLS", "FT:A:COU", "FT:A:CTOT", "FT:A:EH", "FT:A:MLTG", "FT:A:NB", "FT:A:OE",
                    "FT:A:OU", "FT:A:£OUR_1", "FT:A:PSO", "FT:A:TTS2H", "FT:AGSC", "FT:AHCP", "FT:ATG", "FT:AWN",
                    "FT:BWN", "FT:AXB", "FT:AXB$OU", "FT:BTS$OU", "FT:B:BH", "FT:B:BOU", "FT:B:BTOT", "FT:B:CLS",
                    "FT:B:COU", "FT:B:EH", "FT:B:MLTG", "FT:B:NB", "FT:B:NUMC", "FT:B:OE", "FT:B:OU", "FT:B:£OUR_1",
                    "FT:B:PSO", "FT:B:TTS2H", "FT:BAXB", "FT:BEXA", "FT:BHO", "FT:BOU", "FT:BOUP", "FT:BPAGG", "FT:BTS",
                    "P:BTS", "FT:CAXB", "FT:CFTSC", "FT:CHCP", "FT:CLTSC", "FT:COE", "FT:COU", "FT:£COUR_1", "FT:£CS_1",
                    "FT:£CS_2", "P:£CS_2", "FT:CSFLEX", "FT:DBLC", "FT:DBLC$3HCP", "FT:DBLC$1HBTS", "FT:DBLC$2HBTS",
                    "FT:DBLC$BTS", "FT:DBLC$OU", "FT:DNB", "FT:3HCP", "FT:£OUR_1", "FT:£OUR_3", "FT:£OUR_4", "FT:FGSC",
                    "FT:FTBOOK", "FT:FTSC", "FT:HSH", "FT:A:HSH", "FT:B:HSH", "FT:HTFT", "FT:£HTFTCS_1", "FT:HWMBD",
                    "FT:LGSC", "FT:LTS", "FT:MBTSC", "P:AXB$BTS", "FT:MLTG", "FT:MTFL", "FT:NG", "FT:NGSC", "FT:OE",
                    "FT:OTYN", "FT:OU", "FT:£OUR_2", "FT:PENSOYN", "FT:PSO", "P:PSO", "FT:SPRD", "FT:TOFG10",
                    "FT:TOFG15", "FT:WATNR", "FT:WM", "FT:WTKO", "FT:WTTS", "FT:WTWPS", "FT:WWNGBS", "FT:WWPS",
                    "FT:WWROM", "FT:WWW3", "OT:AHCP", "OT:AXB", "OT:GYN", "OT:NG", "OT:OU", "OT:WWROM", "P:1HFTSC",
                    "P:A:BOU", "P:A:BTOT", "P:A:CTOT", "P:A:£OUR_1", "P:A:OU", "P:B:OU", "P:B:PSO", "P:A:PSO", "P:AHCP",
                    "P:ATG", "P:AXB", "P:B:BOU", "P:B:BTOT", "P:B:COU", "P:B:CTOT", "P:B:£OUR_1", "P:BAXB", "P:BEXA",
                    "P:BOUP", "P:BPAGG", "P:CAXB", "P:CFTSC", "P:CHCP", "P:CLTSC", "P:COE", "P:COU", "P:£COUR_1",
                    "P:£CS_1", "P:DBLC", "P:DBLC$BTS", "P:DNB", "P:3HCP", "P:EXNG", "P:FTB", "P:MLTG", "P:AXB$OU",
                    "P:NG", "P:OE", "P:OU", "P:£OUR_1", "P:£OUR_2", "P:WWTR", "PEN:A:OE", "PEN:A:OU", "PEN:AXB$OU",
                    "PEN:B:OE", "PEN:B:OU", "PEN:CS", "PEN:EXNP", "PEN:NG", "PEN:NP", "PEN:OE", "PEN:OU", "PEN:WM",
                    "POT:AHCP", "POT:AXB");

    private List<String> tierSixOpenMarketList = Arrays.asList("FT:AXB", "FT:DBLC", "FT:HTFT", "FT:HSH", "FT:3HCP",
                    "FT:DBLC$3HCP", "FT:OU", "FT:A:OU", "FT:B:OU", "FT:£OUR_4", "FT:A:£OUR_1", "FT:B:£OUR_1",
                    "FT:£OUR_3", "FT:BTS", "FT:OE", "FT:FTS", "FT:£CS_2");

    public IntralotFootballTradingRules() {

        double[] fixedOUlines = {0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5};
        double minProbBoundaries = 0.035;
        double maxProbBoundaries = 0.965;
        DerivedMarketSpec derivedMarketFixedLineSpec1 = DerivedMarketSpec.getDerivedMarketSpecForFixedLines(
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, fixedOUlines, minProbBoundaries,
                        maxProbBoundaries);

        addRule(new DerivedMarketTradingRule(null, "FT:OU", derivedMarketFixedLineSpec1));

        DerivedMarketSpec preMatchLinesToDisplaySpec1 = DerivedMarketSpec.getDerivedMarketSpecForAsianLines("", "",
                        DerivedMarketSpecApplicability.PRE_MATCH_AND_IN_PLAY, 6, 1,
                        DerivedMarketGapBetweenLines.QUARTER_LINE, minProbBoundaries, maxProbBoundaries);
        addRule(new DerivedMarketTradingRule(null, "FT:AHCP", preMatchLinesToDisplaySpec1));

        DerivedMarketSpec preMatchLinesToDisplaySpec = DerivedMarketSpec.getDerivedMarketSpecForAsianLines("", "",
                        DerivedMarketSpecApplicability.PRE_MATCH_ONLY, 3, 1, DerivedMarketGapBetweenLines.QUARTER_LINE);

        addRule(new DerivedMarketTradingRule(null, "FT:SPRD", preMatchLinesToDisplaySpec));
        addRule(new DerivedMarketTradingRule(null, "P:AHCP", preMatchLinesToDisplaySpec1));


        FootballTradingRule footballTradingRule = new FootballTradingRule("Intralot Football trading rule", null, null);
        footballTradingRule.setAcceptedMarketsList(allTiersAcceptedMarketList);
        footballTradingRule.setFinalMinuteDisplayMarketList(finalMinuteOpenMarketList);
        footballTradingRule.setPrematchDisplayMarketList(allTiersAcceptedMarketList);
        footballTradingRule.setInplayDisplayMarketList(allTiersAcceptedMarketList);
        footballTradingRule.setMinSelectionProb(0.035);
        footballTradingRule.setMaxSelectionProb(0.965);
        addRule(footballTradingRule);

        FootballTradingRule footballTradingRuleTierSix =
                        new FootballTradingRule("IntralotFootball trading rule - Tier 6", 6, null);
        footballTradingRuleTierSix.setPrematchDisplayMarketList(tierSixOpenMarketList);
        footballTradingRuleTierSix.setInplayDisplayMarketList(tierSixOpenMarketList);
        addRule(footballTradingRuleTierSix);

        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generatePriceDistanceVariant("Intralot Football TriggerParamFindTradingRule", null);
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
