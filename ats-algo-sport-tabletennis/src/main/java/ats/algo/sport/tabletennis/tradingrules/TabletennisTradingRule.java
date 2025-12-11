package ats.algo.sport.tabletennis.tradingrules;

import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.tabletennis.TabletennisMatchState;

public class TabletennisTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> gamePointsSuspendMarketList;
    private List<String> finalGamePoint8OpenMarketList;

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



    public List<String> getGamePointsSuspendMarketList() {
        return gamePointsSuspendMarketList;
    }

    public void setGamePointsSuspendMarketList(List<String> gamePointsSuspendMarketList) {
        this.gamePointsSuspendMarketList = gamePointsSuspendMarketList;
    }



    public List<String> getFinalGamePoint8OpenMarketList() {
        return finalGamePoint8OpenMarketList;
    }

    public void setFinalGamePoint8OpenMarketList(List<String> finalGamePoint8OpenMarketList) {
        this.finalGamePoint8OpenMarketList = finalGamePoint8OpenMarketList;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     */
    public TabletennisTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        TabletennisMatchState tabletennisMatchState = (TabletennisMatchState) matchState;
        String marketType = market.getType();
        if (market.getFullKey().equals("FT:ML_M")) {
            if (!tabletennisMatchState.isTieBreak() && !tabletennisMatchState.isFinalGamePoint(8))
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open unless tie-break");
            else {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                                "Open in tie-break or final 8 points");
            }

        }


        if (tabletennisMatchState.preMatch()) {
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
            }

            if (tabletennisMatchState.isGamePoint()) {// &&inList(marketType,gamePointsSuspendMarketList)
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Game Point Suspending Market");
            }

            if (tabletennisMatchState.isPossibleFinalGame() && !tabletennisMatchState.isTieBreak()
                            && marketType.equals("FT:ML") && !tabletennisMatchState.isFinalGamePoint(8)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Final Game Rule");
            }

            if (tabletennisMatchState.isFinalGamePoint(8)) {
                if (!inList(marketType, finalGamePoint8OpenMarketList))
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Final Game point 8 Rule");
                else {
                    marketStatus.reset();
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                                    "Final Game point 8 Rule");

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
        return false;
    }
}
