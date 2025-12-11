package ats.algo.sport.outrights;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

public class OutrightsMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public OutrightsMatchFormatOptions() {
        super.addPropertyOptions("competition", OptionsList.getEnumSet(CompetitionType.class));
    }

}
