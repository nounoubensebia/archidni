package com.archidni.archidni.Data.UserSuggestions;

import android.content.Context;

import com.archidni.archidni.Model.PlaceSuggestion.CommonPlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;

import java.util.ArrayList;

/**
 * Created by noure on 03/02/2018.
 */

public class CommonSuggestionsRepository {
    CommonSuggestionsDataStore commonSuggestionsDataStore;

    public ArrayList<CommonPlaceSuggestion> getCommonSuggestions (Context context)
    {
        if (commonSuggestionsDataStore ==null)
        {
            commonSuggestionsDataStore = new CommonSuggestionsDataStore(context);
        }
        return commonSuggestionsDataStore.getCommonSuggestions();
    }

    public void addSuggestion (CommonPlaceSuggestion commonPlaceSuggestion,Context context)
    {
        if (commonSuggestionsDataStore ==null)
        {
            commonSuggestionsDataStore = new CommonSuggestionsDataStore(context);
        }
        commonSuggestionsDataStore.addCommonSuggestion(commonPlaceSuggestion,context);
    }
}
