package com.archidni.archidni.Model;

import com.google.gson.Gson;


import java.io.Serializable;

/**
 * Created by noure on 02/02/2018.
 */

public class Coordinate implements Serializable {
    private double latitude;
    private double longitude;

    public static final Coordinate DEFAULT_LOCATION = new Coordinate(36.728566111788055,
            3.1676888465881348);

    public Coordinate(Double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    public com.google.android.gms.maps.model.LatLng toGoogleMapLatLng ()
    {
        return new com.google.android.gms.maps.model.LatLng(latitude,longitude);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Coordinate fromJson(String json) {
        return new Gson().fromJson(json,Coordinate.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate))
        {
            return false;
        }
        else
        {
            Coordinate coordinate = (Coordinate)obj;
            return (coordinate.getLatitude()==latitude&&coordinate.getLongitude()==longitude);
        }
    }
}
