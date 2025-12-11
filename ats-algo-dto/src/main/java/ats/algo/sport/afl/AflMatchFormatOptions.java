package ats.algo.sport.afl;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class AflMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public AflMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {80}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {5}));
    }

}
