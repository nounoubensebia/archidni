package com.archidni.archidni.Data.GeoData.AutocompleteCacher;

import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;

import java.util.ArrayList;

public class AutocompleteEntry {
    private String key;
    private ArrayList<TextQuerySuggestion> value;

    public AutocompleteEntry(String key, ArrayList<TextQuerySuggestion> value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<TextQuerySuggestion> getValue() {
        return value;
    }
}
