package ats.algo.manualresulting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.core.util.json.JsonUtil;

public class ManualResultingTest {


    public ManualResultingTest() {

    }

    @Test
    public void testManualResultingProformas() {
        MatchFormat matchFormat1 = new TennisMatchFormat();
        MatchResultMap matchResultMap1 = matchFormat1.generateMatchResultProForma();
        assertTrue(matchResultMap1.getMap().get("set3Score") != null);
        assertTrue(matchResultMap1.getMap().get("set5Score") == null);
        String json = JsonUtil.marshalJson(matchResultMap1, true);
        System.out.print(matchResultMap1.toString());
        System.out.println(json);
        MatchResultMap matchResultMap11 = JsonUtil.unmarshalJson(json, MatchResultMap.class);
        assertEquals(matchResultMap1, matchResultMap11);
        /*
         * try five set match
         */
        MatchFormat matchFormat2 = new TennisMatchFormat(5, FinalSetType.ADVANTAGE_SET, false, true);
        MatchResultMap matchResultMap2 = matchFormat2.generateMatchResultProForma();
        assertTrue(matchResultMap2.getMap().get("set5Score") != null);
        String json2 = JsonUtil.marshalJson(matchResultMap2, true);
        System.out.print(matchResultMap2.toString());
        System.out.println(json2);
        MatchResultMap matchResultMap22 = JsonUtil.unmarshalJson(json2, MatchResultMap.class);
        assertEquals(matchResultMap2, matchResultMap22);

        /*
         * Try soccer
         */
        MatchFormat matchFormat3 = new FootballMatchFormat();
        MatchResultMap matchResultMap3 = matchFormat3.generateMatchResultProForma();
        assertTrue(matchResultMap3.getMap().get("firstHalfGoals") != null);
        assertTrue(matchResultMap3.getMap().get("extraTimeFirstHalfGoals") == null);
        json = JsonUtil.marshalJson(matchResultMap1, true);
        System.out.print(matchResultMap3.toString());
        System.out.println(json);
        MatchResultMap matchResultMap33 = JsonUtil.unmarshalJson(json, MatchResultMap.class);
        assertEquals(matchResultMap1, matchResultMap33);
        /*
         * try matchFormat without the overrides
         */
        MatchFormat matchFormat4 = new TestMatchFormat(SupportedSportType.AMERICAN_FOOTBALL);
        MatchResultMap matchResultMap4 = matchFormat4.generateMatchResultProForma();
        assertTrue(matchResultMap4.getMap().get("notSupportedForThisSport") != null);
        assertTrue(matchResultMap4.getMap().get("set3Score") == null);
        String json3 = JsonUtil.marshalJson(matchResultMap4, true);
        System.out.print(matchResultMap4.toString());
        System.out.println(json3);
        MatchResultMap matchResultMap44 = JsonUtil.unmarshalJson(json3, MatchResultMap.class);
        assertEquals(matchResultMap4, matchResultMap44);
    }

    /**
     * verify that every market that was published has been resulted
     * 
     * @param marketKeys
     * @param resultedMarkets
     * @param exceptionsSet
     */
    static void verifyAllMarketsResulted(Set<String> marketKeys, ResultedMarkets resultedMarkets,
                    Set<String> exceptionsSet) {
        boolean failTest = false;
        Set<String> missingMarkets = new HashSet<String>(1);
        for (String key : marketKeys) {
            ResultedMarket resultedMarket = resultedMarkets.getResultedMarkets().get(key);
            if (resultedMarket == null) {
                if (!exceptionsSet.contains(key)) {
                    System.out.println("Missing resulted market: " + key);
                    failTest = true;
                    missingMarkets.add(key);
                }
            }
        }
        if (failTest) {
            System.out.println("Here = " + missingMarkets);
            System.out.println("Here = " + missingMarkets);
            System.out.println("Here = " + missingMarkets);
            System.out.println("Here = " + missingMarkets);
            System.out.println("Here = " + missingMarkets);
            System.out.println("Here = " + missingMarkets);
            fail();
        }
    }



    /**
     * test class for use by testMatchResultProforma
     * 
     * @author Geoff
     *
     */
    private class TestMatchFormat extends MatchFormat {
        private static final long serialVersionUID = 1L;

        public TestMatchFormat(SupportedSportType sportType) {
            super(sportType);
        }

        @Override
        public LinkedHashMap<String, String> getAsMap() {
            return null;
        }

        @Override
        public String setFromMap(Map<String, String> map) {
            return null;
        }

        @Override
        public MatchFormatOptions matchFormatOptions() {
            return null;
        }

        @Override
        public void applyFormat(Map<String, Object> jsonObject) {}

        @Override
        public MatchFormat generateDefaultMatchFormat() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
