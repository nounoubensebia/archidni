package com.archidni.archidni.Data.LineStationSuggestions;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by nouno on 15/02/2018.
 */

public class LineStationDataStore {
    private static final String GET_LINE_SUGGESTIONS_URL = "/api/v1/line/autocomplete";
    private static final String GET_STATION_SUGGESTIONS_URL = "/api/v1/station/autocomplete";

    public void getLineSuggestions (Context context, String text,
                                    final LineStationDataStore.OnSearchComplete onSearchComplete)
    {
        AppSingleton.getInstance(context).getRequestQueue().cancelAll("LINE_SUGGESTIONS");
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("text",text);
        String url = SharedPrefsUtils.getServerUrl(context) +
                AppSingleton.buildGetUrl(GET_LINE_SUGGESTIONS_URL,map);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onSearchComplete.onSearchComplete(fromJson(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,"LINE_SUGGESTIONS");
    }

    public void getStationSuggestions (Context context, String text,
                                    final LineStationDataStore.OnSearchComplete onSearchComplete)
    {
        AppSingleton.getInstance(context).getRequestQueue().cancelAll("STATION_SUGGESTIONS");
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("text",text);
        String url = SharedPrefsUtils.getServerUrl(context)+
                AppSingleton.buildGetUrl(GET_STATION_SUGGESTIONS_URL,map);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onSearchComplete.onSearchComplete(fromJson(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(context).addToRequestQueue(stringRequest,"STATION_SUGGESTIONS");
    }

    private ArrayList<LineStationSuggestion> fromJson (String response)
    {
        ArrayList<LineStationSuggestion> lineStationSuggestions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int transportMeanId = jsonObject.getInt("transport_mode_id")-1;
                LineStationSuggestion lineStationSuggestion = new LineStationSuggestion(name,id,
                        transportMeanId);
                lineStationSuggestions.add(lineStationSuggestion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lineStationSuggestions;
    }

    public interface OnSearchComplete {
        void onSearchComplete (ArrayList<LineStationSuggestion> lineStationSuggestions);
        void onError ();
    }
}
