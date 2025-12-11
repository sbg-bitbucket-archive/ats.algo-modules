package ats.algo.sport.outrights.calcengine.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Teams {

    Map<String, Team> teams;

    public Teams() {
        teams = new HashMap<>();
    }

    public Team get(String id) {
        return teams.get(id);
    }

    public int size() {
        return teams.size();
    }

    public Collection<Team> values() {
        return teams.values();
    }

    public void put(String key, Team team) {
        teams.put(key, team);
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, Team> teams) {
        this.teams = teams;
    }

    public Team getFromFiveThirtyEightName(String name) {
        for (Team team : teams.values()) {
            if (team.getFiveThirtyEightName().equals(name))
                return team;
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teams == null) ? 0 : teams.hashCode());
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
        Teams other = (Teams) obj;
        if (teams == null) {
            if (other.teams != null)
                return false;
        } else if (!teams.equals(other.teams))
            return false;
        return true;
    }

}
