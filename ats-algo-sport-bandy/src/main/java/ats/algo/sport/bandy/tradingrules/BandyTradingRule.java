package ats.algo.sport.bandy.tradingrules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.bandy.BandyMatchState;

public class BandyTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private Map<String, Double[]> overrideProbabilitySuspensionList;
    private double standardMinProbabilityForSuspension;
    private double standardMaxProbabilityForSuspension;



    public void addOverrideProbabilitySuspensionList(String marketListForOverrideProbability, Double[] rangeOfProbs) {
        overrideProbabilitySuspensionList.put(marketListForOverrideProbability, rangeOfProbs);
    }

    public double getStandardMinProbabilityForSuspension() {
        return standardMinProbabilityForSuspension;
    }

    public void setStandardMinProbabilityForSuspension(double standardMinProbabilityForSuspension) {
        this.standardMinProbabilityForSuspension = standardMinProbabilityForSuspension;
    }

    public double getStandardMaxProbabilityForSuspension() {
        return standardMaxProbabilityForSuspension;
    }

    public void setStandardMaxProbabilityForSuspension(double standardMaxProbabilityForSuspension) {
        this.standardMaxProbabilityForSuspension = standardMaxProbabilityForSuspension;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public BandyTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        overrideProbabilitySuspensionList = new HashMap<String, Double[]>();
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        BandyMatchState bandyMatchState = (BandyMatchState) matchState;
        String marketType = market.getType();
        int elapsedTimeInSec = bandyMatchState.getElapsedTimeSecs();
        if (bandyMatchState.preMatch()) {
        } else { // in play
            /*
             * check whether open
             */
            suspendByMinutes(elapsedTimeInSec, marketType, market, marketStatus);
            if (market.getSelectionsProbs().size() == 2 || market.getSelectionsProbs().size() == 3) {
                for (Entry<String, Double[]> marketFromList : overrideProbabilitySuspensionList.entrySet()) {
                    if (marketFromList.getKey().equals(market.getType())) {
                        for (double prob : market.getSelectionsProbs().values()) {
                            double probMin = marketFromList.getValue()[0];
                            double probMax = marketFromList.getValue()[1];

                            if (prob < probMin || prob > probMax) {
                                marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                SuspensionStatus.SUSPENDED_DISPLAY,
                                                "SD - Market Selections are outside min/max");
                            }
                        }
                    } else {
                        for (double prob : market.getSelectionsProbs().values()) {
                            if (prob <= standardMinProbabilityForSuspension
                                            || prob >= standardMaxProbabilityForSuspension) {
                                marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                SuspensionStatus.SUSPENDED_UNDISPLAY,
                                                "SU - Market Selections are outside min/max");
                            }
                        }
                    }
                }

            }
        }
    }
}
