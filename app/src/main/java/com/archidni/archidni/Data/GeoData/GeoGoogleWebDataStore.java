package com.archidni.archidni.Data.GeoData;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Data.OnlineDataStore;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.Places.PathPlace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GeoGoogleWebDataStore extends OnlineDataStore implements GeoDataStore {

    private static final String GOOGLE_API_KEY ="AIzaSyBUk19I8oW1d_OKmuPpzu6v5pEvTx3sBzE";
    private static final String URL_PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private static final String URL_PLACE_TYPE = "https://maps.googleapis.com/maps/api/place/details/json?";


    private void getTextAutocompleteSuggestionsFromWebApi (Context context,
                                                           final String text,
                                                           final GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete)
    {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("input",text);
        map.put("key",GOOGLE_API_KEY);
        map.put("components","country:dz");
        map.put("language","fr");
        final String requestUrl = URL_PLACES_AUTOCOMPLETE + AppSingleton.buildParametersString(map);
        cancelRequests(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<TextQuerySuggestion> textQuerySuggestions = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String placeId = jsonObject1.getString("place_id");
                                String mainText = jsonObject1.getJSONObject("structured_formatting")
                                        .getString("main_text");
                                String secondaryText = "";
                                if (jsonObject1.getJSONObject("structured_formatting").has("secondary_text"))
                                {
                                    secondaryText = jsonObject1.getJSONObject("structured_formatting")
                                            .getString("secondary_text");
                                    int type;
                                    if (jsonObject1.getJSONArray("types").getString(0).equals("establishment"))
                                    {
                                        type = TextQuerySuggestion.TYPE_BUILDING;
                                    }
                                    else
                                    {
                                        type = TextQuerySuggestion.TYPE_LOCATION;
                                    }
                                    TextQuerySuggestion textQuerySuggestion =
                                            new TextQuerySuggestion(mainText,secondaryText,type,
                                                    placeId);
                                    textQuerySuggestions.add(textQuerySuggestion);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions,text);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                onPlaceSuggestionsSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(stringRequest,getTag());
    }



    private void getPlacesDetailsFromWebApi (Context context, final TextQuerySuggestion textQuerySuggestion,
                                             final GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete)
    {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("placeid", textQuerySuggestion.getPlaceId());
        map.put("key",GOOGLE_API_KEY);
        final String requestUrl = URL_PLACE_TYPE + AppSingleton.buildParametersString(map);
        cancelRequests(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            Coordinate coordinate = new Coordinate(location.getDouble("lat"),
                                    location.getDouble("lng"));
                            onPlaceDetailsSearchComplete.onResultFound(new PathPlace(textQuerySuggestion.getMainText(),
                                    coordinate));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onPlaceDetailsSearchComplete.onError();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onPlaceDetailsSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(stringRequest,getTag());
    }

    @Override
    public String getTag() {
        return "GEO_SUGGESTIONS";
    }

    @Override
    public void getTextAutoCompleteSuggestions(Context context, String text, GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete) {
        getTextAutocompleteSuggestionsFromWebApi(context,text,onPlaceSuggestionsSearchComplete);
    }

    @Override
    public void getPlaceDetails(Context context, TextQuerySuggestion textQuerySuggestion, GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete) {
        getPlacesDetailsFromWebApi(context,textQuerySuggestion,onPlaceDetailsSearchComplete);
    }
}
