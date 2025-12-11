package ats.algo.sport.volleyball.tradingrules;

import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchState;

public class VolleyballTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> setPointSuspendMarketList;
    private List<String> finalSetSuspendMarketList;
    private List<String> threeSetSuspendMarketList;
    private List<String> secondSetSuspendMarketList;
    private List<String> finalPointSuspendMarketList;
    private List<String> totalPointSuspendMarketList;
    private List<String> handicapPointSuspendMarketList;
    private List<String> raceSuspendMarketList;
    private List<String> leadSuspendMarketList;
    private List<String> ExtraPointSuspendMarketList;

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

    public List<String> getSetPointSuspendMarketList() {
        return setPointSuspendMarketList;
    }

    public void setSetPointSuspendMarketList(List<String> setPointSuspendMarketList) {
        this.setPointSuspendMarketList = setPointSuspendMarketList;
    }

    public List<String> getFinalSetSuspendMarketList() {
        return finalSetSuspendMarketList;
    }

    public void setFinalSetSuspendMarketList(List<String> finalSetSuspendMarketList) {
        this.finalSetSuspendMarketList = finalSetSuspendMarketList;
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

    public List<String> getHandicapPointSuspendMarketList() {
        return handicapPointSuspendMarketList;
    }

    public void setHandicapPointSuspendMarketList(List<String> handicapPointSuspendMarketList) {
        this.handicapPointSuspendMarketList = handicapPointSuspendMarketList;
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

    public List<String> getThreeSetSuspendMarketList() {
        return threeSetSuspendMarketList;
    }

    public void setThreeSetSuspendMarketList(List<String> threeSetSuspendMarketList) {
        this.threeSetSuspendMarketList = threeSetSuspendMarketList;
    }

    public List<String> getSecondSetSuspendMarketList() {
        return secondSetSuspendMarketList;
    }

    public void setSecondSetSuspendMarketList(List<String> secondSetSuspendMarketList) {
        this.secondSetSuspendMarketList = secondSetSuspendMarketList;
    }

    /**
     * Ollie Addition
     * 
     */

    public List<String> getExtraPointSuspendMarketList() {
        return ExtraPointSuspendMarketList;
    }

    public void setExtraPointSuspendMarketList(List<String> ExtraPointSuspendMarketList) {
        this.ExtraPointSuspendMarketList = ExtraPointSuspendMarketList;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     */
    public VolleyballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        VolleyballMatchState volleyballMatchState = (VolleyballMatchState) matchState;
        MarketStatus marketStatus = market.getMarketStatus();
        String marketType = market.getType();
        VolleyballMatchFormat volleyballMatchFormat = (VolleyballMatchFormat) volleyballMatchState.getMatchFormat();
        int nPointInRegularSet = volleyballMatchFormat.getnPointInRegularSet();
        int nPointInFinalSet = volleyballMatchFormat.getnPointInFinalSet();

        if (volleyballMatchState.preMatch()) {
            if (prematchDisplayMarketList != null && inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN, "Open pre-match");
            }
        } else { // in play
            /*
             * check whether open
             */
            if (prematchDisplayMarketList != null && inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open inplay");
            }
        }

        if (volleyballMatchState.isInFinalSet()) {

            if (marketType.contains("FT:ML")
                            & (volleyballMatchState.getPointsB() == 14 || volleyballMatchState.getPointsA() == 14)) {
                info(marketType);
                info("A = " + volleyballMatchState.getPointsA());
                info("B = " + volleyballMatchState.getPointsB());
                info("Points in FS = " + nPointInFinalSet);
                info("");
            }
            if ((volleyballMatchState.getPointsA() >= (nPointInFinalSet - 3)
                            || volleyballMatchState.getPointsB() >= (nPointInFinalSet - 3))
                            && inList(marketType, setPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 3);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }
            if (inList(marketType, finalSetSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend in final set");
            }
            if ((volleyballMatchState.getPointsA() > (nPointInFinalSet - 5)
                            || volleyballMatchState.getPointsB() > (nPointInFinalSet - 5))
                            && inList(marketType, totalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 5);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((volleyballMatchState.getPointsA() > (nPointInFinalSet - 6)
                            || volleyballMatchState.getPointsB() > (nPointInFinalSet - 6))
                            && inList(marketType, handicapPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 6);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((volleyballMatchState.getPointsA() >= (nPointInFinalSet - 5)
                            || volleyballMatchState.getPointsB() >= (nPointInFinalSet - 5))
                            && inList(marketType, ExtraPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 5);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((volleyballMatchState.getPointsA() >= (nPointInFinalSet - 1)
                            || volleyballMatchState.getPointsB() >= (nPointInFinalSet - 1))
                            && inList(marketType, finalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 1);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

        } else {
            if ((volleyballMatchState.getPointsA() >= (nPointInRegularSet - 3)
                            || volleyballMatchState.getPointsB() >= (nPointInRegularSet - 3))
                            && inList(marketType, setPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 3);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if (((volleyballMatchState.getSetsA() + volleyballMatchState.getSetsB()) >= 3)
                            && inList(marketType, threeSetSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when set > 3 ");
            }

            if ((volleyballMatchState.getSetsA() >= 2 || volleyballMatchState.getSetsB() >= 2)
                            && inList(marketType, secondSetSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when set > 2 ");
            }

            if ((volleyballMatchState.getPointsA() >= (nPointInRegularSet - 1)
                            || volleyballMatchState.getPointsB() >= (nPointInRegularSet - 1))
                            && inList(marketType, finalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 3);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }
            if ((volleyballMatchState.getPointsA() > (nPointInRegularSet - 8)
                            || volleyballMatchState.getPointsB() > (nPointInRegularSet - 8))
                            && inList(marketType, totalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 8);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((volleyballMatchState.getPointsA() > (nPointInRegularSet - 6)
                            || volleyballMatchState.getPointsB() > (nPointInRegularSet - 6))
                            && inList(marketType, handicapPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 6);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }
        }
        int[] setGameNo = getSetRaceorLeadNo(market.getSequenceId());
        if (inList(marketType, raceSuspendMarketList)) {
            int pointNo = 0;
            if (setGameNo[1] == 5)
                pointNo = 1;
            else if (setGameNo[1] == 10)
                pointNo = 6;
            else if (setGameNo[1] == 15)
                pointNo = 12;
            else if (setGameNo[1] == 20)
                pointNo = 17;
            String point = String.valueOf(pointNo);
            if (volleyballMatchState.getPointsA() > pointNo || volleyballMatchState.getPointsB() > pointNo) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend race markets when points > " + point);
            }
        }

        if (inList(marketType, leadSuspendMarketList)) {
            int pointNo = 0;
            if (setGameNo[1] == 10)
                pointNo = 4;
            else if (setGameNo[1] == 20)
                pointNo = 9;
            else if (setGameNo[1] == 30)
                pointNo = 14;
            String point = String.valueOf(pointNo);
            if (volleyballMatchState.getPointsA() > pointNo || volleyballMatchState.getPointsB() > pointNo) {
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
