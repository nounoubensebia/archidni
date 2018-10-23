package com.archidni.archidni.Data.GeoData.PlaceDetailsCacher;

import com.archidni.archidni.Model.Places.PathPlace;

public class PlaceDetailsEntry {
    private String key;
    private PathPlace value;

    public PlaceDetailsEntry(String key, PathPlace value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public PathPlace getValue() {
        return value;
    }
}
