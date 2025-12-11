package ats.algo.manualresulting;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.PublishMarketsManager;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.afl.AflMatchFormat;

public class AflManualResultingTest extends AlgoManagerSimpleTestBase {


    public AflManualResultingTest() {
        super();
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void testAflHandleManualResultMarkets() {
        MethodName.log();
        AflMatchFormat aflMatchFormat = new AflMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.AUSSIE_RULES, 11L, aflMatchFormat);
        Gaussian g = publishedMatchParams.getParamMap().get("totalScoreRate").getGaussian();
        g.setMean(g.getMean() + 0.0000001);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());

        assertTrue(publishedMarkets.size() > 0);
        MatchResultMap matchResultMap = aflMatchFormat.generateMatchResultProForma();
        matchResultMap.getMap().get("q1Point").setValue("20-16");
        matchResultMap.getMap().get("q1Goal").setValue("3-2");
        matchResultMap.getMap().get("q2Point").setValue("10-10");
        matchResultMap.getMap().get("q2Goal").setValue("1-1");
        matchResultMap.getMap().get("q3Point").setValue("12-14");
        matchResultMap.getMap().get("q3Goal").setValue("1-2");
        matchResultMap.getMap().get("q4Point").setValue("18-6");
        matchResultMap.getMap().get("q4Goal").setValue("3-0");
        // matchResultMap.getMap().get("qExtraTimePoint").setValue("0-0");
        // matchResultMap.getMap().get("qExtratimeGoal").setValue("0-0");
        matchResultMap.setEventId(11L);
        int nMarkets = publishedMarkets.size();
        publishedMarkets = null;
        keysForDiscontinuedMarkets = null;
        algoManager.handleManualResultMarkets(matchResultMap);
        assertEquals(0, publishedMarkets.size());
        assertEquals(nMarkets, keysForDiscontinuedMarkets.size());
        assertTrue(this.publishedNotifyEventCompleted);
        // System.out.println(publishedResultedMarkets);
        /*
         * test a few specific marktes have been resulted as expected
         */
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertEquals("Team A", resultedMarket.getWinningSelections().get(0));

        Set<String> exceptionsSet = new HashSet<String>(1);
        /*
         * can't result the first to 5 games market, so add to the exceptions list, otherwise will get a fail on the
         * unit test
         */
        exceptionsSet.add("FT:PW5G_S1");
        // verifyAllMarketsResulted(marketKeys, publishedResultedMarkets, exceptionsSet);
    }



}
