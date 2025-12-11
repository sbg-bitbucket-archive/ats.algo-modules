package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;

public class AmericanfootballMatchResultMarkets extends MatchResultMarkets {

    AmericanfootballMatchState previousMatchState;
    AmericanfootballMatchState currentMatchState;

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (AmericanfootballMatchState) previousMatchState;
        this.currentMatchState = (AmericanfootballMatchState) currentMatchState;
        String winningSelection = null;
        switch (market.getType()) {
            case "FT:AXB": // Match winner
                TeamId matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                        case B:
                            winningSelection = "B";
                        case UNKNOWN:
                            winningSelection = "Draw";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:ML": // Match winner
                matchWinner = this.currentMatchState.getMatchWinner();
                if (matchWinner != null) {
                    switch (matchWinner) {
                        case A:
                            winningSelection = "A";
                            break;
                        case B:
                            winningSelection = "B";
                            break;
                        case UNKNOWN:
                            throw new IllegalArgumentException("Match must be settled ");
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:5MR": // 5 mins Match winner
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                // int fiveMinsMarketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
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

            case "FT:5MG": // 5 mins Match winner
                matchWinner = this.currentMatchState.getFiveMinsMatchWinner();
                // fiveMinsMarketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
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
            case "FT:HH": // Match winner

                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int firstHalfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                    + this.currentMatchState.getSecondQuarterPointsA();
                    int firstHalfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                    + this.currentMatchState.getSecondQuarterPointsB();
                    int secondHalfPointsA = this.currentMatchState.getThirdQuarterPointsA()
                                    + this.currentMatchState.getFourthQuarterPointsA();
                    int secondHalfPointsB = this.currentMatchState.getThirdQuarterPointsB()
                                    + this.currentMatchState.getFourthQuarterPointsB();


                    TeamId highScoreHalf = TeamId.UNKNOWN;
                    if (firstHalfPointsA + firstHalfPointsB > secondHalfPointsA + secondHalfPointsB) {
                        highScoreHalf = TeamId.A;
                    } else if (firstHalfPointsA + firstHalfPointsB < secondHalfPointsA + secondHalfPointsB) {
                        highScoreHalf = TeamId.B;
                    } else {
                        highScoreHalf = TeamId.UNKNOWN;
                    }
                    switch (highScoreHalf) {
                        case A:
                            winningSelection = "H1";
                            break;
                        case B:
                            winningSelection = "H2";
                            break;
                        case UNKNOWN:
                            winningSelection = "Draw";
                            break;
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:DBLC": // Match winner

                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int fullTimeGoalsA = this.currentMatchState.getPointsA();
                    int fullTimeGoalsB = this.currentMatchState.getPointsB();

                    TeamId matchDoubleChance = TeamId.UNKNOWN;
                    if (fullTimeGoalsA >= fullTimeGoalsB) {
                        matchDoubleChance = TeamId.A;
                    }
                    if (fullTimeGoalsA <= fullTimeGoalsB) {
                        matchDoubleChance = TeamId.B;
                    }
                    if (fullTimeGoalsA > fullTimeGoalsB || fullTimeGoalsA < fullTimeGoalsB) {
                        matchDoubleChance = TeamId.UNKNOWN;
                    }
                    switch (matchDoubleChance) {
                        case A:
                            winningSelection = "AX";
                        case B:
                            winningSelection = "BX";
                        case UNKNOWN:
                            winningSelection = "AB";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:CS": // correct score
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getPointsA(),
                                    this.currentMatchState.getPointsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FTOT:CS": // correct score
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getPointsA(),
                                    this.currentMatchState.getPointsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:TFG": // time first goal
                int timeFirstGoal = this.currentMatchState.getElapsedTimeSecsFirstGoal();
                if (timeFirstGoal != 0) {
                    winningSelection = String.format("%d", 1 + timeFirstGoal / 600);
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    winningSelection = String.format("No Goal");
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OU": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OU:ALT": // goals total
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:A:OU": // goals total home
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:OT": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if (this.currentMatchState.getPointsA() == this.currentMatchState.getPointsB()) {
                        winningSelection = "Yes";
                    } else {
                        winningSelection = "No";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:OE": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    if ((this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB()) % 2 == 0) {
                        winningSelection = "Even";
                    } else {
                        winningSelection = "Odd";
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:B:OU": // goals total away
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD": // match handicap
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:SPRD:ALT": // alternative match handicap
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:EH": // match handicap EURO
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:TDOU": // match handicap EURO
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getTdA() + this.currentMatchState.getTdB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;

            case "FT:DNB": // Match winner
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                    if (matchWinner != null) {
                        switch (matchWinner) {
                            case A:
                                winningSelection = "A win or Draw && A win or B win";
                            case B:
                                winningSelection = "B win or Draw && A win or B win";
                            case UNKNOWN:
                                winningSelection = "A win or Draw && B win or Draw";
                        }
                        return new CheckMarketResultedOutcome(winningSelection);
                    }
                } else
                    return null;

            case "FT:NG": // next goal
                int marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                int currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                int previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastPoint() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            case "P:AXB": // winner in current period

                boolean isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");

                if (isHalfMarket) {

                    int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentHalfSeqNo <= halfSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousHalfSeqNo >= halfSeqNo) {
                        int a = this.currentMatchState.getPreviousHalfPointsA();
                        int b = this.currentMatchState.getPreviousHalfPointsB();
                        if (a > b) {
                            winningSelection = "A";
                        } else if (a < b) {
                            winningSelection = "B";
                        } else {
                            winningSelection = "Draw";
                        }

                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                } else {

                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo =
                                    convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousPeriodSeqNo =
                                    convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousPeriodSeqNo == periodSeqNo) {
                        int a = this.currentMatchState.getPreviousPeriodPointsA();
                        int b = this.currentMatchState.getPreviousPeriodPointsB();
                        if (a > b) {
                            winningSelection = "A";
                        } else if (a < b) {
                            winningSelection = "B";
                        } else {
                            winningSelection = "Draw";
                        }

                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                }

                // case "P:AXB": // winner in current half
                // int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                // int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                // if (currentHalfSeqNo <= halfSeqNo) {
                // /*
                // * this half is still progressing
                // */
                // return null;
                // }
                //
                // int previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                // if (previousHalfSeqNo >= halfSeqNo) {
                // int a = this.currentMatchState.getPreviousHalfPointsA();
                // int b = this.currentMatchState.getPreviousHalfPointsB();
                // if (a > b) {
                // winningSelection = "A";
                // } else if (a < b) {
                // winningSelection = "B";
                // } else {
                // winningSelection = "Draw";
                // }
                //
                // return new CheckMarketResultedOutcome(winningSelection);
                // } else {
                // throw new IllegalArgumentException("Should not get here");
                // }

            case "P:SPRD": // half match handicap
                isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                if (isHalfMarket) {
                    int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentHalfSeqNo <= halfSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForHalf(0));
                    if (previousHalfSeqNo == halfSeqNo) {
                        int n = this.currentMatchState.getPreviousHalfPointsA()
                                        - this.currentMatchState.getPreviousHalfPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                } else {

                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo =
                                    convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousPeriodSeqNo =
                                    convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousPeriodSeqNo == periodSeqNo) {
                        int n = this.currentMatchState.getPreviousPeriodPointsA()
                                        - this.currentMatchState.getPreviousPeriodPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                }

            case "P:OU": // Period Goals totals
                isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                if (isHalfMarket) {
                    int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentHalfSeqNo <= halfSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForHalf(0));
                    if (previousHalfSeqNo == halfSeqNo) {
                        int n = this.currentMatchState.getPreviousHalfPointsA()
                                        + this.currentMatchState.getPreviousHalfPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                } else {
                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo =
                                    convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousPeriodSeqNo =
                                    convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousPeriodSeqNo == periodSeqNo) {
                        int n = this.currentMatchState.getPreviousPeriodPointsA()
                                        + this.currentMatchState.getPreviousPeriodPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                }

            case "P:TDOU": // HALVES goals total
                int halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                if (currentHalfSeqNo <= halfSeqNo) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }

                int previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForHalf(0));
                if (previousHalfSeqNo == 1) {
                    int n = this.currentMatchState.getFirstQuarterTDA() + this.currentMatchState.getFirstQuarterTDB()
                                    + this.currentMatchState.getSecondQuarterTDA()
                                    + this.currentMatchState.getSecondQuarterTDB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else if (previousHalfSeqNo == 2) {
                    int n = this.currentMatchState.getThirdQuarterTDA() + this.currentMatchState.getThirdQuarterTDB()
                                    + this.currentMatchState.getFourthQuarterTDA()
                                    + this.currentMatchState.getFourthQuarterTDB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else {
                    throw new IllegalArgumentException("Should not get here");
                }

            case "P:A:OU": // HALVES goals total A
                isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                if (isHalfMarket) {
                    halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentHalfSeqNo <= halfSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForHalf(0));
                    if (previousHalfSeqNo == halfSeqNo) {
                        int n = this.currentMatchState.getPreviousHalfPointsA();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                } else {
                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo =
                                    convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousPeriodSeqNo =
                                    convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousPeriodSeqNo == periodSeqNo) {
                        int n = this.currentMatchState.getPreviousPeriodPointsA();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                }

            case "P:B:OU": // HALVES goals total A
                isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                if (isHalfMarket) {
                    halfSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    currentHalfSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentHalfSeqNo <= halfSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    previousHalfSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForHalf(0));
                    if (previousHalfSeqNo == halfSeqNo) {
                        int n = this.currentMatchState.getPreviousHalfPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                } else {
                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo =
                                    convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this half is still progressing
                         */
                        return null;
                    }

                    int previousPeriodSeqNo =
                                    convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                    if (previousPeriodSeqNo == periodSeqNo) {
                        int n = this.currentMatchState.getPreviousPeriodPointsB();
                        winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                        return new CheckMarketResultedOutcome(winningSelection, n);
                    } else {
                        throw new IllegalArgumentException("Should not get here");
                    }
                }
            case "FTOT:WM": // highest score quater team a
                if (this.currentMatchState.isMatchCompleted()) {
                    int pointsA = this.currentMatchState.getPointsA();
                    int pointsB = this.currentMatchState.getPointsB();
                    int i = pointsA - pointsB;
                    // int winingMargingIndicator = (int)((((i <= 0.0F) ? 0.0F - (i) : i)-1)/6);
                    // if(winingMargingIndicator>4)
                    // winingMargingIndicator=4;

                    if (i >= 1 && i <= 6) {
                        winningSelection = "A 1-6";
                    } else if (i >= 7 && i <= 12) {
                        winningSelection = "A 7-12";
                    } else if (i >= 13 && i <= 18) {
                        winningSelection = "A 13-18";
                    } else if (i >= 19 && i <= 24) {
                        winningSelection = "A 19-24";
                    } else if (i >= 25) {
                        winningSelection = "A 25+";
                    } else if (i >= -6 && i <= -1) {
                        winningSelection = "B 1-6";
                    } else if (i >= -12 && i <= -7) {
                        winningSelection = "B 7-12";
                    } else if (i >= -18 && i <= -13) {
                        winningSelection = "B 13-18";
                    } else if (i >= -24 && i <= -19) {
                        winningSelection = "B 19-24";
                    } else {
                        winningSelection = "B 25+";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:A:HQ": // highest score quater team a
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int q1PointsA = this.currentMatchState.getFirstQuarterPointsA();
                    int q2PointsA = this.currentMatchState.getSecondQuarterPointsA();
                    int q3PointsA = this.currentMatchState.getThirdQuarterPointsA();
                    int q4PointsA = this.currentMatchState.getFourthQuarterPointsA();

                    if (q1PointsA > q2PointsA && q1PointsA > q3PointsA && q1PointsA > q4PointsA) {
                        winningSelection = "Q1";
                    } else if (q2PointsA > q1PointsA && q2PointsA > q3PointsA && q2PointsA > q4PointsA) {
                        winningSelection = "Q2";
                    } else if (q3PointsA > q1PointsA && q3PointsA > q2PointsA && q3PointsA > q4PointsA) {
                        winningSelection = "Q3";
                    } else if (q4PointsA > q1PointsA && q4PointsA > q2PointsA && q4PointsA > q3PointsA) {
                        winningSelection = "Q4";
                    } else {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:B:HQ": // highest score quater team a
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int q1PointsA = this.currentMatchState.getFirstQuarterPointsB();
                    int q2PointsA = this.currentMatchState.getSecondQuarterPointsB();
                    int q3PointsA = this.currentMatchState.getThirdQuarterPointsB();
                    int q4PointsA = this.currentMatchState.getFourthQuarterPointsB();

                    if (q1PointsA > q2PointsA && q1PointsA > q3PointsA && q1PointsA > q4PointsA) {
                        winningSelection = "Q1";
                    } else if (q2PointsA > q1PointsA && q2PointsA > q3PointsA && q2PointsA > q4PointsA) {
                        winningSelection = "Q2";
                    } else if (q3PointsA > q1PointsA && q3PointsA > q2PointsA && q3PointsA > q4PointsA) {
                        winningSelection = "Q3";
                    } else if (q4PointsA > q1PointsA && q4PointsA > q2PointsA && q4PointsA > q3PointsA) {
                        winningSelection = "Q4";
                    } else {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:A:HH": // winner in current period
                if (!this.currentMatchState.isNormalTimeMatchCompleted()) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }


                int a = 0;
                int b = 0;
                a = this.currentMatchState.getFirstQuarterPointsA() + this.currentMatchState.getSecondQuarterPointsA();
                b = this.currentMatchState.getThirdQuarterPointsA() + this.currentMatchState.getFourthQuarterPointsA();
                if (a > b) {
                    winningSelection = "H1";
                } else if (a < b) {
                    winningSelection = "H2";
                } else {
                    winningSelection = "Draw";
                }

                return new CheckMarketResultedOutcome(winningSelection);


            case "FT:B:HH": // winner in current period
                if (!this.currentMatchState.isNormalTimeMatchCompleted()) {
                    /*
                     * this half is still progressing
                     */
                    return null;
                }


                a = 0;
                b = 0;

                a = this.currentMatchState.getFirstQuarterPointsB() + this.currentMatchState.getSecondQuarterPointsB();
                b = this.currentMatchState.getThirdQuarterPointsB() + this.currentMatchState.getFourthQuarterPointsB();

                if (a > b) {
                    winningSelection = "H1";
                } else if (a < b) {
                    winningSelection = "H2";
                } else {
                    winningSelection = "Draw";
                }

                return new CheckMarketResultedOutcome(winningSelection);


            case "FT:HQ": // highest score quater two teams
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int q1Points = this.currentMatchState.getFirstQuarterPointsA()
                                    + this.currentMatchState.getFirstQuarterPointsB();
                    int q2Points = this.currentMatchState.getSecondQuarterPointsA()
                                    + this.currentMatchState.getSecondQuarterPointsB();
                    int q3Points = this.currentMatchState.getThirdQuarterPointsA()
                                    + this.currentMatchState.getThirdQuarterPointsB();
                    int q4Points = this.currentMatchState.getFourthQuarterPointsA()
                                    + this.currentMatchState.getFourthQuarterPointsB();

                    if (q1Points > q2Points && q1Points > q3Points && q1Points > q4Points) {
                        winningSelection = "Q1";
                    } else if (q2Points > q1Points && q2Points > q3Points && q2Points > q4Points) {
                        winningSelection = "Q2";
                    } else if (q3Points > q1Points && q3Points > q2Points && q3Points > q4Points) {
                        winningSelection = "Q3";
                    } else if (q4Points > q1Points && q4Points > q2Points && q4Points > q3Points) {
                        winningSelection = "Q4";
                    } else {
                        winningSelection = "Draw";
                    }

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "FT:HF": // full time half time resulting
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int firstHalfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                    + this.currentMatchState.getSecondQuarterPointsA();
                    int firstHalfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                    + this.currentMatchState.getSecondQuarterPointsB();


                    if (this.currentMatchState.getNormalTimePointsA() > this.currentMatchState.getNormalTimePointsB()
                                    && firstHalfPointsA > firstHalfPointsB)
                        winningSelection = "H/H";
                    else if (this.currentMatchState.getNormalTimePointsA() > this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA == firstHalfPointsB)
                        winningSelection = "D/H";
                    else if (this.currentMatchState.getNormalTimePointsA() > this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA < firstHalfPointsB)
                        winningSelection = "A/H";
                    else if (this.currentMatchState.getNormalTimePointsA() == this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA > firstHalfPointsB)
                        winningSelection = "H/D";
                    else if (this.currentMatchState.getNormalTimePointsA() == this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA == firstHalfPointsB)
                        winningSelection = "D/D";
                    else if (this.currentMatchState.getNormalTimePointsA() == this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA < firstHalfPointsB)
                        winningSelection = "A/D";
                    else if (this.currentMatchState.getNormalTimePointsA() < this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA > firstHalfPointsB)
                        winningSelection = "H/A";
                    else if (this.currentMatchState.getNormalTimePointsA() < this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA == firstHalfPointsB)
                        winningSelection = "D/A";
                    else if (this.currentMatchState.getNormalTimePointsA() < this.currentMatchState
                                    .getNormalTimePointsB() && firstHalfPointsA < firstHalfPointsB)
                        winningSelection = "A/A";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;

            case "P:CS": // correct score
                int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this PERIOD is still progressing
                     */
                    return null;
                }
                int previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodPointsA(),
                                    this.currentMatchState.getCurrentPeriodPointsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("period correct score not resulting correctly");
                }


                /**
                 * Markets merged to align period markets
                 */
                // case "P:SPRD": // match handicap
                // periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                // currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                // if (currentPeriodSeqNo <= periodSeqNo) {
                // /*
                // * this half is still progressing
                // */
                // return null;
                // }
                //
                // previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                // if (previousPeriodSeqNo == periodSeqNo) {
                // int n = this.currentMatchState.getPreviousPeriodPointsA()
                // - this.currentMatchState.getPreviousPeriodPointsB();
                // winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(n, market.getLineId());
                // return new CheckMarketResultedOutcome(winningSelection, n);
                // } else {
                // throw new IllegalArgumentException("Should not get here");
                // }

                // case "P:OU": // total goals in current half
                // periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                // currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                // if (currentPeriodSeqNo <= periodSeqNo) {
                // /*
                // * this half is still progressing
                // */
                // return null;
                // }
                //
                // previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                // if (previousPeriodSeqNo == periodSeqNo) {
                // int n = this.currentMatchState.getPreviousPeriodPointsA()
                // + this.currentMatchState.getPreviousPeriodPointsB();
                // winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                // return new CheckMarketResultedOutcome(winningSelection, n);
                // } else {
                // throw new IllegalArgumentException("Should not get here");
                // }

                // case "P:A:OU": // total goals in current period A
                // periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                // currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                // if (currentPeriodSeqNo <= periodSeqNo) {
                // /*
                // * this half is still progressing
                // */
                // return null;
                // }
                //
                // previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                // if (previousPeriodSeqNo == periodSeqNo) {
                // int n = this.currentMatchState.getPreviousPeriodPointsA();
                // winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                // return new CheckMarketResultedOutcome(winningSelection, n);
                // } else {
                // throw new IllegalArgumentException("Should not get here");
                // }

                // case "P:B:OU": // total goals in current period B
                // periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                // currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                // if (currentPeriodSeqNo <= periodSeqNo) {
                // /*
                // * this half is still progressing
                // */
                // return null;
                // }
                //
                // previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                // if (previousPeriodSeqNo == periodSeqNo) {
                // int n = this.currentMatchState.getPreviousPeriodPointsB();
                // winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                // return new CheckMarketResultedOutcome(winningSelection, n);
                // } else {
                // throw new IllegalArgumentException("Should not get here");
                // }

            case "P:BTTS": // BOTH TEAM TO SCORE PERIOR
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                boolean periodScoreMarketResulted = !(this.currentMatchState.getCurrentPeriodPointsA() == 0
                                || this.currentMatchState.getCurrentPeriodPointsB() == 0);

                if (currentPeriodSeqNo <= periodSeqNo && !periodScoreMarketResulted) {
                    if (this.currentMatchState.isPeriodCompleted()) {
                        return new CheckMarketResultedOutcome("No both team scored this period");
                    } else
                        return null;
                }

                previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));

                if (currentPeriodSeqNo > periodSeqNo || periodScoreMarketResulted) {
                    if (periodScoreMarketResulted)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Period both team scored resulting error");
                }

            case "OT:NG": // OVERTIME NEXT GOAL
                marketSeqNo = convertGoalSeqIdToIndex(market.getSequenceId());
                currentStateSeqNo = convertGoalSeqIdToIndex(this.currentMatchState.getSequenceIdForGoal(0));
                if (currentStateSeqNo <= marketSeqNo) {
                    /*
                     * next goal has not yet been scored. see if match is over
                     */
                    if (this.currentMatchState.isMatchCompleted()) {
                        return new CheckMarketResultedOutcome("No goal");
                    } else
                        return null;
                }

                previousStateSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForGoal(0));
                if (previousStateSeqNo == marketSeqNo) {
                    if (this.currentMatchState.getTeamScoringLastPoint() == TeamId.A)
                        winningSelection = "A";
                    else
                        winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    throw new IllegalArgumentException("Only one goal should be scored in each matchState change");
                }

            default:
                throw new IllegalArgumentException(
                                "Market type missing from AmericanfootballMatchResultMarkets: " + market.getType());

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
