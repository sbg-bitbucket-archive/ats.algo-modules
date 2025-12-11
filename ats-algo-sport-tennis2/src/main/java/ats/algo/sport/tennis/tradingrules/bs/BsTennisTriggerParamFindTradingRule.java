package ats.algo.sport.tennis.tradingrules.bs;

import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.SequenceId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.tradingrules.PriceSourceWeights;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchState;

public class BsTennisTriggerParamFindTradingRule extends TriggerParamFindTradingRule {

    private static final long serialVersionUID = 1L;


    protected BsTennisTriggerParamFindTradingRule() {
        super("BS Tennis TriggerParamFindingTradingRule", null);
    }

    @Override
    protected String processPricesPreMatch(MarketPricesList marketPricesList, PriceSourceWeights priceSourceWeights,
                    EventSettings eventSettings, AlgoMatchState matchState) {

        eventSettingsCheckPrematch(marketPricesList, eventSettings);
        return super.processPricesPreMatch(marketPricesList, priceSourceWeights, eventSettings, matchState);
    }


    /**
     * adds BS specific logic to the standard TennisTriggerParamFind rule
     */
    @Override
    protected String processPricesInPlay(MarketPricesList marketPricesList, AlgoMatchState matchState,
                    PriceSourceWeights priceSourceWeights, EventSettings eventSettings) {

        TennisMatchState tennisMatchState = (TennisMatchState) matchState;
        TennisMatchFormat tennisMatchFormat = (TennisMatchFormat) matchState.getMatchFormat();
        boolean breakPoint = tennisMatchState.isBreakPoint();
        String currentPointSequenceId = tennisMatchState.getSequenceIdForPoint(0);
        boolean intoDeuceSequence = isInDeuceSequence(currentPointSequenceId);
        boolean tieBreak = tennisMatchState.isInTieBreak();
        boolean superTieBreak = tennisMatchState.isInSuperTieBreak();
        boolean finalSet = tennisMatchState.isInFinalSet();

        int gamesA = tennisMatchState.getGamesA();
        int gamesB = tennisMatchState.getGamesB();
        int gameNo = tennisMatchState.getGameNo();

        if ((tennisMatchState.getMatchWinningSetScore() - 1) == tennisMatchState.getSetsA()) {
            boolean stopParamFindA = true;
            if (gamesA < 5) {
                stopParamFindA = false;
            } else {
                if (finalSet) {
                    if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) && gameNo > 10
                                    && gameNo % 2 == 0) {
                        stopParamFindA = false;
                    } else if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.NORMAL_WITH_TIE_BREAK)
                                    && gameNo == 11) {
                        stopParamFindA = false;
                    }
                } else if (gameNo == 11) {
                    stopParamFindA = false;
                } else if (gamesA < gamesB) {
                    stopParamFindA = false;
                }
            }
            if (stopParamFindA) {
                return "Potential match winning game for Player A";
            }
        }
        if ((tennisMatchState.getMatchWinningSetScore() - 1) == tennisMatchState.getSetsB()) {
            boolean stopParamFindB = true;
            if (gamesB < 5) {
                stopParamFindB = false;
            } else {
                if (finalSet) {
                    if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) && gameNo > 10
                                    && gameNo % 2 == 0) {
                        stopParamFindB = false;
                    } else if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.NORMAL_WITH_TIE_BREAK)
                                    && gameNo == 11) {
                        stopParamFindB = false;
                    }
                } else if (gameNo == 11) {
                    stopParamFindB = false;
                } else if (gamesB < gamesA) {
                    stopParamFindB = false;
                }
            }
            if (stopParamFindB) {
                return "Potential match winning game for Player B";
            }
        }


        if (breakPoint)
            return "At break point";
        if (intoDeuceSequence)
            return "In deuce sequence";
        if (tieBreak || superTieBreak)
            return "In tiebreak";

        boolean firstPoint = "S1.1.1".equals(currentPointSequenceId);
        if (firstPoint)
            updatePricesforFirstPoint(marketPricesList);

        eventSettingsCheckInplay(marketPricesList, matchState, eventSettings);

        filterAsianAndHandicapAndTotalMarkets(marketPricesList);
        /*
         * mark currentGameWinner price invalid, if present and if within a game and mark G:ML prices for any earlier
         * markets invalid
         */
        if (((TennisMatchState) matchState).isInTieBreak())
            return "In tiebreak";

        String currentGameSequenceId = ((TennisMatchState) matchState).getSequenceIdForGame(0);
        int currentGame = SequenceId.getSequenceIdComparator(currentGameSequenceId);


        if (!firstPoint)
            checkValidityOfNextGameMarkets(marketPricesList, currentGame);

        return (super.processPricesInPlay(marketPricesList, matchState, priceSourceWeights, eventSettings));

    }

    private void eventSettingsCheckPrematch(MarketPricesList marketPricesList, EventSettings eventSettings) {
        long eventTier = eventSettings.getEventTier();

        if (eventTier == 4) {
            for (MarketPrices marketPrices : marketPricesList.values()) {
                for (MarketPrice marketPrice : marketPrices) {
                    if (marketPrice.getType().equals("FT:OU")) {
                        marketPrice.setValid(false);
                    }
                }
            }
        }
    }


    private void eventSettingsCheckInplay(MarketPricesList marketPricesList, AlgoMatchState matchState,
                    EventSettings eventSettings) {
        // TODO Auto-generated method stub

    }

    private void updatePricesforFirstPoint(MarketPricesList marketPricesList) {

        for (MarketPrices marketPrices : marketPricesList.values()) {
            for (MarketPrice marketPrice : marketPrices) {
                if (marketPrice.getType().equals("G:ML") && !marketPrice.getSequenceId().equals("S1.1")) {
                    marketPrice.setValid(false);
                } else if (marketPrice.getType().equals("G:ML") && marketPrice.getSequenceId().equals("S1.1")) {
                    marketPrice.setValid(true);
                }

            }
        }

    }

    protected void checkValidityOfNextGameMarkets(MarketPricesList marketPricesList, int currentGame) {
        int nextGame = currentGame + 1;
        for (MarketPrices marketPrices : marketPricesList.values()) {
            for (MarketPrice marketPrice : marketPrices) {
                if (marketPrice.getType().equals("G:ML")) {
                    if (SequenceId.getSequenceIdComparator(marketPrice.getSequenceId()) != nextGame) {
                        marketPrice.setValid(false);
                    } else if (SequenceId.getSequenceIdComparator(marketPrice.getSequenceId()) == nextGame) {
                        marketPrice.setValid(true);
                    }

                }
            }
        }
    }

    protected void filterAsianAndHandicapAndTotalMarkets(MarketPricesList marketPricesList) {

        /*
         * mark any markets invalid ir they have line id's of form "22", instead of "22.5", if present
         */
        for (MarketPrices marketPrices : marketPricesList.values()) {
            for (MarketPrice marketPrice : marketPrices) {
                String lineId = marketPrice.getLineId();
                if (isInteger(lineId)) {
                    marketPrice.setValid(false);
                }
            }
        }
    }

    protected boolean isInteger(String lineId) {
        boolean isInteger;
        try {
            Integer.parseInt(lineId);
            isInteger = true;
        } catch (NumberFormatException e) {
            isInteger = false;
        }
        return isInteger;
    }

    protected boolean isInDeuceSequence(String sequenceId) {
        String pointId = sequenceId.substring(5);
        int seqIdOfGame = Integer.parseInt(pointId);

        if (seqIdOfGame > 6)
            return true;
        return false;
    }
}
