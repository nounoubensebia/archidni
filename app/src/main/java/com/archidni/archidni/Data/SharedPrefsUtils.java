package com.archidni.archidni.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by noure on 03/02/2018.
 */

public class SharedPrefsUtils {
    public static String SHARED_PREFS_ENTRY_USER_SUGGESTIONS;

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
}
