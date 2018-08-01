package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Transport.LineSkeleton;

import java.io.Serializable;

public class RideLine implements Serializable {
    private LineSkeleton pathLine;
    private String destination;

    public RideLine(LineSkeleton pathLine, String destination) {
        this.pathLine = pathLine;
        this.destination = destination;
    }

    public LineSkeleton getPathLine() {
        return pathLine;
    }

    public String getDestination() {
        return destination;
    }
}
