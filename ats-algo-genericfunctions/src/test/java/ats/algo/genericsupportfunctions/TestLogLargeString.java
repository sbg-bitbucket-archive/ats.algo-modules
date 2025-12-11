package ats.algo.genericsupportfunctions;

import org.junit.Test;

import ats.core.AtsBean;

public class TestLogLargeString extends AtsBean {

    private static final int BIG_STRING_LENGTH = 11500;

    @Test
    public void testLogLargeString() {
        String s = "This is a test string. ";
        error(s);
        StringBuilder sb = new StringBuilder();
        int n = BIG_STRING_LENGTH / s.length();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        String bigS = sb.toString();
        error("bigS length: %d", bigS.length());
        error("bigS: %s", bigS);

    }


}
