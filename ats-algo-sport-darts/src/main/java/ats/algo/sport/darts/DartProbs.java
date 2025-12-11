package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.sport.darts.DartTarget;

public class DartProbs {
    /**
     * determine which set of base probabilities to use when constructing tables
     * 
     * @author Geoff *
     */
    public enum BaseProbSet {
        UNITTESTSET1,
        UNITTESTSET2,
        LIVE
    }

    enum PosnType {
        straight,
        left,
        right,
        zero,
        bull,
        any
    }

    /**
     * row in the probability lookup table
     * 
     * @author Geoff
     * 
     */
    private class ProbElement {
        public double low;
        public double high;
        public int multiplier;
        public PosnType posn;

        /**
         * sets the element of the prob array
         * 
         * @param l lower limit for rand no
         * @param h upper limit for rand no
         * @param m multiplier - 1,2,3 for single,double,triple
         * @param p posnType
         * 
         */
        public void setProbElement(double l, double h, int m, PosnType p) {
            low = l;
            high = l + h;
            multiplier = m;
            posn = p;
        }
    }

    /*
     * indices into the arrays - first dimension
     */
    public static final int aimedAtBull = 0;
    public static final int aimedAtSingle = 1;
    public static final int aimedAtDouble = 2;
    public static final int aimedAtTriple = 3;
    /*
     * indices into the arrays - second dimension
     */
    private static int hitTriple = 0;
    private static int hitAdjTriple = 1;
    private static int hitDouble = 2;
    private static int hitAdjDouble = 3;
    private static int hitSingle = 4;
    private static int hitAdjSingle = 5;
    private static int hitZero = 6;
    private static int hitInnerBull = 7;
    private static int hitOuterBull = 8;
    private static int hitAnySingle = 9;

    private double[][] baseProbs; // the base set of probabilities that correspond to a skill rating of 1.0
    ProbElement[][] probs; // the adjusted set of probs for this player

    /**
     * Lookup table is used to speed up the probability calculation process by doing integer lookup instead of series of
     * double calcs
     */
    static final int LookupTableSize = 200;
    int[][] probLookupTable;

    /**
     * 
     * @param probSet select the base set of probabilities to be used - LIVE except when unit testing
     * @param playerSkill the set of player skills
     */
    public DartProbs(BaseProbSet probSet, double skill, double triplesVsDoubles) {
        setBaseProbs(probSet);
        probs = new ProbElement[4][];
        probLookupTable = new int[4][LookupTableSize];
        updateProbs(skill, triplesVsDoubles);
    }

    /**
     * sets the probabilities for this player based on the specified skills
     * 
     * @param playerSkill
     */
    public void updateProbs(double skill, double triplesVsDoubles) {
        /*
         * the player skill is modified by the triples vs doubles factor
         */
        double fixedFactorForTriples = 0.5;
        double fixedFactorForDoubles = 1.0;
        double skillAtTriplesAdjustmentFactor = 1 + (triplesVsDoubles - 1) * fixedFactorForTriples;
        double skillAtDoublesAdjustmentFactor = 1 - (triplesVsDoubles - 1) * fixedFactorForDoubles;
        this.setProbs(aimedAtBull, hitInnerBull, skill);
        this.setProbs(aimedAtSingle, hitSingle, skill);
        this.setProbs(aimedAtDouble, hitDouble, skill * skillAtDoublesAdjustmentFactor);
        this.setProbs(aimedAtTriple, hitTriple, skill * skillAtTriplesAdjustmentFactor);
    }

