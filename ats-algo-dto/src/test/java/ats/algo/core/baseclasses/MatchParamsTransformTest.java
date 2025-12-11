package ats.algo.core.baseclasses;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchParams.TransformedParam;

public class MatchParamsTransformTest {

    class TestMatchParams extends MatchParams {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void setDefaultParams(MatchFormat matchFormat) {}
    }

    MatchParams matchParams;

    @Test
    public void test() {
        matchParams = new TestMatchParams();
        MatchParam matchParam = new MatchParam();
        matchParam.setMinAllowedParamValue(-5.0);
        matchParam.setMaxAllowedParamValue(5.0);
        try {
            transformParam(-4.9382, .22);
            transformParam(-4.99999, .22);
            transformParam(4.99999, .22);
            transformParam(4.99999, 5);
            transformParam(0, 5);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    private void transformParam(double mean, double sd) {
        MatchParam matchParam = new MatchParam();
        matchParam.setMinAllowedParamValue(-5.0);
        matchParam.setMaxAllowedParamValue(5.0);
        matchParam.getGaussian().setProperties(mean, sd, 0);
        TransformedParam t = matchParams.transformParam(matchParam);
        System.out.printf("mean: %.3f, sd: %.3f, value: %.3f, delta: %.3f\n", mean, sd, t.value, t.delta);

    }

}
