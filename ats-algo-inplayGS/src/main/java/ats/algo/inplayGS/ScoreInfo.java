package ats.algo.inplayGS;

public class ScoreInfo {
    int scoreA;
    int scoreB;
    double prob;

    /**
     * 
     * @param scoreStr must be "No Score" or of the form "n-m"
     * @param prob
     */
    public ScoreInfo(String scoreStr, double prob) {
        this.prob = prob;
        if (scoreStr.equals("No Score")) {
            scoreA = 0;
            scoreB = 0;
        } else {
            String[] bits = scoreStr.split("-");
            scoreA = Integer.valueOf(bits[0]);
            scoreB = Integer.valueOf(bits[1]);
        }
    }

    public int goalsToGoA(ScoreInfo currentScore) {
        return scoreA - currentScore.scoreA;
    }

    public int goalsToGoB(ScoreInfo currentScore) {
        return scoreB - currentScore.scoreB;
    }

    @Override
    public String toString() {
        return "ScoreInfo [scoreA=" + scoreA + ", scoreB=" + scoreB + ", prob=" + String.format("%.3f", prob) + "]";
    }

    /**
     * returns true if this object has the same or more goals in it than currentScoreInfo
     * 
     * @param currentScoreInfo
     * @return
     */
    public boolean consistentWith(ScoreInfo currentScoreInfo) {
        return this.scoreA + this.scoreB >= currentScoreInfo.scoreA + currentScoreInfo.scoreB;
    }
}
