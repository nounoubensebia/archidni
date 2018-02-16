package com.archidni.archidni.Data.Paths;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.GeoUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathInstruction;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.Path.RideInstruction;
import com.archidni.archidni.Model.Path.WaitInstruction;
import com.archidni.archidni.Model.Path.WalkInstruction;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.TimeUtils;
import com.google.gson.Gson;
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

public class PathOnlineDataStore {

    private static final String URL_GET_PATH = "http://192.168.1.11/awiyi-web-yasser/PathFinder/get_path.php";


    public void getPaths (final PathSettings pathSettings, final OnSearchCompleted onSearchCompleted) {
        String url;
        final Place departure = pathSettings.getOrigin();
        final Place arrival = pathSettings.getDestination();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("lat1",departure.getCoordinate().getLatitude()+"");
        map.put("lon1",departure.getCoordinate().getLongitude()+"");
        map.put("lat2",arrival.getCoordinate().getLatitude()+"");
        map.put("lon2",arrival.getCoordinate().getLongitude()+"");
        map.put("time",pathSettings.getDepartureTime()+"");
        map.put("day", TimeUtils.getDayFromTimeStamp(pathSettings.getDepartureDate())+"");
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
        /*
        if (bannedTransportMeans.size()>0)
        {
            map.put("transportMeanToBan1",bannedTransportMeans.get(0).getId()+"");
        }
        if (bannedTransportMeans.size()>1)
        {
            map.put("transportMeanToBan2",bannedTransportMeans.get(1).getId()+"");
        }
        if (bannedTransportMeans.size()>2)
        {
            map.put("transportMeanToBan3",bannedTransportMeans.get(2).getId()+"");
        }*/
        url = AppSingleton.buildGetUrl(URL_GET_PATH,map);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                            if (jsonObject.has("polyline")) {
                                Gson gson = new Gson();
                                Type fooType = new TypeToken<ArrayList<Coordinate>>() {
                                }.getType();
                                ArrayList<Coordinate> polyline = gson.fromJson(jsonObject.get("polyline").toString(), fooType);
                                //Coordinate coordinate1 = polyline.get(0);
                                double distance = GeoUtils.distance(polyline.get(0), polyline.get(polyline.size() - 1));
                                int duration = (int) GeoUtils.getOnFootDuration(distance);
                                String destination = jsonObject.getString("destination");
                                WalkInstruction walkInstruction = new WalkInstruction(duration, (float) distance, polyline, destination);
                                pathInstructions.add(walkInstruction);

                            }
                            if (jsonObject.has("coordinate")) {

                                Coordinate coordinate = new Gson().fromJson(jsonObject.getJSONObject("coordinate").toString(), Coordinate.class);
                                WaitInstruction waitInstruction = new WaitInstruction((int) jsonObject.getDouble("duration"), coordinate);
                                pathInstructions.add(waitInstruction);
                            }
                            if (jsonObject.has("lineLabel")) {
                                String json = jsonObject.toString();
                                RideInstruction rideInstruction = new Gson().fromJson(json, RideInstruction.class);
                                TransportMean transportMean = rideInstruction.getTransportMean();
                                TransportMean newTransportMean = new TransportMean(0,"name");
                                if (transportMean.getId()==0)
                                    newTransportMean = TransportMean.allTransportMeans.get(2);
                                if (transportMean.getId()==1)
                                    newTransportMean = TransportMean.allTransportMeans.get(0);
                                if (transportMean.getId()==2)
                                    newTransportMean = TransportMean.allTransportMeans.get(3);
                                if (transportMean.getId()==3)
                                    newTransportMean = TransportMean.allTransportMeans.get(1);
                                rideInstruction.setTransportMeanId(newTransportMean.getId());
                                pathInstructions.add(rideInstruction);
                            }
                        }
                        foundPaths.add(new Path(departure,arrival,pathSettings,pathInstructions));
                    }
                    onSearchCompleted.onResultsFound(foundPaths);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onSearchCompleted.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchCompleted.onError();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,10, (float) 1.0));

        AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(stringRequest,"TAG_GET_PATH");
    }

    public interface OnSearchCompleted {
        void onResultsFound (ArrayList<Path> paths);
        void onError();
    }
}