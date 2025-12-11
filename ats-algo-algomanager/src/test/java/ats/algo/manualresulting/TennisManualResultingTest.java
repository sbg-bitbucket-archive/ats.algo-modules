package ats.algo.manualresulting;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.PublishMarketsManager;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;

public class TennisManualResultingTest extends AlgoManagerSimpleTestBase {


    public TennisManualResultingTest() {
        super();
        PublishMarketsManager.publishAllMarkets = true;
    }

    @Test
    public void testTennisHandleManualResultMarkets() {
        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat);
        TennisMatchParams tennisMatchParams = (TennisMatchParams) publishedMatchParams;
        tennisMatchParams = new TennisMatchParams();
        tennisMatchParams.setOnServePctA1(0.65, 0.08);
        tennisMatchParams.setOnServePctB1(0.58, 0.05);
        tennisMatchParams.setEventId(11L);
        algoManager.handleSetMatchParams(tennisMatchParams.generateGenericMatchParams());
        System.out.println(publishedMarkets);

        Set<String> marketKeys = publishedMarkets.getMarketKeys();

        assertTrue(publishedMarkets.size() > 0);
        MatchResultMap matchResultMap = tennisMatchFormat.generateMatchResultProForma();
        matchResultMap.getMap().get("set1Score").setValue("7-6");
        matchResultMap.getMap().get("set2Score").setValue("3-6");
        matchResultMap.getMap().get("set3Score").setValue("5-7");
        matchResultMap.setEventId(11L);
        int nMarkets = publishedMarkets.size();
        publishedMarkets = null;
        keysForDiscontinuedMarkets = null;
        algoManager.handleManualResultMarkets(matchResultMap);
        assertEquals(0, publishedMarkets.size());
        assertEquals(nMarkets, keysForDiscontinuedMarkets.size());
        assertTrue(this.publishedNotifyEventCompleted);
        System.out.println(publishedResultedMarkets);
        /*
         * test a few specific markets have been resulted as expected
         */
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:ML_M");
        assertEquals("B", resultedMarket.getWinningSelections().get(0));
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:OU#23.5_M");
        assertEquals("Over", resultedMarket2.getWinningSelections().get(0));
        assertEquals(34, resultedMarket2.getActualOutcome());
        Set<String> exceptionsSet = new HashSet<String>(1);
        /*
         * can't result the first to 5 games market, so add to the exceptions list, otherwise will get a fail on the
         * unit test
         */
        exceptionsSet.add("FT:PW5G_S1");
        exceptionsSet.add("FT:BF_M");
        System.out.println();
        ManualResultingTest.verifyAllMarketsResulted(marketKeys, publishedResultedMarkets, exceptionsSet);
    }



}
