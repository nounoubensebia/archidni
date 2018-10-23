package com.archidni.archidni.Data.GeoData;

import android.content.Context;
import android.support.annotation.NonNull;

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
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
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

public interface GeoRepository  {

    public void getTextAutoCompleteSuggestions(Context context,
                                               String text,
                                               final OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete);


    public void getPlaceDetails (Context context, final TextQuerySuggestion textQuerySuggestion,
                                 final GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete);

    public interface OnPlaceSuggestionsSearchComplete {
        public void onResultsFound (ArrayList<TextQuerySuggestion> textQuerySuggestions,String query);
        public void onError ();
    }

    public void cancelRequests(Context context);

    public interface OnPlaceDetailsSearchComplete {
        public void onResultFound(PathPlace place);
        public void onError();
    }
}
