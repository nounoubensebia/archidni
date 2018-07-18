package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;

import java.io.Serializable;

public interface PlaceInterface extends Serializable {
    public String getName();
    public Coordinate getCoordinate();
}
