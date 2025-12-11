package ats.algo.sport.outrights.calcengine.core;

public class FullMatchProbs extends MatchProbs {



    private int totalGoals;
    private double probUnderTotalGoals;
    private int goalsHcap;
    private double probHomeWinsHcap;

    /**
     * calculates probs for total goals and handicap once the correct score grid has been calculated
     */

    public FullMatchProbs(Team home, Team away, RatingsFactors ratingsFactors, boolean playedAtNeutralGround) {
        super();
        super.initialise(home.getRatingAttack(), home.getRatingDefense(), away.getRatingAttack(),
                        away.getRatingDefense(), ratingsFactors, playedAtNeutralGround);
        calcExtraProbs();
    }

    public FullMatchProbs(double expectedGoalsHome, double expectedGoalsAway) {
        super(expectedGoalsHome, expectedGoalsAway);
        calcExtraProbs();
    }


    private void calcExtraProbs() { /*
                                     * calc the derived probs for match winner, handicap and totals markets
                                     */

        double[] totalGoalsProbs = new double[2 * MAX_GOALS];
        double[] hcapGoalsProbs = new double[2 * MAX_GOALS + 1];
        for (int i = 0; i < super.probsSize; i++) {
            ProbsEntry entry = probs[i];
            FixtureResult score = entry.score;
            double p = entry.prob;
            totalGoalsProbs[score.getScoreA() + score.getScoreB()] += p;
            hcapGoalsProbs[MAX_GOALS + score.getScoreA() - score.getScoreB()] += p;
        }
        /*
         * calc total goals
         */
        LineProb lineProb = calcLineProb(totalGoalsProbs);
        this.totalGoals = lineProb.n;
        this.probUnderTotalGoals = lineProb.probUnder;
        /*
         * calc hcap
         */
        LineProb hcapLineProb = calcLineProb(hcapGoalsProbs);
        this.goalsHcap = hcapLineProb.n - MAX_GOALS;
        this.probHomeWinsHcap = lineProb.probUnder;
    }


    public int getTotalGoals() {
        return totalGoals;
    }

    public double getProbUnderTotalGoals() {
        return probUnderTotalGoals;
    }

    public int getGoalsHcap() {
        return goalsHcap;
    }

    public double getProbHomeWinsHcap() {
        return probHomeWinsHcap;
    }

}
