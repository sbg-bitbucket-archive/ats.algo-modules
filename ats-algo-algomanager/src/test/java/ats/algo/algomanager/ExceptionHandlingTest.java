package ats.algo.algomanager;

import org.junit.Test;

import ats.core.AtsBean;
import ats.core.util.log.Level;
import ats.core.util.log.LogUtil;

public class ExceptionHandlingTest extends AtsBean {

    @Test
    public void test() {
        LogUtil.initConsoleLogging(Level.TRACE);
        int x = 0;
        int y = 1;
        int z = 0;
        long eventId = 16L;

        try {
            z = y / x;
        } catch (Exception ex) {

            error("Problem publishing paramFindResults for event %s", eventId, ex);
        }
        System.out.println(String.valueOf(z));

        try {
            z = y / x;
        } catch (Exception ex) {
            error("Problem publishing paramFindResults for event %s", eventId);
            error(ex);
        }
        System.out.println(String.valueOf(z));
    }

}
