package ats.algo.montecarloframework;

import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;

public class TestStatistics extends MarketsFactory {
    private TwoWayStatistic matchWinner;
    private CorrectScoreStatistic correctScore;
    private TotalStatistic total;
    private HandicapStatistic handicap;
    private ThreeWayStatistic threeWay;

    public TestStatistics(int nSets) {
        super();
        int maxScore = nSets / 2 + 1;
        matchWinner = new TwoWayStatistic("MW", "Match winner", true, "M", "A", "B");
        correctScore = new CorrectScoreStatistic("CS", "Correct score", true, "M", maxScore);
        total = new TotalStatistic("TS", "Total score", true, "M", nSets);
        handicap = new HandicapStatistic("HS", "Handicap", true, "M", nSets);
        threeWay = new ThreeWayStatistic("TW", "Three way", true, "M", "A", "B", "Neither");
    }

    public void updateStats(int scoreA, int scoreB) {
        matchWinner.increment(scoreA > scoreB);
        correctScore.increment(scoreA, scoreB);
        total.increment(scoreA + scoreB);
        handicap.increment(scoreA - scoreB);
        TeamId id = TeamId.UNKNOWN;
        if (scoreA - scoreB - 1 > 0)
            id = TeamId.A;
        if (scoreA - scoreB - 1 < 0)
            id = TeamId.B;
        threeWay.increment(id);
    }
}
