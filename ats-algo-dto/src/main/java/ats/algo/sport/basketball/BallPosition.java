package ats.algo.sport.basketball;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.basketball.BasketballMatchIncident.FieldPositionType;

/**
 * BallPosition class for Basketball
 * 
 * @author Jin
 * 
 */
public class BallPosition implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private FieldPositionType fieldPositionType;
    private TeamId ballHoldingTeam = TeamId.UNKNOWN;

    /**
     * Class constructor. Not for general use
     */
    public BallPosition() {
        this.fieldPositionType = FieldPositionType.UNKNOWN;
        this.ballHoldingTeam = TeamId.UNKNOWN;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BallPosition clone = (BallPosition) super.clone();
        return clone;
    }

    /**
     * Get the position of the ball on the field
     * 
     * @return fieldPositionType
     */
    public FieldPositionType getFieldPositionType() {
        return fieldPositionType;
    }

    /**
     * Set the position of the ball on the field
     * 
     * @param fieldPositionType
     */
    public void setFieldPositionType(FieldPositionType fieldPositionType) {
        this.fieldPositionType = fieldPositionType;
    }

    /**
     * Get the ball holding team ID
     * 
     * @return fieldPositionType
     */
    public TeamId getBallHoldingTeam() {
        return ballHoldingTeam;
    }

    /**
     * Set the ball holding team ID
     * 
     * @param ballHoldingTeam
     */
    public void setBallHoldingTeam(TeamId ballHoldingTeam) {
        this.ballHoldingTeam = ballHoldingTeam;
    }

    /**
     * Reset the ball position info
     * 
     * @param ballHoldingTeam,fieldPositionType
     */
    public void resetBallPosition() {
        ballHoldingTeam = TeamId.UNKNOWN;
        fieldPositionType = FieldPositionType.UNKNOWN;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ballHoldingTeam == null) ? 0 : ballHoldingTeam.hashCode());
        result = prime * result + ((fieldPositionType == null) ? 0 : fieldPositionType.hashCode());
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
        BallPosition other = (BallPosition) obj;
        if (ballHoldingTeam != other.ballHoldingTeam)
            return false;
        if (fieldPositionType != other.fieldPositionType)
            return false;
        return true;
    }
}
