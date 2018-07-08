package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Interests.ParkingType;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.R;
import com.google.gson.Gson;

import java.io.Serializable;

public class Parking extends Place implements Serializable, MapPlace,MainListPlace {

    private int capacity;
    private int id;

    public Parking(String mainText,Coordinate coordinate, int capacity, int id) {
        super(mainText, "Capacité : "+capacity, coordinate);
        this.capacity = capacity;
        this.id = id;
    }

    @Override
    public boolean hasSecondaryText() {
        return true;
    }

    @Override
    public String getName() {
        return getMainText();
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

    @Override
    public int getIcon() {
        return R.drawable.marker_parking;
    }
}
