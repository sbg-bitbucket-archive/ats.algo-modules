package ats.algo.paramfindproblemanalysis;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;

public class FootballParamFindProblemTest extends AlgoManagerSimpleTestBase {

    static long eventId = 12345L;

    public void testFootballParamFindFail() {
        for (int i = 0; i < 1; i++) {
            MatchFormat matchFormat = new FootballMatchFormat();
            ((FootballMatchFormat) matchFormat).setPenaltiesPossible(false);
            ((FootballMatchFormat) matchFormat).setExtraTimeMinutes(0);
            ((FootballMatchFormat) matchFormat).setMatchLevel(3);
            // this is a prematch
            algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat, 4);
            FootballMatchParams matchParams = (FootballMatchParams) publishedMatchParams;
            matchParams.setEventId(eventId);

            matchParams.setGoalTotal(4.96670052, 0.22);
            matchParams.setGoalSupremacy(-4.919, 0.22);
            matchParams.setHomeLoseBoost(0.54699695, 0.05);
            matchParams.setAwayLoseBoost(0.0, 0.05);

            algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
            algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFail());
            System.out.print("iteration no: " + i + 1);
            System.out.println(publishedParamFinderResults);
            System.out.println(publishedMatchParams);
            if (!publishedParamFinderResults.isSuccess())
                System.exit(0);
            algoManager.handleRemoveEvent(eventId);
        }
    }

    public void testBobProdPramFindOverFiveMinutes() {
        MatchFormat matchFormat = new FootballMatchFormat();
        ((FootballMatchFormat) matchFormat).setPenaltiesPossible(false);
        ((FootballMatchFormat) matchFormat).setExtraTimeMinutes(0);
        ((FootballMatchFormat) matchFormat).setMatchLevel(3);
        // this is a prematch
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat, 4);
        FootballMatchParams matchParams = (FootballMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailBob());
        System.out.print("iteration no: " + 1);
        System.out.println(publishedParamFinderResults);
        System.out.println(publishedMatchParams);
        if (!publishedParamFinderResults.isSuccess())
            System.exit(0);
        algoManager.handleRemoveEvent(eventId);
    }

    public void testPublicDemoParamFindOverFiveMinutes() {
        MatchFormat matchFormat = new FootballMatchFormat();
        ((FootballMatchFormat) matchFormat).setPenaltiesPossible(false);
        ((FootballMatchFormat) matchFormat).setExtraTimeMinutes(0);
        ((FootballMatchFormat) matchFormat).setMatchLevel(3);
        // this is a prematch
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat, 3);
        FootballMatchParams matchParams = (FootballMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);

        matchParams.setGoalTotal(4.885, 0.22);
        matchParams.setGoalSupremacy(-5.0, 0.22);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo());
        System.out.print("iteration no: " + 1);
        System.out.println(publishedParamFinderResults);
        System.out.println(publishedMatchParams);
        if (!publishedParamFinderResults.isSuccess())
            System.exit(0);
        algoManager.handleRemoveEvent(eventId);
    }

    private MarketPricesList getTestMarketPricesFail() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices BSports = new MarketPrices();

        BSports.setSourceWeight(1);
        marketPricesList.put("B365", BSports);

        MarketPrice b1 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        b1.put("A", 51.0);
        b1.put("B", 1.0);
        b1.put("Draw", 51.0);
        BSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "6.5");
        b2.put("Over", 5.0);
        b2.put("Under", 1.14);
        BSports.addMarketPrice(b2);

        return marketPricesList;

    }

    private MarketPricesList getTestMarketPricesFailBob() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices BSports = new MarketPrices();

        BSports.setSourceWeight(0.33);
        marketPricesList.put("B365", BSports);

        MarketPrice b1 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        b1.put("A", 26.0);
        b1.put("B", 1.03);
        b1.put("Draw", 15.0);
        BSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "5.5");
        b2.put("Over", 1.615);
        b2.put("Under", 2.2);
        BSports.addMarketPrice(b2);

        return marketPricesList;

    }

    private MarketPricesList getTestMarketPricesFailPublicDemo() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices BSports = new MarketPrices();

        BSports.setSourceWeight(0.33);
        marketPricesList.put("B365", BSports);

        MarketPrice b1 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        b1.put("A", 23.0);
        b1.put("B", 1.025);
        b1.put("Draw", 14.0);
        BSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.12);
        b2.put("Under", 6.25);
        BSports.addMarketPrice(b2);

        return marketPricesList;

    }

    public static void main(String[] args) {
        FootballParamFindProblemTest tester = new FootballParamFindProblemTest();
        tester.testFootballParamFindFail();
        // tester.testPublicDemoParamFindOverFiveMinutes();
        // tester.test1();
        System.out.println("finished");
        System.exit(0);
    }

}
