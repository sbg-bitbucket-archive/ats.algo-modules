package ats.algo.sport.tennis.tradingrules;

import static org.junit.Assert.*;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.Selection;
import ats.algo.core.triggerparamfind.tradingrules.MarketPricesCache;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindData;
import ats.algo.core.triggerparamfind.tradingrules.TriggerParamFindTradingRule;
import ats.algo.sport.tennis.TennisMatchState;
import org.junit.Test;
import org.mockito.Mockito;


import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;

public class TennisTriggerParamFindTradingRuleTest {



    @Test
    public void test2() {

        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices marketTest1 = new MarketPrices();
        marketTest1.setSourceWeight(1);
        marketTest1.addMarketPrice(testMktPrice("FT:ML", "M"));
        marketTest1.addMarketPrice(testMktPrice("G:ML", "S1.2"));

        marketPricesList.put("TestSource1", marketTest1);
        MarketPrices marketTest2 = new MarketPrices();
        marketTest2.setSourceWeight(1);
        marketTest2.addMarketPrice(testMktPrice("G:ML", "S1.2"));
        marketTest2.addMarketPrice(testMktPrice("G:ML", "S1.3"));
        marketPricesList.put("TestSource2", marketTest2);

        TennisTriggerParamFindTradingRule.checkLatestGameWinnerPricesForValidity(marketPricesList);
        MarketPrice m1 = marketPricesList.get("TestSource1").get("G:ML", "S1.2");
        MarketPrice m2 = marketPricesList.get("TestSource2").get("G:ML", "S1.2");
        MarketPrice m3 = marketPricesList.get("TestSource2").get("G:ML", "S1.3");
        assertFalse(m1.isValid());
        assertFalse(m2.isValid());
        assertTrue(m3.isValid());

        MarketPricesList marketPricesList1 = new MarketPricesList();
        MarketPrices marketTest3 = new MarketPrices();
        marketTest3.setSourceWeight(1);
        marketTest3.addMarketPrice(testMktPrice("FT:ML", "M"));
        marketTest3.addMarketPrice(testMktPrice("G:ML", "S1.3"));

        marketPricesList1.put("TestSource3", marketTest3);

        MarketPrices marketTest4 = new MarketPrices();
        marketTest4.setSourceWeight(1);
        marketTest4.addMarketPrice(testMktPrice("G:ML", "S1.2"));
        marketTest4.addMarketPrice(testMktPrice("G:ML", "S1.3"));
        marketPricesList1.put("TestSource4", marketTest4);

        TennisTriggerParamFindTradingRule.checkLatestGameWinnerPricesForValidity(marketPricesList1);
        MarketPrice m4 = marketPricesList1.get("TestSource3").get("G:ML", "S1.3");
        MarketPrice m5 = marketPricesList1.get("TestSource4").get("G:ML", "S1.2");
        MarketPrice m6 = marketPricesList1.get("TestSource4").get("G:ML", "S1.3");
        assertTrue(m4.isValid());
        assertFalse(m5.isValid());
        assertTrue(m6.isValid());

        MarketPricesList marketPricesList2 = new MarketPricesList();
        MarketPrices marketTest5 = new MarketPrices();
        marketTest5.setSourceWeight(1);
        marketTest5.addMarketPrice(testMktPrice("FT:ML", "M"));
        marketTest5.addMarketPrice(testMktPrice("G:ML", "S1.3"));

        marketPricesList2.put("TestSource5", marketTest5);

        MarketPrices marketTest6 = new MarketPrices();
        marketTest6.setSourceWeight(1);
        marketTest6.addMarketPrice(testMktPrice("G:ML", "S1.3"));
        marketTest6.addMarketPrice(testMktPrice("G:ML", "S1.3"));
        marketPricesList2.put("TestSource6", marketTest6);

        TennisTriggerParamFindTradingRule.checkLatestGameWinnerPricesForValidity(marketPricesList2);
        MarketPrice m7 = marketPricesList2.get("TestSource5").get("G:ML", "S1.3");
        MarketPrice m8 = marketPricesList2.get("TestSource6").get("G:ML", "S1.3");
        MarketPrice m9 = marketPricesList2.get("TestSource6").get("G:ML", "S1.3");
        assertTrue(m7.isValid());
        assertTrue(m8.isValid());
        assertTrue(m9.isValid());

    }


    private MarketPrice testMktPrice(String type, String seqId) {
        MarketPrice p = new MarketPrice(type, "test", MarketCategory.GENERAL, null, seqId);
        p.put("A", 1.86);
        p.put("B", 1.88);
        return p;
    }

    @Test
    public void testShouldRequestParamFind() {
        MatchState matchState = new TennisMatchState().generateSimpleMatchState();

        Markets markets = new Markets();
        Market matchWinner = new Market(MarketCategory.GENERAL,"FT:ML","M","Match Winner");
        matchWinner.getSelections().put("A",new Selection(0.51));
        matchWinner.getSelections().put("B",new Selection(0.49));
        matchWinner.setValid(true);
        markets.addMarketWithFullKey(matchWinner);
//        markets.addMarketWithFullKey(new Market(MarketCategory.OVUN,"FT:OU","M","Total Games"));
//        markets.addMarketWithFullKey(new Market(MarketCategory.HCAP,"FT:SPRD","M","Game Handicap"));

        MarketPricesList marketPricesListAfterChange = new MarketPricesList();
        marketPricesListAfterChange.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:ML", "M",0.31,0.69));
//        marketPricesListAfterChange.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:OU", "M",1.757,2.14));
//        marketPricesListAfterChange.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:SPRD", "M",1.689,2.21));
        TriggerParamFindData triggerParamFindData = new TriggerParamFindData();
        MarketPricesCache marketPricesCache = new MarketPricesCache();
        marketPricesCache.addPricesToCache(marketPricesListAfterChange,System.currentTimeMillis());

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:ML", "M",0.27,0.73));
//        marketPricesList.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:OU", "M",1.757,2.14));
//        marketPricesList.addMarketPrice("Pinnacle",generateMarketPriceObject("FT:SPRD", "M",1.689,2.21));

        TriggerParamFindData triggerParamFindDataSpy = Mockito.spy(triggerParamFindData);
        Mockito.doReturn(marketPricesCache).when(triggerParamFindDataSpy).getMarketPricesCache();
        Mockito.doReturn(marketPricesList).when(triggerParamFindDataSpy).getLastUsedMarketPricesList();

        EventSettings eventSettings = new EventSettings();
        TriggerParamFindTradingRule triggerParamFindTradingRule = new TriggerParamFindTradingRule("Test Rule", 1);
        triggerParamFindTradingRule.setTimeBetweenParamFinds(System.currentTimeMillis()-100000);   //Should trigger PF based on timer

        assertNull(triggerParamFindTradingRule
                        .shouldRequestParamFind(matchState, markets, true, triggerParamFindDataSpy, eventSettings, false));    //PF denied as priceDistance is less than threshold
    }

    private MarketPrice generateMarketPriceObject(String type, String seqId,Double priceA,Double priceB) {
        MarketPrice p = new MarketPrice(type, "FT:ML|null|M|GENERAL", MarketCategory.GENERAL, null, seqId);
        p.put("A", priceA);
        p.put("B", priceB);
        p.setMarketWeight(4.0);
        p.setTimeStamp(System.currentTimeMillis());
        return p;
    }

}
