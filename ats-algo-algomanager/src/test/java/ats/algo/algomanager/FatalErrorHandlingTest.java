package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import org.junit.Test;
import static org.junit.Assert.*;

import ats.algo.core.common.SupportedSportType;

import ats.algo.sport.testsport.TestSportMatchEngine;
import ats.algo.sport.testsport.TestSportMatchFormat;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/*
 * tests functionality using the single threaded SimpleAlgoManagerConfiguration
 */
public class FatalErrorHandlingTest extends AlgoManagerSimpleTestBase {

    private static final Logger logger = LoggerFactory.getLogger(FatalErrorHandlingTest.class);

    @Test
    public void test() {
        MethodName.log();
        TestSportMatchEngine.resetTestFlags();
        TestSportMatchEngine.simulateFatalError = true;
        logger.error("*** THIS UNIT TEST GENERATES AN EXPECTED EXCEPTION ***");
        algoManager.handleNewEventCreation(SupportedSportType.TEST_SPORT, 11L, new TestSportMatchFormat());
        if (fatalErrorNotified != null)
            assertTrue(fatalErrorNotified);
        logger.error("*** END OF EXPECTED EXCEPTION ***");
    }
}
