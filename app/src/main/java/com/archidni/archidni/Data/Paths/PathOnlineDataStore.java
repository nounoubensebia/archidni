package com.archidni.archidni.Data.Paths;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;

import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.RideLine;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Places.PathPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSkeleton;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        map.put("time", StringUtils.getTimeString(pathSettings.getDepartureTime())+"");
        map.put("date", pathSettings.getDepartureDate()+"");
        ArrayList<TransportMean> bannedTransportMeans = new ArrayList<>();
        bannedTransportMeans.addAll(TransportMean.allTransportMeans);
        int s = 0;
        if (pathSettings.getTransportMeansSelector()!=null)
        {
            for (TransportMean transportMean : TransportMean.allTransportMeans)
            {
                for (TransportMean transportMean1 : pathSettings.getTransportMeansSelector().getSelectedTransportMeans())
                {
                    if (transportMean.getId()==transportMean1.getId())
                    {
                        bannedTransportMeans.remove(transportMean1.getId()-s);
                        s++;
                    }
                }
            }
        }
        cancelRequests(context);
        url = SharedPrefsUtils.getServerUrl(context)+AppSingleton.buildGetUrl(URL_GET_PATH,map);
        final OauthStringRequest stringRequest = new OauthStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Path> foundPaths = new ArrayList<>();
                try {
                    JSONArray rootJsonArray = new JSONArray(response);
                    for (int j=0;j<rootJsonArray.length();j++) {
                        ArrayList<PathInstruction> pathInstructions = new ArrayList<>();
                        JSONArray jsonArray = rootJsonArray.getJSONArray(j);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("type").equals("walk_instruction"))
                            {
                                String polyline = jsonObject.getString("polyline");
                                double distance = GeoUtils.distance(GeoUtils.getPolylineFromGoogleMapsString(polyline));
                                String destination;
                                if (jsonObject.getString("destination_type").equals("station"))
                                {
                                    String stationName = jsonObject.getString("destination");
                                    destination = "la station "+stationName;
                                }
                                else
                                {
                                    destination = "votre destination";
                                }
                                int duration = jsonObject.getInt("duration")*60;
                                int type = 0;
                                if (i>0&&i<jsonArray.length()-1)
                                    type = WalkInstruction.TYPE_TRANSFER;
                                else
                                    if (i==jsonArray.length()-1)
                                        type = WalkInstruction.TYPE_ARRIVAL;
                                WalkInstruction walkInstruction = new WalkInstruction(polyline,
                                        (float)distance
                                        ,destination,duration,type);
                                if (!GeoUtils.polylineContainsOnlyEquals(walkInstruction.getPolyline()))
                                pathInstructions.add(walkInstruction);
                            }
                            if (jsonObject.getString("type").equals("wait_instruction"))
                            {
                                Coordinate coordinate = new Gson().fromJson(jsonObject.getJSONObject("coordinate").toString(), Coordinate.class);
                                JSONArray linesArray = jsonObject.getJSONArray("lines");
                                ArrayList<WaitLine> waitLines = new ArrayList<>();
                                for (int l=0;l<linesArray.length();l++)
                                {
                                    JSONObject lineObject = linesArray.getJSONObject(l);
                                    String lineName = lineObject.getString("line_name");
                                    int transportModeId = lineObject.getInt("transport_mode_id")-1;
                                    long duration = lineObject.getInt("duration")*60;
                                    String destination = lineObject.getString("destination");
                                    boolean exactWaitingTime = lineObject.getBoolean("exact_waiting_time");
                                    boolean hasPerturbations = lineObject.getBoolean("has_perturbations");
                                    int id = lineObject.getInt("id");
                                    waitLines.add(new WaitLine(new LineSkeleton(id,
                                            lineName,
                                            TransportMean.allTransportMeans.get(transportModeId)),
                                            destination,duration,exactWaitingTime,hasPerturbations));
                                }
                                pathInstructions.add(new WaitInstruction(coordinate,waitLines));
                            }
                            if (jsonObject.getString("type").equals("ride_instruction"))
                            {
                                int duration = jsonObject.getInt("duration")*60;
                                int transportModeId = jsonObject.getInt("transport_mode_id")-1;
                                Gson gson = new Gson();
                                Type fooType = new TypeToken<ArrayList<Station>>() {
                                }.getType();
                                ArrayList<Station> stations = gson.fromJson(jsonObject.get("stations").toString(), fooType);
                                ArrayList<Section> sections = new ArrayList<>();




                                int k=0;
                                while (k<stations.size()-1)
                                {
                                    sections.add(new Section(stations.get(k),stations.get(k+1)));
                                    k++;
                                }

                                String polylineString = jsonObject.getString("polyline");
                                float errorMargin = (float)jsonObject.getDouble("error_margin");
                                RideInstruction rideInstruction = new RideInstruction(duration,
                                        transportModeId
                                        ,sections
                                        ,polylineString
                                        ,errorMargin);

                                pathInstructions.add(rideInstruction);
                            }
                        }
                        foundPaths.add(new Path(pathSettings,pathInstructions));
                    }
                    for (int i=0;i<foundPaths.size();i++)
                    {
                        for (int j=0;j<foundPaths.size();j++)
                        {
                            if (foundPaths.get(i).getDuration()<foundPaths.get(j).getDuration())
                            {
                                Path path = foundPaths.get(i);
                                foundPaths.set(i,foundPaths.get(j));
                                foundPaths.set(j,path);
                            }
                        }
                    }
                    onSearchCompleted.onResultsFound(foundPaths);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,10, (float) 1.0));
        stringRequest.performRequest(getTag());
    }

    @Override
    public String getTag() {
        return "PATH";
    }


}
