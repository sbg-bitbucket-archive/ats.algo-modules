package ats.algo.sport.darts;

import java.io.Serializable;

import ats.algo.core.common.TeamId;

/**
 * Holds the score and accumulated stats for a particular player for the currently active leg
 * 
 * @author Geoff
 *
 */
public class PlayerLegState implements Serializable {
    private static final long serialVersionUID = 1L;
    public TeamId Id;
    public int Points;
    public int NDartsthrown;
    public int N180sThrown;

    public PlayerLegState() {

    }

    public PlayerLegState(TeamId id, int points, int ndartsThrown, int n180sThrown) {
        Id = id;
        Points = points;
        NDartsthrown = ndartsThrown;
        N180sThrown = n180sThrown;
    }

    /**
     * makes a copy of itself
     * 
     * @return the copy
     */
    public PlayerLegState copy() {
        PlayerLegState cc = new PlayerLegState(this.Id, this.Points, this.NDartsthrown, this.N180sThrown);
        return cc;
    }

    /**
     * resets stats for the start of a new leg
     */
    public void startNewLeg() {
        Points = 501;
        NDartsthrown = 0;
        N180sThrown = 0;
    }

    /**
     * sets the player state if starting part way through a leg - usually only for testing
     * 
     * @param points
     * @param nDartsThrown
     * @param n180sThrown
     */
    public void setPartScore(int points, int nDartsThrown, int n180sThrown) {
        Points = points;
        NDartsthrown = nDartsThrown;
        N180sThrown = n180sThrown;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getN180sThrown() {
        return N180sThrown;
    }

    public void setN180sThrown(int n180sThrown) {
        N180sThrown = n180sThrown;
    }

    public int getNDartsthrown() {
        return NDartsthrown;
    }

    public void setNDartsthrown(int nDartsthrown) {
        NDartsthrown = nDartsthrown;
    }


}
