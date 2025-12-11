package ats.algo.sport.football.lc.tradingrules;


import ats.algo.core.monitorfeed.tradingrules.MonitorFeedSpecs;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethod;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballMatchState;
import ats.algo.sport.football.tradingrules.FootballSuspendToAwaitParamFindTradingRule;

public class LcFootballTradingRules extends TradingRules {

    public LcFootballTradingRules() {
        TriggerParamFindTradingRule triggerParamFindTradingRule = TriggerParamFindTradingRule
                        .generateTimerVariant("LC Football TriggerParamFindTradingRule", 15, null);
        // triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:OU", 1);
        // triggerParamFindTradingRule.addMarketTypeRequiredPreMatch("FT:AXB,FT:AHCP", 1);
        // triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:OU", 1);
        // triggerParamFindTradingRule.addMarketTypeRequiredInPlay("FT:AXB,FT:AHCP", 1);
        // triggerParamFindTradingRule.addMarketWeight("FT:AXB", 2.0);
        // triggerParamFindTradingRule.addMarketWeight("FT:AHCP", 2.0);
        // Abelson first scorer markets price change will always trigger the param find;
        triggerParamFindTradingRule.feedsSpecialMarketsParamFindingTrigger("Abelson", "FT:FSG");
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

        MonitorFeedSpecs[] pricingFeedsUpdateTimeOut = new MonitorFeedSpecs[2];// 3mins 5 min
        MonitorFeedSpecs[] incidentFeedsUpdateTimeOut = new MonitorFeedSpecs[1];
        pricingFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(2 * 60, true, TraderAlertType.INPUT_PRICES_MISSING_WARNING);
        pricingFeedsUpdateTimeOut[1] = new MonitorFeedSpecs(3 * 60, false, TraderAlertType.INPUT_PRICES_MISSING_DANGER);
        incidentFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(60, false, TraderAlertType.INPUT_INCIDENT_MISSING_DANGER);
        MonitorFeedTradingRule monitorFeedTradingRule = new MonitorFeedTradingRule(incidentFeedsUpdateTimeOut,
                        pricingFeedsUpdateTimeOut, footballSuspensionMethod);
        addRule(monitorFeedTradingRule);

        addRule(new FootballSuspendToAwaitParamFindTradingRule());

        LcFootballTraderAlertTradingRule lcFootballTraderAlertTradingRule =
                        new LcFootballTraderAlertTradingRule("LC Football AlertTradingRule");
        addRule(lcFootballTraderAlertTradingRule);
    }
}
