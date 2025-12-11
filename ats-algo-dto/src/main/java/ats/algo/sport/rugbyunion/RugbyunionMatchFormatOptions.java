package ats.algo.sport.rugbyunion;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class RugbyunionMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public RugbyunionMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {80}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {10, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
