package ats.algo.algomanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ats.algo.core.markets.ResultedMarkets;
import ats.core.AtsBean;

class EventStateHistory extends AtsBean {

    private static final boolean DEBUG_ON = false;

    private int maxSize;
    private Map<String, EventState> eventStateHistory;
    /*
     * ordered list of requestId's. item 0 is the oldest.
     */
    List<String> incidentIdList;

    EventStateHistory(int size) {
        eventStateHistory = new HashMap<String, EventState>(size);
        incidentIdList = new ArrayList<String>(size);

        this.maxSize = size;
    }

    /**
     * adds eventState to the history. If history cache size is > maxSize then removes the oldest cached element and
     * returns the associated ResultedMarkets object, which may be null if already published
     * 
     * @param eventState
     * @return if cache was full then oldest element, else null
     */
    ResultedMarkets addToHistory(EventState eventState) {
        if (DEBUG_ON)
            debug("addToHistory requested for eventState: %s", eventState);
        EventState oldestEventState = null;
        String newIncidentId = eventState.getIncidentId();
        if (incidentIdList.contains(newIncidentId)) {
            /*
             * if already in list just overwrite previous entry
             */
            eventStateHistory.put(newIncidentId, eventState);

        } else {
            if (incidentIdList.size() == maxSize) {
                String reqId = incidentIdList.remove(0);
                oldestEventState = eventStateHistory.remove(reqId);
            }
            incidentIdList.add(newIncidentId);
            eventStateHistory.put(newIncidentId, eventState);
        }
        ResultedMarkets resultedMarkets = null;
        if (oldestEventState != null)
            if (!oldestEventState.isResultedMarketsPublished())
                resultedMarkets = oldestEventState.getResultedMarkets();
        if (DEBUG_ON) {
            debug("addToHistory returning object: %s", resultedMarkets);
            debug("eventStateHistory  following addToHistory: %s", this);
        }

        return resultedMarkets;
    }

    /**
     * gets the most recent eventState in the history without affecting the cache in any way.
     * 
     * @return most recent eventState, or null if empty
     */
    EventState getMostRecentEventStateFromHistory() {
        if (DEBUG_ON)
            debug("getMostRecentEventStateFromHistory requested");
        EventState newestEventState = null;
        int i = incidentIdList.size() - 1;
        if (i >= 0) {
            String incidentId = incidentIdList.get(i);
            newestEventState = eventStateHistory.get(incidentId);
        }
        if (DEBUG_ON) {
            if (newestEventState != null)
                debug("getMostRecentEventStateFromHistory returning object: %s", newestEventState);
            else
                debug("getMostRecentEventStateFromHistory returning null");
        }
        return newestEventState;
    }

    /**
     * returns the most recent requestId in the history, or null if empty
     * 
     * @return
     */
    String getPreviousIncidentId() {
        return getPreviousIncidentId(null);

    }

    /**
     * gets the most recent undoable eventState in the history where the incidentSubType (if non-null) also matches,
     * without affecting the cache in any way.
     * 
     * @param incidentSubType
     * @return
     */
    String getPreviousIncidentId(Object incidentSubType) {
        if (DEBUG_ON)
            debug("getPreviousIncidentId requested");
        String previousIncidentId = null;
        int i = incidentIdList.size() - 1;
        while (previousIncidentId == null && i >= 0) {
            String incidentId = incidentIdList.get(i);
            EventState eventState = eventStateHistory.get(incidentId);
            if (eventState.undoable()) {
                if (incidentSubType == null)
                    previousIncidentId = incidentId;
                else {
                    /*
                     * check the incident sub types also match
                     */
                    Object historyIncidentsubType = eventState.getMatchIncident().getIncidentSubType();
                    if (historyIncidentsubType.equals(incidentSubType))
                        previousIncidentId = incidentId;
                    else
                        i--;
                }
            } else
                i--;
        }
        if (DEBUG_ON) {
            if (previousIncidentId != null)
                debug("getPreviousIncidentId returning incidentId: %s", previousIncidentId);
            else
                debug("getPreviousIncidentId returning null");
            debug("previousIncidentId" + previousIncidentId);
        }
        return previousIncidentId;
    }

    int size() {
        if (eventStateHistory.size() != incidentIdList.size())
            throw new IllegalStateException("size mismatch");
        return eventStateHistory.size();
    }

    /**
     * removes all eventStates with time less than specified expiryTime and returns the most recent, if any
     * 
     * @param expiryTimeMillis
     * @return null if no time expired eventStates, else the most recent one
     */
    ResultedMarkets getMostRecentTimeExpiredResultedMarkets(long expiryTimeMillis) {
        long timeDiff = System.currentTimeMillis() - expiryTimeMillis;
        if (DEBUG_ON)
            debug("getMostRecentTimeExpiredResultedMarkets requested for timeDiff : %d", timeDiff);
        EventState eventState = null;
        Set<String> ids = new HashSet<String>();
        for (int i = 0; i < incidentIdList.size(); i++) {
            String reqId = incidentIdList.get(i);
            if (reqId != null) {
                EventState oldEventState = eventStateHistory.get(reqId);
                if (oldEventState.isTimeExpired(expiryTimeMillis) && !oldEventState.isResultedMarketsPublished()) {
                    /*
                     * oldEvent state has unpublished resulted markets and is older than the expiry time
                     */
                    ids.add(reqId);
                    eventState = oldEventState;
                }
            }
        }
        /*
         * eventState will be null if no ResultedMarkets to publish that are older than expiryTime. Otherwise will be
         * the most recent such eventState
         */
        ResultedMarkets resultedMarkets = null;
        if (eventState != null) {
            resultedMarkets = eventState.getResultedMarkets();
            for (String id : ids) {
                /*
                 * set resultedMarkets for all older eventStates to show have now been published
                 */
                eventStateHistory.get(id).setResultedMarketsPublished(true);
            }
        }
        if (DEBUG_ON) {
            if (eventState == null) {
                debug("getMostRecentTimeExpiredResultedMarkets returning null");
                debug("eventStateHistory: %s", this);
            } else {
                debug("getMostRecentTimeExpiredResultedMarkets  returning object: %s", resultedMarkets);
                debug("eventStateHistory following getMostRecentTimeExpiredResultedMarkets: %s", this);
            }
        }
        return resultedMarkets;
    }

