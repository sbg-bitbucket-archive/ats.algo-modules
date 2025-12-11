package ats.algo.core.comparetomarket;

import java.io.Serializable;
import java.util.ArrayList;

import ats.core.util.json.JsonUtil;


public class ParamFindResultsDescription implements Serializable {


    private static final long serialVersionUID = 1L;
    private String resultSummary;
    private ArrayList<String> resultDetail;

    public ParamFindResultsDescription() {
        resultDetail = new ArrayList<String>();
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }

    public ArrayList<String> getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(ArrayList<String> resultDetail) {
        this.resultDetail = resultDetail;
    }

    public void addResultDetailRow(String s) {
        resultDetail.add(s);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resultDetail == null) ? 0 : resultDetail.hashCode());
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
        ParamFindResultsDescription other = (ParamFindResultsDescription) obj;
        if (resultDetail == null) {
            if (other.resultDetail != null)
                return false;
        } else if (!resultDetail.equals(other.resultDetail))
            return false;
        if (resultSummary == null) {
            if (other.resultSummary != null)
                return false;
        } else if (!resultSummary.equals(other.resultSummary))
            return false;
        return true;
    }

    @Override
    public String toString() {

        return JsonUtil.marshalJson(this);
        // return "ParamFindResultsDescription [resultSummary=" + resultSummary + ", resultDetail=" + resultDetail +
        // "]";
    }


}
