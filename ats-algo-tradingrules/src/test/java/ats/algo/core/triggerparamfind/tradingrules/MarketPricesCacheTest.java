package ats.algo.core.triggerparamfind.tradingrules;


import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;

public class MarketPricesCacheTest {

    @Test
    public void test1() {
        MarketPricesCache cache = new MarketPricesCache();
        cache.addPricesToCache(marketPricesList1("Bet365", 1.8, 1.8), 100L);
        MarketPricesList marketPricesList = cache.getPricesSinceTime(150);
        assertEquals(0, marketPricesList.size());
        marketPricesList = cache.getPricesSinceTime(50L);
        // System.out.println(marketPricesList);
        assertEquals(1, marketPricesList.size());
        assertEquals(1.8, marketPricesList.get("Bet365").get("FT:ML", "M").get("A"), 0.0001);
        cache.addPricesToCache(marketPricesList1("Pinnacle", 2.0, 2.0), 200L);
        marketPricesList = cache.getPricesSinceTime(50L);
        System.out.println(marketPricesList);
        assertEquals(2, marketPricesList.size());
        assertEquals(1.8, marketPricesList.get("Bet365").get("FT:ML", "M").get("A"), 0.0001);
        assertEquals(2.0, marketPricesList.get("Pinnacle").get("FT:ML", "M").get("A"), 0.0001);
        marketPricesList = cache.getPricesSinceTime(150L);
        System.out.println(marketPricesList);
        assertEquals(1, marketPricesList.size());
        assertEquals(2.0, marketPricesList.get("Pinnacle").get("FT:ML", "M").get("A"), 0.0001);

    }

    private MarketPricesList marketPricesList1(String source, double p1, double p2) {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices prices = new MarketPrices();
        prices.setSourceWeight(1);
        marketPricesList.put(source, prices);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", p1);
        b1.put("B", p2);
        prices.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "166");
        b2.put("Over", p1);
        b2.put("Under", p2);
        prices.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "1.5");
        b3.put("AH", p1);
        b3.put("BH", p2);
        prices.addMarketPrice(b3);
        MarketPrice b4 = new MarketPrice("H1:MW", "first half winner", MarketCategory.GENERAL);
        b4.setSequenceId("H1");
        b4.put("AH", p1);
        b4.put("BH", p2);
        prices.addMarketPrice(b4);
        MarketPrice b5 = new MarketPrice("H2:MW", "second half winner", MarketCategory.GENERAL);
        b5.setSequenceId("H2");
        b5.put("AH", p1);
        b5.put("BH", p2);
        prices.addMarketPrice(b5);
        return marketPricesList;
    }


}
