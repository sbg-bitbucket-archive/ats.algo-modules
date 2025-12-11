package ats.algo.montecarloframework;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatisticsOverflowTest {

    @Test
    public void test() {
        TestStatistics testStatistics = new TestStatistics(5);
        for (int i = 0; i < 300; i++)
            testStatistics.updateStats(3, 2);

        try {
            testStatistics.updateStats(4, 4);
            testStatistics.generateMonteCarloMarkets();
        } catch (Exception e) {
            fail(); // one error in 300 should not generate exception
        }
        try {
            testStatistics.updateStats(4, 4);
            testStatistics.generateMonteCarloMarkets();
            fail(); // two errors in 300 should generate exception
        } catch (Exception e) {

        }
    }

}
