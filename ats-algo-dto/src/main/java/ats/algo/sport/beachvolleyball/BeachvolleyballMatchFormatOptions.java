/**
 * 
 */
package ats.algo.sport.beachvolleyball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class BeachvolleyballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BeachvolleyballMatchFormatOptions() {
        super.addPropertyOptions("nSetsInMatch", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7}));
        super.addPropertyOptions("nPointInRegularSet", OptionsList.getIntegerSet(new int[] {15, 21}));
        super.addPropertyOptions("nPointInFinalSet", OptionsList.getIntegerSet(new int[] {9, 15}));
    }

}
