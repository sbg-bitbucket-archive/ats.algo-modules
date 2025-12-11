package ats.algo.sport.snooker.tradingrules;

import java.util.HashMap;
import java.util.List;
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
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.snooker.SnookerMatchState;

public class SnookerTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> matchSuspendMarketList;
    private double standardMinProbabilityForSuspension;
    private double standardMaxProbabilityForSuspension;
    private Map<String, Double[]> overrideProbabilitySuspensionList;

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

    public List<String> getMatchSuspendMarketList() {
        return matchSuspendMarketList;
    }

    public void setMatchSuspendMarketList(List<String> matchSuspendMarketList) {
        this.matchSuspendMarketList = matchSuspendMarketList;
    }

    public Map<String, Double[]> getOverrideProbabilitySuspensionList() {
        return overrideProbabilitySuspensionList;
    }

    public void setOverrideProbabilitySuspensionList(Map<String, Double[]> overrideProbabilitySuspensionList) {
        this.overrideProbabilitySuspensionList = overrideProbabilitySuspensionList;
    }

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
    public SnookerTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        overrideProbabilitySuspensionList = new HashMap<String, Double[]>();
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        SnookerMatchState snookerMatchState = (SnookerMatchState) matchState;
        String marketType = market.getType();

        if (market.getSelectionsProbs().size() == 2 || market.getSelectionsProbs().size() == 3) {
            for (Entry<String, Double[]> marketFromList : overrideProbabilitySuspensionList.entrySet()) {
                if (marketFromList.getKey().equals(market.getType())) {
                    for (double prob : market.getSelectionsProbs().values()) {
                        double probMin = marketFromList.getValue()[0];
                        double probMax = marketFromList.getValue()[1];

                        if (prob < probMin || prob > probMax) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "SD - Market Selections are outside min/max");
                        }
                    }
                } else {
                    for (double prob : market.getSelectionsProbs().values()) {
                        if (prob <= standardMinProbabilityForSuspension
                                        || prob >= standardMaxProbabilityForSuspension) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "SU - Market Selections are outside min/max");
                        }
                    }
                }
            }


        }
        if (snookerMatchState.preMatch()) {
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

                if (oneMoreFrameToWin(snookerMatchState) && inList(marketType, matchSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Susspend market when a player only needs 1 frame to win the match");
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

    private boolean oneMoreFrameToWin(SnookerMatchState snookerMatchState) {
        SnookerMatchFormat snookerMatchFormat = (SnookerMatchFormat) snookerMatchState.getMatchFormat();

        int nFramesInMatch = (snookerMatchFormat.getnFramesInMatch() + 1) / 2 - 1;
        if (snookerMatchState.getFramesA() == nFramesInMatch)
            return true;
        if (snookerMatchState.getFramesB() == nFramesInMatch)
            return true;
        return false;
    }

}
