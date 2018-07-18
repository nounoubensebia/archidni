package com.archidni.archidni.Model.Places;

import java.io.Serializable;

public interface MainActivityPlace extends PlaceInterface,Serializable {

    public abstract int getMarkerResource ();

    public abstract int getSelectedMarkerResource();

    public int getColor();

    public String getDescription ();

    public int getId();

    public int getIcon();
}
