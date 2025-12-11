package ats.algo.sport.americanfootball;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.sport.americanfootball.AmericanfootballMatchState.AmericanfootballMatchStatusType;

/**
 * This class specially for american football indicating if in conversion status
 * 
 **/
public class AmericanfootballMatchStatus implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AmericanfootballMatchStatus clone = (AmericanfootballMatchStatus) super.clone();
        return clone;
    }

    AmericanfootballMatchStatusType americanfootballMatchStatusType;
    TeamId teamId;

    public AmericanfootballMatchStatus() {
        americanfootballMatchStatusType = AmericanfootballMatchStatusType.NORMAL;
        teamId = TeamId.UNKNOWN;
    }

    public AmericanfootballMatchStatusType getAmericanfootballMatchStatusType() {
        if (this.americanfootballMatchStatusType == null) {
            this.americanfootballMatchStatusType = AmericanfootballMatchStatusType.NORMAL;
        }
        return americanfootballMatchStatusType;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setAmericanfootballMatchStatus(AmericanfootballMatchStatusType americanfootballMatchStatus,
                    TeamId teamId) {
        this.americanfootballMatchStatusType = americanfootballMatchStatus;
        this.teamId = teamId;
    }

    public void resetAmericanfootballMatchStatus() {
        americanfootballMatchStatusType = AmericanfootballMatchStatusType.NORMAL;
        teamId = TeamId.UNKNOWN;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                        + ((americanfootballMatchStatusType == null) ? 0 : americanfootballMatchStatusType.hashCode());
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        AmericanfootballMatchStatus other = (AmericanfootballMatchStatus) obj;
        if (americanfootballMatchStatusType != other.americanfootballMatchStatusType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }



}
