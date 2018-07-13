package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;

import java.io.Serializable;

/**
 * Created by nouno on 19/02/2018.
 */

public class Section implements Serializable {
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
        return GeoUtils.distance(getOrigin().getCoordinate(),getDestination().getCoordinate());
    }


}
