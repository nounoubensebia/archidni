package com.archidni.archidni.Model;

import com.archidni.archidni.App;
import com.archidni.archidni.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by noure on 03/02/2018.
 */

public class StringUtils {
    public static String getTextFromDuration (long seconds)
    {
        int hours = (int) seconds/3600;
        int minutes = (int)(seconds-hours*3600)/60;
        String hourString= "";
        if (hours>0)
        {
            if (hours==1)
            {
                hourString = hours + " " +App.getAppContext().getString(R.string.hour)+" ";
            }
            else
            {
                hourString = hours + " "+ App.getAppContext().getString(R.string.hours)+" ";
            }
        }
        return hourString +""+ minutes +" "+
                App.getAppContext().getString(R.string.minutes);
    }
    public static String getTextFromDistance (int meters)
    {
        float km = meters/1000;
        int m = (meters-(int)(meters/1000));
        if (km>1)
        {
            return km+" "+"Km";
        }
        else
        {
            return m+" "+App.getAppContext().getString(R.string.metre);
        }
    }

    public static String getTimeString (long timestamp)
    {
        int hours = (int) timestamp / 3600;
        int minutes = (int) timestamp / 60 - hours * 60;
        if (minutes >= 10)
            return (hours + ":" + minutes);
        else
            return (hours + ":" + 0 + minutes);
    }

    public static String getLocationString(Coordinate coordinate)
    {
        return (float)coordinate.getLatitude()+", "+(float)coordinate.getLongitude();
    }


    public static String getDateString (long timestamp)
    {
        Calendar chosenCal = Calendar.getInstance();
        chosenCal.setTimeInMillis(timestamp * 1000);
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTimeInMillis(System.currentTimeMillis());
        if (chosenCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                chosenCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH) &&
                chosenCal.get(Calendar.DAY_OF_MONTH) == currentCal.get(Calendar.DAY_OF_MONTH))
            return (App.getAppContext().getString(R.string.today));
        else {
            SimpleDateFormat format = new SimpleDateFormat("EEEE, d MMMM , yyyy");
            return (format.format(chosenCal.getTime()));
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidPassword(String password) {
        return password.length()>5;
    }

    public static boolean isValidName(String name )
    {
        return name.matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" );
    }
}
