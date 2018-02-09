package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class TrainTrip extends Trip {
    private ArrayList<Integer> departures;

    public TrainTrip(ArrayList<Pair<Integer, Integer>> stationTimes, ArrayList<Integer> departures) {
        super(stationTimes);
        this.departures = departures;
    }

    public ArrayList<Integer> getDepartures() {
        return departures;
    }
}
