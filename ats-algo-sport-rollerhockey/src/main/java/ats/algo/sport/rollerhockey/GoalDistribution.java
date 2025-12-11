package ats.algo.sport.rollerhockey;

import ats.algo.sport.rollerhockey.GoalDistribution;
import ats.algo.sport.rollerhockey.RollerhockeyMatchPeriod;

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

    static double getGoalDistribution(boolean matchLengthIs90Mins, int extraTimeMinutes,
                    RollerhockeyMatchPeriod matchPeriod, int elapsedTimeSecs, int injuryTimeSecs) {
        if (matchPeriod == RollerhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == RollerhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
            if (extraTimeMinutes == 5) {
                return determine5ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }

        if (matchPeriod == RollerhockeyMatchPeriod.IN_SHOOTOUT)
            return determineShootOutGoalDistribution();
        if (matchLengthIs90Mins)
            return determine40MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else
            throw new IllegalArgumentException("No goal distribution for the selected length of match");

    }

    private static double determine40MinsGoalDistribution(RollerhockeyMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.125 / (periodsPerMinute * (5));
        }
        return goalDist;
    }

    static double determine5ExtraMinsGoalDistribution(RollerhockeyMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 41 * periodsPerMinute) {
            goalDist = 0.025 / (periodsPerMinute * 1);
        } else if (period < 42 * periodsPerMinute) {
            goalDist = 0.025 / (periodsPerMinute * 1);
        } else if (period < 43 * periodsPerMinute) {
            goalDist = 0.025 / (periodsPerMinute * 1);
        } else if (period < 44 * periodsPerMinute) {
            goalDist = 0.025 / (periodsPerMinute * 1);
        } else if (period < 45 * periodsPerMinute) {
            goalDist = 0.025 / (periodsPerMinute * 1);
        } else {
            throw new IllegalArgumentException("Goal distribution entered 5 min plus extra time.");
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

}
