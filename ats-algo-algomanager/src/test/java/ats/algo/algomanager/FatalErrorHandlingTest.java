package ats.algo.algomanager;

import org.junit.Test;
import static org.junit.Assert.*;

import ats.algo.core.common.SupportedSportType;

import ats.algo.sport.testsport.TestSportMatchEngine;
import ats.algo.sport.testsport.TestSportMatchFormat;

/*
 * tests functionality using the single threaded SimpleAlgoManagerConfiguration
 */
public class FatalErrorHandlingTest extends AlgoManagerSimpleTestBase {


    @Test
    public void test() {
        TestSportMatchEngine.resetTestFlags();
        TestSportMatchEngine.simulateFatalError = true;
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, 11L, new TestSportMatchFormat());
        assertTrue(fatalErrorNotified);
    }
}
