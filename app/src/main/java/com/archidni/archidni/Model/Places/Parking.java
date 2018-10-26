package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.R;
import com.google.gson.Gson;

import java.io.Serializable;

public class Parking extends PathPlace implements Serializable, MainActivityPlace {

    private int id;
    private int capacity;

    public Parking(String name, Coordinate coordinate, int id, int capacity) {
        super(name, coordinate);
        this.id = id;
        this.capacity = capacity;
    }


    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public static Parking fromJson (String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json,Parking.class);
    }

    public String toJson ()
    {
        return new Gson().toJson(this);
    }


    @Override
    public int getMarkerResource() {
        return R.drawable.marker_parking;
    }

    @Override
    public int getSelectedMarkerResource() {
        return R.drawable.marker_parking_selected;
    }

    @Override
    public int getColor() {
        return R.color.colorBlue;
    }

    @Override
    public String getDescription() {
        return "Capacité : "+capacity;
    }

    @Override
    public int getIcon() {
        return R.drawable.marker_parking;
    }


    @Override
    public String getTitle() {
        return getName();
    }
}
