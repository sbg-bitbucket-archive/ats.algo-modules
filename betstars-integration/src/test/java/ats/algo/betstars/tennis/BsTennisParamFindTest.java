package ats.algo.betstars.tennis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.betstars.algo.ats.integration.BsTennisMatchParams;

import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.betstars.BsSimpleAlgoManagerTestBase;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.common.TeamId;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.TraderParamFindResultsDescription;
import ats.algo.core.comparetomarket.TraderParamFindResultsDetailRow;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.tradingrules.TradingRules;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.bs.tradingrules.BsTennisTradingRules;

public class BsTennisParamFindTest extends BsSimpleAlgoManagerTestBase {

    @Test
    public void testParamFindForClosePrices() {

        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);

        publishedMarkets = null;
        publishedParamFinderResults = null;
        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesNear(false));
        assertTrue(publishedParamFinderResults.isSuccess());
        assertEquals(0.0, publishedParamFinderResults.getFunctionValueAtMinimum(), 0.02);
        assertEquals(0.50, publishedMarkets.get("FT:ML").get("A"), 0.02);


    }

    @Test
    public void testAutoControlWithPrematchWithTopBookieHavingWrongMarkets() {

        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);

        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TriggerParamFindTradingRulesResult result = algoManager.handleSupplyMarketPrices(eventId,
                        getTestMarketPricesForAutoControlForNonCorrectMarkets(false, true, true));

        assertTrue(result.isParamFindScheduled());

        Set<String> bookiesAccepted = new HashSet<String>();

        TraderParamFindResultsDescription results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            System.out.println(string);
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("Pinnacle") || string.getSource().equals("MarathonBet"));
        }
        assertTrue(bookiesAccepted.size() == 2);

        bookiesAccepted.clear();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = null;

        Sleep.sleep(5);
        algoManager.handleClearParamFindData(eventId);

        result = algoManager.handleSupplyMarketPrices(eventId,
                        getTestMarketPricesForAutoControlForNonCorrectMarkets(false, true, false));

        assertTrue(result.isParamFindScheduled());

        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            System.out.println(string);
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("Unibet") || string.getSource().equals("MarathonBet"));
        }
        assertTrue(bookiesAccepted.size() == 2);

        bookiesAccepted.clear();

        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = null;
        algoManager.handleClearParamFindData(eventId);

        Sleep.sleep(5);

        result = algoManager.handleSupplyMarketPrices(eventId,
                        getTestMarketPricesForAutoControlForNonCorrectMarkets(false, true, true));

        assertTrue(result.isParamFindScheduled());

        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            System.out.println(string);
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("Pinnacle") || string.getSource().equals("MarathonBet"));
        }
        assertTrue(bookiesAccepted.size() == 2);

        bookiesAccepted.clear();

    }

    @Test
    public void testAutoControl() {

        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);

        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());

        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesForAutoControl(false, true));

        assertTrue(result.isParamFindScheduled());

        Set<String> bookiesAccepted = new HashSet<String>();

        TraderParamFindResultsDescription results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("Pinnacle") || string.getSource().equals("MarathonBet"));
        }

        assertTrue(bookiesAccepted.size() == 2);

        bookiesAccepted.clear();

        algoManager.handleClearParamFindData(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = null;

        Map<String, String> properties = new HashMap<String, String>(1);
        properties.put("eventTier", "1");
        algoManager.handleSetEventProperties(eventId, properties);

        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesForAutoControl(false, false));

        assertTrue(result.isParamFindScheduled());

        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();
        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("Pinnacle") || string.getSource().equals("Unibet"));
        }
        System.out.println(bookiesAccepted);
        assertTrue(bookiesAccepted.size() == 2);

        // Going inplay

        algoManager.handleClearParamFindData(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = null;

        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.MATCH_STARTING, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.A), true);

        bookiesAccepted.clear();

        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesForAutoControl(true, true));

        assertTrue(result.isParamFindScheduled());

        System.out.println(algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription());
        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("MarathonBet") || string.getSource().equals("Unibet")
                            || string.getSource().equals("WilliamHill"));

        }

        assertTrue(bookiesAccepted.size() == 3);

        bookiesAccepted.clear();
        algoManager.handleClearParamFindData(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        result = null;

        result = algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesForAutoControl(true, false));

        assertTrue(result.isParamFindScheduled());

        System.out.println(algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription());
        results = algoManager.getEventDetails(eventId).getTriggerParamFindData()
                        .getLastSuccessfulTraderParamFindResultsDescription();

        for (TraderParamFindResultsDetailRow string : results.getResultDetailRows()) {
            if (!(bookiesAccepted.contains(string.getSource()))) {
                bookiesAccepted.add(string.getSource());
            }
            assertTrue(string.getSource().equals("BetVictor") || string.getSource().equals("Unibet")
                            || string.getSource().equals("WilliamHill"));

        }

        assertTrue(bookiesAccepted.size() == 3);

        System.out.println("END");

    }

    @Test
    public void testMatchstatePropertiesNotSet() {
        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);
        /*
         * compare with prices close to the current prices - should still trigger a full param find
         */
        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.FAULT, null);
        matchIncident.setEventId(eventId);
        matchIncident.setPointWinner(TeamId.UNKNOWN);
        algoManager.handleMatchIncident(matchIncident, true);
        // System.out.print(publishedMatchState);
        matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.POINT_WON, null);
        matchIncident.setEventId(eventId);
        matchIncident.setPointWinner(TeamId.A);
        matchIncident.setServerSideAtStartOfMatch(TeamId.B);
        matchIncident.setServerPlayerAtStartOfMatch(0);
        System.out.print(publishedMatchState);
        /*
         * will see a NPE in the log
         */
    }

    long eventId = 1234L;

    @Test
    public void testRT4782ProblemReport() {
        algoManager.autoSyncWithMatchFeed(false);
        TennisMatchFormat tennisMatchFormat =
                        new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        TradingRules tradingRules = new BsTennisTradingRules();
        algoManager.setTradingRules(SupportedSportType.TENNIS, tradingRules);
        algoManager.handleNewEventCreation(SupportedSportType.TENNIS, eventId, tennisMatchFormat, 3);
        BsTennisMatchParams matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.MATCH_STARTING, TeamId.A), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.B), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        algoManager.handleMatchIncident(getIncident(TennisMatchIncidentType.POINT_WON, TeamId.A), true);
        System.out.print(publishedMatchState);
        matchParams = (BsTennisMatchParams) publishedMatchParams;
        matchParams.setOnServePctA1(.603, .05);
        matchParams.setOnServePctB1(.660, .05);
        matchParams.setEventId(eventId);
        algoManager.handleSetMatchParams(matchParams.generateGenericMatchParams());
        // System.out.println(publishedMatchState);
        // System.out.println(publishedMatchParams);
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getTestMarketPricesFor4782());
        assertFalse(result.isParamFindScheduled()); // Break point - no param find

    }


    private TennisMatchIncident getIncident(TennisMatchIncidentType type, TeamId teamId) {
        TennisMatchIncident matchIncident = new TennisMatchIncident(0, type, teamId);
        matchIncident.setEventId(eventId);
        return matchIncident;
    }

    private MarketPricesList getTestMarketPricesFor4782() {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(1);
        marketPricesList.put("Bet365", bet365);
        MarketPrice b1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        b1.put("A", 5.0);
        b1.put("B", 1.16);
        bet365.addMarketPrice(b1);
        MarketPrice b2 = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "20.5");
        b2.put("Over", 2.00);
        b2.put("Under", 1.72);
        bet365.addMarketPrice(b2);
        MarketPrice b3 = new MarketPrice("FT:SPRD", "Games hcap", MarketCategory.HCAP, "4.5");
        b3.put("AH", 2.10);
        b3.put("BH", 1.66);
        bet365.addMarketPrice(b3);
        MarketPrice b4 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
        b4.put("A", 5.50);
        b4.put("B", 1.14);
        bet365.addMarketPrice(b4);

        MarketPrices betVictor = new MarketPrices();
        betVictor.setSourceWeight(1);
        marketPricesList.put("BetVictor", betVictor);
        MarketPrice v1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        v1.put("A", 5.0);
        v1.put("B", 1.15);
        betVictor.addMarketPrice(v1);
        MarketPrice v4 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.3");
        v4.put("A", 1.36);
        v4.put("B", 3.00);
        betVictor.addMarketPrice(v4);

        MarketPrices pinnacle = new MarketPrices();
        pinnacle.setSourceWeight(1);
        marketPricesList.put("Pinnacle", pinnacle);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.put("A", 5.32);
        p1.put("B", 1.2);
        pinnacle.addMarketPrice(p1);
        MarketPrice p2 = new MarketPrice("FT:OU", "Total games", MarketCategory.OVUN, "21.5");
        p2.put("Over", 2.29);
        p2.put("Under", 1.64);
        pinnacle.addMarketPrice(p2);
        MarketPrice p3 = new MarketPrice("FT:SPRD", "Games hcap", MarketCategory.HCAP, "4.5");
        p3.put("AH", 2.14);
        p3.put("BH", 1.72);
        pinnacle.addMarketPrice(p3);

        MarketPrices unibet = new MarketPrices();
        unibet.setSourceWeight(1);
        marketPricesList.put("Unibet", unibet);
        MarketPrice u1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        u1.put("A", 4.6);
        u1.put("B", 1.18);
        unibet.addMarketPrice(u1);
        MarketPrice u3 = new MarketPrice("FT:SPRD", "Games hcap", MarketCategory.HCAP, "6.5");
        u3.put("AH", 1.29);
        u3.put("BH", 3.45);
        unibet.addMarketPrice(u3);
        MarketPrice u4 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.3");
        u4.put("A", 1.40);
        u4.put("B", 2.85);
        unibet.addMarketPrice(u4);

        return marketPricesList;
    }


    private MarketPricesList getTestMarketPricesForAutoControl(boolean useGameWinner, boolean useMarathonBet) {
        MarketPricesList marketPricesList = new MarketPricesList();

        MarketPrices betVictor = new MarketPrices();
        betVictor.setSourceWeight(1);
        marketPricesList.put("BetVictor", betVictor);
        MarketPrice v1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        v1.put("A", 1.25);
        v1.put("B", 3.75);
        betVictor.addMarketPrice(v1);
        if (useGameWinner) {
            MarketPrice v2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            v2.put("A", 1.44);
            v2.put("B", 2.62);
            betVictor.addMarketPrice(v2);
        }

        MarketPrices pinnacle = new MarketPrices();
        pinnacle.setSourceWeight(1);
        marketPricesList.put("Pinnacle", pinnacle);
        MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        p1.put("A", 1.25);
        p1.put("B", 3.75);
        pinnacle.addMarketPrice(p1);
        if (useGameWinner) {
            MarketPrice p2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            p2.put("A", 1.44);
            p2.put("B", 2.62);
            pinnacle.addMarketPrice(p2);
        }

        MarketPrices unibet = new MarketPrices();
        unibet.setSourceWeight(1);
        marketPricesList.put("Unibet", unibet);
        MarketPrice u1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        u1.put("A", 1.25);
        u1.put("B", 3.75);
        unibet.addMarketPrice(u1);
        if (useGameWinner) {
            MarketPrice u2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            u2.put("A", 1.44);
            u2.put("B", 2.62);
            unibet.addMarketPrice(u2);
        }

        MarketPrices paddyPower = new MarketPrices();
        paddyPower.setSourceWeight(1);
        marketPricesList.put("PaddyPower", paddyPower);
        MarketPrice pp1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        pp1.put("A", 1.25);
        pp1.put("B", 3.75);
        paddyPower.addMarketPrice(pp1);
        if (useGameWinner) {
            MarketPrice pp2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            pp2.put("A", 1.44);
            pp2.put("B", 2.62);
            paddyPower.addMarketPrice(pp2);
        }

        if (useMarathonBet) {
            MarketPrices marathonBet = new MarketPrices();
            paddyPower.setSourceWeight(1);
            marketPricesList.put("MarathonBet", marathonBet);
            MarketPrice m1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
            m1.put("A", 1.25);
            m1.put("B", 3.75);
            marathonBet.addMarketPrice(m1);
            if (useGameWinner) {
                MarketPrice m2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
                m2.put("A", 1.44);
                m2.put("B", 2.62);
                marathonBet.addMarketPrice(m2);
            }
        }

        MarketPrices williamHill = new MarketPrices();
        williamHill.setSourceWeight(1);
        marketPricesList.put("WilliamHill", williamHill);
        MarketPrice wh1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        wh1.put("A", 1.25);
        wh1.put("B", 3.75);
        williamHill.addMarketPrice(wh1);
        if (useGameWinner) {
            MarketPrice wh2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            wh2.put("A", 1.44);
            wh2.put("B", 2.62);
            williamHill.addMarketPrice(wh2);
        }
        return marketPricesList;
    }

    private MarketPricesList getTestMarketPricesForAutoControlForNonCorrectMarkets(boolean useGameWinner,
                    boolean useMarathonBet, boolean usePinnacleMatchWinner) {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices betVictor = new MarketPrices();
        betVictor.setSourceWeight(1);
        marketPricesList.put("BetVictor", betVictor);
        MarketPrice v1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        v1.put("A", 1.25);
        v1.put("B", 3.75);
        betVictor.addMarketPrice(v1);
        if (useGameWinner) {
            MarketPrice v2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            v2.put("A", 1.44);
            v2.put("B", 2.62);
            betVictor.addMarketPrice(v2);
        }

        MarketPrices pinnacle = new MarketPrices();
        pinnacle.setSourceWeight(1);
        marketPricesList.put("Pinnacle", pinnacle);
        if (usePinnacleMatchWinner) {
            MarketPrice p1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
            p1.put("A", 1.25);
            p1.put("B", 3.75);
            pinnacle.addMarketPrice(p1);
        }
        MarketPrice p3 = new MarketPrice("FT:OU", "Total Games", MarketCategory.OVUN, "22.5");
        p3.put("Over", 1.85);
        p3.put("Under", 1.85);
        pinnacle.addMarketPrice(p3);
        MarketPrice p4 = new MarketPrice("FT:SPRD", "Handicap", MarketCategory.OVUN, "-3.5");
        p4.put("Over", 1.85);
        p4.put("Under", 1.85);
        pinnacle.addMarketPrice(p3);
        if (useGameWinner) {
            MarketPrice p2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            p2.put("A", 1.44);
            p2.put("B", 2.62);
            pinnacle.addMarketPrice(p2);
        }

        MarketPrices unibet = new MarketPrices();
        unibet.setSourceWeight(1);
        marketPricesList.put("Unibet", unibet);
        MarketPrice u1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        u1.put("A", 1.25);
        u1.put("B", 3.75);
        unibet.addMarketPrice(u1);
        if (useGameWinner) {
            MarketPrice u2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            u2.put("A", 1.44);
            u2.put("B", 2.62);
            unibet.addMarketPrice(u2);
        }

        MarketPrices paddyPower = new MarketPrices();
        paddyPower.setSourceWeight(1);
        marketPricesList.put("PaddyPower", paddyPower);
        MarketPrice pp1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        pp1.put("A", 1.25);
        pp1.put("B", 3.75);
        paddyPower.addMarketPrice(pp1);
        if (useGameWinner) {
            MarketPrice pp2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            pp2.put("A", 1.44);
            pp2.put("B", 2.62);
            paddyPower.addMarketPrice(pp2);
        }

        if (useMarathonBet) {
            MarketPrices marathonBet = new MarketPrices();
            paddyPower.setSourceWeight(1);
            marketPricesList.put("MarathonBet", marathonBet);
            MarketPrice m1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
            m1.put("A", 1.25);
            m1.put("B", 3.75);
            marathonBet.addMarketPrice(m1);
            if (useGameWinner) {
                MarketPrice m2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
                m2.put("A", 1.44);
                m2.put("B", 2.62);
                marathonBet.addMarketPrice(m2);
            }
        }

        MarketPrices williamHill = new MarketPrices();
        williamHill.setSourceWeight(1);
        marketPricesList.put("WilliamHill", williamHill);
        MarketPrice wh1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        wh1.put("A", 1.25);
        wh1.put("B", 3.75);
        williamHill.addMarketPrice(wh1);
        if (useGameWinner) {
            MarketPrice wh2 = new MarketPrice("G:ML", "Game winner", MarketCategory.GENERAL, null, "S1.2");
            wh2.put("A", 1.44);
            wh2.put("B", 2.62);
            williamHill.addMarketPrice(wh2);
        }
        return marketPricesList;
    }

    /**
     * should be close to the default settings, so should not trigger a paramFind
     *
     * @return
     */
    private MarketPricesList getTestMarketPricesNear(boolean useTotals) {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices betVictor = new MarketPrices();
        betVictor.setSourceWeight(1);
        marketPricesList.put("BetVictor", betVictor);
        MarketPrice v1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        v1.put("A", 1.83);
        v1.put("B", 1.83);
        betVictor.addMarketPrice(v1);

        MarketPrices Unibet = new MarketPrices();
        betVictor.setSourceWeight(1);
        marketPricesList.put("Unibet", Unibet);
        MarketPrice Unibet1 = new MarketPrice("FT:ML", "Match winner", MarketCategory.GENERAL, null);
        Unibet1.put("A", 1.83);
        Unibet1.put("B", 1.83);
        Unibet.addMarketPrice(Unibet1);

        return marketPricesList;
    }
}
