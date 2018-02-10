package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class TransportUtils {
    public static long NO_DEPARTURE = -1;

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

    public static ArrayList<Station> filterStations (ArrayList<Station> stations,
                                                     BoundingBox boundingBox)
    {
        ArrayList<Station> stations1 = new ArrayList<>();
        for (Station station:stations)
        {
            if (station.isInsideBoundingBox(boundingBox))
            {
                stations1.add(station);
            }
        }
        return stations1;
    }

    public static ArrayList<Line> filterLines (ArrayList<Line> lines,
                                                     BoundingBox boundingBox)
    {
        ArrayList<Line> lines1 = new ArrayList<>();
        for (Line line:lines)
        {
            if (line.hasStationInsideBoundingBox(boundingBox))
            {
                lines1.add(line);
            }
        }
        return lines1;
    }

    public static ArrayList<Pair<Pair<Line,TrainTrip>,Long>> getNextDeparturesFromStation (ArrayList<Line> lines,
                                                                                 Station station,
                                                                                 long departureTime,
                                                                                 long departureDate,int maxToGet)
    {
        ArrayList<Pair<Line,ArrayList<Pair<TrainTrip,ArrayList<Long>>>>> pairs = new ArrayList<>();
        for (Line line:lines)
        {
            if (line instanceof  TrainLine)
            {
            TrainLine trainLine = (TrainLine)line;
            pairs.add(new Pair<Line, ArrayList<Pair<TrainTrip, ArrayList<Long>>>>(trainLine,
                    trainLine.getStationNextDepartures(station,departureTime,departureDate)));
            }
        }

        ArrayList<Pair<Pair<Line,TrainTrip>,Long>> finalPairs = new ArrayList<>();
        for (Pair<Line,ArrayList<Pair<TrainTrip,ArrayList<Long>>>> pair:pairs)
        {
           for (Pair<TrainTrip,ArrayList<Long>> tripTimePair:pair.second)
           {
               for (Long nextDeparture:tripTimePair.second)
               {
                   finalPairs.add(
                           new Pair<>(new
                                   Pair<>(pair.first,tripTimePair.first),nextDeparture));
               }
           }
        }
        sortPairs(finalPairs,maxToGet);
        return finalPairs;
    }


    private static void sortPairs(ArrayList<Pair<Pair<Line, TrainTrip>, Long>> pairs,int maxToGet)
    {
        for (int i = 0; i < pairs.size(); i++) {
            Pair<Pair<Line,TrainTrip>,Long> pair1 = pairs.get(i);
            for (int j = 0; j < pairs.size(); j++) {
                Pair<Pair<Line,TrainTrip>,Long> pair2 = pairs.get(j);
                if (pair1.second<pair2.second)
                {
                    pairs.set(i,pair2);
                    pairs.set(j,pair1);
                    pair1 = pair2;
                }
            }
        }
        int k = pairs.size();
        if ( k > maxToGet )
            pairs.subList(maxToGet, k).clear();
    }

    public static Station getStationById (ArrayList<Station> stations,long id)
    {
        for (Station station:stations)
        {
            if (station.getId()==id)
            {
                return station;
            }
        }
        return null;
    }

}
