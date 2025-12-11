package ats.algo.sport.floorball.tradingrules;

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
import ats.algo.sport.floorball.FloorballMatchState;

public class FloorballTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;

    private static final String ALL_MARKET = "All_MARKET";

    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> acceptedMarketsList;

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

    public List<String> getAcceptedMarketsList() {
        return acceptedMarketsList;
    }

    public void setAcceptedMarketsList(List<String> acceptedMarketsList) {
        this.acceptedMarketsList = acceptedMarketsList;
    }

    public FloorballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {

        MarketStatus marketStatus = market.getMarketStatus();
        String marketType = market.getType();
        FloorballMatchState floorballMatchState = (FloorballMatchState) matchState;
        int elapsedTimeInSec = floorballMatchState.getElapsedTimeSecs();

        if (acceptedMarketsList != null) {
            if (!acceptedMarketsList.isEmpty()) {
                if (!inList(marketType, acceptedMarketsList)) {
                    market.setFilterOutThisMarketForClient(true);
                } else {
                    market.setFilterOutThisMarketForClient(false);
                }
            }
        }

        if (floorballMatchState.preMatch()) {
            /*
             * assume if prematchdisplayMarketList is null then applies to all markets
             */
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");

            }
        } else {
            // if((marketType.equals("FT:AXB")||marketType.equals("FT:DBLC"))&&Math.abs(floorballMatchState.getGoalsA()-floorballMatchState.getGoalsB())<3)
            // {
            // return;
            // }

            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            }

            suspendByMinutes(elapsedTimeInSec, marketType, market, marketStatus);

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

}
