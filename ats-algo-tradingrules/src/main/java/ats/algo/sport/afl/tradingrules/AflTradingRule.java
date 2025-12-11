package ats.algo.sport.afl.tradingrules;

import static ats.algo.sport.afl.tradingrules.AflTradingList.ALL_MARKET;

import java.util.List;

import ats.algo.core.GamePeriod;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.DerivedMarketsInfo;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.afl.AflMatchIncident;
import ats.algo.sport.afl.AflMatchIncident.AflMatchIncidentType;
import ats.algo.sport.afl.AflMatchState;

public class AflTradingRule extends SetSuspensionStatusTradingRule {
    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> halfTimeSuspendMarketList;
    private List<String> fullTimeSuspendMarketList;
    private List<String> beforeFullTimeOpenMarketList;
    private List<String> extraTimeOpenMarketList;
    private int halfTimeSuspend;
    private int halfTimeEnd;
    private int fullTimeEnd;
    private int extraHalfTimeEnd;
    private int extraTimeEnd;
    private int fullTimeSuspend;
    private int minuteSusspend;
    // private int[] min;


    /**
     * get time end for extra half period
     * 
     * @return extraHalfTimeEnd
     */
    public int getExtraHalfTimeEnd() {
        return extraHalfTimeEnd;
    }

    /**
     * set time end for extra half period
     * 
     * @param extraHalfTimeEnd
     */
    public void setExtraHalfTimeEnd(int extraHalfTimeEnd) {
        this.extraHalfTimeEnd = extraHalfTimeEnd;
    }

    /**
     * get time end for extra period
     * 
     * @return extraTimeEnd
     */
    public int getExtraTimeEnd() {
        return extraTimeEnd;
    }

    /**
     * set time end for extra period
     * 
     * @param extraTimeEnd
     */
    public void setExtraTimeEnd(int extraTimeEnd) {
        this.extraTimeEnd = extraTimeEnd;
    }

    /**
     * get markets open before full time
     * 
     * @return beforeFullTimeOpenMarketList
     */
    public List<String> getBeforeFullTimeOpenMarketList() {
        return beforeFullTimeOpenMarketList;
    }

    /**
     * set markets open before full time
     * 
     * @param beforeFullTimeOpenMarketList
     */
    public void setBeforeFullTimeOpenMarketList(List<String> beforeFullTimeOpenMarketList) {
        this.beforeFullTimeOpenMarketList = beforeFullTimeOpenMarketList;
    }

    /**
     * get markets to be open during extra time
     * 
     * @return extraTimeOpenMarketList
     */
    public List<String> getExtraTimeOpenMarketList() {
        return extraTimeOpenMarketList;
    }

    /**
     * set markets to be open during extra time
     * 
     * @param extraTimeOpenMarketList
     */
    public void setExtraTimeOpenMarketList(List<String> extraTimeOpenMarketList) {
        this.extraTimeOpenMarketList = extraTimeOpenMarketList;
    }


    /**
     * get pre-match display market list
     * 
     * @return prematchDisplayMarketList
     */
    public List<String> getPrematchDisplayMarketList() {
        return prematchDisplayMarketList;
    }

    /**
     * set pre-match display market list
     * 
     * @param prematchDisplayMarketList
     */
    public void setPrematchDisplayMarketList(List<String> prematchDisplayMarketList) {
        this.prematchDisplayMarketList = prematchDisplayMarketList;
    }

    /**
     * get in play display market list
     * 
     * @return inplayDisplayMarketList
     */
    public List<String> getInplayDisplayMarketList() {
        return inplayDisplayMarketList;
    }

    /**
     * set in play display market list
     * 
     * @param inplayDisplayMarketList
     */
    public void setInplayDisplayMarketList(List<String> inplayDisplayMarketList) {
        this.inplayDisplayMarketList = inplayDisplayMarketList;
    }

    /**
     * get half time suspend market list
     * 
     * @return halfTimeSuspendMarketList
     */
    public List<String> getHalfTimeSuspendMarketList() {
        return halfTimeSuspendMarketList;
    }

