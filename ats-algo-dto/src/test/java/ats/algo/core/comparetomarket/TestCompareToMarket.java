package ats.algo.core.comparetomarket;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.core.util.json.JsonUtil;

public class TestCompareToMarket {

    @Test
    /**
     * basic test markets and marketPrices the same
     */
    public void test1() {
        Markets markets = setTestMarkets(.7206, .2794, .5217, .4783);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(1.0, 1.36, 3.4, 1.84, 2.0));
        MarketPricesStatus status = CompareToMarket.getMarketPricesStatus(markets, marketPricesList);
        // printEverything(markets, marketPricesList, status);
        assertTrue(status.isPricesOk());
        assertFalse(status.isParamFindRequired());
        assertEquals(0, status.getMinCostPossibleWithTheseMarketsPrices(), 0.0001);
        assertEquals(0, status.getActualCostForOurMarkets(), 0.005);
        assertEquals(0, status.getWarningMessages().size());
    }

    @Test
    /*
     * test big difference between markets and market prices
     */
    public void test2() {
        Markets markets = setTestMarkets(0.05, 0.95, 0.1, 0.9);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(1.0, 1.36, 3.4, 1.84, 2.0));
        MarketPricesStatus status = CompareToMarket.getMarketPricesStatus(markets, marketPricesList);
        // printEverything(markets, marketPricesList, status);
        assertTrue(status.isPricesOk());
        assertTrue(status.isParamFindRequired());
        assertEquals(0, status.getMinCostPossibleWithTheseMarketsPrices(), 0.0001);
        assertEquals(.404, status.getActualCostForOurMarkets(), 0.005);
        assertEquals(0, status.getWarningMessages().size());
    }

    @Test
    /*
     * test three sources of prices , one of them with a price the wrong way
     */
    public void test3() {
        CompareToMarket.setParamFindKickoffThreshold(0.025);
        Markets markets = setTestMarkets(0.05, 0.95, 0.1, 0.9);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(1.5, 1.36, 3.4, 1.84, 2.0));
        marketPricesList.put("Source2", setTestMarketPrices(2.5, 1.36, 3.4, 1.89, 1.9));
        marketPricesList.put("Source3", setTestMarketPrices(4.0, 3.4, 1.36, 1.82, 2.1));

        MarketPricesStatus status = CompareToMarket.getMarketPricesStatus(markets, marketPricesList);
        // printEverything(markets, marketPricesList, status);
        assertTrue(status.isParamFindRequired());
        assertFalse(status.isPricesOk());
        assertEquals(.112, status.getMinCostPossibleWithTheseMarketsPrices(), 0.003);
        assertEquals(.341, status.getActualCostForOurMarkets(), 0.001);
        assertEquals(6, status.getWarningMessages().size());
    }

    @Test
    /*
     * test big differences in weightings
     */
    public void test4() {
        CompareToMarket.setParamFindKickoffThreshold(0.025);
        Markets markets = setTestMarkets(.5, .5, 0.5, 0.5);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(100.0, 1.87, 1.87, 1.87, 1.87));
        marketPricesList.put("Source2", setTestMarketPrices(1.0, 1.05, 9.39, 1.87, 1.87));
        MarketPricesStatus status = CompareToMarket.getMarketPricesStatus(markets, marketPricesList);
        // printEverything(markets, marketPricesList, status);
        assertTrue(status.isPricesOk());
        assertFalse(status.isParamFindRequired());
        assertEquals(0.019, status.getMinCostPossibleWithTheseMarketsPrices(), 0.025);
        assertEquals(0.019, status.getActualCostForOurMarkets(), 0.025);
        assertEquals(2, status.getWarningMessages().size());
    }

    // @SuppressWarnings("unused")
    // private void printEverything(Markets markets, MarketPricesList marketPricesList, MarketPricesStatus status) {
    // System.out.print(markets.toString());
    // System.out.print(marketPricesList.toString());
    // System.out.print(status.toString());
    // }

    private Markets setTestMarkets(double probA, double probB, double probOver, double probUnder) {
        Markets markets = new Markets();
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match winner");
        market.setCategory(MarketCategory.GENERAL);
        market.setIsValid(true);
        market.put("A", probA);
        market.put("B", probB);
        markets.addMarketWithShortKey(market);
        market = new Market(MarketCategory.OVUN, "FT:OU", "M", "Total Games");
        market.setLineId("20.5");
        market.setIsValid(true);
        market.put("Over", probOver);
        market.put("Under", probUnder);
        market.setLineBase(12);
        double[] lineProbs = {0.00312, 0.01699, 0.04808, 0.10508, 0.18292, 0.2792, 0.37246, 0.45557, 0.52741, 0.57781,
                0.63665, 0.67548, 0.69067, 0.72045, 0.75686, 0.78658, 0.81981, 0.85361, 0.8853, 0.91775, 0.94552,
                0.96216, 0.9747, 0.98781, 0.99501, 0.99643, 0.99851};
        market.setLineProbs(lineProbs);
        markets.addMarketWithShortKey(market);
        return markets;

    }

    private MarketPrices setTestMarketPrices(double weight, double priceA, double priceB, double priceOver,
                    double priceUnder) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(weight);
        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, "", "M");
        ab.put("A", priceA);
        ab.put("B", priceB);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "20.5", "M");
        tmtg.put("Over", priceOver);
        tmtg.put("Under", priceUnder);
        m.addMarketPrice(tmtg);
        return m;
    }

    @Test
    /*
     * test three sources of prices , one of them with a price the wrong way
     */
    public void testSerialisation() {
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(1.5, 1.36, 3.4, 1.84, 2.0));
        marketPricesList.put("Source2", setTestMarketPrices(2.5, 1.36, 3.4, 1.89, 1.9));
        marketPricesList.put("Source3", setTestMarketPrices(4.0, 3.4, 1.36, 1.82, 2.1));
        System.out.println(marketPricesList.toString());
        String json = JsonUtil.marshalJson(marketPricesList, true);
        System.out.println(json);
        MarketPricesList marketPricesList2 = JsonUtil.unmarshalJson(json, MarketPricesList.class);
        boolean z = marketPricesList.equals(marketPricesList2);
        assertTrue(z);
    }

    @Test
    /*
     * test three sources of prices , one of them with a price the wrong way
     */
    public void testToString() {
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Source1", setTestMarketPrices(1.5, 1.36, 3.4, 1.84, 2.0));
        marketPricesList.put("Source2", setTestMarketPrices(2.5, 1.36, 3.4, 1.89, 1.9));
        marketPricesList.put("Source3", setTestMarketPrices(4.0, 3.4, 1.36, 1.82, 2.1));
        System.out.println(marketPricesList.toString());

    }

}
