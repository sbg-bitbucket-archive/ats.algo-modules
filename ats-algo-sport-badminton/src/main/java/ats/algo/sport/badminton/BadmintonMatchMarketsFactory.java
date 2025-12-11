package ats.algo.sport.badminton;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.badminton.BadmintonMatch.BadmintonMatchFacts;
import ats.algo.sport.badminton.BadmintonMatchIncidentResult.BadmintonMatchIncidentResultType;

public class BadmintonMatchMarketsFactory extends MarketsFactory {

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
    private TotalStatistic pointsTotal;
    private TotalStatistic gamePointTotal;
    private HandicapStatistic pointsHandicap;
    private HandicapStatistic setsHandicap;
    private HandicapStatistic gamePointsHandicap;
    private TwoWayStatistic gameMoneyLine;
    private TwoWayStatistic gamePointWinnerNext;
    private TwoWayStatistic gamePointWinnerNPlus2;

    private CorrectScoreStatistic gameCorrectScore;
    private TotalStatistic gamePointTotalA;
    private TotalStatistic gamePointTotalB;
    private TotalStatistic totalPointsA;
    private TotalStatistic totalPointsB;
    private TwoWayStatistic pointsTotalOddEven;
    private TwoWayStatistic gamePointsTotalOddEven;
    private NWayStatistic gameXCorrectScoreNWay;
    private NWayStatistic gameXWinningMarginNWay;
    private TwoWayStatistic gameExtraPoints;

    private TwoWayStatistic raceToFivePoints;
    private ThreeWayStatistic leadAfterTenPoints;
    private TwoWayStatistic raceToTenPoints;
    private ThreeWayStatistic leadAfterTwentyPoints;
    private TwoWayStatistic raceToFifteenPoints;
    private ThreeWayStatistic leadAfterThirtyPoints;

    private TotalStatistic nextGamePointTotal;
    private HandicapStatistic nextGamePointsHandicap;
    private TwoWayStatistic nextGameMoneyLine;
    private TotalStatistic nextGamePointTotalA;
    private TotalStatistic nextGamePointTotalB;
    private TwoWayStatistic nextGamePointsTotalOddEven;
    private NWayStatistic nextGameXCorrectScoreNWay;
    private NWayStatistic nextGameXWinningMarginNWay;
    private TwoWayStatistic nextGameExtraPoints;

    private TwoWayStatistic nextGameraceToFivePoints;
    private ThreeWayStatistic nextGameleadAfterTenPoints;
    private TwoWayStatistic nextGameraceToTenPoints;
    private ThreeWayStatistic nextGameleadAfterTwentyPoints;
    private TwoWayStatistic nextGameraceToFifteenPoints;
    private ThreeWayStatistic nextGameleadAfterThirtyPoints;

    private int setNo;
    private int currentSetNo;
    private int currentPointNo;
    private int currentPointNoA;
    private int currentPointNoB;
    private boolean evenNosets;
    private String[] gameCorrectScoreSelections = {"21-13 or better", "21-14", "21-15", "21-16", "21-17", "21-18",
            "21-19", "13-21 or better", "14-21", "15-21", "16-21", "17-21", "18-21", "19-21", "after extra points A",
            "after extra points B"};
    private String[] winningMarginSelections = {"A 1-2", "A 3-5", "A 6-8", "A 9+", "B 1-2", "B 3-5", "B 6-8", "B 9+"};

    BadmintonMatchMarketsFactory(BadmintonMatchState matchState) {
        this.setCorrectScoreDisplayHasTeamId(true);
        setNo = matchState.getGameScoreForWin() * 2 - 1;
        currentSetNo = matchState.getGameNo(1);
        currentPointNo = matchState.getPointNo(0);
        currentPointNoA = matchState.getPointsA();
        currentPointNoB = matchState.getPointsB();
        int maxPointScore = 500;
        int maxPointScoreA = 300;

        String thisSetSequenceId = matchState.getSequenceIdforGame(0);
        String thisPointSequenceId = matchState.getSequenceIdForPoint(1);

        boolean generateMarket = true;
        boolean matchNotAbandoned = (matchState.getMostRecentMatchIncidentResult()
                        .getBadmintonMatchIncidentResultType() != BadmintonMatchIncidentResultType.MATCHABANDONED);

        matchWinnersetsOdd =
                        new TwoWayStatistic("FT:ML", "Match Winner", generateMarket & matchNotAbandoned, "M", "A", "B");
        pointsTotal = new TotalStatistic("FT:OU", "Total Points", generateMarket & matchNotAbandoned, "M",
                        maxPointScore);
        pointsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Points Odd/Even", generateMarket & matchNotAbandoned,
                        "M", "Odd", "Even");

