package ats.algo.algomanager;

import static org.junit.Assert.*;

import ats.algo.algomanager.AlgoStats;
import ats.algo.algomanager.EventStatisticsCollector;


public class EventStatisticsTest {
    /*
     * note - this test will generally pass, but not when run as part of a group of tests. So since not reliable leave
     * it commented out
     */
    // @Test
    public void test() {
        EventStatisticsCollector eventStatistics = new EventStatisticsCollector();
        long priceCalcRequestTime;
        long paramFindRequestTime;
        for (int i = 0; i < 20; i++) {
            System.out.printf("Iteration %d\n", i);
            priceCalcRequestTime = System.currentTimeMillis();
            paramFindRequestTime = System.currentTimeMillis();
            if (i == 1)
                sleep(200);
            else
                sleep(50);
            eventStatistics.priceCalcCompleted(1000L, "Test", priceCalcRequestTime);
            if (i == 19)
                sleep(500);
            else
                sleep(50);

            eventStatistics.paramFindCompleted(1000L, "Test", paramFindRequestTime);
        }
        AlgoStats snapshot = eventStatistics.getSnapshot();

        System.out.print(snapshot.toString());

        assertEquals(20, snapshot.getCountPriceCalcs());
        assertEquals(.057, snapshot.getAveragePriceCalcs(), 0.01);
        assertEquals(.2, snapshot.getMaxPriceCalcs(), 0.01);
        assertEquals(.13, snapshot.getAverageParamFinds(), 0.01);
        assertEquals(.55, snapshot.getMaxParamFinds(), 0.01);
        long t1 = snapshot.getTimeOfMaxPriceCalcs();
        long t2 = snapshot.getTimeOfMaxParamFinds();
        int t = (int) (t2 - t1);
        assertTrue(t > 2200 && t < 2450);
        System.out.printf("%d, %d, %d", t1, t2, t);
    }



    private void sleep(int m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
        }
    }

}
