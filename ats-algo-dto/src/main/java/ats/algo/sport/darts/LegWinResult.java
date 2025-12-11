package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;

/**
 * holds the state of the match following the event of a leg being won
 * 
 * @author Geoff
 * 
 */
class LegWinResult {

    public enum LegWinType {
        IsMatchWinner,
        IsMatchDraw,
        IsSetWinner,
        IsNotMatchOrSetWinner
    }

    public LegWinType legWinType;
    public int lastSetLegsA;
    public int lastSetLegsB;
    public int matchScoreSetsOrLegsA;
    public int matchScoreSetsOrLegsB;
    public int n180sA;
    public int n180sB;
    public TeamId matchWinner;
    public TeamId playerAtOcheAtStartOfLeg;
    public TeamId currentLegWinner;
    public boolean is9DartFinish;

    public LegWinResult(LegWinType legWinType, int lastSetLegsA, int lastSetLegsB, int matchScoreSetsOrLegsA,
                    int matchScoreSetsOrLegsB, int n180sA, int n180sB, TeamId matchWinner, boolean is9DartFinish) {
        this.legWinType = legWinType;
        this.lastSetLegsA = lastSetLegsA;
        this.lastSetLegsB = lastSetLegsB;
        this.matchScoreSetsOrLegsA = matchScoreSetsOrLegsA;
        this.matchScoreSetsOrLegsB = matchScoreSetsOrLegsB;
        this.n180sA = n180sA;
        this.n180sB = n180sB;
        this.matchWinner = matchWinner;
        this.is9DartFinish = is9DartFinish;
    }

    public LegWinResult() {

    }
}
