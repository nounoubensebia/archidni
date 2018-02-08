package com.archidni.archidni.Model;

/**
 * Created by noure on 08/02/2018.
 */

public class BoundingBox {
    private Coordinate northEast;
    private Coordinate southWest;

    public BoundingBox(Coordinate northEast, Coordinate southWest) {
        this.northEast = northEast;
        this.southWest = southWest;
    }

    public Coordinate getNorthEast() {
        return northEast;
    }

    public Coordinate getSouthWest() {
        return southWest;
    }
}
