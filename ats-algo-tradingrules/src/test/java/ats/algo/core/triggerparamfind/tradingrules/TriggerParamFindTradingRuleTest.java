package ats.algo.core.triggerparamfind.tradingrules;


import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.tradingrules.PriceSourceWeights;

public class TriggerParamFindTradingRuleTest {


    @Test
    public void testMarketTypesNotInPricesListMethod() {
        MarketPricesList marketPricesList = testMarketPrices();
        Map<String, Integer> marketTypesRequired = new HashMap<String, Integer>();
        marketTypesRequired.put("FT:ML", 1);
        String s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);

        marketTypesRequired.put("FT:ML", 2);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);
        marketTypesRequired.put("FT:ML", 1);
        marketTypesRequired.put("G:ML", 2);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);
        marketTypesRequired.put("G:ML", 3);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);
        marketTypesRequired.put("G:ML", 2);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);
        /*
         * mark one of the prices invalid
         */
        marketPricesList.get("TestSource1").get("G:ML", "S1.2").setValid(false);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);
        System.out.println(s);
    }

    private MarketPricesList testMarketPrices() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);
        m.addMarketPrice(testMktPrice("FT:ML", "M"));
        m.addMarketPrice(testMktPrice("G:ML", "S1.2"));

        marketPricesList.put("TestSource1", m);
        MarketPrices m2 = new MarketPrices();
        m2.setSourceWeight(1);
        m2.addMarketPrice(testMktPrice("G:ML", "S1.2"));
        m2.addMarketPrice(testMktPrice("G:ML", "S1.3"));
        marketPricesList.put("TestSource2", m2);
        return marketPricesList;
    }

    private MarketPrice testMktPrice(String type, String seqId) {
        MarketPrice p = new MarketPrice(type, "test", MarketCategory.GENERAL, null, seqId);
        p.put("A", 1.86);
        p.put("B", 1.88);
        return p;
    }

    @Test
    public void testMarketWeights() {
        /*
         * this test uses same data as ComparePricesTest.test()
         */
        EventSettings eventSettings = new EventSettings();
        TriggerParamFindTradingRule rule = new TriggerParamFindTradingRule("Test rule", null);
        TriggerParamFindData triggerParamFindData = new TriggerParamFindData();
        MarketPricesList marketPricesList = testList1();
        triggerParamFindData.getMarketPricesCache().addPricesToCache(marketPricesList, 100L);
        TestMatchState matchState = new TestMatchState();
        MarketPricesList marketPricesList2 = rule.shouldRequestParamFind(matchState, testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 != null);
        assertEquals(.151, rule.pricesDistance, 0.003);
        /*
         * add second price to marketPricesList
         */
        MarketPrice p2 = new MarketPrice("G:ML", "Total games", MarketCategory.GENERAL, null);
        p2.setSequenceId("S1.2");
        p2.put("A", 1.82);
        p2.put("B", 1.92);
        marketPricesList.get("Pinnacle").addMarketPrice(p2);
        triggerParamFindData.getMarketPricesCache().addPricesToCache(marketPricesList, 200L);
        rule.addMarketWeight("G:ML", 0);
        marketPricesList2 = rule.shouldRequestParamFind(matchState, testMarkets1(), false, triggerParamFindData,
                        eventSettings, false);
        assertTrue(marketPricesList2 != null);
        assertEquals(.151, rule.pricesDistance, 0.003);
        rule.addMarketWeight("FT:ML", 1);
        rule.addMarketWeight("G:ML", 1);
        triggerParamFindData.getMarketPricesCache().addPricesToCache(marketPricesList, 300L);
        triggerParamFindData.setLastUsedMarketPricesList(new MarketPricesList());
        marketPricesList2 = rule.shouldRequestParamFind(matchState, testMarkets1(), false, triggerParamFindData,
                        eventSettings, false);
        assertEquals(.115, rule.pricesDistance, 0.003);
        assertTrue(marketPricesList2 != null);
    }

    @Test
    public void testSourceAndMarketWeights() {
        /*
         * this test uses same data as ComparePricesTest.test3()
         */
        EventSettings eventSettings = new EventSettings();
        TriggerParamFindTradingRule rule = new TriggerParamFindTradingRule("Test rule", null);
        TriggerParamFindData triggerParamFindData = new TriggerParamFindData();
        MarketPricesList marketPricesList = testList2();
        triggerParamFindData.getMarketPricesCache().addPricesToCache(marketPricesList, 100L);
        rule.addMarketWeight("FT:ML", 2);
        rule.addMarketWeight("G:ML", 3);
        PriceSourceWeights sourceWeights = triggerParamFindData.getPriceSourceWeights();
        sourceWeights.setPriceSourceWeight("Pinnacle", 5);
        sourceWeights.setPriceSourceWeight("B365", 7);

        TestMatchState matchState = new TestMatchState();
        MarketPricesList marketPricesList2 = rule.shouldRequestParamFind(matchState, testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertEquals(.106, rule.pricesDistance, 0.001);
        assertTrue(marketPricesList2 != null);
        assertEquals(7.0, marketPricesList2.get("B365").getSourceWeight(), 0.0001);
        assertEquals(2.0, marketPricesList2.get("Pinnacle").get("FT:ML", "M").getMarketWeight(), 0.0001);;
    }

    @Test
    public void testRequiredMarkets() {
        EventSettings eventSettings = new EventSettings();
        TriggerParamFindTradingRule rule = new TriggerParamFindTradingRule("Test rule", null);
        TriggerParamFindData triggerParamFindData = new TriggerParamFindData();
        MarketPricesList marketPricesList = testList2();
        triggerParamFindData.getMarketPricesCache().addPricesToCache(marketPricesList, 100L);
        MarketPricesList marketPricesList2 = rule.shouldRequestParamFind(new TestMatchState(), testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 != null);

        triggerParamFindData.setLastUsedMarketPricesList(new MarketPricesList());
        rule.addMarketTypeRequiredPreMatch("FT:ML", 2);
        marketPricesList2 = rule.shouldRequestParamFind(new TestMatchState(), testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 != null);

        triggerParamFindData.setLastUsedMarketPricesList(new MarketPricesList());
        rule.addMarketTypeRequiredPreMatch("X:YZ", 1);
        marketPricesList2 = rule.shouldRequestParamFind(new TestMatchState(), testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 == null);


        triggerParamFindData.setLastUsedMarketPricesList(new MarketPricesList());
        rule.clearMarketTypesRequiredPreMatch();
        rule.addMarketTypeRequiredPreMatch("G:ML", 1);
        marketPricesList2 = rule.shouldRequestParamFind(new TestMatchState(), testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 != null);

        triggerParamFindData.setLastUsedMarketPricesList(new MarketPricesList());
        rule.addMarketTypeRequiredPreMatch("G:ML", 2);
        marketPricesList2 = rule.shouldRequestParamFind(new TestMatchState(), testMarkets1(), false,
                        triggerParamFindData, eventSettings, false);
        assertTrue(marketPricesList2 == null);
    }

    @Test
    public void testMarketTypesNotInPricesList() {
        MarketPricesList marketPricesList = testList2();
        Map<String, Integer> marketTypesRequired = new HashMap<String, Integer>();

        marketTypesRequired.put("FT:ML", 1);
        String s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);

        marketTypesRequired.put("FT:ML", 3);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);
        marketTypesRequired = new HashMap<String, Integer>();
        marketTypesRequired.put("XYZ", 1);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);
    }

    @Test
    public void testMarketTypesNotInPricesListWithMultiplePossibleTypes() {
        MarketPricesList marketPricesList = testList2();

        Map<String, Integer> marketTypesRequired = new HashMap<>();
        // FT:ML is present, FT:AHCP isn't
        marketTypesRequired.put("FT:ML,FT:AHCP", 1);
        String s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);

        marketTypesRequired.clear();
        // no markets present
        marketTypesRequired.put("FT:AXB,FT:AHCP", 1);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s != null);

        marketTypesRequired.clear();
        // both markets present
        marketTypesRequired.put("FT:ML,G:ML", 1);
        s = TriggerParamFindTradingRule.marketTypesNotInPricesList(marketTypesRequired, marketPricesList);
        assertTrue(s == null);
    }

    private Markets testMarkets1() {
        Market market = generateFTMLMarket();
        Market market2 = generateGMLMarket();
        Markets markets = new Markets();
        markets.addMarketWithShortKey(market);
        markets.addMarketWithShortKey(market2);
        return markets;
    }

    /**
     * Generates an example handicap market. All properties are set correctly except for the probabilities which are set
     * to fixed values in this example code. The probabilities are set up for a balanced line of -0.5
     */

    private static Market generateFTMLMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match winner");
        market.setIsValid(true);
        market.put("A", 0.6);
        market.put("B", 0.4);
        return market;
    }

    private static Market generateGMLMarket() {
        Market market = new Market(MarketCategory.GENERAL, "G:ML", "S1.2", "Next game winner");
        market.setIsValid(true);
        market.put("A", 0.6);
        market.put("B", 0.4);
        return market;
    }

    private MarketPricesList testList1() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices pinnacle = new MarketPrices();
        marketPricesList.put("Pinnacle", pinnacle);
        pinnacle.setSourceWeight(1);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.setSequenceId("M");
        p1.put("A", 2.3);
        p1.put("B", 1.5);
        pinnacle.addMarketPrice(p1);
        return marketPricesList;
    }

    private MarketPricesList testList2() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices pinnacle = new MarketPrices();
        marketPricesList.put("Pinnacle", pinnacle);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.setSequenceId("M");
        p1.put("A", 2.3);
        p1.put("B", 1.5);
        pinnacle.addMarketPrice(p1);
        MarketPrice p2 = new MarketPrice("G:ML", "Total games", MarketCategory.GENERAL, null);
        p2.setSequenceId("S1.2");
        p2.put("A", 1.82);
        p2.put("B", 1.92);
        pinnacle.addMarketPrice(p2);
        MarketPrices b365 = new MarketPrices();
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.setSequenceId("M");
        b1.put("A", 2.3);
        b1.put("B", 1.5);
        b365.addMarketPrice(b1);
        marketPricesList.put("B365", b365);
        return marketPricesList;
    }

    private class TestMatchState extends MatchState {

        private static final long serialVersionUID = 1L;

        @Override
        public MatchState copy() {
            return null;
        }

        @Override
        public MatchIncidentPrompt getNextPrompt() {
            return null;
        }

        @Override
        public MatchIncident getMatchIncident(String response) {
            return null;
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
        public boolean isMatchCompleted() {
            return false;
        }

        @Override
        public MatchFormat getMatchFormat() {
            return null;
        }

        @Override
        public GamePeriod getGamePeriod() {
            return null;
        }

        @Override
        public boolean preMatch() {
            return true;
        }

        @Override
        public int secsLeftInCurrentPeriod() {
            return 0;
        }

        @Override
        public MatchState generateSimpleMatchState() {
            return this;
        }
    }

}


