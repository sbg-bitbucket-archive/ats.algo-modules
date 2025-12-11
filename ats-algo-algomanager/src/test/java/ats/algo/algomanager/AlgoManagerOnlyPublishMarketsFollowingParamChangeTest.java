package ats.algo.algomanager;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;

public class AlgoManagerOnlyPublishMarketsFollowingParamChangeTest extends AlgoManagerSimpleTestBase {


    @Test
    public void test() {
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, new TennisMatchFormat());
        assertTrue(this.publishedMarkets == null);
        algoManager.close();
        System.setProperty("onlyPublishMarketsFollowingParamChange", "false");
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 12L, new TennisMatchFormat());
        assertTrue(this.publishedMarkets != null);
    }

    @Test
    @Ignore
    public void testHisorySize() {
        System.setProperty("algo.tennis.undoEventStateHistorySize", "6");
        System.setProperty("algo.badminton.undoEventStateHistorySize", "4");
        algoManager = new AlgoManager(algoManagerConfiguration, this);
        long tennis = 123;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, tennis, new TennisMatchFormat());

        long baddy = 124;
        algoManager.handleNewEventCreation(SupportedSportType.BADMINTON, baddy, new BadmintonMatchFormat());

        System.out.println(algoManager.getEventDetails(tennis).getEventStateHistorySize());
        System.out.println(algoManager.getEventDetails(baddy).getEventStateHistorySize());

        assertTrue(algoManager.getEventDetails(tennis).getEventStateHistorySize() > algoManager.getEventDetails(baddy)
                        .getEventStateHistorySize());


    }


}
