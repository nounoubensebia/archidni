package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Place;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSearcher {
    private Place origin;
    private Place destination;
    private long departureTimeInSeconds;
    private long departureDateInSeconds;

    public PathSearcher(Place origin, Place destination, long departureTimeInSeconds,
                        long departureDateInSeconds) {
        this.origin = origin;
        this.destination = destination;
        this.departureTimeInSeconds = departureTimeInSeconds;
        this.departureDateInSeconds = departureDateInSeconds;
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

    public long getDepartureTimeInSeconds() {
        return departureTimeInSeconds;
    }

    public long getDepartureDateInSeconds() {
        return departureDateInSeconds;
    }
}
