package ats.algo.sport.outrights.calcengine.momentum;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.core.util.json.JsonUtil;

public class Rating {

    private Gaussian ratingAttack;
    private Gaussian ratingDefense;

    public Rating() {
        super();
    }

    /**
     * 
     * @param ratingAttack
     * @param ratingDefense
     */
    public Rating(Gaussian ratingAttack, Gaussian ratingDefense) {
        this();
        this.ratingAttack = ratingAttack;
        this.ratingDefense = ratingDefense;
    }

    public Rating(double ratingAttack, double ratingDefense, double ratingsStdDevn) {
        this();
        this.ratingAttack = new Gaussian(ratingAttack, ratingsStdDevn);
        this.ratingDefense = new Gaussian(ratingDefense, ratingsStdDevn);
    }

    public Gaussian getRatingAttack() {
        return ratingAttack;
    }

    public void setRatingAttack(Gaussian ratingAttack) {
        this.ratingAttack = ratingAttack;
    }

    public Gaussian getRatingDefense() {
        return ratingDefense;
    }

    public void setRatingDefense(Gaussian ratingDefense) {
        this.ratingDefense = ratingDefense;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public Rating copy() {
        return new Rating(ratingAttack.copy(), ratingDefense.copy());
    }

}
