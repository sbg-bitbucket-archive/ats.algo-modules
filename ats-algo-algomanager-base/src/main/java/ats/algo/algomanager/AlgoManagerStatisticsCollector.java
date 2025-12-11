package ats.algo.algomanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.Statistics;

/**
 * holds performance statistics for Algo Manager
 * 
 * @author Geoff
 *
 */
public class AlgoManagerStatisticsCollector {

    AlgoManagerConfiguration algoManagerConfiguration;
    private Statistics priceCalcQueueStats;
    private Statistics paramFindQueueStats;
    private int noEventsCreated;
    private Map<Long, EventDetails> eventList;

    public AlgoManagerStatisticsCollector(Map<Long, EventDetails> eventList,
                    AlgoManagerConfiguration algoManagerConfiguration) {
        this.eventList = eventList;
        this.algoManagerConfiguration = algoManagerConfiguration;
        priceCalcQueueStats = new Statistics();
        paramFindQueueStats = new Statistics();
    }

    void recordEventCreated() {
        noEventsCreated++;
    }

    /**
     * called regularly by timer to update AlgoManager stats
     */
    void updateStatistics() {
        long now = System.currentTimeMillis();
        int size1 = algoManagerConfiguration.getPriceCalcQueueSize();
        priceCalcQueueStats.updateStats(size1, now);
        int size2 = algoManagerConfiguration.getParamFindQueueSize();
        paramFindQueueStats.updateStats(size2, now);
    }

    int getNoEventsCreated() {
        return noEventsCreated;
    }

    double getAveragePriceCalcQueueSize() {
        return priceCalcQueueStats.getAverage();
    }

    int getMaxPriceCalcQueueSize() {
        return (int) priceCalcQueueStats.getMax();
    }

    long getTimeMaxPriceCalcQueueSize() {
        return priceCalcQueueStats.getTimeOfMaxEvent();
    }

    double getAverageParamFindQueueSize() {
        return paramFindQueueStats.getAverage();
    }

    int getMaxParamFindQueueSize() {
        return (int) paramFindQueueStats.getMax();
    }

    long getTimeMaxParamFindQueueSize() {
        return paramFindQueueStats.getTimeOfMaxEvent();
    }

    int getNoFreeCalculators() {
        return algoManagerConfiguration.getNoAlgoCalculators() - (int) priceCalcQueueStats.getLast();
    }

    int getNoFreeParamFinders() {
        return algoManagerConfiguration.getNoAlgoParamFinders() - (int) paramFindQueueStats.getLast();
    }

    private AlgoStats summaryStats;
    private Map<SupportedSportType, AlgoStats> summaryStatsBySportList;
    private Map<SupportedSportType, Map<Long, AlgoStats>> summaryStatsByEventList;
    private int noActiveEvents;


    private void calculateSummaryEventStatistics() {

        summaryStats = new AlgoStats();
        summaryStatsBySportList = new HashMap<SupportedSportType, AlgoStats>();
        summaryStatsByEventList = new HashMap<SupportedSportType, Map<Long, AlgoStats>>();
        /*
         * iterate through event list and increment stats at the event level
         */
        for (Entry<Long, EventDetails> entry : eventList.entrySet()) {
            Long eventId = entry.getKey();
            EventDetails eventDetails = entry.getValue();
            EventStatisticsCollector eventStatsCollector = eventDetails.getStatistics();
            SupportedSportType sport = eventDetails.getSupportedSport();
            AlgoStats summaryStatsBySport = summaryStatsBySportList.get(sport);
            Map<Long, AlgoStats> summaryStatsBySportEventList = summaryStatsByEventList.get(sport);
            if (summaryStatsBySport == null) {
                summaryStatsBySport = new AlgoStats();
                summaryStatsBySportList.put(sport, summaryStatsBySport);
                summaryStatsBySportEventList = new HashMap<Long, AlgoStats>();
                summaryStatsByEventList.put(sport, summaryStatsBySportEventList);
            }
            AlgoStats eventStats = eventStatsCollector.getSnapshot();
            summaryStatsBySportEventList.put(eventId, eventStats);
            summaryStatsBySport.incrementStats(eventStats);
            summaryStats.incrementStats(eventStats);
        }

        noActiveEvents = eventList.size();

    }

    int getEventsCreated() {
        return noEventsCreated;
    }

    int getNoActiveEvents() {
        return noActiveEvents;
    }

    int getNoPriceCalculators() {
        return algoManagerConfiguration.getNoAlgoCalculators();
    }

    int getNoParamFinders() {
        return algoManagerConfiguration.getNoAlgoParamFinders();
    }

    int getCurrentPriceCalcQueueSize() {
        return (int) priceCalcQueueStats.getLast();
    }

    int getCurrentParamFindQueueSize() {
        return (int) paramFindQueueStats.getLast();
    }

    AlgoManagerStatistics getSnapshot() {
        AlgoManagerStatistics snapshot = new AlgoManagerStatistics();
        calculateSummaryEventStatistics();
        snapshot.setnPriceCalculators(this.getNoPriceCalculators());
        snapshot.setnParamFinders(this.getNoParamFinders());
        snapshot.setnEventsCreated(this.getNoEventsCreated());
        snapshot.setnActiveEvents(this.getNoActiveEvents());
        snapshot.setAveragePriceCalcQueueSize(priceCalcQueueStats.getAverage());
        snapshot.setMaxPriceCalcQueueSize((int) priceCalcQueueStats.getMax());
        snapshot.setTimeMaxPriceCalcQueueSize(priceCalcQueueStats.getTimeOfMaxEvent());
        snapshot.setAverageParamFindQueueSize(paramFindQueueStats.getAverage());
        snapshot.setMaxParamFindQueueSize((int) paramFindQueueStats.getMax());
        snapshot.setTimeMaxParamFindQueueSize(paramFindQueueStats.getTimeOfMaxEvent());
        snapshot.setSummaryStats(summaryStats);
        snapshot.setSummaryStatsBySport(summaryStatsBySportList);
        snapshot.setSummaryStatsByEvent(summaryStatsByEventList);
        return snapshot;
    }
}
