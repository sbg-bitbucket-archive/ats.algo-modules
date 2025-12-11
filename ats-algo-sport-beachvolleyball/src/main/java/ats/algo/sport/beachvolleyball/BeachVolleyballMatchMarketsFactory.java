package ats.algo.sport.beachvolleyball;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatch.BeachVolleyballMatchFacts;

public class BeachVolleyballMatchMarketsFactory extends MarketsFactory {

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
    private TotalStatistic pointsTotal;
    private TotalStatistic setPointTotal;
    private TotalStatistic homeTotalPoints;
    private TotalStatistic awayTotalPoints;
    private HandicapStatistic setsHandicap;
    private HandicapStatistic pointsHandicap;
    private HandicapStatistic setPointsHandicap;
    private TwoWayStatistic setMoneyLine;
    private TwoWayStatistic nextSetMoneyLine;
    private TwoWayStatistic setPointWinnerNext;
    private TwoWayStatistic setPointWinnerNPlus2;
    // private CorrectScoreStatistic matchCorrectsetScore;
    private int setNo;
    private int currentSetNo;
    private boolean evenNosets;
    private int currentPointNo;
    private int currentPointNoA;
    private int currentPointNoB;
    private boolean finalSet;

    BeachVolleyballMatchMarketsFactory(BeachVolleyballMatchState matchState) {
        setNo = matchState.getSetScoreForWin() * 2 - 1;
        currentSetNo = matchState.getSetsA() + matchState.getSetsB() + 1;
        currentPointNo = matchState.getPointsA() + matchState.getPointsB();
        currentPointNoA = matchState.getPointsA();
        currentPointNoB = matchState.getPointsB();
        int maxPointScore = 700;
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
        setPointsHandicap = new HandicapStatistic("P:PSPRD", marketDescription, generateMarket, thisSetSequenceId,
                        maxPointScoreA);
        // if ((currentSetNo + currentPointNo) % 3 == 1) {
        marketDescription = String.format("Set %d Point %d Winner", currentSetNo, currentPointNo + 2);
        sequenceId = matchState.getSequenceIdForPoint(2);
        setPointWinner = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        marketDescription = String.format("Set %d Point %d Winner", currentSetNo, currentPointNo + 3);
        sequenceId = matchState.getSequenceIdForPoint(3);
        setPointWinnerNext = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");

        sequenceId = matchState.getSequenceIdForPoint(4);
        marketDescription = String.format("Set %d Point %d Winner", currentSetNo, currentPointNo + 4);
        setPointWinnerNPlus2 = new TwoWayStatistic("P:PW", marketDescription, generateMarket, sequenceId, "A", "B");



        // } else if ((currentSetNo + currentPointNo) % 3 == 2) {
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 2);
        // sequenceId = matchState.getSequenceIdForPoint(2);
        // setPointWinner = new TwoWayStatistic("P:PW2", marketDescription,
        // generateMarket, sequenceId, "A", "B");
        // sequenceId = matchState.getSequenceIdForPoint(3);
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 3);
        // setPointWinnerNext = new TwoWayStatistic("P:PW3", marketDescription,
        // generateMarket, sequenceId, "A", "B");
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 4);
        // sequenceId = matchState.getSequenceIdForPoint(4);
        // setPointWinnerNPlus2 = new TwoWayStatistic("P:PW1",
        // marketDescription, generateMarket, sequenceId, "A",
        // "B");
        //
        // } else if ((currentSetNo + currentPointNo) % 3 == 0) {
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 2);
        // sequenceId = matchState.getSequenceIdForPoint(2);
        // setPointWinner = new TwoWayStatistic("P:PW3", marketDescription,
        // generateMarket, sequenceId, "A", "B");
        // sequenceId = matchState.getSequenceIdForPoint(3);
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 3);
        // setPointWinnerNext = new TwoWayStatistic("P:PW1", marketDescription,
        // generateMarket, sequenceId, "A", "B");
        // marketDescription = String.format("Set %d, Point %d Winner",
        // currentSetNo, currentPointNo + 4);
        // sequenceId = matchState.getSequenceIdForPoint(4);
        // setPointWinnerNPlus2 = new TwoWayStatistic("P:PW2",
        // marketDescription, generateMarket, sequenceId, "A",
        // "B");
        // }

        homeTotalPoints = new TotalStatistic("FT:A:OU", "Home Team total points", generateMarket, "M", maxPointScoreA);
        awayTotalPoints = new TotalStatistic("FT:B:OU", "Away Team total points", generateMarket, "M", maxPointScoreA);
        // matchCorrectsetScore = new CorrectScoreStatistic("FT:CS", "Correct
        // Set Score", true, "M", setNo + 1);
        // correctsetScore = new CorrectScoreStatistic("SB", "Match set correct
        // score", true, "M", setNo+1);

        // setsTotalA = new TotalStatistic("FT:STA", "Match total sets A",
        // maxsetScore);
        // setsTotalB = new TotalStatistic("FT:STB", "Match total sets B",
        // maxsetScore);

        // setWinnerNext = new TwoWayStatistic("FT:STN", "set winner - next
        // set", "A", "B");
        // setWinnerNPlus2 = new TwoWayStatistic("FT:SWP", "set winner - set+2",
        // "A", "B");

        int nextSetNo = currentSetNo + 1;
        generateMarket = nextSetNo <= setNo;
        marketDescription = String.format("Set %d Money Line ", nextSetNo);
        thisSetSequenceId = matchState.getSequenceIdforSet(1);
        nextSetMoneyLine = new TwoWayStatistic("P:ML", marketDescription, generateMarket, thisSetSequenceId, "A", "B");
    }

    void updateStats(BeachVolleyballMatchState matchState, BeachVolleyballMatchFacts matchFacts) {
        BeachVolleyballMatchFormat beachVolleyballMatchFormat =
                        (BeachVolleyballMatchFormat) matchState.getMatchFormat();
        int nPointInRegularSet = beachVolleyballMatchFormat.getnPointInRegularSet();
        int nPointInFinalSet = beachVolleyballMatchFormat.getnPointInFinalSet();
        int setsA = matchState.getSetsA();
        int setsB = matchState.getSetsB();
        int pointsA1 = 0;
        int pointsB1 = 0;
        int sumA = 0;
        int sumB = 0;
        int currentSetToalPoints = 0;
        int currentSetHandicapPoints = 0;
        boolean serveA = matchFacts.serveIsA;
        double pA = matchFacts.pointWinningA;
        double pB = matchFacts.pointWinningB;
        double pA1 = 0.0;
        double pB1 = 0.0;
        double pA2 = 0.0;
        double pB2 = 0.0;
        double pA3 = 0.0;
        double pB3 = 0.0;
        boolean currentSetWInnerisA = false;
        boolean nextSetWInnerisA = false;
        for (int i = 0; i < setNo; i++) {
            pointsA1 = matchState.getGameScoreInSetN(i).A;
            pointsB1 = matchState.getGameScoreInSetN(i).B;
            sumA += pointsA1;
            sumB += pointsB1;
            if (i == currentSetNo - 1) {
                currentSetWInnerisA = pointsA1 > pointsB1;
                currentSetToalPoints = pointsA1 + pointsB1;
                currentSetHandicapPoints = pointsA1 - pointsB1;
                // System.out.println(currentSetNo+"--"+pointsA1+"----"+pointsB1+"---");
            }
            if (i == currentSetNo) {
                nextSetWInnerisA = pointsA1 > pointsB1;
                // currentSetToalPoints = pointsA1 + pointsB1;
                // currentSetHandicapPoints = pointsA1 - pointsB1;
                // System.out.println(currentSetNo+"--"+pointsA1+"----"+pointsB1+"---");
            }
            // System.out.println(pointsA1+"----in--"+pointsB1);
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
        // matchCorrectsetScore.increment(setsA, setsB);
        pointsHandicap.increment(sumA - sumB);
        homeTotalPoints.increment(sumA);
        awayTotalPoints.increment(sumB);
        setMoneyLine.increment(currentSetWInnerisA);
        setPointTotal.increment(currentSetToalPoints);
        setPointsHandicap.increment(currentSetHandicapPoints);
        nextSetMoneyLine.increment(nextSetWInnerisA);

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

        // correctsetScore.increment(setsA, setsB);
        // System.out.println(sumA+"----"+sumB);
        //

        // setsTotal.increment(setsA + setsB);
        // setsTotalA.increment(setsA);
        // setsTotalB.increment(setsB);
        //
        // setWinnerCurrent.increment(matchFacts.setWinnerCurrentIsA);
        // if (matchFacts.setNextIsPlayed)
        // setWinnerNext.increment(matchFacts.setWinnerNextIsA);
        // if (matchFacts.setNPlus2IsPlayed)
        // setWinnerNPlus2.increment(matchFacts.setWinnerNPlus2IsA);
    }

}
