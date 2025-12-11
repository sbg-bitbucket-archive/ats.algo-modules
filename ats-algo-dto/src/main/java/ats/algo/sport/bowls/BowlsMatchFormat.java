package ats.algo.sport.bowls;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class BowlsMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;
    public int nSetsInMatch;
    public int nEndInSet;
    public int bowlsInEnd;
    public int nEndInFinalSet;
    public boolean winningBySet;

    public BowlsMatchFormat() {
        super(SupportedSportType.BOWLS);
        /*
         * set sensible defaults;
         */
        nSetsInMatch = 3;
        nEndInSet = 7;
        nEndInFinalSet = 3;
        bowlsInEnd = 4;
        winningBySet = true;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nSetsInMatch = DtoJsonUtil.toInt(format.get("nSetsInMatch"));
        nEndInSet = DtoJsonUtil.toInt(format.get("nEndInSet"));
        bowlsInEnd = DtoJsonUtil.toInt(format.get("bowlsInEnd"));
        nEndInFinalSet = DtoJsonUtil.toInt(format.get("nEndInFinalSet"));
        winningBySet = DtoJsonUtil.toBoolean(format.get("winningBySet"), true);
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new BowlsMatchFormatOptions();
    }

    public BowlsMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.BOWLS);
        applyFormat(format);
    }

    public boolean isWinningByEnd() {
        return winningBySet;
    }

    public void setWinningByEnd(boolean winningByEnd) {
        this.winningBySet = winningByEnd;
    }

    public int getBowlsInEnd() {
        return bowlsInEnd;
    }

    public void setBowlsInEnd(int bowlsInEnd) {
        this.bowlsInEnd = bowlsInEnd;
    }

    public int getnEndInFinalSet() {
        return nEndInFinalSet;
    }

    public void setnEndInFinalSet(int nEndInFinalSet) {
        this.nEndInFinalSet = nEndInFinalSet;
    }

    public int getnEndInSet() {
        return nEndInSet;
    }

    public void setnEndInSet(int nEndInSet) {
        this.nEndInSet = nEndInSet;
    }

    public void setnSetsInMatch(int nSetsInMatch) {
        this.nSetsInMatch = nSetsInMatch;
    }

    public int getnSetsInMatch() {
        return nSetsInMatch;
    }

    private static final String nEndInSetKey = "nEndInSet";
    private static final String nEndInFinalSetKey = "nEndInFinalSet";
    private static final String nSetsInMatchKey = "nSetsInMatch";
    private static final String winningByEndKey = "winningBySet";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nEndInSetKey, String.format("%d", nEndInSet));
        map.put(nSetsInMatchKey, String.format("%s", nSetsInMatch));
        map.put(nEndInFinalSetKey, String.format("%s", nEndInFinalSet));
        map.put(winningByEndKey, String.format("%s", winningBySet));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        int nPointsPerRegularSet;
        int nSetsInMatch;
        int nEndInFinalSet;
        boolean winningBySet;
        int marginChart;
        try {
            newValue = map.get(nSetsInMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nSetsInMatch = Integer.parseInt(newValue);
            if (nSetsInMatch < 1)
                throw new Exception();
            newValue = map.get(nEndInSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nPointsPerRegularSet = Integer.parseInt(newValue);
            if (nEndInSet < 3)
                throw new Exception();
            newValue = map.get(nEndInFinalSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nEndInFinalSet = Integer.parseInt(newValue);
            newValue = map.get(winningByEndKey);
            errMsg = String.format("Invalid value: %s", newValue);
            winningBySet = Boolean.getBoolean(newValue);
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
        this.nEndInSet = nPointsPerRegularSet;
        this.nSetsInMatch = nSetsInMatch;
        this.nEndInFinalSet = nEndInFinalSet;
        this.winningBySet = winningBySet;
        return null;
    }

    public boolean isWinningBySet() {
        return winningBySet;
    }

    public void setWinningBySet(boolean winningBySet) {
        this.winningBySet = winningBySet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bowlsInEnd;
        result = prime * result + nEndInFinalSet;
        result = prime * result + nEndInSet;
        result = prime * result + nSetsInMatch;
        result = prime * result + (winningBySet ? 1231 : 1237);
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
        BowlsMatchFormat other = (BowlsMatchFormat) obj;
        if (bowlsInEnd != other.bowlsInEnd)
            return false;
        if (nEndInFinalSet != other.nEndInFinalSet)
            return false;
        if (nEndInSet != other.nEndInSet)
            return false;
        if (nSetsInMatch != other.nSetsInMatch)
            return false;
        if (winningBySet != other.winningBySet)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new BowlsMatchFormat();
    }

}
