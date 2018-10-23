package com.archidni.archidni.Data.GeoData.AutocompleteCacher;

import com.archidni.archidni.App;
import com.archidni.archidni.Data.CacheManager;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AutocompleteCacheManager implements CacheManager<String,ArrayList<TextQuerySuggestion>> {

    private static final String CACHING_ENTRY = "AUTOCOMPLETE_CACHE_ENTRY";

    @Override
    public ArrayList<TextQuerySuggestion> get(String key) {
        if (SharedPrefsUtils.verifyKey(App.getAppContext(),CACHING_ENTRY))
        {
            ArrayList<AutocompleteEntry> entries = new Gson().fromJson(
                    SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                    new TypeToken<ArrayList<AutocompleteEntry>>(){}.getType());
            for (AutocompleteEntry entry:entries)
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
    public void set(String key, ArrayList<TextQuerySuggestion> value) {
        ArrayList<AutocompleteEntry> entries;
        ArrayList<TextQuerySuggestion> textQuerySuggestion = get(key);
        if (textQuerySuggestion==null)
        {
            if (SharedPrefsUtils.verifyKey(App.getAppContext(),CACHING_ENTRY))
            {
                String s = SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY);
                entries = new Gson().fromJson(
                        SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                        new TypeToken<ArrayList<AutocompleteEntry>>(){}.getType());
            }
            else
            {
                entries = new ArrayList<>();
            }
            if (entries.size()<1000)
                entries.add(new AutocompleteEntry(key,value));
            else
                entries.set(0,new AutocompleteEntry(key,value));
        }
        else
        {
            entries = new Gson().fromJson(
                    SharedPrefsUtils.loadString(App.getAppContext(),CACHING_ENTRY),
                    new TypeToken<ArrayList<AutocompleteEntry>>(){}.getType());
            for (int i=0;i<entries.size();i++)
            {
                AutocompleteEntry entry = entries.get(i);
                if (entry.getKey().equals(key))
                {
                    entries.set(i,new AutocompleteEntry(key,value));
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
