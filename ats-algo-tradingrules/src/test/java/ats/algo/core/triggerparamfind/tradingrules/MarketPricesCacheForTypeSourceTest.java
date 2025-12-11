package ats.algo.core.triggerparamfind.tradingrules;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.markets.MarketCategory;

public class MarketPricesCacheForTypeSourceTest {

    @Test
    public void test1() {
        MarketPricesCacheForTypeSource cache = new MarketPricesCacheForTypeSource();
        cache.add(100L, marketPrice1());
        cache.add(200L, marketPrice2());
        cache.add(300L, marketPrice3(1.8, 1.8));
        List<MarketPrice> prices = cache.getLatestMarketPrices(250);
        assertEquals(1, prices.size());
        assertEquals("H1:MW", prices.get(0).getType());
        assertEquals(1.8, prices.get(0).get("AH"), 0.00001);
        cache.add(400L, marketPrice3(2.1, 1.4));
        prices = cache.getLatestMarketPrices(250);
        assertEquals(1, prices.size());
        assertEquals("H1:MW", prices.get(0).getType());
        assertEquals(2.1, prices.get(0).get("AH"), 0.00001);

        prices = cache.getLatestMarketPrices(150);
        assertEquals(2, prices.size());
        assertEquals("H2:MW", prices.get(0).getType());
        assertEquals(1.83, prices.get(0).get("AH"), 0.00001);

        prices = cache.getLatestMarketPrices(0);
        assertEquals(3, prices.size());
        assertEquals("FT:ML", prices.get(0).getType());
        assertEquals(2.1, prices.get(2).get("AH"), 0.00001);

    }

    private MarketPrice marketPrice1() {
        MarketPrice b = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b.put("A", 1.8);
        b.put("B", 1.77);
        return b;
    }

    private MarketPrice marketPrice2() {
        MarketPrice b = new MarketPrice("H2:MW", "second half winner", MarketCategory.GENERAL);
        b.setSequenceId("H2");
        b.put("AH", 1.83);
        b.put("BH", 1.83);
        return b;
    }

    private MarketPrice marketPrice3(double p1, double p2) {
        MarketPrice b = new MarketPrice("H1:MW", "first half winner", MarketCategory.GENERAL);
        b.setSequenceId("H1");
        b.put("AH", p1);
        b.put("BH", p2);
        return b;
    }


}
