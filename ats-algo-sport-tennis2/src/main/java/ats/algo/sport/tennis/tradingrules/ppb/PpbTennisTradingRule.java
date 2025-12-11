package ats.algo.sport.tennis.tradingrules.ppb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.tennis.PlayerId;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;

public class PpbTennisTradingRule extends SetSuspensionStatusTradingRule {

    public static final String ALL_MARKET = "All_MARKET";

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> noPFSuspendMarketList;
    private List<String> breakPointSuspendMarketList;
    private List<String> gamePointSuspendMarketList;
    private List<String> dangerPointSuspendMarketList;
    private List<String> setRunningSuspendMarketList;
    private List<String> setBettingSuspendMarketList;
    private List<String> setCSSuspendMarketList;
    private List<String> tieBreakSuspendMarketList;
    private List<String> threePointSuspendMarketList;
    private List<String> currentPointSuspendMarketList;
    private List<String> firstPointSuspendMarketList;
    private List<String> unKnownServeSuspendMarketList;
    private List<String> zeorProbaliltySuspendMarketList;
    private boolean inPlayRequiresSuccessfulParamFind;
    private boolean ruleForDoublesMatch;
    private Map<String, List<String>> preMatchSequenceIdFilterList;
    private Map<String, List<String>> inplaySequenceIdFilterList;

    // JSON
    public PpbTennisTradingRule() {}

    public List<String> getZeorProbaliltySuspendMarketList() {
        return zeorProbaliltySuspendMarketList;
    }

    public void setZeorProbaliltySuspendMarketList(List<String> zeorProbaliltySuspendMarketList) {
        this.zeorProbaliltySuspendMarketList = zeorProbaliltySuspendMarketList;
    }

    public List<String> getUnKnownServeSuspendMarketList() {
        return unKnownServeSuspendMarketList;
    }

