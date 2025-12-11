package ats.algo.sport.rollerhockey;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class RollerhockeyMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public RollerhockeyMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {40}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {5, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
