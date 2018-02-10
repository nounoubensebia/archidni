package com.archidni.archidni.Data.Lines;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TrainLine;
import com.archidni.archidni.Model.TransportMean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by noure on 07/02/2018.
 */

public class LinesOnlineDataStore {

    private static final String GET_LINES_URL = "http://192.168.1.12:8000/api/v1/lines";

    private static final String GET_STATIONS_URL = "http://192.168.1.12:8000/api/v1/station";

    public void getLines(Context context, final Coordinate position,
                         final OnSearchCompleted onSearchCompleted) {
        LinkedHashMap map = new LinkedHashMap();
        String positionString = position.getLatitude() + "," + position.getLongitude();
        map.put("position", positionString);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                AppSingleton.buildGetUrl(GET_LINES_URL, map),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject root = new JSONObject(response);
                            JSONArray data = root.getJSONArray("data");
                            ArrayList<Line> lines = parseLines(data);
                            onSearchCompleted.onLinesFound(lines);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onSearchCompleted.onLinesFound(new ArrayList<Line>());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchCompleted.onError();
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest, "TAG");
    }

    public void getLinesPassingByStation(Context context,Station station,
                                         final OnSearchCompleted onSearchCompleted) {
        String url = GET_STATIONS_URL + "/" + station.getId() + "/lines";
        final ArrayList<Line> lines = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject1 = data.getJSONObject(i);
                        Line line = parseLine(jsonObject1);
                        lines.add(line);
                    }
                    onSearchCompleted.onLinesFound(lines);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onSearchCompleted.onLinesFound(new ArrayList<Line>());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchCompleted.onError();
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,"TAG");
    }

    private Line parseLine (JSONObject jsonObject)
    {
        try {

            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            JSONArray sectionsArray = jsonObject.getJSONArray("sections");
            ArrayList<Section> sections = new ArrayList<>();
            for (int j = 0; j < sectionsArray.length(); j++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(j);
                JSONObject originObject = sectionObject.getJSONObject("origin");
                JSONObject destinationObject = sectionObject.getJSONObject("destination");
                Station origin = new Station(originObject.getInt("id"),
                        originObject.getString("name"),
                        TransportMean.allTransportMeans.get(originObject
                                .getInt("transport_mode_id") - 1), new Coordinate(
                        originObject.getDouble("latitude"),
                        originObject.getDouble("longitude")));
                Station destination = new Station(destinationObject.getInt("id"),
                        destinationObject.getString("name"),
                        TransportMean.allTransportMeans.get(destinationObject
                                .getInt("transport_mode_id") - 1), new Coordinate(
                        destinationObject.getDouble("latitude"),
                        destinationObject.getDouble("longitude")));
                Section section = new Section(origin, destination);
                sections.add(section);
            }
            TransportMean transportMean = TransportMean.allTransportMeans.get(
                    jsonObject.getInt("transport_mode_id") - 1
            );
            return new Line(id, name, transportMean, sections);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Line> parseLines(JSONArray data) {
        ArrayList<Line> lines = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                lines.add(parseLine(jsonObject));
            }
        } catch (JSONException jsonException) {
            return new ArrayList<>();
        }
        return lines;
    }

    public interface OnSearchCompleted {
        void onLinesFound(ArrayList<Line> lines);

        void onError();
    }
}
