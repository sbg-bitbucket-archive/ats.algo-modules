package ats.algo.sport.fieldhockey;

import ats.algo.core.common.TeamId;
import ats.algo.montecarloframework.MarketsFactory;

public class FieldhockeyMatchMarketsFactory extends MarketsFactory {

    /**
     * holds any facts about a simulation of a match that has just been played (via the Match class) that are need in
     * the calculation of the required statistics and that are not captured by the matchState object for Fieldhockey if
     * we need to know who won leg N+2, where N is the current leg, then we need to collect that "fact" in this class
     * 
     * @author Jin
     * 
     */
    private TwoWayStatistic puckLine;
    private ThreeWayStatistic matchWinner;
    private ThreeWayStatistic fiveMinsWinner;
    private ThreeWayStatistic matchDoubleChance;
    private CorrectScoreStatistic correctGoalScore;
    private TotalStatistic goalsTotal;
    private TotalStatistic goalsTotalA;
    private TotalStatistic goalsTotalB;
    private TwoWayStatistic periodBothTeamToScore;
    private TwoWayStatistic fiveMinsIfGoal;
    private EuroHandicapStatistic matchWinnerEuroHandicap;

    private HandicapStatistic goalsHandicap;
    private ThreeWayStatistic nextGoal;
    private ThreeWayStatistic overTimeNextGoal;
    private TotalStatistic goalsTotalCurrentPeriod;
    private ThreeWayStatistic periodWinner;
    private CorrectScoreStatistic periodCorrectGoalScore;
    private NWayStatistic timeOfFirstGoal;

    private TwoWayStatistic overTimePossible;
    private static final int maxNoGoalsPerTeam = 40;
    private boolean allowGenerateAllSelection = false;

