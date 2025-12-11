package ats.algo.sport.football.tradingrules;

import static ats.algo.sport.football.tradingrules.FootballTradingList.ALL_MARKET;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.core.comparetomarket.TraderParamFindResultsDescription;
import ats.algo.core.comparetomarket.TraderParamFindResultsDetailRow;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRuleType;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchState;

public class FootballTradingRule extends SetSuspensionStatusTradingRule {

    private static final long serialVersionUID = 1L;

    private List<String> lineCapdMarketList;// jin added
    private int[] lineRages;
    private Map<String, Double> priceCapList;
    private Map<String, Double> extraMarginList;
    private List<String> prematchDisplayMarketList;
    private List<String> inplayDisplayMarketList;
    private List<String> inplaySuspendMarketList;
    private List<String> goalSuspendMarketList;
    private List<String> cornerSuspendMarketList;
    private List<String> redCardSuspendMarketList;
    private List<String> yellowCardSuspendMarketList;
    private List<String> penaltySuspendMarketList;
    private List<String> halfTimeSuspendMarketList;
    // private List<List<String>> minuteSuspendMatketList;
    private List<String> fullTimeSuspendMarketList;
    private List<String> finalMinuteDisplayMarketList;
    private List<String> fiveMinuteSuspendMarketList;
    private List<String> tenMinuteSuspendMarketList;
    private List<String> fifteenMinuteSuspendMarketList;
    private List<String> beforeFullTimeOpenMarketList;
    private List<String> extraTimeOpenMarketList;
    private List<String> halfTimeExtraTimeSuspendMarketList;
    private List<String> fullTimeExtraTimeSuspendMarketList;
    private List<String> penaltyOpenMarketList;
    private Map<String, MarketGroup> paramFindSuspensionList;
    private int halfTimeSuspend;
    private int halfTimeEnd;
    private int fullTimeEnd;
    private int extraHalfTimeEnd;
    private int extraTimeEnd;
    private int fullTimeSuspend;
    private int minuteSusspend;
    private int[] min;
    private int elapsedMinutesForSuspensionOfMarkets;
    private boolean filterMarketByClients = false;
    private List<String> filterMarketList;

    // private boolean possibleGoalStatusUnsettled = false;
    // private boolean gsMarketClose = false;

    public List<String> getPenaltyOpenMarketList() {
        return penaltyOpenMarketList;
    }

    public boolean isFilterMarketByClients() {
        return filterMarketByClients;
    }

    public void setFilterMarketByClients(boolean filterMarketByClients) {
        this.filterMarketByClients = filterMarketByClients;
    }

    public List<String> getFilterMarketList() {
        return filterMarketList;
    }

    public void setFilterMarketList(List<String> filterMarketList) {
        this.filterMarketList = filterMarketList;
    }

    public void setPenaltyOpenMarketList(List<String> penaltyOpenMarketList) {
        this.penaltyOpenMarketList = penaltyOpenMarketList;
    }

    public int getExtraHalfTimeEnd() {
        return extraHalfTimeEnd;
    }

    public void setExtraHalfTimeEnd(int extraHalfTimeEnd) {
        this.extraHalfTimeEnd = extraHalfTimeEnd;
    }

    public int getExtraTimeEnd() {
        return extraTimeEnd;
    }

    public void setExtraTimeEnd(int extraTimeEnd) {
        this.extraTimeEnd = extraTimeEnd;
    }

    public List<String> getBeforeFullTimeOpenMarketList() {
        return beforeFullTimeOpenMarketList;
    }

    public void setBeforeFullTimeOpenMarketList(List<String> beforeFullTimeOpenMarketList) {
        this.beforeFullTimeOpenMarketList = beforeFullTimeOpenMarketList;
    }

    public List<String> getExtraTimeOpenMarketList() {
        return extraTimeOpenMarketList;
    }

    public void setExtraTimeOpenMarketList(List<String> extraTimeOpenMarketList) {
        this.extraTimeOpenMarketList = extraTimeOpenMarketList;
    }

    public List<String> getHalfTimeExtraTimeSuspendMarketList() {
        return halfTimeExtraTimeSuspendMarketList;
    }

    public void setHalfTimeExtraTimeSuspendMarketList(List<String> halfTimeExtraTimeSuspendMarketList) {
        this.halfTimeExtraTimeSuspendMarketList = halfTimeExtraTimeSuspendMarketList;
    }

    public List<String> getFullTimeExtraTimeSuspendMarketList() {
        return fullTimeExtraTimeSuspendMarketList;
    }