    /**
     * sets the target probability table entries derived from the baseProbs. The base probs are adjusted in two ways: a)
     * "adjacent" is split into left or right, both with equal prob and b) "the item we were aiming at has it's
     * probability adjusted by the skill factor, while all others have their probability adjusted by a factor beta which
     * ensures all probs still sum to one There is one instance of this 13 element table for each type of element that
     * can be aimed at: inner bull (0), single (1), double (2), triple (3) The numbers represent the probability of what
     * the dart might actually hit. e.g. if we are aiming for T20, then we might hit T20,20,1,T1,5,T5 and a probability
     * for each if this is set via this method. It is unlikely that a top class player will hit anything else, so other
     * options will have prob set to 0 Probabilities must sum to 1
     * 
     * @param targetIndex the row in the baseProbs array of the item being aimed at (single,double,triple,bull)
     * @param hitIndex the column in the the baseProbs array of the item being aimed at, which will be adjusted by the
     *        "skill" factor
     * @param skill the skill factor
     * @throws Exception
     */
    private void setProbs(int targetIndex, int hitIndex, double skill) {
        probs[targetIndex] = new ProbElement[13];
        for (int i = 0; i < 13; i++)
            probs[targetIndex][i] = new ProbElement();
        double p = baseProbs[targetIndex][hitIndex];
        //
        // alpha is the factor by which to adjust the target prob; beta the factor by which to adjust all the other
        // probs so they sum to 1
        //
        double alpha, beta;
        //
        // if p=1 then nothing to adjust
        //
        if (p == 1) {
            //
            //
            alpha = 1;
            beta = 0;
        } else {
            //
            // if skill>1 then instead of p' = skill*p, use p' = 1-(1-p)/skill, to ensure p' is <=1
            if (skill > 1)
                alpha = (1 - (1 - p) / skill) / p;
            else
                alpha = skill;
            beta = (1 - p * alpha) / (1 - p);
        }

        double[] tmpProb = new double[10];
        for (int i = 0; i < 10; i++) {
            tmpProb[i] = baseProbs[targetIndex][i];
            if (i == hitIndex)
                tmpProb[i] *= alpha;
            else
                tmpProb[i] *= beta;
        }
        double t = tmpProb[hitTriple];
        double tL = tmpProb[hitAdjTriple] / 2;
        double tR = tmpProb[hitAdjTriple] / 2;
        double d = tmpProb[hitDouble];
        double dL = tmpProb[hitAdjDouble] / 2;
        double dR = tmpProb[hitAdjDouble] / 2;
        double s = tmpProb[hitSingle];
        double sL = tmpProb[hitAdjSingle] / 2;
        double sR = tmpProb[hitAdjSingle] / 2;
        double z = tmpProb[hitZero];
        double ib = tmpProb[hitInnerBull];
        double ob = tmpProb[hitOuterBull];
        double a = tmpProb[hitAnySingle];

        double runningTotal = 0;
        probs[targetIndex][0].setProbElement(runningTotal, t, 3, PosnType.straight);
        runningTotal += t;
        probs[targetIndex][1].setProbElement(runningTotal, tL, 3, PosnType.left);
        runningTotal += tL;
        probs[targetIndex][2].setProbElement(runningTotal, tR, 3, PosnType.right);
        runningTotal += tR;
        probs[targetIndex][3].setProbElement(runningTotal, d, 2, PosnType.straight);
        runningTotal += d;
        probs[targetIndex][4].setProbElement(runningTotal, dL, 2, PosnType.left);
        runningTotal += dL;
        probs[targetIndex][5].setProbElement(runningTotal, dR, 2, PosnType.right);
        runningTotal += dR;
        probs[targetIndex][6].setProbElement(runningTotal, s, 1, PosnType.straight);
        runningTotal += s;
        probs[targetIndex][7].setProbElement(runningTotal, sL, 1, PosnType.left);
        runningTotal += sL;
        probs[targetIndex][8].setProbElement(runningTotal, sR, 1, PosnType.right);
        runningTotal += sR;
        probs[targetIndex][9].setProbElement(runningTotal, z, 0, PosnType.zero);
        runningTotal += z;
        probs[targetIndex][10].setProbElement(runningTotal, ib, 2, PosnType.bull);
        runningTotal += ib;
        probs[targetIndex][11].setProbElement(runningTotal, ob, 1, PosnType.bull);
        runningTotal += ob;
        probs[targetIndex][12].setProbElement(runningTotal, a, 1, PosnType.any);
        runningTotal += a;
        if ((runningTotal < 0.999999) || (runningTotal > 1.000001))
            throw new IllegalArgumentException();

        for (int i = 0; i < 13; i++) {
            int indexLow = (int) (probs[targetIndex][i].low * LookupTableSize);
            int indexHigh = (int) (probs[targetIndex][i].high * LookupTableSize);
            for (int j = indexLow; j < indexHigh; j++)
                probLookupTable[targetIndex][j] = i;
        }

    }

