package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;

import java.io.Serializable;

public abstract class PathPlaceAbstract extends Place implements Serializable {

    public PathPlaceAbstract(String name, Coordinate coordinate) {
        super(name, coordinate);
    }

    public abstract String getTitle();
}
