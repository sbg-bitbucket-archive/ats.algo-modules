package ats.algo.sport.outrights.calcengine.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.genericsupportfunctions.GCMath;
import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FcastStanding implements Comparable<FcastStanding> {
    private String teamId;
    private int played;
    private double won;
    private double drawn;
    private double goalsFor;
    private double goalsAgainst;
    private double points;
    private double targetPoints;

    public FcastStanding() {}

    public FcastStanding(String teamId) {
        super();
        this.teamId = teamId;
    }

    public FcastStanding(String teamId, int played, double won, double drawn, double goalsFor, double goalsAgainst,
                    double points) {
        super();
        this.teamId = teamId;
        this.played = played;
        this.won = won;
        this.drawn = drawn;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
        this.targetPoints = points;
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

    public double getWon() {
        return won;
    }

    public void setWon(double won) {
        this.won = won;
    }

    public double getDrawn() {
        return drawn;
    }

    public void setDrawn(double drawn) {
        this.drawn = drawn;
    }

    public double getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(double goalsFor) {
        this.goalsFor = goalsFor;
    }

    public double getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(double goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(double targetPoints) {
        this.targetPoints = targetPoints;
    }

    @JsonIgnore
    public double getLost() {
        return played - won - drawn;
    }

    @JsonIgnore
    public double getGoalsDiff() {
        return goalsFor - goalsAgainst;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public int compareTo(FcastStanding other) {
        double ptsDiff = other.getPoints() - this.getPoints();
        if (ptsDiff != 0.0)
            return (int) (100 * ptsDiff); // multiply by 100 to avoid rounding probs when casting to int
        return (int) (100 * ((other.getGoalsFor() - other.getGoalsAgainst())
                        - (this.getGoalsFor() - this.getGoalsAgainst())));
    }

    public FcastStanding copy() {
        FcastStanding standing = new FcastStanding(teamId, played, won, drawn, goalsFor, goalsAgainst, points);
        standing.setTargetPoints(this.targetPoints);
        return standing;
    }

    public void incrementPoints(double n) {
        points += n;
    }

    public void incrementGoalsFor(double n) {
        goalsFor += n;
    }

    public void incrementGoalsAgainst(double n) {
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

    public static String targetPointsOk(Standing standing, FcastStanding fcastStanding, double targetPoints) {
        int n = fcastStanding.getPlayed() - standing.getPlayed();
        double points = fcastStanding.getPoints();
        return targetPointsOk(n, points, targetPoints);
    }

    public static String targetPointsOk(double n, double point, double targetPoints) {
        double pointsDiff = Math.abs(point - targetPoints);
        /*
         * allow aprox 1.5 std devns from the mean (var =
         */
        double maxAllowedPointsDiff = GCMath.round(Math.sqrt(n) * 2.0, 0);
        if (pointsDiff > maxAllowedPointsDiff)
            return String.format(
                            "proposed targetPoints differs from forecast points by greater than allowed max of %.1f",
                            maxAllowedPointsDiff);
        return null;
    }

    /**
     * resets any of the counters that may get amended during a simulation run
     * 
     * @param other
     */
    public void setEqualTo(FcastStanding other) {
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
        long temp;
        temp = Double.doubleToLongBits(drawn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(goalsAgainst);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(goalsFor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(played);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(points);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        temp = Double.doubleToLongBits(won);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        FcastStanding other = (FcastStanding) obj;
        if (Double.doubleToLongBits(drawn) != Double.doubleToLongBits(other.drawn))
            return false;
        if (Double.doubleToLongBits(goalsAgainst) != Double.doubleToLongBits(other.goalsAgainst))
            return false;
        if (Double.doubleToLongBits(goalsFor) != Double.doubleToLongBits(other.goalsFor))
            return false;
        if (Double.doubleToLongBits(played) != Double.doubleToLongBits(other.played))
            return false;
        if (Double.doubleToLongBits(points) != Double.doubleToLongBits(other.points))
            return false;
        if (teamId == null) {
            if (other.teamId != null)
                return false;
        } else if (!teamId.equals(other.teamId))
            return false;
        if (Double.doubleToLongBits(won) != Double.doubleToLongBits(other.won))
            return false;
        return true;
    }

}
