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
    public WalkInstruction(int duration, float distance, String polyline,String destination) {
        super(duration);
        this.distance = distance;
        this.polyline = polyline;
        this.destination = destination;
    }

    @Override
    public String getMainText() {
        return "Marcher pour atteindre "+destination;
    }

    @Override
    public String getSecondaryText() {
        return ((int)getDuration()/60+" minutes, "+(int)(getDistance())+" mètres");
    }

    @Override
    public long getInstructionIcon() {
        return R.drawable.ic_walk_green_24dp;
    }

    @Override
    public int getInstructionWhiteIcon() {
        return R.drawable.ic_walk_white_24dp;
    }

    @Override
    public ArrayList<Coordinate> getPolyline() {
        return GeoUtils.getPolylineFromGoogleMapsString(polyline);
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
