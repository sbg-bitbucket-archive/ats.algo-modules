package ats.algo.sport.outrights.calcengine.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    private String teamID;
    private String displayName;
    private String fiveThirtyEightName;
    private String sportingIndexName;
    private String lsportsName;
    private double ratingAttack;
    private double ratingDefense;
    private double biasAttack;
    private double biasDefense;
    private int manualPointsAdjustment;
    private int manualTieBreakAdjustment;

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
        this.sportingIndexName = null;
        this.ratingAttack = ratingAttack;
        this.ratingDefense = ratingDefense;
        biasAttack = 0.0;
        biasDefense = 0.0;
    }

    /**
     * Constructor for specifying explicitly the name by which it is known on sporting index and lsports
     * 
     * @param teamID
     * @param name
     * @param fiveThirtyEightName
     * @param sportingIndexName
     * @param lsportsName
     * @param ratingAttack
     * @param ratingDefense
     */
    public Team(String teamID, String name, String fiveThirtyEightName, String sportingIndexName, String lsportsName,
                    double ratingAttack, double ratingDefense) {
        this(teamID, name, fiveThirtyEightName, ratingAttack, ratingDefense);
        this.sportingIndexName = sportingIndexName;
        this.lsportsName = lsportsName;
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


    public String getSportingIndexName() {
        return sportingIndexName;
    }

    public void setSportingIndexName(String sportingIndexName) {
        this.sportingIndexName = sportingIndexName;
    }



    public String getLsportsName() {
        return lsportsName;
    }

    public void setLsportsName(String lsportsName) {
        this.lsportsName = lsportsName;
    }

    public double getBiasAttack() {
        return biasAttack;
    }

    public void setBiasAttack(double biasAttack) {
        this.biasAttack = biasAttack;
    }

    public double getBiasDefense() {
        return biasDefense;
    }

    public void setBiasDefense(double biasDefense) {
        this.biasDefense = biasDefense;
    }

    /**
     * checks to see that a proposed bias value is within acceptable bounds
     * 
     * @param bias
     * @return null if ok, else an error msg
     */
    public static String biasAttackOk(double attack, double bias) {
        if (attack + bias <= 0.0)
            return "bias invalid.  Would make attack rating negative";

        return null;
    }

    public static String biasDefenseOk(double defense, double bias) {
        if (defense + bias <= 0.0)
            return "bias invalid.  Would make defense rating negative";
        return null;
    }


    public int getManualPointsAdjustment() {
        return manualPointsAdjustment;
    }

    public void setManualPointsAdjustment(int manualPointsAdjustment) {
        this.manualPointsAdjustment = manualPointsAdjustment;
    }

    public int getManualTieBreakAdjustment() {
        return manualTieBreakAdjustment;
    }

    public void setManualTieBreakAdjustment(int manualTieBreakAdjustment) {
        this.manualTieBreakAdjustment = manualTieBreakAdjustment;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(biasAttack);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(biasDefense);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((fiveThirtyEightName == null) ? 0 : fiveThirtyEightName.hashCode());
        result = prime * result + ((lsportsName == null) ? 0 : lsportsName.hashCode());
        result = prime * result + manualPointsAdjustment;
        result = prime * result + manualTieBreakAdjustment;
        temp = Double.doubleToLongBits(ratingAttack);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratingDefense);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((sportingIndexName == null) ? 0 : sportingIndexName.hashCode());
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
        if (Double.doubleToLongBits(biasAttack) != Double.doubleToLongBits(other.biasAttack))
            return false;
        if (Double.doubleToLongBits(biasDefense) != Double.doubleToLongBits(other.biasDefense))
            return false;
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
        if (lsportsName == null) {
            if (other.lsportsName != null)
                return false;
        } else if (!lsportsName.equals(other.lsportsName))
            return false;
        if (manualPointsAdjustment != other.manualPointsAdjustment)
            return false;
        if (manualTieBreakAdjustment != other.manualTieBreakAdjustment)
            return false;
        if (Double.doubleToLongBits(ratingAttack) != Double.doubleToLongBits(other.ratingAttack))
            return false;
        if (Double.doubleToLongBits(ratingDefense) != Double.doubleToLongBits(other.ratingDefense))
            return false;
        if (sportingIndexName == null) {
            if (other.sportingIndexName != null)
                return false;
        } else if (!sportingIndexName.equals(other.sportingIndexName))
            return false;
        if (teamID == null) {
            if (other.teamID != null)
                return false;
        } else if (!teamID.equals(other.teamID))
            return false;
        return true;
    }

    /**
     * try sorting first on assumption teamId is of the form "Tnn". If that fails use standard string sort. Standard
     * string sort may give undesired result, e.g. "T10" appears before "T2".
     * 
     * @param b
     * @return
     */
    public int compareTo(Team b) {
        try {
            int idA = Integer.parseInt(teamID.substring(1));
            int idB = Integer.parseInt(b.teamID.substring(1));
            return idA - idB;
        } catch (NumberFormatException e) {
            return teamID.compareTo(b.teamID);
        }
    }

}
