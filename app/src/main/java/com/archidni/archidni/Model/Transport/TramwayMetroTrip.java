package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by noure on 10/02/2018.
 */

public class TramwayMetroTrip extends Trip {
    private ArrayList<TimePeriod> timePeriods;

    public TramwayMetroTrip(ArrayList<Pair<Integer, Integer>> stationTimes,
                            ArrayList<TimePeriod> timePeriods) {
        super(stationTimes);
        this.timePeriods = timePeriods;
    }
}
