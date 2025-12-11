package ats.algo.core.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchIncident;

/**
 * immutable container for matchIncidents which give information about a player responsible for a particular event that
 * may happen during a match. Not sport specific. For now only contains GOAL_SCORER but may get added to in future
 * 
 * @author gicha
 *
 */
public class PlayerMatchIncident extends MatchIncident {

    public enum PlayerMatchIncidentType {
        GOAL_SCORER
    }

    private static final long serialVersionUID = 1L;

    private int sequenceNo;
    private PlayerMatchIncidentType incidentSubType;
    private String playerName;

    @JsonCreator
    /**
     * 
     * @param playerMatchIncidentType
     * @param sequenceNo
     */
    public PlayerMatchIncident(@JsonProperty("incidentSubType") PlayerMatchIncidentType incidentSubType,
                    @JsonProperty("sequenceNo") int sequenceNo, @JsonProperty("teamId") TeamId teamId,
                    @JsonProperty("playerName") String playerName) {
        super();
        this.sequenceNo = sequenceNo;
        this.incidentSubType = incidentSubType;
        this.teamId = teamId;
        this.playerName = playerName;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public PlayerMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(PlayerMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    /**
     * 
     * @param sequenceNo
     * @param teamId
     * @param playerName
     * @return
     */
    public static PlayerMatchIncident generateMatchIncidentForGoalScorer(int sequenceNo, TeamId teamId,
                    String playerName) {
        return new PlayerMatchIncident(PlayerMatchIncidentType.GOAL_SCORER, sequenceNo, teamId, playerName);
    }

    @JsonIgnore
    public String getPlayerKey() {
        return teamId.toString() + "." + playerName;
    }

}
