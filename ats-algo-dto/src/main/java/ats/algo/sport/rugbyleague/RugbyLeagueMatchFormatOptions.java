package ats.algo.sport.rugbyleague;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class RugbyLeagueMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public RugbyLeagueMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {80}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {10, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
