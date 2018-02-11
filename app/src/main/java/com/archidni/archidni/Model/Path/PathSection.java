package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;

/**
 * Created by noure on 11/02/2018.
 */

public class PathSection extends Section {
    private int duration;

    public PathSection(Station origin, Station destination, int duration) {
        super(origin, destination);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