    /**
     * set half time suspend market list
     * 
     * @param halfTimeSuspendMarketList
     */
    public void setHalfTimeSuspendMarketList(List<String> halfTimeSuspendMarketList) {
        this.halfTimeSuspendMarketList = halfTimeSuspendMarketList;
    }

    /**
     * get full time suspend market list
     * 
     * @return fullTimeSuspendMarketList
     */
    public List<String> getFullTimeSuspendMarketList() {
        return fullTimeSuspendMarketList;
    }

    /**
     * set full time suspend market list
     * 
     * @param fullTimeSuspendMarketList
     */
    public void setFullTimeSuspendMarketList(List<String> fullTimeSuspendMarketList) {
        this.fullTimeSuspendMarketList = fullTimeSuspendMarketList;
    }

    /**
     * get half time minutes anchor for suspending markets
     * 
     * @return halfTimeSuspend
     */
    public int getHalfTimeSuspend() {
        return halfTimeSuspend;
    }

    /**
     * set half time minutes anchor for suspending markets according the half time market list
     * 
     * @param halfTimeSuspend
     */
    public void setHalfTimeSuspend(int halfTimeSuspend) {
        this.halfTimeSuspend = halfTimeSuspend;
    }

    /**
     * get full time minutes anchor for suspending markets
     * 
     * @return fullTimeSuspend
     */
    public int getFullTimeSuspend() {
        return fullTimeSuspend;
    }

    /**
     * set full time minutes anchor for suspending markets according the full time market list
     * 
     * @param fullTimeSuspend
     */
    public void setFullTimeSuspend(int fullTimeSuspend) {
        this.fullTimeSuspend = fullTimeSuspend;
    }

    /**
     * get minutes anchor for suspending markets according the minutes suspend market list
     * 
     * @return minuteSusspend
     */
    public int getMinuteSusspend() {
        return minuteSusspend;
    }

    /**
     * set minutes anchor for suspending markets according the minutes suspend market list
     * 
     * @param minuteSusspend
     */
    public void setMinuteSusspend(int minuteSusspend) {
        this.minuteSusspend = minuteSusspend;
    }

    // public int[] getMin() {
    // return min;
    // }
    //
    // public void setMin(int[] min) {
    // this.min = min;
    // }
    /**
     * get the minutes that ends half time
     * 
     * @return halfTimeEnd
     */
    public int getHalfTimeEnd() {
        return halfTimeEnd;
    }

    /**
     * set the minutes that ends half time
     * 
     * @param halfTimeEnd
     */
    public void setHalfTimeEnd(int halfTimeEnd) {
        this.halfTimeEnd = halfTimeEnd;
    }

    /**
     * get the minutes that ends full time
     * 
     * @return fullTimeEnd
     */
    public int getFullTimeEnd() {
        return fullTimeEnd;
    }

    /**
     * set the minutes that ends full time
     * 
     * @param fullTimeEnd
     */
    public void setFullTimeEnd(int fullTimeEnd) {
        this.fullTimeEnd = fullTimeEnd;
    }

