package ats.algo.sport.badminton;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult.BadmintonMatchIncidentResultType;

public class BadmintonResultMarkets extends MatchResultMarkets {
    BadmintonMatchState previousMatchState;
    BadmintonMatchState currentMatchState;
    int marketSetNo;
    int marketPointNo;
    boolean matchAbandoned = false;
    private String[] gameCorrectScoreSelections = {"21-13 or better", "21-14", "21-15", "21-16", "21-17", "21-18",
            "21-19", "13-21 or better", "14-21", "15-21", "16-21", "17-21", "18-21", "19-21", "after extra points A",
            "after extra points B"};
    private String[] winningMarginSelections = {"A 1-2", "A 3-5", "A 6-8", "A 9+", "B 1-2", "B 3-5", "B 6-8", "B 9+"};

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (BadmintonMatchState) previousMatchState;
        this.currentMatchState = (BadmintonMatchState) currentMatchState;
        parseSequenceId(market.getSequenceId());
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;

        matchAbandoned = (this.currentMatchState.getMostRecentMatchIncidentResult()
                        .getBadmintonMatchIncidentResultType() == BadmintonMatchIncidentResultType.MATCHABANDONED);
        TeamId teamWonAbandon = this.currentMatchState.abandonedMatchWinner();
        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (matchAbandoned) {
                    switch (teamWonAbandon) {
                        case A:
                            return new CheckMarketResultedOutcome("A");
                        case B:
                            return new CheckMarketResultedOutcome("B");
                        case UNKNOWN:
                            return new CheckMarketResultedOutcome();
                    }
                }

                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGamesA() > this.currentMatchState.getGamesB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:PSPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getGamesA()
                                    + this.currentMatchState.getGamesB()); i++) {

                        pointsA1 = this.currentMatchState.getGameScoreInGameN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInGameN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;

                    }

                    int pointtDiff = sumA - sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointtDiff,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtDiff);
                } else
                    return null;

            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {

                    int pointtDiff = this.currentMatchState.getGamesA() - this.currentMatchState.getGamesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionHcapMarket(pointtDiff,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtDiff);
                } else
                    return null;
            case "FT:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getGamesA()
                                    + this.currentMatchState.getGamesB()); i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInGameN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInGameN(i).B;
                        sumA += pointsA1;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumA + sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;
            case "FT:CS":
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", this.currentMatchState.getGamesA(),
                                    this.currentMatchState.getGamesB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:OE":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getGamesA()
                                    + this.currentMatchState.getGamesB()); i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInGameN(i).A;
                        pointsB1 = this.currentMatchState.getGameScoreInGameN(i).B;
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
            case "FT:A:OU":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getGamesA()
                                    + this.currentMatchState.getGamesB()); i++) {
                        pointsA1 = this.currentMatchState.getGameScoreInGameN(i).A;
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
                    for (int i = 0; i < (this.currentMatchState.getGamesA()
                                    + this.currentMatchState.getGamesB()); i++) {
                        pointsB1 = this.currentMatchState.getGameScoreInGameN(i).B;
                        sumB += pointsB1;
                    }

                    int pointtTotal = sumB;
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:OU":
                return checkSetMktTotalResulted(market);
            case "P:CS":
                return checkSetMktCSResulted();
            case "P:WM":
                return checkSetMktWMResulted();
            case "P:A:OU":
                return checkSetMktTotalAResulted(market);
            case "P:B:OU":
                return checkSetMktTotalBResulted(market);
            case "P:SPRD":
                return checkSetMktHandcaipResulted(market);
            case "P:PW":
                return checkSetPointMktResulted();
            case "P:RACE":
                return checkMktRaceToFivePoint();
            case "P:LEAD":
                return checkMktLeadAfterTenPoint();
            case "P:ET":
                return checkSetMktExtraPoint();
            case "P:OE":
                return checkSetMktOEResulted();
            default:
                return null;
        }

    }

    private CheckMarketResultedOutcome checkMktLeadAfterTenPoint() {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB() + 1;
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
            currentPointNoA = this.currentMatchState.getGameScoreInGameN(currentSetNo - 2).A;
            currentPointNoB = this.currentMatchState.getGameScoreInGameN(currentSetNo - 2).B;
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
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkMktRaceToFivePoint() {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB() + 1;
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
            currentPointNoA = this.currentMatchState.getGameScoreInGameN(currentSetNo - 2).A;
            currentPointNoB = this.currentMatchState.getGameScoreInGameN(currentSetNo - 2).B;
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
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktExtraPoint() {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA > 25 || pointsB > 25) {
                winningSelection = "Yes";
            } else {
                winningSelection = "No";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetPointMktResulted() {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
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
            if (this.currentMatchState.getGameScoreInGameN(currentSetNo - 1).A > this.currentMatchState
                            .getGameScoreInGameN(currentSetNo - 1).B) {
                winningSelection = "A";
            } else
                winningSelection = "B";
            if (marketPointNo == this.currentMatchState.getGameScoreInGameN(currentSetNo - 1).A
                            + this.currentMatchState.getGameScoreInGameN(currentSetNo - 1).B)
                return new CheckMarketResultedOutcome(winningSelection);
            else {
                return new CheckMarketResultedOutcome();
            }
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else if (matchAbandoned) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktHandcaipResulted(Market market) {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
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

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsA + pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktCSResulted() {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA <= 13)
                winningSelection = gameCorrectScoreSelections[7];
            else if (pointsB <= 13)
                winningSelection = gameCorrectScoreSelections[0];
            else if (pointsA > 21 && pointsA > pointsB)
                winningSelection = gameCorrectScoreSelections[gameCorrectScoreSelections.length - 2];
            else if (pointsB > 21 && pointsB > pointsA)
                winningSelection = gameCorrectScoreSelections[gameCorrectScoreSelections.length - 1];
            else
                winningSelection = String.format("%d-%d", pointsA, pointsB);
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktWMResulted() {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
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

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
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

    private CheckMarketResultedOutcome checkSetMktTotalAResulted(Market market) {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            String winningSelection;
            int pointtTotal = pointsA;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktTotalBResulted(Market market) {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            if (pointsA > pointsB) {
                winningSelection = "A";
            } else {
                winningSelection = "B";
            }
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else if (matchAbandoned) {
            return new CheckMarketResultedOutcome();
        } else
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

    /**
     * returns true of n odd
     * 
     * @param n
     * @return
     */
    boolean isOdd(int n) {
        return 2 * (n / 2) != n;
    }

}
