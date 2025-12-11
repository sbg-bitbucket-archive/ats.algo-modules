/**
 * 
 */
package ats.algo.sport.baseball;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class BaseballMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public BaseballMatchFormatOptions() {
        super.addPropertyOptions("nInningsinMatch", OptionsList.getIntegerSet(new int[] {9}));
        super.addPropertyOptions("nExtraInnings", OptionsList.getIntegerSet(new int[] {1, 3, 5, 7, 9}));
        super.addPropertyOptions("drawPossible", OptionsList.getBooleanSet());
    }

}
