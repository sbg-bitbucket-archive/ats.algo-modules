package ats.algo.sport.tennis;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;

public class TennisMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public TennisMatchFormatOptions() {
        super.addPropertyOptions("doublesMatch", OptionsList.getBooleanSet());
        super.addPropertyOptions("sex", OptionsList.getEnumSet(Sex.class));
        super.addPropertyOptions("surface", OptionsList.getEnumSet(Surface.class));
        super.addPropertyOptions("tournamentLevel", OptionsList.getEnumSet(TournamentLevel.class));
        super.addPropertyOptions("setsPerMatch", OptionsList.getIntegerSet(new int[] {3, 5}));
        super.addPropertyOptions("finalSetType", OptionsList.getEnumSet(FinalSetType.class));
        super.addPropertyOptions("noAdvantageGameFormat", OptionsList.getBooleanSet());
        super.addPropertyOptions("noAdvantageTieBreakFormat", OptionsList.getBooleanSet());
    }
}
