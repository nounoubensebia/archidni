package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class MoveInstruction extends PathInstruction {

    protected int distanceInMeters;
    protected ArrayList<Coordinate> polyline;

    public MoveInstruction(long durationInSeconds, int order, int distanceInMeters,
                           ArrayList<Coordinate> polyline) {
        super(durationInSeconds, order);
        this.distanceInMeters = distanceInMeters;
        this.polyline = polyline;
    }
}
