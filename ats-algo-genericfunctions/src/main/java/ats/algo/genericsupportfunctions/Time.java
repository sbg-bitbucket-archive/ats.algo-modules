package ats.algo.genericsupportfunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {

    static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    static final SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss zzz");

    /**
     * returns the current time HH:MM:SS
     * 
     * @return
     */
    public static String getTimeAsString() {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    /*
     * returns the specified time as HH:MM:SS
     */
    public static String getTimeAsString(long mSecs) {
        int secs = (int) (mSecs / 1000);
        int hours = secs / 3600;
        int minutes = (secs - hours * 3600) / 60;
        int secs2 = secs - hours * 3600 - minutes * 60;
        int deciSecs = (int) (mSecs - ((long) secs) * 1000) / 10;
        return String.format("%02d:%02d:%02d.%01d", hours, minutes, secs2, deciSecs);
    }

    /*
     * returns the specified time as yyyy.MM.dd HH:mm:ss
     */
    public static String getDateAsString(long mSecs) {
        if (mSecs == 0)
            return "-";
        else {
            Date date = new Date(mSecs);
            return ft.format(date);
        }
    }
}
