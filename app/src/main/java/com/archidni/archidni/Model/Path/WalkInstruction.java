package com.archidni.archidni.Model.Path;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class WalkInstruction extends MoveInstruction implements Serializable {
    private String polyline;
    private float distance;
    private String destination;
    private long duration;
    private int type;

    public static final int TYPE_DEPARTURE = 0;
    public static final int TYPE_TRANSFER = 1;
    public static final int TYPE_ARRIVAL = 2;

    public WalkInstruction(String polyline, float distance, String destination, int duration, int type) {
        this.polyline = polyline;
        this.distance = distance;
        this.destination = destination;
        this.duration = duration;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getMainText() {
        return "Marcher pour atteindre "+destination;
    }


    public long getInstructionIcon() {
        return R.drawable.ic_walk_green_24dp;
    }

    public int getInstructionWhiteIcon() {
        return R.drawable.ic_walk_white_24dp;
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        return GeoUtils.getPolylineFromGoogleMapsString(polyline);
    }

    @Override
    public String getTtile() {
        switch (type)
        {
            case TYPE_DEPARTURE : return "Départ";
            case TYPE_TRANSFER : return "Correspendance";
            case TYPE_ARRIVAL : return "Arrivée";
        }
        return null;
    }

    /*@Override
    public long getDuration() {
        return GeoUtils.getOnFootDuration((double)distance);
    }*/

    @Override
    public long getDuration() {
        return duration;
    }

    public String getDistanceString ()
    {
        return (int)(getDistance())+" mètres";
    }
    @Override
    public float getDistance() {
        return distance;
    }
}
