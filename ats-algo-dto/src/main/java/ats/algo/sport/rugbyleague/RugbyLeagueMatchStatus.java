package ats.algo.sport.rugbyleague;

import java.io.Serializable;

import ats.algo.core.common.TeamId;

/**
 * container of Rugby League match info about the match status, of which choose from Conversion, Penalty or Normal time
 * 
 * @author Jin
 *
 */
public class RugbyLeagueMatchStatus implements Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RugbyLeagueMatchStatus clone = (RugbyLeagueMatchStatus) super.clone();
        return clone;
    }

    RugbyLeagueMatchStatusType rugbyLeagueMatchStatus;
    TeamId teamId;

    /**
     * constructor of RugbyLeagueMatchStatus class
     */
    public RugbyLeagueMatchStatus() {
        rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
    }

    /**
     * gets current match status
     * 
     * @return
     */
    public RugbyLeagueMatchStatusType getRugbyLeagueMatchStatus() {
        if (rugbyLeagueMatchStatus == null) {
            rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
        }
        return rugbyLeagueMatchStatus;
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
     * set match status, inputs are RugbyLeagueMatchStatusType rugbyLeagueMatchStatus and TeamId teamId
     * 
     * @return
     */
    public void setRugbyLeagueMatchStatus(RugbyLeagueMatchStatusType rugbyLeagueMatchStatus, TeamId teamId) {
        this.rugbyLeagueMatchStatus = rugbyLeagueMatchStatus;
        this.teamId = teamId;
    }

    /**
     * rests match status to normal match, and team ID to UNKNOWN
     * 
     * @return
     */
    public void resetRugbyLeagueMatchStatus() {
        rugbyLeagueMatchStatus = RugbyLeagueMatchStatusType.NORMAL;
        teamId = TeamId.UNKNOWN;
    }


}


enum RugbyLeagueMatchStatusType {
    NORMAL,
    PENALTY,
    CONVERSION
}
