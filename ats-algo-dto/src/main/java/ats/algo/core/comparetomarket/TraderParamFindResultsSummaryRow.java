package ats.algo.core.comparetomarket;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TraderParamFindResultsSummaryRow implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean sourcePricesOk;
    private ParamFindResultsStatus successStatus;
    private String paramFindResultsSummary;

    /**
     *
     * @param sourcePricesOk
     * @param successStatus
     * @param summary
     */
    @JsonCreator
    public TraderParamFindResultsSummaryRow(@JsonProperty("sourcePricesOk") boolean sourcePricesOk,
                    @JsonProperty("successStatus") ParamFindResultsStatus successStatus,
                    @JsonProperty("summary") String summary) {
        super();
        this.sourcePricesOk = sourcePricesOk;
        this.successStatus = successStatus;
        this.paramFindResultsSummary = summary;

    }

    public boolean isSourcePricesOk() {
        return sourcePricesOk;
    }

    public void setSourcePricesOk(boolean sourcePricesOk) {
        this.sourcePricesOk = sourcePricesOk;
    }

    public ParamFindResultsStatus getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(ParamFindResultsStatus successStatus) {
        this.successStatus = successStatus;
    }

    public String getParamFindResultsSummary() {
        return paramFindResultsSummary;
    }

    public void setParamFindResultsSummary(String paramFindResultsSummary) {
        this.paramFindResultsSummary = paramFindResultsSummary;
    }

    public String toString() {
        return String.format("Param find completed.  Source prices ok: %b, PF summary status: %s.  %s", sourcePricesOk,
                        successStatus.toString(), paramFindResultsSummary);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((paramFindResultsSummary == null) ? 0 : paramFindResultsSummary.hashCode());
        result = prime * result + (sourcePricesOk ? 1231 : 1237);
        result = prime * result + ((successStatus == null) ? 0 : successStatus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TraderParamFindResultsSummaryRow other = (TraderParamFindResultsSummaryRow) obj;
        if (paramFindResultsSummary == null) {
            if (other.paramFindResultsSummary != null)
                return false;
        } else if (!paramFindResultsSummary.equals(other.paramFindResultsSummary))
            return false;
        if (sourcePricesOk != other.sourcePricesOk)
            return false;
        if (successStatus != other.successStatus)
            return false;
        return true;
    }



}
