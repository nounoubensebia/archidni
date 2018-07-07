package com.archidni.archidni.Model.Transport;

import java.util.ArrayList;

public class StationLines {
    private Station station;
    private ArrayList<Line> lines;

    public StationLines(Station station, ArrayList<Line> lines) {
        this.station = station;
        this.lines = lines;
    }

    public Station getStation() {
        return station;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
