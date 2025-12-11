package ats.algo.algomanager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.matchengineframework.MatchEngine;
import ats.core.AtsBean;

/**
 * A multi-threaded configuration suitable for use in a multi-core shared memory environment
 *
 * @author Geoff
 *
 */
public class SharedMemoryEnvironmentAlgoManagerConfiguration extends AlgoManagerConfiguration {
    int nCalculators;
    int nParamFinders;
    volatile AlgoCalculator[] algoCalculators;
    volatile AlgoParamFinder[] algoParamFinders;
    CompletionService<Integer> algoCalculatorCompletionService;
    CompletionService<Integer> algoParamFinderCompletionService;
    LinkedList<PriceCalcRequest> pendingCalculationsQueue;
    LinkedList<ParamFindRequest> pendingParamFindsQueue;
    static List<ExecutorService> threadPools;
    MonitorCalculators monitorCalculators;
    MonitorParamFinders monitorParamFinders;
    Thread monitorCalculatorsThread;
    Thread monitorParamFindersThread;

    /**
     * Runs on separate thread to wait for price calcs to finish
     *
     * @author Geoff
     *
     */
    private class MonitorCalculators extends AtsBean implements Runnable {

        private volatile boolean stopRunning;

        MonitorCalculators() {
            stopRunning = false;
        }

        void killMonitor() {
            stopRunning = true;
        }

        @Override
        public void run() {
            while (!stopRunning) {
                int id;
                /*
                 * wait for next calc to finish
                 */
                try {
                    Future<Integer> fId = algoCalculatorCompletionService.take();
                    id = (int) fId.get();
                } catch (InterruptedException e) {
                    stopRunning = true;
                    break;
                } catch (ExecutionException e) {
                    String s = e.getMessage();
                    if (s == null)
                        s = "unknown exception generated";
                    fatal(s, e);
                    // e.printStackTrace();
                    stopRunning = true;
                    break;
                }
                synchronized (algoCalculators) {
                    /*
                     * send results back to AlgoManager
                     */
                    Markets markets = algoCalculators[id].getCalculatedMarkets();
                    MatchParams matchParams = algoCalculators[id].getUpdatedMatchParams();
                    MatchEngineSavedState matchEngineSavedState = algoCalculators[id].getUpdatedMatchEngineSavedState();
                    String uniqueRequestId = algoCalculators[id].getUniqueRequestId();
                    boolean fatalError = algoCalculators[id].isFatalError();
                    algoCalculators[id].setFree(true);
                    try {
                        if (fatalError) {
                            algoHandlePriceCalcError.handle(algoCalculators[id].getEventId(),
                                            algoCalculators[id].getUniqueRequestId(), "fatal calc error");
                        } else {
                            /*
                             * this is a fudge - only guaranteed to work if we generate the markets and set the eventId
                             * - 3rd parties may not
                             */

                            PriceCalcResponse response = new PriceCalcResponse(uniqueRequestId, markets,
                                            matchParams.generateGenericMatchParams(), matchEngineSavedState, null);
                            /*
                             * this is a fudge - only guaranteed to work if we generate the markets and set the eventId
                             * - 3rd parties may not
                             */
                            long eventId = algoCalculators[id].getEventId();
                            algoHandlePriceCalcResponse.handle(eventId, response);
                        }
                    } catch (Exception ex) {
                        error("Problem notifying calculated markets %s", markets);
                        error(ex);
                    }
                    /*
                     * see if anything waiting to be calculated. The handlePriceCalcResults method in AlgoManager will
                     * have scheduled a price calc for this eventId, if one was waiting, so need to check whether the
                     * calculator is still free
                     */
                    if (algoCalculators[id].isFree() && !pendingCalculationsQueue.isEmpty()) {
                        PriceCalcRequest request = pendingCalculationsQueue.removeFirst();
                        algoCalculators[id].setCalculationParams(request);
                        algoCalculatorCompletionService.submit(algoCalculators[id]);
                        algoCalculators[id].setFree(false);
                    }
                }
            }
            // System.out.println("MonitorCalculators exiting\n");
        }
    }

    /**
     * Runs on separate thread to wait for param finds to finish
     *
     * @author Geoff
     *
     */
    private class MonitorParamFinders extends AtsBean implements Runnable {

        private volatile boolean stopRunning;

        MonitorParamFinders() {
            stopRunning = false;
        }

