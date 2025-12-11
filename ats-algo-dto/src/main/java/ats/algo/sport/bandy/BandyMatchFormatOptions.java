package ats.algo.sport.bandy;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class BandyMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BandyMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {90}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {0, 15}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
