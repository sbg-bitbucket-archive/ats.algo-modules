package ats.algo.core.request;

import ats.algo.algomanager.AbstractPriceCalculator;

public class ClassifiedPriceCalcRequest extends AbstractAlgoRequest {
    private static final long serialVersionUID = 1L;
    private PriceCalcRequest request;
    private Class<? extends AbstractPriceCalculator> clazz;

    public ClassifiedPriceCalcRequest(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {
        this.request = request;
        this.clazz = clazz;
    }

    public AbstractPriceCalculator getPriceCalculator() {
        try {
            return (AbstractPriceCalculator) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public long getEventId() {
        return request.getEventId();
    }

    @Override
    public String getUniqueRequestId() {
        return request.getUniqueRequestId();
    }

    public PriceCalcRequest getRequest() {
        return request;
    }

    public PriceCalcResponse executeCalculation() {
        if (requestTime == 0) {
            requestTime = System.currentTimeMillis();
        }
        PriceCalcResponse response = getPriceCalculator().calculate(request);
        long timePriceCalcRequestReceived = response.getTimePriceCalcRequestReceived();
        if (timePriceCalcRequestReceived <= 0) {
            timePriceCalcRequestReceived = request.getRequestTime();
            if (timePriceCalcRequestReceived <= 0) {
                timePriceCalcRequestReceived = requestTime;
            }
            response.setTimePriceCalcRequestReceived(timePriceCalcRequestReceived);
        }
        if (response.getTimePriceCalcResponseIssued() <= 0) {
            response.setTimePriceCalcResponseIssued(System.currentTimeMillis());
        }
        return response;
    }
}
