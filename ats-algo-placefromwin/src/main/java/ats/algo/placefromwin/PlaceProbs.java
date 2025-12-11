package ats.algo.placefromwin;

import ats.algo.genericsupportfunctions.GCMath;

public class PlaceProbs {
    private final int nIterations = 62500;

    FinishPosnProbs finishPosnProbs;
    int nRunners;
    int noToPlace;
    double[] probWin;

    /// <summary>
    /// Class constructor
    /// </summary>
    /// <param name="probWin">array holding probability that each runner wins.
    /// Must sum to 1</param>
    /// <param name="probFall">probability that any given horse falls</param>
    /// <param name="noToPlace">no of places that are winning places</param>
    /**
     * 
     * @param probWin array holding probability that each runner wins. Must sum to 1
     * @param probFall probability that any given horse falls
     * @param noToPlace no of places that are winning places
     */
    public PlaceProbs(double[] probWin, double probFall, int noToPlace) {
        finishPosnProbs = new FinishPosnProbs(probWin, probFall);
        nRunners = probWin.length;
        this.noToPlace = noToPlace;
        double totalProb = 0;
        this.probWin = new double[nRunners];
        for (int i = 0; i < nRunners; i++) {
            this.probWin[i] = probWin[i];
            totalProb += probWin[i];
        }
        if (GCMath.round(totalProb, 6) != 1.0)
            throw new IllegalArgumentException("Win probabilities don't sum to 1");
    }

    /**
     * calculates the "to place" probabilities
     * 
     * @return array holding probabilities that each horse places.
     */
    public double[] calc() {
        double[][] finishProbs;
        finishProbs = finishPosnProbs.calc(nIterations);
        double[] placeProbs = new double[nRunners];
        for (int i = 0; i < nRunners; i++) {
            placeProbs[i] = probWin[i];
            for (int j = 2; j <= noToPlace; j++) {
                placeProbs[i] += finishProbs[i][j];
            }
        }
        return placeProbs;
    }
}
