package ats.algo.genericsupportfunctions;

import java.util.ArrayList;
import java.util.List;

public class SplitString {

    /**
     * Splits a string into a number of substrings, each of length <= maxSize
     * 
     * @param str
     * @param maxSize
     * @return
     */
    public static List<String> split(String str, int maxSize) {
        List<String> ret = new ArrayList<String>((str.length() + maxSize - 1) / maxSize);
        for (int start = 0; start < str.length(); start += maxSize) {
            ret.add(str.substring(start, Math.min(str.length(), start + maxSize)));
        }
        return ret;
    }

}
