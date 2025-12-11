package ats.algo.sport.outrights.calcengine.core;

import java.util.Map.Entry;

import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Selection;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.core.util.json.JsonUtil;

/**
 * calculates the correct score probs and associated derived probs based on supplied team ratings
 * 
 * This class has been optimised for performance - used intensively by the outrights calc engine
 * 
 * @author gicha
 *
 */
public class MatchProbs {

    class ProbsEntry {
        FixtureResult score;
        double prob;

        public ProbsEntry() {
            score = new FixtureResult();
        }

        public FixtureResult getScore() {
            return score;
        }

        public double getProb() {
            return prob;
        }

        public void setScore(FixtureResult score) {
            this.score = score;
        }

        public void setProb(double prob) {
            this.prob = prob;
        }

        @Override
        public String toString() {
            return JsonUtil.marshalJson(this);
        }

        public void update(int goalsA, int goalsB, double p) {
            score.setScoreA(goalsA);
            score.setScoreB(goalsB);
            prob = p;
        }

    }

    protected ProbsEntry[] probs;
    protected int probsSize;

    // private double homeAttack;
    // private double homeDefense;
    // private double awayAttack;
    // private double awayDefense;
    // private boolean playedAtNeutralGround;

    private double probHomeWin;
    private double probAwayWin;
    private double probDraw;
    private double conditionalProbHomeWin;

    double[] goalProbsA;
    double[] goalProbsB;

    public static final int MAX_GOALS = 15;

    public MatchProbs() {
        probs = new ProbsEntry[(MAX_GOALS + 1) * (MAX_GOALS + 1)];
        /*
         * create the set of objects that will be used
         */
        int index = 0;
        for (int i = 0; i <= MAX_GOALS; i++)
            for (int j = 0; j < MAX_GOALS; j++) {
                probs[index] = new ProbsEntry();
                index++;
            }
        goalProbsA = new double[MAX_GOALS];
        goalProbsB = new double[MAX_GOALS];

    }


    public void initialise(double homeAttack, double homeDefense, double awayAttack, double awayDefense,
                    RatingsFactors ratingsFactors, boolean playedAtNeutralGround) {
        /*
         * uncomment if needed for debug
         */
        // this.homeAttack = homeAttack;
        // this.homeDefense = homeDefense;
        // this.awayAttack = awayAttack;
        // this.awayDefense = awayDefense;
        // this.playedAtNeutralGround = playedAtNeutralGround;
        double meanGoalsHome = ratingsFactors.getAttackRatingFactor() * homeAttack
                        + ratingsFactors.getDefenseRatingFactor() * awayDefense;
        double meanGoalsAway = ratingsFactors.getAttackRatingFactor() * awayAttack
                        + ratingsFactors.getDefenseRatingFactor() * homeDefense;
        if (playedAtNeutralGround) {
            meanGoalsHome += ratingsFactors.getNeutralGroundAdjFactor();
            meanGoalsAway += ratingsFactors.getNeutralGroundAdjFactor();
        } else {

            meanGoalsHome += ratingsFactors.getHomeAdjFactor();
            meanGoalsAway += ratingsFactors.getAwayAdjFactor();
        }
        initFromExpectedGoals(meanGoalsHome, meanGoalsAway);
    }

    /**
     * Calculates the correct score grid
     * 
     * @param expectedGoalsHome
     * @param expectedGoalsAway
     * @return
     */
    public MatchProbs(double expectedGoalsHome, double expectedGoalsAway) {
        this();
        initFromExpectedGoals(expectedGoalsHome, expectedGoalsAway);
    }

    /**
     * calculates from the probs in market
     * 
     * @param market
     */
    public MatchProbs(Market market) {
        this();
        probsSize = 0;
        double sumProbs = 0.0;
        for (Entry<String, Selection> e : market.getSelections().entrySet()) {
            PairOfIntegers score = PairOfIntegers.generateFromString(e.getKey());
            double p = e.getValue().getProb();
            ProbsEntry entry = probs[probsSize];
            entry.update(score.A, score.B, p);
            sumProbs += p;
            probsSize++;
            if (score.A > score.B)
                probHomeWin += p;
            else if (score.A < score.B)
                probAwayWin += p;
            else
                probDraw += p;
        }
        normaliseProbs(sumProbs);
    }

    private static final double MIN_PROB = 1.0e-4;

