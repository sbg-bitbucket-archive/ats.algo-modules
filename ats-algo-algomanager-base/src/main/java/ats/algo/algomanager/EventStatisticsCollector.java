package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.Statistics;

/**
 * container for holding statistics related to a specific event
 * 
 * @author Geoff
 *
 */

public class EventStatisticsCollector {

    private Statistics priceCalcStats;
    private Statistics paramFindStats;

    public EventStatisticsCollector() {
        priceCalcStats = new Statistics();
        paramFindStats = new Statistics();
    }

    void priceCalcCompleted(long eventId, String uniqueRequestId, long requestTime) {
        long timeNow = System.currentTimeMillis();
        double latency = ((double) (timeNow - requestTime)) / 1000.0;
        // System.out.printf("Request: %d, Complete: %d, latency: %.4f\n",
        // requestTime,timeNow, latency );
        priceCalcStats.updateStats(latency, timeNow);
    }

    public void priceCalcAbandoned(long eventId, String requestId) {
        priceCalcStats.incrementFatalErrorCount();
    }

    public void paramFindAbandoned(long eventId, String requestId) {
        paramFindStats.incrementFatalErrorCount();
    }

    void paramFindCompleted(long eventId, String requestId, long requestTime) {
        long timeNow = System.currentTimeMillis();
        double latency = ((double) (timeNow - requestTime)) / 1000.0;
        paramFindStats.updateStats(latency, timeNow);
    }

    double getLatencyLastPriceCalc() {
        return priceCalcStats.getLast();
    }

    double getLatencyLastParamFind() {
        return paramFindStats.getLast();
    }

    int getnPriceCalcsExecuted() {
        return (int) priceCalcStats.getnEvents();
    }

    int getnParamFindsExecuted() {
        return (int) paramFindStats.getnEvents();
    }

    /**
     * gets exponential moving average of latency of price calcs
     * 
     * @return
     */
    double getAverageLatencyPriceCalcs() {
        return priceCalcStats.getAverage();
    }

    /**
     * gets exponential moving average of latency of param finds
     * 
     * @return
     */
    double getAverageLatencyParamFinds() {
        return paramFindStats.getAverage();
    }

    long getTimeMaxLatencyPriceCalcs() {
        return priceCalcStats.getTimeOfMaxEvent();
    }

    long getTimeMaxLatencyParamFinds() {
        return paramFindStats.getTimeOfMaxEvent();
    }

    double getMaxLatencyPriceCalcs() {
        return priceCalcStats.getMax();
    }

    double getMaxLatencyParamFinds() {
        return paramFindStats.getMax();
    }

    AlgoStats getSnapshot() {
        AlgoStats snapshot = new AlgoStats();
        snapshot.incrementPriceCalcStats(this.getnPriceCalcsExecuted(), this.getAverageLatencyPriceCalcs(),
                        this.getMaxLatencyPriceCalcs(), this.getLatencyLastPriceCalc(),
                        this.getTimeMaxLatencyPriceCalcs());
        snapshot.incrementParamFindStats(this.getnParamFindsExecuted(), this.getAverageLatencyParamFinds(),
                        this.getMaxLatencyParamFinds(), this.getLatencyLastParamFind(),
                        this.getTimeMaxLatencyParamFinds());
        return snapshot;
    }

}
