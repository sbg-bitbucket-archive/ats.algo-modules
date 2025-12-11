/**
 * 
 */
package ats.algo.sport.testcricket;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;
import ats.algo.sport.testcricket.TestCricketMatchFormat.MatchForm;

/**
 * @author Robert
 *
 */
public class TestCricketMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public TestCricketMatchFormatOptions() {
        super.addPropertyOptions("matchForm", OptionsList.getEnumSet(MatchForm.class));
        super.addPropertyOptions("nInningsinMatch", OptionsList.getIntegerSet(new int[] {2, 4}));
        super.addPropertyOptions("maxOverinInning", OptionsList.getIntegerSet(new int[] {1000}));
        super.addPropertyOptions("nBatmeninMatch", OptionsList.getIntegerSet(new int[] {11}));
        super.addPropertyOptions("nBallsinOver", OptionsList.getIntegerSet(new int[] {6}));
        super.addPropertyOptions("nDayinMatch", OptionsList.getIntegerSet(new int[] {4, 5}));

    }
}
