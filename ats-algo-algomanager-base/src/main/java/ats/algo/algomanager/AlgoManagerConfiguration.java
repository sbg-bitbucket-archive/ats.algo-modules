package ats.algo.algomanager;


import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.core.AtsBean;

/**
 * defines the "wiring" between AlgoManager, AlgoCalculator(s) and AlgoParamFinder(s). This may be all single threaded,
 * or distributed across multiple processors. The
 *
 * @author Geoff
 *
 */
public abstract class AlgoManagerConfiguration extends AtsBean {

    @FunctionalInterface
    public interface HandlePriceCalcResponse {
        public void handle(long eventId, PriceCalcResponse response);
    }

    @FunctionalInterface
    public interface HandlePriceCalcError {
        public void handle(long eventId, String uniqueRequestId, String errorCause);
    }

    @FunctionalInterface
    public interface HandleParamFindResponse {
        public void handle(long eventId, ParamFindResponse response);
    }

    @FunctionalInterface
    public interface HandleParamFindError {
        public void handle(long eventId, String uniqueRequestId, String errorCause);
    }

    protected HandlePriceCalcResponse algoHandlePriceCalcResponse;
    protected HandleParamFindResponse algoHandleParamFindResponse;
    protected HandlePriceCalcError algoHandlePriceCalcError;
    protected HandleParamFindError algoHandleParamFindError;

    public void setHandlePriceCalcResponse(HandlePriceCalcResponse handlePriceCalcResponse) {
        this.algoHandlePriceCalcResponse = handlePriceCalcResponse;
    }

    public void setHandleParamFindResponse(HandleParamFindResponse handleParamFindResponse) {
        this.algoHandleParamFindResponse = handleParamFindResponse;
    }

    public void setHandlePriceCalcError(HandlePriceCalcError algoHandlePriceCalcError) {
        this.algoHandlePriceCalcError = algoHandlePriceCalcError;
    }

    public void setHandleParamFindError(HandleParamFindError algoHandleParamFindError) {
        this.algoHandleParamFindError = algoHandleParamFindError;
    }

    public abstract void schedulePriceCalc(PriceCalcRequest request);

    public abstract void scheduleExternalPriceCalc(PriceCalcRequest request);

    public abstract void scheduleParamFind(ParamFindRequest request);

    public abstract void scheduleExternalParamFind(ParamFindRequest request);

    public abstract void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz);

    public abstract void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz);

    public abstract void abandonPriceCalc(long eventId, String uniqueRequestId);

    public abstract void abandonParamFind(long eventId, String uniqueRequestId);


    public void handlePriceCalcResponse(long eventId, PriceCalcResponse response) {
        this.algoHandlePriceCalcResponse.handle(eventId, response);
    }

    public void handleParamFindResponse(long eventId, ParamFindResponse response) {
        this.algoHandleParamFindResponse.handle(eventId, response);
    }


    /**
     * called when we know that a connection to an active MQ broker is going to be needed to support connection to the
     * external model. May be called more than once, so should take no action if connection already established
     * 
     * @param url the URL of the ActiveMQ broker
     * @return true if connection successfully established, false otherwise
     */
    public abstract boolean establishExternalModelConnection(String url);

    public abstract int getNoAlgoCalculators();

    public abstract int getNoAlgoParamFinders();

    public abstract int getPriceCalcQueueSize();

    public abstract int getParamFindQueueSize();

    private volatile boolean flushMemoryFlag;

    /**
     * writing volatile variable to main memory flushes all other variables that have changed in this thread to main
     * memory at the same time
     */
    void flushMemory() {
        flushMemoryFlag = !flushMemoryFlag;
    }

    /**
     * return a string describing the current state of the object
     *
     * @return
     */
    String getStatus() {
        return null;
    }

    /**
     * will be executed when the object is finished with
     */
    void close() {

    }



}
