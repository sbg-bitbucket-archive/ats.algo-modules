package ats.algo.core.comparetomarket;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.genericsupportfunctions.JsonSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TraderParamFindResultsDescription implements Serializable {

    private static final long serialVersionUID = 1L;
    private TraderParamFindResultsSummaryRow resultSummary;
    private ArrayList<TraderParamFindResultsDetailRow> resultDetailRows;

    public TraderParamFindResultsDescription() {
        resultDetailRows = new ArrayList<TraderParamFindResultsDetailRow>();
    }

    public TraderParamFindResultsSummaryRow getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(TraderParamFindResultsSummaryRow resultSummary) {
        this.resultSummary = resultSummary;
    }

    public ArrayList<TraderParamFindResultsDetailRow> getResultDetailRows() {
        return resultDetailRows;
    }

    public void setResultDetailRows(ArrayList<TraderParamFindResultsDetailRow> resultDetailRows) {
        this.resultDetailRows = resultDetailRows;
    }

    public void addResultDetailRow(TraderParamFindResultsDetailRow detailRow) {
        resultDetailRows.add(detailRow);

    }

    public ParamFindResultsDescription convertToLegacy() {
        ParamFindResultsDescription legacyDescription = new ParamFindResultsDescription();
        String legacyResultSummary = JsonSerializer.serialize(resultSummary, false);
        legacyDescription.setResultSummary(legacyResultSummary);
        ArrayList<String> legacyResultDetailRows = new ArrayList<String>();
        for (TraderParamFindResultsDetailRow resultsDetailRow : resultDetailRows) {
            legacyResultDetailRows.add(JsonSerializer.serialize(resultsDetailRow, false));
        }
        legacyDescription.setResultDetail(legacyResultDetailRows);
        return legacyDescription;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("TraderParamFindResultsDescription [resultSummary=" + resultSummary + "\nresultDetailRows=[");
        for (TraderParamFindResultsDetailRow resultsDetailRow : resultDetailRows) {
            b.append("\n  ");
            b.append(resultsDetailRow.toString());
        }
        b.append("]]");
        return b.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resultDetailRows == null) ? 0 : resultDetailRows.hashCode());
        result = prime * result + ((resultSummary == null) ? 0 : resultSummary.hashCode());
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
        TraderParamFindResultsDescription other = (TraderParamFindResultsDescription) obj;
        if (resultDetailRows == null) {
            if (other.resultDetailRows != null)
                return false;
        } else if (!resultDetailRows.equals(other.resultDetailRows))
            return false;
        if (resultSummary == null) {
            if (other.resultSummary != null)
                return false;
        } else if (!resultSummary.equals(other.resultSummary))
            return false;
        return true;
    }


}
