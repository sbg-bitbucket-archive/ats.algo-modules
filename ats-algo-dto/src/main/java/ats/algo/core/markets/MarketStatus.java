package ats.algo.core.markets;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private SuspensionStatus suspensionStatus;
    private String suspensionStatusRuleName;
    private String suspensionStatusReason;

    public MarketStatus() {
        this(SuspensionStatus.OPEN, "Default", "Default");
    }

    public MarketStatus(SuspensionStatus suspensionStatus, String suspensionStatusRuleName,
                    String suspensionStatusReason) {
        super();
        this.suspensionStatus = suspensionStatus;
        this.suspensionStatusRuleName = suspensionStatusRuleName;
        this.suspensionStatusReason = suspensionStatusReason;
    }

    /**
     * gets the status of this market - e.g. whether to display or not
     * 
     * @return
     */
    public SuspensionStatus getSuspensionStatus() {
        return suspensionStatus;
    }

    /**
     * sets the status of this market - e.g. whether to display or not
     * 
     * @return
     */
    public void setSuspensionStatus(SuspensionStatus suspensionStatus) {
        this.suspensionStatus = suspensionStatus;
    }

    /**
     * gets a descriptive string which explains the reason for the current market status - e.g. what triggered the
     * suspension
     * 
     * @return
     */
    public String getSuspensionStatusReason() {
        return suspensionStatusReason;
    }

    /**
     * sets a descriptive string which explains the reason for the current market status - e.g. e.g. what triggered the
     * suspension
     * 
     * @param suspensionStatusReason
     */
    public void setSuspensionStatusReason(String suspensionStatusReason) {
        this.suspensionStatusReason = suspensionStatusReason;
    }

    /**
     * gets the name of the rule that set the status and statusReason
     * 
     * @return
     */
    public String getSuspensionStatusRuleName() {
        return suspensionStatusRuleName;
    }

    /**
     * sets the name of the rule that set the status and statusReason
     * 
     * @return
     */
    public void setSuspensionStatusRuleName(String suspensionStatusRuleName) {
        this.suspensionStatusRuleName = suspensionStatusRuleName;
    }

    public void reset() {
        suspensionStatus = SuspensionStatus.OPEN;
        suspensionStatusRuleName = "Default";
        suspensionStatusReason = "Default";
    }

    public void setStatusIfHigherPriority(String ruleName, SuspensionStatus targetStatus, String reason) {
        if (targetStatus == null || suspensionStatus == null)
            targetStatus = SuspensionStatus.SUSPENDED_UNDISPLAY;
        if (targetStatus.isHigherPriorityThan(this.suspensionStatus)) {
            setSuspensionStatus(targetStatus);
            setSuspensionStatusRuleName(ruleName);
            setSuspensionStatusReason(reason);
        }
    }

    public MarketStatus copy() {
        return new MarketStatus(this.suspensionStatus, this.suspensionStatusRuleName, this.suspensionStatusReason);
    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
        // return "MarketStatus [suspensionStatus=" + suspensionStatus + ", suspensionStatusRuleName="
        // + suspensionStatusRuleName + ", suspensionStatusReason=" + suspensionStatusReason + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((suspensionStatus == null) ? 0 : suspensionStatus.hashCode());
        result = prime * result + ((suspensionStatusReason == null) ? 0 : suspensionStatusReason.hashCode());
        result = prime * result + ((suspensionStatusRuleName == null) ? 0 : suspensionStatusRuleName.hashCode());
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
        MarketStatus other = (MarketStatus) obj;
        if (suspensionStatus != other.suspensionStatus)
            return false;
        if (suspensionStatusReason == null) {
            if (other.suspensionStatusReason != null)
                return false;
        } else if (!suspensionStatusReason.equals(other.suspensionStatusReason))
            return false;
        if (suspensionStatusRuleName == null) {
            if (other.suspensionStatusRuleName != null)
                return false;
        } else if (!suspensionStatusRuleName.equals(other.suspensionStatusRuleName))
            return false;
        return true;
    }



}
