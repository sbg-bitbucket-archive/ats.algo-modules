package ats.algo.loadtester;

import ats.algo.algomanager.SupportedSports;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.sport.tennis.TennisMatchFormat;

public class CheckActiveTennisMatchEngine {

    public static void main(String[] args) {
        MatchFormat matchFormat = new TennisMatchFormat();
        MatchEngine matchEngine = SupportedSports.getActiveMatchEngine(SupportedSportType.TENNIS, matchFormat);
        System.out.printf("Active MatchEngine class for tennis is: %s\n", matchEngine.getClass());
    }

}
