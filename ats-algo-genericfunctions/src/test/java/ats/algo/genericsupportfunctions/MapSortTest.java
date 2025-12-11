package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MapSortTest {

    @Test
    public void test() {
        Map<String, Integer> map = new HashMap<String, Integer>(4);
        map.put("four", 4);
        map.put("one", 1);
        map.put("three", 3);
        map.put("two", 2);
        List<String> list = MapSort.sortKeys(map);
        for (String key : list)
            System.out.println(key);
        assertEquals("two", list.get(1));
        assertEquals("four", list.get(3));
    }

}
