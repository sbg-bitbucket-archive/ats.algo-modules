package ats.algo.sport.football;

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

    static double getGoalDistribution(boolean matchLengthIs80Mins, FootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs, double totalG, double supmG, boolean v3tickdown) {
        if (matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
                        || matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME)
            return determineExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        if (matchLengthIs80Mins)
            return determine80MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        else if (!v3tickdown) {
            return determine90MinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        } else {
            // return determinGoalDistributionV3TickDown(totalG, supmG, matchLengthIs80Mins, matchPeriod,
            // elapsedTimeSecs);
            throw new IllegalArgumentException("v3 method moved");
        }
    }

    private static double determine90MinsGoalDistribution(FootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 15 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 15);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.14 / (periodsPerMinute * 15);
        } else if (matchPeriod == FootballMatchPeriod.IN_FIRST_HALF) {
            goalDist = 0.17 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.15 / (periodsPerMinute * 15);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.19 / (periodsPerMinute * 15);
        } else {
            goalDist = 0.24 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        }
        return goalDist;
    }

    private static double determine80MinsGoalDistribution(FootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 10 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 10);
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.10 / (periodsPerMinute * 10);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 10);
        } else if (matchPeriod == FootballMatchPeriod.IN_FIRST_HALF) {
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
    // <2 0-1.5Sup + 2-3.25goals 0-1.5 Sup + 3.25+goals 1.5+Sup 2-3.25goals 1.5+ Sup + 3.25+ goals >4

    static double[] v3GoalDist = {1, 0.001116667, 0.001383333, 0.0014, 0.001466667, 0.001483333, 0.0015, 2, 0.0011,
            0.0014, 0.0014, 0.001466667, 0.001466667, 0.0015, 3, 0.001116667, 0.001383333, 0.0014, 0.001466667,
            0.001483333, 0.0015, 4, 0.001116667, 0.001383333, 0.0014, 0.001466667, 0.001483333, 0.0015, 5, 0.0011,
            0.0014, 0.0014, 0.001466667, 0.001466667, 0.0015, 6, 0.001116667, 0.001383333, 0.0014, 0.001466667,
            0.001483333, 0.0015, 7, 0.001116667, 0.001383333, 0.0014, 0.001466667, 0.001483333, 0.0015, 8, 0.0011,
            0.0014, 0.0014, 0.001466667, 0.001466667, 0.0015, 9, 0.001116667, 0.001383333, 0.0014, 0.001466667,
            0.001483333, 0.0015, 10, 0.001116667, 0.001383333, 0.0014, 0.001466667, 0.001483333, 0.0015, 11, 0.0011,
            0.0014, 0.0014, 0.001466667, 0.001466667, 0.0015, 12, 0.001116667, 0.001383333, 0.0014, 0.001466667,
            0.001483333, 0.0015, 13, 0.001116667, 0.001383333, 0.001416667, 0.001466667, 0.001483333, 0.0015, 14,
            0.0011, 0.0014, 0.0014, 0.001466667, 0.001466667, 0.0015, 15, 0.001116667, 0.001383333, 0.0014, 0.001466667,
            0.001483333, 0.0015, 16, 0.001783333, 0.001666667, 0.0017, 0.0017, 0.001716667, 0.00175, 17, 0.001766667,
            0.001666667, 0.001716667, 0.0017, 0.0017, 0.001733333, 18, 0.001783333, 0.001666667, 0.0017, 0.0017,
            0.001716667, 0.00175, 19, 0.001783333, 0.001666667, 0.001716667, 0.0017, 0.001716667, 0.00175, 20,
            0.001766667, 0.001666667, 0.001716667, 0.0017, 0.0017, 0.001733333, 21, 0.001783333, 0.001666667, 0.0017,
            0.0017, 0.001716667, 0.00175, 22, 0.001783333, 0.001666667, 0.001716667, 0.0017, 0.001716667, 0.00175, 23,
            0.001766667, 0.001666667, 0.001716667, 0.0017, 0.0017, 0.001733333, 24, 0.001783333, 0.001666667, 0.0017,
            0.0017, 0.001716667, 0.00175, 25, 0.001783333, 0.001666667, 0.001716667, 0.0017, 0.001716667, 0.00175, 26,
            0.001766667, 0.001666667, 0.0017, 0.0017, 0.0017, 0.001733333, 27, 0.001783333, 0.001666667, 0.001716667,
            0.0017, 0.001716667, 0.00175, 28, 0.001783333, 0.001666667, 0.001716667, 0.0017, 0.001716667, 0.00175, 29,
            0.001766667, 0.001666667, 0.0017, 0.0017, 0.0017, 0.001733333, 30, 0.001783333, 0.001666667, 0.001716667,
            0.0017, 0.001716667, 0.00175, 31, 0.001783333, 0.001883333, 0.001916667, 0.00185, 0.00195, 0.00195, 32,
            0.001766667, 0.0019, 0.0019, 0.001866667, 0.001933333, 0.001933333, 33, 0.001783333, 0.001883333,
            0.001916667, 0.00185, 0.00195, 0.00195, 34, 0.001783333, 0.001883333, 0.001916667, 0.00185, 0.00195,
            0.00195, 35, 0.001766667, 0.0019, 0.0019, 0.001866667, 0.001933333, 0.001933333, 36, 0.001783333,
            0.001883333, 0.001916667, 0.00185, 0.00195, 0.00195, 37, 0.001783333, 0.001883333, 0.001916667, 0.00185,
            0.00195, 0.00195, 38, 0.001766667, 0.0019, 0.0019, 0.001866667, 0.001933333, 0.001933333, 39, 0.001783333,
            0.001883333, 0.001916667, 0.00185, 0.00195, 0.00195, 40, 0.001783333, 0.001883333, 0.001916667, 0.00185,
            0.00195, 0.00195, 41, 0.001766667, 0.0019, 0.0019, 0.001866667, 0.001933333, 0.001933333, 42, 0.001783333,
            0.001883333, 0.001916667, 0.00185, 0.00195, 0.00195, 43, 0.001783333, 0.001883333, 0.001916667, 0.00185,
            0.00195, 0.00195, 44, 0.001766667, 0.0019, 0.0019, 0.001866667, 0.001933333, 0.001933333, 45, 0.001783333,
            0.001883333, 0.001916667, 0.00185, 0.00195, 0.00195, 46, 0.001783333, 0.001866667, 0.001816667, 0.001816667,
            0.001783333, 0.0018, 47, 0.001766667, 0.001866667, 0.0018, 0.0018, 0.0018, 0.0018, 48, 0.001783333,
            0.001866667, 0.001816667, 0.001816667, 0.001783333, 0.0018, 49, 0.001783333, 0.001866667, 0.001816667,
            0.001816667, 0.001783333, 0.0018, 50, 0.001766667, 0.001866667, 0.0018, 0.0018, 0.0018, 0.0018, 51,
            0.001783333, 0.001866667, 0.001816667, 0.001816667, 0.001783333, 0.0018, 52, 0.001783333, 0.001866667,
            0.001816667, 0.001816667, 0.001783333, 0.0018, 53, 0.001766667, 0.001866667, 0.0018, 0.0018, 0.0018, 0.0018,
            54, 0.001783333, 0.001866667, 0.001816667, 0.001816667, 0.001783333, 0.0018, 55, 0.001783333, 0.001866667,
            0.001816667, 0.001816667, 0.001783333, 0.0018, 56, 0.001766667, 0.001866667, 0.0018, 0.0018, 0.0018, 0.0018,
            57, 0.001783333, 0.001866667, 0.001816667, 0.001816667, 0.001783333, 0.0018, 58, 0.001783333, 0.001866667,
            0.001816667, 0.001816667, 0.001783333, 0.0018, 59, 0.001766667, 0.001866667, 0.0018, 0.0018, 0.0018, 0.0018,
            60, 0.001783333, 0.001866667, 0.001816667, 0.001816667, 0.001783333, 0.0018, 61, 0.002116667, 0.002016667,
            0.002, 0.002, 0.00195, 0.001916667, 62, 0.0021, 0.002033333, 0.002, 0.002, 0.001933333, 0.0019, 63,
            0.002116667, 0.002016667, 0.002, 0.002, 0.00195, 0.001916667, 64, 0.002116667, 0.002016667, 0.002, 0.002,
            0.00195, 0.001916667, 65, 0.0021, 0.002033333, 0.002, 0.002, 0.001933333, 0.0019, 66, 0.002116667,
            0.002016667, 0.002, 0.002, 0.00195, 0.001916667, 67, 0.002116667, 0.002016667, 0.002, 0.002, 0.00195,
            0.001916667, 68, 0.0021, 0.002033333, 0.002, 0.002, 0.001933333, 0.0019, 69, 0.002116667, 0.002016667,
            0.002, 0.002, 0.00195, 0.001916667, 70, 0.002116667, 0.002016667, 0.002, 0.002, 0.00195, 0.001916667, 71,
            0.0021, 0.002033333, 0.002, 0.002, 0.001933333, 0.0019, 72, 0.002116667, 0.002016667, 0.002, 0.002, 0.00195,
            0.001916667, 73, 0.002116667, 0.002016667, 0.002, 0.002, 0.00195, 0.001916667, 74, 0.0021, 0.002033333,
            0.002, 0.002, 0.001933333, 0.0019, 75, 0.002116667, 0.002016667, 0.002, 0.002, 0.00195, 0.001916667, 76,
            0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.00185, 77, 0.002133333, 0.0019, 0.0019, 0.0019,
            0.001866667, 0.001833333, 78, 0.002116667, 0.0019, 0.0019, 0.0019, 0.001883333, 0.00185, 79, 0.002133333,
            0.0019, 0.0019, 0.0019, 0.001866667, 0.001833333, 80, 0.002133333, 0.001883333, 0.001883333, 0.001883333,
            0.001866667, 0.00185, 81, 0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.001833333, 82, 0.002116667,
            0.0019, 0.0019, 0.0019, 0.001883333, 0.00185, 83, 0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667,
            0.001833333, 84, 0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.00185, 85, 0.002133333, 0.0019, 0.0019,
            0.0019, 0.001866667, 0.00185, 86, 0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.001833333, 87,
            0.002116667, 0.0019, 0.0019, 0.0019, 0.001883333, 0.00185, 88, 0.002133333, 0.0019, 0.0019, 0.0019,
            0.001866667, 0.001833333, 89, 0.002133333, 0.001883333, 0.001883333, 0.001883333, 0.001866667, 0.00185, 90,
            0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.001833333, 91, 0.002116667, 0.0019, 0.0019, 0.0019,
            0.001883333, 0.00185, 92, 0.002133333, 0.0019, 0.0019, 0.0019, 0.001866667, 0.001833333, 93, 0.002133333,
            0.0019, 0.0019, 0.0019, 0.001866667, 0.00185, 94, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

    static double determineExtraMinsGoalDistribution(FootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;
        if (period < 5 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 5);
        } else if (period < 10 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 5);
        } else if (matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF) {
            goalDist = 0.09 / (periodsPerMinute * (5 + injuryTimeSecs / 60));
        } else if (period < 20 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 5);
        } else if (period < 25 * periodsPerMinute) {
            goalDist = 0.09 / (periodsPerMinute * 5);
        } else {
            goalDist = 0.09 / (periodsPerMinute * (5 + injuryTimeSecs / 60));
        }
        return goalDist;
    }

    public static double getCornerDistribution(boolean is80MinMatch, FootballMatchPeriod matchPeriod,
                    int elapsedTimeSecs, int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 15 * periodsPerMinute) {
            goalDist = 0.166666667 / (periodsPerMinute * 15);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.166666667 / (periodsPerMinute * 15);
        } else if (matchPeriod == FootballMatchPeriod.IN_FIRST_HALF) {
            goalDist = 0.166666667 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.166666667 / (periodsPerMinute * 15);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.166666667 / (periodsPerMinute * 15);
        } else {
            goalDist = 0.166666667 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        }
        return goalDist;
    }

    public static double determinGoalDistributionV3TickDown(int col, FootballMatchPeriod matchPeriod,
                    int elapsedTimeSecs) {
        // if (is80MinMatch)
        // throw new IllegalArgumentException("Currently not supported 80 minutes match");

        // int row = Math.floorDiv(elapsedTimeSecs, 60);
        int row = (elapsedTimeSecs / 60);
        // int col = 0;
        // if (totalG < 2) {
        // col = 1;
        // } else if (totalG < 3.25) {
        // if (supmG <= 1.5) {
        // col = 2;
        // } else {
        // col = 4;
        // }
        // } else if (totalG > 4) {
        // col = 6;
        //
        // } else { // total goal <4 >3.25
        // if (supmG <= 1.5) {
        // col = 3;
        // } else {
        // col = 5;
        // }
        // }

        if (row > 45 && matchPeriod.equals(FootballMatchPeriod.IN_FIRST_HALF)) {
            row = 45;
        } else if (row > 92 && matchPeriod.equals(FootballMatchPeriod.IN_SECOND_HALF)) {
            row = 92;
        } else if (row > 93 && matchPeriod.equals(FootballMatchPeriod.AT_FULL_TIME)) { // not necessary
            row = 93;
        }

        // if (v3GoalDist[col + (row) * 7] > 0.5) {
        // throw new IllegalArgumentException("Error in the goal distribution v3 tick down use");
        // }

        return v3GoalDist[col + (row) * 7];
    }

    public static double getCardDistribution(boolean is80MinMatch, FootballMatchPeriod matchPeriod, int elapsedTimeSecs,
                    int injuryTimeSecs) {
        double goalDist;
        int period = elapsedTimeSecs / timeIncrementSecs;

        if (period < 15 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 15);
        } else if (period < 30 * periodsPerMinute) {
            goalDist = 0.11 / (periodsPerMinute * 15);
        } else if (matchPeriod == FootballMatchPeriod.IN_FIRST_HALF) {
            goalDist = 0.11 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        } else if (period < 60 * periodsPerMinute) {
            goalDist = 0.15 / (periodsPerMinute * 15);
        } else if (period < 75 * periodsPerMinute) {
            goalDist = 0.22 / (periodsPerMinute * 15);
        } else {
            goalDist = 0.3 / (periodsPerMinute * (15 + injuryTimeSecs / 60));
        }
        return goalDist;
    }

    public static double getGoalDistribution(FootballMatchPeriod matchPeriod, int elapsedTimeSecs, int injuryTimeSecs,
                    int v3ColIndex, boolean b) {
        if (matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_FIRST_HALF
                        || matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_SECOND_HALF
                        || matchPeriod == FootballMatchPeriod.IN_EXTRA_TIME_HALF_TIME) {
            return determineExtraMinsGoalDistribution(matchPeriod, elapsedTimeSecs, injuryTimeSecs);
        } else if (matchPeriod == FootballMatchPeriod.MATCH_COMPLETED) {
            return 0;
        } else if (matchPeriod == FootballMatchPeriod.IN_SHOOTOUT) {
            return 0.5;
        } else {

            return determinGoalDistributionV3TickDown(v3ColIndex, matchPeriod, elapsedTimeSecs);
        }
    }

    public static double getCardDistributionShootOut(int counter) {
        if (counter == 0)
            return 0.866;
        else if (counter == 1)
            return 0.817;
        else if (counter == 2)
            return 0.793;
        else if (counter == 3)
            return 0.725;
        else if (counter == 4)
            return 0.80;
        else if (counter == 5)
            return 0.643;
        else // sunnden death
            return 0.643;
    }

}