        void killMonitor() {
            stopRunning = true;
        }

        @Override
        public void run() {
            while (!stopRunning) {
                int id;
                try {
                    Future<Integer> fId = algoParamFinderCompletionService.take();
                    id = (int) fId.get();
                } catch (InterruptedException e) {
                    stopRunning = true;
                    break;
                } catch (ExecutionException e) {
                    String s = e.getMessage();
                    if (s == null)
                        s = "unknown exception generated";
                    fatal(s, e);
                    // e.printStackTrace();
                    stopRunning = true;
                    break;
                }
                synchronized (algoParamFinders) {
                    ParamFindResults paramFindResults = algoParamFinders[id].getParamFindResults();
                    MatchParams matchParams = algoParamFinders[id].getCalculatedMatchParams();
                    String uniqueRequestId = algoParamFinders[id].getUniqueRequestId();
                    boolean fatalError = algoParamFinders[id].isFatalError();
                    String fatalErrorCause = algoParamFinders[id].getFatalErrorCause();
                    algoParamFinders[id].setFree(true);
                    try {
                        if (fatalError) {
                            algoHandleParamFindError.handle(algoParamFinders[id].getEventId(),
                                            algoParamFinders[id].getUniqueRequestId(), fatalErrorCause);
                        } else {
                            ParamFindResponse response = new ParamFindResponse(uniqueRequestId, paramFindResults,
                                            matchParams.generateGenericMatchParams());
                            /*
                             * this is a fudge - only guaranteed to work if we generate the markets and set the eventId
                             * - 3rd parties may not
                             */
                            algoHandleParamFindResponse.handle(algoParamFinders[id].getEventId(), response);
                        }

                    } catch (Exception ex) {
                        error("Problem notifying param find result %s", paramFindResults);
                        error(ex);
                    }
                    /*
                     * schedule any waiting param finds. It is possible that the handle method might in future schedule
                     * a param find if one were waiting for the same event, so check whether paramFinder is still free
                     */
                    if (algoParamFinders[id].isFree() && !pendingParamFindsQueue.isEmpty()) {
                        ParamFindRequest request = pendingParamFindsQueue.removeFirst();
                        algoParamFinders[id].setParamFindParams(request);
                        algoParamFinderCompletionService.submit(algoParamFinders[id]);
                        algoParamFinders[id].setFree(false);
                    }
                }
            }
            // System.out.println("MonitorParamFinders exiting\n");
        }
    }

    /**
     * container for the parameters to pass to the price calc engine
     *
     * @author Geoff
     *
     */
    class PendingPriceCalc {
        private Class<? extends MatchEngine> matchEngineClass;
        private CalcRequestCause calcRequestCause;
        private MatchFormat matchFormat;
        private MatchState matchState;
        private MatchParams matchParams;
        private MatchIncident matchIncident;
        private MatchIncidentResult matchIncidentResult;
        private MatchEngineSavedState matchEngineSavedState;
        private String incidentId;
        private long requestTime;

        public PendingPriceCalc(Class<? extends MatchEngine> matchEngineClass, CalcRequestCause calcRequestCause,
                        MatchFormat matchFormat, MatchState matchState, MatchParams matchParams,
                        MatchIncident matchIncident, MatchIncidentResult matchIncidentResult,
                        MatchEngineSavedState matchEngineSavedState, String incidentId, long requestTime) {
            super();
            this.matchEngineClass = matchEngineClass;
            this.calcRequestCause = calcRequestCause;
            this.matchFormat = matchFormat;
            this.matchState = matchState;
            this.matchParams = matchParams;
            this.matchIncident = matchIncident;
            this.matchIncidentResult = matchIncidentResult;
            this.matchEngineSavedState = matchEngineSavedState;
            this.incidentId = incidentId;
            this.requestTime = requestTime;
        }

        public String getIncidentId() {
            return incidentId;
        }

        public long getRequestTime() {
            return requestTime;
        }

        public MatchFormat getMatchFormat() {
            return matchFormat;
        }

        public MatchState getMatchState() {
            return matchState;
        }

        public MatchParams getMatchParams() {
            return matchParams;
        }

        public Class<? extends MatchEngine> getMatchEngineClass() {
            return matchEngineClass;
        }

        public CalcRequestCause getCalcRequestCause() {
            return calcRequestCause;
        }

        public MatchIncident getMatchIncident() {
            return matchIncident;
        }

