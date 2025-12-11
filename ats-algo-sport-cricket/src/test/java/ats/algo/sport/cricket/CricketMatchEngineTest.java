package ats.algo.sport.cricket;

import ats.algo.genericsupportfunctions.MethodName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ats.algo.core.markets.Markets;
import ats.algo.sport.cricket.CricketMatchEngine;
import ats.algo.sport.cricket.CricketMatchFormat;
import ats.algo.sport.cricket.CricketMatchParams;
import ats.algo.sport.cricket.CricketMatchFormat.MatchForm;


public class CricketMatchEngineTest {
    private final double[] ballPer = {0.23, 0.07, 0.005, 0.13, 0.002, 0.04, 0.029};
    private final double[] wicketPer = {0.63, 0.16, 0.12, 0.05, 0.03, 0.01};
    private final double[] per = new double[ballPer.length + wicketPer.length];

    @Test
    public void testCalculation() {
        MethodName.log();

        @SuppressWarnings("unused")
        double sum = 0;

        @SuppressWarnings("unused")
        double sum1 = 0; // ball probablities
        for (int i = 0; i < ballPer.length; i++) {
            if (i == ballPer.length - 1) {
                // sum += ballPer[i];
            } else
                sum += (i + 1) * ballPer[i];
            sum1 += ballPer[i];
        }
        @SuppressWarnings("unused")
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
        // System.out.println(ta + "--" + tb + "--" + sum + "---" + sum1 + "--" + sum3);
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
        // System.out.println("sum (ball +wicket) probs= " + sum);
        // System.out.println("sum1 = " + sum1);
        // System.out.println("sum3 = " + sum);
        // System.out.println(per.length);
        for (int i = 0; i < per.length; i++) {
            // System.out.println(per[i]);
        }
        // System.out.println("---------------------");
        for (int i = 0; i < per.length; i++) {
            // System.out.println(perA[i]);
        }
        // System.out.println("---------------------");
        for (int i = 0; i < per.length; i++) {
            // System.out.println(perB[i]);
        }
    }

    @Test
    public void test() {
        MethodName.log();
        CricketMatchFormat matchFormat = new CricketMatchFormat();
        matchFormat.setMatchForm(MatchForm.Twenty20);
        CricketMatchEngine matchEngine = new CricketMatchEngine(matchFormat);
        CricketMatchParams matchParams = (CricketMatchParams) matchEngine.getMatchParams();
        double p = 179;
        double p1 = 169;
        matchParams.setTeamAScoreRate(p, 0);
        matchParams.setTeamBScoreRate(p1, 0);
        matchEngine.setMatchParams(matchParams);
        matchEngine.calculate();
        Markets markets = matchEngine.getCalculatedMarkets();
        // System.out.println(markets.get("FT:ML").get("A"));
        // System.out.println(markets.get("FT:ML").get("B"));

        assertEquals(0.64859, markets.get("FT:ML").get("A"), 0.02);
        assertEquals(0.35141, markets.get("FT:ML").get("B"), 0.02);

    }
}
