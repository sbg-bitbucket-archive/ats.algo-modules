package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class BeachVolleyballMatchResultMarkets extends MatchResultMarkets {
    BeachVolleyballMatchState previousMatchState;
    BeachVolleyballMatchState currentMatchState;
    boolean matchCompleted;
    int marketSetNo;
    int marketPointNo;

    /**
     * 
     * @param market
     * @param matchState
     * @return
     */
    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        this.previousMatchState = (BeachVolleyballMatchState) previousMatchState;
        this.currentMatchState = (BeachVolleyballMatchState) currentMatchState;
        matchCompleted = this.currentMatchState.isMatchCompleted();
        parseSequenceId(market.getSequenceId());
        int pointsA1 = 0;
        int pointsB1 = 0;
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
            case "FT:CS":
                if (matchCompleted) {
                    winningSelection =
                                    String.format("%d-%d", ((BeachVolleyballMatchState) currentMatchState).getSetsA(),
                                                    ((BeachVolleyballMatchState) currentMatchState).getSetsB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:OU":
                return checkSetMktTotalResulted(market);
            case "P:PSPRD":
                return checkSetMktHandcaipResulted(market);
            case "P:PW":
                return checkSetPointMktResulted(market, 3);
        }
        return null;

    }

    private CheckMarketResultedOutcome checkSetPointMktResulted(Market market, int n) {
        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
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
            // System.out.println(marketSetNo+"---"+pointsA+"--"+pointsB);
            return new CheckMarketResultedOutcome(winningSelection, pointDiff);
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktTotalResulted(Market market) {

        int currentSetNo = this.currentMatchState.getSetsA() + this.currentMatchState.getSetsB();
        if (currentSetNo > marketSetNo - 1) {
            int pointsA = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).A;
            int pointsB = this.currentMatchState.getGameScoreInSetN(marketSetNo - 1).B;
            String winningSelection;
            int pointtTotal = pointsA + pointsB;
            winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal, market.getLineId());
            // System.out.println(marketSetNo+"---"+pointsA+"--"+pointsB);
            return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
        } else
            return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        // int marketSeqNo = convertSeqNoToIndex(marketSetNo, marketGameNo, 0);
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
            // System.out.println(marketSetNo+"---"+pointsA+"--"+pointsB);
            return new CheckMarketResultedOutcome(winningSelection);
        } else if (matchCompleted) {
            return new CheckMarketResultedOutcome();
        }
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

}
