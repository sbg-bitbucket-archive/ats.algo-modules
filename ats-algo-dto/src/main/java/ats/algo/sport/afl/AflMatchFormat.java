package ats.algo.sport.afl;

/*
 * 4 periods 20 minutes each
 * 
 * Switching goalkeeper for field player -- a boost
 * 
 * Penalties : 2, 5, 10 minutes (not sure if terminate send off if goals scored) 2 mins can be terminates , not for 5 or
 * 10 minutes
 * 
 * 
 * Extra time: 10 minutes until one team scores
 */
import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.core.matchresult.MatchResulter;



public class AflMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private int normalTimeMinutes;
    private int extraTimeMinutes;

    public AflMatchFormat() {
        super(SupportedSportType.AUSSIE_RULES);
        /* CJ */
        // normalTimeMinutes = 90;
        normalTimeMinutes = 80;
        extraTimeMinutes = 0;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get("normalTimeMinutes"));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get("extraTimeMinutes"));
    }

    public AflMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.AUSSIE_RULES);
        applyFormat(format);
    }

    public int getNormalTimeMinutes() {
        return normalTimeMinutes;
    }

    public void setNormalTimeMinutes(int normalTimeMinutes) {
        this.normalTimeMinutes = normalTimeMinutes;
    }

    public int getExtraTimeMinutes() {
        return extraTimeMinutes;
    }

    public void setExtraTimeMinutes(int extraTimeMinutes) {
        this.extraTimeMinutes = extraTimeMinutes;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new AflMatchFormatOptions();
    }

    private static final String extraTimeMinutesKey = "extraTimeMinutes";
    // private static final String twoHalvesFormatKey = "Match contains two halves? (Yes/No)";
    // private static final String lengthOfMatchKey = "Length of match 40/48 minutes?";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String newValue = null;;
        String errorMessage = null;
        int marginChart;
        try {

            setNormalTimeMinutes(80);

            newValue = map.get(extraTimeMinutesKey);
            int n2 = Integer.parseInt(newValue);
            if (n2 != 5 && (n2 != 0))
                throw new Exception();
            setExtraTimeMinutes(n2);
            /*
             * margin chart
             */

            newValue = map.get(getMarginchartnamekey());
            errorMessage = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", newValue);
            marginChart = Integer.parseInt(newValue);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();
            setMarginChart(marginChart);
        } catch (Exception e) {
            errorMessage = String.format("Invalid input: %s.", newValue);
            return errorMessage;
        }
        return null;
    }

    @Override
    public MatchResulter generateMatchResulter() {
        MatchResulter matchResulter = new AflMatchResulter();
        return matchResulter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + extraTimeMinutes;
        result = prime * result + normalTimeMinutes;
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
        AflMatchFormat other = (AflMatchFormat) obj;
        if (extraTimeMinutes != other.extraTimeMinutes)
            return false;
        if (normalTimeMinutes != other.normalTimeMinutes)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new AflMatchFormat();
    }


}
