package ats.algo.algomanager.outrights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.outrights.OutrightsMatchIncident;
import ats.algo.sport.outrights.OutrightsWatchList;

/**
 * provides the support needed to monitor outrights watchlists and generate calc requests
 * 
 * @author gicha
 *
 */
public class OutrightsMonitor {


    /**
     * map of outrightEventId to monitor entry
     */
    private Map<Long, OutrightsMonitorEntry> entriesByOutrightEventId;

    static long TIME_BETWEEN_CALCS_MS = 30000;


    public OutrightsMonitor() {
        entriesByOutrightEventId = new HashMap<>();
    }

    /**
     * add a new outrights event - should be invoked when new outrights event created * @param outrightsEventId
     */
    public void addOutrightEvent(long outrightsEventId) {
        entriesByOutrightEventId.put(outrightsEventId, new OutrightsMonitorEntry());
    }

    /**
     * update the watchList with the contents of a received PriceCalcResponse
     * 
     * @param outrightsEventId
     * @param watchList
     */
    public void updateWatchList(long outrightsEventId, OutrightsWatchList watchList) {
        OutrightsMonitorEntry entry = entriesByOutrightEventId.get(outrightsEventId);
        if (entry == null) {
            entry = new OutrightsMonitorEntry();
            entriesByOutrightEventId.put(outrightsEventId, entry);
        }
        entry.updateWatchList(watchList);

    }

    /**
     * check published markets to see if on any watchlists
     * 
     * @param eventID id of the match to which these markets belong
     * @param markets
     */
    public void addMarketsOnWatchLists(long matchEventId, Markets markets) {
        for (OutrightsMonitorEntry entry : entriesByOutrightEventId.values()) {
            entry.addMarketsOnWatchLists(matchEventId, markets);
        }
    }

    public void addResultedMarketsOnWatchLists(long matchEventId, ResultedMarkets resultedMarkets) {
        for (OutrightsMonitorEntry entry : entriesByOutrightEventId.values()) {
            entry.addResultedMarketsOnWatchLists(matchEventId, resultedMarkets);
        }

    }


    /**
     * receive notification of any match incident
     * 
     * @param eventId
     * @param matchIncident
     */
    public void notifyMatchIncident(long matchEventId, MatchIncident matchIncident) {
        if (matchIncident == null)
            return;
        for (OutrightsMonitorEntry entry : entriesByOutrightEventId.values()) {
            entry.notifyMatchIncident(matchEventId, matchIncident);
        }
    }

    public List<OutrightsMatchIncident> priceCalcsToSchedule() {
        List<OutrightsMatchIncident> incidents = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (Entry<Long, OutrightsMonitorEntry> e : entriesByOutrightEventId.entrySet()) {
            OutrightsMatchIncident incident = e.getValue().matchIncidentToSchedule(now);
            if (incident != null) {
                incident.setEventId(e.getKey());
                incidents.add(incident);
            }
        }
        return incidents;
    }

    public void removeOutrightsEvent(long eventId) {
        entriesByOutrightEventId.remove(eventId);

    }



}
