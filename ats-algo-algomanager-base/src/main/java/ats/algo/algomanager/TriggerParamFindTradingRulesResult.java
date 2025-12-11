package ats.algo.algomanager;

public class TriggerParamFindTradingRulesResult {

    private boolean paramFindScheduled;
    private String actionReason;

    public TriggerParamFindTradingRulesResult(boolean paramFindScheduled, String actionReason) {
        super();
        this.paramFindScheduled = paramFindScheduled;
        this.actionReason = actionReason;
    }

    public boolean isParamFindScheduled() {
        return paramFindScheduled;
    }

    public String getActionReason() {
        return actionReason;
    }



}
