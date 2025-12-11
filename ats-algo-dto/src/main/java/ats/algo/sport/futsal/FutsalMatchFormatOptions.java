package ats.algo.sport.futsal;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class FutsalMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public FutsalMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {40, 50}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {0, 5}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
