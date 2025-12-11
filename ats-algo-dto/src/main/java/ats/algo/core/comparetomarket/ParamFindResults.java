package ats.algo.core.comparetomarket;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.MarketGroup;
import ats.algo.core.tradingrules.ParamFindingRuleResult;
import ats.algo.genericsupportfunctions.GCMath;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;

/**
 * used to return the results of the param finding
 *
 * @author Geoff
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamFindResults extends AtsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int PF_DECIMAL_PLACES = 3;

    private int nIterations;
    private double functionValueAtMinimum;
    private double minFunctionValueAchievable;
    private ParamFindResultsStatus paramFindResultsStatus;
    private boolean shouldSuspendMarkets;
    private TraderParamFindResultsDescription traderParamFindResultsDescription;
    private ParamFindResultsDescription paramFindResultsDescription;
    private boolean axisRotationReqd;
    private String detailedLog;
    private Set<MarketGroup> marketGroupsToSuspend;

    public ParamFindResults() {
        nIterations = 0;
        minFunctionValueAchievable = 0;
        traderParamFindResultsDescription = new TraderParamFindResultsDescription();
    }

    public ParamFindResults copy() {
        ParamFindResults copy = new ParamFindResults();
        copy.setEqualTo(this);
        return copy;
    }

    private void setEqualTo(ParamFindResults other) {
        nIterations = other.nIterations;
        functionValueAtMinimum = other.functionValueAtMinimum;
        minFunctionValueAchievable = other.minFunctionValueAchievable;
        paramFindResultsStatus = other.paramFindResultsStatus;
        shouldSuspendMarkets = other.shouldSuspendMarkets;
        paramFindResultsDescription = other.paramFindResultsDescription;
        traderParamFindResultsDescription = other.traderParamFindResultsDescription;

        axisRotationReqd = other.axisRotationReqd;
        detailedLog = other.detailedLog;
        marketGroupsToSuspend = other.marketGroupsToSuspend;
    }

    public ParamFindResultsDescription getParamFindResultsDescription() {
        return paramFindResultsDescription;
    }

    public void setParamFindResultsDescription(ParamFindResultsDescription paramFindResultsDescription) {
        this.paramFindResultsDescription = paramFindResultsDescription;
    }

    public boolean isAxisRotationReqd() {
        return axisRotationReqd;
    }

    public void setAxisRotationReqd(boolean axisRotationReqd) {
        this.axisRotationReqd = axisRotationReqd;
    }

    public ParamFindResultsStatus getParamFindResultsStatus() {
        return paramFindResultsStatus;
    }

    public void setParamFindResultsStatus(ParamFindResultsStatus paramFindResultsSummaryStatus) {
        this.paramFindResultsStatus = paramFindResultsSummaryStatus;
    }

    public boolean isShouldSuspendMarkets() {
        return shouldSuspendMarkets;
    }

    public void setShouldSuspendMarkets(boolean shouldSuspendMarkets) {
        this.shouldSuspendMarkets = shouldSuspendMarkets;
    }



    public Set<MarketGroup> getMarketGroupsToSuspend() {
        return marketGroupsToSuspend;
    }

    public void setMarketGroupsToSuspend(Set<MarketGroup> marketGroupsToSuspend) {
        this.marketGroupsToSuspend = marketGroupsToSuspend;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return paramFindResultsStatus != ParamFindResultsStatus.RED
                        && paramFindResultsStatus != ParamFindResultsStatus.BLUE;
    }

    public void setnIterations(int nIterations) {
        this.nIterations = nIterations;
    }

    public void setFunctionValueAtMinimum(double functionValueAtMinimum) {
        this.functionValueAtMinimum = functionValueAtMinimum;
    }

    public int getnIterations() {
        return nIterations;
    }

    public double getFunctionValueAtMinimum() {
        return functionValueAtMinimum;
    }

    public double getMinFunctionValueAchievable() {
        return minFunctionValueAchievable;
    }

    public void setMinFunctionValueAchievable(double minFunctionValueAchievable) {
        this.minFunctionValueAchievable = minFunctionValueAchievable;
    }

    public String getDetailedLog() {
        return detailedLog;
    }

    public void setDetailedLog(String detailedLog) {
        this.detailedLog = detailedLog;
    }

    public TraderParamFindResultsDescription getTraderParamFindResultsDescription() {
        return traderParamFindResultsDescription;
    }

    public void setTraderParamFindResultsDescription(
                    TraderParamFindResultsDescription traderParamFindResultsDescription) {
        this.traderParamFindResultsDescription = traderParamFindResultsDescription;
    }

    public void convertTraderPfResultsDescriptionToLegacyDescription() {
        paramFindResultsDescription = traderParamFindResultsDescription.convertToLegacy();
        traderParamFindResultsDescription = null;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (axisRotationReqd ? 1231 : 1237);
        result = prime * result + ((detailedLog == null) ? 0 : detailedLog.hashCode());
        long temp;
        temp = Double.doubleToLongBits(functionValueAtMinimum);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((marketGroupsToSuspend == null) ? 0 : marketGroupsToSuspend.hashCode());
        temp = Double.doubleToLongBits(minFunctionValueAchievable);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + nIterations;
        result = prime * result + ((paramFindResultsDescription == null) ? 0 : paramFindResultsDescription.hashCode());
        result = prime * result + ((paramFindResultsStatus == null) ? 0 : paramFindResultsStatus.hashCode());
        result = prime * result + (shouldSuspendMarkets ? 1231 : 1237);
        result = prime * result + ((traderParamFindResultsDescription == null) ? 0
                        : traderParamFindResultsDescription.hashCode());
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
        ParamFindResults other = (ParamFindResults) obj;
        if (axisRotationReqd != other.axisRotationReqd)
            return false;
        if (detailedLog == null) {
            if (other.detailedLog != null)
                return false;
        } else if (!detailedLog.equals(other.detailedLog))
            return false;
        if (Double.doubleToLongBits(functionValueAtMinimum) != Double.doubleToLongBits(other.functionValueAtMinimum))
            return false;
        if (marketGroupsToSuspend == null) {
            if (other.marketGroupsToSuspend != null)
                return false;
        } else if (!marketGroupsToSuspend.equals(other.marketGroupsToSuspend))
            return false;
        if (Double.doubleToLongBits(minFunctionValueAchievable) != Double
                        .doubleToLongBits(other.minFunctionValueAchievable))
            return false;
        if (nIterations != other.nIterations)
            return false;
        if (paramFindResultsDescription == null) {
            if (other.paramFindResultsDescription != null)
                return false;
        } else if (!paramFindResultsDescription.equals(other.paramFindResultsDescription))
            return false;
        if (paramFindResultsStatus != other.paramFindResultsStatus)
            return false;
        if (shouldSuspendMarkets != other.shouldSuspendMarkets)
            return false;
        if (traderParamFindResultsDescription == null) {
            if (other.traderParamFindResultsDescription != null)
                return false;
        } else if (!traderParamFindResultsDescription.equals(other.traderParamFindResultsDescription))
            return false;
        return true;
    }

    /**
     * 
     * @param eventId
     * @param requestId
     * @param requestTime
     * @param ruleResult
     * @return
     */
    public static ParamFindResults createFromParamFindingRuleResult(ParamFindingRuleResult ruleResult) {
        ParamFindResults results = new ParamFindResults();
        results.setParamFindResultsStatus(ruleResult.getResultColour());
        results.addResultSummaryInfo(false, ruleResult.getResultColour(), ruleResult.getResultDescription());
        return results;

    }

    /**
     * Adds summary information about this param find that will be relayed to the trader
     * 
     * @param sourcePricesOk true if the source Data was self-consistent, false if big discrepancies between prices from
     *        different sources
     * @param status Summary status
     * @param summaryMsg text string containing summary of the pf
     * @return
     */
    public void addResultSummaryInfo(Boolean sourcePricesOk, ParamFindResultsStatus status, String summaryMsg) {
        this.setParamFindResultsStatus(status);
        TraderParamFindResultsSummaryRow summaryRow =
                        new TraderParamFindResultsSummaryRow(sourcePricesOk, status, summaryMsg);
        traderParamFindResultsDescription.setResultSummary(summaryRow);
    }

    /**
     * Updates the results with the various calculated metrics
     * 
     * @param minCostAchievable
     * @param sourceDataOk
     * @param costMetricAtStart
     * @param costMetricAtEnd
     * @param infoAtStart
     * @param infoAtEnd
     */
    void updateParamFindResultsForMetrics(double minCostAchievable, Boolean sourceDataOk, double costMetricAtStart,
                    double costMetricAtEnd,
                    Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoAtStart,
                    Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoAtEnd) {
        TraderParamFindResultsDescription resultsDescription = this.getTraderParamFindResultsDescription();
        this.setShouldSuspendMarkets(false); // the default setting)
        ParamFindResultsStatus costMetricResultsStatus = ParamFindResultsStatus.GREEN;
        String preface = "";
        if (costMetricAtEnd - minCostAchievable > CompareToMarket.getRedAlertThreshold()) {
            preface = "Param find failed to find good fit. Cost above red threshold - check prices. ";
            costMetricResultsStatus = ParamFindResultsStatus.RED;
        } else if (costMetricAtEnd - minCostAchievable > CompareToMarket.getAmberAlertThreshold()) {
            preface = "Param find failed to find good fit - check prices";
            costMetricResultsStatus = ParamFindResultsStatus.AMBER;
        }
        if (minCostAchievable > CompareToMarket.getAmberAlertThreshold()) {
            preface += "Inconsistent inputs – check prices against market. ";
            if (costMetricResultsStatus != ParamFindResultsStatus.RED)
                costMetricResultsStatus = ParamFindResultsStatus.AMBER;
        }
        String summaryText = preface + String.format("Cost metric: %.3f -> %.3f.  Min achievable cost %.3f",
                        costMetricAtStart, costMetricAtEnd, minCostAchievable);

        /*
         * do the checks against individual probabilities
         */
        ParamFindResultsStatus probsResultsStatus = ParamFindResultsStatus.GREEN;

        for (Entry<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> e1 : infoAtStart.entrySet()) {
            String marketKey = e1.getKey();
            for (Entry<String, Map<String, CompareToMarketMetricsItemInfo>> e2 : e1.getValue().entrySet()) {
                String sourceKey = e2.getKey();
                /*
                 * do check for the first selection only from each market for each source
                 */
                boolean first = true;
                for (Entry<String, CompareToMarketMetricsItemInfo> e3 : e2.getValue().entrySet()) {
                    if (first) {
                        String selectionKey = e3.getKey();
                        CompareToMarketMetricsItemInfo info = e3.getValue();
                        double probAtEnd = infoAtEnd.get(marketKey).get(sourceKey).get(selectionKey).ourProb;
                        double probDiff = 0;
                        if (probAtEnd > 0 && info.targetProb > 0)
                            probDiff = GCMath.round(Math.abs(probAtEnd - info.targetProb), PF_DECIMAL_PLACES);
                        ParamFindResultsStatus thisProbResultsStatus = ParamFindResultsStatus.GREEN;
                        if (probDiff > CompareToMarket.getRedAlertThreshold())
                            thisProbResultsStatus = ParamFindResultsStatus.RED;
                        else if (probDiff > CompareToMarket.getAmberAlertThreshold())
                            thisProbResultsStatus = ParamFindResultsStatus.AMBER;
                        probsResultsStatus = higherPriorityOf(probsResultsStatus, thisProbResultsStatus);
                        boolean shouldHighlightRow = thisProbResultsStatus != ParamFindResultsStatus.GREEN;
                        TraderParamFindResultsDetailRow detailRow = new TraderParamFindResultsDetailRow(
                                        shouldHighlightRow, sourceKey, marketKey, info.selectionName, info.weight,
                                        info.targetProb, info.ourProb, probAtEnd);
                        resultsDescription.addResultDetailRow(detailRow);
                    }
                    first = false;
                }
            }
        }
        String probsMsg = null;
        switch (probsResultsStatus) {
            case RED:
                probsMsg = "Large differences with market - check prices ";
                this.setShouldSuspendMarkets(true);
                break;
            case AMBER:
                probsMsg = "Some differences with market - check prices ";
                break;
            case GREEN:
            case BLACK:
            case BLUE:
            case YELLOW:
                probsMsg = "";
                break;

        }
        ParamFindResultsStatus summaryStatus = higherPriorityOf(costMetricResultsStatus, probsResultsStatus);
        this.setParamFindResultsStatus(summaryStatus);
        TraderParamFindResultsSummaryRow summaryRow =
                        new TraderParamFindResultsSummaryRow(sourceDataOk, costMetricResultsStatus, summaryText);
        summaryRow.setParamFindResultsSummary(probsMsg + summaryRow.getParamFindResultsSummary());
        summaryRow.setSuccessStatus(summaryStatus);
        resultsDescription.setResultSummary(summaryRow);
    }

    private static ParamFindResultsStatus higherPriorityOf(ParamFindResultsStatus resultsStatus1,
                    ParamFindResultsStatus resultsStatus2) {
        switch (resultsStatus1) {
            case RED:
                return ParamFindResultsStatus.RED;
            case BLUE:
                if (resultsStatus2 == ParamFindResultsStatus.RED)
                    return ParamFindResultsStatus.RED;
                else
                    return ParamFindResultsStatus.BLUE;
            case AMBER:
                if (resultsStatus2 == ParamFindResultsStatus.RED)
                    return ParamFindResultsStatus.RED;
                else if (resultsStatus2 == ParamFindResultsStatus.BLUE)
                    return ParamFindResultsStatus.BLUE;
                else
                    return ParamFindResultsStatus.AMBER;
            case GREEN:
            case BLACK:
            case YELLOW:

                return resultsStatus2;
        }
        throw new IllegalArgumentException("Unrecognised status" + resultsStatus1);
    }

    public void addResultDetailRows(Map<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> infoAtStart) {
        for (Entry<String, Map<String, Map<String, CompareToMarketMetricsItemInfo>>> e1 : infoAtStart.entrySet()) {
            String marketKey = e1.getKey();
            for (Entry<String, Map<String, CompareToMarketMetricsItemInfo>> e2 : e1.getValue().entrySet()) {
                String sourceKey = e2.getKey();
                boolean first = true;
                for (Entry<String, CompareToMarketMetricsItemInfo> e3 : e2.getValue().entrySet()) {
                    if (first) {
                        CompareToMarketMetricsItemInfo info = e3.getValue();
                        TraderParamFindResultsDetailRow detailRow = new TraderParamFindResultsDetailRow(false,
                                        sourceKey, marketKey, info.selectionName, info.weight, info.targetProb,
                                        info.ourProb, info.ourProb);
                        traderParamFindResultsDescription.addResultDetailRow(detailRow);
                    }
                    first = false;
                }
            }
        }
    }

    /**
     * Adds a detail line item which may be shown to the trader. Provides the backing detail behind the summaryInfo
     * 
     * @param shouldHighlight true if this row should be displayed in bold or some similar highlighting scheme
     * @param source the price source this row relates to
     * @param marketType the marketType this row relates to
     * @param selection the selection this row relates to
     * @param weight the weight given to this price in the pf process - combination of sourceWeight*marketWeight
     * @param targetProb - the prob we are trying to paramFind against
     * @param ourProbBeforePF - our prob before the pf started
     * @param ourProbAfterPF - our prob after the pf was completed
     */
    public void addResultDetailRow(Boolean shouldHighlight, String source, String marketType, String selection,
                    double weight, double targetProb, double ourProbBeforePF, double ourProbAfterPF) {
        TraderParamFindResultsDetailRow detailRow = new TraderParamFindResultsDetailRow(shouldHighlight, source,
                        marketType, selection, weight, targetProb, ourProbBeforePF, ourProbAfterPF);
        traderParamFindResultsDescription.addResultDetailRow(detailRow);
    }

}
