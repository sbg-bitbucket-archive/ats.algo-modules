package ats.algo.montecarloframework;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchEngineThreadPool {

    ExecutorService threadPool;
    ExecutorCompletionService<Boolean> executorCompletionService;

    /**
     * creates the completion service required to support concurrent calculations by the Monte Carlo match engine
     * 
     * @param nthreads
     */
    public MatchEngineThreadPool(int nThreads) {
        if (nThreads > 0) {
            threadPool = Executors.newFixedThreadPool(nThreads);
            executorCompletionService = new ExecutorCompletionService<Boolean>(threadPool);
        }
    }

    public ExecutorCompletionService<Boolean> getExecutorCompletionService() {
        return executorCompletionService;
    }

    public void close() {
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }
}
