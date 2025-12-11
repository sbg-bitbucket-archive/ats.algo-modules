package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class VolleyballMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public VolleyballMatchIncidentResult() {
        super();
    }

    public enum VolleyballMatchIncidentResultType {
        PREMATCH,
        SERVE,
        SERVEFINALSET,
        POINTWON,
        SETWON,
        MATCHWON
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private VolleyballMatchIncidentResultType volleyballMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint sets true or false if player A serve the point
     * @param playerAwonPoint sets true or false if player A won the point
     * @param volleyballMatchIncidentResultType sets volleyball match incident result type
     * @param teamId sets teamId
     */
    public VolleyballMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    VolleyballMatchIncidentResultType volleyballMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.volleyballMatchIncidentResultType = volleyballMatchIncidentResultType;
        this.teamId = teamId;
    }

    public VolleyballMatchIncidentResult(VolleyballMatchIncidentResultType volleyballMatchIncidentResultType) {
        super();
        this.volleyballMatchIncidentResultType = volleyballMatchIncidentResultType;
        this.playerAServedPoint = false;
        this.playerAwonPoint = false;
        this.teamId = TeamId.UNKNOWN;
    }

    /**
     * Returns true if play serve point
     * 
     * @return playerAServedPoint
     */
    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    /**
     * 
     * 
     * @param playerAServedPoint to set true if playerA serve Point
     * 
     */
    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    /**
     * Returns true if play won point
     * 
     * @return playerAwonPoint
     */
    public boolean isPlayerAwonPoint() {
        return playerAwonPoint;
    }

    /**
     * 
     * 
     * @param playerAwonPoint to set true if playerA won Point
     * 
     */
    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    /**
     * Returns volleyball match incident result type
     * 
     * @return volleyballMatchIncidentResultType
     */
    public VolleyballMatchIncidentResultType getVolleyballMatchIncidentResultType() {
        return volleyballMatchIncidentResultType;
    }

    /**
     * 
     * 
     * @param volleyballMatchIncidentResultType to set volleyballMatchIncidentResultType
     * 
     */
    public void setVolleyballMatchIncidentResultType(
                    VolleyballMatchIncidentResultType volleyballMatchIncidentResultType) {
        this.volleyballMatchIncidentResultType = volleyballMatchIncidentResultType;
    }

    /**
     * Returns Team Id
     * 
     * @return teamId
     */
    public TeamId getTeamId() {
        return teamId;
    }

    /**
     * 
     * 
     * @param teamId to set team Id
     * 
     */
    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "VolleyballMatchIncidentResult [playerAServedPoint=" + playerAServedPoint
                        + ", volleyballMatchIncidentResultType=" + volleyballMatchIncidentResultType + ", teamId="
                        + teamId + "]";
    }

}
