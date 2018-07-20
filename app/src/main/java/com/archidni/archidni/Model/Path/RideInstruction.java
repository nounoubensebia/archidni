package com.archidni.archidni.Model.Path;

import com.archidni.archidni.GeoUtils;
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
    private String polyline;
    private float errorMargin;


    public RideInstruction(long duration, long transportMeanId, ArrayList<Section> sections,
                           String lineLabel, String terminus, String polyline, float errorMargin) {
        super(duration);
        this.transportMeanId = transportMeanId;
        this.sections = sections;
        this.lineLabel = lineLabel;
        this.terminus = terminus;
        this.polyline = polyline;
        this.errorMargin = errorMargin;
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
        return (s+" arrêts"+" (environ "+(getDuration()/60)+" minutes)");
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
    public long getDuration() {
        return (long) (super.getDuration() + super.duration*errorMargin);
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
        return GeoUtils.distance(getPolyline());
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        return GeoUtils.getPolylineFromGoogleMapsString(polyline);
    }

    @Override
    public String getDurationString() {
        if (transportMeanId!=TransportMean.ID_BUS&&transportMeanId!=4)
        return ("environ "+getDuration()/60) + " minutes";
        else
            return "";
    }

}
