package ats.algo.sport.handball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class HandballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public HandballMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {50}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {5, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
