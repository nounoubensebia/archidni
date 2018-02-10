package com.archidni.archidni.Data.Lines;

import android.content.Context;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Section;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TimePeriod;
import com.archidni.archidni.Model.Transport.TrainLine;
import com.archidni.archidni.Model.Transport.TrainTrip;
import com.archidni.archidni.Model.Transport.TramwayMetroLine;
import com.archidni.archidni.Model.Transport.TramwayMetroTrip;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.TimeUtils;

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

    public void getLinesPassingByStation(Context context, final Station station,
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
                        JSONObject lineObject = data.getJSONObject(i);
                        Line line = parseLine(lineObject);
                        if (line.getTransportMean().getId() == 1) {
                            JSONArray trips = lineObject.getJSONArray("trips");
                            ArrayList<TrainTrip> trainTrips = new ArrayList<>();
                            for (int j = 0; j < trips.length(); j++) {
                                JSONObject tripObject = trips.getJSONObject(j);
                                long days = tripObject.getLong("days");
                                JSONArray departuresJsonArray = tripObject.getJSONArray("departures");
                                ArrayList<Long> departures = new ArrayList<>();
                                for (int k = 0; k < departuresJsonArray.length(); k++) {
                                    JSONObject departureObject = departuresJsonArray.getJSONObject(k);
                                    long departureTime = TimeUtils
                                            .getTimeFromString(departureObject.getString("time"));
                                    departures.add(departureTime);
                                }
                                JSONArray stationsJsonArray = tripObject.getJSONArray("stations");
                                ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
                                for (int k = 0; k < stationsJsonArray.length(); k++) {
                                    JSONObject stationObject = stationsJsonArray.getJSONObject(k);
                                    pairs.add(new Pair<Integer, Integer>(stationObject.getInt("id"),
                                            stationObject.getInt("minutes")));
                                }
                                trainTrips.add(new TrainTrip(days, pairs, departures));
                                line = new TrainLine(line.getId(), line.getName(),
                                        line.getTransportMean(), line.getSections(), trainTrips);
                            }
                        }
                        else
                        {
                            JSONArray trips = lineObject.getJSONArray("trips");
                            ArrayList<TramwayMetroTrip> tramwayMetroTrips  = new ArrayList<>();
                            for (int j = 0; j < trips.length(); j++) {
                                JSONObject tripObject = trips.getJSONObject(j);
                                long days = tripObject.getLong("days");
                                JSONArray timePeriodsJson = tripObject.getJSONArray("time_periods");
                                ArrayList<TimePeriod> timePeriods = new ArrayList<>();
                                for (int k = 0; k < timePeriodsJson.length(); k++) {
                                    JSONObject timePeriodJson = timePeriodsJson.getJSONObject(k);
                                    long start = TimeUtils.getTimeFromString(
                                            timePeriodJson.getString("start"));
                                    long end = TimeUtils.getTimeFromString(
                                            timePeriodJson.getString("end"));
                                    int waitingTime = timePeriodJson.getInt("waitingTime");
                                    timePeriods.add(new TimePeriod(start,end,waitingTime));
                                }
                                JSONArray stationsJsonArray = tripObject.getJSONArray("stations");
                                ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
                                for (int k = 0; k < stationsJsonArray.length(); k++) {
                                    JSONObject stationObject = stationsJsonArray.getJSONObject(k);
                                    pairs.add(new Pair<Integer, Integer>(stationObject.getInt("id"),
                                            stationObject.getInt("minutes")));
                                }
                                tramwayMetroTrips.add(new TramwayMetroTrip(days,pairs,timePeriods));
                            }

                            line = new TramwayMetroLine(line.getId(),line.getName(),
                                    line.getTransportMean(),line.getSections(),tramwayMetroTrips);
                        }
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
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest, "TAG");
    }

    private Line parseLine(JSONObject jsonObject) {
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
            jsonException.printStackTrace();
            return new ArrayList<>();
        }
        return lines;
    }

    public interface OnSearchCompleted {
        void onLinesFound(ArrayList<Line> lines);

        void onError();
    }
}
