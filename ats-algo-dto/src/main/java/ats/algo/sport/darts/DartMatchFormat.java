package ats.algo.sport.darts;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class DartMatchFormat extends MatchFormat {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum DartMatchLevel {
        WORLDCLASS,
        LOCAL
        // TODO
    }

    private DartMatchLevel dartMatchLevel;
    private int nLegsPerSet;
    private int nLegsOrSetsInMatch;
    private boolean doubleReqdToStart;

    @Override
    public void applyFormat(Map<String, Object> format) {
        nLegsPerSet = DtoJsonUtil.toInt(format.get("nLegsPerSet"));
        nLegsOrSetsInMatch = DtoJsonUtil.toInt(format.get("nLegsOrSetsInMatch"));
        doubleReqdToStart = DtoJsonUtil.toBoolean(format.get("doubleReqdToStart"), false);
        dartMatchLevel = DartMatchLevel.valueOf(DtoJsonUtil.toString(format.get("dartMatchLevel")));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new DartMatchFormatOptions();
    }

    public DartMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.DARTS);
        applyFormat(format);
    }

    public DartMatchFormat() {
        super(SupportedSportType.DARTS);
        /*
         * set sensible defaults;
         */
        this.nLegsPerSet = 3;
        this.nLegsOrSetsInMatch = 7;
        this.dartMatchLevel = DartMatchLevel.WORLDCLASS;
        this.doubleReqdToStart = false;
    }

    public DartMatchFormat(DartMatchLevel dartMatchLevel, int nLegsPerSet, int nLegsOrSetsInMatch,
                    boolean doubleReqdToStart) {
        super(SupportedSportType.DARTS);
        /*
         * set sensible defaults;
         */
        this.nLegsPerSet = nLegsPerSet;
        this.nLegsOrSetsInMatch = nLegsOrSetsInMatch;
        this.dartMatchLevel = dartMatchLevel;
        this.doubleReqdToStart = doubleReqdToStart;
    }

    public DartMatchLevel getDartMatchLevel() {
        return dartMatchLevel;
    }

    @JsonIgnore
    public boolean isSetBasedMatch() {
        return nLegsPerSet == 1;
    }

    public int getnLegsPerSet() {
        return nLegsPerSet;
    }

    public int getnLegsOrSetsInMatch() {
        return nLegsOrSetsInMatch;
    }

    void setnLegsPerSet(int nLegsPerSet) {
        this.nLegsPerSet = nLegsPerSet;
    }

    void setnLegsOrSetsInMatch(int nLegsOrSetsInMatch) {
        this.nLegsOrSetsInMatch = nLegsOrSetsInMatch;
    }

    void setDoubleReqdToStart(boolean doubleReqdToStart) {
        this.doubleReqdToStart = doubleReqdToStart;
    }

    public boolean isDoubleReqdToStart() {
        return doubleReqdToStart;
    }

    private static final String dartMatchLevelKey = "dartMatchLevel";
    private static final String nLegsPerSetKey = "nLegsPerSet";
    private static final String nLegsOrSetsInMatchKey = "nLegsOrSetsInMatch";
    private static final String doubleReqdtoStartKey = "doubleReqdToStart";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(dartMatchLevelKey, dartMatchLevel.toString());
        map.put(nLegsPerSetKey, String.format("%d", nLegsPerSet));
        map.put(nLegsOrSetsInMatchKey, String.format("%d", nLegsOrSetsInMatch));
        String s = "False";
        if (doubleReqdToStart)
            s = "True";
        map.put(doubleReqdtoStartKey, String.format("%s", s));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        DartMatchLevel level;
        int nLegsPerSet;
        int nLegsOrSetsInMatch;
        boolean doubleReqdToStart;
        int marginChart;
        try {
            newValue = map.get(dartMatchLevelKey);
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "WORLDCLASS":
                    level = DartMatchLevel.WORLDCLASS;
                    break;
                case "LOCAL":
                    level = DartMatchLevel.LOCAL;
                    break;
                default:
                    throw new Exception();
            }
            newValue = map.get(nLegsPerSetKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nLegsPerSet = Integer.parseInt(newValue);
            if (nLegsPerSet < 1)
                throw new Exception();
            newValue = map.get(nLegsOrSetsInMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nLegsOrSetsInMatch = Integer.parseInt(newValue);
            if (nLegsOrSetsInMatch < 3)
                throw new Exception();
            newValue = map.get(doubleReqdtoStartKey);
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    doubleReqdToStart = true;
                    break;
                case "FALSE":
                    doubleReqdToStart = false;
                    break;
                default:
                    throw new Exception();
            }
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
        this.dartMatchLevel = level;
        this.nLegsPerSet = nLegsPerSet;
        this.nLegsOrSetsInMatch = nLegsOrSetsInMatch;
        this.doubleReqdToStart = doubleReqdToStart;
        return null;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new DartMatchFormat();
    }

}
