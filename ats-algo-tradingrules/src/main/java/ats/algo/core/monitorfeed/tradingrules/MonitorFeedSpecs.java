package ats.algo.core.monitorfeed.tradingrules;

import ats.algo.core.traderalert.TraderAlert.TraderAlertType;

public class MonitorFeedSpecs {

    private int timeoutSecs;
    private boolean alertingOnly; // no suspension
    private TraderAlertType traderAlertType;


    public MonitorFeedSpecs(int timeoutSecs) {
        this.timeoutSecs = timeoutSecs;
    }


    public MonitorFeedSpecs(int timeoutSecs, boolean alertingOnly, TraderAlertType traderAlertType) {
        this.timeoutSecs = timeoutSecs;
        this.alertingOnly = alertingOnly;
        this.traderAlertType = traderAlertType;
    }

    public int getTimeoutSecs() {
        return timeoutSecs;
    }

    public boolean isAlertingOnly() {
        return alertingOnly;
    }

    public TraderAlertType getTraderAlertType() {
        return traderAlertType;
    }



}
