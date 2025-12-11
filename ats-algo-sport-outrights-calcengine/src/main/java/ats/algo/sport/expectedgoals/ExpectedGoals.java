package ats.algo.sport.expectedgoals;

import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.genericsupportfunctions.Minimiser;
import ats.algo.genericsupportfunctions.Minimiser.MinimiserResults;
import ats.algo.genericsupportfunctions.PairOfDoubles;
import ats.algo.margining.Margining;
import ats.algo.sport.outrights.calcengine.core.FullMatchProbs;
import ats.core.AtsBean;

public class ExpectedGoals extends AtsBean {

    /**
     * calculates the expected home and away goals given the match odds
     * 
     * @param priceHome
     * @param priceAway
     * @param priceDraw
     * @return
     */
    public PairOfDoubles calcExpectedGoals(double priceHome, double priceAway, double priceDraw) {
        double[] prices = new double[3];
        prices[0] = priceHome;
        prices[1] = priceAway;
        prices[2] = priceDraw;
        double totalMargin = 1 / priceHome + 1 / priceAway + 1 / priceDraw - 1;
        Margining margining = new Margining();
        double[] probs = margining.removeMargin(prices, 1.0);
        targetProbHome = probs[0];
        targetProbAway = probs[1];
        targetProbDraw = probs[2];

        Minimiser minimiser = new Minimiser();
        // minimiser.setDetailedLogRequired(true);
        // minimiser.setLogIterations(true);
        double[] startingPoint = {3.0, 1.0};
        MinimiserResults results = minimiser.minimise(startingPoint, 0.5, 0.0005, expGoals -> calcFit(expGoals));
        // System.out.println(results);
        PairOfDoubles expGoals = null;
        if (results.success) {
            expGoals = new PairOfDoubles();
            expGoals.A = GCMath.round(results.bestX[0], 1);
            expGoals.B = GCMath.round(results.bestX[1], 1);
            // info("priceHome: %.2f, priceAway: %.2f, priceDraw: %.2f, totalMargin: %.4f, expGoalsHome: %.1f,
            // expGoalsAway: %.1f",
            // priceHome, priceAway, priceDraw, totalMargin, expGoals.A, expGoals.B);
        } else {
            error("priceHome: %.2f, priceAway: %.2f, priceDraw: %.2f, , totalMargin: %.4f, fnValAtMinimum: %.4f, nIterations, %d",
                            priceHome, priceAway, priceDraw, totalMargin, results.functionValueAtMinimum,
                            results.nIterations);
        }
        return expGoals;
    }

    private double targetProbHome;
    private double targetProbAway;
    private double targetProbDraw;

    private double calcFit(double[] expGoals) {
        FullMatchProbs matchProbs = new FullMatchProbs(expGoals[0], expGoals[1]);
        double errHome = matchProbs.getProbHomeWin() - targetProbHome;
        double errAway = matchProbs.getProbAwayWin() - targetProbAway;
        double errDraw = matchProbs.getProbDraw() - targetProbDraw;
        return Math.sqrt(errHome * errHome + errAway * errAway + errDraw * errDraw);
    }

}
