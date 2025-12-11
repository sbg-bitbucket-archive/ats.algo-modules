package ats.algo.sport.tennis.tradingrules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.tennis.PlayerId;
import ats.algo.sport.tennis.TennisMatchState;

public class TennisTradingRule extends SetSuspensionStatusTradingRule {

    public static final String ALL_MARKET = "All_MARKET";

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> noPFSuspendMarketList;
    private List<String> breakPointSuspendMarketList;
    private List<String> tieBreakSuspendMarketList;
    private List<String> threePointSuspendMarketList;
    private List<String> currentPointSuspendMarketList;
    private List<String> firstPointSuspendMarketList;
    private List<String> unKnownServeSuspendMarketList;
    private boolean inPlayRequiresSuccessfulParamFind;
    private boolean ruleForDoublesMatch;
    private Map<String, List<String>> preMatchSequenceIdFilterList;
    private Map<String, List<String>> inplaySequenceIdFilterList;
    private Map<String, List<String>> inplaySequenceIdFilterListPrePF;
    private Map<String, List<String>> prematchSequenceIdFilterListForBreakPoint;
    private Map<String, List<String>> prematchSequenceIdFilterListForTiebreak;
    private Map<String, List<String>> prematchSequenceIdFilterListForFinalSetTiebreak;
    private Map<String, List<String>> inplaySequenceIdFilterListForBreakPoint;
    private Map<String, List<String>> inplaySequenceIdFilterListForTiebreak;
    private Map<String, List<String>> inplaySequenceIdFilterListForFinalSetTiebreak;

