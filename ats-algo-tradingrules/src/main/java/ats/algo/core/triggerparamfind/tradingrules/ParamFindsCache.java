package ats.algo.core.triggerparamfind.tradingrules;

import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFindResultsStatus;
import ats.algo.genericsupportfunctions.SimpleCache;

public class ParamFindsCache {

    private static final int CACHE_SIZE = 5;

    private SimpleCache<String, ParamFindsCacheEntry> cache;
    private long lastPfTime;

    public ParamFindsCache() {

        cache = new SimpleCache<String, ParamFindsCacheEntry>(CACHE_SIZE);
        lastPfTime = 0;
    }

    public void setPfRequested(String uniqueRequestId, long requestTime) {
        cache.add(uniqueRequestId, new ParamFindsCacheEntry(requestTime));

    }

    public void setPfCompleted(String uniqueRequestId, ParamFindResults results) {
        ParamFindsCacheEntry cacheEntry = cache.get(uniqueRequestId);
        if (cacheEntry != null) {
            cacheEntry.setParamfindResults(results);
            long now = System.currentTimeMillis();
            cacheEntry.setPfEndTime(now);
            lastPfTime = now;
            cacheEntry.setFatalPfError(false);
        }
    }

    public void setPfFatalError(String uniqueRequestId) {
        ParamFindsCacheEntry cacheEntry = cache.get(uniqueRequestId);
        if (cacheEntry != null) {
            long now = System.currentTimeMillis();
            cacheEntry.setPfEndTime(now);
            lastPfTime = now;
            cacheEntry.setFatalPfError(true);
        }
    }

    public long getLastPfTime() {
        return lastPfTime;
    }

    public void resetLastPfTime() {
        lastPfTime = 0;
    }

    public boolean lastParamFindRedOrAmber() {
        ParamFindsCacheEntry cacheEntry = cache.getMostRecent();
        if (cacheEntry == null)
            return false;
        if (cacheEntry.isFatalPfError())
            return true;
        ParamFindResultsStatus lastStatus = cacheEntry.getParamfindResults().getParamFindResultsStatus();
        return lastStatus == ParamFindResultsStatus.RED || lastStatus == ParamFindResultsStatus.AMBER;
    }


}