        public MatchIncidentResult getMatchIncidentResult() {
            return matchIncidentResult;
        }

        public MatchEngineSavedState getMatchEngineSavedState() {
            return matchEngineSavedState;
        }

    }

    class PendingParamFind {
        private Class<? extends MatchEngine> matchEngineClass;
        private MatchFormat matchFormat;
        private MatchState matchState;
        private MatchParams matchParams;
        private MarketPricesList marketPricesList;
        private MatchEngineSavedState matchEngineSavedState;
        private String requestId;
        private long requestTime;

        public PendingParamFind(Class<? extends MatchEngine> matchEngineClass, MatchFormat matchFormat,
                        MatchState matchState, MatchParams matchParams, MarketPricesList marketPricesList,
                        MatchEngineSavedState matchEngineSavedState, String requestId, long requestTime) {
            super();
            this.matchEngineClass = matchEngineClass;
            this.matchFormat = matchFormat;
            this.matchState = matchState;
            this.matchParams = matchParams;
            this.marketPricesList = marketPricesList;
            this.matchEngineSavedState = matchEngineSavedState;
            this.requestId = requestId;
            this.requestTime = requestTime;
        }

        public long getRequestTime() {
            return requestTime;
        }

        public String getRequestId() {
            return requestId;
        }

        MatchFormat getMatchFormat() {
            return matchFormat;
        }

        MatchState getMatchState() {
            return matchState;
        }

        MatchParams getMatchParams() {
            return matchParams;
        }

        MarketPricesList getMarketPricesList() {
            return marketPricesList;
        }

        public Class<? extends MatchEngine> getMatchEngineClass() {
            return matchEngineClass;
        }

        public MatchEngineSavedState getMatchEngineSavedState() {
            return matchEngineSavedState;
        }

    }

    public SharedMemoryEnvironmentAlgoManagerConfiguration() {
        this(4, 2, 6);
    }

    public SharedMemoryEnvironmentAlgoManagerConfiguration(int nCalculators, int nParamFinders) {
        this(nCalculators, nParamFinders, 6);
    }

    /**
     *
     * @param nCalculators - no of calculator instances to create
     * @param nParamFinders - no of param finder instances to create
     * @param nThreadsPerMatchEngine - no of concurrent calculation threads to run in parallel in each instance of
     *        calculator or param finder
     */
    public SharedMemoryEnvironmentAlgoManagerConfiguration(int nCalculators, int nParamFinders,
                    int nThreadsPerMatchEngine) {
        super();
        SupportedSportsInitialisation.init();
        /*
         * create the array of calculators and param finders and create the thread resources to be used by each instance
         * for the MonteCarlo calculations
         */
        this.nCalculators = nCalculators;
        this.nParamFinders = nParamFinders;
        algoCalculators = new AlgoCalculator[nCalculators];
        algoParamFinders = new AlgoParamFinder[nParamFinders];
        threadPools = new LinkedList<ExecutorService>(); // maintain a list of
                                                         // them so they can
                                                         // be destroyed when
                                                         // object is closed
                                                         // down

        for (int i = 0; i < nCalculators; i++) {
            algoCalculators[i] = new AlgoCalculator(i, nThreadsPerMatchEngine);
            algoCalculators[i].setFree(true);
        }
        for (int i = 0; i < nParamFinders; i++) {
            algoParamFinders[i] = new AlgoParamFinder(i, nThreadsPerMatchEngine);
            algoParamFinders[i].setFree(true);
        }

        /*
         * create the queues
         */
        pendingCalculationsQueue = new LinkedList<PriceCalcRequest>();
        pendingParamFindsQueue = new LinkedList<ParamFindRequest>();
        /*
         * create the completion services
         */
        if (nCalculators > 0) {
            algoCalculatorCompletionService = createCompletionService(nCalculators);
            monitorCalculators = new MonitorCalculators();
            monitorCalculatorsThread = new Thread(monitorCalculators);
            monitorCalculatorsThread.start();
        }
        if (nParamFinders > 0) {
            algoParamFinderCompletionService = createCompletionService(nParamFinders);
            monitorParamFinders = new MonitorParamFinders();
            monitorParamFindersThread = new Thread(monitorParamFinders);
            monitorParamFindersThread.start();
        }

    }

