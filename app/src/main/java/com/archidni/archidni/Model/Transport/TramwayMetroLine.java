package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.TimeUtils;

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

    public ArrayList<TramwayMetroTrip> getTramwayMetroTrips() {
        return tramwayMetroTrips;
    }

    public ArrayList<TramwayMetroTrip> getDayDepartures (long departureDate)
    {
        ArrayList<TramwayMetroTrip> tramwayMetroTrips1 = new ArrayList<>();
        for (TramwayMetroTrip tramwayMetroTrip:tramwayMetroTrips)
        {
            if (tramwayMetroTrip.getDays()% TimeUtils.getDayFromTimeStamp(departureDate)==0)
            {
                tramwayMetroTrips1.add(tramwayMetroTrip);
            }
        }
        return tramwayMetroTrips1;
    }
}
