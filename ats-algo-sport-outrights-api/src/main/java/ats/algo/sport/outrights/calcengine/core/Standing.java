package ats.algo.sport.outrights.calcengine.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Standing implements Comparable<Standing> {
    private String teamId;
    private int played;
    private int won;
    private int drawn;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    /*
     * these properties are not part of the API - it is not relayed to the GUI via the team object
     */
    private int manualTieBreakAdjustment;

    public Standing() {}

    public Standing(String teamId, int played, int won, int drawn, int goalsFor, int goalsAgainst, int points) {
        super();
        this.teamId = teamId;
        this.played = played;
        this.won = won;
        this.drawn = drawn;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



    @JsonIgnore
    public int getManualTieBreakAdjustment() {
        return manualTieBreakAdjustment;
    }

    @JsonIgnore
    public void setManualTieBreakAdjustment(int manualTieBreakAdjustment) {
        this.manualTieBreakAdjustment = manualTieBreakAdjustment;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int compareTo(Standing other) {
        int ptsDiff = other.getPoints() + -this.getPoints();
        if (ptsDiff != 0)
            return ptsDiff;
        int goalsDiff = (other.getGoalsFor() - other.getGoalsAgainst()) - (this.getGoalsFor() - this.getGoalsAgainst());
        if (goalsDiff != 0)
            return goalsDiff;
        int totalGoalsDiff = other.getGoalsFor() - this.getGoalsFor();
        if (totalGoalsDiff != 0)
            return totalGoalsDiff;
        int manualTieBreakDiff = other.getManualTieBreakAdjustment() - this.getManualTieBreakAdjustment();
        return manualTieBreakDiff;

    }

    public Standing copy() {
        Standing standing = new Standing(teamId, played, won, drawn, goalsFor, goalsAgainst, points);
        return standing;
    }

    public void incrementPoints(int n) {
        points += n;
    }

    public void incrementGoalsFor(int n) {
        goalsFor += n;
    }

    public void incrementGoalsAgainst(int n) {
        goalsAgainst += n;
    }

    public void incrementMatchesPlayed() {
        played++;
    }

    public void incrementMatchesWon() {
        won++;
    }

    public void incrementMatchesDrawn() {
        drawn++;
    }

    @JsonIgnore
    public int getLost() {
        return played - won - drawn;
    }

    @JsonIgnore
    public int getGoalsDiff() {
        return goalsFor - goalsAgainst;
    }

    /**
     * resets any of the counters that may get amended during a simulation run
     * 
     * @param other
     */
    public void setEqualTo(Standing other) {
        this.played = other.played;
        this.won = other.won;
        this.drawn = other.drawn;
        this.goalsFor = other.goalsFor;
        this.goalsAgainst = other.goalsAgainst;
        this.points = other.points;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + drawn;
        result = prime * result + goalsAgainst;
        result = prime * result + goalsFor;
        result = prime * result + played;
        result = prime * result + points;
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        result = prime * result + won;
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
        Standing other = (Standing) obj;
        if (drawn != other.drawn)
            return false;
        if (goalsAgainst != other.goalsAgainst)
            return false;
        if (goalsFor != other.goalsFor)
            return false;
        if (played != other.played)
            return false;
        if (points != other.points)
            return false;
        if (teamId == null) {
            if (other.teamId != null)
                return false;
        } else if (!teamId.equals(other.teamId))
            return false;
        if (won != other.won)
            return false;
        return true;
    }

}
