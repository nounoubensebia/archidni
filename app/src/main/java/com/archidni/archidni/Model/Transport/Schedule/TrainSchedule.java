package com.archidni.archidni.Model.Transport.Schedule;

import com.archidni.archidni.Model.Transport.Schedule.Schedule;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.StationTime;
import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;
import java.util.List;

public class TrainSchedule extends Schedule {

    //trip departure time in seconds since midnight
    private long departureTime;

    private ArrayList<StationTime> stationTimes;

    public TrainSchedule(int days, long departureTime, ArrayList<StationTime> stationTimes) {
        super(days);
        this.departureTime = departureTime;
        this.stationTimes = stationTimes;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public ArrayList<StationTime> getStationTimes() {
        return stationTimes;
    }

    public Station getDestination ()
    {
        return stationTimes.get(stationTimes.size()-1).getStation();
    }

    public Station getOrigin ()
    {
        return stationTimes.get(0).getStation();
    }

    public TransportMean getTransportMean()
    {
        return stationTimes.get(0).getStation().getTransportMean();
    }
}