    /**
     * Creates the completion service needed for each set of AlgoCalculators and AlgoParamFinders
     *
     * @param nthreads
     * @return
     */
    private static ExecutorCompletionService<Integer> createCompletionService(int nthreads) {
        ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);
        ExecutorCompletionService<Integer> e = new ExecutorCompletionService<Integer>(threadPool);
        threadPools.add(threadPool);
        return e;
    }

    @Override
    /**
     * If an algoCalculator instance is free then submits the calc to run on a separate thread; otherwise queues it up
     */
    public void schedulePriceCalc(PriceCalcRequest request) {
        synchronized (algoCalculators) {
            /*
             * first check to see whether already doing a calc for this eventId. If yes then add to queue
             */

            /*
             * not already calculating for this event. Is there a spare calculator?
             */
            int id = getFreeCalculator();

            if (id == -1) {
                /*
                 * all calculators are in use so queue up
                 */
                addCalcToQueue(request);
            } else {
                scheduleCalc(id, request);
            }
        }
    }

    @Override
    public void scheduleExternalPriceCalc(PriceCalcRequest request) {
        throw new IllegalArgumentException("Can't schedule calc via url in this configuration");
    }

    private void addCalcToQueue(PriceCalcRequest request) {
        pendingCalculationsQueue.add(request);
    }

    private void scheduleCalc(int id, PriceCalcRequest request) {
        // System.out.printf("DEBUG Config.scheduleCalc %d,%d, %s\n",
        // matchState.getEventId(),matchParams.getEventId(), requestId);
        algoCalculators[id].setCalculationParams(request);
        algoCalculatorCompletionService.submit(algoCalculators[id]);
        algoCalculators[id].setFree(false);
    }

    @Override
    /**
     *
     * @param supportedSport
     * @param matchFormat
     * @param matchState
     * @param matchParams
     * @param marketPricesList
     */
    public void scheduleParamFind(ParamFindRequest request) {

        synchronized (algoParamFinders) {
            int id = getFreeParamFinder();

            if (id == -1) {
                /*
                 * all param finders are in use so queue up
                 */
                pendingParamFindsQueue.add(request);
            } else {
                algoParamFinders[id].setParamFindParams(request);
                algoParamFinderCompletionService.submit(algoParamFinders[id]);
                algoParamFinders[id].setFree(false);
            }
        }
    }

    @Override
    public void scheduleExternalParamFind(ParamFindRequest request) {
        throw new IllegalArgumentException("Can't schedule param find via url in this configuration");
    }

    @Override
    String getStatus() {
        int nActiveCalculations = 0;
        int nActiveParamFinds = 0;
        for (int id = 0; id < nCalculators; id++) {
            if (!algoCalculators[id].isFree())
                nActiveCalculations++;
        }
        for (int id = 0; id < nParamFinders; id++) {
            if (!algoParamFinders[id].isFree())
                nActiveParamFinds++;
        }
        return String.format("Active calcs: %d, Active param finds: %d, Pending calcs: %d, Pending param finds: %d",
                        nActiveCalculations, nActiveParamFinds, pendingCalculationsQueue.size(),
                        pendingParamFindsQueue.size());
    }

    @Override
    void close() {
        monitorCalculators.killMonitor();
        monitorParamFinders.killMonitor();
        monitorCalculatorsThread.interrupt();
        monitorParamFindersThread.interrupt();
        for (ExecutorService threadPool : threadPools) {
            threadPool.shutdown();
        }

    }

    private int getFreeCalculator() {
        for (int i = 0; i < nCalculators; i++) {
            if (algoCalculators[i].isFree())
                return i;
        }
        return -1;
    }

    private int getFreeParamFinder() {
        for (int i = 0; i < nParamFinders; i++) {
            if (algoParamFinders[i].isFree())
                return i;
        }
        return -1;
    }

    @Override
    public int getPriceCalcQueueSize() {
        return pendingCalculationsQueue.size();
    }

    @Override
    public int getParamFindQueueSize() {
        return pendingParamFindsQueue.size();
    }

    @Override
    public int getNoAlgoCalculators() {
        return nCalculators;
    }

    @Override
    public int getNoAlgoParamFinders() {
        return nParamFinders;
    }

    @Override
    public boolean establishExternalModelConnection(String url) {
        return false;
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {

    }

    @Override
    public void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {

    }

    @Override
    public void abandonPriceCalc(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

    @Override
    public void abandonParamFind(long eventId, String uniqueRequestId) {
        /*
         * do nothing
         */
    }

}
