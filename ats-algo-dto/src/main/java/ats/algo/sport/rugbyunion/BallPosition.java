package ats.algo.sport.rugbyunion;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.rugbyunion.RugbyUnionMatchIncident.FieldPositionType;

public class BallPosition implements Cloneable, Serializable {
    /**
     * container for indicating which team is in possession and where the ball is on the field
     * 
     * @author Jin
     *
     */
    private static final long serialVersionUID = 1L;
    TeamId ballHoldingNow;
    FieldPositionType fieldPosition;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BallPosition clone = (BallPosition) super.clone();
        return clone;
    }

    // Possibly adding yard info
    public BallPosition() {}

    /**
     * rest to default values - unknown field position and possession
     */
    public void resetBallPosition() {
        ballHoldingNow = TeamId.UNKNOWN;
        fieldPosition = FieldPositionType.UNKNOWN;
    }

    /**
     * gets the field position
     * 
     * @return
     */
    public FieldPositionType getFieldPosition() {
        if (fieldPosition == null) {
            fieldPosition = FieldPositionType.UNKNOWN;
        }
        return fieldPosition;
    }

    /**
     * sets the field position
     * 
     * @param fieldPosition
     */
    public void setFieldPosition(FieldPositionType fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    /**
     * sets the team in possession
     * 
     * @param team
     */
    public void setBallHoldingNow(TeamId team) {
        this.ballHoldingNow = team;
    }

    /**
     * gets the team in possession
     * 
     * @return
     */
    public TeamId getBallHoldingNow() {
        if (ballHoldingNow == null) {
            ballHoldingNow = TeamId.UNKNOWN;
        }

        return ballHoldingNow;
    }

    /**
     * switch the team in possession
     */
    public void alterBallHoldingNow() {
        switch (ballHoldingNow) {
            case A:
                this.ballHoldingNow = TeamId.B;
                break;
            case B:
                this.ballHoldingNow = TeamId.A;
                break;
            default:
                throw new IllegalArgumentException("No current ball position info");
        }

    }



}
