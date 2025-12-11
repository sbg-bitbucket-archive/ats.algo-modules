package ats.algo.sport.bowls;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class BowlsMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BowlsMatchIncidentResult() {
        super();
    }

    public enum BowlsMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        SERVENOW,
        ENDWON1,
        ENDWON2,
        ENDWON3,
        ENDWON4,
        SETDRAW,
        SETWON,
        MATCHWON,
        EXTRAEND
    }

    private boolean playerAServedEnd;
    private boolean playerAwonEnd;
    private boolean raceToXpoints;
    private BowlsMatchIncidentResultType bowlsMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param bowlsMatchIncidentResultType
     */
    public BowlsMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint, boolean raceToXpoints,
                    BowlsMatchIncidentResultType bowlsMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedEnd = playerAServedPoint;
        this.playerAwonEnd = playerAwonPoint;
        this.raceToXpoints = raceToXpoints;
        this.bowlsMatchIncidentResultType = bowlsMatchIncidentResultType;
        this.teamId = teamId;
    }

    public BowlsMatchIncidentResult(BowlsMatchIncidentResultType bowlsMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedEnd = false;
        this.playerAwonEnd = false;
        this.raceToXpoints = false;
        this.bowlsMatchIncidentResultType = bowlsMatchIncidentResultType;
        this.teamId = teamId;
    }

    public boolean isRaceToXpoints() {
        return raceToXpoints;
    }

    public void setRaceToXpoints(boolean raceToXpoints) {
        this.raceToXpoints = raceToXpoints;
    }

    public BowlsMatchIncidentResult(BowlsMatchIncidentResultType bowlsMatchIncidentResultType) {
        this.bowlsMatchIncidentResultType = bowlsMatchIncidentResultType;
        this.teamId = TeamId.UNKNOWN;
        this.playerAServedEnd = false;
        this.playerAwonEnd = false;
        this.raceToXpoints = false;
    }

    public boolean isPlayerAServedEnd() {
        return playerAServedEnd;
    }

    public void setPlayerAServedEnd(boolean playerAServedPoint) {
        this.playerAServedEnd = playerAServedPoint;
    }

    public boolean isPlayerAwonEnd() {
        return playerAwonEnd;
    }

    public void setPlayerAwonEnd(boolean playerAwonPoint) {
        this.playerAwonEnd = playerAwonPoint;
    }

    public void setBowlsMatchIncidentResultType(BowlsMatchIncidentResultType bowlsMatchIncidentResultType) {
        this.bowlsMatchIncidentResultType = bowlsMatchIncidentResultType;
    }

    public BowlsMatchIncidentResultType getBowlsMatchIncidentResultType() {
        return bowlsMatchIncidentResultType;
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
        result = prime * result
                        + ((bowlsMatchIncidentResultType == null) ? 0 : bowlsMatchIncidentResultType.hashCode());
        result = prime * result + (playerAServedEnd ? 1231 : 1237);
        result = prime * result + (playerAwonEnd ? 1231 : 1237);
        result = prime * result + (raceToXpoints ? 1231 : 1237);
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
        BowlsMatchIncidentResult other = (BowlsMatchIncidentResult) obj;
        if (bowlsMatchIncidentResultType != other.bowlsMatchIncidentResultType)
            return false;
        if (playerAServedEnd != other.playerAServedEnd)
            return false;
        if (playerAwonEnd != other.playerAwonEnd)
            return false;
        if (raceToXpoints != other.raceToXpoints)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

}
