package com.archidni.archidni.Model.Path;

import com.archidni.archidni.Model.TransportMean;

import java.io.Serializable;
import java.util.ArrayList;

public class PathPreferences implements Serializable {
    private int sortPreference;
    private ArrayList<TransportMean> transportModesNotUsed;


    public static final int SORT_BY_MINIMUM_TIME = 0;
    public static final int SORT_BY_MINIMUM_WALKING_TIME = 1;
    public static final int SORT_BY_MINIMUM_TRANSFERS = 2;

    public static final PathPreferences DEFAULT =
            new PathPreferences(SORT_BY_MINIMUM_TIME,new ArrayList<TransportMean>());

    public PathPreferences(int sortPreference, ArrayList<TransportMean> transportModesNotUsed) {
        this.sortPreference = sortPreference;
        this.transportModesNotUsed = transportModesNotUsed;
    }

    public int getSortPreference() {
        return sortPreference;
    }

    public ArrayList<TransportMean> getTransportModesNotUsed() {
        return transportModesNotUsed;
    }


}
