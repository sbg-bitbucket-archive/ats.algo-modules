package ats.algo.algomanager;

import static org.junit.Assert.assertTrue;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;

public class PricesOutOfRangeTest extends AlgoManagerSimpleTestBase {

    // @Test
    public void testParamFind() {
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 1234567890123456789L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        /*
         * pf should proceed on basis of just FT:OU price
         */
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices1());
        System.out.println(publishedParamFinderResults.toString());
        assertTrue(publishedParamFinderResults.isSuccess());
        /*
         * pf should proceed but fail because no valid prices
         */
        /*
         * GC - comment out for now because temporarily disabled this logic
         */
        // algoManager.handleSupplyMarketPrices(getTestMarketPrices2(eventId));
        // System.out.println(publishedParamFinderResults.toString());
        // assertFalse(publishedParamFinderResults.isSuccess());


    }

    private MarketPricesList getTestMarketPrices1() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 25.0);
        ab.put("B", 1.02);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
        tmtg.put("Over", 1.82);
        tmtg.put("Under", 1.92);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }


    @SuppressWarnings("unused")
    private MarketPricesList getTestMarketPrices2() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 25.0);
        ab.put("B", 1.02);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
        tmtg.put("Over", 1.05);
        tmtg.put("Under", 20.0);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }

}
