package ats.algo.core.comparetomarket;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.MarketCategory;

public class MarketPriceSanityCheckTest {

    @Test
    public void test() {
        MarketPrice price = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        price.put("A", 23.0);
        price.put("B", 1.025);
        price.put("Draw", 14.0);
        assertEquals(true, price.sanityCheckPrice());
        assertEquals(null, price.getSanityCheckFailureReason());
        price.put("B", 1.0);
        assertEquals(false, price.sanityCheckPrice());
        System.out.println(price.getSanityCheckFailureReason());
        assertTrue(price.getSanityCheckFailureReason().contains("= 1.0"));

        price = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        price.put("A", 3.0);
        price.put("B", 3.0);
        price.put("Draw", 4.0);
        assertEquals(false, price.sanityCheckPrice());
        System.out.println(price.getSanityCheckFailureReason());
        assertTrue(price.getSanityCheckFailureReason().contains("< 0.0"));

        price = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        price.put("A", 2.0);
        price.put("B", 2.0);
        price.put("Draw", 2.0);
        assertEquals(false, price.sanityCheckPrice());
        System.out.println(price.getSanityCheckFailureReason());
        assertTrue(price.getSanityCheckFailureReason().contains("Average margin per selection"));

    }



}
