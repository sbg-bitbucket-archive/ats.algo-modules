/**
 * 
 */
package ats.algo.sport.darts;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;
import ats.algo.sport.darts.DartMatchFormat.DartMatchLevel;

/**
 * @author Robert
 *
 */
public class DartMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public DartMatchFormatOptions() {
        super.addPropertyOptions("nLegsPerSet", OptionsList.getIntegerSet(
                        new int[] {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39}));
        super.addPropertyOptions("nLegsOrSetsInMatch", OptionsList.getIntegerSet(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39}));
        super.addPropertyOptions("doubleReqdToStart", OptionsList.getBooleanSet());
        super.addPropertyOptions("dartMatchLevel", OptionsList.getEnumSet(DartMatchLevel.class));
    }

}
