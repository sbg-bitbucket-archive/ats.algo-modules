package ats.algo.sport.cricket.tradingrules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.cricket.CricketMatchFormat;
import ats.algo.sport.cricket.CricketMatchState;

public class CricketTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    public static final String ALL_MARKET = "All_MARKET";
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> finalOverDisplayMarketList;
    private Map<String, Double[]> overrideProbabilitySuspensionList = new HashMap<String, Double[]>();
    private double standardMinProbabilityForSuspension;
    private double standardMaxProbabilityForSuspension;

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

    public Map<String, Double[]> getOverrideProbabilitySuspensionList() {
        return overrideProbabilitySuspensionList;
    }

    public void setOverrideProbabilitySuspensionList(Map<String, Double[]> overrideProbabilitySuspensionList) {
        this.overrideProbabilitySuspensionList = overrideProbabilitySuspensionList;
    }

    public List<String> getFinalOverDisplayMarketList() {
        return finalOverDisplayMarketList;
    }

    public void setFinalOverDisplayMarketList(List<String> finalOverDisplayMarketList) {
        this.finalOverDisplayMarketList = finalOverDisplayMarketList;
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
    public CricketTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    public void addOverrideProbabilitySuspensionList(String marketListForOverrideProbability, Double[] rangeOfProbs) {
        overrideProbabilitySuspensionList.put(marketListForOverrideProbability, rangeOfProbs);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        CricketMatchState cricketMatchState = (CricketMatchState) matchState;
        CricketMatchFormat cricketMatchFormat = (CricketMatchFormat) cricketMatchState.getMatchFormat();
        String marketType = market.getType();
        if (market.getSelectionsProbs().size() == 2 || market.getSelectionsProbs().size() == 3) {
            for (Entry<String, Double[]> marketFromList : overrideProbabilitySuspensionList.entrySet()) {
                if (marketFromList.getKey().equals(market.getType())) {
                    for (double prob : market.getSelectionsProbs().values()) {
                        double probMin = marketFromList.getValue()[0];
                        double probMax = marketFromList.getValue()[1];

                        if (prob < probMin || prob > probMax) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "SD - Market Selections are outside min/max");
                        }
                    }
                }
            }

        }
        if (cricketMatchState.preMatch()) {
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");
            }
        } else { // in play
            /*
             * check whether open
             */
            int oversA = cricketMatchState.getOversA();
            int oversB = cricketMatchState.getOversB();
            if ((cricketMatchState.getBat() == TeamId.A && oversA == cricketMatchFormat.getnOversinMatch())
                            || (cricketMatchState.getBat() == TeamId.B
                                            && oversB == cricketMatchFormat.getnOversinMatch())) {
                if (finalOverDisplayMarketList != null && !inList(marketType, finalOverDisplayMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Not Open in final over");
                }
            }
            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
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
        if (marketTypeList.size() == 1 && marketTypeList.get(0).equals(ALL_MARKET)) {
            return true;
        }
        for (String marketType2 : marketTypeList)
            if (marketType.equals(marketType2))
                return true;
        return false;
    }

}
