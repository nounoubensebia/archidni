package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Interests.ParkingType;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.R;
import com.google.gson.Gson;

import java.io.Serializable;

public class Parking extends MapPlace implements Serializable {

    private int capacity;
    private long id;

    public Parking(String mainText,Coordinate coordinate, int capacity, long id) {
        super(mainText, "Capacité : "+capacity, coordinate);
        this.capacity = capacity;
        this.id = id;
    }

    public long getId() {
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
    public int getMarkerDrawable() {
        return R.drawable.marker_parking;
    }

    @Override
    public int getSelectedMarkerDrawable() {
        return R.drawable.marker_parking_selected;
    }

    @Override
    public int getColor() {
        return R.color.color_transport_mean_selected_1;
    }
}
