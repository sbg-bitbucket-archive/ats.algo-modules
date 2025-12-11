package ats.algo.sport.tennis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.GCMath;

/**
 * implements super tie break logic, only useful for the tennis double
 *
 * @author Jin
 * 
 */
public class SuperTieBreak implements Serializable {
    /**
     * 
     */

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private boolean realMatch = true;
    int pointsA;
    int pointsB;
    TeamId onServeSideNow;
    int onServePlayerTeamANow;
    int onServePlayerTeamBNow;
    TeamId onServeAtStartOfSuperTieBreak;
    int onServePlayerTeamAAtStartOfSuperTieBreak;
    int onServePlayerTeamBAtStartOfSuperTieBreak;
    private boolean doubles;
    private static final int finalSetSuperTBNo = 10;
    protected StoreLastGameScore storeLastGameScore;

    @JsonIgnore
    public void setRealMatch(boolean realMatch) {
        this.realMatch = realMatch;
    }

    @JsonIgnore
    public void setStoreLastGameScore(StoreLastGameScore storeLastGameScore) {
        this.storeLastGameScore = storeLastGameScore;
    }

    public SuperTieBreak() {}

    public SuperTieBreak(boolean doubles) {
        this.doubles = doubles;
    }



    void startNewSuperTieBreak(TeamId onServeNow, int onServePlayerTeamANow, int onServePlayerTeamBNow) {
        pointsA = 0;
        pointsB = 0;
        this.onServeSideNow = onServeNow;
        this.onServePlayerTeamANow = onServePlayerTeamANow;
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
        this.onServeAtStartOfSuperTieBreak = onServeNow;
        this.onServePlayerTeamAAtStartOfSuperTieBreak = onServePlayerTeamANow;
        this.onServePlayerTeamBAtStartOfSuperTieBreak = onServePlayerTeamBNow;
    }

    public void setEqualTo(SuperTieBreak superTieBreak) {
        this.pointsA = superTieBreak.pointsA;
        this.pointsB = superTieBreak.pointsB;
        this.onServePlayerTeamANow = superTieBreak.onServePlayerTeamANow;
        this.onServePlayerTeamBNow = superTieBreak.onServePlayerTeamBNow;
        this.onServeAtStartOfSuperTieBreak = superTieBreak.onServeAtStartOfSuperTieBreak;
        this.onServePlayerTeamAAtStartOfSuperTieBreak = superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
        this.onServePlayerTeamBAtStartOfSuperTieBreak = superTieBreak.onServePlayerTeamBAtStartOfSuperTieBreak;
        this.storeLastGameScore = superTieBreak.storeLastGameScore;
    }

    GameOrTieBreakEventResult updateForPointPlayed(boolean pointWonByA) {
        GameOrTieBreakEventResult gameOutcome;
        if (pointWonByA) {
            gameOutcome = GameOrTieBreakEventResult.POINTWONA;
            pointsA++;
        } else {
            gameOutcome = GameOrTieBreakEventResult.POINTWONB;
            pointsB++;
        }
        /*
         * update scores accordingly
         */
        // if (noAdvantageGameFormat) {
        // // first reached 4 points won game
        // if (pointsA >= finalSetSuperTBNo) {
        // gameOutcome = GameOrTieBreakEventResult.SUPERTIEBREAKSETWONA;
        // }
        // if (pointsB >= finalSetSuperTBNo) {
        // gameOutcome = GameOrTieBreakEventResult.SUPERTIEBREAKSETWONB;
        // }
        //
        // } else

        {

            if (pointsA - pointsB >= 2 && pointsA >= finalSetSuperTBNo) {
                gameOutcome = GameOrTieBreakEventResult.SUPERTIEBREAKSETWONA;
            }
            if (pointsB - pointsA >= 2 && pointsB >= finalSetSuperTBNo) {
                gameOutcome = GameOrTieBreakEventResult.SUPERTIEBREAKSETWONB;
            }
        }
        if (GCMath.isOdd(pointsA + pointsB))
            onServeSideNow = TennisMatchState.switchServer(onServeSideNow);
        // Can be simplified
        if ((pointsA + pointsB) >= 3 && superTieBreakAlterPlayer(pointsA + pointsB)) {
            onServePlayerTeamANow = TennisMatchState.switchPlayer(doubles, onServePlayerTeamANow);
            onServePlayerTeamBNow = TennisMatchState.switchPlayer(doubles, onServePlayerTeamBNow);
        }
        if (realMatch)
            if (gameOutcome != GameOrTieBreakEventResult.POINTWONA
                            && gameOutcome != GameOrTieBreakEventResult.POINTWONB)
                storeLastGameScore.handle(pointsA, pointsB);

        return gameOutcome;
    }

