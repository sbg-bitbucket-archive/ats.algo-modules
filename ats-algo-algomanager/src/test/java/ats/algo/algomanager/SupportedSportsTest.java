package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ats.algo.algomanager.SupportedSports;
import ats.algo.core.common.SupportedSportType;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.sport.football.FootballMatchEngine;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchEngine;
import ats.algo.sport.tennis.TennisMatchFormat;

public class SupportedSportsTest {

    @Before
    public void beforeEachTest() {
        System.setProperty("algo.tennis.engine.class", "ats.algo.sport.football.FootballMatchEngine");
        // System.setProperty("algo.tennis.engine.class",
        // "com.betstars.algo.ats.integration.tennis.BsTennisMatchEngine");
        SupportedSportsInitialisation.init();
    }

    @After
    public void afterEachTest() {
        System.clearProperty("algo.tennis.engine.class");
        SupportedSportsInitialisation.init();
    }

    @Test
    public void test() {
        MethodName.log();
        /*
         * this test replaces the Tennis Model with Football, which is of course something one would only do for testing
         * purposes
         */
        assertEquals("Tennis", SupportedSports.getSportName(SupportedSportType.TENNIS));
        MatchEngine matchEngine;
        matchEngine = SupportedSports.getAmelcoMatchEngine(SupportedSportType.TENNIS, new TennisMatchFormat());
        assertEquals(TennisMatchEngine.class, matchEngine.getClass());
        matchEngine = SupportedSports.getActiveMatchEngine(SupportedSportType.TENNIS, new FootballMatchFormat());
        assertEquals(FootballMatchEngine.class, matchEngine.getClass());
        // assertEquals(BsTennisMatchEngine.class, matchEngine.getClass());

    }
}
