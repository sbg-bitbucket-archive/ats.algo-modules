package ats.algo.core.request;

import ats.algo.algomanager.AbstractParamFinder;

public class ClassifiedParamFindRequest extends AbstractAlgoRequest {
    private static final long serialVersionUID = 1L;
    private ParamFindRequest request;
    private Class<? extends AbstractParamFinder> clazz;

    public ClassifiedParamFindRequest(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {
        this.request = request;
        this.clazz = clazz;
    }

    public AbstractParamFinder getParamFinder() {
        try {
            return (AbstractParamFinder) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public long getEventId() {
        return request.getEventId();
    }

    public ParamFindRequest getRequest() {
        return request;
    }

    @Override
    public String getUniqueRequestId() {
        return request.getUniqueRequestId();
    }

    public ParamFindResponse executeCalculation() {
        return getParamFinder().calculate(request);
    }
}
