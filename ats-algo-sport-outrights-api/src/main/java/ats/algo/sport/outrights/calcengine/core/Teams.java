package ats.algo.sport.outrights.calcengine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.genericsupportfunctions.GCMath;
import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
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


    public Team getFromSportingIndexName(String name) {
        for (Team team : teams.values()) {
            if (team.getSportingIndexName().equals(name))
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

    /**
     * returns teams ordered in a logical way
     * 
     * @return
     */
    public List<Team> list() {
        List<Team> teamList = new ArrayList<>(teams.size());
        teams.forEach((k, v) -> teamList.add(v));
        teamList.sort((Team a, Team b) -> a.compareTo(b));
        return teamList;
    }

    private static final int RATINGS_DECIMAL_PLACES = 3;

    public void adjustRatings(Map<String, Double> ratingsAdjustments) {
        ratingsAdjustments.forEach((teamId, adjustment) -> {
            Team team = teams.get(teamId);
            team.setRatingAttack(GCMath.round(team.getRatingAttack() + adjustment, RATINGS_DECIMAL_PLACES));
            team.setRatingDefense(GCMath.round(team.getRatingDefense() - adjustment, RATINGS_DECIMAL_PLACES));
        });
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
