package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.TransportMean;

import java.io.Serializable;

public class LineSkeleton implements Serializable {
    private int id;
    private String name;
    private TransportMean transportMean;

    public LineSkeleton(int id, String name, TransportMean transportMean) {
        this.id = id;
        this.name = name;
        this.transportMean = transportMean;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }
}
