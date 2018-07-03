package com.archidni.archidni.Model.Places;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.google.gson.Gson;

import java.io.Serializable;

public class Parking extends Place implements Serializable {

    private int capacity;

    public Parking (String name,Coordinate coordinate)
    {
        super(name,"",coordinate);
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
}
