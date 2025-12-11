package ats.algo.core.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.comparetomarket.ParamFindResults;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamFindResponse extends AbstractAlgoResponse {
    private static final long serialVersionUID = 1L;

    private ParamFindResults paramFindResults;
    private GenericMatchParams genericMatchParams;
    private boolean doNotSchedulePriceCalc;

    /**
     * for Json use only
     */
    public ParamFindResponse() {

    }

    public ParamFindResponse(String uniqueRequestId, ParamFindResults paramFindResults,
                    GenericMatchParams genericMatchParams) {
        super(uniqueRequestId);
        this.paramFindResults = paramFindResults;
        this.genericMatchParams = genericMatchParams;
    }

    public void setParamFindResults(ParamFindResults paramFindResults) {
        this.paramFindResults = paramFindResults;
    }

    public ParamFindResults getParamFindResults() {
        return paramFindResults;
    }

    public void setGenericMatchParams(GenericMatchParams genericMatchParams) {
        this.genericMatchParams = genericMatchParams;
    }

    public GenericMatchParams getGenericMatchParams() {
        return genericMatchParams;
    }

    public boolean isDoNotSchedulePriceCalc() {
        return doNotSchedulePriceCalc;
    }

    public void setDoNotSchedulePriceCalc(boolean doNotSchedulePriceCalc) {
        this.doNotSchedulePriceCalc = doNotSchedulePriceCalc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (doNotSchedulePriceCalc ? 1231 : 1237);
        result = prime * result + ((genericMatchParams == null) ? 0 : genericMatchParams.hashCode());
        result = prime * result + ((paramFindResults == null) ? 0 : paramFindResults.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParamFindResponse other = (ParamFindResponse) obj;
        if (doNotSchedulePriceCalc != other.doNotSchedulePriceCalc)
            return false;
        if (genericMatchParams == null) {
            if (other.genericMatchParams != null)
                return false;
        } else if (!genericMatchParams.equals(other.genericMatchParams))
            return false;
        if (paramFindResults == null) {
            if (other.paramFindResults != null)
                return false;
        } else if (!paramFindResults.equals(other.paramFindResults))
            return false;
        return true;
    }

    public static ParamFindResponse generateFatalErrorResponse(String uniqueRequestId, String cause) {
        return (ParamFindResponse) AbstractAlgoResponse.generateFatalErrorResponse(uniqueRequestId, cause);
    }



}
