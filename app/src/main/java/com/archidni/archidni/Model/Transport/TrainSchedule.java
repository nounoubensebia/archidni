package com.archidni.archidni.Model.Transport;

import java.util.ArrayList;
import java.util.List;

public class TrainSchedule extends Schedule {

    //trip departure time in seconds since midnight
    private List<Long> departureTimes;

    private ArrayList<StationTime> stationTimes;

    public TrainSchedule(int days, List<Long> departureTimes, ArrayList<StationTime> stationTimes) {
        super(days);
        this.departureTimes = departureTimes;
        this.stationTimes = stationTimes;
    }

    public List<Long> getDepartureTimes() {
        return departureTimes;
    }

    public ArrayList<StationTime> getStationTimes() {
        return stationTimes;
    }
}
