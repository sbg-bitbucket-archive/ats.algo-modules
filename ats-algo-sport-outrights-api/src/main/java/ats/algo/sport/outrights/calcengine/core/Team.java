package ats.algo.sport.outrights.calcengine.core;

import ats.core.util.json.JsonUtil;

public class Team {
    private String teamID;
    private String displayName;
    private String fiveThirtyEightName;
    private double ratingAttack;
    private double ratingDefense;

    public Team() {}

    /**
     * 
     * @param teamID
     * @param name
     * @param fiveThirtyEightName
     * @param ratingAttack
     * @param ratingDefense
     */
    public Team(String teamID, String name, String fiveThirtyEightName, double ratingAttack, double ratingDefense) {
        super();
        this.teamID = teamID;
        this.displayName = name;
        this.fiveThirtyEightName = fiveThirtyEightName;
        this.ratingAttack = ratingAttack;
        this.ratingDefense = ratingDefense;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    /**
     * the name used in market selections
     * 
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public double getRatingAttack() {
        return ratingAttack;
    }

    public void setRatingAttack(double attackRating) {
        this.ratingAttack = attackRating;
    }

    public double getRatingDefense() {
        return ratingDefense;
    }

    public void setRatingDefense(double defenseRating) {
        this.ratingDefense = defenseRating;
    }

    public String getFiveThirtyEightName() {
        return fiveThirtyEightName;
    }

    public void setFiveThirtyEightName(String fiveThirtyEightName) {
        this.fiveThirtyEightName = fiveThirtyEightName;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
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
        Team other = (Team) obj;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
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
