package ats.algo.core.comparetomarket;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;

public class ComparePricesTest {

    @Test
    public void test() {
        Markets markets = getTestMarkets();
        /*
         * generate a marketPricesList with single price
         */
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices pinnacle = new MarketPrices();
        marketPricesList.put("Pinnacle", pinnacle);
        pinnacle.setSourceWeight(1);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.setSequenceId("M");
        p1.put("A", 2.3);
        p1.put("B", 1.5);
        p1.setMarketWeight(1);
        pinnacle.addMarketPrice(p1);
        MarketPricesStatus marketPricesStatus = new MarketPricesStatus();
        Map<String, Map<String, MarketProbs>> matchedMarkets =
                        CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        CompareToMarketMetrics metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        assertEquals(.151, metrics.getMarketsCost(), 0.003);
        /*
         * add a second price but with zero market weight
         */
        MarketPrice p2 = new MarketPrice("G:ML", "Total games", MarketCategory.GENERAL, null);
        p2.setSequenceId("S1.2");
        p2.put("A", 1.82);
        p2.put("B", 1.92);
        p2.setMarketWeight(0);
        pinnacle.addMarketPrice(p2);
        matchedMarkets = CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        assertEquals(.151, metrics.getMarketsCost(), 0.003);
        /*
         * set second price to weight of 1, first to weight of zero
         */
        p1.setMarketWeight(0);
        p2.setMarketWeight(1);
        matchedMarkets = CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        printAll(marketPricesStatus, matchedMarkets, metrics);
        assertEquals(.060, metrics.getMarketsCost(), 0.003);
        /*
         * set both to weight of 1
         */
        p1.setMarketWeight(1);
        p2.setMarketWeight(1);
        matchedMarkets = CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        printAll(marketPricesStatus, matchedMarkets, metrics);
        assertEquals(.115, metrics.getMarketsCost(), 0.003);
        /*
         * set to different weights
         */
        p1.setMarketWeight(2);
        p2.setMarketWeight(3);
        matchedMarkets = CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        printAll(marketPricesStatus, matchedMarkets, metrics);
        assertEquals(.106, metrics.getMarketsCost(), 0.003);
        assertTrue(CompareToMarket.getPricesDistance(marketPricesList, markets) > CompareToMarket
                        .getSmartParamFindKickoffThreshold());
    }

    @Test
    public void test2() {
        Markets markets = getTestMarkets();
        /*
         * generate a marketPricesList with single price from each of two sources
         */
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices pinnacle = new MarketPrices();
        marketPricesList.put("Pinnacle", pinnacle);
        pinnacle.setSourceWeight(1);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.setSequenceId("M");
        p1.put("A", 2.3);
        p1.put("B", 1.5);
        p1.setMarketWeight(1);
        pinnacle.addMarketPrice(p1);

        MarketPrices b365 = new MarketPrices();
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.setSequenceId("M");
        b1.put("A", 2.3);
        b1.put("B", 1.5);
        b365.addMarketPrice(b1);
        b365.setSourceWeight(1);
        marketPricesList.put("B365", b365);
        MarketPricesStatus marketPricesStatus = new MarketPricesStatus();
        Map<String, Map<String, MarketProbs>> matchedMarkets =
                        CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        CompareToMarketMetrics metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        assertEquals(.151, metrics.getMarketsCost(), 0.003);
        pinnacle.setSourceWeight(3);
        b365.setSourceWeight(2);
        matchedMarkets = CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        assertEquals(.151, metrics.getMarketsCost(), 0.003);

    }

    @Test
    public void test3() {
        Markets markets = getTestMarkets();
        /*
         * generate a marketPricesList with two prices from pinnacle, one from bet365
         */
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices pinnacle = new MarketPrices();
        marketPricesList.put("Pinnacle", pinnacle);
        pinnacle.setSourceWeight(5);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.setSequenceId("M");
        p1.put("A", 2.3);
        p1.put("B", 1.5);
        p1.setMarketWeight(2);
        pinnacle.addMarketPrice(p1);
        MarketPrice p2 = new MarketPrice("G:ML", "Total games", MarketCategory.GENERAL, null);
        p2.setSequenceId("S1.2");
        p2.put("A", 1.82);
        p2.put("B", 1.92);
        p2.setMarketWeight(3);
        pinnacle.addMarketPrice(p2);
        MarketPrices b365 = new MarketPrices();
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.setSequenceId("M");
        b1.put("A", 2.3);
        b1.put("B", 1.5);
        p1.setMarketWeight(2);
        b365.addMarketPrice(b1);
        b365.setSourceWeight(7);
        marketPricesList.put("B365", b365);
        MarketPricesStatus marketPricesStatus = new MarketPricesStatus();
        Map<String, Map<String, MarketProbs>> matchedMarkets =
                        CompareToMarket.getMatchedMarkets(markets, marketPricesList, marketPricesStatus);
        CompareToMarketMetrics metrics = CompareToMarket.calculateCostMetrics(markets, matchedMarkets);
        printAll(marketPricesStatus, matchedMarkets, metrics);
        assertEquals(.106, metrics.getMarketsCost(), 0.001);
    }


    private void printAll(MarketPricesStatus marketPricesStatus, Map<String, Map<String, MarketProbs>> matchedMarkets,
                    CompareToMarketMetrics metrics) {
        System.out.println("marketPricesStatus:");
        System.out.println(marketPricesStatus.toString());
        System.out.println("matchedMarkets:");
        System.out.println(CompareToMarket.matchedMarketsToString(matchedMarkets));
        System.out.println("metrics:");
        System.out.println(metrics.toString());
    }


    public static Markets getTestMarkets() {
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
}


