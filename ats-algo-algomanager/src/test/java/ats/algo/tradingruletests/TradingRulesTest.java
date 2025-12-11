package ats.algo.tradingruletests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.TraderParamFindResultsDescription;
import ats.algo.core.comparetomarket.TraderParamFindResultsDetailRow;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.MarketStatus;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.core.tradingrules.SetSuspensionStatusTradingRule;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.football.baba.tradingrules.BabaFootballTradingRules;
import ats.algo.sport.football.lc.tradingrules.LcFootballTradingRules;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.tradingrules.TennisTradingRule;
import ats.algo.sport.tennis.tradingrules.TennisTradingRules;

/*
 * tests functionality using the single threaded SimpleAlgoManagerConfiguration
 */
public class TradingRulesTest extends AlgoManagerSimpleTestBase {


    @Test
    public void testNewSuspendStatus() {
        Market market = new Market();
        SuspensionStatus suspensionStatus = SuspensionStatus.ACTIVE_UNDISPLAY;
        MarketStatus marketStatus = new MarketStatus();
        marketStatus.setSuspensionStatus(suspensionStatus);
        market.setMarketStatus(marketStatus);
        assertTrue(market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.ACTIVE_UNDISPLAY));
    }

    @Test
    public void testIgnoreBookiePricingInput() {

        TennisTradingRules tradingRules = new TennisTradingRules();
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);

        long eventId = 123;

        TennisMatchFormat matchFormat = new TennisMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, matchFormat);

        TennisMatchParams tennisMatchParams = new TennisMatchParams();

        tennisMatchParams.getOnServePctA1().setMean(60.0);

        GenericMatchParams genericMatchParams = tennisMatchParams.generateGenericMatchParams();

        algoManager.handleSetMatchParams(genericMatchParams);

        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesforTennis());
        System.out.println(result);
        assertTrue(result.isParamFindScheduled());
        result = null;

        System.out.println(this.publishedMarkets);

        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("ignoreBookiePrices", "true");
        System.out.println(algoManager.getEventDetails(eventId).getEventSettings());
        System.out.println(algoManager.getEventDetails(eventId).getEventSettings().toString());
        algoManager.handleSetEventProperties(eventId, properties);

        assertEquals(algoManager.getEventDetails(eventId).getEventSettings().isIgnoreBookiePrices(), true);

        algoManager.handleSetMatchParams(genericMatchParams);

        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesforTennis());
        System.out.println(result.isParamFindScheduled());
        assertFalse(result.isParamFindScheduled());
        sleep(60);
    }

    @Test
    public void testMarketGroupSuspensionForParamFindPrematch() {

        BabaFootballTradingRules tradingRules = new BabaFootballTradingRules(true);
        algoManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);

        long eventId = 123;

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);

        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        assertTrue(algoManager.getEventDetails(eventId).getEventSettings().getEventTier() == 1);

        MarketPricesList cornersIncludedMarketPricesList = getTestMarketPricesforFootballIncludingCorners(true);
        algoManager.handleSupplyMarketPrices(eventId, cornersIncludedMarketPricesList);

        Market market = null;
        for (Market cornerMarket : this.publishedMarkets.getMarkets().values()) {
            if (cornerMarket.getType().equals("FT:COU") && cornerMarket.getLineId().equals("9.5")) {
                market = cornerMarket;
            }
        }
        System.out.println(market);
        assertTrue(market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.OPEN));
        market = null;

        algoManager.handleClearParamFindData(eventId);

        FootballMatchParams footballMatchParams = new FootballMatchParams();
        GenericMatchParams genericMatchParams = footballMatchParams.generateGenericMatchParams();
        genericMatchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(genericMatchParams);

        for (Market cornerMarket : this.publishedMarkets.getMarkets().values()) {
            if (cornerMarket.getType().equals("FT:COU") && cornerMarket.getLineId().equals("9.5")) {
                market = cornerMarket;
            }
        }
        System.out.println(market);
        assertTrue(market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.OPEN));
        market = null;

        cornersIncludedMarketPricesList = null;
        cornersIncludedMarketPricesList = getTestMarketPricesforFootballIncludingCorners(false);
        publishedMarkets = null;
        algoManager.handleSupplyMarketPrices(eventId, cornersIncludedMarketPricesList);

        for (Market cornerMarket : this.publishedMarkets.getMarkets().values()) {
            if (cornerMarket.getType().equals("FT:COU") && cornerMarket.getLineId().equals("9.5")) {
                market = cornerMarket;
            }
        }
        System.out.println(market);
        assertTrue(market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.SUSPENDED_UNDISPLAY));
        market = null;

        assertFalse(this.publishedParamFinderResults.getParamFindResultsDescription().getResultDetail()
                        .contains("FT:COU"));

        algoManager.handleClearParamFindData(eventId);

        algoManager.handleSetMatchParams(genericMatchParams);

        cornersIncludedMarketPricesList = null;
        cornersIncludedMarketPricesList = getTestMarketPricesforFootballIncludingCorners(true);
        algoManager.handleSupplyMarketPrices(eventId, cornersIncludedMarketPricesList);

        market = this.publishedMarkets.getMarketForLineId("FT:COU", "M", "9.5");
        System.out.println(market);
        assertTrue(market.getMarketStatus().getSuspensionStatus().equals(SuspensionStatus.OPEN));
        market = null;

    }

    @Test
    @Ignore // Ignoring due to baba not creating any core markets
    public void testFootballTradingRulesForBaba() {

        BabaFootballTradingRules tradingRules = new BabaFootballTradingRules(true);
        algoManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);

        long eventId = 123;

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);

        MarketPricesList marketPricesList = getTestMarketPricesforFootballIncludingCorners(true);
        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        assertTrue(result.isParamFindScheduled());

        TraderParamFindResultsDescription results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        System.out.println(this.publishedParamFinderResults);
        Set<String> marketsAccepted = new HashSet<String>();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            System.out.println(string);
            if (!(marketsAccepted.contains(string.getMarketType()))) {
                marketsAccepted.add(string.getMarketType());
            }
        }
        System.out.println(marketsAccepted);
        assertFalse(marketsAccepted.contains("FT:COU"));
        assertFalse(marketsAccepted.contains("FT:CHCP"));

        Map<String, String> properties1 = new HashMap<String, String>(1);
        properties1.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties1);

        algoManager.getEventDetails(eventId).clearTriggerParamFindData();
        algoManager.handleClearParamFindData(eventId);
        marketsAccepted.clear();
        result = null;

        FootballMatchParams footballMatchParams = new FootballMatchParams();
        GenericMatchParams genericMatchParams = footballMatchParams.generateGenericMatchParams();
        genericMatchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(genericMatchParams);

        MarketPricesList marketPricesList1 = getTestMarketPricesforFootballIncludingCorners(true);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList1);

        assertTrue(result.isParamFindScheduled());

        results = null;

        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            System.out.println(string);
            if (!(marketsAccepted.contains(string.getMarketType()))) {
                marketsAccepted.add(string.getMarketType());
            }
        }
        System.out.println(marketsAccepted);
        boolean cornersOver = false;
        boolean cornersSpread = false;

        for (String market : marketsAccepted) {
            if (market.contains("FT:COU")) {
                cornersOver = true;
            }
            if (market.contains("FT:CHCP")) {
                cornersSpread = true;
            }
        }

        assertTrue(cornersOver);
        assertTrue(cornersSpread);
    }

    @Test
    public void testFootballTradingRulesForLc() {
        System.setProperty("paramFindTimeOverride", "true");
        System.setProperty("pricesChangeOverride", "true");

        LcFootballTradingRules tradingRules = new LcFootballTradingRules();

        algoManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);

        long eventId = 123;

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);

        MarketPricesList marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.0);
        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        assertTrue(result.isParamFindScheduled());
        marketPricesList = null;
        result = null;

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());
        Sleep.sleep(10);
        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertFalse(result.isParamFindScheduled());

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.00);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());

        int incidentCount = 1;

        ElapsedTimeMatchIncident matchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchIncident1.setEventId(eventId);
        matchIncident1.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident1, true);
        incidentCount++;

        sleep(2);
        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());
        sleep(2);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertFalse(result.isParamFindScheduled());
        sleep(2);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.00);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());
        sleep(5);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertFalse(result.isParamFindScheduled());
        sleep(5);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertFalse(result.isParamFindScheduled());
        sleep(2);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertFalse(result.isParamFindScheduled());
        sleep(2);

        marketPricesList = getTestMarketPricesforFootballBumpedPrices(0.01);
        result = algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
        System.out.println(result.getActionReason());
        assertTrue(result.isParamFindScheduled());
        sleep(1);


    }

    @Ignore
    public void testFootballTradingRulesForDemo2() {

        long eventId = 123;

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);

        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "2");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        MarketPricesList marketPricesList = getTestMarketPricesforFootball();
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        /*
         * Start match
         */

        int incidentCount = 1;

        ElapsedTimeMatchIncident matchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchIncident1.setEventId(eventId);
        matchIncident1.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident1, true);
        incidentCount++;

        // sleep(1);

        MarketStatus marketStatus = publishedMarkets.get("FT:AXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

        /*
         * Test goal incident. Insert goal, check event is suspended. Wait 10 seconds, insert clock incident. Check
         * still suspended.
         * 
         * Will then trigger param find. Should open.
         */

        FootballMatchIncident matchIncident2 = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 15, TeamId.A);
        matchIncident2.setEventId(eventId);
        matchIncident2.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident2, true);
        incidentCount++;

        assertTrue(this.publishedEventLevelShouldSuspend);

        sleep(10);

        assertTrue(this.publishedEventLevelShouldSuspend);

        ElapsedTimeMatchIncident matchIncident3 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 300);
        matchIncident3.setEventId(eventId);
        matchIncident3.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident3, true);
        incidentCount++;

        assertTrue(this.publishedEventLevelShouldSuspend);

        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        assertFalse(this.publishedEventLevelShouldSuspend);
    }

    @Test
    @Ignore
    public void testFootballTradingRulesForDemo() {

        long eventId = 123;

        FootballMatchFormat matchFormat = new FootballMatchFormat();
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);

        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "2");
        algoManager.handleSetEventProperties(eventId, properties);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        MarketPricesList marketPricesList = getTestMarketPricesforFootball();
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        /*
         * Start match
         */

        int incidentCount = 1;

        ElapsedTimeMatchIncident matchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        matchIncident1.setEventId(eventId);
        matchIncident1.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident1, true);
        incidentCount++;

        sleep(1);

        MarketStatus marketStatus = publishedMarkets.get("FT:AXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

        /*
         * Test goal incident. Insert goal, check it is suspended. Wait 60 seconds, check still suspended without
         * another price update. wait another 65 seconds, timer should have gone (120 seconds in first 40 mins), markets
         * should be open.
         */
        this.publishedEventLevelShouldSuspend = false;
        FootballMatchIncident matchIncident2 = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 15, TeamId.A);
        matchIncident2.setEventId(eventId);
        matchIncident2.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident2, true);
        assertTrue(this.publishedEventLevelShouldSuspend);

        incidentCount++;

        assertTrue(this.publishedEventLevelShouldSuspend);

        sleep(60);

        assertTrue(this.publishedEventLevelShouldSuspend);

        sleep(65);

        assertTrue(this.publishedEventLevelShouldSuspend);

        marketPricesList = getTestMarketPricesforFootballGoal();
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        assertFalse(this.publishedEventLevelShouldSuspend);

        sleep(1);

        /*
         * Insert corner. Check that corner is suspended. Wait 30 seconds, check FT:CAXB is suspended.
         * 
         * Wait another 95 seconds, check that it has now opened.
         */

        FootballMatchIncident matchIncident3 =
                        new FootballMatchIncident(FootballMatchIncidentType.CORNER, 240, TeamId.A);
        matchIncident3.setEventId(eventId);
        matchIncident3.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident3, true);
        incidentCount++;

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, marketStatus.getSuspensionStatus());

        sleep(30);

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, marketStatus.getSuspensionStatus());

        sleep(30);

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, marketStatus.getSuspensionStatus());

        sleep(30);

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, marketStatus.getSuspensionStatus());

        sleep(40);

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

        /*
         * Setup for 89th minute suspension End half, start 2nd half and set clock to 88th minute. Do a param find, set
         * event tier to 5. Check that Corners are closed but match winner is open. 89th min surpasses, check
         * matchWinner suspend.
         */

        ElapsedTimeMatchIncident matchIncident4 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 2700);
        matchIncident4.setEventId(eventId);
        matchIncident4.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident4, true);
        incidentCount++;

        ElapsedTimeMatchIncident matchIncident5 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 2701);
        matchIncident5.setEventId(eventId);
        matchIncident5.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident5, true);
        incidentCount++;

        ElapsedTimeMatchIncident matchIncident6 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 5280);
        matchIncident6.setEventId(eventId);
        matchIncident6.setIncidentId("Inc" + incidentCount);
        algoManager.handleMatchIncident(matchIncident6, true);
        incidentCount++;

        marketPricesList = getTestMarketPricesforFootballLate();
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);

        Map<String, String> properties1 = new HashMap<String, String>(1);
        properties1.put("eventTier", "4");
        algoManager.handleSetEventProperties(eventId, properties1);

        sleep(1);

        marketStatus = publishedMarkets.get("FT:AXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

        marketStatus = publishedMarkets.get("FT:DBLC", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

        marketStatus = publishedMarkets.get("FT:CAXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, marketStatus.getSuspensionStatus());

        sleep(75);

        System.out.println(this.publishedMatchState);

        // ElapsedTimeMatchIncident matchIncident7 = new
        // ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK,
        // 5350);
        // matchIncident7.setEventId(eventId);
        // matchIncident7.setRequestId("Inc" + incidentCount);
        // algoManager.handleMatchIncident(matchIncident7, true);
        // incidentCount++;

        marketStatus = publishedMarkets.get("FT:DBLC", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, marketStatus.getSuspensionStatus());

        marketStatus = publishedMarkets.get("FT:AXB", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_DISPLAY, marketStatus.getSuspensionStatus());
    }

    @Test
    public void test() {
        /*
         * check the default rules first
         */
        TradingRules baseTradingRules = new TennisTradingRules();
        algoManager.setTradingRules(SupportedSportType.TENNIS, baseTradingRules);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);

        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 11L, tennisMatchFormat, 2);
        TennisMatchParams matchParams = (TennisMatchParams) publishedMatchParams;
        matchParams.setEventId(11L);
        matchParams.getOnServePctA1().setProperties(.593, .05, 0);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        MarketStatus marketStatus = publishedMarkets.get("FT:ML", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());
        marketStatus = publishedMarkets.get("FT:CS", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, marketStatus.getSuspensionStatus());
        marketStatus = publishedMarkets.get("FT:NUMSET", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, marketStatus.getSuspensionStatus());
        /*
         * change the rules
         */
        SetSuspensionStatusTradingRule[] tradingRules = new SetSuspensionStatusTradingRule[1];
        TennisTradingRule tradingRule = new TennisTradingRule("Unit test trading rule", 2, null);
        List<String> testPreMatchOpenMarketList = Arrays.asList("FT:ML", "FT:NUMSET");
        tradingRule.setPrematchDisplayMarketList(testPreMatchOpenMarketList);
        tradingRule.setRuleForDoublesMatch(false);
        tradingRules[0] = tradingRule;
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, 12L, tennisMatchFormat, 2);
        matchParams.setEventId(12L);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        marketStatus = publishedMarkets.get("FT:ML", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());
        marketStatus = publishedMarkets.get("FT:CS", "M").getMarketStatus();
        assertEquals(SuspensionStatus.SUSPENDED_UNDISPLAY, marketStatus.getSuspensionStatus());
        marketStatus = publishedMarkets.get("FT:NUMSET", "M").getMarketStatus();
        assertEquals(SuspensionStatus.OPEN, marketStatus.getSuspensionStatus());

    }

    private MarketPricesList getTestMarketPricesforFootball() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.33);
        b1.put("B", 7.00);
        b1.put("Draw", 4.50);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.35);
        b2.put("Under", 3.10);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforTennis() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match Betting", MarketCategory.GENERAL, null);
        b1.put("A", 1.83);
        b1.put("B", 1.83);
        bet365.addMarketPrice(b1);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforFootballGoal() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.32);
        b1.put("B", 7.01);
        b1.put("Draw", 4.51);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.35);
        b2.put("Under", 3.10);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforFootballLate() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.32);
        b1.put("B", 7.01);
        b1.put("Draw", 4.51);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 3.10);
        b2.put("Under", 1.35);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforFootballIncludingCorners(boolean includeCornersOver) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.33);
        b1.put("B", 7.00);
        b1.put("X", 4.50);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.35);
        b2.put("Under", 3.10);
        bet365.addMarketPrice(b2);
        if (includeCornersOver) {

            MarketPrice b3 = new MarketPrice("FT:COU", "Total corners", MarketCategory.OVUN, "12.5");
            b3.put("Over", 1.9);
            b3.put("Under", 1.9);
            bet365.addMarketPrice(b3);

        }
        MarketPrice b4 = new MarketPrice("FT:CHCP", "Total corners", MarketCategory.HCAP, "1.5");
        b4.put("AH", 1.9);
        b4.put("BH", 1.9);
        bet365.addMarketPrice(b4);
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesforFootballBumpedPrices(double amountToBump) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:AXB", "Match result", MarketCategory.GENERAL, null);
        b1.put("A", 1.33 + amountToBump);
        b1.put("B", 7.00);
        b1.put("X", 4.50);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total goals", MarketCategory.OVUN, "2.5");
        b2.put("Over", 1.35);
        b2.put("Under", 3.10);
        bet365.addMarketPrice(b2);
        return marketPricesList;
    }

    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams.generateXxxMatchParams();
        printResults("matchParams", matchParams);
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
        printResults("matchState", matchState);
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        printResults("markets", markets);
    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
        printResults("resultedMarkets", resultedMarkets);
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFinderResults,
                    GenericMatchParams matchParams, long elapsedTimeMs) {
        this.publishedParamFinderResults = paramFinderResults;
        printResults("paramFinderResults", paramFinderResults);
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {}

    private void printResults(String resultsDescription, Object results) {
        /*
         * System.out.printf("Published %s for event: %s \n", resultsDescription); System.out.print(results.toString());
         * System.out.printf("--- Published %s ends ---\n\n", resultsDescription);
         */
    }

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    private static void sleep(int nSecs) {
        for (int i = 0; i < nSecs; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Waiting... %d secs (of %d)\n", i + 1, nSecs);
        }
    }

}
