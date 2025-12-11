package ats.algo.algomanager;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.comparetomarket.MarketPricesList;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.comparetomarket.ParamFinder;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.matchengineframework.MatchEngine;
import ats.algo.montecarloframework.MatchEngineThreadPool;
import ats.algo.montecarloframework.matchengine.MonteCarloMatchEngine;
import ats.core.AtsBean;

/**
 * executes a param find for any supportedSport
 *
 * @author Geoff
 *
 */
public class AlgoParamFinder extends AtsBean implements Callable<Integer> {

    private int id;
    private int nThreads;
    private long eventId;
    private String uniqueRequestId;

    private Class<? extends MatchEngine> matchEngineClass;
    private MatchFormat matchFormat;
    private MatchState matchState;
    private MatchParams matchParams;
    private MarketPricesList marketPricesList;
    private MatchEngineSavedState matchEngineSavedState;

    private ParamFindResults paramFindResults;
    private MatchEngineThreadPool matchEngineThreadPool;
    private boolean free;
    private MatchParams calculatedMatchParams;
    private boolean fatalError;
    private String fatalErrorCause;

    public AlgoParamFinder() {
        this(0, 6);
        free = true;
    }

    /**
     * @param id the Id of this instance of the paramFinder
     * @param nThreads No of threads the matchEngine can use
     */

    public AlgoParamFinder(int id, int nThreads) {
        this.id = id;
        this.nThreads = nThreads;
        matchEngineThreadPool = new MatchEngineThreadPool(nThreads - 1);
        info("ParamFinder instantiated with %d threads", nThreads);

    }


    /**
     * sets the parameters for use by the call() method
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void setParamFindParams(ParamFindRequest request) {
        try {
            this.matchEngineClass = (Class<? extends MatchEngine>) Class.forName(request.getMatchEngineClassName());
        } catch (ClassNotFoundException e) {
            error(String.format("Invalid match engine class name: %s", request.getMatchEngineClassName()));
            throw new IllegalArgumentException(e);
        }
        this.matchFormat = request.getMatchFormat();
        this.matchState = request.getMatchState();
        /*
         * never apply bias when doing param find
         */
        this.matchParams = request.getMatchParams().generateXxxMatchParams();
        matchParams.setApplyBias(false);
        this.marketPricesList = request.getMarketPricesList();
        this.matchEngineSavedState = request.getMatchEngineSavedState();
        this.eventId = request.getEventId();
        this.uniqueRequestId = request.getUniqueRequestId();
    }

    @Override
    public Integer call() throws Exception {
        // TODO add logic to handle case where CALCTYPE in request is external model
        MatchEngine matchEngine = null;
        try {
            /*
             * first see if the param finder specific version of a constrctor is available. If not use the normal one
             */
            Constructor<? extends MatchEngine> constructor =
                            matchEngineClass.getConstructor(MatchFormat.class, boolean.class);
            matchEngine = (MatchEngine) constructor.newInstance(matchFormat, true);
        } catch (Exception e) {
            /*
             * do nothing
             */
        }
        if (matchEngine == null) {
            try {
                Constructor<? extends MatchEngine> constructor = matchEngineClass.getConstructor(MatchFormat.class);
                matchEngine = (MatchEngine) constructor.newInstance(matchFormat);

            } catch (Exception e) {
                error(String.format("Invalid constructor for %s match engine", matchEngineClass.toString()));
                throw new IllegalArgumentException(e);
            }
        }
        if (matchEngine instanceof MonteCarloMatchEngine) {
            /*
             * specify the resources to use
             */
            ((MonteCarloMatchEngine) matchEngine).initialiseMatchEngine(nThreads, matchEngineThreadPool);
        }
        info("New param find request for %d, %s.  Match engine to be used: %s", eventId, uniqueRequestId,
                        matchEngine.getClass());
        info("MatchFormat: " + matchFormat);
        info("MatchState: " + matchState.generateSimpleMatchState());
        info("MatchParams: " + matchParams);
        info("MarketPricesList: " + marketPricesList);
        info("MatchEngineSavedState: " + matchEngineSavedState);

        calculatedMatchParams = null;
        MatchParams savedInitialMatchParams = matchParams.copy();
        try {
            matchEngine.setMatchParams(matchParams);
            matchEngine.setMatchState(matchState);
            matchEngine.setCalcRequestCause(CalcRequestCause.PARAM_FIND);
            matchEngine.setMatchEngineSavedState(matchEngineSavedState);
            ParamFinder paramFinder = new ParamFinder(matchEngine);
            paramFinder.setMatchParameters(matchParams);
            paramFinder.setMarketPricesList(marketPricesList);
            paramFindResults = paramFinder.calculate();
            calculatedMatchParams = paramFinder.getMatchParams();
            if (paramFindResults.isSuccess()) {
                debug("Successful param find for %d, %s after %s iterations", eventId, uniqueRequestId,
                                paramFindResults.getnIterations());
            } else {
                debug("Failed param find for %d, %s", eventId, uniqueRequestId);
                calculatedMatchParams = savedInitialMatchParams;
            }
            fatalError = false;
            debug("ParamFindResults: " + paramFindResults);
            debug("Updated MatchParams: " + calculatedMatchParams);
        } catch (Exception e) {
            paramFindResults = new ParamFindResults();
            fatalError = true;
            fatalErrorCause = e.getMessage();
            calculatedMatchParams = savedInitialMatchParams;
            error("Fatal param find exception for event: %d, uniqueRequestId: %s", eventId, uniqueRequestId);
            error("MatchState: " + matchState.generateSimpleMatchState());
            error("MatchParams: " + savedInitialMatchParams);
            error("MarketPricesList: " + marketPricesList);
            error("MatchEngineSavedState: " + matchEngineSavedState);
            error(e);
        }
        if (calculatedMatchParams == null) {
            /*
             * if not calculated then set output to the same as the input
             */
            calculatedMatchParams = savedInitialMatchParams;
        }

        debug("Param find finished for event: %d, uniqueRequestId: %s", eventId, uniqueRequestId);

        return (Integer) id;
    }

    public ParamFindResults getParamFindResults() {
        return paramFindResults;
    }

    public MatchParams getCalculatedMatchParams() {
        return calculatedMatchParams;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public long getEventId() {
        return eventId;
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

    public String getUniqueRequestId() {
        return uniqueRequestId;
    }
}
