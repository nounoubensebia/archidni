package com.archidni.archidni.Model.Reports;

import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.TransportMean;

public class DisruptionSubject {
    private Line line;
    private TransportMean transportMean;

    public DisruptionSubject(Line lineSkeleton) {
        this.line = lineSkeleton;
    }

    public DisruptionSubject(TransportMean transportMean) {
        this.transportMean = transportMean;
    }

    public Line getLine() {
        return line;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }
}
