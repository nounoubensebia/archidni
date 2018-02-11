package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class MoveInstruction extends PathInstruction {

    public MoveInstruction(int duration) {
        super(duration);
    }
    public abstract float getDistance ();
    public abstract ArrayList<Coordinate> getPolyline ();
}
