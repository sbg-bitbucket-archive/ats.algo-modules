package ats.algo.sport.testsport;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;

public class TestSportMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private int noGamesInMatch;

    public TestSportMatchFormat() {
        super(SupportedSportType.TEST_SPORT);
        noGamesInMatch = 7;
    }

    int getNoGamesInMatch() {
        return noGamesInMatch;
    }

    void setNoGamesInMatch(int noGamesInMatch) {
        this.noGamesInMatch = noGamesInMatch;
    }



    public TestSportMatchFormat(SupportedSportType sportType) {
        super(sportType);
        noGamesInMatch = 7;
    }



    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("noGamesInMatch", Integer.toString(noGamesInMatch));
        if (isUseMarginChart())
            map.put(getMarginchartnamekey(), String.valueOf(getMarginChart()));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String value = map.get("noGamesInMatch");
        String errMsg = null;
        int n = 0;
        int marginChart;
        try {
            n = Integer.parseInt(value);
            if (2 * (n / 2) == n)
                errMsg = "noGames must be odd";
            /*
             * margin chart
             */

            value = map.get(getMarginchartnamekey());
            errMsg = String.format("Invalid value: %s.  Allowed integer values 1 to 10 ", value);
            marginChart = Integer.parseInt(value);
            if ((marginChart > 10) || (marginChart < 1))
                throw new Exception();
            setMarginChart(marginChart);
        } catch (Exception e) {
            return errMsg;
        }
        if (errMsg == null)
            noGamesInMatch = n;
        return errMsg;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return null;
    }

    @Override
    public void applyFormat(Map<String, Object> jsonObject) {}

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new TestSportMatchFormat();
    }

}
