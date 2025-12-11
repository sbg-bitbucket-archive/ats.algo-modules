package ats.algo.manualresulting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.PublishMarketsManager;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.FootballMatchPeriod;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisSimpleMatchState;

public class FootballManualResultingTest extends AlgoManagerSimpleTestBase {


    public FootballManualResultingTest() {
        super();
        PublishMarketsManager.publishAllMarkets = true;

    }

    @Test
    public void testFootballSMStoResultedMarketsMap() {
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        FootballSimpleMatchState footballSimpleMatchState =
                        new FootballSimpleMatchState(false, true, false, FootballMatchPeriod.MATCH_COMPLETED, 5400, 2,
                                        1, 3, 5, -1, 0, -1, 0, 1, 0, 1, 1, null, null, null, true);

        MatchResultMap map = footballSimpleMatchState.generateResultMapFromSimpleMatchState(matchFormat);
        System.out.println(map);

        FootballSimpleMatchState matchState = new FootballSimpleMatchState();
        FootballSimpleMatchState sms = matchState.generateMatchStateFromMatchResultMap(map, matchFormat);
        System.out.println(sms);



    }

    @Test
    public void testTennisSMStoResultedMarketsMap() {
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        Map<String, PairOfIntegers> setScore = new HashMap<String, PairOfIntegers>();
        PairOfIntegers s2 = new PairOfIntegers();
        s2.A = 3;
        s2.B = 6;
        setScore.put("scoreInSet2", s2);
        PairOfIntegers s1 = new PairOfIntegers();
        s1.A = 2;
        s1.B = 6;
        setScore.put("scoreInSet1", s1);

        TennisSimpleMatchState tSMS = new TennisSimpleMatchState(false, true, 0, 2, 0, 0, 0, 0, TeamId.UNKNOWN, 2,
                        false, false, setScore);

        MatchResultMap map = tSMS.generateResultMapFromSimpleMatchState(matchFormat);
        System.out.println(map);

        TennisSimpleMatchState matchState = new TennisSimpleMatchState();
        TennisSimpleMatchState sms = matchState.generateMatchStateFromMatchResultMap(map, matchFormat);
        System.out.println(sms);



    }

    @Test
    public void testFootballHandleManualResultMarkets() {
        algoManager.usingParamFindTradingRules(false);
        FootballMatchFormat footballMatchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, 11L, footballMatchFormat);
        FootballMatchParams footballMatchParams = (FootballMatchParams) publishedMatchParams;
        footballMatchParams = new FootballMatchParams();
        footballMatchParams.setGoalTotal(4.5, 0.1);
        footballMatchParams.setGoalSupremacy(1.0, 0.1);
        footballMatchParams.setEventId(11L);
        algoManager.handleSetMatchParams(footballMatchParams.generateGenericMatchParams());
        System.out.println(publishedMarkets);

        Set<String> marketKeys = publishedMarkets.getMarketKeys();

        assertTrue(publishedMarkets.size() > 0);
        MatchResultMap matchResultMap = footballMatchFormat.generateMatchResultProForma();
        matchResultMap.getMap().get("firstHalfGoals").setValue("1-0");
        matchResultMap.getMap().get("secondHalfGoals").setValue("2-1");
        matchResultMap.setEventId(11L);
        int nMarkets = publishedMarkets.size();
        publishedMarkets = null;
        keysForDiscontinuedMarkets = null;
        algoManager.handleManualResultMarkets(matchResultMap);
        System.out.println(publishedMarkets);
        assertEquals(0, publishedMarkets.size());
        assertEquals(nMarkets, keysForDiscontinuedMarkets.size());
        assertTrue(this.publishedNotifyEventCompleted);
        System.out.println(publishedResultedMarkets);
        System.out.println();
        /*
         * test a few specific marktes have been resulted as expected
         */
        ResultedMarket resultedMarket = publishedResultedMarkets.getResultedMarkets().get("FT:AXB_M");
        assertEquals("A", resultedMarket.getWinningSelections().get(0));
        ResultedMarket resultedMarket2 = publishedResultedMarkets.getResultedMarkets().get("FT:OU#3.5_M");
        System.out.print("---" + resultedMarket2 + "--" + publishedResultedMarkets);
        if (resultedMarket2 != null) {
            assertEquals("Over", resultedMarket2.getWinningSelections().get(0));
            assertEquals(4, resultedMarket2.getActualOutcome());
        }
        Set<String> exceptionsSet = new HashSet<String>(1);
        /*
         * can't result the first to 5 games market, so add to the exceptions list, otherwise will get a fail on the
         * unit test
         */
        exceptionsSet.add("FT:AGS_M");
        exceptionsSet.add("FT:60MR_F0");
        exceptionsSet.add("FT:10MC#0.5_F0");
        exceptionsSet.add("FT:30MG_M");
        exceptionsSet.add("FT:20MG_M");
        exceptionsSet.add("FT:5MC#0.5_F0");
        exceptionsSet.add("FT:10MC#1.5_F0");
        exceptionsSet.add("FT:15MC#1.5_F0");
        exceptionsSet.add("FT:5MR_F0");
        exceptionsSet.add("FT:5MG_F0");
        exceptionsSet.add("FT:5MB_F0");
        exceptionsSet.add("FT:15MB_F0");
        exceptionsSet.add("FT:30MR_F0");
        exceptionsSet.add("FT:15MR_F0");
        exceptionsSet.add("FT:75MR_F0");
        exceptionsSet.add("FT:15MG_F0");
        exceptionsSet.add("FT:10MB_F0");
        exceptionsSet.add("G:NS_G1");
        exceptionsSet.add("FT:10MG_F0");
        exceptionsSet.add("FT:10MR_F0");
        exceptionsSet.add("FT:BCORNER_M");
        exceptionsSet.add("P:CS_P2");
        exceptionsSet.add("P:CCS_P1");
        exceptionsSet.add("FT:BOU#40.5_M");
        exceptionsSet.add("FT:CCS_M");
        exceptionsSet.add("P:BTS_P2");
        exceptionsSet.add("P:DBLC$BTS_P2");
        exceptionsSet.add("FT:WNTN:H_M");
        exceptionsSet.add("FT:DBLC$3HCP#3.0_M");
        System.out.println();
        ManualResultingTest.verifyAllMarketsResulted(marketKeys, publishedResultedMarkets, exceptionsSet);
    }



}
