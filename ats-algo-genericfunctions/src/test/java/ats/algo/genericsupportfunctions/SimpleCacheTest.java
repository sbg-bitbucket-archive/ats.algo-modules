package ats.algo.genericsupportfunctions;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SimpleCacheTest {

    @Test
    public void test() {
        SimpleCache<Integer, String> cache = new SimpleCache<Integer, String>(3);
        String x = cache.add(1, "Object1");
        assertEquals(x, null);
        x = cache.add(2, "Object2");
        assertEquals(x, null);
        assertEquals(2, cache.size());
        x = cache.add(3, "Object3");
        assertEquals(x, null);
        assertEquals(3, cache.size());
        assertEquals(3, (int) cache.mostRecentKey());
        x = cache.add(4, "Object4");
        assertEquals(x, "Object1");
        assertEquals(3, cache.size());
        assertEquals(4, (int) cache.mostRecentKey());
        assertEquals("Object4", cache.getMostRecent());
        x = cache.add(3, "Object3v2");
        assertEquals(x, null);
        x = cache.get(2);
        assertEquals(x, "Object2");
        x = cache.get(3);
        assertEquals(x, "Object3v2");
        x = cache.getMostRecent();
        assertEquals(x, "Object4");
        x = cache.rollBackTo(3);
        assertEquals(x, "Object3v2");
        assertEquals(1, cache.size());
        x = cache.get(9999);
        assertEquals(null, x);
        x = cache.add(5, "Object5");
        List<Integer> y = cache.keyList();
        assertEquals(2, y.size());
        assertEquals(5, (int) y.get(1));

    }

}
