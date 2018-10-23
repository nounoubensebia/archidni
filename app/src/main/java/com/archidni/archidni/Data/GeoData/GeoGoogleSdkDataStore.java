package com.archidni.archidni.Data.GeoData;

import android.content.Context;
import android.support.annotation.NonNull;

import com.archidni.archidni.App;
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

import java.util.ArrayList;

public class GeoGoogleSdkDataStore extends OnlineDataStore {

    private GeoDataClient geoDataClient;

    public GeoGoogleSdkDataStore() {
        geoDataClient = Places.getGeoDataClient(App.getAppContext(), null);
    }


    public void getTextAutoCompleteSuggestions(Context context,
                                                           String text,
                                                           final GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete)
    {
        getTextAutocompleteSuggestionsFromAndroidSdk(context,text,onPlaceSuggestionsSearchComplete);
    }

    public void getPlaceDetails (Context context, final TextQuerySuggestion textQuerySuggestion,
                                 final GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete)
    {
        getPlaceDetailsFromAndroidSdk(context,textQuerySuggestion,onPlaceDetailsSearchComplete);
    }

    private void getTextAutocompleteSuggestionsFromAndroidSdk(Context context, final String text,
                                                              final GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete)
    {
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
                autocompletePredictions.release();
            }
        });
        results.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onPlaceSuggestionsSearchComplete.onError();
            }
        });

    }

    private void getPlaceDetailsFromAndroidSdk (Context context, final TextQuerySuggestion textQuerySuggestion,
                                                final GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete)
    {
        geoDataClient.getPlaceById(textQuerySuggestion.getPlaceId()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful())
                {
                    PlaceBufferResponse places = task.getResult();
                    Place place = places.get(0);
                    Coordinate coordinate = new Coordinate(place.getLatLng().latitude,place.getLatLng().longitude);
                    onPlaceDetailsSearchComplete.onResultFound(new PathPlace(textQuerySuggestion.getMainText(),
                            coordinate));
                    places.release();
                }
                else
                {
                    onPlaceDetailsSearchComplete.onError();
                }
            }
        });
    }


    @Override
    public String getTag() {
        return "GEO_SUGGESTIONS";
    }
}