        pointsHandicap = new HandicapStatistic("FT:PSPRD", "Point Handicap", generateMarket & matchNotAbandoned, "M",
                        maxPointScoreA);
        setsHandicap = new HandicapStatistic("FT:SPRD", "Set Handicap", generateMarket & matchNotAbandoned, "M",
                        maxPointScoreA);
        String marketDescription = String.format("Game %d Winner ", currentSetNo);
        gameMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "A", "B");
        marketDescription = String.format("Game %d Extra Point ", currentSetNo);
        gameExtraPoints = new TwoWayStatistic("P:ET", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "Yes", "No");
        marketDescription = String.format("Game %d Total Points ", currentSetNo);
        gamePointTotal = new TotalStatistic("P:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points A", currentSetNo);
        gamePointTotalA = new TotalStatistic("P:A:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points B", currentSetNo);
        gamePointTotalB = new TotalStatistic("P:B:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points Odd/Even", currentSetNo);
        gamePointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "Odd", "Even");
        marketDescription = String.format("Game %d Point Handicap ", currentSetNo);
        gamePointsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScoreA);
        marketDescription = String.format("Game %d Point %d Winner", currentSetNo, currentPointNo + 3);
        thisPointSequenceId = matchState.getSequenceIdForPoint(3);
        gamePointWinner = new TwoWayStatistic("P:PW", marketDescription, generateMarket & matchNotAbandoned,
                        thisPointSequenceId, "A", "B");

        marketDescription = String.format("Game %d Point %d Winner", currentSetNo, currentPointNo + 4);
        thisPointSequenceId = matchState.getSequenceIdForPoint(4);
        gamePointWinnerNext = new TwoWayStatistic("P:PW", marketDescription, generateMarket & matchNotAbandoned,
                        thisPointSequenceId, "A", "B");

        thisPointSequenceId = matchState.getSequenceIdForPoint(5);
        marketDescription = String.format("Game %d Point %d Winner", currentSetNo, currentPointNo + 5);
        gamePointWinnerNPlus2 = new TwoWayStatistic("P:PW", marketDescription, generateMarket & matchNotAbandoned,
                        thisPointSequenceId, "A", "B");

        gameCorrectScore = new CorrectScoreStatistic("FT:CS", "Correct Score of Game ",
                        generateMarket & matchNotAbandoned, "M", maxPointScore);
        marketDescription = String.format("Game %d Correct Score", currentSetNo);
        gameXCorrectScoreNWay = new NWayStatistic("P:CS", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, gameCorrectScoreSelections);

        marketDescription = String.format("Game %d Winning Margin", currentSetNo);
        gameXWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, winningMarginSelections);

        totalPointsA = new TotalStatistic("FT:A:OU", "Total points A", generateMarket & matchNotAbandoned, "M",
                        maxPointScoreA);
        totalPointsB = new TotalStatistic("FT:B:OU", "Total points B", generateMarket & matchNotAbandoned, "M",
                        maxPointScoreA);

        String thisRaceFrameSequenceId = String.format("R%s.0%d", currentSetNo, 5);
        String thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 10);
        marketDescription = String.format("Game %d Race to %d Points ", currentSetNo, 5);
        generateMarket = currentPointNoA < 5 && currentPointNoB < 5;
        raceToFivePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket & matchNotAbandoned,
                        thisRaceFrameSequenceId, "A", "B");
        generateMarket = currentPointNo < 10;
        marketDescription = String.format("Game %d Lead After %d Points ", currentSetNo, 10);
        leadAfterTenPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket & matchNotAbandoned,
                        thisLeadFrameSequenceId, "A", "B", "Draw");
        generateMarket = currentPointNoA < 10 && currentPointNoB < 10;
        thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 10);
        marketDescription = String.format("Game %d Race to %d Points ", currentSetNo, 10);
        raceToTenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket & matchNotAbandoned,
                        thisRaceFrameSequenceId, "A", "B");
        generateMarket = currentPointNo < 20;
        marketDescription = String.format("Game %d Lead After %d Points ", currentSetNo, 20);
        thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 20);
        leadAfterTwentyPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket & matchNotAbandoned,
                        thisLeadFrameSequenceId, "A", "B", "Draw");
        generateMarket = currentPointNoA < 15 && currentPointNoB < 15;
        thisRaceFrameSequenceId = String.format("R%s.%d", currentSetNo, 15);
        marketDescription = String.format("Game %d Race to %d Points ", currentSetNo, 15);
        raceToFifteenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket & matchNotAbandoned,
                        thisRaceFrameSequenceId, "A", "B");
        generateMarket = currentPointNo < 30;
        marketDescription = String.format("Game %d Lead After %d Points ", currentSetNo, 30);
        thisLeadFrameSequenceId = String.format("L%s.%d", currentSetNo, 30);
        leadAfterThirtyPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket & matchNotAbandoned,
                        thisLeadFrameSequenceId, "A", "B", "Draw");

        int nextSetNo = currentSetNo + 1;
        thisSetSequenceId = matchState.getSequenceIdforGame(1);
        generateMarket = nextSetNo <= setNo;
        marketDescription = String.format("Game %d Winner ", nextSetNo);
        nextGameMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "A", "B");
        marketDescription = String.format("Game %d Extra Point ", nextSetNo);
        nextGameExtraPoints = new TwoWayStatistic("P:ET", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "Yes", "No");
        marketDescription = String.format("Game %d Total Points ", nextSetNo);
        nextGamePointTotal = new TotalStatistic("P:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points A", nextSetNo);
        nextGamePointTotalA = new TotalStatistic("P:A:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points B", nextSetNo);
        nextGamePointTotalB = new TotalStatistic("P:B:OU", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScore);
        marketDescription = String.format("Game %d Total Points Odd/Even", nextSetNo);
        nextGamePointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, "Odd", "Even");
        marketDescription = String.format("Game %d Point Handicap ", nextSetNo);
        nextGamePointsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, maxPointScoreA);
        marketDescription = String.format("Game %d Correct Score", nextSetNo);
        nextGameXCorrectScoreNWay = new NWayStatistic("P:CS", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, gameCorrectScoreSelections);

        marketDescription = String.format("Game %d Winning Margin", nextSetNo);
        nextGameXWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket & matchNotAbandoned,
                        thisSetSequenceId, winningMarginSelections);

        thisRaceFrameSequenceId = String.format("R%s.0%d", nextSetNo, 5);
        thisLeadFrameSequenceId = String.format("L%s.%d", nextSetNo, 10);
        marketDescription = String.format("Game %d Race to %d Points ", nextSetNo, 5);
        nextGameraceToFivePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket & matchNotAbandoned,
                        thisRaceFrameSequenceId, "A", "B");
        marketDescription = String.format("Game %d Lead After %d Points ", nextSetNo, 10);
        nextGameleadAfterTenPoints = new ThreeWayStatistic("P:LEAD", marketDescription,
                        generateMarket & matchNotAbandoned, thisLeadFrameSequenceId, "A", "B", "Draw");
        thisRaceFrameSequenceId = String.format("R%s.%d", nextSetNo, 10);
        marketDescription = String.format("Game %d Race to %d Points ", nextSetNo, 10);
        nextGameraceToTenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket & matchNotAbandoned,
                        thisRaceFrameSequenceId, "A", "B");
        marketDescription = String.format("Game %d Lead After %d Points ", nextSetNo, 20);
        thisLeadFrameSequenceId = String.format("L%s.%d", nextSetNo, 20);
        nextGameleadAfterTwentyPoints = new ThreeWayStatistic("P:LEAD", marketDescription,
                        generateMarket & matchNotAbandoned, thisLeadFrameSequenceId, "A", "B", "Draw");
        thisRaceFrameSequenceId = String.format("R%s.%d", nextSetNo, 15);
        marketDescription = String.format("Game %d Race to %d Points ", nextSetNo, 15);
        nextGameraceToFifteenPoints = new TwoWayStatistic("P:RACE", marketDescription,
                        generateMarket & matchNotAbandoned, thisRaceFrameSequenceId, "A", "B");
        marketDescription = String.format("Game %d Lead After %d Points ", nextSetNo, 30);
        thisLeadFrameSequenceId = String.format("L%s.%d", nextSetNo, 30);
        nextGameleadAfterThirtyPoints = new ThreeWayStatistic("P:LEAD", marketDescription,
                        generateMarket & matchNotAbandoned, thisLeadFrameSequenceId, "A", "B", "Draw");

    }

    void updateStats(BadmintonMatchState matchState, BadmintonMatchFacts matchFacts) {
        int setsA = matchState.getGamesA();
        int setsB = matchState.getGamesB();
        int pointsA1;
        int pointsB1;
        int sumA = 0;
        int sumB = 0;
        int currentSetToalPoints = 0;
        int currentSetHandicapPoints = 0;
        int nextSetToalPoints = 0;
        int nextSetHandicapPoints = 0;
        double pA = matchFacts.pointWinningA;
        double pA1;
        boolean currentSetWInnerisA = false;
        boolean nextSetWInnerisA = false;
        boolean currentSetIsFinalSet = ((setsA + setsB) == 2 && (currentSetNo == 2));
        for (int i = 0; i < setNo; i++) {
            pointsA1 = matchState.getGameScoreInGameN(i).A;
            pointsB1 = matchState.getGameScoreInGameN(i).B;
            sumA += pointsA1;
            sumB += pointsB1;
            if (i == currentSetNo - 1) {
                currentSetWInnerisA = pointsA1 > pointsB1;
                currentSetToalPoints = pointsA1 + pointsB1;
                currentSetHandicapPoints = pointsA1 - pointsB1;
            }
            if (i == currentSetNo) {
                nextSetWInnerisA = pointsA1 > pointsB1;
                nextSetToalPoints = pointsA1 + pointsB1;
                nextSetHandicapPoints = pointsA1 - pointsB1;
            }
        }
        gameCorrectScore.increment(setsA, setsB);
        TeamId teamId = TeamId.UNKNOWN;
        if (setsA > setsB)
            teamId = TeamId.A;
        if (setsA < setsB)
            teamId = TeamId.B;
        if (evenNosets)
            matchWinnersetsEven.increment(teamId);
        else
            matchWinnersetsOdd.increment(teamId == TeamId.A);
        pointsTotal.increment(sumA + sumB);
        totalPointsA.increment(sumA);
        totalPointsB.increment(sumB);
        pointsHandicap.increment(sumA - sumB);
        setsHandicap.increment(setsA - setsB);
        pointsTotalOddEven.increment(isOdd(sumA + sumB));

        gameMoneyLine.increment(currentSetWInnerisA);
        gamePointTotal.increment(currentSetToalPoints);
        gamePointTotalA.increment((currentSetToalPoints + currentSetHandicapPoints) / 2);
        gamePointTotalB.increment((currentSetToalPoints - currentSetHandicapPoints) / 2);
        gamePointsHandicap.increment(currentSetHandicapPoints);
        gamePointsTotalOddEven.increment(isOdd(currentSetToalPoints));

        double r = RandomNoGenerator.nextDouble();
        boolean pointWonByA;
        boolean pointWonNextByA;
        boolean pointWonNextPlusByA;
        pA1 = pA;
        pointWonByA = r < pA1;
        r = RandomNoGenerator.nextDouble();
        pointWonNextByA = r < pA1;
        r = RandomNoGenerator.nextDouble();
        pointWonNextPlusByA = r < pA1;
        gamePointWinner.increment(pointWonByA);
        gamePointWinnerNext.increment(pointWonNextByA);
        gamePointWinnerNPlus2.increment(pointWonNextPlusByA);

        int n = 0;
        int home;
        int away;
        int a = (currentSetToalPoints + currentSetHandicapPoints) / 2;
        int b = (currentSetToalPoints - currentSetHandicapPoints) / 2;
        gameExtraPoints.increment(a > 21 || b > 21);
        for (int i = 0; i < gameCorrectScoreSelections.length - 2; i++) {
            String[] scs = gameCorrectScoreSelections[i].split("-");

            home = Integer.parseInt(scs[0]);
            away = Integer.parseInt(scs[1].substring(0, 2));

            if ((home == a) && (away == b)) {
                n = i;
                break;
            }
        }
        if (b <= 13) {
            n = 0;
        }
        if (a <= 13) {
            n = 7;
        }
        if (b > 21 && b > a) {
            n = gameCorrectScoreSelections.length - 1;
        }
        if (a > 21 && a > b) {
            n = gameCorrectScoreSelections.length - 2;
        }
        gameXCorrectScoreNWay.increment(n);
        if (currentSetHandicapPoints > 0) {
            if (currentSetHandicapPoints >= 9)
                n = 3;
            else if (currentSetHandicapPoints >= 6)
                n = 2;
            else if (currentSetHandicapPoints >= 3)
                n = 1;
            else
                n = 0;
        } else {
            if (Math.abs(currentSetHandicapPoints) >= 9)
                n = 7;
            else if (Math.abs(currentSetHandicapPoints) >= 6)
                n = 6;
            else if (Math.abs(currentSetHandicapPoints) >= 3)
                n = 5;
            else
                n = 4;
        }
        gameXWinningMarginNWay.increment(n);

        if (currentPointNoA < 5 && currentPointNoB < 5)
            raceToFivePoints.increment(matchFacts.point5WinnerIsA);
        if (currentPointNo < 10)
            leadAfterTenPoints.increment(matchFacts.point10LeadIsA);
        if (currentPointNoA < 10 && currentPointNoB < 10)
            raceToTenPoints.increment(matchFacts.point10WinnerIsA);
        if (currentPointNo < 20)
            leadAfterTwentyPoints.increment(matchFacts.point20LeadIsA);
        if (currentPointNoA < 15 && currentPointNoB < 15)
            raceToFifteenPoints.increment(matchFacts.point15WinnerIsA);
        if (currentPointNo < 30)
            leadAfterThirtyPoints.increment(matchFacts.point30LeadIsA);
        if (!currentSetIsFinalSet) {
            nextGameMoneyLine.increment(nextSetWInnerisA);
            nextGamePointTotal.increment(nextSetToalPoints);
            nextGamePointTotalA.increment((nextSetToalPoints + nextSetHandicapPoints) / 2);
            nextGamePointTotalB.increment((nextSetToalPoints - nextSetHandicapPoints) / 2);
            nextGamePointsHandicap.increment(nextSetHandicapPoints);
            nextGamePointsTotalOddEven.increment(isOdd(nextSetToalPoints));
        }
        int a1 = (nextSetToalPoints + nextSetHandicapPoints) / 2;
        int b1 = (nextSetToalPoints - nextSetHandicapPoints) / 2;
        if (!currentSetIsFinalSet)
            nextGameExtraPoints.increment(a1 > 21 || b1 > 21);

        for (int i = 0; i < gameCorrectScoreSelections.length - 2; i++) {
            String[] scs = gameCorrectScoreSelections[i].split("-");

            home = Integer.parseInt(scs[0]);
            away = Integer.parseInt(scs[1].substring(0, 2));

            if ((home == a1) && (away == b1)) {
                n = i;
                break;
            }
        }
        if (b1 <= 13) {
            n = 0;
        }
        if (a1 <= 13) {
            n = 7;
        }
        if (b1 > 21 && b1 > a1) {
            n = gameCorrectScoreSelections.length - 2;
        }
        if (a1 > 21 && a1 > b1) {
            n = gameCorrectScoreSelections.length - 1;
        }
        if (!currentSetIsFinalSet)
            nextGameXCorrectScoreNWay.increment(n);
        if (nextSetHandicapPoints > 0) {
            if (nextSetHandicapPoints >= 9)
                n = 3;
            else if (nextSetHandicapPoints >= 6)
                n = 2;
            else if (nextSetHandicapPoints >= 3)
                n = 1;
            else
                n = 0;
        } else {
            if (Math.abs(nextSetHandicapPoints) >= 9)
                n = 7;
            else if (Math.abs(nextSetHandicapPoints) >= 6)
                n = 6;
            else if (Math.abs(nextSetHandicapPoints) >= 3)
                n = 5;
            else
                n = 4;
        }
        if (!currentSetIsFinalSet)
            nextGameXWinningMarginNWay.increment(n);
        if (!currentSetIsFinalSet) {
            if (matchFacts.nextGamepoint5WinnerIsA.equals(TeamId.A))
                nextGameraceToFivePoints.increment(true);
            if (matchFacts.nextGamepoint5WinnerIsA.equals(TeamId.B))
                nextGameraceToFivePoints.increment(false);
            if (matchFacts.nextGamepoint10WinnerIsA.equals(TeamId.A))
                nextGameraceToTenPoints.increment(true);
            else
                nextGameraceToTenPoints.increment(false);

            if (matchFacts.nextGamepoint15WinnerIsA.equals(TeamId.A))
                nextGameraceToFifteenPoints.increment(true);
            else
                nextGameraceToFifteenPoints.increment(false);
            nextGameleadAfterTenPoints.increment(matchFacts.nextGamepoint10LeadIsA);

            nextGameleadAfterTwentyPoints.increment(matchFacts.nextGamepoint20LeadIsA);

            nextGameleadAfterThirtyPoints.increment(matchFacts.nextGamepoint30LeadIsA);
        }

    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }

}
