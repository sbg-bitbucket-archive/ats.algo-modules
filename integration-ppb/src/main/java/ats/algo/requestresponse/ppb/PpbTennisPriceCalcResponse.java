package ats.algo.requestresponse.ppb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.request.AbstractAlgoResponse;

public class PpbTennisPriceCalcResponse extends AbstractAlgoResponse {
    private static final long serialVersionUID = 1L;

    private PpbMarkets ppbMarkets;
    private String ppbSavedState;
    private long timePriceCalcRequestReceived;
    private long timePriceCalcResponseIssued;

    @JsonCreator
    /**
     * 
     * @param markets
     * @param timePriceCalcRequestReceived
     * @param timePriceCalcResponseIssued
     */
    public PpbTennisPriceCalcResponse(@JsonProperty("ppbMarkets") PpbMarkets ppbMarkets,
                    @JsonProperty("ppbSavedState") String ppbSavedState,
                    @JsonProperty("timePriceCalcRequestReceived") long timePriceCalcRequestReceived,
                    @JsonProperty("timePriceCalcResponseIssued") long timePriceCalcResponseIssued) {
        super();
        this.ppbMarkets = ppbMarkets;
        this.ppbSavedState = ppbSavedState;
        this.timePriceCalcRequestReceived = timePriceCalcRequestReceived;
        this.timePriceCalcResponseIssued = timePriceCalcResponseIssued;
    }

    public PpbMarkets getPpbMarkets() {
        return ppbMarkets;
    }

    public long getTimePriceCalcRequestReceived() {
        return timePriceCalcRequestReceived;
    }

    public long getTimePriceCalcResponseIssued() {
        return timePriceCalcResponseIssued;
    }

    public String getPpbSavedState() {
        return ppbSavedState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((ppbMarkets == null) ? 0 : ppbMarkets.hashCode());
        result = prime * result + ((ppbSavedState == null) ? 0 : ppbSavedState.hashCode());
        result = prime * result + (int) (timePriceCalcRequestReceived ^ (timePriceCalcRequestReceived >>> 32));
        result = prime * result + (int) (timePriceCalcResponseIssued ^ (timePriceCalcResponseIssued >>> 32));
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
        PpbTennisPriceCalcResponse other = (PpbTennisPriceCalcResponse) obj;
        if (ppbMarkets == null) {
            if (other.ppbMarkets != null)
                return false;
        } else if (!ppbMarkets.equals(other.ppbMarkets))
            return false;
        if (ppbSavedState == null) {
            if (other.ppbSavedState != null)
                return false;
        } else if (!ppbSavedState.equals(other.ppbSavedState))
            return false;
        if (timePriceCalcRequestReceived != other.timePriceCalcRequestReceived)
            return false;
        if (timePriceCalcResponseIssued != other.timePriceCalcResponseIssued)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PpbTennisPriceCalcResponse [ppbMarkets=" + ppbMarkets + ", ppbSavedState=" + ppbSavedState
                        + ", timePriceCalcRequestReceived=" + timePriceCalcRequestReceived
                        + ", timePriceCalcResponseIssued=" + timePriceCalcResponseIssued + "]";
    }


}
