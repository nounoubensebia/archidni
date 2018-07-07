package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;

import java.io.Serializable;

public abstract class MapPlace extends Place implements Serializable {

    public MapPlace(String mainText, String secondaryText, Coordinate coordinate) {
        super(mainText, secondaryText, coordinate);
    }

    public abstract int getMarkerDrawable ();

    public abstract int getSelectedMarkerDrawable();

    public abstract int getColor();
}
