package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.Model.Transport.Schedule.MetroSchedule;
import com.archidni.archidni.Model.Transport.Schedule.Schedule;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.TimeUtils;

import java.util.ArrayList;

/**
 * Created by noure on 10/02/2018.
 */

public class TramwayMetroLine extends Line {
    private ArrayList<MetroSchedule> schedules;

    public TramwayMetroLine(int id, String name, TransportMean transportMean, ArrayList<LineSection> lineSections, ArrayList<MetroSchedule> schedules) {
        super(id, name, transportMean, lineSections);
        this.schedules = schedules;
    }

    public ArrayList<MetroSchedule> getSchedules() {
        return schedules;
    }

    public ArrayList<MetroSchedule> getDayDepartures (long departureDate)
    {
        ArrayList<MetroSchedule> tramwayMetroTrips1 = new ArrayList<>();
        for (MetroSchedule tramwayMetroTrip:schedules)
        {
            if (tramwayMetroTrip.getDays()% TimeUtils.getDayFromTimeStamp(departureDate)==0)
            {
                tramwayMetroTrips1.add(tramwayMetroTrip);
            }
        }
        return tramwayMetroTrips1;
    }
}
