package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;

/**
 * Created by noure on 07/02/2018.
 */

public class Station extends Place {
    private String name;
    private TransportMean transportMean;
    private int id;

    public Station(int id,String name,TransportMean transportMean,Coordinate coordinate) {
        super(App.getAppContext().getString(R.string.station) +" "+
                name,App.getAppContext().getString(R.string.station_of)+" "+
                transportMean.getName(), coordinate);
        this.name = name;
        this.transportMean = transportMean;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
