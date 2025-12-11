package ats.algo.sport.basketball;

import ats.algo.sport.basketball.GoalDistribution;
import ats.algo.sport.basketball.BasketballMatchPeriod;

public class GoalDistribution {

    private static int timeIncrementSecs;
    private static int periodsPerMinute = 6;// associate to 10 seconds in sim
    private static double periodsPerMinuteRatio = 1.0 / 6.0;// associate to 10 seconds in sim
    private static int normalTimeMinutes;
    private static int extraTimeMinutes;
    private static double q1GD; // for two halves match, q1GD+q2GD = 1st Half GD
    private static double q2GD;
    private static double q3GD;
    private static double q4GD;
    private static double qEGD; // extra time gd

    private static final double[] myDistributionQtr = new double[] {0.067629442, // first minute
            0.079862465, 0.082118271, 0.080053762, 0.081780397, 0.084115702, 0.081417679, 0.085029949, 0.087673316,
            0.085996368, 0.088475766, 0.095846883, // last minute
    };

    public GoalDistribution(boolean twoHalvesFormat, BasketballMatchParams basketballMatchParams, int normalTime,
                    int extraTime) {
        // q1GD = basketballMatchParams.getQ1GD().getMean() > 0 ? basketballMatchParams.getQ1GD().getMean() : 0.001;
        // q2GD = basketballMatchParams.getQ2GD().getMean() > 0 ? basketballMatchParams.getQ2GD().getMean() : 0.001;
        // q3GD = basketballMatchParams.getQ3GD().getMean() > 0 ? basketballMatchParams.getQ3GD().getMean() : 0.001;
        // // q4GD = basketballMatchParams.getQ4GD().getMean();
        // q4GD = (1 - (q1GD + q2GD + q3GD) > 0 ? 1 - (q1GD + q2GD + q3GD) : 0.001);
        // // assume extra time and last quarter has same gd
        // qEGD = (q4GD + q3GD) * 0.25;
        // normalTimeMinutes = normalTime;
        // extraTimeMinutes = extraTime;
    }

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

    @SuppressWarnings("static-access")
    double getGoalDistribution(int matchPeriodNo, int elapsedTimeSecs, String freeShootTeam) {
        if (matchPeriodNo == 6)// extra time period or extra time end
            if (this.extraTimeMinutes == 5) {
                return determine5ExtraMinsGoalDistribution(matchPeriodNo, elapsedTimeSecs);
            }

        if (!freeShootTeam.equals("UNKNOWN"))
            return determineShootOutGoalDistribution();
        if (this.normalTimeMinutes == 40)
            return determine40MinsGoalDistribution(matchPeriodNo, elapsedTimeSecs);
        if (this.normalTimeMinutes == 48)
            return determine48MinsGoalDistribution(matchPeriodNo, elapsedTimeSecs);
        else
            return -1.0;
        // throw new IllegalArgumentException("No goal distribution for the selected length of match");

    }

    private static double determine40MinsGoalDistribution(int matchPeriodNo, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = (q1GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = (q1GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = (q2GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = (q2GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = (q3GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = (q3GD * 0.5) / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = (q4GD * 0.5) / (periodsPerMinute * 5);
        } else {
            goalDist = (q4GD * 0.5) / (periodsPerMinute * (5));
        }



        return goalDist;
    }

    // private static double determine40MinsGoalDistributionNew(BasketballMatchPeriod matchPeriod, int elapsedTimeSecs)
    // {
    // double goalDist;
    // int period = elapsedTimeSecs / timeIncrementSecs;
    //
    // if (period < 5 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 10 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 15 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 20 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 25 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 30 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else if (period < 35 * periodsPerMinute) {
    // goalDist = 0.125 / (periodsPerMinute * 5);
    // } else {
    // goalDist = 0.125 / (periodsPerMinute * (5));
    // }
    // return goalDist;
    // }

    private static double determine48MinsGoalDistribution(int matchPeriodNo, int elapsedTimeSecs) {
        double goalDist = 0;
        // int period = elapsedTimeSecs / timeIncrementSecs;

        // if (period < 6 * periodsPerMinute) {
        // goalDist = (q1GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 12 * periodsPerMinute) {
        // goalDist = (q1GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 18 * periodsPerMinute) {
        // goalDist = (q2GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 24 * periodsPerMinute) {
        // goalDist = (q2GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 30 * periodsPerMinute) {
        // goalDist = (q3GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 36 * periodsPerMinute) {
        // goalDist = (q3GD * 0.5) / (periodsPerMinute * 6);
        // } else if (period < 42 * periodsPerMinute) {
        // goalDist = (q4GD * 0.5) / (periodsPerMinute * 6);
        // } else {
        // goalDist = (q4GD * 0.5) / (periodsPerMinute * 6);
        // }
        // if(elapsedTimeSecs>=1801)
        // System.out.println("");

        int elapsedMinutesIdx = (int) (Math.floor(elapsedTimeSecs / 60.0) % 12);

        if (matchPeriodNo == 1) {
            goalDist = (q1GD) * myDistributionQtr[elapsedMinutesIdx] * periodsPerMinuteRatio;
        } else if (matchPeriodNo == 2) {
            goalDist = (q2GD) * myDistributionQtr[elapsedMinutesIdx] * periodsPerMinuteRatio;
        } else if (matchPeriodNo == 3) {
            goalDist = (q3GD) * myDistributionQtr[elapsedMinutesIdx] * periodsPerMinuteRatio;
        } else if (matchPeriodNo >= 4) {
            goalDist = (q4GD) * myDistributionQtr[elapsedMinutesIdx] * periodsPerMinuteRatio;
        }

        // System.out.println(matchPeriodNo);

        return goalDist;
    }

    static double determine5ExtraMinsGoalDistribution(int matchPeriodNo, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 65 * periodsPerMinute) {
            goalDist = qEGD / (periodsPerMinute * 5);
        } else if (period < 70 * periodsPerMinute) {
            goalDist = qEGD / (periodsPerMinute * 5);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = qEGD / (periodsPerMinute * 5);
        } else {
            goalDist = qEGD / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineShootOutGoalDistribution() {
        return 0.5;
    }

    public static double getPeriodPointsDistribution(BasketballMatchPeriod matchPeriod) {
        double goalDist;

        if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_HALF)) {
            goalDist = q1GD + q2GD;
        } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_HALF)) {
            goalDist = q3GD + q4GD;
        } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FIRST_QUARTER)) {
            goalDist = q1GD;
        } else if (matchPeriod.equals(BasketballMatchPeriod.IN_SECOND_QUARTER)) {
            goalDist = q2GD;
        } else if (matchPeriod.equals(BasketballMatchPeriod.IN_THIRD_QUARTER)) {
            goalDist = q3GD;
        } else if (matchPeriod.equals(BasketballMatchPeriod.IN_FOURTH_QUARTER)) {
            goalDist = q4GD;
        } else {
            goalDist = qEGD;
        }
        return goalDist;
    }

}
