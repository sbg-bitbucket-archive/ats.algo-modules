package ats.algo.sport.americanfootball;

/*
 * Two 35 min periods Can go straight to penalties if scores are tied Green Card 2 minsoff Yellow Card at least 5 mins
 * off Red Card off for rest of game
 * 
 */
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class AmericanfootballMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private static final String normalTimeMinutesKey = "normalTimeMinutes";
    private static final String extraTimeMinutesKey = "extraTimeMinutes";
    private static final String penaltiesPossibleKey = "penaltiesPossible";

    private int normalTimeMinutes;
    private int extraTimeMinutes;
    private boolean penaltiesPossible;

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new AmericanfootballMatchFormatOptions();
    }

    public AmericanfootballMatchFormat() {
        super(SupportedSportType.AMERICAN_FOOTBALL);
        normalTimeMinutes = 60;
        extraTimeMinutes = 15;
        penaltiesPossible = false;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get(normalTimeMinutesKey));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get(extraTimeMinutesKey));
        penaltiesPossible = DtoJsonUtil.toBoolean(format.get(penaltiesPossibleKey), false);
    }

    public AmericanfootballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.AMERICAN_FOOTBALL);
        applyFormat(format);
    }

    @JsonIgnore
    public boolean is60MinMatch() {
        return normalTimeMinutes == 60;
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

    public boolean isPenaltiesPossible() {
        return penaltiesPossible;
    }

    public void setPenaltiesPossible(boolean penaltiesPossible) {
        this.penaltiesPossible = penaltiesPossible;
    }

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        String s;
        if (penaltiesPossible)
            s = "True";
        else
            s = "False";
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

            // add function to change normal time minutes
            setNormalTimeMinutes(60);
            newValue = map.get(extraTimeMinutesKey);

            int n2 = Integer.parseInt(newValue);
            if ((n2 != 15) && (n2 != 0))
                throw new Exception();
            setExtraTimeMinutes(n2);
            newValue = map.get(penaltiesPossibleKey);
            switch (newValue.toUpperCase()) {
                case "TRUE":
                    penaltiesPossible = true;
                    break;
                case "FALSE":
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
        AmericanfootballMatchFormat other = (AmericanfootballMatchFormat) obj;
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
        return new AmericanfootballMatchFormat();
    }

}
