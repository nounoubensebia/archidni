package com.archidni.archidni.Data.Places;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.archidni.archidni.App;
import com.archidni.archidni.Data.LinesAndPlaces.PlaceParser;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.Data.Station.StationParser;
import com.archidni.archidni.Model.Places.MainActivityPlace;
import com.archidni.archidni.Model.StringUtils;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.OauthStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesDataStoreImpl implements PlacesDataStore {


    private static final String PLACES_URI = "/api/v1/places/";

    @Override
    public void getNearbyPlaces(MainActivityPlace place, final PlacesRepository.OnNearbyPlacesFound onNearbyPlacesFound) {
        int id = place.getId();
        String url = String.format("%s%s%s/%s", SharedPrefsUtils.getServerUrl(App.getAppContext()),PLACES_URI,
                id+"",
                "nearby-places");
        OauthStringRequest oauthStringRequest = new OauthStringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    ArrayList<MainActivityPlace> places = new ArrayList<>();
                    JSONArray stationsArray = root.getJSONArray("stations");
                    for (int i = 0; i < stationsArray.length(); i++) {
                        StationParser stationParser = new StationParser();
                        Station station1 = stationParser.parseStation(stationsArray.getJSONObject(i));
                        places.add(station1);
                    }
                    JSONArray placesArray = root.getJSONArray("places");
                    places.addAll(new PlaceParser(placesArray.toString()).getPlaces());
                    onNearbyPlacesFound.onNearbyPlacesFound(places);
                } catch (JSONException jsonException)
                {
                    onNearbyPlacesFound.onError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onNearbyPlacesFound.onError();
            }
        });
        oauthStringRequest.performRequest("TAG");
    }
}
