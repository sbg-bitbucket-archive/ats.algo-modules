package ats.algo.core.common;


/**
 * static methods for managing sequence Id's
 * 
 * @author Geoff
 *
 */
public class SequenceId {

    /**
     * Converts the sequenceId to a string, adding the specified offset. e.g. if input sequenceId is "S1.2", idPosn = 1
     * and idOffset = 2 then the result is S1.4
     * 
     * @param idOffset the offset to add to the rightmost element of the sequenceId
     * @return String representation of the current sequenceId
     */
    public static String getOffsetSequenceId(String s, int idOffset) {

        String prefix = getSequenceIdPrefix(s);
        int[] ids = getSequenceIdElements(s);
        if (ids == null)
            return s;
        String s2 = prefix;
        int n = ids.length;
        for (int i = 0; i < n; i++) {
            int id = ids[i];
            if (i == n - 1) {
                id += idOffset;
            }
            if (i == 0)
                s2 += String.format("%d", id);
            else
                s2 += String.format(".%d", id);
        }
        return s2;
    }

    /**
     * returns the first char of the secquence Id string e.g. if sequenceId is "G2" returns "G"
     * 
     * @param sequenceId
     * @return
     */
    public static String getSequenceIdPrefix(String sequenceId) {
        return sequenceId.substring(0, 1);
    }



    /**
     * returns an array of the sequenceId elements. e.g. if sequenceId is "S2.3" returns the two element array [2,3].
     * 
     * @param s
     * @return array of elements or null if there are none - i.e. sequenceId consists only of a prefix, e.g. "M"
     */
    public static int[] getSequenceIdElements(String s) {
        if (s.length() == 1) {
            /*
             * nothing to parse - single char only
             */
            return null;
        }
        String[] sBits = s.substring(1).split("[.]");

        int[] ids = new int[sBits.length];
        try {
            for (int i = 0; i < sBits.length; i++)
                ids[i] = Integer.parseInt(sBits[i]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Unrecognized format in sequenceId");
        }
        return ids;
    }

    /**
     * converts a sequenceId string to an int so that it can be compared with another sequenceId of the same format. so
     * e.g. "S2.3" would be converted to "2*100+3 = 203. "S2.3.1" would be converted to 2*10000+3*100+1 = 20301. Assumes
     * that none of the components can exceed 100.
     * 
     * @param s
     * @return
     */
    public static int getSequenceIdComparator(String s) {
        int[] sBits = getSequenceIdElements(s);
        int result = sBits[0];
        for (int i = 1; i < sBits.length; i++)
            result = 100 * result + sBits[i];
        return result;
    }

    public static int compare(String s1, String s2) {
        return getSequenceIdComparator(s1) - getSequenceIdComparator(s2);
    }

}
