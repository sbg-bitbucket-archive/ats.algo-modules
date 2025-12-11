package ats.algo.sport.football.tradingrules;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.football.FootballMatchState;

public class FootballGoalTradingRule extends SetSuspensionStatusTradingRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int timeToSuspendFollowingGoal;
    private SuspensionStatus statusFollowingGoal;

    public FootballGoalTradingRule() {
        /*
         * set sensible default values
         */
        super();
        timeToSuspendFollowingGoal = 30;
        statusFollowingGoal = SuspensionStatus.SUSPENDED_DISPLAY;
    }

    public int getTimeToSuspendFollowingGoal() {
        return timeToSuspendFollowingGoal;
    }

    public void setTimeToSuspendFollowingGoal(int timeToSuspendFollowingGoal) {
        this.timeToSuspendFollowingGoal = timeToSuspendFollowingGoal;
    }

    public SuspensionStatus getStatusFollowingGoal() {
        return statusFollowingGoal;
    }

    public void setStatusFollowingGoal(SuspensionStatus statusFollowingGoal) {
        this.statusFollowingGoal = statusFollowingGoal;
    }

    /**
     * 
     * @param ruleName
     * @param rulePriority
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     * @param marketType
     */
    public FootballGoalTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        timeToSuspendFollowingGoal = 30;
        statusFollowingGoal = SuspensionStatus.SUSPENDED_DISPLAY;
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        SuspensionStatus targetStatus;
        String reason;
        FootballMatchState footballMatchState = (FootballMatchState) matchState;
        int secsSinceLastGoal = footballMatchState.secsSinceLastGoal();
        if (secsSinceLastGoal > -1 && (secsSinceLastGoal < timeToSuspendFollowingGoal)) {
            targetStatus = statusFollowingGoal;
            reason = String.format("Time since goal scored %d secs less than suspension threshold %d secs",
                            secsSinceLastGoal, timeToSuspendFollowingGoal);
            marketStatus.setStatusIfHigherPriority(getRuleName(), targetStatus, reason);
        }

    }

    @Override
    public String toString() {
        return "FootballGoalTradingRule [timeToSuspendFollowingGoal=" + timeToSuspendFollowingGoal
                        + ", statusFollowingGoal=" + statusFollowingGoal + ", getRuleType()=" + getRuleType()
                        + ", getRuleName()=" + getRuleName() + ", getEventTier()=" + getEventTier()
                        + ", getMarketGroup()=" + getMarketGroup() + "]";
    }

}
