package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class TrainLine extends Line {
    ArrayList<TrainTrip> trips;

    public TrainLine(int id, String name, TransportMean transportMean, ArrayList<Section> sections, ArrayList<TrainTrip> trips) {
        super(id, name, transportMean, sections);
        this.trips = trips;
    }

    public ArrayList<TrainTrip> getTrips() {
        return trips;
    }
}
