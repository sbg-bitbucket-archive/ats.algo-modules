package ats.algo.loggingtest;

import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.core.AtsBean;

class LoggerTestClass extends AtsBean {
    TennisGMatchFormat tennisMatchFormat;

    LoggerTestClass(TennisGMatchFormat tennisMatchFormat) {

        this.tennisMatchFormat = tennisMatchFormat;
    }

    public void testMethod(int i) {
        debug("debug test msg %d", i);
        error("warning test msg %d", i);
        warn(tennisMatchFormat);
        fatal("fatal test msg");
    }

}
