package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public abstract class MoveInstruction extends PathInstruction {

    public MoveInstruction() {
    }

    public abstract float getDistance ();
    public abstract ArrayList<Coordinate> getPolyline ();
}
