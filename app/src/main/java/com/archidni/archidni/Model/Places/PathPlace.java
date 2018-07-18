package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.google.gson.Gson;

import java.io.Serializable;

public class PathPlace extends PathPlaceAbstract implements Serializable {


    public PathPlace(String name, Coordinate coordinate) {
        super(name, coordinate);
    }


    @Override
    public String getTitle() {
        return getName();
    }

    public static PathPlace fromJson(String string) {
        return new Gson().fromJson(string,PathPlace.class);
    }
}
