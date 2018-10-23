package com.archidni.archidni.Data.GeoData;

import android.content.Context;
import android.util.Log;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;

public class GeoRepositoryImpl implements GeoRepository {

    private GeoGoogleSdkDataStore geoGoogleSdkDataStore;
    private GeoCachedDataStore cachingDataStore;

    @Override
    public void getTextAutoCompleteSuggestions(final Context context, final String text, final OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete) {
        getGeoCachedDataStore().getTextAutoCompleteSuggestions(context, text, new OnPlaceSuggestionsSearchComplete() {
            @Override
            public void onResultsFound(ArrayList<TextQuerySuggestion> textQuerySuggestions, String query) {
                onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions,query);
            }

            @Override
            public void onError() {
                getGeoGoogleSdkDataStore().getTextAutoCompleteSuggestions(context, text, new OnPlaceSuggestionsSearchComplete() {
                    @Override
                    public void onResultsFound(ArrayList<TextQuerySuggestion> textQuerySuggestions, String query) {
                        getGeoCachedDataStore().saveTextAutocompleteSuggestions(query,textQuerySuggestions);
                        Log.d("SEARCH REQUEST","request for "+query);
                        onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions,query);
                    }

                    @Override
                    public void onError() {
                        onPlaceSuggestionsSearchComplete.onError();
                    }
                });
            }
        });
    }

    @Override
    public void getPlaceDetails(final Context context, final TextQuerySuggestion textQuerySuggestion, final OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete) {
        getGeoCachedDataStore().getPlaceDetails(context, textQuerySuggestion, new OnPlaceDetailsSearchComplete() {
            @Override
            public void onResultFound(PathPlace place) {
                onPlaceDetailsSearchComplete.onResultFound(place);
            }

            @Override
            public void onError() {
                getGeoGoogleSdkDataStore().getPlaceDetails(context, textQuerySuggestion,
                        new OnPlaceDetailsSearchComplete() {
                            @Override
                            public void onResultFound(PathPlace place) {
                                getGeoCachedDataStore().savePlaceDetailsSuggestions(textQuerySuggestion,place);
                                onPlaceDetailsSearchComplete.onResultFound(place);
                            }

                            @Override
                            public void onError() {
                                onPlaceDetailsSearchComplete.onError();
                            }
                        });
            }
        });
    }

    public void cancelRequests (Context context)
    {
        getGeoGoogleSdkDataStore().cancelRequests(context);
    }

    private GeoGoogleSdkDataStore getGeoGoogleSdkDataStore()
    {
        if (geoGoogleSdkDataStore==null)
        {
            geoGoogleSdkDataStore = new GeoGoogleSdkDataStore();
        }
        return geoGoogleSdkDataStore;
    }

    private GeoCachedDataStore getGeoCachedDataStore()
    {
        if (cachingDataStore==null)
        {
            cachingDataStore = new GeoCachedDataStore();
        }
        return cachingDataStore;
    }

}
