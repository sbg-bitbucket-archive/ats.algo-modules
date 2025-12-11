package ats.algo.loggingtest;

import ats.algo.sport.tennisG.TennisGMatchFormat;

public class TestLogging {

    public static void main(String[] args) {
        System.out.print("TestLogging\n");
        // LogUtil.initConsoleLogging(Level.TRACE);
        TennisGMatchFormat tennisMatchFormat = new TennisGMatchFormat();
        LoggerTestClass l1 = new LoggerTestClass(tennisMatchFormat);
        for (int i = 0; i < 5; i++) {
            l1.testMethod(i);
            sleep(1000);

        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}
