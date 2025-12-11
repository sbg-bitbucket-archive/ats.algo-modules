package ats.algo.montecarloframework.matchengine;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.montecarloframework.MatchEngineThreadPool;
import ats.algo.montecarloframework.ConcurrentCalculationManager;
import ats.algo.montecarloframework.MonteCarloMatch;

/**
 * For models which implement the MonteCarlo Framework, this provides all the functionality required for the MatchEngine
 * class. It does not need any sport specific extensions
 * 
 * @author Geoff
 * 
 */
public class MonteCarloMatchEngine extends MatchEngine {

    int noIterations;
    int noThreads;

    protected MonteCarloMatch match;
    private ConcurrentCalculationManager concurrentCalculationManager;
    MatchEngineThreadPool matchEngineThreadPool;

    /**
     * 
     * @param matchState
     * @param matchParams
     * @param match
     */
    // public MonteCarloMatchEngine() {
    // super();
    // noIterations = 62500;
    // }

    public MonteCarloMatchEngine(SupportedSportType supportedSportType) {
        super(supportedSportType);
        /*
         * for MonteCarlo models want to find best fit stdDevns when param finding
         */
        noIterations = 62500;
    }

    public void initialiseMatchEngine(int noThreads, MatchEngineThreadPool matchEngineThreadPool) {
        if (match == null)
            throw new IllegalArgumentException("match object has not been initialised");

        this.noThreads = noThreads;
        this.matchEngineThreadPool = matchEngineThreadPool;
        concurrentCalculationManager = new ConcurrentCalculationManager(noThreads, noIterations, match,
                        matchEngineThreadPool.getExecutorCompletionService());
    }

    /**
     * deprecated
     */

    public void initialiseConcurrentCalculationManager() {}

    /**
     * sets the number of iterations for the montecarlo simulation -only needed if not using the default value (62500)
     * 
     * @param noIterations
     */
    public void setNoIterations(int noIterations) {
        this.noIterations = noIterations;
    }

    /**
     * calculates the probabilities for all markets associated with this sport
     * 
     * @return the set of markets
     */
    public void calculate() {
        if (concurrentCalculationManager == null) {
            this.noThreads = 4;
            initialiseMatchEngine(noThreads, new MatchEngineThreadPool(noThreads - 1));
        }
        concurrentCalculationManager.calculate();
        Markets markets = match.generateMarkets();
        matchState.setPriceCalcTime();
        super.calculatedMarkets = markets;
    }

    /**
     * results the set of supplied markets based on the current matchState should be overridden by any sport that
     * results markets
     * 
     * @return
     */
    public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState, MatchState currentMatchState) {
        return null;
    }

}
