package ats.algo.sport.basketball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class BasketballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BasketballMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {40, 48}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {0, 5, 20}));
        super.addPropertyOptions("twoHalvesFormat", OptionsList.getBooleanSet());
    }

}
