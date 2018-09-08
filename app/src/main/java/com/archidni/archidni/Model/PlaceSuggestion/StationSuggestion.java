package com.archidni.archidni.Model.PlaceSuggestion;

import com.archidni.archidni.Model.TransportMean;
import com.google.gson.Gson;

public class StationSuggestion extends PlaceSuggestion {


    private int id;
    private TransportMean transportMean;

    public StationSuggestion(String mainText,int id,TransportMean transportMean) {
        super(mainText, "Station de "+transportMean.getName(), 0);
        this.transportMean = transportMean;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    @Override
    public int getDrawable() {
        return transportMean.getIconEnabled();
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
