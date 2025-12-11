/**
 * 
 */
package ats.algo.sport.cricket;

import ats.algo.core.baseclasses.MatchFormatOptions;
import ats.algo.genericsupportfunctions.OptionsList;
import ats.algo.sport.cricket.CricketMatchFormat.MatchForm;

/**
 * @author Robert
 *
 */
public class CricketMatchFormatOptions extends MatchFormatOptions {

    private static final long serialVersionUID = 1L;

    public CricketMatchFormatOptions() {
        super.addPropertyOptions("nOversinMatch", OptionsList.getIntegerSet(new int[] {20, 50}));
        super.addPropertyOptions("nBatmeninMatch", OptionsList.getIntegerSet(new int[] {11}));
        super.addPropertyOptions("matchForm", OptionsList.getEnumSet(MatchForm.class));
        super.addPropertyOptions("nBallsinOver", OptionsList.getIntegerSet(new int[] {6}));
    }

}
