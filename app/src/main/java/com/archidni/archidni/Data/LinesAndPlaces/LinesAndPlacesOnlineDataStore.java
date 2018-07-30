package com.archidni.archidni.Data.LinesAndPlaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Notifications.Notification;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.Places.Parking;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.LineSection;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TimePeriod;
import com.archidni.archidni.Model.Transport.TrainLine;
import com.archidni.archidni.Model.Transport.TrainTrip;
import com.archidni.archidni.Model.Transport.TramwayMetroLine;
import com.archidni.archidni.Model.Transport.TramwayMetroTrip;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.OauthStringRequest;
import com.archidni.archidni.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by noure on 07/02/2018.
 */

public class LinesAndPlacesOnlineDataStore extends OnlineDataStore {


    private static final String GET_LINES_URL = "/api/v1/line";
    private static final String GET_STATIONS_URL = "/api/v1/station";
    private static final String GET_LINES_AND_PLACES_URL = "/api/v1/linesAndPlaces";


    public void getLinesAndPlaces(Context context,
                                  final OnLinesAndPlacesSearchCompleted onLinesAndPlacesSearchCompleted) {
        cancelRequests(context);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET,
                SharedPrefsUtils.getServerUrl(context) + GET_LINES_AND_PLACES_URL, new Response.Listener<String>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(String response) {
                AsyncTask<String, Void, Pair<ArrayList<Line>, ArrayList<MainActivityPlace>>> asyncTask;
                asyncTask = new AsyncTask<String, Void, Pair<ArrayList<Line>, ArrayList<MainActivityPlace>>>() {
                    @Override
                    protected Pair<ArrayList<Line>, ArrayList<MainActivityPlace>> doInBackground(String... strings) {
                        try {
                            JSONObject root = new JSONObject(strings[0]);
                            JSONArray data = root.getJSONArray("lines");
                            ArrayList<Line> lines = parseLines(data);
                            ArrayList<MainActivityPlace> places = new ArrayList<>();
                            ArrayList<Parking> parkings = parseParkings(root.getJSONArray("parkings"));
                            places.addAll(parkings);
                            return new Pair<>(lines, places);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Pair<ArrayList<Line>, ArrayList<MainActivityPlace>> arrayListArrayListPair) {
                        if (arrayListArrayListPair != null)
                            onLinesAndPlacesSearchCompleted.onLinesAndPlacesFound(
                                    arrayListArrayListPair.first
                                    , arrayListArrayListPair.second);
                        else
                            onLinesAndPlacesSearchCompleted.onError();
                    }
                };
                asyncTask.execute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLinesAndPlacesSearchCompleted.onError();
            }
        });
        oauthStringRequest.performRequest(getTag());
    }

    public ArrayList<Parking> parseParkings(JSONArray parkingsJson) {
        ArrayList<Parking> parkings = new ArrayList<>();
        for (int i = 0; i < parkingsJson.length(); i++) {
            try {
                JSONObject parkingObject = parkingsJson.getJSONObject(i);
                String name = parkingObject.getString("name");
                double latitude = parkingObject.getDouble("latitude");
                double longitude = parkingObject.getDouble("longitude");
                int capacity = parkingObject.getInt("capacity");
                int id = parkingObject.getInt("id");
                parkings.add(new Parking(name, new Coordinate(latitude, longitude), id, capacity));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parkings;
    }


    public void getLine(Context context, LineStationSuggestion lineStationSuggestion,
                        final OnLineSearchCompleted onLineSearchCompleted) {
        cancelRequests(context);
        String url = SharedPrefsUtils.getServerUrl(context) +
                GET_LINES_URL + "/" + lineStationSuggestion.getId();
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            onLineSearchCompleted.onLineFound(parseLine(jsonObject.getJSONObject("data")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onLineSearchCompleted.onError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLineSearchCompleted.onError();
            }
        });
        oauthStringRequest.performRequest(getTag());
    }

    public void getLinesPassingByStation(Context context, final Station station,
                                         final OnLinesSearchCompleted onLinesSearchCompleted) {
        cancelRequests(context);
        String url = SharedPrefsUtils.getServerUrl(context) + GET_STATIONS_URL + "/" + station.getId()
                + "/lines";
        final ArrayList<Line> lines = new ArrayList<>();


        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
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
                                        line.getTransportMean(), line.getLineSections(), trainTrips);
                            }
                        } else {
                            JSONArray trips = lineObject.getJSONArray("trips");
                            ArrayList<TramwayMetroTrip> tramwayMetroTrips = new ArrayList<>();
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
                                    timePeriods.add(new TimePeriod(start, end, waitingTime));
                                }
                                JSONArray stationsJsonArray = tripObject.getJSONArray("stations");
                                ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
                                for (int k = 0; k < stationsJsonArray.length(); k++) {
                                    JSONObject stationObject = stationsJsonArray.getJSONObject(k);
                                    pairs.add(new Pair<Integer, Integer>(stationObject.getInt("id"),
                                            stationObject.getInt("minutes")));
                                }
                                tramwayMetroTrips.add(new TramwayMetroTrip(days, pairs, timePeriods));
                            }

                            line = new TramwayMetroLine(line.getId(), line.getName(),
                                    line.getTransportMean(), line.getLineSections(), tramwayMetroTrips);
                        }
                        lines.add(line);
                    }
                    onLinesSearchCompleted.onLinesFound(lines);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLinesSearchCompleted.onLinesFound(new ArrayList<Line>());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLinesSearchCompleted.onError();
            }
        });
        oauthStringRequest.performRequest(getTag());
    }

    private Line parseLine(JSONObject jsonObject) {
        try {
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            JSONArray sectionsArray = jsonObject.getJSONArray("sections");
            ArrayList<LineSection> lineSections = new ArrayList<>();
            for (int j = 0; j < sectionsArray.length(); j++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(j);
                JSONObject originObject = sectionObject.getJSONObject("origin");
                JSONObject destinationObject = sectionObject.getJSONObject("destination");
                Station origin = new Station(originObject.getInt("id"),
                        originObject.getString("name"),
                        originObject.getInt("transport_mode_id") - 1,
                        new Coordinate(originObject.getDouble("latitude"),
                                originObject.getDouble("longitude")));
                Station destination = new Station(destinationObject.getInt("id"),
                        destinationObject.getString("name"),
                        destinationObject.getInt("transport_mode_id") - 1,
                        new Coordinate(destinationObject.getDouble("latitude"),
                                destinationObject.getDouble("longitude")));
                int order = sectionObject.getInt("order");
                int mode = sectionObject.getInt("mode");
                String polylineString = sectionObject.getString("polyline");
                LineSection lineSection = new LineSection(origin, destination, polylineString, mode, order);
                lineSections.add(lineSection);
            }
            TransportMean transportMean = TransportMean.allTransportMeans.get(
                    jsonObject.getInt("transport_mode_id") - 1
            );
            Line.Builder builder = new Line.Builder(id, name);
            builder.setLineSections(lineSections);
            builder.setTransportMean(transportMean);
            return builder.build();
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

    public void getNotifications (Line line, final LinesAndPlacesRepository.OnNotificationsFound onNotificationsFound)
    {
        String url = SharedPrefsUtils.getServerUrl(App.getAppContext()) + GET_LINES_URL + "/"+line.getId()+"/notifications";
        OauthStringRequest stringRequest = new OauthStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Notification> notifications = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Notification notification = Notification.fromJson(jsonObject.toString());
                        notifications.add(notification);
                    }
                    onNotificationsFound.onNotificationsFound(notifications);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onNotificationsFound.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onNotificationsFound.onError();
            }
        });
        stringRequest.performRequest(getTag());
    }

    @Override
    public String getTag() {
        return "LINE";
    }


    public interface OnLinesAndPlacesSearchCompleted {
        void onLinesAndPlacesFound(ArrayList<Line> lines, ArrayList<MainActivityPlace> places);

        void onError();
    }

    public interface OnLinesSearchCompleted {
        void onLinesFound(ArrayList<Line> lines);

        void onError();
    }

    public interface OnLineSearchCompleted {
        void onLineFound(Line line);

        void onError();
    }
}
