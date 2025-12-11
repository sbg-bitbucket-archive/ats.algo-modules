package ats.algo.algomanager;

import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.baseclasses.MatchState;
import ats.core.AtsBean;

/**
 * class to handle the AlgoManager timer on a separate thread
 * 
 * @author Geoff
 *
 */

class AlgoManagerTimer extends AtsBean implements Runnable {

    private volatile boolean keepTimerRunning;



    private AlgoManagerTimerInterface algoManager;
    private Map<Long, EventDetails> eventList;
    AlgoManagerStatisticsCollector statistics;
    int timerFrequencyMs;

    public AlgoManagerTimer(Map<Long, EventDetails> eventList, AlgoManagerStatisticsCollector statistics,
                    AlgoManagerTimerInterface algoManager) {
        this.eventList = eventList;
        this.statistics = statistics;
        this.algoManager = algoManager;
        keepTimerRunning = true;
        timerFrequencyMs = algoManager.getTimerFrequencyMs();
    }

    void killtimer() {
        keepTimerRunning = false;
    }

    @Override
    public void run() {
        while (keepTimerRunning) {
            try {
                for (Entry<Long, EventDetails> entry : eventList.entrySet()) {
                    EventDetails eventDetails = entry.getValue();
                    synchronized (eventDetails) {
                        long eventId = entry.getKey();
                        long now = System.currentTimeMillis();
                        try {
                            MatchState matchState = eventDetails.getEventState().getMatchState();
                            if (matchState.updateElapsedTime()) {
                                /*
                                 * Request a price calc if one not already in progress
                                 */
                                // System.out.printf("DEBUG - AlgoManagerTimer.run %d, isCalcInProgress: %b\n", eventId,
                                // eventDetails.isPriceCalcInProgress());
                                if (!eventDetails.isPriceCalcInProgress()) {
                                    algoManager.handleTimerInitiatedCalc(eventDetails, now);
                                }
                            }
                            algoManager.handleDelayedPublishResultedMarkets(eventDetails, now);
                            algoManager.runTimerRelatedTradingRules(eventDetails, now);
                        } catch (Exception ex) {
                            error("Problem executing request for match %s", ex, eventId);
                        }
                    }
                }
                statistics.updateStatistics();
            } catch (Exception ex) {
                error("Problem processing events list", ex);
            }
            try {
                Thread.sleep(timerFrequencyMs);
            } catch (InterruptedException e) {
                error("Timer execution interrupted");
                error(e);
            }
        }
        info("Timer is exiting following receipt of stop timer request");
    }
}
