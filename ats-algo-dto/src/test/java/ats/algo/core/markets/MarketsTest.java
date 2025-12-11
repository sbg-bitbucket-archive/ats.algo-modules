package ats.algo.core.markets;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketsTest {

    @Test
    public void test() {
        Market market = generateGamesHandicapMarket2();
        System.out.printf("lineIdStr: %s, selName: %s, prob: %.3f\n", market.getLineId(),
                        market.getSelectionNameOverOrA(), market.get("A"));
        Market market3 = market.getMarketForLineId(market.getLineId());
        System.out.printf("lineIdStr: %s, selName: %s, prob: %.3f\n\n", market3.getLineId(),
                        market3.getSelectionNameOverOrA(), market3.get("A"));

        for (int i = 15; i > -15; i--) {
            String lineIdStr = String.format("%.1f", i - 0.5);
            Market market2 = market.getMarketForLineId(lineIdStr);
            System.out.printf("lineIdStr: %s, selName: %s, prob: %.3f\n", lineIdStr, market2.getSelectionNameOverOrA(),
                            market2.get("A"));
        }
        assertEquals(.656, market.getMarketForLineId("1.5").get("AH"), 0.0005);
        assertEquals(.701, market.getMarketForLineId("2.5").get("AH"), 0.0005);
        assertEquals(.948, market.getMarketForLineId("6.5").get("AH"), 0.0005);
        assertEquals(.026, market.getMarketForLineId("-8.5").get("AH"), 0.0005);

    }

    // @Test
    public void testToString() {
        Markets markets = generateTestMarkets();
        System.out.println(markets.toString());
    }

    @Test
    public void testJsonForMarket() {
        Market market = generateGamesHandicapMarket();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(market);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.print(json);
        Market market2 = null;
        try {
            market2 = mapper.readValue(json, Market.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Market:\n" + market);
        System.out.println("Market2:\n" + market2);
        assertEquals(market, market2);
    }

    @Test
    public void testJsonForMarkets() {
        Markets markets = generateTestMarkets();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(markets);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.print(json);
        Markets markets2 = null;
        try {
            markets2 = mapper.readValue(json, Markets.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(markets, markets2);
    }

    @Test
    public void testGenerateDerivedDynamicRangeMarket() {
        Market srcMarket = generateGamesHandicapMarket();
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForDynamicRangeHcap("ABCD", "TestHandicapMarket",
                        DerivedMarketSpecApplicability.IN_PLAY_ONLY, 3, 2, false);
        Market market = DerivedMarket.getDerivedDyamicRangeHcapMarket(srcMarket, spec);
        System.out.print(market.toString());
        assertEquals(0.036, market.get("Draw"), 0.0001);
        assertEquals(0.036, market.get("Draw"), 0.0001);
        assertEquals(0.141, market.get("Team A to win by 5-6"), 0.0001);
        assertEquals(0.094, market.get("Team B to win by 1-2"), 0.0001);
        assertEquals(0.078, market.get("Team B to win by 7+"), 0.0001);
        ResultedMarket resultedBaseMarket = generateResultedGamesHandicapMarket();
        ResultedMarket resultedDerivedMarket =
                        DerivedMarket.resultDerivedDyamicRangeHcapMarket(market, resultedBaseMarket);
        assertEquals("Team B to win by 1-2", resultedDerivedMarket.getWinningSelections().get(0));
    }

    @Test
    public void testGenerateAllRequiredLines() {
        Market srcMarket = generateGamesHandicapMarket();
        DerivedMarketSpec spec = DerivedMarketSpec
                        .getDerivedMarketSpecForRegularLines(DerivedMarketSpecApplicability.IN_PLAY_ONLY, 3, 2);
        int balancedLineIndex = srcMarket.getBalancedLineIndex();
        System.out.println("balancedLineIndex: " + balancedLineIndex);
        Collection<Market> derivedMarkets = DerivedMarket.generateAllRequiredLinesRegular(srcMarket, spec,
                        srcMarket.getBalancedLineIndex());
        assertEquals(7, derivedMarkets.size());
        for (Market market : derivedMarkets) {
            System.out.println(market);
        }
    }

    @Test
    public void testNearestEvensLines() {
        Market srcMarket = generateGamesHandicapMarket();
        DerivedMarketSpec spec = DerivedMarketSpec.getDerivedMarketSpecForLinesNearestEvens("Test",
                        "test derivedMarket", DerivedMarketSpecApplicability.IN_PLAY_ONLY, 4);
        Collection<Market> derivedMarkets = DerivedMarket.generateNearestEvensMarkets(srcMarket, spec);
        for (Market market : derivedMarkets) {
            System.out.println(market);
        }
        assertEquals(4, derivedMarkets.size());
    }

    @Test
    public void testGenerateLineMarket() {
        Market srcMarket = generateGamesHandicapMarket();
        System.out.println(srcMarket.getBalancedLineIndex());
        Market newMarket = srcMarket.getMarketForLineId("0.75");
        System.out.print(newMarket);

    }


    public static Markets generateTestMarkets() {
        Market market = generateGamesHandicapMarket();
        Market market2 = generateGamesHandicapMarket2();
        Markets markets = new Markets();
        markets.addMarketWithFullKey(market);
        markets.addMarketWithFullKey(market2);
        return markets;
    }

    /**
     * Generates an example handicap market. All properties are set correctly except for the probabilities which are set
     * to fixed values in this example code. The probabilities are set up for a balanced line of -0.5
     */

    private static Market generateGamesHandicapMarket() {
        Market market = new Market(MarketCategory.HCAP, "FT:SPRD", "M", "Match total games handicap");
        market.setIsValid(true);
        market.setLineBase(-10);
        /*
         * probs [n] holds the probability of games difference (gamesA-gamesB) being <= lineBase+ n; Using fixed probs
         * here instead of calculating from the model
         */

        double[] probs = {0.004, 0.015, 0.035, 0.078, 0.136, 0.215, 0.309, 0.386, 0.441, 0.480, 0.516, 0.553, 0.608,
                0.685, 0.777, 0.858, 0.918, 0.959, 0.981, 0.993, 0.997};
        market.setLineProbs(probs);
        market.setLineId("-0.5"); // subType is the balanced line - i.e. the
                                  // one with prob closest to 0.5
        double probB = .516; // i.e. the sum of probs[10]

        market.put("A", 1 - probB);
        market.put("B", probB);
        market.setSelectionNameOverOrA("A(-0.5)");
        market.setSelectionNameUnderOrB("B(+0.5)");
        return market;
    }

    /**
     * generates an example resulted handicap market
     * 
     * @return
     */
    private static ResultedMarket generateResultedGamesHandicapMarket() {
        ResultedMarket resultedMarket = new ResultedMarket("FT:SPRD", "-0.5", MarketCategory.HCAP, "M", false,
                        "Match total games handicap", "B", -2);
        return resultedMarket;
    }

    /**
     * Generates an example handicap market. All properties are set correctly except for the probabilities which are set
     * to fixed values in this example code. The probabilities are set up for a balanced line of -0.5
     */

    private static Market generateGamesHandicapMarket2() {
        Market market = new Market(MarketCategory.HCAP, "FT:SPRD2", "M", "Match total games handicap");
        market.setIsValid(true);
        market.setLineBase(-12);
        /*
         * probs [n] holds the probability of games difference being <= lineBase+ n; Using fixed probs here instead of
         * calculating from the model
         */

        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setLineId("-2.5"); // subType is the balanced line - i.e. the
                                  // one with prob closest to 0.5
        double probB = .4987; // i.e. the sum of probs[10]

        market.put("A", 1 - probB);
        market.put("B", probB);
        market.setSelectionNameOverOrA("A(-2.5)");
        market.setSelectionNameUnderOrB("B(2.5)");
        return market;

    }
}
