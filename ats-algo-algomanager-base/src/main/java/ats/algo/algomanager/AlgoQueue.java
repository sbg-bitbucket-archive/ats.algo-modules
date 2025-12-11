package ats.algo.algomanager;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.matchresult.MatchResultMap;
import ats.core.AtsBean;

/**
 * manages queues required by AlgoManager and AlgoManagerConfiguration
 * 
 * @author Geoff
 *
 */
public class AlgoQueue extends AtsBean {

    public enum AlgoWorkType {
        MATCH_INCIDENT,
        UNDO_INCIDENT,
        TIMER_CALC_REQUEST,
        SET_MATCH_PARAMS_MANUAL,
        SET_MATCH_PARAMS_AUTO,
        RESULT_MARKETS_MANUALLY,
        REVERT_TO_EARLIER_EVENT_STATE,
        ABANDON_EVENT_WITH_VOID_RESULTING,
        ABANDON_EVENT_WITH_NO_RESULTING
    }

    class PendingAlgoWork {
        AlgoWorkType algoWorkType;
        long eventId;
        MatchFormat matchFormat;
        long eventTier;
        GenericMatchParams matchParams;
        MatchState matchState;
        MarketPricesList marketPricesList;
        MatchIncident matchIncident;
        ParamFindResults paramFindResults;
        MatchResultMap matchManualResult;
        String incidentId;
        long requestTime;

        PendingAlgoWork(AlgoWorkType algoWorkType, long eventId, String incidentId, MatchFormat matchFormat,
                        long eventTier, GenericMatchParams matchParams, MatchState matchState,
                        MarketPricesList marketPricesList, MatchIncident matchIncident,
                        ParamFindResults paramFindResults, long requestTime, MatchResultMap matchManualResult) {
            this.algoWorkType = algoWorkType;
            this.eventId = eventId;
            this.matchFormat = matchFormat;
            this.eventTier = eventTier;
            this.matchParams = matchParams;
            this.matchState = matchState;
            this.marketPricesList = marketPricesList;
            this.matchIncident = matchIncident;
            this.paramFindResults = paramFindResults;
            this.incidentId = incidentId;
            this.requestTime = requestTime;
            this.matchManualResult = matchManualResult;
        }

        AlgoWorkType getAlgoWorkType() {
            return this.algoWorkType;
        }

        long getEventId() {
            return eventId;
        }

        MatchIncident getMatchIncident() {
            return matchIncident;
        }

        GenericMatchParams getMatchParams() {
            return matchParams;
        }

        MatchState getMatchState() {
            return matchState;
        }

        MarketPricesList getMarketPricesList() {
            return marketPricesList;
        }

        ParamFindResults getParamFindResults() {
            return paramFindResults;
        }

        String getRequestId() {
            return incidentId;
        }

        long getRequestTime() {
            return requestTime;
        }

        MatchFormat getMatchFormat() {
            return matchFormat;
        }

        long getEventTier() {
            return this.eventTier;
        }

        protected MatchResultMap getMatchManualResult() {
            return matchManualResult;
        }

        protected void setMatchManualResult(MatchResultMap matchManualResult) {
            this.matchManualResult = matchManualResult;
        }

        @Override
        public String toString() {
            return "PendingAlgoWork [" + algoWorkType + ", for event " + eventId + ", incidentId=" + incidentId
                            + ", since" + new Date(requestTime) + "]";
        }
    }

    private LinkedList<PendingAlgoWork> queue;

    AlgoQueue() {
        queue = new LinkedList<PendingAlgoWork>();
    }

    int size() {
        return queue.size();
    }

    public void logCurrentQueueTasks() {
        synchronized (queue) {
            Map<Long, List<PendingAlgoWork>> eventWorkMap = Maps.newHashMap();

            for (PendingAlgoWork work : queue) {
                List<PendingAlgoWork> pendingWorkForEvent = eventWorkMap.get(work.eventId);
                if (pendingWorkForEvent == null) {
                    eventWorkMap.put(work.eventId, pendingWorkForEvent = Lists.newArrayList());
                }
                pendingWorkForEvent.add(work);
            }

            for (Entry<Long, List<PendingAlgoWork>> entry : eventWorkMap.entrySet()) {
                debug("%s work items for match %s :- \n %s", entry.getValue().size(), entry.getKey(), entry.getValue());
            }
        }
    }

