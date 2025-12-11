package ats.algo.montecarloframework;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

/**
 * Manages the spreading of a calculation across multiple instances of a calculator class
 * 
 * @author Geoff
 *
 */
public class ConcurrentCalculationManager {

    int nConcurrent;
    int iterationsPerThread;
    ConcurrentCalculator[] concurrentCalculator;
    MonteCarloMatch[] matches;
    CompletionService<Boolean> completionService;

    /**
     * Spreads a repetitive calculation, e.g. a Monte Carlo simulation, across multiple instances of a calculator.
     * 
     * @param nConcurrent - no of parallel calculators across which to share the load, in range 1-n
     * @param nIterations - total no of iterations of calculation to be performed
     * @param match - the class that contains all the logic relevant to the specific sport being modelled
     */
    public ConcurrentCalculationManager(int nConcurrent, int nIterations, MonteCarloMatch match,
                    ExecutorCompletionService<Boolean> completionService) {
        this.nConcurrent = nConcurrent;
        iterationsPerThread = nIterations / nConcurrent;
        concurrentCalculator = new ConcurrentCalculator[nConcurrent];
        matches = new MonteCarloMatch[nConcurrent];
        this.completionService = completionService;
        matches[0] = match;
        concurrentCalculator[0] = new ConcurrentCalculator(matches[0], iterationsPerThread, "Calculator 0");
        for (int i = 1; i < nConcurrent; i++) {
            matches[i] = match.clone();
            concurrentCalculator[i] = new ConcurrentCalculator(matches[i], iterationsPerThread, "Calculator " + i);
        }
    }

    /**
     * Executes the calculation, spreading over multiple threads and consolidating the results the match object supplied
     * to the constructor is updated with the consolidated stats
     * 
     * @param state - the state of the match from which the calc is to start - typically incorporating the score, skills
     *        estimates etc
     */
    public void calculate() {
        /*
         * initialise the completion service if needed and not been set externally via the setCompetionService method
         */
        if (nConcurrent > 1 && completionService == null)
            throw new IllegalArgumentException("No completion service provided");
        int remainingFutures = 0;
        for (int i = 1; i < nConcurrent; i++) {
            matches[i].resetStatistics();
            completionService.submit(concurrentCalculator[i]);
            remainingFutures++;
        }
        matches[0].resetStatistics();
        concurrentCalculator[0].calculate();
        // System.out.println("Waiting...");
        while (remainingFutures > 0) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                throw new IllegalArgumentException("Unexpected interruption of concurrent Calc");
            }
            remainingFutures--;
        }
        // System.out.println("Calcs finished");

        for (int i = 1; i < nConcurrent; i++) {
            matches[0].consolidateStats(matches[i]);
        }
    }

    public void setCompletionService(CompletionService<Boolean> completionService) {
        this.completionService = completionService;
    }

}
