package com.archidni.archidni.Data.Station;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Station;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nouno on 15/02/2018.
 */

public class StationDataStore {
    private static final String GET_LINE_SUGGESTIONS_URL = "http://192.168.1.7:8000/api/v1/station";

    public void getStation (Context context, LineStationSuggestion lineStationSuggestion,
                            final OnSearchComplete onSearchComplete)
    {
        String url = GET_LINE_SUGGESTIONS_URL+"/"+lineStationSuggestion.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int id = data.getInt("id");
                    String name = data.getString("name");
                    int transportMeanId = data.getInt("transport_mode_id")-1;
                    double latitude = data.getDouble("latitude");
                    double longitude = data.getDouble("longitude");
                    Station station = new Station(id,name,transportMeanId,new Coordinate(latitude,
                            longitude));
                    onSearchComplete.onSearchComplete(station);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onSearchComplete.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,"TAG");
    }

    public interface OnSearchComplete {
        public void onSearchComplete (Station station);
        public void onError ();
    }
}
