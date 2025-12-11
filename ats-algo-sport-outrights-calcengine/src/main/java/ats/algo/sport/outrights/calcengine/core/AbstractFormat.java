package ats.algo.sport.outrights.calcengine.core;

import java.util.LinkedHashMap;
import java.util.Map;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;

public abstract class AbstractFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;

    private CompetitionType competitionType;

    public AbstractFormat(CompetitionType competitionType) {
        super(null);
        this.competitionType = competitionType;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }



    @Override
    public LinkedHashMap<String, String> getAsMap() {
        return null;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        return null;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        return null;
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return null;
    }

    @Override
    public void applyFormat(Map<String, Object> jsonObject) {}

}
