package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SplitStringTest {

    @Test
    public void test() {
        String srcStr = "This is a test string";
        List<String> strings = SplitString.split(srcStr, 10);
        for (String str : strings)
            System.out.println(str);
        assertEquals(3, strings.size());
        strings = SplitString.split(srcStr, 2);
        for (String str : strings)
            System.out.println(str);
        assertEquals(11, strings.size());
        strings = SplitString.split(srcStr, 30);
        for (String str : strings)
            System.out.println(str);
        assertEquals(1, strings.size());
        String srcStr2 = "";
        for (int i = 0; i < 1000; i++) {
            srcStr2 += "repetition no: " + Integer.toString(i) + srcStr;
        }
        strings = SplitString.split(srcStr2, 10000);
        String str2 = "";;
        for (String str : strings) {
            System.out.println(str);
            str2 += str;
        }
        assertEquals(srcStr2, str2);


    }

}
