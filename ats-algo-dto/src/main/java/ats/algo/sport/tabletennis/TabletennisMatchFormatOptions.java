/**
 * 
 */
package ats.algo.sport.tabletennis;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;

/**
 * @author Robert
 *
 */
public class TabletennisMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public TabletennisMatchFormatOptions() {
        super.addPropertyOptions("nGamesInMatch",
                        OptionsList.getIntegerSet(new int[] {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25}));
        super.addPropertyOptions("nPointInRegularGame", OptionsList.getIntegerSet(new int[] {11}));
    }

}