    FieldhockeyMatchMarketsFactory(FieldhockeyMatchState matchState) {
        this.setAllowGenerateAllSelection(allowGenerateAllSelection);
        String matchSequenceId = matchState.getSequenceIdForMatch();
        String periodSequenceId = matchState.getSequenceIdForPeriod(0);
        String goalSequenceId = matchState.getSequenceIdForGoal(0);

        String fiveMinsSequenceId = matchState.getFiveMinsSequenceId();
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
            case 4:
            case 3: // third period
                generateMarketFT = true;
                generateMarketFTOT = true;
                inMatchNormalTime = true;
                break;
            case 5: // extra time
                generateMarketFT = false;
                generateMarketFTOT = true;
                generateMarketOT = true;
                break;
            default:
                generateMarketFT = false;
                generateMarketFTOT = false;
                inMatchNormalTime = false;
                generateMarketOT = false;
        }

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0))
            overTimePenaltyPossible = true;

        matchWinner = new ThreeWayStatistic("FT:AXB", "Match Result", generateMarketFT, matchSequenceId, "A", "B",
                        "Draw");

        puckLine = new TwoWayStatistic("FT:ML", "Match Winner", false, matchSequenceId, "A", "B");

        overTimePossible = new TwoWayStatistic("FT:OT", "Overtime(Yes/No)", false, matchSequenceId, "Yes", "No");


        // if (!matchState.isNormalTimeMatchCompleted())
        // generateMarketFT = true;
        fiveMinsWinner = new ThreeWayStatistic("FT:5MR", "5 Min Result", false, fiveMinsSequenceId, "A", "B", "Draw");
        fiveMinsIfGoal = new TwoWayStatistic("FT:5MG", "5 Min Goal - Yes/No", false, periodSequenceId, "Yes", "No");

        matchDoubleChance = new ThreeWayStatistic("FT:DBLC", "Double Chance", true, matchSequenceId, "AX", "BX", "AB");
        correctGoalScore = new CorrectScoreStatistic("FT:CS", "Correct Score", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        String[] timeOfFirstGoalSelections = {"No Goal", "1-10", "11-20", "21-30", "31-40", "41-50", "51-60"};

        timeOfFirstGoal = new NWayStatistic("FT:TFG", "Time of 1st Goal", false, matchSequenceId,
                        timeOfFirstGoalSelections);
        // (String key, String description, Boolean isValidGivenMatchState, String sequenceId,
        // String[] selections)

        goalsTotal = new TotalStatistic("FT:OU", "Total Goals", generateMarketFT && inMatchNormalTime, matchSequenceId,
                        2 * maxNoGoalsPerTeam);
        goalsTotalA = new TotalStatistic("FT:A:OU", "Home Team Totals", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        goalsTotalB = new TotalStatistic("FT:B:OU", "Away Team Totals", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        goalsHandicap = new HandicapStatistic("FT:SPRD", "Handicap", generateMarketFT, matchSequenceId,
                        maxNoGoalsPerTeam);
        matchWinnerEuroHandicap =
                        new EuroHandicapStatistic("FT:EH", "Euro Handicap", true, matchSequenceId, maxNoGoalsPerTeam);


        String marketDescription = String.format("Next Goal ");
        nextGoal = new ThreeWayStatistic("FT:NG", marketDescription, false, goalSequenceId, "A", "B", "No goal");
        periodSequenceId = matchState.getSequenceIdForPeriod(0);
        periodNo = matchState.getPeriodNo();
        switch (periodNo) {
            case 0:
            case 1:
                marketDescription = "1st Period Total Goals";
                break;
            case 2:
                marketDescription = "2nd Period Total Goals";
                break;
            case 3:
                marketDescription = "3rd Period Total Goals";
                break;
            case 4:
                marketDescription = "4th Period Total Goals";
                break;
            default:
                marketDescription = "";
        }
        goalsTotalCurrentPeriod = new TotalStatistic("P:OU", marketDescription, generateMarketFT, periodSequenceId,
                        maxNoGoalsPerTeam);

        switch (periodNo) {
            case 0: // pre-match
            case 1:
                marketDescription = "1st Period Correct Score";
                break;
            case 2: // second half
                marketDescription = "2nd Period Correct Score";
                break;
            case 3:
                marketDescription = "3rd Period Correct Score";
                break;
            case 4:
                marketDescription = "4th Period Correct Score";
                break;
            default:
                marketDescription = "";
        }
        periodCorrectGoalScore = new CorrectScoreStatistic("P:CS", marketDescription, generateMarketFT,
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
            case 4: // second half
                marketDescription = "4th Period Both Team To Score";
                break;
            default:
                marketDescription = "";
        }
        generateMarketFT = (matchState.getMatchPeriod() != FieldhockeyMatchPeriod.AT_HALF_TIME
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.AT_FULL_TIME
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.AT_EXTRA_TIME_HALF_TIME
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.AT_EXTRA_TIME_END
                        && matchState.getMatchPeriod() != FieldhockeyMatchPeriod.IN_SHOOTOUT
                        && (matchState.getCurrentPeriodGoalsA() == 0 || matchState.getCurrentPeriodGoalsB() == 0));
        periodBothTeamToScore = new TwoWayStatistic("P:BTTS", marketDescription, generateMarketFT, periodSequenceId,
                        "Yes", "No");

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

        periodWinner = new ThreeWayStatistic("P:AXB", marketDescription, generateMarketFT, periodSequenceId, "A", "B",
                        "Draw");

        marketDescription = String.format("Next Goal");
        overTimeNextGoal = new ThreeWayStatistic("OT:NG", marketDescription, true, goalSequenceId, "A", "B", "No goal");
        /*
         * Extra time markets
         */

    }

    void updateStats(FieldhockeyMatchState matchState, FieldhockeyMatchFacts matchFacts) {

        int goalsA = matchState.getGoalsA();
        int goalsB = matchState.getGoalsB();

        if (matchState.isPenaltiesPossible() || (matchState.getExtraPeriodSecs() > 0)) {
            // System.out.println("increment at "+goalsA+" "+goalsB);

            puckLine.increment(goalsA > goalsB);
        }

        int normalTimeGoalsA = matchState.getNormalTimeGoalsA();
        int normalTimeGoalsB = matchState.getNormalTimeGoalsB();
        TeamId teamId = TeamId.UNKNOWN;
        if (normalTimeGoalsA > normalTimeGoalsB)
            teamId = TeamId.A;
        if (normalTimeGoalsA < normalTimeGoalsB)
            teamId = TeamId.B;

        // if (matchState.isNormalTimeMatchCompleted())
        matchWinner.increment(teamId);

        int fiveMinsGoalsA = matchFacts.getFiveMinsGoalsA();
        int fiveMinsGoalsB = matchFacts.getFiveMinsGoalsB();
        teamId = TeamId.UNKNOWN;
        if (fiveMinsGoalsA > fiveMinsGoalsB)
            teamId = TeamId.A;
        if (fiveMinsGoalsA < fiveMinsGoalsB)
            teamId = TeamId.B;

        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsWinner.increment(teamId);
        if (matchState.isNormalTimeMatchCompleted())
            fiveMinsIfGoal.increment((fiveMinsGoalsA + fiveMinsGoalsB) > 0);

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

        // System.out.println(teamId);
        int timeSlotScored = (int) Math.floor(matchState.getElapsedTimeSecsFirstGoal() / 600);
        boolean timeResidual = ((int) matchState.getElapsedTimeSecsFirstGoal() % 600) == 0;
        int timeFlagStart = 0;

        if (matchState.getElapsedTimeSecsFirstGoal() == 0 || matchState.getElapsedTimeSecsFirstGoal() > 3600) {
            timeFlagStart = 0;
        } else {
            if (timeResidual) {
                timeFlagStart = timeSlotScored;
            } else {
                timeFlagStart = timeSlotScored + 1;
            }
        }
        // System.out.println(matchState.getElapsedTimeSecsFirstGoal()+" "+timeFlagStart+" "+(int)
        // matchState.getElapsedTimeSecsFirstGoal()%600);
        // System.out.println(timeFlagStart);
        timeOfFirstGoal.increment(timeFlagStart);

        correctGoalScore.increment(goalsA, goalsB);
        goalsTotal.increment(goalsA + goalsB);

        goalsTotalA.increment(goalsA);
        goalsTotalB.increment(goalsB);
        goalsHandicap.increment(goalsA - goalsB);
        matchWinnerEuroHandicap.increment(goalsA - goalsB);
        nextGoal.increment(matchFacts.getNextToScore());
        overTimeNextGoal.increment(matchFacts.getNextToScore());

        goalsTotalCurrentPeriod.increment(matchFacts.getGoalsTotalCurrentPeriod());

        int periodGoalsA = matchFacts.getCurrentPeriodGoalsA();
        int periodGoalsB = matchFacts.getCurrentPeriodGoalsB();
        periodCorrectGoalScore.increment(periodGoalsA, periodGoalsB);
        teamId = TeamId.UNKNOWN;
        if (periodGoalsA > periodGoalsB)
            teamId = TeamId.A;
        if (periodGoalsA < periodGoalsB)
            teamId = TeamId.B;

        periodWinner.increment(teamId);

        periodBothTeamToScore.increment(matchFacts.getBothTeamToScoreCurrentPeriod());

        /* cj added current period winner */

    }

}
