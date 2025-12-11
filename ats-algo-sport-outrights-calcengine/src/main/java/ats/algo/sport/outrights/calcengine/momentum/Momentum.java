package ats.algo.sport.outrights.calcengine.momentum;

import ats.algo.genericsupportfunctions.Binomial;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.genericsupportfunctions.Integration;
import ats.algo.sport.outrights.calcengine.core.RatingsFactors;

/**
 * calculates updated ratings for two football teams given the final score.
 * 
 * Requires expGoals = alpha * attackRating + beta *defenseRating + epsilon
 * 
 * and prob (#goals=n) = exp (-expgoals) * expgoals^n / n! (i.e. Poisson)
 * 
 * c.f. paper for details of the mathematics
 * 
 * @author gicha
 *
 */
public class Momentum {

    boolean UPDATE_STD_DEVNS = false;

    private boolean doNotApplyMomentum;
    private double alpha; // alpha in paper
    private double beta; // beta in paper
    private double homeAdjFactor; // epsilon in paper
    private double awayAdjFactor; // epsilon in paper
    private double neutralGroundAdjFactor; // epsilon in paper

    public Momentum(RatingsFactors f) {
        doNotApplyMomentum = f.getRatingsStdDevn() == 0.0;
        this.alpha = f.getAttackRatingFactor();
        this.beta = f.getDefenseRatingFactor();
        this.homeAdjFactor = f.getHomeAdjFactor();
        this.awayAdjFactor = f.getAwayAdjFactor();
        this.neutralGroundAdjFactor = f.getNeutralGroundAdjFactor();
    }

    /**
     * updates the ratings based upon Bayesian inferencing
     * 
     * @param homeTeam
     * @param awayTeam
     * @param homeScore
     * @param awayScore
     * @param onNeutralGround
     */
    public void updateRatings(Rating homeTeam, Rating awayTeam, int homeScore, int awayScore, boolean onNeutralGround) {
        if (doNotApplyMomentum)
            return;
        if (onNeutralGround) {
            updateRatings(homeTeam.getRatingAttack(), awayTeam.getRatingDefense(), homeScore, neutralGroundAdjFactor);
            updateRatings(awayTeam.getRatingAttack(), homeTeam.getRatingDefense(), awayScore, neutralGroundAdjFactor);
        } else {
            updateRatings(homeTeam.getRatingAttack(), awayTeam.getRatingDefense(), homeScore, homeAdjFactor);
            updateRatings(awayTeam.getRatingAttack(), homeTeam.getRatingDefense(), awayScore, awayAdjFactor);
        }
    }

    private void updateRatings(Gaussian ratingAttack, Gaussian ratingDefense, int n, double epsilon) {
        double a = ratingAttack.getMean();
        double u = ratingAttack.getStdDevn();
        double d = ratingDefense.getMean();
        double v = ratingDefense.getStdDevn();
        double momentsAttack[] = Integration.normalMoments(n + 2, -alpha * u, 1.0);
        double momentsDefense[] = Integration.normalMoments(n + 2, -beta * v, 1.0);
        double xK00 = 0.0;
        double xK01 = 0.0;
        double xK02 = 0.0;
        double xK10 = 0.0;
        double xK20 = 0.0;
        Binomial b = new Binomial(n);
        double gamma = alpha * a + beta * d + epsilon;
        for (int i = 0; i <= n; i++) {
            double factori = b.coeff(n, i) * Math.pow(gamma, n - i);
            for (int j = 0; j <= i; j++) {
                double factorij = factori * b.coeff(i, j) * Math.pow(alpha * u, j) * Math.pow(beta * v, i - j);
                xK00 += factorij * momentsAttack[j] * momentsDefense[i - j];
                xK10 += factorij * momentsAttack[j + 1] * momentsDefense[i - j];
                xK20 += factorij * momentsAttack[j + 2] * momentsDefense[i - j];
                xK01 += factorij * momentsAttack[j] * momentsDefense[i - j + 1];
                xK02 += factorij * momentsAttack[j] * momentsDefense[i - j + 2];
            }
        }
        double xI10 = (u * xK10 + a * xK00) / xK00;
        double xI20 = (u * u * xK20 + 2 * u * a * xK10 + a * a * xK00) / xK00;
        double xI01 = (v * xK01 + d * xK00) / xK00;
        double xI02 = (v * v * xK02 + 2 * v * d * xK01 + d * d * xK00) / xK00;

        double aNew = xI10;
        double dNew = xI01;
        double uNew;
        double vNew;
        if (UPDATE_STD_DEVNS) {
            uNew = Math.sqrt(xI20 - xI10 * xI10);
            vNew = Math.sqrt(xI02 - xI01 * xI01);
        } else {
            uNew = u;
            vNew = v;
        }

        if (Double.isNaN(uNew) || Double.isNaN(vNew))
            throw new IllegalArgumentException("variance is negative");

        /*
         ************** For debug only. comment out for normal use
         */
        // printRatings(ratingAttack, ratingDefense, n, gamma, aNew, uNew, dNew, vNew);
        /*
         * **************end for debug
         */
        ratingAttack.setProperties(aNew, uNew, 0);
        ratingDefense.setProperties(dNew, vNew, 0);
    }

    @SuppressWarnings("unused")
    private void printRatings(Gaussian ratingAttack, Gaussian ratingDefense, int nGoals, double expGoals, double aNew,
                    double uNew, double dNew, double vNew) {
        System.out.printf(
                        "expGoals: %.1f, actualGoals %d. Attack a: %.3f -> %.3f, u: %.3f -> %.3f; Defense d: %.3f -> %.3f, v: %.3f -> %.3f\n",
                        expGoals, nGoals, ratingAttack.getMean(), aNew, ratingAttack.getStdDevn(), uNew,
                        ratingDefense.getMean(), dNew, ratingDefense.getStdDevn(), vNew);
    }

}
