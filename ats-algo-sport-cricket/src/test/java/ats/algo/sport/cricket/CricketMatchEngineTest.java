package ats.algo.sport.cricket;

import org.junit.Test;


public class CricketMatchEngineTest {
    // private final double[] ballPer = { 0.18, 0.025, 0.005, 0.11, 0.002, 0.05,
    // 0.063 };
    private final double[] ballPer = {0.23, 0.07, 0.005, 0.13, 0.002, 0.04, 0.029};
    private final double[] wicketPer = {0.63, 0.16, 0.12, 0.05, 0.03, 0.01};
    private final double[] per = new double[ballPer.length + wicketPer.length];

    @Test
    public void test() {
        // CricketMatchParams matchParams = (CricketMatchParams) matchEngine
        // .getMatchParams();
        // double p = 0.7;
        // double p1 = 0.7;
        double sum = 0;
        double sum1 = 0;
        for (int i = 0; i < ballPer.length; i++) {
            if (i == ballPer.length - 1) {
                // sum += ballPer[i];
            } else
                sum += (i + 1) * ballPer[i];
            sum1 += ballPer[i];
        }
        double sum3 = ballPer[0] + 2 * ballPer[1] + 3 * ballPer[2] + 4 * ballPer[3] + 5 * ballPer[4] + 6 * ballPer[5]
                        + ballPer[6];

        double wicket = 0.044;
        per[0] = ballPer[0];
        for (int i = 1; i < per.length; i++) {
            if (i > (ballPer.length - 1)) {
                per[i] = wicket * wicketPer[i - ballPer.length] + per[i - 1];
            } else {
                for (int m = 0; m <= i; m++) {
                    per[i] += ballPer[m];
                }

            }

        }

        double ta = 1.51;
        double tb = 0.77;
        System.out.println(ta + "--" + tb + "--" + sum + "---" + sum1 + "--" + sum3);
        double[] perA = new double[per.length];
        double[] perB = new double[per.length];
        perA[0] = ballPer[0] * ta;
        for (int i = 1; i < perA.length; i++)
            if (i > (ballPer.length - 1))
                perA[i] = wicket * wicketPer[i - ballPer.length] + perA[i - 1];
            else
                for (int m = 0; m <= i; m++)
                    perA[i] += ballPer[m] * ta;
        perB[0] = ballPer[0] * tb;
        for (int i = 1; i < perB.length; i++)
            if (i > (ballPer.length - 1))
                perB[i] = wicket * wicketPer[i - ballPer.length] + perB[i - 1];
            else
                for (int m = 0; m <= i; m++)
                    perB[i] += ballPer[m] * tb;
        // per[per.length - 1] = 1 - per[per.length - 2];
        System.out.println(sum);
        System.out.println(sum1);
        System.out.println(per.length);
        for (int i = 0; i < per.length; i++) {
            System.out.println(per[i]);
        }
        System.out.println("---------------------");
        for (int i = 0; i < per.length; i++) {
            System.out.println(perA[i]);
        }
        System.out.println("---------------------");
        for (int i = 0; i < per.length; i++) {
            System.out.println(perB[i]);
        }
        // System.out.println(markets.get("FT:ML").get("A"));
        // System.out.println(markets.get("FT:ML").get("B"));
        //
        // System.out.println(markets.get("FT:PSPRD").get("A"));
        // System.out.println(markets.get("FT:PSPRD").get("B"));

        // assertEquals(0.487856, markets.get("FT:ML").get("A"), 0.02);
        // assertEquals(0.51214, markets.get("FT:ML").get("B"), 0.02);
        // assertEquals(0.492464, markets.get("FT:PSPRD").get("A"), 0.02);
        // assertEquals(0.507536, markets.get("FT:PSPRD").get("B"), 0.02);

        // assertEquals(p*p, markets.get("LB").get("2-0"), 0.012);
        // assertEquals("2.5", markets.get("LT").getSubType());
    }
}