    // JSON
    public TennisTradingRule() {}

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
        return inplaySequenceIdFilterListPrePF;
    }

    public void setInplaySequenceIdFilterListPrePF(Map<String, List<String>> inplaySequenceIdFilterListPrePF) {
        this.inplaySequenceIdFilterListPrePF = inplaySequenceIdFilterListPrePF;
    }

    public Map<String, List<String>> getPrematchSequenceIdFilterListForBreakPoint() {
        return prematchSequenceIdFilterListForBreakPoint;
    }

    public void setPrematchSequenceIdFilterListForBreakPoint(
                    Map<String, List<String>> prematchSequenceIdFilterListForBreakPoint) {
        this.prematchSequenceIdFilterListForBreakPoint = prematchSequenceIdFilterListForBreakPoint;
    }

    public Map<String, List<String>> getPrematchSequenceIdFilterListForTiebreak() {
        return prematchSequenceIdFilterListForTiebreak;
    }

    public void setPrematchSequenceIdFilterListForTiebreak(
                    Map<String, List<String>> prematchSequenceIdFilterListForTiebreak) {
        this.prematchSequenceIdFilterListForTiebreak = prematchSequenceIdFilterListForTiebreak;
    }

    public Map<String, List<String>> getPrematchSequenceIdFilterListForFinalSetTiebreak() {
        return prematchSequenceIdFilterListForFinalSetTiebreak;
    }

    public void setPrematchSequenceIdFilterListForFinalSetTiebreak(
                    Map<String, List<String>> prematchSequenceIdFilterListForFinalSetTiebreak) {
        this.prematchSequenceIdFilterListForFinalSetTiebreak = prematchSequenceIdFilterListForFinalSetTiebreak;
    }

    public Map<String, List<String>> getInplaySequenceIdFilterListForBreakPoint() {
        return inplaySequenceIdFilterListForBreakPoint;
    }

    public void setInplaySequenceIdFilterListForBreakPoint(
                    Map<String, List<String>> inplaySequenceIdFilterListForBreakPoint) {
        this.inplaySequenceIdFilterListForBreakPoint = inplaySequenceIdFilterListForBreakPoint;
    }

    public Map<String, List<String>> getInplaySequenceIdFilterListForTiebreak() {
        return inplaySequenceIdFilterListForTiebreak;
    }

    public void setInplaySequenceIdFilterListForTiebreak(
                    Map<String, List<String>> inplaySequenceIdFilterListForTiebreak) {
        this.inplaySequenceIdFilterListForTiebreak = inplaySequenceIdFilterListForTiebreak;
    }

    public Map<String, List<String>> getInplaySequenceIdFilterListForFinalSetTiebreak() {
        return inplaySequenceIdFilterListForFinalSetTiebreak;
    }

    public void setInplaySequenceIdFilterListForFinalSetTiebreak(
                    Map<String, List<String>> inplaySequenceIdFilterListForFinalSetTiebreak) {
        this.inplaySequenceIdFilterListForFinalSetTiebreak = inplaySequenceIdFilterListForFinalSetTiebreak;
    }

    /**
     *
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     */
    public TennisTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        TennisMatchState tennisMatchState = (TennisMatchState) matchState;
        if (tennisMatchState.isDoubleMatch() != ruleForDoublesMatch)
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
                 * Break Point filter
                 */
                if (tennisMatchState.isBreakPoint() && filterOutThroughSequenceId(market.getType(), mktSequenceId,
                                matchState.getSequenceId(mktSequenceId, 0),
                                this.inplaySequenceIdFilterListForBreakPoint)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "SequenceId filter for breakpoint");
                }

                /*
                 * Tiebreak Point filter
                 */
                if (tennisMatchState.isInTieBreak() && filterOutThroughSequenceId(market.getType(), mktSequenceId,
                                matchState.getSequenceId(mktSequenceId, 0),
                                this.inplaySequenceIdFilterListForTiebreak)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "SequenceId filter for tiebreak");
                }

                /*
                 * Final set Tiebreak Point filter
                 */
                if (tennisMatchState.isInTieBreak() && tennisMatchState.isInFinalSet()
                                && filterOutThroughSequenceId(market.getType(), mktSequenceId,
                                                matchState.getSequenceId(mktSequenceId, 0),
                                                this.inplaySequenceIdFilterListForFinalSetTiebreak)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "SequenceId filter for final set tiebreak");
                }

                /*
                 * in play and market is normally displayed. Check the various undisplay rules
                 *
                 * check whether there has been a PF in play
                 */
                if (matchState.getNoSuccessfulInPlayParamFindsExecuted() < 1
                                && inList(marketType, noPFSuspendMarketList) && inPlayRequiresSuccessfulParamFind) {
                    if (filterOutThroughSequenceId(market.getType(), mktSequenceId,
                                    matchState.getSequenceId(mktSequenceId, 0), this.inplaySequenceIdFilterListPrePF)) {

                    } else {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend without INPF");
                    }
                }
                if (tennisMatchState.getOnServePlayerNow() == PlayerId.UNKNOWN) {
                    if (inList(marketType, unKnownServeSuspendMarketList))
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend when serve unknown");
                }

                // Suspend G
                if (marketType.equals("G:ML") || marketType.equals("G:DEUCE")) {
                    int nextT = Integer.valueOf(market.getSequenceId().substring(3, market.getSequenceId().length()));
                    if (tennisMatchState.getGamesA() + tennisMatchState.getGamesB() + 3 == nextT)
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend on third game");

                }

                /*
                 * check whether first point of game rule applies
                 */
                if (firstPointOfGame(tennisMatchState) && inList(marketType, firstPointSuspendMarketList)) {
                    int[] setGameNo = getSetGameNo(market.getSequenceId());
                    if (tennisMatchState.getSetNo() == setGameNo[0] && setGameNo[1] == tennisMatchState.getGameNo()) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                        "Suspend on first point of game");
                    }
                }
                /*
                 * check whether breakpoint rule applies
                 */
                if (tennisMatchState.isBreakPoint() && inListAll(marketType, breakPointSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Suspend on break point");
                } else {
                    if (tennisMatchState.isBreakPoint() && inList(marketType, breakPointSuspendMarketList)) {
                        int[] setGameNo = getSetGameNo(market.getSequenceId());
                        if (tennisMatchState.getSetNo() == setGameNo[0]
                                        && setGameNo[1] == tennisMatchState.getGameNo()) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend on break point");
                        }
                        if (tennisMatchState.getSetNo() == setGameNo[0] && setGameNo[1] == 0) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend on break point");
                        }
                        if (setGameNo[0] == 0 && setGameNo[1] == 0) {
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                            "Suspend on break point");
                        }
                    }
                }

                /*
                 * check whether tiebreak rule applies
                 */
                if (tennisMatchState.isInTieBreak() && inList(marketType, tieBreakSuspendMarketList)) {
                    int[] setGameNo = getSetGameNo(market.getSequenceId());
                    if (tennisMatchState.getSetNo() == setGameNo[0]) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend during tie break");
                    }
                }
                /*
                 * check whether deuce rule applies
                 */
                if ((tennisMatchState.getPointsA() >= 3 || tennisMatchState.getPointsB() >= 3)
                                && inList(marketType, threePointSuspendMarketList)) {
                    int[] setGameNo = getSetGameNo(market.getSequenceId());
                    if (tennisMatchState.getSetNo() == setGameNo[0] && setGameNo[1] == tennisMatchState.getGameNo()) {
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend from deuce");
                    }
                }
                // Suspend current point winning market
                if (inList(marketType, currentPointSuspendMarketList)) {
                    int[] setGameNo = getSetGamePointNo(market.getSequenceId());
                    if (tennisMatchState.getSetNo() == setGameNo[0] && setGameNo[1] == tennisMatchState.getGameNo()) {
                        if (setGameNo[2] == tennisMatchState.getPointsA() + tennisMatchState.getPointsB() + 1)
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                            "Suspend from deuce");
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

    private boolean firstPointOfGame(TennisMatchState tennisMatchState) {
        return tennisMatchState.getPointsA() + tennisMatchState.getPointsB() >= 1;
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

    private boolean inListAll(String marketType, List<String> marketTypeList) {
        if (marketTypeList == null) {
            return false;
        }
        if (marketTypeList.size() == 1 && marketTypeList.get(0).equals(ALL_MARKET)) {
            return true;
        }
        return false;
    }

    /*
     * returns a two element array n with setNo in n[0], gameNo in n[1]
     */
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

    private int[] getSetGamePointNo(String marketDescription) {
        int[] setGamePointNo = new int[3];
        String[] bits = marketDescription.split("\\.");
        if (bits[0].length() > 1) {
            String setNo = bits[0].substring(1, 2);
            setGamePointNo[0] = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            setGamePointNo[1] = Integer.parseInt(bits[1]);
        if (bits.length >= 3)
            setGamePointNo[2] = Integer.parseInt(bits[2]);

        return setGamePointNo;
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

}
