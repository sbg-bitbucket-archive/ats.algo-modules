package ats.algo.sport.bandy;

import ats.algo.sport.bandy.GoalDistribution;
import ats.algo.sport.bandy.BandyMatchPeriod;

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

    static double getGoalDistribution(boolean matchLengthIs90Mins, int extraTimeMinutes, BandyMatchPeriod matchPeriod,
                    int elapsedTimeSecs, int injuryTimeSecs) {
        if (matchPeriod == BandyMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == BandyMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
            if (extraTimeMinutes == 15) {
                return determine15ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }

        if (matchPeriod == BandyMatchPeriod.IN_SHOOTOUT)
            return determineShootOutGoalDistribution();
        if (matchLengthIs90Mins)
            return determine90MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else
            throw new IllegalArgumentException("No goal distribution for the selected length of match");

    }

    private static double determine90MinsGoalDistribution(BandyMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 15 * periodsPerMinute) {
            goalDist = 0.156666667 / (periodsPerMinute * 15);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.156666667 / (periodsPerMinute * 15);
        } else if (period < 45 * periodsPerMinute) {
            goalDist = 0.156666667 / (periodsPerMinute * 15);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.1766666 / (periodsPerMinute * 15);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.1766666 / (periodsPerMinute * 15);
        } else {
            goalDist = 0.1766666 / (periodsPerMinute * (15));
        }
        return goalDist;
    }

    static double determine15ExtraMinsGoalDistribution(BandyMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 65 * periodsPerMinute) {
            goalDist = 0.0833333333 * 0.25 / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.0833333333 * 0.25 / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.0833333333 * 0.25 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.0833333333 * 0.25 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

}
