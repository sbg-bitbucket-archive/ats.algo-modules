package ats.algo.sport.generic.tradingrules;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;

public class ElapsedTimeTradingRule extends SetSuspensionStatusTradingRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SuspensionStatus defaultStatusPreMatch;
    private SuspensionStatus defaultStatusInPlay;
    private SuspensionStatus statusInAmberZone;
    private SuspensionStatus statusInRedZone;
    private int amberZoneSecsBeforePeriodEnd;
    private int redZoneSecsBeforePeriodEnd;

    public SuspensionStatus getDefaultStatusPreMatch() {
        return defaultStatusPreMatch;
    }

    public void setDefaultStatusPreMatch(SuspensionStatus defaultStatusPreMatch) {
        this.defaultStatusPreMatch = defaultStatusPreMatch;
    }

    public SuspensionStatus getDefaultStatusInPlay() {
        return defaultStatusInPlay;
    }

    public void setDefaultStatusInPlay(SuspensionStatus defaultStatusInPlay) {
        this.defaultStatusInPlay = defaultStatusInPlay;
    }

    public SuspensionStatus getStatusInAmberZone() {
        return statusInAmberZone;
    }

    public void setStatusInAmberZone(SuspensionStatus statusInAmberZone) {
        this.statusInAmberZone = statusInAmberZone;
    }

    public SuspensionStatus getStatusInRedZone() {
        return statusInRedZone;
    }

    public void setStatusInRedZone(SuspensionStatus statusInRedZone) {
        this.statusInRedZone = statusInRedZone;
    }

    public int getAmberZoneSecsBeforePeriodEnd() {
        return amberZoneSecsBeforePeriodEnd;
    }

    public void setAmberZoneSecsBeforePeriodEnd(int amberZoneSecsBeforePeriodEnd) {
        this.amberZoneSecsBeforePeriodEnd = amberZoneSecsBeforePeriodEnd;
    }

    public int getRedZoneSecsBeforePeriodEnd() {
        return redZoneSecsBeforePeriodEnd;
    }

    public void setRedZoneSecsBeforePeriodEnd(int redZoneSecsBeforePeriodEnd) {
        this.redZoneSecsBeforePeriodEnd = redZoneSecsBeforePeriodEnd;
    }

    /*
     * required by Json
     */
    public ElapsedTimeTradingRule() {

    }

    /**
     * 
     * @param supportedSportType
     * @param ruleName
     * @param rulePriority
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     * @param marketType
     */
    public ElapsedTimeTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        this.defaultStatusPreMatch = SuspensionStatus.OPEN;
        this.defaultStatusInPlay = SuspensionStatus.OPEN;
        this.statusInAmberZone = SuspensionStatus.SUSPENDED_DISPLAY;
        this.statusInRedZone = SuspensionStatus.SUSPENDED_UNDISPLAY;;
        this.amberZoneSecsBeforePeriodEnd = 600;
        this.redZoneSecsBeforePeriodEnd = 0;
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        SuspensionStatus targetStatus;
        String reason;
        if (matchState.preMatch()) {
            targetStatus = defaultStatusPreMatch;
            reason = "Default state pre-match";
        } else {
            int timeLeftInPeriod = matchState.secsLeftInCurrentPeriod();
            if (timeLeftInPeriod <= redZoneSecsBeforePeriodEnd) {
                targetStatus = statusInRedZone;
                reason = String.format("Time left in period: %d less than red zone threshold %d", timeLeftInPeriod,
                                redZoneSecsBeforePeriodEnd);
            } else if (timeLeftInPeriod <= amberZoneSecsBeforePeriodEnd) {
                targetStatus = statusInAmberZone;
                reason = String.format("Time left in period: %d less than amber threshold %d", timeLeftInPeriod,
                                amberZoneSecsBeforePeriodEnd);
            } else {
                targetStatus = defaultStatusInPlay;
                reason = "Default state in-play";
            }
        }
        marketStatus.setStatusIfHigherPriority(getRuleName(), targetStatus, reason);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "ElapsedTimeTradingRule [defaultStatusPreMatch=" + defaultStatusPreMatch + ", defaultStatusInPlay="
                        + defaultStatusInPlay + ", statusInAmberZone=" + statusInAmberZone + ", statusInRedZone="
                        + statusInRedZone + ", amberZoneSecsBeforePeriodEnd=" + amberZoneSecsBeforePeriodEnd
                        + ", redZoneSecsBeforePeriodEnd=" + redZoneSecsBeforePeriodEnd + ", getRuleType()="
                        + getRuleType() + ", getRuleName()=" + getRuleName() + ", getEventTier()=" + getEventTier()
                        + ", getMarketGroup()=" + getMarketGroup() + "]";
    }

}
