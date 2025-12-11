package ats.algo.sport.fantasyexample;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;

public class FantasyExampleSportMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private String player1;
    private String player2;
    private String player3;

    public FantasyExampleSportMatchFormat() {
        this(SupportedSportType.FANTASY_EXAMPLE_SPORT);
    }

    public FantasyExampleSportMatchFormat(SupportedSportType sportType) {
        super(sportType);
        player1 = "Patrick Bamford";
        player2 = "Steven Defour";
        player3 = "Ryan Bertrand";
    }



    public String getPlayer1() {
        return player1;
    }



    public void setPlayer1(String player1) {
        this.player1 = player1;
    }



    public String getPlayer2() {
        return player2;
    }



    public void setPlayer2(String player2) {
        this.player2 = player2;
    }



    public String getPlayer3() {
        return player3;
    }



    public void setPlayer3(String player3) {
        this.player3 = player3;
    }



    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("player1", player1);
        map.put("player2", player2);
        map.put("player3", player3);
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
    public MatchFormatOptions matchFormatOptions() {
        return null;
    }

    @Override
    public void applyFormat(Map<String, Object> jsonObject) {}

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new FantasyExampleSportMatchFormat();
    }

}
