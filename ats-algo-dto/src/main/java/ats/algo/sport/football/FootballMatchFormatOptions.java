package ats.algo.sport.football;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class FootballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public FootballMatchFormatOptions() {
        super.addPropertyOptions("normalTimeMinutes", OptionsList.getIntegerSet(new int[] {80, 90}));
        super.addPropertyOptions("extraTimeMinutes", OptionsList.getIntegerSet(new int[] {30, 0}));
        super.addPropertyOptions("matchLevel", OptionsList.getIntegerSet(new int[] {1, 2, 3, 4, 5}));
        super.addPropertyOptions("lastScore", OptionsList.getStringSetFromRange(20));
        super.addPropertyOptions("penaltiesPossible", OptionsList.getBooleanSet());
        super.addPropertyOptions("matchInSecondLeg", OptionsList.getBooleanSet());
        super.addPropertyOptions("matchTwoLegs", OptionsList.getBooleanSet());
        super.addPropertyOptions("awayGoalDouble", OptionsList.getBooleanSet());
        super.addPropertyOptions("shootOutNewFormat", OptionsList.getBooleanSet());
        super.addPropertyOptions("marginChart", OptionsList.getIntegerSet(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

}
