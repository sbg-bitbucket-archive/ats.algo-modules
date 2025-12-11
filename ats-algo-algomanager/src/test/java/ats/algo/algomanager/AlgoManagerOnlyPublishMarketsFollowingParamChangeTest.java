package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;

public class AlgoManagerOnlyPublishMarketsFollowingParamChangeTest extends AlgoManagerSimpleTestBase {


    @Test
    public void test() {
        MethodName.log();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, new TennisMatchFormat());
        assertTrue(this.publishedMarkets == null);
        algoManager.close();
        System.setProperty("onlyPublishMarketsFollowingParamChange", "false");
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 12L, new TennisMatchFormat());
        System.clearProperty("onlyPublishMarketsFollowingParamChange");
        assertTrue(this.publishedMarkets != null);
    }

    @Test
    @Ignore
    public void testHistorySize() {
        MethodName.log();
        System.setProperty("algo.tennis.undoEventStateHistorySize", "6");
        System.setProperty("algo.badminton.undoEventStateHistorySize", "4");
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        long tennis = 123;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, tennis, new TennisMatchFormat());

        long baddy = 124;
        algoManager.handleNewEventCreation(SupportedSportType.BADMINTON, baddy, new BadmintonMatchFormat());

        // System.out.println(algoManager.getEventDetails(tennis).getEventStateHistorySize());
        // System.out.println(algoManager.getEventDetails(baddy).getEventStateHistorySize());
        System.clearProperty("algo.tennis.undoEventStateHistorySize");
        System.clearProperty("algo.badminton.undoEventStateHistorySize");

        assertTrue(algoManager.getEventDetails(tennis).getEventStateHistorySize() > algoManager.getEventDetails(baddy)
                        .getEventStateHistorySize());


    }


}
