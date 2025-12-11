package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class VolleyballMatchResultMarkets extends MatchResultMarkets {
    VolleyballMatchState previousMatchState;
    VolleyballMatchState currentMatchState;
    int marketSetNo;
    int marketPointNo;
    boolean matchCompleted;
    private String[] winningMarginSelections = {"A 1-2", "A 3-5", "A 6-8", "A 9+", "B 1-2", "B 3-5", "B 6-8", "B 9+"};
    private String formatCorrectScore = "%d-%d";
    private String formatCorrectScoreBetter = "%d-%d or better";

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (VolleyballMatchState) previousMatchState;
        this.currentMatchState = (VolleyballMatchState) currentMatchState;
        matchCompleted = this.currentMatchState.isMatchCompleted();
        parseSequenceId(market.getSequenceId());
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;

        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (matchCompleted) {
                    if (this.currentMatchState.getSetsA() > this.currentMatchState.getSetsB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:SPRD":
                if (matchCompleted) {
                    int setDiff = this.currentMatchState.getSetsA() - this.currentMatchState.getSetsB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(setDiff, market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, setDiff);
                } else
                    return null;
            case "FT:PSPRD":
                if (matchCompleted) {
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
                if (matchCompleted) {
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
                if (matchCompleted) {
                    for (int i = 0; i < (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()); i++) {
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
                if (matchCompleted) {
                    for (int i = 0; i < (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()); i++) {
                        pointsB1 = this.currentMatchState.getGameScoreInSetN(i).B;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;

            case "FT:OUS":
                if (matchCompleted) {
                    int setsTotal = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionOvunMarket(setsTotal, market.getLineId());
                    return new CheckMarketResultedOutcome(winningSelection, setsTotal);
                } else
                    return null;
            case "FT:OE":
                if (matchCompleted) {
                    for (int i = 0; i < (this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB()); i++) {
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
            case "FT:A:WAL1":
                if (matchCompleted) {
                    if (this.currentMatchState.getSetsA() >= 1)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.getSetsA() >= 1) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:B:WAL1":
                if (matchCompleted) {
                    if (this.currentMatchState.getSetsB() >= 1)
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else if (this.currentMatchState.getSetsB() >= 1) {
                    winningSelection = "Yes";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:A:WFSAM":
                if (matchCompleted) {
                    pointsA1 = this.currentMatchState.getGameScoreInSetN(0).A;
                    pointsB1 = this.currentMatchState.getGameScoreInSetN(0).B;
                    if (pointsA1 > pointsB1 && this.currentMatchState.getSetsA() > this.currentMatchState.getSetsB())
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:B:WFSAM":
                if (matchCompleted) {
                    pointsA1 = this.currentMatchState.getGameScoreInSetN(0).A;
                    pointsB1 = this.currentMatchState.getGameScoreInSetN(0).B;
                    if (pointsB1 > pointsA1 && this.currentMatchState.getSetsB() > this.currentMatchState.getSetsA())
                        winningSelection = "Yes";
                    else
                        winningSelection = "No";
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:CS":
                return checkSetMktCorrectScoreResulted();
            case "P:CS2":
                return checkSetTwoMktResulted();
            case "P:CS3":
                return checkSetThreeMktResulted();
            case "P:CS4":
                return checkSetFourMktResulted();
            case "P:OU":
                return checkSetMktTotalResulted2(market);
            case "P:SPRD":
                return checkSetMktHandcaipResulted(market);
            case "P:PW":
                return checkSetPointMktResulted();
            case "FT:CS":
                if (matchCompleted) {
                    if (((VolleyballMatchState) currentMatchState)
                                    .getSetsA() > ((VolleyballMatchState) currentMatchState).getSetsB()) {
                        winningSelection = "A " + String.format(formatCorrectScore,
                                        ((VolleyballMatchState) currentMatchState).getSetsA(),
                                        ((VolleyballMatchState) currentMatchState).getSetsB());
                    } else {
                        winningSelection = "B " + String.format(formatCorrectScore,
                                        ((VolleyballMatchState) currentMatchState).getSetsB(),
                                        ((VolleyballMatchState) currentMatchState).getSetsA());
                    }
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:RACE":
                return checkMktRaceToFivePoint();
            case "P:LEAD":
                return checkMktLeadAfterTenPoint();
            case "P:EP":
                return checkSetMktExtraPoint();
            case "P:OE":
                return checkSetMktOEResulted();
            case "P:WM":
                return checkSetMktWMResulted();
            default:
                throw new IllegalArgumentException(
                                "Market type missing from VolleyballMatchResultMarkets: " + market.getType());
        }

    }

    private CheckMarketResultedOutcome checkSetMktWMResulted() {

        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            int currentSetHandicapPoints = pointsA - pointsB;
            String winningSelection;
            if (currentSetHandicapPoints > 0) {
                if (currentSetHandicapPoints >= 9)
                    winningSelection = winningMarginSelections[3];
                else if (currentSetHandicapPoints >= 6)
                    winningSelection = winningMarginSelections[2];
                else if (currentSetHandicapPoints >= 3)
                    winningSelection = winningMarginSelections[1];
                else
                    winningSelection = winningMarginSelections[0];
            } else {
                if (Math.abs(currentSetHandicapPoints) >= 9)
                    winningSelection = winningMarginSelections[7];
                else if (Math.abs(currentSetHandicapPoints) >= 6)
                    winningSelection = winningMarginSelections[6];
                else if (Math.abs(currentSetHandicapPoints) >= 3)
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

        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
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

    private CheckMarketResultedOutcome checkSetTwoMktResulted() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        int setWinA = 0;
        int setWinB = 0;
        if (currentSetNo == marketSetNo) {
            for (int i = 0; i < marketSetNo; i++) {
                int pointsA = this.currentMatchState.getGameScoreInSetN(i).A;
                int pointsB = this.currentMatchState.getGameScoreInSetN(i).B;

                if (pointsA > pointsB)
                    setWinA++;
                else
                    setWinB++;
            }
            String winningSelection;
            if (setWinA > setWinB)
                winningSelection = "A " + String.format(formatCorrectScore, setWinA, setWinB);
            else
                winningSelection = "B " + String.format(formatCorrectScore, setWinB, setWinA);

            return new CheckMarketResultedOutcome(winningSelection);
        }
        return null;
    }

    private CheckMarketResultedOutcome checkSetThreeMktResulted() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        int setWinA = 0;
        int setWinB = 0;
        if (currentSetNo == marketSetNo) {
            for (int i = 0; i < marketSetNo; i++) {
                int pointsA = this.currentMatchState.getGameScoreInSetN(i).A;
                int pointsB = this.currentMatchState.getGameScoreInSetN(i).B;

                if (pointsA > pointsB)
                    setWinA++;
                else
                    setWinB++;
            }

            String winningSelection;
            if (setWinA > setWinB)
                winningSelection = "A " + String.format(formatCorrectScore, setWinA, setWinB);
            else
                winningSelection = "B " + String.format(formatCorrectScore, setWinB, setWinA);


            return new CheckMarketResultedOutcome(winningSelection);
        }
        return null;
    }

    private CheckMarketResultedOutcome checkSetFourMktResulted() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        int setWinA = 0;
        int setWinB = 0;
        if (currentSetNo == marketSetNo) {
            for (int i = 0; i < marketSetNo; i++) {
                int pointsA = this.currentMatchState.getGameScoreInSetN(i).A;
                int pointsB = this.currentMatchState.getGameScoreInSetN(i).B;

                if (pointsA > pointsB)
                    setWinA++;
                else
                    setWinB++;
            }

            String winningSelection;
            if (setWinA > setWinB)
                winningSelection = "A " + String.format(formatCorrectScore, setWinA, setWinB);
            else if (setWinB > setWinA)
                winningSelection = "B " + String.format(formatCorrectScore, setWinB, setWinA);
            else
                winningSelection = String.format(formatCorrectScore, setWinB, setWinA);


            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (matchCompleted) {
            return new CheckMarketResultedOutcome();
        }

        return null;
    }

    private CheckMarketResultedOutcome checkSetPointMktResulted() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        int currentPointNo = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();

        if ((currentSetNo == marketSetNo - 1) && (currentPointNo == marketPointNo)) {

            String winningSelection;
            if (this.currentMatchState.getServe() == TeamId.A) {
                winningSelection = "A";
            } else
                winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentSetNo == marketSetNo && currentPointNo == 0) {
            String winningSelection;
            if (this.currentMatchState.getGameScoreInSetN(currentSetNo - 1).A > this.currentMatchState
                            .getGameScoreInSetN(currentSetNo - 1).B) {
                winningSelection = "A";
            } else
                winningSelection = "B";
            if (marketPointNo == this.currentMatchState.getGameScoreInSetN(currentSetNo - 1).A
                            + this.currentMatchState.getGameScoreInSetN(currentSetNo - 1).B)
                return new CheckMarketResultedOutcome(winningSelection);
            else {
                return new CheckMarketResultedOutcome();
            }
        }

        return null;
    }

    private CheckMarketResultedOutcome checkSetMktHandcaipResulted(Market market) {

        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointDiff = pointsA - pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointDiff, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointDiff);
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktTotalResulted2(Market market) {
        int periodSeqNo = convertPeriodSeqIdToIndex(market.getSequenceId());
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo + 1 > periodSeqNo) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsA + pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else
            return null;
    }

    /**
     * Converts string of form "Pnn" to integer
     * 
     * @param periodSequenceId
     * @return
     */
    int convertPeriodSeqIdToIndex(String sequenceId) {
        int index;
        String s = sequenceId.substring(1, sequenceId.length());
        index = Integer.parseInt(s);
        return index;
    }

    private CheckMarketResultedOutcome checkSetMktCorrectScoreResulted() {

        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        int setNo = ((VolleyballMatchFormat) this.currentMatchState.getMatchFormat()).getnSetsInMatch();

        if (currentSetNo > marketSetNo - 1 && setNo != currentSetNo) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection = pointsA > pointsB ? String.format("A %d-%d", pointsA, pointsB)
                            : String.format("B %d-%d", pointsB, pointsA);
            if (pointsA < 16) {
                pointsA = 15;
                winningSelection = "B " + String.format(formatCorrectScoreBetter, pointsB, pointsA);
            }
            if (pointsB < 16) {
                pointsB = 15;
                winningSelection = "A " + String.format(formatCorrectScoreBetter, pointsA, pointsB);
            }
            if (pointsA > 25 && pointsA > pointsB) {
                winningSelection = "after extra points A";
            }
            if (pointsB > 25 && pointsB > pointsA) {
                winningSelection = "after extra points B";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (matchCompleted) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;

            String winningSelection = String.format(formatCorrectScore, pointsA, pointsB);
            if (pointsA < 11) {
                pointsA = 10;
                winningSelection = "B " + String.format(formatCorrectScoreBetter, pointsA, pointsB);
            }
            if (pointsB < 11) {
                pointsB = 10;
                winningSelection = "A " + String.format(formatCorrectScoreBetter, pointsA, pointsB);
            }
            if (pointsA > 15 && pointsA > pointsB) {
                winningSelection = "after extra points A";
            }
            if (pointsB > 15 && pointsB > pointsA) {
                winningSelection = "after extra points B";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA > pointsB) {
                winningSelection = "A";
            } else {
                winningSelection = "B";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        return null;
    }

    private CheckMarketResultedOutcome checkMktLeadAfterTenPoint() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB() + 1;
        int currentPointNo = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();
        int currentPointNoA;
        int currentPointNoB;
        String winningSelection;
        if (currentSetNo == marketSetNo && currentPointNo == marketPointNo) {
            if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                winningSelection = "A";
            } else if (this.currentMatchState.getPointsB() > this.currentMatchState.getPointsA()) {
                winningSelection = "B";
            } else {
                winningSelection = "Draw";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentSetNo == marketSetNo + 1) {
            currentPointNoA = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).A;
            currentPointNoB = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).B;
            currentPointNo = currentPointNoA + currentPointNoB;
            if (currentPointNo == marketPointNo) {
                if (this.currentMatchState.getPointsA() > this.currentMatchState.getPointsB()) {
                    winningSelection = "A";
                } else if (this.currentMatchState.getPointsB() > this.currentMatchState.getPointsA()) {
                    winningSelection = "B";
                } else {
                    winningSelection = "Draw";
                }
                return new CheckMarketResultedOutcome(winningSelection);
            } else {
                return new CheckMarketResultedOutcome();
            }
        }
        return null;
    }

    private CheckMarketResultedOutcome checkMktRaceToFivePoint() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB() + 1;
        int currentPointNoA;
        int currentPointNoB;
        String winningSelection;
        if (currentSetNo == marketSetNo) {
            if (this.currentMatchState.getPointsA() == marketPointNo
                            && this.currentMatchState.getPointsB() < marketPointNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (this.currentMatchState.getPointsA() < marketPointNo
                            && this.currentMatchState.getPointsB() == marketPointNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (currentSetNo == marketSetNo + 1) {
            currentPointNoA = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).A;
            currentPointNoB = this.currentMatchState.getGameScoreInSetN(currentSetNo - 2).B;
            if (currentPointNoA == marketPointNo && currentPointNoB < marketPointNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (currentPointNoA < marketPointNo && currentPointNoB == marketPointNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            return new CheckMarketResultedOutcome();
        }

        return null;
    }

    private CheckMarketResultedOutcome checkSetMktExtraPoint() {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA > 25 || pointsB > 25) {
                winningSelection = "Yes";
            } else {
                winningSelection = "No";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        return null;
    }

    void parseSequenceId(String sequenceId) {
        marketSetNo = 0;
        marketPointNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, 2);
            marketSetNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketPointNo = Integer.parseInt(bits[1]);
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }
}
