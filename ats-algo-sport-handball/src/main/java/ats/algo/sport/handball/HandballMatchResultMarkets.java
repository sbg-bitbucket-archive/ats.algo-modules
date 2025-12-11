package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class HandballMatchResultMarkets extends MatchResultMarkets {

    HandballMatchState previousMatchState;
    HandballMatchState currentMatchState;

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (HandballMatchState) previousMatchState;
        this.currentMatchState = (HandballMatchState) currentMatchState;
        String winningSelection = null;
        switch (market.getType()) {
            case "FT:AXB": // Match winner
                TeamId matchWinner = this.currentMatchState.getMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                            break;
                        case B:
                            winningSelection = "B";
                            break;
                        case UNKNOWN:
                            winningSelection = "Draw";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;


            case "FT:OU": // goals total
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() + this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:A:OU": // goals total home
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:B:OU": // goals total away
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD": // match handicap
                if (this.currentMatchState.isMatchCompleted()) {
                    int n = this.currentMatchState.getGoalsA() - this.currentMatchState.getGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "OT:NG": // next goal
                int marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                int currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                int previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastGoal() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "FT:DBLC": // next goal
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getNormalTimeGoalsA() > this.currentMatchState.getNormalTimeGoalsB())
                        winningSelection = "A";
                    if (this.currentMatchState.getNormalTimeGoalsA() < this.currentMatchState.getNormalTimeGoalsA())
                        winningSelection = "B";
                    else
                        winningSelection = "Draw";
                    return new CheckMarketResultedOutcome(winningSelection);

                } else {

                    return null;

                }
            case "FT:DNB": // next goal
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB())
                        winningSelection = "A";
                    if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB())
                        winningSelection = "B";
                    else
                        winningSelection = "Draw";
                    return new CheckMarketResultedOutcome(winningSelection);

                } else {

                    return null;

                }
            case "P:DNB":
                int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                if (this.currentMatchState.getGoalsA() > this.currentMatchState.getGoalsB())
                    winningSelection = "A";
                if (this.currentMatchState.getGoalsA() < this.currentMatchState.getGoalsB())
                    winningSelection = "B";
                else
                    winningSelection = "Draw";
                return new CheckMarketResultedOutcome(winningSelection);

            case "P:DBLC": // PERIOD DOUBLE CHANCE
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    if (this.currentMatchState.getNormalTimeGoalsA() > this.currentMatchState.getNormalTimeGoalsB())
                        winningSelection = "A";
                    if (this.currentMatchState.getNormalTimeGoalsA() < this.currentMatchState.getNormalTimeGoalsB())
                        winningSelection = "B";
                    else
                        winningSelection = "Draw";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Should not get here");
                }
            case "P:OU": // total goals in current half
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    throw new IllegalArgumentException("Should not get here");
                }

            case "P:SPRD": // CURRENT HALF HANDICAP
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    int n = this.currentMatchState.getPreviousPeriodGoalsA()
                                    + this.currentMatchState.getPreviousPeriodGoalsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    throw new IllegalArgumentException("Should not get here");
                }

            case "P:AXB": // CURRENT HALF winner, general type, did not do check market result outcome

                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                TeamId periodWinner = this.currentMatchState.getPeriodWinner(periodSeqNo);

                switch (periodWinner) {
                    case A:
                        winningSelection = "A";
                        break;
                    case B:
                        winningSelection = "B";
                        break;
                    case UNKNOWN:
                        winningSelection = "Draw";
                        break;
                }
                return new CheckMarketResultedOutcome(winningSelection);

            default:
                throw new IllegalArgumentException(
                                "Market type missing from HandballMatchResultMarkets: " + market.getType());

        }
    }

    /**
     * Converts string of form "Pnn" to integer
     * 
     * @param periodSequenceId
     * @return
     */
    private int convertPeriodSeqIdToIndex(String sequenceId) {
        int index = 0;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }

    private int convertGoalSeqIdToIndex(String sequenceId) {
        int index = 0;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }
}
