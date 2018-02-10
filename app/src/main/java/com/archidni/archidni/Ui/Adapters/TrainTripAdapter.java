package com.archidni.archidni.Ui.Adapters;

import android.content.Context;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TrainLine;
import com.archidni.archidni.Model.Transport.TrainTrip;

import java.util.ArrayList;

/**
 * Created by noure on 10/02/2018.
 */

public class TrainTripAdapter {
    private Context context;
    private long departureTime;
    private long departureDate;
    private Station station;
    private ArrayList<TrainLine> trainLines;

    public TrainTripAdapter(Context context, long departureTime, long departureDate, Station station,
                            ArrayList<TrainLine> trainLines) {
        this.context = context;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.station = station;
        this.trainLines = trainLines;
    }
}
