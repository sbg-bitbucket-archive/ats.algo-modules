package ats.algo.sport.futsal;

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

    static double getGoalDistribution(boolean matchLengthIs40Mins, int extraTimeMinutes, FutsalMatchPeriod matchPeriod,
                    int elapsedTimeSecs, int injuryTimeSecs) {
        if (matchPeriod == FutsalMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == FutsalMatchPeriod.IN_EXTRA_TIME_SECOND_HALF)
            if (extraTimeMinutes == 5) {
                return determine5ExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
            }

        if (matchPeriod == FutsalMatchPeriod.IN_SHOOTOUT)
            return determineShootOutGoalDistribution();
        if (matchLengthIs40Mins)
            return determine40MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else
            return determine50MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
    }

    private static double determine40MinsGoalDistribution(FutsalMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = 0.1 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 5);
        } else if (period <= 20 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.13 / (periodsPerMinute * 5);
        } else if (period <= 35 * periodsPerMinute) {
            goalDist = 0.14 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.19 / (periodsPerMinute * 5);
            // throw new IllegalArgumentException(" Should not go here");
        }
        return goalDist;
    }

    private static double determine50MinsGoalDistribution(FutsalMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        double period = (double) elapsedTimeSecs / timeIncrementSecs;
        if (period < 4.166 * periodsPerMinute) {
            goalDist = 0.05 / (periodsPerMinute * 4.166);
        } else if (period < 8.332 * periodsPerMinute) {
            goalDist = 0.06 / (periodsPerMinute * 4.166);
        } else if (period < 12.498 * periodsPerMinute) {
            goalDist = 0.06 / (periodsPerMinute * 4.166);
        } else if (period < 16.664 * periodsPerMinute) {
            goalDist = 0.07 / (periodsPerMinute * 4.166);
        } else if (period < 20.83 * periodsPerMinute) {
            goalDist = 0.07 / (periodsPerMinute * 4.166);
        } else if (period < 24.996 * periodsPerMinute) {
            goalDist = 0.07 / (periodsPerMinute * 4.166);
        } else if (period <= 29.162 * periodsPerMinute) {
            goalDist = 0.07 / (periodsPerMinute * 4.166);
        } else if (period <= 33.328 * periodsPerMinute) {
            goalDist = 0.08 / (periodsPerMinute * 4.166);
        } else if (period <= 37.494 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 4.166);
        } else if (period <= 41.66 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 4.166);
        } else if (period <= 45.826 * periodsPerMinute) {
            goalDist = 0.13 / (periodsPerMinute * 4.166);
        } else {
            goalDist = 0.14 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determine5ExtraMinsGoalDistribution(FutsalMatchPeriod matchPeriod, int elapsedTimeSecs) {
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

}
