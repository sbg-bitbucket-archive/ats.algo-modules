package ats.algo.sport.tennisG;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import ats.algo.genericsupportfunctions.Gaussian;
import ats.algo.sport.tennisG.TennisGMatchParams;
import ats.algo.sport.tennisG.TennisGMatchFormat.Sex;
import ats.algo.sport.tennisG.TennisGMatchFormat.Surface;
import ats.algo.sport.tennisG.TennisGMatchFormat.TournamentLevel;

public class TennisDefaultParametersTest {
    @Test
    public void test() {
        MethodName.log();
        TennisGMatchParams matchParams = new TennisGMatchParams();
        Gaussian x = matchParams.getDefaultParams(Sex.MEN, Surface.GRASS, TournamentLevel.ATP);
        assertEquals(.659, x.getMean(), 0.0001);
        assertEquals(.05, x.getStdDevn(), 0.0001);
        x = matchParams.getDefaultParams(Sex.WOMEN, Surface.CLAY, TournamentLevel.PREMIER);
        assertEquals(.55, x.getMean(), 0.0001);
        assertEquals(.06, x.getStdDevn(), 0.0001);

        try {
            x = matchParams.getDefaultParams(Sex.WOMEN, Surface.CLAY, TournamentLevel.ATP);
            /*
             * should succeed - catch should not be triggered"
             */
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
