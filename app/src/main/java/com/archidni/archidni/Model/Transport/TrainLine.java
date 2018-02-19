package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class TrainLine extends Line {
    private ArrayList<TrainTrip> trainTrips;

    public TrainLine(int id, String name, TransportMean transportMean, ArrayList<LineSection> lineSections, ArrayList<TrainTrip> trainTrips) {
        super(id, name, transportMean, lineSections);
        this.trainTrips = trainTrips;
    }

    public ArrayList<TrainTrip> getTrainTrips() {
        return trainTrips;
    }

    public ArrayList<Pair<TrainTrip,ArrayList<Long>>> getStationNextDepartures (Station station,
                                                                              long departureTime,
                                                                              long departureDate)
    {
        ArrayList<Pair<TrainTrip,ArrayList<Long>>> pairs = new ArrayList<>();
        for (TrainTrip trainTrip:trainTrips)
        {
            pairs.add(new Pair<TrainTrip,ArrayList<Long>>(trainTrip,
                    trainTrip.getNextDepartureRemainingTime(station,departureTime,departureDate)));
        }
        return pairs;
    }
}
