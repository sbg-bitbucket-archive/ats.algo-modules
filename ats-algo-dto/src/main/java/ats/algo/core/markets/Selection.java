package ats.algo.core.markets;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.markets.SuspensionStatus;
import ats.core.util.json.JsonUtil;

public class Selection implements Serializable {

    private static final long serialVersionUID = 1L;
    private SuspensionStatus suspensionStatus;
    private double prob;

    @JsonCreator
    /*
     * @param suspensionStatus
     * 
     * @param prob
     */
    public Selection(@JsonProperty("suspensionStatus") SuspensionStatus suspensionStatus,
                    @JsonProperty("prob") double prob) {
        this.prob = prob;
        this.suspensionStatus = suspensionStatus;
    }

    public Selection(double prob) {
        this.prob = prob;
        this.suspensionStatus = SuspensionStatus.OPEN;
    }


    public double getProb() {
        return prob;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    public void setSuspensionStatus(SuspensionStatus suspensionStatus) {
        this.suspensionStatus = suspensionStatus;
    }


    public SuspensionStatus getSuspensionStatus() {
        return suspensionStatus;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(prob);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((suspensionStatus == null) ? 0 : suspensionStatus.hashCode());
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
        Selection other = (Selection) obj;
        if (Double.doubleToLongBits(prob) != Double.doubleToLongBits(other.prob))
            return false;
        if (suspensionStatus != other.suspensionStatus)
            return false;
        return true;
    }


    public Selection copy() {
        Selection cc = new Selection(this.suspensionStatus, this.prob);
        return cc;
    }
}