    private boolean superTieBreakAlterPlayer(int i) {
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

    public int getOnServePlayerTeamANow() {
        return onServePlayerTeamANow;
    }

    public int getOnServePlayerTeamBNow() {
        return onServePlayerTeamBNow;
    }

    public TeamId getOnServeAtStartOfSuperTieBreak() {
        return onServeAtStartOfSuperTieBreak;
    }

    public void setOnServeAtStartOfSuperTieBreak(TeamId onServeAtStartOfTieBreak) {
        this.onServeAtStartOfSuperTieBreak = onServeAtStartOfTieBreak;
    }

    public void setOnServePlayerTeamAAtStartOfSuperTieBreak(int onServePlayerTeamAAtStartOfTieBreak) {
        this.onServePlayerTeamAAtStartOfSuperTieBreak = onServePlayerTeamAAtStartOfTieBreak;
    }

    public void setOnServePlayerTeamBAtStartOfSuperTieBreak(int onServePlayerTeamBAtStartOfTieBreak) {
        this.onServePlayerTeamBAtStartOfSuperTieBreak = onServePlayerTeamBAtStartOfTieBreak;
    }

    public boolean isDoubles() {
        return doubles;
    }

    public void setDoubles(boolean doubles) {
        this.doubles = doubles;
    }

    public int getOnServePlayerTeamAAtStartOfSuperTieBreak() {
        return onServePlayerTeamAAtStartOfSuperTieBreak;
    }

    public int getOnServePlayerTeamBAtStartOfSuperTieBreak() {
        return onServePlayerTeamBAtStartOfSuperTieBreak;
    }

    public void setOnServePlayerTeamANow(int onServePlayerTeamANow) {
        this.onServePlayerTeamANow = onServePlayerTeamANow;
    }

    public void setOnServePlayerTeamBNow(int onServePlayerTeamBNow) {
        this.onServePlayerTeamBNow = onServePlayerTeamBNow;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (doubles ? 1231 : 1237);
        result = prime * result
                        + ((onServeAtStartOfSuperTieBreak == null) ? 0 : onServeAtStartOfSuperTieBreak.hashCode());
        result = prime * result + onServePlayerTeamAAtStartOfSuperTieBreak;
        result = prime * result + onServePlayerTeamANow;
        result = prime * result + onServePlayerTeamBAtStartOfSuperTieBreak;
        result = prime * result + onServePlayerTeamBNow;
        result = prime * result + ((onServeSideNow == null) ? 0 : onServeSideNow.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
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
        SuperTieBreak other = (SuperTieBreak) obj;
        if (doubles != other.doubles)
            return false;
        if (onServeAtStartOfSuperTieBreak != other.onServeAtStartOfSuperTieBreak)
            return false;
        if (onServePlayerTeamAAtStartOfSuperTieBreak != other.onServePlayerTeamAAtStartOfSuperTieBreak)
            return false;
        if (onServePlayerTeamANow != other.onServePlayerTeamANow)
            return false;
        if (onServePlayerTeamBAtStartOfSuperTieBreak != other.onServePlayerTeamBAtStartOfSuperTieBreak)
            return false;
        if (onServePlayerTeamBNow != other.onServePlayerTeamBNow)
            return false;
        if (onServeSideNow != other.onServeSideNow)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        return true;
    }



}
