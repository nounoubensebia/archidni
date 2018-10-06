package com.archidni.archidni.Model.RealtimeBus;

import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RealTimeBusFilter {


    private List<Bus> buses;
    //timeOut in seconds
    private long timeOut;

    public RealTimeBusFilter(List<Bus> buses, long timeOut) {
        this.buses = buses;
        this.timeOut = timeOut;
    }

    public List<Bus> getFilteredBuses ()
    {
        ArrayList<Bus> filteredBuses = new ArrayList<>();
        for (Bus bus:buses)
        {
            if (TimeUtils.getCurrentTimeInSeconds() - bus.getTimeStamp()<timeOut)
            {
                filteredBuses.add(bus);
            }
        }
        return filteredBuses;
    }
}
