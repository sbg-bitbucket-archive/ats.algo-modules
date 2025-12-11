package ats.algo.sport.beachvolleyball.tradingrules;

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
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchState;

public class BeachVolleyballTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> setPointSuspendMarketList;
    private List<String> finalSetSuspendMarketList;
    private List<String> firstSetSuspendMarketList;
    private List<String> finalPointSuspendMarketList;
    private List<String> totalPointSuspendMarketList;
    private List<String> handicapPointSuspendMarketList;
    private List<String> raceSuspendMarketList;
    private List<String> leadSuspendMarketList;

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

    public List<String> getFirstSetSuspendMarketList() {
        return firstSetSuspendMarketList;
    }

    public void setFirstSetSuspendMarketList(List<String> firstSetSuspendMarketList) {
        this.firstSetSuspendMarketList = firstSetSuspendMarketList;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public BeachVolleyballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        BeachVolleyballMatchState beachVolleyballMatchState = (BeachVolleyballMatchState) matchState;
        MarketStatus marketStatus = market.getMarketStatus();
        String marketType = market.getType();
        BeachVolleyballMatchFormat beachVolleyballMatchFormat =
                        (BeachVolleyballMatchFormat) beachVolleyballMatchState.getMatchFormat();
        int nPointInRegularSet = beachVolleyballMatchFormat.getnPointInRegularSet();
        int nPointInFinalSet = beachVolleyballMatchFormat.getnPointInFinalSet();

        if (beachVolleyballMatchState.preMatch()) {
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN, "Open pre-match");
            }
        } else { // in play
            /*
             * check whether open
             */
            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open inplay");
            }
        }

        if (inList(marketType, firstSetSuspendMarketList)) {
            if (beachVolleyballMatchState.getSetNo() >= 2)
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend in final set");
        }

        if (beachVolleyballMatchState.isInFinalSet()) {

            if (inList(marketType, setPointSuspendMarketList)) {
                int[] setPointNo = getSetRaceorLeadNo(market.getSequenceId());
                int i = setPointNo[1] - beachVolleyballMatchState.getPointNo();
                if ((beachVolleyballMatchState.getPointsA() >= (nPointInFinalSet - i)
                                || beachVolleyballMatchState.getPointsB() >= (nPointInFinalSet - i)))
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend when points > " + (nPointInFinalSet - i));
            }
            if (inList(marketType, finalSetSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend in final set");
            }
            if ((beachVolleyballMatchState.getPointsA() >= (nPointInFinalSet - 5)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInFinalSet - 5))
                            && inList(marketType, totalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 5);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((beachVolleyballMatchState.getPointsA() >= (nPointInFinalSet - 6)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInFinalSet - 6))
                            && inList(marketType, handicapPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 6);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((beachVolleyballMatchState.getPointsA() >= (nPointInFinalSet - 1)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInFinalSet - 1))
                            && inList(marketType, finalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInFinalSet - 1);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

        } else {
            if (inList(marketType, setPointSuspendMarketList)) {
                int[] setPointNo = getSetRaceorLeadNo(market.getSequenceId());
                int i = setPointNo[1] - beachVolleyballMatchState.getPointNo();
                if ((beachVolleyballMatchState.getPointsA() >= (nPointInRegularSet - i)
                                || beachVolleyballMatchState.getPointsB() >= (nPointInRegularSet - i)))
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend when points > " + (nPointInFinalSet - i));
            }

            if (((beachVolleyballMatchState.getSetsA() + beachVolleyballMatchState.getSetsB()) >= 3)
                            && inList(marketType, firstSetSuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when set > 3 ");
            }

            if ((beachVolleyballMatchState.getPointsA() >= (nPointInRegularSet - 1)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInRegularSet - 1))
                            && inList(marketType, finalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 3);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }
            if ((beachVolleyballMatchState.getPointsA() >= (nPointInRegularSet - 8)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInRegularSet - 8))
                            && inList(marketType, totalPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 8);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
            }

            if ((beachVolleyballMatchState.getPointsA() >= (nPointInRegularSet - 6)
                            || beachVolleyballMatchState.getPointsB() >= (nPointInRegularSet - 6))
                            && inList(marketType, handicapPointSuspendMarketList)) {
                String point = String.valueOf(nPointInRegularSet - 6);
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Suspend when points > " + point);
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
