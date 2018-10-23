package com.archidni.archidni.Data.GeoData;

import android.content.Context;

import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;

public interface GeoDataStore {
    public void getTextAutoCompleteSuggestions(Context context,
                                               String text,
                                               final GeoRepository.OnPlaceSuggestionsSearchComplete onPlaceSuggestionsSearchComplete);
    public void getPlaceDetails (Context context, final TextQuerySuggestion textQuerySuggestion,
                                 final GeoRepository.OnPlaceDetailsSearchComplete onPlaceDetailsSearchComplete);
}
