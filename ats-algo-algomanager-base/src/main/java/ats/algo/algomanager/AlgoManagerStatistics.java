package ats.algo.algomanager;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ats.algo.core.common.SupportedSportType;
import ats.algo.genericsupportfunctions.Time;

public class AlgoManagerStatistics {

    private int nPriceCalculators;
    private int nParamFinders;
    private int nEventsCreated;
    private int nActiveEvents;
    private double averagePriceCalcQueueSize;
    private int maxPriceCalcQueueSize;
    private int currentPriceCalcQueueSize;
    private long timeMaxPriceCalcQueueSize;
    private double averageParamFindQueueSize;
    private int maxParamFindQueueSize;
    private int currentParamFindQueueSize;
    private long timeMaxParamFindQueueSize;
    private Map<SupportedSportType, AlgoStats> summaryStatsBySport;
    private AlgoStats summaryStats;
    private Map<SupportedSportType, Map<Long, AlgoStats>> summaryStatsByEvent;

    public int getnPriceCalculators() {
        return nPriceCalculators;
    }

    void setnPriceCalculators(int nPriceCalculators) {
        this.nPriceCalculators = nPriceCalculators;
    }

    public int getnParamFinders() {
        return nParamFinders;
    }

    void setnParamFinders(int nParamFinders) {
        this.nParamFinders = nParamFinders;
    }

    public int getnEventsCreated() {
        return nEventsCreated;
    }

    void setnEventsCreated(int nEventsCreated) {
        this.nEventsCreated = nEventsCreated;
    }

    public int getnActiveEvents() {
        return nActiveEvents;
    }

    void setnActiveEvents(int nActiveEvents) {
        this.nActiveEvents = nActiveEvents;
    }

    public double getAveragePriceCalcQueueSize() {
        return averagePriceCalcQueueSize;
    }

    void setAveragePriceCalcQueueSize(double averagePriceCalcQueueSize) {
        this.averagePriceCalcQueueSize = averagePriceCalcQueueSize;
    }

    public int getMaxPriceCalcQueueSize() {
        return maxPriceCalcQueueSize;
    }

    void setMaxPriceCalcQueueSize(int maxPriceCalcQueueSize) {
        this.maxPriceCalcQueueSize = maxPriceCalcQueueSize;
    }

    public int getCurrentPriceCalcQueueSize() {
        return currentPriceCalcQueueSize;
    }

    void setCurrentPriceCalcQueueSize(int currentPriceCalcQueueSize) {
        this.currentPriceCalcQueueSize = currentPriceCalcQueueSize;
    }

    public long getTimeMaxPriceCalcQueueSize() {
        return timeMaxPriceCalcQueueSize;
    }

    void setTimeMaxPriceCalcQueueSize(long timeMaxPriceCalcQueueSize) {
        this.timeMaxPriceCalcQueueSize = timeMaxPriceCalcQueueSize;
    }

    public double getAverageParamFindQueueSize() {
        return averageParamFindQueueSize;
    }

    void setAverageParamFindQueueSize(double averageParamFindQueueSize) {
        this.averageParamFindQueueSize = averageParamFindQueueSize;
    }

    public int getMaxParamFindQueueSize() {
        return maxParamFindQueueSize;
    }

    void setMaxParamFindQueueSize(int maxParamFindQueueSize) {
        this.maxParamFindQueueSize = maxParamFindQueueSize;
    }

    public int getCurrentParamFindQueueSize() {
        return currentParamFindQueueSize;
    }

    void setCurrentParamFindQueueSize(int currentParamFindQueueSize) {
        this.currentParamFindQueueSize = currentParamFindQueueSize;
    }

    public long getTimeMaxParamFindQueueSize() {
        return timeMaxParamFindQueueSize;
    }

    void setTimeMaxParamFindQueueSize(long timeMaxParamFindQueueSize) {
        this.timeMaxParamFindQueueSize = timeMaxParamFindQueueSize;
    }

    public Map<SupportedSportType, AlgoStats> getSummaryStatsBySport() {
        return summaryStatsBySport;
    }

    void setSummaryStatsBySport(Map<SupportedSportType, AlgoStats> summaryStatsBySport) {
        this.summaryStatsBySport = summaryStatsBySport;
    }

    public AlgoStats getSummaryStats() {
        return summaryStats;
    }

    public AlgoStats getSportStats(SupportedSportType sport) {
        AlgoStats algoStats = summaryStatsBySport.get(sport);
        return algoStats != null ? algoStats : new AlgoStats();
    }

    public AlgoStats getEventStats(Long eventId) {
        Iterator<Map<Long, AlgoStats>> iterator = summaryStatsByEvent.values().iterator();
        while (iterator.hasNext()) {
            Map<java.lang.Long, ats.algo.algomanager.AlgoStats> map = iterator.next();
            AlgoStats algoStats = map.get(eventId);
            if (algoStats != null) {
                return algoStats;
            }

        }
        return new AlgoStats();
    }

    void setSummaryStats(AlgoStats summaryStats) {
        this.summaryStats = summaryStats;
    }


    Map<SupportedSportType, Map<Long, AlgoStats>> getSummaryStatsByEvent() {
        return summaryStatsByEvent;
    }

