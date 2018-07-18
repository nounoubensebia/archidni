package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.LineSection;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.TransportMean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 11/02/2018.
 */

public class RideInstruction extends MoveInstruction implements Serializable {

    private long transportMeanId; //id berk
    private ArrayList<Section> sections; //tronçons
    private String lineLabel; //name
    private String terminus; //for example dergana centre
    private ArrayList<Coordinate> polyline;

    public RideInstruction(int duration, long transportMeanId, ArrayList<Section> sections, String lineLabel, String terminus, ArrayList<Coordinate> polyline) {
        super(duration);
        this.transportMeanId = transportMeanId;
        this.sections = sections;
        this.lineLabel = lineLabel;
        this.terminus = terminus;
        this.polyline = polyline;
    }

    public String getLineLabel() {
        return lineLabel;
    }

    public String getTerminus() {
        return terminus;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    @Override
    public String getMainText() {
        return ("Prendre le "+getTransportMean().getName()+" Ligne : "+lineLabel);
    }

    @Override
    public String getSecondaryText ()
    {
        int s = sections.size();
        if (transportMeanId!=TransportMean.ID_BUS&&transportMeanId!=4)
        return (s+" arrêts"+" ("+(getDuration()/60)+" minutes)");
        else
        {
            return (s+" arrêts");
        }
    }

    @Override
    public long getInstructionIcon() {
        return getTransportMean().getStationCirleDrawableId();
    }

    @Override
    public int getInstructionWhiteIcon() {
        return 0;
    }

    /*@Override
    public ArrayList<Coordinate> getPolyline() {
        Coordinate coordinate = sections.get(0).getOrigin().getCoordinate();
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);
        for (int i=0;i<sections.size();i++)
        {
            coordinates.add(sections.get(i).getDestination().getCoordinate());
        }
        return coordinates;
    }*/

    public String getExitInstructionText ()
    {
        return ("Descendre à la station "+sections.get(sections.size()-1).getDestination().getName());
    }


    public TransportMean getTransportMean() {
        return TransportMean.allTransportMeans.get((int)transportMeanId);
    }

    @Override
    public float getDistance() {
        float distance = 0;
        for (Section section:sections)
        {
            distance+= section.getDistance();
        }
        return distance;
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        return polyline;
    }

    @Override
    public String getDurationString() {
        if (transportMeanId!=TransportMean.ID_BUS&&transportMeanId!=4)
        return (getDuration()/60) + " minutes";
        else
            return "";
    }

    public void setTransportMeanId(int transportMeanId) {
        this.transportMeanId = transportMeanId;
    }
}
