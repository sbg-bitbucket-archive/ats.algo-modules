package ats.algo.sport.handball;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

/*
 * handball match format Time out exist, which means no injures time 30-mins two halves for mature team 25 mins/20 mins
 * two halves for younger
 * 
 * extra halves 5 mins, maximum 2 halves, if tie penalties
 * 
 * Shootout best-of-five
 * 
 * 2 yellow cards = TWO_MINS_SUSPENSION 3 yellow cards = RED_CARD = TWO_MINS_SUSPENSION + substitution
 * 
 * 
 */
public class HandballMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;
    private int normalTimeMinutes;
    private int extraTimeMinutes;
    private boolean penaltiesPossible;


    public HandballMatchFormat() {

        super(SupportedSportType.HANDBALL);
        normalTimeMinutes = 60;
        extraTimeMinutes = 0;
        penaltiesPossible = false;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get("normalTimeMinutes"));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get("extraTimeMinutes"));
        penaltiesPossible = DtoJsonUtil.toBoolean(format.get("penaltiesPossible"), false);
    }

    public HandballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.HANDBALL);
        applyFormat(format);
    }

    public int getNormalTimeMinutes() {
        return normalTimeMinutes;
    }

    public void setNormalTimeMinutes(int normalTimeMinutes) {
        this.normalTimeMinutes = normalTimeMinutes;
    }


    @JsonIgnore
    public boolean is50MinMatch() {
        return normalTimeMinutes == 50;
    }

    public int getExtraTimeMinutes() {
        return extraTimeMinutes;
    }

    public void setExtraTimeMinutes(int extraTimeMinutes) {
        this.extraTimeMinutes = extraTimeMinutes;
    }

    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new HandballMatchFormatOptions();
    }

    private static final String normalTimeMinutesKey = "Length of normal time";
    private static final String extraTimeMinutesKey = "Length of possible extra time (5/0 minutes)";
    private static final String penaltiesPossibleKey = "Match may be settled by penalties (Yes/No)";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(normalTimeMinutesKey, String.format("%d", normalTimeMinutes));
        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        String s;
        if (penaltiesPossible)
            s = "Yes";
        else
            s = "No";
        map.put(penaltiesPossibleKey, s);
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
            newValue = map.get(normalTimeMinutesKey);
            int n = Integer.parseInt(newValue);
            if (n != 50 && (n != 60))
                throw new Exception();
            newValue = map.get(extraTimeMinutesKey);
            n = Integer.parseInt(newValue);
            if (n != 0 && (n != 5))
                throw new Exception();
            setExtraTimeMinutes(n);
            newValue = map.get(penaltiesPossibleKey);
            switch (newValue.toUpperCase()) {
                case "YES":
                    penaltiesPossible = true;
                    break;
                case "NO":
                    penaltiesPossible = false;
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
        result = prime * result + (penaltiesPossible ? 1231 : 1237);
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
        HandballMatchFormat other = (HandballMatchFormat) obj;
        if (extraTimeMinutes != other.extraTimeMinutes)
            return false;
        if (normalTimeMinutes != other.normalTimeMinutes)
            return false;
        if (penaltiesPossible != other.penaltiesPossible)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new HandballMatchFormat();
    }


}
