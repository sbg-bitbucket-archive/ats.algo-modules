package ats.algo.sport.volleyball;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class VolleyballMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;

    public int nSetsInMatch;
    public int nPointInRegularSet;
    public int nPointInFinalSet;

    public VolleyballMatchFormat() {
        super(SupportedSportType.VOLLEYBALL);
        /*
         * set sensible defaults;
         */
        nSetsInMatch = 5;
        nPointInRegularSet = 25;
        nPointInFinalSet = 15;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nSetsInMatch = DtoJsonUtil.toInt(format.get("nSetsInMatch"));
        nPointInRegularSet = DtoJsonUtil.toInt(format.get("nPointInRegularSet"));
        nPointInFinalSet = DtoJsonUtil.toInt(format.get("nPointInFinalSet"));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new VolleyballMatchFormatOptions();
    }

    public VolleyballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.VOLLEYBALL);
        applyFormat(format);
    }

    public int getnSetsInMatch() {
        return nSetsInMatch;
    }

    public void setnSetsInMatch(int nSetsInMatch) {
        this.nSetsInMatch = nSetsInMatch;
    }

    public int getnPointInRegularSet() {
        return nPointInRegularSet;
    }

    public void setnPointInRegularSet(int nPointInRegularSet) {
        this.nPointInRegularSet = nPointInRegularSet;
    }

    public int getnPointInFinalSet() {
        return nPointInFinalSet;
    }

    public void setnPointInFinalSet(int nPointInFinalSet) {
        this.nPointInFinalSet = nPointInFinalSet;
    }



    private static final String nPointInRegularSetKey = "nPointInRegularSet";
    private static final String nPointInFinalSetKey = "nPointInFinalSet";
    private static final String nSetsInMatchKey = "nSetsInMatch";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nPointInRegularSetKey, String.format("%d", nPointInRegularSet));
        map.put(nPointInFinalSetKey, String.format("%d", nPointInFinalSet));
        map.put(nSetsInMatchKey, String.format("%s", nSetsInMatch));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        int nPointsPerRegularSet;
        int nPointsPerFinalSet;
        int nSetsInMatch;
        int marginChart;
        try {
            newValue = map.get(nSetsInMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nSetsInMatch = Integer.parseInt(newValue);
            if (nSetsInMatch < 1)
                throw new Exception();
            newValue = map.get(nPointInFinalSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nPointsPerFinalSet = Integer.parseInt(newValue);
            if (nPointsPerFinalSet < 3)
                throw new Exception();
            newValue = map.get(nPointInRegularSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nPointsPerRegularSet = Integer.parseInt(newValue);
            if (nPointsPerRegularSet < 3)
                throw new Exception();
            /*
             * margin chart
             */

            newValue = map.get(getMarginchartnamekey());
            errMsg = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", newValue);
            marginChart = Integer.parseInt(newValue);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();
        } catch (Exception e) {
            return errMsg;
        }
        setMarginChart(marginChart);
        this.nPointInRegularSet = nPointsPerRegularSet;
        this.nPointInFinalSet = nPointsPerFinalSet;
        this.nSetsInMatch = nSetsInMatch;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nPointInFinalSet;
        result = prime * result + nPointInRegularSet;
        result = prime * result + nSetsInMatch;
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
        VolleyballMatchFormat other = (VolleyballMatchFormat) obj;
        if (nPointInFinalSet != other.nPointInFinalSet)
            return false;
        if (nPointInRegularSet != other.nPointInRegularSet)
            return false;
        if (nSetsInMatch != other.nSetsInMatch)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new VolleyballMatchFormat();
    }

}
