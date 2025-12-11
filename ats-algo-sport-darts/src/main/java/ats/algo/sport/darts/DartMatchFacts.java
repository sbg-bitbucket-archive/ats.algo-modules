package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.darts.LegWinResult;
import ats.algo.sport.darts.LegWinResult.LegWinType;

class DartMatchFacts {
    int matchCount180sA;
    int matchCount180sB;
    boolean matchHas9DartFinish;
    int matchHighestCheckoutScoreA;
    int matchHighestCheckoutScoreB;
    int matchSetsA;
    int matchSetsB;
    TeamId matchNextToScore180;

    int currentSetCount180sA;
    int currentSetCount180sB;
    int currentSetLegScoreA;
    int currentSetLegScoreB;
    TeamId currentSetLegPlus1Winner;
    TeamId currentSetLegPlus2Winner;
    TeamId currentSetWinner;

    TeamId nextSetWinner;
    int nextSetLegScoreA;
    int nextSetLegScoreB;

    TeamId currentLegWinner;
    Colour currentLegCheckoutColour;
    int currentLegCheckoutScore;
    int currentLegNo180s;

    TeamId nextLegWinner;
    Colour nextLegCheckoutColour;
    int nextLegCheckoutScore;
    int nextLegNo180s;

    TeamId legPlus2Winner;

    DartMatchFacts() {
        matchNextToScore180 = TeamId.UNKNOWN;
    }

    /**
     * initialises all the variables for the next run of the monte carlo model
     * 
     * @param matchCount180sA
     * @param matchCount180sB
     * @param matchHas9DartFinish
     * @param matchHighestCheckoutScoreA
     * @param matchHighestCheckoutScoreB
     * @param matchNextToScore180
     */

    void resetCountsForNextMatchIteration(DartMatchState matchState) {
        int matchCount180sA = matchState.getPlayerAScore().n180s;
        int matchCount180sB = matchState.getPlayerBScore().n180s;
        boolean matchHas9DartFinish = matchState.getMatchHas9DartFinish();
        int matchHighestCheckoutScoreA = matchState.getPlayerAScore().highestCheckout;
        int matchHighestCheckoutScoreB = matchState.getPlayerBScore().highestCheckout;
        resetCountsForNextMatchIteration(matchCount180sA, matchCount180sB, matchHas9DartFinish,
                        matchHighestCheckoutScoreA, matchHighestCheckoutScoreB);
    }

    void resetCountsForNextMatchIteration(int matchCount180sA, int matchCount180sB, boolean matchHas9DartFinish,
                    int matchHighestCheckoutScoreA, int matchHighestCheckoutScoreB) {
        this.matchCount180sA = matchCount180sA;
        this.matchCount180sB = matchCount180sB;
        this.matchHas9DartFinish = matchHas9DartFinish;
        this.matchHighestCheckoutScoreA = matchHighestCheckoutScoreA;
        this.matchHighestCheckoutScoreB = matchHighestCheckoutScoreB;
        this.matchNextToScore180 = TeamId.UNKNOWN;

        this.currentSetCount180sA = 0;
        this.currentSetCount180sB = 0;
        this.currentSetLegScoreA = 0;
        this.currentSetLegScoreB = 0;
        this.currentSetLegPlus1Winner = TeamId.UNKNOWN;
        this.currentSetLegPlus2Winner = TeamId.UNKNOWN;
        this.currentSetWinner = TeamId.UNKNOWN;

        this.nextSetWinner = TeamId.UNKNOWN;
        this.nextSetLegScoreA = 0;
        this.nextSetLegScoreB = 0;

        this.currentLegWinner = TeamId.UNKNOWN;
        this.currentLegCheckoutColour = null;
        this.currentLegCheckoutScore = 0;
        this.currentLegNo180s = 0;

        this.nextLegWinner = TeamId.UNKNOWN;
        this.nextLegCheckoutColour = null;
        this.nextLegCheckoutScore = 0;
        this.nextLegNo180s = 0;

        this.legPlus2Winner = TeamId.UNKNOWN;

    }

    /**
     * 
     * @param lr
     * @param legWinResult
     * @param currentLegNoPlusN
     * @param currentSetNoPlusN
     */
    void updateFactsWithLegResults(LegResult lr, LegWinResult legWinResult, int currentLegNoPlusN,
                    int currentSetNoPlusN) {
        /*
         * leg based facts
         */
        switch (currentLegNoPlusN) {
            case 0:
                currentLegWinner = lr.legWinner;
                currentLegCheckoutColour = lr.checkoutColour;
                currentLegCheckoutScore = lr.checkoutScore;
                currentLegNo180s = lr.N180sThrownA + lr.N180sThrownB;
                break;
            case 1:
                nextLegWinner = lr.legWinner;
                nextLegCheckoutColour = lr.checkoutColour;
                nextLegCheckoutScore = lr.checkoutScore;
                nextLegNo180s = lr.N180sThrownA + lr.N180sThrownB;
                break;
            case 2:
                legPlus2Winner = lr.legWinner;
            default:
                break;
        }
        /*
         * match based facts
         */
        matchCount180sA += lr.N180sThrownA;
        matchCount180sB += lr.N180sThrownB;
        if (lr.legWinner == TeamId.A) {
            matchHas9DartFinish = matchHas9DartFinish || (lr.NDartsThrownA == 9);
            if (lr.checkoutScore > matchHighestCheckoutScoreA)
                matchHighestCheckoutScoreA = lr.checkoutScore;
        } else {
            matchHas9DartFinish = matchHas9DartFinish || (lr.NDartsThrownB == 9);
            if (lr.checkoutScore > matchHighestCheckoutScoreB)
                matchHighestCheckoutScoreB = lr.checkoutScore;
        }

        if (matchNextToScore180 == TeamId.UNKNOWN && lr.first180InLeg != null) {
            matchNextToScore180 = lr.first180InLeg;
        }

        /*
         * leg based facts for the current set
         */

        if (currentSetNoPlusN == 0) {
            switch (currentLegNoPlusN) {
                case 1:
                    currentSetLegPlus1Winner = lr.legWinner;
                    break;
                case 2:
                    currentSetLegPlus2Winner = lr.legWinner;
                    break;
            }
            currentSetCount180sA += lr.N180sThrownA;
            currentSetCount180sB += lr.N180sThrownB;
        }
        /*
         * set based facts
         */
        if (legWinResult.legWinType == LegWinType.IsMatchWinner || legWinResult.legWinType == LegWinType.IsSetWinner
                        || legWinResult.legWinType == LegWinType.IsMatchDraw)
            // end of a set
            switch (currentSetNoPlusN) {
                case 0:
                    currentSetWinner = lr.legWinner;
                    currentSetLegScoreA = legWinResult.lastSetLegsA;
                    currentSetLegScoreB = legWinResult.lastSetLegsB;

                    break;
                case 1:
                    nextSetWinner = lr.legWinner;
                    nextSetLegScoreA = legWinResult.lastSetLegsA;
                    nextSetLegScoreB = legWinResult.lastSetLegsB;
                    break;
                default:
                    break;
            }
    }
}
