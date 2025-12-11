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
    private TwoWayStatistic runsInFirstInning;
    boolean generateFirstRunMarket = false;
    boolean generateFirstInningsMarket = false;
    private TwoWayStatistic topFirstRun;
    private TwoWayStatistic bottomFirstRun;
    private TwoWayStatistic firstTeamToScore;
    private TwoWayStatistic lastTeamToScore;
    private TwoWayStatistic teamScoreFirstWinMatch;
    private TwoWayStatistic moneyLine;
    private ThreeWayStatistic currentInningResult;
    private ThreeWayStatistic matchResult;

    private TotalStatistic currentInningTotalruns;
    private HandicapStatistic currentInningRunsHandicap;

    private ThreeWayStatistic[] inningsResult;
    private TotalStatistic[] inningTotalruns;
    private TotalStatistic[] inningTotalrunsA;
    private TotalStatistic[] inningTotalrunsB;
    private HandicapStatistic[] inningRunsHandicap;

    private TotalStatistic runsATotal;
    private TotalStatistic runsBTotal;
    private TotalStatistic runsTotal;
    private TwoWayStatistic extraInningPossible;
    private TwoWayStatistic drawNoBet;
    private NWayStatistic winningMargin;

    private HandicapStatistic runsHandicap;

    private ThreeWayStatistic[] raceToRuns;
    private TwoWayStatistic[] leadAfterInnings;

    private NWayStatistic matchResultAndOverUnder;

    private ThreeWayStatistic highestScoringHalf;
    private ThreeWayStatistic teamWithHighestScoringHalf;

    private String[] winningMarginList = {"-4+", "-3", "-2", "-1", "1", "2", "3", "4+"};
    private String[] resultOverUnderList = {"AOver", "AUnder", "BOver", "BUnder", "XOver", "XUnder"};
    private int currentInningNo;
    private String fullTime;
    private int nInningsinMatch;
    private final int maxPointScoreA = 100;
    int totalRunsA;
    int totalRunsB;

    // sd
    private String[] doubleResultList = {"AA", "AB", "BA", "BB", "XA", "XB"};
    private NWayStatistic matchDoubleResult;


    BaseballMatchMarketsFactory(BaseballMatchState matchState) {

        Boolean generateMarket = true;
        fullTime = matchState.getSequenceIdForMatch();
        String marketDescription;
        moneyLine = new TwoWayStatistic("FT:ML", "Money Line", generateMarket, fullTime, "A", "B");
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
                        "A", "B", "X");
        matchResult = new ThreeWayStatistic("FT:AXB", "Match Result", generateMarket, "M", "A", "B", "X");

        highestScoringHalf =
                        new ThreeWayStatistic("FT:HSH", "Highest Scoring Half", generateMarket, "M", "H1", "H2", "X");

        teamWithHighestScoringHalf = new ThreeWayStatistic("FT:T:HSH", "Team with Highest Scoring Half", generateMarket,
                        "M", "A", "B", "X");

        marketDescription = String.format("Inning %d Handicp ", currentInningNo);
        currentInningRunsHandicap = new HandicapStatistic("P:SPRD", marketDescription, generateMarket,
                        thisInningSequenceId, maxPointScoreA);

        extraInningPossible =
                        new TwoWayStatistic("FT:OT", "ExtraInning(Yes/No)", generateMarket, fullTime, "Yes", "No");

        drawNoBet = new TwoWayStatistic("FT:DNB", "Fulltime Draw No Bet", generateMarket, fullTime, "A", "B");

        winningMargin = new NWayStatistic("FT:WM", "Winning Margin", true, "M", winningMarginList);

        matchResultAndOverUnder = new NWayStatistic("FT:AXB$OU", "Match Result and Over/Under 10.5", true, "M",
                        resultOverUnderList);

        matchDoubleResult = new NWayStatistic("FT:HTFT", "Double Result", true, "M", doubleResultList);


        BaseballMatchFormat matchFormat = (BaseballMatchFormat) matchState.getMatchFormat();
        nInningsinMatch = matchFormat.getnInningsinMatch();
        inningsResult = new ThreeWayStatistic[nInningsinMatch];
        inningTotalruns = new TotalStatistic[nInningsinMatch];
        inningTotalrunsA = new TotalStatistic[nInningsinMatch];
        inningTotalrunsB = new TotalStatistic[nInningsinMatch];
        inningRunsHandicap = new HandicapStatistic[nInningsinMatch];
        raceToRuns = new ThreeWayStatistic[nInningsinMatch];
        leadAfterInnings = new TwoWayStatistic[nInningsinMatch];
        for (int i = 1; i <= nInningsinMatch; i++) {
            inningsResult[i - 1] = createInningsResultMarket(i, currentInningNo <= i);
            leadAfterInnings[i - 1] = createLeadAfterInningsMarket(i, currentInningNo <= 1);
            if (i == 3 || i == 5 || i == 7) {
                inningTotalruns[i - 1] = createInningsTotalRunsMarket(i, currentInningNo <= i, TeamId.UNKNOWN);
                inningTotalrunsA[i - 1] = createInningsTotalRunsMarket(i, currentInningNo <= i, TeamId.A);
                inningTotalrunsB[i - 1] = createInningsTotalRunsMarket(i, currentInningNo <= i, TeamId.B);
                inningRunsHandicap[i - 1] = createInningsHandicapMarket(i, currentInningNo <= i);
                raceToRuns[i - 1] = createRaceToRunsMarket(i, true);
            }

        }
        generateFirstInningsMarket = matchState.getInning() < 1 && (matchState.getRunsA() + matchState.getRunsB() == 0);

        runsInFirstInning = new TwoWayStatistic("FT:R$1", "Run in first innings", generateFirstInningsMarket, fullTime,
                        "Yes", "No");
        generateFirstRunMarket = matchState.getRunsA() + matchState.getRunsB() == 0;
        topFirstRun = new TwoWayStatistic("FT:TR1", "Top 1st Run", generateFirstRunMarket, fullTime, "Yes", "No");
        bottomFirstRun = new TwoWayStatistic("FT:BR1", "Bottom 1st Run", generateFirstRunMarket, fullTime, "Yes", "No");
        firstTeamToScore = new TwoWayStatistic("FT:FTTS", "First team to score", generateFirstRunMarket, fullTime, "A",
                        "B");
        lastTeamToScore = new TwoWayStatistic("FT:LTTS", "Last team to score", true, fullTime, "A", "B");

        teamScoreFirstWinMatch = new TwoWayStatistic("FT:R1W", "Team who socres first run wins match", true, fullTime,
                        "Yes", "No");

    }

    private ThreeWayStatistic createInningsResultMarket(int innings, boolean generateMarket) {
        String marketDescription = String.format(innings + " Inning Result ");
        ThreeWayStatistic inningResult = new ThreeWayStatistic("P:AXB:" + innings, marketDescription, generateMarket,
                        fullTime, "A", "B", "X");
        return inningResult;
    }

    private TwoWayStatistic createLeadAfterInningsMarket(int innings, boolean generateMarket) {
        String marketDescription = String.format("Lead after " + innings + " Innings");
        TwoWayStatistic inningResult =
                        new TwoWayStatistic("FT:LEAD", marketDescription, generateMarket, "L" + innings, "A", "B");
        return inningResult;
    }

    private ThreeWayStatistic createRaceToRunsMarket(int innings, boolean generateMarket) {
        String marketDescription = String.format("Race to " + innings + " runs");
        ThreeWayStatistic inningResult = new ThreeWayStatistic("FT:RTR" + innings, marketDescription, generateMarket,
                        fullTime, "A", "B", "X");
        return inningResult;
    }

    private HandicapStatistic createInningsHandicapMarket(int innings, boolean generateMarket) {
        String marketDescription = String.format(innings + " Inning Handicap ");
        HandicapStatistic inningHandicap = new HandicapStatistic("P:SPRD" + innings, marketDescription, generateMarket,
                        fullTime, maxPointScoreA);
        return inningHandicap;
    }


    private TotalStatistic createInningsTotalRunsMarket(int innings, boolean generateMarket, TeamId teamId) {
        String marketDescription = String.format(
                        innings + " Inning Total Runs " + ((teamId == TeamId.UNKNOWN) ? teamId.toString() : ""));
        TotalStatistic inningTotalRuns;
        if (teamId == TeamId.UNKNOWN)
            inningTotalRuns = new TotalStatistic("P:OU" + innings, marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        else if (teamId == TeamId.A)
            inningTotalRuns = new TotalStatistic("P:A:OU" + innings, marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        else
            inningTotalRuns = new TotalStatistic("P:B:OU" + innings, marketDescription, generateMarket, fullTime,
                            maxPointScoreA);
        return inningTotalRuns;
    }

    void updateStats(BaseballMatchState matchState, BaseballMatchFacts matchFacts) {
        int runsA = matchState.getRunsA();
        int runsB = matchState.getRunsB();
        int[][] runsInInningsN = matchState.getRunsInInningsN();
        int normalTimeRunsA = runsInInningsN[0][8];
        int normalTimeRunsB = runsInInningsN[1][8];
        int firstHalfRunsA = matchState.getFirstHalfRunsA();
        int firstHalfRunsB = matchState.getFirstHalfRunsB();
        int currentInningRunsA = 0;
        int currentInningRunsB = 0;
        totalRunsA += runsA;
        totalRunsB += runsB;
        if (currentInningNo == 1) {
            currentInningRunsA = runsInInningsN[0][0];
            currentInningRunsB = runsInInningsN[1][0];
        } else {
            currentInningRunsA = runsInInningsN[0][currentInningNo - 1] - runsInInningsN[0][currentInningNo - 2];
            currentInningRunsB = runsInInningsN[1][currentInningNo - 1] - runsInInningsN[1][currentInningNo - 2];
        }

        if (generateFirstInningsMarket) {
            runsInFirstInning.increment(runsInInningsN[0][0] + runsInInningsN[1][0] > 0);
        }

        if (!generateTeamId(normalTimeRunsA, normalTimeRunsB).equals(TeamId.UNKNOWN))
            moneyLine.increment(generateTeamId(normalTimeRunsA, normalTimeRunsB) == TeamId.A);

        if (normalTimeRunsA != normalTimeRunsB)
            drawNoBet.increment(normalTimeRunsA > normalTimeRunsB);

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
        matchResult.increment(generateTeamId(currentInningRunsA, currentInningRunsB));
        for (int i = currentInningNo; i <= nInningsinMatch; i++) {
            inningsResult[i - 1].increment(generateTeamId(runsInInningsN[0][i - 1], runsInInningsN[1][i - 1]));
            if (i == 3 || i == 5 || i == 7) {
                inningTotalruns[i - 1].increment(runsInInningsN[0][i - 1] + runsInInningsN[1][i - 1]);
                inningTotalrunsA[i - 1].increment(runsInInningsN[0][i - 1]);
                inningTotalrunsB[i - 1].increment(runsInInningsN[1][i - 1]);
                inningRunsHandicap[i - 1].increment(runsInInningsN[0][i - 1] - runsInInningsN[1][i - 1]);

            }
            if (runsInInningsN[0][i - 1] != runsInInningsN[1][i - 1])
                leadAfterInnings[i - 1].increment(runsInInningsN[0][i - 1] > runsInInningsN[1][i - 1]);
        }
        raceToRuns[2].increment(matchFacts.runs3Winner);
        raceToRuns[4].increment(matchFacts.runs5Winner);
        raceToRuns[6].increment(matchFacts.runs7Winner);



        boolean over = runsA + runsB > 10.5;
        if (runsA > runsB) {
            if (over)
                matchResultAndOverUnder.increment(0);
            else
                matchResultAndOverUnder.increment(1);
        } else if (runsA < runsB) {
            if (over)
                matchResultAndOverUnder.increment(2);
            else
                matchResultAndOverUnder.increment(3);
        } else {
            if (over)
                matchResultAndOverUnder.increment(4);
            else
                matchResultAndOverUnder.increment(5);
        }

        // DOUBLE RESULT
        if (runsInInningsN[0][4] > runsInInningsN[1][4] && normalTimeRunsA > normalTimeRunsB) // AA
            matchDoubleResult.increment(0);

        if (runsInInningsN[0][4] > runsInInningsN[1][4] && normalTimeRunsA < normalTimeRunsB) // AB
            matchDoubleResult.increment(1);

        if (runsInInningsN[0][4] < runsInInningsN[1][4] && normalTimeRunsA > normalTimeRunsB) // BA
            matchDoubleResult.increment(2);

        if (runsInInningsN[0][4] < runsInInningsN[1][4] && normalTimeRunsA < normalTimeRunsB) // BB
            matchDoubleResult.increment(3);

        if (runsInInningsN[0][4] == runsInInningsN[1][4] && normalTimeRunsA > normalTimeRunsB) // XA
            matchDoubleResult.increment(4);

        if (runsInInningsN[0][4] == runsInInningsN[1][4] && normalTimeRunsA < normalTimeRunsB) // XB
            matchDoubleResult.increment(5);


        if (generateFirstRunMarket) {
            if (matchFacts.firstRunTeam == TeamId.A) {
                this.topFirstRun.increment(true);
                this.bottomFirstRun.increment(false);
                firstTeamToScore.increment(true);
            } else {
                this.topFirstRun.increment(false);
                this.bottomFirstRun.increment(true);
                firstTeamToScore.increment(false);
            }
        }

        lastTeamToScore.increment(matchState.getTeamScoreLast() == TeamId.A);

        if (normalTimeRunsA + normalTimeRunsB > 2 * (firstHalfRunsA + firstHalfRunsB))
            highestScoringHalf.increment(TeamId.B);
        else if (normalTimeRunsA + normalTimeRunsB < 2 * (firstHalfRunsA + firstHalfRunsB))
            highestScoringHalf.increment(TeamId.A);
        else {
            highestScoringHalf.increment(TeamId.UNKNOWN);
        }

        int secondHalfRunsA = normalTimeRunsA - firstHalfRunsA;
        int secondHalfRunsB = normalTimeRunsB - firstHalfRunsB;
        if ((firstHalfRunsA > secondHalfRunsA ? firstHalfRunsA
                        : secondHalfRunsA) > (firstHalfRunsB > secondHalfRunsB ? firstHalfRunsB : secondHalfRunsB)) {
            teamWithHighestScoringHalf.increment(TeamId.A);
        } else if ((firstHalfRunsA > secondHalfRunsA ? firstHalfRunsA
                        : secondHalfRunsA) < (firstHalfRunsB > secondHalfRunsB ? firstHalfRunsB : secondHalfRunsB)) {
            teamWithHighestScoringHalf.increment(TeamId.B);
        } else {
            teamWithHighestScoringHalf.increment(TeamId.UNKNOWN);
        }


        if (matchFacts.firstRunTeam == generateTeamId(runsA, runsB))
            teamScoreFirstWinMatch.increment(true);
        else
            teamScoreFirstWinMatch.increment(false);
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
