package com.archidni.archidni.Ui.Main;

import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

public class OptimisedMainDataRepository {
    private ArrayList<Line> lines;
    private ArrayList<Place> places;

    public OptimisedMainDataRepository(ArrayList<Line> lines, ArrayList<Place> places) {
        this.lines = lines;
        this.places = places;
    }
}
