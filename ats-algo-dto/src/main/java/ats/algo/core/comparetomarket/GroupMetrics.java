package ats.algo.core.comparetomarket;

/**
 * holds the data generated during the calculateCostMetrics method for the various MarketGroups
 * 
 * @author Geoff
 *
 */
public class GroupMetrics {
    double cumCost;
    double cumMinPossibleCost;
    double cumWeight;

    GroupMetrics() {
        cumCost = 0.0;
        cumMinPossibleCost = 0.0;
        cumWeight = 0.0;
    }

    double cost() {
        return Math.sqrt(cumCost / cumWeight);
    }

    double minPossibleCost() {
        return Math.sqrt(cumMinPossibleCost / cumWeight);
    }

    public double getCumCost() {
        return cumCost;
    }

    public void setCumCost(double cumCost) {
        this.cumCost = cumCost;
    }

    public double getCumMinPossibleCost() {
        return cumMinPossibleCost;
    }

    public void setCumMinPossibleCost(double cumMinPossibleCost) {
        this.cumMinPossibleCost = cumMinPossibleCost;
    }

    public double getCumWeight() {
        return cumWeight;
    }

    public void setCumWeight(double cumWeight) {
        this.cumWeight = cumWeight;
    }

    public boolean paramFindNeeded() {
        return (cost() - minPossibleCost() > CompareToMarket.getParamFindKickoffThreshold());
    }
}
