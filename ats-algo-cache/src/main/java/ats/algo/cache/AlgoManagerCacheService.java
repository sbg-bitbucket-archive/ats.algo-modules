package ats.algo.cache;

import java.util.function.Consumer;

import ats.algo.algomanager.EventStateBlob;

/**
 * Interface defining methods for AlgoManager components to obtain cached state outside of the executing process.
 */
public interface AlgoManagerCacheService {

    /**
     * Performs a put / get to verify connectivity with the remote cache server
     * 
     * @return
     */
    boolean isOperational();

    /**
     * Return any cached event blob
     * 
     * @param eventId
     * @return the cached blob or null
     */
    EventStateBlob getEventState(long eventId);

    /**
     * Store the event blob
     * 
     * @param eventId
     * @param blob
     * @return true if the put was successful false otherwise
     */
    boolean putEventState(long eventId, EventStateBlob blob);

    void putAsyncEventState(long eventId, EventStateBlob blob, Consumer<EventStateBlob> callback);

    /**
     * Returns the requestID of the most recently check-pointed incident for the inplay event
     * 
     * @param eventId
     * @return the incidents requestId or null
     */
    String getMostRecentlyBlobedIncidentId(long eventId);

    /**
     * Remove any cached event blob
     * 
     * @param eventId
     */
    void removeEventState(long eventId);
}
