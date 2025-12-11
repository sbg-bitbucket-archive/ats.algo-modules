package ats.algo.core.comparetomarket;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.genericsupportfunctions.GCMath;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TraderParamFindResultsDetailRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean shouldHighlight;
    private String source;
    private String marketType;
    private String selection;
    private double weight;
    private double targetProb;
    private double ourProbBeforePF;
    private double ourProbAfterPF;

    /**
     *
     * @param source
     * @param marketType
     * @param selection
     * @param weight
     * @param targetProb
     * @param ourProbBeforePF
     * @param ourProbAfterPF
     */
    @JsonCreator
    public TraderParamFindResultsDetailRow(@JsonProperty("shouldHighlight") Boolean shouldHighlight,
                    @JsonProperty("source") String source, @JsonProperty("marketType") String marketType,
                    @JsonProperty("selection") String selection, @JsonProperty("weight") double weight,
                    @JsonProperty("targetProb") double targetProb,
                    @JsonProperty("ourProbBeforePF") double ourProbBeforePF,
                    @JsonProperty("ourProbAfterPF") double ourProbAfterPF) {
        super();
        this.shouldHighlight = shouldHighlight;
        this.source = source;
        this.marketType = marketType;
        this.selection = selection;
        this.weight = GCMath.round(weight, 1);
        this.targetProb = GCMath.round(targetProb, 2);
        this.ourProbBeforePF = GCMath.round(ourProbBeforePF, 2);
        this.ourProbAfterPF = GCMath.round(ourProbAfterPF, 2);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTargetProb() {
        return targetProb;
    }

    public void setTargetProb(double targetProb) {
        this.targetProb = targetProb;
    }

    public double getOurProbBeforePF() {
        return ourProbBeforePF;
    }

    public void setOurProbBeforePF(double ourProbBeforePF) {
        this.ourProbBeforePF = ourProbBeforePF;
    }

    public double getOurProbAfterPF() {
        return ourProbAfterPF;
    }

    public void setOurProbAfterPF(double ourProbAfterPF) {
        this.ourProbAfterPF = ourProbAfterPF;
    }



    public boolean isShouldHighlight() {
        return shouldHighlight;
    }

    public void setShouldHighlight(boolean shouldHighlight) {
        this.shouldHighlight = shouldHighlight;
    }

    public String toString() {
        return String.format("%-10s %-15s %-30s Weight: %.1f.  Target prob: %.2f. Our prob %.2f -> %.2f", source,
                        marketType, selection, weight, targetProb, ourProbBeforePF, ourProbAfterPF);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketType == null) ? 0 : marketType.hashCode());
        long temp;
        temp = Double.doubleToLongBits(ourProbAfterPF);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ourProbBeforePF);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((selection == null) ? 0 : selection.hashCode());
        result = prime * result + (shouldHighlight ? 1231 : 1237);
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        temp = Double.doubleToLongBits(targetProb);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        TraderParamFindResultsDetailRow other = (TraderParamFindResultsDetailRow) obj;
        if (marketType == null) {
            if (other.marketType != null)
                return false;
        } else if (!marketType.equals(other.marketType))
            return false;
        if (Double.doubleToLongBits(ourProbAfterPF) != Double.doubleToLongBits(other.ourProbAfterPF))
            return false;
        if (Double.doubleToLongBits(ourProbBeforePF) != Double.doubleToLongBits(other.ourProbBeforePF))
            return false;
        if (selection == null) {
            if (other.selection != null)
                return false;
        } else if (!selection.equals(other.selection))
            return false;
        if (shouldHighlight != other.shouldHighlight)
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        if (Double.doubleToLongBits(targetProb) != Double.doubleToLongBits(other.targetProb))
            return false;
        if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
            return false;
        return true;
    }


}
