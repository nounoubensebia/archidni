package com.archidni.archidni.Model.Transport;



import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by nouno on 09/02/2018.
 */

public class TrainTrip extends Trip {
    private ArrayList<Long> departures;

    public TrainTrip(long days, ArrayList<Pair<Integer, Integer>> stationTimes, ArrayList<Long> departures) {
        super(days, stationTimes);
        this.departures = departures;
    }

    public ArrayList<Long> getDepartures() {
        return departures;
    }

    public ArrayList<Long> getNextDepartureRemainingTime (Station station, long departureTime, long departureDate)
    {
        long day = com.archidni.archidni.TimeUtils.getDayFromTimeStamp(departureDate);
        if (false)
        {
            return new ArrayList<>();
        }
        else
        {
            ArrayList<Long> nextDepartures = new ArrayList<>();
            for (Long departure : departures)
            {
                for (Pair<Integer,Integer> stationPair:getStationTimes())
                {
                    if (stationPair.first==station.getId()&&(stationPair.second+departure)>departureTime)
                    {
                        nextDepartures.add((long)stationPair.second*60+departure);
                    }
                }
            }
            return nextDepartures;
        }
    }
}
