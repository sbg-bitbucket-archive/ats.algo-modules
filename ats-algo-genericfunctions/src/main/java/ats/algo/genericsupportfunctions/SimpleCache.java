package ats.algo.genericsupportfunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * implements a cache of objects each with an associated key. The order in which objects are added to the cache is
 * preserved and a facility is provided to roll back a key in the cache by throwing away any more recent objects.
 * 
 * @author gicha
 *
 * @param <T>
 */
public class SimpleCache<K, T> {

    private int maxSize;
    protected Map<K, T> cache;
    List<K> keyList;

    public SimpleCache(int size) {
        cache = new HashMap<K, T>(size);
        keyList = new ArrayList<K>(size);
        this.maxSize = size;
    }


    /**
     * adds object to history. If history cache size is > maxSize then removes the oldest cached element and returns
     * 
     * @param key
     * @param object
     * @return if cache was full then oldest element, else null
     */
    public T add(K key, T object) {
        T oldestObject = null;
        if (keyList.contains(key)) {
            /*
             * if already in list just overwrite previous entry
             */
            cache.put(key, object);

        } else {
            if (keyList.size() == maxSize) {
                K reqId = keyList.remove(0);
                oldestObject = cache.remove(reqId);
            }
            keyList.add(key);
            cache.put(key, object);
        }
        return oldestObject;
    }

    /**
     * get the object associated with this key
     * 
     * @param key
     * @return null if not in the cache
     */
    public T get(K key) {
        return cache.get(key);
    }

    /**
     * returns the key for the object most recently added to the cache
     * 
     * @return
     */
    public K mostRecentKey() {
        K key = null;
        int i = keyList.size() - 1;
        if (i >= 0)
            key = keyList.get(i);
        return key;
    }

    // public T mostRecentEntry() {
    // return cache.get(mostRecentKey());
    // }

    /**
     * gets the most recent object in the history without affecting the cache in any way.
     * 
     * @return most recent eventState, or null if empty
     */
    public T getMostRecent() {
        T newestObject = null;
        K key = mostRecentKey();
        if (key != null)
            newestObject = cache.get(key);
        return newestObject;
    }

    int size() {
        if (cache.size() != keyList.size())
            throw new IllegalStateException("size mismatch");
        return cache.size();
    }

    public List<K> keyList() {
        return keyList;
    }


    /**
     * Rolls back to the specified key. If found then the object associated with this key and all more recent ones are
     * removed from the cache
     * 
     * @param key
     * @return null if not found, otherwise the T associated with this requestId
     */
    public T rollBackTo(K key) {
        T object = null;
        int i = 0;
        for (i = 0; i < keyList.size(); i++) {
            K reqId = keyList.get(i);
            if (reqId.equals(key)) {
                object = cache.get(reqId);
                break;
            }
        }
        Set<K> ids = new HashSet<K>();
        for (int j = i; j < keyList.size(); j++) {
            K reqId = keyList.get(j);
            ids.add(reqId);
            cache.remove(reqId);
        }
        keyList.removeAll(ids);
        return object;
    }

    @Override
    public String toString() {
        return "SimpleCache [maxSize=" + maxSize + ", history=" + cache + ", keyList=" + keyList + "]";
    }



}