    public AflTradingRule() {

    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public AflTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        DerivedMarketsInfo derivedMarketsInfo = market.getDerivedMarketsInfo();
        AflMatchState aflMatchState = (AflMatchState) matchState;
        AflMatchFormat matchFormat = (AflMatchFormat) aflMatchState.getMatchFormat();
        this.setHalfTimeEnd(matchFormat.getNormalTimeMinutes() / 2);
        this.setFullTimeEnd(matchFormat.getNormalTimeMinutes());
        this.setExtraHalfTimeEnd(matchFormat.getExtraTimeMinutes() / 2);
        this.setExtraTimeEnd(matchFormat.getExtraTimeMinutes());
        MatchIncident matchIncident = aflMatchState.getLastMatchIncidentType();
        String marketType = market.getType();
        if (aflMatchState.preMatch()) {
            /*
             * assume if prematchdisplayMarketList is null then applies to all markets
             */
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");
                derivedMarketsInfo.setDataToGenerateStaticMarkets(matchState.getDataToGenerateStaticMarkets());
            }


        } else { // in play
            /*
             * check whether open
             */
            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            } else {

                if (matchIncident.getClass() == AflMatchIncident.class) {
                    /*
                     * in play and market is normally displayed. Check the various un display rules
                     * 
                     * check whether goal suspend rule applies for possible goal, goal, goal not confirmed
                     */
                    @SuppressWarnings("unused")
                    AflMatchIncident lastMatchIncident = (AflMatchIncident) matchIncident;

                } else {
                    int elapsedTime = aflMatchState.getElapsedTimeAtLastMatchIncidentSecs() / 60;
                    GamePeriod gamePeriod = aflMatchState.getGamePeriod();


                    if ((elapsedTime >= (halfTimeEnd - halfTimeSuspend)) && gamePeriod.isBefore(GamePeriod.HALF_TIME)) {
                        if (inList(marketType, halfTimeSuspendMarketList)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend on half markets");
                        }
                    }

                    if (gamePeriod.isAfter(GamePeriod.HALF_TIME)) {
                        marketStatus.setSuspensionStatusRuleName(getRuleName());
                        marketStatus.setSuspensionStatus(SuspensionStatus.OPEN);
                        marketStatus.setSuspensionStatusReason("Open on half markets");
                    }

                    if (elapsedTime >= (fullTimeEnd - fullTimeSuspend)
                                    && (gamePeriod.isBefore(GamePeriod.NORMAL_TIME_END))) {
                        if (inList(marketType, beforeFullTimeOpenMarketList)) {
                            marketStatus.setSuspensionStatusRuleName(getRuleName());
                            marketStatus.setSuspensionStatus(SuspensionStatus.OPEN);
                            marketStatus.setSuspensionStatusReason("Open on before full time market");
                        } else {
                            marketStatus.setSuspensionStatusRuleName(getRuleName());
                            marketStatus.setSuspensionStatus(SuspensionStatus.SUSPENDED_UNDISPLAY);
                            marketStatus.setSuspensionStatusReason("Suspend on before full time market");
                        }
                    }
                    if (elapsedTime >= fullTimeEnd) {
                        if (inList(marketType, fullTimeSuspendMarketList)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "Suspend on full time market");
                        }
                    }
                    if (gamePeriod.isAfter(GamePeriod.NORMAL_TIME_END)) {
                        if (inList(marketType, extraTimeOpenMarketList)) {
                            marketStatus.setSuspensionStatusRuleName(getRuleName());
                            marketStatus.setSuspensionStatus(SuspensionStatus.OPEN);
                            marketStatus.setSuspensionStatusReason("Open on before full time market");
                        }
                        if (elapsedTime >= extraHalfTimeEnd) {
                            // if (inList(marketType, halfTimeExtraTimeSuspendMarketList)) {
                            // marketStatus.setStatusIfHigherPriority(getRuleName(),
                            // SuspensionStatus.SUSPENDED_DISPLAY, "Suspend on extra half time market");
                            // }
                        }
                        if (gamePeriod.isAfter(GamePeriod.EXTRA_TIME_HALF_TIME)) {
                            marketStatus.setSuspensionStatusRuleName(getRuleName());
                            marketStatus.setSuspensionStatus(SuspensionStatus.OPEN);
                            marketStatus.setSuspensionStatusReason("Open on half markets");
                        }
                        if (elapsedTime >= extraTimeEnd) {
                            // if (inList(marketType, fullTimeExtraTimeSuspendMarketList)) {
                            // marketStatus.setStatusIfHigherPriority(getRuleName(),
                            // SuspensionStatus.SUSPENDED_DISPLAY, "Suspend on extra full time market");
                            // }

                        }
                    }
                    // if (gamePeriod.equals(GamePeriod.PENALTY_SHOOTING)) {
                    // if (inList(marketType, penaltyOpenMarketList)) {
                    // marketStatus.setSuspensionStatusRuleName(getRuleName());
                    // marketStatus.setSuspensionStatus(SuspensionStatus.OPEN);
                    // marketStatus.setSuspensionStatusReason("Open on before full time market");
                    // }
                    // }

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
        if (marketTypeList == null) {
            return false;
        }
        if (marketTypeList.contains(marketType)) {
            return true;
        }
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
    private boolean isIncident(MatchIncident lastMatchIncident, AflMatchIncidentType type) {
        if (lastMatchIncident.getIncidentSubType().equals(type))
            return true;
        return false;
    }
}
