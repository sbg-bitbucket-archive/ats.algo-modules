package ats.algo.sport.americanfootball.tradingrules;

import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.AmericanfootballMatchIncidentType;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;

public class AmericanFootballTradingRule extends SetSuspensionStatusTradingRule {
    private static final long serialVersionUID = 1L;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> goalSuspendMarketList;
    private List<String> cornerSuspendMarketList;
    private List<String> redCardSuspendMarketList;
    private List<String> yellowCardSuspendMarketList;
    private List<String> penaltySuspendMarketList;
    private List<String> halfTimeSuspendMarketList;
    // private List<List<String>> minuteSuspendMatketList;
    private List<String> fullTimeSuspendMarketList;
    private List<String> fiveMinuteSuspendMarketList;
    private List<String> tenMinuteSuspendMarketList;
    private List<String> fifteenMinuteSuspendMarketList;
    private List<String> beforeFullTimeSuspendMarketList;
    private int halfTimeSuspend;
    private int halfTimeEnd;
    private int fullTimeEnd;
    private int fullTimeSuspend;
    private int minuteSusspend;
    private int[] min;

    public List<String> getPrematchDisplayMarketList() {
        return prematchDisplayMarketList;
    }

    public void setPrematchDisplayMarketList(List<String> prematchDisplayMarketList) {
        this.prematchDisplayMarketList = prematchDisplayMarketList;
    }

    public List<String> getInplayDisplayMarketList() {
        return inplayDisplayMarketList;
    }

    public void setInplayDisplayMarketList(List<String> inplayDisplayMarketList) {
        this.inplayDisplayMarketList = inplayDisplayMarketList;
    }

    public List<String> getGoalSuspendMarketList() {
        return goalSuspendMarketList;
    }

    public void setGoalSuspendMarketList(List<String> goalSuspendMarketList) {
        this.goalSuspendMarketList = goalSuspendMarketList;
    }

    public List<String> getCornerSuspendMarketList() {
        return cornerSuspendMarketList;
    }

    public void setCornerSuspendMarketList(List<String> cornerSuspendMarketList) {
        this.cornerSuspendMarketList = cornerSuspendMarketList;
    }

    public List<String> getRedCardSuspendMarketList() {
        return redCardSuspendMarketList;
    }

    public void setRedCardSuspendMarketList(List<String> redCardSuspendMarketList) {
        this.redCardSuspendMarketList = redCardSuspendMarketList;
    }

    public List<String> getYellowCardSuspendMarketList() {
        return yellowCardSuspendMarketList;
    }

    public void setYellowCardSuspendMarketList(List<String> yellowCardSuspendMarketList) {
        this.yellowCardSuspendMarketList = yellowCardSuspendMarketList;
    }

    public List<String> getPenaltySuspendMarketList() {
        return penaltySuspendMarketList;
    }

    public void setPenaltySuspendMarketList(List<String> penaltySuspendMarketList) {
        this.penaltySuspendMarketList = penaltySuspendMarketList;
    }

    public List<String> getHalfTimeSuspendMarketList() {
        return halfTimeSuspendMarketList;
    }

    public void setHalfTimeSuspendMarketList(List<String> halfTimeSuspendMarketList) {
        this.halfTimeSuspendMarketList = halfTimeSuspendMarketList;
    }

    public List<String> getFullTimeSuspendMarketList() {
        return fullTimeSuspendMarketList;
    }

    public void setFullTimeSuspendMarketList(List<String> fullTimeSuspendMarketList) {
        this.fullTimeSuspendMarketList = fullTimeSuspendMarketList;
    }

    public List<String> getFiveMinuteSuspendMarketList() {
        return fiveMinuteSuspendMarketList;
    }

    public void setFiveMinuteSuspendMarketList(List<String> fiveMinuteSuspendMarketList) {
        this.fiveMinuteSuspendMarketList = fiveMinuteSuspendMarketList;
    }

    public List<String> getBeforeFullTimeSuspendMarketList() {
        return beforeFullTimeSuspendMarketList;
    }

    public void setBeforeFullTimeSuspendMarketList(List<String> beforeFullTimeSuspendMarketList) {
        this.beforeFullTimeSuspendMarketList = beforeFullTimeSuspendMarketList;
    }

    public List<String> getTenMinuteSuspendMarketList() {
        return tenMinuteSuspendMarketList;
    }

    public void setTenMinuteSuspendMarketList(List<String> tenMinuteSuspendMarketList) {
        this.tenMinuteSuspendMarketList = tenMinuteSuspendMarketList;
    }

    public List<String> getFifteenMinuteSuspendMarketList() {
        return fifteenMinuteSuspendMarketList;
    }

    public void setFifteenMinuteSuspendMarketList(List<String> fifteenMinuteSuspendMarketList) {
        this.fifteenMinuteSuspendMarketList = fifteenMinuteSuspendMarketList;
    }

    public int getHalfTimeSuspend() {
        return halfTimeSuspend;
    }

    public void setHalfTimeSuspend(int halfTimeSuspend) {
        this.halfTimeSuspend = halfTimeSuspend;
    }

    public int getFullTimeSuspend() {
        return fullTimeSuspend;
    }

