package ats.algo.sport.baseball;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class BaseballMatchFormat extends MatchFormat {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int nInningsinMatch;
    public int nExtraInnings;
    public boolean drawPossible;

    public BaseballMatchFormat() {
        super(SupportedSportType.BASEBALL);
        /*
         * set sensible defaults;
         */
        nInningsinMatch = 9;
        nExtraInnings = 1;
        drawPossible = false;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nInningsinMatch = DtoJsonUtil.toInt(format.get("nInningsinMatch"));
        nExtraInnings = DtoJsonUtil.toInt(format.get("nExtraInnings"));
        drawPossible = DtoJsonUtil.toBoolean(format.get("drawPossible"), false);
    }

    public BaseballMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.BASEBALL);
        applyFormat(format);

    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new BaseballMatchFormatOptions();
    }

    public int getnInningsinMatch() {
        return nInningsinMatch;
    }

    public void setnInningsinMatch(int nInningsinMatch) {
        this.nInningsinMatch = nInningsinMatch;
    }

    public int getnExtraInnings() {
        return nExtraInnings;
    }

    public void setnExtraInnings(int nExtraInnings) {
        this.nExtraInnings = nExtraInnings;
    }

    public boolean isDrawPossible() {
        return drawPossible;
    }

    public void setDrawPossible(boolean drawPossible) {
        this.drawPossible = drawPossible;
    }

    private static final String nInningsinMatchKey = "nInningsinMatch";
    private static final String nExtraInningsKey = "nExtraInnings";
    private static final String drawPossibleKey = "drawPossible";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nInningsinMatchKey, String.format("%d", nInningsinMatch));
        map.put(nExtraInningsKey, String.format("%d", nExtraInnings));
        String s;
        if (drawPossible)
            s = "Yes";
        else
            s = "No";
        map.put(drawPossibleKey, s);
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        int nInningsinMatch;
        int nExtraInnings;
        boolean drawPossible;
        int marginChart;
        try {
            newValue = map.get(nInningsinMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nInningsinMatch = Integer.parseInt(newValue);
            if (nInningsinMatch == 9) {
            } else
                throw new Exception();

            newValue = map.get(nExtraInningsKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nExtraInnings = Integer.parseInt(newValue);

            newValue = map.get(drawPossibleKey);
            errMsg = String.format("Invalid value: %s", newValue);
            switch (newValue.toUpperCase()) {
                case "YES":
                    drawPossible = true;
                    break;
                case "NO":
                    drawPossible = false;
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
        this.nInningsinMatch = nInningsinMatch;
        this.nExtraInnings = nExtraInnings;
        this.drawPossible = drawPossible;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (drawPossible ? 1231 : 1237);
        result = prime * result + nExtraInnings;
        result = prime * result + nInningsinMatch;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseballMatchFormat other = (BaseballMatchFormat) obj;
        if (drawPossible != other.drawPossible)
            return false;
        if (nExtraInnings != other.nExtraInnings)
            return false;
        if (nInningsinMatch != other.nInningsinMatch)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new BaseballMatchFormat();
    }

}
