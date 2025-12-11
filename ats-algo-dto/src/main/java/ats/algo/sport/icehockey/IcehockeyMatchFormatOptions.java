package ats.algo.sport.icehockey;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class IcehockeyMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public IcehockeyMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {50, 60}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {20, 5, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
