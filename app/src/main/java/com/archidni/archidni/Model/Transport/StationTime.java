package com.archidni.archidni.Model.Transport;

public class StationTime {

    private Station station;
    private float timeAtStation;

    public StationTime(Station station, int timeAtStation) {
        this.station = station;
        this.timeAtStation = timeAtStation;
    }

    public Station getStation() {
        return station;
    }

    public float getTimeAtStation() {
        return timeAtStation;
    }
}
