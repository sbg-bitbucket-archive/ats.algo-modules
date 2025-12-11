package ats.algo.sport.baseball;

import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.core.common.TeamId;
import ats.algo.sport.baseball.BaseballMatch.BaseballMatchFacts;
import ats.algo.sport.baseball.BaseballMatchState;

public class BaseballMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for example if we
     * need to know who won set N+2, where N is the five set, then we need to collect that "fact" in this class
     * 
     * @author Robert
     *
     */

    private TwoWayStatistic moneyLine;
    // private ThreeWayStatistic matchResult;
    private ThreeWayStatistic currentInningResult;
    private TotalStatistic currentInningTotalruns;
    private HandicapStatistic currentInningRunsHandicap;
    private ThreeWayStatistic threeInningResult;
    private TotalStatistic threeInningTotalruns;
    private HandicapStatistic threeInningRunsHandicap;
    private HandicapStatistic fiveInningRunsHandicap;
    private ThreeWayStatistic fiveInningResult;
    private TotalStatistic fiveInningTotalruns;
    private HandicapStatistic sevenInningRunsHandicap;
    private ThreeWayStatistic sevenInningResult;
    private TotalStatistic sevenInningTotalruns;

    private TotalStatistic runsATotal;
    private TotalStatistic runsBTotal;
    private TotalStatistic threeInningrunsATotal;
    private TotalStatistic threeInningrunsBTotal;
    private TotalStatistic fiveInningrunsATotal;
    private TotalStatistic fiveInningrunsBTotal;
    private TotalStatistic sevenInningrunsATotal;
    private TotalStatistic sevenInningrunsBTotal;
    private TotalStatistic runsTotal;
    private TwoWayStatistic extraInningPossible;
    private NWayStatistic winningMargin;

    private HandicapStatistic runsHandicap;

    // private ThreeWayStatistic hitsMoneyLine;
    // private TotalStatistic hitsATotal;
    // private TotalStatistic hitsBTotal;
    // private TotalStatistic hitsTotal;
    // private HandicapStatistic hitsHandicap;
    private String[] winningMarginList = {"-4+", "-3", "-2", "-1", "1", "2", "3", "4+"};
    private int currentInningNo;
    // private int inningNo;
    int totalRunsA;
    int totalRunsB;

    BaseballMatchMarketsFactory(BaseballMatchState matchState) {
        // totalRunsA = 0;
        // totalRunsB = 0;
        // inningNo = ((BasebaFllMatchFormat)
        // matchState.getMatchFormat()).getnInningsinMatch();
        int maxPointScoreA = 100;
        // int maxhitScoreA = 200;
        // int maxwicketScoreA = 20;
        // int maxextraScoreA = 100;
        // String thisWicketSequenceId = matchState.getSequenceIdforWicket(0);
        // String thisWicketCaughtSequenceId =
        // matchState.getSequenceIdforCaughtWicket(0);
        // String thisRaceFrameSequenceId = String.format("R%d", raceNumber);
        Boolean generateMarket = true;
        String fullTime = matchState.getSequenceIdForMatch();
        String marketDescription;
        moneyLine = new TwoWayStatistic("FT:ML", "Money Line", generateMarket, fullTime, "A", "B");
        // hitsMoneyLine = new ThreeWayStatistic("H:ML", "Hit Money Line",
        // generateMarket, fullTime, "A", "B", "Draw");
        // hitsATotal = new TotalStatistic("H:OU:A", "Total A hits",
        // generateMarket, fullTime, maxhitScoreA);
        // hitsBTotal = new TotalStatistic("H:OU:B", "Total B hits",
        // generateMarket, fullTime, maxhitScoreA);
        // hitsTotal = new TotalStatistic("H:OU", "Total hits", generateMarket,
        // fullTime, maxhitScoreA);
        // hitsHandicap = new HandicapStatistic("H:SPRD", "Hit Lines",
        // generateMarket, fullTime, maxPointScoreA);
        // matchResult = new ThreeWayStatistic("FT:AXB", "Match Result",
        // generateMarket, fullTime, "A", "B", "Draw");
        generateMarket = true;
        runsATotal = new TotalStatistic("FT:A:OU", "Total A runs", generateMarket, fullTime, maxPointScoreA);
        runsBTotal = new TotalStatistic("FT:B:OU", "Total B runs", generateMarket, fullTime, maxPointScoreA);
        runsTotal = new TotalStatistic("FT:OU", "Totals", generateMarket, fullTime, maxPointScoreA);
        runsHandicap = new HandicapStatistic("FT:SPRD", "Run Lines", generateMarket, fullTime, maxPointScoreA);

        currentInningNo = matchState.getInning() + 1;
        String thisInningSequenceId = matchState.getSequenceIdforInning(1);
        marketDescription = String.format("Inning %d Total runs ", currentInningNo);
        currentInningTotalruns = new TotalStatistic("P:OU", marketDescription, generateMarket, thisInningSequenceId,
                        maxPointScoreA);
        marketDescription = String.format("Inning %d Result ", currentInningNo);
        currentInningResult = new ThreeWayStatistic("P:AXB", marketDescription, generateMarket, thisInningSequenceId,
                        "A", "B", "Draw");
        marketDescription = String.format("Inning %d Handicp ", currentInningNo);
        currentInningRunsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket,
                        thisInningSequenceId, maxPointScoreA);

        extraInningPossible =
                        new TwoWayStatistic("FT:OT", "ExtraInning(Yes/No)", generateMarket, fullTime, "Yes", "No");

        winningMargin = new NWayStatistic("FT:WM", "Winning Margin", true, "M", winningMarginList);

        if (currentInningNo < 4) {
            marketDescription = String.format("3 Inning Total runs ");
            threeInningTotalruns =
                            new TotalStatistic("P:OU3", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("3 Inning Total runs A");
            threeInningrunsATotal =
                            new TotalStatistic("P:A:OU3", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("3 Inning Total runs B");
            threeInningrunsBTotal =
                            new TotalStatistic("P:B:OU3", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("3 Inning Result ");
            threeInningResult = new ThreeWayStatistic("P:AXB:3", marketDescription, generateMarket, fullTime, "A", "B",
                            "Draw");
            marketDescription = String.format("3 Inning  Handicp ");
            threeInningRunsHandicap = new HandicapStatistic("P:SPRD3", marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        }

        if (currentInningNo < 6) {
            marketDescription = String.format("5 Inning Total runs ");
            fiveInningTotalruns =
                            new TotalStatistic("P:OU5", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("5 Inning Total runs A");
            fiveInningrunsATotal =
                            new TotalStatistic("P:A:OU5", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("5 Inning Total runs B");
            fiveInningrunsBTotal =
                            new TotalStatistic("P:B:OU5", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("5 Inning Result ");
            fiveInningResult = new ThreeWayStatistic("P:AXB:5", marketDescription, generateMarket, fullTime, "A", "B",
                            "Draw");
            marketDescription = String.format("5 Inning  Handicp ");
            fiveInningRunsHandicap = new HandicapStatistic("P:SPRD5", marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        }
        if (currentInningNo < 8) {
            marketDescription = String.format("7 Inning Total runs ");
            sevenInningTotalruns =
                            new TotalStatistic("P:OU7", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("7 Inning Total runs A");
            sevenInningrunsATotal =
                            new TotalStatistic("P:A:OU7", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("7 Inning Total runs B");
            sevenInningrunsBTotal =
                            new TotalStatistic("P:B:OU7", marketDescription, generateMarket, fullTime, maxPointScoreA);
            marketDescription = String.format("7 Inning Result ");
            sevenInningResult = new ThreeWayStatistic("P:AXB7", marketDescription, generateMarket, fullTime, "A", "B",
                            "Draw");
            marketDescription = String.format("7 Inning  Handicp ");
            sevenInningRunsHandicap = new HandicapStatistic("P:SPRD7", marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        }
    }

    void updateStats(BaseballMatchState matchState, BaseballMatchFacts matchFacts) {
        // int oversA = matchState.getOversA();
        // int oversB = matchState.getOversB();
        int runsA = matchState.getRunsA();
        int runsB = matchState.getRunsB();
        // int hitsA = matchState.getHitsA();
        // int hitsB = matchState.getHitsB();
        // int innings = matchState.getInning();
        int[][] runsInInningsN = matchState.getRunsInInningsN();
        int normalTimeRunsA = runsInInningsN[0][8];
        int normalTimeRunsB = runsInInningsN[1][8];
        int currentInningRunsA = 0;
        int currentInningRunsB = 0;
        int threeInningRunsA = 0;
        int threeInningRunsB = 0;
        int fiveInningRunsA = 0;
        int fiveInningRunsB = 0;
        int sevenInningRunsA = 0;
        int sevenInningRunsB = 0;
        // int extrasA = matchState.getExtrasA();
        // int extrasB = matchState.getExtrasB();
        totalRunsA += runsA;
        totalRunsB += runsB;
        if (currentInningNo == 1) {
            currentInningRunsA = runsInInningsN[0][0];
            currentInningRunsB = runsInInningsN[1][0];
        } else {
            currentInningRunsA = runsInInningsN[0][currentInningNo - 1] - runsInInningsN[0][currentInningNo - 2];
            currentInningRunsB = runsInInningsN[1][currentInningNo - 1] - runsInInningsN[1][currentInningNo - 2];
        }

        threeInningRunsA = runsInInningsN[0][2];
        threeInningRunsB = runsInInningsN[1][2];
        fiveInningRunsA = runsInInningsN[0][4];
        fiveInningRunsB = runsInInningsN[1][4];
        sevenInningRunsA = runsInInningsN[0][6];
        sevenInningRunsB = runsInInningsN[1][6];

        // System.out.println(runsA + "----in--" + runsB + "--" + hitsA + "--" +
        // hitsB);
        // matchState.isFirstHalf() + "--" + normalTimeRunsA + "--"
        // + normalTimeRunsB + "--" + matchState.getBat() + "--" +
        // matchState.getBatFirst() + "--" + totalRunsA
        // + "---" + totalRunsB + "--" + fiveInningRunsA + "--" +
        // fiveInningRunsB + "--" + fiveInningNo);

        // matchResult.increment(generateTeamId(normalTimeRunsA,
        // normalTimeRunsB));
        if (!generateTeamId(normalTimeRunsA, normalTimeRunsB).equals(TeamId.UNKNOWN))
            moneyLine.increment(generateTeamId(normalTimeRunsA, normalTimeRunsB) == TeamId.A);
        // hitsMoneyLine.increment(generateTeamId(hitsA, hitsB));
        // hitsATotal.increment(hitsA);
        // hitsBTotal.increment(hitsB);
        // hitsTotal.increment(hitsA + hitsB);
        // hitsHandicap.increment(hitsA - hitsB);
        if (normalTimeRunsA == normalTimeRunsB)
            extraInningPossible.increment(true);
        else
            extraInningPossible.increment(false);
        int winingMargingIndicator;
        if (runsA > runsB) {
            winingMargingIndicator = (runsA <= (runsB + 3)) ? runsA - runsB + 3 : 7;
            winningMargin.increment(winingMargingIndicator);
        } else if (runsA < runsB) {
            winingMargingIndicator = (runsA <= (runsB - 4)) ? 0 : 4 - runsB + runsA;
            winningMargin.increment(winingMargingIndicator);
        }

        runsATotal.increment(normalTimeRunsA);
        runsBTotal.increment(normalTimeRunsB);
        runsTotal.increment(normalTimeRunsA + normalTimeRunsB);
        runsHandicap.increment(normalTimeRunsA - normalTimeRunsB);
        currentInningTotalruns.increment(currentInningRunsA + currentInningRunsB);
        currentInningRunsHandicap.increment(currentInningRunsA - currentInningRunsB);
        currentInningResult.increment(generateTeamId(currentInningRunsA, currentInningRunsB));
        if (currentInningNo < 4) {
            threeInningTotalruns.increment(threeInningRunsA + threeInningRunsB);
            threeInningRunsHandicap.increment(threeInningRunsA - threeInningRunsB);
            threeInningResult.increment(generateTeamId(threeInningRunsA, threeInningRunsB));
            threeInningrunsATotal.increment(threeInningRunsA);
            threeInningrunsBTotal.increment(threeInningRunsB);
        }
        if (currentInningNo < 6) {
            fiveInningTotalruns.increment(fiveInningRunsA + fiveInningRunsB);
            fiveInningRunsHandicap.increment(fiveInningRunsA - fiveInningRunsB);
            fiveInningResult.increment(generateTeamId(fiveInningRunsA, fiveInningRunsB));
            fiveInningrunsATotal.increment(fiveInningRunsA);
            fiveInningrunsBTotal.increment(fiveInningRunsB);
        }
        if (currentInningNo < 8) {
            sevenInningTotalruns.increment(sevenInningRunsA + sevenInningRunsB);
            sevenInningRunsHandicap.increment(sevenInningRunsA - sevenInningRunsB);
            sevenInningResult.increment(generateTeamId(sevenInningRunsA, sevenInningRunsB));
            sevenInningrunsATotal.increment(sevenInningRunsA);
            sevenInningrunsBTotal.increment(sevenInningRunsB);
        }
        // System.out.println(generateTeamId(currentInningRunsA,
        // currentInningRunsB) + "--"
        // + generateTeamId(threeInningRunsA, threeInningRunsB) + "--" +
        // currentInningRunsA + "--"
        // + currentInningRunsB + "--" + threeInningRunsA + "--" +
        // threeInningRunsB);

    }

    private TeamId generateTeamId(int runsA, int runsB) {
        TeamId teamId = TeamId.UNKNOWN;
        if (runsA > runsB) {
            teamId = TeamId.A;
        }
        if (runsA < runsB) {
            teamId = TeamId.B;
        }
        if (runsA == runsB) {
            teamId = TeamId.UNKNOWN;
        }
        return teamId;
    }
}
