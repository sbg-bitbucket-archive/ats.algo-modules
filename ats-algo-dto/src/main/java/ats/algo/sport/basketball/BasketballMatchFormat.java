package ats.algo.sport.basketball;

/*
 * 3 periods 20 minutes each
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;
import ats.algo.core.matchresult.MatchResulter;

public class BasketballMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private int normalTimeMinutes;
    private int extraTimeMinutes;
    private boolean twoHalvesFormat;
    // private boolean isFourtyMinutesMatch;

    public BasketballMatchFormat() {
        super(SupportedSportType.BASKETBALL);
        /* CJ */
        // normalTimeMinutes = 90;
        normalTimeMinutes = 40;
        extraTimeMinutes = 5;
        twoHalvesFormat = false;
        // FIXME: ADDING EXTRA TIME NO OF PERIOD PARAM

    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get("normalTimeMinutes"));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get("extraTimeMinutes"));
        twoHalvesFormat = DtoJsonUtil.toBoolean(format.get("penaltiesPossible"), false);
    }

    public BasketballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.BASKETBALL);
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

    public boolean isTwoHalvesFormat() {
        return twoHalvesFormat;
    }

    public void setTwoHalvesFormat(boolean twoHalvesFormat) {
        this.twoHalvesFormat = twoHalvesFormat;
    }

    @JsonIgnore
    public boolean isFourtyMinutesMatch() {
        return normalTimeMinutes == 40;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new BasketballMatchFormatOptions();
    }

    private static final String extraTimeMinutesKey = "extraTimeMinutes"; // Length of possible extra time (0/5/20
                                                                          // minutes)
    private static final String twoHalvesFormatKey = "twoHalvesFormat";// Match contains two halves? (Yes/No)
    private static final String lengthOfMatchKey = "lengthOfMatch";// Length of match 40/48 minutes?

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        String s;
        if (twoHalvesFormat)
            s = "True";
        else
            s = "False";
        map.put(twoHalvesFormatKey, s);

        map.put(lengthOfMatchKey, String.format("%d", normalTimeMinutes));
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

            // add function to change normal time minutes
            setNormalTimeMinutes(40);
            newValue = map.get(lengthOfMatchKey);
            int n1 = Integer.parseInt(newValue);
            if (n1 != 40 && n1 != 48)
                throw new Exception();
            setNormalTimeMinutes(n1);
            newValue = map.get(extraTimeMinutesKey);
            int n2 = Integer.parseInt(newValue);
            if (n2 != 5 && (n2 != 20) && (n2 != 0))
                throw new Exception();
            setExtraTimeMinutes(n2);
            newValue = map.get(twoHalvesFormatKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    twoHalvesFormat = true;
                    break;
                case "FALSE":
                    twoHalvesFormat = false;
                    break;
                default:
                    throw new Exception();
            }
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + extraTimeMinutes;
        result = prime * result + normalTimeMinutes;
        result = prime * result + (twoHalvesFormat ? 1231 : 1237);
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
        BasketballMatchFormat other = (BasketballMatchFormat) obj;
        if (extraTimeMinutes != other.extraTimeMinutes)
            return false;
        if (normalTimeMinutes != other.normalTimeMinutes)
            return false;
        if (twoHalvesFormat != other.twoHalvesFormat)
            return false;
        return true;
    }

    @Override
    public MatchResulter generateMatchResulter() {
        MatchResulter matchResulter = new BasketballMatchResulter();
        return matchResulter;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new BasketballMatchFormat();
    }

}
