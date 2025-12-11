package ats.algo.montecarloframework;

import java.util.concurrent.Callable;

/**
 * wrapper which allows the supplied match class to be either run inline or on separate thread
 * 
 * @author Geoff
 *
 */
public class ConcurrentCalculator implements Callable<Boolean> {

    @SuppressWarnings("unused")
    private String calculatorId;
    private MonteCarloMatch match;
    public int nIterations;


    /**
     * 
     * @param match - the class containing all the sport specific calcs
     * @param nIterations - no of iterations to execute
     * @param calculatorId - may be useful for debugging
     */
    public ConcurrentCalculator(MonteCarloMatch match, int nIterations, String calculatorId) {
        this.calculatorId = calculatorId;
        this.nIterations = nIterations;
        this.match = match;
    }


    /**
     * Executes the calculation, repeating nIterations times.
     */
    public void calculate() {

        for (int i = 0; i < nIterations; i++)
            match.playMatch();
    }

    @Override
    public Boolean call() throws Exception {
        calculate();
        // System.out.println(Thread.currentThread().getName()+" End.");
        return true;
    }
}

