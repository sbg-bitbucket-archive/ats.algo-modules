package ats.algo.algomanager;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.algomanager.AlgoCalculator;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.sport.darts.DartMatchEngine;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.DartMatchState;
import ats.algo.sport.tennis.TennisMatchEngine;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;

public class AlgoCalculatorTest {

    @Test
    public void test() {
        MethodName.log();

        AlgoCalculator algoCalculator = new AlgoCalculator();

        TennisMatchFormat matchFormat = new TennisMatchFormat(3, FinalSetType.NORMAL_WITH_TIE_BREAK, false, false);
        TennisMatchState matchState = new TennisMatchState(matchFormat);
        TennisMatchParams matchParams = new TennisMatchParams();
        MatchEngineSavedState matchEngineSavedState = new MatchEngineSavedState();
        matchParams.setEventId(12345L);
        matchParams.setDoublesMatch(false);
        matchParams.setOnServePctA1(.65, .08);
        matchParams.setOnServePctB1(.59, .08);
        /*
         * the call to handleCalcRequest should invoke the getMarkets method
         */
        PriceCalcRequest request = new PriceCalcRequest(12345L, EventSettings.generateEventSettingsForTesting(2),
                        TennisMatchEngine.class.getName(), CalcRequestCause.NEW_MATCH, matchFormat, matchState,
                        matchParams.generateGenericMatchParams(), null, null, matchEngineSavedState,
                        System.currentTimeMillis());
        algoCalculator.setCalculationParams(request);
        Markets markets = null;
        try {
            algoCalculator.call();
        } catch (Exception e) {
            fail();
        }
        markets = algoCalculator.getCalculatedMarkets();
        // System.out.print(markets.toString());
        assertEquals(0.67, markets.get("FT:ML").get("A"), 0.01);
        /*
         * repeat for a different sport
         */
        DartMatchFormat dartMatchFormat = new DartMatchFormat();
        DartMatchState dartMatchState = new DartMatchState(dartMatchFormat);
        DartMatchParams dartMatchParams = new DartMatchParams(dartMatchFormat);
        dartMatchParams.setEventId(67890L);
        PriceCalcRequest request2 = new PriceCalcRequest(67890L, EventSettings.generateEventSettingsForTesting(2),
                        DartMatchEngine.class.getName(), CalcRequestCause.NEW_MATCH, dartMatchFormat, dartMatchState,
                        dartMatchParams.generateGenericMatchParams(), null, null, matchEngineSavedState,
                        System.currentTimeMillis());
        algoCalculator.setCalculationParams(request2);
        try {
            algoCalculator.call();
        } catch (Exception e) {
            fail();
        }
        markets = algoCalculator.getCalculatedMarkets();
        assertEquals("5.5", markets.get("FT:OUS").getLineId());
        assertEquals(0.59, markets.get("FT:OUS").get("Over"), 0.01);
    }
}
