package ats.algo.sport.americanfootball;

import ats.algo.sport.americanfootball.AmericanfootballMatchPeriod;
import ats.algo.sport.americanfootball.AmericanfootballMatchState.AmericanfootballMatchStatusType;

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
                    AmericanfootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    AmericanfootballMatchStatusType rugbyUnionMatchStatus, int injuryTimeSecs, boolean TD) {

        // if (rugbyUnionMatchStatus == AmericanfootballMatchStatusType.CONVERSION) {
        // return determineShootOutGoalDistribution();
        // } else {

        if (matchPeriod == AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES
                        || matchPeriod == AmericanfootballMatchPeriod.AT_EXTRA_TIME_END)
            return determineExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs);
        if (matchLengthIs80Mins)
            if (TD)
                return determine60MinsTouchDownDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
            else
                return determine60MinsFieldGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else
            throw new IllegalArgumentException("No goal distribution for the selected length of match");
        // }
    }

    private static double determine60MinsFieldGoalDistribution(AmericanfootballMatchPeriod matchPeriod,
                    int elapsedTimeSecs, int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = (0.079 + 0.005 + 0.003) / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = (0.079 + 0.005 + 0.003) / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = (0.079 + 0.005 + 0.003) / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = (0.10 - 0.005) / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = (0.10 - 0.005) / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = (0.10 - 0.005) / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = (0.061 - 0.015 - 0.003) / (periodsPerMinute * 5);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = (0.061 - 0.015 - 0.003) / (periodsPerMinute * 5);
        } else if (period < 45 * periodsPerMinute) {
            goalDist = (0.061 - 0.015 - 0.003) / (periodsPerMinute * 5);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = (0.093 + 0.015) / (periodsPerMinute * 5);
        } else if (period < 55 * periodsPerMinute) {
            goalDist = (0.093 + 0.015) / (periodsPerMinute * 5);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = (0.093 + 0.015) / (periodsPerMinute * 5);
        } else {
            goalDist = 0.09 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    private static double determine60MinsTouchDownDistribution(AmericanfootballMatchPeriod matchPeriod,
                    int elapsedTimeSecs, int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = (0.066 - 0.00168 + 0.002) / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = (0.066 - 0.00168 + 0.002) / (periodsPerMinute * 5);
        } else if (period < 15 * periodsPerMinute) {
            goalDist = (0.066 - 0.00168 + 0.002) / (periodsPerMinute * 5);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = (0.097 - 0.00168) / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = (0.097 - 0.00168) / (periodsPerMinute * 5);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = (0.097 - 0.00168) / (periodsPerMinute * 5);
        } else if (period < 35 * periodsPerMinute) {
            goalDist = (0.075 - 0.00168 - 0.002) / (periodsPerMinute * 5);
        } else if (period < 40 * periodsPerMinute) {
            goalDist = (0.075 - 0.00168 - 0.002) / (periodsPerMinute * 5);
        } else if (period < 45 * periodsPerMinute) {
            goalDist = (0.075 - 0.00168 - 0.002) / (periodsPerMinute * 5);
        } else if (period < 50 * periodsPerMinute) {
            goalDist = (0.096 - 0.00168) / (periodsPerMinute * 5);
        } else if (period < 55 * periodsPerMinute) {
            goalDist = (0.096 - 0.00168) / (periodsPerMinute * 5);
        } else if (period < 60 * periodsPerMinute) {
            goalDist = (0.096 - 0.00168) / (periodsPerMinute * 5);
        } else {
            goalDist = 0.096 / (periodsPerMinute * 5);
        }
        return goalDist;
    }

    static double determineExtraMinsGoalDistribution(AmericanfootballMatchPeriod matchPeriod, int elapsedTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 5 * periodsPerMinute) {
            goalDist = 0.0966666667 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.0966666667 / (periodsPerMinute * 5);
        } else if (period <= 15 * periodsPerMinute) {
            goalDist = 0.0966666667 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.0966666667 / (periodsPerMinute * 5);
        }

        return goalDist;
    }

    // static double determineShootOutGoalDistribution() {
    // return 0.99;
    // }
    //
    // public static double getConversionRate() {
    // return 0.5;
    // }

    // public static double getPeriodPointsDistribution(AmericanfootballMatchPeriod matchPeriod) {
    // double goalDist;
    //
    // if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FIRST_QUATER)) {
    // goalDist = 0.20;//0.19996316;
    // } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_SECOND_QUATER)) {
    // goalDist = 0.305;//0.30775; // add here
    // } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_THIRD_QUATER)) {
    // goalDist = 0.205;//0.21 ; // to 21
    // } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_FOURTH_QUATER)) {
    // goalDist = 0.29;//0.282285383 ;
    // } else if (matchPeriod.equals(AmericanfootballMatchPeriod.IN_EXTRA_TIME_15MINUTES)){
    // goalDist = 0.25 ;
    // }else {
    // goalDist = 0.0;
    // }
    // return goalDist;
    // }

}
