package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class Line {
    private int id;
    private TransportMean transportMean;
    private ArrayList<Section> sections;

    public Line(int id, TransportMean transportMean, ArrayList<Section> sections) {
        this.id = id;
        this.transportMean = transportMean;
        this.sections = sections;
    }

    public ArrayList<Station> getStations ()
    {
        Station first = sections.get(0).getOrigin();
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(first);
        for (Section section:sections)
        {
            stations.add(section.getDestination());
        }
        return stations;
    }
}
