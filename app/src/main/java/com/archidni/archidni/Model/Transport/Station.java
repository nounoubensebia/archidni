package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Places.MapPlace;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.io.Serializable;

/**
 * Created by noure on 07/02/2018.
 */

public class Station extends MapPlace implements Serializable {
    private String name;
    private int transportMean;
    private int id;

    public Station(int id,String name,int transportMean,Coordinate coordinate) {
        super(App.getAppContext().getString(R.string.station) +" "+
                name,App.getAppContext().getString(R.string.station_of)+" "+
                TransportMean.allTransportMeans.get(transportMean).getName(), coordinate);
        this.name = name;
        this.transportMean = transportMean;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TransportMean getTransportMean() {
        return TransportMean.allTransportMeans.get(transportMean);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Station))
        {
            return false;
        }
        else
        {
            return (((Station)obj).getId()==id);
        }
    }

    public boolean isInsideBoundingBox(BoundingBox boundingBox)
    {
        return (getCoordinate().getLongitude()<boundingBox.getNorthEast().getLongitude()&&
        getCoordinate().getLongitude()>boundingBox.getSouthWest().getLongitude()&&
        getCoordinate().getLatitude()<boundingBox.getNorthEast().getLatitude()&&
        getCoordinate().getLatitude()>boundingBox.getSouthWest().getLatitude());
    }

    public String toJson ()
    {
        return new Gson().toJson(this);
    }

    public static Station fromJson (String json)
    {
        return new Gson().fromJson(json,Station.class);
    }

    @Override
    public int getMarkerDrawable() {
        return getTransportMean().getMarkerIcon();
    }

    @Override
    public int getSelectedMarkerDrawable() {
        return getTransportMean().getSelectedMarker();
    }

    @Override
    public int getColor() {
        return getTransportMean().getColor();
    }
}
