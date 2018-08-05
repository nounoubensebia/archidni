package com.archidni.archidni;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by noure on 03/02/2018.
 */

public class TimeUtils {

    private static final long DAY_SUNDAY = 1;
    private static final long DAY_MONDAY = 2;
    private static final long DAY_THURSDAY =3;
    private static final long DAY_WEDNESDAY = 4;
    private static final long DAY_TUESDAY = 5;
    private static final long DAY_FRIDAY = 6;
    private static final long DAY_SATURDAY = 7;

    public static long getDayFromTimeStamp (long timeStamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timeStamp*1000));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek)
        {
            case Calendar.SUNDAY : return DAY_SUNDAY;
            case Calendar.SATURDAY: return DAY_SATURDAY;
            case Calendar.THURSDAY : return DAY_THURSDAY;
            case Calendar.WEDNESDAY: return DAY_WEDNESDAY;
            case Calendar.MONDAY : return DAY_MONDAY;
            case Calendar.FRIDAY: return DAY_FRIDAY;
            case Calendar.TUESDAY : return DAY_TUESDAY;

        }
        return -1;
    }

    public static long getCurrentTimeInSeconds ()
    {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        return now/1000;
    }

    public static long getSecondsFromMidnight ()
    {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long secondsPassed = passed / 1000;
        return secondsPassed;
    }

    public static long getTimeFromString (String s)
    {
        String[] units = s.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]); //first element
        int seconds = Integer.parseInt(units[2]); //second element
        return (long) (3600 * hours + 60 * minutes + seconds);
    }
}
