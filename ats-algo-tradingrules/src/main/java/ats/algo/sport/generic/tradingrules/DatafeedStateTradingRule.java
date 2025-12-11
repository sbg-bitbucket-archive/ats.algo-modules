package ats.algo.sport.generic.tradingrules;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;

public class DatafeedStateTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;

    /*
     * 
     */
    public DatafeedStateTradingRule() {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, "Datafeed state rule", null, null);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        DatafeedMatchIncidentType datafeedStatus = matchState.getDataFeedStatus();
        if (datafeedStatus == null) {
            /*
             * do nothing
             */
            return;
        }
        switch (datafeedStatus) {
            case BET_STOP:
            case CANCELLED_COVERAGE: {
                MarketStatus marketStatus = market.getMarketStatus();
                SuspensionStatus targetStatus = SuspensionStatus.SUSPENDED_DISPLAY;
                String statusReason = String.format("Datafeed state is: %s", datafeedStatus);
                marketStatus.setStatusIfHigherPriority(super.getRuleName(), targetStatus, statusReason);
            }
                break;
            case OK:
            case BET_START:
            case SCOUT_DISCONNECT:
            default:
                /*
                 * do nothing
                 */
                break;
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "DatafeedStateTradingRule [getRuleType()=" + getRuleType() + ", getRuleName()=" + getRuleName()
                        + ", getEventTier()=" + getEventTier() + ", getMarketGroup()=" + getMarketGroup() + "]";
    }

}
