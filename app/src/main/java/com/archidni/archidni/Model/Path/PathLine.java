package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.TransportMean;

public class PathLine extends LineSkeleton{
    private String shortName;

    public PathLine(int id, String name, TransportMean transportMean, String shortName) {
        super(id, name, transportMean);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
