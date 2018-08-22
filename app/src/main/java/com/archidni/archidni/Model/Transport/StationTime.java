package com.archidni.archidni.Model.Transport;

public class StationTime {

    private Station station;
    private int timeAtStation;

    public StationTime(Station station, int timeAtStation) {
        this.station = station;
        this.timeAtStation = timeAtStation;
    }

    public Station getStation() {
        return station;
    }

    public int getTimeAtStation() {
        return timeAtStation;
    }
}
