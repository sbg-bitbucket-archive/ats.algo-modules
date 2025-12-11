package ats.algo.core.comparetomarket;

/**
 * container for the stats calculated by the consolidateMarketProbs method
 * 
 * @author Geoff
 *
 */
class MarketProbsStats {
    int n;
    double sumProbs;
    double sumSquareProbs;
    double sumWeights;
    double mean;
    double stdDevn;

    MarketProbsStats() {
        n = 0;
        sumProbs = 0;
        sumSquareProbs = 0;
        sumWeights = 0;
    }
}
