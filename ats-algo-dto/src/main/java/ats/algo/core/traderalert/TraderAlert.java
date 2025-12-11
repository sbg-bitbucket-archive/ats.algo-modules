package ats.algo.core.traderalert;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.core.util.json.JsonUtil;

/**
 * immutable class for containing a trader alert
 * 
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraderAlert implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum TraderAlertType {
        CONNECTION_BROKEN,
        MATCH_STATE_MISMATCH,
        MATCH_STATE_MISMATCH_CLEARING,
        MATCH_STATE_MISMATCH_CLEARED,
        CONSECUTIVE_FAILED_PARAMS,
        FIRST_INPLAY_PF,
        ABANDONED_EVENT,
        EVENT_INPLAY,
        FEED_DISCONNECTED,
        RAIN,
        TOILET_BREAK,
        HEAT_DELAY,
        MEDICAL_TIMEOUT,
        ON_COURT_COACHING,
        CHALLENGE,
        INPUT_PRICES_MISSING_WARNING,
        INPUT_PRICES_MISSING_DANGER,
        INPUT_INCIDENT_MISSING_WARNING,
        INPUT_INCIDENT_MISSING_DANGER,
        MATCHFORMAT_WARNING,
        LADCOR_TRADER_ALERT

    }

    private TraderAlertType traderAlertType;
    private String alertText;
    private Map<String, String> alertAttributes;

    @JsonCreator
    public TraderAlert(@JsonProperty("traderAlertType") TraderAlertType traderAlertType,
                    @JsonProperty("alertText") String alertText,
                    @JsonProperty("alertAttributes") Map<String, String> alertAttributes) {
        super();
        this.traderAlertType = traderAlertType;
        this.alertText = alertText;
        this.alertAttributes = alertAttributes;
    }

    public TraderAlertType getTraderAlertType() {
        return traderAlertType;
    }

    public String getAlertText() {
        return alertText;
    }

    public Map<String, String> getAlertAttributes() {
        return alertAttributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alertAttributes == null) ? 0 : alertAttributes.hashCode());
        result = prime * result + ((alertText == null) ? 0 : alertText.hashCode());
        result = prime * result + ((traderAlertType == null) ? 0 : traderAlertType.hashCode());
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
        TraderAlert other = (TraderAlert) obj;
        if (alertAttributes == null) {
            if (other.alertAttributes != null)
                return false;
        } else if (!alertAttributes.equals(other.alertAttributes))
            return false;
        if (alertText == null) {
            if (other.alertText != null)
                return false;
        } else if (!alertText.equals(other.alertText))
            return false;
        if (traderAlertType != other.traderAlertType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }
}
