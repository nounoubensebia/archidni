package com.archidni.archidni.Model.Path;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.LineSection;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.TransportMean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 11/02/2018.
 */

public class RideInstruction extends MoveInstruction implements Serializable {

    private long duration;
    private long transportMeanId; //id berk
    private ArrayList<Section> sections; //tronçons
    private ArrayList<RideLine> rideLines; //name//for example dergana centre
    private String polyline;
    private float errorMargin;


    public RideInstruction(long duration, long transportMeanId, ArrayList<Section> sections, ArrayList<RideLine> rideLines, String polyline, float errorMargin) {
        this.duration = duration;
        this.transportMeanId = transportMeanId;
        this.sections = sections;
        this.rideLines = rideLines;
        this.polyline = polyline;
        this.errorMargin = errorMargin;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public ArrayList<RideLine> getRideLines() {
        return rideLines;
    }

    @Override
    public long getDuration() {
        return (long) (duration + duration*errorMargin);
    }

    @Override
    public String getTtile() {
        return "Prendre le bus";
    }

    public Station getDestination ()
    {
        return (sections.get(sections.size()-1).getDestination());
    }

    public Station getOrigin ()
    {
        return (sections.get(0).getOrigin());
    }

    public String getExitInstructionText ()
    {
        return ("Descendre à la station "+sections.get(sections.size()-1).getDestination().getName());
    }

    public ArrayList<Station> getStations ()
    {
        ArrayList<Station> stations = TransportUtils.getStationsFromSections(sections);
        for (Station station:stations)
        {
            station.setTransportMean((int)transportMeanId);
        }
        return new ArrayList<>(stations);
    }

    public TransportMean getTransportMean() {
        return TransportMean.allTransportMeans.get((int)transportMeanId);
    }

    @Override
    public float getDistance() {
        return GeoUtils.distance(getPolyline());
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        return GeoUtils.getPolylineFromGoogleMapsString(polyline);
    }

    public String getDurationString() {
        if (transportMeanId!=TransportMean.ID_BUS&&transportMeanId!=4)
        return ("environ "+getDuration()/60) + " minutes";
        else
            return "";
    }

}