    /**
     * simulates the actual throw of a dart, based on what was aimed at and what is in the probability table
     * 
     * @param aimedAt 0=bull, 1 = single, 2 = double, 3 = triple
     * @param targetNo the number aimed at 1-20,25. If 25 double bull is assumed
     * @return what was actually hit
     */
    public DartTarget getActual(int aimedAt, int targetNo) {
        int arrIndex = aimedAt;
        int rInt = RandomNoGenerator.nextInt(0, LookupTableSize);
        // Console.WriteLine(rInt);
        int i = probLookupTable[arrIndex][rInt];
        int n = 0;
        switch (probs[arrIndex][i].posn) {
            case straight:
                n = targetNo;
                break;
            case left:
                n = DartBoard.leftNo(targetNo);
                break;
            case right:
                n = DartBoard.rightNo(targetNo);
                break;
            case zero:
                n = 0;
                break;
            case bull:
                n = 25;
                break;
            case any:
                n = RandomNoGenerator.nextDartBoardNo();
                break;
        }
        DartTarget dr = new DartTarget(probs[arrIndex][i].multiplier, n);
        return dr;
    }

    /**
     * returns the probability of hitting target aimed at
     * 
     * @param targetType
     * @param actual
     * @return
     */
    public double getProb(int targetType) {
        int index = 0;
        switch (targetType) {
            case aimedAtBull:
                index = 10;
                break;
            case aimedAtSingle:
                index = 6;
                break;
            case aimedAtDouble:
                index = 3;
                break;
            case aimedAtTriple:
                index = 0;
                break;
        }
        ProbElement p = probs[targetType][index];
        return p.high - p.low;
    }

    private void setBaseProbs(BaseProbSet probSet) {
        switch (probSet) {
            case UNITTESTSET1:
                //
                // This set should be used for unit testing - results are calibrated against these numbers so don't
                // change
                // order is: triple, adjTriple, double, adjDouble, single, adjSingle, zero, innerbull, outerBull,
                // anySingle
                //
                baseProbs = new double[][] {{0, 0, 0, 0, 0, 0, 0, .15, .3, .55}, // if aimed at bull
                        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, // if aimed at single
                        {0, 0, .5, .1, .2, .1, .1, 0, 0, 0}, // double
                        {.6, .1, 0, 0, .2, .1, 0, 0, 0, 0}}; // triple
                break;
            case UNITTESTSET2:
                //
                // also for unit testing
                //
                baseProbs = new double[][] {{0, 0, 0, 0, 0, 0, 0, .15, .3, .55}, // if aimed at bull
                        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, // if aimed at single
                        {0, 0, .4, 0, 0, 0, .6, 0, 0, 0}, // double
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0}}; // triple
                break;
            case LIVE:
                //
                // This set should be used for live - may be adjusted over time as more historical data is analysed.
                //
                baseProbs = new double[][] {{0, 0, 0, 0, 0, 0, 0, .29, .47, .24}, // bull
                        {0, 0, .01, 0, .97, .02, 0, 0, 0, 0}, // single
                        {0, 0, .47, .01, .21, 0, .31, 0, 0, 0}, // double
                        {.41, .02, 0, 0, .53, .04, 0, 0, 0, 0}}; // triple
                break;
        }
    }

}
