package ats.algo.sport.outrights.server.api;

import java.io.Serializable;

public class TeamDataUpdate implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private double points;
    private double ratingOffense;
    private double ratingDefense;
    private int won;
    private int drawn;
    private int lost;
    private int goalDiff;

    public TeamDataUpdate() {}

    /**
     * 
     * @param ratingOffense
     * @param ratingDefense
     * @param won
     * @param drawn
     * @param lost
     * @param goalDiff
     */
    public TeamDataUpdate(double ratingOffense, double ratingDefense, int won, int drawn, int lost, int goalDiff) {
        this.ratingOffense = ratingOffense;
        this.ratingDefense = ratingDefense;
        this.won = won;
        this.drawn = drawn;
        this.lost = lost;
        this.goalDiff = goalDiff;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getRatingOffense() {
        return ratingOffense;
    }

    public void setRatingOffense(double off) {
        this.ratingOffense = off;
    }

    public double getRatingDefense() {
        return ratingDefense;
    }

    public void setRatingDefense(double def) {
        this.ratingDefense = def;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int w) {
        this.won = w;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int d) {
        this.drawn = d;
    }

    public double getLost() {
        return lost;
    }

    public void setLost(int l) {
        this.lost = l;
    }

    public int getGoalDiff() {
        return goalDiff;
    }

    public void setGoalDiff(int goalDiff) {
        this.goalDiff = goalDiff;
    }

    public String toString() {
        return " ratingOffense " + this.ratingOffense + " ratingDefense " + this.ratingDefense;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + drawn;
        result = prime * result + goalDiff;
        result = prime * result + lost;
        long temp;
        temp = Double.doubleToLongBits(points);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratingDefense);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratingOffense);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        TeamDataUpdate other = (TeamDataUpdate) obj;
        if (drawn != other.drawn)
            return false;
        if (goalDiff != other.goalDiff)
            return false;
        if (lost != other.lost)
            return false;
        if (Double.doubleToLongBits(points) != Double.doubleToLongBits(other.points))
            return false;
        if (Double.doubleToLongBits(ratingDefense) != Double.doubleToLongBits(other.ratingDefense))
            return false;
        if (Double.doubleToLongBits(ratingOffense) != Double.doubleToLongBits(other.ratingOffense))
            return false;
        if (won != other.won)
            return false;
        return true;
    }



}
