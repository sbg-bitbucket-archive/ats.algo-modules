package ats.algo.sport.americanfootball;

import java.util.Map;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Markets;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.americanfootball.AmericanfootballMatchState;

public class AmericanfootballMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for American
     * football if we need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in
     * this class
     * 
     * @author Jin
     * 
     */

    private TwoWayStatistic moneyLine;
    private ThreeWayStatistic matchResult;
    @SuppressWarnings("unused")
    private ThreeWayStatistic fiveMinsWinner;
    private ThreeWayStatistic matchDoubleChance;
    @SuppressWarnings("unused")
    private CorrectScoreStatistic fulltimeCorrectScore;
    private CorrectScoreStatistic ftotCorrectPoints;
    private TotalStatistic pointsTotal;
    private TotalStatistic pointsTotalA;
    private TotalStatistic pointsTotalB;

    private TwoWayStatistic periodBothTeamToScore;
    @SuppressWarnings("unused")
    private TwoWayStatistic fiveMinsIfGoal;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic pointsHandicap;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic overTimeNextGoal;
    private TotalStatistic trysTotalCurrentPeriod;
    private TotalStatistic touchDownTotal;
    private ThreeWayStatistic periodWinner;
    private HandicapStatistic periodPointsHandicap;
    private TotalStatistic periodPointsTotal;
    private TotalStatistic periodPointsTotalA;
    private TotalStatistic periodPointsTotalB;
    private String[] oddEvenList = {"Odd", "Even"};
    private NWayStatistic totalPointsOddEven;


    private CorrectScoreStatistic periodCorrectTds;
    private TwoWayStatistic overTimePossible;
    private NWayStatistic highestScoreHalf;
    private NWayStatistic highestScoreHalfTeamA;
    private NWayStatistic highestScoreHalfTeamB;
    private ThreeWayStatistic matchResultForHalves;
    private HandicapStatistic halvesPointsHandicap;
    private TotalStatistic halvesPointsTotal;
    private TotalStatistic halvesPointsTotalA;
    private TotalStatistic halvesPointsTotalB;
    private TotalStatistic halvesTD;

    private NWayStatistic halfTimeFullTimeResult;
    private NWayStatistic highestScoreQuarterTeamA;
    private NWayStatistic highestScoreQuarterTeamB;
    private NWayStatistic highestScoreQuarter;
    private NWayStatistic winningMargin;
    private static final int maxNoGoalsPerTeam = 280;
    private String[] winningMarginList = {"WM1", "WM2", "WM3", "WM4", "WM5", "WM6", "WM7", "WM8", "WM9", "WM10"};
    private String[] halTimeFullTimeResultList = {"H/H", "H/A", "H/D", "D/H", " D/D", "D/A", " A/H", " A/D", " A/A"};

    AmericanfootballMatchMarketsFactory(AmericanfootballMatchState matchState) {
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String fiveMinsSequenceId = matchState.getSequenceIdForFiveMinsResult();
        String goalSequenceId = matchState.getSequenceIdForGoal(0);

        int periodNo = matchState.getPeriodNo();
        Boolean generateMarketFT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketFTOT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketOT = false;
        Boolean inMatchNormalTime = true;
        @SuppressWarnings("unused")
        boolean overTimePenaltyPossible = false;
        // Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 2: // second period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 3:
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 4: // extra time
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            default:
                generateMarketFT = false;
                generateMarketFTOT = false;
                inMatchNormalTime = false;
                generateMarketOT = false;
        }

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0))
            overTimePenaltyPossible = true;
        matchResult = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, false, matchSequenceId, "A",
                        "B", "Draw");

        moneyLine = new TwoWayStatistic("FT:ML", "Money Line", MarketGroup.GOALS, true, matchSequenceId, "A", "B");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", false, matchSequenceId, "Yes", "No");

        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", "5 Min Result", false, fiveMinsSequenceId, "A", "B", "Draw");
        fiveMinsIfGoal = new TwoWayStatistic("FT:5MG", "5 Min Goal - Yes/No", false, periodSequenceId, "Yes", "No");
        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", false, matchSequenceId, "AX", "BX", "AB");
        ftotCorrectPoints = new CorrectScoreStatistic("FTOT:CS", "Correct Score", MarketGroup.GOALS, false,
                        matchSequenceId, maxNoGoalsPerTeam);
        String[] timeOfFirstGoalSelections = {"No Goal", "1-10", "11-20", "21-30", "31-40", "41-50", "51-60"};

        new NWayStatistic("FT:TFG", "Time of 1st Goal", false, matchSequenceId, timeOfFirstGoalSelections);

        pointsTotal = new TotalStatistic("FT:OU", "Total Points", MarketGroup.GOALS, generateMarketFT && true, "M",
                        2 * maxNoGoalsPerTeam);
        pointsTotalA = new TotalStatistic("FT:A:OU", "Home Team Total Points", MarketGroup.GOALS, true, matchSequenceId,
                        maxNoGoalsPerTeam);
        pointsTotalB = new TotalStatistic("FT:B:OU", "Away Team Total Points", MarketGroup.GOALS, true, matchSequenceId,
                        maxNoGoalsPerTeam);

        pointsHandicap = new HandicapStatistic("FT:SPRD", "Points Spread", MarketGroup.GOALS, generateMarketFT, "M",
                        maxNoGoalsPerTeam);

        periodPointsHandicap = new HandicapStatistic("P:SPRD", periodSequenceId + " Quarter Points Spread",
                        MarketGroup.GOALS, generateMarketFT, periodSequenceId, maxNoGoalsPerTeam);

        matchWinnerEuroHandicap =
                        new EuroHandicapStatistic("FT:EH", "Euro Handicap", false, matchSequenceId, maxNoGoalsPerTeam);

        String marketDescription = String.format("Next Goal ");
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, false, goalSequenceId, "A", "B", "No goal");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String halfSequenceId = matchState.getSequenceIdForHalf(0);

        periodNo = matchState.getPeriodNo();
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st period total Touchdowns";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd period total Touchdowns";
                break;
            case 3:
                marketDescription = "4st period total Touchdowns";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4nd period total Touchdowns";
                break;
            case 6: // second half
                marketDescription = "extra period total Touchdowns";
                break;

            // default:
            // throw new IllegalArgumentException("period 3 does not exist");
            // // marketDescription = "";
        }
        trysTotalCurrentPeriod = new TotalStatistic("P:OU", marketDescription, MarketGroup.GOALS, false,
                        periodSequenceId, maxNoGoalsPerTeam);

        touchDownTotal = new TotalStatistic("FT:TDOU", "Total Touchdowns", MarketGroup.GOALS, true, "M",
                        maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Period Correct Touchdowns";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Period Correct Touchdowns";
                break;
            case 3:
                marketDescription = "3rd Period Correct Touchdowns";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Period Correct Touchdowns";
                break;
            //
            // case 5: // second half
            // throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        periodCorrectTds = new CorrectScoreStatistic("P:CS", marketDescription, MarketGroup.GOALS, false,
                        periodSequenceId, maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Period Both Team To Score";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Period Both Team To Score";
                break;
            case 3:
                marketDescription = "3rd Period Both Team To Score";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Period Both Team To Score";
                break;

            // case 5: // second half
            // throw new IllegalArgumentException("period 3 does not exist");
            default:
                marketDescription = "";
        }
        generateMarketFT = false;

        // = (matchState.getMatchPeriod() != AmericanfootballMatchPeriod.AT_HALF_TIME
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.AT_FULL_TIME
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.AT_EXTRA_TIME_HALF_TIME
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.AT_EXTRA_TIME_END
        // && matchState.getMatchPeriod() != AmericanfootballMatchPeriod.IN_SHOOTOUT
        // && (matchState.getCurrentPeriodPointsA() == 0 || matchState.getCurrentPeriodPointsB() == 0));

        periodBothTeamToScore = new TwoWayStatistic("P:BTTS", marketDescription, false, periodSequenceId, "Yes", "No");

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "1st Quarter";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Quarter";
                break;
            case 3: // first half
                marketDescription = "3rd Quarter";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Quarter";
                break;

            // case 5: // second half
            // throw new IllegalArgumentException("period 3 does not exist");

            default:
                marketDescription = "";
        }
        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription + " Result", true, periodSequenceId, "A", "B",
                        "Draw");

        periodPointsTotal = new TotalStatistic("P:OU", marketDescription + " Total Points", MarketGroup.GOALS, true,
                        periodSequenceId, 2 * maxNoGoalsPerTeam);
        periodPointsTotalA = new TotalStatistic("P:A:OU", marketDescription + " Home Team Total Points",
                        MarketGroup.GOALS, true, periodSequenceId, maxNoGoalsPerTeam);
        periodPointsTotalB = new TotalStatistic("P:B:OU", marketDescription + " Away Team Total Points",
                        MarketGroup.GOALS, true, periodSequenceId, maxNoGoalsPerTeam);

        marketDescription = String.format("OT - Next Goal");
        overTimeNextGoal =
                        new ThreeWayStatistic("OT:NG", marketDescription, false, goalSequenceId, "A", "B", "No goal");

        totalPointsOddEven = new NWayStatistic("FT:OE", "Total Points Odd/Even", true, "M", oddEvenList);

        String[] highestScoreHalfString = {"H1", "H2", "Draw"};
        highestScoreHalf = new NWayStatistic("FT:HH", "Highest Scoring Half", true, "M", highestScoreHalfString);

        String[] highestScoreQuarterString = {"Q1", "Q2", "Q3", "Q4", "Draw"};
        highestScoreQuarter = new NWayStatistic("FT:HQ", " Highest Scoring Quarter", MarketGroup.GOALS, true, "M",
                        highestScoreQuarterString);

        String[] highestScoreHalfTeamAString = {"H1", "H2", "Draw"};
        highestScoreHalfTeamA = new NWayStatistic("FT:A:HH", "Team A Highest Scoring Half", true, "M",
                        highestScoreHalfTeamAString);
        highestScoreHalfTeamB = new NWayStatistic("FT:B:HH", "Team B Highest Scoring Half", true, "M",
                        highestScoreHalfTeamAString);
        String[] highestScoreQuarterTeamAString = {"Q1", "Q2", "Q3", "Q4", "Draw"};
        highestScoreQuarterTeamA = new NWayStatistic("FT:A:HQ", "Team A Highest Scoring Quarter", true,
                        periodSequenceId, highestScoreQuarterTeamAString);
        highestScoreQuarterTeamB = new NWayStatistic("FT:B:HQ", "Team B Highest Scoring Quarter", true,
                        periodSequenceId, highestScoreQuarterTeamAString);
        matchResultForHalves = new ThreeWayStatistic("P:AXB", halfSequenceId + " Half time Match Result",
                        MarketGroup.GOALS, true, halfSequenceId, "A", "B", "Draw");
        halvesPointsHandicap = new HandicapStatistic("P:SPRD", halfSequenceId + " Half Points Spread",
                        MarketGroup.GOALS, true, halfSequenceId, maxNoGoalsPerTeam);
        halvesPointsTotal = new TotalStatistic("P:OU", halfSequenceId + " Total Points", MarketGroup.GOALS, true,
                        halfSequenceId, maxNoGoalsPerTeam);
        halvesPointsTotalA = new TotalStatistic("P:A:OU", halfSequenceId + " Total Points A", MarketGroup.GOALS, true,
                        halfSequenceId, maxNoGoalsPerTeam);
        halvesPointsTotalB = new TotalStatistic("P:B:OU", halfSequenceId + " Total Points B", MarketGroup.GOALS, true,
                        halfSequenceId, maxNoGoalsPerTeam);
        halvesTD = new TotalStatistic("P:TDOU", halfSequenceId + " Total Touch Downs", MarketGroup.GOALS,
                        inMatchNormalTime, halfSequenceId, maxNoGoalsPerTeam);
        if (periodNo < 3)
            halfTimeFullTimeResult = new NWayStatistic("FT:HF", "Half Time/ Full Time", MarketGroup.GOALS,
                            inMatchNormalTime, "M", halTimeFullTimeResultList);

        winningMargin = new NWayStatistic("FTOT:WM", "Winning Margin", true, "M", winningMarginList);
        /*
         * Extra time markets
         */

    }

    void updateStats(AmericanfootballMatchState matchState, AmericanfootballMatchFacts matchFacts) {

        int pointsA = matchState.getPointsA();
        int pointsB = matchState.getPointsB();


        moneyLine.increment(pointsA > pointsB);
        int i = pointsA - pointsB;


        // for(int i=-26;i<26;i++){
        int winingMargingIndicator = (int) ((((i <= 0.0F) ? 0.0F - (i) : i) - 1) / 6);
        // }
        if (winingMargingIndicator > 4)
            winingMargingIndicator = 4;

        int baseFactor = i <= 0.0F ? 5 : 0;
        winingMargingIndicator += baseFactor;

        // System.out.println(i+" "+winningMarginList[winingMargingIndicator]);
        winningMargin.increment(winingMargingIndicator);

        int normalTimeGoalsA = matchState.getNormalTimePointsA();
        int normalTimeGoalsB = matchState.getNormalTimePointsB();

        if ((pointsA + pointsB) % 2 == 0)
            totalPointsOddEven.increment(1);
        else
            totalPointsOddEven.increment(0);

        TeamId teamId = TeamId.UNKNOWN;

        int cpa = matchFacts.getComparePointsA();
        int cpb = matchFacts.getComparePointsB();

        if (cpa > cpb)
            teamId = TeamId.A;
        if (cpa < cpb)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            matchResult.increment(teamId);



        if (normalTimeGoalsA == normalTimeGoalsB) {
            overTimePossible.increment(true);
        } else {
            overTimePossible.increment(false);
        }

        if (normalTimeGoalsA >= normalTimeGoalsB) {
            teamId = TeamId.A;
            matchDoubleChance.increment(teamId);
        }
        if (normalTimeGoalsA <= normalTimeGoalsB) {
            teamId = TeamId.B;
            matchDoubleChance.increment(teamId);
        }
        if (normalTimeGoalsA < normalTimeGoalsB || normalTimeGoalsA > normalTimeGoalsB) {
            teamId = TeamId.UNKNOWN;
            matchDoubleChance.increment(teamId);
        }

        int q1PointsA = matchState.getFirstQuarterPointsA();
        int q1PointsB = matchState.getFirstQuarterPointsB();
        int q2PointsA = matchState.getSecondQuarterPointsA();
        int q2PointsB = matchState.getSecondQuarterPointsB();
        int q3PointsA = matchState.getThirdQuarterPointsA();
        int q3PointsB = matchState.getThirdQuarterPointsB();
        int q4PointsA = matchState.getFourthQuarterPointsA();
        int q4PointsB = matchState.getFourthQuarterPointsB();

        if (q1PointsA + q1PointsB > q2PointsA + q2PointsB && q1PointsA + q1PointsB > q3PointsA + q3PointsB
                        && q1PointsA + q1PointsB > q4PointsA + q4PointsB) {
            highestScoreQuarter.increment(0);
        } else if (q2PointsA + q2PointsB > q1PointsA + q1PointsB && q2PointsA + q2PointsB > q3PointsA + q3PointsB
                        && q2PointsA + q2PointsB > q4PointsA + q4PointsB) {
            highestScoreQuarter.increment(1);
        } else if (q3PointsA + q3PointsB > q1PointsA + q1PointsB && q3PointsA + q3PointsB > q2PointsA + q2PointsB
                        && q3PointsA + q3PointsB > q4PointsA + q4PointsB) {
            highestScoreQuarter.increment(2);
        } else if (q4PointsA + q4PointsB > q1PointsA + q1PointsB && q4PointsA + q4PointsB > q2PointsA + q2PointsB
                        && q4PointsA + q4PointsB > q3PointsA + q3PointsB) {
            highestScoreQuarter.increment(3);
        } else {
            // highestScoreQuarter.increment(4);
        }

        if (q1PointsA > q2PointsA && q1PointsA > q3PointsA && q1PointsA > q4PointsA) {
            highestScoreQuarterTeamA.increment(0);
        } else if (q2PointsA > q1PointsA && q2PointsA > q3PointsA && q2PointsA > q4PointsA) {
            highestScoreQuarterTeamA.increment(1);
        } else if (q3PointsA > q1PointsA && q3PointsA > q2PointsA && q3PointsA > q4PointsA) {
            highestScoreQuarterTeamA.increment(2);
        } else if (q4PointsA > q1PointsA && q4PointsA > q2PointsA && q4PointsA > q3PointsA) {
            highestScoreQuarterTeamA.increment(3);
        } else {
            // highestScoreQuarterTeamA.increment(4);
        }

        if (q1PointsB > q2PointsB && q1PointsB > q3PointsB && q1PointsB > q4PointsB) {
            highestScoreQuarterTeamB.increment(0);
        } else if (q2PointsB > q1PointsB && q2PointsB > q3PointsB && q2PointsB > q4PointsB) {
            highestScoreQuarterTeamB.increment(1);
        } else if (q3PointsB > q1PointsB && q3PointsB > q2PointsB && q3PointsB > q4PointsB) {
            highestScoreQuarterTeamB.increment(2);
        } else if (q4PointsB > q1PointsB && q4PointsB > q2PointsB && q4PointsB > q3PointsB) {
            highestScoreQuarterTeamB.increment(3);
        } else {
            // highestScoreQuarterTeamB.increment(4);
        }

        if (q1PointsA + q2PointsA > q3PointsA + q4PointsA) {
            highestScoreHalfTeamA.increment(0);
        } else if (q1PointsA + q2PointsA < q3PointsA + q4PointsA) {
            highestScoreHalfTeamA.increment(1);
        } else {
            highestScoreHalfTeamA.increment(2);
        }

        if (q1PointsB + q2PointsB > q3PointsB + q4PointsB) {
            highestScoreHalfTeamB.increment(0);
        } else if (q1PointsB + q2PointsB < q3PointsB + q4PointsB) {
            highestScoreHalfTeamB.increment(1);
        } else {
            highestScoreHalfTeamB.increment(2);
        }

        int halfPointsA = 0;
        int halfPointsB = 0;
        int firstHalfPointsA = q1PointsA + q2PointsA;
        int secondHalfPointsA = q3PointsA + q4PointsA;

        int firstHalfPointsB = q1PointsB + q2PointsB;
        int secondHalfPointsB = q3PointsB + q4PointsB;
        int halfTDA = 0;
        int halfTDB = 0;
        String halfIndicator = matchFacts.getHalfIndicator();
        if (halfIndicator.equals("H1")) {
            halfPointsA = firstHalfPointsA;
            halfPointsB = firstHalfPointsB;
            halfTDA = matchState.getFirstQuarterTDA() + matchState.getSecondQuarterTDA();
            halfTDB = matchState.getFirstQuarterTDB() + matchState.getSecondQuarterTDB();
            if (halfPointsA > halfPointsB)
                matchResultForHalves.increment(TeamId.A);
            else if (halfPointsA < halfPointsB)
                matchResultForHalves.increment(TeamId.B);
            else
                matchResultForHalves.increment(TeamId.UNKNOWN);
        } else if (halfIndicator.equals("H2")) {
            halfPointsA = secondHalfPointsA;
            halfPointsB = secondHalfPointsB;
            halfTDA = matchState.getThirdQuarterTDA() + matchState.getFourthQuarterTDA();
            halfTDB = matchState.getThirdQuarterTDB() + matchState.getFourthQuarterTDB();
            if (halfPointsA > halfPointsB)
                matchResultForHalves.increment(TeamId.A);
            else if (halfPointsA < halfPointsB)
                matchResultForHalves.increment(TeamId.B);
            else
                matchResultForHalves.increment(TeamId.UNKNOWN);
        }


        // half&&full time market
        if (halfIndicator.equals("H1")) {
            if (normalTimeGoalsA > normalTimeGoalsB && firstHalfPointsA > firstHalfPointsB)
                halfTimeFullTimeResult.increment(0);
            if (normalTimeGoalsA < normalTimeGoalsB && firstHalfPointsA > firstHalfPointsB)
                halfTimeFullTimeResult.increment(1);
            if (normalTimeGoalsA == normalTimeGoalsB && firstHalfPointsA > firstHalfPointsB)
                halfTimeFullTimeResult.increment(2);
            if (normalTimeGoalsA > normalTimeGoalsB && firstHalfPointsA == firstHalfPointsB)
                halfTimeFullTimeResult.increment(3);
            if (normalTimeGoalsA == normalTimeGoalsB && firstHalfPointsA == firstHalfPointsB)
                halfTimeFullTimeResult.increment(4);
            if (normalTimeGoalsA < normalTimeGoalsB && firstHalfPointsA == firstHalfPointsB)
                halfTimeFullTimeResult.increment(5);
            if (normalTimeGoalsA > normalTimeGoalsB && firstHalfPointsA < firstHalfPointsB)
                halfTimeFullTimeResult.increment(6);
            if (normalTimeGoalsA == normalTimeGoalsB && firstHalfPointsA < firstHalfPointsB)
                halfTimeFullTimeResult.increment(7);
            if (normalTimeGoalsA < normalTimeGoalsB && firstHalfPointsA < firstHalfPointsB)
                halfTimeFullTimeResult.increment(8);
        }

        halvesPointsHandicap.increment(halfPointsA - halfPointsB);
        halvesPointsTotal.increment(halfPointsA + halfPointsB);
        halvesTD.increment(halfTDA + halfTDB);
        halvesPointsTotalA.increment(halfPointsA);
        halvesPointsTotalB.increment(halfPointsB);
        if (firstHalfPointsA + firstHalfPointsB > secondHalfPointsA + secondHalfPointsB) {
            teamId = TeamId.A;
            highestScoreHalf.increment(0);
        } else if (firstHalfPointsA + firstHalfPointsB < secondHalfPointsA + secondHalfPointsB) {
            teamId = TeamId.B;
            highestScoreHalf.increment(1);
        } else {
            // teamId = TeamId.UNKNOWN;
            // highestScoreHalf.increment(2);
        }

        // fulltimeCorrectScore.increment(normalTimeGoalsA, normalTimeGoalsB);
        ftotCorrectPoints.increment(pointsA, pointsB);
        pointsTotal.increment(pointsA + pointsB);
        pointsTotalA.increment(pointsA);
        pointsTotalB.increment(pointsB);
        pointsHandicap.increment(pointsA - pointsB);

        matchWinnerEuroHandicap.increment(pointsA - pointsB);
        if (!matchState.isNormalTimeMatchCompleted())
            nextGoal.increment(matchFacts.getNextPoint());
        overTimeNextGoal.increment(matchFacts.getNextPoint());
        trysTotalCurrentPeriod.increment(matchFacts.getTrysTotalCurrentPeriod());
        touchDownTotal.increment(matchState.getTdA() + matchState.getTdB());
        int periodTrysA = matchFacts.getCurrentPeriodTrysA();
        int periodTrysB = matchFacts.getCurrentPeriodTrysB();

        periodCorrectTds.increment(periodTrysA, periodTrysB);

        int currentQuarterPointsA = matchFacts.getCurrentPeriodPointsA();
        int currentQuarterPointsB = matchFacts.getCurrentPeriodPointsB();

        teamId = TeamId.UNKNOWN;
        if (currentQuarterPointsA > currentQuarterPointsB)
            teamId = TeamId.A;
        if (currentQuarterPointsA < currentQuarterPointsB)
            teamId = TeamId.B;
        periodWinner.increment(teamId);
        periodPointsHandicap.increment(currentQuarterPointsA - currentQuarterPointsB);
        periodBothTeamToScore.increment(matchFacts.getBothTeamToScoreCurrentPeriod());
        periodPointsTotalA.increment(currentQuarterPointsA);
        periodPointsTotalB.increment(currentQuarterPointsB);
        periodPointsTotal.increment(currentQuarterPointsA + currentQuarterPointsB);
    }

    @Override
    public void addDerivedMarkets(Markets markets, AlgoMatchState matchState, MatchParams matchParams) {
        // if (!((AmericanfootballMatchState) matchState).isNormalTimeMatchCompleted()) {
        // double[] probs = { 0, 0, 0, 0, 0, 0 };
        // Market sourceMarket = markets.get("FT:AXB");
        // Map<String, Double> matchResult = sourceMarket.copy();
        // Market market = new Market(MarketCategory.GENERAL, MarketGroup.GOALS, "FT:DNB", "M", "Draw no bet");
        // market.setIsValid(true);
        // // String marketDescription = "Match Result A win";
        // probs[0] = matchResult.get("A") / (matchResult.get("A") + matchResult.get("B"));
        // // marketDescription = "Match Result B win";
        // probs[1] = matchResult.get("B") / (matchResult.get("A") + matchResult.get("B"));
        // markets.addMarket(market);
        // }
    }

    /**
     * 
     * @param csGrid as generated for the "FT:CS" market
     * @return SG return normalised correct score grid
     */
    @SuppressWarnings("unused")
    private double[][] creatScoreGrid(Map<String, Double> csGrid) {
        double[][] SG = new double[maxNoGoalsPerTeam][maxNoGoalsPerTeam];
        double normTemp = 0;
        // normalising
        for (Map.Entry<String, Double> entry : csGrid.entrySet())
            normTemp += entry.getValue();

        for (Map.Entry<String, Double> entry : csGrid.entrySet()) {
            String scores = entry.getKey();
            String[] temp_scores = scores.split("-");
            int scoreA = Integer.parseInt(temp_scores[0]);
            int scoreB = Integer.parseInt(temp_scores[1]);
            SG[scoreB][scoreA] = entry.getValue() / normTemp;
        }
        return SG;
    }

}