    public void setFullTimeSuspend(int fullTimeSuspend) {
        this.fullTimeSuspend = fullTimeSuspend;
    }

    public int getMinuteSusspend() {
        return minuteSusspend;
    }

    public void setMinuteSusspend(int minuteSusspend) {
        this.minuteSusspend = minuteSusspend;
    }

    public int[] getMin() {
        return min;
    }

    public void setMin(int[] min) {
        this.min = min;
    }

    public int getHalfTimeEnd() {
        return halfTimeEnd;
    }

    public void setHalfTimeEnd(int halfTimeEnd) {
        this.halfTimeEnd = halfTimeEnd;
    }

    public int getFullTimeEnd() {
        return fullTimeEnd;
    }

    public void setFullTimeEnd(int fullTimeEnd) {
        this.fullTimeEnd = fullTimeEnd;
    }

    public AmericanFootballTradingRule() {

    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public AmericanFootballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup,
                    Integer marketTier) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        AmericanfootballMatchState americanfootballMatchState = (AmericanfootballMatchState) matchState;
        // MatchIncident matchIncident = americanfootballMatchState.getLastMatchIncidentType();
        String marketType = market.getType();
        if (americanfootballMatchState.preMatch()) {
            /*
             * assume if prematchdisplayMarketList is null then applies to all markets
             */
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");
            }
        } else { // in play
            /*
             * check whether open
             */
            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            } else {

                // if (matchIncident.getClass() == AmericanfootballMatchIncident.class) {
                // /*
                // * in play and market is normally displayed. Check the
                // * various undisplay rules
                // *
                // * check whether goal suppend rule applies for possible
                // * goal, goal, goal not confirmed
                // */
                // AmericanfootballMatchIncident lastMatchIncident = (AmericanfootballMatchIncident) matchIncident;
                // if (isIncident(lastMatchIncident, AmericanfootballMatchIncidentType.FIELD_GOAL)
                // && inList(marketType, goalSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                // "Suspend on possible goal");
                // }
                // if ((isIncident(lastMatchIncident, AmericanfootballMatchIncidentType.TOUCH_DOWN))
                // && inList(marketType, goalSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                // "Open after the goal confirmed or not");
                // }
                //
                //
                //
                //
                // } else {
                // ElapsedTimeMatchIncident elapsedTimeMatchIncident = (ElapsedTimeMatchIncident) matchIncident;
                // int elapsedTime = elapsedTimeMatchIncident.getElapsedTimeSecs() / 60;
                // minuteSuspendMatketList = new ArrayList<List<String>>(min.length);
                // minuteSuspendMatketList.add(fiveMinuteSuspendMarketList);
                // minuteSuspendMatketList.add(tenMinuteSuspendMarketList);
                // minuteSuspendMatketList.add(fifteenMinuteSuspendMarketList);
                // for (int m = 0; m < min.length; m++) {
                // if ((elapsedTime % min[m]) >= (min[m] - minuteSusspend)) {
                // if (inList(marketType, minuteSuspendMatketList.get(m))) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(),
                // SuspensionStatus.SUSPENDED_DISPLAY, "Suspend on %d min market" + min[m]);
                // }
                // }
                //
                // if ((elapsedTime % min[m]) == 0) {
                // if (inList(marketType, minuteSuspendMatketList.get(m))) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                // "Open on %d min market" + min[m]);
                // }
                // }
                //
                // }
                //
                // if (elapsedTime >= (halfTimeEnd - halfTimeSuspend)) {
                // if (inList(marketType, halfTimeSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                // "Suspend on half markets");
                // System.out.println(elapsedTime+halfTimeEnd+"--"+halfTimeSuspend);
                // }
                // }
                //
                // if (elapsedTime >= halfTimeEnd) {
                // if (inList(marketType, halfTimeSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                // "Open on half markets");
                // System.out.println(elapsedTime+halfTimeEnd+"--"+halfTimeSuspend);
                // }
                // }
                //
                // if (elapsedTime > (fullTimeEnd - fullTimeSuspend)) {
                // if (inList(marketType, beforeFullTimeSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                // "Suspend on before full time market");
                // }
                // }
                // if (elapsedTime >= fullTimeEnd) {
                // if (inList(marketType, fullTimeSuspendMarketList)) {
                // marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                // "Suspend on full time market");
                // }
                // }
                //
                // }
                //
            }
        }
    }

    /**
     * returns true if marketType appears in marketTypeList
     * 
     * @param marketType
     * @param marketTypeList
     * @return
     */
    private boolean inList(String marketType, List<String> marketTypeList) {
        if (marketTypeList == null)
            return false;
        for (String marketType2 : marketTypeList)
            if (marketType.equals(marketType2))
                return true;
        if (marketTypeList.size() == 1 && marketTypeList.get(0).equals(ALL_MARKET)) {
            return true;
        }
        return false;
    }

    /**
     * returns true if match incident
     * 
     * @param marketType
     * @param marketTypeList
     * @return
     */
    @SuppressWarnings("unused")
    private boolean isIncident(MatchIncident lastMatchIncident, AmericanfootballMatchIncidentType type) {
        if (lastMatchIncident.getIncidentSubType().equals(type))
            return true;
        return false;
    }
}
