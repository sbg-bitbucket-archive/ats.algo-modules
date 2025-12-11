package ats.algo.paramfindproblemanalysis;

import static org.junit.Assert.*;

import java.util.Map;

import ats.algo.algomanager.AlgoManagerSimpleTestBase;
import ats.algo.algomanager.TriggerParamFindTradingRulesResult;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.MarketPrice;
import ats.algo.core.comparetomarket.MarketPrices;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.markets.MarketCategory;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.football.FootballMatchParams;

public class ParamFindProblemTest extends AlgoManagerSimpleTestBase {



    // @Test
    public void test1() {
        long eventId = 123L;
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());
        TriggerParamFindTradingRulesResult result =
                        algoManager.handleSupplyMarketPrices(eventId, getMarketPricesForTest1(1.88));
        assertTrue(result.isParamFindScheduled());
    }

    private MarketPricesList getMarketPricesForTest1(double probAh) {
        MarketPrices m = new MarketPrices();
        m.setSourceWeight(0.5);
        MarketPrice tmtg = new MarketPrice("FT:AH", "Unknown market", MarketCategory.OVUN, "22.5");
        tmtg.put("AH", probAh);
        tmtg.put("BH", 1.98);
        m.addMarketPrice(tmtg);
        MarketPricesList marketPricesList = new MarketPricesList();
        marketPricesList.put("Bet365", m);
        return marketPricesList;
    }

    long eventId = 123L;

    // @Test
    public void test2() {
        algoManager.onlyPublishMarketsFollowingParamChange(false);
        algoManager.handleNewEventCreation(SupportedSportType.SOCCER, eventId, new FootballMatchFormat());
        algoManager.handleMatchIncident(footballMatchStartingIncident(), true);
        algoManager.handleMatchIncident(elapsedTimeIncident(120), true);
        algoManager.handleSetMatchParams(test2MatchParams());
        MarketPricesList marketPricesList = test2MarketPrices();
        algoManager.handleSupplyMarketPrices(eventId, marketPricesList);
    }



    private MatchIncident footballMatchStartingIncident() {
        MatchIncident incident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_PERIOD_START, 0);
        incident.setEventId(eventId);
        return incident;
    }

    private MatchIncident elapsedTimeIncident(int secs) {
        MatchIncident incident = new ElapsedTimeMatchIncident(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK, secs);
        incident.setEventId(eventId);
        return incident;
    }

    private GenericMatchParams test2MatchParams() {
        FootballMatchParams matchParams = new FootballMatchParams(new FootballMatchFormat());
        Map<String, MatchParam> map = matchParams.getParamMap();
        map.get("goalTotal").getGaussian().setProperties(4.19, 0.22, 0);
        map.get("homeLoseBoost").getGaussian().setProperties(0.024, 0.05, 0);
        map.get("awayLoseBoost").getGaussian().setProperties(0.223, 0.05, 0);
        return matchParams.generateGenericMatchParams();
    }

    private MarketPricesList test2MarketPrices() {
        MarketPricesList marketPricesList = new MarketPricesList();
        MarketPrices bet365 = new MarketPrices();
        bet365.setSourceWeight(.33);
        MarketPrice p1 = new MarketPrice("FT:AHCP", "Unknown market", MarketCategory.GENERAL, "0.25", "S0-0");
        p1.put("AH", 1.9);
        p1.put("BH", 1.9);
        bet365.addMarketPrice(p1);
        MarketPrice p2 = new MarketPrice("FT:AXB", "Unknown market", MarketCategory.GENERAL, null);
        p2.put("A", 2.88);
        p2.put("B", 2.1);
        p2.put("Draw", 3.75);
        bet365.addMarketPrice(p2);
        // MarketPrice p3 = new MarketPrice("FT:OU", "Unknown market", MarketCategory.OVUN, "3.25");
        MarketPrice p3 = new MarketPrice("FT:OU", "Unknown market", MarketCategory.OVUN, "3.5");
        p3.put("Over", 1.95);
        p3.put("Under", 1.85);
        bet365.addMarketPrice(p3);
        marketPricesList.put("Bet365", bet365);
        MarketPrices pinnacle = new MarketPrices();
        pinnacle.setSourceWeight(.33);
        // MarketPrice p4 = new MarketPrice("FT:OU", "Unknown market", MarketCategory.OVUN, "3.75");
        MarketPrice p4 = new MarketPrice("FT:OU", "Unknown market", MarketCategory.OVUN, "3.5");
        p4.put("Over", 1.83);
        p4.put("Under", 1.97);
        pinnacle.addMarketPrice(p4);
        marketPricesList.put("Pinnacle", pinnacle);
        return marketPricesList;
    }



}
