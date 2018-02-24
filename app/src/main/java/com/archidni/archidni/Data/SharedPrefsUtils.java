package com.archidni.archidni.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by noure on 03/02/2018.
 */

public class SharedPrefsUtils {
    public static String SHARED_PREFS_ENTRY_USER_SUGGESTIONS = "USER_SUGGESTIONS";
    public static String SHARED_PREFS_ENTRY_USER_OBJECT = "USER_OBJECT";
    public static String SHARED_PREFS_ENTRY_FAVORITES = "FAVORITES";
    public static String SHARED_PREFS_ENTRY_SERVER_URL= "SERVER_URL";


    public static String getServerUrl (Context context)
    {
        if (verifyKey(context,SHARED_PREFS_ENTRY_SERVER_URL))
        {
            return loadString(context,SHARED_PREFS_ENTRY_SERVER_URL);
        }
        else
        {
            saveString(context,SHARED_PREFS_ENTRY_SERVER_URL,"http://192.168.1.3:8000");
            return loadString(context,SHARED_PREFS_ENTRY_SERVER_URL);
        }
    }

    public static void saveString (Context context,String key,String s)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,s);
        editor.commit();
    }

    public static String loadString (Context context,String key)
    {
        if (verifyKey(context,key))
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String json = sharedPref.getString(key,null);
            return json;
        }
        else
        {
            return null;
        }
    }

    public static boolean verifyKey (Context context,String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.contains(key);
    }

    public static void disconnectUser (Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}
