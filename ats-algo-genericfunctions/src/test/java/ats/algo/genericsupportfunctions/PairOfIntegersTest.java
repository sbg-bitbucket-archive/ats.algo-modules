package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.core.util.json.JsonUtil;

public class PairOfIntegersTest {

    @Test
    public void test() {
        PairOfIntegers p = new PairOfIntegers();
        p.A = 3;
        p.B = 4;

        String json = JsonUtil.marshalJson(p);
        System.out.println(json);
        PairOfIntegers p2 = JsonUtil.unmarshalJson(json, PairOfIntegers.class);
        assertEquals(p.A, p2.A);
        assertEquals(p.B, p2.B);
    }


}
