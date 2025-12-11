package ats.algo.sport.handball;

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

    static double getGoalDistribution(boolean matchLengthIs80Mins, HandballMatchPeriod matchPeriod,
                    int elapsedTimeSecs) {
        if (matchPeriod == HandballMatchPeriod.IN_EXTRATIME_PERIOD
                        || matchPeriod == HandballMatchPeriod.AT_EXTRATIME_PERIOD_END)
            return determineExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs, 0);
        if (matchLengthIs80Mins)
            return determine80MinsGoalDistribution(matchPeriod, elapsedTimeSecs, 0);
        else
            return determine60MinsGoalDistribution(matchPeriod, elapsedTimeSecs, 0);
    }

    private static double determine60MinsGoalDistribution(HandballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;


        if (period < 5 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        }

        else if (period < 25 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        }

        else if (period < 45 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        } else if (period < 55 * periodsPerMinute) {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        }

        else {
            goalDist = 0.0833333333 / (periodsPerMinute * 5);
        }
        return goalDist;



    }

    private static double determine80MinsGoalDistribution(HandballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 10 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 10);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.10 / (periodsPerMinute * 10);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 10);
        } else if (matchPeriod == HandballMatchPeriod.IN_FIRST_HALF) {
            goalDist = 0.13 / (periodsPerMinute * (10 + injuryTimeSecs / 60));
        } else if (period < 50 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 10);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.12 / (periodsPerMinute * 10);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = 0.16 / (periodsPerMinute * 10);
        } else {
            goalDist = 0.18 / (periodsPerMinute * (10 + injuryTimeSecs / 60));
        }
        return goalDist;
    }

    static double determineExtraMinsGoalDistribution(HandballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 5 * periodsPerMinute) {
            goalDist = 0 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0 / (periodsPerMinute * 5);
        } else if (matchPeriod == HandballMatchPeriod.IN_EXTRATIME_PERIOD) {
            goalDist = 0 / (periodsPerMinute * (5 + injuryTimeSecs / 60));
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0 / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = 0 / (periodsPerMinute * 5);
        } else {
            goalDist = 0 / (periodsPerMinute * (5 + injuryTimeSecs / 60));
        }
        return goalDist;
    }


    public static double getPeriodPointsDistribution(HandballMatchPeriod matchPeriod) {
        double goalDist;

        if (matchPeriod.equals(HandballMatchPeriod.IN_FIRST_HALF)) {
            goalDist = 0.50;
        } else if (matchPeriod.equals(HandballMatchPeriod.IN_SECOND_HALF)) {
            goalDist = 0.50;
        } else {
            goalDist = 0.0833333;
        }
        return goalDist;
    }

}
