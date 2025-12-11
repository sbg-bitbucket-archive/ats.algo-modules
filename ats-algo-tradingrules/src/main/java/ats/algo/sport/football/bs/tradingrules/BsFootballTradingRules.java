package ats.algo.sport.football.bs.tradingrules;


import ats.algo.core.monitorfeed.tradingrules.MonitorFeedSpecs;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.tradingrules.FootballTriggerParamFindTradingRule;

public class BsFootballTradingRules extends TradingRules {



    public BsFootballTradingRules() {
        FootballTriggerParamFindTradingRule betstarsFootballTriggerParamFindTradingRule =
                        new FootballTriggerParamFindTradingRule();
        betstarsFootballTriggerParamFindTradingRule.setTriggerOnPriceDistance(false);
        betstarsFootballTriggerParamFindTradingRule.setTimeBetweenParamFinds(60 * 1000);

        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("IBCbet", 1);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("sbobet", 2);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("BETISN", 3);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("188bet", 4);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("12bet", 5);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("singbet", 6);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("PinnacleSports", 7);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Bet365", 8);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Skybet", 9);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Unibet", 10);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("bwin", 11);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("willhill", 12);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Marathonbet", 13);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("Ladbrokes", 14);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForPreMatchParamFinding("victor", 15);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("IBCbet", 1);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("sbobet", 2);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("BETISN", 3);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("188bet", 4);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("12bet", 5);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("singbet", 6);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("PinnacleSports", 7);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Bet365", 8);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Skybet", 9);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Unibet", 10);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("bwin", 11);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("willhill", 12);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Marathonbet", 13);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("Ladbrokes", 14);
        betstarsFootballTriggerParamFindTradingRule.addBookiePriorityForInPlayParamFinding("victor", 15);
        betstarsFootballTriggerParamFindTradingRule.addMarketTypeGroupsRequiredPreMatch("FT:OU");
        betstarsFootballTriggerParamFindTradingRule.addMarketTypeListRequiredPreMatch("FT:AXB,FT:AHCP");
        betstarsFootballTriggerParamFindTradingRule.addMarketTypeGroupsRequiredInPlay("FT:OU");
        betstarsFootballTriggerParamFindTradingRule.addMarketTypeListRequiredInPlay("FT:AXB,FT:AHCP");
        betstarsFootballTriggerParamFindTradingRule.setNumberOfBookiesRequiredPrematch(7);
        betstarsFootballTriggerParamFindTradingRule.setNumberOfBookiesRequiredInplay(7);
        betstarsFootballTriggerParamFindTradingRule.setUseAllBookies(true);


        betstarsFootballTriggerParamFindTradingRule
                        .addMarketGroupsRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF.toString(), null);
        betstarsFootballTriggerParamFindTradingRule
                        .addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF.toString(), null);

        betstarsFootballTriggerParamFindTradingRule.addMarketGroupsRequiredAtPeriod(
                        FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF.toString(), null);
        betstarsFootballTriggerParamFindTradingRule
                        .addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF.toString(), null);

        betstarsFootballTriggerParamFindTradingRule
                        .addMarketGroupsRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME.toString(), null);
        betstarsFootballTriggerParamFindTradingRule
                        .addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME.toString(), null);
        // betstarsFootballTriggerParamFindTradingRule.addMarketGroupsRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF.toString(),
        // "ET:OU");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF.toString(),
        // "ET:AXB");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF.toString(),
        // "ET:AHCP");
        //
        // betstarsFootballTriggerParamFindTradingRule.addMarketGroupsRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF.toString(),
        // "ET:OU");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF.toString(),
        // "ET:AXB");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF.toString(),
        // "ET:AHCP");
        //
        // betstarsFootballTriggerParamFindTradingRule.addMarketGroupsRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME.toString(),
        // "ET:OU");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME.toString(),
        // "ET:AXB");
        // betstarsFootballTriggerParamFindTradingRule.addMarketTypesRequiredAtPeriod(FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME.toString(),
        // "ET:AHCP");
        // // Abelson first scorer markets price change will always trigger the param find;
        betstarsFootballTriggerParamFindTradingRule.feedsSpecialMarketsParamFindingTrigger("Abelson", "FT:FSG");
        addRule(betstarsFootballTriggerParamFindTradingRule);

        MonitorFeedSpecs[] pricingFeedsUpdateTimeOut = new MonitorFeedSpecs[2];// 3mins 5 min
        MonitorFeedSpecs[] incidentFeedsUpdateTimeOut = new MonitorFeedSpecs[1];
        pricingFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(3 * 60, true, TraderAlertType.INPUT_PRICES_MISSING_WARNING);
        pricingFeedsUpdateTimeOut[1] = new MonitorFeedSpecs(5 * 60, false, TraderAlertType.INPUT_PRICES_MISSING_DANGER);
        incidentFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(60, false, TraderAlertType.INPUT_INCIDENT_MISSING_WARNING);
        MonitorFeedTradingRule monitorFeedTradingRule =
                        new MonitorFeedTradingRule(incidentFeedsUpdateTimeOut, pricingFeedsUpdateTimeOut, null);
        addRule(monitorFeedTradingRule);
    }


}
