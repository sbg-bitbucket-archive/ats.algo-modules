package ats.algo.genericsupportfunctions;

public class Statistics {

    long timeOfLastEvent;
    double last;
    long nEvents;
    double total;
    long timeOfMaxEvent;
    double max;
    long fatalErrorCount;

    public Statistics() {
        last = 0;
        nEvents = 0;
        total = 0;
        max = 0;
    }

    public void updateStats(double value, long timeOfEvent) {
        last = value;
        if (last > max) {
            max = last;
            timeOfMaxEvent = timeOfEvent;
        }
        total += value;
        nEvents++;
    }

    public long getTimeOfLastEvent() {
        return timeOfLastEvent;
    }

    public long getTimeOfMaxEvent() {
        return timeOfMaxEvent;
    }

    public double getLast() {
        return last;
    }

    public long getnEvents() {
        return nEvents;
    }

    public double getAverage() {
        if (nEvents == 0)
            return 0;
        else
            return total / nEvents;
    }

    public double getMax() {
        return max;
    }

    public long getfatalErrorCount() {
        return fatalErrorCount;
    }

    public void incrementFatalErrorCount() {
        fatalErrorCount++;
    }

}
