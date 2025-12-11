package ats.algo.genericsupportfunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapSort {

    /**
     * returns a list of the mapKeys sorted by reference to the values in the map
     * 
     * @param map
     * @return
     */
    public static <K, V extends Comparable<V>> List<K> sortKeys(Map<K, V> map) {
        List<K> list = new ArrayList<K>(map.size());
        for (K key : map.keySet())
            list.add(key);
        list.sort((e1, e2) -> map.get(e1).compareTo(map.get(e2)));
        return list;
    }

}
