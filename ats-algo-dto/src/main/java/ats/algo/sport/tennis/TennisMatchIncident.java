package ats.algo.sport.tennis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.GenderId;
import ats.algo.core.common.TeamId;
import ats.core.util.json.JsonUtil;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TennisMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;

    public enum TennisMatchIncidentType {
        MATCH_STARTING,
        POINT_WON,
        NEW_SET_STARTING,
        INJURY_STATUS_REVERSE,
        FAULT,
        SERVING_ORDER,
        IPTL_POWERPOINT,
        PENALTY_POINT_WON,
        PENALTY_GAME_WON,
        PENALTY_MATCH_WON,
        POINT_START,
        CHALLENGER_BALLMARK,
        MATCH_END,


        RAIN, // 60
        TOILET_BREAK, // 20
        HEAT_DELAY, // 30
        MEDICAL_TIMEOUT, // 15
        ON_COURT_COACHING, // 10
        CHALLENGE// N/A
    }

    public enum TennisPointResult {
        ACE,
        DOUBLE_FAULT,
        RALLY,
        UNKNOWN,
    }

    private TennisMatchIncidentType incidentSubType;
    private TeamId serverSideAtStartOfMatch;
    private int serverPlayerAtStartOfMatch;
    private TeamId serverSideAtStartOfCurrentGame;
    private int serverPlayerAtStartOfCurrentGame;
    private TeamId pointWinner;
    private GenderId genderId;
    private TennisPointResult pointResult;

    /**
     * constructor without params is used to create an instance of the class which is then updated many times - avoids
     * creating a new instance every iteration of the monte carlo model
     */
    public TennisMatchIncident() {
        super();
    }

    /**
     * Constructor with params can be used to save a line of code when not worried about performance overhead of
     * instantiating new object for every incident
     * 
     * @param elapsedTime time in seconds since start of match
     * @param matchIncidentType
     * @param playerId if incidentType is MATCH_STARTING then the id of the first player to serve in the match. If
     *        incident type is POINT_WON then the id of the player winning the point
     */
    public TennisMatchIncident(int elapsedTime, TennisMatchIncidentType incidentSubType, TeamId teamId, int playerId) {
        setTennisMatchIncident(elapsedTime, incidentSubType, teamId, playerId, null);
    }

    public TennisMatchIncident(int elapsedTime, TennisMatchIncidentType incidentSubType, TeamId teamId, int playerId,
                    GenderId gender) {
        setTennisMatchIncident(elapsedTime, incidentSubType, teamId, playerId, gender);
    }

    public TennisMatchIncident(int elapsedTime, TennisMatchIncidentType matchIncidentType, TeamId teamId) {
        setTennisMatchIncident(elapsedTime, matchIncidentType, teamId, 1, null);
    }

    /**
     * 
     * @param elapsedTime time in seconds since start of match
     * @param matchIncidentType
     * @param teamId if incidentType is MATCH_STARTING then the id of the first player to serve in the match. If
     *        incident type is POINT_WON then the id of the player winning the point.
     */
    public void setTennisMatchIncident(int elapsedTime, TennisMatchIncidentType incidentSubType, TeamId teamId,
                    int playerId, GenderId gender) {
        super.setElapsedTime(elapsedTime);
        this.incidentSubType = incidentSubType;
        switch (incidentSubType) {
            case MATCH_STARTING:
                serverSideAtStartOfMatch = teamId;
                serverPlayerAtStartOfMatch = playerId;
                pointWinner = TeamId.UNKNOWN;
                genderId = gender;
                break;
            case SERVING_ORDER:
                serverSideAtStartOfCurrentGame = teamId;
                serverPlayerAtStartOfCurrentGame = playerId;
                pointWinner = TeamId.UNKNOWN;
                genderId = gender;
                break;

            case IPTL_POWERPOINT:
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                pointWinner = teamId;
                break;
            case PENALTY_MATCH_WON:
            case PENALTY_GAME_WON:
            case PENALTY_POINT_WON:
            case POINT_WON:
                // point won incident should not change serverside info
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                pointWinner = teamId;
                break;

            case NEW_SET_STARTING:
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                // serverPlayerAtStartOfMatch = ;
                pointWinner = TeamId.UNKNOWN;
                break;
            case INJURY_STATUS_REVERSE:
                break;
            default:
                break;
        }
    }

    /**
     * 
     * @param elapsedTime time in seconds since start of match
     * @param matchIncidentType
     * @param teamId if incidentType is MATCH_STARTING then the id of the first player to serve in the match. If
     *        incident type is POINT_WON then the id of the player winning the point.
     */
    public void setTennisMatchIncident(int elapsedTime, TennisMatchIncidentType matchIncidentType, TeamId teamId,
                    int playerId) {
        super.setElapsedTime(elapsedTime);
        this.incidentSubType = matchIncidentType;
        switch (matchIncidentType) {
            case MATCH_STARTING:
                serverSideAtStartOfMatch = teamId;
                serverPlayerAtStartOfMatch = playerId;
                pointWinner = TeamId.UNKNOWN;
                // genderId = gender;
                break;
            case SERVING_ORDER:
                serverSideAtStartOfCurrentGame = teamId;
                serverPlayerAtStartOfCurrentGame = playerId;
                pointWinner = TeamId.UNKNOWN;
                // genderId = gender;
                break;

            case IPTL_POWERPOINT:
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                pointWinner = teamId;
                break;
            case PENALTY_MATCH_WON:
            case PENALTY_GAME_WON:
            case PENALTY_POINT_WON:
            case POINT_WON:
                // point won incident should not change serverside info
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                pointWinner = teamId;
                break;

            case NEW_SET_STARTING:
                serverSideAtStartOfMatch = TeamId.UNKNOWN;
                serverPlayerAtStartOfMatch = 0;
                // serverPlayerAtStartOfMatch = ;
                pointWinner = TeamId.UNKNOWN;
                break;
            case INJURY_STATUS_REVERSE:
                break;
            default:
                break;
        }
    }

    @Override
    public TennisMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(TennisMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }


    @Deprecated
    @JsonIgnore
    public TennisMatchIncidentType getMatchIncidentType() {
        return incidentSubType;
    }

    @Deprecated
    @JsonIgnore
    public TennisMatchIncidentType getIncidentType() {
        return incidentSubType;
    }


    public TeamId getPointWinner() {
        return pointWinner;
    }

    public void setPointWinner(TeamId pointWinner) {
        this.pointWinner = pointWinner;
    }

    public TeamId getServerSideAtStartOfMatch() {
        return serverSideAtStartOfMatch;
    }

    public int getServerPlayerAtStartOfMatch() {
        return serverPlayerAtStartOfMatch;
    }

    public void setServerSideAtStartOfMatch(TeamId serverSideAtStartOfMatch) {
        this.serverSideAtStartOfMatch = serverSideAtStartOfMatch;
    }

    public GenderId getGenderId() {
        return genderId;
    }

    public void setGenderId(GenderId genderId) {
        this.genderId = genderId;
    }

    @JsonIgnore
    public TennisMatchStateFromFeed getTennisMatchStateFromFeed() {
        return (TennisMatchStateFromFeed) super.getMatchStateFromFeed();
    }

    @JsonIgnore
    public void setTennisMatchStateFromFeed(TennisMatchStateFromFeed tennisMatchStateFromFeed) {
        super.setMatchStateFromFeed(tennisMatchStateFromFeed);
    }

    public void setServerPlayerAtStartOfMatch(int serverPlayerAtStartOfMatch) {
        this.serverPlayerAtStartOfMatch = serverPlayerAtStartOfMatch;
    }

    public TeamId getServerSideAtStartOfCurrentGame() {
        return serverSideAtStartOfCurrentGame;
    }

    public void setServerSideAtStartOfCurrentGame(TeamId serverSideAtStartOfCurrentGame) {
        this.serverSideAtStartOfCurrentGame = serverSideAtStartOfCurrentGame;
    }

    public int getServerPlayerAtStartOfCurrentGame() {
        return serverPlayerAtStartOfCurrentGame;
    }

    public void setServerPlayerAtStartOfCurrentGame(int serverPlayerAtStartOfCurrentGame) {
        this.serverPlayerAtStartOfCurrentGame = serverPlayerAtStartOfCurrentGame;
    }

    public TennisPointResult getPointResult() {
        return pointResult;
    }

    public void setPointResult(TennisPointResult pointResult) {
        this.pointResult = pointResult;
    }

    public static MatchIncident generateTennisIncidentForMatchStarting(long eventId, TeamId firstServer) {
        TennisMatchIncident matchIncident =
                        new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, firstServer, 0);
        matchIncident.setEventId(eventId);
        return matchIncident;

    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((genderId == null) ? 0 : genderId.hashCode());
        result = prime * result + ((incidentSubType == null) ? 0 : incidentSubType.hashCode());
        result = prime * result + ((pointResult == null) ? 0 : pointResult.hashCode());
        result = prime * result + ((pointWinner == null) ? 0 : pointWinner.hashCode());
        result = prime * result + serverPlayerAtStartOfMatch;
        result = prime * result + ((serverSideAtStartOfMatch == null) ? 0 : serverSideAtStartOfMatch.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TennisMatchIncident other = (TennisMatchIncident) obj;
        if (genderId != other.genderId)
            return false;
        if (incidentSubType != other.incidentSubType)
            return false;
        if (pointResult != other.pointResult)
            return false;
        if (pointWinner != other.pointWinner)
            return false;
        if (serverPlayerAtStartOfMatch != other.serverPlayerAtStartOfMatch)
            return false;
        if (serverSideAtStartOfMatch != other.serverSideAtStartOfMatch)
            return false;
        return true;
    }



}
