package ats.algo.algomanager.outrights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.genericsupportfunctions.Sleep;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsWatchList;

public class OutrightsMonitorTest {

    @Test
    public void test() {
        OutrightsMonitor monitor = new OutrightsMonitor();
        monitor.addOutrightEvent(123456L);
        monitor.addOutrightEvent(345678L);
        OutrightsWatchList watchList = new OutrightsWatchList();
        watchList.addEntry(987L, "FT:CS");
        watchList.addEntry(986L, "FT:CS");
        watchList.addEntry(985L, "FT:CS");

        monitor.updateWatchList(123456L, watchList);
        Markets markets = new Markets();
        Market market1 = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Correct score");
        Market market2 = new Market(MarketCategory.GENERAL, "FT:XYZ", "M", "Other mkt");
        markets.addMarketWithFullKey(market1);
        markets.addMarketWithFullKey(market2);
        monitor.addMarketsOnWatchLists(987L, markets);
        ResultedMarket rm = new ResultedMarket("FT:CS", null, MarketCategory.GENERAL, "M", false, "Corrrect score",
                        "3-2", 0);
        ResultedMarkets rms = new ResultedMarkets();
        rms.addMarket(rm);
        monitor.addResultedMarketsOnWatchLists(986L, rms);
        assertEquals(0, monitor.priceCalcsToSchedule().size());
        OutrightsMonitor.TIME_BETWEEN_CALCS_MS = 3000; // 3 secs
        Sleep.sleepMs(3500);
        assertEquals(2, monitor.priceCalcsToSchedule().size());
        OutrightsMonitor.TIME_BETWEEN_CALCS_MS = 30000; // back to 30 secs
        assertEquals(0, monitor.priceCalcsToSchedule().size());
        MatchIncident ipIncident = new FootballMatchIncident(FootballMatchIncidentType.GOAL, 0, TeamId.A);
        monitor.notifyMatchIncident(987L, ipIncident);
        System.out.println("Marker");
        List<OutrightsMatchIncident> incidents = monitor.priceCalcsToSchedule();
        assertEquals(1, incidents.size());
        OutrightsMatchIncident incident = incidents.get(0);
        System.out.println(incident);
        Map<Long, Market> ipMarkets = incident.getInputMarkets();
        assertEquals(1, ipMarkets.size());
        Market market = ipMarkets.get(987L);
        assertTrue(market != null);
        assertEquals("FT:CS", market.getType());
        Map<Long, ResultedMarket> ipResultedMarkets = incident.getInputResultedMarkets();
        assertEquals(1, ipResultedMarkets.size());
        ResultedMarket resultedMarket = ipResultedMarkets.get(986L);
        assertTrue(resultedMarket != null);
        assertEquals("3-2", resultedMarket.getWinningSelections().get(0));



    }

}
