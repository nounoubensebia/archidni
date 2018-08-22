package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.TransportMean;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by noure on 07/02/2018.
 */

public class Line extends LineSkeleton implements Serializable {

    //TODO use builder pattern and eliminate LineSkeleton

    private ArrayList<LineSection> lineSections;

    public Line(int id, String name, TransportMean transportMean, ArrayList<LineSection> lineSections) {
        super(id, name, transportMean);
        this.lineSections = lineSections;
    }



    public ArrayList<Station> getStations() {
        Station first = lineSections.get(0).getOrigin();
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(first);
        for (LineSection lineSection : lineSections) {
            stations.add(lineSection.getDestination());
        }
        return stations;
    }

    public ArrayList<Coordinate> getPolyline(boolean outBound) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        ArrayList<LineSection> selectedLineSections;
        if (outBound) {
            selectedLineSections = getOutboundSections();
        } else {
            selectedLineSections = getInboundSections();
        }
        for (LineSection lineSection : selectedLineSections) {
            List<LatLng> latLngs = PolyUtil.decode(lineSection.getPolylineString());
            for (LatLng latLng : latLngs) {
                coordinates.add(new Coordinate(latLng.latitude, latLng.longitude));
            }
        }
        return coordinates;
    }

    public ArrayList<Coordinate> getPolyline() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (LineSection lineSection:lineSections)
        {
            coordinates.addAll(GeoUtils.getPolylineFromGoogleMapsString(lineSection.getPolylineString()));
        }
        return coordinates;
    }



    public boolean insideSearchCircle(ArrayList<Coordinate> coordinates, float distance) {
        for (Station station : getStations()) {
            for (Coordinate coordinate : coordinates) {
                if (GeoUtils.distance(station.getCoordinate(), coordinate) < distance)
                    return true;
            }
        }
        return false;
    }



    public Station getOrigin() {
        if (!isBusLine())
            return lineSections.get(0).getOrigin();
        else {
            return getOutboundSections().get(0).getOrigin();
        }
    }

    public Station getDestination() {
        if (!isBusLine())
            return lineSections.get(lineSections.size() - 1).getDestination();
        else {
            return getOutboundSections().get(getOutboundSections().size() - 1).getDestination();
        }
    }



    public boolean hasStationInsideBoundingBox(BoundingBox boundingBox) {
        return (TransportUtils.filterStations(getStations(), boundingBox).size() > 0);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Line fromJson(String json) {
        return new Gson().fromJson(json, Line.class);
    }

    private ArrayList<Coordinate> getDirectPolyline() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(lineSections.get(0).getOrigin().getCoordinate());
        for (LineSection lineSection : lineSections) {
            coordinates.add(lineSection.getDestination().getCoordinate());
        }
        return coordinates;
    }

    public ArrayList<LineSection> getLineSections() {
        return lineSections;
    }

    public boolean isBusLine() {
        for (LineSection lineSection : lineSections) {
            if (lineSection.getMode() != 0) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Station> getInboundStations() {
        return TransportUtils.getStationsFromSections(getInboundSections());
    }

    public ArrayList<Station> getOutboundStations() {
        return TransportUtils.getStationsFromSections(getOutboundSections());
    }

    private ArrayList<LineSection> getInboundSections() {
        return getSectionByMode(1);
    }

    private ArrayList<LineSection> getOutboundSections() {
        return getSectionByMode(0);
    }

    private ArrayList<LineSection> getSectionByMode(int mode) {
        ArrayList<LineSection> sectionsByMode = new ArrayList<>();
        for (LineSection lineSection : lineSections) {
            if (lineSection.getMode() == mode)
                sectionsByMode.add(lineSection);
        }
        return sectionsByMode;
    }

}
