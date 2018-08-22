package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.UiUtils.TransportMeansSelector;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by noure on 03/02/2018.
 */

public class PathSettings implements Serializable {
    private PathPlace origin;
    private PathPlace destination;
    private Calendar departureArrivalTime;
    private boolean arriveBy;
    private PathPreferences pathPreferences;

    public PathSettings(PathPlace origin, PathPlace destination, Calendar departureArrivalTime, boolean arriveBy, PathPreferences pathPreferences) {
        this.origin = origin;
        this.destination = destination;
        this.departureArrivalTime = departureArrivalTime;
        this.arriveBy = arriveBy;
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

    public Calendar getDepartureArrivalTime() {
        return departureArrivalTime;
    }


    public TransportMeansSelector getTransportMeansSelector() {
        return null;
    }

    public void setDepartureArrivalTime(Calendar departureArrivalTime) {
        this.departureArrivalTime = departureArrivalTime;
    }


    public PathPreferences getPathPreferences() {
        return pathPreferences;
    }

    public void setPathPreferences(PathPreferences pathPreferences) {
        this.pathPreferences = pathPreferences;
    }

    public boolean isArriveBy() {
        return arriveBy;
    }

    public void setArriveBy(boolean arriveBy) {
        this.arriveBy = arriveBy;
    }
}
