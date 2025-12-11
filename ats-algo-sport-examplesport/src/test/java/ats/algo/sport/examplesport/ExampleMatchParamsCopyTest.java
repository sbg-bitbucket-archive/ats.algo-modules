package ats.algo.sport.examplesport;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.*;

import org.junit.Test;

public class ExampleMatchParamsCopyTest {

    @Test
    public void test() {
        MethodName.log();
        ExampleMatchParams p1 = new ExampleMatchParams();
        p1.getProbAWinsLeg().updateGaussian(.8, 0.03);
        p1.getProbAWinsLeg().getGaussian().setBias(0.4);
        assertEquals(p1.getProbAWinsLeg().getGaussian().getMean(),
                        p1.getParamMap().get("probAWinsLeg").getGaussian().getMean(), 0.00001);
        ExampleMatchParams p2 = new ExampleMatchParams();
        p2.setEqualTo(p1);
        assertEquals(p1.getProbAWinsLeg().getGaussian().getMean(),
                        p1.getParamMap().get("probAWinsLeg").getGaussian().getMean(), 0.00001);
        assertEquals(p2.getProbAWinsLeg().getGaussian().getMean(),
                        p2.getParamMap().get("probAWinsLeg").getGaussian().getMean(), 0.00001);
        assertEquals(0.8, p2.getProbAWinsLeg().getGaussian().getMean(), 0.0001);
        // System.out.println(p1.toString());
        // System.out.println(p2.toString());
        assertEquals(p1, p2);
        ExampleMatchParams p3 = (ExampleMatchParams) p2.copy();
        assertEquals(p1, p3);
        assertEquals(0.8, p3.getProbAWinsLeg().getGaussian().getMean(), 0.0001);
    }



}
