package com.archidni.archidni;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.model.LatLng;
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
}
