package com.archidni.archidni.Model.Transport;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class TransportUtils {
    public static ArrayList<Station> getStationsFromLines (ArrayList<Line> lines)
    {
        ArrayList<Station> stations = new ArrayList<>();
        for (Line line:lines)
        {
            for (Station station:line.getStations())
            {
                if (!containsStation(station.getId(),stations))
                {
                    stations.add(station);
                }
            }
        }
        return stations;
    }

    private static boolean containsStation (int stationId,ArrayList<Station> stations)
    {
        for (Station station:stations)
        {
            if (stationId==station.getId())
            {
                return true;
            }
        }
        return false;
    }
}
