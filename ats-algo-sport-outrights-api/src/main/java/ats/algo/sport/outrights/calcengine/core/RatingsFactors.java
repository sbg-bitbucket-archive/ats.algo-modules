package ats.algo.sport.outrights.calcengine.core;

/**
 * contains the factors which convert the ratings for each team into expected goals.
 * 
 * @author gicha
 *
 */
public class RatingsFactors {
    private double attackRatingFactor;
    private double defenseRatingFactor;
    private double homeAdjFactor;
    private double awayAdjFactor;
    private double neutralGroundAdjFactor;
    private double ratingsStdDevn;
    private double restDaysFactor;

    public RatingsFactors() {}

    /**
     * 
     * @param attackRatingFactor
     * @param defenseRatingFactor
     * @param homeAdjFactor
     * @param awayAdjFactor
     * @param neutralGroundAdjFactor
     * @param ratingsStdDevn
     */
    public RatingsFactors(double attackRatingFactor, double defenseRatingFactor, double homeAdjFactor,
                    double awayAdjFactor, double neutralGroundAdjFactor, double ratingsStdDevn) {
        super();
        this.attackRatingFactor = attackRatingFactor;
        this.defenseRatingFactor = defenseRatingFactor;
        this.homeAdjFactor = homeAdjFactor;
        this.awayAdjFactor = awayAdjFactor;
        this.neutralGroundAdjFactor = neutralGroundAdjFactor;
        this.ratingsStdDevn = ratingsStdDevn;
        this.restDaysFactor = 0.0;
    }

    public RatingsFactors(double attackRatingFactor, double defenseRatingFactor, double homeAdjFactor,
                    double awayAdjFactor, double neutralGroundAdjFactor, double ratingsStdDevn, double restDaysFactor) {
        this(attackRatingFactor, defenseRatingFactor, homeAdjFactor, awayAdjFactor, neutralGroundAdjFactor,
                        ratingsStdDevn);
        this.restDaysFactor = restDaysFactor;
    }

    public static RatingsFactors defaultFiveThirtyEightRatingsFactors() {
        return new RatingsFactors(1.0, 1.0, 0.323 - 1.618, -1.618, -1.618, 0.0);
        // return new RatingsFactors(1.0, 1.0, 0.3684 - 1.6397, -1.6397, -1.6397, 0.0);
    }

    public double getAttackRatingFactor() {
        return attackRatingFactor;
    }

    public void setAttackRatingFactor(double attackRatingFactor) {
        this.attackRatingFactor = attackRatingFactor;
    }

    public double getDefenseRatingFactor() {
        return defenseRatingFactor;
    }

    public void setDefenseRatingFactor(double defenseRatingFactor) {
        this.defenseRatingFactor = defenseRatingFactor;
    }

    public double getHomeAdjFactor() {
        return homeAdjFactor;
    }

    public void setHomeAdjFactor(double homeAdjFactor) {
        this.homeAdjFactor = homeAdjFactor;
    }

    public double getAwayAdjFactor() {
        return awayAdjFactor;
    }

    public void setAwayAdjFactor(double awayAdjFactor) {
        this.awayAdjFactor = awayAdjFactor;
    }

    public double getRatingsStdDevn() {
        return ratingsStdDevn;
    }

    public void setRatingsStdDevn(double ratingsStdDevn) {
        this.ratingsStdDevn = ratingsStdDevn;
    }

    public double getNeutralGroundAdjFactor() {
        return neutralGroundAdjFactor;
    }

    public void setNeutralGroundAdjFactor(double neutralGroundAdjFactor) {
        this.neutralGroundAdjFactor = neutralGroundAdjFactor;
    }

    public double getRestDaysFactor() {
        return restDaysFactor;
    }

    public void setRestDaysFactor(double restDaysFactor) {
        this.restDaysFactor = restDaysFactor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(attackRatingFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(awayAdjFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(defenseRatingFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(homeAdjFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(neutralGroundAdjFactor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratingsStdDevn);
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
        RatingsFactors other = (RatingsFactors) obj;
        if (Double.doubleToLongBits(attackRatingFactor) != Double.doubleToLongBits(other.attackRatingFactor))
            return false;
        if (Double.doubleToLongBits(awayAdjFactor) != Double.doubleToLongBits(other.awayAdjFactor))
            return false;
        if (Double.doubleToLongBits(defenseRatingFactor) != Double.doubleToLongBits(other.defenseRatingFactor))
            return false;
        if (Double.doubleToLongBits(homeAdjFactor) != Double.doubleToLongBits(other.homeAdjFactor))
            return false;
        if (Double.doubleToLongBits(neutralGroundAdjFactor) != Double.doubleToLongBits(other.neutralGroundAdjFactor))
            return false;
        if (Double.doubleToLongBits(ratingsStdDevn) != Double.doubleToLongBits(other.ratingsStdDevn))
            return false;
        return true;
    }

}
