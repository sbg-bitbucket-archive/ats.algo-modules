package ats.algo.placefromwin;

public class FinishPosnProbs {

    private Race race;
    private int nStarters;

    /*
     * @param probWin array giving the prob that each runner wins
     * 
     * @param probFall prob that any runner falls
     */
    public FinishPosnProbs(double[] probWin, double probFall) {
        race = new Race(probWin, probFall);
        nStarters = probWin.length;
    }

    /**
     * runs the monte carlo simulation
     * 
     * @param nIterations no if iterations to run
     * @return 2 d array placeProbs(i,j) is prob that the i'th runner finishes in j'th place. j = 0 means dn
     */
    public double[][] calc(int nIterations) {
        int[][] placeCount = new int[nStarters][nStarters + 1];
        for (int i = 0; i < nIterations; i++) {
            int[] result = race.run();
            for (int j = 0; j < nStarters; j++) {
                int place = result[j];
                placeCount[j][place]++;
            }
        }
        //
        // convert place counts to probs
        //
        double[][] placeProbs = new double[nStarters + 1][nStarters + 1];
        for (int i = 0; i < nStarters; i++)
            for (int j = 0; j < nStarters + 1; j++)
                placeProbs[i][j] = ((double) placeCount[i][j]) / nIterations;
        //
        //
        // add the probs of there being no runner finishing in this posn (e.g. there will be no fourth if a runner fell)
        int[] finisherCount = new int[nStarters + 1];
        for (int i = 0; i < nStarters; i++)
            for (int j = 0; j < nStarters + 1; j++)
                finisherCount[j] += placeCount[i][j];
        for (int j = 0; j < nStarters + 1; j++)
            placeProbs[nStarters][j] = ((double) (nIterations - finisherCount[j])) / nIterations;
        return placeProbs;
    }


}
