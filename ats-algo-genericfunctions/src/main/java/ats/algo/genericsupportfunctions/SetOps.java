package ats.algo.genericsupportfunctions;

import java.util.HashSet;
import java.util.Set;

public class SetOps {

    /**
     * returns a set containing all members of set1 not in set2
     * 
     * @param set1 a set of objects of type T
     * @param set2 a set of objects of the same type T
     * @return a set of objects of type T
     */
    public static <T> Set<T> inSet1NotSet2(Set<T> set1, Set<T> set2) {
        Set<T> set = new HashSet<T>();
        for (T member : set1) {
            if (!set2.contains(member))
                set.add(member);
        }
        return set;

    }
}
