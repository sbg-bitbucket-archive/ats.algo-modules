package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.genericsupportfunctions.SimpleCache;

public class MatchIncidentResultCache {

    SimpleCache<String, MatchIncidentResultCacheEntry> cache;

    private static final int CACHE_SIZE = 10;

    public MatchIncidentResultCache() {
        cache = new SimpleCache<String, MatchIncidentResultCacheEntry>(CACHE_SIZE);
    }

    public void addMatchIncidentResult(String requestId, long currentTimeMillis,
                    MatchIncidentResult matchIncidentResult) {
        cache.add(requestId, new MatchIncidentResultCacheEntry(currentTimeMillis, matchIncidentResult));
    }

    public void revertToEarlierState(String requestId) {
        cache.rollBackTo(requestId);
    }

    public long getLastIncidentTime() {
        MatchIncidentResultCacheEntry cacheEntry = cache.getMostRecent();
        if (cacheEntry == null)
            return 0;
        else
            return cacheEntry.getIncidentTime();
    }


}