    public void setUnKnownServeSuspendMarketList(List<String> unKnownServeSuspendMarketList) {
        this.unKnownServeSuspendMarketList = unKnownServeSuspendMarketList;
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

    public List<String> getNoPFSuspendMarketList() {
        return noPFSuspendMarketList;
    }

    public void setNoPFSuspendMarketList(List<String> noPFSuspendMarketList) {
        this.noPFSuspendMarketList = noPFSuspendMarketList;
    }

    public List<String> getBreakPointSuspendMarketList() {
        return breakPointSuspendMarketList;
    }

    public void setBreakPointSuspendMarketList(List<String> breakPointSuspendMarketList) {
        this.breakPointSuspendMarketList = breakPointSuspendMarketList;
    }

    public List<String> getTieBreakSuspendMarketList() {
        return tieBreakSuspendMarketList;
    }

    public void setTieBreakSuspendMarketList(List<String> tieBreakSuspendMarketList) {
        this.tieBreakSuspendMarketList = tieBreakSuspendMarketList;
    }

    public List<String> getThreePointSuspendMarketList() {
        return threePointSuspendMarketList;
    }

    public void setThreePointSuspendMarketList(List<String> threePointSuspendMarketList) {
        this.threePointSuspendMarketList = threePointSuspendMarketList;
    }

    public List<String> getCurrentPointSuspendMarketList() {
        return currentPointSuspendMarketList;
    }

    public void setCurrentPointSuspendMarketList(List<String> currentPointSuspendMarketList) {
        this.currentPointSuspendMarketList = currentPointSuspendMarketList;
    }

    public List<String> getFirstPointSuspendMarketList() {
        return firstPointSuspendMarketList;
    }

    public void setFirstPointSuspendMarketList(List<String> firstPointSuspendMarketList) {
        this.firstPointSuspendMarketList = firstPointSuspendMarketList;
    }

    public boolean isRuleForDoublesMatch() {
        return ruleForDoublesMatch;
    }

    public void setRuleForDoublesMatch(boolean ruleForDoublesMatch) {
        this.ruleForDoublesMatch = ruleForDoublesMatch;
    }

    public boolean isInPlayRequiresSuccessfulParamFind() {
        return inPlayRequiresSuccessfulParamFind;
    }

    public void setInPlayRequiresSuccessfulParamFind(boolean inPlayRequiresSuccessfulParamfind) {
        this.inPlayRequiresSuccessfulParamFind = inPlayRequiresSuccessfulParamfind;
    }

    public Map<String, List<String>> getPreMatchSequenceIdFilterList() {
        return preMatchSequenceIdFilterList;
    }

    public void setPreMatchSequenceIdFilterList(Map<String, List<String>> preMatchSequenceIdFilterList) {
        this.preMatchSequenceIdFilterList = preMatchSequenceIdFilterList;
    }

    public Map<String, List<String>> getInplaySequenceIdFilterList() {
        return inplaySequenceIdFilterList;
    }

    public void setInplaySequenceIdFilterList(Map<String, List<String>> inplaySequenceIdFilterList) {
        this.inplaySequenceIdFilterList = inplaySequenceIdFilterList;
    }

    public Map<String, List<String>> getInplaySequenceIdFilterListPrePF() {
        return inplaySequenceIdFilterList;
    }

    public List<String> getSetRunningSuspendMarketList() {
        return setRunningSuspendMarketList;
    }

    public void setSetRunningSuspendMarketList(List<String> setRunningSuspendMarketList) {
        this.setRunningSuspendMarketList = setRunningSuspendMarketList;
    }

    public List<String> getGamePointSuspendMarketList() {
        return gamePointSuspendMarketList;
    }

    public void setGamePointSuspendMarketList(List<String> gamePointSuspendMarketList) {
        this.gamePointSuspendMarketList = gamePointSuspendMarketList;
    }


    public List<String> getSetCSSuspendMarketList() {
        return setCSSuspendMarketList;
    }

    public void setSetCSSuspendMarketList(List<String> setCSSuspendMarketList) {
        this.setCSSuspendMarketList = setCSSuspendMarketList;
    }



    public List<String> getDangerPointSuspendMarketList() {
        return dangerPointSuspendMarketList;
    }

    public void setDangerPointSuspendMarketList(List<String> dangerPointSuspendMarketList) {
        this.dangerPointSuspendMarketList = dangerPointSuspendMarketList;
    }

    public List<String> getSetBettingSuspendMarketList() {
        return setBettingSuspendMarketList;
    }

    public void setSetBettingSuspendMarketList(List<String> setBettingSuspendMarketList) {
        this.setBettingSuspendMarketList = setBettingSuspendMarketList;
    }

    /**
     *
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     */
    public PpbTennisTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(AlgoMatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        TennisMatchState tennisMatchState = (TennisMatchState) matchState;
        TennisMatchFormat tennisMatchFormat = (TennisMatchFormat) tennisMatchState.getMatchFormat();
        if (tennisMatchState.isDoubleMatch())
            if (!ruleForDoublesMatch)
                return;
        String marketType = market.getType();
        boolean applyPreMatchRules = tennisMatchState.preMatch();
        if (applyPreMatchRules) {
            /*
             * assume if prematchdisplayMarketList is null then applies to all markets
             */
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");
            }
            String mktSequenceId = market.getSequenceId();
            if (filterOutThroughSequenceId(market.getType(), mktSequenceId, matchState.getSequenceId(mktSequenceId, 0),
                            preMatchSequenceIdFilterList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "SequenceId filter");
            }
        } else { // in play
            // FIXME: PROVISIONAL RULE ASKED BY PRIYA, suspend all G: markets in the 13th
            /*
             * it is conflict to FAST4 rule currently removed from FAST4 logic
             */
            if (!tennisMatchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET)) {
                if (marketType.contains("G:") && market.getSequenceId().contains(".13")) {
                    marketStatus.setStatusIfHigherPriority("Priya's Rule", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Priya said suspend but display for now");
                    return;
                }
            }


            if (marketType.equals("P:TBCS")) { // Tiebreak correct score rules
                if (tennisMatchState.isInTieBreak()) {
                    int A = tennisMatchState.getTieBreak().getPointsA();
                    int B = tennisMatchState.getTieBreak().getPointsB();
                    if ((A >= 7 && A - B >= 1) || (A == 6 && B <= 5))
                        marketStatus.setStatusIfHigherPriority("Tiebreak correct score rules",
                                        SuspensionStatus.SUSPENDED_DISPLAY, "Tiebreak correct score rules");
                    else if ((B >= 7 && B - A >= 1) || (B == 6 && A <= 5))
                        marketStatus.setStatusIfHigherPriority("Tiebreak correct score rules",
                                        SuspensionStatus.SUSPENDED_DISPLAY, "Tiebreak correct score rules");
                    return;
                } else {
                    marketStatus.setStatusIfHigherPriority("Tiebreak correct score rules",
                                    SuspensionStatus.SUSPENDED_UNDISPLAY, "Tiebreak correct score rules");

                }
            }

            if (marketType.equals("FT:CTBCS")) { //
                if (tennisMatchState.isInSuperTieBreak()) {
                    int A = tennisMatchState.getSuperTieBreak().getPointsA();
                    int B = tennisMatchState.getSuperTieBreak().getPointsB();
                    if ((A >= 10 && A - B >= 1) || (A == 9 && B <= 8))
                        marketStatus.setStatusIfHigherPriority("ChampTiebreak correct score rules",
                                        SuspensionStatus.SUSPENDED_DISPLAY, "ChampTiebreak correct score rules");
                    else if ((B >= 10 && B - A >= 1) || (B == 9 && A <= 8))
                        marketStatus.setStatusIfHigherPriority("ChampTiebreak correct score rules",
                                        SuspensionStatus.SUSPENDED_DISPLAY, "ChampTiebreak correct score rules");
                    return;
                } else {
                    marketStatus.setStatusIfHigherPriority("ChampTiebreak correct score rules",
                                    SuspensionStatus.SUSPENDED_UNDISPLAY, "ChampTiebreak correct score rules");

                }
            }



            /*
             * check whether open
             */

            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            } else {
                String mktSequenceId = market.getSequenceId();
                if (filterOutThroughSequenceId(market.getType(), mktSequenceId,
                                matchState.getSequenceId(mktSequenceId, 0), this.inplaySequenceIdFilterList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "SequenceId filter");
                }
                /*
                 * in play and market is normally displayed. Check the various undisplay rules
                 *
                 * check whether there has been a PF in play
                 */
                if (tennisMatchState.getOnServePlayerNow() == PlayerId.UNKNOWN) {
                    if (inList(marketType, unKnownServeSuspendMarketList))
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend when serve unknown");
                }


                /*
                 * check whether gamepoint rule applies
                 */
                if (tennisMatchState.getDoubletennisMatchIncident().getSourceSystem() != null) {
                    if ((tennisMatchState.getDoubletennisMatchIncident().getSourceSystem().equals("IMG")
                                    && tennisMatchState.getDoubletennisMatchIncident().getIncidentSubType()
                                                    .equals(TennisMatchIncidentType.POINT_START))
                                    || tennisMatchState.getDoubletennisMatchIncident().getSourceSystem()
                                                    .equals("BETRADAR")) {

                        if (tennisMatchState.isGamePoint() && inList(marketType, gamePointSuspendMarketList)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend on game point");
                        }
                    }
                }


                if (tennisMatchState.getDoubletennisMatchIncident().getIncidentSubType()
                                .equals(TennisMatchIncidentType.FAULT)) {
                    if (tennisMatchState.isGamePoint() && inListAll(marketType, gamePointSuspendMarketList)) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                                        "Open when fault on game point");
                    } else {
                        if (tennisMatchState.isGamePoint() && inList(marketType, gamePointSuspendMarketList)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                                            "Open when fault on game point");
                        }
                    }
                }



                // one game away winning set suspending logic
                int gamesA = tennisMatchState.getGamesA();
                int gamesB = tennisMatchState.getGamesB();
                boolean oneGameAwayWinnningSet = false;
                if (gamesA == 5 && gamesB != 5)
                    oneGameAwayWinnningSet = true;
                if (gamesB == 5 && gamesA != 5)
                    oneGameAwayWinnningSet = true;
                if (oneGameAwayWinnningSet && inList(marketType, setCSSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Suspend when one game before winning set");
                    if (tennisMatchState.isGamePoint()) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend when game Point");
                    }
                }
                int setsA = tennisMatchState.getSetsA();
                int setsB = tennisMatchState.getSetsB();
                int pointsA = tennisMatchState.getPointsA();
                int pointsB = tennisMatchState.getPointsB();
                int[] setGame = getSetGameNo(mktSequenceId);

                if (marketType.equals("P:CS2") || marketType.equals("P:CS4") || marketType.equals("P:CS6")) {
                    if (tennisMatchState.isBreakPoint()) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend the markets when danger points");
                    }
                }

                if (tennisMatchState.isInTieBreak()) {
                    if (pointsA >= 5 || pointsB >= 5) {
                        if (inList(marketType, setRunningSuspendMarketList)
                                        || inList(marketType, setBettingSuspendMarketList)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend when player reach 5 in tie break");
                        }
                    }
                }
                int oneSetAwayWinning = (tennisMatchFormat.getSetsPerMatch() - 1) / 2;
                if (setsA == oneSetAwayWinning && gamesA >= 4) {
                    if (inList(marketType, setBettingSuspendMarketList)) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend when player win " + setsA + " set and 4 games");
                    }

                }

                if (setsB == oneSetAwayWinning && gamesB >= 4) {
                    if (inList(marketType, setBettingSuspendMarketList)) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend when player win " + setsB + " set and 4 games");
                    }
                }
                if (oneSetAwayWinning == setsA && oneSetAwayWinning == setsB)
                    if (inList(marketType, setBettingSuspendMarketList))
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend when both player win " + setsB + " set");


                if (tennisMatchState.isBreakPoint()) {
                    if (inList(marketType, dangerPointSuspendMarketList)) {
                        if (setGame[0] == (setsA + setsB + 1) && setGame[1] == (gamesA + gamesB + 1)) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend the markets when danger points");
                        } else if (marketType.contains("FT:")) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend the markets when danger points");
                        }
                    }
                }
                if (market.getSelections().size() == 1) {
                    for (double prob : market.getSelectionsProbs().values()) {
                        if (prob == 0.0) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "One selection with 0 prob - suspend market");
                        }
                    }
                }
                for (double prob : market.getSelectionsProbs().values()) {
                    if (prob == 1.0) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "One selection with prob of 1 - suspend market");
                    }
                    if (inList(marketType, zeorProbaliltySuspendMarketList))
                        if (prob == 0.0) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "One selection with prob of 0 - suspend market");
                        }
                }
            }
        }
    }

    /**
     * checks to see whether there is an entry for this marketType. If none found returns false. If yes checks to see
     * whether the market sequenceId matches one of those in the filter. If it does returns false, otherwise returns
     * true.
     * 
     * @param sequenceId - the
     * @param preMatchSequenceIdFilterList2
     * @return true if this market should be filtered out
     */
    private boolean filterOutThroughSequenceId(String mktType, String mktSequenceId, String currentSequenceId,
                    Map<String, List<String>> sequenceIdFilterMap) {
        if (sequenceIdFilterMap == null)
            return false;
        List<String> sequenceIdFilters = sequenceIdFilterMap.get(mktType);
        if (sequenceIdFilters == null)
            return false;
        boolean foundSequenceIdMatch = false;
        for (String sequenceIdFilter : sequenceIdFilters) {
            foundSequenceIdMatch =
                            foundSequenceIdMatch || foundMatch(mktSequenceId, currentSequenceId, sequenceIdFilter);
        }
        return !foundSequenceIdMatch;
    }

    /**
     * returns true if currentSequenceId + sequenceIdFilter = mktSequenceId
     * 
     * @param mktSequenceId
     * @param currentSequenceId
     * @param sequenceIdFilter
     * @return
     */
    private boolean foundMatch(String mktSequenceId, String currentSequenceId, String sequenceIdFilter) {
        List<Integer> mktSeqInts = getSequenceIdAsIntArray(mktSequenceId);
        List<Integer> currentSeqInts = getSequenceIdAsIntArray(currentSequenceId);
        List<Integer> filterSeqInts = getSequenceIdAsIntArray(sequenceIdFilter);
        if (mktSeqInts.size() != currentSeqInts.size() || mktSeqInts.size() != filterSeqInts.size())
            throw new IllegalArgumentException(String.format(
                            "filterOnSequenceIds given incompatible seq ids: mkt: %s, current: %s, filter: %s",
                            mktSequenceId, currentSequenceId, currentSequenceId));
        boolean match = true;
        for (int i = 0; i < mktSeqInts.size(); i++)
            match &= ((int) mktSeqInts.get(i) == (int) currentSeqInts.get(i) + (int) filterSeqInts.get(i));
        return match;
    }

    // private boolean firstPointOfGame(TennisMatchState tennisMatchState) {
    // return tennisMatchState.getPointsA() + tennisMatchState.getPointsB() >= 1;
    // }

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

    private boolean inListAll(String marketType, List<String> marketTypeList) {
        if (marketTypeList == null) {
            return false;
        }
        if (marketTypeList.size() == 1 && marketTypeList.get(0).equals(ALL_MARKET)) {
            return true;
        }
        return false;
    }

    private List<Integer> getSequenceIdAsIntArray(String sequenceId) {

        String[] bits = sequenceId.split("\\.");
        List<Integer> result = new ArrayList<Integer>(bits.length);

        if (bits[0].length() > 1) {
            String setNo = bits[0].substring(1, 2);
            result.add(0, Integer.parseInt(setNo));
        }
        for (int i = 1; i < bits.length; i++) {
            result.add(i, Integer.parseInt(bits[i]));
        }
        return result;

    }

    private int[] getSetGameNo(String marketDescription) {
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
