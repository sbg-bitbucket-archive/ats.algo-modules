package ats.algo.sport.snooker;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class SnookerMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;
    public int nFramesInMatch;
    public int nPointInRegularFrame;

    public SnookerMatchFormat() {
        super(SupportedSportType.SNOOKER);
        /*
         * set sensible defaults;
         */
        nFramesInMatch = 19;
        nPointInRegularFrame = 147;

    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nFramesInMatch = DtoJsonUtil.toInt(format.get("nFramesInMatch"));
        nPointInRegularFrame = DtoJsonUtil.toInt(format.get("nPointInRegularFrame"));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new SnookerMatchFormatOptions();
    }

    public SnookerMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.SNOOKER);
        applyFormat(format);
    }

    public void setnPointInRegularFrame(int nPointInRegularFrame) {
        this.nPointInRegularFrame = nPointInRegularFrame;
    }

    public void setnFramesInMatch(int nFramesInMatch) {
        this.nFramesInMatch = nFramesInMatch;
    }

    public int getnPointInRegularFrame() {
        return nPointInRegularFrame;
    }

    public int getnFramesInMatch() {
        return nFramesInMatch;
    }

    private static final String nPointInRegularFrameKey = "nPointInRegularFrame";
    private static final String nFramesInMatchKey = "nFramesInMatch";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nPointInRegularFrameKey, String.format("%d", nPointInRegularFrame));
        map.put(nFramesInMatchKey, String.format("%s", nFramesInMatch));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errMsg = "Unknown error";
        String newValue;
        int nPointsPerRegularFrame;
        int nFramesInMatch;
        int marginChart;
        try {
            newValue = map.get(nFramesInMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nFramesInMatch = Integer.parseInt(newValue);
            if (nFramesInMatch < 1)
                throw new Exception();
            newValue = map.get(nPointInRegularFrameKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nPointsPerRegularFrame = Integer.parseInt(newValue);
            if (nPointsPerRegularFrame < 3)
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
        this.nPointInRegularFrame = nPointsPerRegularFrame;
        this.nFramesInMatch = nFramesInMatch;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nFramesInMatch;
        result = prime * result + nPointInRegularFrame;
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
        SnookerMatchFormat other = (SnookerMatchFormat) obj;
        if (nFramesInMatch != other.nFramesInMatch)
            return false;
        if (nPointInRegularFrame != other.nPointInRegularFrame)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new SnookerMatchFormat();
    }
}
