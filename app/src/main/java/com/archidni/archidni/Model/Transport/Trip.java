package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class Trip {
    private long days;
    private ArrayList<Pair<Integer,Integer>> stationTimes;


    public Trip(long days, ArrayList<Pair<Integer, Integer>> stationTimes) {
        this.days = days;
        this.stationTimes = stationTimes;
    }

    public ArrayList<Pair<Integer, Integer>> getStationTimes() {
        return stationTimes;
    }

    public long getDays() {
        return days;
    }


    public Station getDestination (Line line)
    {
        int max = stationTimes.get(0).second;
        int maxId = stationTimes.get(0).first;
        for (int i = 1; i < stationTimes.size(); i++) {
            if (max<stationTimes.get(i).second)
            {
                max = stationTimes.get(i).second;
                maxId = stationTimes.get(i).first;
            }
        }
        return TransportUtils.getStationById(line.getStations(),maxId);
    }

    public Station getOrigin (Line line)
    {
        int min = stationTimes.get(0).second;
        int minId = stationTimes.get(0).first;
        for (int i = 1; i < stationTimes.size(); i++) {
            if (min>stationTimes.get(i).second)
            {
                min = stationTimes.get(i).second;
                minId = stationTimes.get(i).first;
            }
        }
        return TransportUtils.getStationById(line.getStations(),minId);
    }
}
