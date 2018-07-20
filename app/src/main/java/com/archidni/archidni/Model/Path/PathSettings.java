package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

import java.io.Serializable;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSettings implements Serializable {
    private PathPlace origin;
    private PathPlace destination;
    private long departureTime;
    private long departureDate;
    private PathPreferences pathPreferences;

    public PathSettings(PathPlace origin, PathPlace destination, long departureTime, long departureDate, PathPreferences pathPreferences) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.pathPreferences = pathPreferences;
    }

    public void setOrigin(PathPlace origin) {
        this.origin = origin;
    }

    public void setDestination(PathPlace destination) {
        this.destination = destination;
    }

    public PathPlace getOrigin() {
        return origin;
    }

    public PathPlace getDestination() {
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

    public PathPreferences getPathPreferences() {
        return pathPreferences;
    }

    public void setPathPreferences(PathPreferences pathPreferences) {
        this.pathPreferences = pathPreferences;
    }
}
