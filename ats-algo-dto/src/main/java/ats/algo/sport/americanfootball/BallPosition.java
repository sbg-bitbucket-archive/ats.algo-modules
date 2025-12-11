package ats.algo.sport.americanfootball;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.americanfootball.AmericanfootballMatchIncident.FieldPositionType;

public class BallPosition implements Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    TeamId ballHoldingNow;
    FieldPositionType fieldPosition;
    FieldPositionType anchorFieldPosition;
    int downsRemain;


    @Override
    protected Object clone() throws CloneNotSupportedException {
        BallPosition clone = (BallPosition) super.clone();
        return clone;
    }

    // Possibly adding yard info
    public BallPosition() {
        ballHoldingNow = TeamId.UNKNOWN;
        fieldPosition = FieldPositionType.UNKNOWN;
        anchorFieldPosition = FieldPositionType.UNKNOWN;
        downsRemain = 0;
    }

    public void resetBallPosition() {
        ballHoldingNow = TeamId.UNKNOWN;
        fieldPosition = FieldPositionType.UNKNOWN;
        downsRemain = 0;
    }

    public void resetFieldPosition() {
        fieldPosition = FieldPositionType.UNKNOWN;
        anchorFieldPosition = FieldPositionType.UNKNOWN;
        ballHoldingNow = TeamId.UNKNOWN;
        downsRemain = 0;
    }

    public FieldPositionType getFieldPosition() {
        if (fieldPosition == null) {
            fieldPosition = FieldPositionType.UNKNOWN;
        }
        return fieldPosition;
    }

    public int getDownsRemain() {
        return downsRemain;
    }

    public void minusDownsNumber() {
        this.downsRemain -= 1;
        if (this.downsRemain == 0)
            this.ballHoldingNow = TeamId.UNKNOWN;
        if (this.downsRemain < 0) {
            throw new IllegalArgumentException("Downs number remain can not be negative");
        }
    }

    public void setDownsRemain(int downsRemain) {
        this.downsRemain = downsRemain;
    }

    public void setFieldPosition(FieldPositionType fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public void setBallHoldingNow(TeamId team) {
        if (team != ballHoldingNow) {
            downsRemain = 4;
            anchorFieldPosition = fieldPosition;
        }
        this.ballHoldingNow = team;
    }

    public FieldPositionType getAnchorFieldPosition() {
        if (anchorFieldPosition == null) {
            anchorFieldPosition = FieldPositionType.UNKNOWN;
        }
        return anchorFieldPosition;
    }

    public TeamId getBallHoldingNow() {
        if (ballHoldingNow == null) {
            ballHoldingNow = TeamId.UNKNOWN;
        }

        return ballHoldingNow;
    }

    public void alterBallHoldingNow() {
        switch (ballHoldingNow) {
            case A:
                this.ballHoldingNow = TeamId.B;
                downsRemain = 4;
                break;
            case B:
                this.ballHoldingNow = TeamId.A;
                downsRemain = 4;
                break;
            default:
                throw new IllegalArgumentException("No current ball position info");
        }

    }

}
