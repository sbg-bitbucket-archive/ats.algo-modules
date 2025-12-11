package ats.algo.sport.afl;

import java.util.List;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;
import ats.algo.sport.afl.AflMatchState;
import ats.algo.sport.afl.GoalInfo;

public class AflMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Afl if we need
     * to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Jin
     * 
     */
    private TwoWayStatistic moneyLine;
    private TwoWayStatistic halfTimeLeader;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic nextBehind;
    private ThreeWayStatistic nextPoint;
    private ThreeWayStatistic matchResult;
    private ThreeWayStatistic matchDoubleChance;
    private TotalStatistic pointsTotal;
    private TotalStatistic pointsTotalA;
    private TotalStatistic pointsTotalB;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic pointsHandicap;

    private TwoWayPlusStatistic pointsHandicapHome24;
    private TwoWayPlusStatistic pointsHandicapHome39;
    // private TwoWayStatistic pointsHandicapAway24;
    // private TwoWayStatistic pointsHandicapAway39;

    private HandicapStatistic dummyQ1Handicap;
    private HandicapStatistic halfHandicap;
    private TotalStatistic goalsTotalCurrentPeriod;
    private TotalStatistic pointsTotalEndOfCurrentPeriod;
    private TotalStatistic pointsTotalEndOfQ1;
    private TotalStatistic pointsTotalEndOfCurrentPeriodA;
    private TotalStatistic pointsTotalEndOfCurrentPeriodB;
    private TwoWayStatistic periodWinner;
    private ThreeWayStatistic periodWinnerAmelco;
    private TwoWayStatistic highestScoreHalf;
    private NWayStatistic highestScoreQuarter;
    private String[] quarterList = {"First Quarter", "Second Quarter", "Third Quarter", "Fourth Quarter"};

    private TwoWayStatistic overTimePossible;
    private static final int maxNoGoalsPerTeam = 450;
    private NWayStatistic winningMargin;
    private String[] winningMarginList = {"Team A 1-39", "Team A 40+", "Team B 1-39", "Team B 40+", "Draw"};

    private NWayStatistic winningMarginTwo;
    private String[] winningMarginListTwo = {"Team A 1-24", "Team A 25+", "Team B 1-24", "Team B 25+", "Team Draw"};

    // private NWayStatistic winningMargin10P;
    // private String[] winningMarginList10P = { "A 1-9", "A 10-19", "A 20-29", "A 30-39","A 40-49",
    // "A 50-59","A
    // 60-69", "A 70-79","A 80-89", "A 90-99","A 90-99", "A 100+", "Draw"
    // ,"B 1-9", "B 10-19", "B 20-29", "B 30-39","B 40-49", "B 50-59","B 60-69", "B 70-79","B 80-89",
    // "B 90-99","B
    // 90-99", "B 100+" };

    private NWayStatistic triBet;
    private NWayStatistic triBet2;
    private NWayStatistic triBet3;
    private String[] triBetList = {"Team A win by more than 15.5 points", "Team B win by more than 15.5 points",
            "Either Team to win by less than 15.5 points"};
    private String[] triBetList2 = {"Team A win by more than 15.5 points", "Team B win by more than 24.5 points",
            "Either Team to win by less than 24.5 points"};
    private String[] triBetList3 = {"Team A win by more than 15.5 points", "Team B win by more than 39.5 points",
            "Either Team to win by less than 39.5 points"};

    private NWayStatistic firstScoringQuarter;
    private NWayStatistic firstScoringMatch;
    private String[] firstScoringQuarterList =
                    {"Team A Goal", "Team A Behind", "Team B Goal", "Team B Behind", "No Score"};

    private String[] oddEvenList = {"Odd", "Even"};
    private NWayStatistic totalPointsOddEven;
    private NWayStatistic totalPointsOddEvenA;
    private NWayStatistic totalPointsOddEvenB;
    private NWayStatistic quarterPointsOddEven;
    private NWayStatistic q1PointsOddEven;

    private NWayStatistic lastScoringQuarter;
    private NWayStatistic lastScoringMatch;
    private String[] lastScoringQuarterList =
                    {"Team A Goal", "Team A Behind", "Team B Goal", "Team B Behind", "No Score"};

    private TwoWayStatistic firstTeamToScoreQuarter;
    private TwoWayStatistic firstTeamToScoreMatch;

    private TwoWayStatistic lastTeamToScoreInQuarter;
    private TwoWayStatistic lastTeamToScore;
    private TwoWayStatistic firstTeamToScoreTenQuarter;
    private TwoWayStatistic firstTeamToScoreFifteenQuarter;
    private TwoWayStatistic firstTeamToScoreTwentyQuarter;
    private TwoWayStatistic firstTeamToScoreFifteen;
    private TwoWayStatistic firstTeamToScoreTwentyFive;
    private TwoWayStatistic firstTeamToScoreThirtyFive;
    private TwoWayStatistic firstTeamToScoreFifty;

    private TwoWayStatistic firstTo3B;
    private TwoWayStatistic firstTo4B;
    private TwoWayStatistic firstTo5B;
    private TwoWayStatistic firstTo6B;

    private TwoWayStatistic firstTo3G;
    private TwoWayStatistic firstTo4G;
    private TwoWayStatistic firstTo5G;
    private TwoWayStatistic firstTo6G;

    private TwoWayStatistic bothTo40Pts;
    private TwoWayStatistic bothTo50Pts;
    private TwoWayStatistic bothTo60Pts;
    private TwoWayStatistic bothTo70Pts;
    private TwoWayStatistic bothTo80Pts;
    private TwoWayStatistic bothTo90Pts;
    private TwoWayStatistic bothTo100Pts;

    private TotalStatistic totalGoalsOverUnder;
    private TotalStatistic totalGoalsOverUnderA;
    private TotalStatistic totalGoalsOverUnderB;

    private TotalStatistic totalBehindsOverUnder;
    private TotalStatistic totalBehindsOverUnderA;
    private TotalStatistic totalBehindsOverUnderB;

    private TwoWayStatistic thirdQuarterTimeLeader;
    private NWayStatistic halfTimeFullTime;
    private String[] halTimeFullTimeResultList = {"Team A / Team A", "Team A / Team B", "Team A / Draw",
            "Draw / Team A", "Draw / Draw", "Draw / Team B", "Team B / Team A", "Team B / Draw", "Team B / Team B"};

    private NWayStatistic timeOfFirstGoal;
    private NWayStatistic timeOfFirstGoalQuarter;
    private String[] timeOfFirstGoalList = {"0.00-1.00", "1.01-2.00", "2.01-3.00", "3.01-4.00", "4.01-5.00", "5.01+"};

    private NWayStatistic quarterByQuarterLeader;
    private String[] quarterByQuarterLeaderList = {"Team A  / Team A / Team A / Team A",
            "Team A / Team A / Team A / Team B", "Team A / Team A / Team B / Team A",
            "Team A / Team A / Team B / Team B", "Team A / Team B / Team A / Team A",
            "Team A / Team B / Team A / Team B", "Team A / Team B / Team B / Team A",
            "Team A / Team B / Team B / Team B", "Team B / Team A / Team A / Team A",
            "Team B / Team A / Team A / Team B", "Team B / Team A / Team B / Team A",
            "Team B / Team A / Team B / Team B", "Team B / Team B / Team A / Team A",
            "Team B / Team B / Team A / Team B", "Team B / Team B / Team B / Team A",
            "Team B / Team B / Team B / Team B", "Draw at End Of any Quarter"};

    AflMatchMarketsFactory(AflMatchState matchState) {

        String matchSequenceId = matchState.getSequenceIdForMatch();
        String pointSequenceId = matchState.getSequenceIdForPoint(0);
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String goalSequenceId = matchState.getSequenceIdForGoal(0);
        String behindSequenceId = matchState.getSequenceIdForBehind(0);

        int periodNo = matchState.getPeriodNo();
        Boolean preMatchMarket = (matchState.getMatchPeriod().equals(AflMatchPeriod.PREMATCH));
        Boolean generateMarketFT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketFTOT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketOT = false;
        Boolean inMatchNormalTime = true;
        String halfMatchSequenceId = matchState.getSequenceIdForHalf(0);
        String halfTimeMarkets = "";
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first period
            case 2: // second period
                halfTimeMarkets = "First Half ";
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 3: // third period
            case 4: // extra time
                halfTimeMarkets = "Second Half ";
                generateMarketFT = true;
                generateMarketFTOT = true;
                generateMarketOT = true;
                break;
            default:
                generateMarketFT = false;
                generateMarketFTOT = false;
                inMatchNormalTime = false;
                generateMarketOT = false;
        }

        matchResult = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, false, matchSequenceId,
                        "Team A", "Team B", "Draw");

        moneyLine = new TwoWayStatistic("FT:ML", "Match Betting", MarketGroup.GOALS, true, matchSequenceId, "Team A",
                        "Team B");

        nextGoal = new ThreeWayStatistic("FTOT:NG", "Next Goal", MarketGroup.GOALS, true, goalSequenceId, "Team A",
                        "Team B", "No Score");

        nextBehind = new ThreeWayStatistic("FTOT:NB", "Next Behind", MarketGroup.GOALS, true, behindSequenceId,
                        "Team A", "Team B", "No Score");

        nextPoint = new ThreeWayStatistic("FTOT:NP", "Next Point", MarketGroup.GOALS, true, pointSequenceId, "Team A",
                        "Team B", "No Score");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", MarketGroup.GOALS, false, matchSequenceId,
                        "Yes", "No");

        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", MarketGroup.GOALS, false, matchSequenceId,
                        "Team A / Draw", "Team B / Draw", "Team A / Team B");

        pointsTotal = new TotalStatistic("FT:OU", "Total Points", MarketGroup.GOALS,
                        generateMarketFT && inMatchNormalTime, matchSequenceId, 2 * maxNoGoalsPerTeam);
        pointsTotalA = new TotalStatistic("FT:OU:P:A", "Total Points - A", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);
        pointsTotalB = new TotalStatistic("FT:OU:P:B", "Total Points - B", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);
        pointsHandicap = new HandicapStatistic("FT:SPRD", "Match Handicap", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);


        pointsHandicapHome24 = new TwoWayPlusStatistic("FT:PLUS:24", "A +24.5", MarketGroup.GOALS, generateMarketFT,
                        "M", "Team A +24.5", "Team B +24.5");
        pointsHandicapHome39 = new TwoWayPlusStatistic("FT:PLUS:39", "A +39.5", MarketGroup.GOALS, generateMarketFT,
                        "M", "Team A +39.5", "Team B + 39.5");
        // pointsHandicapAway24 = new TwoWayStatistic("FT:PLUS:24:B", "B +24.5", MarketGroup.GOALS,
        // false, "M", "Yes", "No");
        // pointsHandicapAway39 = new TwoWayStatistic("FT:PLUS:39:B", "B +39.5", MarketGroup.GOALS,
        // false, "M", "Yes", "No");


        halfHandicap = new HandicapStatistic("HT:SPRD", halfTimeMarkets + "Handicap", MarketGroup.GOALS,
                        generateMarketFT, halfMatchSequenceId, maxNoGoalsPerTeam);

        boolean generateMarketFTDQ1 = false;
        generateMarketFTDQ1 = periodNo < 2;
        dummyQ1Handicap = new HandicapStatistic("P:SPRD", "Dummy Quarter 1 Handicap", MarketGroup.GOALS,
                        generateMarketFTDQ1, ("P" + Integer.toString(periodNo)), maxNoGoalsPerTeam);

        matchWinnerEuroHandicap = new EuroHandicapStatistic("FT:EH", "Euro Handicap", MarketGroup.GOALS, false,
                        matchSequenceId, maxNoGoalsPerTeam);

        String marketDescription = String.format(" ");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Quarter Total Goals";
                break;
            case 2:
                marketDescription = "2nd Quarter Total Goals";
                break;
            case 3:
                marketDescription = "3rd Quarter Total Goals";
                break;
            case 4:
                marketDescription = "4th Quarter Total Goals";
                break;

            case 6: // second half
                break;
            default:
                marketDescription = "";
        }
        goalsTotalCurrentPeriod = new TotalStatistic("P:OU:G", marketDescription, MarketGroup.GOALS, generateMarketFT,
                        periodSequenceId, maxNoGoalsPerTeam);
        boolean generateEOP = false;
        boolean generateEOP1 = false;
        String marketDescriptionA = "";
        String marketDescriptionB = "";
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "End of 1st Quarter Total Points";
                marketDescriptionA = "Total Points - Over/Under - A at the end of the 1st Quarter";
                marketDescriptionB = "Total Points - Over/Under - B at the end of the 1st Quarter";
                generateEOP = true;
                generateEOP1 = true;
                periodSequenceId = String.format("P%d", 2);
                break;
            case 2:
                marketDescription = "End of 2nd Quarter Total Points";
                marketDescriptionA = "Total Points - Over/Under - A at the end of the 2nd Quarter";
                marketDescriptionB = "Total Points - Over/Under - B at the end of the 2nd Quarter";
                generateEOP = true;
                break;
            case 3:
                marketDescription = "End of 3rd Quarter Total Points";
                marketDescriptionA = "Total Points - Over/Under - A at the end of the 3rd Quarter";
                marketDescriptionB = "Total Points - Over/Under - B at the end of the 3rd Quarter";
                generateEOP = true;
                break;
            case 4:
                marketDescription = "End of 4th Quarter total points";
                generateEOP = false;
                break;

            case 6: // second half
                break;
            default:
                marketDescription = "";
        }
        pointsTotalEndOfCurrentPeriod = new TotalStatistic("P:OU:P", marketDescription, MarketGroup.GOALS, generateEOP,
                        periodSequenceId, maxNoGoalsPerTeam);

        pointsTotalEndOfQ1 = new TotalStatistic("P:OU", "End of 1st Quarter Total Points", MarketGroup.GOALS,
                        generateEOP1, "M", maxNoGoalsPerTeam);

        pointsTotalEndOfCurrentPeriodA = new TotalStatistic("P:OU:A", marketDescriptionA, MarketGroup.GOALS,
                        generateEOP, periodSequenceId, maxNoGoalsPerTeam);
        pointsTotalEndOfCurrentPeriodB = new TotalStatistic("P:OU:B", marketDescriptionB, MarketGroup.GOALS,
                        generateEOP, periodSequenceId, maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "1st Period Result";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Period Result";
                break;

            case 3: // first half
                marketDescription = "3rd Period Result";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Period Result";
                break;

            default:
                marketDescription = "";
        }

        highestScoreHalf = new TwoWayStatistic("FT:HSH", "Highest Scoring Half", MarketGroup.GOALS, generateMarketFT,
                        "M", "First Half", "Second Half");


        highestScoreQuarter =
                        new NWayStatistic("FT:HSQ", "Highest Scoring Quarter", generateMarketFT, "M", quarterList);

        periodWinner = new TwoWayStatistic("P:W", marketDescription, MarketGroup.GOALS, generateMarketFT,
                        String.format("P%d", matchState.getPeriodNo()), "Team A", "Team B");

        periodWinnerAmelco = new ThreeWayStatistic("P:W:AM", marketDescription, MarketGroup.GOALS, generateMarketFT,
                        String.format("P%d", matchState.getPeriodNo()), "Team A", "Team B", "Draw");

        winningMargin = new NWayStatistic("FTOT:WM", "Winning Margin", true, "M", winningMarginList);
        winningMarginTwo = new NWayStatistic("FTOT:WM2", "Winning Margin 1 - 24", true, "M", winningMarginListTwo);
        // winningMargin10P = new NWayStatistic("FTOT:WM10P", "Winning Margin 10 Points", true, "M",
        // winningMarginList10P);

        triBet = new NWayStatistic("FTOT:TB15", "Tri-Bet - 15.5", true, "M", triBetList);
        triBet2 = new NWayStatistic("FTOT:TB24", "Tri-Bet - 24.5", true, "M", triBetList2);
        triBet3 = new NWayStatistic("FTOT:TB39", "Tri-Bet - 39.5", true, "M", triBetList3);
        String periodId = "";

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                periodId = String.format("P%d", 1);
                break;
            case 2: // first half
                periodId = String.format("P%d", 2);
                break;
            // case 2: //half time
            case 3: // second half
                periodId = String.format("P%d", 3);
                break;

            case 4: // first half
                periodId = String.format("P%d", 4);
                break;

            default:
                marketDescription = "";
        }

        String firstScoringPeriodId = "";
        boolean creatFirstScoringMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1:
            case 2: // first half
                firstScoringPeriodId = String.format("P%d", 2);
                marketDescription = "1st Scoring Play - 2nd Quarter";
                if (matchState.getFirstScoringPlay2Quarter() == 0)
                    creatFirstScoringMarket = true;
                break;
            // case 2: //half time
            case 3: // second half
                firstScoringPeriodId = String.format("P%d", 3);
                marketDescription = "1st Scoring Play - 3rd Quarter";
                if (matchState.getFirstScoringPlay3Quarter() == 0)
                    creatFirstScoringMarket = true;
                break;

            case 4: // first half
                firstScoringPeriodId = String.format("P%d", 4);
                marketDescription = "1st Scoring Play - 4th Quarter";
                if (matchState.getFirstScoringPlay4Quarter() == 0)
                    creatFirstScoringMarket = true;
                break;

            default:
                marketDescription = "";
        }
        firstScoringQuarter = new NWayStatistic("P:FSQ", marketDescription, creatFirstScoringMarket,
                        firstScoringPeriodId, firstScoringQuarterList);
        firstScoringMatch = new NWayStatistic("FT:FSQ", "1st Scoring Play",
                        matchState.getPointsA() + matchState.getPointsA() <= 0, "M", firstScoringQuarterList);

        String marketDescription2 = "";
        String marketDescription3 = "";
        boolean creatLastTeamScoreMarket = false;
        boolean creatLastScoringMarket = false;
        String lastScoringPeriodId = "";
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                lastScoringPeriodId = String.format("P%d", 1);
                marketDescription2 = "Last Scoring Play - 1nd Quarter";
                marketDescription3 = "Last Team to Score - 1nd Quarter";
                creatLastScoringMarket = true;
                creatLastTeamScoreMarket = true;
                break;
            case 2: // first half
                lastScoringPeriodId = String.format("P%d", 2);
                marketDescription2 = "Last Scoring Play - 2nd Quarter";
                marketDescription3 = "Last Team to Score - 2nd Quarter";
                creatLastScoringMarket = true;
                creatLastTeamScoreMarket = true;
                break;
            // case 2: //half time
            case 3: // second half
                lastScoringPeriodId = String.format("P%d", 3);
                marketDescription2 = "Last Scoring Play - 3rd Quarter";
                marketDescription3 = "Last Team to Score - 3nd Quarter";
                creatLastScoringMarket = true;
                creatLastTeamScoreMarket = true;
                break;

            case 4: // first half
                lastScoringPeriodId = String.format("P%d", 4);
                firstScoringPeriodId = String.format("P%d", 4);
                marketDescription2 = "Last Scoring Play - 4th Quarter";
                creatLastScoringMarket = true;
                creatLastTeamScoreMarket = true;
                break;

            default:
                marketDescription = "";
        }
        lastScoringQuarter = new NWayStatistic("P:LSQ", marketDescription2, creatLastScoringMarket, lastScoringPeriodId,
                        lastScoringQuarterList);
        lastScoringMatch = new NWayStatistic("FT:LSQ", "Last Scoring Play", true, "M", lastScoringQuarterList);

        lastTeamToScoreInQuarter = new TwoWayStatistic("P:LTSQ", marketDescription3, creatLastTeamScoreMarket,
                        lastScoringPeriodId, "A", "B");

        lastTeamToScore = new TwoWayStatistic("FT:LTTS", "Last Team to Score", creatLastTeamScoreMarket, "M", "Team A",
                        "Team B");

        totalPointsOddEven = new NWayStatistic("FT:OE", "Total Points Even/Odd",
                        matchState.isCreatingTotalPointsOddEvenMarket(), "M", oddEvenList);

        totalPointsOddEvenA = new NWayStatistic("FT:A:OE", "Total Points Even/Odd",
                        matchState.isCreatingTotalPointsOddEvenMarket(), "M", oddEvenList);

        totalPointsOddEvenB = new NWayStatistic("FT:B:OE", "Total Points Even/Odd",
                        matchState.isCreatingTotalPointsOddEvenMarket(), "M", oddEvenList);

        boolean creatQuarterOddEven = false;
        /// System.out.println(matchState.generatingOddEvenMarketForQuarterNo());
        switch (matchState.generatingOddEvenMarketForQuarterNo()) {

            case 0: // pre-match
                creatQuarterOddEven = true;
                break;
            case 1:
                creatQuarterOddEven = true;
                break;
            case 2: // first half
                creatQuarterOddEven = true;
                firstScoringPeriodId = String.format("P%d", 2);
                marketDescription = "Total Points - Even/Odd - At the end of the 2nd Quarter";
                creatFirstScoringMarket = true;
                break;
            // case 2: //half time
            case 3: // second half
                creatQuarterOddEven = true;
                firstScoringPeriodId = String.format("P%d", 3);
                marketDescription = "Total Points - Even/Odd - At the end of the 3rd Quarter";
                creatFirstScoringMarket = true;
                break;

            default:
                marketDescription = "";
        }
        quarterPointsOddEven = new NWayStatistic("P:E:OE", marketDescription, creatQuarterOddEven, firstScoringPeriodId,
                        oddEvenList);
        q1PointsOddEven = new NWayStatistic("P:OE", "Total Points - Even/Odd - At the end of the 1st Quarter",
                        periodNo < 2, "M", oddEvenList);
        boolean creatQuarterMarter = false;
        switch (matchState.getPeriodNo()) {
            case 0: // pre-match
            case 1:
            case 2: // second quarter
                creatQuarterMarter = true;
                firstScoringPeriodId = String.format("P%d", 2);
                marketDescription = "First team to Score - 2nd Quarter";
                break;
            // case 2: //half time
            case 3: // second quarter
                creatQuarterMarter = true;
                firstScoringPeriodId = String.format("P%d", 3);
                marketDescription = "First team to Score - 3rd Quarter";
                break;
            case 4: // fourth quarter
                creatQuarterMarter = true;
                firstScoringPeriodId = String.format("P%d", 4);
                marketDescription = "First team to Score - 4th Quarter";
                break;
            default:
                marketDescription = "";
        }
        if (!matchState.checkListForFirstScoreTeamForQuarter(matchState.getPeriodNo()).equals(TeamId.UNKNOWN))
            creatQuarterMarter = false;

        firstTeamToScoreQuarter = new TwoWayStatistic("P:FTTS", marketDescription, MarketGroup.GOALS,
                        creatQuarterMarter, firstScoringPeriodId, "Team A", "Team B");
        firstTeamToScoreMatch = new TwoWayStatistic("FT:FTTS", "First team to Score", MarketGroup.GOALS,
                        matchState.getPointsA() + matchState.getPointsB() == 0, "M", "Team A", "Team B");



        boolean creatFirstToScore10Quarter =
                        matchState.checkListForScoreXXPoints(matchState.getPeriodNo(), 10).equals(TeamId.UNKNOWN);
        creatFirstToScore10Quarter =
                        creatFirstToScore10Quarter && (matchState.getPeriodNo() > 1 && matchState.getPeriodNo() <= 4);

        boolean creatFirstToScore15Quarter =
                        matchState.checkListForScoreXXPoints(matchState.getPeriodNo(), 15).equals(TeamId.UNKNOWN);
        creatFirstToScore15Quarter =
                        creatFirstToScore15Quarter && (matchState.getPeriodNo() > 1 && matchState.getPeriodNo() <= 4);

        boolean creatFirstToScore20Quarter =
                        matchState.checkListForScoreXXPoints(matchState.getPeriodNo(), 20).equals(TeamId.UNKNOWN);
        creatFirstToScore20Quarter =
                        creatFirstToScore20Quarter && (matchState.getPeriodNo() > 1 && matchState.getPeriodNo() <= 4);

        firstTeamToScoreTenQuarter = new TwoWayStatistic("P:FTTS:10",
                        "First Team to Score 10 Points -In the " + matchState.getPeriodNo() + " Quarter",
                        MarketGroup.GOALS, creatFirstToScore10Quarter, firstScoringPeriodId, "Team A", "Team B");

        firstTeamToScoreFifteenQuarter = new TwoWayStatistic("P:FTTS:15",
                        "First Team to Score 15 Points -In the " + matchState.getPeriodNo() + " Quarter",
                        MarketGroup.GOALS, creatFirstToScore15Quarter, firstScoringPeriodId, "Team A", "Team B");

        firstTeamToScoreTwentyQuarter = new TwoWayStatistic("P:FTTS:20",
                        "First Team to Score 20 Points -In the " + matchState.getPeriodNo() + " Quarter",
                        MarketGroup.GOALS, creatFirstToScore20Quarter, firstScoringPeriodId, "Team A", "Team B");

        // 25 35 55
        boolean creatFirstToScore15match = (matchState.getPointsA() + matchState.getPointsB()) < 15;
        firstTeamToScoreFifteen = new TwoWayStatistic("FT:FTTS:15", "First Team to Score 15 Points ", MarketGroup.GOALS,
                        creatFirstToScore15match, "M", "Team A", "Team B");

        boolean creatFirstToScore25Quarter = (matchState.getPointsA() + matchState.getPointsB()) < 25;
        firstTeamToScoreTwentyFive = new TwoWayStatistic("FT:FTTS:25", "First Team to Score 25 Points ",
                        MarketGroup.GOALS, creatFirstToScore25Quarter, "M", "Team A", "Team B");

        boolean creatFirstToScore35Quarter = (matchState.getPointsA() + matchState.getPointsB()) < 35;
        firstTeamToScoreThirtyFive = new TwoWayStatistic("FT:FTTS:35", "First Team to Score 35 Points ",
                        MarketGroup.GOALS, creatFirstToScore35Quarter, "M", "Team A", "Team B");

        boolean creatFirstToScore55Quarter = (matchState.getPointsA() + matchState.getPointsB()) < 55;
        firstTeamToScoreFifty = new TwoWayStatistic("FT:FTTS:50", "First Team to Score 55 Points ", MarketGroup.GOALS,
                        creatFirstToScore55Quarter, "M", "Team A", "Team B");

        firstTo3B = new TwoWayStatistic("FTOT:FT3B", "First to 3 Behinds", MarketGroup.GOALS,
                        (matchState.getFirstTo3B().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo4B = new TwoWayStatistic("FTOT:FT4B", "First to 4 Behinds", MarketGroup.GOALS,
                        (matchState.getFirstTo4B().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo5B = new TwoWayStatistic("FTOT:FT5B", "First to 5 Behinds", MarketGroup.GOALS,
                        (matchState.getFirstTo5B().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo6B = new TwoWayStatistic("FTOT:FT6B", "First to 6 Behinds", MarketGroup.GOALS,
                        (matchState.getFirstTo6B().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");

        firstTo3G = new TwoWayStatistic("FTOT:FT3G", "First to 3 Goals", MarketGroup.GOALS,
                        (matchState.getFirstTo3G().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo4G = new TwoWayStatistic("FTOT:FT4G", "First to 4 Goals", MarketGroup.GOALS,
                        (matchState.getFirstTo4G().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo5G = new TwoWayStatistic("FTOT:FT5G", "First to 5 Goals", MarketGroup.GOALS,
                        (matchState.getFirstTo5G().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");
        firstTo6G = new TwoWayStatistic("FTOT:FT6G", "First to 6 Goals", MarketGroup.GOALS,
                        (matchState.getFirstTo6G().equals(TeamId.UNKNOWN)), "M", "Team A", "Team B");

        bothTo40Pts = new TwoWayStatistic("FT:BTT:40", "Both Teams to 40pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo50Pts = new TwoWayStatistic("FT:BTT:50", "Both Teams to 50pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo60Pts = new TwoWayStatistic("FT:BTT:60", "Both Teams to 60pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo70Pts = new TwoWayStatistic("FT:BTT:70", "Both Teams to 70pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo80Pts = new TwoWayStatistic("FT:BTT:80", "Both Teams to 80pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo90Pts = new TwoWayStatistic("FT:BTT:90", "Both Teams to 90pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");
        bothTo100Pts = new TwoWayStatistic("FT:BTT:100", "Both Teams to 100pts", MarketGroup.GOALS, preMatchMarket, "M",
                        "Yes", "No");

        totalGoalsOverUnder = new TotalStatistic("FT:OU:G", "Total Goals Over/Under", MarketGroup.GOALS, preMatchMarket,
                        "M", maxNoGoalsPerTeam);
        totalGoalsOverUnderA = new TotalStatistic("FT:OU:G:A", "Total Goals - A", MarketGroup.GOALS, preMatchMarket,
                        "M", maxNoGoalsPerTeam);
        totalGoalsOverUnderB = new TotalStatistic("FT:OU:G:B", "Total Goals - B", MarketGroup.GOALS, preMatchMarket,
                        "M", maxNoGoalsPerTeam);

        totalBehindsOverUnder = new TotalStatistic("FT:OU:B", "Total Behinds Over/Under", MarketGroup.GOALS,
                        preMatchMarket, "M", maxNoGoalsPerTeam);
        totalBehindsOverUnderA = new TotalStatistic("FT:OU:B:A", "Total Behinds - A", MarketGroup.GOALS, preMatchMarket,
                        "M", maxNoGoalsPerTeam);
        totalBehindsOverUnderB = new TotalStatistic("FT:OU:B:B", "Total Behinds - B", MarketGroup.GOALS, preMatchMarket,
                        "M", maxNoGoalsPerTeam);

        thirdQuarterTimeLeader = new TwoWayStatistic("P:E:W", "Third Quarter Time Leader", MarketGroup.GOALS,
                        preMatchMarket, "M", "Team A", "Team B");

        halfTimeFullTime = new NWayStatistic("FT:HTFT", "Half Time/ Full Time", MarketGroup.GOALS, preMatchMarket, "M",
                        halTimeFullTimeResultList);
        boolean goalHappendAlready = matchState.getGoalsA() + matchState.getGoalsB() == 0;
        boolean quaterGoalHappendAlready = matchState.getTimeOfFirstGoalQuarter() == -1;
        timeOfFirstGoal = new NWayStatistic("FT:TOFG", "Time of First Goal", MarketGroup.GOALS, goalHappendAlready, "M",
                        timeOfFirstGoalList);

        timeOfFirstGoalQuarter = new NWayStatistic("P:TOFG", "Time of First Goal Quarter", MarketGroup.GOALS,
                        creatQuarterMarter && quaterGoalHappendAlready, periodId, timeOfFirstGoalList);

        quarterByQuarterLeader = new NWayStatistic("FT:QBQ", "Quarter by Quarter Leader", MarketGroup.GOALS,
                        preMatchMarket, "M", quarterByQuarterLeaderList);

        halfTimeLeader = new TwoWayStatistic("HT:W", "Half Time Leader", MarketGroup.GOALS, preMatchMarket, "M",
                        "Team A", "Team B");

    }

    void updateStats(AflMatchState matchState, AflMatchFacts matchFacts) {

        // for(GoalInfo item: matchState.getGoalInfoList()){
        // System.out.println(item.getMins() + " " + item.getTeam() + " " +item.getMethod()+ " ");
        // }
        boolean preMatchMarkets = matchFacts.getCurrentMatchPeriod().equals(AflMatchPeriod.PREMATCH);

        int pointsA = matchState.getPointsA();
        int pointsB = matchState.getPointsB();

        int q1A = matchState.getQ1PointsA();
        int q1B = matchState.getQ1PointsB();
        int q2A = matchState.getQ2PointsA();
        int q2B = matchState.getQ2PointsB();
        int q3A = matchState.getQ3PointsA();
        int q3B = matchState.getQ3PointsB();
        int q4A = matchState.getQ4PointsA();
        int q4B = matchState.getQ4PointsB();
        int i = pointsA - pointsB;
        int winingMargingIndicator = 4;
        int winingMargingIndicatorTwo = 4;
        // int winingMargingIndicator10P = 11;
        int triBetFactor = 2;
        int triBetFactor2 = 2;
        int triBetFactor3 = 2;
        if (i > 0 && i <= 39) {
            winingMargingIndicator = 0;
        } else if (i < 0 && i >= -39) {
            winingMargingIndicator = 2;
        } else if (i > 40) {
            winingMargingIndicator = 1;
        } else if (i < -40) {
            winingMargingIndicator = 3;
        }

        if (i > 15) {
            triBetFactor = 0;
        } else if (i < -15) {
            triBetFactor = 1;
        }

        if (i > 24) {
            triBetFactor2 = 0;
        } else if (i < -24) {
            triBetFactor2 = 1;
        }

        if (i > 39) {
            triBetFactor3 = 0;
        } else if (i < -39) {
            triBetFactor3 = 1;
        }
        // if (i > 0 ) {
        // winingMargingIndicator10P = (int) ((double )i/10);
        // } else if (i < 0 ) {
        // winingMargingIndicator10P = 12+(int) ((double )(-i)/10);
        // } else if (i == 0) {
        // winingMargingIndicator10P = 11;
        // }


        if (i > 0 && i <= 24) {
            winingMargingIndicatorTwo = 0;
        } else if (i < 0 && i >= -24) {
            winingMargingIndicatorTwo = 2;
        } else if (i > 24) {
            winingMargingIndicatorTwo = 1;
        } else if (i < -24) {
            winingMargingIndicatorTwo = 3;
        }

        winningMargin.increment(winingMargingIndicator);
        triBet.increment(triBetFactor);
        triBet2.increment(triBetFactor2);
        triBet3.increment(triBetFactor3);

        winningMarginTwo.increment(winingMargingIndicatorTwo);
        // winningMargin10P.increment(winingMargingIndicator10P);

        if (pointsA != pointsB) {
            moneyLine.increment(pointsA > pointsB);
        }


        int normalTimeGoalsA = matchState.getNormalTimePointsA();
        int normalTimeGoalsB = matchState.getNormalTimePointsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
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

        pointsTotal.increment(pointsA + pointsB);

        pointsTotalA.increment(pointsA);
        pointsTotalB.increment(pointsB);
        pointsHandicap.increment(pointsA - pointsB);

        if ((pointsA - pointsB) > 24.5)
            pointsHandicapHome24.increment(true);
        else if ((pointsA - pointsB) < -24.5)
            pointsHandicapHome24.increment(false);
        else {
            pointsHandicapHome24.increment(true);
            pointsHandicapHome24.increment(false);
        }
        pointsHandicapHome24.incrementCount(1);

        if ((pointsA - pointsB) > 39.5)
            pointsHandicapHome39.increment(true);
        else if ((pointsA - pointsB) < -39.5)
            pointsHandicapHome39.increment(false);
        else {
            pointsHandicapHome39.increment(true);
            pointsHandicapHome39.increment(false);
        }
        pointsHandicapHome39.incrementCount(1);


        // pointsHandicapAway24.increment(pointsB + 24.5 > pointsA);
        // pointsHandicapAway39.increment(pointsB + 39.5 > pointsA);

        int periodNo = matchFacts.getCurrentPeriodNo();
        if (periodNo == 0 || periodNo == 1 || periodNo == 2) {
            halfHandicap.increment(matchState.getQ1PointsA() - matchState.getQ1PointsB() + matchState.getQ2PointsA()
                            - matchState.getQ2PointsB());
        } else if (periodNo == 3 || periodNo == 4) {
            halfHandicap.increment((matchState.getQ3PointsA() - matchState.getQ3PointsB() + matchState.getQ4PointsA()
                            - matchState.getQ4PointsB()));
        }
        if (periodNo == 0 || periodNo == 1) {
            dummyQ1Handicap.increment(matchState.getQ1PointsA() - matchState.getQ1PointsB());
        }

        matchWinnerEuroHandicap.increment(pointsA - pointsB);
        goalsTotalCurrentPeriod.increment(matchFacts.getGoalsTotalCurrentPeriod());

        int totalPointsEndOfQuarter = 0;
        int eoqA = 0;
        int eoqB = 0;
        if (matchFacts.getCurrentPeriodNo() == 1) {
            eoqA = matchState.getQ1PointsA();
            eoqB = matchState.getQ1PointsB();
            totalPointsEndOfQuarter = eoqA + eoqB;
            pointsTotalEndOfCurrentPeriodA.increment(eoqA);
            pointsTotalEndOfCurrentPeriodB.increment(eoqB);
            pointsTotalEndOfQ1.increment(totalPointsEndOfQuarter);
            pointsTotalEndOfCurrentPeriod.increment(matchState.getQ1PointsA() + matchState.getQ2PointsA()
                            + matchState.getQ1PointsB() + matchState.getQ2PointsB());
        } else if (matchFacts.getCurrentPeriodNo() == 2) {
            eoqA = matchState.getQ1PointsA() + matchState.getQ2PointsA();
            eoqB = matchState.getQ1PointsB() + matchState.getQ2PointsB();

            totalPointsEndOfQuarter = eoqA + eoqB;
            pointsTotalEndOfCurrentPeriodA.increment(eoqA);
            pointsTotalEndOfCurrentPeriodB.increment(eoqB);
            pointsTotalEndOfCurrentPeriod.increment(totalPointsEndOfQuarter);

        } else if (matchFacts.getCurrentPeriodNo() == 3) {
            eoqA = matchState.getQ1PointsA() + matchState.getQ2PointsA() + matchState.getQ3PointsA();
            eoqB = matchState.getQ1PointsB() + matchState.getQ2PointsB() + matchState.getQ3PointsB();

            totalPointsEndOfQuarter = eoqA + eoqB;
            pointsTotalEndOfCurrentPeriodA.increment(eoqA);
            pointsTotalEndOfCurrentPeriodB.increment(eoqB);
            pointsTotalEndOfCurrentPeriod.increment(totalPointsEndOfQuarter);
        }


        // int periodGoalsA = matchFacts.getCurrentPeriodGoalsA();
        // int periodGoalsB = matchFacts.getCurrentPeriodGoalsB();
        int periodPointsA = matchFacts.getCurrentPeriodPointsA();
        int periodPointsB = matchFacts.getCurrentPeriodPointsB();
        teamId = TeamId.UNKNOWN;
        if (periodPointsA > periodPointsB)
            teamId = TeamId.A;
        if (periodPointsA < periodPointsB)
            teamId = TeamId.B;

        periodWinnerAmelco.increment(teamId);

        if (teamId.equals(TeamId.A)) {
            periodWinner.increment(true);
        } else if (teamId.equals(TeamId.B)) {
            periodWinner.increment(false);
        }
        nextGoal.increment(matchFacts.getNextToScoreGoal());
        nextBehind.increment(matchFacts.getNextToScoreBehind());
        nextPoint.increment(matchFacts.getNextPoint());
        /* cj added current period winner */

        int firstScoringIndicator = 0;
        // int periodNo = matchFacts.getCurrentPeriodNo();

        if (periodNo <= 2) {
            firstScoringIndicator = matchState.getFirstScoringPlay2Quarter();
            if (firstScoringIndicator != 0)
                firstScoringQuarter.increment(firstScoringIndicator - 1);
        } else if (periodNo == 3) {
            firstScoringIndicator = matchState.getFirstScoringPlay3Quarter();
            if (firstScoringIndicator != 0)
                firstScoringQuarter.increment(firstScoringIndicator - 1);
        } else if (periodNo == 4) {
            firstScoringIndicator = matchState.getFirstScoringPlay4Quarter();
            if (firstScoringIndicator != 0)
                firstScoringQuarter.increment(firstScoringIndicator - 1);
        }

        if (matchFacts.getTimeOfFirstGoal() == -1) {

            firstScoringIndicator = matchState.checkListForFirstScoreTeamMethod();

            firstScoringMatch.increment(firstScoringIndicator);
        }

        TeamId lastScoringTeamInQuarter = TeamId.UNKNOWN;
        List<GoalInfo> goalInfoList = matchState.getGoalInfoList();
        if (periodNo == 1) {
            firstScoringIndicator = checkListForLastScore(goalInfoList, periodNo); //
            lastScoringTeamInQuarter = matchState.checkListForTeamLastScoreInQuarter(goalInfoList, periodNo);
            if (lastScoringTeamInQuarter.equals(TeamId.A))
                lastTeamToScoreInQuarter.increment(true);
            else if (lastScoringTeamInQuarter.equals(TeamId.B))
                lastTeamToScoreInQuarter.increment(false);
            lastScoringQuarter.increment(firstScoringIndicator);
        } else if (periodNo == 2) {
            firstScoringIndicator = checkListForLastScore(goalInfoList, periodNo);
            lastScoringTeamInQuarter = matchState.checkListForTeamLastScoreInQuarter(goalInfoList, periodNo);
            if (lastScoringTeamInQuarter.equals(TeamId.A))
                lastTeamToScoreInQuarter.increment(true);
            else if (lastScoringTeamInQuarter.equals(TeamId.B))
                lastTeamToScoreInQuarter.increment(false);
            lastScoringQuarter.increment(firstScoringIndicator);
        } else if (periodNo == 3) {
            firstScoringIndicator = checkListForLastScore(goalInfoList, periodNo);
            lastScoringTeamInQuarter = matchState.checkListForTeamLastScoreInQuarter(goalInfoList, periodNo);
            if (lastScoringTeamInQuarter.equals(TeamId.A))
                lastTeamToScoreInQuarter.increment(true);
            else if (lastScoringTeamInQuarter.equals(TeamId.B))
                lastTeamToScoreInQuarter.increment(false);
            lastScoringQuarter.increment(firstScoringIndicator);
        } else if (periodNo == 4) {
            firstScoringIndicator = checkListForLastScore(goalInfoList, periodNo);
            lastScoringTeamInQuarter = matchState.checkListForTeamLastScoreInQuarter(goalInfoList, periodNo);
            if (lastScoringTeamInQuarter.equals(TeamId.A))
                lastTeamToScoreInQuarter.increment(true);
            else if (lastScoringTeamInQuarter.equals(TeamId.B))
                lastTeamToScoreInQuarter.increment(false);
            lastScoringQuarter.increment(firstScoringIndicator);
        }


        firstScoringIndicator = checkListForLastScore(goalInfoList, -1);
        lastScoringMatch.increment(firstScoringIndicator);

        lastScoringTeamInQuarter = matchState.checkListForTeamLastScoreInQuarter(goalInfoList, -1);
        if (lastScoringTeamInQuarter.equals(TeamId.A))
            lastTeamToScore.increment(true);
        else if (lastScoringTeamInQuarter.equals(TeamId.B))
            lastTeamToScore.increment(false);

        if ((pointsA + pointsB) % 2 == 0)
            totalPointsOddEven.increment(1);
        else
            totalPointsOddEven.increment(0);

        if ((pointsA) % 2 == 0)
            totalPointsOddEvenA.increment(1);
        else
            totalPointsOddEvenA.increment(0);

        if ((pointsB) % 2 == 0)
            totalPointsOddEvenB.increment(1);
        else
            totalPointsOddEvenB.increment(0);

        int quarterOddEvenNo = matchFacts.getCurrentOddEvenMarketForQuarterNo();
        int quarterA = 0;
        int quarterB = 0;
        if (quarterOddEvenNo == 0) {
            // do nothing
        } else if (quarterOddEvenNo == 2) {
            quarterA = matchState.getQ2PointsA() + matchState.getQ1PointsA();
            quarterB = matchState.getQ2PointsB() + matchState.getQ1PointsB();
            if ((quarterA + quarterB) % 2 == 0)
                quarterPointsOddEven.increment(1);
            else
                quarterPointsOddEven.increment(0);
        } else if (quarterOddEvenNo == 3) {
            quarterA = matchState.getQ3PointsA() + matchState.getQ2PointsA() + matchState.getQ1PointsA();
            quarterB = matchState.getQ3PointsB() + matchState.getQ2PointsB() + matchState.getQ1PointsB();
            if ((quarterA + quarterB) % 2 == 0)
                quarterPointsOddEven.increment(1);
            else
                quarterPointsOddEven.increment(0);
        }

        if (periodNo <= 1) {
            quarterA = matchState.getQ1PointsA();
            quarterB = matchState.getQ1PointsB();
            if ((quarterA + quarterB) % 2 == 0)
                q1PointsOddEven.increment(1);
            else
                q1PointsOddEven.increment(0);

        }

        if (matchState.checkListForFirstScoreTeamForQuarter(periodNo).equals(TeamId.A))
            firstTeamToScoreQuarter.increment(true);
        else if (matchState.checkListForFirstScoreTeamForQuarter(periodNo).equals(TeamId.B))
            firstTeamToScoreQuarter.increment(false);


        if (matchState.checkListForFirstScoreTeamForQuarter(-1).equals(TeamId.A))
            firstTeamToScoreMatch.increment(true);
        else if (matchState.checkListForFirstScoreTeamForQuarter(-1).equals(TeamId.B))
            firstTeamToScoreMatch.increment(false);

        if (matchFacts.getCurrentFirstScoreToTen().equals(TeamId.UNKNOWN)) {
            TeamId teamScore10Points = matchState.checkListForScoreXXPoints(periodNo, 10);
            if (matchFacts.currentPeriodNo > 1 && matchFacts.currentPeriodNo <= 4)
                if (teamScore10Points.equals(TeamId.A))
                    firstTeamToScoreTenQuarter.increment(true);
                else if (teamScore10Points.equals(TeamId.B))
                    firstTeamToScoreTenQuarter.increment(false);
        }
        if (matchFacts.getCurrentFirstScoreToFifteen().equals(TeamId.UNKNOWN)) {
            TeamId teamScore15Points = matchState.checkListForScoreXXPoints(periodNo, 15);
            if (matchFacts.currentPeriodNo > 1 && matchFacts.currentPeriodNo <= 4)
                if (teamScore15Points.equals(TeamId.A))
                    firstTeamToScoreFifteenQuarter.increment(true);
                else if (teamScore15Points.equals(TeamId.B))
                    firstTeamToScoreFifteenQuarter.increment(false);
        }
        if (matchFacts.getCurrentFirstScoreToTwenty().equals(TeamId.UNKNOWN)) {
            TeamId teamScore20Points = matchState.checkListForScoreXXPoints(periodNo, 20);
            if (matchFacts.currentPeriodNo > 1 && matchFacts.currentPeriodNo <= 4)
                if (teamScore20Points.equals(TeamId.A))
                    firstTeamToScoreTwentyQuarter.increment(true);
                else if (teamScore20Points.equals(TeamId.B))
                    firstTeamToScoreTwentyQuarter.increment(false);
        }

        int currentPointsA = matchFacts.getCurrentPointsA();
        int currentPointsB = matchFacts.getCurrentPointsB();

        // FIXME
        TeamId firstTeamScore15Points = matchState.checkListForScoreXXPoints(0, 15);
        if (currentPointsA < 15 && currentPointsB < 15)
            if (firstTeamScore15Points.equals(TeamId.A))
                firstTeamToScoreFifteen.increment(true);
            else if (firstTeamScore15Points.equals(TeamId.B))
                firstTeamToScoreFifteen.increment(false);

        TeamId firstTeamScore25Points = matchState.checkListForScoreXXPoints(0, 25);
        if (currentPointsA < 25 && currentPointsB < 25)
            if (firstTeamScore25Points.equals(TeamId.A))
                firstTeamToScoreTwentyFive.increment(true);
            else if (firstTeamScore25Points.equals(TeamId.B))
                firstTeamToScoreTwentyFive.increment(false);

        TeamId firstTeamScore35Points = matchState.checkListForScoreXXPoints(0, 35);
        if (currentPointsA < 35 && currentPointsB < 35)
            if (firstTeamScore35Points.equals(TeamId.A))
                firstTeamToScoreThirtyFive.increment(true);
            else if (firstTeamScore35Points.equals(TeamId.B))
                firstTeamToScoreThirtyFive.increment(false);

        TeamId firstTeamScore50Points = matchState.checkListForScoreXXPoints(0, 50);
        if (currentPointsA < 50 && currentPointsB < 50)
            if (firstTeamScore50Points.equals(TeamId.A))
                firstTeamToScoreFifty.increment(true);
            else if (firstTeamScore50Points.equals(TeamId.B))
                firstTeamToScoreFifty.increment(false);

        if (matchFacts.getFirstTo3B().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo3B().equals(TeamId.A))
                firstTo3B.increment(true);
            else if (matchState.getFirstTo3B().equals(TeamId.B))
                firstTo3B.increment(false);
        }

        if (matchFacts.getFirstTo4B().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo4B().equals(TeamId.A))
                firstTo4B.increment(true);
            else if (matchState.getFirstTo4B().equals(TeamId.B))
                firstTo4B.increment(false);
        }

        if (matchFacts.getFirstTo5B().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo5B().equals(TeamId.A))
                firstTo5B.increment(true);
            else if (matchState.getFirstTo5B().equals(TeamId.B))
                firstTo5B.increment(false);
        }

        if (matchFacts.getFirstTo6B().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo6B().equals(TeamId.A))
                firstTo6B.increment(true);
            else if (matchState.getFirstTo6B().equals(TeamId.B))
                firstTo6B.increment(false);
        }

        if (matchFacts.getFirstTo3G().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo3G().equals(TeamId.A))
                firstTo3G.increment(true);
            else if (matchState.getFirstTo3G().equals(TeamId.B))
                firstTo3G.increment(false);
        }

        if (matchFacts.getFirstTo4G().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo4G().equals(TeamId.A))
                firstTo4G.increment(true);
            else if (matchState.getFirstTo4G().equals(TeamId.B))
                firstTo4G.increment(false);
        }

        if (matchFacts.getFirstTo5G().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo5G().equals(TeamId.A))
                firstTo5G.increment(true);
            else if (matchState.getFirstTo5G().equals(TeamId.B))
                firstTo5G.increment(false);
        }

        if (matchFacts.getFirstTo6G().equals(TeamId.UNKNOWN)) {
            if (matchState.getFirstTo6G().equals(TeamId.A))
                firstTo6G.increment(true);
            else if (matchState.getFirstTo6G().equals(TeamId.B))
                firstTo6G.increment(false);
        }


        highestScoreHalf.increment((q1A + q1B) + (q2A + q2B) > (q3A + q3B) + (q4A + q4B));

        int hqi = 0;
        if ((q1A + q1B) > (q2A + q2B) && (q1A + q1B) > (q3A + q3B) && (q1A + q1B) > (q4A + q4B))
            hqi = 0;
        else if ((q2A + q2B) > (q1A + q1B) && (q2A + q2B) > (q3A + q3B) && (q2A + q2B) > (q4A + q4B))
            hqi = 1;
        else if ((q3A + q3B) > (q1A + q1B) && (q3A + q3B) > (q2A + q2B) && (q3A + q3B) > (q4A + q4B))
            hqi = 2;
        else
            hqi = 3;

        highestScoreQuarter.increment(hqi);

        int timeOfFirstGoalMatch = matchFacts.getTimeOfFirstGoal();
        if (timeOfFirstGoalMatch == -1) {
            int selection = 0;
            if (matchState.getTimeOfFirstGoalMatch() < 60) {
            } else if (matchState.getTimeOfFirstGoalMatch() < 120) {
                selection = 1;
            } else if (matchState.getTimeOfFirstGoalMatch() < 180) {
                selection = 2;
            } else if (matchState.getTimeOfFirstGoalMatch() < 240) {
                selection = 3;
            } else if (matchState.getTimeOfFirstGoalMatch() < 300) {
                selection = 4;
            } else {
                selection = 5;
            }
            timeOfFirstGoal.increment(selection);
        }

        int timeOfFirstGoalQuarterTemp = matchFacts.getTimeOfFirstGoalQuarter();
        if (timeOfFirstGoalQuarterTemp != -1) {
            int selection = 99;
            int simTime = matchFacts.getTimeOfFirstGoalQuarter();
            if (simTime < 60) {
                selection = 0;
            } else if (simTime < 120) {
                selection = 1;
            } else if (simTime < 180) {
                selection = 2;
            } else if (simTime < 240) {
                selection = 3;
            } else if (simTime < 300) {
                selection = 4;
            } else {
                selection = 5;
            }
            timeOfFirstGoalQuarter.increment(selection);
        }

        if (preMatchMarkets) {
            int goalsA = matchState.getGoalQ1A() + matchState.getGoalQ2A() + matchState.getGoalQ3A()
                            + matchState.getGoalQ4A();
            int goalsB = matchState.getGoalQ1B() + matchState.getGoalQ2B() + matchState.getGoalQ3B()
                            + matchState.getGoalQ4B();
            int behindsA = matchState.getBehindQ1A() + matchState.getBehindQ2A() + matchState.getBehindQ3A()
                            + matchState.getBehindQ4A();
            int behindsB = matchState.getBehindQ1B() + matchState.getBehindQ2B() + matchState.getBehindQ3B()
                            + matchState.getBehindQ4B();
            int thirdQPointsA = matchState.getQ1PointsA() + matchState.getQ2PointsA() + matchState.getQ3PointsA();
            int thirdQPointsB = matchState.getQ1PointsB() + matchState.getQ2PointsB() + matchState.getQ3PointsB();

            bothTo40Pts.increment(pointsA >= 40 && pointsB >= 40);
            bothTo50Pts.increment(pointsA >= 50 && pointsB >= 50);
            bothTo60Pts.increment(pointsA >= 60 && pointsB >= 60);
            bothTo70Pts.increment(pointsA >= 70 && pointsB >= 70);
            bothTo80Pts.increment(pointsA >= 80 && pointsB >= 80);
            bothTo90Pts.increment(pointsA >= 90 && pointsB >= 90);
            bothTo100Pts.increment(pointsA >= 100 && pointsB >= 100);

            totalGoalsOverUnder.increment(goalsA + goalsB);
            totalGoalsOverUnderA.increment(goalsA);
            totalGoalsOverUnderB.increment(goalsB);

            totalBehindsOverUnder.increment(behindsA + behindsB);
            totalBehindsOverUnderA.increment(behindsA);
            totalBehindsOverUnderB.increment(behindsB);
            if (thirdQPointsA != thirdQPointsB)
                thirdQuarterTimeLeader.increment(thirdQPointsA - thirdQPointsB > 0);

            int halfTimeA = matchState.getQ1PointsA() + matchState.getQ2PointsA();
            int halfTimeB = matchState.getQ1PointsB() + matchState.getQ2PointsB();
            if (normalTimeGoalsA > normalTimeGoalsB && halfTimeA > halfTimeB)
                halfTimeFullTime.increment(0);
            if (normalTimeGoalsA < normalTimeGoalsB && halfTimeA > halfTimeB)
                halfTimeFullTime.increment(1);
            if (normalTimeGoalsA == normalTimeGoalsB && halfTimeA > halfTimeB)
                halfTimeFullTime.increment(2);
            if (normalTimeGoalsA > normalTimeGoalsB && halfTimeA == halfTimeB)
                halfTimeFullTime.increment(3);
            if (normalTimeGoalsA == normalTimeGoalsB && halfTimeA == halfTimeB)
                halfTimeFullTime.increment(4);
            if (normalTimeGoalsA < normalTimeGoalsB && halfTimeA == halfTimeB)
                halfTimeFullTime.increment(5);
            if (normalTimeGoalsA > normalTimeGoalsB && halfTimeA < halfTimeB)
                halfTimeFullTime.increment(6);
            if (normalTimeGoalsA == normalTimeGoalsB && halfTimeA < halfTimeB)
                halfTimeFullTime.increment(7);
            if (normalTimeGoalsA < normalTimeGoalsB && halfTimeA < halfTimeB)
                halfTimeFullTime.increment(8);

            int quarterLeader = 16;
            // int q1A = matchState.getQ1PointsA();
            // int q1B = matchState.getQ1PointsB();
            // int q2A = matchState.getQ2PointsA();
            // int q2B = matchState.getQ2PointsB();
            // int q3A = matchState.getQ3PointsA();
            // int q3B = matchState.getQ3PointsB();
            // int q4A = matchState.getQ4PointsA();
            // int q4B = matchState.getQ4PointsB();

            // { "HHHH", "HHHA", "HHAH", "HHAA", "HAHH", "HAHA", "HAAH","HAAA",
            // "AHHH","AHHA","AHAH","AHAA","AAHH","AAHA","AAAH","AAAA","Draw At End Of Any Quarter" };
            if (q1A > q1B) {
                if (q2A > q2B) { // HH
                    if (q3A > q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 0; // HHHH
                        } else if (q4A < q4B) {
                            quarterLeader = 1; // HHHA
                        }
                    } else if (q3A < q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 2; // HHAH
                        } else if (q4A < q4B) {
                            quarterLeader = 3; // HHAA
                        }
                    }

                } else if (q2A < q2B) { // HA
                    if (q3A > q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 4; // HAHH
                        } else if (q4A < q4B) {
                            quarterLeader = 5; // HAHA
                        }
                    } else if (q3A < q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 6; // HAAH
                        } else if (q4A < q4B) {
                            quarterLeader = 7; // HAAA
                        }
                    }
                }

            } else if (q1A < q1B) {// A
                if (q2A > q2B) { // AH
                    if (q3A > q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 8; // AHHH
                        } else if (q4A < q4B) {
                            quarterLeader = 9; // AHHA
                        }
                    } else if (q3A < q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 10; // AHAH
                        } else if (q4A < q4B) {
                            quarterLeader = 11; // AHAA
                        }
                    }

                } else if (q2A < q2B) { // AA
                    if (q3A > q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 12; // AAHH
                        } else if (q4A < q4B) {
                            quarterLeader = 13; // AAHA
                        }
                    } else if (q3A < q3B) {
                        if (q4A > q4B) {
                            quarterLeader = 14; // AAAH
                        } else if (q4A < q4B) {
                            quarterLeader = 15; // AAAA
                        }
                    }
                }
            }

            quarterByQuarterLeader.increment(quarterLeader);

            if (q1A + q2A != q1B + q2B)
                halfTimeLeader.increment(q1A + q2A > q1B + q2B);

        }

    }

    // FIXME: SHOULE GONE INTO AFLMATCH STATE
    private int checkListForLastScore(List<GoalInfo> goalInfoList, int periodNo) {
        int lastScoreInPeriodSeconds = -1;
        TeamId team = TeamId.UNKNOWN;
        int scoreMethod = -1;
        if (periodNo != -1) {
            for (GoalInfo item : goalInfoList) {
                if (item.getPeriodNoG() == periodNo) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                    scoreMethod = item.getMethod();
                }
            }
        } else {
            for (GoalInfo item : goalInfoList) {
                int scoreSecs = item.getSecs();
                if (scoreSecs > lastScoreInPeriodSeconds) {
                    lastScoreInPeriodSeconds = item.getSecs();
                    team = item.getTeam();
                    scoreMethod = item.getMethod();
                }
            }

        }

        if (scoreMethod == 0 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 0;
        } else if (scoreMethod == 1 && team.equals(TeamId.A)) {
            lastScoreInPeriodSeconds = 1;
        } else if (scoreMethod == 0 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 2;
        } else if (scoreMethod == 1 && team.equals(TeamId.B)) {
            lastScoreInPeriodSeconds = 3;
        } else {
            lastScoreInPeriodSeconds = 4; // No score in period
        }

        return lastScoreInPeriodSeconds;
    }

}
