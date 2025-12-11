package ats.algo.sport.tennis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.common.TeamId;

/**
 * holds the state for the currently active game
 *
 * @author Geoff
 * 
 */
public class Game implements Serializable, StoreLastGameScore {
    /**
     * 
     */
    @JsonIgnore
    private boolean realMatch = true;
    private static final long serialVersionUID = 1L;
    int pointsA;
    int pointsB;
    int pointNo;
    TeamId onServeSideNow;
    int onServePlayerTeamANow;
    int onServePlayerTeamBNow;
    boolean noAdvantageGameFormat;
    boolean powerPointCurrentPoint;
    private String previousGameScore;
    // @FunctionalInterface
    // public interface StoreLastGameScore {
    // public void handle(int a, int b);
    // }


    @JsonIgnore
    public void setRealMatch(boolean realMatch) {
        this.realMatch = realMatch;
    }

    public Game() {} // required for JSON serialisation

    public Game(boolean noAdvantageGameFormat) {
        this.noAdvantageGameFormat = noAdvantageGameFormat;
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

    public int getPointNo() {
        return pointNo;
    }

    public void setPointNo(int pointNo) {
        this.pointNo = pointNo;
    }

    // used only for current json
    public TeamId getOnServeNow() {
        return onServeSideNow;
    }

    public void setOnServeNow(TeamId onServeNow) {
        this.onServeSideNow = onServeNow;
    }

    public TeamId getOnServeSideNow() {
        return onServeSideNow;
    }

    public void setOnServeSideNow(TeamId onServeNow) {
        this.onServeSideNow = onServeNow;
    }

    public int getOnServePlayerTeamANow() {
        return onServePlayerTeamANow;
    }

    public void setOnServePlayerTeamANow(int onServePlayerTeamANow) {
        this.onServePlayerTeamANow = onServePlayerTeamANow;
    }

    public int getOnServePlayerTeamBNow() {
        return onServePlayerTeamBNow;
    }

    public void setOnServePlayerTeamBNow(int onServePlayerTeamBNow) {
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
    }

    public boolean isNoAdvantageGameFormat() {
        return noAdvantageGameFormat;
    }

    public void setNoAdvantageGameFormat(boolean noAdvantageGameFormat) {
        this.noAdvantageGameFormat = noAdvantageGameFormat;
    }

    public boolean isPowerPointCurrentPoint() {
        return powerPointCurrentPoint;
    }

    public void setPowerPointCurrentPoint(boolean powerPointCurrentPoint) {
        this.powerPointCurrentPoint = powerPointCurrentPoint;
    }


    public String getPreviousGameScore() {
        return previousGameScore;
    }

    public void setPreviousGameScore(String previousGameScore) {
        this.previousGameScore = previousGameScore;
    }

    void startNewGame(TeamId onServeSideNow, int onServePlayerTeamANow, int onServePlayerTeamBNow) {
        pointsA = 0;
        pointsB = 0;
        pointNo = 0;
        this.onServeSideNow = onServeSideNow;
        this.onServePlayerTeamANow = onServePlayerTeamANow;
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
        this.previousGameScore = null;
    }

    public void setEqualTo(Game game) {
        this.noAdvantageGameFormat = game.noAdvantageGameFormat;
        this.pointsA = game.pointsA;
        this.pointsB = game.pointsB;
        this.pointNo = game.pointNo;
        this.onServeSideNow = game.onServeSideNow;
        this.onServePlayerTeamANow = game.onServePlayerTeamANow;
        this.onServePlayerTeamBNow = game.onServePlayerTeamBNow;
        this.powerPointCurrentPoint = game.powerPointCurrentPoint;
        this.previousGameScore = game.previousGameScore;
        // System.out.println(game.storeLastGameScore);
    }

    /**
     * updates score for point played, and returns result
     *
     * @param pointWonByA
     * @return
     */
    GameOrTieBreakEventResult updateForPointPlayed(boolean pointWonByA) {
        GameOrTieBreakEventResult pointWonOutcome;
        pointNo++;
        if (pointWonByA) {
            pointWonOutcome = GameOrTieBreakEventResult.POINTWONA;
            pointsA++;
            // For IPTL games only
            if (powerPointCurrentPoint && onServeSideNow.equals(TeamId.B)) {
                pointNo++;
                pointsA++;
            }
        } else {
            pointWonOutcome = GameOrTieBreakEventResult.POINTWONB;
            pointsB++;
            if (powerPointCurrentPoint && onServeSideNow.equals(TeamId.A)) {
                pointNo++;
                pointsB++;
            }
        }
        if (noAdvantageGameFormat) {
            if (pointsA >= 4)
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONA;
            if (pointsB >= 4)
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONB;

        } else {
            if (pointsA - pointsB >= 2 && pointsA >= 4) {
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONA;
            }
            if (pointsB - pointsA >= 2 && pointsB >= 4) {
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONB;
            }
            if (pointsA == 4 && pointsB == 4) {
                pointsA = 3;
                pointsB = 3;
            }

        }
        if (realMatch)
            if (pointWonOutcome != GameOrTieBreakEventResult.POINTWONA
                            && pointWonOutcome != GameOrTieBreakEventResult.POINTWONB)
                handle(pointsA, pointsB);

        return pointWonOutcome;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (noAdvantageGameFormat ? 1231 : 1237);
        result = prime * result + onServePlayerTeamANow;
        result = prime * result + onServePlayerTeamBNow;
        result = prime * result + ((onServeSideNow == null) ? 0 : onServeSideNow.hashCode());
        result = prime * result + pointNo;
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + (powerPointCurrentPoint ? 1231 : 1237);
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
        Game other = (Game) obj;
        if (noAdvantageGameFormat != other.noAdvantageGameFormat)
            return false;
        if (onServePlayerTeamANow != other.onServePlayerTeamANow)
            return false;
        if (onServePlayerTeamBNow != other.onServePlayerTeamBNow)
            return false;
        if (onServeSideNow != other.onServeSideNow)
            return false;
        if (pointNo != other.pointNo)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (powerPointCurrentPoint != other.powerPointCurrentPoint)
            return false;
        return true;
    }

    @Override
    public void handle(int a, int b) {
        String value = pointsA + "-" + pointsB;
        this.previousGameScore = value;
    }


}
