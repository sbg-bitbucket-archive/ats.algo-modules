package ats.algo.sport.rugbyunion;

import java.io.Serializable;

import ats.algo.core.common.TeamId;

/**
 * container of Rugby Union match info about the match status, of which choose from Conversion, Penalty or Normal time
 * 
 * @author Jin
 *
 */
public class RugbyUnionMatchStatus implements Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RugbyUnionMatchStatus clone = (RugbyUnionMatchStatus) super.clone();
        return clone;
    }

    RugbyUnionMatchStatusType rugbyunionMatchStatus;
    TeamId teamId;

    /**
     * constructor of RugbyUnionMatchStatus class
     */
    public RugbyUnionMatchStatus() {
        rugbyunionMatchStatus = RugbyUnionMatchStatusType.NORMAL;
    }

    /**
     * gets current match status
     * 
     * @return
     */
    public RugbyUnionMatchStatusType getRugbyUnionMatchStatus() {
        if (rugbyunionMatchStatus == null) {
            rugbyunionMatchStatus = RugbyUnionMatchStatusType.NORMAL;
        }
        return rugbyunionMatchStatus;
    }

    /**
     * gets the team ID for current match status, for example team ID for current conversion
     * 
     * @return
     */
    public TeamId getTeamId() {
        return teamId;
    }

    /**
     * set match status, inputs are RugbyUnionMatchStatusType rugbyunionMatchStatus and TeamId teamId
     * 
     * @return
     */
    public void setRugbyUnionMatchStatus(RugbyUnionMatchStatusType rugbyunionMatchStatus, TeamId teamId) {
        this.rugbyunionMatchStatus = rugbyunionMatchStatus;
        this.teamId = teamId;
    }

    /**
     * rests match status to normal match, and team ID to UNKNOWN
     * 
     * @return
     */
    public void resetRugbyUnionMatchStatus() {
        rugbyunionMatchStatus = RugbyUnionMatchStatusType.NORMAL;
        teamId = TeamId.UNKNOWN;
    }


}


enum RugbyUnionMatchStatusType {
    NORMAL,
    PENALTY,
    CONVERSION
}
