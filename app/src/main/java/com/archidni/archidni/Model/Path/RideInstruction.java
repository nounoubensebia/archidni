package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.TransportMean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 11/02/2018.
 */

public class RideInstruction extends MoveInstruction implements Serializable {

    private long transportMeanId; //id berk
    private ArrayList<PathSection> sections; //tronçons
    private String lineLabel; //name
    private String terminus; //for example dergana centre

    public RideInstruction(int duration,long transportMean, ArrayList<PathSection> sections, String lineLabel, String terminus) {
        super(duration);
        this.transportMeanId = transportMean;
        this.sections = sections;
        this.lineLabel = lineLabel;
        this.terminus = terminus;
    }

    public ArrayList<PathSection> getSections() {
        return sections;
    }

    @Override
    public String getMainText() {
        return ("Prendre le "+getTransportMean().getName()+" Ligne : "+lineLabel+ " vers "+terminus);
    }

    @Override
    public String getSecondaryText ()
    {
        int s = sections.size();

        return (s+" arrêts"+" ("+(getDuration()/60)+" minutes)");
    }

    @Override
    public long getInstructionIcon() {
        return getTransportMean().getStationCirleDrawableId();
    }

    @Override
    public int getInstructionWhiteIcon() {
        return 0;
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        Coordinate coordinate = sections.get(0).getOrigin().getCoordinate();
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);
        for (int i=0;i<sections.size();i++)
        {
            coordinates.add(sections.get(i).getDestination().getCoordinate());
        }
        return coordinates;
    }

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
            distance+=section.getDistance();
        }
        return distance;
    }

    @Override
    public long getDuration() {
        int duration = 0;
        for (PathSection section : sections)
        {
            duration+=section.getDuration();
        }
        return duration;
    }


    public void setTransportMeanId(int transportMeanId) {
        this.transportMeanId = transportMeanId;
    }
}
