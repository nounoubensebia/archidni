package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class Trip {
    private ArrayList<Pair<Integer,Integer>> stationTimes;

    public Trip(ArrayList<Pair<Integer, Integer>> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public ArrayList<Pair<Integer, Integer>> getStationTimes() {
        return stationTimes;
    }
}
