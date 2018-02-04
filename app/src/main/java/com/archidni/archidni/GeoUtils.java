package com.archidni.archidni;

import com.archidni.archidni.Model.Coordinate;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by noure on 04/02/2018.
 */

public class GeoUtils {

    private static float HUMAN_SPEED = 3.6f;
    public static int distance (Coordinate coordinate1, Coordinate coordinate2)
    {
        LatLng latLng1 = coordinate1.toMapBoxLatLng();
        LatLng latLng2 = coordinate2.toMapBoxLatLng();
        return (int)latLng1.distanceTo(latLng2);
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
