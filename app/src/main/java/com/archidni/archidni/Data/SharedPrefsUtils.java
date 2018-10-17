package com.archidni.archidni.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.archidni.archidni.AccessToken;
import com.archidni.archidni.Model.Favorites;
import com.archidni.archidni.Model.User;
import com.google.gson.Gson;

/**
 * Created by noure on 03/02/2018.
 */

public class SharedPrefsUtils {
    public static String SHARED_PREFS_ENTRY_USER_SUGGESTIONS = "USER_SUGGESTIONS";
    public static String SHARED_PREFS_ENTRY_USER_OBJECT = "USER_OBJECT";
    public static String SHARED_PREFS_ENTRY_FAVORITES = "FAVORITES";
    public static String SHARED_PREFS_ENTRY_SERVER_URL= "SERVER_URL";
    public static String SHARED_PREFS_ENTRY_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String SHARED_PREFS_ENTRY_REFRESH_TOKEN = "REFRESH_TOKEN";
    public static String SHARED_PREFS_ENTRY_DESTROYER = "DESTROYER";
    public static String SHARED_PREFS_ENTRY_REQUEST_NUMBER = "REQUEST_NUMBER";

    public static boolean checkRequestNumber (Context context)
    {
        if (!verifyKey(context,SHARED_PREFS_ENTRY_REQUEST_NUMBER))
        {
            saveString(context,SHARED_PREFS_ENTRY_REQUEST_NUMBER,"0");
        }
        int requestNumber = Integer.parseInt(loadString(context,SHARED_PREFS_ENTRY_REQUEST_NUMBER));
        requestNumber++;
        if (requestNumber>0)
        {
            saveString(context,SHARED_PREFS_ENTRY_REQUEST_NUMBER,"0");
            return true;
        }
        else
        {
            saveString(context,SHARED_PREFS_ENTRY_REQUEST_NUMBER,requestNumber+"");
            return false;
        }
    }


    public static void destroyApp (Context context)
    {
        SharedPrefsUtils.saveString(context,SHARED_PREFS_ENTRY_DESTROYER,"lines");
    }

    public static boolean isAppDestroyed (Context context)
    {
        return verifyKey(context,SHARED_PREFS_ENTRY_DESTROYER);
    }

    public static String getServerUrl (Context context)
    {
        if (verifyKey(context,SHARED_PREFS_ENTRY_SERVER_URL))
        {
            return loadString(context,SHARED_PREFS_ENTRY_SERVER_URL);
        }
        else
        {
            saveString(context,SHARED_PREFS_ENTRY_SERVER_URL,"http://localhost");
            return loadString(context,SHARED_PREFS_ENTRY_SERVER_URL);
        }
    }

    public static void setAccessToken (Context context,AccessToken accessToken)
    {
        String json = new Gson().toJson(accessToken);
        saveString(context,SHARED_PREFS_ENTRY_ACCESS_TOKEN,json);
    }

    public static void setRefreshToken (Context context,String refreshToken)
    {
        saveString(context,SHARED_PREFS_ENTRY_REFRESH_TOKEN,refreshToken);
    }

    public static String getRefreshToken (Context context) {
        return loadString(context,SHARED_PREFS_ENTRY_REFRESH_TOKEN);
    }

    public static AccessToken getAccessToken (Context context)
    {
        AccessToken accessToken = new Gson().fromJson(loadString(context,SHARED_PREFS_ENTRY_ACCESS_TOKEN),
                AccessToken.class);
        return accessToken;
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

    public static boolean favoritesAdded (Context context)
    {
        if (SharedPrefsUtils.verifyKey(context,SHARED_PREFS_ENTRY_FAVORITES))
        {
            Favorites favorites = new Gson().fromJson(SharedPrefsUtils.loadString(context
                    ,SHARED_PREFS_ENTRY_FAVORITES),Favorites.class);
            if (favorites!=null&&favorites.getLines()!=null&&favorites.getLines().size()>0)
            {
                return  true;
            }
        }
        return false;
    }

    public static User getConnectedUser (Context context)
    {
        return new Gson().fromJson(loadString(context,SHARED_PREFS_ENTRY_USER_OBJECT),User.class);
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
