package ats.algo.sport.futsal;

/*
 * Two 35 min periods Can go straight to penalties if scores are tied Green Card 2 minsoff Yellow Card at least 5 mins
 * off Red Card off for rest of game
 * 
 */
import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;



public class FutsalMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private int normalTimeMinutes;
    private int extraTimeMinutes;
    private boolean penaltiesPossible;

    public FutsalMatchFormat() {
        super(SupportedSportType.FUTSAL);
        normalTimeMinutes = 40;
        extraTimeMinutes = 5; // 5 mins is for two halfs
        penaltiesPossible = true;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        normalTimeMinutes = DtoJsonUtil.toInt(format.get("normalTimeMinutes"));
        extraTimeMinutes = DtoJsonUtil.toInt(format.get("extraTimeMinutes"));
        penaltiesPossible = DtoJsonUtil.toBoolean(format.get("penaltiesPossible"), false);
    }

    public FutsalMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.FUTSAL);
        applyFormat(format);
    }

    @JsonIgnore
    public boolean is40MinMatch() {
        return normalTimeMinutes == 40;
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
    public MatchFormatOptions matchFormatOptions() {
        return new FutsalMatchFormatOptions();
    }

    private static final String normalTimeMinutesKey = "Length of normal time (40/50 minutes)";
    private static final String extraTimeMinutesKey = "Length of possible extra time (0/5 minutes)";
    private static final String penaltiesPossibleKey = "Match may be settled by shoot out? (Yes/No)";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put(extraTimeMinutesKey, String.format("%d", extraTimeMinutes));
        map.put(normalTimeMinutesKey, String.format("%d", normalTimeMinutes));
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

            // add function to change normal time minutes
            setNormalTimeMinutes(40);
            newValue = map.get(normalTimeMinutesKey);
            int n2 = Integer.parseInt(newValue);
            if ((n2 != 50) && (n2 != 40))
                throw new Exception();
            setNormalTimeMinutes(n2);

            newValue = map.get(extraTimeMinutesKey);

            n2 = Integer.parseInt(newValue);
            if ((n2 != 5) && (n2 != 0))
                throw new Exception();
            setExtraTimeMinutes(n2);
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
        FutsalMatchFormat other = (FutsalMatchFormat) obj;
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
        return new FutsalMatchFormat();
    }


}
