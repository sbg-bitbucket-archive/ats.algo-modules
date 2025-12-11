package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.Time;

public class AlgoStats {
    private double weightedSumPriceCalcs;
    private int countPriceCalcs;
    private double maxPriceCalcs;
    private double lastPriceCalcs;
    private long timeOfMaxPriceCalcs;
    private double weightedSumParamFinds;
    private int countParamFinds;
    private double maxParamFinds;
    private double lastParamFinds;
    private long timeOfMaxParamFinds;
    private int countFatalPriceCalcErrors;
    private int countFatalParamFindErrors;

    AlgoStats() {
        this.weightedSumPriceCalcs = 0;
        this.countPriceCalcs = 0;
        this.maxPriceCalcs = 0;
        this.weightedSumParamFinds = 0;
        this.countParamFinds = 0;
        this.maxParamFinds = 0;
        this.lastPriceCalcs = 0;
        this.lastParamFinds = 0;
        this.countFatalPriceCalcErrors = 0;
        this.countFatalParamFindErrors = 0;
    }

    void incrementPriceCalcStats(int count, double value, double max, double last, long timeOfMax) {

        weightedSumPriceCalcs += count * value;
        this.countPriceCalcs += count;
        if (max > this.maxPriceCalcs) {
            this.maxPriceCalcs = max;
            this.timeOfMaxPriceCalcs = timeOfMax;
        }
        if (last > this.lastPriceCalcs)
            this.lastPriceCalcs = last;
    }

    void incrementParamFindStats(int count, double value, double max, double last, long timeOfMax) {
        weightedSumParamFinds += count * value;
        this.countParamFinds += count;
        if (max > this.maxParamFinds) {
            this.maxParamFinds = max;
            this.timeOfMaxParamFinds = timeOfMax;
        }
        if (last > this.lastParamFinds)
            this.lastParamFinds = last;
    }

    void incrementStats(AlgoStats stats) {
        incrementPriceCalcStats(stats.getCountPriceCalcs(), stats.getAveragePriceCalcs(), stats.getMaxPriceCalcs(),
                        stats.getLastPriceCalcs(), stats.getTimeOfMaxPriceCalcs());
        incrementParamFindStats(stats.getCountParamFinds(), stats.getAverageParamFinds(), stats.getMaxParamFinds(),
                        stats.getLastParamFinds(), stats.getTimeOfMaxParamFinds());
    }

    public int getCountPriceCalcs() {
        return countPriceCalcs;
    }

    public double getMaxPriceCalcs() {
        return maxPriceCalcs;
    }

    long getTimeOfMaxPriceCalcs() {
        return timeOfMaxPriceCalcs;
    }

    public double getAveragePriceCalcs() {
        if (countPriceCalcs > 0)
            return weightedSumPriceCalcs / countPriceCalcs;
        else
            return 0;
    }

    double getWeightedSumPriceCalcs() {
        return weightedSumPriceCalcs;
    }

    public int getCountParamFinds() {
        return countParamFinds;
    }

    public double getMaxParamFinds() {
        return maxParamFinds;
    }

    long getTimeOfMaxParamFinds() {
        return timeOfMaxParamFinds;
    }

    public double getAverageParamFinds() {
        if (countParamFinds > 0)
            return weightedSumParamFinds / countParamFinds;
        else
            return 0;
    }

    double getWeightedSumParamFinds() {
        return weightedSumParamFinds;
    }

    public double getLastPriceCalcs() {
        return lastPriceCalcs;
    }

    public double getLastParamFinds() {
        return lastParamFinds;
    }

    public int getCountFatalPriceCalcErrors() {
        return countFatalPriceCalcErrors;
    }

    public int getCountFatalParamFindErrors() {
        return countFatalParamFindErrors;
    }

    public String toString() {
        String s = "";
        s += String.format(
                        "  Price calcs.  # calcs: %d, average latency: %.2fs, max latency: %.2fs, last latency: %.2fs, Time of max:  %s, Fatal price calc errors: %d\n",
                        this.getCountPriceCalcs(), this.getAveragePriceCalcs(), this.getMaxPriceCalcs(),
                        this.getLastPriceCalcs(), Time.getDateAsString(this.getTimeOfMaxPriceCalcs()),
                        this.getCountFatalPriceCalcErrors());

        s += String.format(
                        "  Param finds. # param finds: %d, average latency: %.2fs, max latency: %.2fs, last latency: %.2fs, Time of max:  %s, Fatal param find errors: %d\n",
                        this.getCountParamFinds(), this.getAverageParamFinds(), this.getMaxParamFinds(),
                        this.getLastParamFinds(), Time.getDateAsString(this.getTimeOfMaxParamFinds()),
                        this.getCountFatalParamFindErrors());
        return s;
    }
}
