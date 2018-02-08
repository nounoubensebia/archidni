package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class Line {
    private int id;
    private String name;
    private TransportMean transportMean;
    private ArrayList<Section> sections;


    public Line(int id, String name, TransportMean transportMean, ArrayList<Section> sections) {
        this.id = id;
        this.name = name;
        this.transportMean = transportMean;
        this.sections = sections;
    }

    public ArrayList<Station> getStations ()
    {
        Station first = sections.get(0).getOrigin();
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(first);
        for (Section section:sections)
        {
            stations.add(section.getDestination());
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
        return sections.get(0).getOrigin();
    }

    public Station getDestination()
    {
        return sections.get(sections.size()-1).getDestination();
    }

    public String getName() {
        return name;
    }

    public boolean hasStationInsideBoundingBox (BoundingBox boundingBox)
    {
        return (TransportUtils.filterStations(getStations(),boundingBox).size()>0);
    }
}