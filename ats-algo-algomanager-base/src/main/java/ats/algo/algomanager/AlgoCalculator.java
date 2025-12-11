package ats.algo.algomanager;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.montecarloframework.MatchEngineThreadPool;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.core.AtsBean;

/**
 * executes a price calculation for any supportedSport
 *
 * @author Geoff
 *
 */
public class AlgoCalculator extends AtsBean implements Callable<Integer> {

    private int id;
    private int nThreads;
    private long eventId;
    private String uniqueRequestId;

    private Class<? extends MatchEngine> matchEngineClass;
    private CalcRequestCause calcRequestCause;
    private MatchFormat matchFormat;
    private MatchState matchState;
    private MatchParams matchParams;
    private MatchEngineSavedState matchEngineSavedState;
    private MatchEngineThreadPool matchEngineThreadPool;

    private Markets calculatedMarkets;
    private MatchParams updatedMatchParams;
    private MatchEngineSavedState updatedMatchEngineSavedState;
    private MatchIncident matchIncident;
    private MatchIncidentResult matchIncidentResult;
    private boolean fatalError;
    private String fatalErrorCause;
    private long eventTier;
    private boolean free;

    /**
     * sets properties to sensible defaults;
     */
    public AlgoCalculator() {
        this(0, 6);
        free = true;
    }

    /**
     * @param id the Id of this instance of the calculator
     * @param nThreads No of threads the matchEngine can use
     */
    public AlgoCalculator(int id, int nThreads) {
        this.id = id;
        this.nThreads = nThreads;
        matchEngineThreadPool = new MatchEngineThreadPool(nThreads - 1);
        info("AlgoCalculator instantiated with %d threads", nThreads);
    }


    /**
     * sets the parameters for use by the call() method
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void setCalculationParams(PriceCalcRequest request) {
        try {
            this.matchEngineClass = (Class<? extends MatchEngine>) Class.forName(request.getMatchEngineClassName());
        } catch (ClassNotFoundException e) {
            error(String.format("Invalid match engine class name: %s", request.getMatchEngineClassName()));
            throw new IllegalArgumentException(e);
        }
        this.calcRequestCause = request.getCalcRequestCause();
        this.matchFormat = request.getMatchFormat();
        this.matchState = request.getMatchState();
        this.uniqueRequestId = request.getUniqueRequestId();
        /*
         * always apply bias when doing price calc
         */
        this.matchParams = request.getMatchParams().generateXxxMatchParams();
        matchParams.setApplyBias(true);
        this.matchIncident = request.getMatchIncident();
        this.matchIncidentResult = request.getMatchIncidentResult();
        this.matchEngineSavedState = request.getMatchEngineSavedState();
        this.eventId = request.getEventId();
        this.eventTier = request.getEventSettings().getEventTier();
    }

    @Override
    public Integer call() throws Exception {
        MatchEngine matchEngine = null;
        try {
            Constructor<?> constructor = matchEngineClass.getConstructor(MatchFormat.class);
            matchEngine = (MatchEngine) constructor.newInstance(matchFormat);
        } catch (Exception e) {
            error(String.format("Invalid constructor for %s match engine", matchEngineClass.toString()));
            throw new IllegalArgumentException(e);
        }
        if (matchEngine instanceof MonteCarloMatchEngine) {
            /*
             * specify the resources to use
             */
            ((MonteCarloMatchEngine) matchEngine).initialiseMatchEngine(nThreads, matchEngineThreadPool);
        }
        info("EventId: %d, uniqueRequestId: %s. New price calc request received.  Match engine to be used: %s", eventId,
                        uniqueRequestId, matchEngine.getClass());
        info("CalcRequestCause: " + calcRequestCause);
        info("MatchIncident: " + matchIncident);
        info("MatchIncidentResult: " + matchIncidentResult);
        info("MatchState: " + matchState.generateSimpleMatchState());
        info("MatchParams: " + matchParams);
        info("MatchEngineSavedState: " + matchEngineSavedState);

        /*
         * set the state, the params and run the calc
         */
        matchEngine.setMatchState(matchState);
        matchEngine.setMatchParams(matchParams);
        matchEngine.setCalcRequestCause(calcRequestCause);
        matchEngine.setMatchEngineSavedState(matchEngineSavedState);
        matchEngine.setMatchIncident(matchIncident);
        matchEngine.setMatchIncidentResult(matchIncidentResult);
        matchEngine.setEventTier(eventTier);
        try {
            if (!matchState.isMatchCompleted()) {
                matchEngine.calculate();
                calculatedMarkets = matchEngine.getCalculatedMarkets();
                updatedMatchParams = matchEngine.getUpdatedMatchParams();
                updatedMatchEngineSavedState = matchEngine.getMatchEngineSavedState();
            } else {
                calculatedMarkets = new Markets(); // return empty set of
                                                   // markets
                updatedMatchParams = matchParams;
                updatedMatchEngineSavedState = matchEngineSavedState;
            }
            fatalError = false;
        } catch (Exception e) {
            calculatedMarkets = new Markets(); // return empty set of markets
            updatedMatchParams = matchParams;
            updatedMatchEngineSavedState = matchEngineSavedState;
            fatalError = true;
            fatalErrorCause = e.getMessage();
            error("Fatal calc exception for event: %d, uniqueRequestId: %s", eventId, uniqueRequestId);
            error("MatchState:\n" + matchState.generateSimpleMatchState());
            error("MatchParams: \n" + matchParams);
            error(e);
            throw (e);
        }
        /*
         * return the results
         */
        info("EventId: %d, uniqueRequestId: %s.  Price calc successfully completed.  #markets published: %d", eventId,
                        uniqueRequestId, calculatedMarkets.size());
        return (Integer) id;
    }

    public Markets getCalculatedMarkets() {
        return calculatedMarkets;
    }

    public MatchParams getUpdatedMatchParams() {
        return updatedMatchParams;
    }

    public MatchEngineSavedState getUpdatedMatchEngineSavedState() {
        return updatedMatchEngineSavedState;
    }

    public long getEventId() {
        return eventId;
    }

    public String getUniqueRequestId() {
        return this.uniqueRequestId;
    }


    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isFatalError() {
        return fatalError;
    }

    public String getFatalErrorCause() {
        return fatalErrorCause;
    }

    /**
     * if called before closing disposing of the object releases the threadpool resource
     */
    public void close() {
        matchEngineThreadPool.close();
    }



}
