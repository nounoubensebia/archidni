package com.archidni.archidni.Model;

import com.google.gson.Gson;

/**
 * Created by noure on 02/02/2018.
 */

public class Place {
    private String mainText;
    private String secondaryText;
    private Coordinate coordinate;

    public Place(String mainText, String secondaryText, Coordinate coordinate) {
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        this.coordinate = coordinate;
    }

    public String getMainText() {
        return mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String toJson ()
    {
        return new Gson().toJson(this);
    }

    public static Place fromJson (String json)
    {
        return new Gson().fromJson(json,Place.class);
    }
}
