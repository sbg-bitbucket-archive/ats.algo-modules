package ats.algo.sport.darts;

import java.io.Serializable;

import ats.algo.core.common.TeamId;

/**
 * Holds the state of the currently active leg
 * 
 * @author Geoff
 *
 */
public class LegState implements Serializable {
    private static final long serialVersionUID = 1L;
    public PlayerLegState playerA;
    public PlayerLegState playerB;
    public TeamId first180InLeg;
    public boolean next180InLeg;
    public TeamId playerAtOche;
    public ThreeDartSet threeDartSet;
    public int pointsAtStartOfCurrentThreeDartSet;
    public boolean doubleReqdToStart;

    public LegState() {

    }// required for JSON serialisation

    public LegState(boolean doubleReqdToStart) {
        playerA = new PlayerLegState(TeamId.A, 501, 0, 0);
        playerB = new PlayerLegState(TeamId.B, 501, 0, 0);
        first180InLeg = TeamId.UNKNOWN;
        playerAtOche = TeamId.UNKNOWN;
        next180InLeg = false;
        threeDartSet = new ThreeDartSet();
        pointsAtStartOfCurrentThreeDartSet = 501;
        this.doubleReqdToStart = doubleReqdToStart;
    }

    public PlayerLegState getPlayerA() {
        return playerA;
    }

    public void setPlayerA(PlayerLegState playerA) {
        this.playerA = playerA;
    }

    public PlayerLegState getPlayerB() {
        return playerB;
    }

    public void setPlayerB(PlayerLegState playerB) {
        this.playerB = playerB;
    }

    public TeamId getFirst180InLeg() {
        return first180InLeg;
    }

    public void setFirst180InLeg(TeamId first180InLeg) {
        this.first180InLeg = first180InLeg;
    }

    public TeamId getPlayerAtOche() {
        return playerAtOche;
    }

    public void setPlayerAtOche(TeamId playerAtOche) {
        this.playerAtOche = playerAtOche;
    }

    public ThreeDartSet getThreeDartSet() {
        return threeDartSet;
    }

    public void setThreeDartSet(ThreeDartSet threeDartSet) {
        this.threeDartSet = threeDartSet;
    }

    public int getPointsAtStartOfCurrentThreeDartSet() {
        return pointsAtStartOfCurrentThreeDartSet;
    }

    public void setPointsAtStartOfCurrentThreeDartSet(int pointsAtStartOfCurrentThreeDartSet) {
        this.pointsAtStartOfCurrentThreeDartSet = pointsAtStartOfCurrentThreeDartSet;
    }

    public boolean isDoubleReqdToStart() {
        return doubleReqdToStart;
    }

    public void setDoubleReqdToStart(boolean doubleReqdToStart) {
        this.doubleReqdToStart = doubleReqdToStart;
    }

    /**
     * makes a copy of itself
     * 
     * @return copy of the original
     */
    public LegState copy() {
        LegState c = new LegState(this.doubleReqdToStart);
        c.playerA = this.playerA.copy();
        c.playerB = this.playerB.copy();
        c.next180InLeg = this.next180InLeg;
        c.first180InLeg = this.first180InLeg;
        c.playerAtOche = this.playerAtOche;
        c.threeDartSet = this.threeDartSet.copy();
        c.pointsAtStartOfCurrentThreeDartSet = this.pointsAtStartOfCurrentThreeDartSet;
        return c;
    }

    /**
     * updates the LegScore with the effect of this dart throw.
     * 
     * @param dt
     * @return whether this dart has won leg or resulted in a change of player at Oche
     */
    public LegThrowResult updateScore(DartTarget dt) {
        LegThrowResult throwResult;
        if (playerAtOche == TeamId.A)
            throwResult = processThrow(playerA, dt);
        else
            throwResult = processThrow(playerB, dt);
        return throwResult;
    }

