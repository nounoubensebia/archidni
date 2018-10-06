package com.archidni.archidni.Data.RealtimeBus;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.archidni.archidni.App;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.OauthStringRequest;

import org.json.JSONException;

public class BusDataStoreImpl implements BusDataStore {

    private static final String uri = "/api/v1/buses";

    @Override
    public void getBuses(final BusRepository.OnBusesFound onBusesFound) {
        String url = String.format("%s%s",SharedPrefsUtils.getServerUrl(App.getAppContext()),uri);
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BusParser busParser = new BusParser(response);
                        try {
                            onBusesFound.onBusesFound(busParser.parseBuses());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onBusesFound.onError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onBusesFound.onError();
            }
        });
        oauthStringRequest.performRequest("BUS_REALTIME");
    }
}
