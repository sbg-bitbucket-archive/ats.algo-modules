package ats.algo.sport.basketball.tradingrules;

import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.basketball.BasketballMatchIncident.BasketballMatchIncidentType;
import ats.algo.sport.basketball.BasketballMatchState;

public class BasketballTradingRule extends SetSuspensionStatusTradingRule {
    private static final long serialVersionUID = 1L;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> finalFiveMinuteMatchSuspendList;
    private List<String> finalTwoMinuteMatchSuspendList;
    private List<String> firstHalfFiveMinuteMatchSuspendList;
    private List<String> firstHalfTwoMinuteMatchSuspendList;
    private List<String> OTOneMinuteSuspendList;
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

    public List<String> getFinalFiveMinuteMatchSuspendList() {
        return finalFiveMinuteMatchSuspendList;
    }

    public void setFinalFiveMinuteMatchSuspendList(List<String> finalFiveMinuteMatchSuspendList) {
        this.finalFiveMinuteMatchSuspendList = finalFiveMinuteMatchSuspendList;
    }

    public List<String> getFinalTwoMinuteMatchSuspendList() {
        return finalTwoMinuteMatchSuspendList;
    }

    public void setFinalTwoMinuteMatchSuspendList(List<String> finalTwoMinuteMatchSuspendList) {
        this.finalTwoMinuteMatchSuspendList = finalTwoMinuteMatchSuspendList;
    }

    public List<String> getFirstHalfFiveMinuteMatchSuspendList() {
        return firstHalfFiveMinuteMatchSuspendList;
    }

    public void setfirstHalfFiveMinuteMatchSuspendList(List<String> firstHalfFiveMinuteMatchSuspendList) {
        this.firstHalfFiveMinuteMatchSuspendList = firstHalfFiveMinuteMatchSuspendList;
    }

    public List<String> getFirstHalfTwoMinuteMatchSuspendList() {
        return firstHalfTwoMinuteMatchSuspendList;
    }

    public void setFirstHalfTwoMinuteMatchSuspendList(List<String> firstHalfTwoMinuteMatchSuspendList) {
        this.firstHalfTwoMinuteMatchSuspendList = firstHalfTwoMinuteMatchSuspendList;
    }

    public List<String> getOTOneMinuteSuspendList() {
        return OTOneMinuteSuspendList;
    }

    public void setOTOneMinuteSuspendList(List<String> OTOneMinuteSuspendList) {
        this.OTOneMinuteSuspendList = OTOneMinuteSuspendList;
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

    public BasketballTradingRule() {

    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public BasketballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        int quarterLengths = 10;
        MarketStatus marketStatus = market.getMarketStatus();
        BasketballMatchState basketballMatchState = (BasketballMatchState) matchState;
        BasketballMatchFormat basketballMatchFormat = (BasketballMatchFormat) basketballMatchState.getMatchFormat();

        if (basketballMatchFormat.getNormalTimeMinutes() == 48)
            quarterLengths = 12;
        // MatchIncident matchIncident = basketballMatchState.getLastMatchIncidentType();
        String marketType = market.getType();
        if (basketballMatchState.preMatch()) {
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

                if ((basketballMatchState.getElapsedTimeSecs() <= ((4 * quarterLengths) * 60))
                                && (basketballMatchState.getElapsedTimeSecs() >= ((4 * quarterLengths - 5) * 60))) {
                    if (inplayDisplayMarketList != null && inList(marketType, finalFiveMinuteMatchSuspendList)) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "5 minutes to go suspensions logic");

                    }
                }
                if ((basketballMatchState.getElapsedTimeSecs() <= ((4 * quarterLengths) * 60))
                                && (basketballMatchState.getElapsedTimeSecs() >= ((4 * quarterLengths - 2) * 60))) {
                    if (inplayDisplayMarketList != null && inList(marketType, finalTwoMinuteMatchSuspendList)) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "2 minutes to go suspension logic");
                    }
                }
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
    private boolean isIncident(MatchIncident lastMatchIncident, BasketballMatchIncidentType type) {
        if (lastMatchIncident.getIncidentSubType().equals(type))
            return true;
        return false;
    }
}
