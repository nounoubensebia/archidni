package com.archidni.archidni.Model;

/**
 * Created by nouno on 15/02/2018.
 */

public class LineStationSuggestion {
    private String name;
    private int id;
    private int transportMeanId;

    public LineStationSuggestion(String name, int id, int transportMeanId) {
        this.name = name;
        this.id = id;
        this.transportMeanId = transportMeanId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public TransportMean getTransportMean() {
        return TransportMean.allTransportMeans.get(transportMeanId);
    }
}
