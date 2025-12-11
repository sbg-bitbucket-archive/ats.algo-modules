package ats.algo.core.comparetomarket;

class CompareToMarketMetricsItemInfo {
    String marketSequenceId;
    String lineId;
    String selectionName;
    double weight;
    double targetProb;
    double ourProb;

    public CompareToMarketMetricsItemInfo() {}

    /**
     * 
     * @param marketSequenceId
     * @param lineId
     * @param selectionName
     * @param weight
     * @param targetProb
     * @param ourProb
     */
    public CompareToMarketMetricsItemInfo(String marketSequenceId, String lineId, String selectionName, double weight,
                    double targetProb, double ourProb) {
        super();
        this.marketSequenceId = marketSequenceId;
        this.lineId = lineId;
        this.selectionName = selectionName;
        this.weight = weight;
        this.targetProb = targetProb;
        this.ourProb = ourProb;
    }

}
