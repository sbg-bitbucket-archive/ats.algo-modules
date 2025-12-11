package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.sport.outrights.calcengine.core.Team;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingsListEntry {
    private String teamID;
    private String fiveThirtyEightName;
    private double ratingAttack;
    private double ratingDefense;


    public RatingsListEntry() {

    }

    public RatingsListEntry(Team team) {
        this.teamID = team.getTeamID();
        this.fiveThirtyEightName = team.getFiveThirtyEightName();
        this.ratingAttack = team.getRatingAttack();
        this.ratingDefense = team.getRatingDefense();
    }

    public String getTeamID() {
        return teamID;
    }

    public String getFiveThirtyEightName() {
        return fiveThirtyEightName;
    }

    public double getRatingAttack() {
        return ratingAttack;
    }

    public double getRatingDefense() {
        return ratingDefense;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public void setFiveThirtyEightName(String fiveThirtyEightName) {
        this.fiveThirtyEightName = fiveThirtyEightName;
    }

    public void setRatingAttack(double ratingAttack) {
        this.ratingAttack = ratingAttack;
    }

    public void setRatingDefense(double ratingDefense) {
        this.ratingDefense = ratingDefense;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fiveThirtyEightName == null) ? 0 : fiveThirtyEightName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(ratingAttack);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratingDefense);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
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
        RatingsListEntry other = (RatingsListEntry) obj;
        if (fiveThirtyEightName == null) {
            if (other.fiveThirtyEightName != null)
                return false;
        } else if (!fiveThirtyEightName.equals(other.fiveThirtyEightName))
            return false;
        if (Double.doubleToLongBits(ratingAttack) != Double.doubleToLongBits(other.ratingAttack))
            return false;
        if (Double.doubleToLongBits(ratingDefense) != Double.doubleToLongBits(other.ratingDefense))
            return false;
        if (teamID == null) {
            if (other.teamID != null)
                return false;
        } else if (!teamID.equals(other.teamID))
            return false;
        return true;
    }



}
