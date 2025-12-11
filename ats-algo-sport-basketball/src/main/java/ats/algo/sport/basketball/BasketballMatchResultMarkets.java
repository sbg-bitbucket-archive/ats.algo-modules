package ats.algo.sport.basketball;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class BasketballMatchResultMarkets extends MatchResultMarkets {

    BasketballMatchState previousMatchState;
    BasketballMatchState currentMatchState;

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (BasketballMatchState) previousMatchState;
        this.currentMatchState = (BasketballMatchState) currentMatchState;
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


            case "FT:RT20": // Match winner
                matchWinner = this.currentMatchState.getNormalTimeMatchWinner();
                if (this.currentMatchState.getRaceTo().get(2) == TeamId.UNKNOWN && matchWinner == null) {
                    return null;
                } else {
                    TeamId race2winner = this.currentMatchState.getRaceTo().get(2);
                    if (race2winner == TeamId.A)
                        return new CheckMarketResultedOutcome("A");
                    else if (race2winner == TeamId.B)
                        return new CheckMarketResultedOutcome("B");
                    else
                        return new CheckMarketResultedOutcome();

                }


            case "P:AXB": // Half Time Match winner
                boolean twoHalvesFormat = this.currentMatchState.isTwoHalvesFormat();
                boolean isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                //
                if (isHalfMarket) {

                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this period is still progressing
                         */
                        return null;
                    }
                    int halfPointsA = 0;
                    int halfPointsB = 0;
                    if (twoHalvesFormat) {
                        if (periodSeqNo < 2) {
                            halfPointsA = this.currentMatchState.getFirstHalfPointsA();
                            halfPointsB = this.currentMatchState.getFirstHalfPointsB();
                        } else if (periodSeqNo < 3) {
                            halfPointsA = this.currentMatchState.getSecondHalfPointsA();
                            halfPointsB = this.currentMatchState.getSecondHalfPointsB();
                        }
                        if (halfPointsA > halfPointsB)
                            winningSelection = "A";
                        else if (halfPointsA < halfPointsB)
                            winningSelection = "B";
                        else
                            winningSelection = "Draw";
                        return new CheckMarketResultedOutcome(winningSelection);
                    } else {
                        if (periodSeqNo < 2) {
                            halfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                            + this.currentMatchState.getSecondQuarterPointsA();
                            halfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                            + this.currentMatchState.getSecondQuarterPointsB();
                        } else if (periodSeqNo < 3) {
                            halfPointsA = this.currentMatchState.getThirdQuarterPointsA()
                                            + this.currentMatchState.getFourthQuarterPointsA();
                            halfPointsB = this.currentMatchState.getThirdQuarterPointsB()
                                            + this.currentMatchState.getFourthQuarterPointsB();
                        }
                        if (halfPointsA > halfPointsB)
                            winningSelection = "A";
                        else if (halfPointsA < halfPointsB)
                            winningSelection = "B";
                        else
                            winningSelection = "Draw";
                        return new CheckMarketResultedOutcome(winningSelection);
                    }
                } else {
                    // quarter market resulting
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
                    int a = 0;
                    int b = 0;
                    if (previousPeriodSeqNo == 1) {
                        a = this.currentMatchState.getFirstQuarterPointsA();
                        b = this.currentMatchState.getFirstQuarterPointsB();
                    } else if (previousPeriodSeqNo == 2) {
                        a = this.currentMatchState.getSecondQuarterPointsA();
                        b = this.currentMatchState.getSecondQuarterPointsB();
                    } else if (previousPeriodSeqNo == 3) {
                        a = this.currentMatchState.getThirdQuarterPointsA();
                        b = this.currentMatchState.getThirdQuarterPointsB();
                    } else if (previousPeriodSeqNo == 4) {
                        a = this.currentMatchState.getFourthQuarterPointsA();
                        b = this.currentMatchState.getFourthQuarterPointsB();
                    }

                    if (a > b) {
                        winningSelection = "A";
                    } else if (a < b) {
                        winningSelection = "B";
                    } else {
                        winningSelection = "Draw";
                    }


                    return new CheckMarketResultedOutcome(winningSelection);
                }



            case "P:OU": // Half Time Match winner
                twoHalvesFormat = this.currentMatchState.isTwoHalvesFormat();
                isHalfMarket = market.getSequenceId().substring(0, 1).equals("H");
                if (isHalfMarket)

                {
                    int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                    int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                    if (currentPeriodSeqNo <= periodSeqNo) {
                        /*
                         * this period is still progressing
                         */
                        return null;
                    }
                    int halfPointsA = 0;
                    int halfPointsB = 0;
                    if (twoHalvesFormat) {
                        if (periodSeqNo < 2) {
                            halfPointsA = this.currentMatchState.getFirstHalfPointsA();
                            halfPointsB = this.currentMatchState.getFirstHalfPointsB();
                        } else if (periodSeqNo < 3) {
                            halfPointsA = this.currentMatchState.getSecondHalfPointsA();
                            halfPointsB = this.currentMatchState.getSecondHalfPointsB();
                        }
                    } else {
                        if (periodSeqNo < 2) {
                            halfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                            + this.currentMatchState.getSecondQuarterPointsA();
                            halfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                            + this.currentMatchState.getSecondQuarterPointsB();
                        } else if (periodSeqNo < 3) {
                            halfPointsA = this.currentMatchState.getThirdQuarterPointsA()
                                            + this.currentMatchState.getFourthQuarterPointsA();
                            halfPointsB = this.currentMatchState.getThirdQuarterPointsB()
                                            + this.currentMatchState.getFourthQuarterPointsB();
                        }
                    }

                    int nn = halfPointsA + halfPointsB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(nn, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, nn);
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
                    int a = 0;
                    int b = 0;
                    if (previousPeriodSeqNo == 1) {
                        a = this.currentMatchState.getFirstQuarterPointsA();
                        b = this.currentMatchState.getFirstQuarterPointsB();
                    } else if (previousPeriodSeqNo == 2) {
                        a = this.currentMatchState.getSecondQuarterPointsA();
                        b = this.currentMatchState.getSecondQuarterPointsB();
                    } else if (previousPeriodSeqNo == 3) {
                        a = this.currentMatchState.getThirdQuarterPointsA();
                        b = this.currentMatchState.getThirdQuarterPointsB();
                    } else if (previousPeriodSeqNo == 4) {
                        a = this.currentMatchState.getFourthQuarterPointsA();
                        b = this.currentMatchState.getFourthQuarterPointsB();
                    }
                    int n = a + b;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                }



            case "P:A:OU":
                twoHalvesFormat = this.currentMatchState.isTwoHalvesFormat();
                int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                int currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this period is still progressing
                     */
                    return null;
                }
                int halfPointsA = 0;
                if (twoHalvesFormat) {
                    if (periodSeqNo < 2) {
                        halfPointsA = this.currentMatchState.getFirstHalfPointsA();
                    } else if (periodSeqNo < 3) {
                        halfPointsA = this.currentMatchState.getSecondHalfPointsA();
                    }
                } else {
                    if (periodSeqNo < 2) {
                        halfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                        + this.currentMatchState.getSecondQuarterPointsA();
                    } else if (periodSeqNo < 3) {
                        halfPointsA = this.currentMatchState.getThirdQuarterPointsA()
                                        + this.currentMatchState.getFourthQuarterPointsA();
                    }
                }

                int nn = halfPointsA;
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(nn, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, nn);
            case "P:B:OU":
                twoHalvesFormat = this.currentMatchState.isTwoHalvesFormat();
                periodSeqNo =

                                convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this period is still progressing
                     */
                    return null;
                }
                int halfPointsB = 0;
                if (twoHalvesFormat) {
                    if (periodSeqNo < 2) {
                        halfPointsB = this.currentMatchState.getFirstHalfPointsB();
                    } else if (periodSeqNo < 3) {
                        halfPointsB = this.currentMatchState.getSecondHalfPointsB();
                    }
                } else {
                    if (periodSeqNo < 2) {
                        halfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                        + this.currentMatchState.getSecondQuarterPointsB();
                    } else if (periodSeqNo < 3) {
                        halfPointsB = this.currentMatchState.getThirdQuarterPointsB()
                                        + this.currentMatchState.getFourthQuarterPointsB();
                    }
                }

                nn = halfPointsB;
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(nn, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, nn);
            case "P:SPRD":
                twoHalvesFormat = this.currentMatchState.isTwoHalvesFormat();
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForHalf(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this period is still progressing
                     */
                    return null;
                }
                halfPointsA = 0;
                halfPointsB = 0;
                if (twoHalvesFormat) {
                    if (periodSeqNo < 2) {
                        halfPointsA = this.currentMatchState.getFirstHalfPointsA();
                        halfPointsB = this.currentMatchState.getFirstHalfPointsB();
                    } else if (periodSeqNo < 3) {
                        halfPointsA = this.currentMatchState.getSecondHalfPointsA();
                        halfPointsB = this.currentMatchState.getSecondHalfPointsB();
                    }
                } else {
                    if (periodSeqNo < 2) {
                        halfPointsA = this.currentMatchState.getFirstQuarterPointsA()
                                        + this.currentMatchState.getSecondQuarterPointsA();
                        halfPointsB = this.currentMatchState.getFirstQuarterPointsB()
                                        + this.currentMatchState.getSecondQuarterPointsB();
                    } else if (periodSeqNo < 3) {
                        halfPointsA = this.currentMatchState.getThirdQuarterPointsA()
                                        + this.currentMatchState.getFourthQuarterPointsA();
                        halfPointsB = this.currentMatchState.getThirdQuarterPointsB()
                                        + this.currentMatchState.getFourthQuarterPointsB();
                    }
                }

                nn = halfPointsA - halfPointsB;
                winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(nn, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, nn);

            case "FTOT:ML": // Match winner
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
                            return new CheckMarketResultedOutcome();
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


            case "FTOT:OU": // goals total
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

            case "FT:EH": // match handicap EURO
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() - this.currentMatchState.getPointsB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionEuroHcapMarket(n, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, n);
                } else
                    return null;


            case "FT:OE":
                if (this.currentMatchState.isNormalTimeMatchCompleted()) {
                    int n = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();
                    if (n % 2 != 0) {
                        return new CheckMarketResultedOutcome("Odd");
                    } else {
                        return new CheckMarketResultedOutcome("Even");
                    }
                } else
                    return null;

            case "P:CS": // correct score
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                if (currentPeriodSeqNo <= periodSeqNo) {
                    /*
                     * this PERIOD is still progressing
                     */
                    return null;
                }
                int previousPeriodSeqNo = convertGoalSeqIdToIndex(this.previousMatchState.getSequenceIdForPeriod(0));
                if (previousPeriodSeqNo == periodSeqNo) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getCurrentPeriodGoalsA(),
                                    this.currentMatchState.getCurrentPeriodGoalsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else {
                    return new CheckMarketResultedOutcome();
                }



            case "P:BTTS": // BOTH TEAM TO SCORE PERIOR
                periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
                currentPeriodSeqNo = convertPeriodSeqIdToIndex(this.currentMatchState.getSequenceIdForPeriod(0));
                boolean periodScoreMarketResulted = !(this.currentMatchState.getCurrentPeriodGoalsA() == 0
                                || this.currentMatchState.getCurrentPeriodGoalsB() == 0);

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
                    return new CheckMarketResultedOutcome();
                }


            default:
                throw new IllegalArgumentException(
                                "Market type missing from BasketballMatchResultMarkets: " + market.getType());

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
