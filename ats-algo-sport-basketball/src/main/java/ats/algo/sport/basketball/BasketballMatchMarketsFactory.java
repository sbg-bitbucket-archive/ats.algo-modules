package ats.algo.sport.basketball;

import java.util.ArrayList;

import ats.algo.core.MarketGroup;
import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;

public class BasketballMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Basketball if
     * we need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Geoff
     * 
     */
    private TwoWayStatistic moneyLine;
    private TwoWayStatistic raceTo20Points;
    private ThreeWayStatistic matchResult;
    private ThreeWayStatistic matchHalfResult;
    private ThreeWayStatistic matchDoubleChance;
    private TotalStatistic goalsTotal;
    private TotalStatistic goalsTotalHalfTime;
    private TotalStatistic goalsTotalAHalfTime;
    private TotalStatistic goalsTotalBHalfTime;
    private TotalStatistic goalsTotalA;
    private TotalStatistic goalsTotalB;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic goalsHandicap;
    private HandicapStatistic goalsHandicapHalfTime;
    private TotalStatistic goalsTotalCurrentPeriod;
    private ThreeWayStatistic periodWinner;

    private TwoWayStatistic overTimePossible;
    private static final int maxNoGoalsPerTeam = 600;

    private boolean twoHalvesFormat;


    // @SuppressWarnings("unused")
    // private ThreeWayStatistic raceTo20Points;
    // private TwoWayStatistic goalsTotalOddEven;
    // private NWayStatistic doubleResult;
    // private String[] doubleResultList = {"AA", "XB", "BA", "AB", "XB", "BB"};

    BasketballMatchMarketsFactory(BasketballMatchState matchState) {
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String matchSequenceId = matchState.getSequenceIdForMatch();
        twoHalvesFormat = matchState.isTwoHalvesFormat();
        int periodNo = matchState.getPeriodNo();
        String halfSequenceId = matchState.getSequenceIdForHalf(0);


        // add extra

        Boolean generateMarketFT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketFTOT = false;
        @SuppressWarnings("unused")
        Boolean generateMarketOT = false;
        Boolean inMatchNormalTime = true;
        // Boolean generateMarket = false;
        switch (periodNo) {
            case 0: // pre-match
            case 1: // first period
            case 2: // second period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 3: // third period
            case 4: // extra time
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

        matchResult = new ThreeWayStatistic("FT:AXB", "Match Result", MarketGroup.GOALS, false, matchSequenceId, "A",
                        "B", "Draw");

        matchHalfResult = new ThreeWayStatistic("P:AXB", "Half time Result", MarketGroup.GOALS, true, halfSequenceId,
                        "A", "B", "Draw");

        moneyLine = new TwoWayStatistic("FTOT:ML", "Money Line", MarketGroup.GOALS, true, matchSequenceId, "A", "B");

        raceTo20Points = new TwoWayStatistic("FT:RT20", "Race to 20 points", MarketGroup.GOALS,
                        matchState.getRaceTo().get(2) == TeamId.UNKNOWN, matchSequenceId, "A", "B");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", MarketGroup.GOALS, false, matchSequenceId,
                        "Yes", "No");

        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", MarketGroup.GOALS, false, matchSequenceId,
                        "AX", "BX", "AB");

        goalsTotal = new TotalStatistic("FTOT:OU", "Total Points", MarketGroup.GOALS,
                        generateMarketFT && inMatchNormalTime, matchSequenceId, 2 * maxNoGoalsPerTeam);
        goalsTotalA = new TotalStatistic("FT:A:OU", "Home Team Total Points", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);
        goalsTotalB = new TotalStatistic("FT:B:OU", "Away Team Total Points", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam);

        goalsTotalHalfTime = new TotalStatistic("P:OU", "Half Time Total Points", MarketGroup.GOAL_DISTRIBUTION,
                        generateMarketFT && inMatchNormalTime, halfSequenceId, maxNoGoalsPerTeam + 100);
        goalsTotalAHalfTime = new TotalStatistic("P:A:OU", "Half Time Home Team Total Points",
                        MarketGroup.GOAL_DISTRIBUTION, generateMarketFT, halfSequenceId, maxNoGoalsPerTeam - 100);
        goalsTotalBHalfTime = new TotalStatistic("P:B:OU", "Half Time Away Team Total Points",
                        MarketGroup.GOAL_DISTRIBUTION, generateMarketFT, halfSequenceId, maxNoGoalsPerTeam - 100);

        goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap", MarketGroup.GOALS, generateMarketFT,
                        matchSequenceId, maxNoGoalsPerTeam + 100);

        goalsHandicapHalfTime = new HandicapStatistic("P:SPRD", "Half Time Handicap", MarketGroup.GOAL_DISTRIBUTION,
                        generateMarketFT, halfSequenceId, maxNoGoalsPerTeam + 50);

        matchWinnerEuroHandicap = new EuroHandicapStatistic("FT:EH", "Euro Handicap", MarketGroup.GOALS, false,
                        matchSequenceId, maxNoGoalsPerTeam + 100);

        String marketDescription = String.format(" ");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();
        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Quarter Total Points";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Quarter Total Points";
                break;
            case 3:
                marketDescription = "3rd Quarter Total Points";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Quarter Total Points";
                break;

            case 6: // second half
                break;
            default:
                marketDescription = "";
        }
        goalsTotalCurrentPeriod = new TotalStatistic("P:OU", marketDescription, MarketGroup.CORNERS, generateMarketFT,
                        periodSequenceId, maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1: // first half
                marketDescription = "1st Quarter Result";
                break;
            // case 2: //half time
            case 2: // second half
                marketDescription = "2nd Quarter Result";
                break;

            case 3: // first half
                marketDescription = "3rd Quarter Result";
                break;
            // case 2: //half time
            case 4: // second half
                marketDescription = "4th Quarter Result";
                break;

            default:
                marketDescription = "";
        }

        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription, generateMarketFT, periodSequenceId, "A", "B",
                        "Draw");

        // raceTo20Points = new ThreeWayStatistic("FT:RTC", "Race To 20 Points", MarketGroup.GOALS, false,
        // matchSequenceId,
        // "A", "B", "Neither");

        // goalsTotalOddEven = new TwoWayStatistic("FT:OE", "Total Points Odd/Even", MarketGroup.GOALS,
        // generateMarketFT,
        // "M", "Odd", "Even");


        /*
         * update for sinan
         */
        // doubleResult = new NWayStatistic("FT:DR", "Double Result", MarketGroup.GOALS, true, "M", doubleResultList);

    }

    void updateStats(BasketballMatchState matchState, BasketballMatchFacts matchFacts) {

        int goalsA = matchState.getPointsA();
        int goalsB = matchState.getPointsB();
        // if (goalsA == goalsB) {
        // throw new IllegalArgumentException("Should not happens");
        // }

        ArrayList<TeamId> racetoList = matchState.getRaceTo();
        if (racetoList.get(2) == TeamId.A)
            raceTo20Points.increment(true);
        else if (racetoList.get(2) == TeamId.B)
            raceTo20Points.increment(false);

        moneyLine.increment(goalsA > goalsB);

        int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        int normalTimeGoalsB = matchState.getNormalTimeGoalsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            matchResult.increment(teamId);

        // probMatrix[matchState.getElapsedTimeSecs() / 5][normalTimeGoalsA][normalTimeGoalsB] += 1.0;

        int periodNo = matchFacts.getCurrentPeriodNo();
        int halfPointsA = 0;
        int halfPointsB = 0;
        if (twoHalvesFormat) {
            if (periodNo < 2) {
                halfPointsA = matchState.getFirstHalfPointsA();
                halfPointsB = matchState.getFirstHalfPointsB();
            } else if (periodNo < 3) {
                halfPointsA = matchState.getSecondHalfPointsA();
                halfPointsB = matchState.getSecondHalfPointsB();
            }
        } else { // four quarters format

            if (periodNo < 3) {
                halfPointsA = matchState.getFirstQuarterPointsA() + matchState.getSecondQuarterPointsA();
                halfPointsB = matchState.getFirstQuarterPointsB() + matchState.getSecondQuarterPointsB();
            } else if (periodNo < 5) {
                halfPointsA = matchState.getThirdQuarterPointsA() + matchState.getFourthQuarterPointsA();
                halfPointsB = matchState.getThirdQuarterPointsB() + matchState.getFourthQuarterPointsB();
            }

        }
        teamId = TeamId.UNKNOWN;
        if (halfPointsA > halfPointsB)
            teamId = TeamId.A;
        if (halfPointsA < halfPointsB)
            teamId = TeamId.B;
        if (matchState.isNormalTimeMatchCompleted())
            matchHalfResult.increment(teamId);


        goalsTotalHalfTime.increment(halfPointsA + halfPointsB);
        goalsTotalAHalfTime.increment(halfPointsA);
        goalsTotalBHalfTime.increment(halfPointsB);

        goalsHandicapHalfTime.increment(halfPointsA - halfPointsB);

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

        goalsTotal.increment(goalsA + goalsB);

        goalsTotalA.increment(goalsA);
        goalsTotalB.increment(goalsB);
        goalsHandicap.increment(goalsA - goalsB);
        matchWinnerEuroHandicap.increment(goalsA - goalsB);
        goalsTotalCurrentPeriod.increment(matchFacts.getGoalsTotalCurrentPeriod());

        int periodGoalsA = matchFacts.getCurrentPeriodGoalsA();
        int periodGoalsB = matchFacts.getCurrentPeriodGoalsB();
        teamId = TeamId.UNKNOWN;
        if (periodGoalsA > periodGoalsB)
            teamId = TeamId.A;
        if (periodGoalsA < periodGoalsB)
            teamId = TeamId.B;

        periodWinner.increment(teamId);
        /* cj added current period winner */

        // goalsTotalOddEven.increment((goalsA + goalsB) % 2 == 1);
        // int firstHalfScoreA = matchState.getFirstQuarterPointsA() + matchState.getSecondQuarterPointsA();
        // int firstHalfScoreB = matchState.getFirstQuarterPointsB() + matchState.getSecondQuarterPointsB();
        // int secondHalfScoreA = matchState.getThirdQuarterPointsA() + matchState.getFourthQuarterPointsA();
        // int secondHalfScoreB = matchState.getThirdQuarterPointsB() + matchState.getFourthQuarterPointsB();

        // if (firstHalfScoreA > firstHalfScoreB && secondHalfScoreA > secondHalfScoreB)
        // doubleResult.increment(0);
        // if (firstHalfScoreA == firstHalfScoreB && secondHalfScoreA < secondHalfScoreB)
        // doubleResult.increment(1);
        // if (firstHalfScoreA < firstHalfScoreB && secondHalfScoreA > secondHalfScoreB)
        // doubleResult.increment(2);
        //
        // if (firstHalfScoreA > firstHalfScoreB && secondHalfScoreA < secondHalfScoreB)
        // doubleResult.increment(3);
        //
        // if (firstHalfScoreA == firstHalfScoreB && secondHalfScoreA < secondHalfScoreB)
        // doubleResult.increment(4);
        //
        // if (firstHalfScoreA < firstHalfScoreB && secondHalfScoreA < secondHalfScoreB)
        // doubleResult.increment(5);


        // if (goalsA == 20 && goalsA > goalsB){
        // raceTo20Points.increment(TeamId.A);
        // }
        //
        // else if (goalsB == 20 && goalsB > goalsA){
        // raceTo20Points.increment(TeamId.B);
        // }
        //
        // else{
        // raceTo20Points.increment(TeamId.UNKNOWN);
        // }
    }

}
