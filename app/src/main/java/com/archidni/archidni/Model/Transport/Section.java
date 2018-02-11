package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;

/**
 * Created by noure on 07/02/2018.
 */

public class Section {
    private Station origin;
    private Station destination;

    public Section(Station origin, Station destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }

    public int getDistance ()
    {
        return GeoUtils.distance(origin.getCoordinate(),destination.getCoordinate());
    }
}
