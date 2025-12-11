package ats.algo.cache.ignite;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.CacheServerNotFoundException;

import ats.algo.algomanager.EventStateBlob;
import ats.algo.cache.AlgoManagerCacheService;
import ats.core.AtsBean;

public class AlgoManagerIgniteCache extends AtsBean implements AlgoManagerCacheService {

    protected Ignite algoMgrIgnite;
    protected IgniteCache<Long, EventStateBlob> eventStateCache;
    private long stateBlobExpiryInHours = 12;
    private long nextUseTimeAfterCacheError;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void setAlgoMgrIgnite(Ignite algoMgrIgnite) {
        this.algoMgrIgnite = algoMgrIgnite;
    }

    public void setStateBlobExpiryInHours(long stateBlobExpiryInHours) {
        this.stateBlobExpiryInHours = stateBlobExpiryInHours;
    }

    public void init() {
        eventStateCache = algoMgrIgnite.getOrCreateCache("eventStateCache");
        eventStateCache = eventStateCache.withExpiryPolicy(
                        new ModifiedExpiryPolicy(new Duration(TimeUnit.HOURS, stateBlobExpiryInHours)));
    }

    @Override
    public boolean isOperational() {
        long testKey = 1l;

        try {
            EventStateBlob test = new EventStateBlob();
            test.setIncidentId("operationalTest");
            eventStateCache.put(testKey, test);
        } catch (Exception e) {
            warn("Ignite eventStateCache is not operational : put operation failed", e);
            return false;
        }

        try {
            EventStateBlob test = eventStateCache.get(testKey);
            if (null == test) {
                warn("Ignite eventStateCache is not operational : get operation returned null");
                return false;
            }
        } catch (Exception e) {
            warn("Ignite eventStateCache is not operational : get operation failed", e);
            return false;
        }

        info("Ignite eventStateCache is operational : put and get operations successfull");
        return true;
    }

    @Override
    public EventStateBlob getEventState(long eventId) {
        if (isSafeToCache()) {
            try {
                return eventStateCache.get(eventId);
            } catch (Exception ex) {
                calcNextUseTimeAfterCacheError(ex);
                warn("Problem fetching match " + eventId + " blob", ex);
            }
        }
        return null;
    }

    @Override
    public String getMostRecentlyBlobedIncidentId(long eventId) {
        EventStateBlob eventBlob = getEventState(eventId);
        return eventBlob != null ? eventBlob.getIncidentId() : null;
    }

    @Override
    public void putAsyncEventState(long eventId, EventStateBlob blob, Consumer<EventStateBlob> callback) {
        executor.submit(() -> {
            if (putEventState(eventId, blob)) {
                callback.accept(blob);
            }
        });
    }

    @Override
    public boolean putEventState(long eventId, EventStateBlob blob) {
        if (isSafeToCache()) {
            try {
                eventStateCache.put(eventId, blob);
                debug("Cached event %s checkpoint for requestId %s", eventId, blob.getIncidentId());
                return true;
            } catch (Exception ex) {
                warn("Problem putting match " + eventId + " blob", ex);
                calcNextUseTimeAfterCacheError(ex);
            }
        }
        return false;
    }

    @Override
    public void removeEventState(long eventId) {
        if (isSafeToCache()) {
            try {
                eventStateCache.remove(eventId);
            } catch (Exception ex) {
                warn("Problem removing match " + eventId + " blob", ex);
                calcNextUseTimeAfterCacheError(ex);
            }
        }
    }

    protected boolean isSafeToCache() {
        if (nextUseTimeAfterCacheError != 0 && System.currentTimeMillis() >= nextUseTimeAfterCacheError) {
            nextUseTimeAfterCacheError = 0;
        }
        return nextUseTimeAfterCacheError == 0;
    }

    protected void calcNextUseTimeAfterCacheError(Exception ex) {
        if (ex instanceof IgniteCheckedException) {
            nextUseTimeAfterCacheError = Instant.now().plusSeconds(60).toEpochMilli();
        } else if (ex instanceof CacheServerNotFoundException) {
            nextUseTimeAfterCacheError = Instant.now().plus(java.time.Duration.ofMinutes(20)).toEpochMilli();
        }
    }
}
