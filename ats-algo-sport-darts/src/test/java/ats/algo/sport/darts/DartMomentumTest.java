package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.darts.DartMatchIncidentResult;
import ats.algo.sport.darts.DartMatchParams;
import ats.algo.sport.darts.LegProbTables;
import ats.algo.sport.darts.DartMatchIncidentResult.DartMatchIncidentOutcome;

public class DartMomentumTest {

    /**
     * tests to check momentum logic
     */
    @Test
    public void testBayesianSkillsUpdate() {
        MethodName.log();
        LegProbTables lpt = new LegProbTables();
        lpt.loadTables();
        DartMatchFormat matchFormat = new DartMatchFormat();
        DartMatchParams matchParams = new DartMatchParams(matchFormat);
        DartMatchIncidentResult eventResult = new DartMatchIncidentResult();
        /*
         * 
         */
        matchParams.setSkillA(1, 0.2);
        matchParams.setSkillB(1, 0.2);
        eventResult.setResult(TeamId.A, DartMatchIncidentOutcome.LEGWONA);
        matchParams.updateParamsGivenMatchIncidentResult(eventResult);
        assertEquals(1.009, matchParams.getSkillA().getMean(), 0.001);
        assertEquals(0.992, matchParams.getSkillB().getMean(), 0.001);
        /*
         * 
         */
        matchParams.setSkillA(1, 0.2);
        matchParams.setSkillB(1, 0.2);
        eventResult.setResult(TeamId.B, DartMatchIncidentOutcome.SETWONA);
        matchParams.updateParamsGivenMatchIncidentResult(eventResult);
        assertEquals(1.010, matchParams.getSkillA().getMean(), 0.001);
        assertEquals(0.988, matchParams.getSkillB().getMean(), 0.001);
        /*
         * 
         */
        matchParams.setSkillA(1, 0.2);
        matchParams.setSkillB(1, 0.2);
        eventResult.setResult(TeamId.A, DartMatchIncidentOutcome.LEGWONB);
        matchParams.updateParamsGivenMatchIncidentResult(eventResult);
        assertEquals(0.988, matchParams.getSkillA().getMean(), 0.001);
        assertEquals(1.010, matchParams.getSkillB().getMean(), 0.001);
        /*
         * 
         */
        matchParams.setSkillA(1, 0.2);
        matchParams.setSkillB(1, 0.2);
        eventResult.setResult(TeamId.B, DartMatchIncidentOutcome.MATCHWONB);
        matchParams.updateParamsGivenMatchIncidentResult(eventResult);
        assertEquals(0.992, matchParams.getSkillA().getMean(), 0.001);
        assertEquals(1.009, matchParams.getSkillB().getMean(), 0.001);
    }

}
