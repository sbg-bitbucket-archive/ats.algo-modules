package ats.algo.tradingruletests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedSpecs;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRule;
import ats.algo.core.traderalert.TraderAlert.TraderAlertType;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.ppb.tradingrules.PpbTennisMonitorFeedDelaySuspensionMethodHelper;
import ats.algo.sport.tennis.tradingrules.TennisMonitorFeedDelaySuspensionMethodHelper;

public class MonitorFeedTradingRuleTest extends AlgoManagerSimpleTestBase {

    @Test
    public void testNewMonitorFeedTradingRulesWithBetStop() {
        TradingRules tradingRules = new TradingRules();
        // delay the suspension for xx seconds
        PpbTennisMonitorFeedDelaySuspensionMethodHelper delaySuspensionMethod =
                        new PpbTennisMonitorFeedDelaySuspensionMethodHelper.Builder().medicalTimeout(0, 19000, true)
                                        .build();
        tradingRules.addRule(new MonitorFeedTradingRule(0, 0, (now, matchState) -> delaySuspensionMethod
                        .monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;

        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchState matchState =
                        (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
        matchState.getSuspensionStatus().setCacheSuspensionStatus(true);
        algoManager.getEventDetails(eventId).getEventState().setMatchState(matchState);

        assertEquals(((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState())
                        .getSuspensionStatus().isCacheSuspensionStatus(), true);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        assertEquals(this.publishedEventLevelShouldSuspend, null);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        long time = System.currentTimeMillis();
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setIncidentId("START");
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        assertEquals(this.publishedEventLevelShouldSuspend, null);
        this.publishedEventLevelShouldSuspend = false;

        TennisMatchIncident incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("15-0");
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        assertEquals(this.publishedEventLevelShouldSuspend, false);

        incident = new TennisMatchIncident(2, TennisMatchIncidentType.MEDICAL_TIMEOUT, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("MED");
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        System.out.println("Suspend due to trading rules - no delay before suspension");
        Sleep.sleep(2);
        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);
        System.out.println("5 seconds has elapsed - new incident");

        long time1 = System.currentTimeMillis();
        int elapsedTime = Math.toIntExact(time1 - time);
        DatafeedMatchIncident dataFeedIncident =
                        new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_STOP, elapsedTime);
        dataFeedIncident.setEventId(eventId);
        dataFeedIncident.setIncidentId("STOP");
        algoManager.handleMatchIncident(dataFeedIncident, true);
        dataFeedIncident = null;

        Sleep.sleep(2);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(3);
        long time2 = System.currentTimeMillis();
        elapsedTime = Math.toIntExact(time2 - time);
        dataFeedIncident = new DatafeedMatchIncident(DatafeedMatchIncidentType.BET_START, elapsedTime);
        dataFeedIncident.setEventId(eventId);
        dataFeedIncident.setIncidentId("START");
        algoManager.handleMatchIncident(dataFeedIncident, true);
        dataFeedIncident = null;
        Sleep.sleep(2);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, false);

    }

    @Test
    public void testNewMonitorFeedTradingRulesWithUndo() {
        TradingRules tradingRules = new TradingRules();
        // delay the suspension for xx seconds
        PpbTennisMonitorFeedDelaySuspensionMethodHelper delaySuspensionMethod =
                        new PpbTennisMonitorFeedDelaySuspensionMethodHelper.Builder().medicalTimeout(0, 20000, true)
                                        .build();
        tradingRules.addRule(new MonitorFeedTradingRule(0, 0, (now, matchState) -> delaySuspensionMethod
                        .monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;

        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchState matchState =
                        (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
        matchState.getSuspensionStatus().setCacheSuspensionStatus(true);
        algoManager.getEventDetails(eventId).getEventState().setMatchState(matchState);

        assertEquals(((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState())
                        .getSuspensionStatus().isCacheSuspensionStatus(), true);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        assertEquals(this.publishedEventLevelShouldSuspend, null);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setIncidentId("START");
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        assertEquals(this.publishedEventLevelShouldSuspend, null);
        this.publishedEventLevelShouldSuspend = false;

        TennisMatchIncident incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("15-0");
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        assertEquals(this.publishedEventLevelShouldSuspend, false);

        incident = new TennisMatchIncident(2, TennisMatchIncidentType.MEDICAL_TIMEOUT, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("MED");
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        System.out.println("Suspend due to trading rules - no delay before suspension");
        Sleep.sleep(2);
        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);
        System.out.println("5 seconds has elapsed - new incident");

        incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("30-0");
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(3);
        incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        startingTennisMatchIncident.setIncidentId("30-0");
        startingTennisMatchIncident.setUndo(true);
        algoManager.handleUndoMatchIncident(eventId, incident);
        incident = null;

        Sleep.sleep(2);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, false);

    }

    @Test
    public void testNewMonitorFeedTradingRules() {
        TradingRules tradingRules = new TradingRules();
        // delay the suspension for xx seconds
        PpbTennisMonitorFeedDelaySuspensionMethodHelper delaySuspensionMethod =
                        new PpbTennisMonitorFeedDelaySuspensionMethodHelper.Builder().medicalTimeout(0, 20000, true)
                                        .build();
        tradingRules.addRule(new MonitorFeedTradingRule(0, 0, (now, matchState) -> delaySuspensionMethod
                        .monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);

        algoManager.autoSyncWithMatchFeed(false);
        algoManager.onlyPublishMarketsFollowingParamChange(false);

        algoManager.transitionToInplayAlert(true);
        long eventId = 12345L;

        TennisMatchFormat tennisMatchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF,
                        3, FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);

        TennisMatchState matchState =
                        (TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState();
        matchState.getSuspensionStatus().setCacheSuspensionStatus(true);
        algoManager.getEventDetails(eventId).getEventState().setMatchState(matchState);

        assertEquals(((TennisMatchState) algoManager.getEventDetails(eventId).getEventState().getMatchState())
                        .getSuspensionStatus().isCacheSuspensionStatus(), true);

        TennisMatchParams matchParams = new TennisMatchParams();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        assertEquals(this.publishedEventLevelShouldSuspend, null);

        TennisMatchIncident startingTennisMatchIncident = new TennisMatchIncident();
        startingTennisMatchIncident.setElapsedTime(0);
        startingTennisMatchIncident.setIncidentSubType(TennisMatchIncidentType.MATCH_STARTING);
        startingTennisMatchIncident.setServerSideAtStartOfMatch(TeamId.A);
        startingTennisMatchIncident.setServerPlayerAtStartOfMatch(1);
        startingTennisMatchIncident.setPointResult(TennisPointResult.UNKNOWN);
        startingTennisMatchIncident.setEventId(eventId);

        algoManager.handleMatchIncident(startingTennisMatchIncident, true);

        assertEquals(this.publishedEventLevelShouldSuspend, null);
        this.publishedEventLevelShouldSuspend = false;

        TennisMatchIncident incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        assertEquals(this.publishedEventLevelShouldSuspend, false);

        incident = new TennisMatchIncident(2, TennisMatchIncidentType.MEDICAL_TIMEOUT, TeamId.A);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        System.out.println("Suspend due to trading rules - no delay before suspension");
        Sleep.sleep(2);
        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);
        System.out.println("5 seconds has elapsed - new incident");

        incident = new TennisMatchIncident(1, TennisMatchIncidentType.POINT_WON, TeamId.A);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        incident = null;

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, true);

        Sleep.sleep(5);

        assertEquals(this.publishedEventLevelShouldSuspend, false);

    }

    @Test
    public void testFootballPricingFeedStatus() {
        TradingRules tradingRules = new TradingRules();
        // set rule 4 secs in soccer

        MonitorFeedSpecs[] incidentFeedsUpdateTimeOut = new MonitorFeedSpecs[1];
        MonitorFeedSpecs[] pricingFeedsUpdateTimeOut = new MonitorFeedSpecs[2]; // 3mins 5 mins
        pricingFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(20, true, TraderAlertType.INPUT_PRICES_MISSING_WARNING);
        pricingFeedsUpdateTimeOut[1] = new MonitorFeedSpecs(30, false, TraderAlertType.INPUT_PRICES_MISSING_DANGER);
        incidentFeedsUpdateTimeOut[0] = new MonitorFeedSpecs(40, false, TraderAlertType.INPUT_INCIDENT_MISSING_DANGER);
        MonitorFeedTradingRule monitorFeedTradingRule =
                        new MonitorFeedTradingRule(incidentFeedsUpdateTimeOut, pricingFeedsUpdateTimeOut, null);

        tradingRules.addRule(monitorFeedTradingRule);
        algoManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        ElapsedTimeMatchIncident footballMatchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        footballMatchIncident1.setEventId(eventId);

        algoManager.handleMatchIncident(footballMatchIncident1, true);

        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo());

        FootballMatchParams footballMatchParams = new FootballMatchParams();

        GenericMatchParams genericMatchParams = footballMatchParams.generateGenericMatchParams();

        algoManager.handleSetMatchParams(genericMatchParams);

        Sleep.sleep(22);
        assertTrue(traderAlert != null);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.INPUT_PRICES_MISSING_WARNING));
        traderAlert = null;

        ElapsedTimeMatchIncident footballMatchIncident2 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 300);
        footballMatchIncident2.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident2, true);

