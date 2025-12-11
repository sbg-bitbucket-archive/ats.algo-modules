package ats.algo.sport.floorball;

import ats.algo.sport.floorball.GoalDistribution;
import ats.algo.sport.floorball.FloorballMatchPeriod;

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
                    FloorballMatchPeriod matchPeriod, int elapsedTimeSecs, int injuryTimeSecs) {
        if (matchPeriod == FloorballMatchPeriod.IN_EXTRA_TIME)
            if (extraTimeMinutes == 5) {
                return determine5ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }
        if (extraTimeMinutes == 20) {
            return determine20ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
        }

        if (matchPeriod == FloorballMatchPeriod.IN_SHOOTOUT)
            return determineShootOutGoalDistribution();
        if (matchLengthIs80Mins)
            throw new IllegalArgumentException("No goal distribution for 80 minutes");
        else
            return determine60MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
    }

    private static double determine60MinsGoalDistribution(FloorballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = 0.0775 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.0775 / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = 0.0775 / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.0775 / (periodsPerMinute * 5);
        }

        else if (period < 25 * periodsPerMinute) {
            goalDist = 0.085 / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.085 / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = 0.085 / (periodsPerMinute * 5);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = 0.085 / (periodsPerMinute * 5);
        }

        else if (period < 45 * periodsPerMinute) {
            goalDist = 0.0875 / (periodsPerMinute * 5);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.0875 / (periodsPerMinute * 5);
        } else if (period < 55 * periodsPerMinute) {
            goalDist = 0.0875 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.0875 / (periodsPerMinute * (5));
        }
        return goalDist;
    }

    static double determine5ExtraMinsGoalDistribution(FloorballMatchPeriod matchPeriod, int elapsedTimeSecs) {
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

    static double determine20ExtraMinsGoalDistribution(FloorballMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 65 * periodsPerMinute) {
            goalDist = 0.3333 * 0.25 / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.3333 * 0.25 / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.3333 * 0.25 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.3333 * 0.25 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

}
