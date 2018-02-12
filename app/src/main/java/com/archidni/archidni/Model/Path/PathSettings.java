package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Place;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

import java.io.Serializable;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSettings implements Serializable {
    private Place origin;
    private Place destination;
    private long departureTime;
    private long departureDate;

    public PathSettings(Place origin, Place destination, long departureTime,
                        long departureDate) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
    }

    public void setOrigin(Place origin) {
        this.origin = origin;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    public Place getOrigin() {
        return origin;
    }

    public Place getDestination() {
        return destination;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public TransportMeansSelector getTransportMeansSelector() {
        return null;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public void setDepartureDate(long departureDate) {
        this.departureDate = departureDate;
    }
}
