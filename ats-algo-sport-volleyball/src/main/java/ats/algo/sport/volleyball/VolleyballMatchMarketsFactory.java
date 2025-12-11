package ats.algo.sport.volleyball;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.volleyball.VolleyballMatch.VolleyballMatchFacts;

public class VolleyballMatchMarketsFactory extends MarketsFactory {

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
    private TwoWayStatistic setPointWinner;
    private CorrectScoreStatistic scoreAfterTwoSet;
    private CorrectScoreStatistic scoreAfterThreeSet;
    private CorrectScoreStatistic scoreAfterFourSet;
    private TotalStatistic pointsTotal;
    private TotalStatistic setsTotal;
    private TotalStatistic setPointTotal;
    private TotalStatistic homeTotalPoints;
    private TotalStatistic awayTotalPoints;
    private HandicapStatistic setsHandicap;
    private HandicapStatistic pointsHandicap;
    private HandicapStatistic setPointsHandicap;
    private TwoWayStatistic setMoneyLine;
    private TwoWayStatistic setPointWinnerNext;
    private TwoWayStatistic setPointWinnerNPlus2;
    private NWayStatistic setXCorrectScoreNWay;
    private CorrectScoreStatistic matchCorrectsetScore;

    private TwoWayStatistic raceToFivePoints;
    private ThreeWayStatistic leadAfterTenPoints;
    private TwoWayStatistic raceToTenPoints;
    private ThreeWayStatistic leadAfterTwentyPoints;
    private TwoWayStatistic raceToFifteenPoints;
    private ThreeWayStatistic leadAfterThirtyPoints;
    private TwoWayStatistic setExtraPoints;
    private TwoWayStatistic pointsTotalOddEven;
    private TwoWayStatistic setPointsTotalOddEven;
    private NWayStatistic setWinningMarginNWay;
    private TwoWayStatistic homeWinAtLeastOneSet;
    private TwoWayStatistic awayWinAtLeastOneSet;
    private TwoWayStatistic homeWinFirstSetandMatch;
    private TwoWayStatistic awayWinFirstSetandMatch;
    private int setNo;
    private int currentSetNo;
    private int currentPointNo;
    private int currentPointNoA;
    private int currentPointNoB;
    private boolean evenNosets;
    private boolean finalSet;
    private String[] setCorrectScoreSelections = {"A 25-15 or better", "A 25-16", "A 25-17", "A 25-18", "A 25-19",
            "A 25-20", "A 25-21", "A 25-22", "A 25-23", "B 15-25 or better", "B 25-16", "B 25-17", "B 25-18", "B 25-19",
            "B 25-20", "B 25-21", "B 25-22", "B 25-23", "after extra points A", "after extra points B"};
    private String[] finalSetCorrectScoreSelections = {"A 15-10 or better", "A 15-11", "A 15-12", "A 15-13",
            "B 15-10 or better", "B 15-11", "B 15-12", "B 15-13", "after extra points A", "after extra points B"};
    private String[] winningMarginSelections = {"A 1-2", "A 3-5", "A 6-8", "A 9+", "B 1-2", "B 3-5", "B 6-8", "B 9+"};
    private boolean allowGenerateAllSelection = true;
    private boolean correctScoreDisplayHasTeamId = true;
    private int simulationLimit = 200;

