package ats.algo.core.markets;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ats.algo.genericsupportfunctions.JsonSerializer;

public class ResultedMarketsTest {

    @Test
    public void testSerialisation() {

        ResultedMarkets resultedMarkets = new ResultedMarkets();
        resultedMarkets.setIncidentId("RequestId String");
        resultedMarkets.setTimeStamp(System.currentTimeMillis());
        ResultedMarket market1 =
                        new ResultedMarket("FT:ML", null, MarketCategory.GENERAL, "M", false, "description", "A", 0);

        ResultedMarket market2 =
                        new ResultedMarket("FT:TG", "20.5", MarketCategory.GENERAL, "M", false, "description", "A", 22);
        market2.setWinningSelections(getTestSelections("WS"));
        market2.setHalfWon(getTestSelections("HW"));
        market2.setHalfLost(getTestSelections("HL"));
        market2.setStakeBack(getTestSelections("SB"));
        resultedMarkets.addMarket(market1);
        resultedMarkets.addMarket(market2);
        System.out.print(resultedMarkets.toString());
        String json = JsonSerializer.serialize(resultedMarkets, true);
        System.out.println(json);
        ResultedMarkets resultedMarkets2 = JsonSerializer.deserialize(json, ResultedMarkets.class);
        assertEquals(resultedMarkets, resultedMarkets2);
    }

    @Test
    public void testResultedSelectionsToString() {
        ResultedMarket market1 =
                        new ResultedMarket("FT:ML", null, MarketCategory.GENERAL, "M", false, "description", "A", 0);
        System.out.println(market1.resultedSelectionsToString());
        ResultedMarket market2 =
                        new ResultedMarket("FT:TG", "20.5", MarketCategory.GENERAL, "M", false, "description", "A", 22);
        market2.setWinningSelections(getTestSelections("WS"));
        market2.setHalfWon(getTestSelections("HW"));
        market2.setHalfLost(getTestSelections("HL"));
        market2.setStakeBack(getTestSelections("SB"));
        System.out.println(market2.resultedSelectionsToString());
        market2.setWinningSelections(null);
        System.out.println(market2.resultedSelectionsToString());
        market2.setHalfLost(null);
        System.out.println(market2.resultedSelectionsToString());
        market2.setWinningSelections(new ArrayList<String>(0));
        System.out.println(market2.resultedSelectionsToString());
        assertFalse(market2.resultedSelectionsToString().contains("won"));


    }

    @Test
    public void testSubTyep() {
        ArrayList<String> arrayList = new ArrayList<String>(1);
        ResultedMarket market2 = new ResultedMarket("FT:AOU", "1.75", MarketCategory.OVUN, "M", false,
                        "Asian Total Goals", arrayList, 0);
        market2.setHalfLost(getTestSelections("Under"));
        market2.setHalfWon(getTestSelections("Over"));
        market2.setStakeBack(arrayList);
        System.out.println("before : " + market2);
        ResultedMarket market1 = market2.getResultedMarketForSubType("1.75");
        System.out.println("after : " + market1);
    }



    private List<String> getTestSelections(String prefix) {
        List<String> testSelns = new ArrayList<String>(2);
        testSelns.add(prefix + "_1");
        testSelns.add(prefix + "_2");
        return testSelns;
    }

}
