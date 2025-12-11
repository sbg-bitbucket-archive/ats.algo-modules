/**
 * 
 */
package ats.algo.sport.badminton;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class BadmintonMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BadmintonMatchFormatOptions() {
        super.addPropertyOptions("nGamesInMatch", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7, 9, 11}));
        super.addPropertyOptions("nPointInRegularGame", OptionsList.getIntegerSet(new int[] {15, 21}));
        super.addPropertyOptions("maxPointInFinalGame", OptionsList.getIntegerSet(new int[] {30}));
    }

}
