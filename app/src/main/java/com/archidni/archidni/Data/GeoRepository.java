package com.archidni.archidni.Data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.archidni.archidni.App;
import com.archidni.archidni.AppSingleton;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.Places.PathPlace;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by noure on 02/02/2018.
 */

public class GeoRepository extends OnlineDataStore {
    private static final String GOOGLE_API_KEY ="AIzaSyBUk19I8oW1d_OKmuPpzu6v5pEvTx3sBzE";
    private static final String URL_PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    private static final String URL_PLACE_TYPE = "https://maps.googleapis.com/maps/api/place/details/json?";

    public void getTextAutoCompleteSuggestions(Context context,
                                               String text,
                                               final OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete)
    {
        /*LinkedHashMap<String,String> map = new LinkedHashMap<>();
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
                onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                onPlaceSuggestionsSearchComplete.onError();
            }
        });
        AppSingleton.getInstance(App.getAppContext()).addToRequestQueue(stringRequest,getTag());*/
        getQuery(context,text,onPlaceSuggestionsSearchComplete);
    };

    private void getQuery(Context context, final String text, final OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete)
    {
        GeoDataClient geoDataClient = Places.getGeoDataClient(context, null);
        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(18.76921389474957,-5.2066028499999675),
                new LatLng(34.99235879854858,13.997498712500033));
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setCountry("DZ").setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES|
        AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build();
        Task<AutocompletePredictionBufferResponse> results = geoDataClient.getAutocompletePredictions(
                text,
                latLngBounds,
                autocompleteFilter
        );
        results.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {
            @Override
            public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {
                ArrayList<TextQuerySuggestion> textQuerySuggestions = new ArrayList<>();
                for (AutocompletePrediction autocompletePrediction:autocompletePredictions)
                {
                    String mainText = autocompletePrediction.getPrimaryText(null).toString();
                    String secondaryText = autocompletePrediction.getSecondaryText(null).toString();
                    if (secondaryText!=null && !secondaryText.equals(""))
                        {
                        int type;
                        if (autocompletePrediction.getPlaceTypes().get(0)== Place.TYPE_ESTABLISHMENT)
                        {
                            type = TextQuerySuggestion.TYPE_BUILDING;
                        }
                        else
                        {
                            type = TextQuerySuggestion.TYPE_LOCATION;
                        }
                        String placeId = autocompletePrediction.getPlaceId();
                        TextQuerySuggestion textQuerySuggestion = new TextQuerySuggestion(mainText,
                                secondaryText,type,placeId);
                        textQuerySuggestions.add(textQuerySuggestion);
                    }
                }
                onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions,text);
            }
        });
        results.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onPlaceSuggestionsSearchComplete.onError();
            }
        });

    }

    public void getPlaceDetails (Context context, final TextQuerySuggestion textQuerySuggestion,
                                 final OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete)
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

    public interface OnPlaceSuggestionsSearchComplete {
        public void onResultsFound (ArrayList<TextQuerySuggestion> textQuerySuggestions,String query);
        public void onError ();
    }

    public interface OnPlaceDetailsSearchComplete {
        public void onResultFound(PathPlace place);
        public void onError();
    }
}
