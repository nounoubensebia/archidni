package com.archidni.archidni.UiUtils;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.Interests.ParkingType;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;


import java.util.ArrayList;

public abstract class SelectorItem {

    private int id;
    private String name;

    public static final int PARKING_ID = 5;

    public static ArrayList<SelectorItem> allItems =
            new ArrayList<SelectorItem>(){{
                addAll(TransportMean.allTransportMeans);
                add(new ParkingType(5,"Parkings"));
                /*add(new TransportMean(0, App.getAppContext().getString(R.string.metro)));
                add(new TransportMean(1, App.getAppContext().getString(R.string.train)));
                add(new TransportMean(2, App.getAppContext().getString(R.string.bus)));
                add(new TransportMean(3, App.getAppContext().getString(R.string.tramway)));
                add(new TransportMean(4,"Téléphérique"));*/
            }};

    public SelectorItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract int getIconDisabled ();
    public abstract int getMarkerIcon();
    public abstract int getIconEnabled();
    public abstract int getColor();
    public abstract int getTheme();
    public abstract int getCoordinateDrawable();


}
