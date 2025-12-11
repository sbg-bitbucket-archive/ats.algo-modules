package ats.algo.genericsupportfunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunctions {

    public static String millisToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
