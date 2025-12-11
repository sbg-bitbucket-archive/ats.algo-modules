package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class ZipUtilTest {



    // @Test
    public void test() {
        PairOfIntegers p1 = new PairOfIntegers();
        p1.A = 6;
        p1.B = 7;
        PairOfIntegers p2 = new PairOfIntegers();
        p2.A = 8;
        p2.B = 9;
        PairOfDoubles d1 = new PairOfDoubles();
        d1.A = 2.4;
        d1.B = 3.5;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("p1", p1);
        map.put("p2", p2);
        map.put("d1", d1);
        try {
            ZipUtil.save(new File("C:\\aatmp\\ztest.zip"), map);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("SAVED");

        Map<String, Object> map2 = null;
        try {
            map2 = ZipUtil.load("C:\\aatmp\\ztest.zip", "d1", PairOfDoubles.class, PairOfIntegers.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(map2);
        assertEquals(map.get("p1"), map2.get("p1"));
    }


}
