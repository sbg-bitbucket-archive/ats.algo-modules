package ats.algo.placefromwin;

import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.genericsupportfunctions.RandomNoGenerator;

public class Race {

    /**
     * holds the entryNo of an entrant still in the race and the associated probability
     * 
     * @author Geoff
     *
     */
    private class Runner {
        public int entryNo;
        public double prob;
    }

    private final double alpha = 7;

    private double[] probWin;
    private double probDnf;
    private int nStarters;

    /// <summary>
    /// initialise the class
    /// </summary>
    /// <param name="probWin">array holding probability that each runner wins
    /// the race</param>
    /// <param name="probFall">prob that any given runner falls/does not
    /// finish</param>
    public Race(double[] probWin, double probFall) {
        nStarters = probWin.length;
        this.probDnf = probFall;
        this.probWin = new double[nStarters];

        //
        // store the probs that runner wins
        //
        double totAdjProb = 0;
        for (int i = 0; i < nStarters; i++) {
            double pDiff = probWin[i] - 1 / nStarters;
            this.probWin[i] = probWin[i] * (1 + alpha * probFall * pDiff * pDiff);
            // this.probWin[i] = probWin[i];
            totAdjProb += probWin[i];
        }
        for (int i = 0; i < nStarters; i++)
            this.probWin[i] = this.probWin[i] / totAdjProb;
    }

    /**
     * runs an instance of the race
     * 
     * @return array holding the finishing positions of each runner. 0 means did not finish
     */
    public int[] run() {

        int[] results = new int[nStarters];
        //
        // find out which horses fall and remove from the list of runners
        //
        int nFinishers = 0;
        double totProb = 0;
        boolean[] dnf = new boolean[nStarters];
        for (int i = 0; i < nStarters; i++) {
            double r = RandomNoGenerator.nextDouble();
            dnf[i] = ((r < probDnf) || (probWin[i] == 0));
            // dnf[i] = (i == 2 || i == 3); //for debug only
            if (dnf[i]) {
                results[i] = 0; // this runner did not finish
            } else {
                nFinishers++;
                totProb += probWin[i];
            }
        }
        //
        // calculate the adjusted list of runners, taking into account those
        // runners that have fallen
        //
        if (nFinishers == 0)
            return results;
        Runner[] runners = new Runner[nFinishers];
        int offset = 0;
        for (int i = 0; i < nFinishers; i++) {
            runners[i] = new Runner();
            while (dnf[i + offset]) // skip over those that fell
                offset++;
            runners[i].entryNo = i + offset;
            runners[i].prob = probWin[i + offset] / totProb;
        }

        //
        // determine the finishing order for the runners that finished
        //
        for (int place = 1; place <= nFinishers; place++) {
            int i = selectWinner(runners);
            int entryNo = runners[i].entryNo;
            results[entryNo] = place;
            runners = removeEntrant(runners, i);
        }
        return results;
    }


    /**
     * removes the i'th entrant from the array of runners and renormalises the probabilities
     * 
     * @param r array of runners
     * @param i index of runner to remove
     * @return
     */
    private Runner[] removeEntrant(Runner[] r, int i) {
        int n = r.length;
        double factor = 1 / (1 - r[i].prob);
        Runner[] opR = new Runner[n - 1];
        int offset = 0;
        for (int j = 0; j < n - 1; j++) {
            opR[j] = new Runner();
            if (j == i)
                offset = 1;
            opR[j].entryNo = r[j + offset].entryNo;
            opR[j].prob = r[j + offset].prob * factor;
        }
        return opR;
    }

    /**
     * selects the winner of a race based on the supplied list of runners
     * 
     * @param runners array of Runners holding index and prob of each entrant
     * @return index of the winning entrant
     */
    private int selectWinner(Runner[] runners) {
        int n = runners.length;
        double[] pTarget = new double[n];
        double cumTotal = 0;
        for (int i = 0; i < n; i++) {
            cumTotal += runners[i].prob;
            pTarget[i] = cumTotal;
        }
        pTarget[n - 1] = 1; // ensure that rounding errors don't introduce the
                            // ocassional error - e.g. a rand no generated
                            // between .999999981 and 1
        if (GCMath.round(cumTotal, 6) != 1)
            throw new IllegalArgumentException("selectWinner: probs don't sum to 1");
        double r = RandomNoGenerator.nextDouble();
        for (int i = 0; i < n; i++) {
            if (r < pTarget[i])
                return i;
        }
        throw new IllegalArgumentException("selectWinner: probs don't sum to 1");
    }
}
