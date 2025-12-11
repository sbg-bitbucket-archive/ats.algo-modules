package ats.algo.sport.tabletennis;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.tabletennis.TabletennisMatch.TabletennisMatchFacts;

public class TabletennisMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the current set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private ThreeWayStatistic matchWinnersetsEven;
    private TwoWayStatistic matchWinnersetsOdd;
    private TwoWayStatistic gamePointWinner;
    private CorrectScoreStatistic gameCorrectScore;
    private TotalStatistic pointsTotal;
    // private TotalStatistic setsTotal;
    private TotalStatistic gamePointTotal;
    private TotalStatistic gamePointTotalA;
    private TotalStatistic gamePointTotalB;
    private TotalStatistic totalPointsA;
    private TotalStatistic totalPointsB;
    private TwoWayStatistic pointsTotalOddEven;
    private TwoWayStatistic gamePointsTotalOddEven;
    private NWayStatistic gameXWinningMarginNWay;

    private NWayStatistic nextGameXWinningMarginNWay;
    private TotalStatistic nextGamePointTotal;
    private HandicapStatistic nextGamePointsHandicap;
    private TwoWayStatistic nextGameMoneyLine;
    private TwoWayStatistic nextGamePointsTotalOddEven;
    private TotalStatistic nextGamePointTotalA;
    private TotalStatistic nextGamePointTotalB;

    private TwoWayStatistic raceToThreePoints;// race to 3, 5, 7, 9, continuing
    private TwoWayStatistic raceToFivePoints; // with odd numbers ;
    private TwoWayStatistic raceToSevenPoints;
    private TwoWayStatistic raceToNinePoints;

    private TwoWayStatistic nextGameraceToThreePoints;// race to 3, 5, 7, 9,
    // continuing
    private TwoWayStatistic nextGameraceToFivePoints; // with odd numbers ;
    private TwoWayStatistic nextGameraceToSevenPoints;
    private TwoWayStatistic nextGameraceToNinePoints;

    private HandicapStatistic pointsHandicap;
    private HandicapStatistic gamePointsHandicap;
    private TwoWayStatistic gameMoneyLine;
    private TwoWayStatistic gamePointWinnerNext;
    private TwoWayStatistic gamePointWinnerNPlus2;
    private int setNo;
    private int currentGameNo;
    private int currentPointNoA;
    private int currentPointNoB;
    private boolean evenNosets;
    private String[] winningMarginSelections = {"A 2", "A 3-4", "A 5+", "B 2", "B 3-4", "B 5+"};

    TabletennisMatchMarketsFactory(TabletennisMatchState matchState) {
        setNo = matchState.getGameScoreForWin() * 2 - 1;
        currentGameNo = matchState.getGameNo(1);
        // int maxsetScore = setNo;
        int maxPointScore = 300;
        int maxPointScoreA = 200;
        // int setMaxPointScore = 200;
        // int maxHandicapMatch = 75;
        // int maxHandicapSet = 25;
        int currentPointNo = matchState.getPointNo(0);
        currentPointNoA = matchState.getPointsA();
        currentPointNoB = matchState.getPointsB();
        String thisSetSequenceId = matchState.getSequenceIdforGame(0);
        String thisPointSequenceId = matchState.getSequenceIdForPoint(0);
        Boolean generateMarket = true;
        matchWinnersetsOdd = new TwoWayStatistic("FT:ML", "Match Winner", generateMarket, "M", "A", "B");
        pointsTotal = new TotalStatistic("FT:OU", "Total Points", generateMarket, "M", maxPointScore);

        pointsHandicap = new HandicapStatistic("FT:PSPRD", "Point Handicap", generateMarket, "M", maxPointScoreA);

        totalPointsA = new TotalStatistic("FT:A:OU", "Total points A", generateMarket, "M", maxPointScoreA);
        totalPointsB = new TotalStatistic("FT:B:OU", "Total points B", generateMarket, "M", maxPointScoreA);
        pointsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Points Odd/Even", generateMarket, "M", "Odd", "Even");
        String marketDescription = String.format("Game %d Winner", currentGameNo);
        // if ((currentGameNo + currentPointNo) % 3 == 1) {
        marketDescription = String.format("Game %d Point %d Winner", currentGameNo, currentPointNo + 2);
        thisPointSequenceId = matchState.getSequenceIdForPoint(2);
        gamePointWinner = new TwoWayStatistic("P:PW", marketDescription, generateMarket, thisPointSequenceId, "A", "B");

        marketDescription = String.format("Game %d Point %d Winner", currentGameNo, currentPointNo + 3);
        thisPointSequenceId = matchState.getSequenceIdForPoint(3);
        gamePointWinnerNext =
                        new TwoWayStatistic("P:PW", marketDescription, generateMarket, thisPointSequenceId, "A", "B");

        thisPointSequenceId = matchState.getSequenceIdForPoint(4);
        marketDescription = String.format("Game %d Point %d Winner", currentGameNo, currentPointNo + 4);
        gamePointWinnerNPlus2 =
                        new TwoWayStatistic("P:PW", marketDescription, generateMarket, thisPointSequenceId, "A", "B");

        // } else if ((currentGameNo + currentPointNo) % 3 == 2) {
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 2);
        // thisPointSequenceId = matchState.getSequenceIdForPoint(2);
        // gamePointWinnerNext = new TwoWayStatistic("P:PW2", marketDescription,
        // generateMarket, thisPointSequenceId,
        // "A", "B");
        // gamePointWinner = gamePointWinnerNext;
        // thisPointSequenceId = matchState.getSequenceIdForPoint(3);
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 3);
        // gamePointWinnerNPlus2 = new TwoWayStatistic("P:PW3",
        // marketDescription, generateMarket, thisPointSequenceId,
        // "A", "B");
        // gamePointWinnerNext = gamePointWinnerNPlus2;
        //
        // thisPointSequenceId = matchState.getSequenceIdForPoint(4);
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 4);
        // gamePointWinnerNPlus2 = new TwoWayStatistic("P:PW1",
        // marketDescription, generateMarket, thisPointSequenceId,
        // "A", "B");
        //
        // } else if ((currentGameNo + currentPointNo) % 3 == 0) {
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 2);
        // thisPointSequenceId = matchState.getSequenceIdForPoint(2);
        // gamePointWinnerNext = new TwoWayStatistic("P:PW3", marketDescription,
        // generateMarket, thisPointSequenceId,
        // "A", "B");
        // gamePointWinner = gamePointWinnerNext;
        // thisPointSequenceId = matchState.getSequenceIdForPoint(3);
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 3);
        // gamePointWinnerNPlus2 = new TwoWayStatistic("P:PW1",
        // marketDescription, generateMarket, thisPointSequenceId,
        // "A", "B");
        // gamePointWinnerNext = gamePointWinnerNPlus2;
        //
        // marketDescription = String.format("Game %d Point %d Winner",
        // currentGameNo, currentPointNo + 4);
        // thisPointSequenceId = matchState.getSequenceIdForPoint(4);
        // gamePointWinnerNPlus2 = new TwoWayStatistic("P:PW2",
        // marketDescription, generateMarket, thisPointSequenceId,
        // "A", "B");
        // }

        marketDescription = String.format("Game %d Winner", currentGameNo);
        gameMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisSetSequenceId, "A", "B");

        marketDescription = String.format("Game %d Total Points ", currentGameNo);
        gamePointTotal = new TotalStatistic("P:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScore);
        marketDescription = String.format("Game %d Total Points A", currentGameNo);
        gamePointTotalA = new TotalStatistic("P:A:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScore);
        marketDescription = String.format("Game %d Total Points B", currentGameNo);
        gamePointTotalB = new TotalStatistic("P:B:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScore);

        marketDescription = String.format("Game %d Point Handicap ", currentGameNo);
        gamePointsHandicap = new HandicapStatistic("P:SPSPRD", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScoreA);
        gameCorrectScore = new CorrectScoreStatistic("FT:CS", "Correct Score of Game ", generateMarket, "M",
                        maxPointScore);

        marketDescription = String.format("Game %d Total Points Odd/Even", currentGameNo);
        gamePointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket, thisSetSequenceId,
                        "Odd", "Even");
        marketDescription = String.format("Game %d Winning Margin", currentGameNo);
        gameXWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket, thisSetSequenceId,
                        winningMarginSelections);
        String thisRaceFrameSequenceId = String.format("R%s.%d", currentGameNo, 3);
        marketDescription = String.format("Game %d Race to %d Points ", currentGameNo, 3);
        generateMarket = currentPointNoA < 3 && currentPointNoB < 3;
        raceToThreePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", currentGameNo, 5);
        marketDescription = String.format("Game %d Race to %d Points ", currentGameNo, 5);
        generateMarket = currentPointNoA < 5 && currentPointNoB < 5;
        raceToFivePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", currentGameNo, 7);
        marketDescription = String.format("Game %d Race to %d Points ", currentGameNo, 7);
        generateMarket = currentPointNoA < 7 && currentPointNoB < 7;
        raceToSevenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", currentGameNo, 9);
        marketDescription = String.format("Game %d Race to %d Points ", currentGameNo, 9);
        generateMarket = currentPointNoA < 9 && currentPointNoB < 9;
        raceToNinePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");

        int nextGameNo = currentGameNo + 1;
        thisSetSequenceId = matchState.getSequenceIdforGame(1);
        generateMarket = setNo >= nextGameNo;
        thisRaceFrameSequenceId = String.format("R%s.%d", nextGameNo, 3);
        marketDescription = String.format("Game %d Race to %d Points ", nextGameNo, 3);
        nextGameraceToThreePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket,
                        thisRaceFrameSequenceId, "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", nextGameNo, 5);
        marketDescription = String.format("Game %d Race to %d Points ", nextGameNo, 5);
        nextGameraceToFivePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket,
                        thisRaceFrameSequenceId, "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", nextGameNo, 7);
        marketDescription = String.format("Game %d Race to %d Points ", nextGameNo, 7);
        nextGameraceToSevenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket,
                        thisRaceFrameSequenceId, "A", "B");

        thisRaceFrameSequenceId = String.format("R%s.%d", nextGameNo, 9);
        marketDescription = String.format("Game %d Race to %d Points ", nextGameNo, 9);
        nextGameraceToNinePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket,
                        thisRaceFrameSequenceId, "A", "B");

        marketDescription = String.format("Game %d Total Points Odd/Even", nextGameNo);
        nextGamePointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket, thisSetSequenceId,
                        "Odd", "Even");
        marketDescription = String.format("Game %d Total Points ", nextGameNo);
        nextGamePointTotal =
                        new TotalStatistic("P:OU", marketDescription, generateMarket, thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points A", nextGameNo);
        nextGamePointTotalA = new TotalStatistic("P:A:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScore);
        marketDescription = String.format("Game %d Total Points B", nextGameNo);
        nextGamePointTotalB = new TotalStatistic("P:B:OU", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScore);
        marketDescription = String.format("Game %d Point Handicap ", nextGameNo);
        nextGamePointsHandicap = new HandicapStatistic("P:SPSPRD", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScoreA);
        marketDescription = String.format("Game %d Winner", nextGameNo);
        nextGameMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisSetSequenceId, "A", "B");
        marketDescription = String.format("Game %d Winning Margin", nextGameNo);
        nextGameXWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket, thisSetSequenceId,
                        winningMarginSelections);

    }

    void updateStats(TabletennisMatchState matchState, TabletennisMatchFacts matchFacts) {
        int gamesA = matchState.getGamesA();
        int gamesB = matchState.getGamesB();
        int pointsA1 = 0;
        int pointsB1 = 0;
        int sumA = 0;
        int sumB = 0;
        boolean serveA = matchFacts.servePointIsA;
        boolean serveNextA = matchFacts.servePointNextIsA;
        boolean isTieBreak = matchFacts.isTieBreak;
        int currentSetToalPoints = 0;
        int currentSetHandicapPoints = 0;
        int nextSetToalPoints = 0;
        int nextSetHandicapPoints = 0;
        boolean currentSetWInnerisA = false;
        boolean nextSetWInnerisA = false;
        for (int i = 0; i < setNo; i++) {
            pointsA1 = matchState.getGameScoreInGameN(i).A;
            pointsB1 = matchState.getGameScoreInGameN(i).B;
            sumA += pointsA1;
            sumB += pointsB1;
            if (i == currentGameNo - 1) {
                currentSetWInnerisA = pointsA1 > pointsB1;
                currentSetToalPoints = pointsA1 + pointsB1;
                currentSetHandicapPoints = pointsA1 - pointsB1;
                // System.out.println(currentSetNo+"--"+pointsA1+"----"+pointsB1+"---");
            }
            if (i == currentGameNo) {
                nextSetWInnerisA = pointsA1 > pointsB1;
                nextSetToalPoints = pointsA1 + pointsB1;
                nextSetHandicapPoints = pointsA1 - pointsB1;
                // System.out.println(currentSetNo+"--"+pointsA1+"----"+pointsB1+"---");
            }
            // System.out.println(pointsA1+"----in--"+pointsB1);
        }
        TeamId teamId = TeamId.UNKNOWN;
        if (gamesA > gamesB)
            teamId = TeamId.A;
        if (gamesA < gamesB)
            teamId = TeamId.B;
        if (evenNosets)
            matchWinnersetsEven.increment(teamId);
        else
            matchWinnersetsOdd.increment(teamId == TeamId.A);
        pointsTotal.increment(sumA + sumB);
        pointsHandicap.increment(sumA - sumB);
        totalPointsA.increment(sumA);
        totalPointsB.increment(sumB);
        pointsTotalOddEven.increment(isOdd(sumA + sumB));
        gameCorrectScore.increment(gamesA, gamesB);
        gameMoneyLine.increment(currentSetWInnerisA);
        gamePointTotal.increment(currentSetToalPoints);
        gamePointsHandicap.increment(currentSetHandicapPoints);
        gamePointTotalA.increment((currentSetHandicapPoints + currentSetToalPoints) / 2);
        gamePointTotalB.increment((currentSetToalPoints - currentSetHandicapPoints) / 2);
        gamePointsTotalOddEven.increment(isOdd(currentSetToalPoints));

        nextGameMoneyLine.increment(nextSetWInnerisA);
        nextGamePointTotal.increment(nextSetToalPoints);
        nextGamePointTotalA.increment((nextSetToalPoints + nextSetHandicapPoints) / 2);
        nextGamePointTotalB.increment((nextSetToalPoints - nextSetHandicapPoints) / 2);
        nextGamePointsHandicap.increment(nextSetHandicapPoints);
        nextGamePointsTotalOddEven.increment(isOdd(nextSetToalPoints));

        double r = RandomNoGenerator.nextDouble();

        boolean pointWonByA;
        boolean pointWonNextByA;
        boolean pointWonNextPlusByA;
        if (!isTieBreak) {
            if (serveA && serveNextA) {
                pointWonByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < 1 - matchFacts.perWinningB;
            } else if (serveA) {
                pointWonByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < matchFacts.perWinningA;
            } else if (serveNextA) {
                pointWonByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < 1 - matchFacts.perWinningB;
            } else {
                pointWonByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < matchFacts.perWinningA;
            }
        } else {
            if (serveNextA) {
                pointWonByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < matchFacts.perWinningA;
            } else {
                pointWonByA = r < 1 - matchFacts.perWinningB;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < matchFacts.perWinningA;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < 1 - matchFacts.perWinningB;
            }

        }

        gamePointWinner.increment(pointWonByA);
        gamePointWinnerNext.increment(pointWonNextByA);
        gamePointWinnerNPlus2.increment(pointWonNextPlusByA);

        if (currentPointNoA < 3 && currentPointNoB < 3)
            raceToThreePoints.increment(matchFacts.point3WinnerIsA);
        if (currentPointNoA < 5 && currentPointNoB < 5)
            raceToFivePoints.increment(matchFacts.point5WinnerIsA);
        if (currentPointNoA < 7 && currentPointNoB < 7)
            raceToSevenPoints.increment(matchFacts.point7WinnerIsA);
        if (currentPointNoA < 9 && currentPointNoB < 9)
            raceToNinePoints.increment(matchFacts.point9WinnerIsA);
        nextGameraceToThreePoints.increment(matchFacts.nextGamepoint3WinnerIsA);
        nextGameraceToFivePoints.increment(matchFacts.nextGamepoint5WinnerIsA);
        nextGameraceToSevenPoints.increment(matchFacts.nextGamepoint7WinnerIsA);
        nextGameraceToNinePoints.increment(matchFacts.nextGamepoint9WinnerIsA);

        int n = -1;
        if (currentSetHandicapPoints > 0) {
            if (currentSetHandicapPoints >= 5)
                n = 2;
            else if (currentSetHandicapPoints >= 3)
                n = 1;
            else
                n = 0;
        } else {
            if (Math.abs(currentSetHandicapPoints) >= 5)
                n = 5;
            else if (Math.abs(currentSetHandicapPoints) >= 3)
                n = 4;
            else
                n = 3;
        }
        gameXWinningMarginNWay.increment(n);

        n = -1;
        if (nextSetHandicapPoints > 0) {
            if (nextSetHandicapPoints >= 5)
                n = 2;
            else if (nextSetHandicapPoints >= 3)
                n = 1;
            else
                n = 0;
        } else {
            if (Math.abs(nextSetHandicapPoints) >= 5)
                n = 5;
            else if (Math.abs(nextSetHandicapPoints) >= 3)
                n = 4;
            else
                n = 3;
        }
        nextGameXWinningMarginNWay.increment(n);
    }

    private boolean isOdd(int i) {
        if (i % 2 == 1)
            return true;
        else
            return false;
    }

}
