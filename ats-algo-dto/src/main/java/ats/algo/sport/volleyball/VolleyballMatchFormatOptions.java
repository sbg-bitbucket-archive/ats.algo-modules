/**
 * 
 */
package ats.algo.sport.volleyball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class VolleyballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public VolleyballMatchFormatOptions() {
        super.addPropertyOptions("nPointInFinalSet", OptionsList.getIntegerSet(new int[] {9, 11, 15}));
        super.addPropertyOptions("nPointInRegularSet", OptionsList.getIntegerSet(new int[] {15, 21, 25}));
        super.addPropertyOptions("nSetsInMatch", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7, 9}));

    }
}

