package ats.algo.core.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.core.util.json.JsonUtil;

public class PlayerInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private TeamId teamId;
    private String playerName;
    private PlayerStatus playerStatus;

    public PlayerInfo() {

    }

    public PlayerInfo(TeamId teamId, String playerName, PlayerStatus playerStatus) {
        super();
        this.teamId = teamId;
        this.playerName = playerName;
        this.playerStatus = playerStatus;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    @JsonIgnore
    /**
     * get the name to use in player related markets
     * 
     * @return
     */
    public String getSelectionName() {
        return getKey();
    }

    @JsonIgnore
    public String getKey() {
        return teamId.toString() + "." + playerName;
    }



    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
        result = prime * result + ((playerStatus == null) ? 0 : playerStatus.hashCode());
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
        PlayerInfo other = (PlayerInfo) obj;
        if (playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!playerName.equals(other.playerName))
            return false;
        if (playerStatus != other.playerStatus)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }



}
