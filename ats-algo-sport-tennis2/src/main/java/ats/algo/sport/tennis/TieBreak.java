package ats.algo.sport.tennis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.GCMath;

/**
 * implements tie break logic
 *
 * @author Geoff
 * 
 */
public class TieBreak implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int nPointsInFinalSetTieBreakGame = 7;
    @JsonIgnore
    private boolean realMatch = true;
    int pointsA;
    int pointsB;
    TeamId onServeSideNow;
    int onServePlayerTeamANow;
    int onServePlayerTeamBNow;
    TeamId onServeAtStartOfTieBreak;
    int onServePlayerTeamAAtStartOfTieBreak;
    int onServePlayerTeamBAtStartOfTieBreak;
    boolean noAdvantageTiebreakFormat;
    boolean doubles;
    protected StoreLastGameScore storeLastGameScore;
    boolean fastFourMatch = false;

    public TieBreak() {} // required for JSON serialisation

    public TieBreak(boolean doubles) {
        this.doubles = doubles;
    }

    public TieBreak(boolean doubles, boolean noAdvantageTiebreakFormat) {
        this.doubles = doubles;
        this.noAdvantageTiebreakFormat = noAdvantageTiebreakFormat;
    }

    @JsonIgnore
    public void setRealMatch(boolean realMatch) {
        this.realMatch = realMatch;
    }

    @JsonIgnore
    public void setStoreLastGameScore(StoreLastGameScore storeLastGameScore) {
        this.storeLastGameScore = storeLastGameScore;
        // System.out.println(storeLastGameScore);
    }

    public boolean isFastFourMatch() {
        return fastFourMatch;
    }

    public void setFastFourMatch(boolean fastFourMatch) {
        this.fastFourMatch = fastFourMatch;
    }

    public int getnPointsInFinalSetTieBreakGame() {
        return nPointsInFinalSetTieBreakGame;
    }

    public void setnPointsInFinalSetTieBreakGame(int nPointsInFinalSetTieBreakGame) {
        this.nPointsInFinalSetTieBreakGame = nPointsInFinalSetTieBreakGame;
    }

    void startNewTieBreak(TeamId onServeNow, int onServePlayerTeamANow, int onServePlayerTeamBNow) {
        pointsA = 0;
        pointsB = 0;
        this.onServeSideNow = onServeNow;
        this.onServePlayerTeamANow = onServePlayerTeamANow;
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
        this.onServeAtStartOfTieBreak = onServeNow;
        this.onServePlayerTeamAAtStartOfTieBreak = onServePlayerTeamANow;
        this.onServePlayerTeamBAtStartOfTieBreak = onServePlayerTeamBNow;
    }

    public void setEqualTo(TieBreak tieBreak) {
        this.pointsA = tieBreak.pointsA;
        this.pointsB = tieBreak.pointsB;
        this.onServePlayerTeamANow = tieBreak.onServePlayerTeamANow;
        this.onServePlayerTeamBNow = tieBreak.onServePlayerTeamBNow;
        this.onServeAtStartOfTieBreak = tieBreak.onServeAtStartOfTieBreak;
        this.noAdvantageTiebreakFormat = tieBreak.noAdvantageTiebreakFormat;
        this.onServePlayerTeamAAtStartOfTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
        this.onServePlayerTeamBAtStartOfTieBreak = tieBreak.onServePlayerTeamBAtStartOfTieBreak;
        this.storeLastGameScore = tieBreak.storeLastGameScore;
        this.fastFourMatch = tieBreak.fastFourMatch;
    }

    GameOrTieBreakEventResult updateForPointPlayed(boolean pointWonByA, boolean inFinalSet) {
        GameOrTieBreakEventResult gameOutcome;
        if (pointWonByA) {
            gameOutcome = GameOrTieBreakEventResult.POINTWONA;
            pointsA++;
        } else {
            gameOutcome = GameOrTieBreakEventResult.POINTWONB;
            pointsB++;
        }

        if (!noAdvantageTiebreakFormat) {

            if (pointsA - pointsB >= 2 && pointsA >= (inFinalSet ? nPointsInFinalSetTieBreakGame : 7)) {
                gameOutcome = GameOrTieBreakEventResult.GAMEWONA;
            }
            if (pointsB - pointsA >= 2 && pointsB >= (inFinalSet ? nPointsInFinalSetTieBreakGame : 7)) {
                gameOutcome = GameOrTieBreakEventResult.GAMEWONB;
            }
        } else {
            if (fastFourMatch) {
                if (pointsA >= 5) {
                    gameOutcome = GameOrTieBreakEventResult.GAMEWONA;
                }
                if (pointsB >= 5) {
                    gameOutcome = GameOrTieBreakEventResult.GAMEWONB;
                }

            } else {

                if (pointsA >= 7) {
                    gameOutcome = GameOrTieBreakEventResult.GAMEWONA;
                }
                if (pointsB >= 7) {
                    gameOutcome = GameOrTieBreakEventResult.GAMEWONB;
                }
            }
        }
        if (GCMath.isOdd(pointsA + pointsB))
            onServeSideNow = TennisMatchState.switchServer(onServeSideNow);
        // alternate server player at tie breaks
        // if (!GCMath.isOdd(pointsA + pointsB) && tieBreakAlterPlayer(pointsA + pointsB)) {
        // onServePlayerTeamANow = switchPlayer(onServePlayerTeamANow);
        // onServePlayerTeamBNow = switchPlayer(onServePlayerTeamBNow);
        // }
        if ((pointsA + pointsB) >= 3 && tieBreakAlterPlayer(pointsA + pointsB)) {
            onServePlayerTeamANow = TennisMatchState.switchPlayer(doubles, onServePlayerTeamANow);
            onServePlayerTeamBNow = TennisMatchState.switchPlayer(doubles, onServePlayerTeamBNow);
        }
        if (realMatch)
            if (gameOutcome != GameOrTieBreakEventResult.POINTWONA
                            && gameOutcome != GameOrTieBreakEventResult.POINTWONB)
                storeLastGameScore.handle(pointsA, pointsB);
        return gameOutcome;
    }

    private boolean tieBreakAlterPlayer(int i) {
        if ((i - 3) % 4 == 0)
            return true;
        else
            return false;
    }

    public int getPointsA() {
        return pointsA;
    }

    public void setPointsA(int pointsA) {
        this.pointsA = pointsA;
    }

    public int getPointsB() {
        return pointsB;
    }

    public void setPointsB(int pointsB) {
        this.pointsB = pointsB;
    }

    public TeamId getOnServeSideNow() {
        return onServeSideNow;
    }

    public void setOnServeSideNow(TeamId onServeNow) {
        this.onServeSideNow = onServeNow;
    }

    public TeamId getOnServeNow() {
        return onServeSideNow;
    }

    public void setOnServeNow(TeamId onServeNow) {
        this.onServeSideNow = onServeNow;
    }

    public int getOnServePlayerTeamANow() {
        return onServePlayerTeamANow;
    }

    public int getOnServePlayerTeamBNow() {
        return onServePlayerTeamBNow;
    }

    public TeamId getOnServeAtStartOfTieBreak() {
        return onServeAtStartOfTieBreak;
    }

    public void setOnServeAtStartOfTieBreak(TeamId onServeAtStartOfTieBreak) {
        this.onServeAtStartOfTieBreak = onServeAtStartOfTieBreak;
    }

    public void setOnServePlayerTeamAAtStartOfTieBreak(int onServePlayerTeamAAtStartOfTieBreak) {
        this.onServePlayerTeamAAtStartOfTieBreak = onServePlayerTeamAAtStartOfTieBreak;
    }

    public void setOnServePlayerTeamBAtStartOfTieBreak(int onServePlayerTeamBAtStartOfTieBreak) {
        this.onServePlayerTeamBAtStartOfTieBreak = onServePlayerTeamBAtStartOfTieBreak;
    }


    public boolean isDoubles() {
        return doubles;
    }

    public void setDoubles(boolean doubles) {
        this.doubles = doubles;
    }

    public int getOnServePlayerTeamAAtStartOfTieBreak() {
        return onServePlayerTeamAAtStartOfTieBreak;
    }

    public int getOnServePlayerTeamBAtStartOfTieBreak() {
        return onServePlayerTeamBAtStartOfTieBreak;
    }

    public void setOnServePlayerTeamANow(int onServePlayerTeamANow) {
        this.onServePlayerTeamANow = onServePlayerTeamANow;
    }

    public void setOnServePlayerTeamBNow(int onServePlayerTeamBNow) {
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
    }


    public boolean isNoAdvantageTiebreakFormat() {
        return noAdvantageTiebreakFormat;
    }

    public void setNoAdvantageTiebreakFormat(boolean noAdvantageTiebreakFormat) {
        this.noAdvantageTiebreakFormat = noAdvantageTiebreakFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (doubles ? 1231 : 1237);
        result = prime * result + (noAdvantageTiebreakFormat ? 1231 : 1237);
        result = prime * result + ((onServeAtStartOfTieBreak == null) ? 0 : onServeAtStartOfTieBreak.hashCode());
        result = prime * result + onServePlayerTeamAAtStartOfTieBreak;
        result = prime * result + onServePlayerTeamANow;
        result = prime * result + onServePlayerTeamBAtStartOfTieBreak;
        result = prime * result + onServePlayerTeamBNow;
        result = prime * result + ((onServeSideNow == null) ? 0 : onServeSideNow.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + (realMatch ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TieBreak other = (TieBreak) obj;
        if (doubles != other.doubles)
            return false;
        if (noAdvantageTiebreakFormat != other.noAdvantageTiebreakFormat)
            return false;
        if (onServeAtStartOfTieBreak != other.onServeAtStartOfTieBreak)
            return false;
        if (onServePlayerTeamAAtStartOfTieBreak != other.onServePlayerTeamAAtStartOfTieBreak)
            return false;
        if (onServePlayerTeamANow != other.onServePlayerTeamANow)
            return false;
        if (onServePlayerTeamBAtStartOfTieBreak != other.onServePlayerTeamBAtStartOfTieBreak)
            return false;
        if (onServePlayerTeamBNow != other.onServePlayerTeamBNow)
            return false;
        if (onServeSideNow != other.onServeSideNow)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (realMatch != other.realMatch)
            return false;
        return true;
    }


}