        Sleep.sleep(12);
        assertTrue(traderAlert != null);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.INPUT_PRICES_MISSING_DANGER));
        traderAlert = null;
        assertTrue(publishedEventLevelShouldSuspend);

        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());

        Sleep.sleep(7);
        assertFalse(publishedEventLevelShouldSuspend);
        assertTrue(traderAlert == null);
        Sleep.sleep(17);

        ElapsedTimeMatchIncident footballMatchIncident3 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 500);
        footballMatchIncident3.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident3, true);

        System.out.println(traderAlert);
        assertTrue(traderAlert != null);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.INPUT_PRICES_MISSING_WARNING));
        traderAlert = null;
        Sleep.sleep(12);
        assertTrue(traderAlert != null);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.INPUT_PRICES_MISSING_DANGER));
        traderAlert = null;

        assertTrue(publishedEventLevelShouldSuspend);

        Sleep.sleep(3);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        Sleep.sleep(7);
        traderAlert = null;
        ElapsedTimeMatchIncident footballMatchIncident4 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, 500);
        footballMatchIncident4.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident4, true);
        assertFalse(publishedEventLevelShouldSuspend);

        Sleep.sleep(11);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        Sleep.sleep(11);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        Sleep.sleep(11);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        Sleep.sleep(11);
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFailPublicDemo2());
        assertTrue(traderAlert != null);
        assertTrue(traderAlert.getTraderAlertType().equals(TraderAlertType.INPUT_INCIDENT_MISSING_DANGER));
        assertTrue(publishedEventLevelShouldSuspend);
    }

    private MarketPricesList getTestMarketPricesFailPublicDemo() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices BSports = new MarketPrices();

        BSports.setSourceWeight(0.33);
        marketPricesList.put("B365", BSports);

        MarketPrice b1 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        b1.put("A", 2.026);
        b1.put("B", 2.843);
        b1.put("Draw", 3.939);
        BSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "3.5");
        b2.put("Over", 2.170);
        b2.put("Under", 1.642);
        BSports.addMarketPrice(b2);

        return marketPricesList;

    }

    private MarketPricesList getTestMarketPricesFailPublicDemo2() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices BSports = new MarketPrices();

        BSports.setSourceWeight(0.33);
        marketPricesList.put("B365", BSports);

        MarketPrice b1 = new MarketPrice("FT:AXB", "Match Result", MarketCategory.GENERAL, null);
        b1.put("A", 2.025);
        b1.put("B", 2.843);
        b1.put("Draw", 3.939);
        BSports.addMarketPrice(b1);

        MarketPrice b2 = new MarketPrice("FT:OU", "Total Goals", MarketCategory.OVUN, "3.5");
        b2.put("Over", 2.170);
        b2.put("Under", 1.642);
        BSports.addMarketPrice(b2);

        return marketPricesList;

    }



    @Test
    public void testRainMonitoring() {
        TradingRules tradingRules = new TradingRules();
        // delay the suspension for xx seconds
        TennisMonitorFeedDelaySuspensionMethodHelper delaySuspensionMethod =
                        new TennisMonitorFeedDelaySuspensionMethodHelper.Builder().rain(0, 10000 * 6 * 60 * 24, false)
                                        .build();
        tradingRules.addRule(new MonitorFeedTradingRule(0, 0, (now, matchState) -> delaySuspensionMethod
                        .monitorFeedTradingRuleSuspensionMethod(now, matchState)));

        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        assertTrue(publishedEventLevelShouldSuspend == null);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        publishedEventLevelShouldSuspend = null;
        incident = new TennisMatchIncident(0, TennisMatchIncidentType.RAIN, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        assertTrue(publishedEventLevelShouldSuspend == null); // equals to the false status
        Sleep.sleep(7);
        assertTrue(publishedEventLevelShouldSuspend == true);
        // Sleep.sleep(19);
        // assertTrue(publishedEventLevelShouldSuspend);
        // moving the game forward

        incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertTrue(publishedEventLevelShouldSuspend == false);


    }


    @Test
    @Ignore
    public void testIncidentMonitoring() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(new MonitorFeedTradingRule(4, 0));
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        assertTrue(publishedEventLevelShouldSuspend == null);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend);
        publishedEventLevelShouldSuspend = null;
        incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertFalse(publishedEventLevelShouldSuspend);
        Sleep.sleep(4);
        assertTrue(publishedEventLevelShouldSuspend);
    }

    @Test
    @Ignore
    public void testPriceMonitoring() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(new MonitorFeedTradingRule(0, 6));
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        Sleep.sleep(2);
        assertTrue(publishedEventLevelShouldSuspend == null);
        MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertTrue(publishedEventLevelShouldSuspend == null);
        Sleep.sleep(7);
        assertTrue(publishedEventLevelShouldSuspend);
        /*
         * the first supplyMarketPrices triggers a real param find so dont run tests until the second one which should
         * not trigger a pf
         */
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices());
        publishedEventLevelShouldSuspend = null;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPrices());
        Sleep.sleep(2);
        assertFalse(publishedEventLevelShouldSuspend);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend);
    }

    @Test
    @Ignore
    public void testHandleIncidentFeedStatus() {
        TradingRules tradingRules = new TradingRules();
        tradingRules.addRule(new MonitorFeedTradingRule(4, 0));
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        MatchIncident incident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        algoManager.handleIncidentFeedStatus(eventId, "FEED_ID", false);
        assertTrue(publishedEventLevelShouldSuspend);
        publishedEventLevelShouldSuspend = null;
        incident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, TeamId.A, 1);
        incident.setEventId(eventId);
        algoManager.handleMatchIncident(incident, true);
        Sleep.sleep(2);
        assertFalse(publishedEventLevelShouldSuspend);
        Sleep.sleep(4);
        assertTrue(publishedEventLevelShouldSuspend);
    }

    @Test
    @Ignore
    public void testHalfTimeFootballFeedStatus() {
        TradingRules tradingRules = new TradingRules();
        // set rule 4 secs in soccer
        tradingRules.addRule(new MonitorFeedTradingRule(4, 0));
        algoManager.setTradingRules(SupportedSportType.SOCCER, tradingRules);
        FootballMatchFormat matchFormat = new FootballMatchFormat();
        long eventId = 11L;
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, matchFormat);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend == null);
        ElapsedTimeMatchIncident footballMatchIncident1 =
                        new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        footballMatchIncident1.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident1, true);
        algoManager.handleIncidentFeedStatus(eventId, "FEED_ID", false);
        assertTrue(publishedEventLevelShouldSuspend);
        // in half time will not suspend Reason half time
        footballMatchIncident1 = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_END, 0);
        footballMatchIncident1.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident1, true);
        Sleep.sleep(6);
        assertFalse(publishedEventLevelShouldSuspend);
        // in second half time will suspend Reason after 4
        footballMatchIncident1 = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        footballMatchIncident1.setEventId(eventId);
        algoManager.handleMatchIncident(footballMatchIncident1, true);
        Sleep.sleep(6);
        assertTrue(publishedEventLevelShouldSuspend);

    }



    private MarketPricesList getTestMarketPrices() {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(1);

        MarketPrice ab = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        ab.put("A", 1.86);
        ab.put("B", 1.88);
        m.addMarketPrice(ab);

        MarketPrice tmtg = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "22.5");
        tmtg.put("Over", 1.82);
        tmtg.put("Under", 1.92);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("TestSource1", m);
        return marketPricesList;
    }
}
