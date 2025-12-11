package ats.algo.core.recordplayback;


import java.io.IOException;
import java.util.Map;

import ats.algo.genericsupportfunctions.ZipUtil;

public class RecordingLoadFromFileTest {


    // @Test
    public void test() {
        Map<String, Object> map2 = null;
        try {
            map2 = ZipUtil.load("C:\\AAtmp\\match-4984710.zip", "recording-header", RecordingHeader.class,
                            RecordedItem.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(map2.keySet());

    }

}
