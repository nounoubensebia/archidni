package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.google.gson.Gson;

import java.io.Serializable;

public class Place implements PlaceInterface,Serializable {
    private String name;
    private Coordinate coordinate;

    public Place(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String toJson ()
    {
        return new Gson().toJson(this);
    }
}
