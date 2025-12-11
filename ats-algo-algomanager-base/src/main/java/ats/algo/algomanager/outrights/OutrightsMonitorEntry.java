package ats.algo.algomanager.outrights;

import java.util.HashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsWatchList;

class OutrightsMonitorEntry {
    private long timeOfLastCalc;
    private boolean scheduleAsap;
    private OutrightsWatchList watchList;
    private Map<Long, Market> watchedMarkets;
    private Map<Long, ResultedMarket> watchedResultedMarkets;

    OutrightsMonitorEntry() {
        watchList = new OutrightsWatchList();
        watchedMarkets = new HashMap<>();
        watchedResultedMarkets = new HashMap<>();
        timeOfLastCalc = System.currentTimeMillis();
    }

    public void updateWatchList(OutrightsWatchList watchList) {
        this.watchList = watchList;
        scheduleAsap = false;
    }

    public void addMarketsOnWatchLists(long matchEventId, Markets markets) {
        String marketType = watchList.get(matchEventId);
        if (marketType != null) {
            Market market = markets.get(marketType);
            if (market != null)
                watchedMarkets.put(matchEventId, market);
        }
    }

    public void addResultedMarketsOnWatchLists(long matchEventId, ResultedMarkets resultedMarkets) {
        String marketType = watchList.get(matchEventId);
        if (marketType != null) {
            ResultedMarket resultedMarket = resultedMarkets.get(marketType);
            if (resultedMarket != null)
                watchedResultedMarkets.put(matchEventId, resultedMarket);
        }
    }

    /**
     * watches for goals being scored
     * 
     * @param matchEventId
     * @param matchIncident
     */
    public void notifyMatchIncident(long matchEventId, MatchIncident matchIncident) {
        String marketType = watchList.get(matchEventId);
        if (marketType == null)
            return;
        if (matchIncident instanceof FootballMatchIncident) {
            FootballMatchIncident incident = (FootballMatchIncident) matchIncident;
            if (incident.getIncidentSubType() == FootballMatchIncidentType.GOAL)
                scheduleAsap = true;
        }
    }

    public OutrightsMatchIncident matchIncidentToSchedule(long now) {
        OutrightsMatchIncident incident = null;
        long timeElapsed = now - timeOfLastCalc;
        // System.out.printf("asap: %b, elapsed: %d\n", scheduleAsap, timeElapsed);
        if (scheduleAsap || timeElapsed >= OutrightsMonitor.TIME_BETWEEN_CALCS_MS) {
            incident = new OutrightsMatchIncident();
            incident.setInputMarkets(watchedMarkets);
            incident.setInputResultedMarkets(watchedResultedMarkets);
            timeOfLastCalc = now;
            scheduleAsap = false;
        }
        return incident;
    }

}
