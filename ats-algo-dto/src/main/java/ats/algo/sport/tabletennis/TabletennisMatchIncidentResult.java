package ats.algo.sport.tabletennis;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

/**
 * 
 * @author Robert
 *
 */
public class TabletennisMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TabletennisMatchIncidentResult() {
        super();
    }

    public enum TabletennisMatchIncidentResultType {
        PREMATCH,
        SERVEFIRST,
        POINTWON,
        GAMEWON,
        MATCHWON
    }

    private boolean playerAServedPoint;
    private boolean playerAwonPoint;
    private TabletennisMatchIncidentResultType tabletennisMatchIncidentResultType;
    private TeamId teamId;

    /**
     * constructor to use when point played
     * 
     * @param playerAServedPoint
     * @param playerAwonPoint
     * @param tabletennisMatchIncidentResultType
     */
    public TabletennisMatchIncidentResult(boolean playerAServedPoint, boolean playerAwonPoint,
                    TabletennisMatchIncidentResultType tabletennisMatchIncidentResultType, TeamId teamId) {
        super();
        this.playerAServedPoint = playerAServedPoint;
        this.playerAwonPoint = playerAwonPoint;
        this.tabletennisMatchIncidentResultType = tabletennisMatchIncidentResultType;
        this.teamId = teamId;
    }

    public TabletennisMatchIncidentResult(TabletennisMatchIncidentResultType tabletennisMatchIncidentResultType) {
        super();
        this.tabletennisMatchIncidentResultType = tabletennisMatchIncidentResultType;
        this.playerAServedPoint = false;
        this.playerAwonPoint = false;
        this.teamId = TeamId.UNKNOWN;
    }

    public boolean isPlayerAServedPoint() {
        return playerAServedPoint;
    }

    public boolean isPlayerAwonPoint() {
        return playerAwonPoint;
    }

    public void setPlayerAServedPoint(boolean playerAServedPoint) {
        this.playerAServedPoint = playerAServedPoint;
    }

    public void setPlayerAwonPoint(boolean playerAwonPoint) {
        this.playerAwonPoint = playerAwonPoint;
    }

    public TabletennisMatchIncidentResultType getTabletennisMatchIncidentResultType() {
        return tabletennisMatchIncidentResultType;
    }

    public void setTabletennisMatchIncidentResultType(
                    TabletennisMatchIncidentResultType tabletennisMatchIncidentResultType) {
        this.tabletennisMatchIncidentResultType = tabletennisMatchIncidentResultType;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

}
