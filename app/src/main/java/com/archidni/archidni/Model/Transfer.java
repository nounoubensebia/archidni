package com.archidni.archidni.Model;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;

import java.util.ArrayList;

public class Transfer {
    private Station station;
    private ArrayList <Line> lines;

    public Transfer(Station station, ArrayList<Line> lines) {
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
