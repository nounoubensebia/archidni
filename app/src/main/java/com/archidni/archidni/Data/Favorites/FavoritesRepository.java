package com.archidni.archidni.Data.Favorites;

import android.content.Context;

import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.Favorites;
import com.archidni.archidni.Model.Transport.Line;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by nouno on 17/02/2018.
 */

public class FavoritesRepository {

    public void addLineToFavorites (Context context, Line line)
    {
        if (SharedPrefsUtils.verifyKey(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES))
        {
            Favorites favorites = new Gson().fromJson(SharedPrefsUtils.loadString(context,
                    SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES),Favorites.class);
            favorites.getLines().add(line);
            SharedPrefsUtils.saveString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES,
                    new Gson().toJson(favorites));
        }
        else
        {
            ArrayList<Line> lines = new ArrayList<>();
            lines.add(line);
            Favorites favorites = new Favorites(lines);
            SharedPrefsUtils.saveString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES,
                    new Gson().toJson(favorites));
        }
    }

    public void deleteLine (Context context,Line line)
    {
        Favorites favorites = new Gson().fromJson(SharedPrefsUtils.loadString(context,
                SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES),Favorites.class);
        for (int i = 0; i < favorites.getLines().size(); i++) {
            if (favorites.getLines().get(i).getId()==line.getId())
            {
                favorites.getLines().remove(i);
                SharedPrefsUtils.saveString(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES,
                        new Gson().toJson(favorites));
                return;
            }
        }
    }

    public boolean lineExists (Context context,Line line)
    {
        if (SharedPrefsUtils.verifyKey(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES))
        {
            Favorites favorites = new Gson().fromJson(SharedPrefsUtils.loadString(context,
                    SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES),Favorites.class);
            for (Line line1:favorites.getLines())
            {
                if (line.getId()==line1.getId())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public Favorites getFavorites (Context context)
    {
        if (SharedPrefsUtils.verifyKey(context,SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES))
        {
            Favorites favorites = new Gson().fromJson(SharedPrefsUtils.loadString(context,
                    SharedPrefsUtils.SHARED_PREFS_ENTRY_FAVORITES),Favorites.class);
            return favorites;
        }
        else
        {
            return null;
        }
    }
}
