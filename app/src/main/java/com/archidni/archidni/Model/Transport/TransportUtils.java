package com.archidni.archidni.Model.Transport;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;

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

    public static boolean containsLine (int lineId,ArrayList<Line> lines)
    {
        for (Line line:lines)
        {
            if (lineId==line.getId())
            {
                return true;
            }
        }
        return false;
    }

    public static void sortStationsByDistance (ArrayList<Station> stations, Coordinate coordinate)
    {
        for (int i = 0; i < stations.size(); i++) {
            Station station1 = stations.get(i);
            for (int j = i; j < stations.size(); j++) {
                Station station2 = stations.get(j);
                if (GeoUtils.distance(coordinate,station2.getCoordinate()) <
                        GeoUtils.distance(coordinate,station1.getCoordinate()))
                {
                    Station station = station1;
                    stations.set(i,station2);
                    stations.set(j,station);
                    station1 = station2;
                }
            }
        }
    }
}
