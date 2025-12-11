package ats.algo.sport.football;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;

public class FootballParamFindPreProcessorTest {

    @Before
    public void before() {
        // LogUtil.initConsoleLogging(Level.INFO);
    }

    @Test
    public void testGoalsWithoutAhcpMarket() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", false);
        assertEquals(FootballMatchParams.GOAL_TOTAL_DEFAULT_VALUE, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE, matchParams.getGoalSupremacy().getMean(),
                        0.0001);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE, matchParams.getGoalSupremacy().getMean(),
                        0.0001);
    }

    @Test
    public void testGoalsInPlay() {
        MethodName.log();
        /*
         * goals params should not be affected in play
         */
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchEngine.getMatchState().updateStateForIncident(incident, false);
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", false);
        assertEquals(FootballMatchParams.GOAL_TOTAL_DEFAULT_VALUE, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE, matchParams.getGoalSupremacy().getMean(),
                        0.0001);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(FootballMatchParams.GOAL_TOTAL_DEFAULT_VALUE, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE, matchParams.getGoalSupremacy().getMean(),
                        0.0001);
    }

    @Test
    public void testGoalsWithAhcpMarket() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        assertEquals(FootballMatchParams.GOAL_TOTAL_DEFAULT_VALUE, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(FootballMatchParams.GOAL_SUPREMACY_DEFAULT_VALUE, matchParams.getGoalSupremacy().getMean(),
                        0.0001);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);
    }

    @Test
    public void testCornersPreMatch() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForCorners1("BET365", null);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
    }

    /*
     * Robert card test start
     */
    @Test
    public void testCardrsPreMatch() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForCards1("BET365", null);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
        assertEquals(4.3, matchParams.getCardTotal().getMean(), 0.1);
        assertEquals(-0.6, matchParams.getCardSupremacy().getMean(), 1);
    }

    @Test
    public void testCardsInPlay() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        AlgoMatchState matchState = matchEngine.getMatchState();
        matchState.updateStateForIncident(incident, false);
        FootballMatchIncident incident2 = new FootballMatchIncident(FootballMatchIncidentType.CORNER, 10, TeamId.A);
        for (int i = 0; i < 5; i++)
            matchState.updateStateForIncident(incident2, false);
        FootballMatchIncident incident3 =
                        new FootballMatchIncident(FootballMatchIncidentType.YELLOW_CARD, 100, TeamId.A);
        for (int i = 0; i < 1; i++)
            matchState.updateStateForIncident(incident3, false);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForCardsInPlay("BET365");
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
        assertEquals(6.88, matchParams.getCardTotal().getMean(), 0.1);
        assertEquals(-0.43, matchParams.getCardSupremacy().getMean(), 0.1);
    }

    @Test
    public void testMissingCardMarket() {
        MethodName.log();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        marketPricesList = getTestMarketPricesForCorners1("BET365", marketPricesList);
        marketPricesList.getMarketPricesList().get("BET365").getMarketPrices().remove("FT:COU#8.00_M");
        assertEquals(4, marketPricesList.getMarketPricesList().get("BET365").getMarketPrices().size());
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(FootballMatchParams.CORNER_TOTAL_DEFAULT_VALUE, matchParams.getCornerTotal().getMean(), 0.1);
        assertEquals(FootballMatchParams.CORNER_SUPREMACY_DEFAULT_VALUE, matchParams.getCornerSupremacy().getMean(),
                        0.1);
        assertEquals(FootballMatchParams.CARD_TOTAL_DEFAULT_VALUE, matchParams.getCardTotal().getMean(), 0.1);
        assertEquals(FootballMatchParams.CARD_SUPREMACY_DEFAULT_VALUE, matchParams.getCardSupremacy().getMean(), 0.1);
    }


    @Test
    public void testCardAndCornersAndGoalsPreMatch() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        marketPricesList = getTestMarketPricesForCorners2("BET365", marketPricesList);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        // // System.out.println(matchParams.getCornerSupremacy().getMean());
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);

        assertEquals(5.2, matchParams.getCardTotal().getMean(), 0.1);
        assertEquals(-0.5, matchParams.getCardSupremacy().getMean(), 0.1);
        // matchEngine.setMatchParams(matchParams);
        // ParamFinder paramFinder = new ParamFinder(matchEngine);
        // paramFinder.setMatchParameters(matchParams);
        // paramFinder.setMarketPricesList(marketPricesList);
        // ParamFindResults results = paramFinder.calculate();
        // // System.out.println(results);
    }

    @Test
    public void testCornersInPlay() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        ElapsedTimeMatchIncident incident =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        AlgoMatchState matchState = matchEngine.getMatchState();
        matchState.updateStateForIncident(incident, false);
        FootballMatchIncident incident2 = new FootballMatchIncident(FootballMatchIncidentType.CORNER, 10, TeamId.A);
        for (int i = 0; i < 5; i++)
            matchState.updateStateForIncident(incident2, false);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForCornersInPlay("BET365");
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        // // System.out.println( matchParams.getCornerTotal().getMean());
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        // // System.out.println(matchParams.getCornerSupremacy().getMean());
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
    }

    @Test
    public void testCornersAndGoalsPreMatch() {
        MethodName.log();
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        marketPricesList = getTestMarketPricesForCorners1("BET365", marketPricesList);
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);

        assertEquals(11.0, matchParams.getCornerTotal().getMean(), 0.1);
        // // System.out.println(matchParams.getCornerSupremacy().getMean());
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
        // matchEngine.setMatchParams(matchParams);
        // ParamFinder paramFinder = new ParamFinder(matchEngine);
        // paramFinder.setMatchParameters(matchParams);
        // paramFinder.setMarketPricesList(marketPricesList);
        // ParamFindResults results = paramFinder.calculate();
        // // System.out.println(results);
    }



    @Test
    public void testMissingCornerMarket() {
        MethodName.log();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        marketPricesList = getTestMarketPricesForCorners1("BET365", marketPricesList);
        marketPricesList.getMarketPricesList().get("BET365").getMarketPrices().remove("FT:COU#8.00_M");
        assertEquals(4, marketPricesList.getMarketPricesList().get("BET365").getMarketPrices().size());
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(FootballMatchParams.CORNER_TOTAL_DEFAULT_VALUE, matchParams.getCornerTotal().getMean(), 0.1);
        assertEquals(FootballMatchParams.CORNER_SUPREMACY_DEFAULT_VALUE, matchParams.getCornerSupremacy().getMean(),
                        0.1);
    }

    @Test
    public void testMultipleSources() {
        MethodName.log();
        MarketPricesList marketPricesList = getTestMarketPricesForGoals("BET365", true);
        marketPricesList = getTestMarketPricesForCorners1("BET365", marketPricesList);
        marketPricesList.getMarketPricesList().get("BET365").getMarketPrices().remove("FT:COU#8.00_M");
        MarketPricesList marketPricesListSOURCE2 = getTestMarketPricesForGoals("SOURCE2", true);
        marketPricesListSOURCE2 = getTestMarketPricesForCorners1("SOURCE2", marketPricesListSOURCE2);
        /*
         * Fold the SOURCE2 market prices into the main list
         */
        marketPricesList.getMarketPricesList().put("SOURCE2",
                        marketPricesListSOURCE2.getMarketPricesList().get("SOURCE2"));
        /*
         * the SOURCE2 prices should be used so the values of the corners markets should change
         */
        FootballMatchEngine matchEngine = new FootballMatchEngine(new FootballMatchFormat(), true);
        FootballMatchParams matchParams = (FootballMatchParams) matchEngine.getMatchParams();
        FootballParamFindPreProcessor.process(matchEngine, matchParams, marketPricesList);
        assertEquals(4.5, matchParams.getGoalTotal().getMean(), 0.0001);
        assertEquals(-1.25, matchParams.getGoalSupremacy().getMean(), 0.0001);
        assertEquals(11, matchParams.getCornerTotal().getMean(), 0.1);
        // // System.out.println( matchParams.getCornerSupremacy().getMean());
        assertEquals(1, matchParams.getCornerSupremacy().getMean(), 0.1);
    }

    private MarketPricesList getTestMarketPricesForGoals(String source, boolean includeAhcp) {
        /*
         * prices equate to goalTotal = 4, goalSupremacy = -1
         * 
         */
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);
        MarketPrice axb = new MarketPrice("FT:AXB", "Match winner", MarketCategory.GENERAL, null);
        axb.put("A", 4.021);
        axb.put("B", 1.533);
        axb.put("Draw", 5.26);
        m.addMarketPrice(axb);
        MarketPrice ou = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "4.5");
        ou.put("Over", 1.620);
        ou.put("Under", 2.209);
        m.addMarketPrice(ou);

        if (includeAhcp) {
            MarketPrice ahcp = new MarketPrice("FT:AHCP", "Total goals", MarketCategory.OVUN, "1.25", "S0-0");
            ahcp.put("AH", 1.817);
            ahcp.put("BH", 1.925);
            m.addMarketPrice(ahcp);
        }

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put(source, m);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForCorners1(String source, MarketPricesList marketPricesListIn) {
        /*
         * prices equate to cornerTotal = 8.3, cornerSupremacy = -3.7
         * 
         */
        MarketPricesList marketPricesList;
        MarketPrices m;
        if (marketPricesListIn == null) {
            marketPricesList = new MarketPricesList();
            m = new MarketPrices();
            m.setSourceWeight(1);
            marketPricesList.put(source, m);
        } else {
            marketPricesList = marketPricesListIn;
            m = marketPricesList.get(source);
        }

        MarketPrice cou = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "8.00");
        cou.put("Over", 1.868);
        cou.put("Under", 1.871);
        m.addMarketPrice(cou);

        MarketPrice csprd = new MarketPrice("FT:CSPRD", "spread corners", MarketCategory.HCAP, "3.50");
        csprd.put("AH", 1.897);
        csprd.put("BH", 1.842);
        m.addMarketPrice(csprd);

        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForCorners2(String source, MarketPricesList marketPricesListIn) {
        /*
         * prices equate to cornerTotal = 8.3, cornerSupremacy = -3.7
         * 
         */
        MarketPricesList marketPricesList;
        MarketPrices m;
        if (marketPricesListIn == null) {
            marketPricesList = new MarketPricesList();
            m = new MarketPrices();
            m.setSourceWeight(1);
            marketPricesList.put(source, m);
        } else {
            marketPricesList = marketPricesListIn;
            m = marketPricesList.get(source);
        }

        MarketPrice cou = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "8.00");
        cou.put("Over", 1.868);
        cou.put("Under", 1.871);
        m.addMarketPrice(cou);

        MarketPrice csprd = new MarketPrice("FT:CSPRD", "spread corners", MarketCategory.HCAP, "3.50");
        csprd.put("AH", 1.897);
        csprd.put("BH", 1.842);
        m.addMarketPrice(csprd);

        MarketPrice bcou = new MarketPrice("FT:BCOU", "Total cards", MarketCategory.OVUN, "5.00");
        bcou.put("Over", 1.868);
        bcou.put("Under", 1.871);
        m.addMarketPrice(bcou);

        MarketPrice bcsprd = new MarketPrice("FT:BCSPRD", "Card spread corners", MarketCategory.HCAP, "0.50");
        bcsprd.put("AH", 1.897);
        bcsprd.put("BH", 1.842);
        m.addMarketPrice(bcsprd);

        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForCards1(String source, MarketPricesList marketPricesListIn) {
        /*
         * prices equate to totalBooking = 49.5, bookingSupremacy = -3.5
         * 
         */
        MarketPricesList marketPricesList;
        MarketPrices m;
        if (marketPricesListIn == null) {
            marketPricesList = new MarketPricesList();
            m = new MarketPrices();
            m.setSourceWeight(1);
            marketPricesList.put(source, m);
        } else {
            marketPricesList = marketPricesListIn;
            m = marketPricesList.get(source);
        }
        MarketPrice cou = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "8.00");
        cou.put("Over", 1.868);
        cou.put("Under", 1.871);
        m.addMarketPrice(cou);

        MarketPrice csprd = new MarketPrice("FT:CSPRD", "spread corners", MarketCategory.HCAP, "3.50");
        csprd.put("AH", 1.897);
        csprd.put("BH", 1.842);
        m.addMarketPrice(csprd);

        MarketPrice bou = new MarketPrice("FT:BCOU", "Total card", MarketCategory.OVUN, "4");
        bou.put("Over", 1.868);
        bou.put("Under", 1.871);
        m.addMarketPrice(bou);

        MarketPrice bsprd = new MarketPrice("FT:BCSPRD", "Card Handicap", MarketCategory.HCAP, "0.50");
        bsprd.put("AH", 1.897);
        bsprd.put("BH", 1.842);
        m.addMarketPrice(bsprd);

        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForCornersInPlay(String source) {
        /*
         * prices equate to in play, timeElapsed=10 secs, cornerTotal = 8.3, cornerSupremacy = -3.7, cornersA = 5,
         * cornersB = 0
         * 
         */
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice cou = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "12.5");
        cou.put("Over", 1.826);
        cou.put("Under", 1.915);
        m.addMarketPrice(cou);

        MarketPrice csprd = new MarketPrice("FT:CSPRD", "spread corners", MarketCategory.HCAP, "-1.50");
        csprd.put("AH", 1.824);
        csprd.put("BH", 1.916);
        m.addMarketPrice(csprd);

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put(source, m);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForCardsInPlay(String source) {
        /*
         * prices equate to in play, timeElapsed=10 secs, cornerTotal = 8.3, cornerSupremacy = -3.7, cornersA = 5,
         * cornersB = 0
         * 
         */
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice cou = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "12.5");
        cou.put("Over", 1.826);
        cou.put("Under", 1.915);
        m.addMarketPrice(cou);

        MarketPrice csprd = new MarketPrice("FT:CSPRD", "spread corners", MarketCategory.HCAP, "-1.50");
        csprd.put("AH", 1.824);
        csprd.put("BH", 1.916);
        m.addMarketPrice(csprd);

        MarketPrice bcou = new MarketPrice("FT:BCOU", "Total cards", MarketCategory.OVUN, "7.5");
        bcou.put("Over", 1.826);
        bcou.put("Under", 1.915);
        m.addMarketPrice(bcou);

        MarketPrice bcsprd = new MarketPrice("FT:BCSPRD", "Card handicap", MarketCategory.HCAP, "-0.5");
        bcsprd.put("AH", 1.824);
        bcsprd.put("BH", 1.916);
        m.addMarketPrice(bcsprd);

        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put(source, m);
        return marketPricesList;
    }

}
