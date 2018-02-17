package com.archidni.archidni.Model;

import com.archidni.archidni.Model.Transport.Line;

import java.util.ArrayList;

/**
 * Created by nouno on 17/02/2018.
 */

public class Favorites {
    private ArrayList <Line> lines;

    public Favorites(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
