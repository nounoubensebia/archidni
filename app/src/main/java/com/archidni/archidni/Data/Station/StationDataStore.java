package com.archidni.archidni.Data.Station;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.OauthStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nouno on 15/02/2018.
 */

public class StationDataStore extends OnlineDataStore {
    private static final String GET_LINE_SUGGESTIONS_URL = "/api/v1/station";


    public void getStation (Context context, LineStationSuggestion lineStationSuggestion,
                            final OnSearchComplete onSearchComplete)
    {
        cancelRequests(context);
        String url = SharedPrefsUtils.getServerUrl(context) +
                GET_LINE_SUGGESTIONS_URL + "/" + lineStationSuggestion.getId();
        OauthStringRequest stringRequest = new OauthStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
        stringRequest.performRequest(getTag());
    }

    public void getTransfers (Context context, Station station,
                              StationDataRepository.OnTransferSearchCompleted onTransferSearchCompleted)
    {

    }

    @Override
    public String getTag() {
        return "STATIONS";
    }

    public interface OnSearchComplete {
        public void onSearchComplete (Station station);
        public void onError ();
    }
}
