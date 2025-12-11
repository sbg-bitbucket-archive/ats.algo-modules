package ats.algo.core.comparetomarket;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.markets.MarketCategory;

public class MarketPricesListTest {

    @Test
    public void test() {
        MarketPricesList list = getTestMarketPricesList();
        assertEquals(3, list.size());
        list.filterOutZeroWeightSources();
        System.out.println(list);;
        assertEquals(2, list.size());
    }

    @Test
    public void testDiffers() {
        MarketPricesList list1 = getTestMarketPricesList();
        MarketPricesList list2 = getTestMarketPricesList();
        assertTrue(list1.pricesSame(list2));
        MarketPrice mp1 = list1.getMarketPricesList().get("TestSource1").getMarketPrices().get("FT:OU_M");
        MarketPrice mp2 = list2.getMarketPricesList().get("TestSource1").getMarketPrices().get("FT:OU_M");
        mp2.setTimeStamp(375);
        assertTrue(list1.pricesSame(list2));
        mp2.setSequenceId("ABC");
        assertFalse(list1.pricesSame(list2));
        mp2.setSequenceId("M");
        assertTrue(list1.pricesSame(list2));
        mp1.setLineId(null);
        assertFalse(list1.pricesSame(list2));
        mp1.setLineId("24.5");
        mp2.setLineId("24.5");
        assertTrue(list1.pricesSame(list2));
        mp2.setLineId(null);
        assertFalse(list1.pricesSame(list2));
        mp2.setLineId("24.5");
        assertTrue(list1.pricesSame(list2));
        mp1.getSelections().put("Over", 16.2);
        assertFalse(list1.pricesSame(list2));
        mp2.getSelections().put("Over", 16.2);
        assertTrue(list1.pricesSame(list2));
        mp2.getSelections().put("Under", 34.4);
        assertFalse(list1.pricesSame(list2));

    }

    private static MarketPricesList getTestMarketPricesList() {
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", getTestMarketPrices(1.0));
        marketPricesList.put("TestSource2", getTestMarketPrices(0.0));
        marketPricesList.put("TestSource3", getTestMarketPrices(3.0));
        return marketPricesList;
    }

    private static MarketPrices getTestMarketPrices(double weight) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(weight);
        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.86);
        ab.put("B", 1.88);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
        tmtg.put("Over", 1.82);
        tmtg.put("Under", 1.92);
        m.addMarketPrice(tmtg);
        return m;
    }

}
