package com.archidni.archidni.Model.Transport;

import android.util.Pair;

import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by noure on 07/02/2018.
 */

public class TransportUtils {
    public static long NO_DEPARTURE = -1;

    public static ArrayList<StationLines> getStationLines (ArrayList<Line> lines)
    {
        ArrayList<StationLines> stationLinesArrayList = new ArrayList<>();
        /*for (Station station:stations)
        {
            ArrayList<Line> linesPassingByStation = new ArrayList<>();
            for (Line line:lines)
            {
                for (Station station1:line.getStations())
                {
                    if (station1.getId()==station.getId())
                    {
                        linesPassingByStation.add(line);
                        break;
                    }
                }
            }
            stationLinesArrayList.add(new StationLines(station,linesPassingByStation));
        }*/
        for (Line line:lines)
        {
            for (Station station:line.getStations())
            {
                StationLines stationLines = getStationFromList(station.getId(),stationLinesArrayList);
                if (stationLines!=null)
                {
                    if (!containsLine(line.getId(),stationLines.getLines()))
                        stationLines.getLines().add(line);
                }
                else
                {
                    ArrayList<Line> lines1 = new ArrayList<>();
                    lines1.add(line);
                    stationLinesArrayList.add(new StationLines(station,lines1));
                }
            }
        }
        return stationLinesArrayList;
    }



    public static ArrayList<Station> getStationsFromLines (ArrayList<Line> lines)
    {
        ArrayList<Station> stations = new ArrayList<>();
        for (Line line:lines)
        {
            stations.addAll(line.getStations());
        }
        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station station, Station t1) {
                return station.getId()-t1.getId();
            }
        });
        if (stations.size()>0)
        {
            ArrayList<Station> stationsWithoutDuplicates = new ArrayList<>();
            int i = 0;
            while (i<stations.size())
            {
                Station station = stations.get(i);
                stationsWithoutDuplicates.add(station);
                i++;
                while (i<stations.size()&&stations.get(i).equals(station))
                {
                    i++;
                }
            }
            return stationsWithoutDuplicates;
        }
        return stations;
    }

    public static ArrayList<Station> getNearbyStations (Coordinate coordinate,ArrayList<Station> stations,float maxDistance)
    {
        ArrayList<Station> closeStations = new ArrayList<>();
        for (Station station:stations)
        {
            if (GeoUtils.distance(station.getCoordinate(),coordinate)<maxDistance)
            {
                closeStations.add(station);
            }
        }
        return closeStations;
    }

    public static ArrayList<Coordinate> getCoordinatesFromStations (ArrayList<Station> stations)
    {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (Station station : stations)
        {
            coordinates.add(station.getCoordinate());
        }
        return coordinates;
    }

    public static ArrayList<Station> getStationsFromSections (ArrayList<? extends Section> lineSections)
    {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(lineSections.get(0).getOrigin());
        for (Section lineSection : lineSections)
        {
            stations.add(lineSection.getDestination());
        }
        return stations;
    }


    private static void addStationToListOptimal (Station stationToAdd,ArrayList<Station> stations)
    {
        stations.add(stationToAdd);

        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station station, Station t1) {
                return station.getId()-t1.getId();
            }
        });
    }

    private static StationLines getStationFromList(int stationId, ArrayList<StationLines> stations)
    {
        int i=0;
        int f=stations.size()-1;
        int m = (f+i)/2;
        int cmp;
        while (i<=f)
        {
            StationLines station = stations.get(m);
            cmp = stationId - station.getStation().getId();
            if (cmp<0)
            {
                f=m-1;
            }
            if (cmp>0)
            {
                i=m+1;
            }
            if (cmp==0)
            {
                return station;
            }
            else
            {
                m=(f+i)/2;
            }
        }
        return null;
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
            for (int j = i+1; j < pairs.size(); j++) {
                Pair<Pair<Line,TrainTrip>,Long> pair2 = pairs.get(j);
                if (pair1.second>pair2.second)
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

    public static ArrayList<Pair<Line,TimePeriod>> getDayTrips (long departureDate,
                                                                      ArrayList<Line> lines)
    {
        ArrayList<Pair<Line,TimePeriod>> arrayList = new ArrayList<>();
        for (Line line:lines)
        {
            if (line instanceof TramwayMetroLine)
            {
                TramwayMetroLine tramwayMetroLine = (TramwayMetroLine) line;
                for (TramwayMetroTrip tramwayMetroTrip:tramwayMetroLine.getTramwayMetroTrips())
                {
                    for (TimePeriod timePeriod:tramwayMetroTrip.getTimePeriods())
                    {
                        arrayList.add(new Pair<Line, TimePeriod>(line,timePeriod));
                    }
                }
            }
        }
        sortDayTrips(arrayList);
        return arrayList;
    }

    private static void sortDayTrips (ArrayList <Pair<Line,TimePeriod>> dayTrips)
    {
        for (int i = 0; i < dayTrips.size(); i++) {
            Pair<Line,TimePeriod> pair1 = dayTrips.get(i);
            for (int j = i+1; j < dayTrips.size(); j++) {
                Pair<Line,TimePeriod> pair2 = dayTrips.get(j);
                if (pair1.second.getStart()>pair2.second.getStart())
                {
                    dayTrips.set(i,pair2);
                    dayTrips.set(j,pair1);
                    pair1 = pair2;
                }
            }
        }
    }
    
}
