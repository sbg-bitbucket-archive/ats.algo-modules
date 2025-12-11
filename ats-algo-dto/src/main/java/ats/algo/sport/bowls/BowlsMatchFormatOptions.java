/**
 * 
 */
package ats.algo.sport.bowls;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class BowlsMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BowlsMatchFormatOptions() {
        super.addPropertyOptions("nSetsInMatch", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7, 9}));
        super.addPropertyOptions("nEndInSet", OptionsList.getIntegerSet(new int[] {5, 7, 9}));
        super.addPropertyOptions("bowlsInEnd", OptionsList.getIntegerSet(new int[] {2, 4, 6}));
        super.addPropertyOptions("nEndInFinalSet", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7}));
        super.addPropertyOptions("winningBySet", OptionsList.getBooleanSet());
    }

}
