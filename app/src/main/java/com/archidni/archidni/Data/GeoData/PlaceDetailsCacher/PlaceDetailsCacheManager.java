package com.archidni.archidni.Data.GeoData.PlaceDetailsCacher;

import android.util.Pair;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.CacheManager;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.Places.PathPlace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PlaceDetailsCacheManager implements CacheManager<String,PathPlace> {

    private static final String CACHING_ENTRY = "PLACE_DETAILS_CASH_ENTRY";

    @Override
    public PathPlace get(String key) {
        if (SharedPrefsUtils.verifyKey(App.getAppContext(),CACHING_ENTRY))
        {
            ArrayList<PlaceDetailsEntry> entries = new Gson().fromJson(
                    SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                    new TypeToken<ArrayList<PlaceDetailsEntry>>(){}.getType());
            for (PlaceDetailsEntry entry:entries)
            {
                if (key.equals(entry.getKey()))
                {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void set(String key, PathPlace value) {
        ArrayList<PlaceDetailsEntry> entries;
        PathPlace pathPlace = get(key);
        if (pathPlace==null)
        {
            if (SharedPrefsUtils.verifyKey(App.getAppContext(),CACHING_ENTRY))
            {
                String s = SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY);
                entries = new Gson().fromJson(
                        SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                        new TypeToken<ArrayList<PlaceDetailsEntry>>(){}.getType());
            }
            else
            {
                entries = new ArrayList<>();
            }
            if (entries.size()<1000)
                entries.add(new PlaceDetailsEntry(key,value));
            else
                entries.set(0,new PlaceDetailsEntry(key,value));
        }
        else
        {
            entries = new Gson().fromJson(
                    SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                    new TypeToken<ArrayList<PlaceDetailsEntry>>(){}.getType());
            for (int i=0;i<entries.size();i++)
            {
                PlaceDetailsEntry entry = entries.get(i);
                if (entry.getKey().equals(key))
                {
                    entries.set(i,new PlaceDetailsEntry(key,value));
                }
            }
        }
        SharedPrefsUtils.saveString(App.getAppContext(),CACHING_ENTRY,new Gson().toJson(entries));
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clear() {

    }
}
