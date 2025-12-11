package ats.algo.sport.floorball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class FloorballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public FloorballMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {50, 60}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {5, 20, 0}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
