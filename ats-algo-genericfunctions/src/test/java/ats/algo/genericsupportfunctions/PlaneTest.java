package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlaneTest {

    @Test
    public void test() {
        Vector3D v1 = new Vector3D(0.0, 1.0, -0.2);
        Vector3D v2 = new Vector3D(1.0, 0.0, -0.4);
        Vector3D v3 = new Vector3D(0.0, 0.0, 0.2);
        Plane l1 = new Plane(v1, v2, v3, 1.0);
        assertEquals(-0.8, l1.calcZ(1.0, 1.0), 0.00001);

        v1 = new Vector3D(6.0, 4.0, -17.5);
        v2 = new Vector3D(5.0, 3.0, -14.0);
        v3 = new Vector3D(-1.0, -2.0, 5.5);
        Plane l2 = new Plane(v1, v2, v3, -1.0);
        assertEquals(-27.0, l2.calcZ(7.0, 9.0), 0.00001);

        PairOfDoubles p = Plane.findIntersection(l1, l2);
        assertEquals(-18.0, p.A, 0.00001);
        assertEquals(25.0, p.B, 0.00001);


    }

}
