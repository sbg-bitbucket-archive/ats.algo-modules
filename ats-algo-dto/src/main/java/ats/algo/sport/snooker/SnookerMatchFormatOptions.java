/**
 * 
 */
package ats.algo.sport.snooker;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class SnookerMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public SnookerMatchFormatOptions() {
        super.addPropertyOptions("nFramesInMatch", OptionsList.getIntegerSet(
                        new int[] {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39}));
        super.addPropertyOptions("nPointInRegularFrame", OptionsList.getIntegerSet(new int[] {147}));
    }

}