    void setSummaryStatsByEvent(Map<SupportedSportType, Map<Long, AlgoStats>> summaryStatsByEvent) {
        this.summaryStatsByEvent = summaryStatsByEvent;
    }

    public String toStringSingleLineSummary() {
        String s = String.format(
                        "active events: %d, #price calcs: %d, #pfs: %d, calc queue size: %d, pf queue size: %d, avg latency price calcs: %.2fsecs, avg latency pfs: %.2fsecs\n",
                        nActiveEvents, summaryStats.getCountPriceCalcs(), summaryStats.getCountParamFinds(),
                        currentPriceCalcQueueSize, currentParamFindQueueSize, summaryStats.getAveragePriceCalcs(),
                        summaryStats.getAverageParamFinds());
        return s;
    }

    public String toStringBySport() {
        String s = String.format("** Summary stats by sport **\n");
        for (Entry<SupportedSportType, AlgoStats> entry : summaryStatsBySport.entrySet()) {
            SupportedSportType sport = entry.getKey();
            s += sport.toString() + "\n";
            s += entry.getValue().toString();
        }
        return s;
    }

    public String toStringByEvent() {
        String s = String.format("** Summary stats by event **\n");
        for (Entry<SupportedSportType, Map<Long, AlgoStats>> entry : summaryStatsByEvent.entrySet()) {
            SupportedSportType sport = entry.getKey();
            for (Entry<Long, AlgoStats> entry2 : entry.getValue().entrySet()) {
                s += String.format("Event: %d (%s):\n", entry2.getKey(), sport.toString());
                s += entry2.getValue().toString();
            }
        }
        return s;
    }

    public int getCountFatalParamFindErrors() {
        return summaryStats.getCountFatalParamFindErrors();
    }

    public int getCountFatalPriceCalcErrors() {
        return summaryStats.getCountFatalPriceCalcErrors();
    }

    public String toString() {
        String s = String.format("** AlgoManagerStatistics **\n");
        s += String.format(
                        "No of Price calculators: %d, no of param finders: %d, no of events handled since last restart: %d, no of active events now :%d\n",
                        nPriceCalculators, nParamFinders, nEventsCreated, nActiveEvents);
        s += "** SummaryStats: **\n";
        s += String.format("  Price calc queue size. Average: %.2f, max: %d, current: %d, time of max: %s\n",
                        averagePriceCalcQueueSize, maxPriceCalcQueueSize, currentPriceCalcQueueSize,
                        Time.getDateAsString(timeMaxPriceCalcQueueSize));
        s += String.format("  Param find queue size. Average: %.2f, max: %d, current: %d, time of max: %s\n",
                        averageParamFindQueueSize, maxParamFindQueueSize, currentParamFindQueueSize,
                        Time.getDateAsString(timeMaxParamFindQueueSize));
        s += summaryStats.toString();
        s += toStringBySport();
        s += toStringByEvent();
        return s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(averageParamFindQueueSize);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(averagePriceCalcQueueSize);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + currentParamFindQueueSize;
        result = prime * result + currentPriceCalcQueueSize;
        result = prime * result + maxParamFindQueueSize;
        result = prime * result + maxPriceCalcQueueSize;
        result = prime * result + nActiveEvents;
        result = prime * result + nEventsCreated;
        result = prime * result + nParamFinders;
        result = prime * result + nPriceCalculators;
        result = prime * result + getCountFatalParamFindErrors();
        result = prime * result + getCountFatalPriceCalcErrors();
        result = prime * result + (int) (timeMaxParamFindQueueSize ^ (timeMaxParamFindQueueSize >>> 32));
        result = prime * result + (int) (timeMaxPriceCalcQueueSize ^ (timeMaxPriceCalcQueueSize >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlgoManagerStatistics other = (AlgoManagerStatistics) obj;
        if (Double.doubleToLongBits(averageParamFindQueueSize) != Double
                        .doubleToLongBits(other.averageParamFindQueueSize))
            return false;
        if (Double.doubleToLongBits(averagePriceCalcQueueSize) != Double
                        .doubleToLongBits(other.averagePriceCalcQueueSize))
            return false;
        if (currentParamFindQueueSize != other.currentParamFindQueueSize)
            return false;
        if (currentPriceCalcQueueSize != other.currentPriceCalcQueueSize)
            return false;
        if (maxParamFindQueueSize != other.maxParamFindQueueSize)
            return false;
        if (maxPriceCalcQueueSize != other.maxPriceCalcQueueSize)
            return false;
        if (getCountFatalParamFindErrors() != other.getCountFatalParamFindErrors())
            return false;
        if (getCountFatalPriceCalcErrors() != other.getCountFatalPriceCalcErrors())
            return false;
        if (nActiveEvents != other.nActiveEvents)
            return false;
        if (nEventsCreated != other.nEventsCreated)
            return false;
        if (nParamFinders != other.nParamFinders)
            return false;
        if (nPriceCalculators != other.nPriceCalculators)
            return false;
        if (timeMaxParamFindQueueSize != other.timeMaxParamFindQueueSize)
            return false;
        if (timeMaxPriceCalcQueueSize != other.timeMaxPriceCalcQueueSize)
            return false;
        return true;
    }
}