    private void initFromExpectedGoals(double expectedGoalsHome, double expectedGoalsAway) {
        if (expectedGoalsHome <= 0)
            expectedGoalsHome = 0;
        if (expectedGoalsAway <= 0)
            expectedGoalsAway = 0;
        /*
         * calc poisson distns for goals scored by each team
         */

        goalProbsA[0] = Math.exp(-expectedGoalsHome);
        goalProbsB[0] = Math.exp(-expectedGoalsAway);
        for (int goals = 1; goals < MAX_GOALS; goals++) {
            if ((goals > expectedGoalsHome) && (goalProbsA[goals - 1] < MIN_PROB))
                goalProbsA[goals] = -1.0;
            else
                goalProbsA[goals] = goalProbsA[goals - 1] * expectedGoalsHome / goals;

            if (goals > expectedGoalsAway && goalProbsB[goals - 1] < MIN_PROB)
                goalProbsB[goals] = -1.0;
            else
                goalProbsB[goals] = goalProbsB[goals - 1] * expectedGoalsAway / goals;

        }
        probsSize = 0;
        double sumProbs = 0.0;
        for (int goalsA = 0; goalsA < MAX_GOALS; goalsA++) {
            if (goalProbsA[goalsA] == -1.0)
                break;
            for (int goalsB = 0; goalsB < MAX_GOALS; goalsB++) {
                if (goalProbsB[goalsB] == -1.0)
                    break;
                double p = goalProbsA[goalsA] * goalProbsB[goalsB];
                if (p > MIN_PROB) {
                    ProbsEntry entry = probs[probsSize];
                    entry.update(goalsA, goalsB, p);
                    sumProbs += p;
                    probsSize++;
                    if (goalsA > goalsB)
                        probHomeWin += p;
                    else if (goalsA < goalsB)
                        probAwayWin += p;
                    else
                        probDraw += p;
                }
            }
        }
        normaliseProbs(sumProbs);
        conditionalProbHomeWin = probHomeWin / (probHomeWin + probAwayWin);
    }

    void set(int index, int goalsA, int goalsB, double prob) {
        ProbsEntry entry = probs[index];
        entry.update(goalsA, goalsB, prob);

    }

    /**
     * probs must sum to exactly 1.0 else simulation using random nos in [0,1] won't work
     * 
     * @param probs
     */
    private void normaliseProbs(double sumProbs) {
        for (int i = 0; i < this.probsSize; i++) {
            ProbsEntry e = this.probs[i];
            e.prob /= sumProbs;
        }
        /*
         * normalise the win lose draw probs
         */
        double sumWld = this.probHomeWin + this.probAwayWin + this.probDraw;
        probHomeWin /= sumWld;
        probAwayWin /= sumWld;
        probDraw /= sumWld;
    }

    /**
     * simulates a single match. if mustHaveAWinner is false draws are allowed
     * 
     * @param mustHaveAWinner
     * @return Score at full time (i.e. excluding extra time, penalties). If mustHaveAWinner is true then sets the
     *         winner of the competition
     */
    public FixtureResult simulateMatch(boolean mustHaveAWinner) {
        FixtureResult result = simulateFinalScore();
        if (mustHaveAWinner) {
            /*
             * is there a winner at end of full time?
             */
            if (result.getScoreA() > result.getScoreB()) {
                result.setWinningTeamID(TeamId.A);
            } else if (result.getScoreA() < result.getScoreB()) {
                result.setWinningTeamID(TeamId.B);
            } else {
                /*
                 * draw at the end of full time so calc conditional prob of A winning given one team wins to simulate
                 * extra time plus penalties
                 */

                double r = RandomNoGenerator.nextDouble();
                if (r < conditionalProbHomeWin)
                    result.setWinningTeamID(TeamId.A);
                else
                    result.setWinningTeamID(TeamId.B);
            }

        }
        return result;
    }

    private FixtureResult simulateFinalScore() {
        double r = RandomNoGenerator.nextDouble();
        double cumProb = 0;
        for (int i = 0; i < probsSize; i++) {
            ProbsEntry entry = probs[i];
            cumProb += entry.prob;
            if (r < cumProb) {
                entry.score.setWinningTeamID(null);
                return entry.score;
            }
        }
        throw new IllegalArgumentException("probs out of range");
    }

    /**
     * 
     * @param randomNo
     * @param goalsProbs
     * @return
     */
    static int simulateGoals(double randomNo, double[] goalsProbs) {
        double cumProb = 0;
        for (int n = 0; n < goalsProbs.length; n++) {
            cumProb += goalsProbs[n];
            if (randomNo < cumProb)
                return n;
        }
        throw new IllegalArgumentException("probs out of range");
    }

    static LineProb calcLineProb(double[] probs) {
        int n;
        double probUnder = 0;;
        for (n = 0; n < probs.length; n++) {
            probUnder += probs[n];
            if (probUnder >= 0.5)
                break;
        }
        /*
         * decide which is closest to 0.5
         */
        double p2 = probUnder - probs[n];
        if (probUnder - 0.5 > 0.5 - p2) {
            n--;
            probUnder = p2;
        }
        return new LineProb(n, probUnder);
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public ProbsEntry[] getProbs() {
        return probs;
    }

    public void setProbsSize(int n) {
        probsSize = n;
    }

    public double getProbHomeWin() {
        return probHomeWin;
    }

    public double getProbAwayWin() {
        return probAwayWin;
    }

    public double getProbDraw() {
        return probDraw;
    }

}