    VolleyballMatchMarketsFactory(VolleyballMatchState matchState) {
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        this.setCorrectScoreDisplayHasTeamId(correctScoreDisplayHasTeamId);
        this.setSimulationLimit(simulationLimit);
        VolleyballMatchFormat volleyballMatchFormat = (VolleyballMatchFormat) matchState.getMatchFormat();
        int nPointInRegularSet = volleyballMatchFormat.getnPointInRegularSet();
        int nPointInFinalSet = volleyballMatchFormat.getnPointInFinalSet();
        setNo = matchState.getSetScoreForWin() * 2 - 1;
        finalSet = matchState.isInFinalSet();
        currentSetNo = matchState.getSetsA() + matchState.getSetsB() + 1;
        currentPointNo = matchState.getPointsA() + matchState.getPointsB();
        currentPointNoA = matchState.getPointsA();
        currentPointNoB = matchState.getPointsB();
        int maxPointScore = 1000;
        int maxPointScoreA = 500;
        int maxHandicapMatch = 200;

        String sequenceId;
        String thisSetSequenceId = matchState.getSequenceIdforSet(0);
        Boolean generateMarket = true;
        matchWinnersetsOdd = new TwoWayStatistic("FT:ML", "Money line", generateMarket, "M", "A", "B");
        pointsTotal = new TotalStatistic("FT:OU", "Total Points", generateMarket, "M", maxPointScore);
        setsHandicap = new HandicapStatistic("FT:SPRD", "Set Handicap", generateMarket, "M", maxHandicapMatch);
        pointsHandicap = new HandicapStatistic("FT:PSPRD", "Point Handicap", generateMarket, "M", maxPointScoreA);
        String marketDescription = String.format("Set %d Money Line ", currentSetNo);
        setMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisSetSequenceId, "A", "B");

        marketDescription = String.format("Set %d Total Points ", currentSetNo);

        setPointTotal = new TotalStatistic("P:OU", marketDescription, generateMarket, thisSetSequenceId, maxPointScore);

