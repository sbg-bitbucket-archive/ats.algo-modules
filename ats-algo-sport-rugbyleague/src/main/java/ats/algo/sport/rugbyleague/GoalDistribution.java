package ats.algo.sport.rugbyleague;

import ats.algo.sport.rugbyleague.RugbyLeagueMatchPeriod;

public class GoalDistribution {

    private static int timeIncrementSecs = 10;
    private static int periodsPerMinute = 6;

    /**
     * determines the time related distribution of goals throughout the match
     * 
     * @param timeIncrementSecs - the time intervals used by the model
     * @param halfTimeSecs - the length of each half in normal time
     * @param extraTimeSecs - the length of each half in extra
     */
    static void setTimeIncrementSecs(int timeIncrementSecs) {
        GoalDistribution.timeIncrementSecs = timeIncrementSecs;
        GoalDistribution.periodsPerMinute = 60 / timeIncrementSecs;
    }

    static void setMatchLengthIs80Mins(boolean value) {}

    static double getGoalDistribution(boolean matchLengthIs80Mins, int extraTimeMinutes,
                    RugbyLeagueMatchPeriod matchPeriod, int elapsedTimeSecs,
                    RugbyLeagueMatchStatusType rugbyLeagueMatchStatus, int injuryTimeSecs) {

        if (rugbyLeagueMatchStatus == RugbyLeagueMatchStatusType.PENALTY) {
            return determineShootOutGoalDistribution();
        } else if (rugbyLeagueMatchStatus == RugbyLeagueMatchStatusType.CONVERSION) {
            return determineShootOutGoalDistribution();
        } else {

            if (matchPeriod == RugbyLeagueMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                            || matchPeriod == RugbyLeagueMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
                if (extraTimeMinutes == 5) {
                    return determine5ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
                }

            if (matchPeriod == RugbyLeagueMatchPeriod.IN_SHOOTOUT)
                return determineShootOutGoalDistribution();
            if (matchLengthIs80Mins)
                return determine80MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
            else
                throw new IllegalArgumentException("No goal distribution for the selected length of match");
        }
    }

    private static double determine80MinsGoalDistribution(RugbyLeagueMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 10 * periodsPerMinute) {
            goalDist = 0.11935 / (periodsPerMinute * 10);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.11935 / (periodsPerMinute * 10);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.11935 / (periodsPerMinute * 10);
        } else if (period <= 40 * periodsPerMinute) {
            goalDist = 0.11935 / (periodsPerMinute * 10);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 10);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 10);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.1363 / (periodsPerMinute * 10);
        } else if (period <= 80 * periodsPerMinute) {
            goalDist = 0.1363 / (periodsPerMinute * 10);
        } else {
            goalDist = 0;
            // throw new IllegalArgumentException(" Should not go here");
        }
        return goalDist;
    }

    static double determine5ExtraMinsGoalDistribution(RugbyLeagueMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        // if (period < 65 * periodsPerMinute) {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // } else if (period < 70 * periodsPerMinute) {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // } else if (period < 75 * periodsPerMinute) {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // } else if (period < 80 * periodsPerMinute) {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // } else if (period < 85 * periodsPerMinute) {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // } else {
        // goalDist = 0.0714285715 / (periodsPerMinute * 5);
        // }
        goalDist = 0.125 / (periodsPerMinute * 5);
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

    public static double getConversionRate() {
        return 0.5;
    }

}
