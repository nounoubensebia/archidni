package com.archidni.archidni.Data.GeoData;

import android.content.Context;

import com.archidni.archidni.Data.GeoData.AutocompleteCacher.AutocompleteCacheManager;
import com.archidni.archidni.Data.GeoData.PlaceDetailsCacher.PlaceDetailsCacheManager;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.Model.Places.PathPlace;

import java.util.ArrayList;

public class GeoCachedDataStore implements GeoDataStore {



    @Override
    public void getTextAutoCompleteSuggestions(Context context, String text, GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete) {
        AutocompleteCacheManager geoAutocompleteCacheManager = new AutocompleteCacheManager();
        ArrayList<TextQuerySuggestion> textQuerySuggestions = geoAutocompleteCacheManager.get(text);
        if (textQuerySuggestions != null)
        {
            onPlaceSuggestionsSearchComplete.onResultsFound(textQuerySuggestions,text);
        }
        else
        {
            onPlaceSuggestionsSearchComplete.onError();
        }
    }

    @Override
    public void getPlaceDetails(Context context, TextQuerySuggestion textQuerySuggestion, GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete) {
        PlaceDetailsCacheManager placeDetailsCacheEntry = new PlaceDetailsCacheManager();
        PathPlace pathPlace = placeDetailsCacheEntry.get(textQuerySuggestion.getPlaceId());
        if (pathPlace!=null)
        {
            onPlaceDetailsSearchComplete.onResultFound(pathPlace);
        }
        else
        {
            onPlaceDetailsSearchComplete.onError();
        }
    }

    public void saveTextAutocompleteSuggestions (String text,ArrayList<TextQuerySuggestion> textQuerySuggestions)
    {
        AutocompleteCacheManager geoAutocompleteCacheManager = new AutocompleteCacheManager();
        geoAutocompleteCacheManager.set(text,textQuerySuggestions);
    }

    public void savePlaceDetailsSuggestions (TextQuerySuggestion textQuerySuggestion,PathPlace pathPlace)
    {
        PlaceDetailsCacheManager placeDetailsCacheEntry = new PlaceDetailsCacheManager();
        placeDetailsCacheEntry.set(textQuerySuggestion.getPlaceId(),pathPlace);
    }
}