    /**
     * Rolls back to the state before the specified incidentId. If found and rollBack allowed then the eventState
     * associated with this eventId and all more recent ones are removed from the cache
     * 
     * @param incidentId
     * @param allowRollBackIfResultedMarketsPublished if false then only allows roll back if the the resultedMArkets is
     *        not null (i.e. not yet published)
     * @return null if can't roll back, otherwise the EventState associated with this incidentId
     */
    RevertEventState rollBackToBefore(String incidentId, boolean allowRollBackIfResultedMarketsPublished) {
        if (DEBUG_ON)
            debug("rollBackToBefore requested for reqId: %s, allowRollBackIfResultedMarketsPublished: %b", incidentId,
                            allowRollBackIfResultedMarketsPublished);
        EventState eventState = null;
        RevertEventState revertEventState = new RevertEventState();
        // EventState eventState = null;

        int incidentIdIndex = incidentIdList.size() - 1 >= 0 ? incidentIdList.size() - 1 : 0;
        // FIXME: ASSUME incidentIdList is "Oldest" incident on the entry "0"
        if (incidentIdList != null && incidentIdList.size() > 0)
            while (incidentIdIndex >= 0) {
                String reqId = incidentIdList.get(incidentIdIndex);
                EventState tmpEventState = eventStateHistory.get(reqId);
                if (!tmpEventState.undoable()) {
                    debug("Not undo-able reqId: %s found.", reqId);

                    revertEventState.addNonUndoableIncidentsToBeReplayed(tmpEventState.getMatchIncident());
                }

                if (reqId.equals(incidentId)) {
                    if (!tmpEventState.undoable()) {
                        debug("Warning, rollBackToBefore for reqId: %s found eventState we want to undo is non undo able: %b",
                                        incidentId, tmpEventState.isResultedMarketsPublished());
                        return null;
                    }
                    // EventState tmpEventState = eventStateHistory.get(reqId);
                    if (DEBUG_ON)
                        debug("rollBackToBefore for reqId: %s found eventState we want to undo. resultedMarketsPublished: %b",
                                        incidentId, tmpEventState.isResultedMarketsPublished());
                    if (allowRollBackIfResultedMarketsPublished || !tmpEventState.isResultedMarketsPublished()) {
                        eventState = tmpEventState;
                    }
                    break;
                }
                incidentIdIndex--;
            }

        if (eventState != null) {
            /*
             * found the state we want to undo. Is the previous state still in the cache? If not then return null
             */
            if (incidentIdIndex == 0) {
                // debug("rollBackToBefore for reqId: %s failed. Previous state
                // not in cache", incidentId);
                return null;
            }
            eventState = eventStateHistory.get(incidentIdList.get(incidentIdIndex - 1));
            /*
             * get set of all reqIds for more recent eventStates and remove them from the history
             */
            Set<String> ids = new HashSet<String>();
            for (int i = incidentIdIndex; i < incidentIdList.size(); i++) {
                String reqId = incidentIdList.get(i);
                ids.add(reqId);
                eventStateHistory.remove(reqId);
            }
            incidentIdList.removeAll(ids);
        } else {
            if (DEBUG_ON)
                debug("rollBackToBefore for reqId: %s.  Can't undo.  The previous eventState is no longer in history",
                                incidentId);
        }
        if (DEBUG_ON) {
            // debugPrintHistory();
            debug("eventStateHistory following rollBackToBefore for reqId: %s: %s", incidentId, this);
        }
        if (eventState == null)
            return null;
        else {
            revertEventState.setEventState(eventState);
        }
        return revertEventState;
    }

    @Override
    public String toString() {
        return "EventStateHistory [size=" + size() + ", requestIdList=" + incidentIdList + ", eventStateHistory="
                        + eventStateHistory + "]";
    }

    @SuppressWarnings("unused")
    private void debugPrintHistory() {
        System.out.println("\nEVENT_STATE_HISTORY");
        System.out.println("incidentIdList=" + incidentIdList);
        for (String reqId : incidentIdList) {
            EventState eventState = this.eventStateHistory.get(reqId);
            System.out.printf("\nNextInicdentId: %s, resultedMarketsPublished: %b\n", reqId,
                            eventState.isResultedMarketsPublished());
            System.out.println(eventState.getMatchState().generateSimpleMatchState());
            System.out.println(eventState.getResultedMarkets());
        }
    }

}