        marketDescription = String.format("Set %d Point Handicap ", currentSetNo);
        setPointsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScoreA);
        marketDescription = String.format("Set %d, Point %d Winner", currentSetNo, currentPointNo + 2);
        sequenceId = matchState.getSequenceIdForPoint(1);
        setPointWinner = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        marketDescription = String.format("Set %d, Point %d Winner", currentSetNo, currentPointNo + 3);
        sequenceId = matchState.getSequenceIdForPoint(2);
        setPointWinnerNext = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = matchState.getSequenceIdForPoint(3);
        marketDescription = String.format("Set %d, Point %d Winner", currentSetNo, currentPointNo + 4);
        setPointWinnerNPlus2 = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");


        homeTotalPoints = new TotalStatistic("FT:A:OU", "Home Team Total Points", generateMarket, "M", maxPointScoreA);
        awayTotalPoints = new TotalStatistic("FT:B:OU", "Away Team Total Points", generateMarket, "M", maxPointScoreA);
        if (setNo == 5) {
            if (currentSetNo < 3) {
                thisSetSequenceId = String.format("E%d", 2);
                scoreAfterTwoSet = new CorrectScoreStatistic("P:CS2", "Correct Score after sets 2", generateMarket,
                                thisSetSequenceId, maxHandicapMatch);
            }
            if (currentSetNo < 4) {
                thisSetSequenceId = String.format("E%d", 3);
                scoreAfterThreeSet = new CorrectScoreStatistic("P:CS3", "Correct Score after sets 3", generateMarket,
                                thisSetSequenceId, maxHandicapMatch);
            }

            if (currentSetNo < 5) {
                thisSetSequenceId = String.format("E%d", 4);
                scoreAfterFourSet = new CorrectScoreStatistic("P:CS4", "Correct Score after sets 4", generateMarket,
                                thisSetSequenceId, maxHandicapMatch);
            }
        }
        if (setNo == 3) {
            if (currentSetNo < 3) {
                thisSetSequenceId = String.format("E%d", 2);
                scoreAfterTwoSet = new CorrectScoreStatistic("P:CS2", "Correct Score after sets 2", generateMarket,
                                thisSetSequenceId, maxHandicapMatch);
            }
            if (currentSetNo < 4) {
                thisSetSequenceId = String.format("E%d", 3);
                scoreAfterThreeSet = new CorrectScoreStatistic("P:CS3", "Correct Score after sets 3", generateMarket,
                                thisSetSequenceId, maxHandicapMatch);
            }
        }

        marketDescription = String.format("Set %d correct score", currentSetNo);
        thisSetSequenceId = String.format("C%d", currentSetNo);
        if (!finalSet && nPointInRegularSet == 25) {
            setXCorrectScoreNWay = new NWayStatistic("P:CS", marketDescription, generateMarket, thisSetSequenceId,
                            setCorrectScoreSelections);
        } else if (finalSet && nPointInFinalSet == 15)
            setXCorrectScoreNWay = new NWayStatistic("P:CS", marketDescription, generateMarket, thisSetSequenceId,
                            finalSetCorrectScoreSelections);
        matchCorrectsetScore = new CorrectScoreStatistic("FT:CS", "Correct Set Score", true, "M", setNo + 1);
        marketDescription = String.format("Set %d Extra Points", currentSetNo);
        thisSetSequenceId = String.format("X%d", currentSetNo);
        setExtraPoints = new TwoWayStatistic("P:EP", marketDescription, generateMarket, thisSetSequenceId, "Yes", "No");

        generateMarket = currentPointNoA < 5 && currentPointNoB < 5;
        String raceFrame = "R%s.%d";

        String thisRaceFrameSequenceId = String.format(raceFrame, currentSetNo, 5);
        String leadeFrame = "L%s.%d";
        String thisLeadFrameSequenceId = String.format(leadeFrame, currentSetNo, 10);
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 5);
        raceToFivePoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");
        generateMarket = currentPointNo < 10;
        marketDescription = String.format("Set %d Lead After %d Points ", currentSetNo, 10);
        leadAfterTenPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket, thisLeadFrameSequenceId,
                        "A", "B", "Draw");
        generateMarket = currentPointNoA < 10 && currentPointNoB < 10;
        thisRaceFrameSequenceId = String.format(raceFrame, currentSetNo, 10);
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 10);
        raceToTenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId, "A",
                        "B");
        generateMarket = currentPointNo < 20;
        marketDescription = String.format("Set %d Lead After %d Points ", currentSetNo, 20);
        thisLeadFrameSequenceId = String.format(leadeFrame, currentSetNo, 20);
        leadAfterTwentyPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket,
                        thisLeadFrameSequenceId, "A", "B", "Draw");
        generateMarket = currentPointNoA < 15 && currentPointNoB < 15 && !finalSet;
        thisRaceFrameSequenceId = String.format(raceFrame, currentSetNo, 15);
        marketDescription = String.format("Set %d Race to %d Points ", currentSetNo, 15);
        raceToFifteenPoints = new TwoWayStatistic("P:RACE", marketDescription, generateMarket, thisRaceFrameSequenceId,
                        "A", "B");
        generateMarket = currentPointNo < 30 && !finalSet;
        marketDescription = String.format("Set %d Lead After %d Points ", currentSetNo, 30);
        thisLeadFrameSequenceId = String.format(leadeFrame, currentSetNo, 30);
        leadAfterThirtyPoints = new ThreeWayStatistic("P:LEAD", marketDescription, generateMarket,
                        thisLeadFrameSequenceId, "A", "B", "Draw");
        generateMarket = true;
        setsTotal = new TotalStatistic("FT:OUS", "Total sets", generateMarket, "M", maxPointScore);
        pointsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Points Even/Odd", generateMarket, "M", "Odd", "Even");
        marketDescription = String.format("Set %d Total Points Even/Odd", currentSetNo);
        setPointsTotalOddEven = new TwoWayStatistic("P:OE", marketDescription, generateMarket, thisSetSequenceId, "Odd",
                        "Even");
        marketDescription = String.format("Set %d Winning Margin", currentSetNo);
        setWinningMarginNWay = new NWayStatistic("P:WM", marketDescription, generateMarket, thisSetSequenceId,
                        winningMarginSelections);
        marketDescription = "Home Win at least One Set";
        homeWinAtLeastOneSet = new TwoWayStatistic("FT:A:WAL1", marketDescription,
                        generateMarket && ((VolleyballMatchState) matchState).getSetsA() == 0, "M", "Yes", "No");
        marketDescription = "Away Win at least One Set";

        boolean awayWinAtLeastOneSetGeneratingMarket = ((VolleyballMatchState) matchState).getSetsB() == 0;
        awayWinAtLeastOneSet = new TwoWayStatistic("FT:B:WAL1", marketDescription, awayWinAtLeastOneSetGeneratingMarket,
                        "M", "Yes", "No");
        marketDescription = "Home Win First Set and Match";
        homeWinFirstSetandMatch =
                        new TwoWayStatistic("FT:A:WFSAM", marketDescription, generateMarket, "M", "Yes", "No");
        marketDescription = "Away Win First Set and Match";
        awayWinFirstSetandMatch =
                        new TwoWayStatistic("FT:B:WFSAM", marketDescription, generateMarket, "M", "Yes", "No");
    }

    void updateStats(VolleyballMatchState matchState, VolleyballMatchFacts matchFacts) {
        VolleyballMatchFormat volleyballMatchFormat = (VolleyballMatchFormat) matchState.getMatchFormat();
        int nPointInRegularSet = volleyballMatchFormat.getnPointInRegularSet();
        int nPointInFinalSet = volleyballMatchFormat.getnPointInFinalSet();
        int setsA = matchState.getSetsA();
        int setsB = matchState.getSetsB();
        int raceTwoSetA = 0;
        int raceTwoSetB = 0;
        int raceThreeSetA = 0;
        int raceThreeSetB = 0;
        int raceFourSetA = 0;
        int raceFourSetB = 0;
        int pointsA1;
        int pointsB1;
        int pointsA2 = 0;
        int pointsB2 = 0;
        int sumA = 0;
        int sumB = 0;
        int currentSetToalPoints = 0;
        int currentSetHandicapPoints = 0;
        boolean serveA = matchFacts.serveIsA;
        double pA = matchFacts.pointWinningA;
        double pB = matchFacts.pointWinningB;
        double pA1;
        double pB1;
        double pA2;
        double pB2;
        double pA3;
        double pB3;
        boolean currentSetWInnerisA = false;
        boolean firstSetWInnerisA = false;
        boolean setExtraPoint = false;
        for (int i = 0; i < setNo; i++) {
            pointsA1 = matchState.getGameScoreInSetN(i).A;
            pointsB1 = matchState.getGameScoreInSetN(i).B;
            if (i == 0)
                firstSetWInnerisA = pointsA1 > pointsB1;
            sumA += pointsA1;
            sumB += pointsB1;
            if (i == 0 || i == 1) {
                if (pointsA1 > pointsB1)
                    raceTwoSetA++;
                else
                    raceTwoSetB++;
            }
            if (i == 0 || i == 1 || i == 2) {
                if (pointsA1 > pointsB1)
                    raceThreeSetA++;
                else
                    raceThreeSetB++;
            }
            if (i == 0 || i == 1 || i == 2 || i == 3) {
                if ((pointsA1 > pointsB1) && (pointsA1 > 0 || pointsB1 > 0))
                    raceFourSetA++;
                else if (pointsB1 > pointsA1)
                    raceFourSetB++;
            }
            if (i == currentSetNo - 1) {
                currentSetWInnerisA = pointsA1 > pointsB1;
                currentSetToalPoints = pointsA1 + pointsB1;
                currentSetHandicapPoints = pointsA1 - pointsB1;
                pointsA2 = pointsA1;
                pointsB2 = pointsB1;
                if (!finalSet) {
                    if ((pointsA2 > nPointInRegularSet) || (pointsB2 > nPointInRegularSet)) {
                        setExtraPoint = true;
                    }
                } else {
                    if ((pointsA2 > nPointInFinalSet) || (pointsB2 > nPointInFinalSet)) {
                        setExtraPoint = true;
                    }
                }

            }

        }
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
        setsHandicap.increment(setsA - setsB);
        pointsHandicap.increment(sumA - sumB);
        homeTotalPoints.increment(sumA);
        awayTotalPoints.increment(sumB);
        setMoneyLine.increment(currentSetWInnerisA);
        setPointTotal.increment(currentSetToalPoints);
        setPointsHandicap.increment(currentSetHandicapPoints);
        double r = RandomNoGenerator.nextDouble();
        boolean pointWonByA;
        boolean pointWonNextByA;
        boolean pointWonNextPlusByA;
        if (serveA) {
            pA1 = pA * pA + (1 - pA) * (1 - pB);
            pA2 = pA * pA1 + (1 - pA1) * (1 - pB);
            pA3 = pA * pA2 + (1 - pA2) * (1 - pB);
            pointWonByA = r < pA1;
            r = RandomNoGenerator.nextDouble();
            pointWonNextByA = r < pA2;
            r = RandomNoGenerator.nextDouble();
            pointWonNextPlusByA = r < pA3;
        } else {
            pB1 = pB * pB + (1 - pA) * (1 - pB);
            pB2 = pB * pB1 + (1 - pA) * (1 - pB1);
            pB3 = pB * pB2 + (1 - pA) * (1 - pB2);
            pointWonByA = r < (1 - pB1);
            r = RandomNoGenerator.nextDouble();
            pointWonNextByA = r < (1 - pB2);
            r = RandomNoGenerator.nextDouble();
            pointWonNextPlusByA = r < (1 - pB3);
        }
        if (finalSet) {
            if (currentPointNoA == (nPointInFinalSet - 1) && currentPointNoB < (nPointInFinalSet - 1)) {
                pB1 = pB2 = pB3 = pB;
                pointWonByA = r < (1 - pB1);
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < (1 - pB2);
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < (1 - pB3);
            }
            if (currentPointNoB == (nPointInFinalSet - 1) && currentPointNoA < (nPointInFinalSet - 1)) {
                pA1 = pA2 = pA3 = pA;
                pointWonByA = r < pA1;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < pA2;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < pA3;
            }
        } else {
            if (currentPointNoA == (nPointInRegularSet - 1) && currentPointNoB < (nPointInRegularSet - 1)) {
                pB1 = pB2 = pB3 = pB;
                pointWonByA = r < (1 - pB1);
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < (1 - pB2);
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < (1 - pB3);
            }
            if (currentPointNoB == (nPointInRegularSet - 1) && currentPointNoA < (nPointInRegularSet - 1)) {
                pA1 = pA2 = pA3 = pA;
                pointWonByA = r < pA1;
                r = RandomNoGenerator.nextDouble();
                pointWonNextByA = r < pA2;
                r = RandomNoGenerator.nextDouble();
                pointWonNextPlusByA = r < pA3;
            }
        }
        setPointWinner.increment(pointWonByA);
        setPointWinnerNext.increment(pointWonNextByA);
        setPointWinnerNPlus2.increment(pointWonNextPlusByA);
        if (setNo == 5) {
            if (currentSetNo < 3)
                scoreAfterTwoSet.increment(raceTwoSetA, raceTwoSetB);
            if (currentSetNo < 4)
                scoreAfterThreeSet.increment(raceThreeSetA, raceThreeSetB);
            if (currentSetNo < 5 && raceFourSetA + raceFourSetB == 4)
                scoreAfterFourSet.increment(raceFourSetA, raceFourSetB);
        }
        if (setNo == 3) {
            if (currentSetNo < 3)
                scoreAfterTwoSet.increment(raceTwoSetA, raceTwoSetB);
            if (currentSetNo < 4 && raceFourSetA + raceFourSetB == 3)
                scoreAfterThreeSet.increment(raceThreeSetA, raceThreeSetB);
        }
        matchCorrectsetScore.increment(setsA, setsB);
        int n = 0;
        int home;
        int away;
        if (!finalSet && nPointInRegularSet == 25) {
            for (int i = 0; i < setCorrectScoreSelections.length - 2; i++) {
                String[] scs = setCorrectScoreSelections[i].split("-");


                away = Integer.parseInt(scs[1].substring(0, 2));
                scs = scs[0].split(" ");
                home = Integer.parseInt(scs[1]);
                if ((home == pointsA2) && (away == pointsB2)) {
                    n = i;
                    break;
                }
            }
            if (pointsB2 <= 15) {
                n = 0;
            }
            if (pointsA2 <= 15) {
                n = 9;
            }
            if (pointsB2 > 25 && pointsB2 > pointsA2) {
                n = setCorrectScoreSelections.length - 1;
            }
            if (pointsA2 > 25 && pointsA2 > pointsB2) {
                n = setCorrectScoreSelections.length - 2;
            }
            setXCorrectScoreNWay.increment(n);
        } else if (finalSet && nPointInFinalSet == 15) {

            for (int i = 0; i < finalSetCorrectScoreSelections.length - 2; i++) {
                String[] scs = finalSetCorrectScoreSelections[i].split("-");

                away = Integer.parseInt(scs[1].substring(0, 2));
                scs = scs[0].split(" ");
                home = Integer.parseInt(scs[1]);

                if ((home == pointsA2) && (away == pointsB2)) {
                    n = i;
                    break;
                }
            }
            if (pointsB2 <= 10) {
                n = 0;
            }
            if (pointsA2 <= 10) {
                n = 4;
            }
            if (pointsB2 > 15 && pointsB2 > pointsA2) {
                n = finalSetCorrectScoreSelections.length - 1;
            }
            if (pointsA2 > 15 && pointsA2 > pointsB2) {
                n = finalSetCorrectScoreSelections.length - 2;
            }
            setXCorrectScoreNWay.increment(n);
        }
        if (currentPointNoA < 5 && currentPointNoB < 5)
            raceToFivePoints.increment(matchFacts.point5WinnerIsA);
        if (currentPointNo < 10)
            leadAfterTenPoints.increment(matchFacts.point10LeadIsA);
        if (currentPointNoA < 10 && currentPointNoB < 10)
            raceToTenPoints.increment(matchFacts.point10WinnerIsA);
        if (currentPointNo < 20)
            leadAfterTwentyPoints.increment(matchFacts.point20LeadIsA);
        if (currentPointNoA < 15 && currentPointNoB < 15) {
            if (matchFacts.point15Winner)
                raceToFifteenPoints.increment(matchFacts.point15WinnerIsA);
            else {
                if ((pointsA2 == 15) && (pointsB2 < 15))
                    raceToFifteenPoints.increment(true);
                else
                    raceToFifteenPoints.increment(false);
            }
        }

        if (currentPointNo < 30) {
            if (matchFacts.point30Lead)
                leadAfterThirtyPoints.increment(matchFacts.point30LeadIsA);
            else {
                if ((pointsA2 + pointsB2) == 30) {
                    if (pointsA2 > pointsB2) {
                        leadAfterThirtyPoints.increment(TeamId.A);
                    } else if (pointsA2 < pointsB2) {
                        leadAfterThirtyPoints.increment(TeamId.B);
                    } else
                        leadAfterThirtyPoints.increment(TeamId.UNKNOWN);
                }

            }
        }
        setExtraPoints.increment(setExtraPoint);

        setsTotal.increment(setsA + setsB);
        pointsTotalOddEven.increment(isOdd(sumA + sumB));
        setPointsTotalOddEven.increment(isOdd(currentSetToalPoints));
        if (setsA >= 1) {
            homeWinAtLeastOneSet.increment(true);
        } else {
            homeWinAtLeastOneSet.increment(false);
        }

        if (setsB >= 1) {
            awayWinAtLeastOneSet.increment(true);
        } else {
            awayWinAtLeastOneSet.increment(false);
        }
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
        setWinningMarginNWay.increment(n);
        if (firstSetWInnerisA) {
            if (setsA > setsB)
                homeWinFirstSetandMatch.increment(true);
            else
                homeWinFirstSetandMatch.increment(false);

        } else {
            if (setsA < setsB)
                awayWinFirstSetandMatch.increment(true);
            else
                awayWinFirstSetandMatch.increment(false);

        }
    }

    private boolean isOdd(int i) {
        return i % 2 == 1;
    }
}
