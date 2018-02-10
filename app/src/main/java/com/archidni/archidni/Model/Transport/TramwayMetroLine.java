package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by noure on 10/02/2018.
 */

public class TramwayMetroLine extends Line {
    private ArrayList<TramwayMetroTrip> tramwayMetroTrips;

    public TramwayMetroLine(int id, String name, TransportMean transportMean,
                            ArrayList<Section> sections, ArrayList<TramwayMetroTrip> tramwayMetroTrips) {
        super(id, name, transportMean, sections);
        this.tramwayMetroTrips = tramwayMetroTrips;
    }
}
