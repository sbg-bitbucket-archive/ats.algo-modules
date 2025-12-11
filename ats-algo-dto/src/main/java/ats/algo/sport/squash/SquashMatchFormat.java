package ats.algo.sport.squash;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;

public class SquashMatchFormat extends MatchFormat {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int nGamesInMatch;
    public int nPointInRegularGame;

    public SquashMatchFormat() {
        super(SupportedSportType.SQUASH);
        /*
         * set sensible defaults;
         */
        nGamesInMatch = 5;
        nPointInRegularGame = 11;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        nGamesInMatch = DtoJsonUtil.toInt(format.get("nGamesInMatch"));
        nPointInRegularGame = DtoJsonUtil.toInt(format.get("nPointInRegularGame"));
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new SquashMatchFormatOptions();
    }

    public SquashMatchFormat(Map<String, Object> format) {
        super(SupportedSportType.SQUASH);
        applyFormat(format);
    }

    public void setnPointInRegularGame(int nPointInRegularGame) {
        this.nPointInRegularGame = nPointInRegularGame;
    }

    public void setnGamesInMatch(int nGamesInMatch) {
        this.nGamesInMatch = nGamesInMatch;
    }

    public int getnPointInRegularGame() {
        return nPointInRegularGame;
    }

    public int getnGamesInMatch() {
        return nGamesInMatch;
    }

    private static final String nPointInRegularGameKey = "nPointInRegularGame";
    private static final String nGamesInMatchKey = "nGamesInMatch";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nPointInRegularGameKey, String.format("%d", nPointInRegularGame));
        map.put(nGamesInMatchKey, String.format("%s", nGamesInMatch));
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
        int marginChart;
        try {
            newValue = map.get(nGamesInMatchKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nSetsInMatch = Integer.parseInt(newValue);
            if (nSetsInMatch < 1)
                throw new Exception();
            newValue = map.get(nPointInRegularGameKey);
            errMsg = String.format("Invalid value: %s", newValue);
            nPointsPerRegularSet = Integer.parseInt(newValue);
            if (nPointInRegularGame < 3)
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
        this.nPointInRegularGame = nPointsPerRegularSet;
        this.nGamesInMatch = nSetsInMatch;
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nPointInRegularGame;
        result = prime * result + nGamesInMatch;
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
        SquashMatchFormat other = (SquashMatchFormat) obj;
        if (nPointInRegularGame != other.nPointInRegularGame)
            return false;
        if (nGamesInMatch != other.nGamesInMatch)
            return false;
        return true;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new SquashMatchFormat();
    }

}
