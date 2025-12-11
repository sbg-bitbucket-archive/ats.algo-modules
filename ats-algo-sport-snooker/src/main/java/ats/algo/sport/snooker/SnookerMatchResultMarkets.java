package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketUtilityFunctions;

public class SnookerMatchResultMarkets extends MatchResultMarkets {
    SnookerMatchState previousMatchState;
    SnookerMatchState currentMatchState;
    int marketFrameNo;
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
        this.previousMatchState = (SnookerMatchState) previousMatchState;
        this.currentMatchState = (SnookerMatchState) currentMatchState;

        parseSequenceId(market.getSequenceId());
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;

        String winningSelection;

        switch (market.getType()) {
            case "FT:ML":
                if (this.currentMatchState.isMatchCompleted()) {
                    if (this.currentMatchState.getFramesA() > this.currentMatchState.getFramesB())
                        winningSelection = "A";
                    else
                        winningSelection = "B";

                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "FT:SPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    int setDiff = this.currentMatchState.getFramesA() - this.currentMatchState.getFramesB();
                    winningSelection =
                                    MarketUtilityFunctions.getWinningSelectionHcapMarket(setDiff, market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, setDiff);
                } else
                    return null;
            case "FT:PSPRD":
                if (this.currentMatchState.isMatchCompleted()) {
                    for (int i = 0; i < (this.currentMatchState.getFramesA()
                                    + this.currentMatchState.getFramesB()); i++) {

                        pointsA1 = this.currentMatchState.getFrameScoreInMatchN(i).A;
                        pointsB1 = this.currentMatchState.getFrameScoreInMatchN(i).B;
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
                    int pointtTotal = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
                    winningSelection = MarketUtilityFunctions.getWinningSelectionOvunMarket(pointtTotal,
                                    market.getLineId());

                    return new CheckMarketResultedOutcome(winningSelection, pointtTotal);
                } else
                    return null;

            case "FT:CS":
                if (this.currentMatchState.isMatchCompleted()) {
                    winningSelection = String.format("%d-%d", ((SnookerMatchState) currentMatchState).getFramesA(),
                                    ((SnookerMatchState) currentMatchState).getFramesB());
                    return new CheckMarketResultedOutcome(winningSelection);
                } else
                    return null;
            case "P:ML":
                return checkSetMktResulted();
            case "P:X-Y":
                int currentSetNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB() + 1;
                if (currentSetNo == marketFrameNo + 1) {
                    int framesA = ((SnookerMatchState) currentMatchState).getFramesA()
                                    - ((SnookerMatchState) currentMatchState).getRaceScore()[currentSetNo / 4 - 1].A;
                    int framesB = ((SnookerMatchState) currentMatchState).getFramesB()
                                    - ((SnookerMatchState) currentMatchState).getRaceScore()[currentSetNo / 4 - 1].B;
                    winningSelection = String.format("%d-%d", framesA, framesB);
                    return new CheckMarketResultedOutcome(winningSelection);

                }
                if (this.currentMatchState.isMatchCompleted()) {
                    return new CheckMarketResultedOutcome();
                }
                return null;
            case "P:RACE":
                return checkRaceToFrameX();
            default:
                return null;
        }


    }

    private CheckMarketResultedOutcome checkRaceToFrameX() {

        int framesA;
        framesA = this.currentMatchState.getFramesA();
        int framesB;
        framesB = this.currentMatchState.getFramesB();

        String winningSelection;
        if (framesA == marketFrameNo && framesB < marketFrameNo) {
            winningSelection = "A";
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (framesB == marketFrameNo && framesA < marketFrameNo) {
            winningSelection = "B";
            return new CheckMarketResultedOutcome(winningSelection);
        }
        if (this.currentMatchState.isMatchCompleted()) {

            framesA = this.currentMatchState.getFramesA();
            framesB = this.currentMatchState.getFramesB();
            if (framesA == marketFrameNo && framesB < marketFrameNo) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (framesB == marketFrameNo && framesA < marketFrameNo) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            return new CheckMarketResultedOutcome();
        }

        return null;
    }

    private CheckMarketResultedOutcome checkSetMktResulted() {
        int currentSetNo = this.currentMatchState.getFramesA() + this.currentMatchState.getFramesB();
        if (currentSetNo > marketFrameNo - 1) {
            int pointsA = this.currentMatchState.getFramesA();
            int pointsB = this.currentMatchState.getFramesB();
            int pointsA1 = this.previousMatchState.getFramesA();
            int pointsB1 = this.previousMatchState.getFramesB();

            String winningSelection;
            if (pointsA > pointsA1) {
                winningSelection = "A";
                return new CheckMarketResultedOutcome(winningSelection);
            }
            if (pointsB > pointsB1) {
                winningSelection = "B";
                return new CheckMarketResultedOutcome(winningSelection);
            }
        }
        if (this.currentMatchState.isMatchCompleted()) {
            return new CheckMarketResultedOutcome();
        }
        return null;
    }

    void parseSequenceId(String sequenceId) {
        marketFrameNo = 0;
        marketPointNo = 0;
        String[] bits = sequenceId.split("\\.");
        if (bits[0].length() > 1 && bits[0] != "M") {
            String setNo = bits[0].substring(1, bits[0].length());
            marketFrameNo = Integer.parseInt(setNo);
        }
        if (bits.length >= 2)
            marketPointNo = Integer.parseInt(bits[1]);
    }

}
