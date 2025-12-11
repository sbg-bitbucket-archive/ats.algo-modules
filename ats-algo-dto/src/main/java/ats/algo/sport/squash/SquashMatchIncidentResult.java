package ats.algo.sport.squash;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class SquashMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum SquashMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        POINTWON,
        GAMEWON,
        MATCHWON
    }

    public SquashMatchIncidentResult() {
        super();
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private SquashMatchIncidentResultType squashMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tennisMatchIncidentResultType
     */
    public SquashMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    SquashMatchIncidentResultType squashMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.squashMatchIncidentResultType = squashMatchIncidentResultType;
        this.teamId = teamId;
    }

    public SquashMatchIncidentResult(SquashMatchIncidentResultType squashMatchIncidentResultType) {
        this.squashMatchIncidentResultType = squashMatchIncidentResultType;
    }

    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public boolean isPlayerAwonPoint() {
        return playerAwonPoint;
    }

    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    public void setSquashMatchIncidentResultType(SquashMatchIncidentResultType squashMatchIncidentResultType) {
        this.squashMatchIncidentResultType = squashMatchIncidentResultType;
    }

    public SquashMatchIncidentResultType getSquashMatchIncidentResultType() {
        return squashMatchIncidentResultType;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (playerAServedPoint ? 1231 : 1237);
        result = prime * result + (playerAwonPoint ? 1231 : 1237);
        result = prime * result
                        + ((squashMatchIncidentResultType == null) ? 0 : squashMatchIncidentResultType.hashCode());
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
        SquashMatchIncidentResult other = (SquashMatchIncidentResult) obj;
        if (playerAServedPoint != other.playerAServedPoint)
            return false;
        if (playerAwonPoint != other.playerAwonPoint)
            return false;
        if (squashMatchIncidentResultType != other.squashMatchIncidentResultType)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
