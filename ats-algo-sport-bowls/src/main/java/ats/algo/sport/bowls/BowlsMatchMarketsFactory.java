package ats.algo.sport.bowls;

import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.bowls.BowlsMatch.BowlsMatchFacts;
import ats.algo.core.common.TeamId;

public class BowlsMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the current set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private TwoWayStatistic matchWinnersetsOdd;
    private ThreeWayStatistic setWinnersetsOdd;
    private TwoWayStatistic endWinnersetsOdd;
    private TotalStatistic endCurrentTotalPoints;
    private CorrectScoreStatistic endCurrentCorrectScore;
    private ThreeWayStatistic raceTo4Points;
    private ThreeWayStatistic raceTo6Points;
    private ThreeWayStatistic raceTo8Points;
    private ThreeWayStatistic raceTo10Points;
    private ThreeWayStatistic leadAfter3Ends;
    private ThreeWayStatistic leadAfter4Ends;
    private ThreeWayStatistic leadAfter5Ends;
    private ThreeWayStatistic leadAfter6Ends;

    private HandicapStatistic pointHandicap;
    private TotalStatistic matchTotalPoints;
    private TotalStatistic totalSets;
    private TotalStatistic setTotalPoints;
    private HandicapStatistic setsHandicap;

    private TotalStatistic totalPointsA;
    private TotalStatistic totalPointsB;
    private TwoWayStatistic pointsTotalOddEven;
    private TwoWayStatistic gamePointsTotalOddEven;
    private NWayStatistic setCorrectScoreNWay;
    private NWayStatistic setWinningMarginNWay;

    private int currentEnd;
    private int currentSetNo;
    private int setNo;
    private String[] setCorrectScoreSelections = {"2-0", "2-1", "1.5-0.5", "0.5-1.5", " 1-2", "0-2"};
    private String[] winningMarginSelections = {"A 1-3", "A 4-6", "A 7-9", "A 10+", "B 1-3", "B 4-6", "B 7-9", "B 10+"};

    BowlsMatchMarketsFactory(BowlsMatchState matchState) {
        int maxPoisntScoreEnd = matchState.getBowlsInEnd() * matchState.getEndScoreForWin();
        setNo = (int) (matchState.getSetScoreForWin() * 2 - 1);
        int maxPoisntScoreMatch = matchState.getBowlsInEnd() * matchState.getEndScoreForWin() * setNo;
        currentSetNo = (int) (matchState.getSetsA() + matchState.getSetsB() + 1);
        int currentPointA = matchState.getPointsA();
        int currentPointB = matchState.getPointsB();
        currentEnd = matchState.getCurrentEnd();
        int endScore = matchState.getEndScoreForWin();
        int finalEndScore = matchState.getEndsInFinalSet();
        String thisSetSequenceId = matchState.getSequenceIdforSet(0);
        String thisEndSequenceId = matchState.getSequenceIdforEnd(0);
        Boolean generateMarket = true;
        matchWinnersetsOdd = new TwoWayStatistic("FT:ML", "Match Winner", generateMarket, "M", "A", "B");
        pointsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Points Odd/Even", generateMarket, "M", "Odd", "Even");
        setCorrectScoreNWay = new NWayStatistic("FT:CS", "Correct Score of Set", generateMarket, "M",
                        setCorrectScoreSelections);
        String marketDescription = String.format("Set %d Result ", currentSetNo);
        setWinnersetsOdd = new ThreeWayStatistic("P:ML", marketDescription, generateMarket, thisSetSequenceId, "A", "B",
                        "D");
        marketDescription = String.format("Set %d End %d Winner ", currentSetNo, currentEnd);
        endWinnersetsOdd = new TwoWayStatistic("PE:ML", marketDescription, generateMarket, thisEndSequenceId, "A", "B");

        marketDescription = String.format("Set %d Winning Margin", currentSetNo);
        setWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket, thisSetSequenceId,
                        winningMarginSelections);
        pointHandicap = new HandicapStatistic("FT:SPRD", "Match Point Handicap", generateMarket, "M",
                        maxPoisntScoreMatch);

        matchTotalPoints = new TotalStatistic("FT:OU", "Match Total Points", generateMarket, "M", maxPoisntScoreMatch);
        totalSets = new TotalStatistic("FT:OUS", "Match Total Sets", generateMarket, "M", setNo);
        marketDescription = String.format("Set %d Total Points Odd/Even", currentSetNo);
        gamePointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket, thisSetSequenceId,
                        "Odd", "Even");
        marketDescription = String.format("Set %d Total Points ", currentSetNo);
        setTotalPoints = new TotalStatistic("P:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPoisntScoreMatch);
        marketDescription = String.format("Set %d Handicap ", currentSetNo);

        setsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket, thisSetSequenceId,
                        maxPoisntScoreMatch);

        totalPointsA = new TotalStatistic("FT:A:OU", "Total points A", generateMarket, "M", maxPoisntScoreEnd);
        totalPointsB = new TotalStatistic("FT:B:OU", "Total points B", generateMarket, "M", maxPoisntScoreEnd);
        marketDescription = String.format("Set %d End %d Total Points ", currentSetNo, currentEnd);
        endCurrentTotalPoints = new TotalStatistic("P:OU", marketDescription, generateMarket, thisEndSequenceId,
                        maxPoisntScoreEnd);

        marketDescription = String.format("Set %d End %d Correct Score ", currentSetNo, currentEnd);
        endCurrentCorrectScore =
                        new CorrectScoreStatistic("P:CS", marketDescription, generateMarket, thisEndSequenceId, 4);
        generateMarket = currentPointA < 4 && currentPointB < 4;
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 4);
        String thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 4);
        raceTo4Points = new ThreeWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId, "A",
                        "B", "Neither");
        generateMarket = currentPointA < 6 && currentPointB < 6;
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 6);
        thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 6);
        raceTo6Points = new ThreeWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId, "A",
                        "B", "Neither");
        generateMarket = currentPointA < 8 && currentPointB < 8;
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 8);
        thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 8);
        raceTo8Points = new ThreeWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId, "A",
                        "B", "Neither");
        generateMarket = currentPointA < 10 && currentPointB < 10;
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 10);
        thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 10);
        raceTo10Points = new ThreeWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B", "Neither");

        marketDescription = String.format("Set %d Lead After %d Ends ", currentSetNo, 3);
        String thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 3);

        if (!matchState.isFinalSet()) {
            generateMarket = currentEnd <= 3 && 3 < endScore;
        } else {
            generateMarket = currentEnd <= 3 && 3 < finalEndScore;
        }
        leadAfter3Ends = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket, thisLeadFrameSequenceId,
                        "A", "B", "Draw");
        marketDescription = String.format("Set %d Lead After %d Ends ", currentSetNo, 4);
        thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 4);
        if (!matchState.isFinalSet()) {
            generateMarket = currentEnd <= 4 && 4 < endScore;
        } else {
            generateMarket = currentEnd <= 4 && 4 < finalEndScore;
        }
        leadAfter4Ends = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket, thisLeadFrameSequenceId,
                        "A", "B", "Draw");
        marketDescription = String.format("Set %d Lead After %d Ends ", currentSetNo, 5);
        thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 5);
        if (!matchState.isFinalSet()) {
            generateMarket = currentEnd <= 5 && 5 < endScore;
        } else {
            generateMarket = currentEnd <= 5 && 5 < finalEndScore;
        }
        leadAfter5Ends = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket, thisLeadFrameSequenceId,
                        "A", "B", "Draw");
        marketDescription = String.format("Set %d Lead After %d Ends ", currentSetNo, 6);
        thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 6);
        if (!matchState.isFinalSet()) {
            generateMarket = currentEnd <= 6 && 6 < endScore;
        } else {
            generateMarket = currentEnd <= 6 && 6 < finalEndScore;
        }
        leadAfter6Ends = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket, thisLeadFrameSequenceId,
                        "A", "B", "Draw");

    }

    void updateStats(BowlsMatchState matchState, BowlsMatchFacts matchFacts) {
        double setsA = matchState.getSetsA();
        double setsB = matchState.getSetsB();
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;
        int totalCurrentSet = 0;
        int handicapCurrentSet = 0;
        TeamId teamId = TeamId.UNKNOWN;
        TeamId CurrentSetWinner = TeamId.UNKNOWN;
        for (int i = 0; i < setNo; i++) {
            pointsA1 = matchState.getGameScoreInSetN(i).A;
            pointsB1 = matchState.getGameScoreInSetN(i).B;
            sumA += pointsA1;
            sumB += pointsB1;
            if (i == currentSetNo - 1) {
                handicapCurrentSet = pointsA1 - pointsB1;
                totalCurrentSet = pointsA1 + pointsB1;
                if (pointsA1 < pointsB1)
                    CurrentSetWinner = TeamId.B;
                else if (pointsA1 > pointsB1)
                    CurrentSetWinner = TeamId.A;
            }
        }
        if (setsA > setsB)
            teamId = TeamId.A;
        if (setsA < setsB)
            teamId = TeamId.B;
        matchWinnersetsOdd.increment(teamId == TeamId.A);

        setWinnersetsOdd.increment(CurrentSetWinner);
        // nextSetWinnersetsOdd.increment(matchFacts.setNextWinner);
        endWinnersetsOdd.increment(matchFacts.endWinnerCurrentIsA);
        endCurrentTotalPoints.increment(Math.abs(Math.abs(matchFacts.totalPointsCurrentEnd)));

        if (matchFacts.totalPointsCurrentEnd > 0) {
            endCurrentCorrectScore.increment(matchFacts.totalPointsCurrentEnd, 0);
        } else
            endCurrentCorrectScore.increment(0, (-1) * matchFacts.totalPointsCurrentEnd);
        raceTo4Points.increment(matchFacts.raceTo4Points);
        raceTo6Points.increment(matchFacts.raceTo6Points);
        raceTo8Points.increment(matchFacts.raceTo8Points);
        raceTo10Points.increment(matchFacts.raceTo10Points);
        leadAfter3Ends.increment(matchFacts.end3LeadIsA);
        leadAfter4Ends.increment(matchFacts.end4LeadIsA);
        leadAfter5Ends.increment(matchFacts.end5LeadIsA);
        leadAfter6Ends.increment(matchFacts.end6LeadIsA);

        pointHandicap.increment(sumA - sumB);
        matchTotalPoints.increment(sumA + sumB);
        totalPointsA.increment(sumA);
        totalPointsB.increment(sumB);
        pointsTotalOddEven.increment(isOdd(sumA + sumB));
        gamePointsTotalOddEven.increment(isOdd(totalCurrentSet));

        totalSets.increment((int) (setsA + setsB));
        setTotalPoints.increment(totalCurrentSet);
        setsHandicap.increment(handicapCurrentSet);
        if ((setsA == 2) && (setsB == 0))
            setCorrectScoreNWay.increment(0);
        if ((setsA == 2) && (setsB == 1))
            setCorrectScoreNWay.increment(1);
        if ((setsA == 1.5) && (setsB == 0.5))
            setCorrectScoreNWay.increment(2);
        if ((setsA == 0.5) && (setsB == 1.5))
            setCorrectScoreNWay.increment(3);
        if ((setsA == 1) && (setsB == 2))
            setCorrectScoreNWay.increment(4);
        if ((setsA == 0) && (setsB == 2))
            setCorrectScoreNWay.increment(5);

        int n = -1;
        if (handicapCurrentSet > 0) {
            if (handicapCurrentSet >= 10)
                n = 3;
            else if (handicapCurrentSet >= 7)
                n = 2;
            else if (handicapCurrentSet >= 4)
                n = 1;
            else
                n = 0;
        } else {
            if (Math.abs(handicapCurrentSet) >= 10)
                n = 7;
            else if (Math.abs(handicapCurrentSet) >= 7)
                n = 6;
            else if (Math.abs(handicapCurrentSet) >= 4)
                n = 5;
            else
                n = 4;
        }
        setWinningMarginNWay.increment(n);
    }

    private boolean isOdd(int i) {
        if (i % 2 == 1)
            return true;
        else
            return false;
    }
}
