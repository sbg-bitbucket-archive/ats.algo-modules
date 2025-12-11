package ats.algo.sport.fieldhockey;

import ats.algo.sport.fieldhockey.GoalDistribution;
import ats.algo.sport.fieldhockey.FieldhockeyMatchPeriod;

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
                    FieldhockeyMatchPeriod matchPeriod, int elapsedTimeSecs, int injuryTimeSecs) {
        if (matchPeriod == FieldhockeyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == FieldhockeyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
            if (extraTimeMinutes == 15) {
                return determine15ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }

        if (matchPeriod == FieldhockeyMatchPeriod.IN_SHOOTOUT)
            return determineShootOutGoalDistribution();
        if (matchLengthIs90Mins)
            return determine70MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else
            throw new IllegalArgumentException("No goal distribution for the selected length of match");

    }

    private static double determine70MinsGoalDistribution(FieldhockeyMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 10 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.142857143 / (periodsPerMinute * 10);
        } else {
            goalDist = 0.142857143 / (periodsPerMinute * (10));
        }
        return goalDist;
    }

    static double determine15ExtraMinsGoalDistribution(FieldhockeyMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 65 * periodsPerMinute) {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        } else if (period < 80 * periodsPerMinute) {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        } else if (period < 85 * periodsPerMinute) {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.0714285715 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

}
