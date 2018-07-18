package com.archidni.archidni;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;


import java.util.ArrayList;

/**
 * Created by noure on 04/02/2018.
 */

public class GeoUtils {

    private static float HUMAN_SPEED = 3.6f;
    public static int distance (Coordinate coordinate1, Coordinate coordinate2)
    {
        LatLng latLng1 = coordinate1.toGoogleMapLatLng();
        LatLng latLng2 = coordinate2.toGoogleMapLatLng();
        return (int) SphericalUtil.computeDistanceBetween(latLng1,latLng2);
    }

    public static boolean polylineContainsOnlyEquals (ArrayList<Coordinate> polyline)
    {
        Coordinate coordinate = polyline.get(0);
        for (Coordinate coordinate1:polyline)
        {
            if (coordinate1.getLatitude()!=coordinate.getLatitude()||coordinate1.getLongitude()!=
                    coordinate.getLongitude())
            {
                return false;
            }
        }
        return true;
    }

    public static int distance (ArrayList<Coordinate> coordinates)
    {
        Coordinate prevCoordinate = coordinates.get(0);
        int distance = 0;
        for (int i = 1;i < coordinates.size();i++)
        {
            distance+= distance(prevCoordinate,coordinates.get(i));
            prevCoordinate = coordinates.get(i);
        }
        return distance;
    }

    public static long getOnFootDuration (Double distance)
    {

        return (long)(((distance/1000)/HUMAN_SPEED)*3600);
    }

    public static long getOnFootDuration (Coordinate coordinate1, Coordinate coordinate2)
    {
        double distance = distance (coordinate1,coordinate2);
        return (long)((((distance/1000)/HUMAN_SPEED)*3600));
    }

    public static Coordinate getPolylineCenter (ArrayList<Coordinate> polyline)
    {
        LatLng southEast = new LatLng(Math.min(polyline.get(0).getLatitude(),
                polyline.get(polyline.size()-1).getLatitude()),
                Math.min(polyline.get(0).getLongitude(),polyline.get(polyline.size()-1).getLongitude()));
        LatLng northWest = new LatLng(Math.max(polyline.get(0).getLatitude(),
                polyline.get(polyline.size()-1).getLatitude()),
                Math.max(polyline.get(0).getLongitude(),polyline.get(polyline.size()-1).getLongitude()));
        LatLngBounds latLngBounds = new LatLngBounds(southEast,northWest);
        for (Coordinate coordinate:polyline)
        {
            latLngBounds = latLngBounds.including(coordinate.toGoogleMapLatLng());
        }
        return new Coordinate(latLngBounds.getCenter().latitude,latLngBounds.getCenter().longitude);
    }

    private static Coordinate midPoint(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);


        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        return new Coordinate(lat3,lon3);
    }
}
