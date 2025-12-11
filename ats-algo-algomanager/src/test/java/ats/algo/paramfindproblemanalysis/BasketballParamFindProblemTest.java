package ats.algo.paramfindproblemanalysis;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.basketball.BasketballMatchParams;

public class BasketballParamFindProblemTest extends AlgoManagerSimpleTestBase {

    static long eventId = 12345L;

    /*
     * repeat the param find 100 times to try to get it to fail
     */
    public void test1() {
        MatchFormat matchFormat = new BasketballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
        publishedMatchParams.setEventId(eventId);

        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesMaxIterationsExceededError());
        System.out.println(publishedParamFinderResults.getParamFindResultsStatus());
        // System.out.println(publishedMatchParams);

        for (int i = 0; i < 5; i++) {
            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesIllegalArgumentExceptionError());
            System.out.println(publishedParamFinderResults.getParamFindResultsStatus());
            // System.out.println(publishedMatchParams);

        }
    }

    /*
     * run the param find for the specific params that fail. Repeat 100 ties to try to reproduce the error
     */
    public void testIllegalArgumentExceptionError() {
        for (int i = 0; i < 100; i++) {
            MatchFormat matchFormat = new BasketballMatchFormat();
            algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
            BasketballMatchParams matchParams = (BasketballMatchParams) publishedMatchParams;
            matchParams.setEventId(eventId);
            matchParams.getPace().setProperties(159.433, 17.0879, 0);
            matchParams.getAdv().setProperties(21.956, 1.3757, 0);
            matchParams.getAdjRate().setProperties(.122, 0.18, 0);
            matchParams.getQ1GD().setProperties(0.25, 0.05, 0);
            matchParams.getQ2GD().setProperties(0.25, 0.05, 0);
            matchParams.getQ3GD().setProperties(0.25, 0.05, 0);
            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesIllegalArgumentExceptionError());
            System.out.print("iteration no: " + i + 1);
            System.out.println(publishedParamFinderResults);
            System.out.println(publishedMatchParams);
            if (!publishedParamFinderResults.isSuccess())
                System.exit(0);
            algoManager.handleRemoveEvent(eventId);
        }
    }

    public void testMaxIterationsExceededError() {
        for (int i = 0; i < 1; i++) {
            MatchFormat matchFormat = new BasketballMatchFormat();
            algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
            BasketballMatchParams matchParams = (BasketballMatchParams) publishedMatchParams;
            matchParams.setEventId(eventId);
            matchParams.getPace().setProperties(146.752, 2.9501, 0);
            matchParams.getAdv().setProperties(4.704, 2.096, 0);
            matchParams.getAdjRate().setProperties(1.96, -0.0806, 0);
            matchParams.getQ1GD().setProperties(0.25, 0.05, 0);
            matchParams.getQ2GD().setProperties(0.25, 0.05, 0);
            matchParams.getQ3GD().setProperties(0.25, 0.05, 0);
            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesMaxIterationsExceededError());
            System.out.print("iteration no: " + i + 1);
            System.out.println(publishedParamFinderResults);
            System.out.println(publishedMatchParams);
            if (!publishedParamFinderResults.isSuccess())
                System.exit(0);
            algoManager.handleRemoveEvent(eventId);
        }
    }

    public void testTrappedInLocalMinumum() {
        for (int i = 0; i < 1; i++) {
            MatchFormat matchFormat = new BasketballMatchFormat();
            algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
            BasketballMatchParams matchParams = (BasketballMatchParams) publishedMatchParams;
            matchParams.setEventId(eventId);

            // Change the starting position to see param find trapped
            matchParams.getPace().setProperties(120.752, 2.9501, 0);
            matchParams.getAdv().setProperties(4.704, 2.096, 0);
            matchParams.getAdjRate().setProperties(0, 0.15, 0);

            algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesInfiniteLoop());
            System.out.print("iteration no: " + i + 1);
            System.out.println(publishedParamFinderResults);
            System.out.println(publishedMatchParams);
            if (!publishedParamFinderResults.isSuccess())
                System.exit(0);
            algoManager.handleRemoveEvent(eventId);
        }
    }

    /*
     * run the price calc for the specific that params that fail
     */
    public void test2() {
        MatchFormat matchFormat = new BasketballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.BASKETBALL, eventId, matchFormat, 4);
        BasketballMatchParams matchParams = (BasketballMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        matchParams.getPace().setProperties(171.41637698, 16.551827194236687, 0);
        matchParams.getAdv().setProperties(-2.83975392, 2.2040670909299047, 0);
        matchParams.getAdjRate().setProperties(0.36847812, 0.05969853580528302, 0);
        matchParams.getQ1GD().setProperties(0.38991949, 0.051797566300060716, 0);
        matchParams.getQ2GD().setProperties(0.2845502, 0.049020088528793755, 0);
        matchParams.getQ3GD().setProperties(0.32642347, 0.04399201220587514, 0);
        algoManager.handleSetMatchParams(publishedMatchParams.generateGenericMatchParams());
    }

    private MarketPricesList getTestMarketPricesIllegalArgumentExceptionError() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 1.25);
        b1.put("B", 3.75);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "162");
        b2.put("Over", 2.1);
        b2.put("Under", 1.58);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "-25");
        b3.put("AH", 2.2);
        b3.put("BH", 1.53);
        bet365.addMarketPrice(b3);
        return marketPricesList;

    }

    private MarketPricesList getTestMarketPricesMaxIterationsExceededError() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 1.01);
        b1.put("B", 12.00);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "147.5");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "-4");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        bet365.addMarketPrice(b3);
        return marketPricesList;

    }

    private MarketPricesList getTestMarketPricesInfiniteLoop() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices lSports = new MarketPrices();

        lSports.setSourceWeight(1);
        marketPricesList.put("LSPORTS", lSports);

        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 9.00);
        b1.put("B", 1.04);
        lSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total point", MarketCategory.OVUN, "115.5");
        b2.put("Over", 1.83);
        b2.put("Under", 1.83);
        lSports.addMarketPrice(b2);

        MarketPrice b3 = new MarketPrice("FT:SPRD", "Points hcap", MarketCategory.HCAP, "16.5");
        b3.put("AH", 1.83);
        b3.put("BH", 1.83);
        lSports.addMarketPrice(b3);

        // MarketPrices bet365 = new MarketPrices();
        // bet365.setSourceWeight(1);
        // marketPricesList.put("Bet365", bet365);
        // MarketPrice l1 = new MarketPrice("FT:ML", "Match winner",
        // MarketCategory.GENERAL, null);
        // l1.put("A", 9.00);
        // l1.put("B", 1.04);
        // bet365.addMarketPrice(l1);

        return marketPricesList;

    }

    public static void main(String[] args) {
        BasketballParamFindProblemTest tester = new BasketballParamFindProblemTest();
        tester.testTrappedInLocalMinumum();
        // tester.test1();
        System.out.println("finished");
    }

}
