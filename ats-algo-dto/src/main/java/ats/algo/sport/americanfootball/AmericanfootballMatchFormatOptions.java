package ats.algo.sport.americanfootball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class AmericanfootballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public AmericanfootballMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {60}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {0, 15}));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
    }

}
