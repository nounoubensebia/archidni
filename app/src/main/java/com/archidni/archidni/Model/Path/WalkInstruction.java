package com.archidni.archidni.Model.Path;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.R;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class WalkInstruction extends MoveInstruction {
    private ArrayList<Coordinate> polyline;
    private float distance;
    private String destination;
    public WalkInstruction(int duration, float distance, ArrayList<Coordinate> polyline,String destination) {
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
        return (getDurationInSeconds()/60+" minutes, "+(int)(getDistance()*1000)+" mètres");
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
        return polyline;
    }



    public String getDistanceString ()
    {
        return (int)(getDistance()*1000)+" mètres";
    }
    @Override
    public float getDistance() {
        return distance;
    }
}
