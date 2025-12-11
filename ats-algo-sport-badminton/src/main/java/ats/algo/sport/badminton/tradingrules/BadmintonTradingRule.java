package ats.algo.sport.badminton.tradingrules;

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
import ats.algo.sport.badminton.BadmintonMatchState;

public class BadmintonTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> gamePointSuspendMarketList;
    private List<String> finalPointSuspendMarketList;
    private List<String> totalPointSuspendMarketList;
    private List<String> raceSuspendMarketList;
    private List<String> leadSuspendMarketList;
    private List<String> nextGameSuspendMarketList;
    private Map<String, Double[]> overrideProbabilitySuspensionList;
    private double standardMinProbabilityForSuspension;
    private double standardMaxProbabilityForSuspension;
    private int pointTotalForSuspensionOfMarket;
    private List<String> pointTotalDisplayMarketList;
    private List<String> secondGameSuspendMarketList;


    public List<String> getSecondGameSuspendMarketList() {
        return secondGameSuspendMarketList;
    }

    public void setSecondGameSuspendMarketList(List<String> secondGameSuspendMarketList) {
        this.secondGameSuspendMarketList = secondGameSuspendMarketList;
    }

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

    public List<String> getGamePointSuspendMarketList() {
        return gamePointSuspendMarketList;
    }

    public void setGamePointSuspendMarketList(List<String> gamePointSuspendMarketList) {
        this.gamePointSuspendMarketList = gamePointSuspendMarketList;
    }

    public List<String> getFinalPointSuspendMarketList() {
        return finalPointSuspendMarketList;
    }

    public void setFinalPointSuspendMarketList(List<String> finalPointSuspendMarketList) {
        this.finalPointSuspendMarketList = finalPointSuspendMarketList;
    }

    public List<String> getTotalPointSuspendMarketList() {
        return totalPointSuspendMarketList;
    }

    public void setTotalPointSuspendMarketList(List<String> totalPointSuspendMarketList) {
        this.totalPointSuspendMarketList = totalPointSuspendMarketList;
    }

    public List<String> getRaceSuspendMarketList() {
        return raceSuspendMarketList;
    }

    public void setRaceSuspendMarketList(List<String> raceSuspendMarketList) {
        this.raceSuspendMarketList = raceSuspendMarketList;
    }

    public List<String> getLeadSuspendMarketList() {
        return leadSuspendMarketList;
    }

    public void setLeadSuspendMarketList(List<String> leadSuspendMarketList) {
        this.leadSuspendMarketList = leadSuspendMarketList;
    }

    public List<String> getNextGameSuspendMarketList() {
        return nextGameSuspendMarketList;
    }

    public void setNextGameSuspendMarketList(List<String> nextGameSuspendMarketList) {
        this.nextGameSuspendMarketList = nextGameSuspendMarketList;
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

    public int getPointTotalForSuspensionOfMarket() {
        return pointTotalForSuspensionOfMarket;
    }

    public void setPointTotalForSuspensionOfMarket(int pointTotalForSuspensionOfMarket) {
        this.pointTotalForSuspensionOfMarket = pointTotalForSuspensionOfMarket;
    }

    public List<String> getPointTotalDisplayMarketList() {
        return pointTotalDisplayMarketList;
    }

    public void setPointTotalDisplayMarketList(List<String> pointTotalDisplayMarketList) {
        this.pointTotalDisplayMarketList = pointTotalDisplayMarketList;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public BadmintonTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        overrideProbabilitySuspensionList = new HashMap<String, Double[]>();
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        BadmintonMatchState badmintonMatchState = (BadmintonMatchState) matchState;
        String marketType = market.getType();
        if (badmintonMatchState.preMatch()) {
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
            if (badmintonMatchState.getGameNo(0) >= 1 && inList(marketType, secondGameSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in second game");
            }


            if (badmintonMatchState.getPointsA() >= pointTotalForSuspensionOfMarket
                            || badmintonMatchState.getPointsB() >= pointTotalForSuspensionOfMarket) {
                if (pointTotalDisplayMarketList != null && !inList(marketType, pointTotalDisplayMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "SU when one player is >= " + pointTotalForSuspensionOfMarket);
                } else {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Just Suspend when one player is >= " + pointTotalForSuspensionOfMarket);
                }

            }

        }

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

        int[] setGameNo = getSetRaceorLeadNo(market.getSequenceId());
        if (inList(marketType, raceSuspendMarketList)) {
            int pointNo = 0;
            if (setGameNo[1] == 5)
                pointNo = 2;
            else if (setGameNo[1] == 10)
                pointNo = 7;
            else if (setGameNo[1] == 15)
                pointNo = 12;
            else if (setGameNo[1] == 20)
                pointNo = 17;
            String point = String.valueOf(pointNo);
            if ((badmintonMatchState.getPointsA() > pointNo && badmintonMatchState.getGameNo(1) == setGameNo[0])
                            || (badmintonMatchState.getPointsB() > pointNo
                                            && badmintonMatchState.getGameNo(1) == setGameNo[0])) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend race markets when points > " + point);
            }
        }

        if (inList(marketType, leadSuspendMarketList)) {
            int pointNo = 0;
            if (setGameNo[1] == 10)
                pointNo = 5;
            else if (setGameNo[1] == 20)
                pointNo = 15;
            else if (setGameNo[1] == 30)
                pointNo = 25;
            String point = String.valueOf(pointNo);
            if ((badmintonMatchState.getPointsA() > pointNo && badmintonMatchState.getGameNo(1) == setGameNo[0])
                            || (badmintonMatchState.getPointsB() > pointNo
                                            && badmintonMatchState.getGameNo(1) == setGameNo[0])) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend lead markets when points > " + point);
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

    /*
     * returns a two element array n with setNo in n[0], gameNo in n[1]
     */
    private int[] getSetRaceorLeadNo(String marketDescription) {
        int[] setGameNo = new int[2];
        String[] bits = marketDescription.split("\\.");
        if (bits[0].length() > 1) {
            String setNo = bits[0].substring(1, 2);
            setGameNo[0] = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            setGameNo[1] = Integer.parseInt(bits[1]);
        return setGameNo;
    }
}
