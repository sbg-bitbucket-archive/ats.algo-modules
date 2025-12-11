package ats.algo.sport.outrights;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.dto.util.DtoJsonUtil;


public class OutrightsMatchFormat extends MatchFormat {
    private static final long serialVersionUID = 1L;
    private CompetitionType competition;

    public OutrightsMatchFormat() {
        this(CompetitionType.PREMIER_LEAGUE);
    }

    public OutrightsMatchFormat(CompetitionType competitionType) {
        super(SupportedSportType.OUTRIGHTS);
        competition = competitionType;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        String s = DtoJsonUtil.toString(format.get("competition"));
        competition = CompetitionType.valueOf(s);
    }

    public OutrightsMatchFormat(Map<String, Object> format) {
        this();
        applyFormat(format);
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return new OutrightsMatchFormatOptions();
    }

    private String COMPETITION_KEY = "Competition";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(COMPETITION_KEY, competition.toString());
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
            newValue = map.get(COMPETITION_KEY);
            competition = CompetitionType.valueOf(newValue);
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
    public MatchFormat generateDefaultMatchFormat() {
        return new OutrightsMatchFormat();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((COMPETITION_KEY == null) ? 0 : COMPETITION_KEY.hashCode());
        result = prime * result + ((competition == null) ? 0 : competition.hashCode());
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
        OutrightsMatchFormat other = (OutrightsMatchFormat) obj;
        if (COMPETITION_KEY == null) {
            if (other.COMPETITION_KEY != null)
                return false;
        } else if (!COMPETITION_KEY.equals(other.COMPETITION_KEY))
            return false;
        if (competition != other.competition)
            return false;
        return true;
    }



}
