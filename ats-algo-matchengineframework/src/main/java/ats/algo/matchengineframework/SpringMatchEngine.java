package ats.algo.matchengineframework;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.common.SupportedSportType;

/**
 * 
 * @author Geoff for use by calculation engines which use the Spring framework.
 *
 */
public abstract class SpringMatchEngine extends MatchEngine {

    public SpringMatchEngine(SupportedSportType supportedSportType) {
        super(supportedSportType);
    }

    /**
     * should be overridden by classes wishing to set matchFormat after the MatchEngine object has been instantiated
     * 
     * @param matchFormat
     */
    public void setMatchFormat(MatchFormat matchFormat) {
        /*
         * do nothing unless overridden
         */

    }



}