    void removePendingWorkForEvent() {
        Iterator<PendingAlgoWork> workIter = queue.iterator();
        while (workIter.hasNext()) {
            if (workIter.next() != null) {
                workIter.remove();
            } else if (workIter.toString() != null) {
                workIter.remove();
            }
        }
    }

    PendingAlgoWork removeFirstForEvent() {
        PendingAlgoWork work = null;
        if (queue.size() > 0) {
            work = queue.get(0);
            this.info("removingFirst %d, %s. Queue size %d", work.getEventId(), work.getRequestId(), queue.size());
            queue.remove(0);
        }
        return work;
    }

    void addMatchIncident(MatchIncident matchIncident, long requestTime) {
        this.info("addMatchIncident %d, %s.  Queue size: %d", matchIncident.getEventId(), matchIncident.getIncidentId(),
                        queue.size());
        add(AlgoWorkType.MATCH_INCIDENT, matchIncident.getEventId(), matchIncident.getIncidentId(), requestTime, null,
                        0, null, null, null, matchIncident, null, null);
    }

    void addSetMatchParams(GenericMatchParams matchParams, long requestTime, CalcRequestCause calcRequestCause) {
        this.info("addSetMatchParams %d.  Queue size: %d", matchParams.getEventId(), queue.size());
        AlgoWorkType workType;
        if (calcRequestCause == CalcRequestCause.PARAMS_CHANGED_BY_TRADER)
            workType = AlgoWorkType.SET_MATCH_PARAMS_MANUAL;
        else
            workType = AlgoWorkType.SET_MATCH_PARAMS_AUTO;
        add(workType, matchParams.getEventId(), null, requestTime, null, 0, matchParams, null, null, null, null, null);
    }

    void addUndoMatchIncident(long eventId, long requestTime, MatchIncident matchIncident) {
        this.info("addUndoMatchIncident %d.  Queue size: %d", eventId, queue.size());
        add(AlgoWorkType.UNDO_INCIDENT, eventId, null, requestTime, null, 0, null, null, null, matchIncident, null,
                        null);
    }

    void addRevertToEarlierEventState(long eventId, String incidentId, long requestTime) {
        this.info("addUndoMatchIncident %d, %s.  Queue size: %d", eventId, incidentId, queue.size());
        add(AlgoWorkType.REVERT_TO_EARLIER_EVENT_STATE, eventId, incidentId, requestTime, null, 0, null, null, null,
                        null, null, null);
    }

    private void add(AlgoWorkType algoWorkType, long eventId, String incidentId, long requestTime,
                    MatchFormat matchFormat, long eventTier, GenericMatchParams matchParams, MatchState matchState,
                    MarketPricesList marketPricesList, MatchIncident matchIncident, ParamFindResults paramFindResults,
                    MatchResultMap matchResult) {
        // System.out.printf("Adding to queue: %s\n", algoWorkType.toString());
        PendingAlgoWork work = new PendingAlgoWork(algoWorkType, eventId, incidentId, matchFormat, eventTier,
                        matchParams, matchState, marketPricesList, matchIncident, paramFindResults, requestTime,
                        matchResult);
        synchronized (queue) {
            queue.add(work);
        }
    }

    public void addResultMarketsManually(long eventId, MatchResultMap matchResult, long now) {
        add(AlgoWorkType.RESULT_MARKETS_MANUALLY, eventId, "ResultManually", now, null, 0, null, null, null, null, null,
                        matchResult);
    }

    public void addAbandonEventWithVoidResulting(long eventId, long now) {
        add(AlgoWorkType.ABANDON_EVENT_WITH_VOID_RESULTING, eventId, null, now, null, 0, null, null, null, null, null,
                        null);
    }

    public void addAbandonEventWithNoResulting(long eventId, long now) {
        add(AlgoWorkType.ABANDON_EVENT_WITH_NO_RESULTING, eventId, null, now, null, 0, null, null, null, null, null,
                        null);
    }
}
