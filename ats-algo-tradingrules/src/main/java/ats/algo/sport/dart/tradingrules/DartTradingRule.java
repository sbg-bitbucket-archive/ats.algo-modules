package ats.algo.sport.dart.tradingrules;

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
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.LegState;

public class DartTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> less250LegSuspendMarketList;
    private List<String> matchSuspendMarketList;
    private List<String> matchEndSuspendMarketList;
    private List<String> firstDartSuspendMarketList;
    private List<String> less250SetSuspendMarketList;
    private List<String> all180MarketSuspendMarketList;
    private List<String> twoLeg180MarketSuspendMarketList;
    private List<String> oneLeg180AMarketSuspendMarketList;
    private List<String> oneLeg180BMarketSuspendMarketList;

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

    public List<String> getLess250LegSuspendMarketList() {
        return less250LegSuspendMarketList;
    }

    public void setLess250LegSuspendMarketList(List<String> less250LegSuspendMarketList) {
        this.less250LegSuspendMarketList = less250LegSuspendMarketList;
    }

    public List<String> getMatchSuspendMarketList() {
        return matchSuspendMarketList;
    }

    public void setMatchSuspendMarketList(List<String> matchSuspendMarketList) {
        this.matchSuspendMarketList = matchSuspendMarketList;
    }

    public List<String> getFirstDartSuspendMarketList() {
        return firstDartSuspendMarketList;
    }

    public void setFirstDartSuspendMarketList(List<String> firstDartSuspendMarketList) {
        this.firstDartSuspendMarketList = firstDartSuspendMarketList;
    }

    public List<String> getMatchEndSuspendMarketList() {
        return matchEndSuspendMarketList;
    }

    public void setMatchEndSuspendMarketList(List<String> matchEndSuspendMarketList) {
        this.matchEndSuspendMarketList = matchEndSuspendMarketList;
    }

    public List<String> getLess250SetSuspendMarketList() {
        return less250SetSuspendMarketList;
    }

    public void setLess250SetSuspendMarketList(List<String> less250SetSuspendMarketList) {
        this.less250SetSuspendMarketList = less250SetSuspendMarketList;
    }

    public List<String> getAll180MarketSuspendMarketList() {
        return all180MarketSuspendMarketList;
    }

    public void setAll180MarketSuspendMarketList(List<String> all180MarketSuspendMarketList) {
        this.all180MarketSuspendMarketList = all180MarketSuspendMarketList;
    }

    public List<String> getTwoLeg180MarketSuspendMarketList() {
        return twoLeg180MarketSuspendMarketList;
    }

    public void setTwoLeg180MarketSuspendMarketList(List<String> twoLeg180MarketSuspendMarketList) {
        this.twoLeg180MarketSuspendMarketList = twoLeg180MarketSuspendMarketList;
    }


    public List<String> getOneLeg180AMarketSuspendMarketList() {
        return oneLeg180AMarketSuspendMarketList;
    }

    public void setOneLeg180AMarketSuspendMarketList(List<String> oneLeg180AMarketSuspendMarketList) {
        this.oneLeg180AMarketSuspendMarketList = oneLeg180AMarketSuspendMarketList;
    }

    public List<String> getOneLeg180BMarketSuspendMarketList() {
        return oneLeg180BMarketSuspendMarketList;
    }

    public void setOneLeg180BMarketSuspendMarketList(List<String> oneLeg180BMarketSuspendMarketList) {
        this.oneLeg180BMarketSuspendMarketList = oneLeg180BMarketSuspendMarketList;
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public DartTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {
        MarketStatus marketStatus = market.getMarketStatus();
        DartMatchState dartMatchState = (DartMatchState) matchState;
        int setNo = dartMatchState.getSetNo();
        int legNo = dartMatchState.getLegNo();
        String marketType = market.getType();
        if (dartMatchState.preMatch()) {
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
                // LegState legState = dartMatchState.getCurrentLeg();
                int points = Math.min(dartMatchState.getCurrentLeg().getPlayerA().getPoints(),
                                dartMatchState.getCurrentLeg().getPlayerB().getPoints());
                boolean firstLeg = (dartMatchState.getCurrentLeg().getPlayerA().getPoints() == 501)
                                && (dartMatchState.getCurrentLeg().getPlayerB().getPoints() == 501);
                if (lessThan(points, 250) && inList(marketType, less250LegSuspendMarketList)) {
                    int[] setLegNo = getSetLegNo(market.getSequenceId());
                    if (setNo == setLegNo[0] && legNo == setLegNo[1])
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend on leg when point less 250");
                }

                if (twoMoreLegToWinSet(dartMatchState) && inList(marketType, less250SetSuspendMarketList)) {
                    int[] setLegNo = getSetLegNo(market.getSequenceId());
                    if (setNo == setLegNo[0])
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend on set when point less 250");
                }

                if (!firstLeg && inList(marketType, firstDartSuspendMarketList)) {
                    int[] setLegNo = getSetLegNo(market.getSequenceId());
                    if (setNo == setLegNo[0] && legNo == setLegNo[1])
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Suspend on first point of leg");
                }
                if (oneMoreLegToWin(dartMatchState) && inList(marketType, matchSuspendMarketList)
                                && lessThan(points, 250)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on one more leg to win the match");
                }

                if (maxTwoSetsRemain(dartMatchState) && inList(marketType, matchEndSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on when max of 2 sets remain");
                }

                if (twoDartDownScore120(dartMatchState) && inList(marketType, all180MarketSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on 2 darts score 120");
                }
                if (twoMoreLegToWin(dartMatchState) && inList(marketType, twoLeg180MarketSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on two more legs to win the match");
                }

                if (oneMoreLegToWinA(dartMatchState) && inList(marketType, oneLeg180AMarketSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on one more legs to win the match");
                }
                if (oneMoreLegToWinB(dartMatchState) && inList(marketType, oneLeg180BMarketSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend on one more legs to win the match");
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

    private boolean lessThan(int point, int expectedPoint) {
        if (point <= expectedPoint)
            return true;
        return false;
    }

    private boolean oneMoreLegToWin(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = dartMatchFormat.getnLegsOrSetsInMatch();
        int maxSetScore = nLegsOrSetsInMatch / 2 + 1;
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int setA = dartMatchState.getPlayerAScore().getSets();
        int legA = dartMatchState.getPlayerAScore().getLegs();
        int setB = dartMatchState.getPlayerBScore().getSets();
        int legB = dartMatchState.getPlayerBScore().getLegs();

        if (setA == (maxSetScore - 1) && legA == (maxLegScore - 1))
            return true;
        if (setB == (maxSetScore - 1) && legB == (maxLegScore - 1))
            return true;
        return false;
    }

    private boolean oneMoreLegToWinA(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = dartMatchFormat.getnLegsOrSetsInMatch();
        int maxSetScore = nLegsOrSetsInMatch / 2 + 1;
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int setA = dartMatchState.getPlayerAScore().getSets();
        int legA = dartMatchState.getPlayerAScore().getLegs();

        if (setA == (maxSetScore - 1) && legA == (maxLegScore - 1))
            return true;
        return false;
    }

    private boolean oneMoreLegToWinB(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = dartMatchFormat.getnLegsOrSetsInMatch();
        int maxSetScore = nLegsOrSetsInMatch / 2 + 1;
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int setB = dartMatchState.getPlayerBScore().getSets();
        int legB = dartMatchState.getPlayerBScore().getLegs();

        if (setB == (maxSetScore - 1) && legB == (maxLegScore - 1))
            return true;
        return false;
    }

    private boolean twoMoreLegToWin(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = dartMatchFormat.getnLegsOrSetsInMatch();
        int maxSetScore = nLegsOrSetsInMatch / 2 + 1;
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int setA = dartMatchState.getPlayerAScore().getSets();
        int legA = dartMatchState.getPlayerAScore().getLegs();
        int setB = dartMatchState.getPlayerBScore().getSets();
        int legB = dartMatchState.getPlayerBScore().getLegs();

        if (setA == (maxSetScore - 1) && legA == (maxLegScore - 2))
            return true;
        if (setB == (maxSetScore - 1) && legB == (maxLegScore - 2))
            return true;
        return false;
    }

    private boolean twoMoreLegToWinSet(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int legA = dartMatchState.getPlayerAScore().getLegs();
        int pointA = dartMatchState.getCurrentLeg().getPlayerA().getPoints();
        int legB = dartMatchState.getPlayerBScore().getLegs();
        int pointB = dartMatchState.getCurrentLeg().getPlayerB().getPoints();

        if (legA == (maxLegScore - 2) && lessThan(pointA, 250))
            return true;
        if (legA == (maxLegScore - 1))
            return true;
        if (legB == (maxLegScore - 2) && lessThan(pointB, 250))
            return true;
        if (legB == (maxLegScore - 1))
            return true;
        return false;
    }

    private boolean twoDartDownScore120(DartMatchState dartMatchState) {
        LegState currentLegState = dartMatchState.getCurrentLeg();
        if ((currentLegState.getPlayerA().getNDartsthrown() == 2)
                        && (Integer) currentLegState.getThreeDartSet().getTotal() == 120)
            return true;
        if ((currentLegState.getPlayerB().getNDartsthrown() == 2)
                        && (Integer) currentLegState.getThreeDartSet().getTotal() == 120)
            return true;
        return false;
    }

    private boolean maxTwoSetsRemain(DartMatchState dartMatchState) {
        DartMatchFormat dartMatchFormat = dartMatchState.getMatchFormat();
        int nLegsPerSet = dartMatchFormat.getnLegsPerSet();
        int nLegsOrSetsInMatch = dartMatchFormat.getnLegsOrSetsInMatch();
        int maxLegScore = nLegsPerSet / 2 + 1;
        // boolean setBasedMatch = nLegsPerSet > 1;
        int setA = dartMatchState.getPlayerAScore().getSets();
        int legA = dartMatchState.getPlayerAScore().getLegs();
        int pointA = dartMatchState.getCurrentLeg().getPlayerA().getPoints();
        int setB = dartMatchState.getPlayerBScore().getSets();
        int legB = dartMatchState.getPlayerBScore().getLegs();
        int pointB = dartMatchState.getCurrentLeg().getPlayerB().getPoints();

        if (setA == (nLegsOrSetsInMatch - 2) && legA == (maxLegScore - 1) && lessThan(pointA, 250))
            return true;
        if (setA == (nLegsOrSetsInMatch - 1))
            return true;
        if (setB == (nLegsOrSetsInMatch - 2) && legB == (maxLegScore - 1) && lessThan(pointB, 250))
            return true;
        if (setB == (nLegsOrSetsInMatch - 1))
            return true;
        return false;
    }

    /*
     * returns a two element array n with setNo in n[0], gameNo in n[1]
     */
    private int[] getSetLegNo(String marketDescription) {
        int[] setLegNo = new int[2];
        String[] bits = marketDescription.split("\\.");
        if (bits[0].length() > 1) {
            String setNo = bits[0].substring(1, 2);
            setLegNo[0] = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            setLegNo[1] = Integer.parseInt(bits[1]);
        return setLegNo;
    }

}
