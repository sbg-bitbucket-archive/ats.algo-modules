package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.tennis.TennisMatchFormat;

public class EventSettingsTest extends AlgoManagerSimpleTestBase {

    private long eventId = 12L;



    @Test
    public void test() {
        MethodName.log();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, new TennisMatchFormat());
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("SOURCEWEIGHT_Bet365", "2.5");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices());
        // ParamFindResults result = super.publishedParamFinderResults;
        // System.out.println(result);
    }

    private MarketPricesList getTestMarketPrices() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 1.88);
        b1.put("B", 1.77);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "166");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "1.5");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        bet365.addMarketPrice(b3);
        return marketPricesList;
    }

}
