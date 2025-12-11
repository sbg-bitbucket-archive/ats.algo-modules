package ats.algo.sport.darts.tradingrules;

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
import ats.algo.sport.darts.DartMatchState;

public class DartTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;



    public void setPrematchDisplayMarketList(List<String> prematchDisplayMarketList) {
        this.prematchDisplayMarketList = prematchDisplayMarketList;
    }


    public void setInplayDisplayMarketList(List<String> inplayDisplayMarketList) {
        this.inplayDisplayMarketList = inplayDisplayMarketList;
    }



    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public DartTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        DartMatchState dartMatchState = (DartMatchState) matchState;
        String marketType = market.getType();
        if (dartMatchState.preMatch()) {
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");
            }
        } else {// in play
            /*
             * check whether open
             */
            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            } else {


                if (dartMatchState.checkoutPossible()) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Suspend if checkout from this leg possible");
                }

                if (dartMatchState.maybeFinalLeg()) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Suspend if this may be final leg of the match");
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
