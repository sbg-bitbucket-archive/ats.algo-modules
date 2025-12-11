package ats.algo.core.monitorfeed.tradingrules;

public class MonitorFeedTradingRuleSuspensionMethodResult {

    public enum MonitorFeedTradingRuleSuspensionMethodResultType {
        SUSPEND_IMMEDIATELY,
        DO_NOT_SUSPEND,
        APPLY_STANDARD_RULES
    }

    private MonitorFeedTradingRuleSuspensionMethodResultType resultType;
    private String reason;

    public MonitorFeedTradingRuleSuspensionMethodResult(MonitorFeedTradingRuleSuspensionMethodResultType resultType,
                    String reason) {
        super();
        this.resultType = resultType;
        this.reason = reason;
    }

    public MonitorFeedTradingRuleSuspensionMethodResultType getResultType() {
        return resultType;
    }

    public String getReason() {
        return reason;
    }



}

