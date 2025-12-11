package ats.algo.genericsupportfunctions;

import java.util.HashMap;
import java.util.Map;

public class ArrayToMap {

    /**
     * converts a string array to Map<String, String>, where the keys are of the form "<prefix><nn>>", so eg if
     * prefix="goal" then first element will have key "goal1"
     * 
     * @param array
     * @param prefix
     * @return
     */
    public static Map<String, String> convert(String[] array, String prefix) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 1; i <= array.length; i++) {
            if (array[i] == null)
                break;
            else
                map.put(prefix + Integer.toString(i), array[i]);
        }
        return map;
    }

}
