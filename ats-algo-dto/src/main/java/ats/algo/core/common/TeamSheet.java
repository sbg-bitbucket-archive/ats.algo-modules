package ats.algo.core.common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.core.util.json.JsonUtil;

public class TeamSheet implements Serializable {

    private static final long serialVersionUID = 1L;
    private LinkedHashMap<String, PlayerInfo> teamSheetMap;

    public TeamSheet() {
        teamSheetMap = new LinkedHashMap<String, PlayerInfo>();
    }

    public LinkedHashMap<String, PlayerInfo> getTeamSheetMap() {
        return teamSheetMap;
    }

    public void setTeamSheetMap(LinkedHashMap<String, PlayerInfo> teamSheet) {
        this.teamSheetMap = teamSheet;
    }

    public static TeamSheet generateDefaultTeamSheet() {
        TeamSheet teamSheet = new TeamSheet();
        addDefaultPlayers(teamSheet, TeamId.A);
        addDefaultPlayers(teamSheet, TeamId.B);
        return teamSheet;
    }

    private static void addDefaultPlayers(TeamSheet teamSheet, TeamId teamId) {
        Map<String, PlayerInfo> teamSheetMap = teamSheet.getTeamSheetMap();
        for (long i = 1; i <= 17; i++) {
            PlayerStatus status = (i <= 11) ? PlayerStatus.PLAYING : PlayerStatus.ON_THE_BENCH;
            String playerName = teamId.toString() + "." + "Player" + i;
            teamSheetMap.put(playerName, new PlayerInfo(teamId, playerName, status));
        }
    }

    public void addPlayer(TeamId teamId, String playerName, PlayerStatus status) {
        PlayerInfo playerInfo = new PlayerInfo(teamId, playerName, status);
        teamSheetMap.put(playerInfo.getKey(), playerInfo);
    }

    /**
     * gets a map of player status suitable for display
     * 
     * @return
     */
    public LinkedHashMap<String, String> playerStatusMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(teamSheetMap.size());
        for (Entry<String, PlayerInfo> e : teamSheetMap.entrySet()) {
            map.put(e.getKey(), e.getValue().getPlayerStatus().toString());
        }
        return map;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teamSheetMap == null) ? 0 : teamSheetMap.hashCode());
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
        TeamSheet other = (TeamSheet) obj;
        if (teamSheetMap == null) {
            if (other.teamSheetMap != null)
                return false;
        } else if (!teamSheetMap.equals(other.teamSheetMap))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
