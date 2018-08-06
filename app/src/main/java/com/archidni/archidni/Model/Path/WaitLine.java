package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;

import java.io.Serializable;

public class WaitLine implements Serializable {
    private LineSkeleton line;
    private String destination;
    private long time;
    private boolean isExactWaitingTime;
    private boolean hasPerturbations;

    public WaitLine(LineSkeleton line, String destination, long time, boolean isExactWaitingTime, boolean hasPerturbations) {
        this.line = line;
        this.destination = destination;
        this.time = time;
        this.isExactWaitingTime = isExactWaitingTime;
        this.hasPerturbations = hasPerturbations;
    }

    public boolean hasPerturbations() {
        return hasPerturbations;
    }

    public LineSkeleton getLine() {
        return line;
    }

    public String getDestination() {
        return destination;
    }

    public long getTime() {
        return time;
    }

    public boolean isExactWaitingTime() {
        return isExactWaitingTime;
    }
}
