package ats.algo.tradingruletests;


import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class TriggerParamFindTradingRuleTestSpecialFeedsProvider extends AlgoManagerSimpleTestBase {

    static long eventId = 12345L;

    // to test this function we need to change the football trading rule and set trigger by distance to "false"
    // Also, only PS football engine will require this function for now, check if the incoming prices are from eg.
    // "Alberson"
    @Test
    @Ignore
    public void testInplay() {
        MatchFormat matchFormat = new FootballMatchFormat();
        int sleepSecs = (int) (TriggerParamFindTradingRule.MIN_TIME_BETWEEN_PARAM_FINDS / 1000) + 3;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat, 4);
        publishedMatchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        MarketPricesList marketPricesList = getTestMarketPricesTestAlberson(1.7, 2.2);
        MarketPricesList marketPricesListTX = getTestMarketPricesTestTX(1.91, 1.77);

        MarketPricesList marketPricesListNonValid = getTestMarketPricesTest2(1.91, 1.77);
        MarketPricesList marketPricesListValid = getTestMarketPricesTestValid(1.91, 1.77);

        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, marketPricesListNonValid);
        Sleep.sleep(2);
        assertFalse(result.isParamFindScheduled());

        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesListValid);
        Sleep.sleep(2);
        assertTrue(result.isParamFindScheduled());

        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesListTX);
        Sleep.sleep(10);
        assertFalse(result.isParamFindScheduled());


        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        Sleep.sleep(sleepSecs);
        assertTrue(result.isParamFindScheduled());
        /*
         * this should not result in a param find because prematch and prices are the same
         */
        /*
         * Goes inplay
         */
        ElapsedTimeMatchIncident incidentStart =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incidentStart.setEventId(eventId);
        incidentStart.setIncidentId("R1");

        Sleep.sleep(sleepSecs);
        algoManager.handleMatchIncident(incidentStart, true);
        Sleep.sleep(sleepSecs);
        MarketPricesList marketPricesList2 = getTestMarketPricesTestAlberson(1.9, 1.9);
        /*
         *  
         */
        TriggerParamFindTradingRulesResult result2 = algoManager.handleSupplyMarketPrices(eventId, marketPricesList2);
        Sleep.sleep(5);
        assertTrue(result2.isParamFindScheduled());
        this.publishedParamFinderResults = null;
        Sleep.sleep(sleepSecs);
        // assertTrue (publishedParamFinderResults != null || algoManager.paramFindInProgress(eventId));

    }


    @Ignore
    @Test
    public void test() {
        MatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat, 4);
        publishedMatchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        MarketPricesList marketPricesList = getTestMarketPricesTestAlberson(1.91, 1.77);
        MarketPricesList marketPricesListTX = getTestMarketPricesTestTX(1.91, 1.77);

        MarketPricesList marketPricesListNonValid = getTestMarketPricesTest2(1.91, 1.77);
        MarketPricesList marketPricesListValid = getTestMarketPricesTestValid(1.91, 1.77);

        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, marketPricesListNonValid);
        Sleep.sleep(2);
        assertFalse(result.isParamFindScheduled());

        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesListValid);
        Sleep.sleep(2);
        assertTrue(result.isParamFindScheduled());

        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesListTX);
        Sleep.sleep(10);
        assertFalse(result.isParamFindScheduled());


        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        Sleep.sleep(10);
        assertTrue(result.isParamFindScheduled());
        /*
         * this should not result in a param find because prematch and prices are the same
         */
        // result = algoManager.handleSupplyMarketPrices(eventId,marketPricesList);
        // assertFalse (result.isParamFindScheduled());
        // /*
        // * Goes inplay
        // * */
        // ElapsedTimeMatchIncident incidentStart =
        // new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        // incidentStart.setEventId(eventId);
        // incidentStart.setIncidentId("R1");
        //
        //
        // algoManager.handleMatchIncident(incidentStart, true);
        //
        // MarketPricesList marketPricesList2 = getTestMarketPricesTestAlberson(12, 1.05);
        // /*
        // * this pf should proceed but only after 15 secs - prices have changed
        // */
        // result = algoManager.handleSupplyMarketPrices(eventId,marketPricesList2);
        // assertTrue (result.isParamFindScheduled());
        // this.publishedParamFinderResults = null;
        // int sleepSecs = (int) (TriggerParamFindTradingRule.MIN_TIME_BETWEEN_PARAM_FINDS/1000) + 3;
        // Sleep.sleep(sleepSecs);
        // assertTrue (publishedParamFinderResults != null || algoManager.paramFindInProgress(eventId));

    }

    private MarketPricesList getTestMarketPricesTestTX(double ftmlA, double ftmlB) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("TX", bet365);
        MarketPrice b1 = new MarketPrice("FT:10MG", "10 mins winner", MarketCategory.GENERAL, null);
        b1.put("A", ftmlA);
        b1.put("B", ftmlB);
        bet365.addMarketPrice(b1);
        // MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "2.5");
        // b2.put("Over", 1.83);
        // b2.put("Under", 1.83);
        // bet365.addMarketPrice(b2);
        // MarketPrice b3 = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "0.5");
        // b3.put("AH", 1.83);
        // b3.put("BH", 1.83);
        // bet365.addMarketPrice(b3);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesTestAlberson(double ftmlA, double ftmlB) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Alberson", bet365);
        MarketPrice b1 = new MarketPrice("G:NS", "First to score", MarketCategory.GENERAL, "G1");
        b1.put("Over", ftmlA);
        b1.put("Under", ftmlB);
        bet365.addMarketPrice(b1);
        // MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "2.5");
        // b2.put("Over", 1.83);
        // b2.put("Under", 1.83);
        // bet365.addMarketPrice(b2);
        // MarketPrice b3 = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "0.5");
        // b3.put("AH", 1.83);
        // b3.put("BH", 1.83);
        // bet365.addMarketPrice(b3);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesTest2(double ftmlA, double ftmlB) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        // MarketPrice b1 = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null);
        // b1.put("A", ftmlA);
        // b1.put("B", ftmlB);
        // bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "0.5");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        bet365.addMarketPrice(b3);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesTestValid(double ftmlA, double ftmlB) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", ftmlA);
        b1.put("B", ftmlB);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.HCAP, "0.5");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        bet365.addMarketPrice(b3);
        return marketPricesList;
    }

}
