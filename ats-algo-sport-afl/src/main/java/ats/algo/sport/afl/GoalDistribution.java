package ats.algo.sport.afl;

import ats.algo.sport.afl.GoalDistribution;
import ats.algo.sport.afl.AflMatchPeriod;

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

    static double getGoalDistribution(int matchLength, int extraTimeMinutes, AflMatchPeriod matchPeriod,
                    int elapsedTimeSecs) {
        if (matchPeriod == AflMatchPeriod.IN_EXTRA_TIME)
            if (extraTimeMinutes == 5) {
                return determine5ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }
        if (matchLength == 80)
            return determine80MinsGoalDistribution(matchPeriod, elapsedTimeSecs);
        else
            throw new IllegalArgumentException("No goal distribution for the selected length of match");

    }

    private static double determine80MinsGoalDistribution(AflMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 45 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 55 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 65 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.0625 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.0625 / (periodsPerMinute * (5));
        }
        return goalDist;
    }

    // private static double determine48MinsGoalDistribution(AflMatchPeriod matchPeriod, int elapsedTimeSecs) {
    // double goalDist;
    // int period = elapsedTimeSecs / timeIncrementSecs;
    //
    // if (period < 6 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 6);
    // } else if (period < 12 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 6);
    // } else if (period < 18 * periodsPerMinute) {
    // goalDist = 0.115 / (periodsPerMinute * 6);
    // } else if (period < 24 * periodsPerMinute) {
    // goalDist = 0.115 / (periodsPerMinute * 6);
    // } else if (period < 30 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 6);
    // } else if (period < 36 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 6);
    // } else if (period < 42 * periodsPerMinute) {
    // goalDist = 0.135 / (periodsPerMinute * 6);
    // } else {
    // goalDist = 0.135 / (periodsPerMinute * 6);
    // }
    // return goalDist;
    // }

    static double determine5ExtraMinsGoalDistribution(AflMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 65 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.125 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.125 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

}
