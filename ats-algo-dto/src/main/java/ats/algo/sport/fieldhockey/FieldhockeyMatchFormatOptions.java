package ats.algo.sport.fieldhockey;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class FieldhockeyMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public FieldhockeyMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {70}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {0, 15}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
