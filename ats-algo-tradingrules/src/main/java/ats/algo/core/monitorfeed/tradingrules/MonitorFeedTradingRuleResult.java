package ats.algo.core.monitorfeed.tradingrules;

import ats.algo.core.traderalert.TraderAlert;

public class MonitorFeedTradingRuleResult {

    private boolean shouldSuspend;
    private String suspensionReason;
    private TraderAlert alertActionForIncidents; // optional, when this is null indicating no alerts needed
    private TraderAlert alertActionForPrices;


    public MonitorFeedTradingRuleResult(Boolean shouldSuspend, String suspensionReason) {
        super();
        this.shouldSuspend = shouldSuspend;
        this.suspensionReason = suspensionReason;
    }

    public MonitorFeedTradingRuleResult(Boolean shouldSuspend, String suspensionReason, TraderAlert alertIncidents,
                    TraderAlert alertPrices) {
        super();
        this.shouldSuspend = shouldSuspend;
        this.suspensionReason = suspensionReason;
        this.alertActionForPrices = alertPrices;
        this.alertActionForIncidents = alertIncidents;
    }

    public boolean isShouldSuspend() {
        return shouldSuspend;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public TraderAlert getAlertActionForIncidents() {
        return alertActionForIncidents;
    }

    public TraderAlert getAlertActionForPrices() {
        return alertActionForPrices;
    }

}
