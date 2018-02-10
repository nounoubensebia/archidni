package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class TrainLine extends Line {
    private ArrayList<TrainTrip> trainTrips;

    public TrainLine(int id, String name, TransportMean transportMean, ArrayList<Section> sections, ArrayList<TrainTrip> trainTrips) {
        super(id, name, transportMean, sections);
        this.trainTrips = trainTrips;
    }

    public ArrayList<TrainTrip> getTrainTrips() {
        return trainTrips;
    }
}
