package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;

public interface MainListPlace {


    public boolean hasSecondaryText();

    public String getSecondaryText();

    public String getName();

    public Coordinate getCoordinate();

    public int getId();

    public int getColor();

    public int getIcon();

}
