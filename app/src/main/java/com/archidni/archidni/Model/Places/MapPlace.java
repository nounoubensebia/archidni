package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;

import java.io.Serializable;

public interface MapPlace extends PlaceInterface{

    public abstract int getMarkerDrawable ();

    public abstract int getSelectedMarkerDrawable();

    public abstract int getColor();
}