    /**
     * does the grunt work for UpdateScore
     * 
     * @param player
     * @param dt
     * @return whether this dart has won leg or resulted in a change of player at Oche
     */
    LegThrowResult processThrow(PlayerLegState player, DartTarget dt) {
        threeDartSet.noDartsThrown++;
        player.NDartsthrown++;
        if (doubleReqdToStart && player.Points == 501 && dt.multiplier != 2)
            dt.no = 0;

        if (dt.multiplier != 0) {
            player.Points -= dt.multiplier * dt.no;
            threeDartSet.setActual(threeDartSet.noDartsThrown, dt);
        } else {
            if (dt.no == -3) {
                threeDartSet.DartHit[1].multiplier = 0;
                threeDartSet.DartHit[1].no = 0;
                threeDartSet.DartHit[0].multiplier = 0;
                threeDartSet.DartHit[0].no = 0;
                player.Points = pointsAtStartOfCurrentThreeDartSet;
            }
            if (dt.no == -2) {
                player.Points += threeDartSet.DartHit[1].multiplier * threeDartSet.DartHit[1].no;
                threeDartSet.DartHit[1].multiplier = 0;
                threeDartSet.DartHit[1].no = 0;
                dt.no = 0;
                threeDartSet.setActual(threeDartSet.noDartsThrown, dt);
            }
            if (dt.no == -1) {
                player.Points += threeDartSet.DartHit[0].multiplier * threeDartSet.DartHit[0].no;
                threeDartSet.DartHit[0].multiplier = 0;
                threeDartSet.DartHit[0].no = 0;
                dt.no = 0;
                threeDartSet.setActual(threeDartSet.noDartsThrown, dt);
            }
        }
        if ((dt.multiplier == 2) && (player.Points == 0))
            return LegThrowResult.ISLEGWINNINGDART;
        if (player.Points < 2) {
            player.Points = pointsAtStartOfCurrentThreeDartSet;
            changePlayeratOche();
            return LegThrowResult.NEXTDARTFROMOTHERPLAYER;
        }
        if (threeDartSet.noDartsThrown == 3) {
            if (threeDartSet.is180()) {
                player.N180sThrown++;
                if (first180InLeg == TeamId.UNKNOWN && !next180InLeg) {
                    first180InLeg = player.Id;
                    next180InLeg = true;
                }
            }
            changePlayeratOche();
            return LegThrowResult.NEXTDARTFROMOTHERPLAYER;
        } else {

            return LegThrowResult.NEXTDARTFROMTHISPLAYER;
        }
    }

    /**
     * sets up for the start of a new leg
     * 
     * @param playerId the id of the player at the oche
     */
    public void startNewLeg(TeamId playerId) {
        playerA.startNewLeg();
        playerB.startNewLeg();
        playerAtOche = playerId;
        threeDartSet.noDartsThrown = 0;
        pointsAtStartOfCurrentThreeDartSet = 501;
        next180InLeg = false;
        first180InLeg = TeamId.UNKNOWN;
    }

    /**
     * sets the state if starting part way through a leg. Assumes starting at the start of a three dart set.
     * 
     * @param pointsA
     * @param pointsB
     * @param nDartsA
     * @param nDartsB
     * @param n180sA
     * @param n180sB
     * @param playerId - id of player about to throw first of 3 dart set
     */
    public void setPartLegScore(int pointsA, int pointsB, int nDartsA, int nDartsB, int n180sA, int n180sB,
                    TeamId playerId) {
        playerA.setPartScore(pointsA, nDartsA, n180sA);
        playerB.setPartScore(pointsB, nDartsB, n180sB);
        playerA.Points = pointsA;
        playerB.Points = pointsB;
        playerAtOche = playerId;
        threeDartSet.noDartsThrown = 0;
        if (playerId == TeamId.A)
            pointsAtStartOfCurrentThreeDartSet = pointsA;
        else
            pointsAtStartOfCurrentThreeDartSet = pointsB;
    }

    /// <summary>
    /// called at end of each set of three darts to switch player state
    /// </summary>
    private void changePlayeratOche() {
        if (playerAtOche == TeamId.A) {
            playerAtOche = TeamId.B;
            pointsAtStartOfCurrentThreeDartSet = playerB.Points;
        } else {
            playerAtOche = TeamId.A;
            pointsAtStartOfCurrentThreeDartSet = playerA.Points;
        }
        threeDartSet.noDartsThrown = 0;
    }

    /**
     * returns the total score from the current three dart set
     * 
     * @return
     */
    public int currentThreeDartSetScore() {
        if (playerAtOche == TeamId.A)
            return pointsAtStartOfCurrentThreeDartSet - playerA.Points;
        else
            return pointsAtStartOfCurrentThreeDartSet - playerB.Points;
    }

    /**
     * returns the score for the player currently at the Oche
     * 
     * @return
     */
    public int scoreAtOche() {
        if (playerAtOche == TeamId.A)
            return playerA.Points;
        else
            return playerB.Points;
    }

    /**
     * the score for the player currently NOT at the Oche
     * 
     * @return
     */
    public int scoreAtRest() {
        if (playerAtOche == TeamId.A)
            return playerB.Points;
        else
            return playerA.Points;
    }
}
