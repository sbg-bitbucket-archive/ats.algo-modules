package ats.algo.sport.squash;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class SquashMatchResultMarkets extends MatchResultMarkets {
    SquashMatchState previousMatchState;
    SquashMatchState currentMatchState;
    int marketSetNo;
    int marketPointNo;
    private String[] winningMarginSelections = {"A 2", "A 3-4", "A 5+", "B 2", "B 3-4", "B 5+"};

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (SquashMatchState) previousMatchState;
        this.currentMatchState = (SquashMatchState) currentMatchState;
        parseSequenceId(market.getSequenceId());
        int pointsA1 = 0;
        int pointsB1 = 0;
        int sumA = 0;
        int sumB = 0;

        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getGamesA() > this.currentMatchState.getGamesB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    int setDiff = this.currentMatchState.getGamesA() - this.currentMatchState.getGamesB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(setDiff, market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, setDiff);
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
            case "P:ML":
                return checkSetMktResulted();
            case "P:OU":
                return checkSetMktTotalResulted(market, TeamId.UNKNOWN);
            case "P:A:OU":
                return checkSetMktTotalResulted(market, TeamId.A);
            case "P:B:OU":
                return checkSetMktTotalResulted(market, TeamId.B);
            case "P:SPSPRD":
                return checkSetMktHandcaipResulted(market);
            case "P:PW":
                return checkSetPointMktResulted(market, 3);
            case "P:WM":
                return checkSetMktWMResulted(market);
            case "P:RACE":
                return checkMktRaceToFivePoint();
            case "P:OE":
                return checkSetMktOEResulted();
            default:
                throw new IllegalArgumentException(
                                "Market type missing from SquashMatchResultMarkets: " + market.getType());
        }

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

    private CheckMarketResultedOutcome checkSetMktWMResulted(Market market) {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            int currentSetHandicapPoints = pointsA - pointsB;
            String winningSelection;
            if (currentSetHandicapPoints > 0) {
                if (currentSetHandicapPoints >= 5)
                    winningSelection = winningMarginSelections[2];
                else if (currentSetHandicapPoints >= 3)
                    winningSelection = winningMarginSelections[1];
                else
                    winningSelection = winningMarginSelections[0];
            } else {
                if (Math.abs(currentSetHandicapPoints) >= 5)
                    winningSelection = winningMarginSelections[5];
                else if (Math.abs(currentSetHandicapPoints) >= 3)
                    winningSelection = winningMarginSelections[4];
                else
                    winningSelection = winningMarginSelections[3];
            }
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetPointMktResulted(Market market, int n) {
        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        int currentPointNo = this.currentMatchState.getPointsA() + this.currentMatchState.getPointsB();

        if ((currentSetNo == marketSetNo - 1) && (currentPointNo == marketPointNo)) {

            String winningSelection;
            if (this.currentMatchState.getServe() == TeamId.A) {
                winningSelection = "A";
            } else
                winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (currentSetNo == marketSetNo)
            if (currentPointNo == 0) {
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
        if (this.currentMatchState.isMatchCompleted())
            return new CheckMarketResultedOutcome();
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
            // System.out.println(marketSetNo+"---"+pointsA+"--"+pointsB);
            return new CheckMarketResultedOutcome(winningSelection, pointDiff);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktTotalResulted(Market market, TeamId teamId) {

        int currentSetNo = this.currentMatchState.getGamesA() + this.currentMatchState.getGamesB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInGameN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal;
            if (teamId == TeamId.UNKNOWN)
                pointtTotal = pointsA + pointsB;
            else if (teamId == TeamId.A)
                pointtTotal = pointsA;
            else
                pointtTotal = pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        // int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
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
            // System.out.println(marketSetNo+"---"+pointsA+"--"+pointsB);
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        } else
            return null;
    }

    private void parseSequenceId(String sequenceId) {
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