    public void setFullTimeExtraTimeSuspendMarketList(List<String> fullTimeExtraTimeSuspendMarketList) {
        this.fullTimeExtraTimeSuspendMarketList = fullTimeExtraTimeSuspendMarketList;
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

    public List<String> getGoalSuspendMarketList() {
        return goalSuspendMarketList;
    }

    public void setGoalSuspendMarketList(List<String> goalSuspendMarketList) {
        this.goalSuspendMarketList = goalSuspendMarketList;
    }

    public List<String> getCornerSuspendMarketList() {
        return cornerSuspendMarketList;
    }

    public void setCornerSuspendMarketList(List<String> cornerSuspendMarketList) {
        this.cornerSuspendMarketList = cornerSuspendMarketList;
    }

    public List<String> getRedCardSuspendMarketList() {
        return redCardSuspendMarketList;
    }

    public void setRedCardSuspendMarketList(List<String> redCardSuspendMarketList) {
        this.redCardSuspendMarketList = redCardSuspendMarketList;
    }

    public List<String> getYellowCardSuspendMarketList() {
        return yellowCardSuspendMarketList;
    }

    public void setYellowCardSuspendMarketList(List<String> yellowCardSuspendMarketList) {
        this.yellowCardSuspendMarketList = yellowCardSuspendMarketList;
    }

    public List<String> getPenaltySuspendMarketList() {
        return penaltySuspendMarketList;
    }

    public void setPenaltySuspendMarketList(List<String> penaltySuspendMarketList) {
        this.penaltySuspendMarketList = penaltySuspendMarketList;
    }

    public List<String> getHalfTimeSuspendMarketList() {
        return halfTimeSuspendMarketList;
    }

    public void setHalfTimeSuspendMarketList(List<String> halfTimeSuspendMarketList) {
        this.halfTimeSuspendMarketList = halfTimeSuspendMarketList;
    }

    public List<String> getFullTimeSuspendMarketList() {
        return fullTimeSuspendMarketList;
    }

    public void setFullTimeSuspendMarketList(List<String> fullTimeSuspendMarketList) {
        this.fullTimeSuspendMarketList = fullTimeSuspendMarketList;
    }


    public Map<String, Double> getPriceCapList() {
        return priceCapList;
    }

    public void setPriceCapList(Map<String, Double> priceCapList) {
        this.priceCapList = priceCapList;
    }

    public Map<String, Double> getExtraMarginList() {
        return extraMarginList;
    }

    public void setExtraMarginList(Map<String, Double> extraMarginList) {
        this.extraMarginList = extraMarginList;
    }

    public List<String> getFinalMinuteDisplayMarketList() {
        return finalMinuteDisplayMarketList;
    }

    public void setFinalMinuteDisplayMarketList(List<String> finalMinuteDisplayMarketList) {
        this.finalMinuteDisplayMarketList = finalMinuteDisplayMarketList;
    }

    public List<String> getFiveMinuteSuspendMarketList() {
        return fiveMinuteSuspendMarketList;
    }

    public void setFiveMinuteSuspendMarketList(List<String> fiveMinuteSuspendMarketList) {
        this.fiveMinuteSuspendMarketList = fiveMinuteSuspendMarketList;
    }

    public List<String> getTenMinuteSuspendMarketList() {
        return tenMinuteSuspendMarketList;
    }

    public void setTenMinuteSuspendMarketList(List<String> tenMinuteSuspendMarketList) {
        this.tenMinuteSuspendMarketList = tenMinuteSuspendMarketList;
    }

    public List<String> getFifteenMinuteSuspendMarketList() {
        return fifteenMinuteSuspendMarketList;
    }

    public void setFifteenMinuteSuspendMarketList(List<String> fifteenMinuteSuspendMarketList) {
        this.fifteenMinuteSuspendMarketList = fifteenMinuteSuspendMarketList;
    }

    public Map<String, MarketGroup> getParamFindSuspensionList() {
        return paramFindSuspensionList;
    }

    public void addParamFindSuspensionList(String marketsNeededInParamFindForMarketGroup, MarketGroup marketGroup) {
        paramFindSuspensionList.put(marketsNeededInParamFindForMarketGroup, marketGroup);
    }

    public int getHalfTimeSuspend() {
        return halfTimeSuspend;
    }

    public List<String> getInplaySuspendMarketList() {
        return inplaySuspendMarketList;
    }

    public void setInplaySuspendMarketList(List<String> inplaySuspendMarketList) {
        this.inplaySuspendMarketList = inplaySuspendMarketList;
    }

    public void setHalfTimeSuspend(int halfTimeSuspend) {
        this.halfTimeSuspend = halfTimeSuspend;
    }

    public int getFullTimeSuspend() {
        return fullTimeSuspend;
    }

    public void setFullTimeSuspend(int fullTimeSuspend) {
        this.fullTimeSuspend = fullTimeSuspend;
    }

    public int getMinuteSusspend() {
        return minuteSusspend;
    }

    public void setMinuteSusspend(int minuteSusspend) {
        this.minuteSusspend = minuteSusspend;
    }

    public int[] getMin() {
        return min;
    }

    public void setMin(int[] min) {
        this.min = min;
    }

    public int getHalfTimeEnd() {
        return halfTimeEnd;
    }

    public void setHalfTimeEnd(int halfTimeEnd) {
        this.halfTimeEnd = halfTimeEnd;
    }

    public int getFullTimeEnd() {
        return fullTimeEnd;
    }

    public void setFullTimeEnd(int fullTimeEnd) {
        this.fullTimeEnd = fullTimeEnd;
    }

    public List<String> getLineCapdMarketList() {
        return lineCapdMarketList;
    }

    public void setLineCapdMarketList(List<String> lineCapdMarketList) {
        this.lineCapdMarketList = lineCapdMarketList;
    }

    public int[] getLineRages() {
        return lineRages;
    }

    public void setLineRages(int[] lineRages) {
        this.lineRages = lineRages;
    }

    public FootballTradingRule() {
        paramFindSuspensionList = new HashMap<String, MarketGroup>();
    }

    /**
     * 
     * @param ruleName
     * @param eventTier
     * @param marketGroup
     * @param marketTier
     */
    public FootballTradingRule(String ruleName, Integer eventTier, MarketGroup marketGroup) {
        super(TradingRuleType.SET_MARKET_SUSPENSION_STATUS, ruleName, eventTier, marketGroup);
        elapsedMinutesForSuspensionOfMarkets = 89;
        paramFindSuspensionList = new HashMap<String, MarketGroup>();
    }

    @Override
    public void applyRule(MatchState matchState, Market market, CalcRequestCause priceCalcCause,
                    TriggerParamFindData triggerParamFindData) {

        MarketStatus marketStatus = market.getMarketStatus();
        FootballMatchState footballMatchState = (FootballMatchState) matchState;
        FootballMatchFormat matchFormat = (FootballMatchFormat) footballMatchState.getMatchFormat();
        this.setHalfTimeEnd(matchFormat.getNormalTimeMinutes() / 2);
        this.setFullTimeEnd(matchFormat.getNormalTimeMinutes());
        this.setExtraHalfTimeEnd(matchFormat.getExtraTimeMinutes() / 2);
        this.setExtraTimeEnd(matchFormat.getExtraTimeMinutes());
        MatchIncident matchIncident = footballMatchState.getLastMatchIncidentType();
        String marketType = market.getType();
        String selectionTypeA = market.getSelectionNameOverOrA();
        String selectionTypeB = market.getSelectionNameUnderOrB();


        if (filterMarketByClients && !filterMarketList.isEmpty()) {
            if (inList(marketType, filterMarketList))
                market.setFilterMarketByClient(true);
            else
                market.setFilterMarketByClient(false);
        }
        if (footballMatchState.preMatch()) {
            /*
             * assume if prematchdisplayMarketList is null then applies to all markets
             */
            if (prematchDisplayMarketList != null && !inList(marketType, prematchDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open pre-match");

            }

            if (lineCapdMarketList != null && lineRages != null && inList(marketType, lineCapdMarketList)) {
                if (lineRages.length == 2) {
                    if (Double.parseDouble(market.getLineId()) > lineRages[1]
                                    || Double.parseDouble(market.getLineId()) < lineRages[0])
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Line Cap Suspension");
                } else {
                    throw new IllegalArgumentException("Line Ranges Should be in a pair");
                }

            }
            double maxPrice = 0.0;

            if (priceCapList != null && priceCapList.containsKey(marketType)) {
                maxPrice = priceCapList.get(marketType);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    if (entry.getValue() < 1 / maxPrice)
                        entry.setValue(1 / maxPrice);
                }

            }

            if (priceCapList != null && priceCapList.containsKey(marketType + selectionTypeA)) {
                maxPrice = priceCapList.get(marketType + selectionTypeA);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    if (entry.getKey().equals(selectionTypeA) && entry.getValue() > maxPrice)
                        entry.setValue(maxPrice);
                }

            }
            if (priceCapList != null && priceCapList.containsKey(marketType + selectionTypeB)) {
                maxPrice = priceCapList.get(marketType + selectionTypeB);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    if (entry.getKey().equals(selectionTypeB) && entry.getValue() < maxPrice)
                        entry.setValue(maxPrice);
                }

            }

            if (extraMarginList != null && extraMarginList.containsKey(marketType)) {
                double extraMargin = extraMarginList.get(marketType);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    entry.setValue(extraMargin + entry.getValue());
                }

            }

        } else { // in play
            /*
             * check whether open
             */

            if (priceCapList != null && priceCapList.containsKey(marketType)) {
                double maxPrice = priceCapList.get(marketType);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    if (entry.getValue() < 1 / maxPrice)
                        entry.setValue(1 / maxPrice);
                }
            }

            if (extraMarginList != null && extraMarginList.containsKey(marketType)) {
                double extraMargin = extraMarginList.get(marketType);
                for (Map.Entry<String, Double> entry : market.getSelectionsProbs().entrySet()) {
                    entry.setValue(extraMargin + entry.getValue());
                }
            }

            if (inplayDisplayMarketList != null && !inList(marketType, inplayDisplayMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");

            }

            if (inplaySuspendMarketList != null && inList(marketType, inplaySuspendMarketList)) {
                marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Not open in play");
            }


            if (lineCapdMarketList != null && lineRages != null && inList(marketType, lineCapdMarketList)) {
                if (lineRages.length == 2) {
                    if (Double.parseDouble(market.getLineId()) > lineRages[1]
                                    || Double.parseDouble(market.getLineId()) < lineRages[0])
                        marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                        "Line Cap Suspension");
                } else {
                    throw new IllegalArgumentException("Line Ranges Should be in a pair");
                }

            }

            int elapsedTime = footballMatchState.getElapsedTimeSecs() / 60;
            int differenceBetweenCurrentTimeAndIncident = footballMatchState.getElapsedTimeSecs()
                            - footballMatchState.getElapsedTimeAtLastMatchIncidentSecs();


            if (elapsedTime >= elapsedMinutesForSuspensionOfMarkets) {
                if (fullTimeSuspendMarketList != null && !inList(marketType, fullTimeSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend and remove as at end of game");
                }
                if (fullTimeSuspendMarketList != null && inList(marketType, fullTimeSuspendMarketList)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                                    "Suspend as at end of game");
                }

                if (differenceBetweenCurrentTimeAndIncident > 90) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend and remove as at end of game and no more incidents");
                }
            } else {

                int timeSinceLastIncidentOfAnyType = footballMatchState.getElapsedTimeAtLastMatchIncidentSecs();
                int timeSinceLastIncidentOfFootballType =
                                footballMatchState.getElapsedTimeAtLastFootballMatchIncidentSecs();

                Class<? extends MatchIncident> clazz = matchIncident.getClass();

                if (clazz == FootballMatchIncident.class) {

                    FootballMatchIncident lastMatchIncident = (FootballMatchIncident) matchIncident;

                    switch (lastMatchIncident.getIncidentSubType()) {
                        case GOAL:
                        case RED_CARD:
                        case PENALTY:
                            /*
                             * these incidents cause event level suspension via the
                             * FootballSuspendToAwaitParamFindTradingRule so no need to do anything at the individual
                             * market level
                             */
                            break;
                        case CORNER:
                            if (market.getMarketGroup().equals(MarketGroup.CORNERS)
                                            && priceCalcCause.equals(CalcRequestCause.MATCH_INCIDENT))
                                marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                SuspensionStatus.SUSPENDED_DISPLAY, "Suspend and display for a corner");
                            break;
                        case YELLOW_CARD:
                            if (market.getMarketGroup().equals(MarketGroup.BOOKINGS)
                                            && priceCalcCause.equals(CalcRequestCause.MATCH_INCIDENT))
                                marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                SuspensionStatus.SUSPENDED_DISPLAY, "Suspend and display for a corner");
                            break;
                        default:
                            break;
                    }

                } else if (clazz == TeamSheetMatchIncident.class) {

                } else {
                    switch (market.getMarketGroup()) {
                        case BOOKINGS:
                            if (footballMatchState.getLastFootballMatchIncidentType() != null
                                            && timeSinceLastIncidentOfFootballType != -1) {
                                if (footballMatchState.getLastFootballMatchIncidentType()
                                                .equals(FootballMatchIncidentType.YELLOW_CARD)) {
                                    if (timeSinceLastIncidentOfAnyType - timeSinceLastIncidentOfFootballType < 60) {
                                        marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                        SuspensionStatus.SUSPENDED_DISPLAY,
                                                        "Still within the 60 seconds of a yellow card");
                                    }
                                }
                            }
                            break;
                        case CORNERS:
                            if (footballMatchState.getLastFootballMatchIncidentType() != null
                                            && timeSinceLastIncidentOfFootballType != -1) {
                                if (footballMatchState.getLastFootballMatchIncidentType()
                                                .equals(FootballMatchIncidentType.CORNER)) {
                                    if (timeSinceLastIncidentOfAnyType - timeSinceLastIncidentOfFootballType < 60) {
                                        marketStatus.setStatusIfHigherPriority(getRuleName(),
                                                        SuspensionStatus.SUSPENDED_DISPLAY,
                                                        "Still within the 60 seconds of a corner");
                                    }
                                }
                            }
                            break;
                        default:
                            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.OPEN,
                                            "60 seconds has passed so can open other market groups");
                            break;
                    }
                }


            }
        }

        boolean filterMarkets = false;
        if (paramFindSuspensionList.size() > 0 && triggerParamFindData
                        .getLastSuccessfulTraderParamFindResultsDescription().getResultSummary() != null) {

            TraderParamFindResultsDescription traderParamFindResultsDescription =
                            triggerParamFindData.getLastSuccessfulTraderParamFindResultsDescription();
            String[] marketTypesParamFoundAgainst =
                            new String[traderParamFindResultsDescription.getResultDetailRows().size()];
            int i = 0;

            for (TraderParamFindResultsDetailRow e : traderParamFindResultsDescription.getResultDetailRows()) {

                marketTypesParamFoundAgainst[i] = e.getMarketType();
                i++;
            }

            // System.out.println("Markets we param found against = " + Arrays.toString(marketTypesParamFoundAgainst));

            for (Entry<String, MarketGroup> e : paramFindSuspensionList.entrySet()) {

                MarketGroup marketGroup = e.getValue();

                String marketTypesFromSuspensionList = e.getKey();
                if (marketTypesFromSuspensionList.contains(",")) {
                    /*
                     * a list of market types that can be used interchangeably, e.g.for football at least one of Match
                     * Result and Asian Handicap must be provided
                     */
                    String[] paramFindMarketTypesNeededOrWillSuspend = marketTypesFromSuspensionList.split(",");
                    // System.out.println("Markets needed or will suspend = " +
                    // Arrays.toString(paramFindMarketTypesNeededOrWillSuspend));
                    int numOfMarkets = paramFindMarketTypesNeededOrWillSuspend.length;
                    // System.out.println("Number of markets still needed off list = " + numOfMarkets);
                    for (String marketTypeNeeded : paramFindMarketTypesNeededOrWillSuspend) {
                        for (String marketTypeFromParamFind : marketTypesParamFoundAgainst) {
                            // System.out.println("Needed = " + marketTypeNeeded + " ParamFind = " +
                            // marketTypeFromParamFind);
                            if (marketTypeFromParamFind.contains(marketTypeNeeded)) {
                                numOfMarkets = numOfMarkets - 1;
                                // System.out.println("Updated num of markets = " + numOfMarkets);
                                break;
                            }

                        }
                    }
                    if (numOfMarkets > 0) {
                        filterMarkets = true;
                    }
                }
                if (filterMarkets && market.getMarketGroup().equals(marketGroup)) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend and undisplay as we did not param find against this market group");
                }
            }
        }

        Collection<Double> selections = market.getSelectionsProbs().values();
        if (selections.size() == 2) {
            for (double selection : selections) {
                if (selection == 1) {
                    marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_UNDISPLAY,
                                    "Suspend and undisplay as this market has 100% probability");
                }
            }
        }


        if (footballMatchState.isVarReferralInProgress()) {
            marketStatus.setStatusIfHigherPriority(getRuleName(), SuspensionStatus.SUSPENDED_DISPLAY,
                            "Var-referral in progress");
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
        if (marketTypeList == null) {
            return false;
        }
        if (marketTypeList.contains(marketType)) {
            return true;
        }
        if (marketTypeList.size() == 1 && marketTypeList.get(0).equals(ALL_MARKET)) {
            return true;
        }
        return false;
    }
}
