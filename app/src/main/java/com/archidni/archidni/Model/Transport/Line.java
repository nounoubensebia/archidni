package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.TransportMean;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class Line implements Serializable {
    private int id;
    private String name;
    private TransportMean transportMean;
    private ArrayList<LineSection> lineSections;

    Line(int id, String name, TransportMean transportMean, ArrayList<LineSection> lineSections) {
        this.id = id;
        this.name = name;
        this.transportMean = transportMean;
        this.lineSections = lineSections;
    }

    public ArrayList<Station> getStations ()
    {
        Station first = lineSections.get(0).getOrigin();
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(first);
        for (LineSection lineSection : lineSections)
        {
            stations.add(lineSection.getDestination());
        }
        return stations;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }

    public boolean insideSearchCircle (ArrayList<Coordinate> coordinates,float distance)
    {
        for (Station station:getStations())
        {
            for (Coordinate coordinate: coordinates)
            {
                if (GeoUtils.distance(station.getCoordinate(),coordinate)<distance)
                    return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public Station getOrigin()
    {
        return lineSections.get(0).getOrigin();
    }

    public Station getDestination()
    {
        return lineSections.get(lineSections.size()-1).getDestination();
    }

    public String getName() {
        return name;
    }

    public boolean hasStationInsideBoundingBox (BoundingBox boundingBox)
    {
        return (TransportUtils.filterStations(getStations(),boundingBox).size()>0);
    }

    public String toJson ()
    {
        return new Gson().toJson(this);
    }

    public static Line fromJson (String json)
    {
        return new Gson().fromJson(json,Line.class);
    }

    public ArrayList<Coordinate> getPolyline() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(lineSections.get(0).getOrigin().getCoordinate());
        for (LineSection lineSection : lineSections)
        {
            coordinates.add(lineSection.getDestination().getCoordinate());
        }
        return coordinates;
    }

    public ArrayList<LineSection> getLineSections() {
        return lineSections;
    }

    public boolean isBusLine ()
    {
        for (LineSection lineSection : lineSections)
        {
            if (lineSection.getMode()!=0)
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Station> getInboundStations ()
    {
        return TransportUtils.getStationsFromSections(getInboundSections());
    }

    public ArrayList<Station> getOutboundStations ()
    {
        return TransportUtils.getStationsFromSections(getOutboundSections());
    }

    private ArrayList<LineSection> getInboundSections ()
    {
        return getSectionByMode(2);
    }

    private ArrayList<LineSection> getOutboundSections ()
    {
        return getSectionByMode(1);
    }

    private ArrayList<LineSection> getSectionByMode(int mode)
    {
        ArrayList<LineSection> sectionsByMode = new ArrayList<>();
        for (LineSection lineSection : lineSections)
        {
            if (lineSection.getMode()==mode)
                sectionsByMode.add(lineSection);
        }
        return sectionsByMode;
    }

    public static class Builder {
        private int id;
        private String name;
        private TransportMean transportMean;
        private ArrayList<LineSection> lineSections;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public void setTransportMean(TransportMean transportMean) {
            this.transportMean = transportMean;
        }

        public void setLineSections(ArrayList<LineSection> lineSections) {
            this.lineSections = lineSections;
        }

        public Line build ()
        {
            return new Line(id,name,transportMean, lineSections);
        }
    }
}
