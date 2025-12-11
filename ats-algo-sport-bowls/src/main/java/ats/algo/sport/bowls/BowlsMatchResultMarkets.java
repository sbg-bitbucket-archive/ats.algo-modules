package ats.algo.sport.bowls;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;

public class BowlsMatchResultMarkets extends MatchResultMarkets {
    BowlsMatchState previousMatchState;
    BowlsMatchState currentMatchState;
    int marketSetNo;
    int marketEndNo;
    private String[] setCorrectScoreSelections = {"2-0", "2-1", "1.5-0.5", "0.5-1.5", " 1-2", "0-2"};
    private String[] winningMarginSelections = {"A 1-3", "A 4-6", "A 7-9", "A 10+", "B 1-3", "B 4-6", "B 7-9", "B 10+"};

    /**
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (BowlsMatchState) previousMatchState;
        this.currentMatchState = (BowlsMatchState) currentMatchState;
        parseSequenceId(market.getSequenceId());
        int pointsA1 = 0;
        int pointsB1 = 0;
        int sumA = 0;
        int sumB = 0;

        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getSetsA() > this.currentMatchState.getSetsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:OUS":
                if (this.currentMatchState.isMatchCompleted()) {
                    int setTotal = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB());
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(setTotal, market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, setTotal);
                } else
                    return null;
            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()); i++) {

                        pointsA1 = this.currentMatchState.getGameScoreInSetN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInSetN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;

                    }

                    int pointtDiff = sumA - sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointtDiff,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtDiff);
                } else
                    return null;
            case "FT:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()); i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInSetN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInSetN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumA + sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;

            case "FT:A:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < this.currentMatchState.getSetNo() - 1; i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInSetN(i).A;
                        sumA += pointsA1;
                    }

                    int pointtTotal = sumA;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;
            case "FT:B:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < this.currentMatchState.getSetNo() - 1; i++) {
                        pointsB1 = this.currentMatchState.getGameScoreInSetN(i).B;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;

            case "FT:OE":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getSetNo() - 1); i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInSetN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInSetN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumA + sumB;
                    if (isOdd(pointtTotal))
                        winningSelection = "Odd";
                    else
                        winningSelection = "Even";
                    return new CheckMarketResultedOutcome(winningSelection);

                } else
                    return null;
            case "FT:CS":
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = "";
                    double setsA = this.currentMatchState.getSetsA();
                    double setsB = this.currentMatchState.getSetsB();
                    if ((setsA == 2) && (setsB == 0))
                        winningSelection = setCorrectScoreSelections[0];
                    if ((setsA == 2) && (setsB == 1))
                        winningSelection = setCorrectScoreSelections[1];
                    if ((setsA == 1.5) && (setsB == 0.5))
                        winningSelection = setCorrectScoreSelections[2];
                    if ((setsA == 0.5) && (setsB == 1.5))
                        winningSelection = setCorrectScoreSelections[3];
                    if ((setsA == 1) && (setsB == 2))
                        winningSelection = setCorrectScoreSelections[4];
                    if ((setsA == 0) && (setsB == 2))
                        winningSelection = setCorrectScoreSelections[5];
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:OU":
                if (market.getSequenceId().contains("S"))
                    return checkSetMktTotalResulted(market);
                else if (market.getSequenceId().contains("E"))
                    return checkSetEndOUMktResulted(market);
                else
                    return null;
            case "P:SPRD":
                return checkSetMktHandcaipResulted(market);
            case "PE:ML":
                return checkSetEndMktResulted();
            case "P:CS":
                return checkSetEndCSMktResulted();
            case "P:RACE":
                return checkMktRaceToFivePoint();
            case "P:LEAD":
                return checkMktLeadAfterTenPoint();
            case "P:OE":
                return checkSetMktOEResulted();
            case "P:WM":
                return checkSetMktWMResulted(market);
            default:
                throw new IllegalArgumentException(
                                "Market type missing from BowlsMatchMatchResultMarkets: " + market.getType());
        }
    }

    private CheckMarketResultedOutcome checkSetMktWMResulted(Market market) {

        int currentSetNo = this.currentMatchState.getSetNo() - 1;
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            int currentSetHandicapPoints = pointsA - pointsB;
            String winningSelection;
            if (currentSetHandicapPoints > 0) {
                if (currentSetHandicapPoints >= 10)
                    winningSelection = winningMarginSelections[3];
                else if (currentSetHandicapPoints >= 7)
                    winningSelection = winningMarginSelections[2];
                else if (currentSetHandicapPoints >= 4)
                    winningSelection = winningMarginSelections[1];
                else
                    winningSelection = winningMarginSelections[0];
            } else {
                if (Math.abs(currentSetHandicapPoints) >= 10)
                    winningSelection = winningMarginSelections[7];
                else if (Math.abs(currentSetHandicapPoints) >= 7)
                    winningSelection = winningMarginSelections[6];
                else if (Math.abs(currentSetHandicapPoints) >= 4)
                    winningSelection = winningMarginSelections[5];
                else
                    winningSelection = winningMarginSelections[4];
            }
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktOEResulted() {

        int currentSetNo = this.currentMatchState.getSetNo() - 1;
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsA + pointsB;
            if (isOdd(pointtTotal))
                winningSelection = "Odd";
            else
                winningSelection = "Even";
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkMktLeadAfterTenPoint() {
        int currentSetNo = this.currentMatchState.getSetNo();
        int currentEndNo = this.currentMatchState.getCurrentEnd();
        String winningSelection = null;
        if (currentSetNo == marketSetNo) {
            if (currentEndNo > marketEndNo) {
                if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                    winningSelection = "A";
                } else if (this.currentMatchState.getPointsB() > this.currentMatchState.getPointsA()) {
                    winningSelection = "B";
                } else {
                    winningSelection = "Draw";
                }
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkMktRaceToFivePoint() {
        int currentSetNo = this.currentMatchState.getSetNo();
        int currentPointNoA;
        int currentPointNoB;
        String winningSelection;
        if (currentSetNo == marketSetNo) {
            if (this.currentMatchState.getPointsA() >= marketEndNo
                            && this.currentMatchState.getPointsB() < marketEndNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (this.currentMatchState.getPointsA() < marketEndNo
                            && this.currentMatchState.getPointsB() >= marketEndNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (currentSetNo == marketSetNo + 1) {
            currentPointNoA = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).A;
            currentPointNoB = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).B;
            if (currentPointNoA >= marketEndNo && currentPointNoB < marketEndNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (currentPointNoA < marketEndNo && currentPointNoB >= marketEndNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            winningSelection = "Neither";
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentMatchState.isMatchCompleted()) {
            winningSelection = "Neither";
            return new CheckMarketResultedOutcome(winningSelection);
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktHandcaipResulted(Market market) {

        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB());
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointDiff = pointsA - pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointDiff, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointDiff);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktTotalResulted(Market market) {

        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB());
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsA + pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB());
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA > pointsB) {
                winningSelection = "A";
            } else if (pointsA < pointsB) {
                winningSelection = "B";
            } else {
                winningSelection = "Draw";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetEndMktResulted() {
        // int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()) + 1;
        int currentEndNo = this.currentMatchState.getCurrentEnd();
        if (currentSetNo == marketSetNo) {
            if (currentEndNo > marketEndNo) {
                boolean pointsA = this.currentMatchState.getPointsA() > this.previousMatchState.getPointsA();
                boolean pointsB = this.currentMatchState.getPointsB() > this.previousMatchState.getPointsB();
                String winningSelection;
                if (pointsA) {
                    winningSelection = "A";
                    return new CheckMarketResultedOutcome(winningSelection);
                }
                if (pointsB) {
                    winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                }
            }
        }
        if (currentSetNo == marketSetNo + 1) {
            if (currentEndNo == 1) {
                String winningSelection;
                int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
                int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
                boolean pointsA1 = pointsA > this.previousMatchState.getPointsA();
                boolean pointsB1 = pointsB > this.previousMatchState.getPointsB();
                if (pointsA1) {
                    winningSelection = "A";
                    return new CheckMarketResultedOutcome(winningSelection);
                }
                if (pointsB1) {
                    winningSelection = "B";
                    return new CheckMarketResultedOutcome(winningSelection);
                }

            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetEndOUMktResulted(Market market) {
        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()) + 1;
        int currentEndNo = this.currentMatchState.getCurrentEnd();
        String winningSelection;
        if (currentSetNo == marketSetNo) {
            if (currentEndNo > marketEndNo) {
                int pointsA = this.currentMatchState.getPointsA() - this.previousMatchState.getPointsA();
                int pointsB = this.currentMatchState.getPointsB() - this.previousMatchState.getPointsB();
                int setTotal = pointsA + pointsB;
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(setTotal, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, setTotal);
            }
        }

        if (currentSetNo == marketSetNo + 1) {
            if (currentEndNo == 1) {
                int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
                int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
                int setTotal = pointsA + pointsB;
                winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(setTotal, market.getLineId());
                return new CheckMarketResultedOutcome(winningSelection, setTotal);
            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetEndCSMktResulted() {
        int currentSetNo = (int) (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()) + 1;
        int currentEndNo = this.currentMatchState.getCurrentEnd();
        if (currentSetNo == marketSetNo) {
            if (currentEndNo > marketEndNo) {
                int pointsA = this.currentMatchState.getPointsA() - this.previousMatchState.getPointsA();
                int pointsB = this.currentMatchState.getPointsB() - this.previousMatchState.getPointsB();
                String winningSelection = String.format("%d-%d", pointsA, pointsB);
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (currentSetNo == marketSetNo + 1) {
            if (currentEndNo == 1) {
                int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A
                                - this.previousMatchState.getPointsA();
                int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B
                                - this.previousMatchState.getPointsB();
                String winningSelection = String.format("%d-%d", pointsA, pointsB);
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private void parseSequenceId(String sequenceId) {
        marketSetNo = 0;
        marketEndNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, 2);
            marketSetNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketEndNo = Integer.parseInt(bits[1]);
    }

    boolean isOdd(int n) {
        return 2 * (n / 2) != n;
    }

}
