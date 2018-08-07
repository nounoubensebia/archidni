package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Places.PathPlaceAbstract;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class Station extends PathPlace implements Serializable,MainActivityPlace {
    private int transportMean;
    private int id;


    public Station(int id, String name,int transportMean, Coordinate coordinate) {
        super(name, coordinate);
        this.transportMean = transportMean;
        this.id = id;
    }

    public void setTransportMean(int transportMean) {
        this.transportMean = transportMean;
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
    public int getColor() {
        return getTransportMean().getColor();
    }

    @Override
    public int getIcon() {
        return getTransportMean().getIconEnabled();
    }

    @Override
    public int getMarkerResource() {
        return getTransportMean().getMarkerIcon();
    }

    @Override
    public int getSelectedMarkerResource() {
        return getTransportMean().getSelectedMarker();
    }

    @Override
    public String getDescription() {
        return "Station de "+TransportMean.allTransportMeans.get(transportMean).getName();
    }

    @Override
    public String getTitle() {
        return getDescription();
    }
}
