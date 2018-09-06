package com.archidni.archidni.Data.Paths;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;

import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * Created by noure on 12/02/2018.
 */

public class PathOnlineDataStore extends OnlineDataStore implements PathDataStore {

    private static final String URL_GET_PATH = "/api/findPath";



    public void getPaths (Context context, final PathSettings pathSettings, final OnSearchCompleted onSearchCompleted) {
        final String url;
        final PathPlace departure = pathSettings.getOrigin();
        final PathPlace arrival = pathSettings.getDestination();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("origin",departure.getCoordinate().getLatitude()+","+departure.getCoordinate().getLongitude());
        map.put("destination",arrival.getCoordinate().getLatitude()+","+arrival.getCoordinate().getLongitude());
        map.put("date", TimeUtils.getDateString(pathSettings.getDepartureArrivalTime()));
        map.put("time",TimeUtils.getTimeString(pathSettings.getDepartureArrivalTime()));
        map.put("arriveBy", Boolean.toString(pathSettings.isArriveBy()));
        cancelRequests(context);
        url = SharedPrefsUtils.getServerUrl(context)+AppSingleton.buildGetUrl(URL_GET_PATH,map);
        final OauthStringRequest stringRequest = new OauthStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    PathParser pathParser = new PathParser(pathSettings);
                    onSearchCompleted.onResultsFound(pathParser.parsePaths(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR PATH URL",url);
                    onSearchCompleted.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchCompleted.onError();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        stringRequest.performRequest(getTag());
    }

    @Override
    public String getTag() {
        return "PATH";
    }


}
