package ats.algo.genericsupportfunctions;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides static methods for generating sets of strings associated with all the members for an Enum class and for some
 * simple types. The objective is to provide a list which can be used to generate a drop down list of all the valid
 * choices for that item
 * 
 * @author Geoff
 *
 */
public class OptionsList {

    /**
     * Gets a set of Strings containing all the possible values of the enum
     * 
     * @param enumClass the class name of the enum, so e.g. Surface.class
     * @return the set of all possible values in String format
     */
    public static <T extends Enum<?>> Set<String> getEnumSet(Class<T> enumClass) {
        T[] x = enumClass.getEnumConstants();
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < x.length; i++)
            set.add(x[i].toString());
        return set;
    }

    /**
     * returns a set containing the strings "true", "false"
     * 
     * @return
     */
    public static Set<String> getBooleanSet() {
        Set<String> set = new HashSet<String>();
        set.add("true");
        set.add("false");
        return set;
    }


    /**
     * returns a set of strings containing all the integers in the supplied list
     * 
     * @param intList
     * @return
     */
    public static Set<String> getIntegerSet(int[] intList) {
        Set<String> set = new HashSet<String>();
        for (int i : intList)
            set.add(String.valueOf(i));
        return set;
    }

    /**
     * returns a set of strings containing all the doubles in the supplied list
     * 
     * @param range
     * @return
     */
    public static Set<String> getStringSetFromRange(int range) {
        Set<String> set = new HashSet<String>();
        for (int i = 0; i <= range; i++)
            for (int j = 0; j <= range; j++)
                set.add(String.valueOf(i) + ":" + String.valueOf(j));

        return set;
    }

}
